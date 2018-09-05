<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/app" />
<c:set var="pathAPP" value="${ctx}/partol" />
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
</head>
<body class="mod-simple-follow">
	<input name="params" value='${obj.param}' type="hidden" id="param">
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
				<a href="javaScript:;"><img class="pn_img_download"
					id="downloadApp" alt="" src="${path}/images/pingan.png"></a>
			</div>
			<!--底部信息-->
			<div class="pn-bottom">
				<p>深圳平安讯科技术有限公司</p>
				<p>客服电话：400-8299309</p>
			</div>
		</div>
	</div>
	<div id="alert"></div>
	<div id="alertTip">
		<div>
			<img src="${path}/images/alertbg.png" />
			<div id="appStore">免流量去App Store下载</div>
		</div>
	</div>
	<img id="close" src="${path}/images/icon-close.png" />
	<script type="text/javascript" src="${ctx}/partol/js/jquery.min.js"></script>
	<script src="${path}/js/zepto.min.js"></script>
	<script src="${path}/js/selector.js"></script>
	<script src="${path}/js/touch.js"></script>
	<script src="${path}/js/layer/layer.js"></script>
</body>

<script type="text/javascript">
var url="${obj.url}";
$("#close").on("click",function(){
	$("#alertTip,#alert,#close").hide();
});
$("#downloadApp").on("click",function(){
	$("#alert,#alertTip,#close").show();
});
$("#appStore").on("click",function(){
	$.ajax({
		type:"post",
		url:ctx+"/login/toPass",
		async:false,
		data:{
			param:$("#param").val()
		},
		success:function(data){
			window.location.href =url ; /***打开app的协议，有ios同事提供***/
		}
	});
});
</script>

</html>

