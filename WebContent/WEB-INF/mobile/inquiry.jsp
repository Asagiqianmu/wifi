<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="data" value="${requestScope.data}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<title>热点查询</title>
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/Wireless_Nanshan.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/portal.css" />
<script type="text/javascript" src="${ctx}/partol/js/jquery.min.js"></script>
</head>
<body>
	<!--标题1-->
	<div class="header_feed header_feed_but">
		<div class="back_but" id="home_buts">
			<a href="javascript:history.go(-1);"><img
				src="${ctx}/static/images/back.png" /></a>
		</div>
	</div>
	<div class="clear"></div>
	<div class="header_feed header_feed_bg">
		<p>热点查询</p>
	</div>
	<!--主体-->
	<div id="search">
		<div class="searchL">
			<input type="text" placeholder='请输入关键字, 如"百事达"' name=""
				id="searchContent" value="" />
		</div>
		<div class="searchC"></div>
		<div class="searchR">
			<img id="searchId" src="${ctx}/static/images/search-for@2x.png" />
		</div>
	</div>
	<div id="inquiryNav">
		<div class="inquiryNavSon clearfix">
			<div class="inquiryNavSonL">
				<img src="${ctx}/static/images/Hot-search-blue@2x.png" />
			</div>
			<div class="inquiryNavSonR">${sessionScope.address2}</div>
		</div>
	</div>
	<ul id="inquirylist">

	</ul>
</body>
<script type="text/javascript" src="${ctx}/static/js/inquiry.js"></script>
<script type="text/javascript">
		base = "${ctx}";
		var data = eval('${requestScope.data}');
		initdata(base, data);
		
		$(function() {
			$("#searchId").click(function() {
				var content = $("#searchContent").val();
				if (content == "") {
					$(".ZindexTip,.ZindexTipAlert").show();
					$("#alertText").text("亲!请输入搜索内容")
					return false;
				}	
				searchHotSpot(content);
			});
		})
	</script>
</html>
