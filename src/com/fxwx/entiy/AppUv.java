package com.fxwx.entiy;
import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t8_uv")
public class AppUv {
	
	@Id
	private int id;// 主键
	
	
	@Column("appid")
	private String appId;//appId 系统分配
	
	@Column("mac")
	private String mac;
	
	@Column("create_time")
	private String createTime;

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

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "AppUv [id=" + id + ", appId=" + appId + ", mac=" + mac + ", createTime=" + createTime + "]";
	}
	
}
