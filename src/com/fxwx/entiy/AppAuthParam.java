package com.fxwx.entiy;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t8_app_auth_param")
public class AppAuthParam {
	
	@Id
	private int id;// 主键
	
	@Column("mac")
	private String ip;//app名称
	
	@Column("extend")
	private String extend;//参数集合 map转json

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	@Override
	public String toString() {
		return "AppAuthParam [id=" + id + ", ip=" + ip + ", extend=" + extend + "]";
	}
}
