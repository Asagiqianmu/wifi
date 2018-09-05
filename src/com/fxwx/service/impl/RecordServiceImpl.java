package com.fxwx.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nutz.dao.QueryResult;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.fxwx.bean.RecordOrderBean;
import com.fxwx.dao.RecordDao;
import com.fxwx.service.RecordService;

@IocBean
public class  RecordServiceImpl implements  RecordService {

	public Logger logger = Logger.getLogger(RecordServiceImpl.class);
	@Inject
	private  RecordDao recordDao;
	
	/**
	 * @Description  获得用户在该场所下的全部订单的记录
	 * @date 2017年1月11日上午10:12:41
	 * @author guoyingjie
	 * @param userId----->用户id
	 * @param siteId------->场所id
	 * @param currentPag---->当前页
	 * @param pageSize------>每页的条数
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> allOrderRecards(int userId, int siteId,
			int currentPage, int pageSize,String siteName) {
		Map<String, Object> map=null;
		List<RecordOrderBean> list = null;
		try {
			//list = recordDao.allOrderRecards(userId, siteId, currentPage, pageSize,siteName);
			map = recordDao.allOrderRecards(userId, siteId, currentPage, pageSize,siteName);
			list=(List<RecordOrderBean>)map.get("list");
			for (int j = 0; j < list.size(); j++) {
				RecordOrderBean rb = list.get(j);
				if(rb.getIsFinishi()==1||rb.getIsFinishi()==-1){
					rb.setState("已完成");
				}else{
					long utime =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rb.getTimestamp()).getTime();
					long nowtime = new Date().getTime();
					long btime = 20*60*1000;//20分钟
					if((nowtime-utime)>btime){
						rb.setState("已失效");
					}else{
						rb.setState("待支付");
					}
				}
				String[] ssStrings = rb.getParamString().split(",");
				for (int i = 0; i < ssStrings.length; i++) {
					if(ssStrings[i].indexOf("amount")>-1){
						float amount = Float.valueOf(ssStrings[i].split(":")[1].replaceAll("\"",""));
						rb.setAllPirce(amount);
					}
					if(ssStrings[i].indexOf("buyNum")>-1){
						int buynum = Integer.valueOf(ssStrings[i].split(":")[1].replaceAll("\"",""));
						rb.setBuyNum(buynum);
					}
					if(ssStrings[i].indexOf("priceName")>-1){
						String priceName = ssStrings[i].split(":")[1].replaceAll("\"","");
						rb.setPriceName(priceName);
					}
					if(ssStrings[i].indexOf("payName")>-1){
						String priceName = ssStrings[i].split(":")[1].replaceAll("\"","");
						rb.setPriceName(priceName);
					}
					
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getCanonicalName(),e);
		}
		return map;
	}
	
	/**
	 * @Description  获得用户在该场所下的待支付订单记录
	 * @date 2017年1月11日上午10:12:41
	 * @author guoyingjie
	 * @param userId----->用户id
	 * @param siteId------->场所id
	 * @param currentPag---->当前页
	 * @param pageSize------>每页的条数
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> noPaymentState(int userId, int siteId,
			int currentPage, int pageSize,String siteName) {
		Map<String, Object> map=null;
		List<RecordOrderBean> list = null;
		try {
			//list = recordDao.noPaymentState(userId, siteId, currentPage, pageSize,siteName);
			map=recordDao.noPaymentState(userId, siteId, currentPage, pageSize,siteName);
			list=(List<RecordOrderBean>)map.get("list");
			for (int j = 0; j < list.size(); j++) {
				RecordOrderBean rb = list.get(j);
				rb.setState("待支付");
				String[] ssStrings = rb.getParamString().split(",");
				for (int i = 0; i < ssStrings.length; i++) {
					if(ssStrings[i].indexOf("amount")>-1){
						float amount = Float.valueOf(ssStrings[i].split(":")[1].replaceAll("\"",""));
						rb.setAllPirce(amount);
					}
					if(ssStrings[i].indexOf("buyNum")>-1){
						int buynum = Integer.valueOf(ssStrings[i].split(":")[1].replaceAll("\"",""));
						rb.setBuyNum(buynum);
					}
					if(ssStrings[i].indexOf("priceName")>-1){
						String priceName = ssStrings[i].split(":")[1].replaceAll("\"","");
						rb.setPriceName(priceName);
					}
					if(ssStrings[i].indexOf("payName")>-1){
						String priceName = ssStrings[i].split(":")[1].replaceAll("\"","");
						rb.setPriceName(priceName);
					}
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getCanonicalName(),e);
		}
		return map;
	}
	/**
	 * @Description  获得用户在该场所下的已完成订单
	 * @date 2017年1月11日上午10:12:41
	 * @author guoyingjie
	 * @param userId----->用户id
	 * @param siteId------->场所id
	 * @param currentPag---->当前页
	 * @param pageSize------>每页的条数
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> finishOrder(int userId, int siteId,
			int currentPage, int pageSize,String siteName) {
		Map<String, Object> map=null;
		List<RecordOrderBean> list = null;
		try {
			//list = recordDao.finishOrder(userId, siteId, currentPage, pageSize,siteName);
			map=recordDao.finishOrder(userId, siteId, currentPage, pageSize,siteName);
			list=(List<RecordOrderBean>)map.get("list");
			for (int j = 0; j < list.size(); j++) {
				RecordOrderBean rb = list.get(j);
				rb.setState("已完成");
				String[] ssStrings = rb.getParamString().split(",");
				for (int i = 0; i < ssStrings.length; i++) {
					if(ssStrings[i].indexOf("amount")>-1){
						float amount = Float.valueOf(ssStrings[i].split(":")[1].replaceAll("\"",""));
						rb.setAllPirce(amount);
					}
					if(ssStrings[i].indexOf("buyNum")>-1){
						int buynum = Integer.valueOf(ssStrings[i].split(":")[1].replaceAll("\"",""));
						rb.setBuyNum(buynum);
					}
					if(ssStrings[i].indexOf("priceName")>-1){
						String priceName = ssStrings[i].split(":")[1].replaceAll("\"","");
						rb.setPriceName(priceName);
					}
					if(ssStrings[i].indexOf("payName")>-1){
						String priceName = ssStrings[i].split(":")[1].replaceAll("\"","");
						rb.setPriceName(priceName);
					}
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getCanonicalName(),e);
		}
		return map;
	}
	/**
	 * @Description  获得用户在该场所下的已失效订单
	 * @date 2017年1月11日上午10:12:41
	 * @author guoyingjie
	 * @param userId----->用户id
	 * @param siteId------->场所id
	 * @param currentPag---->当前页
	 * @param pageSize------>每页的条数
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> disabledOrder(int userId, int siteId,
			int currentPage, int pageSize,String siteName) {
		Map<String, Object> map=null;
		List<RecordOrderBean> list = null;
		try {
			//list = recordDao.disabledOrder(userId, siteId, currentPage, pageSize,siteName);
			map = recordDao.disabledOrder(userId, siteId, currentPage, pageSize,siteName);
			list=(List<RecordOrderBean>)map.get("list");
			for (int j = 0; j < list.size(); j++) {
				RecordOrderBean rb = list.get(j);
				rb.setState("已失效");
				String[] ssStrings = rb.getParamString().split(",");
				for (int i = 0; i < ssStrings.length; i++) {
					if(ssStrings[i].indexOf("amount")>-1){
						float amount = Float.valueOf(ssStrings[i].split(":")[1].replaceAll("\"",""));
						rb.setAllPirce(amount);
					}
					if(ssStrings[i].indexOf("buyNum")>-1){
						int buynum = Integer.valueOf(ssStrings[i].split(":")[1].replaceAll("\"",""));
						rb.setBuyNum(buynum);
					}
					if(ssStrings[i].indexOf("priceName")>-1){
						String priceName = ssStrings[i].split(":")[1].replaceAll("\"","");
						rb.setPriceName(priceName);
					}
					if(ssStrings[i].indexOf("payName")>-1){
						String priceName = ssStrings[i].split(":")[1].replaceAll("\"","");
						rb.setPriceName(priceName);
					}
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getCanonicalName(),e);
		}
		return map;
	}
}
