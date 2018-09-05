package com.fxwx.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONSerializer;

import org.apache.catalina.tribes.tipis.AbstractReplicatedMap.MapOwner;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.view.JspView;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.alibaba.fastjson.JSON;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.Order;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.entiy.SitePaymentRecord;
import com.fxwx.entiy.SitePriceConfig;
import com.fxwx.service.OrderService;
import com.fxwx.service.impl.UserPayServiceImpl;
import com.fxwx.util.PayUtils;
import com.fxwx.util.SetSystemProperty;
import com.fxwx.util.WanipUtil;
import com.fxwx.util.alim.AlipayNet;
import com.fxwx.util.wechar.WXMD5;
import com.fxwx.util.wechar.PayCommonUtil;
import com.fxwx.util.wechar.SignUtils;
import com.fxwx.util.wechar.SwiftpassConfig;
import com.fxwx.util.wechar.WecharPayConfig;
import com.fxwx.util.wechar.XMLUtil;
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
@At("/whPc")
public class WechatPayPcController extends BaseController {

	private static Logger logger = Logger.getLogger(WechatPayPcController.class);

	@Inject
	private UserPayServiceImpl userPayServiceImpl;
	@Inject
	private OrderService orderServiceImpl;

	private static String APPID = "wxc5fb6a6dabc34dfb";
	private static String MCHID = "1332831801";
	private static String KEY = "ceafa600a2d9b2a98d36885081d16058";
	private static String APPSECRET = "ceafa600a2d9b2a98d36885081d16058";
	private static String notify_url = SetSystemProperty.propertyName("url")
			+ SetSystemProperty.propertyName("UnitePortal") + "/whPc/wechatNotify";// 回调地址。测试回调必须保证外网能访问到此地址

