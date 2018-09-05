package com.fxwx.dao;

import java.util.List;
import java.util.Map;

import org.nutz.dao.QueryResult;

import com.fxwx.bean.RecordOrderBean;

/**
 * 此类描述了用户的支付记录的查询操作
 * Copyright (c) All Rights Reserved, 2017.
 * 版权所有                   dfgs Information Technology Co .,Ltd
 * @Project		UnitePortal
 * @File		UserPayRecordDao.java
 * @Date		2017年1月11日 上午10:10:32
 * @Author		gyj
 */
public interface RecordDao {
	
	
	/**
	 * @Description  获得用户在该场所下的全部订单的记录
	 * @date 2017年1月11日上午10:12:41
	 * @author guoyingjie
	 * @param userId----->用户id
	 * @param siteId------->场所id
	 * @param currentPag---->当前页
	 * @param pageSize------>每页的条数
	 */
	public  Map<String, Object> allOrderRecards(int userId,int siteId,int currentPage,int pageSize,String siteName);
	/**
	 * @Description  获得用户在该场所下的待支付订单记录
	 * @date 2017年1月11日上午10:12:41
	 * @author guoyingjie
	 * @param userId----->用户id
	 * @param siteId------->场所id
	 * @param currentPag---->当前页
	 * @param pageSize------>每页的条数
	 */
	public Map<String, Object> noPaymentState(int userId,int siteId,int currentPage,int pageSize,String siteName);
	/**
	 * @Description  获得用户在该场所下的已完成订单
	 * @date 2017年1月11日上午10:12:41
	 * @author guoyingjie
	 * @param userId----->用户id
	 * @param siteId------->场所id
	 * @param currentPag---->当前页
	 * @param pageSize------>每页的条数
	 */
	public Map<String, Object> finishOrder(int userId,int siteId,int currentPage,int pageSize,String siteName);
	/**
	 * @Description  获得用户在该场所下的已失效订单
	 * @date 2017年1月11日上午10:12:41
	 * @author guoyingjie
	 * @param userId----->用户id
	 * @param siteId------->场所id
	 * @param currentPag---->当前页
	 * @param pageSize------>每页的条数
	 */
	public Map<String, Object> disabledOrder(int userId,int siteId,int currentPage,int pageSize,String siteName);
	
}
