package com.fxwx.dao.impl;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.alibaba.fastjson.JSON;
import com.fxwx.bean.SitePriceConfigAll;
import com.fxwx.dao.UserPayDao;
import com.fxwx.entiy.CashPledge;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.CloudUser;
import com.fxwx.entiy.CommonConfig;
import com.fxwx.entiy.IncomeCollect;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.entiy.SiteIncome;
import com.fxwx.entiy.SiteIncomeCollect;
import com.fxwx.entiy.SitePaymentRecord;
import com.fxwx.entiy.SitePriceConfig;
import com.fxwx.util.BigDecimalUtil;
import com.fxwx.util.DateUtil;
import com.fxwx.util.InitDao;
import com.fxwx.util.alim.AlipayNet;


@IocBean
public class UserPayDaoImpl implements UserPayDao {
	
	private static Logger logger = Logger.getLogger(UserPayDaoImpl.class);
	private static String lock="1";
	private final static Object LOCK_OBJECT = new Object();//锁定资源,不能同时对变量的修改
	@Inject
	private Dao dao;
	
	
	@Inject
	private  AlipayNet alipayNet;
	

	@Override
	public SitePriceConfigAll getSitePriceConfigAll(int userId, int siteId) {
		// TODO Auto-generated method stub
		return null;
	}

	//根据用户id查找用户
	@Override
	public PortalUser getUserById(int userId) {
		PortalUser porUser = null;
		try {
			porUser = dao.fetch(PortalUser.class,Cnd.where("id", "=", userId));
		} catch (Exception e) {
			logger.error(this.getClass().getCanonicalName(),e);
		}
		return porUser;
	}
	//获得场所下的按时间计费包
	@SuppressWarnings("all")
	@Override
	public  List<Map> getTimeConfigList(int siteId) {
		String sql = "SELECT id,site_id,unit_price,name, price_type,price_num,comboNumber,v2_recommend_state,v2_give_meal,v2_givemeal_unit,v2_describe FROM t_site_price_config where price_type <=3 AND is_stoped = 0 and site_id="+siteId+" ORDER BY v2_recommend_state DESC";
		Sql sqls = Sqls.create(sql);
		final List<Map> list = new ArrayList<Map>();
		sqls.setCallback(new SqlCallback() {
			@Override
			public Object invoke(java.sql.Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				Map map = null;
				while (rs.next()) {
					map = new HashMap();
					map.put("id",rs.getInt("id"));
					map.put("site_id",rs.getInt("site_id"));
					map.put("unit_price",rs.getInt("unit_price"));
					map.put("name",rs.getString("name"));
					map.put("price_type",rs.getInt("price_type"));
					map.put("price_num",rs.getInt("price_num"));
					map.put("comboNumber",rs.getString("comboNumber"));
					map.put("v2_recommend_state",rs.getInt("v2_recommend_state"));
					map.put("v2_give_meal",rs.getInt("v2_give_meal"));
					map.put("v2_givemeal_unit",rs.getInt("v2_givemeal_unit"));
					map.put("v2_describe",rs.getString("v2_describe"));
					list.add(map);
				}
				return null;
			}
		});
		this.dao.execute(sqls);
		return list;
	}

	//获得场所下的按流量计费包
	@SuppressWarnings("all")
	@Override
	public List<Map> getFlowConfigList(int siteId) {
		String sql = "SELECT id,site_id,unit_price,name, price_type,price_num,comboNumber,v2_recommend_state,v2_give_meal,v2_givemeal_unit,v2_describe FROM t_site_price_config where price_type > 3  AND is_stoped = 0 and site_id="+siteId+" ORDER BY v2_recommend_state DESC";
		Sql sqls = Sqls.create(sql);
		final List<Map> list = new ArrayList<Map>();
		sqls.setCallback(new SqlCallback() {
			@Override
			public Object invoke(java.sql.Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				Map map = null;
				while (rs.next()) {
					map = new HashMap();
					map.put("id",rs.getInt("id"));
					map.put("site_id",rs.getInt("site_id"));
					map.put("unit_price",rs.getInt("unit_price"));
					map.put("name",rs.getString("name"));
					map.put("price_type",rs.getInt("price_type"));
					map.put("price_num",rs.getInt("id"));
					map.put("comboNumber",rs.getString("comboNumber"));
					map.put("v2_recommend_state",rs.getInt("v2_recommend_state"));
					map.put("v2_give_meal",rs.getInt("v2_give_meal"));
					map.put("v2_givemeal_unit",rs.getInt("v2_givemeal_unit"));
					map.put("v2_describe",rs.getString("v2_describe"));
					list.add(map);
				}
				return null;
			}
		});
		this.dao.execute(sqls);
		return list;
	}
	
	
	public static void main(String[] args) {
		Dao dao = InitDao.init("dao");
		UserPayDaoImpl u = new UserPayDaoImpl();
		String username = "13051475463";
		List<Map> list = u.getTimeConfigList(18);
		List<Map> result = new ArrayList<Map>();
		
	}

