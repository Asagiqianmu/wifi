// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PAP_Auth_V1.java

package com.fxwx.portal.core.service.action.v1.pap;

import java.net.*;
import org.apache.log4j.Logger;

import com.fxwx.portal.core.model.Config;

// Referenced classes of package com.leeson.portal.core.service.action.v1.pap:
//			PAP_Quit_V1

public class PAP_Auth_V1
{

	private static Config config = Config.getInstance();
	private static Logger log = Logger.getLogger(PAP_Auth_V1.class);

	public PAP_Auth_V1()
	{
	}

	public static boolean auth(String Bas_IP, int bas_PORT, int timeout_Sec, String in_username, String in_password, byte SerialNo[], byte UserIP[])
	{
		return Req_Auth(in_username.getBytes(), in_password.getBytes(), SerialNo, UserIP, timeout_Sec, Bas_IP, bas_PORT);
	}
	public static boolean dunchong_auth(String Bas_IP, int bas_PORT, int timeout_Sec, String in_usermac, String in_username, String in_password, byte SerialNo[], byte UserIP[])
	{
		return Req_Auth(in_usermac.getBytes(), in_username.getBytes(), in_password.getBytes(), SerialNo, UserIP, timeout_Sec, Bas_IP, bas_PORT);
	}

	public static boolean Req_Auth(byte Username[], byte password[], byte SerialNo[], byte UserIP[], int timeout_Sec, String Bas_IP, int bas_PORT)
	{
		DatagramSocket dataSocket;
		byte Req_Auth[];
		dataSocket = null;
		Req_Auth = new byte[20 + Username.length + password.length];
		Req_Auth[0] = 1;
		Req_Auth[1] = 3;
		Req_Auth[2] = 1;
		Req_Auth[3] = 0;
		Req_Auth[4] = SerialNo[0];
		Req_Auth[5] = SerialNo[1];
		Req_Auth[6] = 0;
		Req_Auth[7] = 0;
		Req_Auth[8] = UserIP[0];
		Req_Auth[9] = UserIP[1];
		Req_Auth[10] = UserIP[2];
		Req_Auth[11] = UserIP[3];
		Req_Auth[12] = 0;
		Req_Auth[13] = 0;
		Req_Auth[14] = 0;
		Req_Auth[15] = 2;
		Req_Auth[16] = 1;
		Req_Auth[17] = (byte)(Username.length + 2);
		for (int i = 0; i < Username.length; i++){
			Req_Auth[18 + i] = Username[i];
		}
		Req_Auth[18 + Username.length] = 2;
		Req_Auth[19 + Username.length] = (byte)(password.length + 2);
		for (int i = 0; i < password.length; i++){
			Req_Auth[20 + Username.length + i] = password[i];
		}
		
		log.error("pap加密后==="+new String(Req_Auth));
		log.error("pap发送前的包长度==="+Req_Auth.length);
		byte ACK_Data[];
		boolean flag;
		try {
			dataSocket = new DatagramSocket();
			DatagramPacket requestPacket = new DatagramPacket(Req_Auth, Req_Auth.length, InetAddress.getByName(Bas_IP), bas_PORT);
			dataSocket.send(requestPacket);
			ACK_Data = new byte[100];
			DatagramPacket receivePacket = new DatagramPacket(ACK_Data, 100);
			dataSocket.setSoTimeout(timeout_Sec * 1000);
			dataSocket.receive(receivePacket);
			ACK_Data = new byte[receivePacket.getLength()];
			for (int i = 0; i < ACK_Data.length; i++){
				ACK_Data[i] = receivePacket.getData()[i];
			}
			
			if ((ACK_Data[14] & 0xff) != 0 && (ACK_Data[14] & 0xff) == 2){
				
				if (/*/*basConfig.getIsdebug().equals("1")*/true){
					log.info("认证成功,准备发送AFF_ACK_AUTH!!!");
				}
				flag = AFF_Ack_Auth(SerialNo, UserIP, Bas_IP, bas_PORT);
				dataSocket.close();
				return flag;
			}
			if ((ACK_Data[14] & 0xff) == 1)
			{
				if (/*/*basConfig.getIsdebug().equals("1")*/true)
					log.info("发送认证请求被拒绝!!!");
			} else
				if ((ACK_Data[14] & 0xff) == 3)
				{
					if (/*/*basConfig.getIsdebug().equals("1")*/true)
						log.info("系统繁忙，请稍后再试!!!");
				} else
					if ((ACK_Data[14] & 0xff) == 4)
					{
						if (/*/*basConfig.getIsdebug().equals("1")*/true)
							log.info("发送认证请求失败!!!");
					} else if (/*/*basConfig.getIsdebug().equals("1")*/true){
						log.info("发送认证请求出现未知错误!!!");
					}
			dataSocket.close();
			return false;
		} catch (Exception e) {
			if (/*/*basConfig.getIsdebug().equals("1")*/true)
				 log.info("发送认证请求无响应!!!");
			PAP_Quit_V1.quit(2, Bas_IP, bas_PORT, timeout_Sec, SerialNo, UserIP);
			dataSocket.close();
			return false;
		}finally{
			dataSocket.close();
		}
	}

