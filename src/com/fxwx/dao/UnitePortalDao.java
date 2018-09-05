package com.fxwx.dao;

import java.util.Date;
import java.util.List;

import com.fxwx.entiy.AppAuthParam;
import com.fxwx.entiy.AppInfo;
import com.fxwx.entiy.AppUser;
import com.fxwx.entiy.AuthType;
import com.fxwx.entiy.AuthUser;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.CloudSiteRouters;
import com.fxwx.entiy.CloudUser;
import com.fxwx.entiy.CommercialTenant;
import com.fxwx.entiy.GiftLength;
import com.fxwx.entiy.PassUrl;
import com.fxwx.entiy.PhoneCode;
import com.fxwx.entiy.PortalId;
import com.fxwx.entiy.PortalLoginLog;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.entiy.SiteIncome;
import com.fxwx.entiy.SitePriceConfig;
import com.fxwx.entiy.TemporaryParams;

/**
 * 统一Portal认证Dao类
 * 
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2018年1月15日 下午9:58:22
 */
public interface UnitePortalDao {
	/* 获取系统类型参数 */
	List<List<String>> getSolarsysType();

	/* 获取系统回调类型参数 */
	List<List<String>> getCallBackSolarsysType();

	/* 获取场所 */
	CloudSite getSite(String nasid);

	/* 根据mac获取用户信息 */
	PortalUser getUserByMac(String mac);

	/* 根据mac获取用户信息 */
	PortalUser getPortalUserByMac(String mac);

	/* 根据用户名mac查询用户信息 */
	PortalUser getUserByMacAndName(String mac, String userName);

	/* 根据用户名查询用户信息 */
	PortalUser getUserByName(String userName);

	/* 根据用户名密码回去用户信息 */
	PortalUser getUserByNameAndPwd(String userName, String pwd);

	/* 根据用户获取场所信息 */
	CloudSite getSiteByname(String userName);

	/* 根据场所id获取场所信息 */
	CloudSite getSiteById(int siteId);

	/* 获取用户锁定时间 */
	String getUserLockTime(String userName, int siteId);

	/* 关联场所到用户表 */
	void updateUserSite(int userId, int siteId);

	/* 注册用户 */
	boolean insertUser(PortalUser user);

	/* 修改用户 */
	boolean updateUser(PortalUser user);

	/* 获取用户到期时间 */
	SiteCustomerInfo getCustomerInfo(int userId, int siteId);

	/* 获取场所赠送时长 */
	GiftLength getGiftLength(int siteId);

	/* 场所赠送用户生日时长或者流量 */
	boolean insertCustomerInfo(SiteCustomerInfo sci, PortalUser user);

	/* 新增用户到期时间 */

	boolean insertCustomerInfo(SiteCustomerInfo sci);

	/* 更改用户到期时间 */
	boolean updateCustomerInfo(SiteCustomerInfo sci);

	/* 用户生日更改用户时长或者流量 */
	boolean updateCustomerInfo(SiteCustomerInfo sci, PortalUser user);

	/* 暂时废弃 查询场所放行url */
	PassUrl selPassUrlBySiteId(int siteId);

	/* 查询场所放行url */
	PassUrl selPassUrlBySolarsys(String solarsysType);

	/* 插入认证用户 */
	boolean insertAccount(AuthUser au);

	/* 查看认证用户 */
	AuthUser selAuthUser(String userName, String nasid, String userMac);
	/* 根据用户MAC、用户名、NASID查找当天认证记录 */
	List<AuthUser> selAuthUserlistToday(String userName, String nasid, String userMac);
	/* 查询认证用户 */
	String selAccount(String mac, String nasid);

	/* 根据用户MAC、用户IP、NASID、hour查找认证记录 */
	List<AuthUser> selAccountByUmac_Uip_Nasid_Hour(String usermac, String userip, String nasid, int hour);

	/* 根据用户MAC、用户IP、NASID查找认证记录 */
	List<AuthUser> selAuthuserByNasid(String nasid);

	/* 更改用户认证时间 */
	boolean updateAccount(AuthUser au);

	/* 根据nasid查询场所路由 */
	CloudSiteRouters selRouterByNasid(String nasid);

	/* 修改路由ip */
	boolean updateRouterIp(int routerId, String ip);

	/* 查询发起认证放行用户 */
	AuthUser selAuthUser(String param);

	/* 更改用户最后一次登录的设备mac */
	void updateClientMac(int userId, String clientMac);

	/* 查询商户 */
	CloudUser getTouch(int userId);

	/* 增加场所负责人联系电话 */
	boolean updateSiteTouch(CloudSite site);

	/* 查询用户账号一天登录不同的设备次数 */
	List<String> selMac(String userName);

	/* 放行不通过时更改ip地址 */
	boolean updateIp(String nasid, String ip);

	/* 增加场所用户关联表 */
	boolean addUserSiteRetance(int userId, int siteId);

	/* 根据nasid获取到该设备认证放行时的加密方式 */
	AuthType getAuthTypeByNasid(String nasid);

	/* 向数据库中存入ssid。said */
	boolean updatessid(PortalId portalid);

	/* 查询数据的条数 */
	List<PortalId> selectSsidAndNasid(String nasid, String ssid);

	/* 添加或修改app认证放行参数 */
	int addOrUpdateParam(String params, String ip);

	/* 获取app信息资料 */
	AppInfo getAppInfoBySiteId(int siteId);

	/* 根据ip获取认证所需参数 */
	AppAuthParam getAppAuthParamByIp(String ip);

	/* 增加或者更改app用户 */
	AppUser addOrUpdateAppUser(String openid, String openkey, int userId);

	/* 根据OpenId获取radius访问记录，nasid、framedipaddress、nasipaddress */
	List<String> getradacctInfo(String openid);
	
	TemporaryParams getTemporaryParamsBykey(String key);
	
	boolean updateWcahtUserOpenidById(int id,int subscribe);
	
	PhoneCode getPhoneCode(String mac);
	
	boolean updateCustomerInfo(int id,Date expiration_time);
	
	boolean SubExpiration_time(int id, int num, String unit);
	
	boolean addCustomerInfo(SiteCustomerInfo siteCustomerInfo);
	
	SitePriceConfig getSitePriceConfigById(int Id);
	
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
