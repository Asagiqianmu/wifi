package com.fxwx.dao.impl;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.w3c.dom.css.RGBColor;

import com.fxwx.bean.RecordOrderBean;
import com.fxwx.dao.RecordDao;
import com.fxwx.entiy.SitePaymentRecord;
import com.fxwx.util.InitDao;

/**
 * Copyright (c) All Rights Reserved, 2017.
 * 版权所有                   dfgs Information Technology Co .,Ltd
 * @Project		UnitePortal 订单查询的功能
 * @File		UserRecordDaoImpl.java
 * @Date		2017年1月11日 下午12:08:21
 * @Author		gyj
 */
@IocBean
public class RecordDaoImpl implements RecordDao {

	@Inject
	private Dao dao;
	/**
	 * @Description  获得用户在该场所下的全部订单的记录
	 * @date 2017年1月11日上午10:12:41
	 * @author guoyingjie
	 * @param userId----->用户id
	 * @param siteId------->场所id
	 * @param currentPag---->当前页
	 * @param pageSize------>每页的条数
	 */
	@Override
	public  Map<String, Object> allOrderRecards(int userId, int siteId,
			int currentPage, int pageSize,String siteName) {
		//currentPage = (currentPage - 1<=0?1:currentPage-1)*pageSize;
		Condition condition =Cnd.where("user_id","=",userId).and("site_id","=",siteId).desc("id");
		Pager pager = dao.createPager(currentPage, pageSize);
	    List<SitePaymentRecord> list = dao.query(SitePaymentRecord.class, condition, pager);
	    pager.setRecordCount(dao.count(SitePaymentRecord.class,condition));
	    QueryResult queryResult= new QueryResult(list, pager);
	    Map<String, Object> map = new HashMap<String, Object>();
	    RecordOrderBean rb=null;
	    List<RecordOrderBean> listBean = new ArrayList<RecordOrderBean>();
		for (int i = 0; i < queryResult.getList().size(); i++) {
			rb = new RecordOrderBean();
			 SitePaymentRecord spd= (SitePaymentRecord)(queryResult.getList().get(i));
			 rb.setSiteId(spd.getId());
			 rb.setUserId(spd.getUserId());
			 rb.setSiteName(siteName);
			 rb.setOrderNum(spd.getOrderNum());
			 rb.setParamString(spd.getParamJson());
			 rb.setPayType(spd.getPayType());
			 rb.setIsFinishi(spd.getIsFinish());
			 rb.setTimestamp(spd.getCreateTime()+"");
			 listBean.add(rb);
		}
		map.put("list",listBean);
		map.put("allPages", queryResult.getPager().getPageCount());
		return map;
	}
	/**
	 * @Description  获得用户在该场所下的缴费待支付记录
	 * @date 2017年1月11日上午10:12:41
	 * @author guoyingjie
	 * @param userId----->用户id
	 * @param siteId------->场所id
	 * @param currentPag---->当前页
	 * @param pageSize------>每页的条数
	 */
	@Override
	public Map<String, Object> noPaymentState(int userId, int siteId,
			int currentPage, int pageSize,String siteName) {
		
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(new Date().getTime()-20*60*1000));
		//currentPage = (currentPage - 1<=0?0:currentPage-1)*pageSize;
		Condition condition =Cnd.where("user_id","=",userId).and("site_id","=",siteId).and("is_finish","=",0).and("create_time",">",time).desc("id");
		Pager pager = dao.createPager(currentPage, pageSize);
	    List<SitePaymentRecord> list = dao.query(SitePaymentRecord.class, condition, pager);
	    pager.setRecordCount(dao.count(SitePaymentRecord.class,condition));
	    QueryResult queryResult= new QueryResult(list, pager);
	    Map<String, Object> map = new HashMap<String, Object>();
	    RecordOrderBean rb=null;
	    List<RecordOrderBean> listBean = new ArrayList<RecordOrderBean>();
	    for (int i = 0; i < queryResult.getList().size(); i++) {
			rb = new RecordOrderBean();
			 SitePaymentRecord spd= (SitePaymentRecord)(queryResult.getList().get(i));
			 rb.setSiteId(spd.getId());
			 rb.setUserId(spd.getUserId());
			 rb.setSiteName(siteName);
			 rb.setOrderNum(spd.getOrderNum());
			 rb.setParamString(spd.getParamJson());
			 rb.setPayType(spd.getPayType());
			 rb.setIsFinishi(spd.getIsFinish());
			 rb.setTimestamp(spd.getCreateTime()+"");
			 listBean.add(rb);
		}
		map.put("list",listBean);
		map.put("allPages", queryResult.getPager().getPageCount());
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
	@Override
	public Map<String, Object> finishOrder(int userId, int siteId,
			int currentPage, int pageSize,String siteName) {
		//currentPage = (currentPage - 1<=0?0:currentPage-1)*pageSize;
		Condition condition =Cnd.where("user_id","=",userId).and("site_id","=",siteId).and("is_finish","!=",0).desc("id");
		Pager pager = dao.createPager(currentPage, pageSize);
	    List<SitePaymentRecord> list = dao.query(SitePaymentRecord.class, condition, pager);
	    pager.setRecordCount(dao.count(SitePaymentRecord.class,condition));
	    QueryResult queryResult= new QueryResult(list, pager);
	    Map<String, Object> map = new HashMap<String, Object>();
	    RecordOrderBean rb=null;
	    List<RecordOrderBean> listBean = new ArrayList<RecordOrderBean>();
	    for (int i = 0; i < queryResult.getList().size(); i++) {
			 rb = new RecordOrderBean();
			 SitePaymentRecord spd= (SitePaymentRecord)(queryResult.getList().get(i));
			 rb.setSiteId(spd.getId());
			 rb.setUserId(spd.getUserId());
			 rb.setSiteName(siteName);
			 rb.setOrderNum(spd.getOrderNum());
			 rb.setParamString(spd.getParamJson());
			 rb.setPayType(spd.getPayType());
			 rb.setIsFinishi(spd.getIsFinish());
			 rb.setTimestamp(spd.getCreateTime()+"");
			 listBean.add(rb);
		}
		map.put("list",listBean);
		map.put("allPages", queryResult.getPager().getPageCount());
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
	@Override
	public Map<String, Object> disabledOrder(int userId, int siteId, int currentPage,
			int pageSize,String siteName) {
		
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(new Date().getTime()-20*60*1000));
		//currentPage = (currentPage - 1<=0?0:currentPage-1)*pageSize;
		Condition condition =Cnd.where("user_id","=",userId).and("site_id","=",siteId).and("is_finish","=",0).and("create_time","<",time).desc("id");
		Pager pager = dao.createPager(currentPage, pageSize);
	    List<SitePaymentRecord> list = dao.query(SitePaymentRecord.class, condition, pager);
	    pager.setRecordCount(dao.count(SitePaymentRecord.class,condition));
	    QueryResult queryResult= new QueryResult(list, pager);
	    Map<String, Object> map = new HashMap<String, Object>();
	    RecordOrderBean rb=null;
	    List<RecordOrderBean> listBean = new ArrayList<RecordOrderBean>();
	    for (int i = 0; i < queryResult.getList().size(); i++) {
			rb = new RecordOrderBean();
			 SitePaymentRecord spd= (SitePaymentRecord)(queryResult.getList().get(i));
			 rb.setSiteId(spd.getId());
			 rb.setUserId(spd.getUserId());
			 rb.setSiteName(siteName);
			 rb.setOrderNum(spd.getOrderNum());
			 rb.setParamString(spd.getParamJson());
			 rb.setPayType(spd.getPayType());
			 rb.setIsFinishi(spd.getIsFinish());
			 rb.setTimestamp(spd.getCreateTime()+"");
			 listBean.add(rb);
		}
		map.put("list",listBean);
		map.put("allPages", queryResult.getPager().getPageCount());
		return map;
	}
}
