package com.fxwx.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewModel;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.view.JspView;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.alibaba.fastjson.JSON;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.entiy.SitePaymentRecord;
import com.fxwx.entiy.SitePriceConfig;
import com.fxwx.service.impl.UserPayServiceImpl;
import com.fxwx.util.SetSystemProperty;
import com.fxwx.util.alim.AlipayNet;
import com.fxwx.util.alipc.AlipayConfig;
import com.fxwx.util.alipc.AlipayNotify;
import com.fxwx.util.alipc.AlipaySubmit;
import com.util.thirdpay.Pay;
 
 


/**
 * Copyright (c) All Rights Reserved, 2017.
 * 版权所有                   dfgs Information Technology Co .,Ltd
 * @Project		UnitePortal
 * @File		AliPayForPcController.java
 * @Date		2017年1月12日 上午9:47:39
 * @Author		gyj------>此类主要是处理pc端的支付宝支付
 */
@IocBean
@At("/rechargeLog")
public class AliPayForPcController {
	private static Logger logger = Logger.getLogger(AliPayForPcController.class);
	@Inject
	private UserPayServiceImpl userPayServiceImpl;
	
	@Inject
	private  AlipayNet alipayNet;
	 
	/** 东方高盛的支付宝账号,写死的 */
	private final String seller_email = "support@solarsys.cn";

	/**
	 * 调用我支付宝的用户必须在他本地调用该方法 然后提交到我的alipay
	 * */
	@At("/gopay")
	@Ok("redirect:/rechargeLog/alipayapi")
	public View gopay(HttpServletRequest request, HttpServletResponse response,ViewModel model) throws ServletException, IOException {
		Map<String ,String>map=new HashMap<String,String>(7);
		PortalUser user=(PortalUser) request.getSession().getAttribute("user");//添加用户到session
		//获取用户ID
		if(user==null) user=userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId")+""));
		CloudSite cloudSite =(CloudSite)request.getSession().getAttribute("site");
		//获取场所ID
		if(cloudSite==null) cloudSite=userPayServiceImpl.getCloudSiteById(Integer.valueOf(request.getParameter("siteId")+""));
		if(user==null||cloudSite==null){
			return new JspView("pc/orderFail");
		}
		// 抽取必填参数
		map.put("userId", user.getId() + "");// 用户Id
		map.put("storeId", cloudSite.getId() + "");// 场所Id
		map.put("tenantId", cloudSite.getUser_id()+"");//商户id
		map.put("payType", request.getParameter("priceConfig"));// 场所收费配置Id
		map.put("buyNum", request.getParameter("nums"));// 购买数量
		map.put("amount", request.getParameter("amount"));// 总金额
		map.put("priceNum",request.getParameter("price_num"));//套餐
		String giveNum=request.getParameter("addMealNum");//赠送的套餐数量
		String giveUnit=request.getParameter("addMealUnit");//赠送的套餐单位
		map.put("addMealNum",giveNum);
		map.put("addMealUnit",giveUnit);
		map.put("mealType",request.getParameter("mealType"));//选择的套餐类型 1----时间，2-----流量
		
//		SitePriceConfig scf = userPayServiceImpl.getSitePriceInfos(cloudSite.getId(), Integer.parseInt(map.get("payType")));
		String myNum = Pay.getUuidOrderNumFromUserId(String.valueOf(user.getId())); // 根据userId生成订单号
		logger.error(request.getParameter("amount"));
		String total_fee = request.getParameter("amount");
		String subject = request.getParameter("price_name");
		String siteName=userPayServiceImpl.getCloudSiteById(cloudSite.getId()).getSite_name();
//		model.addv("myNum", myNum); // 订单号
//		model.addv("subject","校园卡会员充值,按" + subject+"充值("+siteName+")"); // 用途
//		model.addv("total_fee", total_fee); // 金额
//		model.addv("seller_email", seller_email);
		map.put("priceName",subject);//套餐名称
		map.put("myNum",myNum);//套餐名称
		map.put("seller_email",seller_email);
		map.put("subject","校园卡会员充值,按" + subject+"充值("+siteName+")");// 用途
		map.put("total_fee",total_fee);// 金额
		request.getSession().setAttribute("map", map);
		return null;//走默认视图/rechargeLog/alipayapi
	}

	
	/**
	 * 调用我支付宝的用户必须在他本地调用该方法 然后提交到我的alipay
	 * */
	@At("/toGoPay")
	@Ok("redirect:/rechargeLog/alipayapi")
	public View toGoPay(HttpServletRequest request, HttpServletResponse response,ViewModel model) throws ServletException, IOException {
 		Map<String ,String>map=new HashMap<String,String>(8);
		PortalUser user=(PortalUser) request.getSession().getAttribute("user");//添加用户到session
		//获取用户ID
		if(user==null) user=userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId")+""));
		CloudSite cloudSite =(CloudSite)request.getSession().getAttribute("site");
		//获取场所ID
		if(cloudSite==null) cloudSite=userPayServiceImpl.getCloudSiteById(Integer.valueOf(request.getParameter("siteId")+""));
		if(user==null||cloudSite==null){
			return new JspView("pc/orderFail");
		}
		// 抽取必填参数
		map.put("userId", user.getId() + "");// 用户Id
		map.put("storeId", cloudSite.getId() + "");// 场所Id
		map.put("tenantId", cloudSite.getUser_id()+"");//商户id
		map.put("payType",request.getParameter("idss"));//场所收费配置Id
		map.put("buyNum", request.getParameter("nums"));// 购买数量
		map.put("amount", request.getParameter("amount"));// 总金额
		map.put("myNum",request.getParameter("orderNum"));//订单号
		map.put("priceNum",request.getParameter("allPirce"));//套餐
		map.put("total_fee",request.getParameter("amount"));// 金额
		String subject = request.getParameter("price_Name");
		String siteName=userPayServiceImpl.getCloudSiteById(cloudSite.getId()).getSite_name();
		map.put("priceName",subject);//套餐名称
		map.put("seller_email",seller_email);
		map.put("addMealNum",request.getParameter("addMealNum"));
		map.put("addMealUnit",request.getParameter("addMealUnit"));
		map.put("mealType",request.getParameter("mealType"));
		map.put("subject","校园卡会员充值,按" + subject+"充值("+siteName+")");// 用途
		request.getSession().setAttribute("map", map);
		return null;//走默认视图/rechargeLog/alipayapi
	}

