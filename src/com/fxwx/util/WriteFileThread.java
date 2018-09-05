package com.fxwx.util;

public class WriteFileThread implements Runnable{
	String fileName = "";
	String Auth_Account = "";
	String userMac = "";
	String apMac = "";
	
	public WriteFileThread(String fileName, String Auth_Account, String userMac, String apMac) {
		// TODO Auto-generated constructor stub
		this.fileName = fileName;
		this.Auth_Account = Auth_Account;
		this.userMac = userMac;
		this.apMac = apMac;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		XmlAndBcpUtil.buildBCP(fileName, Auth_Account, userMac, apMac);
	}

}
