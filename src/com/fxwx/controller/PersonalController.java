package com.fxwx.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.ViewModel;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.JspView;
import org.nutz.mvc.view.ViewWrapper;

import com.fxwx.entiy.AuthUser;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.GiftLength;
import com.fxwx.entiy.IdEncapsulation;
import com.fxwx.entiy.Message;
import com.fxwx.entiy.PassUrl;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.service.UnitePortalService;
import com.fxwx.service.MessageService;
import com.fxwx.service.PersonalService;
import com.fxwx.util.DateUtil;
import com.fxwx.util.OssSchoolManage;
import com.fxwx.util.StringUtil;
import com.fxwx.util.unifiedEntranceUtil;


@IocBean
@At("/personal")
public class PersonalController extends BaseController {

	@Inject
	private PersonalService personalServiceImpl; // 用户接口

	private static Logger logger = Logger.getLogger(PersonalController.class);
	@Inject
	private UnitePortalService unitePortalServiceImpl;

	@Inject
	private MessageService messageServiceImpl; // 消息

	/**
	 * 完善个人信息
	 * 
	 * @throws ParseException
	 */
	@At("/perfectInformation")
	@Ok("raw:json")
	public String perfectInformation(@Param("sex") String sex,@Param("siteId") int siteId,@Param("userName") String userName,@Param("birthdate") String birthdate,@Param("user_nickname") String user_nickname,@Param("image_url") String image_url,HttpSession session) throws ParseException{
		int state = 0;
		// 根据手机号判断该用户是否存在
		PortalUser portalUser = (PortalUser) session.getAttribute("user");// session中查询
		if (portalUser == null)
			portalUser = personalServiceImpl.getUserByNameAndWord(userName);// session没有，数据库查询
		if (portalUser == null) {
			map1.put("result", false);
			map1.put("message", "该用户不存在");
		}
		if (sex == null || birthdate == null || user_nickname == null) {
			map1.put("result", false);
			map1.put("message", "信息不能为空");
		}
		if (portalUser.getBirthdate() != null
				&& !"".equals(portalUser.getBirthdate())) {
			if (!portalUser.getBirthdate().equals(birthdate)) {// 判断生日是否相等
				map1.put("result", false);
				map1.put("message", "前后台数据不符合");
			}
		}
		if (portalUser.getSex() != -1) {
			if (portalUser.getSex() != Integer.valueOf(sex)) {
				map1.put("result", false);
				map1.put("message", "前后台数据不符合");
			}
		}
		// 如果用户生日为空，并且此次完善了生日则送时常
		if (portalUser.getBirthdate() == null || portalUser.getBirthdate().equals("")) {
			state = 1;
		}
		portalUser.setSex(Integer.valueOf(sex));
		portalUser.setUserNickname(user_nickname);
		portalUser.setBirthdate(birthdate);
		boolean resu = false;
		if (image_url != null && !image_url.startsWith("http://oss.kdfwifi.net/user_pic/") && !"".endsWith(image_url)) {
			// 上传图片
			InputStream baseInputOne = StringUtil.getInputStream(image_url);
			OssSchoolManage oss = new OssSchoolManage();
			String names = userName + ".jpg";// 图片名称
			try {
				String isOk = oss.uploadFile(baseInputOne, "user_pic/" + names,
						"image/jpeg");// 上传图片
				if (isOk != null) {// 上传成功
					// 更新个人信息
					portalUser.setImageUrl(names);
					if (state == 1) {
						GiftLength cl = unitePortalServiceImpl.getGiftLength(siteId);
						if (cl != null && cl.getGiftLength() != 0) {
							resu = unitePortalServiceImpl.giveGift(portalUser,
									siteId, cl, 1);
						}
					} else {
						resu = personalServiceImpl
								.perfectInformation(portalUser);// 个人信息的更新
					}
				} else {
					map1.put("result", false);// 个人信息更新失败
					map1.put("message", "上传图片上失败");
				}
			} catch (Exception e) {
				logger.error("上传图片失败", e);
			}
		} else {
			if (state == 1) {// 该用户存在并且性别，生日，昵称没有完善时
				GiftLength cl = unitePortalServiceImpl.getGiftLength(siteId);
				if (cl != null && cl.getGiftLength() != 0) {
					unitePortalServiceImpl.giveGift(portalUser, siteId, cl, 1);
				}else{
					state = 0;
					resu = personalServiceImpl.perfectInformation(portalUser);
				}
			} else {
				resu = personalServiceImpl.perfectInformation(portalUser);// 个人信息的更新
			}
		}
		// 如果个人信息更新成功，根据用户所在场所，查询送的时长

		if(state==1){
			Message message = new Message();
			message.setContent("完善个人资料特赠送时长");
			message.setCreateTime(new Date());
			message.setDeleteType(1);
			message.setSiteId(siteId);
			message.setType(1);
			message.setUserId(portalUser.getId());
			messageServiceImpl.insertMessage(message);
		}
		map1.put("result", true);// 个人信息更新成功
		map1.put("message", "个人信息更新成功");
		session.setAttribute("user", portalUser);
		return Json.toJson(map1);
	}

