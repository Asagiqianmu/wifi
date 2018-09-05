package com.fxwx.dao.impl;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import com.fxwx.dao.OrderDao;
import com.fxwx.entiy.Order;

@IocBean(name="orderDaoImpl")
public class OrderDaoImpl implements OrderDao{
//	private static final Log log = Logs.getLog(OrderDaoImpl.class);

	@Inject
	private Dao dao;

	@Inject
	private Dao readDao;
	
	@Override
	public boolean insertOrderInfo(Order order) {
		 return this.dao.insert(order).getId()>0?true:false;
	}
	
	@Override
	public Order selectOrderInfoByOut_trade_no(String out_trade_no) {
		return dao.fetch(Order.class,Cnd.where("out_trade_no", "=",out_trade_no));
	}

	@Override
	public boolean updateOrderInfo(Order order) {
		return dao.update(order)>0?true:false;
	}

	@Override
	public boolean updateOrderOpenid(String openid,String userMac) {
		return dao.update(Order.class, Chain.make("openid",openid), Cnd.where("userMac","=",userMac))>0?true:false;
	}
}
