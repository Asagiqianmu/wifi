// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MacLimitMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MacLimitMap
	implements Serializable
{

	private static final long serialVersionUID = 0x4a7a685d4850dbaeL;
	private Map MacLimitMap;
	private static MacLimitMap instance = new MacLimitMap();

	private MacLimitMap()
	{
		MacLimitMap = new ConcurrentHashMap();
	}

	public static MacLimitMap getInstance()
	{
		return instance;
	}

	public Map getMacLimitMap()
	{
		return MacLimitMap;
	}

	public void setMacLimitMap(Map macLimitMap)
	{
		MacLimitMap = macLimitMap;
	}

}
