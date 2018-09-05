package com.fxwx.dao.impl;

import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.fxwx.dao.AdDao;
import com.fxwx.entiy.AdAccessRecord;
import com.fxwx.entiy.AdEffect;
import com.fxwx.entiy.AdInfo;

/**
 * 
 * @author dengfei E-mail:dengfei200857@163.com
 *
 */

@IocBean
public class AdDaoImpl implements AdDao {

	@Inject
	private Dao dao;

	@Override
	public List<AdInfo> getAdInfo() {
		// TODO Auto-generated method stub
		Sql sql = Sqls.create("SELECT * FROM t10_ad_info WHERE ad_status = 1 ORDER BY createtime DESC");
		sql.setCallback(Sqls.callback.entities());
		Entity<AdInfo> entity = dao.getEntity(AdInfo.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getList(AdInfo.class);
	}

	@Override
	public int updateAdEffect(AdEffect adEffect) {
		// TODO Auto-generated method stub
		if (adEffect != null) {
			AdEffect effect = getTodayAdEffect(adEffect.getAdId());
			if (effect == null) {
				return dao.insert(adEffect).getId() > 0 ? 1 : 0;
			} else {
				String sqlStr = "UPDATE t10_ad_effect SET ";
				if (adEffect.getShowNum() == 1) {
					sqlStr += "show_num=show_num+1, ";
				}
				if (adEffect.getPv() == 1) {
					sqlStr += "pv=pv+1, ";
				}
				if (adEffect.getUv() == 1) {
					sqlStr += "uv=uv+1, ";
				}
				sqlStr += "updatetime='" + adEffect.getUpdateTime() + "' ";
				sqlStr += "WHERE id=" + effect.getId();
				Sql sql = Sqls.create(sqlStr);
				dao.execute(sql);
				return 2;
			}
		}
		return 0;
	}

	@Override
	public int updateAdAccessRecord(AdAccessRecord accessRecord) {
		// TODO Auto-generated method stub
		if (accessRecord != null) {
			AdAccessRecord record = getTodayAdAccessRecord(accessRecord.getAdId(), accessRecord.getUserMac());
			if (record == null) {
				return dao.insert(accessRecord).getId() > 0 ? 1 : 0;
			} else {
				String sqlStr = "UPDATE t10_ad_access_record SET ";
				if (accessRecord.getBrowse() == 1) {
					sqlStr += "browse=browse+1, ";
				}
				if (accessRecord.getClick() == 1) {
					sqlStr += "click=click+1, ";
				}
				sqlStr += "lasttime='" + accessRecord.getLastTime() + "' ";
				sqlStr += "WHERE id=" + record.getId();
				Sql sql = Sqls.create(sqlStr);
				dao.execute(sql);
				if(record.getClick() == 0){
					return 1;
				}
				return 2;
			}
		}
		return 0;
	}

	@Override
	public AdEffect getTodayAdEffect(int adId) {
		// TODO Auto-generated method stub
		Sql sql = Sqls.create("SELECT * FROM t10_ad_effect  WHERE ad_id=" + adId + " AND TO_DAYS(createtime)=TO_DAYS(NOW())");
		sql.setCallback(Sqls.callback.entity());
		Entity<AdEffect> entity = dao.getEntity(AdEffect.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getObject(AdEffect.class);
	}

	@Override
	public AdAccessRecord getTodayAdAccessRecord(int adId, String userMac) {
		Sql sql = Sqls.create("SELECT * FROM t10_ad_access_record  WHERE ad_id=" + adId + " AND usermac='" + userMac + "' AND TO_DAYS(createtime)=TO_DAYS(NOW())");
		sql.setCallback(Sqls.callback.entity());
		Entity<AdAccessRecord> entity = dao.getEntity(AdAccessRecord.class);
		sql.setEntity(entity);
		dao.execute(sql);
		return sql.getObject(AdAccessRecord.class);
	}
}
