package com.fxwx.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.service.IdEntityService;

import com.fxwx.dao.BaseDao;
import com.fxwx.dao.UnitePortalDao;
import com.fxwx.entiy.AppAuthParam;
import com.fxwx.entiy.AppInfo;
import com.fxwx.entiy.AppUser;
import com.fxwx.entiy.AppUv;
import com.fxwx.entiy.AuthType;
import com.fxwx.entiy.AuthUser;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.CloudSitePortalEntity;
import com.fxwx.entiy.CloudSiteRouters;
import com.fxwx.entiy.CloudUser;
import com.fxwx.entiy.CommercialTenant;
import com.fxwx.entiy.FeedBack;
import com.fxwx.entiy.GiftLength;
import com.fxwx.entiy.LoginSmsCodeType;
import com.fxwx.entiy.NewPortalId;
import com.fxwx.entiy.PassUrl;
import com.fxwx.entiy.PhoneCode;
import com.fxwx.entiy.PortalId;
import com.fxwx.entiy.PortalLog;
import com.fxwx.entiy.PortalLoginLog;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.RoutersAuthRetance;
import com.fxwx.entiy.RxzLog;
import com.fxwx.entiy.SendSmsState;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.entiy.SiteIncome;
import com.fxwx.entiy.SitePortalAuthRetance;
import com.fxwx.entiy.SitePriceConfig;
import com.fxwx.entiy.TemporaryParams;
import com.fxwx.entiy.WanLanMac;
import com.fxwx.entiy.WcahtSubscribeUserInfo;
import com.fxwx.entiy.WcahtUserOpenid;
import com.fxwx.portal.core.service.action.ReqInfo;
import com.fxwx.portal.core.service.action.v1.chap.Chap_Auth_V1;
import com.fxwx.portal.core.service.action.v1.chap.Chap_Challenge_V1;
import com.fxwx.portal.core.service.action.v1.chap.Chap_Quit_V1;
import com.fxwx.portal.core.service.action.v1.pap.PAP_Auth_V1;
import com.fxwx.portal.core.service.action.v1.pap.PAP_Quit_V1;
import com.fxwx.portal.core.service.action.v2.chap.Chap_Auth_V2;
import com.fxwx.portal.core.service.action.v2.chap.Chap_Challenge_V2;
import com.fxwx.portal.core.service.action.v2.chap.Chap_Quit_V2;
import com.fxwx.portal.core.service.action.v2.pap.PAP_Auth_V2;
import com.fxwx.portal.core.service.action.v2.pap.PAP_Quit_V2;
import com.fxwx.portal.core.service.utils.PortalUtil;
import com.fxwx.service.UnitePortalService;
import com.fxwx.util.DateUtil;
import com.fxwx.util.FileSystemProperty;
import com.fxwx.util.MD5;
import com.fxwx.util.MemcachedUtils;
import com.fxwx.util.RadMd5;
import com.fxwx.util.SHA256;
import com.fxwx.util.SetSystemProperty;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 加缓存
 * 
 * @author dengfei E-mail:dengfei200857@163.com
 * @date 创建时间：2017年5月17日 下午4:29:34
 *
 */
@IocBean(name = "unitePortalServiceImpl", fields = { "dao" })
public class UnitePortalServiceImpl extends IdEntityService<PortalUser> implements UnitePortalService {
	@Inject(value = "unite_portal_daoImpl")
	private UnitePortalDao unite_portal_daoImpl;

	@Inject(value = "baseDaoImp")
	private BaseDao baseDaoImp;

	@Inject
	private UnitePortalService unitePortalServiceImpl; // 用户接口

