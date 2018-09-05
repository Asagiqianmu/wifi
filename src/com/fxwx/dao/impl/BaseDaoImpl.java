package com.fxwx.dao.impl;

import java.util.Date;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.fxwx.dao.BaseDao;
import com.fxwx.entiy.CacheFailLog;
@IocBean(name="baseDaoImp")
public class BaseDaoImpl implements BaseDao {
	@Inject
	private Dao dao;
	@Override
	public void insertFailCount(String key, String reason) {

		CacheFailLog cfl=dao.fetch(CacheFailLog.class, Cnd.where("cache_key","=",key));
		if(cfl==null){
			cfl= new CacheFailLog();
			cfl.setCount(1);
			cfl.setKeyValue(key);
			cfl.setReason(reason);
			cfl.setCreateTime(new Date());
			dao.insert(cfl);
		}else{
			cfl.setCount(cfl.getCount()+1);
			dao.update(cfl);
		}
		
	}
public static void main(String[] args) {
	BaseDaoImpl s= new BaseDaoImpl();
	s.insertFailCount("chap-challenge&chap-id&ip&mac&nasid&ros&uamip&uamport&userurl&", "");
}
}
