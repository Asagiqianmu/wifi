package com.fxwx.entiy;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_ap_cashpledge")
public class APCashPledge {
	
	@Id
	private int id;

	@Column("user_mac")
	private int userMac;//用户id
	
	@Column("ap_mac")
	private String apMac;//mac地址
	@Column("amount")
	private BigDecimal amount;//用户押金
	@Column("order_num")
	private int orderNum;//订单号
	@Column("createtime")
	private Timestamp createtime;//创建时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserMac() {
		return userMac;
	}
	public void setUserMac(int userMac) {
		this.userMac = userMac;
	}
	public String getApMac() {
		return apMac;
	}
	public void setApMac(String apMac) {
		this.apMac = apMac;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	@Override
	public String toString() {
		return "APCashPledge [id=" + id + ", userMac=" + userMac + ", apMac=" + apMac + ", amount=" + amount
				+ ", orderNum=" + orderNum + ", createtime=" + createtime + "]";
	}
}
