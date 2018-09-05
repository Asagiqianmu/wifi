package com.fxwx.util;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.alibaba.fastjson.JSONObject;

public class ChuangRuiApi {
	
	private String accesskey = "3bwMFLhboijhR7te"; //用户开发key
	private String accessSecret = "uEQJzValdCeq8aVHWB42SwzjFrEAamVd"; //用户开发秘钥
	/**
	 * 发送普通短信
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年10月27日 下午2:14:57
	 * @param telephones 多个号码用,号隔开
	 * @throws IOException 
	 * @throws HttpException 
	 * @throws Exception
	 */
    private void sendSMS(String telephones,String code) throws HttpException, IOException {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod("http://api.1cloudsp.com/intl/api/v2/send");
        postMethod.getParams().setContentCharset("UTF-8");
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());


        NameValuePair[] data = {
                new NameValuePair("accesskey", accesskey),
                new NameValuePair("secret", accessSecret),
                new NameValuePair("sign", "376"),//签名ID
                new NameValuePair("templateId", "372"),//模版ID
                new NameValuePair("mobile", telephones),
                new NameValuePair("content", URLEncoder.encode(code, "utf-8"))//（示例模板：{1}您好，您的订单于{2}已通过{3}发货，运单号{4}）
        };
        for (int i = 0; i < data.length; i++) {
        	 System.out.println(data[i].getName()+"--->"+data[i].getValue());
		}
       
        postMethod.setRequestBody(data);

        int statusCode = httpClient.executeMethod(postMethod);
        System.out.println("statusCode: " + statusCode + ", body: "
                    + postMethod.getResponseBodyAsString());
    }

    //个性短信
    private void sendsms2() throws Exception {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod("http://api.1cloudsp.com/api/v2/send");
        postMethod.getParams().setContentCharset("UTF-8");
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());

        //组装个性短信内容
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("8613700000000","先生##9:40##快递公司##1234567");
        jsonObj.put("8613700000001","女士##10:10##物流公司##000000");//（示例模板：{1}您好，您的订单于{2}已通过{3}发货，运单号{4}）

        NameValuePair[] data = {
                new NameValuePair("accesskey", accesskey),
                new NameValuePair("secret", accessSecret),
                new NameValuePair("sign", "123"),
                new NameValuePair("templateId", "100"),
                new NameValuePair("data", URLEncoder.encode(jsonObj.toString(), "utf-8"))
        };
        postMethod.setRequestBody(data);

        int statusCode = httpClient.executeMethod(postMethod);
        System.out.println("statusCode: " + statusCode + ", body: "
                    + postMethod.getResponseBodyAsString());
    }

    public static void main(String[] args) throws Exception {
        ChuangRuiApi t = new ChuangRuiApi();
//        //普通短信
        try {
        	t.sendSMS("886963352906","1234");
		} catch (Exception e) {
			// TODO: handle exception
		}
        //个性短信
//        t.sendsms2();
    }
}

