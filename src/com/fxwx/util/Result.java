package com.fxwx.util;

public class Result {
	
	//微信返回的通讯标识
	public static String WX_RETURN_CODE_SUCCESS="SUCCESS";
	//微信返回的交易结果标识
	public static String WX_RESULT_CODE_SUCCESS="SUCCESS";
	
	//0:下单请求且微信应答成功
	public static int REQUEST_PAY_OK = 0 ;
	
	//1: 微信通知商户返回支付返回成功
	public static int WEIXIN_NOTICE_OK = 1;
	
	//2:下单请求且返回结果是失败
	public static int REQUEST_PAY_FAILURE = 2;
	
	//3:微信通知商户返回支付返回失败
	public static int WEIXIN_NOTICE_FAILURE = 3;
	
	
	//验证签名正确
	public static int CHECK_SIGN_OK = 10;
	//验证支付金额正确
	public static int CHECK_FEE_OK = 11;
	
	//没有错误  0 
	public static int ERR_NONE = 0; 
	//验证签名错误  -1
	public static int ERR_CODE_SING =  -1;
	//签名字符串为 null 或  ""
	public static int ERR_CODE_SING_EMPTY =  -2;
	//没有应答结果
	public static int ERR_RESPONSE_EMPTY =  -3;
	//验证支付金额错误
	public static int CHECK_FEE_FAILURE = -4;
	//无商户订单
	public static int ERR_ORDER_EXISTS = -5;
	//下单请求失败
	public static int ERR_PAY_FAILURE = -6;
	
	//异常错误 -1000
	public static int ERR_EXCEPTION = -1000;
}
