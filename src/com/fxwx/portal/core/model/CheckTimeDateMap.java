// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CheckTimeDateMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CheckTimeDateMap
	implements Serializable
{

	private static final long serialVersionUID = 0xa4c2367469342a8bL;
	private Map CheckTimeDateMap;
	private static CheckTimeDateMap instance = new CheckTimeDateMap();

	private CheckTimeDateMap()
	{
		CheckTimeDateMap = new ConcurrentHashMap();
	}

	public static CheckTimeDateMap getInstance()
	{
		return instance;
	}

	public Map getCheckTimeDateMap()
	{
		return CheckTimeDateMap;
	}

	public void setCheckTimeDateMap(Map checkTimeDateMap)
	{
		CheckTimeDateMap = checkTimeDateMap;
	}

}
