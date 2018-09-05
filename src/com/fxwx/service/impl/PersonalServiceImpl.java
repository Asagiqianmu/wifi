package com.fxwx.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.service.IdEntityService;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.fxwx.dao.UnitePortalDao;
import com.fxwx.dao.PersonalDao;
import com.fxwx.entiy.APCashPledge;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.CloudSitePortalEntity;
import com.fxwx.entiy.GiftLength;
import com.fxwx.entiy.IdEncapsulation;
import com.fxwx.entiy.PassUrl;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.entiy.UserInfo;
import com.fxwx.service.UnitePortalService;
import com.fxwx.service.PersonalService;
import com.fxwx.util.MD5;
import com.fxwx.util.MemcachedUtils;
import com.fxwx.util.SHA256;

//@IocBean(name="personalService")
@IocBean
public class PersonalServiceImpl  implements PersonalService{
	
	@Inject(value="personaldaoimpl")
	private PersonalDao personaldaoimpl;

	@Inject
	private UnitePortalService unitePortalServiceImpl; //用户接口
	
	private static final Log log = Logs.getLog(PersonalServiceImpl.class);

		
	/**
	 *根据用户名获取用户信息 
	 */
	@SuppressWarnings("unused")
	@Override
	public PortalUser getUserByNameAndWord(String userName) {
		//在缓存中查询
		List<PortalUser> listUser =null;//(List<PortalUser>) MemcachedUtils.get(userName);
		//如果缓存中存在
		if(listUser!=null){
			return listUser.get(0);
		}else{//缓存不存在，去数据库查询
			PortalUser user=personaldaoimpl.getUserByNameAndPwd(userName);
			if(user!=null){
				return  user;
			}else{
				return null;
			}
		}
	}
	
	/**
	 *根据用户ID获取用户账户信息 
	 */
	@Override
	public SiteCustomerInfo getUserSiteById(int userId,int siteId) {
		String user=userId+"";
		List<SiteCustomerInfo> listUser =null;//(List<SiteCustomerInfo>) MemcachedUtils.get(user);
		SiteCustomerInfo userSite=personaldaoimpl.getUserSiteById(user,siteId);
		if(userSite!=null){
			return userSite;
		}else{
			return null;
		}
	}
	
	/**
	 *根据用户ID获取用户账户信息 
	 */
	@Override
	public boolean perfectInformation(PortalUser userInfo) {
		return personaldaoimpl.perfectInformation(userInfo);
		
	}
	
	/**
	 *获赠的时长
	 */
	@Override
	public boolean sendTime(int userId,String giftLenth) {
		SiteCustomerInfo site=new SiteCustomerInfo();
		site.setId(userId);
		site.setExpirationTime(giftLenth);
		return personaldaoimpl.sendTime(site);
	}
	
	/**
	 *根据场所ID获取某一场所赠送时长 
	 */
	@Override
	public GiftLength getGiftInfo(int siteId) {
		GiftLength giftLength=personaldaoimpl.getGiftInfo(siteId);
		return giftLength;
	}
	
	/**
	 *获赠的流量
	 */
	@Override
	public boolean sendTimeFlow(int userId, String giftLenth) {
		SiteCustomerInfo site=new SiteCustomerInfo();
		site.setId(userId);
		site.setTotalFlow(giftLenth);
		return personaldaoimpl.sendTimeFlow(site);
	}
	/**
	 * 获取当前用户剩余的上网时长
	 */
	@Override
	public SiteCustomerInfo getLongInternettime(IdEncapsulation idEncapsulation) {
		
		return personaldaoimpl.getLongInternettime(idEncapsulation);
	}

	@Override
	public CloudSite getCloudSite(int siteId) {
		return personaldaoimpl.getCloudSite(siteId);
	}

	@Override
	public PassUrl getPassUrl(String type) {
		return personaldaoimpl.getPassUrl(type);
	}
   

	public boolean perfectUserMessage(){
		
		return false;
	}

	@Override
	public CloudSite contactCustomerService(int userId,int siteId) {
		return personaldaoimpl.contactCustomerService(userId, siteId);
	}
	/**
	 *创建子账号
	 */
	@Override
	public boolean createSonAccount(final int siteId,final String sonname, final String password,final PortalUser user) {
		try {
			Trans.exec(new Atom(){
				@Override
				public void run() {
					PortalUser u = new PortalUser();
					u.setUserName(sonname);
					u.setPassWord(SHA256.getUserPassword(sonname, MD5.encode(password).toLowerCase()));
					u.setSex(1);
					u.setSonstate(1);
					u.setSiteId(siteId);
					boolean is = unitePortalServiceImpl.insertUserRegist(u);
					if(!is){
						throw Lang.makeThrow("注册子账号失败");
					}
					CloudSitePortalEntity cpe=new CloudSitePortalEntity();
					cpe.setCreateTime(new Timestamp(new Date().getTime()));
					cpe.setPortalUserId(u.getId());
					is=personaldaoimpl.relevanceUserAndSite(cpe);
					if(!is){
						throw Lang.makeThrow("注册子账号收集场所信息失败");
					}
					String isHaveSon = user.getSonName();
					if(isHaveSon!=null&&!"".equals(isHaveSon)){
						isHaveSon=isHaveSon+","+sonname;
					}else{
						user.setSonName(sonname);
					}
					boolean isok = personaldaoimpl.updateUserSonAccount(sonname, user.getId());
					if(!isok){
						throw Lang.makeThrow("更新主账号失败");
					}
				}
			});	
		} catch (Exception e) {
			log.error("创建子账号失败",e);
			return false;
		}
		return true;
	}

	@Override
	public boolean updateUserImg(int userId, String img) {
		return personaldaoimpl.updateUserImg(img, userId);
	}
	
	@Override
	public APCashPledge cashPledgeCheck(String apMac) {
		 return personaldaoimpl.cashPledgeCheck(apMac);
	}

	@Override
	public SiteCustomerInfo getSiteCustomerInfo(int uid) {
		return personaldaoimpl.getSiteCustomerInfo(uid);
	}
}
