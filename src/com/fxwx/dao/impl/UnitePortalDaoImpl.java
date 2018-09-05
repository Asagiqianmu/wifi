package com.fxwx.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.fxwx.dao.UnitePortalDao;
import com.fxwx.entiy.AppAuthParam;
import com.fxwx.entiy.AppInfo;
import com.fxwx.entiy.AppUser;
import com.fxwx.entiy.AuthType;
import com.fxwx.entiy.AuthUser;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.CloudSitePortalEntity;
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
import com.fxwx.util.DateUtil;
import com.fxwx.util.FileSystemProperty;

/**
 * 统一Portal认证Dao接口类
 * 
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2018年1月15日 下午9:59:04
 */
@IocBean(name = "unite_portal_daoImpl")
public class UnitePortalDaoImpl implements UnitePortalDao {
	private static final Log log = Logs.getLog(UnitePortalDaoImpl.class);
	@Inject
	private Dao dao;

	// @Inject
	// private Dao readDao;

	/**
	 * 查询系统参数表获取系统类型参数
	 */
	@SuppressWarnings("unchecked")
	public List<List<String>> getSolarsysType() {
		try {
			Sql sql = Sqls.create(
					"SELECT sys_type,sys_param,param_type,sys_index,sys_keyfield,callback_result,callback_keyfield FROM t7_system_parameter");
			sql.setCallback(new SqlCallback() {
				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					List<String> list = null;
					List<List<String>> lists = new ArrayList<List<String>>();
					while (rs.next()) {
						list = new ArrayList<String>();
						list.add(rs.getString("sys_param"));
						list.add(rs.getString("sys_type"));
						list.add(rs.getString("param_type"));
						list.add(rs.getString("sys_index"));
						list.add(rs.getString("sys_keyfield"));
						list.add(rs.getString("callback_result"));
						list.add(rs.getString("callback_keyfield"));
						lists.add(list);
					}
					return lists;
				}
			});
			dao.execute(sql);
			return sql.getObject(List.class);
			// return sql.getList(String.class);
		} catch (Exception e) {
			log.error("获取系统类型参数错误", e);
			return null;
		}
	}

	/**
	 * 获取系统回调参数
	 */
	@Override
	public List<List<String>> getCallBackSolarsysType() {
		try {
			Sql sql = Sqls.create(
					"SELECT callback_param,param_type,sys_type,sys_keyfield,callback_result,callback_keyfield from t7_system_parameter");
			sql.setCallback(new SqlCallback() {
				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					List<String> list = null;
					List<List<String>> lists = new ArrayList<List<String>>();
					while (rs.next()) {
						list = new ArrayList<String>();
						list.add(rs.getString("callback_param"));
						list.add(rs.getString("param_type"));
						list.add(rs.getString("sys_type"));
						list.add(rs.getString("sys_keyfield"));
						list.add(rs.getString("callback_result"));
						list.add(rs.getString("callback_keyfield"));
						lists.add(list);
					}
					return lists;
				}
			});
			dao.execute(sql);
			return sql.getObject(List.class);
		} catch (Exception e) {
			log.error("获取系统类型参数错误", e);
			return null;
		}
	}

	// /**
	// * 查询系统参数表获取系统类型参数
	// */
	// public List<String> getSolarsysType(String param) {
	// try {
	//
	// Sql sql=Sqls.create(" SELECT
	// sys_param,param_type,sys_type,sys_index,callback_keyfield from
	// t7_system_parameter WHERE locate(sys_param,@param)");
	// sql.setParam("param", param);
	// sql.setCallback(new SqlCallback() {
	// @Override
	// public Object invoke(Connection conn, ResultSet rs, Sql sql)
	// throws SQLException {
	// List<String> list= new ArrayList<String>();
	// while (rs.next()) {
	// list.add(rs.getString("sys_param"));
	// list.add(rs.getString("param_type"));
	// list.add(rs.getString("sys_type"));
	// list.add(rs.getString("sys_index"));
	// list.add(rs.getString("callback_keyfield"));
	// }
	// return list;
	// }
	// });
	// dao.execute(sql);
	// return sql.getList(String.class);
	// } catch (Exception e) {
	// log.error("获取系统类型参数错误",e);
	// return null;
	// }
	// }
	/**
	 * 获取场所
	 */
	@Override
	public CloudSite getSite(String nasid) {
		try {
			Sql sql = Sqls.create(
					"SELECT * FROM t_cloud_site WHERE id=(SELECT site_id FROM t_cloud_site_routers WHERE nasid=@param)");
			sql.setParam("param", nasid);
			sql.setCallback(Sqls.callback.entity());
			Entity<CloudSite> entity = dao.getEntity(CloudSite.class);
			sql.setEntity(entity);
			dao.execute(sql);
			return sql.getObject(CloudSite.class);
		} catch (Exception e) {
			log.error("获取场所失败", e);
			return null;
		}
	}

	/**
	 * 根据mac获取用户信息
	 */
	@Override
	public PortalUser getUserByMac(String mac) {

		Sql sql = Sqls.create("SELECT * FROM t_portal_user $condition order by end_time DESC limit 0,1");
		sql.setCondition(Cnd.where("client_mac", "=", mac));
		sql.setCallback(Sqls.callback.entity());
		Entity<PortalUser> entity = dao.getEntity(PortalUser.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getObject(PortalUser.class);
	}

	/**
	 * 根据mac获取用户信息
	 */
	@Override
	public PortalUser getPortalUserByMac(String mac) {

		Sql sql = Sqls.create("SELECT * FROM t_portal_user $condition order by end_time DESC limit 0,1");
		sql.setCondition(Cnd.where("client_mac", "=", mac));
		sql.setCallback(Sqls.callback.entity());
		Entity<PortalUser> entity = dao.getEntity(PortalUser.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getObject(PortalUser.class);
	}

	/**
	 * 根据用户名获取用户信息
	 */
	@Override
	public PortalUser getUserByName(String userName) {
		Sql sql = Sqls.create("SELECT * FROM t_portal_user $condition order by end_time DESC limit 0,1");
		sql.setCondition(Cnd.where("user_name", "=", userName));
		sql.setCallback(Sqls.callback.entity());
		Entity<PortalUser> entity = dao.getEntity(PortalUser.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getObject(PortalUser.class);
	}

	/**
	 * 根据用户名获取场所信息
	 */
	@Override
	public CloudSite getSiteByname(String userName) {
		Sql sql = Sqls.create(
				"SELECT * from t_cloud_site WHERE id=( SELECT site_id FROM t_cloud_site_portal WHERE portal_id=@param)");
		sql.setParam("param", userName);
		sql.setCallback(Sqls.callback.entity());
		Entity<CloudSite> entity = dao.getEntity(CloudSite.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getObject(CloudSite.class);
	}

	/**
	 * 根据场所id获取场所
	 */
	@Override
	public CloudSite getSiteById(int siteId) {
		return dao.fetch(CloudSite.class, Cnd.where("id", "=", siteId));
	}

	/**
	 * 根据用户名密码查询用户信息
	 */
	@Override
	public PortalUser getUserByNameAndPwd(String userName, String pwd) {
		log.error("用户名：" + userName + "---------------------------" + "密码" + pwd);
		PortalUser porUser = dao.fetch(PortalUser.class,
				Cnd.where("user_name", "=", userName).and("pass_word", "=", pwd));
		return porUser;
	}

	/**
	 * 获取用户锁定时间
	 */
	@Override
	public String getUserLockTime(String userName, int siteId) {
		Sql sql = Sqls.create(
				"SELECT lock_time FROM t_site_customer_info WHERE site_id=@param and portal_user_id=(SELECT id FROM t_portal_user where user_name=@param1)");
		sql.params().set("param", siteId).set("param1", userName);
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString("lock_time"));
				}
				return list;
			}
		});
		dao.execute(sql);
		return sql.getList(String.class).get(0);
	}

	/**
	 * 关联场所到用户表
	 */
	@Override
	public void updateUserSite(int userId, int siteId) {
		Sql sql = Sqls.create("UPDATE t_portal_user SET t7_site_id=@param WHERE id=@param1");
		sql.params().set("param", siteId).set("param1", userId);
		dao.execute(sql);
	}

	/**
	 * 注册用户
	 */
	@Override
	public boolean insertUser(PortalUser user) {
		try {
			return dao.insert(user).getId() > 0 ? true : false;

		} catch (Exception e) {
			log.error("注册用户或者修改用户失败", e);
			return false;
		}
	}

	/**
	 * 修改用户密码
	 */
	@Override
	public boolean updateUser(PortalUser user) {
		try {
			log.error("锁定----------------" + user.getPassWord());
			return dao.update(user) > 0 ? true : false;
		} catch (Exception e) {
			log.error("注册用户或者修改用户失败", e);
			return false;
		}
	}

	/**
	 * 获取用户到期时间
	 */
	@Override
	public SiteCustomerInfo getCustomerInfo(int userId, int siteId) {
		SiteCustomerInfo scis = null;

		try {
			scis = dao.fetch(SiteCustomerInfo.class,
					Cnd.where("portal_user_id", "=", userId).and("site_id", "=", siteId));
		} catch (Exception e) {
			log.error(this.getClass().getCanonicalName(), e);
			return null;
		}
		return scis;
	}

	/**
	 * 获取场所赠送时长
	 */
	@Override
	public GiftLength getGiftLength(int siteId) {
		GiftLength gl = null;
		try {
			gl = dao.fetch(GiftLength.class, Cnd.where("site_id", "=", siteId));
		} catch (Exception e) {
			log.error("获取场所赠送时长失败");
		}
		return gl;
	}

	/**
	 * 场所赠送用户时长或者流量
	 */
	@Override
	public boolean insertCustomerInfo(final SiteCustomerInfo sci, final PortalUser user) {
		try {
			Trans.exec(new Atom() {
				@Override
				public void run() {
					int i = dao.insert(sci).getId();
					if (i < 1) {
						throw Lang.makeThrow("送用户时长失败");
					}
					if (user != null) {
						i = dao.update(user);
					}
					if (i < 1)
						throw Lang.makeThrow("更改用户生日失败");
				}
			});

		} catch (Exception e) {
			log.error("送用户时长失败", e);
			return false;
		}
		return true;
	}

	/**
	 * 更改用户到期时常或者流量
	 */
	@Override
	public boolean updateCustomerInfo(final SiteCustomerInfo sci, final PortalUser user) {
		try {
			Trans.exec(new Atom() {
				@Override
				public void run() {
					int i = dao.update(sci);
					if (i < 1) {
						throw Lang.makeThrow("送用户时长失败");
					}
					i = dao.update(user);
					if (i < 1)
						throw Lang.makeThrow("更改用户生日失败");
				}
			});

		} catch (Exception e) {
			log.error("送用户时长失败", e);
			return false;
		}
		return true;
	}

	/**
	 * 查询场所放行url
	 */
	@Override
	public PassUrl selPassUrlBySiteId(int id) {
		return dao.fetch(PassUrl.class, Cnd.where("id", "=", id));
	}

	/**
	 * 插入认证用户
	 */
	@Override
	public boolean insertAccount(AuthUser au) {
		return dao.insert(au).getId() > 0 ? true : false;
	}

	/**
	 * 查询认证用户
	 */
	@Override
	public String selAccount(String mac, String nasid) {
		try {
			Sql sql = Sqls.create(
					"SELECT username FROM t7_authuser WHERE mac=@mac AND nasid=@nasid ORDER BY id DESC LIMIT 0,1");
			sql.setParam("mac", mac).setParam("nasid", nasid);
			sql.setCallback(new SqlCallback() {
				@Override
				public Object invoke(Connection arg0, ResultSet res, Sql sql) throws SQLException {
					String username = "";
					while (res.next()) {
						username = res.getString("username");

					}
					return username;
				}
			});
			dao.execute(sql);
			return sql.getString();
		} catch (Exception e) {
			log.error("查询认证用户失败", e);
			return null;
		}

	}

	/**
	 * 根据用户MAC、用户IP、NASID、hour查找认证记录
	 */
	@Override
	public List<AuthUser> selAccountByUmac_Uip_Nasid_Hour(String usermac, String userip, String nasid, int hour) {
		try {
			Sql sql1 = Sqls.create(
					"SELECT * FROM t7_authuser WHERE authstate=0 AND mac=@mac AND userip=@userip AND nasid=@nasid AND create_time >= DATE_SUB(NOW(),INTERVAL @hour HOUR) ORDER BY create_time DESC");

			Sql sql2 = Sqls.create(
					"SELECT * FROM t7_authuser WHERE authstate=0 AND mac=@mac AND userip=@userip AND nasid=@nasid ORDER BY create_time DESC");
			if (hour > 0) {
				sql1.setParam("mac", usermac).setParam("userip", userip).setParam("nasid", nasid).setParam("hour",
						hour);
				sql1.setCallback(Sqls.callback.entities());
				Entity<AuthUser> entity = dao.getEntity(AuthUser.class);
				sql1.setEntity(entity);
				dao.execute(sql1);
				return sql1.getList(AuthUser.class);
			} else {
				sql2.setParam("mac", usermac).setParam("userip", userip).setParam("nasid", nasid).setParam("hour",
						hour);
				sql2.setCallback(Sqls.callback.entities());
				Entity<AuthUser> entity = dao.getEntity(AuthUser.class);
				sql2.setEntity(entity);
				dao.execute(sql2);
				return sql2.getList(AuthUser.class);
			}

		} catch (Exception e) {
			log.error("查询认证用户失败", e);
			return null;
		}

	}

	/**
	 * 根据nasid查询场所路由
	 */
	@Override
	public CloudSiteRouters selRouterByNasid(String nasid) {
		CloudSiteRouters csr = null;
		try {
			csr = dao.fetch(CloudSiteRouters.class, Cnd.where("nasid", "=", nasid));
		} catch (Exception e) {
			log.error("查询场所路由出错", e);
		}
		return csr;
	}

	/**
	 * 修改场所路由ip
	 */
	@Override
	public boolean updateRouterIp(int routerId, String ip) {
		try {
			int i = dao.update("t_cloud_site_routers", Chain.make("ip", ip), Cnd.where("id", "=", routerId));
			return i > 0 ? true : false;
		} catch (Exception e) {
			log.error("更新路由ip失败", e);
			return false;
		}
	}

	/**
	 * 查询发起认证用户
	 */
	@Override
	public AuthUser selAuthUser(String param) {
		return dao.fetch(AuthUser.class, Cnd.where("authtime", "=", param));
	}

	/**
	 * 更改用户最后一次登录的设备mac
	 */
	@Override
	public void updateClientMac(int userId, String clientMac) {
		dao.update("t_portal_user", Chain.make("client_mac", null), Cnd.where("client_mac", "=", clientMac));
		dao.update("t_portal_user", Chain.make("client_mac", clientMac).add("end_time", DateUtil.getStringDate()),
				Cnd.where("id", "=", userId));
	}

	/***
	 * 查看认证用户
	 */
	@Override
	public AuthUser selAuthUser(String userName, String nasid, String userMac) {
		Sql sql = Sqls.create("SELECT * FROM t7_authuser $condition order by id DESC limit 0,1");
		if (userName == null) {
			sql.setCondition(Cnd.where("mac", "=", userMac).and("nasid", "=", nasid));
		} else if (nasid == null && userMac == null) {
			sql.setCondition(Cnd.where("username", "=", userName));
		} else {
			sql.setCondition(Cnd.where("username", "=", userName).and("mac", "=", userMac).and("nasid", "=", nasid));
		}
		sql.setCallback(Sqls.callback.entity());
		Entity<AuthUser> entity = dao.getEntity(AuthUser.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getObject(AuthUser.class);
	}

	/***
	 * 查看认证用户
	 */
	@Override
	public List<AuthUser> selAuthUserlistToday(String userName, String nasid, String userMac) {
		try {
			Sql sql = Sqls.create("SELECT * FROM t7_authuser WHERE authstate=0 AND mac=@mac AND username=@username AND nasid=@nasid AND to_days(create_time) = to_days(now())");
			sql.setParam("mac", userMac).setParam("username", userName).setParam("nasid", nasid);
			sql.setCallback(Sqls.callback.entities());
			Entity<AuthUser> entity = dao.getEntity(AuthUser.class);
			sql.setEntity(entity);
			dao.execute(sql);
			return sql.getList(AuthUser.class);
		
		} catch (Exception e) {
			log.error("查询认证用户失败", e);
			return null;
		}
	}
	
	/**
	 * 更改用户认证请求时间
	 */
	@Override
	public boolean updateAccount(AuthUser au) {
		return dao.update(au) > 0 ? true : false;
	}

	/**
	 * 插入用户到期时间表
	 */
	@Override
	public boolean insertCustomerInfo(SiteCustomerInfo sci) {
		return dao.insert(sci).getId() > 0 ? true : false;
	}

	/**
	 * 更改用户到期时间
	 */
	@Override
	public boolean updateCustomerInfo(SiteCustomerInfo sci) {
		return dao.update(sci) > 0 ? true : false;
	}

	/**
	 * 根据用户设备mac以及手机号获取用户信息
	 */
	@Override
	public PortalUser getUserByMacAndName(String mac, String userName) {
		return dao.fetch(PortalUser.class, Cnd.where("client_mac", "=", mac).and("user_name", "=", userName));
	}

	/**
	 * 查询商户
	 */
	@Override
	public CloudUser getTouch(int userId) {
		return dao.fetch(CloudUser.class, Cnd.where("id", "=", userId));
	}

	/**
	 * 增加场所联系人
	 */
	@Override
	public boolean updateSiteTouch(CloudSite site) {
		return dao.update(site) > 0 ? true : false;
	}

	/**
	 * 查询用户账号一天登录不同的设备次数
	 */
	@Override
	public List<String> selMac(String userName) {
		Sql sql = Sqls
				.create("SELECT callingstationid FROM radacct WHERE username=@userName  GROUP BY callingstationid");
		sql.setParam("userName", userName);
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection arg0, ResultSet res, Sql sql) throws SQLException {
				List<String> list = new ArrayList<String>();
				while (res.next()) {
					list.add(res.getString("callingstationid"));
				}
				return list;
			}
		});
		dao.execute(sql);
		return sql.getList(String.class);
	}

	/**
	 * 获取放行参数
	 */
	@Override
	public PassUrl selPassUrlBySolarsys(String solarsysType) {
		return dao.fetch(PassUrl.class, Cnd.where("system_type", "=", solarsysType));
	}

	/**
	 * 放行不成功时，修改ip地址
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @version 创建时间：2017年3月31日 上午9:59:53
	 * @describe
	 * @parameter
	 * @return
	 */
	@Override
	public boolean updateIp(String nasid, String ip) {
		int i = dao.update("t_cloud_site_routers", Chain.make("ip", ip), Cnd.where("nasid", "=", nasid));
		return i > 0 ? true : false;
	}

	/**
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @version 创建时间：2017年6月6日 上午8:58:43
	 * @describe 增加场所用户关联表
	 * @parameter
	 * @return
	 */
	@Override
	public boolean addUserSiteRetance(int userId, int siteId) {
		CloudSitePortalEntity cpe = new CloudSitePortalEntity();
		cpe.setCreateTime(new java.sql.Timestamp(new Date().getTime()));
		cpe.setPortalUserId(userId);
		cpe.setSiteId(siteId);
		return dao.insert(cpe).getId() > 0 ? true : false;
	}

	@Override
	public AuthType getAuthTypeByNasid(String nasid) {
		return dao.fetch(AuthType.class, Cnd.where("nasid", "=", nasid));
	}

	@Override
	public List<AuthUser> selAuthuserByNasid(String nasid) {
		Sql sql = Sqls.create("SELECT * FROM t7_authuser WHERE nasid=@nasid GROUP BY mac");
		sql.setParam("nasid", nasid);
		sql.setCallback(Sqls.callback.entities());
		Entity<AuthUser> entity = dao.getEntity(AuthUser.class);
		sql.setEntity(entity);
		dao.execute(sql);
		List<AuthUser> list = sql.getList(AuthUser.class);
		return list;
	}

	/**
	 * 把ssid和nasid存入数据库
	 */
	@Override
	public boolean updatessid(PortalId portalid) {
		return dao.insert(portalid).getId() > 0 ? true : false;
		// return false;
	}

	@Override
	public List<PortalId> selectSsidAndNasid(String nasid, String ssid) {
		Sql sql = Sqls.create("SELECT * FROM t7_portalid_type WHERE ssid=@ssid AND nasid=@nasid;");
		sql.setParam("nasid", nasid).setParam("ssid", ssid);
		sql.setCallback(Sqls.callback.entities());
		Entity<PortalId> entity = dao.getEntity(PortalId.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getList(PortalId.class);
	}

	@Override
	public int addOrUpdateParam(String params, String ip) {
		Sql sql = Sqls.create(
				"INSERT INTO t8_app_auth_param (mac,extend) VALUES(@mac,@param) ON DUPLICATE KEY UPDATE extend=@param");
		sql.setParam("mac", ip).setParam("param", params);
		sql.setCallback(Sqls.callback.entities());
		Entity<AppAuthParam> entity = dao.getEntity(AppAuthParam.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getUpdateCount();
	}

	@Override
	public AppInfo getAppInfoBySiteId(int siteId) {
		// 暂时不打开该sql 因目前后台设置不完善并且配合平安测试
		// Sql sql=Sqls.create("SELECT * FROM t8_app_info WHERE id=(SELECT
		// app_id FROM t8_site_app_retance WHERE site_id=@siteid)");
		Sql sql = Sqls.create("SELECT * FROM t8_app_info WHERE appid=@appid");
		sql.setParam("appid", FileSystemProperty.propertyName("appid"));
		sql.setCallback(Sqls.callback.entities());
		Entity<AppInfo> entity = dao.getEntity(AppInfo.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getObject(AppInfo.class);
	}

	@Override
	public AppAuthParam getAppAuthParamByIp(String ip) {
		return dao.fetch(AppAuthParam.class, Cnd.where("ip", "=", ip));
	}

	@Override
	public AppUser addOrUpdateAppUser(String openid, String openkey, int userId) {
		AppUser au = dao.fetch(AppUser.class, Cnd.where("app_user_name", "=", openid));
		if (au == null) {
			au = new AppUser();
			au.setCreateTime(new Date());
			au.setOpenid(openid);
			au.setOpenkey(openkey);
			au = dao.insert(au);
		} else {
			dao.update(AppUser.class, Chain.make("app_user_key", openkey).add("create_time", new Date()),
					Cnd.where("id", "=", au.getId()));
			au.setOpenkey(openkey);
		}
		// Sql sql=Sqls.create("INSERT INTO t8_portal_app_user
		// (app_user_name,app_user_key,create_time)
		// VALUES(@username,@userkey,@creattime) ON DUPLICATE KEY UPDATE
		// app_user_key=@userkey");
		// sql.setParam("username", openid).setParam("userkey",
		// openkey).setParam("creattime", new Date()).setParam("userkey",
		// openkey);
		// dao.execute(sql);
		// return sql.getUpdateCount();
		return au;
	}

	@Override
	public List<String> getradacctInfo(String openid) {
		String getSql = "SELECT nasid,framedipaddress,nasipaddress from radacct WHERE username=@userName AND acctstoptime IS NULL ORDER BY radacctid DESC LIMIT 0,1";
		Sql sql = Sqls.create(getSql);
		sql.setParam("userName", openid);
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection arg0, ResultSet rs, Sql arg2) throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString("nasid"));
					list.add(rs.getString("framedipaddress"));
					list.add(rs.getString("nasipaddress"));
				}
				return list;
			}
		});
		dao.execute(sql);
		return sql.getList(String.class);
	}

	@Override
	public TemporaryParams getTemporaryParamsBykey(String key) {
		return dao.fetch(TemporaryParams.class, Cnd.where("key_val", "=", key));
	}

	@Override
	public boolean updateWcahtUserOpenidById(int id, int subscribe) {
		int i = dao.update("t8_wcaht_user_openid", Chain.make("subscribe", subscribe), Cnd.where("id", "=", id));
		return i > 0 ? true : false;
	}

	/**
	 * 根据mac标识查询PhoneCode
	 */
	@Override
	public PhoneCode getPhoneCode(String mac) {
		return dao.fetch(PhoneCode.class, Cnd.where("portal_user_id", "=", mac));
	}

	/**
	 * 修改到期时间
	 */
	@Override
	public boolean updateCustomerInfo(int id, Date expiration_time) {
		return dao.update("t_site_customer_info", Chain.make("expiration_time", expiration_time),
				Cnd.where("id", "=", id)) > 0 ? true : false;
	}

	@Override
	public boolean SubExpiration_time(int id, int num, String unit) {
		return dao.update("t_site_customer_info", Chain.make("expiration_time", "DATE_SUB(expiration_time, INTERVAL "+num+" "+unit+")"),
				Cnd.where("id", "=", id)) > 0 ? true : false;
	}
	/**
	 * 插入场所顾客信息
	 */
	@Override
	public boolean addCustomerInfo(SiteCustomerInfo siteCustomerInfo) {
		return dao.insert(siteCustomerInfo).getId() > 0 ? true : false;
	}

	@Override
	public SitePriceConfig getSitePriceConfigById(int Id) {
		Sql sql = Sqls.create("SELECT * FROM t_site_price_config WHERE id=@Id");
		sql.setParam("Id", Id);
		sql.setCallback(Sqls.callback.entity());
		Entity<SitePriceConfig> entity = dao.getEntity(SitePriceConfig.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getObject(SitePriceConfig.class);
	}

	@Override
	public SitePriceConfig getSitePriceConfig(int siteId) {
		Sql sql = Sqls
				.create("SELECT * FROM t_site_price_config WHERE site_id=@sietId AND price_type=6 AND is_stoped=0");// 场所需要交押金
		sql.setParam("siteId", siteId);
		sql.setCallback(Sqls.callback.entity());
		Entity<SitePriceConfig> entity = dao.getEntity(SitePriceConfig.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getObject(SitePriceConfig.class);
	}

	@Override
	public List<SitePriceConfig> getSitePriceMemberCombo(int siteId) {
		Sql sql = Sqls
				.create("SELECT * FROM t_site_price_config WHERE site_id=@siteId and price_type<>6  AND is_stoped=0");
		sql.setParam("siteId", siteId);
		sql.setCallback(Sqls.callback.entities());
		Entity<SitePriceConfig> entity = dao.getEntity(SitePriceConfig.class);
		sql.setEntity(entity);
		dao.execute(sql);
		List<SitePriceConfig> list = sql.getList(SitePriceConfig.class);
		return list;
	}

	@Override
	public boolean addSiteIncome(SiteIncome siteIncome) {
		return dao.insert(siteIncome).getId() > 0 ? true : false;
	}

	/**
	 * ,user_id,is_probative,allow_client_num,create_time,siteNum,state,
	 * systemtype,banner_url,site_type,status,exTime,site_a,authenticationStatus
	 * 通过手机号获取场所信息
	 */
	@Override
	public List<CloudSite> getCloudSiteByUserName(String userName) {
		String getSql = "SELECT * FROM t_cloud_site WHERE id IN (SELECT site_id FROM t_cloud_site_portal WHERE portal_id IN (SELECT id  FROM t_portal_user WHERE user_name=@userName )GROUP BY site_id)";
		Sql sql = Sqls.create(getSql);
		sql.setParam("userName", userName);
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection arg0, ResultSet rs, Sql arg2) throws SQLException {
				List<CloudSite> list = new ArrayList<CloudSite>();
				CloudSite cloudSite = null;
				while (rs.next()) {
					cloudSite = new CloudSite();
					cloudSite.setId(rs.getInt("id"));
					cloudSite.setAddress(rs.getString("address"));
					cloudSite.setSite_name(rs.getString("site_name"));
					cloudSite.setState(rs.getInt("state"));
					cloudSite.setUser_id(rs.getInt("user_id"));
					cloudSite.setIs_probative(rs.getInt("is_probative"));
					cloudSite.setAllow_client_num(rs.getInt("allow_client_num"));
					cloudSite.setCreate_time(rs.getTimestamp("create_time"));
					cloudSite.setSystemtype(rs.getInt("systemtype"));
					cloudSite.setStauts(rs.getInt("status"));
					cloudSite.setBannerUrl(rs.getString("banner_url"));
					cloudSite.setAdminer(rs.getString("site_admin"));
					cloudSite.setAuthenticationStatus(rs.getInt("authenticationStatus"));
					list.add(cloudSite);
				}
				return list;
			}
		});
		dao.execute(sql);
		return sql.getList(CloudSite.class);
	}

	/**
	 * 通过openId查询场所信息
	 */
	@Override
	public List<CloudSite> getCloudSiteByOpenId(String openId) {
		String getSql = "SELECT * FROM t_cloud_site WHERE STATUS=900 AND id IN (SELECT site_id FROM t8_wcaht_user_openid WHERE openId=@openId GROUP BY site_id)";
		Sql sql = Sqls.create(getSql);
		sql.setParam("openId", openId);
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection arg0, ResultSet rs, Sql arg2) throws SQLException {
				List<CloudSite> list = new ArrayList<CloudSite>();
				CloudSite cloudSite = null;
				while (rs.next()) {
					cloudSite = new CloudSite();
					cloudSite.setId(rs.getInt("id"));
					cloudSite.setAddress(rs.getString("address"));
					cloudSite.setSite_name(rs.getString("site_name"));
					cloudSite.setState(rs.getInt("state"));
					cloudSite.setUser_id(rs.getInt("user_id"));
					cloudSite.setIs_probative(rs.getInt("is_probative"));
					cloudSite.setAllow_client_num(rs.getInt("allow_client_num"));
					cloudSite.setCreate_time(rs.getTimestamp("create_time"));
					cloudSite.setSystemtype(rs.getInt("systemtype"));
					cloudSite.setStauts(rs.getInt("status"));
					cloudSite.setBannerUrl(rs.getString("banner_url"));
					cloudSite.setAdminer(rs.getString("site_admin"));
					cloudSite.setAuthenticationStatus(rs.getInt("authenticationStatus"));
					list.add(cloudSite);
				}
				return list;
			}
		});
		dao.execute(sql);
		return sql.getList(CloudSite.class);
	}

	/**
	 * 添加商户信息
	 */
	@Override
	public boolean addCommercialTenant(CommercialTenant commercialTenant) {
		return dao.insert(commercialTenant).getId() > 0 ? true : false;
	}

	@Override
	public List<CommercialTenant> getCommercialTenant(String userName) {
		StringBuffer getSql = new StringBuffer();
		getSql.append("SELECT * FROM t_commercial_tenant WHERE  id IN(SELECT commercial_tenant_id  FROM t_commercial_tenant_cloud_user");
		getSql.append("	WHERE cloud_user_id IN (SELECT user_id FROM `t_cloud_site` WHERE  id IN");
		getSql.append("	(SELECT t7_site_id  FROM`t_portal_user` WHERE user_name=@userName GROUP BY t7_site_id) GROUP BY user_id) GROUP BY commercial_tenant_id)");
		Sql sql = Sqls.create(getSql.toString());
		System.out.println(getSql);
		sql.setParam("userName", userName);
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection arg0, ResultSet rs, Sql arg2) throws SQLException {
				List<CommercialTenant> list = new ArrayList<CommercialTenant>();
				CommercialTenant commercialTenant = null;
				while (rs.next()) {
					commercialTenant = new CommercialTenant();
					commercialTenant.setId(rs.getInt("id"));
					commercialTenant.setAppid(rs.getString("appid"));
					commercialTenant.setPayApp(rs.getInt("pay_app"));
					commercialTenant.setContent(rs.getString("content"));
					commercialTenant.setAffiliationCompany(rs.getString("affiliation_company"));
					commercialTenant.setCreateTime(rs.getTimestamp("create_time"));
					list.add(commercialTenant);
				}
				return list;
			}
		});
		dao.execute(sql);
		return sql.getList(CommercialTenant.class);
	}

	@Override
	public void insertPortalLoginLog(PortalLoginLog portalloginlog) {
		String tableName = "t_portalloginlog_" + DateUtil.getDateNoPs();
		Sql insertSql = Sqls.create("insert into " + tableName
				+ "(site_id,user_name,state,create_time) values (@siteId,@username,@state,@createtime)");
		try {
			insertSql.setParam("siteId", portalloginlog.getSiteId());
			insertSql.setParam("username", portalloginlog.getUser_name());
			insertSql.setParam("state", portalloginlog.getState());
			insertSql.setParam("createtime", portalloginlog.getCreateTime());
			dao.execute(insertSql);

		} catch (Exception e) {
			Sql createSql = Sqls.create("CREATE TABLE " + tableName
					+ " (id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',"
					+ "site_id int(11) DEFAULT NULL COMMENT '场所id',"
					+ "user_name varchar(64) NOT NULL COMMENT '用户名',"
					+ "state int(1) NOT NULL DEFAULT '0' COMMENT '登录状态，1为成功，0，为失败',"
					+ "create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',"
					+ "PRIMARY KEY (`id`)"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='portal用户表登录记录'");
			dao.execute(createSql);
			// if(createSql.equals()){}
			// 创建成功进行插入
			insertSql.setParam("siteId", portalloginlog.getSiteId());
			insertSql.setParam("username", portalloginlog.getUser_name());
			insertSql.setParam("state", portalloginlog.getState());
			insertSql.setParam("createtime", portalloginlog.getCreateTime());
			dao.execute(insertSql);
		}
	}

}
