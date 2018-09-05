package com.fxwx.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.fxwx.dao.MessageDao;
import com.fxwx.entiy.Message;
import com.fxwx.service.MessageService;
@IocBean
public class MessageServiceImpl implements MessageService {

	@Inject
	private MessageDao messageDaoImpl;

	/**
	 * 获取消息数量
	 */
	@Override
	public Message messageCount(Message message) {
		message = messageDaoImpl.messageCount(message);
		if(message != null){
			return message;
		}
		return null;
	}

	/**
	 * 删除消息
	 */
	@Override
	public int messageDelete(Message message) {
		return messageDaoImpl.messageDelete(message);
	}

	/**
	 * 查询消息
	 */
	@Override
	public List<Message> getListMessage(Message message) {
		List<Message> listMessage = new ArrayList<Message>();
		listMessage = messageDaoImpl.getListMessage(message);
		if(listMessage.size() != 0){
			return listMessage;
		}
		return null;
	}
    /**
     * 添加消息
     */
	@Override
	public boolean insertMessage(Message message) {
		return messageDaoImpl.insertMessage(message);
	}

}
