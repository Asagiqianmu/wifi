// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AppTokenMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.HashMap;

public class AppTokenMap
	implements Serializable
{

	private static final long serialVersionUID = 0x9072434d6804d482L;
	private HashMap TokenMap;
	private static AppTokenMap instance = new AppTokenMap();

	private AppTokenMap()
	{
		TokenMap = new HashMap();
	}

	public static AppTokenMap getInstance()
	{
		return instance;
	}

	public static void setInstance(AppTokenMap instance)
	{
		instance = instance;
	}

	public HashMap getTokenMap()
	{
		return TokenMap;
	}

}
