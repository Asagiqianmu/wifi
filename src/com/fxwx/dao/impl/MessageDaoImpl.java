package com.fxwx.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.service.IdEntityService;

import com.fxwx.dao.MessageDao;
import com.fxwx.entiy.Message;
import com.mysql.fabric.xmlrpc.base.Array;

@IocBean(name="messageDao",fields={"dao"})
public class MessageDaoImpl extends IdEntityService<Message> implements MessageDao{

	/**
	 * 消息数量
	 */
	@Override
	public Message messageCount(Message message) {
		Sql sql =Sqls.fetchInt("SELECT COUNT(*) AS number FROM $table WHERE user_id = @userId and site_id = @siteId and delete_type = @deleteType");
		sql.vars().set("table","t7_message");
		sql.params().set("userId",message.getUserId()); 
		sql.params().set("siteId",message.getSiteId()); 
		sql.params().set("deleteType",1);
		this.dao().execute(sql); 
		message.setMessageCount(sql.getInt());
		return  message;
	}
	/**
	 * 删除消息
	 */
	@Override
	public int messageDelete(Message message) {
		return this.dao().update(message,"deleteType");
	}
	/**
	 * 获取消息
	 */
	@Override
	public List<Message> getListMessage(Message message) {
		List<Message> getList = new ArrayList<Message>();
		getList = this.dao().query(Message.class, Cnd.where("user_id","=",message.getUserId()).and("site_id","=",message.getSiteId()).and("delete_type","=",message.getDeleteType()));
		return getList;
	}
	/**
	 * 添加消息
	 */
	@Override
	public boolean insertMessage(Message message) {
		message = this.dao().insert(message);
		if(message != null){
			return true;
		}
		return false;
	}
}
