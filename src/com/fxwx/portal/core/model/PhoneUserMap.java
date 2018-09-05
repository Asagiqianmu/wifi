// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PhoneUserMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.HashMap;

public class PhoneUserMap
	implements Serializable
{

	private static final long serialVersionUID = 0xa7fe2a0ac5b330d7L;
	private HashMap phoneUserMap;
	private static PhoneUserMap instance = new PhoneUserMap();

	private PhoneUserMap()
	{
		phoneUserMap = new HashMap();
	}

	public HashMap getPhoneUserMap()
	{
		return phoneUserMap;
	}

	public static PhoneUserMap getInstance()
	{
		return instance;
	}

}
