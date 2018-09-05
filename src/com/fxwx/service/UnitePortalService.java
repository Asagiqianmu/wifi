package com.fxwx.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fxwx.entiy.AppAuthParam;
import com.fxwx.entiy.AppInfo;
import com.fxwx.entiy.AppUser;
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

import net.sf.json.JSONArray;

/**
 * 用户登录
 * 
 * @author Administrator
 *
 */
public interface UnitePortalService {

	/**
	 * 修改PortalUser对象
	 * 
	 * @param portalUser
	 * @return
	 */
	public boolean updatePortalUser(PortalUser portalUser);

	/**
	 * 注册 第一次登录：快捷登录无需密码，手机号，验证码 第二次登录：设置密码；手机号，验证码，设置登录密码,登录，更新数据库、写入缓存，
	 * 
	 * @return
	 */
	boolean insertUserRegist(PortalUser user);

	/**
	 * 更新用户最后一次登录密码，更新mac地址、另外更新缓存
	 * 
	 * @param user
	 * @param clientMac
	 * @return
	 */
	boolean updatePortalUserMac(PortalUser user);

	/**
	 * 1、根据设备mac确定当前登陆的用户是否是上次登录的用户；
	 * 
	 * @param user
	 * @return
	 */
	PortalUser getMacPortalUser(PortalUser user);

	/**
	 * 根据ID获取当前portalUser对象值；
	 * 
	 * @param user
	 * @return
	 */
	PortalUser getByIdPortalUser(PortalUser user);

	/**
	 * 根据用户查询此用户是否存在
	 * 
	 * @param user
	 * @return
	 */
	PortalUser getUserName(PortalUser user);

	/**
	 * 验证用户是否登录
	 * 
	 * @param name
	 * @param pwd
	 * @return
	 */
	PortalUser getLogin(String name, String pwd);

	/* 场所插入缓存 */
	void insertSiteCache(String key, CloudSite site);

	/* 用户插入缓存 */
	void insertUserCache(String key, PortalUser user);

	/* 根据用户名密码获取用户信息 */
	PortalUser getUserByNameAndWord(String userName, String password, int state);

	/* 根据场所id获取场所信息 */
	CloudSite getSiteById(int siteId);

	/* 查询用户是否被锁定 */
	String getlockTime(SiteCustomerInfo sci);

	/* 把老用户的关联场所表移到用户表中 */
	void relevanceSite(int userId, int siteId);

	/* 根据用户名获取用户信息 */
	PortalUser getUserByName(String userName);

	/* 用户初次登陆注册用户或用户第二次登录修改用户密码 */
	boolean updateUserPwd(PortalUser user, String userName, String passWord, int siteId, int result);

	/* 修改 */

	/**
	 * 根据设备唯一标识查询当前设备一天发送的验证码的数量
	 *
	 */
	int countPhoneMac(PhoneCode code);

	/**
	 * 根据浏览器sessionID查询这个浏览器没关情况下只能发三次
	 *
	 */
	int countPhoneCookie(PhoneCode code);

	/**
	 * 一个手机号用户一天只能发送三条短信
	 * 
	 * @return
	 */
	int countPhone(PhoneCode code);

	/**
	 * 添加插入发送验证码日志
	 */
	boolean insertPhoneCode(PhoneCode code);

	/* 获取用户到期时间 */
	SiteCustomerInfo isHaveSiteCustomerInfo(int userId, int siteId);

	/* 获取场所赠送配置 */
	GiftLength getGiftLength(int siteId);

	/* 赠送用户时长 */
	boolean giveGift(PortalUser user, int siteId, GiftLength cl, int state) throws ParseException;

	/* 查询放行命令 */
	Map<String, Object> getPassUrl(Map map, PortalUser user, long tsmp);

	/* 插入请求认证用户账号 */
	boolean insertAccount(String userName, String nasid, String mac, String ip, long tsmp, int state, String sysType, String userIp, String apmac, String ssid);

	/* 查询请求认证用户账号 */
	AuthUser selAccount(String userName);

	/* 查询请求认证用户账号 */
	List<AuthUser> selAccountByUmac_Uip_Nasid_Hour(String usermac, String userip, String nasid, int hour);

	/* 更改用户最有一次登录的mac */
	void updateUserClentMac(PortalUser user, String lastMac);

	/* 临时放行用户120s */
	void interimPass(int userId, int siteId, SiteCustomerInfo sci) throws ParseException;

	/* 获取用户到期时间表 */
	SiteCustomerInfo getsci(int userId, int siteId);

	/* 查询场所负责人联系方式 */
	CloudUser getTouch(int userId);