	private static final Log log = Logs.getLog(UnitePortalServiceImpl.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public boolean insertUserRegist(PortalUser portalUser) {
		portalUser = this.dao().insert(portalUser);
		if (portalUser != null) {
			return true;
		}
		return false;
	}

	/**
	 * 忘记密码
	 */
	public boolean updatePortalUser(PortalUser portalUser) {
		return unite_portal_daoImpl.updateUser(portalUser);
	}

	@Override
	public boolean updatePortalUserMac(PortalUser user) {
		int result = this.dao().update(user, "passWord");
		if (result != 0) {
			return true;
		}
		return false;
	}

	@Override
	public PortalUser getLogin(String name, String pwd) {
		String newPw = SHA256.getUserPassword(name, pwd);
		PortalUser porUser = this.dao().fetch(PortalUser.class, Cnd.where("user_name", "=", name).and("pass_word", "=", newPw));
		return porUser;
	}

	@Override
	public PortalUser getMacPortalUser(PortalUser user) {
		PortalUser portalUser = null;
		portalUser = this.dao().fetch(PortalUser.class, Cnd.where("client_mac", "=", user.getClientMac()));
		if (portalUser != null) {
			return portalUser;
		}
		return null;
	}

	@Override
	public PortalUser getByIdPortalUser(PortalUser user) {
		PortalUser portalUser = null;
		portalUser = this.dao().fetch(PortalUser.class, user.getId());
		if (portalUser != null) {
			return portalUser;
		}
		return null;
	}

	@Override
	public PortalUser getUserName(PortalUser user) {
		PortalUser portalUser = null;
		portalUser = this.dao().fetch(PortalUser.class, Cnd.where("user_name", "=", user.getUserName()));
		if (portalUser != null) {
			return portalUser;
		}
		return null;
	}

	/**
	 * 场所插入缓存
	 * 
	 * @param key
	 */
	public void insertSiteCache(String key, CloudSite site) {
		List<Object> list = new ArrayList<Object>();
		site.setExpireTime(sdf.format(new Date((new Date().getTime() + 30 * 24 * 60 * 60 * 1000L))));
		list.add(site);
		boolean res = MemcachedUtils.addOnly("site", key, list, 5, "site", (int) (new Date().getTime() + 30 * 24 * 60 * 60 * 1000L) / 1000);
		if (!res) {
			baseDaoImp.insertFailCount(key, "添加场所对象缓存失败");
		}
	}

	/**
	 * 用户插入缓存
	 * 
	 * @param key
	 * @param user
	 */
	public void insertUserCache(String key, PortalUser user) {
		List<Object> list = new ArrayList<Object>();
		list.add(user);
		boolean flag = MemcachedUtils.adds(key, 7 * 24 * 60 * 60, list);
		if (!flag) {
			baseDaoImp.insertFailCount(key, "添加用户对象缓存失败");
		}
	}

	/**
	 * 根据用户名密码获取用户信息
	 */
	@Override
	public PortalUser getUserByNameAndWord(String userName, String password, int state) {
		// List<PortalUser> listUser =(List<PortalUser>)
		// MemcachedUtils.get(userName);
		if (password.length() < 32)
			password = SHA256.getUserPassword(userName, MD5.encode(password).toLowerCase());
		PortalUser user = unite_portal_daoImpl.getUserByNameAndPwd(userName, password);
		if (user == null)
			return null;
		return user;
	}

	/**
	 * 根据场所id获取场所信息
	 * 
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CloudSite getSiteById(int siteId) {
		List<CloudSite> site = (List<CloudSite>) MemcachedUtils.get(siteId + "");
		CloudSite st = null;
		if (site == null) {
			st = unite_portal_daoImpl.getSiteById(siteId);
			if (st == null) {
				return null;
			} else {
				insertSiteCache(st.getId() + "", st);
				return st;
			}
		} else {
			try {
				if (sdf.parse(site.get(0).getExpireTime()).getTime() - new Date().getTime() <= 5000) {
					st = unite_portal_daoImpl.getSiteById(siteId);
					insertSiteCache(st.getId() + "", st);
				}
			} catch (ParseException e) {
				log.error("更新缓存场所失败---" + e);
			}
		}
		return site.get(0);
	}

	/**
	 * 查询用户是否被锁
	 */
	@Override
	public String getlockTime(SiteCustomerInfo sci) {
		try {
			long oneday = 12 * 60 * 60 * 1000;
			String locktime = sci == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sci.getLuckTime());
			if (locktime == null || "".equals(locktime)) {
				return null;
			} else {
				long lock = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(locktime).getTime();
				long manytime = new Date().getTime() - lock;
				if (manytime > oneday) {
					return null;
				} else {
					return DateUtil.dateDiff(oneday - manytime);
				}
			}
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void relevanceSite(int userId, int siteId) {
		unite_portal_daoImpl.updateUserSite(userId, siteId);
	}

	/**
	 * 根据用户名查询用户
	 */
	@Override
	public PortalUser getUserByName(String userName) {
		@SuppressWarnings("unchecked")
//		List<PortalUser> listUser = (List<PortalUser>) MemcachedUtils.get(userName);
//		if (listUser == null) {
			PortalUser user = unite_portal_daoImpl.getUserByName(userName);
			if (user == null) {
				return null;
			} else {
				return user;
			}
//		}
//		return listUser.get(0);
	}

	/**
	 * 注册用户或修改用户密码
	 */
	@Override
	public boolean updateUserPwd(PortalUser user, String userName, String passWord, int siteId, int result) {
		boolean falg = false;
		if (user == null) {
			user = new PortalUser();
			user.setUserName(userName);
			user.setPassWord(SHA256.getUserPassword(userName, MD5.encode(passWord).toLowerCase()));
			user.setSiteId(siteId);
			user.setSex(2);
			// 第一次登录为1
			user.setPwdState(1);
			falg = unite_portal_daoImpl.insertUser(user);
		}else{
			 user.setPassWord(
			 SHA256.getUserPassword(userName,MD5.encode(passWord).toLowerCase()));
			 //第二次登录为2
			 user.setPwdState(2);
			 falg= unite_portal_daoImpl.updateUser(user);
		}
		return falg;
	}

	/**
	 * 添加验证码日志
	 */
	@Override
	public boolean insertPhoneCode(PhoneCode code) {
		PhoneCode codes = new PhoneCode();
		codes = this.dao().insert(code);
		if (codes != null) {
			return true;
		}
		return false;
	}

	/**
	 * 查询某个设备发送验证码的数量 如果当前设备当天超过三次验证码那么为发送频繁
	 */
	@Override
	public int countPhoneMac(PhoneCode code) {
		Sql sql = Sqls.fetchInt("SELECT COUNT(*) AS number FROM $table WHERE mac = @mac and crate_time = @crateTime");
		sql.vars().set("table", "t7_user_phone_code");
		sql.params().set("mac", code.getMac());
		// 获取当前日期
		sql.params().set("crateTime", DateUtil.getDate());
		this.dao().execute(sql);
		return sql.getInt();
	}

	/**
	 * 浏览器的sessionID
	 */
	@Override
	public int countPhoneCookie(PhoneCode code) {
		Sql sql = Sqls.fetchInt("SELECT COUNT(*) AS number FROM $table WHERE crate_time = @crateTime and cookie = @cookie");
		sql.vars().set("table", "t7_user_phone_code");
		// 获取当前日期
		sql.params().set("crateTime", DateUtil.getDate());
		// 获取
		sql.params().set("cookie", code.getCookie());
		this.dao().execute(sql);
		return sql.getInt();
	}

	/**
	 * 一个手机账号当天只能发送三条短信
	 */
	@Override
	public int countPhone(PhoneCode code) {
		Sql sql = Sqls.fetchInt("SELECT COUNT(*) AS number FROM $table WHERE crate_time = @crateTime and phone = @phone");
		sql.vars().set("table", "t7_user_phone_code");
		// 获取当前日期
		sql.params().set("crateTime", DateUtil.getDate());
		// 获取手机号
		sql.params().set("phone", code.getPhone());
		this.dao().execute(sql);
		return sql.getInt();
	}

	/**
	 * @Description 获得到期时间表的数据
	 * @param portalId
	 *            用户id
	 * @param siteId
	 *            场所id
	 * @return
	 */
	public SiteCustomerInfo isHaveSiteCustomerInfo(int userId, int siteId) {
		return unite_portal_daoImpl.getCustomerInfo(userId, siteId);
	}

	/**
	 * 获取场所赠送时间
	 */
	@Override
	public GiftLength getGiftLength(int siteId) {
		return unite_portal_daoImpl.getGiftLength(siteId);
	}

	/**
	 * 赠送用户时长 1===完善资料 ，2====用户生日
	 * 
	 * @throws ParseException
	 */
	@Override
	public boolean giveGift(PortalUser user, int siteId, GiftLength gl, int state) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SiteCustomerInfo sci = this.isHaveSiteCustomerInfo(user.getId(), siteId);
		if (sci == null) {
			sci = new SiteCustomerInfo();
			sci.setCreateTime(new Timestamp(new Date().getTime()));
			sci.setSiteId(siteId);
			sci.setPortalUserId(user.getId());
			sci.setIsTry(0);
			if (state == 1) {
				if (gl.getUnit() <= 3) {// 完善资料送的是时长
					String times = DateUtil.datePlus(gl.getUnit(), gl.getGiftLength());
					sci.setExpirationTime(times);
					String days = DateUtil.dateDiff1(DateUtil.parse(times).getTime() - new Date().getTime());
					sci.setAllDay(days/* .replaceAll("[\u4e00-\u9fa5]+", ",") */);
				} else {
					if (gl.getUnit() == 4) {// 完善资料赠送的是M
						sci.setTotalFlow(String.valueOf(gl.getGiftLength() * 1024));
					} else {// 完善资料赠送的是G
						sci.setTotalFlow(String.valueOf(gl.getGiftLength() * 1024 * 1024));
					}
				}

			} else if (state == 2) {
				if (gl.getBirthdayUnit() <= 3) {// 生日赠送的是时长
					String times = DateUtil.datePlus(gl.getBirthdayUnit(), gl.getBrithdayNum());
					sci.setExpirationTime(times);
					String days = DateUtil.dateDiff1(DateUtil.parse(times).getTime() - new Date().getTime());
					sci.setAllDay(days);
				} else {
					if (gl.getBirthdayUnit() == 4) {// 生日赠送的是M
						sci.setTotalFlow(String.valueOf(gl.getBrithdayNum() * 1024));
					} else {// 生日赠送的是G
						sci.setTotalFlow(String.valueOf(gl.getBrithdayNum() * 1024 * 1024));
					}
				}
			}
			String brath = user.getBirthdate();
			String yea = brath.split("-")[0];
			int nextYea = Integer.valueOf(yea) + 1;
			user.setBirthdate(nextYea + brath.substring(brath.indexOf("-"), brath.length()));
			return unite_portal_daoImpl.insertCustomerInfo(sci, user);
		} else {
			Date now = new Date();
			String str1 = sdf.format(now.getTime());// 当前时间
			String str2 = "";
			String riqi = "";
			if (state == 1) {
				if (gl.getUnit() <= 3) {// 完善资料送的是时长
					// 如果用户有记录但是只有购买流量的记录,这时用户的过期时间为null
					if (sci.getExpirationTime() == null || sci.getExpirationTime().equals("null") || "".equals(sci.getExpirationTime())) {
						str2 = sdf.format(now.getTime());
					} else {
						str2 = sdf.format(sci.getExpirationTime().getTime());
					}
					int cmp = DateUtil.compareDate(str1, str2);
					if (cmp == 1) {
						// 到期时间小于等于当前时间时，在当前时间的基础上计算新的到期时间
						riqi = DateUtil.datePluss(gl.getUnit(), gl.getGiftLength(), str1);
						sci.setExpirationTime(riqi);
						String days = DateUtil.dateDiff1(DateUtil.parse(riqi).getTime() - new Date().getTime());
						sci.setAllDay(days);
					} else {
						// 到期时间大于当前时间时，在到期时间基础上计算新的到期时间
						riqi = DateUtil.datePluss(gl.getUnit(), gl.getGiftLength(), str2);
						sci.setExpirationTime(riqi);
						String d = DateUtil.datePluss(gl.getUnit(), gl.getGiftLength(), str1);
						String days = DateUtil.dateDiff1(DateUtil.parse(d).getTime() - new Date().getTime());
						String newDays = DateUtil.dateAdd(days, sci.getAllDay());
						sci.setAllDay(newDays);
					}
				} else {
					if (gl.getUnit() == 4) {// 完善资料赠送的是M
						sci.setTotalFlow(String.valueOf(gl.getGiftLength() * 1024 + Integer.parseInt(sci.getTotalFlow() == null ? "0" : sci.getTotalFlow())));
					} else {// 完善资料赠送的是G
						sci.setTotalFlow(String.valueOf(gl.getGiftLength() * 1024 * 1024 + Integer.parseInt(sci.getTotalFlow() == null ? "0" : sci.getTotalFlow())));
					}
				}

			} else if (state == 2) {
				if (gl.getBirthdayUnit() <= 3) {// 用户生日送的是时长
					// 如果用户有记录但是只有购买流量的记录,这时用户的过期时间为null
					if (sci.getExpirationTime() == null || sci.getExpirationTime().equals("null") || "".equals(sci.getExpirationTime())) {
						str2 = sdf.format(now.getTime());
					} else {
						str2 = sdf.format(sci.getExpirationTime().getTime());
					}
					int cmp = DateUtil.compareDate(str1, str2);
					if (cmp == 1) {
						// 到期时间小于等于当前时间时，在当前时间的基础上计算新的到期时间
						riqi = DateUtil.datePluss(gl.getBirthdayUnit(), gl.getBrithdayNum(), str1);
						sci.setExpirationTime(riqi);
						String days = DateUtil.dateDiff1(DateUtil.parse(riqi).getTime() - new Date().getTime());
						sci.setAllDay(days);
					} else {
						// 到期时间大于当前时间时，在到期时间基础上计算新的到期时间
						riqi = DateUtil.datePluss(gl.getBirthdayUnit(), gl.getBrithdayNum(), str2);
						sci.setExpirationTime(riqi);
						String d = DateUtil.datePluss(gl.getBirthdayUnit(), gl.getBrithdayNum(), str1);
						String days = DateUtil.dateDiff1(DateUtil.parse(d).getTime() - new Date().getTime());
						String newDays = DateUtil.dateAdd(days, sci.getAllDay());
						sci.setAllDay(newDays);
					}
				} else {
					if (gl.getBirthdayUnit() == 4) {// 完善资料赠送的是M
						sci.setTotalFlow(String.valueOf(gl.getBrithdayNum() * 1024 + Integer.parseInt(sci.getTotalFlow() == null ? "0" : sci.getTotalFlow())));
					} else {// 完善资料赠送的是G
						sci.setTotalFlow(String.valueOf(gl.getBrithdayNum() * 1024 * 1024 + Integer.parseInt(sci.getTotalFlow() == null ? "0" : sci.getTotalFlow())));
					}
				}
			}
			String brath = user.getBirthdate();
			String yea = brath.split("-")[0];
			int nextYea = Integer.valueOf(yea) + 1;
			user.setBirthdate(nextYea + brath.substring(brath.indexOf("-"), brath.length()));
			return unite_portal_daoImpl.updateCustomerInfo(sci, user);
		}
	}

	/**
	 * 查询放行参数
	 * 
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getPassUrl(Map map, PortalUser user, long tsmp) {
		PassUrl pu = unite_portal_daoImpl.selPassUrlBySolarsys(map.get("solarsys") + "");
		Map<String, Object> mapUrl = new HashMap<String, Object>();
		String authUrl = pu.getAuthUrl();
		String[] params = pu.getUrlParam().split(",");
		for (int i = 0; i < params.length; i++) {
			if (map.get(params[i]) != null) {
				authUrl = authUrl.replace(params[i], (String) map.get(params[i]));
			}
			if (params[i].equals("uname")) {
				authUrl = authUrl.replace(params[i], user.getUserName());
			}
			if (params[i].equals("pwd")) {
				String md5pass = user.getPassWord().length() < 10 ? user.getPassWord() : user.getPassWord().substring(0, 16);
				if (map.get("solarsys") != null && map.get("solarsys").equals("chilli")) {
					byte[] bytes = RadMd5.pack(map.get("challenge") + "");
					try {
						String newchall = RadMd5.md52("fxwxwl", bytes);
						byte[] newchallen = RadMd5.pack(newchall);
						md5pass = RadMd5.md5("\0" + md5pass, newchallen);// pc.encodePacket(Password, "testing123");
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				authUrl = authUrl.replace(params[i], md5pass);
			}
			if (authUrl.indexOf("temp") > 0) {
				authUrl = authUrl.replace("temp", tsmp + "");
			}
		}
		log.error(authUrl);
		mapUrl.put("url", authUrl);
		int res = unite_portal_daoImpl.addOrUpdateParam(authUrl, map.get("allMac") + "");
		return mapUrl;
	}

	/**
	 * 插入认证用户
	 */
	@Override
	public boolean insertAccount(String userName, String nasid, String mac, String ip, long tsmp, int state, String sysType, String userIp, String apmac, String ssid) {
		AuthUser au = new AuthUser();
		au.setMac(mac);
		au.setNasid(nasid);
		au.setUserName(userName);
		au.setAuthTime(tsmp);
		au.setCreateTime(new Timestamp(new Date().getTime()));
		au.setIp(ip);
		au.setSysType(sysType);
		au.setAuthState(state);
		au.setUserIp(userIp);
		au.setApmac(apmac);
		au.setSsid(ssid);
		return unite_portal_daoImpl.insertAccount(au);
	}

	/**
	 * 查询认证用户
	 */
	@Override
	public AuthUser selAccount(String userName) {
		return unite_portal_daoImpl.selAuthUser(userName, null, null);
	}

	/**
	 * 查询认证用户
	 */
	@Override
	public List<AuthUser> selAccountByUmac_Uip_Nasid_Hour(String usermac, String userip, String nasid, int hour) {
		return unite_portal_daoImpl.selAccountByUmac_Uip_Nasid_Hour(usermac, userip, nasid, hour);
	}

	/**
	 * 更改用户最后一次登录的设备mac
	 */
	@Override
	public void updateUserClentMac(PortalUser user, String lastMac) {
		insertUserCache(lastMac, user);
		unite_portal_daoImpl.updateClientMac(user.getId(), lastMac);
	}

	public static void main(String[] args) {
		String s = "111?333";
		System.out.println(s.substring(s.indexOf("?") + 1));
		

		int type = 1;
		int portalVer = type == 0 ? 1 : type == 1 ? 1 : 2;// 1==pap1,chap1  2===pap2,chap2
		String authType = type == 0 ? "0" : type == 2 ? "0" : "1";// 0pap 1chap
		System.out.println(portalVer+"---"+authType);
	
		String sss = SHA256.getUserPassword("15972935811", MD5.encode("2452").toLowerCase());
		System.out.println(sss);
	}

	//
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
	 * 临时放行用户120s
	 * 
	 * @throws ParseException
	 */
	@Override
	public void interimPass(int userId, int siteId, SiteCustomerInfo sci) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calender = Calendar.getInstance();
		calender.setTime(new Date());
		calender.add(Calendar.MINUTE, 3);
		String exTime = sdf.format(calender.getTime());
		if (sci == null) {
			sci = new SiteCustomerInfo();
			sci.setCreateTime(new Timestamp(new Date().getTime()));
			sci.setExpirationTime(exTime);
			sci.setPassAccount(1);
			sci.setSiteId(siteId);
			sci.setPortalUserId(userId);
			sci.setAllDay(DateUtil.dateDiff1(3 * 60 * 1000));
			sci.setPassTime(DateUtil.getDate());
			unite_portal_daoImpl.insertCustomerInfo(sci);
		} else {
			if (DateUtil.getDate().equals(sci.getPassTime()) && sci.getPassAccount() < 3) {// 当天时间为超过三次
				sci.setPassAccount(sci.getPassAccount() + 1);
				sci.setAllDay(DateUtil.dateDiff1(3 * 60 * 1000));
				sci.setExpirationTime(exTime);
				unite_portal_daoImpl.updateCustomerInfo(sci);
			} else if (!DateUtil.getDate().equals(sci.getPassTime())) {// 不是同一天
				sci.setPassAccount(1);
				sci.setPassTime(DateUtil.getDate());
				sci.setAllDay(DateUtil.dateDiff1(3 * 60 * 1000));
				sci.setExpirationTime(exTime);
				unite_portal_daoImpl.updateCustomerInfo(sci);
			}
		}
	}

