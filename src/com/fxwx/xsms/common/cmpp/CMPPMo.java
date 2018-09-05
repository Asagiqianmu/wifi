/**   
 * @Title: CMPPMo.java 
 * @Package com.sxit.alarm.cmpp 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2013-11-28 下午09:17:44 
 * @version V1.0   
 */
package com.fxwx.xsms.common.cmpp;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fxwx.xsms.common.cmpp.model.CMPPActive;
import com.fxwx.xsms.common.cmpp.model.CMPPDeliver;
import com.fxwx.xsms.common.cmpp.model.CMPPReport;
import com.fxwx.xsms.common.cmpp.model.CommandID;



/**
 * @ClassName: CMPPMo
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author A18ccms a18ccms_gmail_com
 * @date 2013-11-28 下午09:17:44
 * 
 */
public class CMPPMo extends Thread {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(CMPPMo.class);

	/**
	 * socket连接
	 */
	private static CMPPSocket socket;

	/**
	 * cmpp调用
	 */
	private static CMPPService cmpp;

	private static String host = "211.139.144.201";

	private static int port = 7890;

	private static String spid = "450280";

	private static String password = "NPB7Jn";

	private static int delayTime = 30;

	public CMPPMo(String host, String spid, String password) {
		this.host = host;
		this.spid = spid;
		this.password = password;
	}

	// 以上参数也可以从配置文件处取得

	public void run() {
		socket = new CMPPSocket(host, port);
		// Thread thread = null;
		byte[] resppacket = null;
		int status = -1;
		// 初始化socket连接
		while (true) {
			try {
				socket.initialSock();
				// 将socket连接注册到CMPP api中
				cmpp = new CMPPService(socket, delayTime);
				// 建立和网关的connect连接
				status = cmpp.cmppConnect(spid, password);
				log.info("the resoponse of connect to ismg:" + status);
				if (status == 0) {
					log.info("connect successfully!\n");
					break;
				} else {
					log.warn("connect failer,result is:" + status
							+ "，reconnect!\n");
				}
			} catch (Exception e) {
				try {
					socket.closeSock();
					log.error("connect exception!sleep 5 seconds");
					Thread.sleep(5000L);
				} catch (Exception ee) {
					log.error("connect close exception:" + ee.toString());
				}
				log.error(e.toString());
			}
		}

		// 正式接收网关发送过来的
		int count = 0;
		long beginTime = System.currentTimeMillis();
		int commandID = CommandID.CMPP_ACTIVE_TEST;
		while (true) {
			// 有数据就读出来，没有数据的话，等待50毫秒
			long now = System.currentTimeMillis();
			try {
				int available = socket.getInputStream().available();

				if (available > 0) {
					beginTime = System.currentTimeMillis();
					System.out.println("\n");
					resppacket = socket.read(); // 得到输入流字节形式
					if (log.isDebugEnabled()) {
						log.debug("the response body length is："
								+ resppacket.length);
					}
					byte[] commandid = new byte[4];
					System.arraycopy(resppacket, 4, commandid, 0, 4);
					commandID = Common.bytes4ToInt(commandid);
					switch (commandID) {
					case CommandID.CMPP_DELIVER:
						log.info("deliver message");
						deliver(resppacket);
						break;
					case CommandID.CMPP_ACTIVE_TEST:
						log.info("cmppactive message");
						active(resppacket);
						break;
					case CommandID.CMPP_ACTIVE_TEST_RESP:
						log.info("cmppactive resp message");
						break;
					case CommandID.CMPP_DELIVER_RESP:
						log.info("unresonable message");
						break;
					case CommandID.CMPP_SUBMIT_RESP:
						log.info("submitresp");
						break;
					default:
						log.error("wrong commandid:" + commandID);
						break;
					}
				}
				// 如果1分钟之内没有输入流到达，则发送active包
				else if (commandID != CommandID.CMPP_ACTIVE_TEST
						&& now - beginTime >= 10 * 1000) {
					beginTime = now;
					log.info("send the active packet!");
					int active = cmpp.cmppActiveTestNoResp();
					log.info("the result of sending active packet"
							+ (active == 0 ? "success" : "failer,active="
									+ active));
				} else {
					// 休眠50秒，等待下一个deliver的到来
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						log.error(e.toString());
					}
				}
			}
			// 如果异常的引起原因是连接关掉了等，则重新建立起连接
			catch (IOException e) {
				log.error(e.toString());
				try {
					socket.closeSock();// 关闭连接后在建立连接
					socket.initialSock();
				} catch (IOException ee) {
					log.error(ee.toString());
				}
			}
		}

	}

	private static void deliver(byte[] resppacket) throws IOException {
		// 可以在此定义一private的方法，该方法可以为一处理的线程
		// 对的进行处理
		CMPPDeliver deliver = new CMPPDeliver();
		deliver.parseResponseBody(resppacket);

		// 发送deliverresp
		//cmpp.cmppDeliverResp(deliver);
		log.info("deliverresp sending successfully");

		System.out.println("            ID:"
				+ Common.bytes8ToLong(deliver.msg_Id));
		System.out.println("          dest:" + deliver.dest_Id);
		System.out.println("           msg:" + bytes2str(deliver.msg_Content));
		System.out.println("        format:" + deliver.msg_Fmt);
		System.out.println("deliver length:" + deliver.msg_Length);
		System.out.println("      isreport:" + deliver.registered_Delivery);
		System.out.println("      sequence:" + deliver.getSequenceID());
		System.out.println("        report:" + deliver.report);
		System.out.println("            ID:" + bytes2hex(deliver.msg_Id));
		if (deliver.registered_Delivery == 1) {
			CMPPReport report = deliver.report;
			System.out.println("dest_terminal_Id=" + report.dest_terminal_Id);
			System.out.println("          msg_Id=" + report.msg_Id);
			System.out.println("            stat=" + report.stat);
			System.out.println("     submit_time=" + report.submit_time);
			System.out.println("       done_time=" + report.done_time);
			System.out.println("   smsc_sequence=" + report.smsc_sequence);
		}

	}

	private static String bytes2str(byte b[]) {
		if (b == null || b.length == 0)
			return "";
		String str = "";
		for (int i = 0; i < b.length; i++)
			str += b[i] + " ";
		return str;

	}

	private static void active(byte[] resppacket) throws IOException {
		CMPPActive active = new CMPPActive();
		active.parseResponseBody(resppacket);

		log.info("send activeresp");
		cmpp.cmppActiveResp(active);
		log.info("activeresp send successfully");

	}

	private static String bytes2hex(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			sb.append(Common.byte2hex(b[i]) + " ");
		return sb.toString();
	}

	public static void main(String[] args){
		String url = "218.206.27.231";
		String spid = "100248";
		String passwd = "765131";
		CMPPMo cmppMo = new CMPPMo(url, spid, passwd);
		cmppMo.start();
	}

}
