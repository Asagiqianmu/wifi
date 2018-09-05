package com.fxwx.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {
//	protected static Logger logger =  Logger.getLogger(BaseController.class);
	
	protected HttpServletRequest request;
	protected HttpServletResponse response; 
    protected HttpSession session; 
     
    protected Map<String,Object> map1 = new HashMap<String,Object>();
    protected Map<String,Object> map2 = new HashMap<String,Object>();
    //缓存所需要存入的map
    protected Map<String,Object> cacheMap = new HashMap<String,Object>();
    private void syso() {
		// TODO Auto-generated method stub

	}
}
