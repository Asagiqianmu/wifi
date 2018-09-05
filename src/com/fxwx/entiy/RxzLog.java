package com.fxwx.entiy;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 任子行日志状态实体
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2017年10月24日 上午10:32:07
 */
@Table("t9_rzx_log")
public class RxzLog {

	@Id
	private int id;//主键id
	@Column("auth_type")
	private String authType;//文件路径
	@Column("auth_account")
	private String authAccount;//文件路径
	@Column("mac")
	private String mac;//文件路径
	@Column("ap_mac")
	private String apMac;//文件路径
	@Column("t_state")
	private int tstate;//文件路径
	@Column("createtime")
	private Date createTime;//创建时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public String getAuthAccount() {
		return authAccount;
	}
	public void setAuthAccount(String authAccount) {
		this.authAccount = authAccount;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getApMac() {
		return apMac;
	}
	public void setApMac(String apMac) {
		this.apMac = apMac;
	}
	public int getTstate() {
		return tstate;
	}
	public void setTstate(int tstate) {
		this.tstate = tstate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "RxzLog [id=" + id + ", authType=" + authType + ", authAccount=" + authAccount + ", mac=" + mac + ", apMac=" + apMac + ", tstate=" + tstate + ", createTime=" + createTime + "]";
	}
	
	
}