	/**
	 *  根据id查找场所 
	 */
	@Override
	public CloudSite getCloudSiteById(int siteId) {
		CloudSite site = null;
		try {
			site = dao.fetch(CloudSite.class,Cnd.where("id","=",siteId));
		} catch (Exception e) {
			logger.error(this.getClass().getCanonicalName(),e);
		}
		return site;
	}
	
	/**
	 * 根据场所id和priceInfoId获取场所对应的价格配置信息,该配置必须is_stoped=0
	 * @param siteId
	 * @param priceInfoId
	 * @return
	 */
	@Override
	public SitePriceConfig getSitePriceInfos(int siteId,int priceInfoId){
		SitePriceConfig priceInfo=this.dao.fetch(SitePriceConfig.class, Cnd.where("site_id","=",siteId)
				.and("id", "=", priceInfoId).and("is_stoped", "=", 0));
		return priceInfo;
	}
	
	
	
	/**
	 * 校验校园卡充值支付时的参数是否正确
	 * @param map
	 * @return 校验通过返回 "ok";不通过返回错误原因字符串。
	 */
	@Override
	public String paramsCheck(Map<String,String> map){
		//map非空校验
		if(map.containsValue(null)||map.containsValue("")||map.containsValue("null"))return "参数不能含有空值";
		 
		//根据 siteId,sitePriceInfoId查询店铺配置的计费信息，非空
		SitePriceConfig spcf=this.getSitePriceInfos(Integer.parseInt(map.get("storeId")),Integer.parseInt(map.get("payType")));
		
		if(spcf==null)return "付费类型不存在";
		
		//校验金额  BigDecimal
		BigDecimal pageAmount=new BigDecimal(map.get("amount"));
		BigDecimal calculateResult=BigDecimalUtil.multiply(spcf.getUnit_price(), Integer.parseInt(map.get("buyNum")));
		if(!BigDecimalUtil.doubleValueEquals(pageAmount, calculateResult)){
			return "购买产品单价与配置信息不符";
		}
		//校验赠送的套餐
		if(!map.get("addMealNum").equals("0")){
			if(spcf.getGiveMeal()!=Integer.parseInt(map.get("addMealNum"))||spcf.getGiveMealUnit()!=Integer.parseInt(map.get("addMealUnit"))){
				return "购买赠送单价或数量与配置信息不符";
			}
		}
		return "ok";
	}
	/**
	 * 根据ProUserId查找场所客户账户
	 * @param prouserid
	 * @return
	 */
	@Override
	public SiteCustomerInfo getExpirationTimeByProuserid(int prouserid,int siteId){
		SiteCustomerInfo siteInfo=null;
		try {
		    siteInfo=this.dao.fetch(SiteCustomerInfo.class,Cnd.where("portal_user_id","=",prouserid).and("site_id","=",siteId));
		} catch (Exception e) {
			logger.error("根据ProUserId查找场所客户账户失败");
		}
		return siteInfo;
	}

