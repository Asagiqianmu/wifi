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
<title>修改密码</title>
<link rel="stylesheet" type="text/css"
	href="${path }/bootstrap-dist/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${path }/css/register.css" />
<style type="text/css">
.resetPassword {
	width: 100%;
	padding-bottom: 10%;
	background-color: #FFFFFF;
	margin: 0 auto;
}

.resetPassword>div {
	width: 90%;
	margin: 0 auto;
	/*height: 30px;*/
}

.rd_row>div {
	float: left;
}

.rd_row {
	width: 100%;
	margin: 0 auto;
	height: 100%;
}

.rd_rowLeft {
	width: 20%;
	height: 60px;
	background: url(../partol/img/resetpsd.png) no-repeat 47% 89%;
	background-size: 65%;
}

.rd_rowLeftA {
	width: 20%;
	height: 60px;
	line-height: 60px;
	font-size: 23px;
	text-align: center;
}

.rd_rowRight {
	width: 80%;
	border-bottom: 1px solid #333333;
}

.rd_rowRight input {
	/*border: 1px solid #dedede!important;*/
	width: 100%;
	height: 50px;
	padding-left: 10px;
	margin-top: 10px;
}

#level {
	width: 80%;
	margin-left: 20%;
	margin-top: 3%;
}

#level>li {
	float: left;
	width: 30%;
	border-radius: 2px;
	height: 5px;
	background-color: #dedede;
}

#level>li.selected {
	float: left;
	width: 30%;
	border-radius: 2px;
	height: 5px;
	background-color: red !important;
	color: #fff;
}

#resetBtn {
	border: 1px solid #333333;
	width: 85%;
	margin: 0 auto;
	margin-top: 10%;
	height: 40px;
	line-height: 40px;
	text-align: center;
	color: #258DE6;
	border-radius: 3px;
	cursor: pointer;
}
</style>
<script type="text/javascript">
	var ctx = "${ctx}";
</script>
</head>
<body style="background-color: #F5F5F5;">
	<div class="main">
		<form id="resetForm">
			<div class="resetPassword">
				<div>
					<div class="rd_row clearfix">
						<div class="rd_rowLeftA" style="line-height: 68px;">
							<i class="glyphicon glyphicon-user"></i>
						</div>
						<div class="rd_rowRight name"
							style="margin-top: 25px; padding-left: 4%; font-size: 23px;">
							${obj.userName}</div>
					</div>
					<div class="rd_row clearfix">
						<div class="rd_rowLeft"></div>
						<div class="rd_rowRight">
							<input type="text" name="oldPassword" id="oldPassword"
								placeholder="请输入当前主账号密码" />
						</div>
					</div>
					<div class="rd_row clearfix">
						<div class="rd_rowLeft"></div>
						<div class="rd_rowRight">
							<input type="text" name="newPassword" id="newPassword"
								placeholder="请输入新密码" />

						</div>
					</div>
					<ul id="level" class="clearfix">
						<li></li>
						<li style="margin: 0px 5%;"></li>
						<li></li>
					</ul>
					<div class="rd_row clearfix">
						<div class="rd_rowLeft"></div>
						<div class="rd_rowRight">
							<input type="text" name="ResetNewPassword" id="ResetNewPassword"
								placeholder="请再次输入新密码" />
						</div>
					</div>
					<div id="resetBtn">修改完成</div>
				</div>
			</div>

		</form>
	</div>
	<input type="hidden" value="${obj.url}" id="url">
	<script type="text/javascript" src="${path }/js/jquery-1.9.1.min.js"></script>
	<script src="${path }/layer.mobile-v2.0/layer_mobile/layer.js"></script>
	<script src="${path }/js/updatePwd.js"></script>
</body>
</html>