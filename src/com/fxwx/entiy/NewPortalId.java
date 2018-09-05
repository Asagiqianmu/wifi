package com.fxwx.entiy;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
@Table("t7_newPortalid")
public class NewPortalId {
		//主键，
		@Id
		private int id;
		@Column("ssid")
		private String ssid;
		//用户类型，1.走新版portal认证页面。2走老版portal认证页面
		@Column("user_type")
		private int userType;
		@Column("nasid")
		private String nasid;
		public NewPortalId() {
			super();
		}
		public NewPortalId(int id, String ssid, int userType, String nasid) {
			super();
			this.id = id;
			this.ssid = ssid;
			this.userType = userType;
			this.nasid = nasid;
		}
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
		public int getUserType() {
			return userType;
		}
		public void setUserType(int userType) {
			this.userType = userType;
		}
		public String getNasid() {
			return nasid;
		}
		public void setNasid(String nasid) {
			this.nasid = nasid;
		}
		@Override
		public String toString() {
			return "NewPortalId [id=" + id + ", ssid=" + ssid + ", userType=" + userType + ", nasid=" + nasid + "]";
		}
}
