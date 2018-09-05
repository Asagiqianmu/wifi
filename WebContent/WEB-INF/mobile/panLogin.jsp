<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/app" />
<!DOCTYPE HTML>
<html>
<head lang="zh-CN">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>平安优联</title>
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" type="text/css" href="${path }/css/index.css" />
<!-- 唤起微信的js  必须的 -->
<script type="text/javascript">
		var ctx="${ctx}";
	</script>
<script type="text/javascript" src="${ctx }/partol/js/jquery.min.js"></script>

</head>
<body class="mod-simple-follow">
	<%--     <div class="mod-simple-follow-page__attention">
        <!--认证列表-->
		<div class="approve_list">
		<form action="${ctx}/login/toAppLogin" method="post" id="submitForm" >
		        <input name="params" value='${obj.map.type}' type="hidden">
		</form>
				<div class="chat" id="wechatLogin" >
				<em></em>平安友联认证
			</div>
		</div>
    </div> --%>
	<input name="params" value='${obj.map.type}' type="hidden" id="param">
	<input name="params" value='${obj.andorid}' type="hidden" id="andorid">
	<input name="params" value='${obj.ios}' type="hidden" id="ios">
	<input name="params" value='${obj.mac}' type="hidden" id="usermac">
	<div class="app">
		<!--头部-->
		<div class="pn-header">
			<span class="pn-back"></span><span class="pn-title">平安优联</span> <span
				class="pn-tools"></span>
		</div>
		<!--内容-->
		<div class="pn-content">
			<!--头部logo-->
			<div class="content-logo">
				<img src="${path}/images/logo.png" alt="">
			</div>
			<div class="content-net">
				<img src="${path}/images/wenan.png" alt="">
			</div>
			<div class="content-center">
				<img src="${path}/images/image.png" alt=""> <img alt="">
				<a href="javaScript:;"><img class="pn_img_download" id="openApp"
					alt="" src="${path}/images/da-kai-pinganyoulian.png"></a>
			</div>
			<!--底部信息-->
			<div class="pn-bottom">
				<p>深圳平安讯科技术有限公司</p>
				<p>客服电话：400-8299309</p>
			</div>
		</div>
	</div>
	<script src="${path}/js/zepto.min.js"></script>
	<script src="${path}/js/selector.js"></script>
	<script src="${path}/js/touch.js"></script>
	<script src="${path}/js/index.js"></script>
</body>

</html>

