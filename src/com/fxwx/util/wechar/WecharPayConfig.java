package com.fxwx.util.wechar;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.fxwx.util.SetSystemProperty;

 
/**
 * Copyright (c) All Rights Reserved, 2016.
 * 版权所有                   kdf Information Technology Co .,Ltd
 * @Project		deck 威富通支付配置类
 * @File		WecharPayConfig.java
 * @Date		2016年12月26日 上午9:38:20
 * @Author		gyj
 */
@SuppressWarnings("all")
public class WecharPayConfig {

	
	public static SortedMap<String,String> config(HttpServletRequest request){
		String userAgent = request.getHeader("user-agent");
	    SortedMap returnMap = new TreeMap();
	    returnMap.put("service", "pay.weixin.wappay");
	    returnMap.put("version", "2.0");
	    returnMap.put("charset", "UTF-8");
	    returnMap.put("sign_type", "MD5");
	    returnMap.put("mch_id", SwiftpassConfig.mch_id);
	    returnMap.put("notify_url", SetSystemProperty.propertyName("url")+SwiftpassConfig.notify_url);
	    returnMap.put("nonce_str", String.valueOf(new Date().getTime()));
	    returnMap.put("mch_create_ip",request.getRemoteAddr());
	    returnMap.put("mch_app_name",(SetSystemProperty.propertyName("url")+"").substring(0,(SetSystemProperty.propertyName("url")+"").lastIndexOf("/")));
	    returnMap.put("mch_app_id",(SetSystemProperty.propertyName("url")+"").substring(0,(SetSystemProperty.propertyName("url")+"").lastIndexOf("/")));
	    if(userAgent.indexOf("Android")>-1){
	    	returnMap.put("device_info","AND_WAP");
	    	
	    }else{
	    	returnMap.put("device_info","iOS_WAP");
	    }
		return returnMap;
	}
	 
}
