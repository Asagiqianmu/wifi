package com.fxwx.entiy;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 用户信息表
 * @author Administrator
 *
 */
@Table("t7_user_info")
public class UserInfo {

	@Id
	private int id;//主键id
	@Column("user_id")
	private int userId;//关联用户主键ID
	@Column("user_birthday")
	private String userBirthday;//用户生日
	@Column
	private int sex;//用户性别：0---未知  1----男  2----女
	@Column("create_time")
	private Date createTime;//创建时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", userId=" + userId + ", userBirthday="
				+ userBirthday + ", sex=" + sex + ", createTime=" + createTime
				+ "]";
	}
}
