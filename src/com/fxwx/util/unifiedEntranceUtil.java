package com.fxwx.util;

public class unifiedEntranceUtil {

	/**
	 * 验证用户跳转 PC登录还是手机登录 1=PC 2=手机
	 * @param terminalDevice
	 * @return
	 */
	public static String getPcOrMobile(String terminalDevice) {
		if (terminalDevice != null && !"".equals(terminalDevice)) {
			if (terminalDevice.indexOf("Macintosh") != -1
					|| terminalDevice.indexOf("iPad") != -1
					|| terminalDevice.indexOf("Windows NT") != -1) {// PC
				return "1";
			} else {// mobile
				return "2";
			}
		} 
			return "1";
	}
	/**
	 * 简化判断设备是ios还是安卓
	 * @param terminalDevice
	 * @return
	 */
	public static int getAndroidOrIos(String terminalDevice){
		if (terminalDevice != null && !"".equals(terminalDevice)) {
			
			if(terminalDevice.contains("android")||terminalDevice.contains("Android")){
				return 1;
			}else{
				return 2;
			}
		}
		return -1;
	}
}
