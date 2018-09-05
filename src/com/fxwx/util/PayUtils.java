package com.fxwx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class PayUtils {
	//打印日志用到的日期
	public static String getDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(new Date());
		return date;
	}
	
	//字符串的金额转为int
	public static int toInt(String fee){
		int result = 0;
		try{
			result = Integer.parseInt(fee);
		}catch(Exception e){
			result = 0;
		}
		return result;
	}
	
	//支付完成时间	time_end 例如：20141030133525
	public static Date toDate(String strDate){
		Date date = null;
		try{
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			date = df.parse(strDate);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	//签名串的组装.  支付接口中参数: sign
	public static String getSign(Map<String, Object> sortedMap,String weixinKey) {
		
		StringBuilder bu = new StringBuilder();
		Set<Entry<String, Object>> entrySet = sortedMap.entrySet();
		for(Entry<String, Object> entry:entrySet){
			if(entry.getValue()==null || entry.getValue().equals("") || "sign".equals(entry.getKey())){
				// 空值不参加 , sign参数也不参加 
			}else{
				bu.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		
		bu.append("key=").append(weixinKey);
		System.out.println(bu);
		//需要转为大写
		return MD5.MD5Encode(bu.toString()).toUpperCase();
	}
	
	// 支付接口中参数:nonce_str
	public static String getNonce(final int charCount) {
		return RandomStringUtils.randomAlphanumeric(charCount);
	}
	
	//生成N位随机数
	public static String randomNumber(int length) {
		
		//随机数
		int num = 0;
		String numbers="0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i< length;i++){
			num = random.nextInt(numbers.length());
			sb.append(num);
			
		}
		return sb.toString();
		
	}
	
	//时间戳
	public static String getDateTimeString(Date srcDate){
		String res = "";
		if( srcDate != null ) {
			try{
				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				res = df.format(srcDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return res;
	}
	
	//交易时间
	/**
	 * 交易时间
	 * @param int 交易间隔时间. 单位分钟
	 * @return 开始时间, 结束时间
	 * 最短失效时间间隔必须大于5分钟
	 */
	public static String[]  getTradeTime(int interval) {
		Date currDate = new Date();
		String startTime = getDateTimeString(currDate);
		
		//增加间隔时间
		if( interval < 6 || interval > 30) {
			interval = 30;
		}
		Calendar calc = Calendar.getInstance();
		calc.add(Calendar.MINUTE, interval);
		String expireTime = getDateTimeString(calc.getTime());
		return  new String[]{startTime,expireTime };
	}
	
	//订单金额, 分为单位
	public static int getTotalMoney(String money) {
		BigDecimal bg = new BigDecimal(money);
		return bg.multiply(new BigDecimal("100")).intValue();
	}
	//商户自己的订单号, 不重复
	public static String getOut_Trade_No(int length) {
		String trade_no =  getDateTimeString(new java.util.Date()) + randomNumber(length);
		return trade_no;
	}
	
	//使用HttpURLConnection发送请求
	public static String sendPost(String request_url, Object postData) throws IOException{
		//URL
		URL url = new URL(request_url);
		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
		
		urlConnection.setConnectTimeout(20000); //设置连接主机超时（单位：毫秒） 
		urlConnection.setReadTimeout(60000);    //设置从主机读取数据超时（单位：毫秒） 
		
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		//不使用缓存
		urlConnection.setUseCaches(false);
		urlConnection.setRequestMethod("POST");

		urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
		urlConnection.setRequestProperty("Content-Type", "text/xml");
		urlConnection.connect();
		
		OutputStream outputStream = urlConnection.getOutputStream();
		PrintWriter printWriter = new PrintWriter(outputStream);
		String  xmlData = XmlParser.objectToXml(postData);
		printWriter.write(xmlData);
		printWriter.flush();
		printWriter.close();
		
		//发送缓存的请求
		InputStream inputStream = urlConnection.getInputStream();
		BufferedReader breader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
		//读取数据
		String line = null;
		StringBuilder bu = new StringBuilder();
		while(  (line = breader.readLine()) !=null ) {
			bu.append(line);
		}
		return bu.toString();
	}
	
   //=================使用httpclient的请求方式========================
	//连接超时时间，默认10秒
    private static int socketTimeout = 10000;
    //传输超时时间，默认30秒
    private static int connectTimeout = 30000;
	 //请求器的配置
    private static RequestConfig requestConfig;
    //HTTP请求器
    private static CloseableHttpClient httpClient;
    
	public static String sendPostHttpClient(String url, Object xmlObj) throws Exception {
		
		httpClient = HttpClients.createDefault();
        //根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        //解决XStream对出现双下划线的bug
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xStreamForRequestPostData.alias("xml", xmlObj.getClass());
        //将要提交给API的数据对象转换成XML格式数据Post给API
      
        String postDataXML = xStreamForRequestPostData.toXML(xmlObj);
        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        //设置请求器的配置
        httpPost.setConfig(requestConfig);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");

        } catch (ConnectionPoolTimeoutException e) {
        	e.printStackTrace();
        } catch (ConnectTimeoutException e) {
        	e.printStackTrace();
        } catch (SocketTimeoutException e) {
        	e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            httpPost.abort();
        }
        return result;
    }
	
	//
	//使用HttpURLConnection发送请求
	public static String sendNotifyXml(String request_url, String strXml) throws IOException {
		// URL
		URL url = new URL(request_url);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

		urlConnection.setConnectTimeout(20000); // 设置连接主机超时（单位：毫秒）
		urlConnection.setReadTimeout(60000); // 设置从主机读取数据超时（单位：毫秒）

		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		// 不使用缓存
		urlConnection.setUseCaches(false);
		urlConnection.setRequestMethod("POST");

		urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
		urlConnection.setRequestProperty("Content-Type", "text/xml");
		urlConnection.connect();

		OutputStream outputStream = urlConnection.getOutputStream();
		PrintWriter printWriter = new PrintWriter(outputStream);
		
		printWriter.write(strXml);
		printWriter.flush();
		printWriter.close();

		// 发送缓存的请求
		InputStream inputStream = urlConnection.getInputStream();
		BufferedReader breader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		// 读取数据
		String line = null;
		StringBuilder bu = new StringBuilder();
		while ((line = breader.readLine()) != null) {
			bu.append(line);
		}
		return bu.toString();
	}

}
