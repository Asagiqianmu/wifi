// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   isDo.java

package com.fxwx.portal.core.model;

import java.io.Serializable;

public class isDo
	implements Serializable
{

	private static final long serialVersionUID = 0x33b8e66c7a702c7cL;
	private static Long id = Long.valueOf(0L);
	private static isDo instance = new isDo();

	private isDo()
	{
	}

	public static isDo getInstance()
	{
		return instance;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long i)
	{
		id = i;
	}

}
