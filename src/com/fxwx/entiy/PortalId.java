package com.fxwx.entiy;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t7_portalid_type")
public class PortalId {
	//主键，
	@Id
	private int id;
	@Column("ssid")
	private String ssid;
	@Column("nasid")
	private String nasid;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getNasid() {
		return nasid;
	}

	public void setNasid(String nasid) {
		this.nasid = nasid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((nasid == null) ? 0 : nasid.hashCode());
		result = prime * result + ((ssid == null) ? 0 : ssid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PortalId other = (PortalId) obj;
		if (id != other.id)
			return false;
		if (nasid == null) {
			if (other.nasid != null)
				return false;
		} else if (!nasid.equals(other.nasid))
			return false;
		if (ssid == null) {
			if (other.ssid != null)
				return false;
		} else if (!ssid.equals(other.ssid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PortalIP [id=" + id + ", ssid=" + ssid + ", nasid=" + nasid + "]";
	}
	
}