	/** 接收充值信息的提交 
	 * @throws IOException */
	@At("/alipayapi")
	public View alipayapi(HttpServletRequest request,HttpServletResponse response) throws IOException {
		Map<String,String> map = (Map<String,String>) request.getSession().getAttribute("map");
	   Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		  while (it.hasNext()) {
		   Map.Entry<String, String> entry = it.next();
		  }
		// 校验
		String checkResult = userPayServiceImpl.paramsCheck(map);
		if ("ok".equals(checkResult)) {// 校验通过
			CloudSite site = userPayServiceImpl.getCloudSiteById(Integer.valueOf(map.get("storeId")+""));
			SitePriceConfig scf = userPayServiceImpl.getSitePriceInfos(site.getId(), Integer.parseInt((String) map.get("payType")));
			//根据和当前时间的比较计算到期时间
			PortalUser user=(PortalUser) request.getSession().getAttribute("user");//添加用户到session
			if(user==null) user=userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId")+""));
			CloudSite cloudSite =(CloudSite)request.getSession().getAttribute("site");
			if(cloudSite==null) cloudSite=userPayServiceImpl.getCloudSiteById(Integer.valueOf(request.getParameter("siteId")+""));
			if(user==null||cloudSite==null){
				return new JspView("pc/orderFail");
			}
			SiteCustomerInfo scii =userPayServiceImpl.getExpirationTimeByProuserid(user.getId(),site.getId());
			//计算用户到期时间或者流量
			String riqi=userPayServiceImpl.getUserCustomer(scii, scf, map);
			if("1".equals(map.get("mealType"))){//用户购买的是时间套餐
				map.put("expireDate",riqi);
			}else{
				map.put("expireFlow",riqi);
			}
			logger.error(map.get("myNum"));
//			String orderNum = request.getParameter("myNum");
			String orderNum =map.get("myNum");
			// 保存支付信息for (String key : map.keySet()) {
			userPayServiceImpl.savePaymentinfo(orderNum, map,1);
			String out_trade_no = String.valueOf(orderNum);
			// 根据订单号,获取userId--------------------------------------------
			String strTemp = out_trade_no.substring(out_trade_no.length() - 2);
			Integer len = 0;
			try {
				len = Integer.valueOf(strTemp);
			} catch (Exception e) {
				logger.error("订单号错误,转换后两位时错误--RechargeLogController-167行");
				 return new JspView("pc/orderFail");
			}
			String userId = out_trade_no.substring(2, 2 + len);
			// 验证userId是否指数字
			try {
				int num = Integer.valueOf(userId);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("--支付时没有userId,或者userId错误---RechargeLogController-176行");
				 return new JspView("pc/orderFail");
			}
			String newUserId = "";
			// 循环结束得到
			for (int i = 0; i < userId.length(); i++) {
				int x = Integer.valueOf(userId.substring(i, i + 1));
				int y = 0;
				if (x + 10 - 9 == 10) {
					y = 0;
				} else {
					y = x + 10 - 9;
				}
				newUserId += String.valueOf(y);
			}
			// ----------------------------------------判读订单号是否合法结束
			// 支付类型
			String payment_type = "1";
			String notify_url = SetSystemProperty.propertyName("url")+SetSystemProperty.propertyName("UnitePortal")+"/rechargeLog/notify";
			String return_url = SetSystemProperty.propertyName("url")+SetSystemProperty.propertyName("UnitePortal")+"/priceConfig/payResult";
			//String return_url = "http://localhost/deck/pc/result.jsp";
			// 卖家支付宝帐户
			String seller_email = map.get("seller_email");
			// 订单名称 "校园卡会员充值,按"+(0==scf.getPrice_type()?"小时":(1==scf.getPrice_type()?"天":"月"));
			String subject =map.get("subject");
			// 必填
			// 付款金额
			String total_fee =map.get("total_fee");
			// 防钓鱼时间戳
			String anti_phishing_key = "";

			// 客户端的IP地址
			String exter_invoke_ip = "";

			if (seller_email == null || "".equals(seller_email)
					|| total_fee == null || "".equals(total_fee)) {
				 return new JspView("pc/orderFail");
			}

			// 在库中生成订单号,生成一条记录----------------------start
			if (orderNum == null || "".equals(orderNum)) {
				// 退出
				 return new JspView("pc/orderFail");
			}
			// 把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "create_direct_pay_by_user");
			sParaTemp.put("partner", AlipayConfig.partner);
			sParaTemp.put("_input_charset", AlipayConfig.input_charset);
			sParaTemp.put("payment_type", payment_type);
			sParaTemp.put("notify_url", notify_url);
			sParaTemp.put("return_url", return_url);
			sParaTemp.put("seller_email", this.seller_email); // 东方高盛的支付宝账号,写死的
			sParaTemp.put("out_trade_no", out_trade_no); // 我自己生成的订单号
			sParaTemp.put("subject", subject);
			sParaTemp.put("total_fee", total_fee);
			sParaTemp.put("anti_phishing_key", anti_phishing_key);
			sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
			Map<String, String> sPara = AlipaySubmit.buildRequestPara(sParaTemp);
			request.getSession().setAttribute("sPara", sPara); 
			return new JspView("pc/alipaySubmit");
		} else {// 校验不通过
			 return new JspView("pc/orderFail");
		}
	}

