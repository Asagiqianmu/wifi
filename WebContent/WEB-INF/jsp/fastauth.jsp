<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pathjs" value="${ctx}/web_js" />
<c:set var="pathcss" value="${ctx}/web_css" />
<c:set var="statics" value="${ctx}/static" />
<!DOCTYPE HTML>
<html>
<head lang="zh-CN">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>快速认证</title>
<link type="text/css" href="${pathcss}/base.css" rel="stylesheet" />
<!-- <link rel="stylesheet"
	href="http://wifi.weixin.qq.com/resources/css/style-simple-follow.css" /> -->
<link rel="stylesheet" href="${pathcss}/style-simple-follow.css" />

<style type="text/css">
.chat_h {
	padding: 45px 0 30px 0;
	text-align: center;
}

.chat_h .going {
	color: #333333;
	font-size: 18px;
	margin: 25px 50px 0 50px;
	text-align: center;
	line-height: 1.6;
}

.chat_h .goes {
	color: #666666;
	font-size: 15px;
	margin-top: 15px;
}
.chat_h img{

	width: 55px;
}
.bar_progress {
	margin: 25px 30px 0 30px;
	background: #eeeeee;
	height: 7px;
	border-radius: 8px;
}

.bar_progress div {
	background: url(${statics}/images/progress.png) repeat-x;
	height: 7px;
	display: block;
	border-radius: 8px;
	width: 0;
}

.chat_txt {
	font-size: 12px;
	color: #e55d5d;
	margin: 15px 30px 25px 30px;
	text-align: left;
	line-height: 1.8;
}

.time {
	color: #666666;
	font-size: 12px;
	margin-top: 20%;
}
</style>
</head>

<body>
	<div class="chats">
		<div class="chat_h" id="phone">
			<img src="${statics}/images/fxwxbg.png" alt="" />
			<div class="going">WiFi快速认证正在进行跳转</div>
			<div class="time">正在跳转（您欢迎使用飞讯WiFi，极速上网，低价不限量。）</div>
			<div class="bar_progress">
				<div>
					<input type="hidden" id="telphone" value="${obj.map.user.userName}" />
					<input type="hidden" id="siteId" value="${obj.map.site.id }" />
					<input type="hidden" id="authurl" value="${obj.map.url}" />
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${pathjs}/jquery.min.js"></script>
<script type="text/javascript">
	
	$(".bar_progress div").animate({
		width : "100%"
	}, 3000, function() {
		setTimeout(function() {
			$(".bar_progress div").fadeIn(3000);
		});
	});
	
	function setCookie(name, value, liveMinutes) {  
		if (liveMinutes == undefined || liveMinutes == null) {
			liveMinutes = 60 * 2;
		}
		if (typeof (liveMinutes) != 'number') {
			liveMinutes = 60 * 2;//默认120分钟
		}
		var minutes = liveMinutes * 60 * 1000;
		var exp = new Date();
		exp.setTime(exp.getTime() + minutes + 8 * 3600 * 1000);
		//path=/表示全站有效，而不是当前页
		document.cookie = name + "=" + value + ";path=/;expires=" + exp.toUTCString();
	}
	var phone = $("#telphone").val();
	var siteId = $("#siteId").val();
	var authurl = $("#authurl").val();
	setCookie("ar0", phone, 120);
	setCookie("ar1", siteId, 120);
	
	if(authurl != "" && authurl.length > 10){
		window.location.href = authurl;
	}
	
</script>
</html>
