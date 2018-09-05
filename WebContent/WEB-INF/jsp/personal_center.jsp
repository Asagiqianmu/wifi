<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
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

<link rel="stylesheet" type="text/css" href="${ctx }/web_css/reset.css" />
<link rel="stylesheet" href="${ctx }/web_css/wireless.css" />
<title>个人中心</title>

<style type="text/css">
body {
	margin: 0;
	padding: 0;
	background: #f7f7f7;
    overflow: auto;
}
.head{
	width: 100%;
	height: 50px;
	background-color: #34c1e6;
}
.info{
	width: 100%;
	height: 100%;
}
.info_head{
	width: 80%;
	margin-left: 10%;
	font-size: 16px;
}
</style>
</head>

<body>
	<div class="main">
		<div class="select_bg"></div>
		<!--标题1-->
		<div class="header_feed header_feed_but">
			<div class="back_but" id="home_buts">
				<a href="javascript:history.go(-1);"><img
					src="${ctx}/static/images/back.png" /></a>
			</div>
		</div>
		<div class="clear"></div>
		<div class="header_feed2 header_feed_bg">
			<p>个人中心</p>
		</div>
		<div class="info">
			<div class="info_head">
				<p>用户名：<span id="user">${obj.username}</span></p>
				<p>到期时间：<span id="time">${obj.date}</span></p>
			</div>
		</div>
		<div class="footer">
		 	<c:import url="https://cpu.baidu.com/block/app/f227e05e/12742"></c:import> 
			<%-- <c:import url="https://cpu.baidu.com/block/app/f227e05e/12749"></c:import>  --%>
		</div>
		<!-- <div class="footer2"> -->
		 	<c:import url="http://new.taobc.com/api/wap_03.html" charEncoding="UTF-8" ></c:import> 
		<!-- </div> -->
	</div>
</body>
<script type="text/javascript" src="${ctx}/web_js/jquery.min.js"></script>
<script type="text/javascript">

	$(function() {
		var a_ = $(".footer div div div ul a");
		var src = a_.attr('href');
		a_.attr("href","https://cpu.baidu.com"+src); 
		var a_1 = $(".footer2 div div a");
		var src1 = a_1.attr('href');
		a_1.attr("href","http:"+src1); 
	});
	
</script>
</html>
