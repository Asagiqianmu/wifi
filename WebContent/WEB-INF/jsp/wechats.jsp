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
<title>Wi-Fi认证</title>
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" type="text/css" href="${pathcss}/public.css" />
<link rel="stylesheet" type="text/css" href="${pathcss }/reg.css" />
<!-- 唤起微信的js  必须的 -->
<script type="text/javascript">
	var ctx = "${ctx}";
</script>
<script type="text/javascript" src="${pathjs}/jquery.min.js"></script>
<script type="text/javascript" src="${pathjs}/banner.js"></script>
<%-- 	<script type="text/javascript" src="${path }/js/reg.js"></script> --%>
<style type="text/css">
li {
	float: left;
}
</style>
<style type="text/css">
.shop_info {
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
	margin-top: 10px;
	margin-bottom: 10px;
	color: #333333;
	font-size: 16px;
	text-align: center;
}

.approve_list {
	padding: 10px 0 45px 0;
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

.chat:ACTIVE {
	background: #148848;
}

.chat em {
	background: url(${statics}/images/chat.png) no-repeat;
}

.chat tel {
	background: url(${statics}/images/tel.png) no-repeat;
}

.approve_list em {
	width: 20px;
	height: 17px;
	display: inline-block;
	margin: 0 12px 0 70px;
	vertical-align: middle;
}

.approve_list tel {
	width: 20px;
	height: 20px;
	display: inline-block;
	margin: 0 12px 0 70px;
	vertical-align: middle;
}
</style>
</head>
<body>
	<div class="main">
		<input type="hidden" id="siteType" value='${obj.map.type}' /> <input
			type="hidden" id="nasid" value='${obj.nasid}' /> 
			<input type="hidden" id="siteId" value='${obj.siteId}' /> 
			<input type="hidden" id="sitePrice" value='${obj.sitePrice}' />
		<div class="shops_TopNames">欢迎使用${obj.map.site.site_name }-微信连WIFI</div>
		<div id="banner">
			<ul>
				<li class="on"></li>
				<li></li>
				<li></li>
			</ul>
			<div id="banner_list">
				<img src="${ctx }/school_pic/background1.jpg" width="100%" height="100%" /> 
				<img src="${ctx }/school_pic/background2.jpg" width="100%" height="100%" /> 
				<img src="${ctx }/school_pic/background3.jpg" width="100%" height="100%" />
			</div>
		</div>
		<div>
			<div class="approve_list">
				<input id="ssid" value="Portal_WIFI" type="hidden"> <input
					id="extend" value='${obj.map.type}' type="hidden">
				<div class="chat" id="wechatLogin">
					<em></em>微信认证
				</div>
				<div class="chat" id="TelLogin">
					<tel></tel>
					手机认证
				</div>
			</div>
		</div>
		<div class="copyright">
			<p style="font-size: 16px;">深圳公共无线局域网</p>
		</div>
	</div>
</body>
<script type="text/javascript">
	$("#wechatLogin").click(
			function() {
				var base = "${ctx}";
				$.ajax({
					type : "post",
					url : base + "/PROT/toWechatLogin",
					async : false,
					data : {
						ssid : $("#ssid").val(),
						extend : $("#extend").val(),
						nasid : $("#nasid").val(),
						siteId : $("#siteId").val()
					},
					dataType : "json",
					success : function(data) {
						if (data.result == 100) {
							window.location.href = data.url;
						} else {
							var sitePrice=$("#sitePrice").val();
							window.location.href = base+"/PROT/wechatJd?data="+data.key;
						}
					}
				});
			});
	$("#TelLogin").click(function() {

	});
</script>

</html>

