package com.fxwx.entiy;

import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 
 * 版本信息:
 * @author:dengfei200857@163.com
 * @date: 2017年8月24日 上午11:02:26 
 * @Description:用户MAC对应的用户的OpenID
 * @version: V1.0
 * 版权所有
 */
@Table("t8_wcaht_user_openid")
public class WcahtUserOpenid {

	@Id
	private int id;//主键
	@Column("mac")
	private String mac;//用户MAC
	@Column("appId")
	private String appId;//对应公众号的APPID
	@Column("subscribe")
	private int subscribe;//关注状态：0关注、1取消
	@Column("subscribe_x")
	private int subscribex;//是否是老用户回归
	@Column("nasid")
	private String nasid;//设备的唯一标识NASiD
	@Column("site_id")
	private int siteId;//场所ID
	@Column("openId")
	private String openId;//对应OpenID
	@Column("code")
	private int code;//返回给微信客户端状态码
	@Column("create_time")
	private Timestamp createTime;//创建时间
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public int getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}
	public int getSubscribex() {
		return subscribex;
	}
	public void setSubscribex(int subscribex) {
		this.subscribex = subscribex;
	}
	public String getNasid() {
		return nasid;
	}
	public void setNasid(String nasid) {
		this.nasid = nasid;
	}
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "WcahtUserOpenid [id=" + id + ", mac=" + mac + ", appId=" + appId + ", subscribe=" + subscribe
				+ ", subscribex=" + subscribex + ", nasid=" + nasid + ", siteId=" + siteId + ", openId=" + openId
				+ ", code=" + code + ", createTime=" + createTime + "]";
	}
}
