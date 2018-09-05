// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PortalUtil.java

package com.fxwx.portal.core.service.utils;


public class PortalUtil
{

	public PortalUtil()
	{
	}

	public static String Getbyte2HexString(byte b[])
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++)
		{
			String hex = Integer.toHexString(b[i] & 0xff);
			if (hex.length() == 1)
				hex = (new StringBuilder(String.valueOf('0'))).append(hex).toString();
			sb.append(hex);
		}

		return (new StringBuilder("[")).append(sb.toString()).append("]").toString();
	}

	public static String Getbyte2MacString(byte b[])
	{
		StringBuilder sb = new StringBuilder();
		sb.append("");
		for (int i = 0; i < b.length; i++)
		{
			String hex = Integer.toHexString(b[i] & 0xff);
			if (hex.length() == 1)
				hex = (new StringBuilder(String.valueOf('0'))).append(hex).toString();
			sb.append(hex);
			if (i < b.length - 1)
				sb.append(":");
		}

		return sb.toString();
	}

	public static byte[] SerialNo()
	{
		byte SerialNo[] = new byte[2];
		short SerialNo_int = (short)(int)(1.0D + Math.random() * 32767D);
		for (int i = 0; i < 2; i++)
		{
			int offset = (SerialNo.length - 1 - i) * 8;
			SerialNo[i] = (byte)(SerialNo_int >>> offset & 0xff);
		}

		return SerialNo;
	}

	public static byte[] SerialNo(short SerialNo_int)
	{
		byte SerialNo[] = new byte[2];
		for (int i = 0; i < 2; i++)
		{
			int offset = (SerialNo.length - 1 - i) * 8;
			SerialNo[i] = (byte)(SerialNo_int >>> offset & 0xff);
		}

		return SerialNo;
	}
}
