package com.fxwx.dao;

import java.util.List;

import com.fxwx.entiy.APCashPledge;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.CloudSitePortalEntity;
import com.fxwx.entiy.GiftLength;
import com.fxwx.entiy.IdEncapsulation;
import com.fxwx.entiy.PassUrl;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SiteCustomerInfo;

public interface PersonalDao {
	
	/*根据用户名获取用户信息*/
	PortalUser getUserByNameAndPwd(String userName);
	
	/*根据用户id获取用户账户信息*/
	SiteCustomerInfo getUserSiteById(String userId,int siteId);
	
	/*完善用户信息*/
	boolean perfectInformation(PortalUser user);
	
	/*送免费时长(流量类型)*/
	boolean sendTimeFlow(SiteCustomerInfo user);
	
	/*送免费时长(时间类型)*/
	boolean sendTime(SiteCustomerInfo user);
	
	/*获得充值记录*/
	List<String> rechargeRecord(String param);
	
	/*根据用户所在场所，查询所送时长*/
	GiftLength getGiftInfo(int siteId);
	
	/*根据手机号码和场所Id查询联系人*/
	CloudSite contactCustomerService(int userId,int siteId);
    /**
     * 根据用户ID场所ID查询当前用户上网情况
     * @return
     */
	SiteCustomerInfo getLongInternettime(IdEncapsulation idEncapsulation);
	/**
	 * 根据用户表外键查询当前设备是那个场所
	 * @param siteId
	 * @return
	 */
	CloudSite getCloudSite(int siteId);
	/**
	 * 根据场所外键对应的设备类型
	 * @param pUrlId
	 * @return
	 */
	PassUrl getPassUrl(String type);
	
	/*关联老用户和场所表*/
	boolean relevanceUserAndSite(CloudSitePortalEntity cpe);
	
	/*更新用户子账号*/
	boolean updateUserSonAccount(String sonName,int userId);
	//跟新用户头像
	boolean updateUserImg(String img, int userId);
	/**
	 * 根据mac地址查询用户押金信息
	 * @param ip
	 * @return
	 */
	APCashPledge cashPledgeCheck(String apMac);
	
	/**
	 * 根据用户id查询SiteCustomerInfo
	 * @param uid
	 * @return
	 */
	SiteCustomerInfo getSiteCustomerInfo(int uid);
}
