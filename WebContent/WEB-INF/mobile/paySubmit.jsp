<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0, user-scalable=no" />
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
<title>去支付</title>
</head>
<body>


	<div class="nav-wrap"></div>

	<div class="grid">

		<form method="post" action="${serverUrl}" id="payForm">
			<!--交易信息 start-->
			<input type="hidden" name="version" value="${payOrderInfo.version}"><br />
			<input type="hidden" name="merchant" value="${payOrderInfo.merchant}"><br />
			<input type="hidden" name="orderType"
				value="${payOrderInfo.orderType}"><br /> <input
				type="hidden" name="userId" value="${payOrderInfo.userId}"><br />
			<input type="hidden" name="tradeNum" value="${payOrderInfo.tradeNum}"><br />
			<input type="hidden" name="tradeName"
				value="${payOrderInfo.tradeName}"><br /> <input
				type="hidden" name="tradeTime" value="${payOrderInfo.tradeTime}"><br />
			<input type="hidden" name="amount" value="${payOrderInfo.amount}"><br />
			<input type="hidden" name="currency" value="${payOrderInfo.currency}"><br />
			<input type="hidden" name="notifyUrl"
				value="${payOrderInfo.notifyUrl}"><br /> <input
				type="hidden" name="callbackUrl" value="${payOrderInfo.callbackUrl}"><br />
			<input type="hidden" name="ip" value="${payOrderInfo.ip}"><br />
			<input type="hidden" name="sign" value="${payOrderInfo.sign}"><br />
			<!--交易信息 end-->
		</form>

	</div>

	<script>
 	document.getElementById("payForm").submit();
    
</script>
</body>
</html>
