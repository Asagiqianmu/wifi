package com.fxwx.entiy;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 和通行证回复状态记录实体类
 * @author Administrator
 *
 */
@Table("t9_cmcc_relayState")
public class CmccRelayState {

	@Id
	private int id;//主键id
	@Column("telphone")
	private String telphone;//手机号码
	@Column("relayState")
	private String relayState;//回复状态串码
	@Column("createtime")
	private Date createTime;//创建时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getRelayState() {
		return relayState;
	}
	public void setRelayState(String relayState) {
		this.relayState = relayState;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "CmccRelayState [id=" + id + ", telphone=" + telphone + ", relayState=" + relayState + ", createTime=" + createTime + "]";
	}
	
	
}
