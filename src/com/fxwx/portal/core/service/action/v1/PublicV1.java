// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PublicV1.java

package com.fxwx.portal.core.service.action.v1;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import org.apache.log4j.Logger;

import com.fxwx.portal.core.model.Config;
import com.fxwx.portal.core.service.utils.PortalUtil;

public class PublicV1
{

	private static Config config = Config.getInstance();
	private static Logger log = Logger.getLogger(PublicV1.class);

	public PublicV1()
	{
	}

	public static boolean choose(int type, byte Req_Quit[], int timeout_Sec, String Bas_IP, int bas_PORT)
	{
		if (type == 0)
			return offline(Req_Quit, timeout_Sec, Bas_IP, bas_PORT);
		else
			return timeoutAffirm(type, Req_Quit, Bas_IP, bas_PORT);
	}

	public static boolean offline(byte Req_Quit[], int timeout_Sec, String Bas_IP, int bas_PORT)
	{
		DatagramSocket dataSocket;
		byte ACK_Data[];
		dataSocket = null;
		ACK_Data = new byte[16];
		try
		{
			dataSocket = new DatagramSocket();
			DatagramPacket requestPacket = new DatagramPacket(Req_Quit, 16, InetAddress.getByName(Bas_IP), bas_PORT);
			dataSocket.send(requestPacket);
			DatagramPacket receivePacket = new DatagramPacket(ACK_Data, 16);
			dataSocket.setSoTimeout(timeout_Sec * 1000);
			dataSocket.receive(receivePacket);
		}
		catch (IOException e)
		{
				log.info("建立请求无响应");
				return false;
		} finally {
			dataSocket.close();
		}
		if ((ACK_Data[14] & 0xff) == 1)
		{
			 if (/*config.getDebug().equals("1")*/true) {//后改成动态查询
				 log.info("发送下线请求被拒绝!!!");
			 }
		}
		if ((ACK_Data[14] & 0xff) == 2)
		{
			if (/*config.getIsdebug().equals("1")*/true){//同上
				 log.info("发送下线请求出现错误!!!");
				return false;
			}
		}
		if (/*config.getIsdebug().equals("1")*/true){
			log.info("请求下线成功！！！");
			return true;
		}
		return true;
	}

	/*    */   public static boolean timeoutAffirm(int type, byte[] Req_Quit, String Bas_IP, int bas_PORT)
	/*    */   {
	/* 91 */     DatagramSocket dataSocket = null;
	/* 92 */     Req_Quit[14] = 1;
	/*    */     try {
	/* 94 */       dataSocket = new DatagramSocket();
	/* 95 */       DatagramPacket requestPacket = new DatagramPacket(Req_Quit, 16, 
	/* 96 */         InetAddress.getByName(Bas_IP), bas_PORT);
	/* 97 */       dataSocket.send(requestPacket);
	/* 98 */       if (/*config.getDebug().equals("1")*/true) {
	/* 99 */         log.info("发送超时回复报文成功: " + PortalUtil.Getbyte2HexString(Req_Quit));
	/*    */       }
	/*    */ 
	/* 102 */       return true;
	/*    */     } catch (IOException e) {
	/* 104 */       if (/*config.getDebug().equals("1")*/ true) {
	/* 105 */         log.info("发送超时回复报文出现未知错误！！！");
	/*    */       }
	/*    */ 
	/* 108 */       return false;
	/*    */     } finally {
	/* 110 */       dataSocket.close();
	/*    */     }
	/*    */   }

}
