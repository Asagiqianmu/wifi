package com.fxwx.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.apache.log4j.Logger;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.ViewModel;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.fxwx.bean.SitePriceConfigAll;
import com.fxwx.dao.impl.PersonalDaoImpl;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.CloudUser;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SitePaymentRecord;
import com.fxwx.service.UnitePortalService;
import com.fxwx.service.PersonalService;
import com.fxwx.service.impl.UserPayServiceImpl;
import com.fxwx.util.ExecuteResult;
import com.fxwx.util.ReturnJson;
import com.fxwx.util.unifiedEntranceUtil;

/**
 * Copyright (c) All Rights Reserved, 2017.
 * 版权所有                   dfgs Information Technology Co .,Ltd
 * @Project		UnitePortal---->支付之前的处理
 * @File		PayCommenController.java
 * @Date		2017年1月9日 下午5:27:49
 * @Author		gyj
 */
@IocBean
@At("/priceConfig")
public class PriceConfigController {

	private static Logger logger = Logger.getLogger(PriceConfigController.class);
	
	@Inject
	private PersonalDaoImpl personalDaoImpl;
	
	@Inject
	private UserPayServiceImpl userPayServiceImpl;
	
	@Inject
	private UnitePortalService  unitePortalServiceImpl;
	
	@Inject
	private PersonalService personalServiceImpl; //用户接口
	
	
	/**
	 * @Description  一句话描述此方法的功能
	 * @date 2017年1月9日下午5:35:30
	 * @author guoyingjie
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@At("/toType")
	@Ok("re:jsp:error/error")
	public String toPay(@Param("siteId") int siteId,@Param("userId")int userId,HttpSession session,HttpServletRequest request,ViewModel model ) throws Exception{
		String terminalDevice = request.getHeader("User-Agent");
		String isPc = unifiedEntranceUtil.getPcOrMobile(terminalDevice);//判断设备为手机或pc
		PortalUser user=(PortalUser)session.getAttribute("user");
		CloudSite site=(CloudSite)session.getAttribute("site");
		if(user==null) {
			user=userPayServiceImpl.getUserById(userId);
		}
		if(user==null) return null;
		if(site==null) {
			site=unitePortalServiceImpl.getSiteById(siteId);;
		}
		if(site==null) return null;
		List<Map<String, Object>> lsMeal=userPayServiceImpl.getMealList(siteId,0);
		SitePriceConfigAll spc=userPayServiceImpl.getSitePriceConfigAll(lsMeal,user, site);
		session.setAttribute("site",site);
		session.setAttribute("user",user);
		model.setv("site",site);
		model.setv("user",user);
		model.setv("spc", spc);
		if("1".equals(isPc)){
			return "jsp:pc/buyType";
		}else{
			return "jsp:jsp/personal-type";
		}
	};
	
	/**
	 * 点击获取套餐类型
	 * @param siteId
	 * @param type
	 * @param session
	 */
//	@At("/getMeal")
//	@Ok("raw:json")
//	public String getMeal(int siteId,int type,String userName,HttpSession session){
//		ExecuteResult  result = new ExecuteResult();
//		PortalUser user=(PortalUser)session.getAttribute("user");
//		if(user!=null){
//			if(!user.getUserName().equals(userName)){
//				result.setCode(201);
//				return result.toJsonString();
//			}
//		}
//		if(user==null) {
//			user=personalServiceImpl.getUserByNameAndWord(userName);
//		}
//		if(user==null){
//			result.setCode(202);
//			return result.toJsonString();
//		}
//		CloudSite site=(CloudSite)session.getAttribute("site");
//		if(site==null) {
//			site=unitePortalServiceImpl.getSiteById(siteId);;
//		}
//		if(site==null){
//			result.setCode(203);
//			return result.toJsonString();
//		}
//		List<Map<String, Object>> lsMeal=userPayServiceImpl.getMealList(siteId,type);
//		if(lsMeal==null||lsMeal.size()==0){
//			result.setCode(204);
//			return result.toJsonString();
//		}
//		SitePriceConfigAll spc=userPayServiceImpl.getSitePriceConfigAll(lsMeal,user, site);
//		if(spc==null){
//			result.setCode(205);
//			return result.toJsonString();
//		}
//		result.setData(spc);
//		return result.toJsonString();
//
//	}
	
