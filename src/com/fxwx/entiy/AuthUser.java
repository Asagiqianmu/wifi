package com.fxwx.entiy;
import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t7_authuser")
public class AuthUser {
	
	@Id
	private int id;// 主键
	
	@Column("username")
	private String userName;//用户名
	
	@Column("nasid")
	private String nasid;//路由标识即归属场所
	
	@Column("mac")
	private String mac;//用户设备mac
	
	@Column("ip")
	private String ip;//ip地址
	
	@Column("authtime")
	private long authTime;//作为认证标识
	
	@Column("authstate")
	private int authState;//认证状态 0为正常认证求情,1为临时放行请求
	
	@Column("sys_type")
	private String sysType;//系统类型
	
	@Column("userip")
	private String userIp;//系统类型
	
	@Column("create_time")
	private Timestamp createTime;//'创建时间', 

	@Column("apmac")
	private String apmac;//ap设备mac

	@Column("ssid")
	private String ssid;//热点信号
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the nasid
	 */
	public String getNasid() {
		return nasid;
	}

	/**
	 * @param nasid the nasid to set
	 */
	public void setNasid(String nasid) {
		this.nasid = nasid;
	}

	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * @return the createTime
	 */
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	

	/**
	 * @return the authTime
	 */
	public long getAuthTime() {
		return authTime;
	}

	/**
	 * @param authTime the authTime to set
	 */
	public void setAuthTime(long authTime) {
		this.authTime = authTime;
	}
	
	
	/**
	 * @return the authState
	 */
	public int getAuthState() {
		return authState;
	}

	/**
	 * @param authState the authState to set
	 */
	public void setAuthState(int authState) {
		this.authState = authState;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getApmac() {
		return apmac;
	}

	public void setApmac(String apmac) {
		this.apmac = apmac;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	@Override
	public String toString() {
		return "AuthUser [id=" + id + ", userName=" + userName + ", nasid=" + nasid + ", mac=" + mac + ", ip=" + ip + ", authTime=" + authTime + ", authState=" + authState + ", sysType=" + sysType
				+ ", userIp=" + userIp + ", createTime=" + createTime + ", apmac=" + apmac + ", ssid=" + ssid + "]";
	}


	
}