	/**
	 * 获取用户到期时间信息
	 */
	@Override
	public SiteCustomerInfo getsci(int userId, int siteId) {
		return this.isHaveSiteCustomerInfo(userId, siteId);
	}

	/**
	 * 获取场所联系人
	 */
	@Override
	public CloudUser getTouch(int uId) {
		return unite_portal_daoImpl.getTouch(uId);
	}

	/**
	 * 增加场所负责人联系电话
	 */
	@Override
	public boolean updateSiteAmin(CloudSite site) {
		return unite_portal_daoImpl.updateSiteTouch(site);
	}

	/**
	 * 判断同一个账号是否在当天超过场所设定登录不同的mac地址的次数
	 */
	@Override
	public boolean isSuperThree(String telephone, int count, String mac, SiteCustomerInfo sci) {
		List<String> ls = unite_portal_daoImpl.selMac(telephone);
		int num = 0;
		// 如果已经登录的mac数小于等于场所设定的登录设备数则比较已登录的mac是否和现登录的设备mac
		if (ls.size() <= count) {
			for (int i = 0; i < ls.size(); i++) {
				if (!ls.get(i).replace(":", "").replace("-", "").equals(mac.replace(":", "").replace("-", ""))) {
					num++;
				}
			}
			// 如果不同的记录数和查询出的记录数一样,则代表该用户此次登陆用的是另外一台设备锁定
			// 如果比较之后的结果大于场所设定的终端数则添加锁定时间
			if (num == ls.size() && (1 + ls.size()) > count) {
				sci.setLuckTime(new Date());
				return unite_portal_daoImpl.updateCustomerInfo(sci);
			}
			// if((num+ls.size())>count){
			// sci.setLuckTime(new Date());
			// return unite_portal_daoImpl.updateCustomerInfo(sci);
			// }
		} else {
			sci.setLuckTime(new Date());
			return unite_portal_daoImpl.updateCustomerInfo(sci);
		}
		return false;
	}

	/**
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @version 创建时间：2017年6月6日 上午8:56:23
	 * @describe 用户场所关联表
	 * @parameter
	 * @return
	 */
	@Override
	public boolean userSiteRetance(int userId, int siteId) {
		return unite_portal_daoImpl.addUserSiteRetance(userId, siteId);
	}

