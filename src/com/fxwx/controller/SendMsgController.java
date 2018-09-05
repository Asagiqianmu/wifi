package com.fxwx.controller;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fxwx.entiy.PhoneCode;
import com.fxwx.entiy.SendSmsState;
import com.fxwx.service.CmccRelayStateService;
import com.fxwx.service.UnitePortalService;
import com.fxwx.util.DateUtil;
import com.fxwx.util.MD5;
import com.fxwx.util.MsgUtil;
import com.fxwx.util.RSAUtil;
import com.fxwx.util.SetSystemProperty;
import com.fxwx.util.cmcc.HttpRequestUtil;
import com.fxwx.xsms.common.cmpp.CmppAPI;


/**
 * Copyright (c) All Rights Reserved, 2016. 版权所有 飞讯无限 Information Technology
 * Co.,Ltd
 * 
 * @Project newCloud
 * @File SendMsgController.java
 * @Date 2018年2月29日 下午3:01:50
 * @Author dengfei
 */
@IocBean
@At("/code")
public class SendMsgController extends BaseController {
	private static Logger logger = Logger.getLogger(SendMsgController.class);

	@Inject
	private UnitePortalService unitePortalServiceImpl; // 用户接口
	@Inject
	private CmccRelayStateService cmccRelayStateServiceImpl;
	
	// 产品名称:云通信短信API产品,开发者无需替换
	private static final String product = "Dysmsapi";
	// 产品域名,开发者无需替换
	private static final String domain = "dysmsapi.aliyuncs.com";
	// 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
	static final String accessKeyId = "LTAI8FZoIBVyHoj8";
	static final String accessKeySecret = "wBUHMW4W183LoNY1B5s7FZmn4rBNPQ";
	private static final String SIGN = "飞讯WiFi";
	
