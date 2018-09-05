package com.fxwx.entiy;
import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t9_feedback")
public class FeedBack {
	
	@Id
	private int id;// 主键
	
	@Column("type_name")
	private String typeName;//反馈类型名称
	
	@Column("type")
	private int type;//反馈类型
	
	@Column("content")
	private String content;//反馈内容
	
	@Column("wifiSite")
	private String wifiSite;//wifi地址
	
	@Column("contactWay")
	private String contactWay;//联系方式
	
	@Column("create_time")
	private Timestamp createTime;//认证方式 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWifiSite() {
		return wifiSite;
	}

	public void setWifiSite(String wifiSite) {
		this.wifiSite = wifiSite;
	}

	public String getContactWay() {
		return contactWay;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	

}
