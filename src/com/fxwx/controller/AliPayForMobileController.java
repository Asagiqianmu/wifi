package com.fxwx.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.view.JspView;

import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.entiy.SitePriceConfig;
import com.fxwx.service.impl.UserPayServiceImpl;
import com.fxwx.util.PrintHtmlUtil;
import com.fxwx.util.alim.AlipayNet;
import com.util.thirdpay.Pay;

 

/**
 * Copyright (c) All Rights Reserved, 2017.
 * 版权所有                   dfgs Information Technology Co .,Ltd
 * @Project		cloudPortal
 * @File		AliPayForMobileController.java
 * @Date		2017年1月12日 上午9:45:25
 * @Author		gyj ----->此类主要处理手机端的支付宝支付 
 */
@IocBean
@At("/pay")
public class AliPayForMobileController {
	private static Logger logger = Logger.getLogger(AliPayForMobileController.class);

	@Inject
	private UserPayServiceImpl userPayServiceImpl;
	
	@Inject
	private  AlipayNet alipayNet;
	
	/**
	 * 支付宝支付
	 * @param id
	 * @param response
	 * @return
	 * 
	 */
	@At("/aliPay")
	public View aliPay(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		//校验不成功支直接反回到下订单失败
		View  apViewWrapper =  new JspView("mobile/orderFail");
		Map<String ,String>map=new HashMap<String,String>(7);
		PortalUser user=(PortalUser) request.getSession().getAttribute("user");//添加用户到session
		if(user==null) user=userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId")+""));
		CloudSite cloudSite =(CloudSite)request.getSession().getAttribute("site");
		if(cloudSite==null) cloudSite=userPayServiceImpl.getCloudSiteById(Integer.valueOf(request.getParameter("siteId")+""));
		if(user==null||cloudSite==null){
	        return apViewWrapper;
		}
		map.put("userId",user.getId()+"");//用户Id
		map.put("storeId",cloudSite.getId()+"");//场所Id
		map.put("tenantId", cloudSite.getUser_id()+"");//商户id
		map.put("payType",request.getParameter("priceConfig"));//场所收费配置Id
		map.put("buyNum",request.getParameter("nums"));//购买数量
		map.put("amount",request.getParameter("amount"));//总金额
		map.put("priceNum", request.getParameter("price_num"));//套餐类型
		map.put("priceName",request.getParameter("price_Name"));//套餐名称
		
		String giveNum=request.getParameter("addMealNum");//赠送的套餐数量
		String giveUnit=request.getParameter("addMealUnit");//赠送的套餐单位
		map.put("addMealNum",giveNum);
		map.put("addMealUnit",giveUnit);
		map.put("mealType",request.getParameter("mealType"));//选择的套餐类型 1----时间，2-----流量
		//校验
		String checkResult=userPayServiceImpl.paramsCheck(map);
		if("ok".equals(checkResult)){//校验通过
			
			SitePriceConfig scf=userPayServiceImpl.getSitePriceInfos(cloudSite.getId(),Integer.parseInt(map.get("payType")));
			String siteName=cloudSite.getSite_name();
			
			//根据和当前时间的比较计算到期时间
			SiteCustomerInfo scii =userPayServiceImpl.getExpirationTimeByProuserid(user.getId(),cloudSite.getId());
			//计算用户到期时间或者流量
			String riqi=userPayServiceImpl.getUserCustomer(scii, scf, map);
			if("1".equals(map.get("mealType"))){//用户购买的是时间套餐
				map.put("expireDate",riqi);
			}else{
				map.put("expireFlow",riqi);
			}
			
			String orderNum=Pay.getUuidOrderNumFromUserId(map.get("userId"));
			String subject="校园卡会员充值,按"+map.get("priceName")+"充值("+siteName+")";
			
			//保存支付信息
			userPayServiceImpl.savePaymentinfo(orderNum,map,1);
			//跳转到支付宝
			String html=alipayNet.getPayHTML(orderNum,subject, request.getParameter("amount"));
			logger.error("------------"+html);
			return PrintHtmlUtil.Render(html, response);
		}else{//校验不通过
	        return apViewWrapper;
		}
	}
	 
	/**
	 * 支付宝通知
	 * @param request
	 * @param response
	 */
	@At("/aliPayNotify")
	public void aliPayNotify(HttpServletRequest request, HttpServletResponse response){
		try {
			userPayServiceImpl.Notify(request, response);
			return;
        }catch (Exception e) {
            try {
            	PrintWriter out = response.getWriter();
	            out.write("fail");
            }catch (IOException e1) {
	            e1.printStackTrace();
            }
        }
	}
	/**
	 * 待支付用户点击支付宝充值
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@At("/toAlipay")
	public View toAlipay(HttpServletRequest request,HttpServletResponse response){
		//校验不成功支直接反回到下订单失败
		View  apViewWrapper =  new JspView("mobile/orderFail");
		PortalUser user=(PortalUser) request.getSession().getAttribute("user");//添加用户到session
		if(user==null) user=userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId")+""));
		CloudSite cloudSite =(CloudSite)request.getSession().getAttribute("site");
		if(cloudSite==null) cloudSite=userPayServiceImpl.getCloudSiteById(Integer.valueOf(request.getParameter("siteId")+""));
		if(user==null||cloudSite==null){
	        return apViewWrapper;
		}
		String params=request.getParameter("maps");
		Map<String,Object> json = (Map<String,Object>) JSONSerializer.toJSON(params.replace("[","{").replaceAll("]","}"));
		String priceName = (json.get("name")+"").split("-")[(json.get("name")+"").split("-").length-1];
		String orderNum=json.get("orderNum")+"";//订单号
		String money=json.get("amount")+"";
		String subject="校园卡会员充值,按"+priceName+"充值("+cloudSite.getSite_name()+")";
		boolean res=userPayServiceImpl.checkeOrder(orderNum, cloudSite.getId(), user.getId(),money,priceName);
		if(res){
			//跳转到支付宝
			String html=alipayNet.getPayHTML(orderNum,subject, money);
	        return PrintHtmlUtil.Render(html, response);
		}else{//校验不通过
			return apViewWrapper;
		}
	} 
	 
	@At("/test")//测试
	public String test(HttpServletRequest request,HttpServletResponse response){
		userPayServiceImpl.AliPayNotify("1b40113ff8524543ab42a6caf506c505", "2017051721001004700214249905");
		return "";
	}
}