	/**
	 * 
	 * 深圳移动短信网关CMPP2.0
	 * 发送验证码 -1是发送失败,0是发送成功,-2发送频繁
	 * 
	 * @param telephone
	 * @param templateCode
	 * @param request
	 * @param response
	 * @param session
	 * @return style 验证码样式模板 style ：0 注册发送验证码 style : 1 设置密码发送验证码 style : 2
	 *         忘记密码发送验证码
	 * @throws Exception
	 * 
	 * @return
	 * style 验证码样式模板
	 * style ：0 注册发送验证码
	 * style : 1 设置密码发送验证码
	 * style : 2 忘记密码发送验证码
	 * @throws Exception 
	 *   
	 */
	@At("/msgRandCode")
	@Ok("raw:json")
	public String msgRandCode(@Param("parms") String result,String style,HttpServletRequest request, HttpServletResponse response,HttpSession session){
		// 首先判断传参过来是否为null
		if (result != null && !"".equals(result)) {
			logger.error("解析前result===" + result);
			// 先对字段进行解密
			String decode;
			byte[] en_result = new BigInteger(result, 16).toByteArray();
			byte[] de_result;
			try {
				de_result = RSAUtil.decrypt(RSAUtil.getKeyPair().getPrivate(), en_result);
				StringBuffer sb = new StringBuffer();
				sb.append(new String(de_result));
				decode = sb.reverse().toString();
				decode = URLDecoder.decode(decode, "UTF-8");
				logger.error("解析后===" + decode);
				String a[] = decode.split(",");
				String phone = a[0];// 手机号
				String mac = a.length == 1 ? MsgUtil.randCode() : a[1];// mac
				String ip = "";
				if (a.length == 3){
					ip = a[2];// IP
				}
				String telephone = phone.replace(" ", "");// 去除手机号空格
				String oldeCode = (String) session.getAttribute(telephone);
				// 发送验证码如果上次验证码未过期则直接把上次验证码返回给用户否则直接生成
				String templateCode = "";
				// 判断发送验证码模板信息
//				if (style.equals("0")) {// 注册密码
//					templateCode = "SMS_36185248";
//				} else if (style.equals("1")) {// 设置密码
//					templateCode = "SMS_36340243";
//				} else if (style.equals("2")) {// 忘记密码
//					templateCode = "SMS_36325232";
//				}
				// 首先根据mac值等等唯一标识查询当前设备中发验证码的次数
				PhoneCode pc = new PhoneCode();
				pc.setMac(mac);
				pc.setCrateTime(DateUtil.getDate());
				pc.setPhone(telephone);
				// 第二个当前mac设备当天只能发三条
				int macCount = unitePortalServiceImpl.countPhoneMac(pc);
				// 第三个验证同一个手机号用户当天只能发送三条；
				int phoneCount = unitePortalServiceImpl.countPhone(pc);
				// 三层校验，浏览器、mac设备标识、注册手机号
				int count = 1;
				// 获取可以发送的次数
				int number = Integer.parseInt(SetSystemProperty.propertyName("count"));
				SendSmsState sendSmsState = new SendSmsState();
				sendSmsState.setTelphone(telephone);
				sendSmsState.setMac(mac);
				sendSmsState.setSmsType(1);
				sendSmsState.setCreateTime(new Date());
				
				if (macCount < number && phoneCount < number) {
					// 部署项目所用
					String code = oldeCode == null ? MsgUtil.randCode() : oldeCode;
					boolean flag = CmppAPI.sendMsg(phone, code);
					logger.error("----"+telephone+"" + templateCode+"=========="+code+"短信发送结果===" + flag);
					if (flag) {// 验证码发送成功后给数据库插入相关日志
						session.setAttribute(telephone, code);
						
						// 设置session存活时间位5分钟
						if (oldeCode == null) {
							session.setMaxInactiveInterval(5 * 60);
						}
						PhoneCode phoneCode = new PhoneCode();
						phoneCode.setIp(ip);
						phoneCode.setPhone(telephone);
						phoneCode.setCode(code);
						phoneCode.setMac(mac);
						phoneCode.setCount(count + macCount);
						phoneCode.setCrateTime(DateUtil.getDate());
						unitePortalServiceImpl.insertPhoneCode(phoneCode);
						map1.put("result", 0);
						map1.put("msg", "成功验证码:" + code);
						sendSmsState.setResult(1);
					} else {
						map1.put("result", -1);
						map1.put("msg", "失败验证码");
						sendSmsState.setResult(0);
					}
				
				} else {
					map1.put("result", -2);
					map1.put("msg", "发送太频繁");
					sendSmsState.setResult(0);
				}
				unitePortalServiceImpl.insertSendSmsState(sendSmsState);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				map1.put("msg", "参数异常");
				return Json.toJson(map1);
			}
		} else {
			map1.put("msg", "参数不足");
		}
		logger.error("=============="+map1.toString());
		return Json.toJson(map1);
	}
	
