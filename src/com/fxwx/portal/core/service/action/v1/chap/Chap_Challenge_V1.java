// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Chap_Challenge_V1.java

package com.fxwx.portal.core.service.action.v1.chap;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import org.apache.log4j.Logger;

import com.fxwx.portal.core.model.Config;
import com.fxwx.portal.core.service.utils.PortalUtil;

public class Chap_Challenge_V1
{

	private static Config config = Config.getInstance();
	private static Logger log = Logger.getLogger(Chap_Challenge_V1.class);

	public Chap_Challenge_V1()
	{
	}

	public static byte[] DunChongAction(String Bas_IP, int bas_PORT, int timeout_Sec, byte SerialNo[], byte UserIP[], byte userMac[])
	{
		DatagramSocket dataSocket;
		byte ErrorInfo[];
		byte Req_Challenge[];
		dataSocket = null;
		ErrorInfo = new byte[2];
		Req_Challenge = new byte[18 + userMac.length];
		Req_Challenge[0] = 1;
		Req_Challenge[1] = 1;
		Req_Challenge[2] = 0;
		Req_Challenge[3] = 0;
		Req_Challenge[4] = SerialNo[0];
		Req_Challenge[5] = SerialNo[1];
		Req_Challenge[6] = 0;
		Req_Challenge[7] = 0;
		Req_Challenge[8] = UserIP[0];
		Req_Challenge[9] = UserIP[1];
		Req_Challenge[10] = UserIP[2];
		Req_Challenge[11] = UserIP[3];
		Req_Challenge[12] = 0;
		Req_Challenge[13] = 0;
		Req_Challenge[14] = 0;
		Req_Challenge[15] = 0;
		Req_Challenge[16] = (byte) 128;
		Req_Challenge[17] = (byte)(userMac.length + 2);
		for (int i = 0; i < userMac.length; i++){
			Req_Challenge[18 + i] = userMac[i];
		}
		byte ACK_Challenge[];
		byte abyte0[];
		try {
			dataSocket = new DatagramSocket();
			DatagramPacket requestPacket = new DatagramPacket(Req_Challenge, 16, InetAddress.getByName(Bas_IP), bas_PORT);
			dataSocket.send(requestPacket);
			ACK_Challenge = new byte[34];
			DatagramPacket receivePacket = new DatagramPacket(ACK_Challenge, 34);
			dataSocket.setSoTimeout(timeout_Sec * 1000);
			dataSocket.receive(receivePacket);
			ErrorInfo[0] = ACK_Challenge[6];
			ErrorInfo[1] = ACK_Challenge[7];
			if ((ACK_Challenge[14] & 0xff) == 0){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("请求Challenge成功,准备发送REQ Auth认证请求!!!");
				}
				abyte0 = ACK_Challenge;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Challenge[14] & 0xff) == 1){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("发情Challenge请求被拒绝!!!");
				}
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Challenge[14] & 0xff) == 2){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("发送Challenge请求已建立!!!");
				}
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Challenge[14] & 0xff) == 3){
				if (/*basConfig.getIsdebug().equals("1")*/true)
					log.info("系统繁忙，请稍后再试!!!");
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Challenge[14] & 0xff) == 4){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("发送Challenge请求失败!!!");
				}
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			log.info("发送Challenge请求发生未知错误!!!");
			abyte0 = ErrorInfo;
			dataSocket.close();
			return abyte0;
		} catch (Exception e) {
			if (/*basConfig.getIsdebug().equals("1")*/true){
				log.info("发送Challenge请求无响应!!!");
			}
			abyte0 = ErrorInfo;
			dataSocket.close();
			return abyte0;
		}finally{
			dataSocket.close();
		}
	}
	
	public static byte[] Action(String Bas_IP, int bas_PORT, int timeout_Sec, byte SerialNo[], byte UserIP[])
	{
		DatagramSocket dataSocket;
		byte ErrorInfo[];
		byte Req_Challenge[];
		dataSocket = null;
		ErrorInfo = new byte[2];
		Req_Challenge = new byte[16];
		Req_Challenge[0] = 1;
		Req_Challenge[1] = 1;
		Req_Challenge[2] = 0;
		Req_Challenge[3] = 0;
		Req_Challenge[4] = SerialNo[0];
		Req_Challenge[5] = SerialNo[1];
		Req_Challenge[6] = 0;
		Req_Challenge[7] = 0;
		Req_Challenge[8] = UserIP[0];
		Req_Challenge[9] = UserIP[1];
		Req_Challenge[10] = UserIP[2];
		Req_Challenge[11] = UserIP[3];
		Req_Challenge[12] = 0;
		Req_Challenge[13] = 0;
		Req_Challenge[14] = 0;
		Req_Challenge[15] = 0;
		byte ACK_Challenge[];
		byte abyte0[];
		try {
			dataSocket = new DatagramSocket();
			DatagramPacket requestPacket = new DatagramPacket(Req_Challenge, 16, InetAddress.getByName(Bas_IP), bas_PORT);
			dataSocket.send(requestPacket);
			ACK_Challenge = new byte[34];
			DatagramPacket receivePacket = new DatagramPacket(ACK_Challenge, 34);
			dataSocket.setSoTimeout(timeout_Sec * 1000);
			dataSocket.receive(receivePacket);
			ErrorInfo[0] = ACK_Challenge[6];
			ErrorInfo[1] = ACK_Challenge[7];
			if ((ACK_Challenge[14] & 0xff) == 0){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("请求Challenge成功,准备发送REQ Auth认证请求!!!");
				}
				abyte0 = ACK_Challenge;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Challenge[14] & 0xff) == 1){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("发情Challenge请求被拒绝!!!");
				}
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Challenge[14] & 0xff) == 2){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("发送Challenge请求已建立!!!");
				}
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Challenge[14] & 0xff) == 3){
				if (/*basConfig.getIsdebug().equals("1")*/true)
					log.info("系统繁忙，请稍后再试!!!");
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Challenge[14] & 0xff) == 4){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("发送Challenge请求失败!!!");
				}
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			log.info("发送Challenge请求发生未知错误!!!");
			abyte0 = ErrorInfo;
			dataSocket.close();
			return abyte0;
		} catch (Exception e) {
			if (/*basConfig.getIsdebug().equals("1")*/true){
				log.info("发送Challenge请求无响应!!!");
			}
			abyte0 = ErrorInfo;
			dataSocket.close();
			return abyte0;
		}finally{
			dataSocket.close();
		}
	}

}
