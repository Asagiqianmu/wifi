package com.fxwx.dao;

import java.util.List;

import com.fxwx.entiy.Message;

public interface MessageDao {

	/**
	 * 获取消息数量
	 * @param message
	 * @return
	 */
	Message messageCount(Message message);
	/**
	 * 消息删除
	 * @param message
	 * @return
	 */
	int messageDelete(Message message);
	/**
	 * 查询消息的记录
	 * @param message
	 * @return
	 */
	List<Message> getListMessage(Message message);
	
	/**
	 * 添加消息
	 * @param message
	 * @return
	 */
	boolean insertMessage(Message message);
}
