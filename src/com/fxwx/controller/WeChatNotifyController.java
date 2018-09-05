package com.fxwx.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.At;
import com.fxwx.entiy.Order;
import com.fxwx.entiy.PortalUser;
import com.fxwx.service.OrderService;
import com.fxwx.util.PayUtils;
import com.fxwx.util.wechar.PayCommonUtil;
import com.fxwx.util.wechar.SwiftpassConfig;
import com.fxwx.util.wechar.XmlUtils;
import com.util.thirdpay.Pay;

@IocBean
@At("/whn")
public class WeChatNotifyController extends BaseController{
	private static Logger logger = Logger.getLogger(WechatPayController.class);

	@Inject
	private OrderService orderServiceImpl;

	private static String APPID = "wxc5fb6a6dabc34dfb";
	private static String MCHID = "1332831801";
	private static String KEY = "ceafa600a2d9b2a98d36885081d16058";
	
	/**
	 * 执行回调函数
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@At("/wechatpayNotify")
	public void wechatpayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String result = null;
		// 返回给微信的处理结果
		String notifyXml = null;
		StringBuilder buffer = new StringBuilder();
		try {
			InputStream reqInput = request.getInputStream();
			if (reqInput == null) {
				// 没有接收到数据!!!
				buffer.append("no-data");
			} else {
				BufferedReader bin = new BufferedReader(new InputStreamReader(reqInput, "utf-8"));
				String line = null;
				while ((line = bin.readLine()) != null) {
					buffer.append(line).append("\r\n");
				}
			}
			notifyXml = buffer.toString();
			logger.error("接收到的报文：" + notifyXml);
			if (!notifyXml.isEmpty()) {
				// 根据通知结果算得到签名 sign
				Map<String, Object> map = readStringXml(notifyXml);
				// 解析各种数据
				String appid = (String) map.get("appid");// 应用ID
				String attach = (String) map.get("attach");// 商家数据包
				String bank_type = (String) map.get("bank_type");// 付款银行
				String cash_fee = (String) map.get("cash_fee");// 现金支付金额
				String fee_type = (String) map.get("fee_type");// 货币种类
				String is_subscribe = (String) map.get("is_subscribe");// 是否关注公众账号
				String mch_id = (String) map.get("mch_id");// 商户号
				String nonce_str = (String) map.get("nonce_str");// 随机字符串
				String openid = (String) map.get("openid");// 用户标识
				String out_trade_no = (String) map.get("out_trade_no");// 获取商户订单号
				String result_code = (String) map.get("result_code");// 业务结果
				String return_code = (String) map.get("return_code");// SUCCESS/FAIL
				String sign = (String) map.get("sign");// 获取签名
				String time_end = (String) map.get("time_end");// 支付完成时间
				String total_fee = (String) map.get("total_fee");// 获取订单金额
				String trade_type = (String) map.get("trade_type");// 交易类型
				String transaction_id = (String) map.get("transaction_id");// 微信支付订单号
				
				Map<String, Object> parameters = readStringXml(notifyXml);
				// 数据加密
				parameters.put("appid", appid);// 应用ID
				parameters.put("attach", attach);// 商家数据包
				parameters.put("bank_type", bank_type);// 付款银行
				parameters.put("cash_fee", cash_fee);// 现金支付金额
				parameters.put("fee_type", fee_type);// 货币种类
				parameters.put("is_subscribe", is_subscribe);// 是否关注公众账号
				parameters.put("mch_id", mch_id);// 商户号
				parameters.put("nonce_str", nonce_str);// 随机字符串
				parameters.put("openid", openid);// 用户标识
				parameters.put("out_trade_no", out_trade_no);// 商户订单号
				parameters.put("result_code", result_code);// 业务结果
				parameters.put("return_code", return_code);// SUCCESS/FAIL
				parameters.put("time_end", time_end);// 支付完成时间
				parameters.put("total_fee", total_fee);// 获取订单金额
				parameters.put("trade_type", trade_type);// 交易类型
				parameters.put("transaction_id", transaction_id);// 微信支付订单号
				
				/*
				 * 加密前验证notify支付订单网关---https://gw.tenpay.com/gateway/simpleverifynotifyid.xml
				 * RequestHandler reqHandler = new RequestHandler(request, response);
				 * reqHandler.init(appid,appsecret, partnerkey);
				 */

