package com.fxwx.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ViewModel;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.JspView;
import org.nutz.mvc.view.ViewWrapper;

import com.fxwx.entiy.APCashPledge;
import com.fxwx.entiy.AdAccessRecord;
import com.fxwx.entiy.AdEffect;
import com.fxwx.entiy.AdInfo;
import com.fxwx.entiy.AppInfo;
import com.fxwx.entiy.AppUser;
import com.fxwx.entiy.AuthUser;
import com.fxwx.entiy.CashPledge;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.CloudSitePortalEntity;
import com.fxwx.entiy.CloudSiteRouters;
import com.fxwx.entiy.CloudUser;
import com.fxwx.entiy.CmccRelayState;
import com.fxwx.entiy.CommercialTenant;
import com.fxwx.entiy.FeedBack;
import com.fxwx.entiy.LoginSmsCodeType;
import com.fxwx.entiy.NewPortalId;
import com.fxwx.entiy.Order;
import com.fxwx.entiy.PortalId;
import com.fxwx.entiy.PortalLog;
import com.fxwx.entiy.PortalLoginLog;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.RoutersAuthRetance;
import com.fxwx.entiy.RxzLog;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.entiy.SiteIncome;
import com.fxwx.entiy.SitePaymentRecord;
import com.fxwx.entiy.SitePriceConfig;
import com.fxwx.entiy.TemporaryParams;
import com.fxwx.entiy.WanLanMac;
import com.fxwx.entiy.WcahtSubscribeUserInfo;
import com.fxwx.entiy.WcahtUserOpenid;
import com.fxwx.service.AdService;
import com.fxwx.service.CmccRelayStateService;
import com.fxwx.service.MessageService;
import com.fxwx.service.OrderService;
import com.fxwx.service.PersonalService;
import com.fxwx.service.UnitePortalService;
import com.fxwx.service.UserPayService;
import com.fxwx.util.BASE64;
import com.fxwx.util.BigDecimalUtil;
import com.fxwx.util.CalendarUtil;
import com.fxwx.util.CheckoutUtil;
import com.fxwx.util.Codes;
import com.fxwx.util.DateUtil;
import com.fxwx.util.FileSystemProperty;
import com.fxwx.util.MD5;
import com.fxwx.util.PayUtils;
import com.fxwx.util.SHA256;
import com.fxwx.util.SetSystemProperty;
import com.fxwx.util.Sha1Util;
import com.fxwx.util.StringUtils;
import com.fxwx.util.WanipUtil;
import com.fxwx.util.XmlAndBcpUtil;
import com.fxwx.util.unifiedEntranceUtil;
import com.fxwx.util.cmcc.CmccSMS;
import com.fxwx.util.wechar.PayCommonUtil;
import com.fxwx.util.wechar.XmlUtils;
import com.fxwx.util.weixin.comment.copy.Configure;
import com.fxwx.util.weixin.comment.copy.HttpService;
import com.fxwx.util.weixin.comment.copy.JsonUtil;
import com.fxwx.util.weixin.comment.copy.RandomStringGenerator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@IocBean
@At("/PROT")
public class UserProtocolController extends BaseController {

	@Inject
	private UnitePortalService unitePortalServiceImpl;
 
	@Inject
	private MessageService messageServiceImpl;

	@Inject
	private CmccRelayStateService cmccRelayStateServiceImpl;

	@Inject
	private AdService adServiceImpl;

	@Inject
	private PersonalService personalServiceImpl;

	@Inject
	private UserPayService userPayServiceImpl;

	@Inject
	private OrderService orderServiceImpl;

	private static String APPID = "wx0c564926e0e07b5d";
	private static String MCHID = "1375627102";
	private static String KEY = "0Aeed232a45dvy87gevWEdfg5nn7dgg3";
	private static String notify_url = "https://wifi.feixun-wx.cn/portal/PROT/wechatNotify";
	private static String fxwx_web = "https://wifi.feixun-wx.cn";
	private static final Log log = Logs.getLog(UserProtocolController.class);

	/**
	 * 校验输入手机号该去那个页面 0：未注册 1：已注册（已设置密码） 2：已注册（未设置密码）
	 * 
	 * @return 缓存校验看用户是否在缓存中
	 */
	@At("/checkPhone")
	@Ok("raw:json")
	public String checkPhone(String userName, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		HashMap<String, Object> maps = new HashMap<String, Object>();
		// 判断手机号是否存在
		PortalUser user = unitePortalServiceImpl.getUserByName(userName.replace(" ", ""));
		log.error("-------mac-" + user);
		// maps.put("result",pUser==null?
		// 1:pUser.getPwdState()==1?1:pUser.getPwdState());//去注册
		// session.setAttribute("result",pUser==null?"0":pUser.getPwdState()==0?1:pUser.getPwdState());
		int state = 1;
		String mac = request.getParameter("mac") != null ? request.getParameter("mac")
				: (String) (request.getSession().getAttribute("usermac") != null ? request.getSession().getAttribute("usermac") : "");

		if (user == null) {
			maps.put("result", state);// 去注册
		} else {
			if (!mac.equals(user.getClientMac())) {
				state = 4;
			} else {
				state = 5;
			}
		}
		maps.put("result", state);
		session.setAttribute("result", state);
		return Json.toJson(maps);
	}

	/**
	 * 统一登录 老用户登录
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月15日 下午10:47:48
	 * @param userName
	 * @param password
	 * @param siteId
	 * @param result
	 * @param as
	 * @param request
	 * @param response
	 * @param session
	 * @return 0：登录成功； result 1:登录失败；
	 */
	@SuppressWarnings("unchecked")
	@At("/uniteLogin")
	@Ok("raw:json")
	public String uniteLogin(String userName, String password, int siteId, String result, String as, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String terminalDevice = request.getHeader("User-Agent");
		// String isPc =
		// unifiedEntranceUtil.getPcOrMobile(terminalDevice);//判断设备为手机或pc
		log.error("==uniteLogin=================================" + request.getQueryString());
		log.error("==uniteLogin=================================" + as);
		userName = userName.replace(" ", "");
		request.getSession().setAttribute("userName", userName);
		// password = "111111";
		String mesage = StringEscapeUtils.unescapeJava(as);
		Map<String, Object> json = (Map<String, Object>) JSONSerializer.toJSON(mesage);

		String apMac = (String) (request.getSession().getAttribute("apmac") != null ? request.getSession().getAttribute("apmac") : null);
		String mac = (String) (request.getSession().getAttribute("usermac") != null ? request.getSession().getAttribute("usermac") : null);
		String ssid = (String) (request.getSession().getAttribute("ssid") != null ? request.getSession().getAttribute("ssid") : null);
		log.error("-------apMac-" + apMac);
		log.error("-------mac-" + mac);
		log.error("-------ssid-" + ssid);

		CloudSite site = (CloudSite) session.getAttribute("site");
		if (site == null) {// 场所不存在
			site = unitePortalServiceImpl.getSiteById(siteId);
			if (site == null) {
				map1.put("result", 201);
				map1.put("msg", "场所不存在");
				return Json.toJson(map1);
			}
		}
		// 由于运营后台未做相对应的开发暂时先这样做,如果商户场所为设置场所负责人，则把商户手机号作为场所负责人电话添加其中
		if (site.getAdminer() == null || site.getAdminer().equals("")) {
			CloudUser cloudUser = unitePortalServiceImpl.getTouch(site.getUser_id());
			site.setAdminer(cloudUser.getUserName());
			unitePortalServiceImpl.updateSiteAmin(site);
		}
		session.setAttribute("site", site);
		boolean falg = false;
		// SiteCustomerInfo sci=null;
		// if(result.equals("2")){//2代表用户已经设置过新的密码
		// proUser=unitePortalServiceImpl.getUserByNameAndWord(userName,
		// password,2);
		// //老用户关联表
		// if(proUser!=null&&proUser.getSiteId()==0){
//		 unitePortalServiceImpl.relevanceSite(proUser.getId(), siteId);
		// }else if(proUser==null){
		// map1.put("result",205);
		// map1.put("msg", "用户名或密码错误");
		// return Json.toJson(map1);
		// }
		// CloudSitePortalEntity csp =
		// unitePortalServiceImpl.getSiteRetance(proUser.getId(),siteId);
		// if(csp == null){
//		 unitePortalServiceImpl.userSiteRetance(proUser.getId(),siteId);
		// }
		// if(proUser.getIsStoped()==1){
		// map1.put("result",209);
		// map1.put("msg", "该账号已被停用");
		// return Json.toJson(map1);
		// }
		// sci=unitePortalServiceImpl.getsci(proUser.getId(), siteId);
		// if(sci!=null){
		// //校验用户是否被锁定
		// String lock = unitePortalServiceImpl.getlockTime(sci);
		// if(lock!=null ){
		// int date =DateUtil.compareDate(DateUtil.getStringDate(), lock);
		// if(date != 1){
		// map1.put("result",301);
		// map1.put("msg", "您的账号已被锁定");
		// map1.put("times", lock);
		// return Json.toJson(map1);
		// }
		// }
		// boolean res=unitePortalServiceImpl.isSuperThree(userName,
		// site.getAllow_client_num(), json.get("allMac")+"",sci);
		// if(res){
		// map1.put("result",301);
		// map1.put("msg", "您的账号已被锁定");
		// map1.put("times", "11时:59分");
		// return Json.toJson(map1);
		// }
		//
		// }
		// //0代表场所开启实名认证
		//// if(site.getState()==0){
		//// if(proUser.getState()==1||proUser.getState()==3){
		//// map1.put("result",302);
		//// return Json.toJson(map1);
		//// }
		//// }
		// }else{
		PortalUser proUser = null;
		if (result.equals("1")) {// 用户不存在
			password = request.getParameter("code");
			proUser = unitePortalServiceImpl.getUserByName(userName);
			falg = unitePortalServiceImpl.updateUserPwd(proUser, userName, password, siteId, 1);
			if (falg) {
				if (proUser == null) {
					proUser = unitePortalServiceImpl.getUserByNameAndWord(userName, password, 1);
				}
				CloudSitePortalEntity csp = unitePortalServiceImpl.getSiteRetance(proUser.getId(), siteId);
				// boolean falg1 = false;
				if (csp == null) {
					unitePortalServiceImpl.userSiteRetance(proUser.getId(), siteId);
					// falg1 =
					// unitePortalServiceImpl.userSiteRetance(proUser.getId(),
					// siteId);
				}
			} else {
				map1.put("msg", "网络异常");
				map1.put("result", 300);
			}
		} else {// 用户存在
			proUser = unitePortalServiceImpl.getUserByName(userName);
			if (proUser == null) {
				falg = unitePortalServiceImpl.updateUserPwd(proUser, userName, password, siteId, 1);
				if (falg) {
					proUser = unitePortalServiceImpl.getUserByNameAndWord(userName, password, 1);
				}
			}
		}
		//更新用户与场所关联关系
		CloudSitePortalEntity csp = unitePortalServiceImpl.getSiteRetance(proUser.getId(),siteId);
		if(csp == null){
			unitePortalServiceImpl.userSiteRetance(proUser.getId(),siteId);
		}
		// 检测用户是否是欠费状态,如果为欠费状态则临时放行120s,优化后不在这做处理
		int authStatus = 0;
		// 为回调做处理
		session.setAttribute("userName", proUser.getUserName());
		long tsmp = System.currentTimeMillis();
		if (json.get("solarsys").equals("h3c")) {
			if (authStatus != 2)
				falg = unitePortalServiceImpl.insertAccount(proUser.getUserName(), json.get("allNasid") + "", json.get("allMac") + "", json.get("allIp") + "", tsmp, authStatus,
						json.get("solarsys") + "", json.get("userIp") + "", apMac, ssid);
		} else {
			falg = unitePortalServiceImpl.insertAccount(proUser.getUserName(), json.get("allNasid") + "", json.get("allMac") + "", json.get("allIp") + "", tsmp, authStatus, json.get("solarsys") + "",
					json.get("userIp") + "", apMac, ssid);
		}

		if (!falg) {
			map1.put("msg", "网络异常");
			map1.put("result", 300);
		}
		PortalLoginLog loginLog = new PortalLoginLog();
		loginLog.setSiteId(siteId);
		loginLog.setUser_name(userName);
		loginLog.setCreateTime(new Timestamp(new Date().getTime()));
		// 获取场所放行url
		StringBuffer url = request.getRequestURL();
		String httpurl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
		json.put("httpurl", httpurl);
		if (json.get("solarsys").equals("h3c")) {
			json.put("userName", userName);
			json.put("passWord", proUser.getPassWord().substring(0, 16));
			// json.put("passWord", password);
			json.put("action", "PORTAL_LOGIN");
			json.put("siteId", site.getId());
			// json.put("state", authStatus);
			boolean ress = false;
			if (authStatus != 2) {
				log.error("----passH3c---json------=========-------" + json);
				ress = unitePortalServiceImpl.passH3c(json);
			}
			if (/* ress */ true) {
				String reUrl = unitePortalServiceImpl.h3cUrl(json);
				map1.put("result", 200);
				map1.put("url", reUrl);
				// }else{
				// map1.put("msg", "网络异常");
				// map1.put("result", 300);
			}
			loginLog.setState(1);
			unitePortalServiceImpl.insertPortalLoginLog(loginLog);
			return Json.toJson(map1);
		} else if (json.get("solarsys").equals("moto")) {
			json.put("userName", userName);
			json.put("passWord", proUser.getPassWord().substring(0, 16));
			// json.put("passWord", password);
			json.put("action", "PORTAL_LOGIN");
			json.put("siteId", site.getId());
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@--moto登录认证开始-------" + json);
			// json.put("state", authStatus);
			boolean ress = false;
			if (authStatus != 2) {
				ress = unitePortalServiceImpl.passMoto(json);
			}
			if (/* ress */ true) {
				String reUrl = unitePortalServiceImpl.motoUrl(json);
				map1.put("result", 200);
				map1.put("url", reUrl);
				// }else{
				// map1.put("msg", "网络异常");
				// map1.put("result", 300);
			}
			loginLog.setState(1);
			unitePortalServiceImpl.insertPortalLoginLog(loginLog);
			return Json.toJson(map1);
		} else if (json.get("solarsys").equals("dunchong")) {
			json.put("userName", userName);
			json.put("passWord", proUser.getPassWord().substring(0, 16));
			json.put("usermac", mac);
			// json.put("passWord", password);
			json.put("action", "PORTAL_LOGIN");
			json.put("siteId", site.getId());
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@--敦崇登录认证开始-------" + json);
			// json.put("state", authStatus);
			boolean ress = false;
			if (authStatus != 2) {
				ress = unitePortalServiceImpl.passDunChong(json);
			}
			if (/* ress */ true) {
				String reUrl = unitePortalServiceImpl.DunChongUrl(json);
				map1.put("result", 200);
				map1.put("url", reUrl);
				// }else{
				// map1.put("msg", "网络异常");
				// map1.put("result", 300);
			}
			loginLog.setState(1);
			unitePortalServiceImpl.insertPortalLoginLog(loginLog);
			return Json.toJson(map1);
		}else if (json.get("solarsys").equals("wifidog")) {
			//拼接重定向url
			String params = "user="+userName+"&pwd="+proUser.getPassWord().substring(0, 16)+"&ip="+json.get("userIp")
							+"&mac="+json.get("mac")+"&authtype=web&gw_address="+json.get("gw_address")
							+"&gw_port="+json.get("gw_port")+"&gw_id="+json.get("gw_id")+"&url="+json.get("url");
			// 判断场所是否为收费模式
			if (site.getStauts() == 900) {
				String useDateIsExpire = useDateIsExpire(userName, siteId);
				JSONObject fromObject = JSONObject.fromObject(useDateIsExpire);
				String nasid = null;
				if(json.containsKey("nasid")){
					nasid = (String) json.get("nasid");
				}else if(json.containsKey("gw_id")){
					nasid = (String) json.get("gw_id");
				}
				// 使用时间到期或者是新用户
				if (fromObject.getInt("result") == 201 || fromObject.getInt("result") == 202) {
					String chargeurl = "https://wifi.feixun-wx.cn/portal/PROT/choiceTc?key="+KEY+"&siteId="+siteId+"&userMac="+mac
							+"&nasid="+nasid+"&allMac="+json.get("allMac")+"&userIp="+json.get("userIp")+"&username="+userName+"&tag=2";
					//拼接重定向url
					String params1 = "user=4006660050&pwd=111111&ip="+json.get("userIp")+"&mac="+json.get("mac")
									+"&authtype=web&gw_address="+json.get("gw_address")+"&gw_port="+json.get("gw_port")
									+"&gw_id="+json.get("gw_id")+"&url="+chargeurl;
					String redirectUrl1="http://"+json.get("gw_address")+":8080/wifidog/logincheck/?";
					map1.put("curl", chargeurl);
					map1.put("url", redirectUrl1+params1);
					map1.put("url2", redirectUrl1+params);
					map1.put("result", 100);
					loginLog.setState(1);
					unitePortalServiceImpl.insertPortalLoginLog(loginLog);
					log.error("@@@@@@@@@@@@@@@@@@@@@@@@--登录返回结果-------" + map1.toString());
					return Json.toJson(map1);
				}
			}
			
			String redirectUrl="http://"+json.get("gw_address")+":8080/wifidog/logincheck/?"+params;
			map1.put("url", redirectUrl);
			map1.put("result", 100);
			map1.put("msg", "SUCCESS");
			if (result.equals("4")) {// 用户该次登陆mac和上次登录终端mac不一致
				// 判断该用户使用新mac今日登陆认证的记录，按mac地址统计，进行核减一天
				List<AuthUser> aulist = unitePortalServiceImpl.selAuthUserlistToday(userName, (String) json.get("nasid"), mac);
				if (aulist != null && aulist.size() > 0) {
					if (aulist.size() == 1) {
						unitePortalServiceImpl.SubExpiration_time(proUser.getId(), 1, "DAY");
					}
				}
			}
			loginLog.setState(1);
			unitePortalServiceImpl.insertPortalLoginLog(loginLog);
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@--登录返回结果-------" + map1.toString());
			return Json.toJson(map1);
		}
		try {
			// 判断场所是否为收费模式
			if (site.getStauts() == 900) {
				String useDateIsExpire = useDateIsExpire(userName, siteId);
				JSONObject fromObject = JSONObject.fromObject(useDateIsExpire);
				String nasid = (String) json.get("nasid");
				// 使用时间到期或者是新用户
				if (fromObject.getInt("result") == 201 || fromObject.getInt("result") == 202) {
					String chargeurl = "https://wifi.feixun-wx.cn/portal/PROT/choiceTc?key="+KEY+"&siteId="+siteId+"&userMac="+mac
									+"&nasid="+nasid+"&allMac="+json.get("allMac")+"&userIp="+json.get("userIp")+"&username="+userName+"&tag=2";
					PortalUser user = new PortalUser();
					user.setUserName("4006660050");
					user.setPassWord("111111");
					Map<String, Object> map2 = unitePortalServiceImpl.getPassUrl(json, user, tsmp);
					map2.put("result", 100);
					map2.put("url", chargeurl);
					loginLog.setState(1);
					unitePortalServiceImpl.insertPortalLoginLog(loginLog);
					log.error("@@@@@@@@@@@@@@@@@@@@@@@@--设备认证开始-------" + json);
					return Json.toJson(map2);
				}
			}
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@--设备认证开始-------" + json);
			map1 = unitePortalServiceImpl.getPassUrl(json, proUser, tsmp);
			map1.put("result", 100);
			map1.put("msg", "SUCCESS");
			if (result.equals("4")) {// 用户该次登陆mac和上次登录终端mac不一致
				// 判断该用户使用新mac今日登陆认证的记录，按mac地址统计，进行核减一天
				List<AuthUser> aulist = unitePortalServiceImpl.selAuthUserlistToday(userName, (String) json.get("nasid"), mac);
				if (aulist != null && aulist.size() > 0) {
					if (aulist.size() == 1) {
						unitePortalServiceImpl.SubExpiration_time(proUser.getId(), 1, "DAY");
					}
				}
			}
			loginLog.setState(1);
			unitePortalServiceImpl.insertPortalLoginLog(loginLog);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("===网络异常===========================" + e.getMessage());
			map1.put("msg", "网络异常");
			map1.put("result", 300);
		}
		log.error("@@@@@@@@@@@@@@@@@@@@@@@@--登录返回结果-------" + map1.toString());
		return Json.toJson(map1);
	}

