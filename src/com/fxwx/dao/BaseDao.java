package com.fxwx.dao;


public interface BaseDao {
	/**
	 * 插入失败缓存记录
	 * @param key
	 * @param reason
	 */
	 
	void insertFailCount(String key,String reason);
	
}
