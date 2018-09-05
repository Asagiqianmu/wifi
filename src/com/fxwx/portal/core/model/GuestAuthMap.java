// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GuestAuthMap.java

package com.fxwx.portal.core.model;

import java.io.Serializable;
import java.util.HashMap;

public class GuestAuthMap
	implements Serializable
{

	private static final long serialVersionUID = 0xad815b7444b75331L;
	private HashMap guestAuthMap;
	private static GuestAuthMap instance = new GuestAuthMap();

	private GuestAuthMap()
	{
		guestAuthMap = new HashMap();
	}

	public static GuestAuthMap getInstance()
	{
		return instance;
	}

	public HashMap getGuestAuthMap()
	{
		return guestAuthMap;
	}

}
