package com.fxwx.util;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.log4j.Logger; 

public class Sha1Util {
	private static Logger logger = Logger.getLogger(Sha1Util.class);
	/**
	 * 将需要签名的字段作为
	 * 
	 * @param datas 参数字段
	 * @param sha1Key sha1Key
	 * @return
	 */
	public static String getSignature(List<String> datas, String sha1Key) {
		logger.info("checkSignature() datas:" + datas);

		if (null == datas || datas.isEmpty() || StringUtil.isNull(sha1Key)) {
			logger.warn( "checkSignature() datas:" + datas
					+ ",datas is null Or signature is null.");
			return "";
		}
		datas.add(sha1Key);
		Collections.sort(datas);
		StringBuffer paicString = new StringBuffer(datas.size());
		for (int i = 0; i < datas.size(); i++) {
			paicString = paicString.append(datas.get(i));
		}
		String param = paicString.toString();
		// 六个参数组成的字符串
		logger.info("Sha1Util.checkSignature() param:" + param);
		MessageDigest md = null;
		String signatureNew = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将六个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(param.getBytes());
			// 将sha1加密后的字符串可与signature对比
			signatureNew = bytesToHexString(digest);
			// paServers算出的签名
			logger.info("checkSignature() signatureNew:"
					+ signatureNew);
			return signatureNew;
		} catch (Exception e) {
			logger.error( "checkSignature() is faild. e:"
					+ e.toString());
		}
		return "";
	}

	/*
     * Convert byte[] to hex
     * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * 
     * @param src byte[] data
     * 
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
	 
}
