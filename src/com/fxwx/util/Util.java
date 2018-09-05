package com.fxwx.util;

/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Utilities for encoding and decoding the Base64 representation of binary data.
 * See RFCs <a href="http://www.ietf.org/rfc/rfc2045.txt">2045</a> and <a
 * href="http://www.ietf.org/rfc/rfc3548.txt">3548</a>.
 * 
 * BASE64 基于Android Open Source Project修改
 * 
 * @author gaozhenhai
 * @since 2013.01.15
 * @version 1.0.0_1
 * 
 */
public class Util {
	 public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower)  
	    {  
	        String buff = "";  
	        Map<String, String> tmpMap = paraMap;  
	        try  
	        {  
	            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());  
	            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）  
	            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()  
	            {  
	   
	                @Override  
	                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)  
	                {  
	                    return (o1.getKey()).toString().compareTo(o2.getKey());  
	                }  
	            });  
	            // 构造URL 键值对的格式  
	            StringBuilder buf = new StringBuilder();  
	            for (Map.Entry<String, String> item : infoIds)  
	            {  
	                if (StringUtils.isNotBlank(item.getKey()))  
	                {  
	                    String key = item.getKey();  
	                    String val = item.getValue();  
	                    if (urlEncode)  
	                    {  
	                        val = URLEncoder.encode(val, "utf-8");  
	                    }  
	                    if (keyToLower)  
	                    {  
	                        buf.append(key.toLowerCase() + "=" + val);  
	                    } else  
	                    {  
	                        buf.append(key + "=" + val);  
	                    }  
	                    buf.append("&");  
	                }  
	   
	            }  
	            buff = buf.toString();  
	            if (buff.isEmpty() == false)  
	            {  
	                buff = buff.substring(0, buff.length() - 1);  
	            }  
	        } catch (Exception e)  
	        {  
	           return null;  
	        }  
	        return buff;  
	    }  
	
}