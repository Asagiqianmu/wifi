package com.fxwx.entiy;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 广告访问记录
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2018年4月10日 下午4:29:16
 */
@Table("t10_ad_access_record")
public class AdAccessRecord {
	
	@Id
	private int id;//主键id
	@Column("ad_id")
	private int adId;//广告ID
	@Column("usermac")
	private String userMac;//用户终端Mac
	@Column("systype")
	private int sysType;//终端系统类型
	@Column("browse")
	private int browse;//浏览次数
	@Column("click")
	private int click;//点击访问次数
	@Column("lasttime")
	private String lastTime;//最近一次点击访问时间
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
	public String getUserMac() {
		return userMac;
	}
	public void setUserMac(String userMac) {
		this.userMac = userMac;
	}
	public int getSysType() {
		return sysType;
	}
	public void setSysType(int sysType) {
		this.sysType = sysType;
	}
	public int getBrowse() {
		return browse;
	}
	public void setBrowse(int browse) {
		this.browse = browse;
	}
	public int getClick() {
		return click;
	}
	public void setClick(int click) {
		this.click = click;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "AdAccessRecord [id=" + id + ", adId=" + adId + ", userMac=" + userMac + ", sysType=" + sysType + ", browse=" + browse + ", click=" + click + ", lastTime=" + lastTime + ", createTime="
				+ createTime + "]";
	}
		
}
