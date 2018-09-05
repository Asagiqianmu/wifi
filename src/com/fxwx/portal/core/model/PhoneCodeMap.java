// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PhoneCodeMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PhoneCodeMap
	implements Serializable
{

	private static final long serialVersionUID = 0xa7fe2a0ac5b330d7L;
	private Map phoneCodeMap;
	private static PhoneCodeMap instance = new PhoneCodeMap();

	private PhoneCodeMap()
	{
		phoneCodeMap = new ConcurrentHashMap();
	}

	public static PhoneCodeMap getInstance()
	{
		return instance;
	}

	public Map getPhoneCodeMap()
	{
		return phoneCodeMap;
	}

}