	/* 添加商户场所负责人联系方式 */
	boolean updateSiteAmin(CloudSite site);

	/* 判断同一个账号是否在当天超过场所设定登录不同的mac地址的次数 */
	boolean isSuperThree(String telephone, int count, String mac, SiteCustomerInfo sci);

	/* 用户场所关联表 */
	/**
	 * 创建用户和场所关联
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月15日 下午9:36:36
	 * @param userId
	 * @param siteId
	 * @return
	 */
	boolean userSiteRetance(int userId, int siteId);

	/* 用户注册时根据场所配置赠送时长 */
	boolean insertSiteAddTime(CloudSite site, PortalUser user);

	/*
	 * 添加弹portal日志
	 */
	void inserPortalLog(PortalLog portalLog);

	/**
	 * 查询关联场所
	 */
	CloudSitePortalEntity getSiteRetance(int userId, int siteId);

	/**
	 * h3c放行
	 */
	boolean passH3c(Map<String, Object> map);

	/**
	 * moto放行
	 */
	boolean passMoto(Map<String, Object> map);
	/**
	 * 敦崇放行
	 */
	boolean passDunChong(Map<String, Object> map);
	/**
	 * 拼接h3c成功后跳转url
	 */
	String h3cUrl(Map<String, Object> map);

	/**
	 * 拼接moto成功后跳转url
	 */
	String motoUrl(Map<String, Object> map);
	/**
	 * 拼接敦崇成功后跳转url
	 */
	String DunChongUrl(Map<String, Object> map);

	/**
	 * 如果用户非正常离线修改用户离线时间
	 * 
	 * @param userName
	 * @return
	 */
	boolean updateUserOnlineState(String userName);

	/**
	 * 
	 * @author:dengfei200857@163.com
	 * @date: 2017年8月18日 下午1:51:27
	 * @Description: 根据场所ID查询当前场所的认证方式
	 * @param:
	 * @return
	 */
	SitePortalAuthRetance findSitePortalAuthRetance(int siteId);

	/**
	 * 根据nasid查找认证方式
	 * 
	 * @param nasid
	 * @return
	 */
	RoutersAuthRetance findRoutersAuthRetance(String nasid);

	/**
	 * 筛选必须要的参数
	 * 
	 * @author:dengfei200857@163.com
	 * @date: 2017年8月25日 上午11:59:39
	 * @Description:
	 * @param:
	 * @return
	 */
	Map<String, Object> fifterMap(Map<String, Object> map);

	/**
	 * 
	 * @author:dengfei200857@163.com
	 * @date: 2017年8月24日 上午11:25:59
	 * @Description: 根据MAC获取openID查询是否存在
	 * @param:
	 * @return
	 */
	WcahtUserOpenid findWcahtUserOpenid(String mac, String openId,String appId,int siteId,String nasid);

	/**
	 * 
	 * @author:dengfei200857@163.com
	 * @date: 2017年8月24日 上午11:26:50
	 * @Description: 进行更新最新OpenID
	 * @param:
	 * @return
	 */
	boolean updateWcahtUserOpenid(WcahtUserOpenid wuo);

	/**
	 * 
	 * @author:dengfei200857@163.com
	 * @date: 2017年8月24日 上午11:03:35
	 * @Description: 根据MAC把对应OpenID录入
	 * @param:
	 * @return
	 */
	boolean insertWcahtUserOpenid(WcahtUserOpenid wuo);

	/**
	 * 
	 * @author:dengfei200857@163.com
	 * @date: 2017年9月4日 下午5:13:42
	 * @Description: 根据openId查询看是否存在表中
	 * @param:
	 * @return
	 */
	WcahtSubscribeUserInfo findWSUIOpenId(String openId);

	/**
	 * 
	 * @author:dengfei200857@163.com
	 * @date: 2017年9月4日 下午3:46:20
	 * @Description: 微信关注用户的详情
	 * @param:
	 * @return
	 */
	boolean insertWcahtSubscribeUserInfo(WcahtSubscribeUserInfo wsui);

	/**
	 * 获取短信发送平台
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年9月21日 下午3:34:12
	 * @return
	 */
	List<LoginSmsCodeType> getSendSmsState();

	/**
	 * 保存用户发送验证码记录
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年9月21日 下午3:37:19
	 * @param sendSmsState
	 * @return
	 */
	boolean insertSendSmsState(SendSmsState sendSmsState);

	/**
	 * 保存任子行日志记录
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年10月24日 上午10:34:17
	 * @param rxzLog
	 * @return
	 */
	boolean saveRZX_LogState(RxzLog rxzLog);