	/**
	 * 
	 * 验证码校验第二次登录，修改密码首先先校验
	 * 
	 * @returnstyle
	 */
	@At("/codes")
	@Ok("raw:json")
	public String codes(@Param("userName") String phone, @Param("code") String coded, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String userName = phone.replace(" ", "");
		CmccRelayState cmccRelayState = cmccRelayStateServiceImpl.getCmccRelayState(userName);
		if (cmccRelayState != null) {
			String relayState = cmccRelayState.getRelayState();

			String res_str = CmccSMS.validateDynamicPwd(userName, coded, relayState);
			String resultCode_str = "";
			if (res_str != null) {
				JSONObject json = JSONObject.fromObject(res_str);
				resultCode_str = json.getString("resultCode");
			}
			if (!resultCode_str.equals("")) {
				if (resultCode_str.equals("101000")) {
					map1.put("msgCode", CmccSMS.msgjson.getString(resultCode_str));
					map1.put("result", "0");
				} else {
					map1.put("msgCode", CmccSMS.msgjson.getString(resultCode_str));
					map1.put("result", "-1");
				}
			} else {
				map1.put("msgCode", "验证码校验失败");
				map1.put("result", "-1");
			}
		} else {
			log.error("------------@==@---------短信验证码记录为空----");
			map1.put("msgCode", "验证码校验失败");
			map1.put("result", "-1");
		}
		return Json.toJson(map1);
	}

	/**
	 * 验证码校验第二次登录，修改密码首先先校验 新用户登录
	 * 
	 * @return
	 */
	@At("/codes2")
	@Ok("raw:json")
	public String codes2(@Param("userName") String phone, @Param("code") String coded, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String userName = phone.replace(" ", "");
		String code;
		// 验证码校验
		if (coded != null && !"".equals(coded)) {
			code = (String) session.getAttribute(userName);
			if (code != null) {
				if (coded.equals(code)) {// 校验校验码是否正确;第二次登录设置密码
					map1.put("msgCode", "验证码校验正确");
					map1.put("result", "0");
				} else {
					map1.put("msgCode", "验证码校验失败");
					map1.put("result", "-1");
				}
			} else {
				map1.put("msgCode", "验证码获取失败");
				map1.put("result", "-1");
			}
		} else {
			map1.put("msgCode", "参数不全");
		}
		log.error("=============codes2==");
		return Json.toJson(map1);
	}

	/**
	 * 修改密码
	 * 
	 * @param pass
	 * @param phone
	 * @param response
	 * @param session
	 * @return
	 */
	@At("/forgetPass")
	@Ok("raw:json")
	public String forgetPass(@Param("pass") String pass, @Param("phone") String phone, HttpServletResponse response, HttpSession session) {
		String userName = phone.replace(" ", "");// 去掉手机号空格
		PortalUser result = unitePortalServiceImpl.getUserByName(userName);
		if (result != null) {// 查询结果不为null时
			// 修改密码
			result.setPwdState(2);
			result.setPassWord(SHA256.getUserPassword(userName, MD5.encode(pass).toLowerCase()));
			boolean flag = unitePortalServiceImpl.updatePortalUser(result);
			if (flag) {
				map1.put("result", "1");
				map1.put("msg", "修改成功");
			} else {
				map1.put("result", "0");
				map1.put("msg", "修改失败");
			}
		}
		return Json.toJson(map1);

	}

	// public void saveRZX(String apMac,String userMac,String userName){
	// RxzLog rxzLog = new RxzLog();
	// rxzLog.setApMac(apMac);
	// rxzLog.setMac(userMac);
	// rxzLog.setAuthAccount(userName);
	// rxzLog.setAuthType(XmlAndBcpUtil.Auth_Type);
	// rxzLog.setTstate(1);
	// rxzLog.setCreateTime(new Date());
	// unitePortalServiceImpl.saveRZX_LogState(rxzLog);
	// }

