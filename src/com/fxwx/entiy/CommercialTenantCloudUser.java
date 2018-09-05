package com.fxwx.entiy;

import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_commercial_tenant_cloud_user")//商户与平台用户关联表
public class CommercialTenantCloudUser {
	@Id
	private int id;
	@Column("commercial_tenant_id")
	private int commercialTenantId;
	@Column("cloud_user_id")
	private int cloudUserId;
	@Column("create_time")
	private Timestamp createTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCommercialTenantId() {
		return commercialTenantId;
	}
	public void setCommercialTenantId(int commercialTenantId) {
		this.commercialTenantId = commercialTenantId;
	}
	public int getCloudUserId() {
		return cloudUserId;
	}
	public void setCloudUserId(int cloudUserId) {
		this.cloudUserId = cloudUserId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "CommercialTenantCloudUser [id=" + id + ", commercialTenantId=" + commercialTenantId + ", cloudUserId="
				+ cloudUserId + ", createTime=" + createTime + "]";
	} 
}
