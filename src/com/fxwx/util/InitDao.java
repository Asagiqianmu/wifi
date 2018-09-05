package com.fxwx.util;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

public class InitDao {

	private static Ioc ioc = null;
	/**
	 * @Description  不同数据源有不同的命名
	 * @date 2016年7月27日下午1:20:41
	 * @author guoyingjie
	 * @param daoname
	 * @return
	 */
	public static Dao init(String daoname) {
		if(ioc==null){
			ioc = new NutIoc(new JsonLoader("ioc/db_config.js"));
		}
		NutDao dao = ioc.get(NutDao.class, daoname);
	 
		return dao;
	}
}
