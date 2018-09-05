package com.fxwx.util.wechar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Copyright (c) All Rights Reserved, 2016.
 * 版权所有                   dfgs Information Technology Co .,Ltd
 * @Project		 威富通基本配置项
 * @File		SwiftpassConfig.java
 * @Date		2016年12月26日 下午6:00:32
 * @Author		gyj
 */
public class SwiftpassConfig {
    
    /**
     * 威富通交易密钥
     */
    public static String key ;
    
    /**
     * 威富通商户号
     */
    public static String mch_id;
    
    /**
     * 威富通请求url
     */
    public static String req_url;
    
    /**
     * 通知url
     */
    public static String notify_url;
    
    
    public static String callback_url;
    
    
    static{
        Properties prop = new Properties();   
        InputStream in = SwiftpassConfig.class.getResourceAsStream("/commen.properties");   
        try {   
            prop.load(in);   
            key = prop.getProperty("wxkey").trim();   
            mch_id = prop.getProperty("mch_id").trim();   
            req_url = prop.getProperty("wxreq_url").trim();   
            notify_url = prop.getProperty("wxnotify_url").trim();  
            callback_url = prop.getProperty("wxcallback_url").trim();
        } catch (IOException e) {   
            e.printStackTrace();   
        } 
    }
    
}