	/**
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @version 创建时间：2017年6月6日 上午9:33:11
	 * @describe 用户注册时根据场所配置赠送时长
	 * @parameter
	 * @return
	 */
	@Override
	public boolean insertSiteAddTime(CloudSite site, PortalUser user) {
		try {
			if (site.getIs_probative() != 0) {
				SiteCustomerInfo sci = new SiteCustomerInfo();
				sci.setCreateTime(new Timestamp(new Date().getTime()));
				sci.setSiteId(site.getId());
				sci.setPortalUserId(user.getId());
				sci.setIsTry(0);
				String times = DateUtil.datePlus(0, site.getIs_probative());
				sci.setExpirationTime(times);
				String days = DateUtil.dateDiff1(DateUtil.parse(times).getTime() - new Date().getTime());
				sci.setAllDay(days/* .replaceAll("[\u4e00-\u9fa5]+", ",") */);
				return unite_portal_daoImpl.insertCustomerInfo(sci, null);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;

	}

	@Override
	public void inserPortalLog(PortalLog portalLog) {
		String tableName = "t2_portallog_" + DateUtil.getDateNoPs();
		Sql insertSql = Sqls.create("insert into " + tableName
				+ "(terminaldevice,requesttime,routermac,clientmac,routerip,clientip,routertype,url,nasid) values (@terminaldevice,@requesttime,@routermac,@clientmac,@routerip,@clientip,@routertype,@url,@nasid)");
		try {
			insertSql.setParam("terminaldevice", portalLog.getTerminalDevice());
			insertSql.setParam("requesttime", portalLog.getRequestTime());
			insertSql.setParam("routermac", portalLog.getRouterMac());
			insertSql.setParam("clientmac", portalLog.getClientMac());
			insertSql.setParam("routerip", portalLog.getRouterIp());
			insertSql.setParam("clientip", portalLog.getClientIp());
			insertSql.setParam("routertype", portalLog.getRouterType());
			insertSql.setParam("url", portalLog.getUrl());
			insertSql.setParam("nasid", portalLog.getNasid());
			this.dao().execute(insertSql);

		} catch (Exception e) {
			Sql createSql = Sqls.create("CREATE TABLE " + tableName
					+ " (terminaldevice varchar(1000) DEFAULT NULL,requesttime datetime DEFAULT NULL,routermac varchar(32) DEFAULT NULL,clientmac varchar(64) DEFAULT NULL,routerip varchar(32) DEFAULT NULL,clientip varchar(32) DEFAULT NULL,routertype varchar(15) DEFAULT NULL,url varchar(2000) DEFAULT NULL,nasid varchar(32) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8");
			this.dao().execute(createSql);
			// if(createSql.equals()){}
			// 创建成功进行插入
			insertSql.setParam("terminaldevice", portalLog.getTerminalDevice());
			insertSql.setParam("requesttime", portalLog.getRequestTime());
			insertSql.setParam("routermac", portalLog.getRouterMac());
			insertSql.setParam("clientmac", portalLog.getClientMac());
			insertSql.setParam("routerip", portalLog.getRouterIp());
			insertSql.setParam("clientip", portalLog.getClientIp());
			insertSql.setParam("routertype", portalLog.getRouterType());
			insertSql.setParam("url", portalLog.getUrl());
			insertSql.setParam("nasid", portalLog.getNasid());
			this.dao().execute(insertSql);
		}
	}

	@Override
	public CloudSitePortalEntity getSiteRetance(int userId, int siteId) {
		return this.dao().fetch(CloudSitePortalEntity.class, Cnd.where("portal_id", "=", userId).and("site_id", "=", siteId));
	}

	/**
	 * h3c特殊处理放行,后期优化
	 */
	@Override
	public boolean passH3c(Map<String, Object> map) {

		String nasid = map.get("nasid") + "";
		AuthType at = unite_portal_daoImpl.getAuthTypeByNasid(nasid);
		int type = at.getSecreType();
		int portalVer = type == 0 ? 1 : type == 1 ? 1 : 2;// 1==pap1,chap1
		// 2===pap2,chap2
		String authType = type == 0 ? "0" : type == 2 ? "0" : "1";// 0pap 1chap
		String Action = map.get("action") + "";
		String userName = map.get("userName") + "";
		String passWord = map.get("passWord") + "";
		String ip = map.get("userip") + "";
		String basIp = map.get("basip") + "";
		// String apmac = map.get("apmac") + "";
		String basname = map.get("basname") + "";
		String bas = basname;
		int basPort = 2000;
		String sharedSecret = "fxwxwl";// radius密钥
		if (at == null || at.getSecreType() == 0)
			authType = "0";
		int timeoutSec = 3;
		String basipT = DomainToIP(basIp);
		basIp = basipT;
		short SerialNo_Short = (short) (int) (1.0D + Math.random() * 32767D);
		byte SerialNo[] = PortalUtil.SerialNo(SerialNo_Short);
		byte UserIP[] = new byte[4];
		String ips[] = ip.split("[.]");
		for (int i = 0; i < 4; i++) {
			int m = NumberUtils.toInt(ips[i]);
			byte b = (byte) m;
			UserIP[i] = b;
		}
		if (authType.equals("0") && portalVer == 1) {
			if (Action.equals("PORTAL_LOGIN")) {
				if (bas.equals("1")) {
					byte Ack_info[] = ReqInfo.reqInfo(basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret, 1);
					if (Ack_info.length == 1)
						return Boolean.valueOf(false);
				}
				return Boolean.valueOf(PAP_Auth_V1.auth(basIp, basPort, timeoutSec, userName, passWord, SerialNo, UserIP));
			} else {
				return Boolean.valueOf(PAP_Quit_V1.quit(0, basIp, basPort, timeoutSec, SerialNo, UserIP));
			}
		}
		if ((authType.equals("0")) && (portalVer == 2)) {

			if (Action.equals("PORTAL_LOGIN")) {
				if (bas.equals("1")) {

					byte[] Ack_info = ReqInfo.reqInfo(basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret, 1);

					if (Ack_info.length == 1) {
						return Boolean.valueOf(false);
					}
				}
				return Boolean.valueOf(PAP_Auth_V2.auth(basIp, basPort, timeoutSec, userName, passWord, SerialNo, UserIP, sharedSecret));
			}
			return Boolean.valueOf(PAP_Quit_V2.quit(0, basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret));
		}

		if ((authType.equals("1")) && (portalVer == 2)) {
			return Boolean.valueOf(Portal_V2(Action, userName, passWord, basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret, SerialNo_Short, ip, bas));
		}
		if (authType.equals("1") && portalVer == 1)
			return Boolean.valueOf(Portal_V1(Action, userName, passWord, basIp, basPort, timeoutSec, SerialNo, UserIP, ip, bas, sharedSecret));
		return Boolean.valueOf(false);
	}

	/**
	 * h3c特殊处理放行,后期优化
	 */
	@Override
	public boolean passMoto(Map<String, Object> map) {

		String nasid = map.get("allNasid") + "";
		AuthType at = unite_portal_daoImpl.getAuthTypeByNasid(nasid);
		int type = at.getSecreType();
		int portalVer = type == 0 ? 1 : type == 1 ? 1 : 2;// 1==pap1,chap1
		// 2===pap2,chap2
		String authType = type == 0 ? "0" : type == 2 ? "0" : "1";// 0pap 1chap
		String Action = map.get("action") + "";
		String userName = map.get("userName") + "";
		String passWord = map.get("passWord") + "";
		String ip = map.get("wlanuserip") + "";
		String basIp = map.get("wlanacip") + "";
		// String apmac=map.get("apmac")+"";
		String basname = map.get("wlanacname") + "";
		String bas = basname;
		int basPort = 2000;
		String sharedSecret = "fxwxwl";// radius密钥
		if (at == null || at.getSecreType() == 0)
			authType = "0";
		int timeoutSec = 3;
		String basipT = DomainToIP(basIp);
		basIp = basipT;
		short SerialNo_Short = (short) (int) (1.0D + Math.random() * 32767D);
		byte SerialNo[] = PortalUtil.SerialNo(SerialNo_Short);
		byte UserIP[] = new byte[4];
		String ips[] = ip.split("[.]");
		for (int i = 0; i < 4; i++) {
			int m = NumberUtils.toInt(ips[i]);
			byte b = (byte) m;
			UserIP[i] = b;
		}
		if (authType.equals("0") && portalVer == 1) {
			if (Action.equals("PORTAL_LOGIN")) {
				if (bas.equals("1")) {
					byte Ack_info[] = ReqInfo.reqInfo(basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret, 1);
					if (Ack_info.length == 1)
						return Boolean.valueOf(false);
				}
				log.error("=-------------------=-=--PAP_Auth_V1.auth");
				return Boolean.valueOf(PAP_Auth_V1.auth(basIp, basPort, timeoutSec, userName, passWord, SerialNo, UserIP));
			} else {
				return Boolean.valueOf(PAP_Quit_V1.quit(0, basIp, basPort, timeoutSec, SerialNo, UserIP));
			}
		}
		if ((authType.equals("0")) && (portalVer == 2)) {

			if (Action.equals("PORTAL_LOGIN")) {
				if (bas.equals("1")) {

					byte[] Ack_info = ReqInfo.reqInfo(basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret, 1);

					if (Ack_info.length == 1) {
						return Boolean.valueOf(false);
					}
				}
				log.error("=-------------------=-=--PAP_Auth_V2.auth");
				return Boolean.valueOf(PAP_Auth_V2.auth(basIp, basPort, timeoutSec, userName, passWord, SerialNo, UserIP, sharedSecret));
			}
			return Boolean.valueOf(PAP_Quit_V2.quit(0, basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret));
		}

		if ((authType.equals("1")) && (portalVer == 2)) {
			log.error("=-------------------=-=--Portal_V2");
			return Boolean.valueOf(Portal_V2(Action, userName, passWord, basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret, SerialNo_Short, ip, bas));
		}
		if (authType.equals("1") && portalVer == 1) {
			log.error("=-------------------=-=--Portal_V1");
			return Boolean.valueOf(Portal_V1(Action, userName, passWord, basIp, basPort, timeoutSec, SerialNo, UserIP, ip, bas, sharedSecret));
		}
		return Boolean.valueOf(false);
	}
	/**
	 * 敦崇 特殊处理放行,后期优化
	 */
	@Override
	public boolean passDunChong(Map<String, Object> map) {
		//默认将敦崇AP认证方式设定位CHAP V1
		String nasid = map.get("allNasid") + "";
		AuthType at = unite_portal_daoImpl.getAuthTypeByNasid(nasid);
		int type = at.getSecreType();
		int portalVer = type == 0 ? 1 : type == 1 ? 1 : 2;// 1==pap1,chap1  2===pap2,chap2
		String authType = type == 0 ? "0" : type == 2 ? "0" : "1";// 0pap 1chap
		String Action = map.get("action") + "";
		String userName = map.get("userName") + "";
		String passWord = map.get("passWord") + "";
		String ip = map.get("userip") + "";
		String basIp = map.get("allIp") + "";
		String usermac = map.get("usermac")+"";
		String basname = map.get("acname") + "";
		String bas = basname;
		int basPort = 2000;
		String sharedSecret = "fxwxwl";// radius密钥
		if (at == null || at.getSecreType() == 0)
			authType = "0";
		int timeoutSec = 3;
		String basipT = DomainToIP(basIp);
		basIp = basipT;
		short SerialNo_Short = (short) (int) (1.0D + Math.random() * 32767D);
		byte SerialNo[] = PortalUtil.SerialNo(SerialNo_Short);
		byte UserIP[] = new byte[4];
		String ips[] = ip.split("[.]");
		for (int i = 0; i < 4; i++) {
			int m = NumberUtils.toInt(ips[i]);
			byte b = (byte) m;
			UserIP[i] = b;
		}
//		if (authType.equals("0") && portalVer == 1) {
//			if (Action.equals("PORTAL_LOGIN")) {
//				if (bas.equals("1")) {
//					byte Ack_info[] = ReqInfo.reqInfo(basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret, 1);
//					if (Ack_info.length == 1)
//						return Boolean.valueOf(false);
//				}
//				log.error("=-------------------=-=--PAP_Auth_V1.auth");
//				return Boolean.valueOf(PAP_Auth_V1.dunchong_auth(basIp, basPort, timeoutSec, usermac, userName, passWord, SerialNo, UserIP));
//			} else {
//				return Boolean.valueOf(PAP_Quit_V1.quit(0, basIp, basPort, timeoutSec, SerialNo, UserIP));
//			}
//		}
//		if ((authType.equals("0")) && (portalVer == 2)) {
//
//			if (Action.equals("PORTAL_LOGIN")) {
//				if (bas.equals("1")) {
//
//					byte[] Ack_info = ReqInfo.reqInfo(basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret, 1);
//
//					if (Ack_info.length == 1) {
//						return Boolean.valueOf(false);
//					}
//				}
//				log.error("=-------------------=-=--PAP_Auth_V2.auth");
//				return Boolean.valueOf(PAP_Auth_V2.auth(basIp, basPort, timeoutSec, userName, passWord, SerialNo, UserIP, sharedSecret));
//			}
//			return Boolean.valueOf(PAP_Quit_V2.quit(0, basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret));
//		}
//
//		if ((authType.equals("1")) && (portalVer == 2)) {
//			log.error("=-------------------=-=--Portal_V2");
//			return Boolean.valueOf(Portal_V2(Action, userName, passWord, basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret, SerialNo_Short, ip, bas));
//		}
		if (authType.equals("1") && portalVer == 1) {
			log.error("=--------敦崇-----------=-=--Portal_V1");
			return Boolean.valueOf(Dunchong_Portal_V1(Action, userName, passWord, usermac, basIp, basPort, timeoutSec, SerialNo, UserIP, ip, bas, sharedSecret));
		}
		return Boolean.valueOf(false);
	}
	
	private static boolean Dunchong_Portal_V1(String Action, String userName, String passWord, String userMac, String basIp, int basPort, int timeoutSec, byte SerialNo[], byte UserIP[], String ip, String bas,
			String sharedSecret) {
		byte ReqID[] = new byte[2];
		if (Action.equals("PORTAL_LOGIN")) {
			if (bas.equals("1")) {
				byte Ack_info[] = ReqInfo.reqInfo(basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret, 1);
				if (Ack_info.length == 1)
					return false;
			}
			log.error("发送Ack_Challenge的参数(包括请求头部信息)====basIp:" + basIp + ",basPort:" + basPort + ",timeoutSec:" + timeoutSec+ ",SerialNo:" + new String(SerialNo) + ",UserIP:" + new String(UserIP) + ",userMac:" + userMac);
			byte Challenge[] = new byte[16];
			byte Ack_Challenge_V1[] = Chap_Challenge_V1.DunChongAction(basIp, basPort, timeoutSec, SerialNo, UserIP, userMac.getBytes());
			if (Ack_Challenge_V1.length == 2) {
				ReqID = Ack_Challenge_V1;
				Chap_Quit_V1.DunChong_quit(1, basIp, basPort, timeoutSec, SerialNo, UserIP, ReqID, userMac.getBytes());
				return false;
			}
			ReqID[0] = Ack_Challenge_V1[6];
			ReqID[1] = Ack_Challenge_V1[7];
			log.error("发送计费请求之前的参数(包括请求头部信息)====basIp:" + basIp + ",basPort:" + basPort + ",timeoutSec:" + timeoutSec + ",userName:" + userName + ",passWord:" + passWord + ",userMac:" + userMac + ",SerialNo:"
					+ new String(SerialNo) + ",UserIP:" + new String(UserIP) + ",ReqID:" + new String(ReqID) + ",Challenge:" + new String(Challenge));
			for (int i = 0; i < 16; i++) {
				Challenge[i] = Ack_Challenge_V1[18 + i];
			}

			byte Ack_Auth_V1[] = Chap_Auth_V1.DunChong_auth(basIp, basPort, timeoutSec, userName, passWord, userMac, SerialNo, UserIP, ReqID, Challenge);
			if ((Ack_Auth_V1[0] & 0xff) != 20 && (Ack_Auth_V1[0] & 0xff) != 22) {
				Chap_Quit_V1.DunChong_quit(2, basIp, basPort, timeoutSec, SerialNo, UserIP, ReqID, userMac.getBytes());
				return false;
			} else {
				return true;
			}
		} else {
			return Chap_Quit_V1.DunChong_quit(0, basIp, basPort, timeoutSec, SerialNo, UserIP, ReqID, userMac.getBytes());
		}
	}
	
	private static boolean Portal_V1(String Action, String userName, String passWord, String basIp, int basPort, int timeoutSec, byte SerialNo[], byte UserIP[], String ip, String bas,
			String sharedSecret) {
		byte ReqID[] = new byte[2];
		if (Action.equals("PORTAL_LOGIN")) {
			if (bas.equals("1")) {
				byte Ack_info[] = ReqInfo.reqInfo(basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret, 1);
				if (Ack_info.length == 1)
					return false;
			}
			byte Challenge[] = new byte[16];
			byte Ack_Challenge_V1[] = Chap_Challenge_V1.Action(basIp, basPort, timeoutSec, SerialNo, UserIP);
			if (Ack_Challenge_V1.length == 2) {
				ReqID = Ack_Challenge_V1;
				Chap_Quit_V1.quit(1, basIp, basPort, timeoutSec, SerialNo, UserIP, ReqID);
				return false;
			}
			ReqID[0] = Ack_Challenge_V1[6];
			ReqID[1] = Ack_Challenge_V1[7];
			log.error("发送计费请求之前的参数(包括请求头部信息)====basIp:" + basIp + ",basPort:" + basPort + ",timeoutSec:" + timeoutSec + ",userName:" + userName + ",passWord:" + passWord + ",SerialNo:"
					+ new String(SerialNo) + ",UserIP:" + new String(UserIP) + ",ReqID:" + new String(ReqID) + ",Challenge:" + new String(Challenge));
			for (int i = 0; i < 16; i++) {
				Challenge[i] = Ack_Challenge_V1[18 + i];
			}

			byte Ack_Auth_V1[] = Chap_Auth_V1.auth(basIp, basPort, timeoutSec, userName, passWord, SerialNo, UserIP, ReqID, Challenge);
			if ((Ack_Auth_V1[0] & 0xff) != 20 && (Ack_Auth_V1[0] & 0xff) != 22) {
				Chap_Quit_V1.quit(2, basIp, basPort, timeoutSec, SerialNo, UserIP, ReqID);
				return false;
			} else {
				return true;
			}
		} else {
			return Chap_Quit_V1.quit(0, basIp, basPort, timeoutSec, SerialNo, UserIP, ReqID);
		}
	}

	private static boolean Portal_V2(String Action, String userName, String passWord, String basIp, int basPort, int timeoutSec, byte[] SerialNo, byte[] UserIP, String sharedSecret,
			short SerialNo_Short, String ip, String bas) {

		byte[] ReqID = new byte[2];
		if (Action.equals("PORTAL_LOGIN")) {
			if (bas.equals("1")) {

				byte[] Ack_info = ReqInfo.reqInfo(basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret, 1);
				if (Ack_info.length == 1) {
					return false;
				}

			}
			byte[] Challenge = new byte[16];
			byte[] Ack_Challenge_V2 = Chap_Challenge_V2.challenge(basIp, basPort, timeoutSec, SerialNo, UserIP, sharedSecret);
			if (Ack_Challenge_V2.length == 1) {
				Chap_Quit_V2.quit(1, basIp, basPort, timeoutSec, SerialNo, UserIP, ReqID, sharedSecret);
				return false;
			}
			ReqID[0] = Ack_Challenge_V2[6];
			ReqID[1] = Ack_Challenge_V2[7];
			for (int i = 0; i < 16; i++) {
				Challenge[i] = Ack_Challenge_V2[(34 + i)];
			}
			byte[] Ack_Auth_V2 = Chap_Auth_V2.auth(basIp, basPort, timeoutSec, userName, passWord, SerialNo, UserIP, ReqID, Challenge, sharedSecret);
			if (((Ack_Auth_V2[0] & 0xFF) != 20) && ((Ack_Auth_V2[0] & 0xFF) != 22)) {
				Chap_Quit_V2.quit(2, basIp, basPort, timeoutSec, SerialNo, UserIP, ReqID, sharedSecret);
				return false;
			}
			return true;
		}
		return Chap_Quit_V2.quit(0, basIp, basPort, timeoutSec, SerialNo, UserIP, ReqID, sharedSecret);
	}

	private static String DomainToIP(String domain) {
		String ip = "";
		try {
			ip = InetAddress.getByName(domain).toString().split("/")[1];
		} catch (UnknownHostException e) {
			log.error("DomainToIP ERROR!!");
			log.error("==============ERROR Start=============");
			log.error(e);
			log.error("ERROR INFO ", e);
			log.error("==============ERROR End=============");
		}
		log.error((new StringBuilder("Domain:")).append(domain).append(" IP:").append(ip).toString());
		return ip;
	}

	/**
	 * 拼接h3c放行url
	 */
	@Override
	public String h3cUrl(Map<String, Object> map) {
		String url = null;
		if (map.get("key") != null) {
			String httpurl = map.get("httpurl") + "";
			String keys = map.get("key") + "";
			String usermac = map.get("usermac") + "";
			url = httpurl + "/" + SetSystemProperty.getValue("UnitePortal") + "/login/unifyingTurn?" + keys + "&h3c=success&rep=200&check=ok&uMac=" + usermac;
		} else {
			String ip = map.get("userip") + "";
			String basIp = map.get("basip") + "";
			String apmac = map.get("apmac") + "";
			String nasid = map.get("nasid") + "";
			String type = map.get("solarsys") + "";
			String ssid = map.get("ssid") + "";
			String usermac = map.get("usermac") + "";
			String httpurl = map.get("httpurl") + "";
			url = httpurl + "/" + SetSystemProperty.getValue("UnitePortal") + "/login/unifyingTurn?userip=" + ip + "&nasid=" + nasid + "&ssid=" + ssid + "&apmac=" + apmac + "&basip=" + basIp
					+ "&usermac=" + usermac + "&h3c=success&rep=200";
		}

		return url;
	}

	/**
	 * 拼接moto放行url
	 */
	@Override
	public String motoUrl(Map<String, Object> map) {
		String url = null;
		if (map.get("key") != null) {
			String httpurl = map.get("httpurl") + "";
			String keys = map.get("key") + "";
			String wlanusermac = map.get("wlanusermac") + "";
			url = httpurl + "/" + SetSystemProperty.getValue("UnitePortal") + "/login/unifyingTurn?" + keys + "&moto=success&rep=200&check=ok&uMac=" + wlanusermac;
		} else {
			String wlanuserip = map.get("wlanuserip") + "";
			String wlanacip = map.get("wlanacip") + "";
			String wlanacname = map.get("wlanacname") + "";
			String type = map.get("solarsys") + "";
			String ssid = map.get("ssid") + "";
			String wlanusermac = map.get("wlanusermac") + "";
			String httpurl = map.get("httpurl") + "";
			url = httpurl + "/" + SetSystemProperty.getValue("UnitePortal") + "/login/unifyingTurn?wlanacname=" + wlanacname + "&wlanuserip=" + wlanuserip + "&wlanacip=" + wlanacip + "&ssid=" + ssid
					+ "&wlanusermac=" + wlanusermac + "&moto=success&rep=200";
		}
		return url;
	}
	/**
	 * 拼接DunChong放行url
	 */
	@Override
	public String DunChongUrl(Map<String, Object> map) {
		String url = null;
		if (map.get("key") != null) {
			String httpurl = map.get("httpurl") + "";
			String keys = map.get("key") + "";
			String usermac = map.get("usermac") + "";
			url = httpurl + "/" + SetSystemProperty.getValue("UnitePortal") + "/login/unifyingTurn?" + keys + "&dunchong=success&rep=200&check=ok&uMac=" + usermac;
		} else {
			String userip = map.get("userip") + "";
			String wlanacip = map.get("wlanacip") + "";
			String acname = map.get("acname") + "";
			String type = map.get("solarsys") + "";
			String ssid = map.get("ssid") + "";
			String usermac = map.get("usermac") + "";
			String httpurl = map.get("httpurl") + "";
			url = httpurl + "/" + SetSystemProperty.getValue("UnitePortal") + "/login/unifyingTurn?acname=" + acname + "&userip=" + userip + "&wlanacip=" + wlanacip + "&ssid=" + ssid
					+ "&usermac=" + usermac + "&dunchong=success&rep=200";
		}
		return url;
	}
	/**
	 * 如果用户非正常离线修改用户离线时间
	 */
	@Override
	public boolean updateUserOnlineState(String userName) {
		Sql sql = Sqls.create(
				"UPDATE radacct SET acctstoptime=NOW() WHERE acctstoptime IS  NULL AND DATE_ADD(acctupdatetime,INTERVAL 10 MINUTE)<NOW() AND username=@username ORDER BY radacctid DESC LIMIT 1");
		sql.setParam("username", userName);
		this.dao().execute(sql);
		return false;
	}

	@Override
	public SitePortalAuthRetance findSitePortalAuthRetance(int siteId) {
		return this.dao().fetch(SitePortalAuthRetance.class, Cnd.where("siteId", "=", siteId));
	}

	@Override
	public RoutersAuthRetance findRoutersAuthRetance(String nasid) {
		return this.dao().fetch(RoutersAuthRetance.class, Cnd.where("nasid", "=", nasid));
	}

	@Override
	public Map<String, Object> fifterMap(Map<String, Object> map) {
		String solarsys = map.get("solarsys") + "";
		if (solarsys.equals("ikuai")) {
			return map;
		} else if (solarsys.equals("chilli")) {
			Map<String, Object> map1 = new HashMap<String, Object>();
			PassUrl pu = unite_portal_daoImpl.selPassUrlBySolarsys(map.get("solarsys") + "");
			String authUrl = pu.getAuthUrl();
			String[] params = pu.getUrlParam().split(",");
			for (int i = 0; i < params.length; i++) {
				if (map.get(params[i]) != null) {
					authUrl = authUrl.replace(params[i], (String) map.get(params[i]));
					map1.put(params[i], map.get(params[i]));
				}
			}
			map1.put("challenge", map.get("challenge"));
			map1.put("solarsys", solarsys);
			map1.put("allMac", map.get("allMac"));
			return map1;
		} else if (solarsys.equals("ros")) {
			return map;
		} else if (solarsys.equals("h3c")) {
			return map;
		}
		return null;
	}

	@Override
	public WcahtUserOpenid findWcahtUserOpenid(String mac, String openId,String appId,int siteId,String nasid) {
		/*String sqls = "SELECT * FROM t8_wcaht_user_openid WHERE mac = @mac and appId=@appId and nasid=@nasid and site_id=@siteId  ";
		if (openId != null) {
			sqls += " OR openId = @openId";
		}
		sqls+=" and order by create_time desc limit 0,1";
		Sql sql = Sqls.create(sqls);
		if (openId != null) {
			sql.setParam("openId", openId);
		}
		sql.setParam("mac", mac);
		sql.setParam("appId",appId);
		sql.setParam("nasid",nasid);
		sql.setParam("siteId",siteId);*/
		 
		String	sqls="SELECT * FROM t8_wcaht_user_openid WHERE mac = '"+mac+"' AND appId='"+appId+"' AND nasid='"+nasid+"' AND site_id="+siteId+" ORDER BY create_time DESC LIMIT 0,1";
		log.error("========================findWcahtUserOpenid==="+sqls);
		Sql sql = Sqls.create(sqls);
		sql.setCallback(Sqls.callback.entity());
		Entity<WcahtUserOpenid> entity = this.dao().getEntity(WcahtUserOpenid.class);
		sql.setEntity(entity);
		this.dao().execute(sql);
		log.error("========================findWcahtUserOpenid==="+sqls);
		return sql.getObject(WcahtUserOpenid.class);
	}

	@Override
	public boolean updateWcahtUserOpenid(WcahtUserOpenid wuo) {
		int i = this.dao().update(wuo);
		if (i > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean insertWcahtUserOpenid(WcahtUserOpenid wuo) {
		wuo = this.dao().insert(wuo);
		if (wuo != null) {
			return true;
		}
		return false;
	}

	@Override
	public WcahtSubscribeUserInfo findWSUIOpenId(String openId) {
		String sqls = "SELECT * FROM t8_wcaht_subscribe_user_info WHERE openid = @openid";
		Sql sql = Sqls.create(sqls);
		if (openId != null) {
			sql.setParam("openid", openId);
		}
		sql.setCallback(Sqls.callback.entity());
		Entity<WcahtSubscribeUserInfo> entity = this.dao().getEntity(WcahtSubscribeUserInfo.class);
		sql.setEntity(entity);
		this.dao().execute(sql);
		return sql.getObject(WcahtSubscribeUserInfo.class);
	}

	@Override
	public boolean insertWcahtSubscribeUserInfo(WcahtSubscribeUserInfo wsui) {
		wsui = this.dao().insert(wsui);
		if (wsui != null) {
			return true;
		}
		return false;
	}

	@Override
	public List<LoginSmsCodeType> getSendSmsState() {
		// TODO Auto-generated method stub
		String sqls = "SELECT * FROM t9_login_smscode_type WHERE state = 1";
		Sql sql = Sqls.create(sqls);
		sql.setCallback(Sqls.callback.entities());
		Entity<LoginSmsCodeType> entity = this.dao().getEntity(LoginSmsCodeType.class);
		sql.setEntity(entity);
		this.dao().execute(sql);
		return sql.getList(LoginSmsCodeType.class);
	}

	@Override
	public boolean insertSendSmsState(SendSmsState sendSmsState) {
		// TODO Auto-generated method stub
		sendSmsState = this.dao().insert(sendSmsState);
		if (sendSmsState != null) {
			return true;
		}
		return false;
	}

	public boolean insertFeedback(FeedBack feedBack) {
		// TODO Auto-generated method stub
		feedBack = this.dao().insert(feedBack);
		if (feedBack != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean saveRZX_LogState(RxzLog rxzLog) {
		// TODO Auto-generated method stub
		rxzLog = this.dao().insert(rxzLog);
		if (rxzLog != null) {
			return true;
		}
		return false;
	}

	@Override
	public List<RxzLog> getNotUpLoadRxzLog() {
		// TODO Auto-generated method stub
		String sqls = "SELECT * FROM t9_rzx_log WHERE t_state = 1 AND mac IS NOT NULL AND ap_mac IS NOT NULL";
		Sql sql = Sqls.create(sqls);
		sql.setCallback(Sqls.callback.entities());
		Entity<RxzLog> entity = this.dao().getEntity(RxzLog.class);
		sql.setEntity(entity);
		this.dao().execute(sql);
		return sql.getList(RxzLog.class);
	}

	@Override
	public boolean updateRxzLog(RxzLog rxzLog) {
		// TODO Auto-generated method stub
		int i = this.dao().update(rxzLog);
		if (i > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<PortalId> getAllPortalId() {
		// TODO Auto-generated method stub
		String sqls = "SELECT * FROM t7_portalid_type ";
		Sql sql = Sqls.create(sqls);
		sql.setCallback(Sqls.callback.entities());
		Entity<PortalId> entity = this.dao().getEntity(PortalId.class);
		sql.setEntity(entity);
		this.dao().execute(sql);
		return sql.getList(PortalId.class);
	}

	@Override
	public CloudSiteRouters getCloudSiteRouters(String nasid) {
		String sqls = "SELECT * FROM t_cloud_site_routers WHERE nasid=@nasid";
		// sqls.setParam("nasid", nasid);
		Sql sql = Sqls.create(sqls);
		if (nasid != null) {
			sql.setParam("nasid", nasid);
		}
		sql.setCallback(Sqls.callback.entity());
		Entity<CloudSiteRouters> entity = this.dao().getEntity(CloudSiteRouters.class);
		sql.setEntity(entity);
		this.dao().execute(sql);
		return sql.getObject(CloudSiteRouters.class);
	}

	@Override
	public CloudSite getCloudSite(String nasid) {
		String sqls = "SELECT * FROM `t_cloud_site` WHERE id = (SELECT site_id FROM t_cloud_site_routers WHERE nasid=@nasid)";
		Sql sql = Sqls.create(sqls);
		if (nasid != null) {
			sql.setParam("nasid", nasid);
		}
		sql.setCallback(Sqls.callback.entity());
		Entity<CloudSite> entity = this.dao().getEntity(CloudSite.class);
		sql.setEntity(entity);
		this.dao().execute(sql);
		return sql.getObject(CloudSite.class);
	}

	@Override
	public JSONArray getCloudSiteLikeAddres(String area) {
		String sqls = "SELECT a.site_name,a.address,c.ssid,c.nasid FROM t_cloud_site AS a INNER JOIN t_cloud_site_routers AS b ON a.id=b.site_id RIGHT JOIN t7_portalid_type AS c ON b.nasid=c.nasid WHERE a.address LIKE @area GROUP BY ssid";
		Sql sql = Sqls.create(sqls);
		sql.params().set("area", "%" + area + "%");
		// sql.setCallback(Sqls.callback.entities());
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				JSONArray array = new JSONArray();
				JSONObject json = new JSONObject();
				while (rs.next()) {
					json.put("ssid", rs.getString("ssid"));// WIFI名称
					json.put("address", rs.getString("address"));// 地址
					json.put("site_name", rs.getString("site_name"));// 地点名称
					array.add(json);
				}
				return array;
			}
		});
		// Entity<CloudSite> entity = this.dao().getEntity(CloudSite.class);
		// sql.setEntity(entity);
		this.dao().execute(sql);
		return sql.getObject(JSONArray.class);
	}

	@Override
	public List<CloudSite> getHotSpot(String content) {
		String sqls = "SELECT * FROM `t_cloud_site` WHERE site_name like@site_name";
		Sql sql = Sqls.create(sqls);
		sql.params().set("site_name", "%" + content + "%");
		sql.setCallback(Sqls.callback.entities());
		Entity<CloudSite> entity = this.dao().getEntity(CloudSite.class);
		sql.setEntity(entity);
		this.dao().execute(sql);
		return sql.getList(CloudSite.class);
	}

	@Override
	public CloudSiteRouters getRoutersBySite_id(int site_id) {
		String sqls = "SELECT * FROM t_cloud_site_routers WHERE site_id=@site_id";
		Sql sql = Sqls.create(sqls);
		sql.setParam("site_id", site_id);
		sql.setCallback(Sqls.callback.entity());
		Entity<CloudSiteRouters> entity = this.dao().getEntity(CloudSiteRouters.class);
		sql.setEntity(entity);
		this.dao().execute(sql);
		return sql.getObject(CloudSiteRouters.class);
	}

	@Override
	public PortalId getPortalIdByNasid(String nasid) {
		String sqls = "SELECT * FROM t7_portalid_type WHERE nasid=@nasid";
		Sql sql = Sqls.create(sqls);
		sql.setParam("nasid", nasid);
		sql.setCallback(Sqls.callback.entity());
		Entity<PortalId> entity = this.dao().getEntity(PortalId.class);
		sql.setEntity(entity);
		this.dao().execute(sql);
		return sql.getObject(PortalId.class);
	}

	@Override
	public List<NewPortalId> getNewPortalIdBySsid(String ssid, String nasid) {
		String sqls = "SELECT * FROM t7_newPortalid WHERE ssid=@ssid AND nasid=@nasid";
		Sql sql = Sqls.create(sqls);
		sql.setParam("ssid", ssid).setParam("nasid", nasid);
		sql.setCallback(Sqls.callback.entities());
		Entity<NewPortalId> entity = this.dao().getEntity(NewPortalId.class);
		sql.setEntity(entity);
		this.dao().execute(sql);
		return sql.getList(NewPortalId.class);
	}

	@Override
	public boolean addAppAuthParam(Map<String, Object> map) {
		JSONObject jsonObject = JSONObject.fromObject(map);
		int res = unite_portal_daoImpl.addOrUpdateParam(jsonObject.toString(), map.get("allMac") + "");
		return res > 0 ? true : false;
	}

	@Override
	public AppInfo getAppInfoBySiteId(int siteId) {
		return unite_portal_daoImpl.getAppInfoBySiteId(siteId);
	}

	@Override
	public boolean addAppSiteCustomerInfo(Map<String, Object> map) throws ParseException {

		/***************** 因无手机号暂时封掉后打开该接口 *************************/
		boolean res = false;
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// Calendar calender = Calendar.getInstance();
		// calender.setTime(new Date());
		// int onlineTime=Integer.parseInt(map.get("duration")+"");
		// calender.add(Calendar.SECOND, onlineTime);
		// String exTime = sdf.format(calender.getTime());
		// int userId=Integer.parseInt(map.get("userId")+"");
		// int siteId= Integer.parseInt(map.get("siteId")+"");
		// SiteCustomerInfo sci=this.getsci(userId,0);
		// if (sci == null) {
		// sci = new SiteCustomerInfo();
		// sci.setCreateTime(new Timestamp(new Date().getTime()));
		// sci.setExpirationTime(exTime);
		// sci.setPassAccount(1);
		// sci.setSiteId(0);
		// sci.setPortalUserId(userId);
		// sci.setAllDay(DateUtil.dateDiff1(onlineTime*60* 60 * 1000));
		// sci.setPassTime(DateUtil.getDate());
		// res=unite_portal_daoImpl.insertCustomerInfo(sci);
		// } else {
		// sci.setAllDay(DateUtil.dateDiff1(onlineTime*60* 60 * 1000));
		// sci.setExpirationTime(exTime);
		// res=unite_portal_daoImpl.updateCustomerInfo(sci);
		// }
		/***************** 因无手机号暂时封掉后打开该接口 *************************/
		int i = this.dao().update(AppUser.class, Chain.make("online_time", map.get("duration") == null ? -1 : map.get("duration")), Cnd.where("id", "=", map.get("userId")));
		return i > 0 ? true : false;
	}

	@Override
	public AppAuthParam getAppAuthParam(String mac) {
		return unite_portal_daoImpl.getAppAuthParamByIp(mac);
	}

	@Override
	public List<NewPortalId> getNewPortalIdBySsid(String ssid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean countPvAndUv(int type) {
		String selSql = "update t8_pv SET pv=pv+1";
		selSql = "INSERT INTO t8_pv (appid,pv,create_time) VALUES(@appid,'1',@time) ON DUPLICATE KEY UPDATE pv=pv+1";
		Sql sql = Sqls.create(selSql);
		sql.setParam("appid", FileSystemProperty.propertyName("appid")).setParam("time", DateUtil.getDate());
		this.dao().execute(sql);
		int i = sql.getUpdateCount();
		if (i > 0) {
			sql = Sqls.create("UPDATE t8_app_cpc SET android_count=android_count-1 WHERE appid=@appid AND create_time=@time");
			sql.setParam("appid", FileSystemProperty.propertyName("appid")).setParam("time", DateUtil.getDate());
			this.dao().execute(sql);
		}
		return sql.getUpdateCount() > 0 ? true : false;
	}

	@Override
	public boolean countAppUv(String mac, String appid) {
		AppUv auv = this.dao().fetch(AppUv.class, Cnd.where("mac", "=", mac).and("create_time", "=", DateUtil.getDate()));
		if (auv == null) {
			auv = new AppUv();
			auv.setAppId(appid);
			auv.setCreateTime(DateUtil.getDate());
			auv.setMac(mac);
			auv = this.dao().insert(auv);
		}
		if (auv.getId() > 0) {
			Sql sql = Sqls.create("UPDATE t8_app_cpc SET ios_count=ios_count-1 WHERE appid=@appid AND create_time=@time");
			sql.setParam("appid", appid).setParam("time", DateUtil.getDate());
			this.dao().execute(sql);
		}
		return auv.getId() > 0 ? true : false;
	}

	@Override
	public boolean countAppCpc(String type) {
		String getSql = "";
		switch (Integer.parseInt(type)) {
		case 1:
			getSql = "INSERT INTO t8_app_cpc (appid,android_count,ios_count,create_time) VALUES(@appid,1,0,@time) ON DUPLICATE KEY UPDATE android_count=android_count+1";
			break;
		default:
			getSql = "INSERT INTO t8_app_cpc (appid,android_count,ios_count,create_time) VALUES(@appid,0,1,@time) ON DUPLICATE KEY UPDATE ios_count=ios_count+1";
			break;
		}
		Sql sql = Sqls.create(getSql);
		sql.setParam("appid", FileSystemProperty.propertyName("appid")).setParam("time", DateUtil.getDate());
		this.dao().execute(sql);
		return sql.getUpdateCount() > 0 ? true : false;
	}

	@Override
	public long getTemporaryRelease(String wifiip, String ssid, String publicAccount) {
		// 查询当前用户是否是已认证用户还是临时放行用户
		String sql1 = "SELECT username FROM radacct WHERE calledstationid LIKE '%" + ssid + "' AND framedipaddress=@wifiip and acctstoptime is null ";
		Sql sqls1 = Sqls.create(sql1);
		String calledstationid = ssid;
		// sqls1.params().set("calledstationid",calledstationid);
		sqls1.params().set("wifiip", wifiip);
		sqls1.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				String username = "";
				while (rs.next()) {
					username = rs.getString("username");// 用户名
				}
				return username;
			}
		});
		this.dao().execute(sqls1);
		String usernameType = sqls1.getObject(String.class);// 用户名
		if (usernameType == null || usernameType.equals(""))
			return 0;
		if (usernameType.equals(publicAccount)) {// 判断如果是公众账号则为临时放行账号
			String sqls = "SELECT acctstarttime FROM radacct WHERE calledstationid  LIKE '%" + ssid + "' AND framedipaddress=@wifiip and  username=@publicAccount  and acctstoptime is null ";
			Sql sql = Sqls.create(sqls);
			// String calledstationid = bssid+":"+ssid;
			// sql.params().set("calledstationid",calledstationid);
			sql.params().set("wifiip", wifiip);
			sql.params().set("publicAccount", publicAccount);
			sql.setCallback(new SqlCallback() {
				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					String times = "";
					while (rs.next()) {
						times = rs.getString("acctstarttime");// 上线时间
					}
					return times;
				}
			});
			this.dao().execute(sql);
			String statTime = sql.getObject(String.class);// 开始时间
			if (statTime == null || statTime.equals(""))
				return 0;
			// 获取已经上网的时常
			long hasTime = DateUtil.subtractTime(DateUtil.getStringDate(), DateUtil.getStringFromString(statTime));
			if (hasTime < 0)
				return 0;
			// 获取临时放行总时长
			String appid = FileSystemProperty.propertyName("appid");
			String sqlAppId = "SELECT  temporary_time as temporaryTime from t8_app_info where appid=@appid";
			Sql sqlApp = Sqls.create(sqlAppId);
			sqlApp.params().set("appid", appid);
			sqlApp.setCallback(new SqlCallback() {
				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					String temporaryTime = "";
					while (rs.next()) {
						temporaryTime = rs.getString("temporaryTime");// 上线时间
					}
					return temporaryTime;
				}
			});
			// 总时间-使用=剩余时长
			this.dao().execute(sqlApp);
			String temporaryTime = sqlApp.getObject(String.class);// 开始时间
			long surplusTime = 0;
			if (temporaryTime != "" && !temporaryTime.equals("")) {
				long a = Integer.parseInt(temporaryTime) * 60 * 1000;
				// 剩余时长
				if (a < 0) {
					surplusTime = 0;
				} else {
					surplusTime = (a - hasTime) / 1000;
				}
			}
			return surplusTime;
		} else {
			// 则为已认证用户
			return -1;
		}
	}

	@Override
	public String getRadacctAPMAC(String userMac) {
		// 查询当前用户是否是已认证用户还是临时放行用户
		String sql1 = "SELECT calledstationid FROM radacct WHERE callingstationid=@userMac AND acctstoptime IS NULL ORDER BY  radacctid  DESC  LIMIT 1";
		Sql sqls1 = Sqls.create(sql1);
		sqls1.params().set("userMac", userMac);
		sqls1.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				String calledstationid = "";
				while (rs.next()) {
					calledstationid = rs.getString("calledstationid");// 用户名
				}
				return calledstationid;
			}
		});
		this.dao().execute(sqls1);
		String apMac = sqls1.getObject(String.class);// 用户名
		return apMac;
	}

	@Override
	public boolean insertWanLanMac(WanLanMac wanLanMac) {
		wanLanMac = this.dao().insert(wanLanMac);
		if (wanLanMac != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateWanLanMac(WanLanMac wanLanMac) {
		return this.dao().update(wanLanMac) > 0 ? true : false;
	}

	@Override
	public WanLanMac getLsapMac(String lsapMac) {
		return this.dao().fetch(WanLanMac.class, Cnd.where("lasap_mac", "=", lsapMac));
	};

	@Override
	public String getWanLanMac(String wifiMac) {
		// SELECT * FROM t9_wan_lan_mac where wifi_mac LIKE
		// '%70-8A-09-9D-95-B4%'
		// 查询当前用户是否是已认证用户还是临时放行用户
		String sql1 = "SELECT lasap_mac FROM t9_wan_lan_mac where wifi_mac LIKE '%" + wifiMac + "%'";
		Sql sqls1 = Sqls.create(sql1);
		sqls1.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				String lasapMac = "";
				while (rs.next()) {
					lasapMac = rs.getString("lasap_mac");// 用户名
				}
				return lasapMac;
			}
		});
		this.dao().execute(sqls1);
		return sqls1.getObject(String.class);// 用户名
	}

	@Override
	public boolean addAndroidAndIosHomePagePv(int type, String appid) {
		if (type == -1)
			return false;
		String sql = "INSERT INTO t9_pv_uv (appid,android_homepage_pv,create_time) VALUES (@appid,1,@time) ON DUPLICATE KEY UPDATE android_homepage_pv=android_homepage_pv+1";
		if (type == 2)
			sql = "INSERT INTO t9_pv_uv (appid,ios_homepage_pv,create_time) VALUES (@appid,1,@time) ON DUPLICATE KEY UPDATE ios_homepage_pv=ios_homepage_pv+1";
		Sql sqls = Sqls.create(sql);
		sqls.setParam("appid", appid).setParam("time", DateUtil.getDate());
		this.dao().execute(sqls);
		return sqls.getUpdateCount() > 0 ? true : false;
	}

	@Override
	public boolean addAndroidAndIosHomePageUv(int type, String appid, String mac) {
		if (type == -1)
			return false;
		String d = DateUtil.getDate();
		String getSql = "SELECT clientmac FROM t2_portallog_" + d.replaceAll("-", "") + " WHERE clientmac=@mac AND DATE_FORMAT(requesttime,'%Y-%m-%d')=@time LIMIT 1";
		Sql sql = Sqls.create(getSql);
		sql.setParam("mac", mac).setParam("time", d);
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection arg0, ResultSet rs, Sql arg2) throws SQLException {
				String clientmac = "";
				while (rs.next()) {
					clientmac = rs.getString("clientmac");// 用户名
				}
				return clientmac;
			}
		});
		this.dao().execute(sql);
		List<String> re=sql.getList(String.class);
		if(re!=null&&!re.equals("")&&!re.equals("null")&&re.size()>1)
			return true;// 根据用户mac查到记录则不记录首页的uv量
		String insertSql = "INSERT INTO t9_pv_uv (appid,android_homepage_uv,create_time) VALUES (@appid,1,@time) ON DUPLICATE KEY UPDATE android_homepage_uv=android_homepage_uv+1";
		if (type == 2)
			insertSql = "INSERT INTO t9_pv_uv (appid,ios_homepage_uv,create_time) VALUES (@appid,1,@time) ON DUPLICATE KEY UPDATE ios_homepage_uv=ios_homepage_uv+1";
		sql = Sqls.create(insertSql);
		sql.setParam("appid", appid).setParam("time", d);
		this.dao().execute(sql);
		return sql.getUpdateCount() > 0 ? true : false;
	}

	@Override
	public boolean addAndroidAndIosButtonPv(int type, String appid) {
		if (type == -1)
			return false;
		String insertSql = "INSERT INTO t9_pv_uv (appid,android_button_pv,create_time) VALUES (@appid,1,@time) ON DUPLICATE KEY UPDATE android_button_pv=android_button_pv+1";
		if (type == 2)
			insertSql = "INSERT INTO t9_pv_uv (appid,ios_button_pv,create_time) VALUES (@appid,1,@time) ON DUPLICATE KEY UPDATE ios_button_pv=ios_button_pv+1";
		Sql sql = Sqls.create(insertSql);
		sql.setParam("appid", appid).setParam("time", DateUtil.getDate());
		this.dao().execute(sql);
		return sql.getUpdateCount() > 0 ? true : false;
	}

	@Override
	public boolean addAndroidAndIosButtonUv(int type, String appid, String mac) {
		if (type == -1)
			return false;
		// getSql="SELECT id FROM t7_authuser WHERE mac=@mac AND
		// DATE_FORMAT(create_time,'%Y-%m-%d')=@time LIMIT 1";
		String getSql = "SELECT id FROM t9_app_click_user WHERE client_mac=@mac AND DATE_FORMAT(create_time,'%Y-%m-%d')=@time AND appid=@appid limit 1";
		Sql sql = Sqls.create(getSql);
		sql.setParam("mac", mac).setParam("time", DateUtil.getDate()).setParam("appid", appid);
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection arg0, ResultSet rs, Sql arg2) throws SQLException {
				String id = "";
				while (rs.next()) {
					id = rs.getString("id");// 用户名
				}
				return id;
			}
		});
		this.dao().execute(sql);
		String re = sql.getObject(String.class);
		if (re != null && !re.equals("") && !re.equals("null"))
			return true;// 根据用户mac查到记录则不记录按钮的uv量
		// 不想使用触发器，暂时这样处理 插入用户点击按钮记录表
		String insertSql = "INSERT INTO t9_app_click_user(appid,client_mac,create_time) VALUES(@appid,@mac,@time)";
		sql = Sqls.create(insertSql);
		sql.setParam("appid", appid).setParam("mac", mac).setParam("time", DateUtil.getDate());
		this.dao().execute(sql);
		if (sql.getUpdateCount() > 0) {// 插入成功则记录按钮uv
			insertSql = "INSERT INTO t9_pv_uv (appid,android_button_uv,jump_down__page_pv,create_time) VALUES (@appid,1,1,@time) ON DUPLICATE KEY UPDATE android_button_uv=android_button_uv+1,jump_down__page_pv=jump_down__page_pv+1";
			if (type == 2)
				insertSql = "INSERT INTO t9_pv_uv (appid,ios_button_uv,jump_down__page_pv,create_time) VALUES (@appid,1,1,@time) ON DUPLICATE KEY UPDATE ios_button_uv=ios_button_uv+1,jump_down__page_pv=jump_down__page_pv+1";
			sql = Sqls.create(insertSql);
			sql.setParam("appid", appid).setParam("time", DateUtil.getDate());
			this.dao().execute(sql);
			return sql.getUpdateCount() > 0 ? true : false;
		}
		return false;
	}

	@Override
	public boolean addAuthUserUv(String appid, String userName) {
		String getSql = "SELECT radacctid FROM radacct WHERE username=@username LIMIT 1";
		Sql sql = Sqls.create(getSql);
		sql.setParam("username", userName);
		sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection arg0, ResultSet rs, Sql arg2) throws SQLException {
				String id = "";
				while (rs.next()) {
					id = rs.getString("radacctid");// 用户名
				}
				return id;
			}
		});
		this.dao().execute(sql);
		String re = sql.getObject(String.class);
		if (re != null && !re.equals("") && !re.equals("null"))
			return true;// 根据用户mac查到记录则不记录认证人数的uv量
		String insertSql = "INSERT INTO t9_pv_uv (appid,auth_user_uv,create_time) VALUES (@appid,1,@time) ON DUPLICATE KEY UPDATE auth_user_uv=auth_user_uv+1";
		sql = Sqls.create(insertSql);
		sql.setParam("appid", appid).setParam("time", DateUtil.getDate());
		this.dao().execute(sql);
		return sql.getUpdateCount() > 0 ? true : false;
	}


	@Override
	public List<List<String>> getSystemSolarsysType() {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.getSolarsysType();
	}

	@Override
	public List<List<String>> getCallBackSystemSolarsysType() {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.getCallBackSolarsysType();
	}

	@Override
	public void saveFailCount(String key, String reason) {
		// TODO Auto-generated method stub
		baseDaoImp.insertFailCount(key, reason);
	};

	@Override
	public List<PortalId> selSsidAndNasid(String nasid, String ssid) {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.selectSsidAndNasid(nasid, ssid);
	}

	@Override
	public boolean updateSSID(PortalId portalid) {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.updatessid(portalid);
	}

	@Override
	public PortalUser getUserByMac(String mac) {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.getUserByMac(mac);
	}

	@Override
	public CloudSite getSite(String nasid) {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.getSite(nasid);
	}

	@Override
	public AppUser addOrUpdateAppUser(String openid, String openkey, int userId) {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.addOrUpdateAppUser(openid, openkey, userId);
	}

	@Override
	public AuthUser selAuthUser(String userName, String nasid, String userMac) {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.selAuthUser(userName, nasid, userMac);
	}
	@Override
	public AuthUser selAuthUser(String param) {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.selAuthUser(param);
	}
	@Override
	public List<AuthUser> selAuthUserlistToday(String userName, String nasid, String userMac) {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.selAuthUserlistToday(userName, nasid, userMac);
	}
	@Override
	public PortalUser getUserByMacAndName(String mac, String userName) {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.getUserByMacAndName(mac, userName);
	}
	@Override
	public boolean updateIp(String nasid, String ip) {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.updateIp(nasid, ip);
	}
	@Override
	public List<String> getradacctInfo(String openid) {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.getradacctInfo(openid);
	}
	@Override
	public boolean inserTemporaryParams(TemporaryParams temporaryParams) {
		// TODO Auto-generated method stub
		temporaryParams = this.dao().insert(temporaryParams);
		if (temporaryParams != null) {
			return true;
		}
		return false;
	}
	
	@Override
	public TemporaryParams getTemporaryParamsBykey(String key) {
		return unite_portal_daoImpl.getTemporaryParamsBykey(key);
	}

	/**
	 * 获取用户信息
	 */
	@Override
	public PortalUser getUserByUserNameAndPwd(String userName, String passWord) {
		return unite_portal_daoImpl.getUserByNameAndPwd(userName,passWord);
	}

	@Override
	public boolean updateWcahtUserOpenidById(int id, int subscribe) {
		return unite_portal_daoImpl.updateWcahtUserOpenidById(id,subscribe);
	}
	
	public PhoneCode getPhoneCode(String mac) {
		return unite_portal_daoImpl.getPhoneCode(mac);
	}

	@Override
	public boolean updateCustomerInfo(int id, Date expiration_time) {
		return unite_portal_daoImpl.updateCustomerInfo(id,expiration_time);
	}
	
	@Override
	public boolean SubExpiration_time(int id, int num, String unit) {
		// TODO Auto-generated method stub
		return unite_portal_daoImpl.SubExpiration_time(id, num, unit);
	}
	
	/**
	 * 插入场所顾客信息
	 */
	@Override
	public boolean addCustomerInfo(SiteCustomerInfo siteCustomerInfo) {
		return unite_portal_daoImpl.addCustomerInfo(siteCustomerInfo);
	}

	@Override
	public SitePriceConfig getSitePriceConfigById(int Id) {
		return	unite_portal_daoImpl.getSitePriceConfigById(Id);
	}
	
	@Override
	public SitePriceConfig getSitePriceConfig(int siteId) {
		return	unite_portal_daoImpl.getSitePriceConfig(siteId);
	}

	@Override
	public List<SitePriceConfig> getSitePriceMemberCombo(int siteId) {
		return unite_portal_daoImpl.getSitePriceMemberCombo(siteId);
	}

	@Override
	public boolean addSiteIncome(SiteIncome siteIncome) {
		return unite_portal_daoImpl.addSiteIncome(siteIncome);
	}

	/**
	 * 通过手机号获取场所信息
	 */
	@Override
	public List<CloudSite> getCloudSiteByUserName(String userName) {
		return unite_portal_daoImpl.getCloudSiteByUserName(userName);
	}

	@Override
	public List<CloudSite> getCloudSiteByOpenId(String openId) {
		return unite_portal_daoImpl.getCloudSiteByOpenId(openId);
	}

	@Override
	public boolean addCommercialTenant(CommercialTenant commercialTenant) {
		return unite_portal_daoImpl.addCommercialTenant(commercialTenant);
	}

	@Override
	public List<CommercialTenant> getCommercialTenant(String userName) {
		return unite_portal_daoImpl.getCommercialTenant(userName);
	}

	@Override
	public void insertPortalLoginLog(PortalLoginLog portalloginlog) {
		// TODO Auto-generated method stub
		unite_portal_daoImpl.insertPortalLoginLog(portalloginlog);
	}

}
