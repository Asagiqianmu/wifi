package com.fxwx.util;


import java.util.Map;

import net.sf.json.JSONObject;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

import com.google.gson.Gson;

/**
 * @author pengxw E-mail:pengxianwei@kdfwifi.com
 * @version 创建时间：2016年7月27日 上午11:36:28
 * @describe
 */
public class ReturnJson {
	private int code;
	private Object data;
	private String msessage;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * @return the msessage
	 */
	public String getMsessage() {
		return msessage;
	}
	/**
	 * @param msessage the msessage to set
	 */
	public void setMsessage(String msessage) {
		this.msessage = msessage;
	}
	public String JsonString(){
		return Json.toJson(this,JsonFormat.compact());
	}
	
	public static void main(String [] args){
		String jjson = "{'emailaddrs': [{'type': 'work','value': 'kelly@seankelly.biz'},{'type': 'home','pref': 1, 'value': 'kelly@seankelly.tv'}],'telephones': [{'type': 'work', 'pref': 1, 'value': '214 555 1212'}],'urls': [ {'type': 'work', 'value': 'http://seankelly.biz/'},{'type': 'home', 'value': 'http://seankelly.tv/'}]}";
//		String json = "{'msg': '未注册','result': 0,'msgCode': '验证码校验正确'}";
//		 JSONObject jsonObject = new JSONObject().fromObject(jjson);  
//		 Object obje=jsonObject.get("emailaddrs");  
//		Map<String, Object> pets = Json.fromJsonAsMap(Object.class, jjson);
		 Gson gson = new Gson();
		 Map<String, Object> pets = (Map<String, Object>) gson.fromJson(jjson, Object.class);
		System.out.println(pets.get("emailaddrs"));
		System.out.println(pets.get("telephones"));
       System.out.println(pets.get("urls"));
	}
}
