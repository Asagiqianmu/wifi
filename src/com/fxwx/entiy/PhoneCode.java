package com.fxwx.entiy;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 存储验证码信息表
 * @author Administrator
 *
 */
@Table("t7_user_phone_code")
public class PhoneCode {
	
	@Id
	private int id;  //主键ID
	@Column
	private String phone;  //手机号，用户名
	@Column
	private String code;  //验证码
	@Column
	private String mac;  //mac值标示
	@Column
	private String ip; //往来ip
	@Column
	private String url; //头部信息
	@Column("crate_time")
	private String crateTime; //创建时间
	@Column
	private int count;  //发送的次数
	@Column
	private String cookie; //sessionID
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCrateTime() {
		return crateTime;
	}
	public void setCrateTime(String crateTime) {
		this.crateTime = crateTime;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	@Override
	public String toString() {
		return "PhoneCode [id=" + id + ", phone=" + phone + ", code=" + code
				+ ", mac=" + mac + ", ip=" + ip + ", url=" + url
				+ ", crateTime=" + crateTime + ", count=" + count + ", cookie="
				+ cookie + "]";
	}
}
