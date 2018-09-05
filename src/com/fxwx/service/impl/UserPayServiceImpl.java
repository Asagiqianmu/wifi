package com.fxwx.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.alibaba.fastjson.JSON;
import com.fxwx.bean.SitePriceConfigAll;
import com.fxwx.dao.UserPayDao;
import com.fxwx.entiy.CashPledge;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.CloudUser;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.entiy.SitePaymentRecord;
import com.fxwx.entiy.SitePriceConfig;
import com.fxwx.service.UserPayService;
import com.fxwx.util.BigDecimalUtil;
import com.fxwx.util.DateUtil;

@IocBean
public class UserPayServiceImpl implements UserPayService {

	@Inject
	private UserPayDao userPayDao;
	
	private static final Log logger = Logs.getLog(UserPayServiceImpl.class);

	@Override
	public PortalUser getUserById(int userId) {
		return userPayDao.getUserById(userId);
	}
	
	//获得场所下的按时间计费包
	@Override
	public List<Map> getTimeConfigList(int siteId,int userId) {
		PortalUser user = this.getUserById(userId);
        String username = user.getUserName();
		List<Map> list = userPayDao.getTimeConfigList(siteId);
		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			for (int j = i+1; j <list.size(); j++) {
				Map map2 = list.get(j);
				if(map.get("name").equals(map2.get("name"))){
					String com = (String) map.get("comboNumber");
					String com2 = (String) map2.get("comboNumber");
					if(null==map.get("comboNumber")){
						boolean is = false;
						for (int k = 0; k < com2.split(";").length; k++) {
							if(username.indexOf(com2.split(";")[k])>-1){
								list.remove(i);
								is = true;
								break;
							}
						}
						if(is==false){
							list.remove(j);
						}
					}
					if(null==map2.get("comboNumber")){
						boolean ok = false;
						for (int k = 0; k < com.split(";").length; k++) {
							if(username.indexOf(com.split(";")[k])>-1){
								list.remove(j);
								ok = true;
								break;
							}
						}
						if(ok==false){
							list.remove(i);
						}
					}
				}
			}
		}	
		return list;
	}
	//获得场所下的按流量计费包
	@Override
	public List<Map> getFlowConfigList(int siteId, int userId) {
		PortalUser user = this.getUserById(userId);
        String username = user.getUserName();
		List<Map> list = userPayDao.getFlowConfigList(siteId);
		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			for (int j = i+1; j <list.size(); j++) {
				Map map2 = list.get(j);
				if(map.get("name").equals(map2.get("name"))){
					String com = (String) map.get("comboNumber");
					String com2 = (String) map2.get("comboNumber");
					if(null==map.get("comboNumber")){
						boolean is = false;
						for (int k = 0; k < com2.split(";").length; k++) {
							if(username.indexOf(com2.split(";")[k])>-1){
								list.remove(i);
								is = true;
								break;
							}
						}
						if(is==false){
							list.remove(j);
						}
					}
					if(null==map2.get("comboNumber")){
						boolean ok = false;
						for (int k = 0; k < com.split(";").length; k++) {
							if(username.indexOf(com.split(";")[k])>-1){
								list.remove(j);
								ok = true;
								break;
							}
						}
						if(ok==false){
							list.remove(i);
						}
					}
				}
			}
		}	
		return list;
	}

	/**保存用户支付的记录信息
	 * @param orderNum
	 * @param map
	 * @return 0失败。1成功
	 */
	@Override
	public boolean savePaymentinfo(String orderNum, Map<String, String> map,
			int payType) {
		return userPayDao.savePaymentinfo(orderNum, map, payType);
	}

	@Override
	public SitePaymentRecord getRecordByOrderNum(String orderNum) {
		return userPayDao.getRecordByOrderNum(orderNum);
	}

	@Override
	public int updateALLIncome(BigDecimal amount, int siteId, int userId) {
		return userPayDao.updateALLIncome(amount, siteId, userId);
	}

	@Override
	public int updateSiteIncome(BigDecimal amount, int siteId, int userId) {
		return userPayDao.updateSiteIncome(amount, siteId, userId);
	}

	@Override
	public boolean updateCollect(BigDecimal amount, int siteId, int userId) {
		return userPayDao.updateCollect(amount, siteId, userId);
	}

	@Override
	public int updateFailReason(String failReason, String orderNum) {
		return userPayDao.updateFailReason(failReason, orderNum);
	}

	@Override
	public int changeUserExpireMeal(Map<String, String> map) {
		return userPayDao.changeUserExpireMeal(map);
	}

	@Override
	public int saveSchooleFinanceRecord(BigDecimal amount, int siteId,
			int userId, String userName, int buy_num, String payName,
			int payType) {
		return userPayDao.saveSchooleFinanceRecord(amount, siteId, userId, userName, buy_num, payName, payType);
	}
	@Override
	public int updateToFinish(String tradeNum, String orderNum) {
		return userPayDao.updateToFinish(tradeNum, orderNum);
	}

	@Override
	public void Notify(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			userPayDao.Notify(request, response);
	}

	@Override
	public String AliPayNotify(String orderNum, String tradeNum) {
		return userPayDao.AliPayNotify(orderNum, tradeNum);
	}

	//查找场所
	@Override
	public CloudSite getCloudSiteById(int siteId) {
		return userPayDao.getCloudSiteById(siteId);
	}

	@Override
	public CloudUser getCloudUserById(int userId) {
		return userPayDao.getCloudUserById(userId);
	}

	/**
	 * 获得场所价格配置信息
	 */
	@Override
	public SitePriceConfig getSitePriceInfos(int siteId, int priceInfoId) {
		return userPayDao.getSitePriceInfos(siteId, priceInfoId);
	}

	//检验是否符合支付标准
	@Override
	public String paramsCheck(Map<String, String> map) {
		return userPayDao.paramsCheck(map);
	}
	/**
	 * 根据ProUserId查找场所客户账户
	 * @param prouserid
	 * @return
	 */
	@Override
	public SiteCustomerInfo getExpirationTimeByProuserid(int prouserid,
			int siteId) {
		return userPayDao.getExpirationTimeByProuserid(prouserid, siteId);
	}
	/**
	 * 用户缴费时计算用户的到期套餐
	 * @param scii
	 * @param scf
	 * @param map
	 * @return
	 */
	@Override
	public String getUserCustomer(SiteCustomerInfo scii, SitePriceConfig scf,
			Map<String, String> map) {
		return userPayDao.getUserCustomer(scii, scf, map);
	}

	@Override
	public void updateIsFinish(SitePaymentRecord sitePaymentRecord) {
		userPayDao.updateIsFinish(sitePaymentRecord);
	}

	@Override
	public String jdPayNotify(String orderNum) {
		return userPayDao.jdPayNotify(orderNum);
	}
	/**
	 * 
	 * @Description:获取场所套餐包	
	 * @author songyanbiao
	 * @date 2016年9月19日 下午2:03:06
	 * @param
	 * @return
	 */
	public List<Map<String, Object>> getMealList(int siteId,int type){
		 return userPayDao.getMealBySite(siteId, type);
	}
	/**
	 * @Description 获得场所下的全部的消费配置
	 * @param siteId
	 * @param portalId
	 * @return
	 */
	public SitePriceConfigAll getSitePriceConfigAll(List<Map<String, Object>> listMeal, PortalUser portal,CloudSite site) {
		SitePriceConfigAll siteAll = new SitePriceConfigAll();
		try {
			List<SitePriceConfig> spcList = new ArrayList<SitePriceConfig>();
			int siteId=site.getId();
			if( listMeal.size()!=0){
				for (int i = 0; i < listMeal.size(); i++) {
					boolean flag=false;
					if(listMeal.get(i).get("comboNumber")!=null&&!(listMeal.get(i).get("comboNumber")+"").equals("")){
						for (int j = 0; j < (listMeal.get(i).get("comboNumber")+"").split(";").length; j++) {
							if(portal.getUserName().indexOf((listMeal.get(i).get("comboNumber")+"").split(";")[j])==0){
								SitePriceConfig spc=new SitePriceConfig();
								spc.setCharge_type(Integer.valueOf((listMeal.get(i).get("charge_type")+"").split(",")[1]));
								spc.setDescribe(listMeal.get(i).get("v2_describe")+"");
								spc.setGiveMeal(Integer.valueOf(listMeal.get(i).get("v2_give_meal")+""));
								spc.setGiveMealUnit(Integer.valueOf(listMeal.get(i).get("v2_givemeal_unit")+""));
								spc.setId(Integer.valueOf((listMeal.get(i).get("ids")+"").split(",")[1]));
								spc.setName(listMeal.get(i).get("name")+"");
								spc.setPrice_num(Integer.valueOf(listMeal.get(i).get("price_num")+""));
								spc.setPrice_type(Integer.valueOf(listMeal.get(i).get("price_type")+""));
								spc.setRecommendState(Integer.valueOf(listMeal.get(i).get("v2_recommend_state")+""));
								spc.setUnit_price(new BigDecimal((listMeal.get(i).get("price")+"").split(",")[1]));
								spc.setSite_id(siteId);
								spcList.add(spc);
								flag=false;
								break;
							}else{
								flag=true;
							}
						}
						if(flag){
							SitePriceConfig spc=new SitePriceConfig();
							spc.setCharge_type(Integer.valueOf((listMeal.get(i).get("charge_type")+"").split(",")[0]));
							spc.setDescribe(listMeal.get(i).get("v2_describe")+"");
							spc.setGiveMeal(Integer.valueOf(listMeal.get(i).get("v2_give_meal")+""));
							spc.setGiveMealUnit(Integer.valueOf(listMeal.get(i).get("v2_givemeal_unit")+""));
							spc.setId(Integer.valueOf((listMeal.get(i).get("ids")+"").split(",")[0]));
							spc.setName(listMeal.get(i).get("name")+"");
							spc.setPrice_num(Integer.valueOf(listMeal.get(i).get("price_num")+""));
							spc.setPrice_type(Integer.valueOf(listMeal.get(i).get("price_type")+""));
							spc.setRecommendState(Integer.valueOf(listMeal.get(i).get("v2_recommend_state")+""));
							spc.setUnit_price(new BigDecimal((listMeal.get(i).get("price")+"").split(",")[0]));
							spc.setSite_id(siteId);
							spcList.add(spc);
					  }
					}else{
						SitePriceConfig spc=new SitePriceConfig();
						spc.setCharge_type(Integer.valueOf(listMeal.get(i).get("charge_type")+""));
						spc.setDescribe(listMeal.get(i).get("v2_describe")+"");
						spc.setGiveMeal(Integer.valueOf(listMeal.get(i).get("v2_give_meal")+""));
						spc.setGiveMealUnit(Integer.valueOf(listMeal.get(i).get("v2_givemeal_unit")+""));
						spc.setId(Integer.valueOf(listMeal.get(i).get("ids")+""));
						spc.setName(listMeal.get(i).get("name")+"");
						spc.setPrice_num(Integer.valueOf(listMeal.get(i).get("price_num")+""));
						spc.setPrice_type(Integer.valueOf(listMeal.get(i).get("price_type")+""));
						spc.setRecommendState(Integer.valueOf(listMeal.get(i).get("v2_recommend_state")+""));
						spc.setUnit_price(new BigDecimal(listMeal.get(i).get("price")+""));
						spc.setSite_id(siteId);
						spcList.add(spc);
					}
					
				}
			}
			siteAll.setSiteInof(site);
			siteAll.setList(spcList);
			// 根据路由器Mac设置该路由所隶属场所的价格信息到seesion中，用以将价格信息传递到缴费页面
		} catch (Exception e) {
			logger.error(this.getClass().getCanonicalName()
					+ "===getSitePriceConfigAll", e);
			return null;
		}
		return siteAll;

	}
	/**
	 * 根据订单号查询用户
	 */
	@Override
	public PortalUser findPortalUserByOrderNum(String orderNum) {
		return userPayDao.getUserByOrdernum(orderNum);
	}

	/**
	 * 根据订单号查询用户充值场所
	 */
	@Override
	public CloudSite findCloudSiteByOrderNum(String orderNum) {
		return userPayDao.getSiteByOrdernum(orderNum);
	}

	/**
	 * 根据订单号查询用户使用支付宝充值是否成功
	 */
	@Override
	public boolean getSitePaymentRecordByTradeNum(String ordrNum,
			String tradeNum, int siteId, int UserId) {
		return userPayDao.isAliPaySuccess(ordrNum, tradeNum, siteId, UserId);
	}

	/**
	 * 根据订单号查询用户使用jd充值是否成功
	 */
	@Override
	public boolean checkPayResult(String outTradeNo) {
		return userPayDao.isJdPaySuccess(outTradeNo);
	}

	/**
	 * 用户待支付点击充值查询订单是否已经完成
	 */
	@Override
	public boolean checkeOrder(String orderNum,int siteId,int userId,String money,String priceName) {
		SitePaymentRecord payRecord=userPayDao.getRecordByOrderNum(orderNum);
		if(payRecord==null){//无支付记录
			logger.error("校园卡支付记录获取失败--orderNum:"+orderNum);
			return false;
		}
		//校园卡支付记录表状态校验
		if(payRecord.getIsFinish()==1||payRecord.getIsFinish()==-1){//支付状态为支付成功
			return false;
		}
		if(payRecord.getUserId()!=userId){
			return false;
		}
		if(payRecord.getSiteId()!=siteId){
			return false;
		}
		//校验订单的金额和页面传来的金额是否一致
		Map<String,String> map=JSON.parseObject(payRecord.getParamJson(), Map.class);
		if(!priceName.equals(map.get("priceName"))){
			return false;
		}
		if(!BigDecimalUtil.doubleValueEquals(new BigDecimal(money), new BigDecimal(map.get("amount")))){
			return false;
		}
		return true;
	}

	@Override
	public boolean insertCashPledge(CashPledge cashPledge) {
		return userPayDao.insertCashPledge(cashPledge);
	}

	@Override
	public boolean insertSitePaymentRecord(SitePaymentRecord sitePaymentRecord) {
		return userPayDao.insertSitePaymentRecord(sitePaymentRecord);
	}
}