	/**
	 * 获取任子行未上传的记录
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年10月24日 下午2:06:39
	 * @return
	 */
	List<RxzLog> getNotUpLoadRxzLog();

	/**
	 * 更新记录状态
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年10月24日 下午2:12:00
	 * @param rxzLog
	 * @return
	 */
	boolean updateRxzLog(RxzLog rxzLog);

	/**
	 * 保存用户反馈记录
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年10月12日 下午3:37:19
	 * @param sendSmsState
	 * @return
	 */
	boolean insertFeedback(FeedBack feedBack);

	/**
	 * 查询ssid和nasid
	 * 
	 * @return
	 */
	List<PortalId> getAllPortalId();

	CloudSiteRouters getCloudSiteRouters(String nasid);

	CloudSite getCloudSite(String nasid);

	JSONArray getCloudSiteLikeAddres(String area);

	List<CloudSite> getHotSpot(String content);

	CloudSiteRouters getRoutersBySite_id(int site_id);

	PortalId getPortalIdByNasid(String nasid);

	// List<NewPortalId> getSsidNewPortalIdBySsid(String ssid);

	List<NewPortalId> getNewPortalIdBySsid(String ssid);

	/**
	 * 添加app认证放行参数
	 * 
	 * @param map
	 * @return
	 */
	boolean addAppAuthParam(Map<String, Object> map);

	/**
	 * 根据场景获取认证app的信息资料
	 * 
	 * @param siteId
	 * @return
	 */
	AppInfo getAppInfoBySiteId(int siteId);

	/**
	 * 新增app认证计费时长
	 * 
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public boolean addAppSiteCustomerInfo(Map<String, Object> map) throws ParseException;

	AppAuthParam getAppAuthParam(String mac);

	List<NewPortalId> getNewPortalIdBySsid(String ssid, String nasid);

	/**
	 * 统计安卓页面的pv
	 * 
	 * @param type
	 * @return
	 */
	boolean countPvAndUv(int type);

	/**
	 * 统计ios页面uv
	 * 
	 * @param mac
	 * @return
	 */
	boolean countAppUv(String mac, String appid);

	/**
	 * 统计打开app按钮点击次数
	 * 
	 * @param type
	 * @return
	 */
	boolean countAppCpc(String type);

	/**
	 * 获取APP临时放行剩余时间
	 * 
	 * @param mac
	 * @param ip
	 * @param publicAccount
	 * @return wifiip,ssid,bssid,userName
	 */
	long getTemporaryRelease(String wifiip, String ssid, String publicAccount);

	/**
	 * 根据用户的MAC获取对应APMAC
	 * 
	 * @param userMac
	 * @return
	 */
	String getRadacctAPMAC(String userMac);

	/**
	 * 添加对应MAC
	 * 
	 * @return
	 */
	boolean insertWanLanMac(WanLanMac wanLanMac);

	/**
	 * 更新wifiMAC
	 * 
	 * @return
	 */
	boolean updateWanLanMac(WanLanMac wanLanMac);

	/**
	 * 根据lsapMac获取记录
	 * 
	 * @param ssid
	 * @return
	 */
	WanLanMac getLsapMac(String lsapMac);

	/**
	 * 根据wifi MAC 获取lasapMac
	 * 
	 * @param ssid
	 * @return
	 */
	String getWanLanMac(String wifiMac);

	/**
	 * 统计安卓和ios首页pv
	 * 
	 * @param type
	 * @return
	 */
	boolean addAndroidAndIosHomePagePv(int type, String appid);

	/**
	 * 统计安卓和ios首页Uv
	 * 
	 * @param type
	 * @return
	 */
	boolean addAndroidAndIosHomePageUv(int type, String appid, String mac);

	/**
	 * 统计安卓和ios按钮pv
	 * 
	 * @param type
	 * @return
	 */
	boolean addAndroidAndIosButtonPv(int type, String appid);

	/**
	 * 统计安卓和ios按钮uv
	 * 
	 * @param type
	 * @return
	 */
	boolean addAndroidAndIosButtonUv(int type, String appid, String mac);

	/**
	 * 统计认证成功uv
	 * 
	 * @param appid
	 * @param mac
	 * @return
	 */
	boolean addAuthUserUv(String appid, String userName);

	/**
	 * 获取系统类型参数
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 上午10:41:24
	 * @return
	 */
	List<List<String>> getSystemSolarsysType();

	/**
	 * 获取系统回调类型参数
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 下午5:29:32
	 * @return
	 */
	List<List<String>> getCallBackSystemSolarsysType();

