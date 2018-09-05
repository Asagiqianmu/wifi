package com.fxwx.entiy;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 缓存失败日志记录
 * @author Administrator
 *
 */
@Table("t7_cache_fail_log")
public class CacheFailLog {

	@Id
	private int id; //主键ID
	@Column("cache_key")
	private String keyValue; //key值
	@Column
	private String reason; //失败的原因
	@Column("count")
	private int count; //失败次数
	@Column("create_time")
	private Date createTime; //创建时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "CacheFailLog [id=" + id + ", keyValue=" + keyValue
				+ ", reason=" + reason + ", count=" + count + ", createTime="
				+ createTime + "]";
	}
}
