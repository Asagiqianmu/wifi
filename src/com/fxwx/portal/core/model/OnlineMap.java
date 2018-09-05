// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   OnlineMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OnlineMap
	implements Serializable
{

	private static final long serialVersionUID = 0x487493e2a9f5483bL;
	private Map OnlineUserMap;
	private static OnlineMap instance = new OnlineMap();

	private OnlineMap()
	{
		OnlineUserMap = new ConcurrentHashMap();
	}

	public static OnlineMap getInstance()
	{
		return instance;
	}

	public static void setInstance(OnlineMap instance)
	{
		instance = instance;
	}

	public Map getOnlineUserMap()
	{
		return OnlineUserMap;
	}

	public void setOnlineUserMap(Map onlineUserMap)
	{
		OnlineUserMap = onlineUserMap;
	}

}
