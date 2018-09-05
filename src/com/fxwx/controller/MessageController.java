package com.fxwx.controller;

import java.util.List;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.JspView;
import org.nutz.mvc.view.ViewWrapper;

import com.fxwx.entiy.Message;
import com.fxwx.entiy.PortalUser;
import com.fxwx.service.UnitePortalService;
import com.fxwx.service.MessageService;
import com.fxwx.util.DateUtil;

/**
 * 消息通知
 * @author Administrator
 *
 */
@IocBean
@At("/message")
public class MessageController extends BaseController{

	@Inject
	private UnitePortalService unitePortalServiceImpl; //统一Portal接口
	@Inject
	private MessageService messageServiceImpl; //消息
	/**
	 * 消息数量
	 * 
	 * @return
	 */
	@At("/messageCount")
	@Ok("raw:json")
	public String messageCount(@Param("userName") String userName,@Param("siteId") int siteId){
		//首先根据账号查询当前用户ID；
		if(userName != "" && !"".equals(userName)){
			PortalUser pu = new PortalUser();
			pu.setUserName(userName);
			pu = unitePortalServiceImpl.getUserName(pu);
			if(pu != null){
				//根据当前用户ID，场所ID查询当前用户消息条数
				Message message = new Message();
				message.setUserId(pu.getId());
				message.setSiteId(siteId);
				message = messageServiceImpl.messageCount(message);
				int mcount = message.getMessageCount();
				if(mcount != 0){
					map1.put("mcount", mcount);
					map1.put("result", "1");
				}else{
					map1.put("mcount", 0);
					map1.put("result", "0");
				}
			}
		}else{
			map1.put("msg", "参数不全");
		}
		return Json.toJson(map1);
	}

	/**
	 * 消息通知
	 * 查询成功：1
	 * @return
	 */
	@At("/messageNotification")
	public ViewWrapper  messageNotification(@Param("userName") String userName,@Param("siteId") int siteId){
		//判断不能为NULL
		if(userName != null && !"".equals(userName) && siteId != 0){
			PortalUser pu = new PortalUser();
			pu.setUserName(userName);
			pu = unitePortalServiceImpl.getUserName(pu);
			if(pu != null){
				Message message = new Message();
				message.setSiteId(siteId);
				message.setUserId(pu.getId());
				message.setDeleteType(1);
				List <Message> listMessage = messageServiceImpl.getListMessage(message);
				if(listMessage != null && listMessage.size() != 0){
					for (int i = 0; i < listMessage.size(); i++) {//时间转换处理
						message = listMessage.get(i);
						String createDate = DateUtil.dateToStr(message.getCreateTime());
						message.setCreateTimes(createDate);
					}
				  map1.put("listMessage", listMessage);
                  map1.put("result", "1");
				}else{
				 map1.put("listMessage", listMessage);
                 map1.put("result", "0");
				}
			}else{
				map1.put("result", "0");
			}
		}else{
			map1.put("result", "0");
			map1.put("msg", "参数不全");
		}
		return new ViewWrapper(new JspView("mobile/system-message"),map1);
	}
	
	/**
	 * 消息通知
	 * 查询成功：1
	 * @return
	 */
	@At("/messageNotificationPc")
	@Ok("raw:json")
	public String  messageNotificationPc(@Param("userName") String userName,@Param("siteId") int siteId){
		//判断不能为NULL
		if(userName != null && !"".equals(userName) && siteId != 0){
			PortalUser pu = new PortalUser();
			pu.setUserName(userName);
			pu = unitePortalServiceImpl.getUserName(pu);
			if(pu != null){
				Message message = new Message();
				message.setSiteId(siteId);
				message.setUserId(pu.getId());
				message.setDeleteType(1);
				List <Message> listMessage = messageServiceImpl.getListMessage(message);
				if(listMessage != null && listMessage.size() != 0){
					for (int i = 0; i < listMessage.size(); i++) {//时间转换处理
						message = listMessage.get(i);
						String createDate = DateUtil.dateToStr(message.getCreateTime());
						message.setCreateTimes(createDate);
					}
				  map1.put("listMessage", listMessage);
                  map1.put("result", "1");
				}else{
				 map1.put("listMessage", listMessage);
                 map1.put("result", "0");
				}
			}else{
				map1.put("result", "0");
			}
		}else{
			map1.put("result", "0");
			map1.put("msg", "参数不全");
		}
		return Json.toJson(map1);
	}
	
	/**
	 * 删除消息
	 * 删除成功：1
	 * @return
	 */
	@At("/deleteMessage")
	@Ok("raw:json")
	public String deleteMessage(@Param("id") int id){
		//首先根据用户ID
		if(id != 0){
			Message message = new Message();
			message.setId(id);
			message.setDeleteType(0);
			int result = messageServiceImpl.messageDelete(message);
			if(result != 0){//删除成功
				map1.put("result", "1");
			}else{
				map1.put("result", "0");
			}
		}else{
			map1.put("msg", "参数不足");
		}
		return Json.toJson(map1);
	}
}
