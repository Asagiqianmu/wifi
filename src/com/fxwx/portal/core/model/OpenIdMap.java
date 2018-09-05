// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   OpenIdMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OpenIdMap
	implements Serializable
{

	private static final long serialVersionUID = 0x3becbed62fc61d1eL;
	private Map openIdMap;
	private static OpenIdMap instance = new OpenIdMap();

	private OpenIdMap()
	{
		openIdMap = new ConcurrentHashMap();
	}

	public static OpenIdMap getInstance()
	{
		return instance;
	}

	public Map getOpenIdMap()
	{
		return openIdMap;
	}

}
