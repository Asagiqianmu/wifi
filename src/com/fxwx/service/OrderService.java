package com.fxwx.service;

import com.fxwx.entiy.Order;

public interface OrderService {
	
	/**
	 * 生成订单信息
	 * 
	 * @param order
	 * @return
	 */
	boolean insertOrderInfo(Order order);

	/**
	 * 查询订单号
	 * 
	 * @param out_trade_no
	 * @return
	 */
	Order selectOrderInfoByOut_trade_no(String out_trade_no);
	
	/**
	 * 修改订单信息
	 * @param orderNum
	 * @return
	 */
	boolean updateOrderInfo(Order order);
	
	boolean updateOrderOpenid(String openid,String userMac);
}
