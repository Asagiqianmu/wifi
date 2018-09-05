package com.fxwx.entiy;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 广告效果
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2018年4月10日 下午4:29:16
 */
@Table("t10_ad_effect")
public class AdEffect {

	@Id
	private int id;//主键id
	@Column("ad_id")
	private int adId;//广告ID
	@Column("show_num")
	private int showNum;//展示次数
	@Column("pv")
	private int pv;//点击量
	@Column("uv")
	private int uv;//独立访客量
	@Column("updatetime")
	private String updateTime;//更新时间
	@Column("createtime")
	private Date createTime;//创建时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAdId() {
		return adId;
	}
	public void setAdId(int adId) {
		this.adId = adId;
	}
	public int getShowNum() {
		return showNum;
	}
	public void setShowNum(int showNum) {
		this.showNum = showNum;
	}
	public int getPv() {
		return pv;
	}
	public void setPv(int pv) {
		this.pv = pv;
	}
	public int getUv() {
		return uv;
	}
	public void setUv(int uv) {
		this.uv = uv;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "AdEffect [id=" + id + ", adId=" + adId + ", showNum=" + showNum + ", pv=" + pv + ", uv=" + uv + ", updateTime=" + updateTime + ", createTime=" + createTime + "]";
	}

	
}
