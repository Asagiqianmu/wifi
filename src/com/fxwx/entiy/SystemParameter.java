package com.fxwx.entiy;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t7_system_parameter")
public class SystemParameter {
	@Id
	private int id; //主键
	@Column("sys_type")
	private String sysType; //系统类型
	@Column("sys_param")
	private String sysParam; //系统参数
	@Column("param_type")
	private int paramType; //参数类型
	@Column("create_time")
	private Date createTime; //创建时间

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSysType() {
		return sysType;
	}
	public void setSysType(String sysType) {
		this.sysType = sysType;
	}
	public String getSysParam() {
		return sysParam;
	}
	public void setSysParam(String sysParam) {
		this.sysParam = sysParam;
	}
	public int getParamType() {
		return paramType;
	}
	public void setParamType(int paramType) {
		this.paramType = paramType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