				// 根据通知结果算的签名 sign
				String generate_sign = PayUtils.getSign(parameters, SwiftpassConfig.key);

				// 获取订单信息
				Order order = orderServiceImpl.selectOrderInfoByOut_trade_no(out_trade_no);
				if (order == null) {
					result = setXml("fail", "无商户订单");
				} else {
					if ("success".equals(result_code)) {
						// 对比签名信息,签名正确
						if (generate_sign.equals(sign)) {
							//判断微信收到的钱和订单中的钱是否等额
							if(order.getTotalFee().intValue()==Integer.parseInt(total_fee)){
								result = setXml("SUCCESS", "OK");
								// 更新数据库订单状态信息,已完成
								order.setStatue(1);
								order.setUpdateTime(new Timestamp(new Date().getTime()));
								orderServiceImpl.updateOrderInfo(order);
							} 
						} else {
							// 签名错误
							result = setXml("fail", "签名错误");
						}
					} else {
						// 下单请求失败
						result = setXml("fail", "下单请求失败");
					}	
				}
			} else {
				// 没有应答结果
				result = setXml("fail", "没有应答结果");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.error("微信支付回调数据结束:" + result);
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		out.write(result.getBytes());
		out.flush();
		out.close(); 
	}

	/**
	 * 支付完成后通过微信查询订单接口查询支付状态
	 * @param request
	 * @param responses
	 * @return
	 */
	@At("/wechatPayOrderQuery")
	public String wechatOrderPay(HttpServletRequest request, HttpServletResponse responses){
		PortalUser user = (PortalUser) request.getSession().getAttribute("user");// 从session中获取订单信息
		String orderNum = Pay.getUuidOrderNumFromUserId(user.getId()+"");
		String strRandom = PayCommonUtil.buildRandom(4) + "";
		String nonce_str = "kdfwifi" + strRandom;// 生成随机字符串
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid",APPID);
		packageParams.put("mch_id",MCHID);
		packageParams.put("out_trade_no",orderNum);
		packageParams.put("nonce_str",nonce_str);
		String sign = PayCommonUtil.createSign("UTF-8", packageParams, KEY);
		packageParams.put("sign", sign);// 签名加密
		HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/pay/orderquery");
		CloseableHttpResponse res = null;
		CloseableHttpClient client = null;
		String requestXML = PayCommonUtil.getRequestXml(packageParams);
		StringEntity entityParams = new StringEntity(requestXML, "utf-8");
		httpPost.setEntity(entityParams);
		client = HttpClients.createDefault();
		try {
			res = client.execute(httpPost); 
			Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(res.getEntity()), "UTF-8");
			//真实支付成功
			if (resultMap.get("return_code").toString().equals("SUCCESS") && resultMap.get("result_code").toString().equals("SUCCESS")){
				Order order = orderServiceImpl.selectOrderInfoByOut_trade_no(orderNum);
				//查询订单状态
				if(order.getStatue()==1){
					map1.put("return_code","SUCCESS");
					map1.put("msg","支付完成");
				}else if(order.getStatue()==0){
					map1.put("return_code","fail");
					map1.put("msg","支付异常");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.error("微信支付订单查询:" + map1);
		return Json.toJson(map1);
	}
	
	// 通过xml发给微信消息
	public static String setXml(String return_code, String return_msg) {
		/*SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("return_code", return_code);
		parameters.put("return_msg", return_msg);*/
		return "<xml><return_code><![CDATA[" + return_code + "]]>" + "</return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}

	/**
	 * 将xml字符串转换成map
	 * @param xml
	 * @return
	 */
	public static Map<String, Object> readStringXml(String xml) {
		Map<String, Object> map = new HashMap<String, Object>();
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			List<Element> list = rootElt.elements();// 获取根节点下所有节点
			for (Element element : list) { // 遍历节点
				map.put(element.getName(), element.getText()); // 节点的name为map的key，text为map的value
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
