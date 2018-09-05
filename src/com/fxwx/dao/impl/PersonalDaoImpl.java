package com.fxwx.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.FieldFilter;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

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
@IocBean(name="personaldaoimpl")
public class PersonalDaoImpl implements PersonalDao {
	private static final Log log = Logs.getLog(PersonalDaoImpl.class);

	@Inject
	private Dao dao;

	@Inject
	private Dao readDao;

	/**
	 * 根据用户名查询用户信息
	 */
	@Override
	public PortalUser getUserByNameAndPwd(String userName) {
		PortalUser porUser = dao.fetch(PortalUser.class,
				Cnd.where("user_name", "=", userName));
		return porUser;
	}
	/**
	 * 根据用户id查询用户账户信息
	 */
	@Override
	public SiteCustomerInfo getUserSiteById(String userId,int siteId) {
		SiteCustomerInfo porUserSite = dao.fetch(SiteCustomerInfo.class,
				Cnd.where("portal_user_id", "=", userId).and("site_id", "=", siteId));
		return porUserSite;

	}

	/**
	 * 完善个人信息
	 */
	@Override
	public boolean perfectInformation(PortalUser user) {
		return dao.update(user)>0?true:false;
	}

	/**
	 * 送免费时长
	 */
	@Override
	public boolean sendTime(SiteCustomerInfo user) {
		Sql sql=Sqls.create("UPDATE t_site_customer_info SET expiration_time=@expiration_time WHERE id=@id");
		sql.setParam("expiration_time", user.getExpirationTime()).setParam("id",user.getId());
		try {
			dao.execute(sql);
			int result=sql.getUpdateCount();
			if(result>0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			log.error("送时长失败",e);
			return false;
		}

	}

	/**
	 * 获得充值记录
	 */
	@Override
	public List<String> rechargeRecord(String param) {
		try {

			Sql sql=Sqls.create(" (SELECT order_num,product_id,unit_price,buy_num,total_amount,statue,create_time,product_name from t_order WHERE locate(user_id,@param)) order left join t_product  product on product.id=order.product_id order by create_time desc");
			sql.setParam("param", param);
			sql.setCallback(new SqlCallback() {
				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql)
						throws SQLException {
					List<String> list= new ArrayList<String>();
					while (rs.next()) {
						list.add(rs.getString("sys_param"));
						list.add(rs.getString("param_type"));
						list.add(rs.getString("sys_type"));
						list.add(rs.getString("sys_index"));
					}
					return list;
				}
			});
			dao.execute(sql);
			return sql.getList(String.class);
		} catch (Exception e) {
			log.error("获取系统类型参数错误",e);
			return null;
		}
	}

	/**
	 * 获取某一场所的赠送时长
	 */
	@Override
	public GiftLength getGiftInfo(int siteId) {
		GiftLength giftLength = dao.fetch(GiftLength.class,
				Cnd.where("site_id", "=", siteId));
		return giftLength;
	}

	/**
	 * 赠送的流量
	 */
	@Override
	public boolean sendTimeFlow(SiteCustomerInfo user) {
		Sql sql=Sqls.create("UPDATE t_site_customer_info SET total_flow=@total_flow WHERE id=@id");
		sql.setParam("total_flow", user.getTotalFlow()).setParam("id",user.getId());
		try {
			dao.execute(sql);
			int result=sql.getUpdateCount();
			if(result>0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			log.error("送流量失败",e);
			return false;
		}

	}

	/**
	 * 获取用户剩余上网的相关费用情况
	 */
	@Override
	public SiteCustomerInfo getLongInternettime(IdEncapsulation idEncapsulation) {
		SiteCustomerInfo scis = null;
		try {
			scis = this.dao.fetch(SiteCustomerInfo.class,Cnd.where("portal_user_id", "=", idEncapsulation.getUserId()).and("site_id","=",idEncapsulation.getSiteId()));
		} catch (Exception e) {
			log.error(this.getClass().getCanonicalName(), e);
			return null;
		}
		return scis;
	}

	/**
	 * 根据用户名和场所Id获得联系人
	 */
	@Override
	public CloudSite contactCustomerService(int userId,int siteId) {
		CloudSite cloudsite = null;
		try {
			cloudsite = this.dao.fetch(CloudSite.class,Cnd.where("id", "=", siteId).and("user_id", "=", userId));
		} catch (Exception e) {
			log.error(this.getClass().getCanonicalName(), e);
			return null;
		}
		return cloudsite;
	}

	@Override
	public CloudSite getCloudSite(int siteId) {
		return this.dao.fetch(CloudSite.class,Cnd.where("id", "=", siteId));
	}
	@Override
	public PassUrl getPassUrl(String type) {
		return this.dao.fetch(PassUrl.class,Cnd.where("system_type", "=", type));
	}
	/**
	 * 老用户关联表
	 */
	@Override
	public boolean relevanceUserAndSite(CloudSitePortalEntity cpe) {
		return dao.insert(cpe).getId()>0?true:false;
	}
	/**
	 * 更新用户子账号
	 */
	@Override
	public boolean updateUserSonAccount(String sonName, int userId) {
		return dao.update(PortalUser.class, Chain.make("sonName", sonName), Cnd.where("id","=",userId))>0?true:false;
	}
	/**
	 * 更新用户子账号
	 */
	@Override
	public boolean updateUserImg(String img, int userId) {
		return dao.update(PortalUser.class, Chain.make("image_url",img), Cnd.where("id","=",userId))>0?true:false;
	}
	@Override
	public APCashPledge cashPledgeCheck(String apMac) {
		return	dao.fetch(APCashPledge.class, Cnd.where("apMac", "=",apMac));
	}
	
	@Override
	public SiteCustomerInfo getSiteCustomerInfo(int uid) {
		return this.dao.fetch(SiteCustomerInfo.class,Cnd.where("portal_user_id", "=",uid));
	}
}
