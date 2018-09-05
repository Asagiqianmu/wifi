package com.fxwx.entiy;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * AP WaNMAC 对应LAN MAC
 * @author 飞讯无限智能
 *
 */
@Table("t9_wan_lan_mac")
public class WanLanMac {
	
	@Id
	private int id;// 主键
	
	@Column("lasap_mac")
	private String lasapMac;//wan 口MAC
	
	@Column("wifi_mac")
	private String wifiMac;//lan口MAC

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLasapMac() {
		return lasapMac;
	}

	public void setLasapMac(String lasapMac) {
		this.lasapMac = lasapMac;
	}

	public String getWifiMac() {
		return wifiMac;
	}

	public void setWifiMac(String wifiMac) {
		this.wifiMac = wifiMac;
	}

	@Override
	public String toString() {
		return "WanLanMac [id=" + id + ", lasapMac=" + lasapMac + ", wifiMac=" + wifiMac + "]";
	}
	
}
