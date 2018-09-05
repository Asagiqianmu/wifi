<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,height=device-height,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<meta name="msapplication-tap-highlight" content="no">
<!-- 避免IE使用兼容模式 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- QQ应用模式 -->
<meta name="x5-page-mode" content="app">
<!-- windows phone 点击无高光 -->
<meta name="msapplication-tap-highlight" content="no">
<!-- 针对手持设备优化，主要是针对一些老的不识别viewport的浏览器，比如黑莓 -->
<meta name="HandheldFriendly" content="true">
<!-- 微软的老式浏览器 -->
<meta name="MobileOptimized" content="320">
<title>订单处理中</title>
<link href="${ctx}/img/img/favicon.ico" type="image/x-icon"
	rel="shortcut icon">
<link rel="stylesheet" type="text/css"
	href="${ctx}/partol/css/zhifu.css">
<script type="text/javascript"
	src="http://oss.kdfwifi.net/js/jquery-1.9.1.min.js"></script>
</head>
<body>
	<div style="display: none; position: absolute;">
		<div>
			<!--扫描代码-->
			<input type="hidden" name="out_trade_no" value="${out_trade_no}" />
			<div>
				<div class="m26">
					<h1>订单提交成功，请您尽快付款！</h1>
					<div class="num">
						<span class="color1 ml16" style="font-size: 28px;">订单号：<label
							id="out_trade_no" class="orange">${out_trade_no}</label></span>
					</div>
				</div>
				<div class="title">
					<span class="color1 ml16" style="font-size: 28px;">商品名称：<label
						class="orange">${body}</label></span></br> <span class="color1 ml16"
						style="font-size: 28px;">订单金额：<label class="orange">${total_fee/100}</label>元
					</span> </br> <span class="color1 ml16" style="font-size: 28px;">支付方式：微信支付</span>
				</div>
				<a href="${infourl}" id="pay"><button class="new-btn-login"
						type="button">确认支付</button></a>
			</div>
			<div class="question">
				<div class="new">
					<a href="http://www.zhifuka.net/gateway/weifutong/bind.html"
						target="_blank">微信支付如何绑定银行卡?</a>
				</div>
			</div>
		</div>
		<!--扫描代码结束-->
		<!--底部代码-->
		<div class="s-foot">微信支付</div>
		<!--底部代码结束-->
	</div>
	</div>
	<header class="header">飞讯无限Wi-Fi帐户中心</header>
	<div class="cont">
		<h1 style="color: #44cb32;">缴费处理中···</h1>
		<span class="succ">···</span>
		<p class="info"></p>
	</div>
	<div class="remind">
		<span>温馨提示：</span>
		<p>1、请勿将您的帐号借于他人使用，会有一定机率的帐户锁定风险；</p>
		<p>2、您在上网过程中遇到的任何问题，都可关注飞讯无限WiFi服务公众号“宽未来”获得帮助；</p>
		<p>3、由于ios系统原因，个别iphone用户连接WiFi后不会自动弹跳认证页面，请重启手机即可解决。</p>
	</div>
	<script type="text/javascript">
		window.location.href = $("#pay").attr("href");
	</script>
</body>
</html>
