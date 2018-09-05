package com.fxwx.entiy;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 发送短信记录实体类
 * @author Administrator
 *
 */
@Table("t9_sendsms_state")
public class SendSmsState {

	@Id
	private int id;//主键id
	@Column("telphone")
	private String telphone;//手机号码
	@Column("mac")
	private String mac;//手机MAC
	@Column("sms_type")
	private int smsType;//短信类型
	@Column("result")
	private int result;//短信发送是否成功结果
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
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public int getSmsType() {
		return smsType;
	}
	public void setSmsType(int smsType) {
		this.smsType = smsType;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "SendSmsState [id=" + id + ", telphone=" + telphone + ", mac=" + mac + ", smsType=" + smsType + ", result=" + result + ", createTime=" + createTime + "]";
	}
	
}
