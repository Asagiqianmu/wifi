// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Chap_Auth_V1.java

package com.fxwx.portal.core.service.action.v1.chap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Map;
import org.apache.log4j.Logger;

import com.fxwx.portal.core.model.Config;
import com.fxwx.portal.core.service.utils.ChapPassword;
import com.fxwx.portal.core.service.utils.PortalUtil;

public class Chap_Auth_V1
{

	private static Config config = Config.getInstance();
	private static Logger log = Logger.getLogger(Chap_Auth_V1.class);

	public Chap_Auth_V1()
	{
	}

	public static byte[] auth(String Bas_IP, int bas_PORT, int timeout_Sec, String in_username, String in_password, byte SerialNo[], byte UserIP[], byte ReqID[], 
			byte Challenge[])
	{
		byte ChapPass[] = new byte[16];
		byte Username[] = in_username.getBytes();
		byte password[] = in_password.getBytes();
		try
		{
			ChapPass = ChapPassword.MK_ChapPwd(ReqID, Challenge, password);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return Req_Auth(Username, ChapPass, SerialNo, UserIP, ReqID, timeout_Sec, Bas_IP, bas_PORT);
	}
	/**
	 * 敦崇
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年3月20日 下午6:49:23
	 * @param Bas_IP
	 * @param bas_PORT
	 * @param timeout_Sec
	 * @param in_username
	 * @param in_password
	 * @param SerialNo
	 * @param UserIP
	 * @param ReqID
	 * @param Challenge
	 * @return
	 */
	public static byte[] DunChong_auth(String Bas_IP, int bas_PORT, int timeout_Sec, String in_username, String in_password, String in_userMac, byte SerialNo[], byte UserIP[], byte ReqID[], 
			byte Challenge[])
	{
		byte ChapPass[] = new byte[16];
		byte Username[] = in_username.getBytes();
		byte password[] = in_password.getBytes();
		byte userMac[] = in_userMac.getBytes();
		try
		{
			ChapPass = ChapPassword.MK_ChapPwd(ReqID, Challenge, password);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return DunChongReq_Auth(Username, userMac, ChapPass, SerialNo, UserIP, ReqID, timeout_Sec, Bas_IP, bas_PORT);
	}
	/**
	 * 敦崇
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年3月20日 下午6:52:01
	 * @param Username
	 * @param ChapPass
	 * @param SerialNo
	 * @param UserIP
	 * @param ReqID
	 * @param timeout_Sec
	 * @param Bas_IP
	 * @param bas_PORT
	 * @return
	 */
	public static byte[] DunChongReq_Auth(byte Username[],byte userMac[], byte ChapPass[], byte SerialNo[], byte UserIP[], byte ReqID[], int timeout_Sec, String Bas_IP, int bas_PORT)
	{
		log.error("发送认证报文前数据==="+ReqID);
		DatagramSocket dataSocket;
		byte ErrorInfo[];
		byte Req_Auth[];
		dataSocket = null;
		ErrorInfo = new byte[1];
		//需要携带用户名、密码、ChapPassWord、userMac 4个参数
		Req_Auth = new byte[24 + Username.length + ChapPass.length + ChapPass.length + userMac.length];
		Req_Auth[0] = 1;
		Req_Auth[1] = 3;
		Req_Auth[2] = 0;
		Req_Auth[3] = 0;
		Req_Auth[4] = SerialNo[0];
		Req_Auth[5] = SerialNo[1];
		Req_Auth[6] = ReqID[0];
		Req_Auth[7] = ReqID[1];
		Req_Auth[8] = UserIP[0];
		Req_Auth[9] = UserIP[1];
		Req_Auth[10] = UserIP[2];
		Req_Auth[11] = UserIP[3];
		Req_Auth[12] = 0;
		Req_Auth[13] = 0;
		Req_Auth[14] = 0;
		Req_Auth[15] = 4;//报文携带参数的数量
		//加入用户名
		Req_Auth[16] = 1;//参数属性类型
		Req_Auth[17] = (byte)(Username.length + 2);//参数长度+2个空位
		for (int i = 0; i < Username.length; i++){
			Req_Auth[18 + i] = Username[i];
		}
		//加入 Password
		Req_Auth[18 + Username.length] = 2;//参数属性类型
		Req_Auth[19 + Username.length] = (byte)(ChapPass.length + 2);
		for (int i = 0; i < ChapPass.length; i++){
			Req_Auth[20 + Username.length + i] = ChapPass[i];
		}
		//加入 ChapPssWord
		Req_Auth[20 + Username.length + ChapPass.length] = 4;//参数属性类型
		Req_Auth[21 + Username.length + ChapPass.length] = (byte)(ChapPass.length + 2);
		for (int i = 0; i < ChapPass.length; i++){
			Req_Auth[22 + Username.length + ChapPass.length + i] = ChapPass[i];
		}
		//加入UserMac
		Req_Auth[22 + Username.length + ChapPass.length + ChapPass.length] = (byte)0x80;//参数属性类型
		Req_Auth[23 + Username.length + ChapPass.length + ChapPass.length] = (byte)(userMac.length + 2);
		for (int i = 0; i < userMac.length; i++){
			Req_Auth[24 + Username.length + ChapPass.length + ChapPass.length + i] = userMac[i];
		}
		
		byte ACK_Auth_Data[];
		byte abyte0[];
		try {
			dataSocket = new DatagramSocket();
			DatagramPacket requestPacket = new DatagramPacket(Req_Auth, Req_Auth.length, InetAddress.getByName(Bas_IP), bas_PORT);
			dataSocket.send(requestPacket);
			ACK_Auth_Data = new byte[16];
			DatagramPacket receivePacket = new DatagramPacket(ACK_Auth_Data, ACK_Auth_Data.length);
//			log.error("port===="+receivePacket.getPort()+"===ip=="+receivePacket.getAddress());
			dataSocket.setSoTimeout(timeout_Sec * 1000);
			dataSocket.receive(receivePacket);
			for (int i = 0; i < ACK_Auth_Data.length; i++) {
				log.error("查看接收的全部报文=="+ACK_Auth_Data[i]);
			}
			log.error("解析==="+new String(ACK_Auth_Data)+"==hahahahaahah==="+ACK_Auth_Data[14]);
			if ((ACK_Auth_Data[14] & 0xff) == 0 || (ACK_Auth_Data[14] & 0xff) == 2){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("认证成功,准备发送AFF_ACK_AUTH!!!");
					log.error("chap认证成功,准备发送AFF_ACK_AUTH!!!");
				}
				log.error("发送计费报文前数据==="+ReqID+"====userip==="+UserIP+"====ser==="+SerialNo);
				abyte0 = DunChongAFF_Ack_Auth(userMac, SerialNo, UserIP, ReqID, Bas_IP, bas_PORT);
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Auth_Data[14] & 0xff) == 1){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("发送认证请求被拒绝!!!");
				}
				ErrorInfo[0] = 21;
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Auth_Data[14] & 0xff) == 2){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("发送认证请求连接已建立!!!");
				}
				ErrorInfo[0] = 22;
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Auth_Data[14] & 0xff) == 3){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("系统繁忙,请稍后再试!!!");
				}
				ErrorInfo[0] = 23;
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Auth_Data[14] & 0xff) == 4){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("发送认证请求失败!!!");
				}
				ErrorInfo[0] = 24;
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			log.info("发送认证请求出现未知错误!!!");
			ErrorInfo[0] = 2;
			abyte0 = ErrorInfo;
			dataSocket.close();
			return abyte0;
			
		} catch (Exception e) {
			if (/*basConfig.getIsdebug().equals("1")*/true)
				log.info("发送认证请求无响应!!!");
			ErrorInfo[0] = 2;
			abyte0 = ErrorInfo;
			dataSocket.close();
			return abyte0;
		}finally{
			dataSocket.close();
		}
	}
	public static byte[] Req_Auth(byte Username[], byte ChapPass[], byte SerialNo[], byte UserIP[], byte ReqID[], int timeout_Sec, String Bas_IP, int bas_PORT)
	{
		log.error("发送认证报文前数据==="+ReqID);
		DatagramSocket dataSocket;
		byte ErrorInfo[];
		byte Req_Auth[];
		dataSocket = null;
		ErrorInfo = new byte[1];
		Req_Auth = new byte[20 + Username.length + ChapPass.length];
		Req_Auth[0] = 1;
		Req_Auth[1] = 3;
		Req_Auth[2] = 0;
		Req_Auth[3] = 0;
		Req_Auth[4] = SerialNo[0];
		Req_Auth[5] = SerialNo[1];
		Req_Auth[6] = ReqID[0];
		Req_Auth[7] = ReqID[1];
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
		Req_Auth[18 + Username.length] = 4;
		Req_Auth[19 + Username.length] = (byte)(ChapPass.length + 2);
		for (int i = 0; i < ChapPass.length; i++){
			Req_Auth[20 + Username.length + i] = ChapPass[i];
		}
		byte ACK_Auth_Data[];
		byte abyte0[];
		try {
			dataSocket = new DatagramSocket();
			DatagramPacket requestPacket = new DatagramPacket(Req_Auth, Req_Auth.length, InetAddress.getByName(Bas_IP), bas_PORT);
			dataSocket.send(requestPacket);
			ACK_Auth_Data = new byte[16];
			DatagramPacket receivePacket = new DatagramPacket(ACK_Auth_Data, ACK_Auth_Data.length);
			log.error("port===="+receivePacket.getPort()+"===ip=="+receivePacket.getAddress());
			dataSocket.setSoTimeout(timeout_Sec * 1000);
			dataSocket.receive(receivePacket);
			for (int i = 0; i < ACK_Auth_Data.length; i++) {
				log.error("查看接收的全部报文=="+ACK_Auth_Data[i]);
			}
			log.error("解析==="+new String(ACK_Auth_Data)+"==hahahahaahah==="+ACK_Auth_Data[14]);
			if ((ACK_Auth_Data[14] & 0xff) == 0 || (ACK_Auth_Data[14] & 0xff) == 2){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("认证成功,准备发送AFF_ACK_AUTH!!!");
					log.error("chap认证成功,准备发送AFF_ACK_AUTH!!!");
				}
				log.error("发送计费报文前数据==="+ReqID+"====userip==="+UserIP+"====ser==="+SerialNo);
				abyte0 = AFF_Ack_Auth(SerialNo, UserIP, ReqID, Bas_IP, bas_PORT);
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Auth_Data[14] & 0xff) == 1){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("发送认证请求被拒绝!!!");
				}
				ErrorInfo[0] = 21;
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Auth_Data[14] & 0xff) == 2){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("发送认证请求连接已建立!!!");
				}
				ErrorInfo[0] = 22;
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Auth_Data[14] & 0xff) == 3){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("系统繁忙,请稍后再试!!!");
				}
				ErrorInfo[0] = 23;
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			if ((ACK_Auth_Data[14] & 0xff) == 4){
				if (/*basConfig.getIsdebug().equals("1")*/true){
					log.info("发送认证请求失败!!!");
				}
				ErrorInfo[0] = 24;
				abyte0 = ErrorInfo;
				dataSocket.close();
				return abyte0;
			}
			log.info("发送认证请求出现未知错误!!!");
			ErrorInfo[0] = 2;
			abyte0 = ErrorInfo;
			dataSocket.close();
			return abyte0;
			
		} catch (Exception e) {
			if (/*basConfig.getIsdebug().equals("1")*/true)
				log.info("发送认证请求无响应!!!");
			ErrorInfo[0] = 2;
			abyte0 = ErrorInfo;
			dataSocket.close();
			return abyte0;
		}finally{
			dataSocket.close();
		}
	}

	/**
	 * 敦崇
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年3月21日 下午3:40:07
	 * @param SerialNo
	 * @param UserIP
	 * @param ReqID
	 * @param Bas_IP
	 * @param bas_PORT
	 * @return
	 */
	public static byte[] DunChongAFF_Ack_Auth(byte userMac[],byte SerialNo[], byte UserIP[], byte ReqID[], String Bas_IP, int bas_PORT)
	{
		DatagramSocket dataSocket;
		byte ErrorInfo[];
		byte AFF_Ack_Auth[];
		dataSocket = null;
		ErrorInfo = new byte[1];
		AFF_Ack_Auth = new byte[18 + userMac.length];
		AFF_Ack_Auth[0] = 1;
		AFF_Ack_Auth[1] = 7;
		AFF_Ack_Auth[2] = 0;
		AFF_Ack_Auth[3] = 0;
		AFF_Ack_Auth[4] = SerialNo[0];
		AFF_Ack_Auth[5] = SerialNo[1];
		AFF_Ack_Auth[6] = ReqID[0];
		AFF_Ack_Auth[7] = ReqID[1];
		AFF_Ack_Auth[8] = UserIP[0];
		AFF_Ack_Auth[9] = UserIP[1];
		AFF_Ack_Auth[10] = UserIP[2];
		AFF_Ack_Auth[11] = UserIP[3];
		AFF_Ack_Auth[12] = 0;
		AFF_Ack_Auth[13] = 0;
		AFF_Ack_Auth[14] = 0;
		AFF_Ack_Auth[15] = 0;
		AFF_Ack_Auth[16] = (byte)0x80;
		AFF_Ack_Auth[17] = (byte)(userMac.length + 2);
		for (int i = 0; i < userMac.length; i++){
			AFF_Ack_Auth[18 + i] = userMac[i];
		}
		try
		{
			dataSocket = new DatagramSocket();
			DatagramPacket requestPacket = new DatagramPacket(AFF_Ack_Auth, AFF_Ack_Auth.length, InetAddress.getByName(Bas_IP), bas_PORT);
			dataSocket.send(requestPacket);
			if (/*basConfig.getIsdebug().equals("1")*/true){
				log.info("发送AFF_Ack_Auth认证成功响应报文回复成功!!!");
			}
		}
		catch (IOException e)
		{
			if (/*basConfig.getIsdebug().equals("1")*/true)
				log.info("发送AFF_Ack_Auth认证成功响应报文回复失败!!!");
		}finally{
			dataSocket.close();
		}
		ErrorInfo[0] = 20;
		return ErrorInfo;
	}


	public static byte[] AFF_Ack_Auth(byte SerialNo[], byte UserIP[], byte ReqID[], String Bas_IP, int bas_PORT)
	{
		DatagramSocket dataSocket;
		byte ErrorInfo[];
		byte AFF_Ack_Auth[];
		dataSocket = null;
		ErrorInfo = new byte[1];
		AFF_Ack_Auth = new byte[16];
		AFF_Ack_Auth[0] = 1;
		AFF_Ack_Auth[1] = 7;
		AFF_Ack_Auth[2] = 0;
		AFF_Ack_Auth[3] = 0;
		AFF_Ack_Auth[4] = SerialNo[0];
		AFF_Ack_Auth[5] = SerialNo[1];
		AFF_Ack_Auth[6] = ReqID[0];
		AFF_Ack_Auth[7] = ReqID[1];
		AFF_Ack_Auth[8] = UserIP[0];
		AFF_Ack_Auth[9] = UserIP[1];
		AFF_Ack_Auth[10] = UserIP[2];
		AFF_Ack_Auth[11] = UserIP[3];
		AFF_Ack_Auth[12] = 0;
		AFF_Ack_Auth[13] = 0;
		AFF_Ack_Auth[14] = 0;
		AFF_Ack_Auth[15] = 0;
		try
		{
			dataSocket = new DatagramSocket();
			DatagramPacket requestPacket = new DatagramPacket(AFF_Ack_Auth, 16, InetAddress.getByName(Bas_IP), bas_PORT);
			dataSocket.send(requestPacket);
			if (/*basConfig.getIsdebug().equals("1")*/true){
				log.info("发送AFF_Ack_Auth认证成功响应报文回复成功!!!");
			}
		}
		catch (IOException e)
		{
			if (/*basConfig.getIsdebug().equals("1")*/true)
				log.info("发送AFF_Ack_Auth认证成功响应报文回复失败!!!");
		}finally{
			dataSocket.close();
		}
		ErrorInfo[0] = 20;
		return ErrorInfo;
	}

}