	/**
	 * Portal统一入口
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月15日 下午9:44:41
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@At("/unifiedEntrance")
	@Ok("re:jsp:error/error")
	public String unifiedEntrance(HttpServletRequest request, HttpServletResponse response, ViewModel model) {
		// response.setStatus(302);
		// response.addHeader("location",
		// "112.29.244.40:8080/v7/PROT/acceptAppAuth?"+request.getQueryString());
		String terminalDevice = request.getHeader("User-Agent");
		String isPc = unifiedEntranceUtil.getPcOrMobile(terminalDevice);// 判断设备为手机或pc
		log.error("=======1==========" + WanipUtil.getWanIp(request, response));
		log.error("=======2==========" + request.getRemoteAddr());
		log.error("=======3==========" + request.getLocalAddr());

		// //动态获取域名
		// if(!(SetSystemProperty.propertyName("url").split(":")[1]).equals(request.getServerName())){
		// SetSystemProperty.setProperty("url",
		// "http://"+request.getServerName()+"/");
		// }
		// log.error(SetSystemProperty.getValue("url"));
		String state = request.getParameter("state");
		String res = request.getParameter("res") != null ? request.getParameter("res") : null;
		if (res != null && res.equals("success")) {
			String key_val = request.getParameter("userurl");
			if (key_val.length() > 3) {
				return "redirect:/PROT/wechatJd?data=" + key_val;
			}
		}

		// 增加一个用户终端MAC的session参数，只针对H3C\MOTO设备
		String mac = request.getParameter("usermac") != null ? request.getParameter("usermac")
				: request.getParameter("wlanusermac") != null ? request.getParameter("wlanusermac") : request.getParameter("mac") != null ? request.getParameter("mac") : null;
		String apMac = request.getParameter("apmac") != null ? request.getParameter("apmac") : null;
		String ssid = request.getParameter("ssid") != null ? request.getParameter("ssid") : null;
		String nasid = request.getParameter("nasid") != null ? request.getParameter("nasid") : null;
		if (nasid == null) {
			nasid = request.getParameter("wlanacname") != null ? request.getParameter("wlanacname") : null;
			if (nasid != null) {
				// MOTO设备推送参数单独处理
				if (nasid.indexOf("0755.200.00") > 0) {
					String str = nasid;
					nasid = str.substring(0, 4) + "075520000460";
				}
			}else{//wifidog认证模式
				nasid = request.getParameter("gw_id") != null ? request.getParameter("gw_id") : null;
			}
		}
		if (mac != null) {
			model.setv("mac", mac);
			request.getSession().setAttribute("usermac", mac);
		}
		if (apMac != null) {
			request.getSession().setAttribute("apmac", apMac);
		}

		Map<String, Object> map;
		String isSwitch = "0";
		// 此逻辑为了高效处理用户点击“切换账号”按钮反应过慢，且有时无反应的问题。
		if (state != null && "-1".equals(state)) {
			log.error("=========切换账号=============");
			return "redirect:/PROT/unifyingTurn?" + request.getQueryString();
		} else {
			isSwitch = state == null ? "0" : state;
		}
		// 获取参数类型逻辑函数
		map = LogicalProcessing(request, response, isSwitch);
		log.error("------1------===map==-------" + map);
		// 查询短信验证平台
		if (map != null) {
			for (String key : map.keySet()) {
				if (key.equals("101")) {
					model.setv("map", map);
					return "jsp:error/errorwarn";
				}
			}
			model.setv("map", map);
			// 首先判断是否认证通过
			if (map.get("auth") != null && "authentication".equals(map.get("auth"))) {
				log.error("------2------===map==-------" + map);
				PortalUser user = (PortalUser) map.get("user");
				request.getSession().setAttribute("user", user);
//				int result = (Integer) map.get("result");
//				if(result == 2001){
//					model.setv("map", map);
//					return "jsp:jsp/fastauth";
//				}
				log.error("-----------=====准备转发URL======-------");
				// 跳转到个人中心
				if ("1".equals(isPc)) {
					return "jsp:jsp/homepage";
				} else {
					return "jsp:jsp/homepage";
				}
			}
			CloudSite site = (CloudSite) map.get("site");
			if (site.getAuthenticationStatus() == 1) {// 1.短信认证
				log.error("-----------ssid==" + ssid + "====---nasid----" + nasid);
				if (ssid != null && nasid != null) {
					request.getSession().setAttribute("ssid", ssid);
					List<NewPortalId> listSsid = unitePortalServiceImpl.getNewPortalIdBySsid(ssid, nasid);
					if (listSsid != null && !listSsid.isEmpty()) {
						int userType = listSsid.get(0).getUserType();
						if (userType == 2) {
							// 深圳政府部门Portal页面
							return "jsp:pub_jsp/nsydportal";
						} else if (userType == 7) {// 平安APP页面
							Map<String, Object> map4 = (Map<String, Object>) map.get("type");
							JSONObject jsonObject = JSONObject.fromObject(map4);
							String result = jsonObject.toString();
							map.put("type", result);
							model.setv("map", map);
							model.setv("andorid", FileSystemProperty.propertyName("andorid_open_url"));
							model.setv("ios", FileSystemProperty.propertyName("ios_open_url"));
							request.getSession().setAttribute("legalUrl", "yes");
							int isIos = unifiedEntranceUtil.getAndroidOrIos(terminalDevice);// 判断设备为ios或android
							// 统计ios和安卓首页pv量
							unitePortalServiceImpl.addAndroidAndIosHomePagePv(isIos, FileSystemProperty.propertyName("appid"));
							// 统计ios和安卓首页uv量
							unitePortalServiceImpl.addAndroidAndIosHomePageUv(isIos, FileSystemProperty.propertyName("appid"), mac);
							return "jsp:jsp/panLogin";
						} else if (userType == 3) {// 微信认证页面
							// 微信认证页面
							Map<String, Object> map4 = (Map<String, Object>) map.get("type");
							JSONObject jsonObject = JSONObject.fromObject(map4);
							String result = jsonObject.toString();
							map.put("type", result);
							String uamip = request.getParameter("uamip");
							log.error("====================unifiedEntrance==" + site);
							// 判断场所是否为收费模式
							if (site.getStauts() == 900) {
								boolean isCashPledge = cashPledgeCheck(uamip);// 是否已交押金
								if (isCashPledge) {
									if (site.getAuthenticationStatus() == 2) {// 微信认证
										model.setv("nasid", nasid);
										model.setv("siteId", site.getId());
										return "jsp:jsp/wechats";
									} else if (site.getAuthenticationStatus() == 1) {// 手机号认证
										log.error("===============手机号认证");
										return "jsp:jsp/index";
									}
								} else {
									SitePriceConfig priceConfig = unitePortalServiceImpl.getSitePriceConfig(site.getId());
									model.setv("siteId", site.getId());
									model.setv("priceConfig", priceConfig);
									model.setv("userMac", mac);
									model.setv("apMac", apMac);
									log.error("===================priceConfig==" + priceConfig + "==" + mac + "==" + apMac);
									return "jsp:jsp/cash_pledge";
								}
							} else if (site.getStauts() == 901) {// 场所不为收费模式
								if (site.getAuthenticationStatus() == 2) {// 微信认证
									model.setv("nasid", nasid);
									model.setv("siteId", site.getId());
									return "jsp:jsp/wechats";
								} else if (site.getAuthenticationStatus() == 1) {// 手机号认证
									log.error("===============手机号认证");
									return "jsp:jsp/index";
								}
							}
						} else {
							log.error("===============手机号认证");
							return "jsp:jsp/index";
						}
					} else {
						log.error("===============手机号认证");
						return "jsp:jsp/index";
					}
				} else if (ssid == null && nasid != null) {
					log.error("===============手机号认证");
					return "jsp:jsp/index";
				}
			} else if (site.getAuthenticationStatus() == 2) {// 2.微信认证
				// 微信认证页面
				Map<String, Object> map4 = (Map<String, Object>) map.get("type");
				JSONObject jsonObject = JSONObject.fromObject(map4);
				String result = jsonObject.toString();
				map.put("type", result);
				String uamip = request.getParameter("uamip");
				log.error("====================unifiedEntrance==" + site);
				// 判断场所是否为收费模式
				if (site.getStauts() == 900) {// 场所为收费模式
					boolean isCashPledge = cashPledgeCheck(uamip);// 是否已交押金
					if (isCashPledge) {
						model.setv("nasid", nasid);
						model.setv("siteId", site.getId());
						return "jsp:jsp/wechats";
					} else {
						SitePriceConfig priceConfig = unitePortalServiceImpl.getSitePriceConfig(site.getId());
						model.setv("siteId", site.getId());
						model.setv("priceConfig", priceConfig);
						model.setv("userMac", mac);
						model.setv("apMac", apMac);
						log.error("===================priceConfig==" + priceConfig + "==" + mac + "==" + apMac);
						return "jsp:jsp/cash_pledge";
					}
				} else if (site.getStauts() == 901) {// 场所不为收费模式
					if (site.getAuthenticationStatus() == 2) {// 微信认证
						model.setv("nasid", nasid);
						model.setv("siteId", site.getId());
						return "jsp:jsp/wechats";
					} else if (site.getAuthenticationStatus() == 1) {// 手机号认证
						log.error("===============手机号认证");
						return "jsp:jsp/index";
					}
				}
			} else {// 3.可选择认证方式

			}
		}
		log.error("=========redirect:/PROT/unifyingTurn?=============");
		return "redirect:/PROT/unifyingTurn?" + request.getQueryString();
	}

	@At("/wechats")
	@Ok("re:jsp:error/error")
	public String wechats(HttpServletRequest request, HttpServletResponse response, ViewModel model) {
		return "jsp:jsp/wechats";
	}

	@At("/cash_pledge")
	@Ok("re:jsp:error/error")
	public String cash_pledge(HttpServletRequest request, HttpServletResponse response, ViewModel model) {
		return "jsp:jsp/cash_pledge";
	}

	@At("/wechatJd")
	public ViewWrapper wechatJd(String data, HttpServletRequest request, HttpServletResponse response, ViewModel model) {
		TemporaryParams temporaryParams = unitePortalServiceImpl.getTemporaryParamsBykey(data);
		JSONObject json = JSONObject.fromObject(temporaryParams.getContent());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", json.getString("appId"));
		map.put("shopId", json.getString("shopId"));
		map.put("url", json.getString("url"));
		map.put("mac", json.getString("mac"));
		map.put("bssid", json.getString("bssid"));
		map.put("ssid", json.getString("ssid"));
		map.put("sign", json.getString("sign"));
		map.put("timestamp", json.getString("timestamp"));
		map.put("json", json.getJSONObject("json"));
		map.put("extend", json.getJSONObject("extend"));
		map.put("data", data);
		map.put("tag", 1);// 微信认证

		JSONObject jsonObject = json.getJSONObject("extend");
		String userMac = jsonObject.getString("userMac");
		int siteId = jsonObject.getInt("siteId");
		String nasid = jsonObject.getString("nasid");
		String appId = jsonObject.getString("appId");
		String userIp = json.getJSONObject("json").getString("userIp");
		map.put("siteId", siteId);
		map.put("userMac", userMac);
		map.put("nasid", nasid);
		map.put("userIp", userIp);
		log.error("=========================进入wechatJd====" + userMac + "::" + appId + "::" + nasid + "::" + siteId);
		WcahtUserOpenid wuo = unitePortalServiceImpl.findWcahtUserOpenid(userMac, null, appId, siteId, nasid);
		log.error("==============================findWcahtUserOpenid===" + wuo);
		if (wuo == null) {// 新用户
			map.put("code", 204);
		} else {// 老用户
			CloudSite site = unitePortalServiceImpl.getSiteById(wuo.getSiteId());// 获取场所信息
			if (site != null) {
				if (site.getStauts() == 900) {// 判断场所是否收费是收费模式
					log.error("=========================wechatJd==");
					/*
					 * List<SitePriceConfig> list = null; if
					 * (unitePortalServiceImpl.getSitePriceConfig(site.getId())
					 * == null) {// 场所收费类型为会员套餐 map.put("patType", 0); list =
					 * unitePortalServiceImpl.getSitePriceMemberCombo(site.getId
					 * ()); request.getSession().setAttribute("sitePrice",
					 * list); } else {// 场所收费类型为押金--月租 map.put("patType", 1);
					 * list =
					 * unitePortalServiceImpl.getSitePriceMemberCombo(site.getId
					 * ()); String strJson = Json.toJson(list);
					 * request.getSession().setAttribute("sitePrice", list);
					 * String strJson = Json.toJson(list); String strJson =
					 * Json.toJson(list); model.setv("sitePrice", strJson); }
					 */
					PortalUser portalUser = unitePortalServiceImpl.getUserByMac(wuo.getMac());
					if (portalUser != null) {
						String useDateIsExpire = useDateIsExpire(portalUser.getUserName(), site.getId());
						JSONObject resJson = JSONObject.fromObject(useDateIsExpire);
						log.error("==============================resJson" + resJson);
						if ((Integer) resJson.get("result") == 201) {
							map.put("code", 202);
							map.put("msg", "使用时间到期");
							map.put("stausCode", wuo.getCode());
							log.error("==============================场所是收费模式" + map1);
						} else {
							map.put("code", 203);
							map.put("msg", "使用时间没有到期");
						}
					}
				}
			}
		}
		log.error("==========================wechatJd===" + map);
		return new ViewWrapper(new JspView("jsp/wechatJd"), map);
	}

	/**
	 * Portal认证统一转向入口
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月15日 下午9:46:50
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@At("/unifyingTurn")
	@Ok("re:jsp:error/error")
	// http://localhost:8080/v7/PROT/unifyingTurn?wlanacname=4019.0755.200.00&wlanuserip=10.22.0.170
	// &wlanacip=183.234.196.9&ssid=CMCC-CITIC-LG1238&wlanusermac=4C-49-E3-4E-E8-26&moto=success&rep=200
	public String unifyingTurn(HttpServletRequest request, HttpServletResponse response, ViewModel model) {
		String terminalDevice = request.getHeader("User-Agent");
		String isPc = unifiedEntranceUtil.getPcOrMobile(terminalDevice);// 判断设备为手机或pc
		// 增加一个用户终端MAC的session参数，只针对H3C\MOTO设备
		String mac = request.getParameter("usermac") != null ? request.getParameter("usermac")
				: request.getParameter("wlanusermac") != null ? request.getParameter("wlanusermac") : request.getParameter("mac") != null ? request.getParameter("mac") : null;
		String apMac = request.getParameter("apmac") != null ? request.getParameter("apmac") : null;
		String ssid = request.getParameter("ssid") != null ? request.getParameter("ssid") : null;
		if (mac != null) {
			model.setv("mac", mac);
			request.getSession().setAttribute("usermac", mac);
		}
		if (apMac != null) {
			request.getSession().setAttribute("apmac", apMac);
		}
		if (ssid != null) {
			request.getSession().setAttribute("ssid", ssid);
		}
		List<LoginSmsCodeType> codeTypes = unitePortalServiceImpl.getSendSmsState();
		Random random = new Random();// 定义随机类
		int i = random.nextInt(codeTypes.size());

		Map<String, Object> map;
		map = getCallBckDeviceType(request, response);

		log.error("==unifyingTurn=================================" + map.toString());
		for (String key : map.keySet()) {
			if (key.indexOf("10") == 0)
				return "jsp:error/errorwarn";
		}
		if (map != null) {
			request.getSession().setAttribute("solarsys", ((Map<String, Object>) map.get("type")).get("solarsys") + "");
			model.setv("map", map);
			if ("success".equals(map.get("auth"))) {// 认证成功
				PortalUser user = (PortalUser) map.get("user");
				if (request.getSession().getAttribute("user") == null) {
					if (!((Map<String, Object>) map.get("type")).get("solarsys").toString().equals("h3c") && !((Map<String, Object>) map.get("type")).get("solarsys").toString().equals("moto")) {
						String userMacs = map.get("mac") == null ? ((Map<String, Object>) map.get("type")).get("allMac") + "" : map.get("mac") + "";
						// 根据用户的MAC获取AP的MAC
						// 保存apmac、usermac、手机号
						// 获取用户MAC
						// String userMacs=json.get("allMac")+"";
						String apMacs = unitePortalServiceImpl.getRadacctAPMAC(userMacs);
						log.error("@@@@@@@@@@@@@@@@@@@@@@@@--310--66666-----" + apMacs);
						if (apMacs != null && !apMacs.equals("")) {
							RxzLog rxzLog = new RxzLog();
							int is = apMacs.split(":").length;
							if (is > 0) {
								apMacs = apMacs.split(":")[0];
							}
							// 根据APMACs获取lasapMac
							String lasapMac = unitePortalServiceImpl.getWanLanMac(apMacs);
							log.error("@@@@@@@@@@@@@@@@@@@@@@@@--723--88888888888-----" + lasapMac);
							if (lasapMac != null && !lasapMac.equals("")) {
								rxzLog.setApMac(lasapMac);
								rxzLog.setMac(userMacs);
								rxzLog.setAuthAccount(user.getUserName());
								rxzLog.setAuthType(XmlAndBcpUtil.Auth_Type);
								rxzLog.setTstate(1);
								rxzLog.setCreateTime(new Date());
								log.error("@@@@@@@@@@@@@@@@@@@@@@@@--is lasapMac not null --88888888888-----" + rxzLog);
								unitePortalServiceImpl.saveRZX_LogState(rxzLog);
							}
						}
					}
				}

				request.getSession().setAttribute("user", user);
				unitePortalServiceImpl.updateUserClentMac(user, ((Map<String, Object>) map.get("type")).get("allMac") + "");
				if ("1".equals(isPc)) {
					return "jsp:jsp/homepage";
				} else {
					return "jsp:jsp/homepage";
				}
			} else {// 认证失败跳转
				if (map.get("user") == null) {// 输入账号密码登录回调处理
					if ("1".equals(isPc)) {
						if (map.get("code") != null && (int) map.get("code") == 202) {// 需要充值，跳到个人中心
							return "jsp:jsp/homepage";
						}
						// return "jsp:jsp/index";
					} else {
						if (map.get("code") != null && (int) map.get("code") == 202) {// 需要充值，跳到个人中心
							return "jsp:jsp/homepage";
						}
						// return "jsp:jsp/index";
					}
				} else {// 一键登录回调处理
					if ("1".equals(isPc)) {
						if (map.get("code") != null && (int) map.get("code") == 202) {// 需要充值，跳到个人中心
							PortalUser user = (PortalUser) map.get("user");
							unitePortalServiceImpl.updateUserClentMac(user, ((Map<String, Object>) map.get("type")).get("allMac") + "");
							return "jsp:jsp/homepage";
						}
						// return "jsp:jsp/index";
					} else {
						if (map.get("code") != null && (int) map.get("code") == 202) {// 需要充值，跳到个人中心
							PortalUser user = (PortalUser) map.get("user");
							unitePortalServiceImpl.updateUserClentMac(user, ((Map<String, Object>) map.get("type")).get("allMac") + "");
							return "jsp:jsp/homepage";
						}
						// return "jsp:jsp/login";
					}
				}
			}
		}
		return turnPage(codeTypes.get(i).getSmsType(), isPc);
	}

	public String turnPage(int type, String isPc) {
		if (type == 1) { // 阿里大鱼
			if ("1".equals(isPc)) {
				log.error("-turnPage-----jsp:jsp/index==-------");
				return "jsp:jsp/index";
			} else {
				log.error("-turnPage-----jsp:jsp/index==-------");
				return "jsp:jsp/index";
			}
		} else if (type == 2) { // 和通行证
			if ("1".equals(isPc)) {
				log.error("--turnPage----jsp:jsp/index==-------");
				return "jsp:jsp/index";
			} else {
				log.error("-turnPage-----jsp:jsp/index==-------");
				return "jsp:jsp/index";
			}
		} else {
			if ("1".equals(isPc)) {
				log.error("--turnPage----jsp:jsp/index==-------");
				return "jsp:jsp/index";
			} else {
				log.error("--turnPage----jsp:jsp/index==-------");
				return "jsp:jsp/index";
			}
		}
	}

	/**
	 * 测试
	 * 
	 * @param telephone
	 * @param templateCode
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@At("/codedTest")
	@Ok("raw:json")
	public String codedTest(@Param("telephone") String userName, @Param("templateCode") String templateCode, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		String telephone = userName.replace(" ", "");
		String code = Codes.buildVerifyCode();
		boolean flag = true;
		if (flag) {
			request.getSession().setAttribute(telephone, code);
			map1.put("result", 0);
			map1.put("code", code);
			log.error("验s证码：" + code);
			map1.put("result", -1);
		}
		return Json.toJson(map1);
	}

	/**
	 * 跳到用户锁定页面
	 * 
	 * @param locakTime
	 * @return
	 */
	@At("/toLockPc")
	public ViewWrapper toLockPc(HttpServletRequest request) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("locakTime", request.getParameter("locakTime"));
		map1.put("changeurl", request.getParameter("changeurl"));
		map1.put("userName", request.getParameter("userName"));
		return new ViewWrapper(new JspView("pc/lockUser"), map1);
	}

	/**
	 * 跳到用户锁定页面
	 * 
	 * @param locakTime
	 * @return
	 */
	@At("/toLock")
	public ViewWrapper toLock(HttpServletRequest request) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("locakTime", request.getParameter("locakTime"));
		map1.put("changeurl", request.getParameter("changeurl"));
		map1.put("userName", request.getParameter("userName"));
		return new ViewWrapper(new JspView("mobile/lockUser"), map1);
	}

	/**
	 * 跳转修改密码页面
	 * 
	 * @return
	 */
	@At("/toUpdatePwd")
	public ViewWrapper toUpdatePwd(HttpServletRequest request, ViewModel model) {
		String userName = request.getParameter("userName");
		model.setv("userName", userName);
		model.setv("url", request.getParameter("ur"));
		return new ViewWrapper(new JspView("mobile/resetPawword"), model);
	}

	/**
	 * 锁定页面修改密码
	 * 
	 * @return
	 */

	@At("/userPwd")
	@Ok("raw:json")
	public String userPwd(String userName, String password, String newPassWord, HttpServletRequest request, ViewModel model) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		PortalUser user = unitePortalServiceImpl.getUserByName(userName);
		if (user == null)
			new ViewWrapper(new JspView("error/error"), null);
		if (!user.getPassWord().equals(SHA256.getUserPassword(userName, MD5.encode(password).toLowerCase()))) {
			map1.put("code", 201);
			map1.put("msg", "原始密码不对,请从新输入");
			return Json.toJson(map1);
		}
		boolean falg = unitePortalServiceImpl.updateUserPwd(user, userName, newPassWord, 0, 1);
		if (falg) {
			map1.put("code", 200);
			map1.put("msg", "修改成功请重新登录");
		} else {
			map1.put("code", 201);
			map1.put("msg", "网络故障请稍后重试");
		}
		return Json.toJson(map1);
	}

	/**
	 * 港澳台登陆页面
	 * 
	 * @return
	 */
	@At("/toSpecalLogin")
	public String toSpecalLogin(String as, ViewModel model) {
		String mesage = StringEscapeUtils.unescapeJava(as);
		Map<String, Object> json = (Map<String, Object>) JSONSerializer.toJSON(mesage);
		return "mobile/register1";
	}

	/**
	 * 微信登录
	 * 
	 * @author:dengfei200857@163.com
	 * @date: 2017年8月8日 上午10:47:00
	 * @Description:
	 * @param:
	 * @return
	 * @throws Exception
	 */
	@At("/toWechatLogin")
	@Ok("raw:json")
	public String toWechatLogin(String ssid, String extend, String nasid, String siteId, HttpServletRequest request, HttpServletResponse response, HttpSession session, ViewModel model) {
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		String mesage = StringEscapeUtils.unescapeJava(extend);
		String res = wxTemporaryPass(mesage);// 临时放行
		Map<String, Object> json = (Map<String, Object>) JSONSerializer.toJSON(mesage);
		log.error("toWechatLogin====================" + extend + "::" + res + "::" + json + "::" + mesage);
		String shopId = "81209";
		String appId = Configure.getAppid();
		String secretKey = Configure.getAppSecret();
		String authUrl = basePath + "/portal/PROT/wxAuth?httpCode=200";
		long timestamp = System.currentTimeMillis();
		String mac = (json.get("allMac") + "").replaceAll("-", ":").toLowerCase();
		String bssid = (json.get("called") + "").replaceAll("-", ":").toLowerCase();
		map1.put("appId", appId);
		map1.put("shopId", shopId);
		map1.put("url", authUrl);
		map1.put("mac", mac);
		map1.put("ssid", ssid);
		map1.put("timestamp", timestamp);
		map1.put("json", json);
		map1.put("bssid", bssid);
		// ` map1.put("siteId", siteId);
		// map1.put("userMac",mac);

		json = unitePortalServiceImpl.fifterMap(json);
		JSONObject jsonObject = JSONObject.fromObject(json);
		jsonObject.put("nasid", nasid);
		jsonObject.put("siteId", siteId);
		jsonObject.put("appId", appId);
		jsonObject.put("userMac", mac);
		// 3、将json对象转化为json字符串
		String result = jsonObject.toString();
		map1.put("extend", result);
		String toSign = appId + result + timestamp + shopId + authUrl + mac + ssid + bssid + secretKey;
		log.error("@@@@@@@@@@@@@@@@@@@@@@@@--toWechatLogin====" + toSign);
		String sign = MD5.MD5Encode(toSign);
		map1.put("sign", sign);
		TemporaryParams temporaryParams = new TemporaryParams();
		String key_val = MD5.encode(map1.get("mac") + "" + System.currentTimeMillis());
		temporaryParams.setKey_val(key_val);
		temporaryParams.setContent(Json.toJson(map1));
		temporaryParams.setCreateTime(new Timestamp(new Date().getTime()));
		unitePortalServiceImpl.inserTemporaryParams(temporaryParams);
		// 微信登录成功
		if (json.get("solarsys").equals("chilli") || json.get("solarsys").equals("ikuai") || json.get("solarsys").equals("ros")) {
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@--" + json.get("solarsys") + "临时放行登录认证开始-------" + json);

			/*
			 * // 根据和当前时间的比较计算到期时间 SiteCustomerInfo scii =
			 * userPayServiceImpl.getExpirationTimeByProuserid(proUser.getId(),
			 * siteId); //判断用户的使用时间是否到期 if(scii.getExpirationTime().after(new
			 * Date())){ map1.put("msg","认证成功"); } else{ return
			 * "jsp:jsp/wechats";//时间到期,跳转到支付页面 }
			 */

			Map<String, Object> map = (Map<String, Object>) JSONSerializer.toJSON(res);
			String url = (String) map.get("url");
			url = url.replace("userurl=", "userurl=" + key_val);
			map1.put("url", url);
		}
		map1.put("key", key_val);
		/*
		 * map1.put("tag",1);//微信认证
		 */ log.error("@@@@@@@@@@@@@@@@@@@@@@@@--toWechatLogin-------" + map1);
		return Json.toJson(map1);
	}

	/**
	 * 
	 * @author:dengfei200857@163.com
	 * @date: 2018年4月7日 下午12:11:51
	 * @Description: wxAuth 微信回来接口
	 * @param:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@At("/wxAuth")
	public void wxAuth(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		String openId = request.getParameter("openId");
		session.setAttribute("openId", openId);
		String extend = request.getParameter("extend").replace("[", "{").replace("]", "}");
		String tel = request.getParameter("tid");
		log.error("获取到的微信请求参数===" + openId + "=====" + extend + "=====" + tel + "===");
		JSONObject jasonObject = JSONObject.fromObject(extend);
		Map<String, Object> map = (Map<String, Object>) jasonObject;

		/* String mac = map.get("allMac") + ""; */
		String nasid = map.get("nasid") + "";
		String userMac = map.get("userMac") + "";
		String appId = map.get("appId") + "";
		Integer siteId = Integer.parseInt(map.get("siteId") + "");

		// 进行openID存入数据库
		WcahtUserOpenid wuo = unitePortalServiceImpl.findWcahtUserOpenid(userMac, openId, appId, siteId, nasid);
		if (wuo == null) {// 新用户,将openid插入到订单表
			orderServiceImpl.updateOrderOpenid(openId, userMac);
		}

		WcahtUserOpenid wcu = new WcahtUserOpenid();
		wcu.setMac(userMac);
		wcu.setOpenId(openId);
		wcu.setNasid(nasid);
		wcu.setSiteId(siteId);
		wcu.setAppId(appId);
		wcu.setCode(200);
		wcu.setCreateTime(new Timestamp(new Date().getTime()));
		unitePortalServiceImpl.insertWcahtUserOpenid(wcu);

		/*
		 * if (wcu == null) { wcu = new WcahtUserOpenid(); wcu.setMac(userMac);
		 * wcu.setOpenId(openId); wcu.setNasid(nasid); wcu.setSiteId(siteId);
		 * wcu.setAppId(appId); wcu.setCode(200); wcu.setCreateTime(new
		 * Timestamp(new Date().getTime()));
		 * unitePortalServiceImpl.insertWcahtUserOpenid(wcu); } else if
		 * (wcu.getOpenId().equals(openId) && !wcu.getMac().equals(userMac)) {
		 * wcu.setMac(userMac);
		 * unitePortalServiceImpl.updateWcahtUserOpenid(wcu); } else if
		 * (!wcu.getOpenId().equals(openId) && wcu.getMac().equals(userMac)) {
		 * wcu.setOpenId(openId);
		 * unitePortalServiceImpl.updateWcahtUserOpenid(wcu); }
		 */

		/* Thread.sleep(60000*5); */

		if ("h3c".equals(map.get("solarsys"))) {
			map.put("userName", map.get("allMac"));
			map.put("passWord", "111111");
			map.put("action", "PORTAL_LOGIN");
			unitePortalServiceImpl.passH3c(map);
			response.setStatus(200);
			PrintWriter out = response.getWriter();
			out.write("<meta http-equiv='content-type' content='text/html;charset=UTF-8'/>");
			out.write("");
		} else {
			response.setStatus(200);
			PrintWriter out = response.getWriter();
			out.write("<meta http-equiv='content-type' content='text/html;charset=UTF-8'/>");
			out.write("");
		}
	}

	/**
	 * 校验用户是否关注公众号
	 * 
	 * @author:dengfei200857@163.com
	 * @date: 2017年8月23日 下午3:37:42
	 * @Description:
	 * @param:
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@At("/checkWxUser")
	@Ok("raw:json")
	public String checkWxUser(HttpSession session, String as, String userMac, int siteId, String appId, String nasid, HttpServletRequest request) throws Exception {
		String urals = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + Configure.getAppid() + "&secret=" + Configure.getAppSecret();
		/*
		 * String urals="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+
		 * Configure.getAppid()+"&corpsecret="+Configure.getAppSecret();
		 */
		// String a= as.replace("{", "").replace("}", "");
		/*
		 * String urals=
		 * "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx73df879b0ddf1c0e&secret=5475f39dfb8027f880a17b22765d258f";
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		String mesage = StringEscapeUtils.unescapeJava(new String(as));
		Map<String, Object> json = (Map<String, Object>) JSONSerializer.toJSON(mesage);
		String accessToken = session.getAttribute("access_token") + "";

		log.error("=========================进入checkWxUser====" + userMac + "::" + appId + "::" + nasid + "::" + siteId);
		WcahtUserOpenid wuo = unitePortalServiceImpl.findWcahtUserOpenid(userMac, null, appId, siteId, nasid);
		log.error("=========================checkWxUser====" + wuo);
		if (wuo == null) {
			map1.put("code", 201);
			return Json.toJson(map1);
		}
		// 将portal_user中的临时mac账号替换成openid
		String username = userMac.replaceAll(":", "");
		PortalUser portalUser = unitePortalServiceImpl.getLogin(username, "111111");
		if (portalUser != null) {
			portalUser.setUserName(wuo.getOpenId());
			portalUser.setPassWord(SHA256.getUserPassword(wuo.getOpenId(), MD5.encode("111111").toLowerCase()));
			unitePortalServiceImpl.updatePortalUser(portalUser);
		}
		String openid = wuo.getOpenId();
		log.error("获取session的值===" + accessToken);
		String res = "";
		Map<String, Object> maps = new HashMap<String, Object>();
		if (accessToken == null || accessToken.equals("null")) {
			res = HttpService.doGet(urals);
			log.error("============res==" + res);
			maps = JsonUtil.fromJson(res, HashMap.class);
			session.setAttribute("access_token", maps.get("access_token"));
			urals = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + maps.get("access_token") + "&openid=" + openid;
			log.error("=====================checkWxUser======urals=" + urals);
		} else {
			urals = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openid;
		}
		res = HttpService.doGet(urals);
		log.error("==========================res==" + res);
		maps = JsonUtil.fromJson(res, HashMap.class);
		log.error("是否关注结果查询maps====" + maps);
		String subscribe = maps.get("subscribe") + "";
		log.error("=====================checkWxUser======subscribe=" + subscribe);
		if (subscribe.equals("0")) {// 0==未关注
			map1.put("code", 201);
		} else {// 关注微信号
			String apmac = (String) request.getSession().getAttribute("apmac");
			String ssid = (String) request.getSession().getAttribute("ssid");
			if (json.get("solarsys").equals("h3c")) {
				unitePortalServiceImpl.insertAccount(openid, json.get("allNasid") + "", json.get("allMac") + "", json.get("allIp") + "", new Date().getTime(), 0, json.get("solarsys") + "",
						json.get("userIp") + "", apmac, ssid);
			} else {
				unitePortalServiceImpl.insertAccount(openid, json.get("allNasid") + "", json.get("allMac") + "", json.get("allIp") + "", new Date().getTime(), 0, json.get("solarsys") + "",
						json.get("userIp") + "", apmac, ssid);
			}
			// 判断openid是否存在，存在不添加
			WcahtSubscribeUserInfo wuserInfo = unitePortalServiceImpl.findWSUIOpenId(openid);
			if (wuserInfo == null) {
				// 关注公众号获取用户的详情(调取微信获取用户信息接口)
				WcahtSubscribeUserInfo wsui = new WcahtSubscribeUserInfo();
				wsui.setUserName(maps.get("nickname") + "");
				wsui.setSex(Integer.parseInt(maps.get("sex") + ""));
				wsui.setAddress(maps.get("country") + "." + maps.get("province") + "." + maps.get("city"));
				// 时间戳转时间
				wsui.setSubscribeTime(DateUtil.stampToDate(Integer.valueOf(maps.get("subscribe_time") + "")));
				wsui.setCreateime(new Timestamp(new Date().getTime()));
				wsui.setSubscribe(Integer.parseInt(maps.get("subscribe") + ""));
				wsui.setOpenId(openid);
				wsui.setHeadimgurl(maps.get("headimgurl") + "");
				unitePortalServiceImpl.insertWcahtSubscribeUserInfo(wsui);
			}

			String data = "";
			StringBuffer url = request.getRequestURL();
			String httpurl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
			json.put("httpurl", httpurl);

			if (json.get("solarsys").equals("h3c")) {
				json.put("userName", openid);
				json.put("passWord", "111111");
				json.put("action", "PORTAL_LOGIN");
				data = httpurl + "/PROT/toH3CLogin?as" + json;
				map1.put("url", data);
			} else {
				PortalUser user = new PortalUser();
				user.setUserName(openid);
				user.setPassWord("111111");
				map1 = unitePortalServiceImpl.getPassUrl(json, user, System.currentTimeMillis());
				data = map1.get("url") + "";
				map1.put("code", 200);
				log.error("认证url====" + data);
				map1.put("url", data);
			}
		}
		return Json.toJson(map1);
	}

	/**
	 * h3c微信登录
	 * 
	 * @author:dengfei200857@163.com
	 * @date: 2017年8月25日 下午2:29:09
	 * @Description:
	 * @param:
	 * @return
	 */
	@At("/toH3CLogin")
	public String toH3CLogin(String as) {
		String mesage = StringEscapeUtils.unescapeJava(new String(as));
		Map<String, Object> json = (Map<String, Object>) JSONSerializer.toJSON(mesage);
		// 获取场所放行url
		unitePortalServiceImpl.passH3c(json);
		String reUrl = unitePortalServiceImpl.h3cUrl(json);
		return "redirect:/PROT/unifyingTurn?" + reUrl.split("?")[0];
	}

	/**
	 * 常见问题页面
	 * 
	 * @return
	 */
	@At("/common_problem")
	@Ok("re:jsp:error/error")
	public String commonProblem() {
		return "jsp:jsp/common_problem";
	}

	@At("/common_problemA")
	@Ok("re:jsp:error/error")
	public String commonProblemA() {
		return "jsp:jsp/common_problemA";
	}

	/**
	 * 使用说明页面
	 * 
	 * @return
	 */
	@At("/instruction")
	@Ok("re:jsp:error/error")
	public String instruction() {
		return "jsp:jsp/instructions";
	}

	@At("/instructionA")
	@Ok("re:jsp:error/error")
	public String instructionA() {
		return "jsp:jsp/instructionsA";
	}

	/**
	 * 意见反馈页面
	 * 
	 * @return
	 */
	@At("/feedback")
	@Ok("re:jsp:error/error")
	public String feedback() {
		return "jsp:jsp/feedback";
	}

	/**
	 * 保存意见反馈
	 * 
	 * @return
	 */
	@At("/save_feedback")
	@Ok("raw:json")
	public String saveFeedback(@Param("type") int type, @Param("content") String content, @Param("wifiSite") String wifiSite, @Param("contactWay") String contactWay, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		FeedBack feedBack = new FeedBack();
		feedBack.setType(type);
		if (type == 1) {
			feedBack.setTypeName("网络问题");
		} else if (type == 2) {
			feedBack.setTypeName("使用问题");
		} else if (type == 3) {
			feedBack.setTypeName("其它");
		}
		feedBack.setContent(content);
		feedBack.setWifiSite(wifiSite);
		feedBack.setContactWay(contactWay);
		feedBack.setCreateTime(Timestamp.valueOf(DateUtil.getStringDate()));
		boolean flag = unitePortalServiceImpl.insertFeedback(feedBack);
		if (flag) {
			map1.put("msg", "ok");
		} else {
			map1.put("msg", "err");
		}
		return Json.toJson(map1);
	}

	/**
	 * 进入新版登录完成页面
	 * 
	 * @return
	 */
	@At("/homepage")
	@Ok("re:jsp:error/error")
	public String homePage(HttpServletRequest request, ViewModel model) {
		String userName = (String) request.getSession().getAttribute("userName");
		model.setv("userName", userName);
		return "jsp:jsp/homepage";
	}

	/**
	 * 去使用协议页面
	 * 
	 * @return
	 */
	@At("/userAgreement")
	@Ok("re:jsp:error/error")
	public String userAgreement() {
		return "jsp:jsp/userAgreement";
	}

	/**
	 * 去使用协议页面
	 * 
	 * @return
	 */
	@At("/balance_inquiry")
	@Ok("re:jsp:error/error")
	public String balance_inquiry() {
		return "jsp:jsp/balance_inquiry";
	}	
	
	/**
	 * 轮播图
	 * 
	 * @return
	 */
	@At("/slideshow")
	@Ok("re:jsp:error/error")
	public String slideshow() {
		return "jsp:jsp/slideshow";
	}
	
	@At("/userAgreementA")
	@Ok("re:jsp:error/error")
	public String userAgreementA() {
		return "jsp:jsp/useragreementA";
	}

	/**
	 * 微信公众号充值中心
	 * 
	 * @return
	 */
	@At("/wechatpay")
	@Ok("re:jsp:error/error")
	public String wechatpay(HttpServletRequest request, HttpServletResponse response, ViewModel model) {
		return "jsp:jsp/wechatpay";
	}

	/**
	 * 手机号信息收集
	 * 
	 * @return
	 */
	@At("/getphonenumber")
	@Ok("re:jsp:error/error")
	public String getphonenumber(HttpServletRequest request, HttpServletResponse response, ViewModel model) {
		return "jsp:jsp/getphonenumber";
	}
	
	/**
	 * 1、通过code换取网页授权access_token 2、拉取微信用户信息
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年6月29日 下午7:08:01
	 * @param code
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@At("/wechatGetUserInfo")
	@Ok("raw:json")
	public String wechatGetUserInfo(String code, HttpServletRequest request, HttpServletResponse response, ViewModel model) {
		Map<String,Object> map = new HashMap<String,Object>();
		if (code != null) {
			String url1 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Configure.getAppid() + "&secret=" + Configure.getAppSecret() + "&code=" + code
					+ "&grant_type=authorization_code";
			try {
				String res1 = HttpService.doGet(url1);
				JSONObject json1 = JSONObject.fromObject(res1);
				log.error("wechatGetUserInfo===json1=" + json1);
				if (json1.containsKey("access_token")) {
					String access_token = json1.getString("access_token");
					String refresh_token = json1.getString("refresh_token");
					String openid = json1.getString("openid");
					map.put("openid", openid);
					// String url2 =
					// "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+Configure.getAppid()+"&grant_type=refresh_token&refresh_token="+refresh_token;
					// String res2 = HttpService.doGet(url2);
					// JSONObject json2 = JSONObject.fromObject(res2);
					// if(json2.containsKey("access_token")){
					//
					// }

					String url3 = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
					String res3 = HttpService.doGet(url3);
					JSONObject json3 = JSONObject.fromObject(res3);
					map.put("userinfo", json3);
					List<CloudSite> cloudSites = unitePortalServiceImpl.getCloudSiteByOpenId(openid);
					map.put("list", cloudSites);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.error("wechatGetUserInfo====" + map);
		return Json.toJson(map);
	}

	/**
	 * 去热点查询页面
	 * 
	 * @return
	 */
	@At("/inquiry")
	@Ok("re:jsp:error/error")
	public String inquiry(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// List<PortalId> list=unitePortalServiceImpl.getAllPortalId();
		JSONArray array = new JSONArray();
		CloudSite cloudSite1 = (CloudSite) session.getAttribute("site");
		String address2 = cloudSite1.getAddress();
		session.setAttribute("address2", address2);
		String area = address2.substring(address2.indexOf("区") - 2, address2.indexOf("区") + 1);
		array = unitePortalServiceImpl.getCloudSiteLikeAddres(area);
		// for (int i = 0; i < list.size(); i++) {
		// PortalId portalId = list.get(i);
		// String ssid = portalId.getSsid();
		// String nasid = portalId.getNasid();
		// CloudSite cloudSite = unitePortalServiceImpl.getCloudSite(nasid);
		// String address = cloudSite.getAddress();
		// String site_name = cloudSite.getSite_name();
		// JSONObject json = new JSONObject();
		// json.put("ssid", ssid);//WIFI名称
		// json.put("address", address);//地址
		// json.put("site_name", site_name);//地点名称
		// array.add(json);
		// }
		request.setAttribute("data", array);
		return "jsp:jsp/inquiry";
	}

	/**
	 * 热点查询
	 */
	@At("/searchHotSpot")
	@Ok("raw:json")
	public String searchHotSpot(@Param("content") String content) {
		if (content == "" || content == null) {
		}
		JSONArray array1 = new JSONArray();
		List<CloudSite> list2 = unitePortalServiceImpl.getHotSpot(content);
		for (int i = 0; i < list2.size(); i++) {
			CloudSite cloudSite = list2.get(i);
			String address = cloudSite.getAddress();
			String site_name = cloudSite.getSite_name();
			int site_id = cloudSite.getId();
			CloudSiteRouters cloudSiteRouter = unitePortalServiceImpl.getRoutersBySite_id(site_id);
			if (cloudSiteRouter == null) {
				continue;
			}
			String nasid = cloudSiteRouter.getNasid();
			PortalId portalId = unitePortalServiceImpl.getPortalIdByNasid(nasid);
			if (portalId == null) {
				continue;
			}
			String ssid = portalId.getSsid();
			if (portalId != null || nasid != null) {
				JSONObject json = new JSONObject();
				json.put("ssid", ssid);// WIFI名称
				json.put("address", address);// 地址
				json.put("site_name", site_name);// 地点名称
				array1.add(json);
			}
		}
		return array1.toString();
	}

	/**
	 * 跳转app认证页面
	 * 
	 * @param params
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@At("/toAppLogin")
	@Ok("re:jsp:error/error")
	public String toAppLogin(String params, HttpServletResponse response, ViewModel model) {
		String mesage = StringEscapeUtils.unescapeJava(params);
		// Map<String,Object> passMap = (Map<String,Object>)
		// JSONSerializer.toJSON(mesage);
		model.setv("params", params);
		model.addv("url", FileSystemProperty.propertyName("andorid_download_url"));
		// unitePortalServiceImpl.countPvAndUv(0);
		return "jsp:jsp/androiddown";
	}

	/**
	 * 接收app认证
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@At("/acceptAppAuth")
	@Ok("raw:json")
	public String acceptAppAuth(HttpServletRequest request, HttpServletResponse response) {
		log.error("ping an  coming in intefence   ====");
		Map<String, Object> authMap = checkAppAuthParam(request, response);
		if ("1".equals(authMap.get("code"))) {
			return Json.toJson(authMap);
		}
		AppInfo ai = (AppInfo) authMap.get("app");
		String appId = ai.getAppId();// "20401";
		String sha1key = ai.getSercetKey();// "a24a49eb99c442628cd48eef18843afd";
		String nonceStr = RandomStringGenerator.getRandomStringByLength(8).trim();
		String timeStamp = RandomStringGenerator.getTimeStamp().trim();
		List<String> list = new ArrayList<String>();
		list.add(appId);
		list.add(authMap.get("openid") + "");
		list.add(authMap.get("openkey") + "");
		list.add(timeStamp);
		list.add(nonceStr);
		String sign = Sha1Util.getSignature(list, sha1key);
		String url = FileSystemProperty.propertyName("checkTokenUrl") + "?appid=" + appId + "&openid=" + authMap.get("openid") + "&openkey=" + authMap.get("openkey") + "&timestamp=" + timeStamp
				+ "&nonce=" + nonceStr + "&signature=" + sign;
		String res;
		try {
			res = HttpService.doGet(url);
			Map<String, Object> callBackMap = JsonUtil.fromJson(res, HashMap.class);
			log.error("优联checktoken接口返回结果=======" + callBackMap);
			if (callBackMap.get("code").equals("0")) {
				String durtion = callBackMap.get("data") + "";
				Map<String, Object> durtionMap = null;
				if (durtion == null || durtion.equals("")) {// 若时间为空则认为app未返回时常
					durtionMap = new HashMap<String, Object>();
				} else {
					durtionMap = (Map<String, Object>) JSONSerializer.toJSON(durtion);
				}
				Map<String, Object> json = (Map<String, Object>) authMap.get("aap");
				AppUser au = (AppUser) authMap.get("appuser");
				// if(durtion!=null||!durtion.equals("")){//若时间为空则认为app未返回时常
				durtionMap.put("userId", au.getId());
				durtionMap.put("siteId", json.get("siteid"));
				durtionMap.put("appuser", au);
				boolean result = unitePortalServiceImpl.addAppSiteCustomerInfo(durtionMap);
				if (!result) {
					map1.put("msg", "网络异常");
					map1.put("code", 1);
					return Json.toJson(map1);
				}
				// }
				String apMac = (String) (request.getSession().getAttribute("apmac") != null ? request.getSession().getAttribute("apmac") : null);
				long tsmp = System.currentTimeMillis();
				if (json.get("solarsys").equals("h3c")) {
					unitePortalServiceImpl.insertAccount(au.getOpenid(), json.get("allNasid") + "", json.get("allMac") + "", json.get("allIp") + "", tsmp, 0, json.get("solarsys") + "",
							json.get("userIp") + "", apMac, authMap.get("ssid") + "");
				} else {
					unitePortalServiceImpl.insertAccount(au.getOpenid(), json.get("allNasid") + "", json.get("allMac") + "", json.get("allIp") + "", tsmp, 0, json.get("solarsys") + "",
							json.get("userIp") + "", apMac, authMap.get("ssid") + "");
				}
				// 获取场所放行url
				String httpurl = SetSystemProperty.propertyName("url");
				json.put("httpurl", httpurl);
				if (json.get("solarsys").equals("h3c")) {
					json.put("userName", au.getOpenid());
					json.put("passWord", au.getOpenkey().substring(0, 16));
					json.put("action", "PORTAL_LOGIN");
					json.put("siteId", json.get("siteid"));
					boolean ress = unitePortalServiceImpl.passH3c(json);
					map1.put("code", "0");
				} else if (json.get("solarsys").equals("moto")) {
					json.put("userName", au.getOpenid());
					json.put("passWord", au.getOpenkey().substring(0, 16));
					json.put("action", "PORTAL_LOGIN");
					json.put("siteId", json.get("siteid"));
					log.error("@@@@@@@@@@@@@@@@@@@@@@@@--moto登录认证开始-------" + json);
					boolean ress = unitePortalServiceImpl.passMoto(json);
					map1.put("code", "0");
				} else {
					PortalUser user = new PortalUser();
					user.setUserName(au.getOpenid());
					user.setPassWord("111111");
					map1 = unitePortalServiceImpl.getPassUrl(json, user, tsmp);
					map1.put("code", "0");
				}
				// 统计认证成功人数uv
				unitePortalServiceImpl.addAuthUserUv(FileSystemProperty.propertyName("appid"), au.getOpenid());
			} else {
				map1.put("code", "-1");
				map1.put("msg", callBackMap.get("msg"));
			}
		} catch (Exception e) {
			authMap = new HashMap<String, Object>();
			authMap.put("code", "1");
			authMap.put("msg", "请求checkToken发生错误");
			log.error("请求app验证错误===" + e);
			return Json.toJson(map1);
		}
		log.error("最终回调结果=====" + Json.toJson(map1));
		return Json.toJson(map1);
	}

	/**
	 * 微信连WiFi临时放行
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年4月27日 下午5:04:27
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@At("/wxTemporaryPass")
	@Ok("raw:json")
	public String wxTemporaryPass(String param) {
		String mesage = StringEscapeUtils.unescapeJava(param);
		Map<String, Object> json = (Map<String, Object>) JSONSerializer.toJSON(mesage);
		String userName = FileSystemProperty.propertyName("publicWxAccount");
		String passWord = "111111";
		long tsmp = System.currentTimeMillis();
		if (json.get("solarsys").equals("h3c")) {
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@--h3c临时放行登录认证开始-------" + json);
			json.put("userName", userName);
			json.put("passWord", passWord);
			json.put("action", "PORTAL_LOGIN");
			boolean ress = unitePortalServiceImpl.passH3c(json);
			map1.put("code", 200);
		} else if (json.get("solarsys").equals("moto")) {
			json.put("userName", userName);
			json.put("passWord", passWord);
			json.put("action", "PORTAL_LOGIN");
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@--moto临时放行登录认证开始-------" + json);
			boolean ress = unitePortalServiceImpl.passMoto(json);
			map1.put("result", 200);
		} else {
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@--" + json.get("solarsys") + "临时放行登录认证开始-------" + json);
			PortalUser user = new PortalUser();
			user.setUserName(userName);
			user.setPassWord(passWord);
			map1 = unitePortalServiceImpl.getPassUrl(json, user, tsmp);
			map1.put("result", 100);
		}
		log.error("临时放行登录认证结果-------" + map1);
		return Json.toJson(map1);
	}

	/**
	 * ios用户下载时，使用公共账号放行
	 * 
	 * @return
	 * @throws Exception
	 */
	@At("/toPass")
	@Ok("raw:json")
	public String toPass(String param) throws Exception {
		log.error("========================进入toPass===" + param);
		String mesage = StringEscapeUtils.unescapeJava(param);
		Map<String, Object> json = (Map<String, Object>) JSONSerializer.toJSON(mesage);
		String userName = FileSystemProperty.propertyName("publicAccount");
		String passWord = "111111";
		long tsmp = System.currentTimeMillis();
		if (json.get("solarsys").equals("h3c")) {
			json.put("userName", userName);
			json.put("passWord", passWord);
			json.put("action", "PORTAL_LOGIN");
			boolean ress = unitePortalServiceImpl.passH3c(json);
			map1.put("code", 0);
		} else if (json.get("solarsys").equals("moto")) {
			json.put("userName", userName);
			json.put("passWord", passWord);
			json.put("action", "PORTAL_LOGIN");
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@--moto临时放行登录认证开始-------" + json);
			boolean ress = unitePortalServiceImpl.passMoto(json);
			map1.put("result", 0);
		} else {
			PortalUser user = new PortalUser();
			user.setUserName(userName);
			user.setPassWord(passWord);
			map1 = unitePortalServiceImpl.getPassUrl(json, user, tsmp);
			map1.put("result", 0);
		}
		log.error("========================toPass===" + map1);
		return Json.toJson(map1);
	}

	/**
	 * 校验用户该次登陆账号是否和之前登陆该账号的终端mac一致，若一致 则直接认证放行 ，不一致则需用户发送验证码上网
	 * 
	 * @return
	 */
	@At("/checkTelMac")
	@Ok("raw:json")
	public String checkTelMac(String userName, String mac) {
		Map<String, Object> map = new HashMap<String, Object>();
		PortalUser user = unitePortalServiceImpl.getUserByName(userName);
		map.put("code", 201);
		if (user == null)
			return Json.toJson(map1);
		if (!user.getClientMac().equals(mac))
			return Json.toJson(map1);
		map.put("code", 200);
		return Json.toJson(map1);
	}

	/**
	 * 去ios页面下载
	 * 
	 * @param params
	 * @return
	 */
	@At("/goIosDownLoad")
	@Ok("re:jsp:error/error")
	public String goIosDownLoad(String param, ViewModel model) {
		String mesage = StringEscapeUtils.unescapeJava(param);
		Map<String, Object> passMap = (Map<String, Object>) JSONSerializer.toJSON(mesage);
		// unitePortalServiceImpl.countAppUv(passMap.get("allMac")+"",
		// SetSystemProperty2.propertyName("appid"));
		model.addv("param", Json.toJson(passMap));
		model.addv("url", FileSystemProperty.propertyName("ios_download_url"));
		return "jsp:jsp/iosdown";
	}

	/**
	 * 统计按钮点击次数
	 * 
	 * @param phoneType
	 * @return
	 */
	@At("/openAppCpc")
	@Ok("raw:json")
	public String openAppCpc(String phoneType, HttpServletRequest request, String usermac) {
		String terminalDevice = request.getHeader("User-Agent");
		// //由于开始不知道用户终端是否安装app
		// 只能在用户点击打开app按钮时统计点击次数，此时增加一次，若用户跳转到下载时，直接减去刚才增加的一次
		// unitePortalServiceImpl.countAppCpc(phoneType);
		int isIos = unifiedEntranceUtil.getAndroidOrIos(terminalDevice);// 判断设备为ios或android
		unitePortalServiceImpl.addAndroidAndIosButtonPv(isIos, FileSystemProperty.propertyName("appid"));// 按钮pv统计
		unitePortalServiceImpl.addAndroidAndIosButtonUv(isIos, FileSystemProperty.propertyName("appid"), usermac);// 按钮uv统计
		return Json.toJson(map1);
	}

	/**
	 * App请求临时放行剩余时间接口
	 * 
	 * @return 用户ip : wifiip 热点名字： ssid APMAC标识： bssid
	 */
	@At("/temporaryRelease")
	@Ok("raw:json")
	public String temporaryRelease(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String wifiip = request.getParameter("wifiip");
		String ssid = request.getParameter("ssid");
		// String bssid = request.getParameter("bssid");
		// 是否为null
		if (wifiip == null || ssid == null) {
			map.put("code", "1");
			map.put("msg", "缺失参数");
			Json.toJson(map);
		}
		String userName = FileSystemProperty.propertyName("publicAccount");
		// 根据mac,ip,公众账号获取上线时间
		long surplusTiem = unitePortalServiceImpl.getTemporaryRelease(wifiip, ssid, userName);
		if (surplusTiem == -1) {
			map.put("code", "-1");
			map.put("msg", "已经认证");
			map.put("surplusTiem", 0);
			log.error("临时放行时间-------" + map);
			return Json.toJson(map);
		} else if (surplusTiem > 0) {
			map.put("surplusTiem", surplusTiem);
			map.put("msg", "获取成功");
			map.put("code", "0");
			log.error("临时放行时间-------" + map);
			return Json.toJson(map);
		} else {
			map.put("code", "-1");
			map.put("msg", "获取失败");
			map.put("surplusTiem", 0);
			log.error("临时放行时间-------" + map);
			return Json.toJson(map);
		}
	}

	/**
	 * 硕士调用接口进行添加操作
	 * 
	 * @return
	 *
	 */
	@At("/addWanLanMac")
	@Ok("raw:json")
	public void addWanLanMac(HttpServletRequest request, HttpServletResponse response) {
		// log.error("---开始请求----"+request.getAttributeNames());
		Map<String, Object> map = new HashMap<String, Object>();
		String lasapMac = request.getParameter("lsapMAC");
		String wifiMac = request.getParameter("wifiMac");
		// log.error("lsapMac----1495---"+lasapMac);
		// log.error("wifiMac---1495----"+wifiMac);
		WanLanMac wanLanMac = new WanLanMac();
		wanLanMac.setLasapMac(lasapMac);
		wanLanMac.setWifiMac(wifiMac);
		WanLanMac lsapMac = unitePortalServiceImpl.getLsapMac(lasapMac);
		if (lsapMac != null) {
			// 更新操作
			wanLanMac.setId(lsapMac.getId());
			unitePortalServiceImpl.updateWanLanMac(wanLanMac);
			// log.error("-------更新操作-------");
		} else {
			// 添加操作
			unitePortalServiceImpl.insertWanLanMac(wanLanMac);
			// log.error("-------添加操作-------");
		}
	}

	@At("/test")
	@Ok("raw:json")
	public String test(String ip, String basip) {
		// String mesage=StringEscapeUtils.unescapeJava(as);
		// Map<String,Object> json = (Map<String,Object>)
		// JSONSerializer.toJSON(mesage);
		// AppAuthParam
		// aap=unitePortalServiceImpl.getAppAuthParam(json.get("allMac")+"");
		// if(aap!=null){
		// map1.put("code", 0);
		// }else{
		// map1.put("code", 1);
		// }
		// String res="";
		// try {
		// res = HttpService.doGet("http://www.qq.com");
		// Map callBackMap = JsonUtil.fromJson(res, HashMap.class);
		// System.out.println(callBackMap);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// log.error(res);
		Map<String, Object> map = new HashMap<String, Object>();
		String wifiip = request.getParameter("wifiip");
		String ssid = request.getParameter("ssid");
		String bssid = request.getParameter("bssid");
		// StringBuilder sb = new StringBuilder();
		// try {
		// BufferedReader reader = request.getReader();
		// char[]buff = new char[1024];
		// int len;
		// while((len = reader.read(buff)) != -1) {
		//
		// sb.append(buff,0, len);
		// }
		// } catch (IOException e) {
		// map.put("code","1");
		// map.put("msg","格式不正确"+e);
		// return Json.toJson(map);
		// }
		// Map<String,Object> dataMap= (Map<String,Object>)
		// JSONSerializer.toJSON(sb.toString());
		// String basip=dataMap.get("basip")+"";
		// String ip=dataMap.get("ip")+"";
		// 是否为null
		if (wifiip == null || ssid == null || bssid == null) {
			map.put("code", "1");
			map.put("msg", "缺失参数");
			Json.toJson(map);
		}
		String userName = FileSystemProperty.propertyName("publicAccount");
		// 根据mac,ip,公众账号获取上线时间
		long surplusTiem = unitePortalServiceImpl.getTemporaryRelease(wifiip, ssid, userName);
		if (surplusTiem < 1) {
			map.put("code", "-1");
			map.put("msg", "获取失败");
			map.put("surplusTiem", "0");
		} else {
			map.put("surplusTiem", surplusTiem + "");
			map.put("msg", "获取成功");
			map.put("code", "0");
		}
		log.error("临时放行时间-------" + map);
		return Json.toJson(map);

	}

	/**
	 * 平安用户下线
	 * 
	 * @return
	 */
	@At("/doOffLine")
	@Ok("raw:json")
	public String doOffLine(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> m = offlinePingAn(request, response);
		return Json.toJson(m);
	}

	/**
	 * 逻辑处理 1、请求信息校验 2、获取参数类型，获取系统类型以及场所的详细信息
	 * 
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public synchronized Map<String, Object> LogicalProcessing(HttpServletRequest request, HttpServletResponse response, String state) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 获取请求中的参数
			String key = request.getQueryString();
			log.error("-----------===key================---------" + key);
			if (key == null) {
				map2.put("101", "未检测到系统参数");
				return map2;
			}
			if (key.contains("loginurl")) {
				key = key.substring(key.indexOf("?") + 1);
				log.error("优联回调===筛选参数===" + key);
			}
			String[] arrayLit = key.split("&");
			Map<String, Object> mapVal = null;
			Map<String, String> map = new HashMap<String, String>();
			String params = "";
			for (String string : arrayLit) {
				String[] paramList = string.split("=");
				if (paramList.length > 1) {
					// 该判断是为优联app的傻逼设计做的处理
					if (!paramList[0].equals("openid") && !paramList[0]
							.equals("openkey")/* &&!paramList[0].equals("ip") */) {
						params += paramList[0] + "&";
					}
				}
			}
			List<List<String>> li = unitePortalServiceImpl.getSystemSolarsysType();
			List<String> listDate = new ArrayList<String>();
			listDate.add(sdf.format(new Date((new Date().getTime() + 30 * 24 * 60 * 60 * 1000L))));
			li.add(listDate);
			int accordLen = 0;
			String experTime = li.get(li.size() - 1).get(0);
			if (sdf.parse(experTime).getTime() - new Date().getTime() <= 5000) {
				li = unitePortalServiceImpl.getSystemSolarsysType();
				if (li == null || li.size() == 0) {
					map2.put("101", "未检测到认证类型,请联系客服人员");
					return map2;
				}
				// 在缓存value中加入到期时间
				listDate.add(sdf.format(new Date((new Date().getTime() + 30 * 24 * 60 * 60 * 1000L))));
				li.add(listDate);
			}

			log.error("-==========begain check system type================---------");
			// 三重循环
			/* 校验是那种系统 */
			List<String> list = null;
			List<String> listOne = null;
			for (int i = 0; i < li.size(); i++) {
				listOne = li.get(i);
				accordLen = 0;
				/* 循环遍历匹配第一次弹出页面的url */
				for (int j = 0; j < listOne.get(0).split("&").length; j++) {
					for (int x = 0; x < params.split("&").length; x++) {
						if (listOne.get(0).split("&")[j].equals(params.split("&")[x])) {
							accordLen = accordLen + 1;
							break;
						}
					}
				}
				if (accordLen == listOne.get(0).split("&").length) {
					list = listOne;
					map1.put("solarsys", listOne.get(1));
					break;
				}
			}
			log.error("-==========end check system type================-----list----" + list.toString());

			// 如果为空则认为非法url请求
			if (list == null || list.size() == 0) {
				return null;
			}
			String result = list.get(6)!=null?list.get(6).indexOf(",")>-1?request.getParameter(list.get(6).split(",")[0]):null:null;
			String callResut = list.get(6)!=null?list.get(6).indexOf(",")>-1?request.getParameter(list.get(6).split(",")[1]):null:null;

			log.error("-==========begain check url is auth or callback================----" + result + "-----" + callResut);

			// 校验url是回调还是认证,wifidog不具备此特性
			if (callResut != null && callResut.length() > 3) {
				return null;
			}
			//wifidog不具备此特性
			if (result != null) {
				if (list.get(5).split(",").length > 1) {
					if (result.equals(list.get(5).split(",")[0]) && result.equals(list.get(5).split(",")[1])) {
						return null;
					}
				} else {
					if (result.equals(list.get(5).split(",")[0])) {
						return null;
					}
				}
			}
			/*** 校验结束 ****/
			log.error("-==========end check url is auth or callback================---------");

			if (list.get(2).equals("0")) {// 为0视为明文系统， 1视为密文系统
				log.error("-==========enturn auth================---------");
				log.error("============list===================" + list);
				log.error("=======1=====map1===================" + map1);
				for (int i = 0; i < list.get(0).split("&").length; i++) {
					map1.put(list.get(0).split("&")[i], request.getParameter(list.get(0).split("&")[i]));
				}
				log.error("=======2=====map1===================" + map1);

				String nasid = map1.get(list.get(4).split(",")[0]) + "";// wlanacname 4019.0755.200.00
				String mac = map1.get(list.get(4).split(",")[1]) + "";
				String ip = map1.get(list.get(4).split(",")[2]) + "";
				String userIp = map1.get(list.get(4).split(",")[3]) + "";
				if ("h3c".equals(map1.get("solarsys"))) {
					nasid = map1.get(list.get(4).split(",")[0]) + "";
					mac = map1.get(list.get(4).split(",")[1]) + "";
					ip = map1.get(list.get(4).split(",")[2]) + "";
					userIp = map1.get(list.get(4).split(",")[3]) + "";
				} else if ("moto".equals(map1.get("solarsys"))) {
					// MOTO设备推送参数单独处理
					// if (nasid.indexOf("0755.200.00") > 0) {
					String str = map1.get(list.get(4).split(",")[0]) + "";
					nasid = str.substring(0, 4) + "075520000460";
					mac = map1.get(list.get(4).split(",")[3]) + "";
					ip = map1.get(list.get(4).split(",")[2]) + "";
					userIp = map1.get(list.get(4).split(",")[1]) + "";
					// }
				} else if ("dunchong".equals(map1.get("solarsys"))) {
					nasid = map1.get(list.get(4).split(",")[2]) + "";
					mac = map1.get(list.get(4).split(",")[5]) + "";
					ip = map1.get(list.get(4).split(",")[7]) + "";
					userIp = map1.get(list.get(4).split(",")[4]) + "";
				}else if ("wifidog".equals(map1.get("solarsys"))) {
					nasid = map1.get(list.get(4).split(",")[2]) + "";
					mac = map1.get(list.get(4).split(",")[4]) + "";
					ip = map1.get(list.get(4).split(",")[0]) + "";
					userIp = map1.get(list.get(4).split(",")[3]) + "";
				}

				/**
				 * 把nasid和ssid存入数据库中 chenquan 2017.10.09
				 */
				if (nasid != null) {
					// 从请求的URL中获取ssid
					String ssid = request.getParameter("ssid");
					// 判断ssid是否存在，
					if (ssid != null && !ssid.equals("")) {
						PortalId portalid = new PortalId();
						portalid.setSsid(ssid);
						portalid.setNasid(nasid);
						// 执行插入操作
						// 通过nasid和ssid查询数据库，如果没有查询到则存入数据库。
						List<PortalId> listid = unitePortalServiceImpl.selSsidAndNasid(nasid, ssid);
						if (listid.isEmpty()) {
							unitePortalServiceImpl.updateSSID(portalid);
						}
					}
				}
				CloudSite site = unitePortalServiceImpl.getCloudSite(nasid);
				if (site == null) {
					map2.put("101", "未知的nasid:" + nasid + ",请联系管理员");
					return map2;
				}
				unitePortalServiceImpl.insertSiteCache(nasid, site);
				log.error("============11===================");
				mapVal = new HashMap<String, Object>();
				if (state.equals("0")) {// 1代表是切换账号
					PortalUser user = unitePortalServiceImpl.getUserByMac(mac);
					if (user != null) {
						unitePortalServiceImpl.insertUserCache(mac, user);
						mapVal.put("user", user);
					}else{
						String mac1 = null;
						if(mac.indexOf(":") > 0){
							mac1 = mac.replaceAll(":", "-");
						}else if(mac.indexOf("-") > 0){
							mac1 = mac.replaceAll("-", ":");
						}
						user = unitePortalServiceImpl.getUserByMac(mac1);
						if (user != null) {
							unitePortalServiceImpl.insertUserCache(mac, user);
							mapVal.put("user", user);
						}
					}

					/* 处理app请求时不能触发无感知 */
					if (nasid != null) {
						String ssid = request.getParameter("ssid");
						List<NewPortalId> listid = unitePortalServiceImpl.getNewPortalIdBySsid(ssid, nasid);
						if (listid.isEmpty() || listid.get(0).getUserType() != 7) {// 7 为app认证

							/**
							 * 新加入无感知认证测试 Portal支持基于Mac地址的快速认证
							 * 
							 * 处理方案理由：1、由于Portal认证我们收集到的用户信息太少，很难辨别用户的真实性，
							 * 所以做快速认证必然会存在安全隐患。 2、为满足客户需求，我们尽量在安全角度多考虑。
							 * 3、经过多次认证记录抽样观察，发现单个终端MAC地址在同一个nasid下，终端IP基本是一样的。
							 * 4、校验终端mac、终端ip、AC设备nasid三个参数，若同时在认证记录中匹配得到记录，
							 * 则可以通过基于MAC地址快速认证，否则进行常规Portal认证。
							 * 
							 * 
							 * 校验当前终端MAC、终端IP、AC设备Nas-id是否存在认证记录
							 * 若存在，则直接走与AC的CHAP认证请求;否则跳过。
							 */
							RoutersAuthRetance authRetance = unitePortalServiceImpl.findRoutersAuthRetance(nasid);
							int authType = 0;
							// 无感知认证周期时间，若为0时无周期限制（单位：小时）
							int cycle_time = 0;
							if (authRetance != null) {
								authType = authRetance.getAuthTypeId();
								cycle_time = authRetance.getCycleTime();
							}

							log.error("--------12-----=========-------" + mac + "-----" + userIp + "-----" + nasid);
							List<AuthUser> authUserlist = unitePortalServiceImpl.selAccountByUmac_Uip_Nasid_Hour(mac, userIp, nasid, cycle_time);
							log.error("--------12-0----====authUserlist=====-------" + authUserlist != null ? authUserlist.toString() : "null");
							String userName = null;
							if (authUserlist != null && authUserlist.size() > 0) {
								userName = authUserlist.get(0).getUserName();
								log.error("--------12-1----====userName=====-------" + userName);
								user = unitePortalServiceImpl.getUserByName(userName);
								log.error("--------12-1----====user=====-------" + user);
							} else {
								user = unitePortalServiceImpl.getUserByMac(mac);
								if (user != null) {
									log.error("--------12-2-1---====user=====-------" + user.toString());
								}else{
									String mac1 = null;
									if(mac.indexOf(":") > 0){
										mac1 = mac.replaceAll(":", "-");
									}else if(mac.indexOf("-") > 0){
										mac1 = mac.replaceAll("-", ":");
									}
									user = unitePortalServiceImpl.getUserByMac(mac1);
									if (user != null) {
										log.error("--------12-2-2---====user=====-------" + user.toString());
									}else{
										log.error("--------12-2----====user=====-------null");										
									}
								}
							}

							mapVal.put("user", user);

							log.error("============13===================" + mapVal);
							if (site != null && authUserlist != null && authUserlist.size() > 0 && user != null && authType == 8) {
								log.error("============14===================" + authRetance.toString());
								log.error("-------------=====authUser======-----------" + authUserlist.get(0).toString());
								log.error("-------------====site=======-----------" + site.toString());
								Map<String, Object> datamap = new HashMap<String, Object>();
								datamap.put("nasid", nasid);
								datamap.put("action", "PORTAL_LOGIN");
								// String userName = authUser.getUserName();
								String password = "111111";
								datamap.put("userName", userName);
								PortalUser proUser = unitePortalServiceImpl.getUserByName(userName);
								boolean flag = unitePortalServiceImpl.updateUserPwd(proUser, userName, password, site.getId(), 1);
								if (flag) {
									proUser = unitePortalServiceImpl.getUserByNameAndWord(userName, password, 1);
								}
								CloudSitePortalEntity csp = unitePortalServiceImpl.getSiteRetance(proUser.getId(), site.getId());
								if (csp == null) {
									unitePortalServiceImpl.userSiteRetance(proUser.getId(), site.getId());
								}

								// }else{
								// map1.put("msg", "网络异常");
								// map1.put("result", 300);
								// }
								datamap.put("passWord", proUser.getPassWord().substring(0, 16));
								// datamap.put("passWord", password);
								datamap.put("userip", userIp);
								datamap.put("basip", authUserlist.get(0).getIp());
								StringBuilder sb = new StringBuilder(nasid);
								sb.insert(11, ".");
								sb.insert(8, ".");
								sb.insert(4, ".");
								String basname = sb.substring(0, sb.length() - 3);
								datamap.put("basname", basname);
								datamap.put("siteId", site.getId());
								datamap.put("key", key);
								// 认证状态 0为正常认证求情,1为临时放行请求,2为无感知认证放行
								int authStatus = 2;
								// 为回调做处理
								long tsmp = System.currentTimeMillis();
								// 保存认证记录
								String apmac = (String) request.getSession().getAttribute("apmac");
								ssid = (String) request.getSession().getAttribute("ssid");
								unitePortalServiceImpl.insertAccount(userName, nasid, mac, authUserlist.get(0).getIp(), tsmp, authStatus, map1.get("solarsys") + "", userIp, apmac, ssid);
								// 获取场所放行url
								StringBuffer url = request.getRequestURL();
								log.error("-------------====url=======-----------" + url);
								String httpurl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
								log.error("-------------====httpurl=======-----------" + httpurl);
								datamap.put("httpurl", httpurl);
								if (map1.get("solarsys").equals("h3c")) {
									boolean ress = false;
									// if(authStatus!=2){
									log.error("----passH3c---datamap------=========-------" + datamap);
									ress = unitePortalServiceImpl.passH3c(datamap);
									// }
									log.error("-------ress------=========-------" + ress);
									// 新增
									// if(ress){
									// saveRZX(apmac, mac, userName);
									// }
									if (/* ress */ true) {
										String reUrl = unitePortalServiceImpl.h3cUrl(datamap);
										mapVal.put("result", 200);
										mapVal.put("url", reUrl);
									}
									mapVal.put("auth", "authentication");
									log.error("-------212------=========-------" + mapVal);
									map1.put("allMac", mac);
									map1.put("allNasid", nasid);
									map1.put("allIp", ip);
									map1.put("userIp", userIp);
									if (site.getBannerUrl() != null && !"".equals(site.getBannerUrl())) {
										site.setBannerUrl(site.getBannerUrl().replace(",", ":"));
									} else {
										site.setBannerUrl("");
									}
									mapVal.put("site", site);
									mapVal.put("type", map1);
									return mapVal;
								} else if (map1.get("solarsys").equals("moto")) {
									boolean ress = false;
									// if(authStatus!=2){
									ress = unitePortalServiceImpl.passMoto(datamap);
									// }
									log.error("-------ress------=========-------" + ress);

									// 新增
									// if(ress){
									// saveRZX(apmac, mac, userName);
									// }
									if (/* ress */ true) {
										String reUrl = unitePortalServiceImpl.motoUrl(datamap);
										mapVal.put("result", 200);
										mapVal.put("url", reUrl);
									}
									mapVal.put("auth", "authentication");
									log.error("-------222------=========-------" + mapVal);
									return mapVal;
								}
							}
						}
//						if(user != null){//针对tplink设备做无感知认证
//							if(map1.get("solarsys").equals("wifidog")) {
//								//拼接重定向url
//								String params1 = "user="+user.getUserName()+"&pwd="+user.getPassWord().substring(0, 16)+"&ip="+map1.get("userIp")
//												+"&mac="+map1.get("mac")+"&authtype=web&gw_address="+map1.get("gw_address")
//												+"&gw_port="+map1.get("gw_port")+"&gw_id="+map1.get("gw_id")+"&url="+map1.get("url");
//								// 判断场所是否为收费模式
//								if (site.getStauts() == 900) {
//									String useDateIsExpire = useDateIsExpire(user.getUserName(), site.getId());
//									JSONObject fromObject = JSONObject.fromObject(useDateIsExpire);
//									// 使用时间未到期
//									if (fromObject.getInt("result") == 200) {
//										String redirectUrl="http://"+map1.get("gw_address")+":8080/wifidog/logincheck/?"+params1;
//										map1.put("allMac", mac);
//										map1.put("allNasid", nasid);
//										map1.put("allIp", ip);
//										map1.put("userIp", userIp);
//										if (site.getBannerUrl() != null && !"".equals(site.getBannerUrl())) {
//											site.setBannerUrl(site.getBannerUrl().replace(",", ":"));
//										} else {
//											site.setBannerUrl("");
//										}
//										mapVal.put("site", site);
//										mapVal.put("type", map1);
//										mapVal.put("result", 2001);//快速认证，还需页面跳转执行url
//										mapVal.put("url", redirectUrl);
//										mapVal.put("auth", "authentication");
//										log.error("-------212------=========-------" + mapVal);
//										return mapVal;
//									}
//								}
//							}
//						}
					}
				}

				map1.put("allMac", mac);
				map1.put("allNasid", nasid);
				map1.put("allIp", ip);
				map1.put("userIp", userIp);
				if (site.getBannerUrl() != null && !"".equals(site.getBannerUrl())) {
					site.setBannerUrl(site.getBannerUrl().replace(",", ":"));
				} else {
					site.setBannerUrl("");
				}
				mapVal.put("site", site);
				mapVal.put("type", map1);
				String terminalDevice = request.getHeader("User-Agent");
				// 添加弹portal日志
				if (map1.get("solarsys").equals("chilli")) {
					PortalLog portal_log = new PortalLog();
					portal_log.setClientIp(request.getParameter("ip"));
					portal_log.setClientMac(request.getParameter("mac"));
					portal_log.setNasid(request.getParameter("nasid"));
					portal_log.setRequestTime(CalendarUtil.currentTime());
					portal_log.setRouterIp(request.getParameter("uamip"));
					portal_log.setRouterMac(request.getParameter("called"));
					portal_log.setRouterType("coovachilli");
					portal_log.setTerminalDevice(terminalDevice);
					portal_log.setUrl(request.getParameter("userurl"));
					unitePortalServiceImpl.inserPortalLog(portal_log);
				} else if (map1.get("solarsys").equals("ros")) {
					PortalLog portal_log = new PortalLog();
					portal_log.setClientIp(request.getParameter("ip"));
					portal_log.setClientMac(request.getParameter("mac"));
					portal_log.setNasid(request.getParameter("nasid"));
					portal_log.setRequestTime(CalendarUtil.currentTime());
					portal_log.setRouterIp(request.getParameter("uamip"));
					portal_log.setRouterMac(request.getParameter("called"));
					portal_log.setRouterType("ros");
					portal_log.setTerminalDevice(terminalDevice);
					portal_log.setUrl(request.getParameter("userurl"));
					unitePortalServiceImpl.inserPortalLog(portal_log);
				} else if (map1.get("solarsys").equals("h3c")) {
					PortalLog portal_log = new PortalLog();
					portal_log.setClientIp(request.getParameter("userip"));
					portal_log.setClientMac(request.getParameter("usermac"));
					portal_log.setNasid(request.getParameter("nasid"));
					portal_log.setRequestTime(CalendarUtil.currentTime());
					portal_log.setRouterIp(request.getParameter("basip"));
					portal_log.setRouterMac(request.getParameter("apmac"));
					portal_log.setRouterType("h3c");
					portal_log.setTerminalDevice(terminalDevice);
					portal_log.setUrl(request.getParameter("userurl"));
					unitePortalServiceImpl.inserPortalLog(portal_log);
				} else if (map1.get("solarsys").equals("moto")) {
					log.error("=============12==================");
					PortalLog portal_log = new PortalLog();
					portal_log.setClientIp(request.getParameter("wlanuserip"));
					portal_log.setClientMac(request.getParameter("wlanusermac"));
					portal_log.setNasid(request.getParameter("wlanacname").substring(0, 4) + "075520000460");
					portal_log.setRequestTime(CalendarUtil.currentTime());
					portal_log.setRouterIp(request.getParameter("wlanacip"));
					portal_log.setRouterType("moto");
					portal_log.setTerminalDevice(terminalDevice);
					portal_log.setUrl(request.getParameter("userurl"));
					unitePortalServiceImpl.inserPortalLog(portal_log);
				} else if (map1.get("solarsys").equals("dunchong")) {
					log.error("=============12==================");
					PortalLog portal_log = new PortalLog();
					portal_log.setClientIp(request.getParameter("userip"));
					portal_log.setClientMac(request.getParameter("usermac"));
					portal_log.setNasid(request.getParameter("nasid"));
					portal_log.setRequestTime(CalendarUtil.currentTime());
					portal_log.setRouterIp(request.getParameter("wanlacip"));
					portal_log.setRouterMac(request.getParameter("apmac"));
					portal_log.setRouterType("dunchong");
					portal_log.setTerminalDevice(terminalDevice);
					portal_log.setUrl(request.getParameter("url"));
					unitePortalServiceImpl.inserPortalLog(portal_log);
				}else if (map1.get("solarsys").equals("wifidog")) {
					log.error("=============12==================");
					PortalLog portal_log = new PortalLog();
					portal_log.setClientIp(request.getParameter("ip"));
					portal_log.setClientMac(request.getParameter("mac"));
					portal_log.setNasid(request.getParameter("gw_id"));
					portal_log.setRequestTime(CalendarUtil.currentTime());
					portal_log.setRouterIp(request.getParameter("gw_address"));
					portal_log.setRouterMac("");
					portal_log.setRouterType("wifidog");
					portal_log.setTerminalDevice(terminalDevice);
					portal_log.setUrl(request.getParameter("url"));
					unitePortalServiceImpl.inserPortalLog(portal_log);
					request.getSession().setAttribute("gw_port", request.getParameter("gw_port"));
				}
				return mapVal;
			} else if (list.get(2).equals("1") && list.get(1).equals("ikuai")) {// ikuai特殊认证

				log.error("-==========enturn ikuai auth================---------");
				String ikuaiParam = "";
				for (int i = 0; i < list.get(0).split("&").length; i++) {
					if (list.get(0).split("&")[i].equals("ikenc")) {
						ikuaiParam = request.getParameter(list.get(0).split("&")[i]);
						break;
					}
				}
				String[] status = ikuaiParam.split("-");
				if (status.length == 1) {
					map1 = ikuai_login(request, response, map1);
					String nasid = map1.get(list.get(4).split(",")[0]) + "";
					String mac = map1.get(list.get(4).split(",")[1]) + "";
					CloudSite site = unitePortalServiceImpl.getSite(nasid);
					if (site == null) {
						map2.put("101", "未知的nasid:" + nasid + ",请联系管理员");
						return map2;
					}
					unitePortalServiceImpl.insertSiteCache(nasid, site);
					mapVal = new HashMap<String, Object>();
					if (state.equals("0")) {// 1代表切换账号
						PortalUser user = unitePortalServiceImpl.getUserByMac(mac);
						if (user != null) {
							unitePortalServiceImpl.insertUserCache(mac, user);
							mapVal.put("user", user);
						} else {
							log.error("********user is null************");
						}
					}
					if (site.getBannerUrl() != null && !"".equals(site.getBannerUrl())) {
						site.setBannerUrl(site.getBannerUrl().replace(",", ":"));
					} else {
						site.setBannerUrl("");
					}
					map1.put("allMac", mac);
					map1.put("allNasid", nasid);
					mapVal.put("site", site);
					mapVal.put("type", map1);
					return mapVal;
				} else {
					map2.put("101", "ikuai认证返回参数错误");
					return map2;
				}
			}
			map2.put("101", "未知的认证返回参数");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("********Exception************" + e.getMessage());
			return null;
		}
		return map2;
	}

	/**
	 * 获取系统参数回调类型
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 下午5:44:19
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public synchronized Map<String, Object> getCallBckDeviceType(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String isSwitch = request.getParameter("state");
			String key = request.getQueryString();
			log.error("=======getCallBckDeviceType===============" + key);
			if (key == null) {
				map2.put("101", "未检测到系统参数");
				return map2;
			}
			String[] arrayLit = key.split("&");
			Map<String, Object> mapVal = null;
			Map<String, String> map = new HashMap<String, String>();
			String params = "";
			for (String string : arrayLit) {
				String[] paramList = string.split("=");
				if (paramList.length > 1) {
					params += paramList[0] + "&";
				}
			}
			log.error("=========params=============" + params);
			List<String> list = null;
			List<String> listDate = new ArrayList<String>();
			int accordLen = 0;

			List<List<String>> li = unitePortalServiceImpl.getCallBackSystemSolarsysType();
			if (li == null) {
				map2.put("101", "未检测到认证类型,请联系飞讯无限客服人员");
				return map2;
			}
			listDate.add(sdf.format(new Date((new Date().getTime() + 30 * 24 * 60 * 60 * 1000L))));
			li.add(listDate);

			log.error("=========begain check callback system params============");
			/* 校验回调系统以及参数 */
			for (int i = 0; i < li.size(); i++) {
				List<String> listOne = li.get(i);
				log.error(li.get(i));
				accordLen = 0;
				/* 循环遍历匹配radius server 响应 url */
				for (int j = 0; j < listOne.get(0).split("&").length; j++) {
					for (int x = 0; x < params.split("&").length; x++) {
						if (listOne.get(0).split("&")[j].equals(params.split("&")[x])) {
							accordLen = accordLen + 1;
							break;
						}
					}
				}
				if ((accordLen == listOne.get(0).split("&").length || accordLen == listOne.get(0).split("&").length - 1) && listOne.size() > 1) {
					list = listOne;
					log.error(listOne.size() + "====" + listOne);
					map1.put("solarsys", listOne.get(2));
					break;
				}
			}
			// 如果为空则认为破解url
			if (list == null || list.size() == 0) {
				map2.put("101", "与系统参数不符,请联系管理员");
				return map2;
			}

			/* 校验结束 */

			log.error("=========end check callback system params============");
			CloudSite site = (CloudSite) request.getSession().getAttribute("site");
			mapVal = new HashMap<String, Object>();
			PortalUser user = null;
			if (list.get(1).equals("0")) {// 为0视为明文系统， 1视为密文系统
				for (int i = 0; i < list.get(0).split("&").length; i++) {
					map1.put(list.get(0).split("&")[i], request.getParameter(list.get(0).split("&")[i]));
				}
				// if("h3c".equals(map1.get("solarsys"))){
				// map1.put("basname","5068075520000460");
				// }
				String nasid = map1.get(list.get(3).split(",")[0]) + "";
				String mac = map1.get(list.get(3).split(",")[1]) + "";
				String userip = map1.get(list.get(3).split(",")[3]) + "";
				// //MOTO设备推送参数单独处理
				if (nasid.indexOf("0755.200.00") > 0) {
					String str = map1.get(list.get(3).split(",")[0]) + "";
					nasid = str.substring(0, 4) + "075520000460";
					mac = map1.get(list.get(3).split(",")[3]) + "";
				}

				if ("h3c".equals(map1.get("solarsys"))) {
					nasid = map1.get(list.get(3).split(",")[0]) + "";
					mac = map1.get(list.get(3).split(",")[1]) + "";
					userip = map1.get(list.get(3).split(",")[3]) + "";
				} else if ("moto".equals(map1.get("solarsys"))) {
					// MOTO设备推送参数单独处理
					// if (nasid.indexOf("0755.200.00") > 0) {
					String str = map1.get(list.get(3).split(",")[0]) + "";
					nasid = str.substring(0, 4) + "075520000460";
					mac = map1.get(list.get(3).split(",")[3]) + "";
					userip = map1.get(list.get(3).split(",")[1]) + "";
					// }
				} else if ("dunchong".equals(map1.get("solarsys"))) {
					nasid = map1.get(list.get(3).split(",")[2]) + "";
					mac = map1.get(list.get(3).split(",")[5]) + "";
					userip = map1.get(list.get(3).split(",")[4]) + "";
				}

				map1.put("allMac", mac);
				map1.put("allNasid", nasid);
				site = unitePortalServiceImpl.getSite(nasid);
				if (site == null) {
					map2.put("101", "未知的nasid:" + nasid + ",请联系管理员");
					return map2;
				}
				unitePortalServiceImpl.insertSiteCache(nasid, site);
				if (site.getBannerUrl() != null && !"".equals(site.getBannerUrl())) {
					site.setBannerUrl(site.getBannerUrl().replace(",", ":"));
				} else {
					site.setBannerUrl("");
				}
				String result = request.getParameter(list.get(5).split(",")[0] + "");

				log.error("=========nasid====mac========" + nasid + "-------" + mac);
				AuthUser au = unitePortalServiceImpl.selAuthUser(null, nasid, mac);
				if (au == null) {
					map2.put("101", "未检测到认证用户");
					return map2;
				}
				String userName = au.getUserName();
				if (userName == null) {
					map2.put("101", "未检测到认证用户");
					return map2;
				}
				log.error("=========result========" + result);
				log.error("=========list========" + list.toString());
				if (result.equals(list.get(4).split(",")[0])) {// 认证成功
					List<PortalUser> listUser = null;
					if (listUser != null) {
						user = listUser.get(0);
					} else {
						user = unitePortalServiceImpl.getUserByName(userName);
						if (user != null) {
							unitePortalServiceImpl.insertUserCache(mac, user);
						} else {
							log.error("****callback********user is null***************");
						}
					}
					mapVal.put("mac", mac);
					mapVal.put("user", user);
					mapVal.put("site", site);
					mapVal.put("type", map1);
					// mapVal.put("callback", "callback");
					mapVal.put("auth", "success");
					if (au.getAuthState() == 1) {
						mapVal.put("code", 202);
					}
					return mapVal;
				} else {
					if (isSwitch == null || !isSwitch.equals("1")) {
						if (user == null) {
							user = unitePortalServiceImpl.getUserByName(userName);
							if (user != null) {
								unitePortalServiceImpl.insertUserCache(mac, user);
							} else {
								log.error("**********user is null****************");
							}
						}
					}
					mapVal = getCode(request.getParameter(list.get(5).split(",")[1]), map1.get(list.get(3).split(",")[0]) + "", request, response);
					mapVal.put("userName", userName);
					mapVal.put("auth", "failed");
					if (user != null) {
						mapVal.put("user", user);
					}
					if (isSwitch != null && isSwitch.equals("1")) {
						mapVal.put("msg", "");
					}
					mapVal.put("site", site);
					mapVal.put("type", map1);
					return mapVal;
				}
			} else if (list.get(1).equals("1") && list.get(2).equals("ikuai")) {
				String reqParam = request.getParameter(list.get(0).split(",")[0].split("&")[0]);
				String authTsmp = reqParam.split("-")[2];
				AuthUser au = unitePortalServiceImpl.selAuthUser(authTsmp);
				if (au == null) {
					map2.put("101", "未检测到认证用户");
					return map2;
				}
				site = unitePortalServiceImpl.getSite(au.getNasid());
				if (site == null) {
					map2.put("101", "未知的nasid:" + au.getNasid() + ",请联系管理员");
					return map2;
				}
				map1.put("allMac", au.getMac());
				map1.put("allNasid", au.getNasid());
				map1.put("allIp", au.getIp());
				if (site.getBannerUrl() != null && !"".equals(site.getBannerUrl())) {
					site.setBannerUrl(site.getBannerUrl().replace(",", ":"));
				} else {
					site.setBannerUrl("");
				}
				String ikuaiParam = request.getParameter(list.get(5).split(",")[0] + "");
				if (ikuaiParam.indexOf(list.get(4)) > 0) {// 认证 成功
					List<PortalUser> listUser = null;
					if (listUser != null) {
						user = listUser.get(0);
					}
					if (user == null) {
						user = unitePortalServiceImpl.getUserByName(au.getUserName());
						if (user == null) {
							map2.put("101", "未查到验证用户信息");
							return map2;
						}
					}
					mapVal.put("mac", au.getMac());
					mapVal.put("user", user);
					mapVal.put("site", site);
					mapVal.put("auth", "success");
					mapVal.put("type", map1);
					if (au.getAuthState() == 1) {
						mapVal.put("code", 202);
					}
					return mapVal;
				} else {// 认证失败
					mapVal = getCode(request.getParameter(list.get(5).split(",")[1]), au.getNasid(), request, response);
					user = unitePortalServiceImpl.getUserByMacAndName(au.getMac(), au.getUserName());
					if (user != null) {
						unitePortalServiceImpl.insertUserCache(au.getMac(), user);
					}
					if (site.getBannerUrl() != null && !"".equals(site.getBannerUrl())) {
						site.setBannerUrl(site.getBannerUrl().replace(",", ":"));
					} else {
						site.setBannerUrl("");
					}
					mapVal.put("userName", au.getUserName());
					mapVal.put("auth", "failed");
					if (user != null)
						mapVal.put("user", user);
					mapVal.put("site", site);
					mapVal.put("type", map1);
					return mapVal;
				}
			}
			map2.put("101", "未知的认证系统参数");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		return map2;
	}

	/**
	 * 处理ikuai设备登录请求
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 下午5:05:18
	 * @param request
	 * @param response
	 * @param ikuaiMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unused")
	public Map<String, Object> ikuai_login(HttpServletRequest request, HttpServletResponse response, Map<String, Object> ikuaiMap) throws UnsupportedEncodingException {
		String ikenc = request.getParameter("ikenc");
		String refer = request.getParameter("refer");
		String ikcs = "";
		String ikweb = "";
		if ((StringUtils.isNotBlank(ikenc)))
			try {
				ikcs = BASE64.decryptBASE64(ikenc);
				if ((StringUtils.isNotBlank(refer))) {
					ikweb = BASE64.decryptBASE64(refer);
				}
			} catch (Exception localException1) {
			}
		String gwid = "";
		String mac = "";
		String user_ip = "";
		String bssid = "";
		String ssid = "";
		String nasname = "";
		String basip = "";
		if (StringUtils.isNotBlank(ikcs)) {
			String[] cs = ikcs.split("&");
			for (String c : cs) {
				if (c.startsWith("gwid=")) {
					gwid = c.replace("gwid=", "");
				}
				if (c.startsWith("nasname=")) {
					nasname = c.replace("nasname=", "");
				}
				if (c.startsWith("gwid=")) {
					nasname = c.replace("gwid=", "");
				}
				if (c.startsWith("basip=")) {
					basip = c.replace("basip=", "").trim();
				}
				if (c.startsWith("user_ip=")) {
					user_ip = c.replace("user_ip=", "");
				}
				if (c.startsWith("mac=")) {
					mac = c.replace("mac=", "");
				}
				if (c.startsWith("ssid=")) {
					ssid = c.replace("ssid=", "");
					ssid = URLDecoder.decode(ssid, "utf-8");
				}

			}
		}
		ikuaiMap.put("userIp", user_ip);
		ikuaiMap.put("allIp", user_ip);
		ikuaiMap.put("clientMac", mac);
		ikuaiMap.put("nasid", nasname);
		ikuaiMap.put("allMac", mac);
		ikuaiMap.put("allNasid", nasname);
		// 添加弹portal日志
		PortalLog portal_log = new PortalLog();
		portal_log.setClientIp(user_ip);
		portal_log.setClientMac(mac);
		portal_log.setNasid(nasname);
		portal_log.setRequestTime(CalendarUtil.currentTime());
		portal_log.setRouterIp(basip);
		portal_log.setRouterMac(gwid);
		portal_log.setRouterType("ikuai");
		portal_log.setUrl(ikweb);
		unitePortalServiceImpl.inserPortalLog(portal_log);
		return ikuaiMap;
	}

	/**
	 * 检测App认证参数
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 下午5:19:57
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> checkAppAuthParam(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = request.getReader();
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {

				sb.append(buff, 0, len);
			}
		} catch (IOException e) {
			map.put("code", "1");
			map.put("msg", "格式不正确" + e);
			return map;
		}
		Map<String, Object> dataMap = (Map<String, Object>) JSONSerializer.toJSON(sb.toString());
		String openid = dataMap.get("openid") + "";
		String openkey = dataMap.get("openkey") + "";
		String ip = dataMap.get("ip") + "";
		String ssid = dataMap.get("ssid") + "";
		String mac = dataMap.get("mac") + "";
		String tel = dataMap.get("tid") + "";// 手机号，可以为加密，但必须可以解开，也可为明文
		// String openid="4100069210006";
		// String openkey="d4559f975ae342a8a3c230233b8ec63d";
		// String ip="10.25.33.191";
		// String ssid="Unite-MT";
		// String tel="";
		// String mac="";
		log.error("get pingan  param=====openid=" + openid + "==openkey=" + openkey + "===ip=" + ip + "===ssid=" + ssid + "====mac=" + mac);
		if (openid == null || openkey == null || ip == null
				|| ssid == null/* ||mac==null||tel==null */) {
			map.put("code", "1");
			map.put("msg", "缺失参数");
			return map;
		}

		// AppAuthParam aap=unite_portal_daoImpl.getAppAuthParamByIp(ip);
		// if(aap==null){
		// map.put("code","1");
		// map.put("msg","ip不匹配");
		// return map;
		// }
		Map<String, Object> extendMap = null;
		extendMap = LogicalProcessing(request, response, "0");
		CloudSite site = (CloudSite) extendMap.get("site");
		// extendMap= (Map<String,Object>)
		// JSONSerializer.toJSON(aap.getExtend());
		extendMap = (Map<String, Object>) extendMap.get("type");
		// 小辣椒没有ssid先行测试 后打开该接口
		// if(!extendMap.get("ssid").equals(ssid)){
		// map.put("code","1");
		// map.put("msg","ssid不匹配");
		// return map;
		// }
		extendMap.put("siteid", site.getId());
		// 后续加上该参数开放该接口
		// if(!mac.equals(extendMap.get("allMac"))){
		// map.put("code","1");
		// map.put("msg","mac不匹配");
		// return map;
		// }
		// PortalUser user=this.getUserByName(tel);
		// if(user==null){
		// user= new PortalUser();
		// user.setUserName(tel);
		// user.setPassWord( SHA256.getUserPassword(tel,
		// MD5.encode("111111").toLowerCase()));
		// user.setPwdState(1);
		// user=this.dao().insert(user);
		// }
		// if(user==null){
		// map.put("code","1");
		// map.put("msg","网络错误");
		// return map;
		// }
		AppUser au = unitePortalServiceImpl.addOrUpdateAppUser(openid, openkey, 0);
		boolean insertRes = true;
		if (au.getId() > 0) {// 若无手机号则暂时已app用户表id为用户id，若有手机号则以主账户id为用户id
			CloudSitePortalEntity csp = unitePortalServiceImpl.getSiteRetance(au.getId(), Integer.parseInt(extendMap.get("siteid") + ""));
			if (csp == null) {
				insertRes = unitePortalServiceImpl.userSiteRetance(au.getId(), Integer.parseInt(extendMap.get("siteid") + ""));
			}
		} else {
			map.put("code", "1");
			map.put("msg", "网络错误");
			return map;
		}
		if (!insertRes) {
			map.put("code", "1");
			map.put("msg", "网络错误");
			return map;
		}
		AppInfo ai = unitePortalServiceImpl.getAppInfoBySiteId(Integer.parseInt(extendMap.get("siteid") + ""));
		if (ai == null) {
			map.put("code", "1");
			map.put("msg", "未知的app认证");
			return map;
		}
		map.put("app", ai);
		map.put("openid", openid);
		map.put("openkey", openkey);
		map.put("ip", ip);
		map.put("ssid", ssid);
		map.put("tel", tel);
		map.put("appuser", au);
		map.put("aap", extendMap);
		map.put("code", "0");
		return map;
	}

	/**
	 * 通过radius返回的CODE值来输出错误提示
	 * 
	 * @param err
	 * @param nasid
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> getCode(String err, String nasid, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> mapVal = new HashMap<String, Object>();
		if (err == null || err == "" || err.length() < 1) {
			String ip = WanipUtil.getIpAddr(request);
			boolean flag = unitePortalServiceImpl.updateIp(nasid, ip);
			if (!flag) {
				mapVal.put("code", 306);
				mapVal.put("msg", "网关ip错误,请联系运维人员修改网关ip");
				return mapVal;
			} else {
				mapVal.put("msg", "网络故障,稍后重试");
				mapVal.put("code", 200);
				return mapVal;
			}
		}
		String[] codes = err.split("-");
		switch (codes[0]) {
		case "200":
			// 修改外网ip
			String ip = WanipUtil.getIpAddr(request);
			unitePortalServiceImpl.updateIp(nasid, ip);
			mapVal.put("msg", "网络故障,稍后重试");
			mapVal.put("code", 200);
			break;
		case "201":
			mapVal.put("msg", "找不到此场所！");
			mapVal.put("code", 201);
			break;
		case "202":
			mapVal.put("msg", "账户余额不足需充值！");
			mapVal.put("code", 202);
			break;
		case "203":
			mapVal.put("msg", codes[1]);
			mapVal.put("code", 203);
			break;
		case "205":
			mapVal.put("msg", "用户名或密码错误！");
			mapVal.put("code", 205);
			break;
		case "206":
			mapVal.put("msg", "登陆太频繁！");
			mapVal.put("code", 206);
			break;
		case "208":
			mapVal.put("msg", "账号不存在！");
			mapVal.put("code", 208);
			break;
		case "304":
			// Pattern pattern = Pattern
			// .compile(";\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/");
			// Matcher matcher = pattern.matcher(codes[1]);
			// String model = null;
			// boolean boo = matcher.find();
			// if (boo) {
			// model = matcher.group(1).trim();
			// mapVal.put("data",model);
			// } else if (codes[1]!=null&&((String) codes[1]).indexOf("iPhone")
			// > -1) {
			// mapVal.put("data","iPhone");
			// } else {
			// mapVal.put("data",codes[1]);
			// }
			mapVal.put("msg", "该账号已在其他设备登录");
			mapVal.put("code", 304);
			break;
		default:
			break;
		}
		return mapVal;
	}

	/**
	 * 平安用户下线
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 下午5:46:08
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> offlinePingAn(HttpServletRequest request, HttpServletResponse response) {
		log.error("获取平安传递ALL参数===" + request.getParameterMap());
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		String sb1 = "";
		for (String key : request.getParameterMap().keySet()) {
			sb1 = key;
		}
		Map<String, Object> dataMap = (Map<String, Object>) JSONSerializer.toJSON(sb1.toString());
		log.error("获取平安转换参数===" + dataMap);
		String openid = dataMap.get("openid") + "";
		String timestamp = dataMap.get("timestamp") + "";
		String signature = dataMap.get("signature") + "";
		String nonce = dataMap.get("nonce") + "";
		// String openid="1383541533";
		if (openid == null || timestamp == null || signature == null || nonce == null) {
			map.put("code", "1");
			map.put("msg", "缺失参数");
			log.error("获取平安参数===缺失参数");
			return map;
		}
		List<String> list = new ArrayList<String>();
		list.add(openid);
		list.add(timestamp);
		list.add(nonce);
		AppInfo ap = unitePortalServiceImpl.getAppInfoBySiteId(1);
		String sign = Sha1Util.getSignature(list, ap.getSercetKey());
		if (!sign.equals(signature)) {
			map.put("code", "1");
			map.put("msg", "签名错误");
			log.error("=======签名错误");

			return map;
		}

		List<String> nasid = unitePortalServiceImpl.getradacctInfo(openid);
		if (nasid == null || nasid.size() == 0) {
			map.put("code", "1");
			map.put("msg", "该账户已下线");
			log.error("=======该账户已下线");
			return map;
		}
		param.put("userName", openid);
		param.put("action", "PORTAL_LOGINOUT");
		AuthUser at = unitePortalServiceImpl.selAuthUser(openid, null, null);
		boolean falg = false;
		if (at.getSysType().equals("h3c")) {
			param.put("nasid", nasid.get(0));
			param.put("userip", nasid.get(1));
			param.put("basip", nasid.get(2));
			falg = unitePortalServiceImpl.passH3c(param);
		} else {
			param.put("allNasid", nasid.get(0));
			param.put("wlanuserip", nasid.get(1));
			param.put("wlanacip", nasid.get(2));
			falg = unitePortalServiceImpl.passMoto(param);
		}
		if (falg) {
			map.put("code", "0");
			map.put("msg", "success");
			log.error("=======下线成功");
			return map;
		} else {
			map.put("code", "1");
			map.put("msg", "fail");
			log.error("=======下线失败");
			return map;
		}
	}

	/**
	 * 广告——获取广告列表
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年4月10日 下午6:06:40
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@At("/getAD")
	@Ok("raw:json")
	public String getAd() {
		List<AdInfo> adlist = adServiceImpl.getAdInfo();
		return Json.toJson(adlist);
	}

	// 检查用户是否已交押金//
	public boolean cashPledgeCheck(String ip) {
		APCashPledge apCashPledge = personalServiceImpl.cashPledgeCheck(ip);
		boolean flag = false;
		if (apCashPledge != null) {
			// 判断是否已交押金
			if (apCashPledge.getAmount().intValue() == 10) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * H5支付——缴纳押金统一下单接口
	 * 
	 * @param siteId
	 * @param request
	 * @param responses
	 * @return
	 */
	@At("/unifiedorder")
	@Ok("raw:json")
	public String unifiedorder(int siteId, String body, String detail, String totalFee, String productId, int buyNum, String apMac, String ip, HttpServletRequest request,
			HttpServletResponse responses) {
		/* attach:有支付类型 userMac APMac paytype */
		System.out.println("支付押金----------------------------------------------------");
		CloudSite site = userPayServiceImpl.getCloudSiteById(siteId);
		SortedMap<Object, Object> resMap = new TreeMap<Object, Object>();
		if (site != null) {
			//附加值结构 username*siteId*userMac*apMac*payType*body
			String attach = "null*null*"+ip+"*"+apMac+"*1*"+body;// 押金类型
			String strRandom = PayCommonUtil.buildRandom(4) + "";
			String nonce_str = "fxwxwifi" + strRandom;// 生成随机字符串
			// 商户订单号生成
			String out_Trade_No = PayUtils.getOut_Trade_No(4);
			Order order = new Order();
			order.setAppId(APPID);
			order.setMchId(MCHID);
			order.setDetail(detail);
			order.setDeviceInfo("WEB");
			order.setNonceStr(nonce_str);
			order.setSignType("MD5");
			order.setBody(body);
			order.setDetail(detail);
			order.setAttach(attach);
			order.setOut_trade_no(out_Trade_No);
			order.setFeeType("CNY");
			//将单位为分的数值转换为单位为元，保留小数点后两位。
			order.setTotalFee(BigDecimalUtil.ConvertNumber(new BigDecimal(Double.parseDouble(totalFee)), 100, 2));
			order.setSpbillCreateIp(ip);
			// 交易时间
			String[] tradeTime = PayUtils.getTradeTime(30);
			order.setTimeStart(tradeTime[0]);
			order.setTimeExpire(tradeTime[1]);
			order.setCreateTime(new Timestamp(new Date().getTime()));
			order.setGoodsTag("WXG");
			order.setNotifyUrl(notify_url);
			order.setTradeType("MWEB");
			order.setProductId(productId);
			order.setSceneInfo("{\"h5_info\": {\"type\":\"Wap\",\"wap_url\":" + fxwx_web + "\"\",\"wap_name\":用户支付押金\"\"}}");
			order.setOrderNum(PayUtils.getOut_Trade_No(4));
			order.setStatue(0);
			order.setIsDelete(0);
			order.setBuyNum(buyNum);

			SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>();
			sortedMap.put("appid", APPID);
			sortedMap.put("attach", attach);
			sortedMap.put("body", body);
			sortedMap.put("detail", detail);
			sortedMap.put("device_info", "WEB");
			sortedMap.put("fee_type", "CNY");
			sortedMap.put("goods_tag", "WXG");
			sortedMap.put("mch_id", MCHID);
			sortedMap.put("nonce_str", nonce_str);
			sortedMap.put("notify_url", notify_url);
			sortedMap.put("out_trade_no", out_Trade_No);
			sortedMap.put("product_id", productId);
			sortedMap.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\":" + fxwx_web + "\"\",\"wap_name\":用户支付押金\"\"}}");
			sortedMap.put("sign_type", "MD5");
			sortedMap.put("spbill_create_ip", ip);
			sortedMap.put("time_expire", tradeTime[1]);
			sortedMap.put("time_start", tradeTime[0]);
			sortedMap.put("total_fee", new Integer(totalFee));//单位为分
			sortedMap.put("trade_type", "MWEB");
			// sortedMap.put("openid")

			String sign = PayCommonUtil.createSign("UTF-8", sortedMap, KEY);
			sortedMap.put("sign", sign);
			order.setSign(sign);
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@------生成订单信息" + order);
			CloseableHttpResponse res = null;
			CloseableHttpClient client = null;
			String requestXML = PayCommonUtil.getRequestXml(sortedMap);
			HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
			StringEntity entityParams = new StringEntity(requestXML, "utf-8");
			httpPost.setEntity(entityParams);
			client = HttpClients.createDefault();
			try {
				res = client.execute(httpPost);
				Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(res.getEntity()), "UTF-8");
				if (resultMap != null) {
					// 下单成功
					if (resultMap.get("return_code").toString().equals("SUCCESS") && resultMap.get("result_code").toString().equals("SUCCESS")) {
						// 签名验证
						if (PayCommonUtil.isSignatureValid(resultMap, KEY)) {
							order.setBackJson(Json.toJson(resultMap));
							// 生成订单信息
							boolean flag = orderServiceImpl.insertOrderInfo(order);
							if (flag) {
								String redirect_url = URLEncoder.encode("http://wifi.feixun-wx.cn/portal/PROT/hint?ordernum="+out_Trade_No, "UTF-8");
								String mweb_url = resultMap.get("mweb_url") + "&redirect_url="+redirect_url;
								String prepay_id = resultMap.get("prepay_id");// 预支付交易会话标识
								resMap.put("appId", APPID);
								resMap.put("package", "prepay_id=" + prepay_id);
								resMap.put("nonceStr", nonce_str);
								resMap.put("signType", "MD5");
								resMap.put("timeStamp", DateUtil.create_timestamp());
								String paySign = PayCommonUtil.createSign("UTF-8", resMap, KEY);
								resMap.put("mweb_url", mweb_url);
								resMap.put("pg", prepay_id);
								resMap.put("paySign", paySign);
								resMap.put("code", "200");
								resMap.put("orderNum", out_Trade_No);
							} else {
								resMap.put("msg", "下订单失败");
								resMap.put("code", "201");
							}
						} else {
							resMap.put("msg", "签名错误");
							resMap.put("code", "201");
						}
					}
				} else {
					resMap.put("msg", "支付异常");
					resMap.put("code", "201");
				}
			} catch (Exception e) {
				log.error("支付出现异常" + e);
			}
		} else {
			resMap.put("msg", "下单异常");
			resMap.put("code", "201");
		}
		log.error("@@@@@@@@@@@@@@@@@@@@@@@@------用户支付押金:" + resMap);
		return Json.toJson(resMap);
	}

	/**
	 * H5支付——统一下单
	 * 
	 * @param siteId
	 * @param request
	 * @param responses
	 * @return
	 */
	@At("/payforMemberPack")
	@Ok("raw:json")
	public String payforMemberPack(int siteId, String body, String detail, String totalFee, String productId, String mac, String nasid, String allMac, String userIp,String username, int buyNum, int tag,
			HttpServletRequest request, HttpServletResponse responses) {
		/* attach:有支付类型 1:押金,2:月租 userId siteId */
		log.error("==============支付会员套餐=======" + siteId + body + detail + totalFee + mac + productId + buyNum + tag);

		CloudSite site = userPayServiceImpl.getCloudSiteById(siteId);
		SortedMap<Object, Object> resMap = new TreeMap<Object, Object>();
		if (site != null) {
			Order order = new Order();
			//附加值结构 username*siteId*userMac*apMac*payType*body
			String attach = "";
			String name = username;
			if (tag == 1) {// 微信认证
				WcahtUserOpenid wuo = unitePortalServiceImpl.findWcahtUserOpenid(mac, null, APPID, siteId, nasid);
				if (wuo != null) {// 老用户有openid,新用户为空
					order.setOpenId(wuo.getOpenId());
					if (wuo.getOpenId() == null) {
						// 微信新用户，暂时将终端mac作为平台账号
						String str = mac.replaceAll(":", "");
						name = "1#" + str;
					}
					name = "2#" + wuo.getOpenId();
				}
			} else if (tag == 2) { // 手机号+验证码登录认证
				name = username;
			}
			if (username.equals("")) {
				resMap.put("msg", "下单异常");
				resMap.put("code", "201");
				log.error("@@@@@@@@@@@@@@@@@@@@@@@@------用户名为空！！");
				return Json.toJson(resMap);
			}
			attach = name+"*"+siteId+"*"+mac+"*"+allMac+"*2*"+body;// 月租/会员套餐

			String nonce_str = "fxwx" + PayUtils.randomNumber(26);// 生成随机字符串
			// 商户订单号生成
			String out_Trade_No = PayUtils.getOut_Trade_No(4);

			order.setAppId(APPID);
			order.setMchId(MCHID);
			order.setDetail(detail);
			order.setDeviceInfo("WEB");
			order.setNonceStr(nonce_str);
			order.setSignType("MD5");
			order.setBody(body);// 商品描述
			order.setDetail(detail);// 商品详情
			order.setAttach(attach);// 附加数据
			order.setOut_trade_no(out_Trade_No);// 商户订单号
			order.setFeeType("CNY");
			//将单位为分的数值转换为单位为元，保留小数点后两位。
			order.setTotalFee(BigDecimalUtil.ConvertNumber(new BigDecimal(Double.parseDouble(totalFee)), 100, 2));
			order.setSpbillCreateIp(userIp);
			// 交易时间
			String[] tradeTime = PayUtils.getTradeTime(30);
			order.setTimeStart(tradeTime[0]);
			order.setTimeExpire(tradeTime[1]);
			order.setCreateTime(new Timestamp(new Date().getTime()));
			order.setGoodsTag("WXG");
			order.setNotifyUrl(notify_url);
			order.setTradeType("MWEB");
			order.setProductId(productId);
			order.setSceneInfo("{\"h5_info\": {\"type\":\"Wap\",\"wap_url\":" + fxwx_web + "\"\",\"wap_name\":用户账户充值\"\"}}");
			order.setOrderNum(PayUtils.getOut_Trade_No(4));// 订单号
			order.setStatue(0);
			order.setIsDelete(0);
			order.setBuyNum(buyNum);

			SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>();
			sortedMap.put("appid", APPID);
			sortedMap.put("attach", attach);
			sortedMap.put("body", body);
			sortedMap.put("detail", detail);
			sortedMap.put("device_info", "WEB");
			sortedMap.put("fee_type", "CNY");
			sortedMap.put("goods_tag", "WXG");
			sortedMap.put("mch_id", MCHID);
			sortedMap.put("nonce_str", nonce_str);
			sortedMap.put("notify_url", notify_url);
			sortedMap.put("out_trade_no", out_Trade_No);
			sortedMap.put("product_id", productId);
			sortedMap.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\":" + fxwx_web + "\"\",\"wap_name\":用户账户充值\"\"}}");
			sortedMap.put("sign_type", "MD5");
			sortedMap.put("spbill_create_ip", userIp);
			sortedMap.put("time_expire", tradeTime[1]);
			sortedMap.put("time_start", tradeTime[0]);
			sortedMap.put("time_expire", tradeTime[1]);
			sortedMap.put("total_fee", new Integer(totalFee));
			sortedMap.put("trade_type", "MWEB");
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@------sortedMap生成订单信息" + sortedMap);
			// sortedMap.put("openid")

			String sign = PayCommonUtil.createSign("UTF-8", sortedMap, KEY);
			sortedMap.put("sign", sign);
			order.setSign(sign);
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@------生成订单信息" + order);
			CloseableHttpResponse res = null;
			CloseableHttpClient client = null;
			String requestXML = PayCommonUtil.getRequestXml(sortedMap);
			HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
			StringEntity entityParams = new StringEntity(requestXML, "utf-8");
			httpPost.setEntity(entityParams);
			client = HttpClients.createDefault();

			try {
				res = client.execute(httpPost);
				Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(res.getEntity()), "UTF-8");
				if (resultMap != null) {
					// 下单成功
					if (resultMap.get("return_code").toString().equals("SUCCESS") && resultMap.get("result_code").toString().equals("SUCCESS")) {
						// 签名验证
						if (PayCommonUtil.isSignatureValid(resultMap, KEY)) {
							order.setBackJson(Json.toJson(resultMap));
							// 生成订单信息
							boolean flag = orderServiceImpl.insertOrderInfo(order);
							if (flag) {
								String redirect_url = URLEncoder.encode("http://wifi.feixun-wx.cn/portal/PROT/hint?ordernum="+out_Trade_No, "UTF-8");
								String mweb_url = resultMap.get("mweb_url") + "&redirect_url="+redirect_url;
								String prepay_id = resultMap.get("prepay_id");// 预支付交易会话标识
								resMap.put("appId", APPID);
								resMap.put("package", "prepay_id=" + prepay_id);
								resMap.put("nonceStr", nonce_str);
								resMap.put("signType", "MD5");
								resMap.put("timeStamp", DateUtil.create_timestamp());
								String paySign = PayCommonUtil.createSign("UTF-8", resMap, KEY);
								resMap.put("mweb_url", mweb_url);
								resMap.put("pg", prepay_id);
								resMap.put("paySign", paySign);
								resMap.put("code", "200");
								resMap.put("orderNum", out_Trade_No);
							} else {
								resMap.put("msg", "下订单失败");
								resMap.put("code", "201");
							}
						} else {
							resMap.put("msg", "签名错误");
							resMap.put("code", "201");
						}
					}
				} else {
					resMap.put("msg", "支付异常");
					resMap.put("code", "201");
				}
			} catch (Exception e) {
				log.error("支付出现异常" + e);
			}
		} else {
			resMap.put("msg", "下单异常");
			resMap.put("code", "201");
		}
		log.error("@@@@@@@@@@@@@@@@@@@@@@@@------会员套餐下单:" + resMap);
		return Json.toJson(resMap);
	}

	/**
	 * 微信公众号———统一下单接口
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年6月29日 下午1:51:51
	 * @param siteId
	 * @param body
	 * @param detail
	 * @param totalFee
	 * @param productId
	 * @param username
	 * @param buyNum
	 * @param tag
	 * @param request
	 * @param responses
	 * @return
	 */
	@At("/wechatpayforMemberPack")
	@Ok("raw:json")
	public String wechatpayforMemberPack(int siteId, String openId, String body, String totalFee, String productId, String username, int buyNum, int tag, HttpServletRequest request,
			HttpServletResponse responses) {
		/* attach:有支付类型 1:押金,2:月租 userId siteId */
		log.error("==============支付会员套餐=======" + siteId + "--" + body + "--" + totalFee + "--" + productId + "--" + buyNum + "--" + tag);
		String ip = request.getRemoteAddr();
		CloudSite site = userPayServiceImpl.getCloudSiteById(siteId);
		SortedMap<Object, Object> resMap = new TreeMap<Object, Object>();
		if (username.equals("")) {
			resMap.put("msg", "下单异常");
			resMap.put("code", "201");
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@------用户名为空！！");
			return Json.toJson(resMap);
		}
		if (site != null) {
			Order order = new Order();
			//附加值结构 username*siteId*userMac*apMac*payType*body
			String attach = "";
			String name = "null";
			if (tag == 1) {// 微信账号充值
				order.setOpenId(username);
				name = "2#" + username;
			} else if (tag == 2) { // 手机号+验证码登录认证
				name = username;
			}
			attach = name+"*"+siteId+"*null*null*2*"+body;

			String nonce_str = "fxwx" + PayUtils.randomNumber(26);// 生成随机字符串
			// 商户订单号生成
			String out_Trade_No = PayUtils.getOut_Trade_No(4);

			order.setAppId(APPID);
			order.setMchId(MCHID);
			order.setDeviceInfo("WEB");
			order.setNonceStr(nonce_str);
			order.setSignType("MD5");
			order.setBody(body);// 商品描述
			order.setAttach(attach);// 附加数据
			order.setOut_trade_no(out_Trade_No);// 商户订单号
			order.setFeeType("CNY");
			//将单位为分的数值转换为单位为元，保留小数点后两位。
			order.setTotalFee(BigDecimalUtil.ConvertNumber(new BigDecimal(Double.parseDouble(totalFee)), 100, 2));
			order.setSpbillCreateIp(ip);
			// 交易时间
			String[] tradeTime = PayUtils.getTradeTime(30);
			order.setTimeStart(tradeTime[0]);
			order.setTimeExpire(tradeTime[1]);
			order.setCreateTime(new Timestamp(new Date().getTime()));
			order.setGoodsTag("WXG");
			order.setNotifyUrl(notify_url);
			order.setTradeType("JSAPI");
			order.setProductId(productId);
			// order.setLimitPay("no_credit");
			order.setOpenId(openId);
			order.setOrderNum(out_Trade_No);// 订单号
			order.setStatue(0);
			order.setIsDelete(0);
			order.setBuyNum(buyNum);

			SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>();
			sortedMap.put("appid", APPID);
			sortedMap.put("attach", attach);
			sortedMap.put("body", body);
			sortedMap.put("device_info", "WEB");
			sortedMap.put("fee_type", "CNY");
			sortedMap.put("goods_tag", "WXG");
			sortedMap.put("mch_id", MCHID);
			sortedMap.put("nonce_str", nonce_str);
			sortedMap.put("notify_url", notify_url);
			sortedMap.put("openid", openId);
			sortedMap.put("out_trade_no", out_Trade_No);
			sortedMap.put("product_id", productId);
			sortedMap.put("sign_type", "MD5");
			sortedMap.put("spbill_create_ip", ip);
			sortedMap.put("time_expire", tradeTime[1]);
			sortedMap.put("time_start", tradeTime[0]);
			sortedMap.put("total_fee", new Integer(totalFee));//单位为：分
			sortedMap.put("trade_type", "JSAPI");

			String sign = PayCommonUtil.createSign("UTF-8", sortedMap, KEY);
			sortedMap.put("sign", sign);
			order.setSign(sign);
			CloseableHttpResponse res = null;
			CloseableHttpClient client = null;
			String requestXML = PayCommonUtil.getRequestXml(sortedMap);
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@------最终的提交订单xml:" + requestXML);
			HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");
			StringEntity entityParams = new StringEntity(requestXML, "utf-8");
			httpPost.setEntity(entityParams);
			client = HttpClients.createDefault();

			try {
				res = client.execute(httpPost);
				Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(res.getEntity()), "UTF-8");
				if (resultMap != null) {
					// 下单成功
					if (resultMap.get("return_code").toString().equals("SUCCESS") && resultMap.get("result_code").toString().equals("SUCCESS")) {
						// 签名验证
						if (PayCommonUtil.isSignatureValid(resultMap, KEY)) {
							order.setBackJson(Json.toJson(resultMap));
							// 生成订单信息
							boolean flag = orderServiceImpl.insertOrderInfo(order);
							if (flag) {
								String prepay_id = resultMap.get("prepay_id");// 预支付交易会话标识
								resMap.put("appId", APPID);
								resMap.put("timeStamp", DateUtil.create_timestamp());
								resMap.put("nonceStr", nonce_str);
								resMap.put("package", "prepay_id=" + prepay_id);
								resMap.put("signType", "MD5");
								String paySign = PayCommonUtil.createSign("UTF-8", resMap, KEY);
								resMap.put("paySign", paySign);
								resMap.put("pg", prepay_id);
								resMap.put("code", "200");
								resMap.put("orderNum", out_Trade_No);
							} else {
								resMap.put("msg", "下订单失败");
								resMap.put("code", "201");
							}
						} else {
							resMap.put("msg", "签名错误");
							resMap.put("code", "201");
						}
					}
				} else {
					resMap.put("msg", "支付异常");
					resMap.put("code", "201");
				}
			} catch (Exception e) {
				log.error("支付出现异常" + e);
			}
		} else {
			resMap.put("msg", "下单异常");
			resMap.put("code", "201");
		}
		log.error("@@@@@@@@@@@@@@@@@@@@@@@@------会员套餐下单:" + resMap);
		return Json.toJson(resMap);
	}

	@At("/wecahtToken")
	public String wecahtToken(String signature, String timestamp, String nonce, String echostr, HttpServletRequest request, HttpServletResponse response) {
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (signature != null && CheckoutUtil.checkSignature(signature, timestamp, nonce)) {
			return echostr;
		}
		return null;
	}

	/**
	 * 微信支付———执行回调函数
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@At("/wechatNotify")
	public void wechatpayNotify(HttpServletRequest request, HttpServletResponse response) {
		String result = null;
		// 返回给微信的处理结果
		String notifyXml = null;
		StringBuilder buffer = new StringBuilder();
		try {
			InputStream reqInput = request.getInputStream();
			if (reqInput == null) {
				// 没有接收到数据!!!
				buffer.append("no-data");
			} else {
				BufferedReader bin = new BufferedReader(new InputStreamReader(reqInput, "utf-8"));
				String line = null;
				while ((line = bin.readLine()) != null) {
					buffer.append(line).append("\r\n");
				}
			}
			notifyXml = buffer.toString();
			log.error("接收到的报文：" + notifyXml);
			if (!notifyXml.isEmpty()) {
				Map<String, String> map = PayCommonUtil.xmlToMap(notifyXml);
				String generate_sign = PayCommonUtil.generateSignature(map, KEY);

				if ("SUCCESS".equals((String) map.get("return_code")) && "SUCCESS".equals((String) map.get("result_code"))) {
					// 签名对比
					if (map.get("sign").equals(generate_sign)) {
						result = setXml("SUCCESS", "OK");
						// 获取订单信息
						Order order = orderServiceImpl.selectOrderInfoByOut_trade_no((String) map.get("out_trade_no"));
						if (order == null) {
							result = setXml("fail", "无商户订单");
						} else {
							// 修改支付用户的到期 时间
							// 更新数据库订单状态信息,已完成
							order.setStatue(1);
							order.setUpdateTime(new Timestamp(new Date().getTime()));
							orderServiceImpl.updateOrderInfo(order);
							int product_id = Integer.parseInt(order.getProductId());
							SitePriceConfig sitePriceConfig = unitePortalServiceImpl.getSitePriceConfigById(product_id);
							// 根据产品ID在t_site_price_config表中查找信息
							//附加值结构 username*siteId*userMac*apMac*payType*body
							String[] attach = order.getAttach().split("\\*");
							String usernameStr = attach[0];
							String username = usernameStr.indexOf("#") > -1 ? usernameStr.substring(2) : usernameStr;
							PortalUser user = unitePortalServiceImpl.getUserByName(username);
							String payname = attach[5];
							int payType = Integer.parseInt(attach[4]);// 支付类型
							log.error("=======支付类型====：" + payType);
							if (payType == 1) {// 1为押金
								// 生成押金信息
								CashPledge cashPledge = new CashPledge();
								cashPledge.setUserMac(attach[2]);
								cashPledge.setAp_mac(attach[3]);
								cashPledge.setOrderNum(order.getOrderNum());
								cashPledge.setAmount(order.getTotalFee());
								cashPledge.setCreatetime(new Timestamp(new Date().getTime()));
								userPayServiceImpl.insertCashPledge(cashPledge);
							} else if (payType == 2) {// 2为月租/会员套餐
								int siteId = Integer.parseInt(attach[1]);
								// 生成月租/会员支付信息
								SitePaymentRecord sitePaymentRecord = new SitePaymentRecord();
								sitePaymentRecord.setOrderNum(order.getOrderNum());
								sitePaymentRecord.setTradeNum(order.getOut_trade_no());
								sitePaymentRecord.setParamJson(map.toString());
								sitePaymentRecord.setSiteId(siteId);
								sitePaymentRecord.setUserId(user.getId());
								sitePaymentRecord.setIsFinish(1);// 支付成功
								sitePaymentRecord.setFinishTime(new Timestamp(new Date().getTime()));
								sitePaymentRecord.setFailReason("支付成功");
								sitePaymentRecord.setPayType(3);// 微信支付
								sitePaymentRecord.setInputPayUser(user.getUserName());
								sitePaymentRecord.setOutPayUser(user.getUserName());
								userPayServiceImpl.insertSitePaymentRecord(sitePaymentRecord);

								log.error("=======保存支付记录====：" + sitePaymentRecord);
								// 生成收益信息
								SiteIncome siteIncome = new SiteIncome();
								siteIncome.setTransactionAmount(order.getTotalFee());
								siteIncome.setSiteId(siteId);
								siteIncome.setPortalUserId(user.getId());
								siteIncome.setPortalUserName(user.getUserName());
								siteIncome.setBuyNum(1);
								siteIncome.setPayName(payname);
								siteIncome.setCreateTime(new Timestamp(new Date().getTime()));
								siteIncome.setPayType(3);
								unitePortalServiceImpl.addSiteIncome(siteIncome);

								log.error("=======保存收益记录====：" + siteIncome);
								
								// userMac不为空，说明是微信公众号支付，微信公众号支付只能为老用户充值。
								String usermac = attach[2].equals("null") ? attach[2] : null;
								if (usermac == null) {// 微信公众号支付，为老用户增加上网时长
									PortalUser pUser = unitePortalServiceImpl.getUserByName(username);
									addExpirationTime(pUser.getId(), siteId, sitePriceConfig.getPrice_type(), sitePriceConfig.getPrice_num());
								} else {// H5支付
									PortalUser portalUser = new PortalUser();
									portalUser.setUserName(username);
									portalUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
									portalUser.setState(0);
									portalUser.setClientMac(usermac);
									portalUser.setSiteId(siteId);
									portalUser.setPwdState(2);
									if (usernameStr.indexOf("1#") >= 0) {// 微信新用户——该字符串为mac组合
										portalUser.setUserName(username);
										portalUser.setPassWord(SHA256.getUserPassword(username, MD5.encode("111111").toLowerCase()));
										unitePortalServiceImpl.insertUserRegist(portalUser);
										PortalUser pUser = unitePortalServiceImpl.getLogin(username, "111111");
										addExpirationTime(pUser.getId(), siteId, sitePriceConfig.getPrice_type(), sitePriceConfig.getPrice_num());
									} else if (usernameStr.indexOf("2#") >= 0) {// 微信老用户——该字符串为微信OpenId组合
										PortalUser pUser = unitePortalServiceImpl.getLogin(username, "111111");
										addExpirationTime(pUser.getId(), siteId, sitePriceConfig.getPrice_type(), sitePriceConfig.getPrice_num());
									} else {// 手机号用户
										PortalUser pUser = unitePortalServiceImpl.getUserByName(username);
										addExpirationTime(pUser.getId(), siteId, sitePriceConfig.getPrice_type(), sitePriceConfig.getPrice_num());
									}
								}
							}
						}
					} else {
						// 签名错误
						result = setXml("fail", "签名校验失败");
					}
				} else {
					// 支付失败
					result = setXml("fail", "支付失败");
				}
			} else {
				// 没有应答结果
				result = setXml("fail", "没有应答结果");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			log.error("@@@@@@@@@@@@@@@@@@@@@@@@------微信支付回调数据结束:" + result);
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(result.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean addExpirationTime(int userId, int siteId, int sitePriceType, int price_num){
		boolean flag = false;
		SiteCustomerInfo scii = userPayServiceImpl.getExpirationTimeByProuserid(userId, siteId);
		if (scii != null) {// 如果以前有这个用户
			Date endDate = null;
			Date expirationTime = scii.getExpirationTime();
			Date sDate = new Date();
			if (DateUtil.compareDate(new Date(), expirationTime) < 1) {
				sDate = expirationTime;
			}
			if (sitePriceType == 0) {// 按小时收费
				endDate = DateUtil.addDateMinut(sDate, price_num);
			} else if (sitePriceType == 1) {// 按天收费
				endDate = DateUtil.changeDate(sDate, price_num);
			} else if (sitePriceType == 2) {// 按月收费
				endDate = DateUtil.changeMonth(sDate, price_num);
			}
			log.error("=======更新用户到期时间====："+ scii.getId() +"--"+ endDate);
			flag = unitePortalServiceImpl.updateCustomerInfo(scii.getId(), endDate);
		} else {// 如果以前没有这个客户则插入
			scii = new SiteCustomerInfo();
			if (sitePriceType == 0) {// 按小时收费
				scii.setExpirationTime(DateUtil.addDateMinut(new Date(), price_num));
			} else if (sitePriceType == 1) {// 按天收费
				scii.setExpirationTime(DateUtil.changeDate(new Date(), price_num));
			} else if (sitePriceType == 2) {// 按月收费
				scii.setExpirationTime(DateUtil.changeMonth(new Date(), price_num));
			}
			scii.setSiteId(siteId);
			scii.setPayWay(0);
			scii.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			scii.setCreateTime(new Timestamp(System.currentTimeMillis()));
			scii.setPortalUserId(userId);
			log.error("======保存用户到期时间记录====：" + scii);
			flag = unitePortalServiceImpl.addCustomerInfo(scii);
		}
		if(!flag){
			return addExpirationTime(userId, siteId, sitePriceType, price_num);
		}else{
			return flag;
		}
	}
	
	/**
	 * 用户支付完成后通过微信查询订单接口查询支付状态
	 * 
	 * @param request
	 * @param responses
	 * @return
	 */
	@At("/wechatPayOrderQuery")
	@Ok("raw:json")
	public String wechatOrderPay(String out_trade_no, HttpServletRequest request, HttpServletResponse responses) {
		Map<String,Object> map = new HashMap<String,Object>();
		log.error("out_trade_no:" + out_trade_no);
		
		// 根据商户订单号查询订单信息
		Order order = orderServiceImpl.selectOrderInfoByOut_trade_no(out_trade_no);
//		log.error("订单查询结果:" + order);
		if (order != null) {
			// 查询订单状态
			if (order.getStatue() == 1) {// 支付完成
				map.put("return_code", "SUCCESS");
				map.put("msg", "支付完成");
				log.error("微信支付订单查询:" + map);
				return Json.toJson(map);
			}
		}
		map.put("return_code", "FAIL");
		map.put("msg", "支付异常");
		log.error("微信支付订单查询:" + map);
		return Json.toJson(map);
	}

	// 通过xml发给微信消息
	public static String setXml(String return_code, String return_msg) {
		/*
		 * SortedMap<String, String> parameters = new TreeMap<String, String>();
		 * parameters.put("return_code", return_code);
		 * parameters.put("return_msg", return_msg);
		 */
		return "<xml><return_code><![CDATA[" + return_code + "]]>" + "</return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
	}

	/**
	 * 记录广告一次展示或一次点击
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年4月10日 下午6:23:16
	 * @param adId
	 * @return
	 */
	@At("/operation")
	@Ok("raw:json")
	public String operation(@Param("adId") int adId, @Param("type") int type, @Param("usermac") String usermac, @Param("op") int op) {
		if (adId == 0) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 记录访问广告记录
		AdAccessRecord accessRecord = new AdAccessRecord();
		AdEffect adEffect = new AdEffect();
		int res = -1;
		if (op == 1) {// 浏览广告
			accessRecord.setAdId(adId);
			accessRecord.setUserMac(usermac);
			accessRecord.setSysType(type);
			accessRecord.setBrowse(1);
			accessRecord.setLastTime(DateUtil.getStringDate());
			res = adServiceImpl.updateAdAccessRecord(accessRecord);
			adEffect.setAdId(adId);
			adEffect.setShowNum(1);
			adEffect.setUpdateTime(DateUtil.getStringDate());
			res = adServiceImpl.updateAdEffect(adEffect);
		} else if (op == 2) {// 点击广告
			accessRecord.setAdId(adId);
			accessRecord.setUserMac(usermac);
			accessRecord.setSysType(type);
			accessRecord.setClick(1);
			accessRecord.setLastTime(DateUtil.getStringDate());
			res = adServiceImpl.updateAdAccessRecord(accessRecord);
			adEffect.setAdId(adId);
			adEffect.setPv(1);
			adEffect.setUpdateTime(DateUtil.getStringDate());
			if (res == 1) {// 若访问记录为新增，则识为独立访客量
				adEffect.setUv(1);
			}
			res = adServiceImpl.updateAdEffect(adEffect);
		}

		map.put("code", res);
		return Json.toJson(map);
	}

	@At("/wechatchoiceTc")
	public ViewWrapper wechatchoiceTc(HttpServletRequest request, HttpServletResponse response, ViewModel model) {
		String openid = request.getParameter("openid");
		String username = request.getParameter("username");
		int siteId = Integer.parseInt(request.getParameter("siteId"));
		int tag = Integer.parseInt(request.getParameter("tag"));
		log.error("======================wechatchoiceTc====" + username + "--" + siteId + "--" + tag);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("openid", openid);
		map.put("username", username);
		map.put("siteId", siteId);
		map.put("tag", tag);
		List<SitePriceConfig> olist = unitePortalServiceImpl.getSitePriceMemberCombo(siteId);
		List<SitePriceConfig> list = new ArrayList<SitePriceConfig>();
		Collections.sort(olist, new Comparator<SitePriceConfig>() {
			@Override
			public int compare(SitePriceConfig o1, SitePriceConfig o2) {
				BigDecimal db = o1.getUnit_price().subtract(o2.getUnit_price());
				return db.intValue();
			}
		});

		for (SitePriceConfig sp : olist) {
			sp.setUnit_price(sp.getUnit_price().setScale(2));
			list.add(sp);
		}
		map.put("priceConfig", list);
		log.error("==================wechatchoiceTc===" + list);
		return new ViewWrapper(new JspView("jsp/wechatchoiceTc"), map);
	}

	@At("/choiceTc")
	public ViewWrapper choiceTc(String key, int siteId, String userMac, String nasid, String allMac, String userIp,String username, int tag, HttpServletRequest request, ViewModel model) {
		log.error("======================choiceTc====" + key + "::" + userMac + "::" + siteId + "::" + nasid + "::" + allMac + "::" + userIp + "::" + tag);
		/* log.error("======================choiceTc====" + payType); */
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("key", key);
		map.put("siteId", siteId);
		map.put("userMac", userMac);
		map.put("nasid", nasid);
		map.put("allMac", allMac);
		map.put("userIp", userIp);
		map.put("username", username);
		map.put("tag", tag);
		List<SitePriceConfig> olist = unitePortalServiceImpl.getSitePriceMemberCombo(siteId);
		List<SitePriceConfig> list = new ArrayList<SitePriceConfig>();
		Collections.sort(olist, new Comparator<SitePriceConfig>() {
			@Override
			public int compare(SitePriceConfig o1, SitePriceConfig o2) {
				BigDecimal db = o1.getUnit_price().subtract(o2.getUnit_price());
				return db.intValue();
			}
		});

		for (SitePriceConfig sp : olist) {
			sp.setUnit_price(sp.getUnit_price().setScale(2));
			list.add(sp);
		}
		map.put("priceConfig", list);
		log.error("==================choiceTc===" + list);
		return new ViewWrapper(new JspView("jsp/choice_tc"), map);
	}

	/**
	 * 判断使用时间是否到期
	 * 
	 * @param userName
	 * @param siteId
	 * @return
	 */
	public String useDateIsExpire(String userName, int siteId) {
		PortalUser portalUser = unitePortalServiceImpl.getUserByName(userName);
		if (portalUser != null) {
			SiteCustomerInfo scii = userPayServiceImpl.getExpirationTimeByProuserid(portalUser.getId(), siteId);
			if (scii != null) {
				// 根据和当前时间的比较计算到期时间
				if (scii.getExpirationTime().after(new Date())) {
					map1.put("result", 200);
					map1.put("msg", "使用时间未到期");
				} else {
					map1.put("result", 201);
					map1.put("msg", "使用时间到期");
				}
			} else {// 新用户
				SiteCustomerInfo newscii = new SiteCustomerInfo();
				newscii.setExpirationTime(new Date());
				newscii.setSiteId(siteId);
				newscii.setPayWay(0);
				newscii.setCreateTime(new Timestamp(System.currentTimeMillis()));
				newscii.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				newscii.setPortalUserId(portalUser.getId());
				unitePortalServiceImpl.addCustomerInfo(newscii);
				map1.put("result", 202);
				map1.put("msg", "无信息，须购买");
			}
		} else {
			map1.put("result", 203);
			map1.put("msg", "获取用户信息异常");
		}
		log.error("判断使用时间是否到期:" + map1);
		return Json.toJson(map1);
	}

	/**
	 * 获取场所信息通过手机号
	 * 
	 * @param userName
	 * @return
	 */
	@At("/cloudSiteInfo")
	@Ok("raw:json")
	public String cloudSiteInfo(String userName) {
		List<CloudSite> cloudSites = unitePortalServiceImpl.getCloudSiteByUserName(userName);
		Map<String,Object> map = new HashMap<String,Object>();
		System.out.println(cloudSites);
		if (cloudSites != null && cloudSites.size() > 0) {
			map.put("cloudSites", cloudSites);
			map.put("code", 200);
		} else {
			map.put("code", 201);
			map.put("msg", "未获取到场所信息");
		}
		return Json.toJson(map);
	}

	/**
	 * 获取场所信息通过openId
	 * 
	 * @param userName
	 * @return
	 */
	@At("/getCloudSite")
	@Ok("raw:json")
	public String getCloudSite(String openId) {
		List<CloudSite> cloudSites = unitePortalServiceImpl.getCloudSiteByUserName(openId);
		if (cloudSites != null && cloudSites.size() > 0) {
			map1.put("cloudSites", cloudSites);
			map1.put("code", 200);
		} else {
			map1.put("code", 201);
			map1.put("msg", "未获取到场所信息");
		}
		return Json.toJson(map1);
	}

	/**
	 * 获取商户信息
	 * 
	 * @param userName
	 * @return
	 */
	@At("/getCommercialTenant")
	@Ok("raw:json")
	public String getCommercialTenant(String userName) {
		List<CommercialTenant> commercialTenant = unitePortalServiceImpl.getCommercialTenant(userName);
		if (commercialTenant != null && commercialTenant.size() > 0) {
			map1.put("commercialTenant", commercialTenant);
			map1.put("code", 200);
		} else {
			map1.put("code", 201);
			map1.put("msg", "未获取到商户信息");
		}
		return Json.toJson(map1);
	}
	
	/**
	 * 充值第一步，输入手机号
	 * 
	 * @return
	 */
	@At("/inputphone")
	@Ok("re:jsp:error/error")
	public String inputphone() {
		return "jsp:jsp/inputphone";
	}
	
	
	/**
	 * 加载页面
	 * 
	 * @return
	 */
	@At("/loading")
	@Ok("re:jsp:error/error")
	public String loading() {
		return "jsp:jsp/loading";
	}
	
	/**
	 * 充值完成
	 * 
	 * @return
	 */
	@At("/hint")
	@Ok("re:jsp:error/error")
	public ViewWrapper hint(String ordernum, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("ordernum", ordernum);
		return new ViewWrapper(new JspView("jsp/hint"), map1);
	}
	
	
	/**
	 * 个人中心数据接口
	 * 
	 * @return
	 */
	@At("/personalCenter")
	public ViewWrapper personalCenter(String userName, int siteId, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map1 = new HashMap<String,Object>();
		PortalUser user = unitePortalServiceImpl.getUserByName(userName.replace(" ", ""));
		if(user != null){
			SiteCustomerInfo customerInfo = userPayServiceImpl.getExpirationTimeByProuserid(user.getId(), siteId);
			String endTime = DateUtil.dateToStr(customerInfo.getExpirationTime());
			map1.put("username", userName);
			map1.put("date", endTime);
		}
		return new ViewWrapper(new JspView("jsp/personal_center"), map1);
	}
	
	
}
