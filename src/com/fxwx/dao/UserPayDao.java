package com.fxwx.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.dao.Dao;
import org.omg.CORBA.INTERNAL;

import com.fxwx.bean.SitePriceConfigAll;
import com.fxwx.entiy.CashPledge;
import com.fxwx.entiy.CloudSite;
import com.fxwx.entiy.CloudUser;
import com.fxwx.entiy.Order;
import com.fxwx.entiy.PortalUser;
import com.fxwx.entiy.SiteCustomerInfo;
import com.fxwx.entiy.SitePaymentRecord;
import com.fxwx.entiy.SitePriceConfig;

public interface UserPayDao {

	//获得场所下的计费规则
	public SitePriceConfigAll getSitePriceConfigAll(int userId,int siteId);
	
	//根据用户id查找用户
	public PortalUser getUserById(int userId);
	
	//根据场所id查找场所
	public CloudSite getCloudSiteById(int siteId);
	
	//根据场所id查找场所
	public CloudUser getCloudUserById(int userId);
	
	
	//获得场所下的按时间计费包
	public List<Map> getTimeConfigList(int siteId);
	
	//获得场所下的按流量计费包
	public List<Map> getFlowConfigList(int siteId);
	
	//获得场所价格配置
	public SitePriceConfig getSitePriceInfos(int siteId,int priceInfoId);
	
	//检验是否符合支付标准
	public String paramsCheck(Map<String,String> map);
	
	/**
	 * 根据ProUserId查找场所客户账户
	 * @param prouserid
	 * @return
	 */
	public SiteCustomerInfo getExpirationTimeByProuserid(int prouserid,int siteId);
	/**
	 * 用户缴费时计算用户的到期套餐
	 * @param scii
	 * @param scf
	 * @param map
	 * @return
	 */
	public String getUserCustomer(SiteCustomerInfo scii,SitePriceConfig scf,Map<String ,String>map);
	
	/**保存用户支付的记录信息
	 * @param orderNum
	 * @param map
	 * @return 0失败。1成功
	 */
	public boolean savePaymentinfo(String orderNum,Map<String,String> map,int payType);
	/**
	 * 根据orderNum获取相关记录
	 * @param orderNum
	 * @return
	 */
	public SitePaymentRecord getRecordByOrderNum(String orderNum);
	
	/**
	 * @Description  更新场所收入表
	 * @date 2017年1月10日下午5:57:50
	 * @author guoyingjie
	 * @param amount
	 * @param siteId
	 * @param userId
	 * @return
	 */
	public int updateALLIncome(BigDecimal amount, int siteId, int userId);
	/**
	 * @Description  更新场所收入表
	 * @date 2017年1月10日下午5:57:50
	 * @author guoyingjie
	 * @param amount
	 * @param siteId
	 * @param userId
	 * @return
	 */
	public int updateSiteIncome(BigDecimal amount, int siteId, int userId);
	/**
	 * 
	 * @Description:更新商户汇总表和场所收入表	
	 * @author songyanbiao
	 * @date 2016年7月15日 下午3:32:06
	 * @param
	 * @return
	 */
	public boolean updateCollect(BigDecimal amount, int siteId, int userId);
	/**
	 * 更新失败原因
	 * @param failReason
	 * @param orderNum
	 * @return
	 */
	public int updateFailReason(String failReason,String orderNum);
	/**
	 * 更新用户到期套餐 时间或者流量
	 * @param expireFlow
	 * @param siteId
	 * @param userId
	 * @return
	 */
	public int changeUserExpireMeal(Map<String,String> map);
	/**
	 * 保存场所收入记录
	 * @param amount
	 * @param siteId
	 * @param userId
	 * @param userName
	 * @return  1成功
	 * 交易类型1--支付宝 2--银行卡 3--微信 4--人工
	 */
	public int saveSchooleFinanceRecord(BigDecimal amount,int siteId,int userId,String userName,int buy_num,String payName,int payType);
	/**
	 * 更新支付记录为成功
	 * @param tradeNum
	 * @param orderNum
	 * @return 1成功
	 */
	public int updateToFinish(String tradeNum,String orderNum);
	/**
	 * 支付结果通知接口
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void Notify(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	
	/**
	 * 阿里异步通知处理方法
	 * @param orderNum  订单号
	 * @param tradeNum 支付宝交易号
	 * @return  "success" 成功 ， "fail" 失败
	 */
	public String AliPayNotify(final String orderNum,final String tradeNum);

	/**
	 * 
	 * @Description  修改该订单号为-1.防止用户返回的时候更改状态
	 * @date 2017年1月12日下午2:55:01
	 * @author guoyingjie
	 * @param sitePaymentRecord
	 */
	public void updateIsFinish(SitePaymentRecord sitePaymentRecord);
	/**
	 * 更新支付记录为成功
	 * @param tradeNum
	 * @param orderNum
	 * @return 1成功
	 */
	int updateToFinish(String orderNum);
	/**
	 * 京东支付异步通知处理方法
	 * 
	 * @param orderNum
	 *            订单号
	 * @return "success" 成功 ， "fail" 失败
	 */
	public String jdPayNotify(final String orderNum);
	
	/*获取场所已开套餐*/
	public List<Map<String, Object>> getMealBySite(int siteId,int type);
	
	/*根据订单查询用户*/
	public PortalUser getUserByOrdernum(String orderNum);
	
	/*根据订单号查询场所*/
	public CloudSite getSiteByOrdernum(String orderNum);
	
	/*判断支付宝是否支付成功*/
	public boolean isAliPaySuccess(String ordrNum,String tradeNum, int siteId, int UserId);
	
	/*判断jd是否支付成功*/
	public boolean isJdPaySuccess(String orderNum);
	
	/**
	 * 获取用户
	 * @param userName
	 * @param passWord
	 * @return
	 */
	PortalUser getUserByUserNameAndPwd(String userName,String passWord);
	
	boolean insertCashPledge(CashPledge cashPledge);
	
	/**
	 * 插入月租信息
	 * @param sitePaymentRecord
	 * @return
	 */
	boolean  insertSitePaymentRecord(SitePaymentRecord sitePaymentRecord);
}
