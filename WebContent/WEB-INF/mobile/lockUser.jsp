<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/partol" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<meta name="msapplication-tap-highlight" content="no">
<!-- 避免IE使用兼容模式 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- 针对手持设备优化，主要是针对一些老的不识别viewport的浏览器，比如黑莓 -->
<meta name="HandheldFriendly" content="true">
<!-- 微软的老式浏览器 -->
<meta name="MobileOptimized" content="320">
<!-- uc强制竖屏 -->
<meta name="screen-orientation" content="portrait">
<!-- QQ强制竖屏 -->
<meta name="x5-orientation" content="portrait">
<!-- UC强制全屏 -->
<meta name="full-screen" content="yes">
<!-- QQ强制全屏 -->
<meta name="x5-fullscreen" content="true">
<!-- UC应用模式 -->
<meta name="browsermode" content="application">
<!-- QQ应用模式 -->
<meta name="x5-page-mode" content="app">
<!-- windows phone 点击无高光 -->
<meta name="msapplication-tap-highlight" content="no">
<title>连接数</title>
<link rel="stylesheet" type="text/css" href="${path }/css/public.css" />
<script type="text/javascript" src="${path }/js/jquery.min.js"></script>
<style type="text/css">
.lkn {
	font-size: 0.2rem;
	color: #333;
	text-align: center;
}

.lkn_a {
	font-size: 0.15rem;
	color: #6E6E6E;;
	margin-top: 0.3rem;
	text-align: center;
}

.lkn_b {
	font-size: 0.14rem;
	color: #6E6E6E;;
	text-align: center;
	margin-top: 2%;
	margin-bottom: 17%;
}

.lkn_time {
	color: #333333 !important;
	margin: 0 0.05rem;
}

.lkn_background {
	width: 100%;
	height: 2.5rem;
	background: url(../partol/img/lik.png) no-repeat 50% 50%;
	background-size: 0.8rem 2.2rem;
	/*background-color: #0086B3;*/
	margin-top: 10%;
	margin-bottom: 5%;
}

.lkn_btn {
	width: 80%;
	height: 0.35rem;
	line-height: 0.35rem;
	border: 1px solid #333333;
	margin: 0 auto;
	border-radius: 2px;
}

.lkn_btn>div {
	float: left;
	text-align: center;
}

.lkn_btn_left {
	height: 100%;
	width: 48%;
	color: #333333;
	cursor: pointer;
}

.lkn_btn_right {
	height: 100%;
	width: 48%;
	color: #258de6;
	cursor: pointer;
}

.lkn_btn_center {
	height: 80%;
	width: 1%;
	margin-left: 0px 1%;
	margin-top: 1%;
	background-color: #333333;
}
</style>
<script type="text/javascript">
			var ctx="${ctx}";
		</script>
</head>
<body style="background-color: #ffffff;">
	<div class="main">
		<div class="lkn_background"></div>
		<div class="lkn">你的账号连接数超过限额</div>
		<div class="lkn_a">你的账号存在风险，已被锁定</div>
		<div class="lkn_b">
			将于<span class="lkn_time">${obj.locakTime}</span>自动解锁
		</div>
		<div class="clearfix lkn_btn">
			<div class="lkn_btn_left">修改密码</div>
			<div class="lkn_btn_center"></div>
			<div class="lkn_btn_right">切换账号</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var url="${obj.changeurl}",
		username="${obj.userName}";
		$(function(){
			$(".lkn_btn_right").click(function(){
				window.location.href=url+"&state=-1";
			});
			$(".lkn_btn_left").click(function(){
				window.location.href=ctx+"/login/toUpdatePwd?userName="+username.replace(/\s/g,"")+"&ur="+escape(url);
				
			});
		})
	
	</script>
</html>