package com.fxwx.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONSerializer;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.view.JspView;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.entiy.SitePaymentRecord;
import com.fxwx.entiy.SitePriceConfig;
import com.fxwx.service.impl.UserPayServiceImpl;
import com.fxwx.util.SetSystemProperty;
import com.fxwx.util.alim.AlipayNet;
import com.fxwx.util.wechar.SignUtils;
import com.fxwx.util.wechar.SwiftpassConfig;
import com.fxwx.util.wechar.WXMD5;
import com.fxwx.util.wechar.WecharPayConfig;
import com.fxwx.util.wechar.XmlUtils;
import com.util.thirdpay.Pay;

/**
 * Copyright (c) All Rights Reserved, 2016. 版权所有 dfgs Information Technology Co
 * .,Ltd
 * 
 * @Project kdf-pay
 * @File WechatPayController.java
 * @Date 2016年12月26日 上午9:31:44
 * @Author gyj
 */
@IocBean
@At("/wh")
public class WechatPayController {

	private static Logger logger = Logger.getLogger(WechatPayController.class);
 
	@Inject
	private UserPayServiceImpl userPayServiceImpl;
	
	@Inject
	private  AlipayNet alipayNet;

	@At("/wechatpay")
	public View wechatpay(HttpServletRequest request,HttpServletResponse responses) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		responses.setCharacterEncoding("UTF-8");
		responses.setHeader("Content-type", "text/html;charset=UTF-8");
		View successUrl = new JspView("mobile/weichatSubmit");
		View failUrl = new JspView("mobile/orderfail");
		Map<String, String> map = new HashMap<String, String>(7);
		PortalUser user = (PortalUser) request.getSession().getAttribute("user");// 添加用户到session
		if(user==null) user=userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId")+""));
		CloudSite site = (CloudSite)request.getSession().getAttribute("site");
		int siteId = Integer.valueOf(request.getParameter("siteId")+"");
		if(site==null) site = userPayServiceImpl.getCloudSiteById(siteId);
		if (user == null || site == null) {
			return failUrl;//下订单失败
		}
		map.put("userId", user.getId() + "");// 用户Id
		map.put("storeId", siteId + "");// 场所Id
		map.put("tenantId", site.getUser_id() + "");// 商户id
		map.put("payType", request.getParameter("priceConfig"));// 场所收费配置Id
		map.put("buyNum", request.getParameter("nums"));// 购买数量
		map.put("amount", request.getParameter("amount"));// 总金额
		map.put("priceNum", request.getParameter("price_num"));// 套餐类型
		map.put("priceName",new String(request.getParameter("price_Name").getBytes(),"UTF-8"));// 套餐名称
		String giveNum = request.getParameter("addMealNum");// 赠送的套餐数量
		String giveUnit = request.getParameter("addMealUnit");// 赠送的套餐单位
		map.put("addMealNum", giveNum);
		map.put("addMealUnit", giveUnit);
		map.put("mealType", request.getParameter("mealType"));// 选择的套餐类型 1----时间，2-----流量
		String checkResult = userPayServiceImpl.paramsCheck(map);
		if ("ok".equals(checkResult)) {// 校验通过
			SitePriceConfig scf = userPayServiceImpl.getSitePriceInfos(siteId,Integer.parseInt(map.get("payType")));
			String siteName = site.getSite_name();
			// 根据和当前时间的比较计算到期时间
			SiteCustomerInfo scii = userPayServiceImpl.getExpirationTimeByProuserid(user.getId(),siteId);
			// 计算用户到期时间或者流量
			String riqi = userPayServiceImpl.getUserCustomer(scii, scf, map);
			if ("1".equals(map.get("mealType"))) {// 用户购买的是时间套餐
				map.put("expireDate", riqi);
			} else {
				map.put("expireFlow", riqi);
			}
			String orderNum = Pay.getUuidOrderNumFromUserId(map.get("userId"));
			request.getSession().setAttribute("order",orderNum);
			System.out.print("开始的订单是:"+orderNum);
			String subject = "校园卡会员充值,按" + new String(map.get("priceName").getBytes(),"UTF-8") + "充值("+ siteName + ")";
			// 保存支付信息
			userPayServiceImpl.savePaymentinfo(orderNum, map,5);
	 
	        SortedMap<String,String> map1 = WecharPayConfig.config(request);
	        map1.put("callback_url",SetSystemProperty.propertyName("url")+SwiftpassConfig.callback_url+"?out_trade_no="+orderNum);
	        map1.put("out_trade_no", orderNum);
	        map1.put("body", subject);
	        map1.put("total_fee",((int)(Double.valueOf(map.get("amount"))*100))+"");//微信支付默认是分为单位.而前台传过来的是一元为单位
	        JSONObject jsonobject=new JSONObject();
	        jsonobject.put("company", "kdf_wifi");
	        map1.put("attach",JSONObject.toJSONString(jsonobject));
	        Map<String,String> params = SignUtils.paraFilter(map1);
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String sign = WXMD5.sign(preStr, "&key=" + SwiftpassConfig.key, "utf-8");
            map1.put("sign", sign);
            String reqUrl = SwiftpassConfig.req_url;
            CloseableHttpResponse response = null;
            CloseableHttpClient client = null;
            String res = null;
            try {
                HttpPost httpPost = new HttpPost(reqUrl);
                StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map1),"utf-8");
                httpPost.setEntity(entityParams);
                client = HttpClients.createDefault();
                response = client.execute(httpPost);
                if(response != null && response.getEntity() != null){
                    Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                    res = XmlUtils.toXml(resultMap);
                    if(resultMap.containsKey("sign")){
                        if(!SignUtils.checkParam(resultMap, SwiftpassConfig.key)){
                        	//签名失败
                        	return failUrl;
                        }else{
                            if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
                                String pay_info = resultMap.get("pay_info");
                                request.setAttribute("infourl", pay_info);
                                request.setAttribute("out_trade_no", map1.get("out_trade_no"));
                                request.setAttribute("total_fee", map1.get("total_fee"));
                                request.setAttribute("body", map1.get("body"));
                                return successUrl;
                            }else{
                            	return failUrl;
                            }
                        }
                    }else{
                    	return failUrl;
                    }
                }else{
                	return failUrl;
                }
            } catch (Exception e) {
            	return failUrl;
            } finally {
                if(response != null){
                    try {response.close();}catch(IOException e) {}
                }
                if(client != null){
                    try {client.close();} catch (IOException e) {}
                }
            }
        }else {// 校验不通过
		    return failUrl;
	    }
	}
	
	/**
	 * @Description  微信支付异步通知
	 * @date 2016年12月26日下午1:46:31
	 * @author guoyingjie
	 * @param req
	 * @param resp
	 */
	@At("/wechatNotify")
	public void wechatNotify(HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("utf-8");
			resp.setCharacterEncoding("utf-8");
			resp.setHeader("Content-type", "text/html;charset=UTF-8");
			String resString = XmlUtils.parseRequst(req);
			String respString = "fail";
			if (resString != null && !"".equals(resString)) {
				Map<String, String> map = XmlUtils.toMap(resString.getBytes(),"utf-8");
				String res = XmlUtils.toXml(map);
				if (map.containsKey("sign")) {
					if (!SignUtils.checkParam(map, SwiftpassConfig.key)) {
						respString = "fail";
					} else {
						String status = map.get("status");
						if (status != null && "0".equals(status)) {
							String result_code = map.get("result_code");
							if (result_code != null && "0".equals(result_code)) {
								String out_trade_no = map.get("out_trade_no");//商户订单号
								String transaction_id = map.get("transaction_id");//威富通交易号
								respString = this.wechatNotice(out_trade_no, transaction_id);
							}
						}
					}
				}
			}
			 resp.getWriter().write(respString);
		} catch (Exception e) {
			try {resp.getWriter().write("fail");} catch (IOException e1) {}
			return;
		}
	}
	
	/**
	 * @Description  微信支付异步业务处理
	 * @date 2016年12月26日下午1:48:10
	 * @author guoyingjie
	 * @param orderNum
	 * @param tradeNum
	 * @return
	 */
	public String wechatNotice(final String orderNum,final String tradeNum){
		//获取校园卡支付记录
		SitePaymentRecord payRecord=userPayServiceImpl.getRecordByOrderNum(orderNum);
		
		if(payRecord==null){//无支付记录
			logger.error("校园卡支付记录获取失败--orderNum:"+orderNum+";支付宝交易号："+tradeNum);
			return "fail";
		}
		//校园卡支付记录表状态校验
		if(payRecord.getIsFinish()==1||payRecord.getIsFinish()==-1){//支付状态为支付成功
			return "success";
		}
		//拿到paramMap,校验与系统中的用户状态是不是不一样。
		final Map<String,String> map=JSON.parseObject(payRecord.getParamJson(), Map.class);
		String checkResult= userPayServiceImpl.paramsCheck(map);
		if(!"ok".equals(checkResult)){
				logger.error("支付订单保留参数与系统现有数据不一致--orderNum:"+orderNum+";支付宝交易号："+tradeNum);
				userPayServiceImpl.updateFailReason("支付订单保留参数与系统现有数据不一致",orderNum);
				return "fail";
		}
		try {
	        //事务
	        Trans.exec(new Atom(){
	        	@Override
	        	public void run() {
	               //修改支付用户的到期 时间
	        		int i=userPayServiceImpl.changeUserExpireMeal(map);
	        		if(i!=1){
	        			userPayServiceImpl.updateFailReason("修改支付用户的到期 时间失败",orderNum);
	        			throw Lang.makeThrow("修改支付用户的到期 时间失败--orderNum:"+orderNum+";支付宝交易号："+tradeNum);
	        		}
	        		//校园卡账务信息表添加记录
	        		String userName=userPayServiceImpl.getUserById(Integer.parseInt(map.get("userId"))).getUserName();
	        		i=userPayServiceImpl.saveSchooleFinanceRecord(new BigDecimal(map.get("amount")),Integer.parseInt(map.get("storeId")),
	        				Integer.parseInt(map.get("userId")),userName,Integer.parseInt(map.get("buyNum")),map.get("priceName"),5);
	        		if(i!=1){
	        			userPayServiceImpl.updateFailReason("校园卡账务信息表添加记录失败",orderNum);
	        			throw Lang.makeThrow("校园卡账务信息表添加记录失败--orderNum:"+orderNum+";支付宝交易号："+tradeNum);
	        		}
	        		//校园卡支付记录表状态修改为支付成功
	        		i=userPayServiceImpl.updateToFinish(tradeNum,orderNum);
	        		if(i!=1){//执行不成功
	        			userPayServiceImpl.updateFailReason("校园卡支付记录表状态修改失败：",orderNum);
	        			throw Lang.makeThrow("校园卡支付记录表状态修改失败--orderNum:"+orderNum+";支付宝交易号："+tradeNum);
	        		}
	        		boolean y=userPayServiceImpl.updateCollect(new BigDecimal(map.get("amount")), Integer.parseInt(map.get("storeId")), Integer.parseInt(map.get("tenantId")));
					if(!y)
						throw Lang.makeThrow("计费表用户统计或场所统计插入或更新未成功"+ orderNum);
	        	}
	        });
        }catch (Exception e) {
        	logger.error("支付过程事务故障",e);
        	return "fail";
        }
		return "success"; 
	}
	
	/**
	 * 待支付用户使用微信充值
	 * @param request
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@At("/toWechat")
	public View toWechat(HttpServletRequest request,HttpSession session,HttpServletResponse responses) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		responses.setCharacterEncoding("UTF-8");
		responses.setHeader("Content-type", "text/html;charset=UTF-8");
		View successUrl = new JspView("mobile/weichatSubmit");
		View failUrl = new JspView("mobile/orderfail");
		
		PortalUser user=(PortalUser) request.getSession().getAttribute("user");//添加用户到session
		if(user==null) user=userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId")+""));
		CloudSite cloudSite =(CloudSite)request.getSession().getAttribute("site");
		if(cloudSite==null) cloudSite=userPayServiceImpl.getCloudSiteById(Integer.valueOf(request.getParameter("siteId")+""));
		if(user==null||cloudSite==null){
	        return failUrl;
		}
		String maps=request.getParameter("maps");
		Map<String,Object> json = (Map<String,Object>) JSONSerializer.toJSON(maps.replace("[","{").replaceAll("]","}"));
		String priceName = (json.get("name")+"").split("-")[(json.get("name")+"").split("-").length-1];
		String orderNum=json.get("orderNum")+"";//订单号
		String money=json.get("amount")+"";
		String subject="校园卡会员充值,按"+priceName+"充值("+cloudSite.getSite_name()+")";
		boolean result=userPayServiceImpl.checkeOrder(orderNum, cloudSite.getId(), user.getId(),money,priceName);
		if(result){
			 	SortedMap<String,String> map1 = WecharPayConfig.config(request);
		        map1.put("callback_url",SetSystemProperty.propertyName("url")+SwiftpassConfig.callback_url+"?out_trade_no="+orderNum);
		        map1.put("out_trade_no", orderNum);
		        map1.put("body", subject);
		        map1.put("total_fee",((int)(Double.valueOf(money)*100))+"");//微信支付默认是分为单位.而前台传过来的是一元为单位
	            Map<String,String> params = SignUtils.paraFilter(map1);
	            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
	            SignUtils.buildPayParams(buf,params,false);
	            String preStr = buf.toString();
	            String sign = WXMD5.sign(preStr, "&key=" + SwiftpassConfig.key, "utf-8");
	            map1.put("sign", sign);
	            String reqUrl = SwiftpassConfig.req_url;
	            CloseableHttpResponse response = null;
	            CloseableHttpClient client = null;
	            String res = null;
	            try {
	                HttpPost httpPost = new HttpPost(reqUrl);
	                StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map1),"utf-8");
	                httpPost.setEntity(entityParams);
	                client = HttpClients.createDefault();
	                response = client.execute(httpPost);
	                if(response != null && response.getEntity() != null){
	                    Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
	                    res = XmlUtils.toXml(resultMap);
	                    if(resultMap.containsKey("sign")){
	                        if(!SignUtils.checkParam(resultMap, SwiftpassConfig.key)){
	                        	//签名失败
	                        	return failUrl;
	                        }else{
	                            if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
	                                String pay_info = resultMap.get("pay_info");
	                                request.setAttribute("infourl", pay_info);
	                                request.setAttribute("out_trade_no", map1.get("out_trade_no"));
	                                request.setAttribute("total_fee", map1.get("total_fee"));
	                                request.setAttribute("body", map1.get("body"));
	                                return successUrl;
	                            }else{
	                            	return failUrl;
	                            }
	                        }
	                    }else{
	                    	return failUrl;
	                    }
	                }else{
	                	return failUrl;
	                }
	            } catch (Exception e) {
	            	return failUrl;
	            } finally {
	                if(response != null){
	                    try {response.close();}catch(IOException e) {}
	                }
	                if(client != null){
	                    try {client.close();} catch (IOException e) {}
	                }
	            }
		}else{
			return failUrl;
		}
		
		
	}
	 
}
