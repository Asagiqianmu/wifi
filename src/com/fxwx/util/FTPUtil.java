package com.fxwx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;


/**
 * FTP工具类
 * 
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2017年10月24日 下午2:49:11
 */
public class FTPUtil {

	// FTP服务器地址
	public static final String hostname = "116.211.87.235";
	// FTP服务器端口号
	public static final int port = 8021;
	// FTP登录帐号
	public static final String username = "kdflink";
	// FTP登录密码
	public static final String password = "kdflink@2017";
	// 数据类型
	public static final String dataType = "003";

	public static FTPClient ftpClient;

	/**
	 * FTP登录
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年10月25日 下午1:39:18
	 * @param hostname
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean login(String pathname) {
		boolean flag = false;
		ftpClient = new FTPClient();
		try {
			ftpClient.setControlEncoding("UTF-8");
			// 连接FTP服务器
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			flag = ftpClient.login(username, password);
			if (flag) {
				ftpClient.enterLocalActiveMode();
				System.out.println("-------1----------");
				ftpClient.enterLocalPassiveMode();
				System.out.println("-------2----------");
				ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
				System.out.println("-------3----------");
				ftpClient.setBufferSize(4096);
				System.out.println("-------4----------");
				// 是否成功登录FTP服务器
				int replyCode = ftpClient.getReplyCode();
				System.out.println("-------5----------");
				if (!FTPReply.isPositiveCompletion(replyCode)) {
					System.out.println("-----6------------");
					// 不合法时断开连接
					ftpClient.disconnect();
					return flag;
				}
				System.out.println("--------7---------");
				flag = ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				System.out.println("---------8--------");
				flag = ftpClient.changeWorkingDirectory(pathname);
				System.out.println("---------9--------");
				if (!flag) {
					System.out.println("---------10--------");
					// 1.首先进入ftp的根目录
					flag = ftpClient.changeWorkingDirectory("/");
					String[] dirs = pathname.split("/");
					for (String dir : dirs) {
						// 2.创建并进入不存在的目录
						if (!ftpClient.changeWorkingDirectory(dir)) {
							ftpClient.mkd(dir);
							flag = ftpClient.changeWorkingDirectory(dir);
							System.out.println("进入目录成功:" + dir);
						}
					}
				}
			}
			System.out.println("-----------------FTP登录状态:" + flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * FTP退出
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年10月25日 下午1:39:36
	 */
	public static void exit() {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				System.out.println("ftpClient退出");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public synchronized static boolean uploadFiles(File[] docNew, String[] newFileName) {
		boolean flag = false;
		InputStream input = null;
		try {
			for (int i = 0; i < docNew.length; i++) {
				File file = docNew[i];
				input = new FileInputStream(file);
				if (newFileName[i].trim().equals("")) {
					newFileName[i] = file.getName();
				}
				flag = ftpClient.storeFile(newFileName[i], input);
				if (flag) {
					System.out.println(newFileName[i] + "----upload File succeed");
				} else {
					System.out.println(newFileName[i] + "----upload File false");
				}
			}
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 上传文件（可供Action/Controller层使用）
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器保存目录
	 * @param fileName
	 *            上传到FTP服务器后的文件名称
	 * @param inputStream
	 *            输入文件流
	 * @return
	 */
	public synchronized static boolean uploadFile(String fileName, InputStream inputStream) {
		boolean flag = false;

		try {
			flag = ftpClient.storeFile(fileName, inputStream);
			System.out.println("storeFile=" + fileName + "--" + flag);
			if (flag) {
				System.out.println("上传成功完成" + fileName);
			}
			inputStream.close();
			System.out.println("inputStream关闭");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 上传文件（可对文件进行重命名）
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器后的文件名称
	 * @param originfilename
	 *            待上传文件的名称（绝对地址）
	 * @return
	 */
	public synchronized static boolean uploadFileFromProduction1(String filename, String originfilename) {
		boolean flag = false;
		try {
			InputStream inputStream = new FileInputStream(new File(originfilename));
			flag = uploadFile(filename, inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 上传文件（不可以进行文件的重命名操作）
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器保存目录
	 * @param originfilename
	 *            待上传文件的名称（绝对地址）
	 * @return
	 */
	public static boolean uploadFileFromProduction(String originfilename) {
		boolean flag = false;
		try {
			String fileName = new File(originfilename).getName();
			InputStream inputStream = new FileInputStream(new File(originfilename));
			flag = uploadFile(fileName, inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 删除文件
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器保存目录
	 * @param filename
	 *            要删除的文件名称
	 * @return
	 */
	public static boolean deleteFile(String hostname, int port, String username, String password, String pathname, String filename) {
		boolean flag = false;
		FTPClient ftpClient = new FTPClient();
		try {
			// 连接FTP服务器
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 验证FTP服务器是否登录成功
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				return flag;
			}
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.dele(filename);
			ftpClient.logout();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.logout();
				} catch (IOException e) {

				}
			}
		}
		return flag;
	}

	/**
	 * 下载文件
	 * 
	 * @param hostname
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口号
	 * @param username
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param pathname
	 *            FTP服务器文件目录
	 * @param filename
	 *            文件名称
	 * @param localpath
	 *            下载后的文件路径
	 * @return
	 */
	public static boolean downloadFile(String hostname, int port, String username, String password, String pathname, String filename, String localpath) {
		boolean flag = false;
		FTPClient ftpClient = new FTPClient();
		try {
			// 连接FTP服务器
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 验证FTP服务器是否登录成功
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				return flag;
			}
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(pathname);
			FTPFile[] ftpFiles = ftpClient.listFiles();
			for (FTPFile file : ftpFiles) {
				if (filename.equalsIgnoreCase(file.getName())) {
					File localFile = new File(localpath + "/" + file.getName());
					OutputStream os = new FileOutputStream(localFile);
					ftpClient.retrieveFile(file.getName(), os);
					os.close();
				}
			}
			ftpClient.logout();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.logout();
				} catch (IOException e) {

				}
			}
		}
		return flag;
	}

	public static void main(String[] args) {

		String pathname = "/" + dataType + "/" + DateUtil.getDateyyyyMMdd() + "/" + "FF-FF-FF-FF-FF-FF" + "/";
		// String fileName = XmlAndBcpUtil.getDatafileName();
		String originfilename = "C:\\Users\\Administrator\\Desktop\\rz_log_zip\\145-91110228076584524H-440300-440300-1508907900-00001.zip";
		String fileName = originfilename.substring(originfilename.indexOf("rz_log_zip") + 11);
		String originfilename1 = "C:\\Users\\Administrator\\Desktop\\rz_log_zip\\145-91110228076584524H-440300-440300-1508907900-00001.zip.ok";
		boolean isok_zipok = XmlAndBcpUtil.writeBCP("ok", originfilename1);
		String fileName1 = originfilename1.substring(originfilename1.indexOf("rz_log_zip") + 11);
		if (isok_zipok) {
			if (FTPUtil.login(pathname)) {
				System.out.println("-----------uploadFileFromProduction1----------.." + fileName);
				boolean flag = uploadFileFromProduction1(fileName, originfilename);
				if (flag) {
					System.out.println("-----------uploadFileFromProduction1----------.." + fileName1);
					flag = FTPUtil.uploadFileFromProduction1(fileName1, originfilename1);
					if (flag) {
						System.out.println("----------------------..");
						FTPUtil.exit();
					}
				}
			}
		}
	}
}
