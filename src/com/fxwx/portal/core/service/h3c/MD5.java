// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MD5.java

package com.fxwx.portal.core.service.h3c;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5
{

	private byte md5Buffer[];

	public MD5()
	{
	}

	public void update(byte paramArrayOfByte[])
	{
		int i = md5Buffer != null ? md5Buffer.length : 0;
		int j = paramArrayOfByte != null ? paramArrayOfByte.length : 0;
		byte arrayOfByte[] = new byte[i + j];
		if (i != 0)
			System.arraycopy(md5Buffer, 0, arrayOfByte, 0, i);
		if (j != 0)
			System.arraycopy(paramArrayOfByte, 0, arrayOfByte, i, j);
		md5Buffer = arrayOfByte;
	}

	public void update(byte paramByte)
	{
		int i = md5Buffer != null ? md5Buffer.length : 0;
		byte arrayOfByte[] = new byte[i + 1];
		if (i != 0)
			System.arraycopy(md5Buffer, 0, arrayOfByte, 0, i);
		arrayOfByte[i] = paramByte;
		md5Buffer = arrayOfByte;
	}

	public void update(byte paramArrayOfByte[], int paramInt1, int paramInt2)
	{
		if (paramInt1 > paramInt2)
			return;
		int i = md5Buffer != null ? md5Buffer.length : 0;
		int j = paramArrayOfByte != null ? paramInt2 - paramInt1 : 0;
		byte arrayOfByte[] = new byte[i + j];
		if (i != 0)
			System.arraycopy(md5Buffer, 0, arrayOfByte, 0, i);
		if (j != 0)
			System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, i, j);
		md5Buffer = arrayOfByte;
	}

	public byte[] finalFunc()
	{
		MessageDigest localMessageDigest = null;
		if (md5Buffer == null)
			return null;
		try
		{
			localMessageDigest = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException nosuchalgorithmexception) { }
		if (localMessageDigest != null)
		{
			localMessageDigest.update(md5Buffer);
			return localMessageDigest.digest();
		} else
		{
			return null;
		}
	}
}
