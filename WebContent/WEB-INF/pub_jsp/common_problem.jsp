<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>常见问题</title>
<meta name="viewport"
	content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no;">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<meta charset="utf-8">
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/Wireless_Nanshan.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/portal.css" />
<script type="text/javascript" src="${ctx}/partol/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${ctx}/static/js/Wireless_Nanshan.js"></script>
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/input.css" />
<script type="text/javascript" src="${ctx}/static/js/custom_input.js"></script>
<script type="text/javascript">
		$(function(){
			
		})
	</script>
</head>
<body class="MaxZindex">
	<div>
		<!--登录框-->
		<div class="common_box">
			<div class="login">
				<div class="common_title">《深圳公共无线局域网常见问题》</div>
				<div class="common_text">
					<div class="qus">1.为什么点"获取密码"按钮后，收不到短信?</div>
					<p class="ans">答：当用户点"获取密码"按钮后，一般情况下短信系统会在60秒内发送密码短信，若60秒后收不到短信，可能短信通道问题造成发送不成功，请重新获取短信。</p>
					<div class="qus">2.为什么连上WLAN后，访问认证平台、互联网比较慢?</div>
					<p class="ans">
						答：（1）可能信号不稳定或较弱，信息数据丢失率大。<br />（2）可能用户所连接的WLAN接入点(即AP设备)容量已达到饱和值。<br />（3）当前处于使用高峰期，并发访问用户人数多，网络通道拥挤。
					</p>
					<div class="qus">3.已经成功认证了，为什么还会掉线?</div>
					<p class="ans">
						答：（1）可能是该区域信号不稳定或很弱。<br /> （2）可能用户超过15分钟未产生任何流量或当天免费时长用完将被强制下线。<br />
						（3）可能当前终端认证的手机号码和密码在其他WLAN终端上登录，当前终端自动下线。<br />
						（4）可能处在WLAN使用的高峰期，周围用户的使用影响到您的使用。
					</p>
				</div>

				<br /> <a href="javascript:history.go(-1);" class="back_portal">返回认证</a>
			</div>
		</div>
	</div>
</body>
</html>