	/**
	 * 充值记录
	 */
	public String rechargeRecord(@Param("userId") String userId) {
		// 根据用户Id查询充值记录

		return "aa";
	}

	/**
	 * 跳入editprofile(完善个人信息界面)界面
	 */
	@At("/editprofile")
	public ViewWrapper editprofile(@Param("userName") String userName,
			int siteId, HttpSession session) {
		PortalUser user = (PortalUser) session.getAttribute("user");
		CloudSite site = (CloudSite) session.getAttribute("site");
		if (user == null) {
			user = personalServiceImpl.getUserByNameAndWord(userName);
			session.setAttribute("user", user);
		}
		if (site == null) {
			site = unitePortalServiceImpl.getSiteById(siteId);
			session.setAttribute("user", user);
		}
		map1.put("user", user);
		map1.put("site", site);
		return new ViewWrapper(new JspView("mobile/editprofile"), map1);
	}

	/**
	 * 跳入person界面(完善个人信息后跳入person.jsp)
	 */
	@At("/editAfter")
	@Ok("re:jsp:error/error")
	public String editAfter(@Param("userName") String userName, int siteId,
			HttpSession session, ViewModel model, HttpServletRequest request) {
		String terminalDevice = request.getHeader("User-Agent");
		String isPc = unifiedEntranceUtil.getPcOrMobile(terminalDevice);// 判断设备为手机或pc
		// 删除缓存的用户信息
		// boolean flag=MemcachedUtils.deletes(userName);
		PortalUser user = (PortalUser) session.getAttribute("user");
		CloudSite site = (CloudSite) session.getAttribute("site");
		if (user == null) {
			user = personalServiceImpl.getUserByNameAndWord(userName);
			session.setAttribute("user", user);
		}
		if (site == null) {
			site = unitePortalServiceImpl.getSiteById(siteId);
			session.setAttribute("user", user);
		}
		map1.put("user", user);
		map1.put("site", site);
		//map1.put("code", 202);
		model.setv("map", map1);
		//		return "jsp:jsp/personal";
		if("1".equals(isPc)){
			return "jsp:pc/personal";
		}else{
			return "jsp:jsp/personal";
		}
	}

