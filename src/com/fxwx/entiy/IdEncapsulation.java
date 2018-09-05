package com.fxwx.entiy;

/**
 * 相关ID封装类
 * @author Administrator
 *
 */
public class IdEncapsulation {

	private int userId;//用户ID
	private int siteId;//场所ID
	private String mac;//mac值
	private String nasid;//nasid值
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getNasid() {
		return nasid;
	}
	public void setNasid(String nasid) {
		this.nasid = nasid;
	}
	@Override
	public String toString() {
		return "IdEncapsulation [userId=" + userId + ", siteId=" + siteId
				+ ", mac=" + mac + ", nasid=" + nasid + "]";
	}
}