	/**
	 * 保存失败记录
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 上午10:52:46
	 * @param key
	 * @param reason
	 */
	void saveFailCount(String key, String reason);

	/**
	 * 根据SSID和Nasid获取记录的条数
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 上午10:56:20
	 * @param nasid
	 * @param ssid
	 * @return
	 */
	List<PortalId> selSsidAndNasid(String nasid, String ssid);

	/**
	 * 将ssid更新t7_portalid_type表中
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 上午11:01:17
	 * @param portalid
	 * @return
	 */
	boolean updateSSID(PortalId portalid);

	/**
	 * 根据mac获取用户
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 上午11:12:51
	 * @param mac
	 * @return
	 */
	PortalUser getUserByMac(String mac);

	/**
	 * 根据Nasid获取场所
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 下午5:06:58
	 * @param nasid
	 * @return
	 */
	CloudSite getSite(String nasid);

	/**
	 * 增加或者更改app用户
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 下午5:22:10
	 * @param openid
	 * @param openkey
	 * @param userId
	 * @return
	 */
	AppUser addOrUpdateAppUser(String openid, String openkey, int userId);

	/**
	 * 查看认证用户
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 下午5:35:17
	 * @param userName
	 * @param nasid
	 * @param userMac
	 * @return
	 */
	AuthUser selAuthUser(String userName, String nasid, String userMac);

	/**
	 * 查询发起认证放行用户
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 下午5:37:24
	 * @param param
	 * @return
	 */
	AuthUser selAuthUser(String param);
	
	/**
	 * 根据用户MAC、用户名、NASID查找当天认证记录
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年6月22日 下午1:39:27
	 * @param userName
	 * @param nasid
	 * @param userMac
	 * @return
	 */
	List<AuthUser> selAuthUserlistToday(String userName, String nasid, String userMac);
	/**
	 * 根据用户名mac查询用户信息
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 下午5:39:50
	 * @param mac
	 * @param userName
	 * @return
	 */
	PortalUser getUserByMacAndName(String mac, String userName);

	/**
	 * 放行不通过时更改ip地址
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 下午5:41:26
	 * @param nasid
	 * @param ip
	 * @return
	 */
	boolean updateIp(String nasid, String ip);

	/**
	 * 根据OpenId获取radius访问记录，nasid、framedipaddress、nasipaddress
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年1月16日 下午5:53:01
	 * @param openid
	 * @return
	 */
	List<String> getradacctInfo(String openid);
	
	boolean inserTemporaryParams(TemporaryParams temporaryParams);
	
	TemporaryParams getTemporaryParamsBykey(String key);
	
	/**
	 * 获取用户信息
	 * @param userName
	 * @param passWord
	 * @return
	 */
	PortalUser getUserByUserNameAndPwd(String userName,String passWord);
	
	boolean updateWcahtUserOpenidById(int id,int subscribe);
	/**
	 * 根据mac标识查询PhoneCode
	 * @param mac
	 * @return
	 */
	PhoneCode getPhoneCode(String mac);
	
	/**
	 * 修改到期时间
	 * @param id
	 * @param expiration_time
	 * @return
	 */
	boolean updateCustomerInfo(int id, Date expiration_time);
	
	/**
	 * 核减到期时间
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年6月22日 下午1:54:28
	 * @param id 主键
	 * @param num 数量
	 * @param unit 单位  SECOND MINUTE HOUR DAY
	 * @return
	 */
	boolean SubExpiration_time(int id, int num, String unit);
	
	/**
	 * 插入场所顾客信息
	 * @param siteCustomerInfo
	 * @return
	 */
	boolean addCustomerInfo(SiteCustomerInfo siteCustomerInfo);
	
	SitePriceConfig getSitePriceConfigById(int Id); 
	
	/**
	 * 获取场所金额信息
	 * @param siteId
	 * @return
	 */
	SitePriceConfig getSitePriceConfig(int siteId); 
	
	List<SitePriceConfig> getSitePriceMemberCombo(int siteId);
	
	boolean addSiteIncome(SiteIncome siteIncome);
	
	List<CloudSite> getCloudSiteByUserName(String userName);
	
	List<CloudSite> getCloudSiteByOpenId(String openId);
	
	boolean addCommercialTenant(CommercialTenant commercialTenant);
	
	List<CommercialTenant> getCommercialTenant(String userName); 
	
	/**
	 * 保存用户登录记录
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年7月23日 下午10:37:19
	 * @param deviceInfo
	 * @return
	 */
	void insertPortalLoginLog(PortalLoginLog portalloginlog);
	
}
