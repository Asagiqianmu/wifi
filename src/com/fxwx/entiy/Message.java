package com.fxwx.entiy;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 用户消息通知表
 * @author Administrator
 *
 */
@Table("t7_message")
public class Message {
    
	@Id
	private int id; //ID
	
	@Column("user_id")
	private int userId; //用户主键ID
	
	@Column("site_id")
	private int siteId; //场所ID
	
	@Column("type")
	private int type; //状态：0：场所； 1：个人用户；
	
	@Column("delete_type")
	private int deleteType; //删除状态：0：删除；1：未删除；
	
	@Column("content")
	private String content; //消息内容
	
	@Column("create_time")
	private Date createTime; //创建时间
	
	private int messageCount; ////返回数量
	
    private  String createTimes; //转换时间
    
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

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(int deleteType) {
		this.deleteType = deleteType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public String getCreateTimes() {
		return createTimes;
	}

	public void setCreateTimes(String createTimes) {
		this.createTimes = createTimes;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", userId=" + userId + ", siteId="
				+ siteId + ", type=" + type + ", deleteType=" + deleteType
				+ ", content=" + content + ", createTime=" + createTime
				+ ", messageCount=" + messageCount + ", createTimes="
				+ createTimes + "]";
	}
}
