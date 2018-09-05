package com.fxwx.service.impl;


import java.util.List;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.fxwx.dao.AdDao;
import com.fxwx.entiy.AdAccessRecord;
import com.fxwx.entiy.AdEffect;
import com.fxwx.entiy.AdInfo;
import com.fxwx.service.AdService;

/**
 * 
 * @author dengfei E-mail:dengfei200857@163.com
 *
 */

@IocBean(name ="adServiceImpl")
public class AdServiceImpl implements AdService {

	@Inject
	private AdDao adDao;

	@Override
	public List<AdInfo> getAdInfo() {
		// TODO Auto-generated method stub
		return adDao.getAdInfo();
	}

	@Override
	public AdEffect getTodayAdEffect(int adId) {
		// TODO Auto-generated method stub
		return adDao.getTodayAdEffect(adId);
	}

	@Override
	public int updateAdEffect(AdEffect adEffect) {
		// TODO Auto-generated method stub
		return adDao.updateAdEffect(adEffect);
	}

	@Override
	public AdAccessRecord getTodayAdAccessRecord(int adId, String userMac) {
		// TODO Auto-generated method stub
		return adDao.getTodayAdAccessRecord(adId, userMac);
	}

	@Override
	public int updateAdAccessRecord(AdAccessRecord accessRecord) {
		// TODO Auto-generated method stub
		return adDao.updateAdAccessRecord(accessRecord);
	}
	
	
}
