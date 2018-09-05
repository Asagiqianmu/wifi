package com.fxwx.entiy;

import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 微信认证临时放行时跳转参数
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2018年4月10日 下午4:29:16
 */
@Table("t8_temporary_params")
public class TemporaryParams {

	@Id
	private int id;//主键id
	@Column("key_val")
	private String key_val;//键值
	@Column("content")
	private String content;//内容
	@Column("createtime")
	private Timestamp createTime;//创建时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKey_val() {
		return key_val;
	}
	public void setKey_val(String key_val) {
		this.key_val = key_val;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "TemporaryParams [id=" + id + ", key_val=" + key_val + ", content=" + content + ", createTime=" + createTime + "]";
	}
	
}
