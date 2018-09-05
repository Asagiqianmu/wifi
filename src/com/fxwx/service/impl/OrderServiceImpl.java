package com.fxwx.service.impl;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.fxwx.dao.OrderDao;
import com.fxwx.entiy.Order;
import com.fxwx.service.OrderService;

@IocBean(name="orderServiceImpl")
public class OrderServiceImpl implements OrderService{
	@Inject(value="orderDaoImpl")
	private OrderDao orderDaoImpl;
	
	@Override
	public boolean insertOrderInfo(Order order) {
		return orderDaoImpl.insertOrderInfo(order);
	}

	@Override
	public Order selectOrderInfoByOut_trade_no(String out_trade_no) {
		return orderDaoImpl.selectOrderInfoByOut_trade_no(out_trade_no);
	}

	@Override
	public boolean updateOrderInfo(Order order) {
		return orderDaoImpl.updateOrderInfo(order);
	}

	@Override
	public boolean updateOrderOpenid(String openid, String userMac) {
		return orderDaoImpl.updateOrderOpenid(openid,userMac);
	}
}