	/*
	 * 进度条显示流量比例、天数比例
	 * 
	 * @param userName
	 * 
	 * @param nasid
	 * 
	 * @return
	 * 
	 * @throws ParseException
	 */
	@At("/getResidualTime")
	@Ok("raw:json")
	public String getResidualTime(@Param("userName") String userName,
			@Param("nasid") String nasid, @Param("siteId") int siteId)
					throws ParseException {
		if (userName != null && userName.equals(userName)) {
			// 根据用户名手机号查询当前用户的ID
			PortalUser portalUser = new PortalUser();
			portalUser.setUserName(userName);
			portalUser = unitePortalServiceImpl.getUserName(portalUser);
			if (portalUser != null) {
				// 根据用户ID，场所ID查询当前用户上网费用情况
				IdEncapsulation idEncapsulation = new IdEncapsulation();
				idEncapsulation.setUserId(portalUser.getId());
				idEncapsulation.setSiteId(siteId);
				SiteCustomerInfo sc = new SiteCustomerInfo();
				sc = personalServiceImpl.getLongInternettime(idEncapsulation);
				String flow = "";// 流量值
				String dayTime = "";// 时间值
				NumberFormat nf = new DecimalFormat("0.0");
				if (sc != null) {// 查询当前用户购买时长的总时间//获取当前用户购买流量

					// 处理流量显示（用户总流量数）
					double zkb = Double.parseDouble(sc.getTotalFlow() == null ? "0" : sc.getTotalFlow());
					// 已使用流量数
					double sflow = Double.parseDouble(sc.getUsedFlow() == null ? "0" : sc.getUsedFlow());
					// 剩余流量
					double syflow = zkb - sflow;
					if (syflow <= 0.0) {
						syflow = 0;
						flow = "0M";
					} else {
						if (zkb != 0.0) {
							double mb = zkb / 1024;
							flow = nf.format(mb) + "M";
							if (mb >= 1024) {
								double g = mb / 1024;
								flow = nf.format(g) + "G";
							}
						} else {
							flow = "0M";
						}
						
					}
					if(syflow == 0  ){
						map1.put("flow", "0M");// 总流量显示
						map1.put("totalFlow", 0);// 总流量
						map1.put("syflow", 0);// 剩余总流量
					}else{
						map1.put("flow", flow);// 总流量显示
						map1.put("totalFlow", sc.getTotalFlow());// 总流量
						map1.put("syflow", syflow);// 剩余总流量
					}
					if(sc.getAllDay() != null){//新用户显示时长处理方式
						// 处理买的天数
						String day = sc.getAllDay();// 购买的总时间
						String[] days = day.split(",");
						if (days[0].equals("0") && days[1].equals("0") && days[2].equals("0")) {
							dayTime = "0分";
						} else {
							if (days[0].equals("0") && days[0] == "0") {
								dayTime = days[1] + "时" + days[2] + "分";
								if (days[1].equals("0") && days[1] == "0") {
									dayTime = days[2] + "分";
									if (days[1].equals("0") && days[1] == "0") {
										dayTime = "0分";
									}
								}
							} else {
								dayTime = days[0] + "天" + days[1] + "时" + days[2]+ "分";
							}
						}
						// 处理时间小时的显示
						int day0 = Integer.parseInt(days[0]);// 购买的总天数
						int time0 = Integer.parseInt(days[1]);// 购买小时
						int minte = Integer.parseInt(days[2]);// 购买分钟
						int zDayHour = day0*24*60+time0*60+minte;
//						map1.put("sc", sc.getTotalFlow());//总流量
						// 总小时减去已使用小时的等于剩余的小时//到期时间-当前时间
						String currentTime = DateUtil.getStringDate();// 当前时间
						String expirationTime = sc.getExpirationTime() == null ? currentTime:DateUtil.dateToStr(sc.getExpirationTime());// 结束时间
						// 结束时间-当前时间=未使用的
						long ec = DateUtil.subtractTime(expirationTime, currentTime);
						// 处理未使用时间为小时
						long unusedTime = DateUtil.dayHour(ec);
						if (ec > 0 ) {
							map1.put("dayTime", dayTime);// 显示总天数
							map1.put("zDayHour", zDayHour);// 购买总小时
							map1.put("unusedTime", unusedTime);
						} else {
							map1.put("dayTime", "0分");// 显示总天数
							map1.put("unusedTime", 0);
							map1.put("zDayHour", 0);// 购买总小时
						}
					}else{//老用户时长显示
						//首先显示老用户剩余的总时长——*天/*时/*分
						String currentTime = DateUtil.getStringDate();// 当前时间
						String expirationTime = sc.getExpirationTime() == null ? currentTime:DateUtil.dateToStr(sc.getExpirationTime());// 结束时间
						// 结束时间-当前时间=剩余的时长
						long ec = DateUtil.subtractTime(expirationTime, currentTime);//换算是天时分
						//时间戳换算成天：时：分
						dayTime = DateUtil.dateDH(ec);
						// 处理未使用时间为小时
						long unusedTime = DateUtil.dayHour(ec);
						//最后一次充值时间
						String updateTime =  sc.getUpdateTime() == null ? currentTime:DateUtil.dateToStr(sc.getUpdateTime());//老用户最后一次充值时间
						//总时长=到期时间-最后一次充值时间
						long eu = DateUtil.subtractTime(expirationTime, updateTime);
						//总时长换算成小时
						long zDayHour = DateUtil.dayHour(eu);
						if(eu > 0 && ec > 0){
							map1.put("dayTime", dayTime);//显示剩余使用时间
							map1.put("unusedTime", unusedTime);//未使用时长小时
							map1.put("zDayHour", zDayHour);// 购买总小时
						}else{
							map1.put("dayTime", "0分");//显示剩余使用时间
							map1.put("unusedTime", 0);
							map1.put("zDayHour", 0);// 购买总小时
						}
					}
				} else {
					map1.put("unusedTime", 0);
					map1.put("zDayHour", 0);// 购买总小时
					map1.put("dayTime", "0分");
					map1.put("flow", "0M");// 总流量显示
					map1.put("totalFlow", 0);// 总流量
					map1.put("syflow", 0);// 剩余总流量
				}
			}
		} else {
			map1.put("msg", "参数不足");
		}
		return Json.toJson(map1);
	}

