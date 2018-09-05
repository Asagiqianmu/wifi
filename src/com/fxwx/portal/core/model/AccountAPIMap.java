// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AccountAPIMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.HashMap;

public class AccountAPIMap
	implements Serializable
{

	private static final long serialVersionUID = 0x91723a21f7845761L;
	private HashMap accountAPIMap;
	private static AccountAPIMap instance = new AccountAPIMap();

	private AccountAPIMap()
	{
		accountAPIMap = new HashMap();
		accountAPIMap.put("url", "");
		accountAPIMap.put("state", "");
		accountAPIMap.put("publicurl", "");
		accountAPIMap.put("publicstate", "");
		accountAPIMap.put("autourl", "");
		accountAPIMap.put("autostate", "");
	}

	public static AccountAPIMap getInstance()
	{
		return instance;
	}

	public HashMap getAccountAPIMap()
	{
		return accountAPIMap;
	}

}
