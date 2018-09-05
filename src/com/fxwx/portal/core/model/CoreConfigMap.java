// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CoreConfigMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.HashMap;

public class CoreConfigMap
	implements Serializable
{

	private static final long serialVersionUID = 0x5d86bd3e658a3bbbL;
	private HashMap coreConfigMap;
	private static CoreConfigMap instance = new CoreConfigMap();

	private CoreConfigMap()
	{
		coreConfigMap = new HashMap();
		String core[] = new String[2];
		core[0] = "http://www.openportal.com.cn";
		core[1] = "100";
		coreConfigMap.put("core", core);
	}

	public static CoreConfigMap getInstance()
	{
		return instance;
	}

	public HashMap getCoreConfigMap()
	{
		return coreConfigMap;
	}

}
