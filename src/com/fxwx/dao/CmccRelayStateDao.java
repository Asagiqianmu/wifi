package com.fxwx.dao;

import com.fxwx.entiy.CmccRelayState;

/**
 * 
 * @author dengfei E-mail:dengfei200857@163.com
 *
 */
public interface  CmccRelayStateDao {
	
	CmccRelayState getCmccRelayState(String telphone);
	
	int saveCmccRelayState(String telphone,String relayState);
}
