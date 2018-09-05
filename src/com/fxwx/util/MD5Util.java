package com.fxwx.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MD5Util {
	
	private static Log log = LogFactory.getLog(MD5Util.class);
	
	 /** 
     * 将源字符串使用MD5加密为字节数组 
     * @param source 
     * @return 
     */  
    public static byte[] encode2bytes(String source) {  
        byte[] result = null;  
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            md.reset();  
            md.update(source.getBytes("UTF-8"));  
            result = md.digest();  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
          
        return result;  
    }  
      
    /** 
     * 将源字符串使用MD5加密为32位16进制数 
     * @param source 
     * @return 
     */  
    public static String encode2hex(String source) {  
        byte[] data = encode2bytes(source);  
  
        StringBuffer hexString = new StringBuffer();  
        for (int i = 0; i < data.length; i++) {  
            String hex = Integer.toHexString(0xff & data[i]);  
              
            if (hex.length() == 1) {  
                hexString.append('0');  
            }  
              
            hexString.append(hex);  
        }  
          
        return hexString.toString();  
    }  
      
    /** 
     * 验证字符串是否匹配 
     * @param unknown 待验证的字符串 
     * @param okHex 使用MD5加密过的16进制字符串 
     * @return  匹配返回true，不匹配返回false 
     */  
    public static boolean validate(String unknown , String okHex) {  
        return okHex.equals(encode2hex(unknown));  
    }     
    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
       }
	}
	 private static String byteArrayToHexString(byte b[]) {  
	        StringBuffer resultSb = new StringBuffer();  
	        for (int i = 0; i < b.length; i++)  
	            resultSb.append(byteToHexString(b[i]));  
	  
	        return resultSb.toString();  
	    }  
	  
	    private static String byteToHexString(byte b) {  
	        int n = b;  
	        if (n < 0)  
	            n += 256;  
	        int d1 = n / 16;  
	        int d2 = n % 16;  
	        return hexDigits[d1] + hexDigits[d2];  
	    }  
	  
	    public static String MD5Encode(String origin, String charsetname) {  
	        String resultString = null;  
	        try {  
	            resultString = new String(origin);  
	            MessageDigest md = MessageDigest.getInstance("MD5");  
	            if (charsetname == null || "".equals(charsetname))  
	                resultString = byteArrayToHexString(md.digest(resultString  
	                        .getBytes()));  
	            else  
	                resultString = byteArrayToHexString(md.digest(resultString  
	                        .getBytes(charsetname)));  
	        } catch (Exception exception) {  
	        }  
	        return resultString;  
	    }  
	  
	    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",  
	            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };  
	    
	    
	    
	    /** 
	     * 对字符串md5加密(小写+字母) 
	     * 
	     * @param str 传入要加密的字符串 
	     * @return  MD5加密后的字符串 
	     */  
	    public static String getMD5(String str) {  
	        try {  
	            // 生成一个MD5加密计算摘要  
	            MessageDigest md = MessageDigest.getInstance("MD5");  
	            // 计算md5函数  
	            md.update(str.getBytes());  
	            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符  
	            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值  
	            return new BigInteger(1, md.digest()).toString(16);  
	        } catch (Exception e) {  
	           e.printStackTrace();  
	           return null;  
	        }  
	    }
    public static void main(String[] args) {
//    	log.error(validate("123456", "e10adc3949ba59abbe56e057f20f883e"));
    	System.out.println(getMD5("6C-71-D9-1B-7C-3F"));
    	System.out.println(getMD5("6C-71-D9-1B-7C-3F"));
    	System.out.println(getMD5("6C-71-D9-1B-7C-3F"));
    	System.out.println(getMD5("ooWM_wfRM5i_YPW-kCWd-FdzDuP4"));
    	System.out.println(getMD5("ooWM_wfRM5i_YPW-kCWd-FdzDuP4"));
	}
}
