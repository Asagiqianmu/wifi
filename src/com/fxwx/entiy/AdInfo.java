package com.fxwx.entiy;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 广告信息
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2018年4月10日 下午4:29:16
 */
@Table("t10_ad_info")
public class AdInfo {

	@Id
	private int id;//主键id
	@Column("title")
	private String title;//标题
	@Column("image")
	private String image;//图片
	@Column("a_url")
	private String aUrl;//安卓跳转地址
	@Column("ios_url")
	private String iosUrl;//IOS跳转地址
	@Column("priority")
	private int priority;//优先级，数字越小，级别越高
	@Column("ad_status")
	private int adStatus;//广告状态，0下架，1上架
	@Column("createtime")
	private Date createTime;//创建时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getaUrl() {
		return aUrl;
	}
	public void setaUrl(String aUrl) {
		this.aUrl = aUrl;
	}
	public String getIosUrl() {
		return iosUrl;
	}
	public void setIosUrl(String iosUrl) {
		this.iosUrl = iosUrl;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getAdStatus() {
		return adStatus;
	}
	public void setAdStatus(int adStatus) {
		this.adStatus = adStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "AdInfo [id=" + id + ", title=" + title + ", image=" + image + ", aUrl=" + aUrl + ", iosUrl=" + iosUrl + ", priority=" + priority + ", adStatus=" + adStatus + ", createTime="
				+ createTime + "]";
	}
	
}
