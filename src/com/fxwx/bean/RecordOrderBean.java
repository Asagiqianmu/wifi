package com.fxwx.bean;


/**
 * 此类描述了订单查询结果的封装bean
 * Copyright (c) All Rights Reserved, 2017.
 * 版权所有                   dfgs Information Technology Co .,Ltd
 * @Project		UnitePortal
 * @File		RecordOrderBean.java
 * @Date		2017年1月11日 上午10:20:59
 * @Author		gyj
 */
public class RecordOrderBean {
	
	private int userId;//用户id
	private int siteId;//场所id
	
	private String siteName;//场所名
	
	private String orderNum;//订单号
	
	private String paramString;//存贮信息字符串
	
	private String state;
	
	private int payType;
	
	private int isFinishi;
	
	private String timestamp;

	private int buyNum;
	
	private float allPirce;
	
	private String priceName;
	
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
 
	public String getParamString() {
		return paramString;
	}

	public void setParamString(String paramString) {
		this.paramString = paramString;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}
	
	public int getIsFinishi() {
		return isFinishi;
	}

	public void setIsFinishi(int isFinishi) {
		this.isFinishi = isFinishi;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public float getAllPirce() {
		return allPirce;
	}

	public void setAllPirce(float allPirce) {
		this.allPirce = allPirce;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	@Override
	public String toString() {
		return "RecordOrderBean [userId=" + userId + ", siteId=" + siteId
				+ ", siteName=" + siteName + ", orderNum=" + orderNum
				+ ", paramString=" + paramString + ", state=" + state
				+ ", payType=" + payType + ", isFinishi=" + isFinishi
				+ ", timestamp=" + timestamp + ", buyNum=" + buyNum
				+ ", allPirce=" + allPirce + ", priceName=" + priceName + "]";
	}
 
	 
}
