package com.fxwx.entiy;

import java.sql.Timestamp;

/**
 * 场所套餐
 * 
 * @author Dell
 *
 */
public class SiteCombo {
	private int id;
	private double price1;// 一天会员价
	private double price2;// 半月会员价
	private double price3;// 一月会员价
	private int isCashpledge;// 是否要交押金(0-不需要,1-需要)
	private String declare;// 套餐说明
	private Timestamp create_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPrice1() {
		return price1;
	}

	public void setPrice1(double price1) {
		this.price1 = price1;
	}

	public double getPrice2() {
		return price2;
	}

	public void setPrice2(double price2) {
		this.price2 = price2;
	}

	public double getPrice3() {
		return price3;
	}

	public void setPrice3(double price3) {
		this.price3 = price3;
	}

	public int getIsCashpledge() {
		return isCashpledge;
	}

	public void setIsCashpledge(int isCashpledge) {
		this.isCashpledge = isCashpledge;
	}

	public String getDeclare() {
		return declare;
	}

	public void setDeclare(String declare) {
		this.declare = declare;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	@Override
	public String toString() {
		return "SiteCombo [id=" + id + ", price1=" + price1 + ", price2=" + price2 + ", price3=" + price3
				+ ", isCashpledge=" + isCashpledge + ", declare=" + declare + ", create_time=" + create_time + "]";
	}

}
