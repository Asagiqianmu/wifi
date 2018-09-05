package com.fxwx.util.pay_request;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class PayRequestData {

	private String appid = "";
	private String mch_id = "";
	private String device_info = "";
	private String nonce_str = "";
	private String sign = "";
	private String sign_type = "";
	private String body = "";
	private String detail = "";
	private String attach = "";
	private String out_trade_no = "";
	private String fee_type = "";
	private int total_fee = 0;
	private String spbill_create_ip = "";
	private String time_start = "";
	private String time_expire = "";
	private String goods_tag = "";
	private String notify_url = "";
	private String trade_type = "";
	private String product_id = "";
	private String limit_pay = "";
	private String open_id="";
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public int getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}
	public String getTime_expire() {
		return time_expire;
	}
	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}
	public String getGoods_tag() {
		return goods_tag;
	}
	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getLimit_pay() {
		return limit_pay;
	}
	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	/**
	 * @param appid
	 * @param mch_id
	 * @param device_info
	 * @param nonce_str
	 * @param sign
	 * @param sign_type
	 * @param body
	 * @param detail
	 * @param attach
	 * @param out_trade_no
	 * @param fee_type
	 * @param total_fee
	 * @param spbill_create_ip
	 * @param time_start
	 * @param time_expire
	 * @param goods_tag
	 * @param notify_url
	 * @param trade_type
	 * @param product_id
	 * @param limit_pay
	 * @param open_id
	 */
	public PayRequestData(String appid, String mch_id, String device_info, String nonce_str, String sign,
			String sign_type, String body, String detail, String attach, String out_trade_no, String fee_type,
			int total_fee, String spbill_create_ip, String time_start, String time_expire, String goods_tag,
			String notify_url, String trade_type, String product_id, String limit_pay, String open_id) {
		super();
		this.appid = appid;
		this.mch_id = mch_id;
		this.device_info = device_info;
		this.nonce_str = nonce_str;
		this.sign = sign;
		this.sign_type = sign_type;
		this.body = body;
		this.detail = detail;
		this.attach = attach;
		this.out_trade_no = out_trade_no;
		this.fee_type = fee_type;
		this.total_fee = total_fee;
		this.spbill_create_ip = spbill_create_ip;
		this.time_start = time_start;
		this.time_expire = time_expire;
		this.goods_tag = goods_tag;
		this.notify_url = notify_url;
		this.trade_type = trade_type;
		this.product_id = product_id;
		this.limit_pay = limit_pay;
		this.open_id = open_id;
	}
	
	/**
	 * @param appid appid
	 * @param mch_id 商户好
	 * @param device_info 设备信息
	 * @param sign_type 签名类型
	 * @param fee_type  币种类型
	 * @param spbill_create_ip 商户的发起ip
	 * @param notify_url 微信通知商户通知url
	 * @param trade_type 交易类型
	 * @param product_id 商户的商品id
	 * @param limit_pay  是否使用信用卡
	 * @param open_id    微信的用户id //不使用此参数
	 */
	public PayRequestData(String appid, String mch_id, String device_info, 
			 String sign_type, String fee_type,String spbill_create_ip, 
			 String notify_url, String trade_type, 
			 String product_id, String limit_pay, String open_id) {
	
		this.appid = appid;
		this.mch_id = mch_id;
		this.device_info = device_info;
		this.sign_type = sign_type;
		this.fee_type = fee_type;
		this.spbill_create_ip = spbill_create_ip;
		this.notify_url = notify_url;
		this.trade_type = trade_type;
		this.product_id = product_id;
		this.limit_pay = limit_pay;
		this.open_id = open_id;
	}
	
	
	/**
	 * 
	 */
	public PayRequestData() {
		super();
	}
	
	
	//把当前对象的数据转为Map
	public Map<String, Object> toMap() {
		Map<String, Object> map = new TreeMap<>();
		Field [] fields = getClass().getDeclaredFields();
		try {
			Object obj = null;
			for(Field field : fields ) {
				obj = field.get(this);
				if( obj != null ) {
					map.put(field.getName(), obj);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	@Override
	public String toString() {
		return "PayRequestData [appid=" + appid + ", mch_id=" + mch_id + ", device_info=" + device_info + ", nonce_str="
				+ nonce_str + ", sign=" + sign + ", sign_type=" + sign_type + ", body=" + body + ", detail=" + detail
				+ ", attach=" + attach + ", out_trade_no=" + out_trade_no + ", fee_type=" + fee_type + ", total_fee="
				+ total_fee + ", spbill_create_ip=" + spbill_create_ip + ", time_start=" + time_start + ", time_expire="
				+ time_expire + ", goods_tag=" + goods_tag + ", notify_url=" + notify_url + ", trade_type=" + trade_type
				+ ", product_id=" + product_id + ", limit_pay=" + limit_pay + ", open_id=" + open_id + "]";
	}
	

}