	/**
	 * 支付宝通知
	 * 
	 * @param request
	 * @param response
	 */
	@At("/notify")
	public void aliPayNotifyCard(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			excute(request, response);
			return;
		} catch (Exception e) {
			logger.error("支付结果通知接口异常。", e);
			try {
				PrintWriter out = response.getWriter();
				out.write("fail");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 银行卡异步通知
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void excute(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 获取支付宝POST过来反馈信息
		PrintWriter out = response.getWriter();
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			// 乱码解决,这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		final String out_trade_no = new String(request.getParameter(
				"out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
		// 支付宝交易号
		final String trade_no = new String(request.getParameter("trade_no")
				.getBytes("ISO-8859-1"), "UTF-8");
		// 交易状态
		String trade_status = new String(request.getParameter("trade_status")
				.getBytes("ISO-8859-1"), "UTF-8");
		// 获取支付宝的通知返回参数,可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		String result = "fail";
		if (AlipayNotify.verify(params)) {// 验证成功
			if (trade_status.equals("TRADE_FINISHED")
					|| trade_status.equals("TRADE_SUCCESS")) {
				result = aliPayNotify(out_trade_no, trade_no);
				logger.info("result====" + result);
				//log.error("result====" + result);
			}
			out.write(result); // 请不要修改或删除
		} else {// 验证失败
			out.write("fail");
		}
	}

	/**
	 * 阿里异步通知处理方法
	 * 
	 * @param orderNum
	 *            订单号
	 * @param tradeNum
	 *            支付宝交易号
	 * @return "success" 成功 , "fail" 失败
	 */
	public String aliPayNotify(final String orderNum, final String tradeNum) {
		// 获取校园卡支付记录
		SitePaymentRecord payRecord = userPayServiceImpl.getRecordByOrderNum(orderNum);
		if (payRecord == null) {// 无支付记录
			logger.error("校园卡支付记录获取失败--orderNum:" + orderNum + ";支付宝交易号："
					+ tradeNum);
			return "fail";
		}
		// 校园卡支付记录表状态校验
		if (payRecord.getIsFinish() == 1) {// 支付状态为支付成功
			return "success";

		}
		// 拿到paramMap,校验与系统中的用户状态是不是不一样。
		final Map<String, String> map = JSON.parseObject(
				payRecord.getParamJson(), Map.class);
		String checkResult = userPayServiceImpl.paramsCheck(map);
		if (!"ok".equals(checkResult)) {
			logger.error("支付订单保留参数与系统现有数据不一致--orderNum:" + orderNum
					+ ";支付宝交易号：" + tradeNum);
			userPayServiceImpl.updateFailReason("支付订单保留参数与系统现有数据不一致",
					orderNum);
			return "fail";
		}

		try {
			// 事务
			Trans.exec(new Atom() {
				@Override
				public void run() {
					// 修改支付用户的到期 时间
					int i = userPayServiceImpl.changeUserExpireMeal(map);
					if (i != 1) {
						logger.error("修改支付用户的到期 时间失败--orderNum:" + orderNum+ ";支付宝交易号：" + tradeNum);
						userPayServiceImpl.updateFailReason("修改支付用户的到期 时间失败", orderNum);
						throw Lang.makeThrow("修改支付用户的到期 时间失败--orderNum:"
								+ orderNum + ";支付宝交易号：" + tradeNum);
					}
					// 校园卡账务信息表添加记录
					String userName = userPayServiceImpl.getUserById(Integer.parseInt(map.get("userId"))).getUserName();
					i=userPayServiceImpl.saveSchooleFinanceRecord(
							new BigDecimal(map.get("amount")),
							Integer.parseInt(map.get("storeId")),
		    				Integer.parseInt(map.get("userId")),
		    				userName,
		    				Integer.parseInt(map.get("buyNum")),map.get("priceName"),1);
					if (i != 1) {
						logger.error("校园卡账务信息表添加记录失败--orderNum:" + orderNum
								+ ";支付宝交易号：" + tradeNum);
						userPayServiceImpl.updateFailReason(
								"校园卡账务信息表添加记录失败", orderNum);
						throw Lang.makeThrow("校园卡账务信息表添加记录失败--orderNum:"
								+ orderNum + ";支付宝交易号：" + tradeNum);
					}

					// 校园卡支付记录表状态修改为支付成功
					i = userPayServiceImpl.updateToFinish(tradeNum,
							orderNum);
					if (i != 1) {// 执行不成功
						logger.error("校园卡支付记录表状态修改失败--orderNum:" + orderNum
								+ ";支付宝交易号：" + tradeNum);
						userPayServiceImpl.updateFailReason(
								"校园卡支付记录表状态修改失败：", orderNum);
						throw Lang.makeThrow("校园卡支付记录表状态修改失败--orderNum:"
								+ orderNum + ";支付宝交易号：" + tradeNum);
					}
					boolean y=userPayServiceImpl.updateCollect(new BigDecimal(map.get("amount")), Integer.parseInt(map.get("storeId")), Integer.parseInt(map.get("tenantId")));
					if(!y)
						throw Lang.makeThrow("计费表用户统计或场所统计插入或更新未成功"+ orderNum);
				}
			});
		} catch (Exception e) {
			logger.error("支付过程事务故障", e);
			return "fail";
		}
		return "success";
	}
 
}
