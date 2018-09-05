package com.fxwx.entiy;

import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t7_gift_length")
public class GiftLength {
	
	@Id
	private int id;// 主键
	
	@Column("site_id")
	private int siteId;// 场所Id
	
	@Column("giftlength")
	private int giftLength;// 赠送数量
	
	@Column("unit")
	private int unit;// 赠送单位
	
	@Column("brithdayNum")
	private int brithdayNum;//用户生日赠送的数量
	
	@Column("birthdayUnit")
	private int birthdayUnit;//用户生日赠送的单位
	
	@Column("createDate")
	private Timestamp createTime;// 创建时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public int getGiftLength() {
		return giftLength;
	}

	public void setGiftLength(int giftLength) {
		this.giftLength = giftLength;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the brithdayNum
	 */
	public int getBrithdayNum() {
		return brithdayNum;
	}

	/**
	 * @param brithdayNum the brithdayNum to set
	 */
	public void setBrithdayNum(int brithdayNum) {
		this.brithdayNum = brithdayNum;
	}

	/**
	 * @return the birthdayUnit
	 */
	public int getBirthdayUnit() {
		return birthdayUnit;
	}

	/**
	 * @param birthdayUnit the birthdayUnit to set
	 */
	public void setBirthdayUnit(int birthdayUnit) {
		this.birthdayUnit = birthdayUnit;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GiftLength [id=" + id + ", siteId=" + siteId + ", giftLength="
				+ giftLength + ", unit=" + unit + ", brithdayNum="
				+ brithdayNum + ", birthdayUnit=" + birthdayUnit
				+ ", createTime=" + createTime + "]";

	
	}

}
