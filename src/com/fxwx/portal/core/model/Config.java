// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Config.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Config
	implements Serializable
{

	private static final long serialVersionUID = 0x9347daa6c9303733L;
	private Map configMap;
	private static Config instance = new Config();

	private Config()
	{
		configMap = new ConcurrentHashMap();
	}

	public static Config getInstance()
	{
		return instance;
	}

	public Map getConfigMap()
	{
		return configMap;
	}

	public void setConfigMap(Map configMap)
	{
		this.configMap = configMap;
	}

}
