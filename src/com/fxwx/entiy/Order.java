package com.fxwx.entiy;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 订单信息
 * 
 * @author Dell
 *
 */
@Table("t_order")
public class Order {
	@Id
	private int id;

	@Column("appid")
	private String appId;// 公众账号ID
	@Column("mch_id")
	private String mchId;// 商户号
	@Column("device_info")
	private String deviceInfo;// 设备号
	@Column("nonce_str")
	private String nonceStr;// 随机字符串
	@Column("sign")
	private String sign;// 签名
	@Column("sign_type")
	private String signType;// 签名类型
	@Column("body")
	private String body;// 商品描述
	@Column("detail")
	private String detail;// 商品详情
	@Column("attach")
	private String attach;// 附加数据:(支付类型 1:押金,2:月租)
	@Column("out_trade_no")
	private String out_trade_no;// 商户订单号
	@Column("fee_type")
	private String feeType;// 货币类型
	@Column("total_fee")
	private BigDecimal totalFee;// 总金额
	@Column("spbill_create_ip")
	private String spbillCreateIp;// 终端IP
	@Column("time_start")
	private String timeStart;// 终端IP
	@Column("time_expire")
	private String timeExpire;// 终端IP
	@Column("goods_tag")
	private String goodsTag;// 商品标记
	@Column("notify_url")
	private String notifyUrl;// 通知回调函数url
	@Column("trade_type")
	private String tradeType;// 交易类型
	@Column("product_id")
	private String productId;// 商品id
	@Column("limit_pay")
	private String limitPay;// 指定支付方式
	@Column("openid")
	private String openId;// 用户标识
	@Column("scene_info")
	private String sceneInfo; // 场景信息
	@Column("order_num")
	private String orderNum; // 订单号，全局唯一
	@Column("buy_num")
	private int buyNum; // 购买数量
	@Column("statue")
	private int statue; // 订单状态，0未完成。1完成。2放弃
	@Column("is_delete")
	private int isDelete; // 是否删除，0未删除，1删除
	@Column("back_json")
	private String backJson; // 备份交易信息，map的json格式
	@Column("update_time")
	private Timestamp updateTime;
	@Column("create_time")
	private Timestamp createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
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

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	public String getGoodsTag() {
		return goodsTag;
	}

	public void setGoodsTag(String goodsTag) {
		this.goodsTag = goodsTag;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getLimitPay() {
		return limitPay;
	}

	public void setLimitPay(String limitPay) {
		this.limitPay = limitPay;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getSceneInfo() {
		return sceneInfo;
	}

	public void setSceneInfo(String sceneInfo) {
		this.sceneInfo = sceneInfo;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public int getStatue() {
		return statue;
	}

	public void setStatue(int statue) {
		this.statue = statue;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getBackJson() {
		return backJson;
	}

	public void setBackJson(String backJson) {
		this.backJson = backJson;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeExpire() {
		return timeExpire;
	}

	public void setTimeExpire(String timeExpire) {
		this.timeExpire = timeExpire;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", appId=" + appId + ", mchId=" + mchId + ", deviceInfo=" + deviceInfo
				+ ", nonceStr=" + nonceStr + ", sign=" + sign + ", signType=" + signType + ", body=" + body
				+ ", detail=" + detail + ", attach=" + attach + ", out_trade_no=" + out_trade_no + ", feeType="
				+ feeType + ", totalFee=" + totalFee + ", spbillCreateIp=" + spbillCreateIp + ", timeStart=" + timeStart
				+ ", timeExpire=" + timeExpire + ", goodsTag=" + goodsTag + ", notifyUrl=" + notifyUrl + ", tradeType="
				+ tradeType + ", productId=" + productId + ", limitPay=" + limitPay + ", openId=" + openId
				+ ", sceneInfo=" + sceneInfo + ", orderNum=" + orderNum + ", buyNum=" + buyNum + ", statue=" + statue
				+ ", isDelete=" + isDelete + ", backJson=" + backJson + ", updateTime=" + updateTime
				+ ", createTime=" + createTime + "]";
	}
}
