// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ipMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ipMap
	implements Serializable
{

	private static final long serialVersionUID = 0x487493e2a9f5483bL;
	private Map ipmap;
	private static ipMap instance = new ipMap();

	private ipMap()
	{
		ipmap = new ConcurrentHashMap();
	}

	public static ipMap getInstance()
	{
		return instance;
	}

	public Map getIpmap()
	{
		return ipmap;
	}

	public void setIpmap(Map ipmap)
	{
		this.ipmap = ipmap;
	}

}
