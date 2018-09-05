package com.fxwx.entiy;
import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t9_routers_auth_retance")
public class RoutersAuthRetance {
	
	@Id
	private int id;// 主键
	
	@Column("nasid")
	private String nasId;
	
	@Column("auth_type_id")
	private int authTypeId;//认证库id

	@Column("cycle_time")
	private int cycleTime;
	
	@Column("create_time")
	private Timestamp createTime;//认证方式 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNasId() {
		return nasId;
	}

	public void setNasId(String nasId) {
		this.nasId = nasId;
	}

	public int getAuthTypeId() {
		return authTypeId;
	}

	public void setAuthTypeId(int authTypeId) {
		this.authTypeId = authTypeId;
	}

	public int getCycleTime() {
		return cycleTime;
	}

	public void setCycleTime(int cycleTime) {
		this.cycleTime = cycleTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "RoutersAuthRetance [id=" + id + ", nasId=" + nasId + ", authTypeId=" + authTypeId + ", cycleTime=" + cycleTime + ", createTime=" + createTime + "]";
	}

	
	
}
