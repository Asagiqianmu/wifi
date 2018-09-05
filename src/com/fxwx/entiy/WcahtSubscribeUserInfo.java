package com.fxwx.entiy;

import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 
 * 版本信息:
 * @author:dengfei200857@163.com
 * @date: 2017年9月4日 下午3:37:43 
 * @Description:微信关注用户信息
 * @version: V1.0
 * 版权所有
 */
@Table("t8_wcaht_subscribe_user_info")
public class WcahtSubscribeUserInfo {

	@Id
	private int id;//用户ID
	@Column("user_name")
	private String userName;//用户昵称
	@Column("headimgurl")
	private String headimgurl;//用户头像地址
	@Column("sex")
	private int  sex;//用户性别；1：男2：女
	@Column("subscribe")
	private int subscribe;//关注状态0：未关注1：关注
	@Column("subscribe_time")
	private String subscribeTime;//关注时间
	@Column("address")
	private String address;//用户地址
	@Column("openid")
	private String openId;//用户唯一标示
	@Column("create_time")
	private Timestamp createime;//创建时间

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}
	public String getSubscribeTime() {
		return subscribeTime;
	}
	public void setSubscribeTime(String subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Timestamp getCreateime() {
		return createime;
	}
	public void setCreateime(Timestamp createime) {
		this.createime = createime;
	}
	@Override
	public String toString() {
		return "WcahtSubscribeUserInfo [id=" + id + ", userName=" + userName
				+ ", headimgurl=" + headimgurl + ", sex=" + sex
				+ ", subscribe=" + subscribe + ", subscribeTime="
				+ subscribeTime + ", address=" + address + ", openId=" + openId
				+ ", createime=" + createime + "]";
	}

}
