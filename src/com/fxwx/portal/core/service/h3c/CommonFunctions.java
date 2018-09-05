// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CommonFunctions.java

package com.fxwx.portal.core.service.h3c;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Arrays;

public class CommonFunctions
{

	public CommonFunctions()
	{
	}

	public static BigInteger ipv4ToBytes(String ipv4)
	{
		byte ret[] = new byte[5];
		ret[0] = 0;
		int position1 = ipv4.indexOf(".");
		int position2 = ipv4.indexOf(".", position1 + 1);
		int position3 = ipv4.indexOf(".", position2 + 1);
		ret[1] = (byte)Integer.parseInt(ipv4.substring(0, position1));
		ret[2] = (byte)Integer.parseInt(ipv4.substring(position1 + 1, position2));
		ret[3] = (byte)Integer.parseInt(ipv4.substring(position2 + 1, position3));
		ret[4] = (byte)Integer.parseInt(ipv4.substring(position3 + 1));
		BigInteger ipBig = new BigInteger(ret);
		return ipBig;
	}

	public static byte[] convertIntToByteArray(int paramInt)
	{
		byte arrayOfByte[] = new byte[4];
		int i = 0;
		for (int j = 0; j < 4; j++)
		{
			i = 4 - j - 1;
			arrayOfByte[i] = (byte)(paramInt >> j * 8 & 0xff);
		}

		return arrayOfByte;
	}

	public static byte[] convertBigIntegerTo16Bytes(BigInteger paramBigInteger)
	{
		byte arrayOfByte1[] = new byte[16];
		Arrays.fill(arrayOfByte1, (byte)0);
		byte arrayOfByte2[] = paramBigInteger.toByteArray();
		System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 16 - arrayOfByte2.length, arrayOfByte2.length);
		return arrayOfByte1;
	}

	public static InetAddress convertIntToInetAddress(int paramInt)
	{
		try {
			
			return InetAddress.getByAddress(convertIntToByteArray(paramInt));
		} catch (Exception e) {
			return null;
		}
	}
}
