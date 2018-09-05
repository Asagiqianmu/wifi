// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AutoLoginMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoLoginMap
	implements Serializable
{

	private static final long serialVersionUID = 0x3e42c735b1ab90d8L;
	private Map AutoLoginMap;
	private static AutoLoginMap instance = new AutoLoginMap();

	private AutoLoginMap()
	{
		AutoLoginMap = new ConcurrentHashMap();
	}

	public static AutoLoginMap getInstance()
	{
		return instance;
	}

	public Map getAutoLoginMap()
	{
		return AutoLoginMap;
	}

	public void setAutoLoginMap(Map AutoLoginMap)
	{
		this.AutoLoginMap = AutoLoginMap;
	}

}
