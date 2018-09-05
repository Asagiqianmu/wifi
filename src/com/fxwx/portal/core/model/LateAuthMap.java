// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LateAuthMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LateAuthMap
	implements Serializable
{

	private static final long serialVersionUID = 0x7870ac7be1edbd35L;
	private Map lateAuthMap;
	private static LateAuthMap instance = new LateAuthMap();

	private LateAuthMap()
	{
		lateAuthMap = new ConcurrentHashMap();
	}

	public static LateAuthMap getInstance()
	{
		return instance;
	}

	public Map getLateAuthMap()
	{
		return lateAuthMap;
	}

}
