package com.fxwx.entiy;

import java.io.Serializable;
import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_cloud_site")
public class CloudSite implements Serializable{
	
	private static final long serialVersionUID = -8064309263273468172L;
	
	@Id
	private int id;
	
	@Column
	private String site_name;
	
	@Column
	private String address;
	
	@Column
	private int user_id;
	
	@Column
	private int is_probative;
	
	@Column
	private int allow_client_num;
	
	@Column
	private Timestamp create_time;
	
	@Column
	private int state;

	@Column("banner_url")
	private String bannerUrl;
	
	@Column("site_admin")
	private String adminer;
	
	@Column("systemtype")
	private int systemtype;//场所使用系统类型
	
	@Column("status")
	private int stauts;
	
	@Column("authenticationStatus")
	private int authenticationStatus;//场所认证方式：1.短信认证；2.微信认证；3:可选择。默认为1
	
	/*过期时间*/
	private String expireTime;
	
	private String ip;//ip地址
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSite_name() {
		return site_name;
	}

	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getIs_probative() {
		return is_probative;
	}

	public void setIs_probative(int is_probative) {
		this.is_probative = is_probative;
	}

	public int getAllow_client_num() {
		return allow_client_num;
	}

	public void setAllow_client_num(int allow_client_num) {
		this.allow_client_num = allow_client_num;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public int getSystemtype() {
		return systemtype;
	}

	public void setSystemtype(int systemtype) {
		this.systemtype = systemtype;
	}

	public int getStauts() {
		return stauts;
	}

	public void setStauts(int stauts) {
		this.stauts = stauts;
	}
	
	public int getAuthenticationStatus() {
		return authenticationStatus;
	}

	public void setAuthenticationStatus(int authenticationStatus) {
		this.authenticationStatus = authenticationStatus;
	}

	/**
	 * @return the expireTiem
	 */
	public String getExpireTime() {
		return expireTime;
	}

	/**
	 * @param expireTiem the expireTiem to set
	 */
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	
	/**
	 * @return the adminer
	 */
	public String getAdminer() {
		return adminer;
	}

	/**
	 * @param adminer the adminer to set
	 */
	public void setAdminer(String adminer) {
		this.adminer = adminer;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return "CloudSite [id=" + id + ", site_name=" + site_name + ", address=" + address + ", user_id=" + user_id + ", is_probative=" + is_probative + ", allow_client_num=" + allow_client_num
				+ ", create_time=" + create_time + ", state=" + state + ", bannerUrl=" + bannerUrl + ", adminer=" + adminer + ", systemtype=" + systemtype + ", stauts=" + stauts
				+ ", authenticationStatus=" + authenticationStatus + ", expireTime=" + expireTime + ", ip=" + ip + "]";
	}
}
