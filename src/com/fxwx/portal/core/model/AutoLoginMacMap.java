// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AutoLoginMacMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoLoginMacMap
	implements Serializable
{

	private static final long serialVersionUID = 0x4a7a685d4850dbaeL;
	private Map AutoLoginMacMap;
	private static AutoLoginMacMap instance = new AutoLoginMacMap();

	private AutoLoginMacMap()
	{
		AutoLoginMacMap = new ConcurrentHashMap();
	}

	public static AutoLoginMacMap getInstance()
	{
		return instance;
	}

	public Map getAutoLoginMacMap()
	{
		return AutoLoginMacMap;
	}

	public void setAutoLoginMacMap(Map AutoLoginMacMap)
	{
		AutoLoginMacMap = AutoLoginMacMap;
	}

}
