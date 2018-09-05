package com.fxwx.entiy;

import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_commercial_tenant")//商户表
public class CommercialTenant {
	@Id
	private int id;
	@Column("appid")
	private String appid;//公众账号ID
	@Column("pay_app")
	private int payApp;//0--支付宝,1--微信
	@Column("content")
	private String content;//微信(mch_id:商户号,key:商户平台设置的密钥key,URL:支付接口),支付宝(gateway:网关,app_private_key:私钥,alipay_public_key:公钥)。json格式储存数据
	@Column("affiliation_company")
	private String affiliationCompany;//商户归属公司名称
	@Column("create_time")
	private Timestamp createTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public int getPayApp() {
		return payApp;
	}
	public void setPayApp(int payApp) {
		this.payApp = payApp;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getAffiliationCompany() {
		return affiliationCompany;
	}
	public void setAffiliationCompany(String affiliationCompany) {
		this.affiliationCompany = affiliationCompany;
	}
	@Override
	public String toString() {
		return "CommercialTenant [id=" + id + ", appid=" + appid + ", payApp=" + payApp + ", content=" + content
				+ ", affiliationCompany=" + affiliationCompany + ", createTime=" + createTime + "]";
	}
	
}
