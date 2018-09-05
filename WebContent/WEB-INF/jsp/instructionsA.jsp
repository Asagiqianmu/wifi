<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />


<!DOCTYPE html>
<html>
<head>
<title>使用说明</title>
<meta name="viewport"
	content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no;">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" href="${ctx }/web_css/reset.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/web_css/wireless.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/web_css/portal.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/web_css/input.css" />
<script type="text/javascript" src="${ctx }/partol/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx }/web_js/wireless.js"></script>
<script type="text/javascript" src="${ctx }/web_js/custom_input.js"></script>
<script type="text/javascript" src="${ctx }/web_js/custom_input.js"></script>
</head>
<body class="MaxZindex">
	<div>
		<!--登录框-->
		<div class="common_box">
			<div class="login">
				<div class="common_title">《飞讯WiFi使用说明》</div>
				<div class="common_text">
					<p class="act">1."飞讯WiFi"是由武汉市飞讯无限网络技术有限公司提供或支持的无线WiFi网络，是一项利民、便民、惠民的民生工程，旨在为公众提供更多"信息福利"。</p>
					<p class="act">2.飞讯WiFi的稳定性与周边障碍物（树、墙壁、栅栏、车辆等）、电磁环境（同频干扰、微波炉干扰等）、天气等有密切关系，所以稳定性不及有线网络，影响用户体验。此外，用户终端本身信号发射功率大小也在一定程度上影响用户体验。</p>
					<p class="act">3."飞讯WiFi"支持中国移动、中国电信、中国联通三家运营商手机号码认证上网。</p>
					<p class="act">4."飞讯WiFi"支持智能手机、平板电脑、笔记本电脑等具有无线上网功能的终端接入上网。</p>
					<p class="act">5.成功登录"飞讯WiFi"后，若终端连续15分钟无流量时，认证系统自动将用户作下线处理。用户重新登录即可正常使用。</p>
				</div>
				<br /> <a href="javascript:history.go(-1);" class="back_portal">返回认证</a>
			</div>
		</div>
	</div>
</body>
</html>