	/**
	 * @Description 判断场所是否为vip
	 * @date 2017年1月13日上午10:45:34
	 * @author guoyingjie
	 */
	@At("/isVIP")
	@Ok("raw:json")
	public String isVIP(@Param("siteId")int siteId){
		ExecuteResult  result = new ExecuteResult();
		int mangerId = userPayServiceImpl.getCloudSiteById(siteId).getUser_id();
		CloudUser user = userPayServiceImpl.getCloudUserById(mangerId);
		if(user.getGrade()==1){//免费用户,最大区别就是微信支付的时候,vip直接调取app,否则关注微信公众号
			result.setCode(200);
		}else{
			result.setCode(201);
		}
		return result.toJsonString();
		
		
	};
	
	/**
	 * @Description  按时间购买方式查询结果
	 * @date 2017年1月10日上午11:08:13
	 * @author guoyingjie 
	 * 
	 * @param userId
	 * @param siteId
	 */
	@At("/atTimeToPay")
	@Ok("raw:json")
	public String atTimeToPay(@Param("userId")int userId,@Param("siteId") int siteId,HttpServletRequest request){
		ReturnJson r = new ReturnJson();
		try {
			PortalUser user = userPayServiceImpl.getUserById(userId);
			if(null==user){
				r.setCode(201);
				return r.JsonString();
			}
			List<Map> list = userPayServiceImpl.getTimeConfigList(siteId, userId);
			r.setCode(200);
			r.setData(list);
			return r.JsonString();
		} catch (Exception e) {
			r.setCode(201);
			return r.JsonString();
		}
	}
	/**
	 * @Description  按流量购买方式查询结果
	 * @date 2017年1月10日上午11:08:13
	 * @author guoyingjie
	 * @param userId
	 * @param siteId
	 */
	@At("/atFlowToPay")
	@Ok("raw:json")
	public String atFlowToPay(@Param("userId")int userId,@Param("siteId") int siteId,HttpServletRequest request){
		ReturnJson r = new ReturnJson();
		try {
			PortalUser user = userPayServiceImpl.getUserById(userId);
			if(null==user){
				r.setCode(201);
				return r.JsonString();
			}
			List<Map> list = userPayServiceImpl.getFlowConfigList(siteId, userId);
			r.setCode(200);
			r.setData(list);
			return r.JsonString();
		} catch (Exception e) {
			r.setCode(201);
			logger.error(this.getClass().getCanonicalName(),e);
			return r.JsonString();
		}
	}
	/**
	 * 根据orderNum校验支付是否完成
	 * 
	 * @param orderNum
	 * @param session
	 * @return
	 */
	@At("/checkPayResult")
	@Ok("raw:json")
	public String checkPayResult(@Param("orderNum") String orderNum,
			HttpSession session) {
		ExecuteResult rs = new ExecuteResult();
		SitePaymentRecord spr = userPayServiceImpl.getRecordByOrderNum(orderNum);
		if (spr != null && spr.getIsFinish() == 1) {
			rs.setCode(200);
			spr.setIsFinish(-1);
			userPayServiceImpl.updateIsFinish(spr);
		} else {
			rs.setCode(201);
			rs.setMsg("支付未完成或状态未同步。");
		}

		return rs.toJsonString();
	}
	/**
	 * 支付宝jd支付成功回调
	 * @param request
	 * @param session
	 * @return
	 */
	@At("/payResult")
	@Ok("re:jsp:error/error")
	public String payResult(HttpServletRequest request, HttpSession session,ViewModel model){
		String s=request.getParameter("tradeNum");
		Map<String,Object> map1 = new HashMap<String,Object>();
		PortalUser portalUser = (PortalUser) session.getAttribute("user");
		CloudSite site = (CloudSite) session.getAttribute("site");
		if(portalUser != null){
			
		}
		String orderNum = null;
		String token = null;
		String zhiOrderNum = null;
		String tradeNum = null;
		String syas = null;
		try {
			zhiOrderNum = new String(request.getParameter("out_trade_no"));
			tradeNum = new String(request.getParameter("trade_no"));
		} catch (Exception e) {
			logger.info("=====payResult=====支付宝支付同步通知");
		}
		String terminalDevice = request.getHeader("User-Agent");
		String isPc = unifiedEntranceUtil.getPcOrMobile(terminalDevice);
		// 说明是支付包的支付
		if (zhiOrderNum != null && tradeNum != null) {
			if(portalUser==null){
				portalUser = userPayServiceImpl.findPortalUserByOrderNum(zhiOrderNum);
			}
			if (null == portalUser) {
				if ("1".equals(isPc)) {
					return "jsp:error/error";
				} else {
					return "jsp:error/error";
				}
			}
			if(site==null){
				site = userPayServiceImpl.findCloudSiteByOrderNum(zhiOrderNum);
			}
			boolean is = userPayServiceImpl.getSitePaymentRecordByTradeNum(
					zhiOrderNum, tradeNum, site.getId(), portalUser.getId());
			if (is) {
				map1.put("user", portalUser);
				map1.put("site", site);
				model.setv("map", map1);
				if("1".equals(isPc)){
					return "jsp:pc/personal";
				}else{
					return "jsp:jsp/personal";
				}
			} else {
				if ("1".equals(isPc)) {
					return "jsp:pc/payFail";
				} else {
					return "jsp:jsp/payFail";
				}
			}
		} else {
			// 说明是银行卡
			if(portalUser==null){
				portalUser = userPayServiceImpl.findPortalUserByOrderNum(orderNum);
			}
			if (null == portalUser) {
				if ("1".equals(isPc)) {
					return "jsp:error/error";
				} else {
					return "jsp:error/error";
				}
			}
			if(site==null){
				site = userPayServiceImpl.findCloudSiteByOrderNum(orderNum);
			}
			boolean isok = userPayServiceImpl.checkPayResult(orderNum);
			if (isok) {
				map1.put("user", portalUser);
				map1.put("site", site);
				model.setv("map", map1);
				if("1".equals(isPc)){
					return "jsp:pc/personal";
				}else{
					return "jsp:jsp/personal";
				}
			} else {
				if ("1".equals(isPc)) {
					return "jsp:pc/payFail";
				} else {
					return "jsp:jsp/payFail";
				}
			}
		}
	}
	/**
	 * 微信支付回调
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@At("/wechatResult")
	@Ok("re:jsp:error/error")
	public String wechatResult(HttpServletRequest request,HttpSession session,ViewModel model){
		Map<String,Object> map1 = new HashMap<String,Object>();
		PortalUser portalUser = (PortalUser) session.getAttribute("user");
		CloudSite site = (CloudSite) session.getAttribute("site");
		String orderNum = request.getParameter("out_trade_no");
		String terminalDevice = request.getHeader("User-Agent");
		String isPc = unifiedEntranceUtil.getPcOrMobile(terminalDevice);
		if(portalUser==null){
			portalUser = userPayServiceImpl.findPortalUserByOrderNum(orderNum);
		}
		if(site==null){
			site = userPayServiceImpl.findCloudSiteByOrderNum(orderNum);
		}
		if (null == portalUser) {
			if ("1".equals(isPc)) {
				return "jsp:error/error";
			} else {
				return "jsp:error/error";
			}
		}
		boolean isok = userPayServiceImpl.checkPayResult(orderNum);
		if (isok) {
			map1.put("user", portalUser);
			map1.put("site", site);
			model.setv("map", map1);
			if("1".equals(isPc)){
				return "jsp:pc/personal";
			}else{
				return "jsp:jsp/personal";
			}
		} else {
			if ("1".equals(isPc)) {
				return "jsp:jsp/orderfail";
			} else {
				return "jsp:jsp/orderfail";
			}
		}
	}
}
