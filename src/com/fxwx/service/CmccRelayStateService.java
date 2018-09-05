package com.fxwx.service;

import com.fxwx.entiy.CmccRelayState;

/**
 * 
 * @author dengfei E-mail:dengfei200857@163.com
 *
 */
public interface  CmccRelayStateService {
	
	CmccRelayState getCmccRelayState(String telphone);
	
	int saveCmccRelayState(String telphone,String relayState);
}
