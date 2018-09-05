package com.fxwx.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONSerializer;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.ViewModel;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.fxwx.bean.RecordOrderBean;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.PortalUser;
import com.fxwx.service.UnitePortalService;
import com.fxwx.service.RecordService;
import com.fxwx.service.impl.UserPayServiceImpl;
import com.fxwx.util.ExecuteResult;
import com.fxwx.util.unifiedEntranceUtil;

@IocBean
@At("/record")
public class RecordController {

	@Inject
	private RecordService recordService;
	
	@Inject
	private UserPayServiceImpl userPayServiceImpl;
	
	@Inject
	private UnitePortalService  unitePortalServiceImpl;
	
	private static int pageSize=3;
	
	/**
	 * 进入缴费记录
	 * @param siteId
	 * @param userId
	 * @param session
	 * @param model
	 * @param request
	 * @return
	 */
	@At("/recordPage")
	@Ok("re:jsp:error/error")
    public String recordPage(int siteId,int userId,HttpSession session,ViewModel model,HttpServletRequest request ){
//    	String terminalDevice = request.getHeader("User-Agent");
//		String isPc = unifiedEntranceUtil.getPcOrMobile(terminalDevice);//判断设备为手机或pc
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
		session.setAttribute("site",site);
		session.setAttribute("user",user);
		model.setv("site",site);
		model.setv("user",user);
		return "jsp:jsp/prepaid";
	}
	
//	/**
//	 * 进入缴费记录PC
//	 * @param siteId
//	 * @param userId
//	 * @param session
//	 * @param model
//	 * @param request
//	 * @return
//	 */
//	@At("/recordPage")
//	@Ok("re:jsp:error/error")
//    public String recordPagePC(int siteId,int userId,HttpSession session,ViewModel model,HttpServletRequest request ){
//		PortalUser user=(PortalUser)session.getAttribute("user");
//		CloudSite site=(CloudSite)session.getAttribute("site");
//		if(user==null) {
//			user=userPayServiceImpl.getUserById(userId);
//		}
//		if(user==null) return null;
//		if(site==null) {
//			site=unitePortalServiceImpl.getSiteById(siteId);
//		}
//		if(site==null) return null;
//		session.setAttribute("site",site);
//		session.setAttribute("user",user);
//		model.setv("site",site);
//		model.setv("user",user);
//		result.setData(map);
//		return  model;
//	}
//	
	/**
	 * 获得用户在该场所下的所有订单结果
	 * @param userId
	 * @param siteId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@At("/allOrderRecards")
	@Ok("raw:json")
	public  String allOrderRecards(@Param("userId")int userId,@Param("siteId")int siteId,@Param("currentPage")int currentPage,HttpSession session){
		ExecuteResult result  = new ExecuteResult();
		CloudSite site=(CloudSite)session.getAttribute("site");
		if(site==null) {
			site=unitePortalServiceImpl.getSiteById(siteId);;
		}
		if(site==null){
			result.setCode(201);
			return result.toJsonString();
		}
		List<RecordOrderBean> list = null;
		Map<String, Object> map=null;
		try {
			map = recordService.allOrderRecards(userId, siteId, currentPage, pageSize,site.getSite_name());
			result.setCode(200);
			result.setData(map);
			return result.toJsonString();
		} catch (Exception e) {
			result.setCode(201);
			return result.toJsonString();
		}
	}
	
/**
 *  获得用户在该场所下的待支付订单
 * @param userId
 * @param siteId
 * @param currentPage
 * @param pageSize
 * @return
 */
	@At("/noPaymentState")
	@Ok("raw:json")
	public  String noPaymentState(@Param("userId")int userId,@Param("siteId")int siteId,@Param("currentPage")int currentPage,HttpSession session){
		ExecuteResult result  = new ExecuteResult();
		CloudSite site=(CloudSite)session.getAttribute("site");
		if(site==null) {
			site=unitePortalServiceImpl.getSiteById(siteId);
		}
		if(site==null){
			result.setCode(201);
			return result.toJsonString();
		}
		List<RecordOrderBean> list = null;
		Map<String, Object> map=null;
		try {
			map = recordService.noPaymentState(userId, siteId, currentPage, pageSize,site.getSite_name());
			result.setCode(200);
			result.setData(map);;
			return result.toJsonString();
		} catch (Exception e) {
			result.setCode(201);
			return result.toJsonString();
		}
	}
	
