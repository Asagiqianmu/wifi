// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Chap_Quit_V1.java

package com.fxwx.portal.core.service.action.v1.chap;

import com.fxwx.portal.core.service.action.v1.PublicV1;

public class Chap_Quit_V1
{

	public Chap_Quit_V1()
	{
	}

	/**
	 * 敦崇
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年3月21日 下午3:51:27
	 * @param type
	 * @param Bas_IP
	 * @param bas_PORT
	 * @param timeout_Sec
	 * @param SerialNo
	 * @param UserIP
	 * @param ReqID
	 * @return
	 */
	public static boolean DunChong_quit(int type, String Bas_IP, int bas_PORT, int timeout_Sec, byte SerialNo[], byte UserIP[], byte ReqID[], byte userMac[])
	{
		byte Req_Quit[] = new byte[18 + userMac.length];
		Req_Quit[0] = 1;
		Req_Quit[1] = 5;
		Req_Quit[2] = 0;
		Req_Quit[3] = 0;
		Req_Quit[4] = SerialNo[0];
		Req_Quit[5] = SerialNo[1];
		Req_Quit[6] = ReqID[0];
		Req_Quit[7] = ReqID[1];
		Req_Quit[8] = UserIP[0];
		Req_Quit[9] = UserIP[1];
		Req_Quit[10] = UserIP[2];
		Req_Quit[11] = UserIP[3];
		Req_Quit[12] = 0;
		Req_Quit[13] = 0;
		Req_Quit[14] = 0;
		Req_Quit[15] = 0;
		Req_Quit[16] = (byte) 128;
		Req_Quit[17] = (byte)(userMac.length + 2);
		for (int i = 0; i < userMac.length; i++){
			Req_Quit[18 + i] = userMac[i];
		}
		return PublicV1.choose(type, Req_Quit, timeout_Sec, Bas_IP, bas_PORT);
	}
	

	public static boolean quit(int type, String Bas_IP, int bas_PORT, int timeout_Sec, byte SerialNo[], byte UserIP[], byte ReqID[])
	{
		byte Req_Quit[] = new byte[16];
		Req_Quit[0] = 1;
		Req_Quit[1] = 5;
		Req_Quit[2] = 0;
		Req_Quit[3] = 0;
		Req_Quit[4] = SerialNo[0];
		Req_Quit[5] = SerialNo[1];
		Req_Quit[6] = ReqID[0];
		Req_Quit[7] = ReqID[1];
		Req_Quit[8] = UserIP[0];
		Req_Quit[9] = UserIP[1];
		Req_Quit[10] = UserIP[2];
		Req_Quit[11] = UserIP[3];
		Req_Quit[12] = 0;
		Req_Quit[13] = 0;
		Req_Quit[14] = 0;
		Req_Quit[15] = 0;
		return PublicV1.choose(type, Req_Quit, timeout_Sec, Bas_IP, bas_PORT);
	}
}