	/**
	 * 个人中心下线管理 根据用户查询场所时候存在
	 */
	@At("/offLine")
	@Ok("raw:json")
	public String offLine(String sys,HttpServletRequest request, HttpSession session) {
		PortalUser user=(PortalUser)session.getAttribute("user");
		AuthUser authUser= unitePortalServiceImpl.selAccount(user.getUserName());
		// 根据场所的外键查询当场所对应的设备；设备类型是什么
		if(authUser.getSysType().equals("h3c")){
			Map<String,Object> json =new HashMap<String,Object>();
			json.put("userName", user.getUserName());
			json.put("passWord", user.getPassWord().substring(0, 16));
			json.put("action", "PORTAL_LOGINOUT");
			json.put("userip", authUser.getUserIp());
			json.put("basip", authUser.getIp());
			json.put("basname", authUser.getNasid());
			boolean ress=unitePortalServiceImpl.passH3c(json);	
			if(ress){
				map1.put("purl", "http://www.qq.com");	
				map1.put("result", "1");
			}else{
				map1.put("result", "0");
			}
			return Json.toJson(map1);
		}else{
			PassUrl purl = personalServiceImpl.getPassUrl(authUser.getSysType());
			if (purl != null) {
				if(purl.getSolraysType().equals("chilli")){
					String ip = authUser.getIp();
					if(ip != null){
						purl.setOfflineUrl("http://"+ip+":3990/"+purl.getOfflineUrl());
					}else{
						purl.setOfflineUrl("http://"+purl.getOfflineUrl());
					}
				}
				map1.put("purl", purl);
				map1.put("result", "1");
			} else {
				map1.put("result", "0");
			}
		}
		return Json.toJson(map1);
	}

	/**
	 * 账号管理页面
	 * 
	 * @param userName
	 * @param siteId
	 * @return
	 */
	@At("/toAccount")
	public ViewWrapper toAccount(String userName, int siteId,
			HttpSession session, HttpServletRequest request) {
		String terminalDevice = request.getHeader("User-Agent");
		String isPc = unifiedEntranceUtil.getPcOrMobile(terminalDevice);// 判断设备为手机或pc
		PortalUser user = (PortalUser) session.getAttribute("user");
		CloudSite site = (CloudSite) session.getAttribute("site");
		if (user == null) {
			user = personalServiceImpl.getUserByNameAndWord(userName);
			session.setAttribute("user", user);
		}
		if (site == null) {
			site = unitePortalServiceImpl.getSiteById(siteId);
			session.setAttribute("user", user);
		}
		map1.put("user", user);
		map1.put("site", site);
		if ("1".equals(isPc)) {
			return new ViewWrapper(new JspView("pc/son"), map1);
		} else {
			return new ViewWrapper(new JspView("mobile/son"), map1);
		}

	}

