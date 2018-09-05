package com.fxwx.util.pay_request;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class PayResponseData {
	private String appid = "";
	private String code_url = "";
	private String device_info = "";
	private String mch_id = "";
	private String nonce_str = "";
	private String prepay_id = "";
	private String result_code = "";
	private String return_code = "";
	private String return_msg = "";
	private String trade_type = "";
	private String err_code = "";
	private String sign = "";
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getCode_url() {
		return code_url;
	}
	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getPrepay_id() {
		return prepay_id;
	}
	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	@Override
	public String toString() {
		return "PayResponseData [appid=" + appid + ", code_url=" + code_url + ", device_info=" + device_info
				+ ", mch_id=" + mch_id + ", nonce_str=" + nonce_str + ", prepay_id=" + prepay_id + ", result_code="
				+ result_code + ", return_code=" + return_code + ", return_msg=" + return_msg + ", trade_type="
				+ trade_type + ", sign=" + sign + "]";
	}
	
	
	// 把当前对象的数据转为Map
	public Map<String, Object> toMap() {
		Map<String, Object> map = new TreeMap<>();
		Field[] fields = getClass().getDeclaredFields();
		try {
			Object obj = null;
			for (Field field : fields) {
				obj = field.get(this);
				if (obj != null) {
					map.put(field.getName(), obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