	/**
	 * 敦崇AC
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年3月20日 下午5:15:13
	 * @param Username
	 * @param password
	 * @param SerialNo
	 * @param UserIP
	 * @param timeout_Sec
	 * @param Bas_IP
	 * @param bas_PORT
	 * @return
	 */
	public static boolean Req_Auth(byte usermac[],byte Username[], byte password[], byte SerialNo[], byte UserIP[], int timeout_Sec, String Bas_IP, int bas_PORT)
	{
		DatagramSocket dataSocket;
		byte Req_Auth[];
		dataSocket = null;
		Req_Auth = new byte[22 + usermac.length + Username.length + password.length];
		Req_Auth[0] = 1;
		Req_Auth[1] = 3;
		Req_Auth[2] = 1;
		Req_Auth[3] = 0;
		Req_Auth[4] = SerialNo[0];
		Req_Auth[5] = SerialNo[1];
		Req_Auth[6] = 0;
		Req_Auth[7] = 0;
		Req_Auth[8] = UserIP[0];
		Req_Auth[9] = UserIP[1];
		Req_Auth[10] = UserIP[2];
		Req_Auth[11] = UserIP[3];
		Req_Auth[12] = 0;
		Req_Auth[13] = 0;
		Req_Auth[14] = 0;
		Req_Auth[15] = 2;
		Req_Auth[16] = 1;
		Req_Auth[17] = (byte)(Username.length + 2);
		for (int i = 0; i < Username.length; i++){
			Req_Auth[18 + i] = Username[i];
		}
		Req_Auth[18 + Username.length] = 2;
		Req_Auth[19 + Username.length] = (byte)(password.length + 2);
		for (int i = 0; i < password.length; i++){
			Req_Auth[20 + Username.length + i] = password[i];
		}
		Req_Auth[20 + password.length] = 2;
		Req_Auth[21 + password.length] = (byte)(usermac.length + 2);
		for (int i = 0; i < usermac.length; i++){
			Req_Auth[22 + password.length + i] = usermac[i];
		}
		
		log.error("pap加密后==="+new String(Req_Auth));
		log.error("pap发送前的包长度==="+Req_Auth.length);
		byte ACK_Data[];
		boolean flag;
		try {
			dataSocket = new DatagramSocket();
			DatagramPacket requestPacket = new DatagramPacket(Req_Auth, Req_Auth.length, InetAddress.getByName(Bas_IP), bas_PORT);
			dataSocket.send(requestPacket);
			ACK_Data = new byte[100];
			DatagramPacket receivePacket = new DatagramPacket(ACK_Data, 100);
			dataSocket.setSoTimeout(timeout_Sec * 1000);
			dataSocket.receive(receivePacket);
			ACK_Data = new byte[receivePacket.getLength()];
			for (int i = 0; i < ACK_Data.length; i++){
				ACK_Data[i] = receivePacket.getData()[i];
			}
			
			if ((ACK_Data[14] & 0xff) != 0 && (ACK_Data[14] & 0xff) == 2){
				
				if (/*/*basConfig.getIsdebug().equals("1")*/true){
					log.info("认证成功,准备发送AFF_ACK_AUTH!!!");
				}
				flag = AFF_Ack_Auth(SerialNo, UserIP, Bas_IP, bas_PORT);
				dataSocket.close();
				return flag;
			}
			if ((ACK_Data[14] & 0xff) == 1)
			{
				if (/*/*basConfig.getIsdebug().equals("1")*/true)
					log.info("发送认证请求被拒绝!!!");
			} else
				if ((ACK_Data[14] & 0xff) == 3)
				{
					if (/*/*basConfig.getIsdebug().equals("1")*/true)
						log.info("系统繁忙，请稍后再试!!!");
				} else
					if ((ACK_Data[14] & 0xff) == 4)
					{
						if (/*/*basConfig.getIsdebug().equals("1")*/true)
							log.info("发送认证请求失败!!!");
					} else if (/*/*basConfig.getIsdebug().equals("1")*/true){
						log.info("发送认证请求出现未知错误!!!");
					}
			dataSocket.close();
			return false;
		} catch (Exception e) {
			if (/*/*basConfig.getIsdebug().equals("1")*/true)
				 log.info("发送认证请求无响应!!!");
			PAP_Quit_V1.quit(2, Bas_IP, bas_PORT, timeout_Sec, SerialNo, UserIP);
			dataSocket.close();
			return false;
		}finally{
			dataSocket.close();
		}
	}
	
	
	public static boolean AFF_Ack_Auth(byte SerialNo[], byte UserIP[], String Bas_IP, int bas_PORT)
	{
		DatagramSocket dataSocket;
		byte AFF_Ack_Auth[];
		dataSocket = null;
		AFF_Ack_Auth = new byte[16];
		AFF_Ack_Auth[0] = 1;
		AFF_Ack_Auth[1] = 7;
		AFF_Ack_Auth[2] = 1;
		AFF_Ack_Auth[3] = 0;
		AFF_Ack_Auth[4] = SerialNo[0];
		AFF_Ack_Auth[5] = SerialNo[1];
		AFF_Ack_Auth[6] = 0;
		AFF_Ack_Auth[7] = 0;
		AFF_Ack_Auth[8] = UserIP[0];
		AFF_Ack_Auth[9] = UserIP[1];
		AFF_Ack_Auth[10] = UserIP[2];
		AFF_Ack_Auth[11] = UserIP[3];
		AFF_Ack_Auth[12] = 0;
		AFF_Ack_Auth[13] = 0;
		AFF_Ack_Auth[14] = 0;
		AFF_Ack_Auth[15] = 0;
		try {
			dataSocket = new DatagramSocket();
			DatagramPacket requestPacket = new DatagramPacket(AFF_Ack_Auth, AFF_Ack_Auth.length, InetAddress.getByName(Bas_IP), bas_PORT);
			dataSocket.send(requestPacket);
			if (/*basConfig.getIsdebug().equals("1")*/true){
				log.info("发送AFF_Ack_Auth认证成功确认报文成功！！");
			}
			return true;
		} catch (Exception e) {
			if (/*basConfig.getIsdebug().equals("1")*/true)
				 log.info("发送AFF_Ack_Auth认证成功确认报文出错！！");
			return false;
		}finally{
			dataSocket.close();
		}
		
	}

}
