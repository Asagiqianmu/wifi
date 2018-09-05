<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/partol" />
<!DOCTYPE HTML>
<html>
<head lang="zh-CN">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>微信连Wi-Fi</title>
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" type="text/css" href="${path }/css/public.css" />
<link rel="stylesheet" type="text/css" href="${path }/css/reg.css" />
<!-- 唤起微信的js  必须的 -->
<script type="text/javascript">
	var ctx = "${ctx}";
</script>
<script type="text/javascript" src="${path }/js/jquery.min.js"></script>
<script type="text/javascript" src="${path }/js/banner.js"></script>
<%-- 	<script type="text/javascript" src="${path }/js/reg.js"></script> --%>
<style type="text/css">
li {
	float: left;
}
</style>
<style type="text/css">
.shop_info {
	padding: 17px 13px;
	font-size: 15px;
	color: #777777;
}

.shop_txts {
	margin: 10px 0 0 0;
}

.shops_names {
	margin-bottom: 18px;
	color: #333333;
	font-size: 15px;
	text-align: center;
}

.shops_TopNames {
	margin-top: 15px;
	margin-bottom: 15px;
	color: #333333;
	font-size: 15px;
	text-align: center;
}

.approve_list {
	padding: 10px 0 25px 0;
	border-bottom: 10px solid #f8f8f8;
}

.approve_list>div {
	height: 50px;
	line-height: 50px;
	width: 260px;
	border-radius: 5px;
	margin: 37px auto 0 auto;
	color: #fff;
	font-size: 16px;
	cursor: pointer;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	padding-right: 70px;
}

.chat {
	background: #1aac5b;
}

.chat em {
	background: url(${path}/img/chat.png) no-repeat;
}

.approve_list em {
	width: 20px;
	height: 17px;
	display: inline-block;
	margin: 0 12px 0 70px;
	vertical-align: middle;
}
</style>
</head>
<body class="mod-simple-follow">
	<input type="hidden" id="siteType" value='${obj.map.type}' />
	<div class="shops_TopNames">欢迎使用${obj.map.site.site_name }-微信连WIFI</div>
	<div class="mod-simple-follow-page">
		<div id="banner" style="height: 180px;">
			<div id="banner_list">
				<c:if test="${obj.map.site.bannerUrl==''}">
					<li><img src="${path}/img/zan1.jpg" width="100%" height="100%" /></li>
					<li><img src="${path}/img/zan2.jpg" width="100%" height="100%" /></li>
					<li><img src="${path}/img/zan3.jpg" width="100%" height="100%" />
					</li>
				</c:if>
				<c:if test="${obj.map.site.bannerUrl!=''}">
					<%-- <li><img src="${path}/img/zan1.jpg" width="100%" height="100%" /></li>
						<li><img src="${path}/img/zan2.jpg" width="100%" height="100%"/></li>
						<li><img src="${path}/img/zan3.jpg" width="100%" height="100%"/> </li> --%>
					<c:forEach items="${obj.map.site.bannerUrl.split(':')}" var="p">
						<li><img src="${ctx}/${p}" width="100%" height="100%" /></li>
					</c:forEach>
				</c:if>
			</div>
		</div>
		<div class="mod-simple-follow-page__attention">
			<!--认证列表-->
			<div class="approve_list">
				<form action="${ctx}/PROT/toWechatLogin" method="post"
					id="submitForm">
					<input name="ssid" value="Unite_35687E" type="hidden"> <input
						name="extend" value='${obj.map.type}' type="hidden">
				</form>
				<div class="chat" id="wechatLogin">
					<em></em>微信认证
				</div>
			</div>
		</div>
	</div>
	<div class="copyright">
		<!--店铺信息-->
		<div class="shop_info">
			<div>
				<img src="${path}/img/logo.jpg" />
				<%--<div class="shop_infos_top">
			    <img src="${path}/img/5993c32b10702.png" />
			</div>
			<div class="shop_infos">
			
			 <div class="shops_names">${obj.map.site.site_name }</div>
				<div>${obj.map.site.adminer} </div> 
			</div>
			<div style="clear:both;"></div>
			--%>
			</div>
			<div class="shop_txts" id="shop"
				style="font-size: 16px; color: #333; line-height: 26px;">
				<!-- 	<span style="margin-left: 39px;"></span> -->
				<p style="font-size: 16px;">深圳公共无线局域网</p>
				<!-- <p style=" font-size: 16px; ">服务中小提质增效，服务城乡统筹发展;</p> -->
			</div>
		</div>
		<!--店铺信息--end-->
	</div>
</body>
<script type="text/javascript">
	$("#wechatLogin").click(function() {
		$('#submitForm').submit();
	});
</script>

</html>

