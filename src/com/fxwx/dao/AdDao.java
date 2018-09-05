package com.fxwx.dao;

import java.util.List;

import com.fxwx.entiy.AdAccessRecord;
import com.fxwx.entiy.AdEffect;
import com.fxwx.entiy.AdInfo;

/**
 * 广告Dao
 * 
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2018年4月10日 下午5:12:25
 */
public interface AdDao {

	/**
	 * 获取上架的广告列表
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年4月10日 下午5:21:42
	 * @return
	 */
	List<AdInfo> getAdInfo();

	/**
	 * 根据广告ID查找当天的广告效果记录
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年4月10日 下午5:34:06
	 * @param adId
	 * @return
	 */
	AdEffect getTodayAdEffect(int adId);

	/**
	 * 更新广告推广效果（包含新增） 返回 1插入，2更新，0未处理
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年4月10日 下午5:23:19
	 * @param adEffect
	 * @return
	 */
	int updateAdEffect(AdEffect adEffect);

	/**
	 * 根据广告ID和用户mac查找当天点击访问记录
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年4月10日 下午5:36:27
	 * @param adId
	 * @param userMac
	 * @return
	 */
	AdAccessRecord getTodayAdAccessRecord(int adId, String userMac);

	/**
	 * 更新广告访问记录（包含新增） 返回 1插入，2更新，0未处理
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2018年4月10日 下午5:25:24
	 * @param accessRecord
	 * @return
	 */
	int updateAdAccessRecord(AdAccessRecord accessRecord);
}
