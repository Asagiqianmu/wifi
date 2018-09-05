package com.fxwx.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Codes {

	private static Log log = LogFactory.getLog(Codes.class);
	
	public static String buildVerifyCode() {
		List<String> list = Arrays.asList(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9"});
		Collections.shuffle(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++)
			sb.append(list.get(i));
		return sb.substring(0, 4);
	}
	public  static void main(String [] args){
		log.error(buildVerifyCode());
	}
}
