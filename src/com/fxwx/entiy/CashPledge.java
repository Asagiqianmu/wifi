package com.fxwx.entiy;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_ap_cashpledge")
public class CashPledge {
	@Id
	private int id;
	@Column("user_mac")
	private String userMac;
	@Column("apMac")
	private String ap_mac;
	@Column("order_num")
	private String orderNum;
	@Column("amount")
	private BigDecimal amount;
	@Column("createtime")
	private Timestamp createtime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserMac() {
		return userMac;
	}
	public void setUserMac(String userMac) {
		this.userMac = userMac;
	}
	public String getAp_mac() {
		return ap_mac;
	}
	public void setAp_mac(String ap_mac) {
		this.ap_mac = ap_mac;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	@Override
	public String toString() {
		return "CashPledge [id=" + id + ", userMac=" + userMac + ", ap_mac=" + ap_mac + ", orderNum=" + orderNum
				+ ", amount=" + amount + ", createtime=" + createtime + "]";
	}
}