	@At("/wechatpayPc")
	@Ok("raw:json")
	public String wechatpayPc(HttpServletRequest request, HttpServletResponse responses)
			throws UnsupportedEncodingException {
		responses.setHeader("Content-type", "text/html;charset=UTF-8");
		Map<String, String> map = new HashMap<String, String>(7);
		PortalUser user = (PortalUser) request.getSession().getAttribute("user");// 从session中获取订单信息
		if (user == null)
			user = userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId") + ""));
		CloudSite site = (CloudSite) request.getSession().getAttribute("site");
		int siteId = Integer.valueOf(request.getParameter("siteId") + "");
		if (site == null)
			site = userPayServiceImpl.getCloudSiteById(siteId);
		if (user == null || site == null) {
			map1.put("msg", "下订单失败");
			map1.put("code", "201");
			return Json.toJson(map1);// 下订单失败
		}
		map.put("userId", user.getId() + "");// 用户Id
		map.put("storeId", siteId + "");// 场所Id
		map.put("tenantId", site.getUser_id() + "");// 商户id
		map.put("payType", request.getParameter("priceConfig"));// 场所收费配置Id
		map.put("buyNum", request.getParameter("nums"));// 购买数量
		map.put("amount", request.getParameter("amount"));// 总金额
		map.put("priceNum", request.getParameter("price_num"));// 套餐类型
		map.put("priceName", new String(request.getParameter("price_Name").getBytes(), "UTF-8"));// 套餐名称
		String giveNum = request.getParameter("addMealNum");// 赠送的套餐数量
		String giveUnit = request.getParameter("addMealUnit");// 赠送的套餐单位
		map.put("addMealNum", giveNum);
		map.put("addMealUnit", giveUnit);
		map.put("mealType", request.getParameter("mealType"));// 选择的套餐类型
																// 1----时间，2-----流量
		String checkResult = userPayServiceImpl.paramsCheck(map);
		if ("ok".equals(checkResult)) {// 校验通过
			SitePriceConfig scf = userPayServiceImpl.getSitePriceInfos(siteId, Integer.parseInt(map.get("payType")));
			String siteName = site.getSite_name();
			// 根据和当前时间的比较计算到期时间
			SiteCustomerInfo scii = userPayServiceImpl.getExpirationTimeByProuserid(user.getId(), siteId);
			// 计算用户到期时间或者流量
			String riqi = userPayServiceImpl.getUserCustomer(scii, scf, map);
			if ("1".equals(map.get("mealType"))) {// 用户购买的是时间套餐
				map.put("expireDate", riqi);
			} else {
				map.put("expireFlow", riqi);
			}
			String orderNum = Pay.getUuidOrderNumFromUserId(map.get("userId"));
			request.getSession().setAttribute("order", orderNum);
			System.out.print("开始的订单是:" + orderNum);
			String subject = "校园卡会员充值,按" + new String(map.get("priceName").getBytes(), "UTF-8") + "充值(" + siteName
					+ ")";
			// 保存支付信息
			userPayServiceImpl.savePaymentinfo(orderNum, map, 3);
			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			String strRandom = PayCommonUtil.buildRandom(4) + "";
			String nonce_str = "kdfwifi" + strRandom;// 生成随机字符串
			packageParams.put("appid", APPID);
			packageParams.put("mch_id", MCHID);
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("body", subject);
			packageParams.put("out_trade_no", orderNum);
			packageParams.put("total_fee", (int) (Double.valueOf(map.get("amount")) * 100));
			packageParams.put("spbill_create_ip", WanipUtil.getWanIp(request, responses));
			packageParams.put("notify_url", notify_url);
			packageParams.put("trade_type", "NATIVE");
			String sign = PayCommonUtil.createSign("UTF-8", packageParams, KEY);
			packageParams.put("sign", sign);// 签名加密
			CloseableHttpResponse res = null;
			CloseableHttpClient client = null;
			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
			StringEntity entityParams = new StringEntity(requestXML, "utf-8");
			httpPost.setEntity(entityParams);
			client = HttpClients.createDefault();
			try {
				res = client.execute(httpPost);
				Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(res.getEntity()), "UTF-8");
				if (resultMap.get("return_code").toString().equals("SUCCESS")
						&& resultMap.get("result_code").toString().equals("SUCCESS")) {
					String payUrl = (String) resultMap.get("code_url");
					map1.put("payUrl", payUrl);
					map1.put("code", "200");
					map1.put("orderNum", orderNum);
				} else {
					map1.put("msg", "下订单失败");
					map1.put("code", "201");
				}
				return Json.toJson(map1);// 下订单失败

			} catch (Exception e) {
				logger.error("支付出现异常" + e);
			}
		} else {// 校验不通过
			map1.put("msg", "金额校验不正确");
			map1.put("code", "202");
		}
		return Json.toJson(map1);
	}

	/**
	 * 按月交费
	 * 
	 * @param request
	 * @param responses
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@At("/wechartPayMonth")
	@Ok("raw:json")
	public String wechatPayMonth(HttpServletRequest request, HttpServletResponse responses) {
		/*
		 * System.out.println(
		 * "-----------------------------------------------------");
		 * responses.setHeader("Content-type", "text/html;charset=UTF-8");
		 * PortalUser user = (PortalUser)
		 * request.getSession().getAttribute("user");// 添加用户到session if (user ==
		 * null) user =
		 * userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter(
		 * "userId") + "")); CloudSite site = (CloudSite)
		 * request.getSession().getAttribute("site"); int siteId =
		 * Integer.valueOf(request.getParameter("siteId") + ""); if (site ==
		 * null) site = userPayServiceImpl.getCloudSiteById(siteId); if (site ==
		 * null) { map1.put("msg", "下订单失败"); map1.put("code", "201"); return
		 * Json.toJson(map1);// 下订单失败 }
		 * 
		 * String strRandom = PayCommonUtil.buildRandom(4) + ""; String
		 * nonce_str = "kdfwifi" + strRandom;// 生成随机字符串
		 * 
		 * //生成订单信息 Order order=new Order(); order.setUserId(user.getId());
		 * order.setAppId(APPID); order.setMchId(MCHID);
		 * order.setDeviceInfo(site.getIp()); order.setNonceStr(nonce_str);
		 * order.setBody("用户押金"); order.setDetail("连接WiFi支付押金");
		 * order.setAttach("1");
		 * order.setOut_trade_no(PayUtils.getOut_Trade_No(4));//商户订单号
		 * order.setFeeType("CNY"); // order.setTotalFee(); //
		 * order.setSpbillCreateIp();//终端ip order.setCreateTime(new
		 * Timestamp(new Date().getTime())); order.setGoodsTag("押金类别");
		 * order.setNotifyUrl(SetSystemProperty.propertyName("url") +
		 * SetSystemProperty.propertyName("UnitePortal") +
		 * "/whn/wechatpayNotify"); order.setTradeType("MWEB"); //
		 * order.setProductId(); // order.setLimitPay(); // order.setOpenId();
		 * order.setSceneInfo(
		 * "{\"h5_info\": {\"type\":\"Android\",\"app_name\": \"用户押金\",\"package_name\":\"com.fxwx.controller\"}}"
		 * ); order.setIsDelete(0); order.setStatue(0);
		 * 
		 * //将order对象装换为map Map<String, Object> map = order.toMap();
		 * 
		 * //order.setSign(PayUtils.getSign(map,KEY));
		 * 
		 * 
		 * 
		 * Map<String, String> map1 = new HashMap<String, String>();
		 * map.put("userId", user.getId() + "");// 用户Id map.put("storeId",
		 * siteId + "");// 场所Id map.put("tenantId", site.getUser_id() + "");//
		 * 商户id map.put("payType", request.getParameter("priceConfig"));//
		 * 场所收费配置Id map.put("buyNum", request.getParameter("nums"));// 购买数量
		 * map.put("amount", request.getParameter("amount"));// 总金额
		 * map.put("priceNum", request.getParameter("price_num"));// 套餐类型
		 * map.put("priceName", new
		 * String(request.getParameter("price_Name").getBytes(), "UTF-8"));//
		 * 套餐名称 String giveNum = request.getParameter("addMealNum");// 赠送的套餐数量
		 * String giveUnit = request.getParameter("addMealUnit");// 赠送的套餐单位
		 * map.put("addMealNum", giveNum); map.put("addMealUnit", giveUnit);
		 * map.put("mealType", request.getParameter("mealType"));// 选择的套餐类型
		 * 
		 * String checkResult = userPayServiceImpl.paramsCheck(map1); if
		 * ("ok".equals(checkResult)) {// 校验通过
		 * 
		 * SitePriceConfig scf = userPayServiceImpl.getSitePriceInfos(siteId,
		 * Integer.parseInt(map.get("payType")));//场所价格信息 String siteName =
		 * site.getSite_name();//场所的名字 // 根据和当前时间的比较计算到期时间 SiteCustomerInfo scii
		 * = userPayServiceImpl.getExpirationTimeByProuserid(user.getId(),
		 * siteId);
		 * 
		 * // 计算用户到期时间或者流量 String riqi =
		 * userPayServiceImpl.getUserCustomer(scii, scf, map); if
		 * ("1".equals(map.get("mealType"))) {// 用户购买的是时间套餐
		 * map.put("expireDate", riqi); } else { map.put("expireFlow", riqi); }
		 * 
		 * String siteName = site.getSite_name();//场所的名字 String orderNum =
		 * Pay.getUuidOrderNumFromUserId(map.get("userId"));//生成订单号
		 * request.getSession().setAttribute("order", orderNum);
		 * System.out.print("开始的订单是:" + orderNum); String subject=""; try {
		 * subject = "校园卡会员充值,按" + new String(map.get("priceName").getBytes(),
		 * "UTF-8") + "充值(" + siteName + ")"; } catch
		 * (UnsupportedEncodingException e1) { // TODO Auto-generated catch
		 * block e1.printStackTrace(); }
		 * 
		 * // 保存支付信息 userPayServiceImpl.savePaymentinfo(orderNum, map, 3);
		 * 
		 * SortedMap<Object, Object> packageParams = new TreeMap<Object,
		 * Object>();
		 * 
		 * packageParams.put("appid", APPID); packageParams.put("mch_id",
		 * MCHID); packageParams.put("nonce_str", nonce_str);
		 * packageParams.put("body", subject); packageParams.put("out_trade_no",
		 * orderNum); packageParams.put("total_fee", (int)
		 * (Double.valueOf(map.get("amount")) * 100));
		 * packageParams.put("spbill_create_ip", WanipUtil.getWanIp(request,
		 * responses)); packageParams.put("notify_url", notify_url);
		 * packageParams.put("trade_type", "NATIVE"); String sign =
		 * PayCommonUtil.createSign("UTF-8", packageParams, KEY);
		 * packageParams.put("sign", sign);// 签名加密 CloseableHttpResponse res =
		 * null; CloseableHttpClient client = null; String requestXML =
		 * PayCommonUtil.getRequestXml(packageParams); HttpPost httpPost = new
		 * HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
		 * StringEntity entityParams = new StringEntity(requestXML, "utf-8");
		 * httpPost.setEntity(entityParams); client =
		 * HttpClients.createDefault(); try { res = client.execute(httpPost);
		 * //统一下单成功后微信返回结果 Map<String, String> resultMap =
		 * XmlUtils.toMap(EntityUtils.toByteArray(res.getEntity()), "UTF-8");
		 * 
		 * //下单成功 if (resultMap.get("return_code").toString().equals("SUCCESS")
		 * && resultMap.get("result_code").toString().equals("SUCCESS")) {
		 * order.setOrderNum(orderNum); order.setCreateTime(new Timestamp(new
		 * Date().getTime())); order.setBackupJson(Json.toJson(order)); //生成订单信息
		 * boolean orderFlag = orderServiceImpl.insertOrderInfo(order);
		 * if(orderFlag){ String prepay_id = resultMap.get("prepay_id");
		 * map1.put("prepay_id", prepay_id); map1.put("sign", sign);
		 * map1.put("orderNum", orderNum); } } else { map1.put("msg", "下订单失败");
		 * map1.put("code", "201"); } } catch (Exception e) {
		 * logger.error("支付出现异常" + e); } } return Json.toJson(map1) ;
		 */
		return null;
	}

	/**
	 * 用户交押金
	 * 
	 * @param request
	 * @param responses
	 * @return
	 */
	@At("/wechartPayCashPledge")
	@Ok("raw:json")
	public String wechatPayCashPledge(HttpServletRequest request, HttpServletResponse responses) {
		System.out.println("----------------------------------------------------");
		CloudSite site = (CloudSite) request.getSession().getAttribute("site");
		PortalUser user = (PortalUser) request.getSession().getAttribute("user");// 添加用户到session
		if (user == null)
			user = userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId") + ""));
		int siteId = Integer.valueOf(request.getParameter("siteId") + "");
		if (site == null)
			site = userPayServiceImpl.getCloudSiteById(siteId);
		if (site == null) {
			map1.put("msg", "下订单失败");
			map1.put("code", "201");
			return Json.toJson(map1);// 下订单失败
		}

		String strRandom = PayCommonUtil.buildRandom(4) + "";
		String nonce_str = "fxwxwifi" + strRandom;// 生成随机字符串

		// 生成订单信息
		Order order = new Order();
		order.setAppId(APPID);
		order.setAttach("1");
		order.setBody("用户押金");
		order.setCreateTime(new Timestamp(new Date().getTime()));
		order.setDetail("连接WiFi支付押金");
		order.setDeviceInfo(site.getIp());
		order.setFeeType("CNY");
		order.setGoodsTag("押金类别");
		order.setIsDelete(0);
		// order.setLimitPay();
		order.setMchId(MCHID);
		order.setNonceStr(nonce_str);
		order.setNotifyUrl(SetSystemProperty.propertyName("url") + SetSystemProperty.propertyName("UnitePortal")
				+ "/whn/wechatpayNotify");
		// order.setOpenId();
		String orderNum = Pay.getUuidOrderNumFromUserId(user.getId() + "");
		order.setOrderNum(orderNum);
		order.setOut_trade_no(PayUtils.getOut_Trade_No(4));// 商户订单号
		// order.setProductId();
		order.setSceneInfo(
				"{\"h5_info\": {\"type\":\"Android\",\"app_name\": \"用户押金\",\"package_name\":\"com.fxwx.controller\"}}");
		// order.setSpbillCreateIp();//终端ip
		order.setStatue(0);
		order.setTotalFee(new BigDecimal(request.getParameter("money")));// 总金额
		order.setTradeType("MWEB");

		// 统一下单接口参数
		SortedMap<Object, Object> sortedMap = new TreeMap();
		sortedMap.put("appid", APPID);
		sortedMap.put("mch_id", MCHID);
		sortedMap.put("device_info", site.getIp());
		sortedMap.put("nonce_str", nonce_str);
		sortedMap.put("body", "用户押金");
		sortedMap.put("detail", "连接WiFi支付押金");
		sortedMap.put("attach", "1");
		sortedMap.put("out_trade_no", PayUtils.getOut_Trade_No(4));
		sortedMap.put("totalFee", order.getTotalFee().intValue());
		sortedMap.put("total_fee", order.getTotalFee().intValue());
		sortedMap.put("spbill_create_ip", request.getParameter("spbill_create_ip"));
		sortedMap.put("notify_url", SetSystemProperty.propertyName("url")
				+ SetSystemProperty.propertyName("UnitePortal") + "/whn/wechatpayNotify");
		sortedMap.put("trade_type", "MWEB");
		sortedMap.put("scene_info",
				"{\"h5_info\": {\"type\":\"Android\",\"app_name\": \"用户押金\",\"package_name\":\"com.fxwx.controller\"}}");

		String sign = PayCommonUtil.createSign("UTF-8", sortedMap, KEY);
		order.setSign(sign);

		System.out.print("开始的订单是:" + orderNum);
		CloseableHttpResponse res = null;
		CloseableHttpClient client = null;
		String requestXML = PayCommonUtil.getRequestXml(sortedMap);
		HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
		StringEntity entityParams = new StringEntity(requestXML, "utf-8");
		httpPost.setEntity(entityParams);
		client = HttpClients.createDefault();
		try {
			res = client.execute(httpPost);
			// 统一下单成功后微信返回结果
			Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(res.getEntity()), "UTF-8");
			// 下单成功
			if (resultMap.get("return_code").toString().equals("SUCCESS")
					&& resultMap.get("result_code").toString().equals("SUCCESS")) {
				order.setBackJson(Json.toJson(resultMap));
				// 生成订单信息
				boolean orderFlag = orderServiceImpl.insertOrderInfo(order);
				if (orderFlag) {
					String prepay_id = resultMap.get("prepay_id");
					map1.put("prepay_id", prepay_id);
					map1.put("sign", sign);
					map1.put("orderNum", orderNum);
				}
			} else {
				map1.put("msg", "下订单失败");
				map1.put("code", "201");
			}
		} catch (Exception e) {
			logger.error("支付出现异常" + e);
		}
		return Json.toJson(map1);
	}

	// 用户确认支付
	@At("/towechatpayMonth")
	public View towechatpayMonth(HttpServletRequest request, HttpServletResponse responses) {
		responses.setHeader("Content-type", "text/html;charset=UTF-8");
		View successUrl = new JspView("pc/weichatSubmit");
		View failUrl = new JspView("pc/orderFail");
		Map<String, String> map = new HashMap<String, String>(7);
		PortalUser user = (PortalUser) request.getSession().getAttribute("user");// 添加用户到session
		if (user == null)
			user = userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId") + ""));
		CloudSite site = (CloudSite) request.getSession().getAttribute("site");
		int siteId = Integer.valueOf(request.getParameter("siteId") + "");
		if (site == null)
			site = userPayServiceImpl.getCloudSiteById(siteId);
		if (user == null || site == null) {
			return failUrl;// 下订单失败
		}

		map.put("userId", user.getId() + "");// 用户Id
		map.put("tenantId", site.getUser_id() + "");// 商户id
		map.put("buyNum", request.getParameter("nums"));// 购买数量
		map.put("amount", request.getParameter("amount"));// 总金额
		map.put("myNum", request.getParameter("orderNum"));// 订单号
		map.put("priceNum", request.getParameter("allPirce"));// 套餐
		map.put("total_fee", request.getParameter("amount"));// 金额
		String subject = request.getParameter("price_Name");
		String siteName = userPayServiceImpl.getCloudSiteById(site.getId()).getSite_name();
		map.put("priceName", subject);// 套餐名称
		map.put("addMealNum", request.getParameter("addMealNum"));
		map.put("addMealUnit", request.getParameter("addMealUnit"));
		map.put("mealType", request.getParameter("mealType"));
		map.put("subject", "校园卡会员充值,按" + subject + "充值(" + siteName + ")");// 用途
		String checkResult = userPayServiceImpl.paramsCheck(map);
		if ("ok".equals(checkResult)) {// 校验通过
			SitePriceConfig scf = userPayServiceImpl.getSitePriceInfos(siteId, Integer.parseInt(map.get("payType")));

			// 根据和当前时间的比较计算到期时间
			SiteCustomerInfo scii = userPayServiceImpl.getExpirationTimeByProuserid(user.getId(), siteId);

			// 计算用户到期时间或者流量
			String riqi = userPayServiceImpl.getUserCustomer(scii, scf, map);
			if ("1".equals(map.get("mealType"))) {// 用户购买的是时间套餐
				map.put("expireDate", riqi);
			} else {
				map.put("expireFlow", riqi);
			}
			// 生成订单号
			String orderNum = Pay.getUuidOrderNumFromUserId(map.get("userId"));
			request.getSession().setAttribute("order", orderNum);
			System.out.print("开始的订单是:" + orderNum);

			// 保存支付信息
			userPayServiceImpl.savePaymentinfo(orderNum, map, 3);

			// 微信支付配置
			SortedMap<String, String> map1 = WecharPayConfig.config(request);
			// map1.put("callback_url",SetSystemProperty.propertyName("url")+SwiftpassConfig.callback_url+"?out_trade_no="+orderNum);
			map1.put("out_trade_no", orderNum);// 订单号
			map1.put("body", subject);// 商品名称
			map1.put("total_fee", ((int) (Double.valueOf(map.get("amount")) * 100)) + "");// 微信支付默认是分为单位.而前台传过来的是一元为单位
			Map<String, String> params = SignUtils.paraFilter(map1);
			StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
			SignUtils.buildPayParams(buf, params, false);
			String preStr = buf.toString();
			String sign = WXMD5.sign(preStr, "&key=" + SwiftpassConfig.key, "utf-8");
			map1.put("sign", sign);
			request.getSession().setAttribute("map1", map1);
			request.getSession().setAttribute("map", map);
			return successUrl;
		} else {// 校验不通过
			return failUrl;
		}
	}

	@At("/towechatpayPc")
	public View towechatpayPc(HttpServletRequest request, HttpServletResponse responses)
			throws UnsupportedEncodingException {
		responses.setHeader("Content-type", "text/html;charset=UTF-8");
		View successUrl = new JspView("pc/weichatSubmit");
		View failUrl = new JspView("pc/orderFail");
		Map<String, String> map = new HashMap<String, String>(7);
		PortalUser user = (PortalUser) request.getSession().getAttribute("user");// 从session中得到用户
		if (user == null)
			user = userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId") + ""));
		CloudSite site = (CloudSite) request.getSession().getAttribute("site");
		int siteId = Integer.valueOf(request.getParameter("siteId") + "");
		if (site == null)
			site = userPayServiceImpl.getCloudSiteById(siteId);
		if (user == null || site == null) {
			return failUrl;// 下订单失败
		}
		map.put("userId", user.getId() + "");// 用户Id
		map.put("storeId", site.getId() + "");// 场所Id
		map.put("tenantId", site.getUser_id() + "");// 商户id
		map.put("payType", request.getParameter("idss"));// 场所收费配置Id
		map.put("buyNum", request.getParameter("nums"));// 购买数量
		map.put("amount", request.getParameter("amount"));// 总金额
		map.put("myNum", request.getParameter("orderNum"));// 订单号
		map.put("priceNum", request.getParameter("allPirce"));// 套餐
		map.put("total_fee", request.getParameter("amount"));// 金额
		String subject = request.getParameter("price_Name");
		String siteName = userPayServiceImpl.getCloudSiteById(site.getId()).getSite_name();
		map.put("priceName", subject);// 套餐名称
		map.put("addMealNum", request.getParameter("addMealNum"));
		map.put("addMealUnit", request.getParameter("addMealUnit"));
		map.put("mealType", request.getParameter("mealType"));
		map.put("subject", "校园卡会员充值,按" + subject + "充值(" + siteName + ")");// 用途
		String checkResult = userPayServiceImpl.paramsCheck(map);
		if ("ok".equals(checkResult)) {// 校验通过

			SitePriceConfig scf = userPayServiceImpl.getSitePriceInfos(siteId, Integer.parseInt(map.get("payType")));
			// 根据和当前时间的比较计算到期时间
			SiteCustomerInfo scii = userPayServiceImpl.getExpirationTimeByProuserid(user.getId(), siteId);
			// 计算用户到期时间或者流量
			String riqi = userPayServiceImpl.getUserCustomer(scii, scf, map);
			if ("1".equals(map.get("mealType"))) {// 用户购买的是时间套餐
				map.put("expireDate", riqi);
			} else {
				map.put("expireFlow", riqi);
			}
			String orderNum = Pay.getUuidOrderNumFromUserId(map.get("userId"));
			request.getSession().setAttribute("order", orderNum);
			System.out.print("开始的订单是:" + orderNum);
			// 保存支付信息
			userPayServiceImpl.savePaymentinfo(orderNum, map, 3);
			SortedMap<String, String> map1 = WecharPayConfig.config(request);
			// map1.put("callback_url",SetSystemProperty.propertyName("url")+SwiftpassConfig.callback_url+"?out_trade_no="+orderNum);
			map1.put("out_trade_no", orderNum);// 订单号
			map1.put("body", subject);// 商品名称
			map1.put("total_fee", ((int) (Double.valueOf(map.get("amount")) * 100)) + "");// 微信支付默认是分为单位.而前台传过来的是一元为单位
			Map<String, String> params = SignUtils.paraFilter(map1);
			StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
			SignUtils.buildPayParams(buf, params, false);
			String preStr = buf.toString();
			String sign = WXMD5.sign(preStr, "&key=" + SwiftpassConfig.key, "utf-8");
			map1.put("sign", sign);
			request.getSession().setAttribute("map1", map1);
			request.getSession().setAttribute("map", map);
			return successUrl;
		} else {// 校验不通过
			return failUrl;
		}
	}

	/**
	 * @Description 微信支付异步通知
	 * @date 2016年12月26日下午1:46:31
	 * @author guoyingjie
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@At("/wechatNotify")
	public void wechatNotify(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		SortedMap<String, String> sorMap = new TreeMap<String, String>();
		try {
			InputStream inStream = req.getInputStream();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}

			outStream.close();
			inStream.close();
			String result = new String(outStream.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
			logger.error("哈哈上＝＝＝" + result);
			Map<Object, Object> map = null;
			try {
				map = XMLUtil.doXMLParse(result);
			} catch (JDOMException e) {
				e.printStackTrace();
			}
			for (Object keyValue : map.keySet()) {
				logger.error("微信支付key==" + keyValue + "=微信支付value" + map.get(keyValue));
			}
			if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
				// 对订单进行业务操作
				String out_trade_no = map.get("out_trade_no") + "";// 商户订单号
				String transaction_id = map.get("transaction_id") + "";// 微信生成订单号
				String respString = this.wechatNotice(out_trade_no, transaction_id);
				sorMap.put("return_code", respString);
				resp.getWriter().write(XmlUtils.parsewxXML(sorMap)); // 告诉微信服务器，我收到信息了，不要在调用回调action了
			} else {
				sorMap.put("return_code", "FAIL");
				resp.getWriter().write(XmlUtils.parsewxXML(sorMap));
			}
		} catch (Exception e) {
			logger.error("微信扫码支付异常" + e);
			sorMap.put("return_code", "FAIL");
			resp.getWriter().write(XmlUtils.parsewxXML(sorMap));
		}
	}

	/**
	 * @Description 微信支付异步业务处理
	 * @date 2016年12月26日下午1:48:10
	 * @author guoyingjie
	 * @param orderNum
	 * @param tradeNum
	 * @return
	 */
	public String wechatNotice(final String orderNum, final String tradeNum) {
		logger.error("hdajdhaj");
		// 获取校园卡支付记录
		SitePaymentRecord payRecord = userPayServiceImpl.getRecordByOrderNum(orderNum);

		if (payRecord == null) {// 无支付记录
			logger.error("校园卡支付记录获取失败--orderNum:" + orderNum + ";微信交易交易号：" + tradeNum);
			return "FAIL";
		}
		// 校园卡支付记录表状态校验
		if (payRecord.getIsFinish() == 1 || payRecord.getIsFinish() == -1) {// 支付状态为支付成功
			return "SUCCESS";
		}
		// 拿到paramMap,校验与系统中的用户状态是不是不一样。
		final Map<String, String> map = JSON.parseObject(payRecord.getParamJson(), Map.class);
		String checkResult = userPayServiceImpl.paramsCheck(map);
		if (!"ok".equals(checkResult)) {
			logger.error("支付订单保留参数与系统现有数据不一致--orderNum:" + orderNum + ";支付宝交易号：" + tradeNum);
			userPayServiceImpl.updateFailReason("支付订单保留参数与系统现有数据不一致", orderNum);
			return "FAIL";
		}
		try {
			// 事务
			Trans.exec(new Atom() {
				@Override
				public void run() {
					// 修改支付用户的到期 时间
					int i = userPayServiceImpl.changeUserExpireMeal(map);
					if (i != 1) {
						userPayServiceImpl.updateFailReason("修改支付用户的到期 时间失败", orderNum);
						throw Lang.makeThrow("修改支付用户的到期 时间失败--orderNum:" + orderNum + ";支付宝交易号：" + tradeNum);
					}
					// 校园卡账务信息表添加记录
					String userName = userPayServiceImpl.getUserById(Integer.parseInt(map.get("userId"))).getUserName();
					i = userPayServiceImpl.saveSchooleFinanceRecord(new BigDecimal(map.get("amount")),
							Integer.parseInt(map.get("storeId")), Integer.parseInt(map.get("userId")), userName,
							Integer.parseInt(map.get("buyNum")), map.get("priceName"), 3);
					if (i != 1) {
						userPayServiceImpl.updateFailReason("校园卡账务信息表添加记录失败", orderNum);
						throw Lang.makeThrow("校园卡账务信息表添加记录失败--orderNum:" + orderNum + ";支付宝交易号：" + tradeNum);
					}
					// 校园卡支付记录表状态修改为支付成功
					i = userPayServiceImpl.updateToFinish(tradeNum, orderNum);
					if (i != 1) {// 执行不成功
						userPayServiceImpl.updateFailReason("校园卡支付记录表状态修改失败：", orderNum);
						throw Lang.makeThrow("校园卡支付记录表状态修改失败--orderNum:" + orderNum + ";支付宝交易号：" + tradeNum);
					}
					boolean y = userPayServiceImpl.updateCollect(new BigDecimal(map.get("amount")),
							Integer.parseInt(map.get("storeId")), Integer.parseInt(map.get("tenantId")));
					if (!y)
						throw Lang.makeThrow("计费表用户统计或场所统计插入或更新未成功" + orderNum);
				}
			});
		} catch (Exception e) {
			logger.error("支付过程事务故障", e);
			return "FAIL";
		}
		return "SUCCESS";
	}

	/**
	 * 待支付用户使用微信充值
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@At("/toWechat")
	public View toWechat(HttpServletRequest request, HttpSession session, HttpServletResponse responses)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		responses.setCharacterEncoding("UTF-8");
		responses.setHeader("Content-type", "text/html;charset=UTF-8");
		View successUrl = new JspView("mobile/weichatSubmit");
		View failUrl = new JspView("mobile/orderFail");

		PortalUser user = (PortalUser) request.getSession().getAttribute("user");// 添加用户到session
		if (user == null)
			user = userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId") + ""));
		CloudSite cloudSite = (CloudSite) request.getSession().getAttribute("site");
		if (cloudSite == null)
			cloudSite = userPayServiceImpl.getCloudSiteById(Integer.valueOf(request.getParameter("siteId") + ""));
		if (user == null || cloudSite == null) {
			return failUrl;
		}
		String maps = request.getParameter("maps");
		Map<String, Object> json = (Map<String, Object>) JSONSerializer.toJSON(maps);
		String priceName = (json.get("name") + "").split("-")[1];
		String orderNum = json.get("orderNum") + "";// 订单号
		String money = json.get("amount") + "";
		String subject = "校园卡会员充值,按" + priceName + "充值(" + cloudSite.getSite_name() + ")";
		boolean result = userPayServiceImpl.checkeOrder(orderNum, cloudSite.getId(), user.getId(), money, priceName);
		if (result) {
			SortedMap<String, String> map1 = WecharPayConfig.config(request);
			map1.put("callback_url",
					SetSystemProperty.propertyName("url") + SwiftpassConfig.callback_url + "?out_trade_no=" + orderNum);
			map1.put("out_trade_no", orderNum);
			map1.put("body", subject);
			map1.put("total_fee", ((int) (Double.valueOf(money) * 100)) + "");// 微信支付默认是分为单位.而前台传过来的是一元为单位
			Map<String, String> params = SignUtils.paraFilter(map1);
			StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
			SignUtils.buildPayParams(buf, params, false);
			String preStr = buf.toString();
			String sign = WXMD5.sign(preStr, "&key=" + SwiftpassConfig.key, "utf-8");
			map1.put("sign", sign);
			String reqUrl = SwiftpassConfig.req_url;
			CloseableHttpResponse response = null;
			CloseableHttpClient client = null;
			String res = null;
			try {
				HttpPost httpPost = new HttpPost(reqUrl);
				StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map1), "utf-8");
				httpPost.setEntity(entityParams);
				client = HttpClients.createDefault();
				response = client.execute(httpPost);
				if (response != null && response.getEntity() != null) {
					Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()),
							"utf-8");
					res = XmlUtils.toXml(resultMap);
					if (resultMap.containsKey("sign")) {
						if (!SignUtils.checkParam(resultMap, SwiftpassConfig.key)) {
							// 签名失败
							return failUrl;
						} else {
							if ("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))) {
								String pay_info = resultMap.get("pay_info");
								request.setAttribute("infourl", pay_info);
								request.setAttribute("out_trade_no", map1.get("out_trade_no"));
								request.setAttribute("total_fee", map1.get("total_fee"));
								request.setAttribute("body", map1.get("body"));
								return successUrl;
							} else {
								return failUrl;
							}
						}
					} else {
						return failUrl;
					}
				} else {
					return failUrl;
				}
			} catch (Exception e) {
				return failUrl;
			} finally {
				if (response != null) {
					try {
						response.close();
					} catch (IOException e) {
					}
				}
				if (client != null) {
					try {
						client.close();
					} catch (IOException e) {
					}
				}
			}
		} else {
			return failUrl;
		}

	}

	/**
	 * pc wchat 待支付
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @date 创建时间：2017年12月23日 下午3:22:09
	 *
	 */
	@At("/toWechat")
	@Ok("raw:json")
	public String toWechat1(HttpSession session, HttpServletRequest request, HttpServletResponse responses) {
		PortalUser user = (PortalUser) request.getSession().getAttribute("user");// 添加用户到session
		if (user == null)
			user = userPayServiceImpl.getUserById(Integer.valueOf(request.getParameter("userId") + ""));
		CloudSite cloudSite = (CloudSite) request.getSession().getAttribute("site");
		if (cloudSite == null)
			cloudSite = userPayServiceImpl.getCloudSiteById(Integer.valueOf(request.getParameter("siteId") + ""));
		if (user == null || cloudSite == null) {
			map1.put("msg", "下订单失败");
			map1.put("code", "201");
			return Json.toJson(map1);// 下订单失败;
		}

		String priceName = request.getParameter("price_Name");
		String orderNum = request.getParameter("orderNum");
		String money = request.getParameter("amount");
		// String priceName = (json.get("name")+"").split("-")[1];
		// String orderNum=json.get("orderNum")+"";//订单号
		// String money=json.get("amount")+"";
		String subject = "校园卡会员充值,按" + priceName + "充值(" + cloudSite.getSite_name() + ")";
		boolean result = userPayServiceImpl.checkeOrder(orderNum, cloudSite.getId(), user.getId(), money, priceName);
		if (result) {
			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			String strRandom = PayCommonUtil.buildRandom(4) + "";
			String nonce_str = "kdfwifi" + strRandom;// 生成随机字符串
			packageParams.put("appid", APPID);
			packageParams.put("mch_id", MCHID);
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("body", subject);
			packageParams.put("out_trade_no", orderNum);
			packageParams.put("total_fee", (int) (Double.valueOf(money) * 100));
			packageParams.put("spbill_create_ip", WanipUtil.getWanIp(request, responses));
			packageParams.put("notify_url", notify_url);
			packageParams.put("trade_type", "NATIVE");
			String sign = PayCommonUtil.createSign("UTF-8", packageParams, KEY);
			packageParams.put("sign", sign);// 签名加密
			CloseableHttpResponse res = null;
			CloseableHttpClient client = null;
			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
			StringEntity entityParams = new StringEntity(requestXML, "utf-8");
			httpPost.setEntity(entityParams);
			client = HttpClients.createDefault();
			try {
				res = client.execute(httpPost);
				Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(res.getEntity()), "UTF-8");
				if (resultMap.get("return_code").toString().equals("SUCCESS")
						&& resultMap.get("result_code").toString().equals("SUCCESS")) {
					String payUrl = (String) resultMap.get("code_url");
					map1.put("payUrl", payUrl);
					map1.put("code", "200");
					map1.put("orderNum", orderNum);
				} else {
					map1.put("msg", "下订单失败");
					map1.put("code", "201");
				}
				return Json.toJson(map1);// 下订单失败

			} catch (Exception e) {
				logger.error("支付出现异常" + e);
			}
		} else {// 校验不通过
			map1.put("msg", "金额校验不正确");
			map1.put("code", "202");
		}
		return Json.toJson(map1);
	}

	/**
	 * 校验微信扫码支付是否已经完成
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @date 创建时间：2017年12月23日 下午1:44:47
	 *
	 */
	@At("/checkPay")
	@Ok("raw:json")
	public String checkPay(String orderNum, HttpSession session) {
		boolean res = userPayServiceImpl.checkPayResult(orderNum);
		Map<String, Object> map1 = new HashMap<String, Object>();
		if (res)
			map1.put("code", 200);
		return Json.toJson(map1);
	}

	/**
	 * 查询场所价格
	 * 
	 * @return
	 */
	@At("/findSitePriceConfig")
	@Ok("raw:json")
	public String findSitePriceConfig(int siteId) {
		// 按天,收费类型:1为天,2为月
		SitePriceConfig sitePriceDay = userPayServiceImpl.getSitePriceInfos(siteId, 1);
		// 按月
		SitePriceConfig sitePriceMonth = userPayServiceImpl.getSitePriceInfos(siteId, 2);
		if (sitePriceDay != null && sitePriceMonth != null) {
			map1.put("code", "200");
			map1.put("sitePriceDay", sitePriceDay);
			map1.put("sitePriceMonth", sitePriceMonth);
		} else {
			map1.put("code", "201");
			map1.put("msg", "查询异常");
		}
		return Json.toJson(map1);
	}

	public static void main(String[] args) {
		/* SortedMap<String,String> sortedMap=new sortmap */
	}
}
