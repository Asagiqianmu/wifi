// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MagicMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MagicMap
	implements Serializable
{

	private static final long serialVersionUID = 0x3becbed62fc61d1eL;
	private Map magicMap;
	private static MagicMap instance = new MagicMap();

	private MagicMap()
	{
		magicMap = new ConcurrentHashMap();
	}

	public static MagicMap getInstance()
	{
		return instance;
	}

	public Map getMagicMap()
	{
		return magicMap;
	}

}
