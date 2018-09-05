package com.fxwx.dao;

import com.fxwx.entiy.Order;

public interface OrderDao {
	boolean insertOrderInfo(Order order);
	
	Order selectOrderInfoByOut_trade_no(String out_trade_no);
	
	boolean updateOrderInfo(Order order);
	
	boolean updateOrderOpenid(String openid,String userMac);
}
