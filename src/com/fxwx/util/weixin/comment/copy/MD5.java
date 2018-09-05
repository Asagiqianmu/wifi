package com.fxwx.util.weixin.comment.copy;

import java.security.MessageDigest;
import java.util.Date;

/**
 * User: rizenguo
 * Date: 2014/10/23
 * Time: 15:43
 */
public class MD5 {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 转换字节数组为16进制字串
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 转换byte到16进制
     * @param b 要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5编码
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
           // md.update(resultString.getBytes("UTF-8"));
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("utf-8")));
            //resultString = byteArrayToHexString(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
    public static void main(String[] args) {
    	String toSign = "wxc5fb6a6dabc34dfb" + "demo" + new Date().getTime()  + "3474729" + "http://wifi.weixin.qq.com/assistant/wifigw/auth.xhtml?httpCode=200" + "BC-3A-EA-C7-27-F9" + "Unite_WIFI"+"fc1af456d5962b006989ceefe4d1f537";

    	System.out.println(MD5.MD5Encode(toSign));
	}
}