package com.fxwx.entiy;
import java.sql.Timestamp;
import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t8_portal_app_user")
public class AppUser {
	
	@Id
	private int id;// 主键
	
	@Column("app_user_name")
	private String openid;//app名称
	
	@Column("app_user_key")
	private String openkey;//appId 系统分配
	
	@Column("create_time")
	private Date createTime;//创建时间
	
//	@Column("assign_account")
//	private String assignAccount;//前期app未提供手机账号暂为app分配的账号
//	
	@Column("user_id")
	private int userId;//主账户id
	
	@Column("online_time")
	private int onlineTime;//app用户上网时长 暂时处理 ，若有手机号则需改动

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenkey() {
		return openkey;
	}

	public void setOpenkey(String openkey) {
		this.openkey = openkey;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(int onlineTime) {
		this.onlineTime = onlineTime;
	}

//	public String getAssignAccount() {
//		return assignAccount;
//	}
//
//	public void setAssignAccount(String assignAccount) {
//		this.assignAccount = assignAccount;
//	}

	@Override
	public String toString() {
		return "AppUser [id=" + id + ", openid=" + openid + ", openkey=" + openkey + ", createTime=" + createTime
				+ ", userId=" + userId + ", onlineTime=" + onlineTime + "]";
	}

}
