// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AutoLoginCheckTimeMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoLoginCheckTimeMap
	implements Serializable
{

	private static final long serialVersionUID = 0xa4c2367469342a8bL;
	private Map AutoLoginCheckTimeMap;
	private static AutoLoginCheckTimeMap instance = new AutoLoginCheckTimeMap();

	private AutoLoginCheckTimeMap()
	{
		AutoLoginCheckTimeMap = new ConcurrentHashMap();
	}

	public static AutoLoginCheckTimeMap getInstance()
	{
		return instance;
	}

	public Map getAutoLoginCheckTimeMap()
	{
		return AutoLoginCheckTimeMap;
	}

	public void setAutoLoginCheckTimeMap(Map AutoLoginCheckTimeMap)
	{
		AutoLoginCheckTimeMap = AutoLoginCheckTimeMap;
	}

}