	/**
	 * 阿里大鱼
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年6月13日 下午3:27:57
	 * @param result
	 * @param style
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@At("/msgRandCode2")
	@Ok("raw:json")
	public String msgRandCode2(@Param("parms") String result,String style,HttpServletRequest request, HttpServletResponse response,HttpSession session){
		// 首先判断传参过来是否为null
		if (result != null && !"".equals(result)) {
			logger.error("解析前result===" + result);
			// 先对字段进行解密
			String decode;
			byte[] en_result = new BigInteger(result, 16).toByteArray();
			byte[] de_result;
			try {
				de_result = RSAUtil.decrypt(RSAUtil.getKeyPair().getPrivate(), en_result);
				StringBuffer sb = new StringBuffer();
				sb.append(new String(de_result));
				decode = sb.reverse().toString();
				decode = URLDecoder.decode(decode, "UTF-8");
				logger.error("解析后===" + decode);
				String a[] = decode.split(",");
				String phone = a[0];// 手机号
				String mac = a.length == 1 ? MsgUtil.randCode() : a[1];// mac
				String ip = "";
				if (a.length == 3){
					ip = a[2];// IP
				}
				String telephone = phone.replace(" ", "");// 去除手机号空格
				String oldeCode = (String) session.getAttribute(telephone);
				// 发送验证码如果上次验证码未过期则直接把上次验证码返回给用户否则直接生成
				String templateCode = "";
				// 判断发送验证码模板信息
				if (style.equals("0")) {// 注册密码
					templateCode = "SMS_137411542";
				} else if (style.equals("1")) {// 设置密码
					templateCode = "SMS_137411542";
				} else if (style.equals("2")) {// 忘记密码
					templateCode = "SMS_137411542";
				}
				// 首先根据mac值等等唯一标识查询当前设备中发验证码的次数
				PhoneCode pc = new PhoneCode();
				pc.setMac(mac);
				pc.setCrateTime(DateUtil.getDate());
				pc.setPhone(telephone);
				// 第二个当前mac设备当天只能发三条
				int macCount = unitePortalServiceImpl.countPhoneMac(pc);
				// 第三个验证同一个手机号用户当天只能发送三条；
				int phoneCount = unitePortalServiceImpl.countPhone(pc);
				// 三层校验，浏览器、mac设备标识、注册手机号
				int count = 1;
				// 获取可以发送的次数
				int number = Integer.parseInt(SetSystemProperty.propertyName("count"));
				SendSmsState sendSmsState = new SendSmsState();
				sendSmsState.setTelphone(telephone);
				sendSmsState.setMac(mac);
				sendSmsState.setSmsType(1);
				sendSmsState.setCreateTime(new Date());
				
				if (macCount < number && phoneCount < number) {
					// 部署项目所用
					String code = oldeCode == null ? MsgUtil.randCode() : oldeCode;
					String str = sendMsgToUser(telephone, templateCode, code);
					logger.error("----"+telephone+"" + templateCode+"=========="+code+"短信发送结果===" + str);
					if ("success".equals(str)) {
						// 验证码发送成功后给数据库插入相关日志
						session.setAttribute(telephone, code);
						// 设置session存活时间位5分钟
						if (oldeCode == null) {
							session.setMaxInactiveInterval(5 * 60);
						}
						PhoneCode phoneCode = new PhoneCode();
						phoneCode.setIp(ip);
						phoneCode.setPhone(telephone);
						phoneCode.setCode(code);
						phoneCode.setMac(mac);
						phoneCode.setCount(count + macCount);
						phoneCode.setCrateTime(DateUtil.getDate());
						unitePortalServiceImpl.insertPhoneCode(phoneCode);
						map1.put("result", 0);
						map1.put("msg", "成功验证码:" + code);
						sendSmsState.setResult(1);
					} else {
						map1.put("result", -1);
						map1.put("msg", "发送验证码失败");
						sendSmsState.setResult(0);
					}
				} else {
					map1.put("result", -2);
					map1.put("msg", "发送太频繁");
					sendSmsState.setResult(0);
				}
				unitePortalServiceImpl.insertSendSmsState(sendSmsState);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				map1.put("msg", "参数异常");
				return Json.toJson(map1);
			}
		} else {
			map1.put("msg", "参数不足");
		}
		logger.error("=============="+map1.toString());
		return Json.toJson(map1);
	}


	/**
	 * @Description 短信发送接口
	 * @param telephone---
	 *            接收人电话
	 * @param templateCode---
	 *            模板短信id(后台自定义模板)
	 * @param code---
	 *            验证码
	 * @return 成功响应的话返回json格式的字符串,异常了返回error
	 */
	public static String sendMsgToUser(String telephone, String templateCode, String code) {
		// 发短信
		SendSmsResponse response = null;
		try {
			response = sendSms(telephone, templateCode, code);
			System.out.println("短信接口返回的数据----------------");
			System.out.println("Code=" + response.getCode());
			System.out.println("Message=" + response.getMessage());
			System.out.println("RequestId=" + response.getRequestId());
			System.out.println("BizId=" + response.getBizId());
			// Thread.sleep(3000L);
			// 查明细
			if (response.getCode() != null && response.getCode().equals("OK")) {
				QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(telephone, response.getBizId());
				System.out.println("短信明细查询接口返回数据----------------");
				System.out.println("Code=" + querySendDetailsResponse.getCode());
				return "success";
				// System.out.println("Message=" +
				// querySendDetailsResponse.getMessage());
				// int i = 0;
				// for(QuerySendDetailsResponse.SmsSendDetailDTO
				// smsSendDetailDTO :
				// querySendDetailsResponse.getSmsSendDetailDTOs())
				// {
				// System.out.println("SmsSendDetailDTO["+i+"]:");
				// System.out.println("Content=" +
				// smsSendDetailDTO.getContent());
				// System.out.println("ErrCode=" +
				// smsSendDetailDTO.getErrCode());
				// System.out.println("OutId=" + smsSendDetailDTO.getOutId());
				// System.out.println("PhoneNum=" +
				// smsSendDetailDTO.getPhoneNum());
				// System.out.println("ReceiveDate=" +
				// smsSendDetailDTO.getReceiveDate());
				// System.out.println("SendDate=" +
				// smsSendDetailDTO.getSendDate());
				// System.out.println("SendStatus=" +
				// smsSendDetailDTO.getSendStatus());
				// System.out.println("Template=" +
				// smsSendDetailDTO.getTemplateCode());
				// }
				// System.out.println("TotalCount=" +
				// querySendDetailsResponse.getTotalCount());
				// System.out.println("RequestId=" +
				// querySendDetailsResponse.getRequestId());
			}
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		return "error";
	}

	public static SendSmsResponse sendSms(String telephone, String templateCode, String code) throws ClientException {

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(telephone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(SIGN);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(templateCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam("{\"code\":\"" + code + "\"}");

		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		request.setOutId(telephone);

		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

		return sendSmsResponse;
	}

	public static QuerySendDetailsResponse querySendDetails(String telephone, String bizId) throws ClientException {

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象
		QuerySendDetailsRequest request = new QuerySendDetailsRequest();
		// 必填-号码
		request.setPhoneNumber(telephone);
		// 可选-流水号
		request.setBizId(bizId);
		// 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		request.setSendDate(ft.format(new Date()));
		// 必填-页大小
		request.setPageSize(10L);
		// 必填-当前页码从1开始计数
		request.setCurrentPage(1L);

		// hint 此处可能会抛出异常，注意catch
		QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

		return querySendDetailsResponse;
	}
	
	
	public static void main(String[] args) {
		
		sendMsgToUser("18602754269,13543053798,13971373346,15717116326,15972180115,18827035074,18671121653,15071494743,18571673823,18672774052,13163252512,18674017530,15007128020,15711941010,15997401050", "SMS_140725866", "15");
		
//    	
//    	try {
//    		String clientId = "300011026401";
//        	String firt_url = "https://open.mmarket.com/omee-aus/services/oauth/authorize";
//        	String param = "responseType=code&scope=getUserInfo&clientId="+clientId+"&redirectUri=http://dev.10086.cn&clientState=test&display=mobile";
//        	String result = HttpRequestUtil.sendGetHeaderFields(firt_url, param);
//        	
//        	logger.error("==========="+result);
//        	if(result != null){
//        		int startnum = result.lastIndexOf('=')+1;
//        		int endnum = result.length()-1;
//        		String relayState = result.substring(startnum ,endnum);
//        		logger.error(relayState);
//        	}
//        	
//        	
//			Thread.sleep(1000);
//			
//			String telephone = "15972935811";
//			String code = "";
//			String appid = "300011026401";
//			String key = "wekfiowehfg";
//			String res_str = HttpRequestUtil.sendPost("http://www.cmpassport.com/openapi/sendDynaPwd", "account="+telephone+"&pvccode="+code+"&clientid="+clientId+"&appid="+appid+"&check="+MD5.MD5Encode(telephone+code+clientId+appid+key), false);
//			System.out.println(res_str);
//			logger.error(res_str);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
}