	/**
	 * 用户缴费时计算用户的到期套餐
	 * @param scii
	 * @param scf
	 * @param map
	 * @return
	 */
	@Override
	public String getUserCustomer(SiteCustomerInfo scii,SitePriceConfig scf,Map<String ,String>map){
		String riqi="";
		int flow=0;
		if("1".equals(map.get("mealType"))){//用户购买的是时间套餐
			
			//根据和当前时间的比较计算到期时间
			//没缴过费的话
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			
			String str1 = sdf.format(now.getTime());//当前时间
			String str2 = "";
			if(scii==null){
				str2=sdf.format(now.getTime());
			}else{
				//如果用户有记录但是只有购买流量的记录,这时用户的过期时间为null
				if(scii.getExpirationTime()==null||scii.getExpirationTime().equals("null")||"".equals(scii.getExpirationTime())){
					str2=sdf.format(now.getTime());
				}else{
					str2 = sdf.format(scii.getExpirationTime().getTime());
				}
			}
			int cmp =DateUtil.compareDate(str1,str2);
			if(cmp==1){
				//到期时间小于等于当前时间时，在当前时间的基础上计算新的到期时间
				riqi = DateUtil.newDatePluss(scf.getPrice_type(), Integer.parseInt(map.get("buyNum")),str1,map.get("priceNum"));	
			}else{
				//到期时间大于当前时间时，在到期时间基础上计算新的到期时间
				riqi = DateUtil.newDatePluss(scf.getPrice_type(), Integer.parseInt(map.get("buyNum")),str2,map.get("priceNum"));	
			}
			if(map.get("addMealNum")!="0"&&map.get("addMealNum")!=null&&map.get("addMealUnit")!=null){
				riqi=DateUtil.newDatePluss(Integer.parseInt(map.get("addMealUnit")), Integer.parseInt(map.get("addMealNum")),riqi,"1");
			}
		}else{
			
			if(scii!=null){
				//如果用户有记录但是只有购买时间的记录,这时用户的流量为null
				if(scii.getTotalFlow()!=null&&!scii.getTotalFlow().equals("null")){
					flow=Integer.parseInt(scii.getTotalFlow());
				}
				if(scf.getPrice_type()==4){//购买的套餐是M
					flow=flow+Integer.parseInt(map.get("buyNum"))*Integer.parseInt(map.get("priceNum"))*1024;
				}else{//购买的套餐时G
					flow=flow+Integer.parseInt(map.get("buyNum"))*Integer.parseInt(map.get("priceNum"))*1024*1024;
				}
				//如果用户购买的套餐有赠送流量
				if(map.get("addMealNum")!=null&&map.get("addMealUnit")!=null){
					if("4".equals(map.get("addMealUnit"))){
						flow=flow+Integer.parseInt(map.get("addMealNum"))*1024;
					}else{
						flow=flow+Integer.parseInt(map.get("addMealNum"))*1024*1024;
					}
				}
				
			}else{
				if(scf.getPrice_type()==4){//购买的套餐是M
					flow=flow+Integer.parseInt(map.get("buyNum"))*Integer.parseInt(map.get("priceNum"))*1024;
				}else{//购买的套餐时G
					flow=flow+Integer.parseInt(map.get("buyNum"))*Integer.parseInt(map.get("priceNum"))*1024*1024;
				}
				//如果用户购买的套餐有赠送流量
				if(map.get("addMealNum")!=null&&map.get("addMealUnit")!=null){
					if("4".equals(map.get("addMealUnit"))){
						flow=flow+Integer.parseInt(map.get("addMealNum"))*1024;
					}else{
						flow=flow+Integer.parseInt(map.get("addMealNum"))*1024*1024;
					}
				}
			}
			riqi=String.valueOf(flow);
		}
		return riqi;
	}
	/**保存用户支付的记录信息
	 * @param orderNum
	 * @param map
	 * @return 0失败。1成功
	 */
	@Override
	public boolean savePaymentinfo(String orderNum,Map<String,String> map,int payType){
		try {
			
			String json=JSON.toJSONString(map);
			String payUser="";
			String tel="";
			SitePaymentRecord paymentRecord =new SitePaymentRecord(); 
			paymentRecord.setOrderNum(orderNum);
			paymentRecord.setUserId(Integer.parseInt(map.get("userId")));
			paymentRecord.setSiteId(Integer.parseInt(map.get("storeId")));
			paymentRecord.setParamJson(json);
			paymentRecord.setTradeNum("");
			paymentRecord.setFailReason("");
			paymentRecord.setPayType(payType);
			paymentRecord.setOutPayUser(tel=map.get("payUser")==null?null:map.get("payUser"));
			paymentRecord.setInputPayUser(payUser=map.get("tel")==null?null:map.get("tel"));
			this.dao.insert(paymentRecord);
			return paymentRecord.getId()>0;
		} catch (Exception e) {
			 logger.error(this.getClass().getCanonicalName(),e);
		}
		return false;
	}
	
	
	/**
	 * 根据orderNum获取相关记录
	 * @param orderNum
	 * @return
	 */
	public SitePaymentRecord getRecordByOrderNum(String orderNum){
		try {
			
			return this.dao.fetch(SitePaymentRecord.class, Cnd.where("orderNum", "=", orderNum));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @Description  更新场所收入表
	 * @date 2017年1月10日下午5:57:50
	 * @author guoyingjie
	 * @param amount
	 * @param siteId
	 * @param userId
	 * @return
	 */
	@Override
	public int updateALLIncome(BigDecimal amount, int siteId, int userId){
		String sql="UPDATE t4_income_collect SET platform_income=platform_income+@amount,account_income=account_income+@iamount WHERE user_id=@userId";
        IncomeCollect incomeCollect = this.dao.fetch(IncomeCollect.class,Cnd.where("user_id","=",userId));
        if (null==incomeCollect) {
			return 0;
		}else{
			Sql sqls= Sqls.create(sql);
			sqls.params().set("amount", amount).set("iamount", amount).set("userId",userId);
			this.dao.execute(sqls);
			return 1;
		}
	}
	
	/**
	 * @Description  更新场所收入表
	 * 
	 * @date 2017年1月10日下午5:57:50
	 * 
	 * @author guoyingjie
	 * 
	 * @param amount
	 * 
	 * @param siteId
	 * 
	 * @param userId
	 * 
	 * @return
	 */
	@Override
	public int updateSiteIncome(BigDecimal amount, int siteId, int userId){
		String sql="UPDATE t4_site_income_collect SET platform_income=platform_income+@amount,account_income=account_income+@iamount WHERE user_id=@userId AND site_id=@siteId";
        IncomeCollect incomeCollect = this.dao.fetch(IncomeCollect.class,Cnd.where("user_id","=",userId));
        if (null==incomeCollect) {
			return 0;
		}else{
			Sql sqls= Sqls.create(sql);
			sqls.params().set("amount", amount).set("iamount", amount).set("siteId",siteId).set("userId",userId);
			this.dao.execute(sqls);
			return 1;
		}
	}
	
	/**
	 * 
	 * @Description:更新商户汇总表和场所收入表	
	 * @author songyanbiao
	 * @date 2016年7月15日 下午3:32:06
	 * @param
	 * @return
	 */
	@Override
	public boolean updateCollect(BigDecimal amount, int siteId, int userId) {
		int upback = 0;
		int upback1=0;
		try {
			upback=this.updateALLIncome(amount, siteId, userId);
			upback1=this.updateSiteIncome(amount, siteId, userId);
			if(upback==0&&upback1==0){
				synchronized (lock) {
					IncomeCollect incomeCollect = this.dao.fetch(IncomeCollect.class,Cnd.where("user_id", "=", userId));
					if(incomeCollect==null){
						incomeCollect=new IncomeCollect();
						CommonConfig comconfig = this.dao.fetch(CommonConfig.class,Cnd.where("ident","=","881"));
						incomeCollect.setUserId(userId+"");
						incomeCollect.setPlatformIncome(amount);
						incomeCollect.setAccountRefund(new BigDecimal("0"));
						incomeCollect.setOfflineIncome(new BigDecimal("0"));
						incomeCollect.setWithdrawTime(new Date().getTime());
						incomeCollect.setLowestMoney(comconfig.getCommonMinMoney());
						incomeCollect.setShortestCycle(comconfig.getCommonBalanceday());
						incomeCollect.setChargeRate(comconfig.getCommonChagrge());
						incomeCollect.setAccountIncome(amount);
						upback = this.dao.insert(incomeCollect).getId();

					}else{
						upback=updateALLIncome(amount,siteId,userId);

					}
					SiteIncomeCollect sitincomeCollect = this.dao.fetch(SiteIncomeCollect.class,Cnd.where("site_id", "=", siteId).and("user_id","=",userId));
					if(sitincomeCollect==null){
						sitincomeCollect=new SiteIncomeCollect();
						sitincomeCollect.setSiteId(siteId+"");
						sitincomeCollect.setUserId(userId+"");
						sitincomeCollect.setPlatformIncome(amount);
						sitincomeCollect.setAccountIncome(amount);
						sitincomeCollect.setOfflineIncome(new BigDecimal("0"));
						sitincomeCollect.setAccounRrefund(new BigDecimal("0"));
						upback1 = this.dao.insert(sitincomeCollect).getId();

					}else{
						upback1=updateSiteIncome(amount, siteId,userId);

					}
				}
			}
			if(upback==0&&upback1!=0){
				IncomeCollect incomeCollect = this.dao.fetch(IncomeCollect.class,Cnd.where("user_id", "=", userId));
				if(incomeCollect==null){
					incomeCollect=new IncomeCollect();
					CommonConfig comconfig = this.dao.fetch(CommonConfig.class,Cnd.where("ident","=","881"));
					incomeCollect.setUserId(userId+"");
					incomeCollect.setPlatformIncome(amount);
					incomeCollect.setAccountRefund(new BigDecimal("0"));
					incomeCollect.setOfflineIncome(new BigDecimal("0"));
					incomeCollect.setWithdrawTime(new Date().getTime());
					incomeCollect.setLowestMoney(comconfig.getCommonMinMoney());
					incomeCollect.setShortestCycle(comconfig.getCommonBalanceday());
					incomeCollect.setChargeRate(comconfig.getCommonChagrge());
					incomeCollect.setAccountIncome(amount);
					upback = this.dao.insert(incomeCollect).getId();
				}else{
					upback=updateALLIncome(amount, siteId, userId);

				}
			}
			
			if(upback!=0&&upback1==0){
				SiteIncomeCollect sitincomeCollect = this.dao.fetch(SiteIncomeCollect.class,Cnd.where("site_id", "=", siteId).and("user_id","=",userId));
				if(sitincomeCollect==null){
					sitincomeCollect=new SiteIncomeCollect();
					sitincomeCollect.setSiteId(siteId+"");
					sitincomeCollect.setUserId(userId+"");
					sitincomeCollect.setPlatformIncome(amount);
					sitincomeCollect.setAccountIncome(amount);
					sitincomeCollect.setOfflineIncome(new BigDecimal("0"));
					sitincomeCollect.setAccounRrefund(new BigDecimal("0"));
					upback1 = this.dao.insert(sitincomeCollect).getId();
				}else{
					upback1=updateSiteIncome(amount, siteId, userId);

				}
			}
		} catch (Exception e) {
			logger.error("用户缴费插入汇总表报错,缴费金额==="+amount+"商户id==="+userId+"场所id==="+siteId);
		}
		if(upback>0&&upback1>0){
			return true;
		}else{
			return false;

		}
	}
	
	
	/**
	 * 更新失败原因
	 * @param failReason
	 * @param orderNum
	 * @return
	 */
	@Override
	public int updateFailReason(String failReason,String orderNum){
		SitePaymentRecord spr=getRecordByOrderNum(orderNum);
		spr.setFailReason(failReason);
		return this.dao.update(spr);
	}
	/**
	 * 更新用户到期套餐 时间或者流量
	 * @param expireFlow
	 * @param siteId
	 * @param userId
	 * @return
	 */
	@Override
	public int changeUserExpireMeal(Map<String,String> map){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SiteCustomerInfo sci=this.dao.fetch(SiteCustomerInfo.class, Cnd.where("siteId","=",Integer.parseInt(map.get("storeId"))).and("portal_user_id","=",Integer.parseInt(map.get("userId"))));
		try {
			if(sci==null){
				sci=new SiteCustomerInfo();
				if("1".equals(map.get("mealType"))){
					sci.setExpirationTime(map.get("expireDate"));
					sci.setTotalFlow("0");
					sci.setLastFlow("0");
					String days=DateUtil.dateDiff1(DateUtil.parse(map.get("expireDate")).getTime()-new Date().getTime());
					sci.setAllDay(days);
				}else{
					sci.setExpirationTime(new Date());
					sci.setTotalFlow(map.get("expireFlow"));
					sci.setLastFlow(map.get("expireFlow"));
				}
				sci.setSiteId(Integer.parseInt(map.get("storeId")));
				sci.setPortalUserId(Integer.parseInt(map.get("userId")));
				sci.setCreateTime(new Timestamp(new Date().getTime()));
				sci.setIsTry(1);
				sci.setPayWay(0);
				sci.setUpdateTime(new Timestamp(new Date().getTime()));
				sci.setLottertTime("1970-01-01 00:00:00");
				this.dao.insert(sci);
			}else{
				if("1".equals(map.get("mealType"))){
					Date now = new Date();
					String str1 = sdf.format(now.getTime());//当前时间
					int cmp =DateUtil.compareDate(str1,sdf.format(sci.getExpirationTime().getTime()));
					//修改用户总时间
					if(cmp==1){
						String days = DateUtil.dateDiff1(DateUtil.parse(map.get("expireDate")).getTime()-now.getTime());
						sci.setAllDay(days);
					}else{
						String days = DateUtil.dateDiff1(DateUtil.parse(map.get("expireDate")).getTime()-sci.getExpirationTime().getTime());
						String dayq=sci.getAllDay();
						if(dayq == null ){//为老用户充值
							dayq= DateUtil.dateDiff1(sci.getExpirationTime().getTime()-now.getTime());
						}
						String newDays=DateUtil.dateAdd(days, dayq);
						sci.setAllDay(newDays);
					}
					sci.setExpirationTime(map.get("expireDate"));
					if(sci.getTotalFlow()==null){
						sci.setTotalFlow("0");
					}
					
				}else{
					sci.setTotalFlow(map.get("expireFlow"));
					sci.setLastFlow(map.get("expireFlow"));
					if(sci.getExpirationTime()==null){
						sci.setExpirationTime(new Date());
					}
				}
				sci.setSiteId(Integer.parseInt(map.get("storeId")));
				sci.setPortalUserId(Integer.parseInt(map.get("userId")));
				sci.setIsTry(0);
				Date date = sci.getLottertTime();
				if(date==null){
					sci.setLottertTime("1970-01-01 00:00:00"); 
				}else{
					sci.setLottertTime(date); 
				}
				this.dao.update(sci);
			}
			return sci.getId()>0?1:0;
		} catch (Exception e) {
			logger.error("更新用户到期套餐 时间或者流量失败");
			return 0;
		}
	}
	
	
	/**
	 * 保存场所收入记录
	 * @param amount
	 * @param siteId
	 * @param userId
	 * @param userName
	 * @return  1成功
	 * 交易类型1--支付宝 2--银行卡 3--微信 4--人工
	 */
	@Override
	public int saveSchooleFinanceRecord(BigDecimal amount,int siteId,int userId,String userName,int buy_num,String payName,int payType){
		SiteIncome si=new SiteIncome();
		si.setSiteId(siteId);
		si.setPortalUserId(userId);
		si.setPortalUserName(userName);
		si.setTransactionAmount(amount);
		si.setPayName(payName);
		si.setBuyNum(buy_num);
		si.setPayType(payType);
		this.dao.insert(si);
		return si.getId()>0?1:0;
	}
	/**
	 * 更新支付记录为成功
	 * @param tradeNum
	 * @param orderNum
	 * @return 1成功
	 */
	@Override
	public int updateToFinish(String tradeNum,String orderNum){
		SitePaymentRecord spr=this.dao.fetch(SitePaymentRecord.class, Cnd.where("orderNum", "=", orderNum));
		spr.setIsFinish(1);
		spr.setTradeNum(tradeNum);
		spr.setFinishTime(new Date());
		spr.setFailReason("");
		return this.dao.update(spr);
	}
	
	/**
	 * 更新支付记录为成功
	 * @param tradeNum
	 * @param orderNum
	 * @return 1成功
	 */
	@Override
	public int updateToFinish(String orderNum){
		SitePaymentRecord spr=this.dao.fetch(SitePaymentRecord.class, Cnd.where("orderNum", "=", orderNum));
		spr.setIsFinish(1);
		spr.setTradeNum("");
		spr.setFinishTime(new Date());
		spr.setFailReason("");
		return this.dao.update(spr);
	}
	
	/**
	 * 支付结果通知接口
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@Override
	public void Notify(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//获取支付宝POST过来反馈信息
		PrintWriter out=response.getWriter();
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		if(requestParams==null || requestParams.size()==0){
			logger.error("requestParams==null-----SchoolPaymentService-203行");
			out.write("fail");
			return;
		}
		for (Iterator<?> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		Map<String,String> map=alipayNet.getNotifyParams(params);
		String result="fail";
		if(alipayNet.verify(params)){//验证成功
			if (map.get("trade_status").equals("TRADE_SUCCESS")){
				result=AliPayNotify(map.get("out_trade_no"), map.get("trade_no"));
			}
			out.write(result);	//请不要修改或删除
		}else{//验证失败
				logger.error("验证失败---fail");
			out.write("fail");
		}
		
	}
	
	/**
	 * 阿里异步通知处理方法
	 * @param orderNum  订单号
	 * @param tradeNum 支付宝交易号
	 * @return  "success" 成功 ， "fail" 失败
	 */
	@Override
	public String AliPayNotify(final String orderNum,final String tradeNum){
		//synchronized (LOCK_OBJECT) {
			//获取校园卡支付记录
			SitePaymentRecord payRecord=this.getRecordByOrderNum(orderNum);
			
			if(payRecord==null){//无支付记录
				logger.error("校园卡支付记录获取失败--orderNum:"+orderNum+";支付宝交易号："+tradeNum);
				return "fail";
			}
			//校园卡支付记录表状态校验
			if(payRecord.getIsFinish()==1||payRecord.getIsFinish()==-1){//支付状态为支付成功
				return "success";
			}
			//拿到paramMap,校验与系统中的用户状态是不是不一样。
			final Map<String,String> map=JSON.parseObject(payRecord.getParamJson(), Map.class);
			String checkResult= paramsCheck(map);
			if(!"ok".equals(checkResult)){
					logger.error("支付订单保留参数与系统现有数据不一致--orderNum:"+orderNum+";支付宝交易号："+tradeNum);
					this.updateFailReason("支付订单保留参数与系统现有数据不一致",orderNum);
					return "fail";
			}
			try {
		        //事务
		        Trans.exec(new Atom(){
		        	@Override
		        	public void run() {
		        		 
		               //修改支付用户的到期 时间
		        		int i= changeUserExpireMeal(map);
		        		if(i!=1){
		        			updateFailReason("修改支付用户的到期 时间失败",orderNum);
		        			throw Lang.makeThrow("修改支付用户的到期 时间失败--orderNum:"+orderNum+";支付宝交易号："+tradeNum);
		        			//return "fail";
		        		}
		        		
		        		//校园卡账务信息表添加记录
		        		String userName=getUserById(Integer.parseInt(map.get("userId"))).getUserName();
		        		i=saveSchooleFinanceRecord(new BigDecimal(map.get("amount")),Integer.parseInt(map.get("storeId")),
		        				Integer.parseInt(map.get("userId")),userName,Integer.parseInt(map.get("buyNum")),map.get("priceName"),1);
		        		if(i!=1){
		        			updateFailReason("校园卡账务信息表添加记录失败",orderNum);
		        			throw Lang.makeThrow("校园卡账务信息表添加记录失败--orderNum:"+orderNum+";支付宝交易号："+tradeNum);
		        		}
		        		//校园卡支付记录表状态修改为支付成功
		        		i=updateToFinish(tradeNum,orderNum);
		        		if(i!=1){//执行不成功
		        			updateFailReason("校园卡支付记录表状态修改失败：",orderNum);
		        			throw Lang.makeThrow("校园卡支付记录表状态修改失败--orderNum:"+orderNum+";支付宝交易号："+tradeNum);
		        		}
		        		boolean y=updateCollect(new BigDecimal(map.get("amount")), Integer.parseInt(map.get("storeId")), Integer.parseInt(map.get("tenantId")));
						if(!y)
							throw Lang.makeThrow("计费表用户统计或场所统计插入或更新未成功"+ orderNum);
		        	}
		        });
	        }catch (Exception e) {
	        	logger.error("支付过程事务故障",e);
	        	e.printStackTrace();
	        	return "fail";
	        }
			return "success"; 
		//}
	}

	@Override
	public void updateIsFinish(SitePaymentRecord sitePaymentRecord) {
		 this.dao.update(sitePaymentRecord);
	}
	/**
	 * 京东支付异步通知处理方法
	 * 
	 * @param orderNum
	 *            订单号
	 * @return "success" 成功 ， "fail" 失败
	 */
	@SuppressWarnings("unchecked")
	public String jdPayNotify(final String orderNum) {
		// 获取校园卡支付记录
		
		SitePaymentRecord payRecord =  this.getRecordByOrderNum(orderNum);
		if (payRecord == null) {// 无支付记录
			logger.error("校园卡支付记录获取失败--orderNum:" + orderNum);
			return "fail";
		}
		// 校园卡支付记录表状态校验
		if (payRecord.getIsFinish() == 1||payRecord.getIsFinish()==-1) {// 支付状态为支付成功
			return "ok";
		}
		// 拿到paramMap,校验与系统中的用户状态是不是不一样。
		final Map<String, String> map = JSON.parseObject(
				payRecord.getParamJson(), Map.class);
		String checkResult = this.paramsCheck(map);
		if (!"ok".equals(checkResult)) {
			logger.error("支付订单保留参数与系统现有数据不一致--orderNum:" + orderNum);
			this.updateFailReason("支付订单保留参数与系统现有数据不一致",
					orderNum);
			return "fail";
		}

		try {
			// 事务
			Trans.exec(new Atom() {
				@Override
				public void run() {
					// 修改支付用户的到期 时间
					int i = changeUserExpireMeal(map);
					if (i != 1) {
						logger.error("修改支付用户的到期 时间失败--orderNum:" + orderNum);
						updateFailReason("修改支付用户的到期 时间失败", orderNum);
						throw Lang.makeThrow("修改支付用户的到期 时间失败--orderNum:"+ orderNum);
					}

					// 校园卡账务信息表添加记录
					String userName = getUserById(Integer.parseInt(map.get("userId"))).getUserName();
	        		i=saveSchooleFinanceRecord(new BigDecimal(map.get("amount")),Integer.parseInt(map.get("storeId")),
    				Integer.parseInt(map.get("userId")),userName,Integer.parseInt(map.get("buyNum")),map.get("priceName"),2);
					if (i != 1) {
						logger.error("校园卡账务信息表添加记录失败--orderNum:" + orderNum);
						updateFailReason("校园卡账务信息表添加记录失败", orderNum);
						throw Lang.makeThrow("校园卡账务信息表添加记录失败--orderNum:"+ orderNum);
					}

					// 校园卡支付记录表状态修改为支付成功
					i = updateToFinish(orderNum);
					if (i != 1) {// 执行不成功
						logger.error("校园卡支付记录表状态修改失败--orderNum:" + orderNum);
						updateFailReason("校园卡支付记录表状态修改失败：", orderNum);
						throw Lang.makeThrow("校园卡支付记录表状态修改失败--orderNum:"+ orderNum);
					}
					boolean y=updateCollect(new BigDecimal(map.get("amount")), Integer.parseInt(map.get("storeId")), Integer.parseInt(map.get("tenantId")));
					if(!y)
						throw Lang.makeThrow("计费表用户统计或场所统计插入或更新未成功"+ orderNum);
				}
			});
		} catch (Exception e) {
			logger.error("========未收到广东省=====4444" +  e);
			logger.error("支付过程事务故障", e);
			return "fail";
		}
		return "ok";
	}

	@Override
	public CloudUser getCloudUserById(int userId) {
		return this.dao.fetch(CloudUser.class,Cnd.where("user_id","=",userId));
	}

	/**
	 * 获取用户已开启套餐
	 */
	@Override
	public List<Map<String, Object>> getMealBySite(int siteId, int type) {
		Sql sql=Sqls.create("SELECT GROUP_CONCAT(id) ids,GROUP_CONCAT(unit_price ORDER BY id ASC) price,name,price_type,is_stoped,GROUP_CONCAT(charge_type ORDER BY charge_type ASC) charge_type,"+
			 	" price_num,GROUP_CONCAT(comboNumber) comboNumber,v2_recommend_state,v2_give_meal,v2_givemeal_unit,v2_describe"+
				" FROM t_site_price_config where site_id=@siteId and is_stoped=0 GROUP BY name ORDER BY is_stoped ,price_type ASC");// $condition
//		switch (type) {
//			case 0:sql.setCondition(Cnd.where("site_id","=",siteId).and("is_stoped","=","0").and("price_type","<","4"));break;
//			case 1:sql.setCondition(Cnd.where("site_id","=",siteId).and("is_stoped","=","0").and("price_type","<","6").and("price_type",">","3")).setParam("id", siteId);break;
//			case 2:sql.setCondition(Cnd.where("site_id","=",siteId).and("is_stoped","=","0").and("price_type",">","5")).setParam("id", siteId);break;
//		}
		sql.setParam("siteId", siteId);
		try {
			sql.setCallback(new SqlCallback() {
				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql)
						throws SQLException {
					List<Map<String, Object>> list= new ArrayList<Map<String, Object>>();
					Map<String, Object> map = null;
					while (rs.next()) {
						map = new HashMap<String, Object>();
						map.put("ids",rs.getString("ids"));
						map.put("price",rs.getString("price"));
						map.put("name",rs.getString("name"));
						map.put("price_type",rs.getString("price_type"));
						map.put("is_stoped",rs.getString("is_stoped"));
						map.put("charge_type",rs.getString("charge_type"));
						map.put("price_num",rs.getString("price_num"));
						map.put("comboNumber",rs.getString("comboNumber"));
						map.put("v2_recommend_state",rs.getString("v2_recommend_state"));
						map.put("v2_give_meal",rs.getString("v2_give_meal"));
						map.put("v2_givemeal_unit",rs.getString("v2_givemeal_unit"));
						map.put("v2_describe",rs.getString("v2_describe"));
						list.add(map);
					}
					return list;
				}
			});
			dao.execute(sql);
			return sql.getObject(List.class);
		} catch (Exception e) {
			logger.error("获取系统类型参数错误",e);
			return null;
		}
	}

	/**
	 * 根据用户订单查询用户
	 */
	@Override
	public PortalUser getUserByOrdernum(String orderNum) {
		PortalUser user = null;
		try {
			user = dao.fetch(PortalUser.class,Cnd.wrap("id = (SELECT user_id FROM t_sitepayment_records where order_num = '"+orderNum+"')"));
		} catch (Exception e) {
			logger.error(this.getClass().getCanonicalName());
		}
		return user;
	}

	/**
	 * 根据用户订单号查询用户充值场所
	 */
	@Override
	public CloudSite getSiteByOrdernum(String orderNum) {
		CloudSite site = null;
		try {
			site = dao.fetch(CloudSite.class,Cnd.wrap("id = (SELECT site_id FROM t_sitepayment_records where order_num = '"+orderNum+"')"));
		} catch (Exception e) {
			logger.error(this.getClass().getCanonicalName());
		}
		return site;
	}

	/**
	 * 判断支付宝是否支付成功
	 */
	@Override
	public boolean isAliPaySuccess(String ordrNum, String tradeNum, int siteId,int UserId) {
		SitePaymentRecord spr=dao.fetch(SitePaymentRecord.class, Cnd.where("tradeNum", "=", tradeNum)
				.and("orderNum", "=", ordrNum)
				.and("siteId", "=", siteId).and("userId", "=", UserId));
		if(spr!=null&&(spr.getIsFinish()==1||spr.getIsFinish()==-1)){
			 return true;
		}
		return false;
	}
	
	/**
	 * 判断jd是否支付成功
	 */
	@Override
	public boolean isJdPaySuccess(String orderNum) {
		SitePaymentRecord spr = dao.fetch(SitePaymentRecord.class,Cnd.where("order_num", "=", orderNum));
		if (spr != null && (spr.getIsFinish() == 1 || spr.getIsFinish() == -1)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取用户信息
	 */
	@Override
	public PortalUser getUserByUserNameAndPwd(String userName, String passWord) {
		return dao.fetch(PortalUser.class,
				Cnd.where("user_name", "=", userName).and("pass_word", "=",passWord));
	}

	/**
	 * 插入用户押金信息
	 */
	@Override
	public boolean insertCashPledge(CashPledge cashPledge) {
		 return this.dao.insert(cashPledge).getId()>0?true:false;
	}

	/**
	 * 插入月租信息
	 */
	@Override
	public boolean insertSitePaymentRecord(SitePaymentRecord sitePaymentRecord) {
		return this.dao.insert(sitePaymentRecord).getId()>0?true:false;
	}
}
