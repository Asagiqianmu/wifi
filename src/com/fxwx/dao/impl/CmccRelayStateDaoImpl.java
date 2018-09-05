package com.fxwx.dao.impl;


import java.util.Date;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.fxwx.dao.CmccRelayStateDao;
import com.fxwx.entiy.CmccRelayState;

/**
 * 
 * @author dengfei E-mail:dengfei200857@163.com
 *
 */

@IocBean
public class  CmccRelayStateDaoImpl implements CmccRelayStateDao {
	
	@Inject
	private Dao dao;
	
	@Override
	public CmccRelayState getCmccRelayState(String telphone) {
		Sql sql=Sqls.create("SELECT * FROM t9_cmcc_relayState $condition ORDER BY createtime DESC LIMIT 0,1");
		sql.setCondition(Cnd.where("telphone","=",telphone));
		sql.setCallback(Sqls.callback.entity());
		Entity<CmccRelayState> entity =dao.getEntity(CmccRelayState.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getObject(CmccRelayState.class);
	}
	
	@Override
	public int saveCmccRelayState(String telphone,String relayState) {
		CmccRelayState state=new CmccRelayState();
		state.setTelphone(telphone);
		state.setRelayState(relayState);
		state.setCreateTime(new Date());
		return dao.insert(state).getId()>0?1:0;
	}
}