	/**
	 * 创建子账号
	 * 
	 * @param userName
	 * @param siteId
	 * @param passWord
	 * @param sonName
	 * @return
	 */
	@At("/createSonAccount")
	@Ok("raw:json")
	public String createSonAccount(String userName, int siteId,
			String passWord, String sonName, HttpSession session) {
		if (!((userName + "a").equals(sonName) && (userName + "b")
				.equals(sonName))) {
			map1.put("msg", "前后台数据不符");
			map1.put("code", 201);
			return Json.toJson(map1);
		}
		PortalUser user = (PortalUser) session.getAttribute("user");
		CloudSite site = (CloudSite) session.getAttribute("site");
		if (user == null)
			user = personalServiceImpl.getUserByNameAndWord(userName);
		if (user == null) {
			map1.put("msg", "该用户不存在");
			map1.put("code", 203);
			return Json.toJson(map1);
		}
		if (site == null)
			site = unitePortalServiceImpl.getSiteById(siteId);
		if (site == null) {
			map1.put("msg", "该场所不存在");
			map1.put("code", 203);
			return Json.toJson(map1);
		}
		// 如果子账号已经注册则返回
		if (user.getSonName().indexOf(sonName) > -1) {
			map1.put("msg", "该子账号已注册");
			map1.put("code", 201);
			return Json.toJson(map1);
		}
		boolean falg = personalServiceImpl.createSonAccount(siteId, sonName,
				passWord, user);
		if (falg) {
			String isHaveSon = user.getSonName();
			if (isHaveSon != null && !"".equals(isHaveSon)) {
				isHaveSon = isHaveSon + "," + sonName;
			} else {
				user.setSonName(sonName);
			}
			session.setAttribute("site", site);
			session.setAttribute("user", user);
		} else {
			map1.put("msg", "网络繁忙请稍后重试");
			map1.put("code", 201);
			return Json.toJson(map1);
		}
		map1.put("code", 200);
		return Json.toJson(map1);
	}

	/**
	 * 删除子账号
	 * 
	 * @param userName
	 * @param siteId
	 * @param passWord
	 * @param sonName
	 * @param session
	 * @return
	 */
	@At("/delSonAccount")
	@Ok("raw:json")
	public String delSonAccount(String userName, int siteId, String passWord,
			String sonName, HttpSession session) {

		return null;
	}
	/**
	 * 查询用户的信息
	 * @param userName
	 * @return
	 */
	@At("/fillePortalUser")
	@Ok("raw:json")
	public String fillePortalUser(@Param("userName") String userName){
		PortalUser pu = personalServiceImpl.getUserByNameAndWord(userName);
		if(pu != null){
			map1.put("user", pu);
		}
		return Json.toJson(map1);
	}
	/**
	 * 根据用户ID更新头像
	 * @param userId
	 * @param img
	 * @return
	 */
	@At("/updateImg")
	@Ok("raw:json")
	public String updateImg(@Param("userId") String userId,@Param("img") String image_url,@Param ("userName") String userName){
		if (image_url != null
				&& !image_url.startsWith("http://oss.kdfwifi.net/user_pic/")
				&& !"".endsWith(image_url)&& userId != "" && !"".equals(userId)) {
			// 上传图片
			InputStream baseInputOne = StringUtil.getInputStream(image_url);
			OssSchoolManage oss = new OssSchoolManage();
			String names = userName + ".jpg";// 图片名称
			try {
				String isOk = oss.uploadFile(baseInputOne, "user_pic/" + names,"image/jpeg");// 上传图片
				if (isOk != null) {// 上传成功
					//跟新数据
					boolean flag = personalServiceImpl.updateUserImg(Integer.parseInt(userId),names);
					if(flag){
						map1.put("result", true);// 个人信息更新成功
					}else{
						map1.put("result", false);// 个人信息更新失败
					}
				} else {
					map1.put("result", false);// 个人信息更新失败
					map1.put("message", "上传图片上失败");
				}
			} catch (Exception e) {
				logger.error("上传图片失败", e);
			}
		}else{
			map1.put("message", "参数不足");// 个人信息更新失败
		}
		return Json.toJson(map1);
	}
}
