<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/PcPartol" />
<!DOCTYPE html>
<html>
<head>
<title>提交订单失败</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="${path}/css/reset.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/recharge.css" />
</head>
<body>
	<div class="pay_error" style="display: block;">
		<div class="recharge_body_title"></div>
		<div class="pay_succes_title">订单提交失败！</div>
		<span>温馨提示：</span>
		<p class="pay_succes_p">请勿将您的帐号借于他人使用，会有一定机率的帐户锁定风险；</p>
		<p class="pay_succes_p">您在上网过程中遇到的任何问题，都可关注飞讯无限WiFi服务公众号“宽未来”获得帮助；</p>
		<p class="pay_succes_p">由于ios系统原因，个别iphone用户连接WiFi后不会自动弹跳认证页面，请重启手机即可解决。</p>
	</div>
</body>
</html>