	/**
	 * 获得用户在该场所下的已完成订单
	 * @param userId
	 * @param siteId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@At("/finishOrder")
	@Ok("raw:json")
	public  String finishOrder(@Param("userId")int userId,@Param("siteId")int siteId,@Param("currentPage")int currentPage,HttpSession session){
		ExecuteResult result  = new ExecuteResult();
		CloudSite site=(CloudSite)session.getAttribute("site");
		if(site==null) {
			site=unitePortalServiceImpl.getSiteById(siteId);;
		}
		if(site==null){
			result.setCode(201);
			return result.toJsonString();
		}
		List<RecordOrderBean> list = null;
		Map<String, Object> map=null;
		try {
			map = recordService.finishOrder(userId, siteId, currentPage, pageSize,site.getSite_name());
			result.setCode(200);
			result.setData(map);;
			return result.toJsonString();
		} catch (Exception e) {
			result.setCode(201);
			return result.toJsonString();
		}
	}
	
	/**
	 * 获得用户在该场所下的已失效订单
	 * @param userId
	 * @param siteId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@At("/disabledOrder")
	@Ok("raw:json")
	public  String disabledOrder(@Param("userId")int userId,@Param("siteId")int siteId,@Param("currentPage")int currentPage,HttpSession session){
		ExecuteResult result  = new ExecuteResult();
		CloudSite site=(CloudSite)session.getAttribute("site");
		if(site==null) {
			site=unitePortalServiceImpl.getSiteById(siteId);
		}
		if(site==null){
			result.setCode(201);
			return result.toJsonString();
		}
		session.getAttribute("site");
		List<RecordOrderBean> list = null;
		Map<String, Object> map=null;
		try {
			map = recordService.disabledOrder(userId, siteId, currentPage, pageSize,site.getSite_name());
			result.setCode(200);
			result.setData(map);
			return result.toJsonString();
		} catch (Exception e) {
			result.setCode(201);
			return result.toJsonString();
		}
	}
	/**
	 * 用户待支付订单点击支付
	 * @param request
	 * @param session
	 * @return
	 */
	@At("/toPay")
	@Ok("re:jsp:error/error")
	public String toPay(HttpServletRequest request,HttpSession session,ViewModel model){
		String terminalDevice = request.getHeader("User-Agent");
		String isPc = unifiedEntranceUtil.getPcOrMobile(terminalDevice);//判断设备为手机或pc
		String params= "";
		Map<String,Object> json = null; 
		if("1".equals(isPc)){//pc"[] {}"
			 params=request.getParameter("maps");
			 String nn = params.replace("[","{").replaceAll("]","}");
			 json = (Map<String,Object>) JSONSerializer.toJSON(nn);
		}else{
			 params=request.getParameter("maps");
			 String nns = params.replace("[","{").replaceAll("]","}");
			 json = (Map<String,Object>) JSONSerializer.toJSON(nns);
		}
		PortalUser user=(PortalUser)session.getAttribute("user");
		CloudSite site=(CloudSite)session.getAttribute("site");
		if(user==null) {
			user=userPayServiceImpl.getUserById(Integer.parseInt(json.get("userId")+""));
		}
		if(user==null) return null;
		if(site==null) {
			site=unitePortalServiceImpl.getSiteById(Integer.parseInt(json.get("siteId")+""));;
		}
		if(site==null) return null;
		session.setAttribute("site",site);
		session.setAttribute("user",user);
		model.setv("site",site);
		model.setv("user",user);
		model.setv("map",json);
		if("1".equals(isPc)){
			return "jsp:pc/personal";
		}else{
			return "jsp:jsp/prepaids";
		}
	}
}
