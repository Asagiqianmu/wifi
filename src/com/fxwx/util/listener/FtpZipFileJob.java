package com.fxwx.util.listener;

import java.io.File;
import java.util.List;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fxwx.controller.BaseController;
import com.fxwx.entiy.RxzLog;
import com.fxwx.service.UnitePortalService;
import com.fxwx.util.DateUtil;
import com.fxwx.util.FTPUtil;
import com.fxwx.util.XmlAndBcpUtil;

/**
 * zip打包认证日志文件
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2017年10月24日 下午1:11:21
 */

@IocBean
public class FtpZipFileJob extends BaseController implements Job{
	Ioc ioc = null;
	
	@Inject
	private UnitePortalService unitePortalServiceImpl; //用户接口
	
	// FTP服务器地址
	public static final String hostname = "116.211.87.235";
	// FTP服务器端口号
	public static final int port = 8021;
	//FTP登录帐号
	public static final String username = "kdflink";
	//FTP登录密码
	public static final String password = "kdflink@2017";
	//数据类型
	public static final String dataType = "003";

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("__________sdfsdfdf___________________");
		if (unitePortalServiceImpl == null){
			System.out.println("__________unitePortalServiceImpl____为空___开始导入______");
			unitePortalServiceImpl = Mvcs.ctx().getDefaultIoc().get(UnitePortalService.class);
			List<RxzLog> list = unitePortalServiceImpl.getNotUpLoadRxzLog();
			if(list != null && list.size() > 0){
				System.out.println("__________list____大小_______________"+list.size());
				String fname = XmlAndBcpUtil.getDatafileName();
				String xmlpath = XmlAndBcpUtil.path + fname + ".xml";
				//bcp文件记录不能超过5000条
				if(list.size() <= 5000){
					XmlAndBcpUtil.buildXML(list.size(), fname);
					String bcppath = XmlAndBcpUtil.path + fname + ".bcp";
					String bcpStr = "";
					for (int i = 0; i < list.size(); i++) {
						bcpStr += list.get(i).getAuthType() + "\t" + list.get(i).getAuthAccount() + "\t" + list.get(i).getMac() + "\t" + list.get(i).getApMac() + "\n";
					}
					if(XmlAndBcpUtil.buildBCP(fname, bcpStr)){
						String[] paths = new String[2];
						paths[0] = xmlpath;
						paths[1] = bcppath;
						String zipfileName = XmlAndBcpUtil.getZIPfileName();
						String zip_srcPath = XmlAndBcpUtil.zip_path+zipfileName+".zip";
						String ok_zip_srcPath = XmlAndBcpUtil.zip_path+zipfileName+".zip.ok";
						if(XmlAndBcpUtil.judeDirExists(new File(XmlAndBcpUtil.zip_path))){
							boolean isok_zip = XmlAndBcpUtil.writeZipFile(zip_srcPath, paths);
							boolean isok_zipok = XmlAndBcpUtil.writeBCP("ok", ok_zip_srcPath);
							String pathname = "/"+dataType+"/"+DateUtil.getDateyyyyMMdd()+"/"+"FF-FF-FF-FF-FF-FF"+"/";
							if (isok_zip & isok_zipok) {
								if (FTPUtil.login(pathname)) {
									boolean flag = FTPUtil.uploadFileFromProduction1(zipfileName+".zip", zip_srcPath);
									if(flag){
										if(isok_zipok){
											flag = FTPUtil.uploadFileFromProduction1(zipfileName+".zip.ok", ok_zip_srcPath);
											if(flag){
												System.out.println("----------------------..");
												FTPUtil.exit();
												for (int i = 0; i < list.size(); i++) {
													RxzLog rxzLog = list.get(i);
													rxzLog.setTstate(2);
													unitePortalServiceImpl.updateRxzLog(rxzLog);
												}
											}
										}
									}
								}
							}
						}
					}
				}else{
					//记录超过5000条，则分批处理提交
					int a = list.size()/5000;
					if(list.size()%5000 > 0){
						a = list.size()/5000 + 1;
					}
					for (int j = 1; j <= a; j++) {
						int size = 5000*j;
						if(size > list.size()){
							size = list.size();
						}
						int min = 5000*(j-1);
						XmlAndBcpUtil.buildXML(size-(5000*(j-1)), fname);
						String bcppath = XmlAndBcpUtil.path + fname + ".bcp";
						String bcpStr = "";
						for (int i = min; i < size; i++) {
							bcpStr += list.get(i).getAuthType() + "\t" + list.get(i).getAuthAccount() + "\t" + list.get(i).getMac() + "\t" + list.get(i).getApMac() + "\n";
						}
						if(XmlAndBcpUtil.buildBCP(fname, bcpStr)){
							String[] paths = new String[2];
							paths[0] = xmlpath;
							paths[1] = bcppath;
							String zipfileName = XmlAndBcpUtil.getZIPfileName();
							String zip_srcPath = XmlAndBcpUtil.zip_path+zipfileName+".zip";
							String ok_zip_srcPath = XmlAndBcpUtil.zip_path+zipfileName+".zip.ok";
							if(XmlAndBcpUtil.judeDirExists(new File(XmlAndBcpUtil.zip_path))){
								boolean isok_zip = XmlAndBcpUtil.writeZipFile(zip_srcPath, paths);
								boolean isok_zipok = XmlAndBcpUtil.writeBCP("ok", ok_zip_srcPath);
								String pathname = "/"+dataType+"/"+DateUtil.getDateyyyyMMdd()+"/"+"FF-FF-FF-FF-FF-FF"+"/";
								if (isok_zip & isok_zipok) {
									if (FTPUtil.login(pathname)) {
										boolean flag = FTPUtil.uploadFileFromProduction1(zipfileName+".zip", zip_srcPath);
										if(flag){
											if(isok_zipok){
												flag = FTPUtil.uploadFileFromProduction1(zipfileName+".zip.ok", ok_zip_srcPath);
												if(flag){
													System.out.println("----------------------..");
													FTPUtil.exit();
													for (int i = min; i < size; i++) {
														RxzLog rxzLog = list.get(i);
														rxzLog.setTstate(2);
														unitePortalServiceImpl.updateRxzLog(rxzLog);
													}
												}
											}
										}
									}
								}
							}
						}
					}
					
				}
			}
			System.out.println("__________list____为NULL_______________");
			
		}
	}
}
