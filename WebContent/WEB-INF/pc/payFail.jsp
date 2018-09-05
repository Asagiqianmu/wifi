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
<title>缴费失败</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="${path}/css/reset.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/recharge.css" />
</head>
<body>
	<!--支付失败模块-->
	<div class="pay_error" style="display: block;">
		<div class="recharge_body_title"></div>
		<div class="pay_succes_title">支付失败！</div>
		<p class="pay_succes_p">成功支付到入账有时可能存在延迟,如未及时到账请</p>
		<p class="pay_succes_p">
			耐心等待，或拨打客服电话<span>15972935811</span>
		</p>
		<div class="checkBalance">查询余额</div>
	</div>
</body>
</html>