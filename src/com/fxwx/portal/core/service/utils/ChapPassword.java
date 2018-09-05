// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ChapPassword.java

package com.fxwx.portal.core.service.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;

import com.fxwx.portal.core.model.Config;

// Referenced classes of package com.leeson.portal.core.service.utils:
//			PortalUtil

public class ChapPassword
{

	private static Logger log = Logger.getLogger(ChapPassword.class);
	private static Config config = Config.getInstance();

	public ChapPassword()
	{
	}

	public static byte[] MK_ChapPwd(byte ReqID[], byte Challenge[], byte usp[])
		throws UnsupportedEncodingException
	{
		byte ChapPwd[] = new byte[16];
		byte buf[] = new byte[1 + usp.length + Challenge.length];
		buf[0] = ReqID[1];
		for (int i = 0; i < usp.length; i++)
			buf[1 + i] = usp[i];

		for (int i = 0; i < Challenge.length; i++)
			buf[1 + usp.length + i] = Challenge[i];

		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(buf);
			ChapPwd = md.digest();
			if (/*basConfig.getIsdebug().equals("1")*/true)
				log.info((new StringBuilder("生成Chap-Password")).append(PortalUtil.Getbyte2HexString(ChapPwd)).toString());
		}
		catch (NoSuchAlgorithmException e)
		{
			if (/*basConfig.getIsdebug().equals("1")*/true)
				log.info("生成Chap-Password出错！");
		}
		return ChapPwd;
	}

}
