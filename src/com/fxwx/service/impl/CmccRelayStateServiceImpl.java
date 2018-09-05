package com.fxwx.service.impl;


import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.fxwx.dao.CmccRelayStateDao;
import com.fxwx.entiy.CmccRelayState;
import com.fxwx.service.CmccRelayStateService;

/**
 * 
 * @author dengfei E-mail:dengfei200857@163.com
 *
 */

@IocBean(name ="cmccRelayStateServiceImpl")
public class CmccRelayStateServiceImpl implements CmccRelayStateService {

	@Inject
	private CmccRelayStateDao relayStateDao;
	
	@Override
	public CmccRelayState getCmccRelayState(String telphone) {
		return relayStateDao.getCmccRelayState(telphone);
	}
	
	@Override
	public int saveCmccRelayState(String telphone,String relayState) {
		return relayStateDao.saveCmccRelayState(telphone, relayState);
	}
}
