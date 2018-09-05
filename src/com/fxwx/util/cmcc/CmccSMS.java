package com.fxwx.util.cmcc;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.alibaba.fastjson.JSONObject;
import com.fxwx.util.MD5;

/**
 * 和通行证封装工具类
 * 
 * @author dengfei E-mail:dengfei200857@163.com
 *
 */
public class CmccSMS {
	private static final String clientId = "300011026401";
	private static final String appid = "300011026401";
	private static final String key = "wekfiowehfg";
	private static final String authorize_url = "https://open.mmarket.com/omee-aus/services/oauth/authorize";
	private static final String endDynaPwd_url = "http://www.cmpassport.com/openapi/sendDynaPwd";
	private static final String validate_url = "http://www.cmpassport.com/openapi/validateDynamicPwd";
	private static final String apiid = "937615837,937615838,937615839,937615840";

	private static final Log log = Logs.getLog(CmccSMS.class);
	
	private static final String MSG = "{\"0\":\"正在处理.请稍等...\","
			+ "\"1\":\"请输入通行证帐号\","
			+ "\"2\":\"请输入密码\","
			+ "\"3\":\"请输入通行证号、手机号或邮箱\","
			+ "\"4\":\"请输入手机号\","
			+ "\"11\":\"登录名非法\","
			+ "\"21\":\"由于网络或系统原因，暂时无法登录。请稍后再试\","
			+ "\"21\":\"网络或系统原因，暂时无法登录\","
			+ "\"22\":\"参数错误\","
			+ "\"101199\":\"由于网络或系统原因，暂时无法登录。请稍后再试\","
			+ "\"101199\":\"网络或系统原因，暂时无法登录\","
			+ "\"101000\":\"验证成功\","
			+ "\"101101\":\"验证不通过\","
			+ "\"101102\":\"超过最大验证次数20次\","
			+ "\"101103\":\"失败超过三次，需要输入图形验证码\","
			+ "\"101104\":\"图形验证码超时\","
			+ "\"101105\":\"动态短信发出\","
			+ "\"101106\":\"Sim卡认证通过\","
			+ "\"101107\":\"Sim卡认证失败\","
			+ "\"101108\":\"Sim卡认证超时\","
			+ "\"101109\":\"动态短信验证通过\","
			+ "\"101110\":\"动态短信验证不通过\","
			+ "\"101111\":\"动态短信验证超时\","
			+ "\"101112\":\"移动签名认证失败\","
			+ "\"101113\":\"移动签名认证成功\","
			+ "\"101114\":\"移动签名认证超时\","
			+ "\"101115\":\"动态短信认证\","
			+ "\"101117\":\"图片验证码错误，请重新输入\","
			+ "\"101118\":\"您的手机暂不支持一键登录\","
			+ "\"101119\":\"请输入图片验证码\","
			+ "\"101120\":\"您的手机暂不支持本功能\","
			+ "\"101121\":\"请求格式不正确\","
			+ "\"101122\":\"参数校验不通过\","
			+ "\"101123\":\"授权信息同步失败\","
			+ "\"101124\":\"帐号不存在\","
			+ "\"101124_2\":\"操作过于频繁，请稍后再试\","
			+ "\"101125\":\"未绑定手机号，请先绑定\"}";
	
	public static JSONObject msgjson = new JSONObject();
	static {
		msgjson = JSONObject.parseObject(MSG);
	}
	
	public static String getHeaderFeilds(){
		String param = "responseType=code&scope=getUserInfo&clientId=" + clientId + "&redirectUri=http://dev.10086.cn&clientState=test&display=mobile";
		String result = HttpRequestUtil.sendGetHeaderFields(authorize_url, param);

		log.error(result);
		if (result != null) {
			int startnum = result.lastIndexOf('=') + 1;
			int endnum = result.length() - 1;
			String relayState = result.substring(startnum, endnum);
			log.error(relayState);
			return relayState;
		}
		return null;
	}
	
	public static String sendCode(String telphone) {
		String res_str = "";

		String code = "";
//		res_str = HttpRequestUtil.sendPost(endDynaPwd_url,
//				"account=" + telphone + 
//				"&pvccode=" + code + 
//				"&clientid=" + clientId + 
//				"&appid=" + appid + 
//				"&check=" + MD5.MD5Encode(telphone + code + clientId + appid + key), false);
//		
		res_str = HttpRequestUtil.sendPost(endDynaPwd_url, "account="+telphone+"&pvccode="+code+"&clientid="+clientId+"&appid="+appid+"&check="+MD5.MD5Encode(telphone+code+clientId+appid+key), false);
		log.error("#########"+res_str);
		
		return res_str;
	}
	
	public static String validateDynamicPwd(String telphone, String code, String relayState){
//		String resultCode = "";
        String data = "{'username':'"+telphone+"',"
        		+ "'password':'"+code+"',"
        		+ "'callBackURL':'https://open.mmarket.com:443/omee-aus/services/oauth/show',"
        		+ "'operationType':'1',"
        		+ "'asDomain':'dev.10086.cn',"
        		+ "'authType':'UPDS',"
        		+ "'appid':'"+appid+"',"
        		+ "'apiid':'"+apiid+"',"
        		+ "'c':'3',"
        		+ "'relayState':'"+relayState+"'}";
        log.error(data);
        String res_str = HttpRequestUtil.sendPost(validate_url, data.replace("'", "\"") ,false);
        log.error(res_str);
		return res_str;
	}
}
