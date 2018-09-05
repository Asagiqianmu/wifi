package com.fxwx.entiy;


import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 用户登录验证类型实体类
 * @author Administrator
 *
 */
@Table("t9_login_smscode_type")
public class LoginSmsCodeType {

	@Id
	private int id;//主键id
	@Column("sms_type")
	private int smsType;//短信类型
	@Column("sms_name")
	private String smsName;//短信平台名称
	@Column("state")
	private int state;//是否可用
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSmsType() {
		return smsType;
	}
	public void setSmsType(int smsType) {
		this.smsType = smsType;
	}
	public String getSmsName() {
		return smsName;
	}
	public void setSmsName(String smsName) {
		this.smsName = smsName;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "LoginSmsCodeType [id=" + id + ", smsType=" + smsType + ", smsName=" + smsName + ", state=" + state + "]";
	}
	
}
