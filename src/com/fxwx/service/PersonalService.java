package com.fxwx.service;

import com.fxwx.entiy.APCashPledge;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.GiftLength;
import com.fxwx.entiy.IdEncapsulation;
import com.fxwx.entiy.PassUrl;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.entiy.UserInfo;

public interface PersonalService {
	
	/*根据用户名获取用户信息*/
	PortalUser getUserByNameAndWord(String userName);
	
	/*根据用户id获取用户的账户信息*/
	SiteCustomerInfo getUserSiteById(int userId,int siteId);
	
	/*完善个人信息*/
	boolean perfectInformation(PortalUser userInfo);
	
	/*送免费时长（时长）*/
	boolean sendTime(int userId,String giftLenth);
	
	/*送免费时长(获赠流量)*/
	boolean sendTimeFlow(int userId,String giftLenth);
	
	/*根据用户所在场所，查询所送时长*/
	GiftLength getGiftInfo(int siteId);
	
	/*根据用户名和场所Id，获得联系人*/
	CloudSite contactCustomerService(int userId,int siteId);
	 /**
	  * 获取当前用户剩余的时间长
	  * @param idEncapsulation
	  * @return
	  */
	SiteCustomerInfo getLongInternettime(IdEncapsulation idEncapsulation);
	/**
	 * 根据用户的表外键查询当前设备是那个场所
	 * @param site
	 * @return
	 */
	CloudSite getCloudSite(int site);
	/**
	 * 根据场所外键对应的设备类型
	 * @param pUrlId
	 * @return
	 */
	PassUrl getPassUrl(String type);
	
	/*创建子账号*/
	boolean createSonAccount(int siteId,String sonname,String password,PortalUser user);
	/*更新用户头像*/
	boolean updateUserImg(int userId,String img);
	
	
	/**
	 * 检查用户是否已交押金
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
