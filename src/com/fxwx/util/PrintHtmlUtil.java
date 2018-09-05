package com.fxwx.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.view.ViewWrapper;



public class PrintHtmlUtil {
	
	public static ViewWrapper Render(String jsonString, HttpServletResponse response)  
    {  
        try {  
        	HttpServletHelper.WriteHtml(response, jsonString); 
            
        } catch (IOException e) {  
            e.printStackTrace();  
        }catch (Exception e) {
	        e.printStackTrace();
        }  
  
        return null;  
    }  

}
