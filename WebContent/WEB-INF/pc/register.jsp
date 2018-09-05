<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/PcPartol" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!--确保IE浏览器采用最新的渲染模式-->
<title>飞讯无限—WIFI</title>
<link rel="stylesheet" type="text/css" href="${path }/css/reset.css" />
<link rel="stylesheet" type="text/css" href="${path }/css/recharge.css" />
<script type="text/javascript">
    	var base="${ctx}";
        </script>
</head>
<body>
	<div class="cart-page">
		<div class="title">
			<div>Welcome TO Unite WI-FI</div>
		</div>
		<div class="bgdCenter clearfix">
			<div class="pwd_left"></div>
			<!--输入手机号登录模块-->
			<div class="pwd-rightPhone  cell_phone" style="display: none;">
				<div class="pwd-rightTitle">输入手机号即可轻松上网</div>
				<div class="pwd-rightInput">
					<input type="text" name="phone" id="phone" placeholder="请输入您的手机号码" />
					<div id="next_a">下一步</div>
				</div>
				<div class="new_user">
					<p>欢迎使用中国移动WIFI</p>
					<%-- <c:if test="${obj.map.site.is_probative=='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}分钟</p>
					</c:if>
					<c:if test="${obj.map.site.is_probative!='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}小时</p>
					</c:if> --%>
				</div>
				<div class="RightFooter">服务支持：${obj.map.site.adminer} |
					飞讯无限提供计费服务</div>
			</div>
			<!--输入验证码-->
			<div class="pwd-rightPhone rightPhone_a security_code"
				style="display: none;">
				<div class="pwd-rightTitle">输入验证码</div>
				<div class="pwd-Identifying">
					<div>
						<span class="num_phone"> </span> <input id="btn_code"
							type="button" value="获取验证码" onclick="Identifying();">
					</div>

					<div class="Identifying_input">
						<input type="text" name="Identifying_input_a"
							id="Identifying_input_a" />
					</div>
				</div>
				<div id="next_b" style="font-size: 18px;">登陆</div>
				<div class="new_user">
					<p>欢迎使用中国移动WIFI</p>
					<%-- <c:if test="${obj.map.site.is_probative=='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}分钟</p>
					</c:if>
					<c:if test="${obj.map.site.is_probative!='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}小时</p>
					</c:if> --%>
				</div>
				<div class="RightFooter">中国移动通信有限公司</div>
				<%-- 					<div class="RightFooter">服务支持：${obj.map.site.adminer} | 飞讯无限提供计费服务</div>
 --%>
			</div>

			<!--已经注册输入密码登录-->
			<div class="pwd-right enp_a" style="display: none;">
				<div class="pwd-rightTitle">输入登录密码</div>
				<div>
					<span class="num_phone_a"></span>
				</div>
				<div class="pwd-rightInput" style="margin-top: 20px;">
					<input type="password" name="password" id="password"
						placeholder="请输入注册密码" /> <input type="text" name="password2"
						id="password2" placeholder="请输入注册密码" />
					<div id="show" onclick="hidePsw();"></div>
					<div id="hide" onclick="showPsw();"></div>
				</div>
				<div class="findPassword">忘记密码</div>
				<div id="next_f">登 录</div>
				<div class="new_user">
					<p>欢迎使用中国移动WIFI</p>
					<%-- <c:if test="${obj.map.site.is_probative=='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}分钟</p>
					</c:if>
					<c:if test="${obj.map.site.is_probative!='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}小时</p>
					</c:if> --%>
				</div>
				<%-- 					<div class="RightFooter">服务支持：${obj.map.site.adminer} | 飞讯无限提供计费服务</div>
 --%>
				<div class="RightFooter">中国移动通信有限公司</div>

			</div>
			<!--忘记密码模块-->
			<div class="pwd-rightPhone rightPhone_a identifying"
				style="display: none;">
				<div class="pwd-rightTitle">输入验证码</div>
				<div class="pwd-Identifying">
					<div class="pic-validation">
						<div class="code" id="checkCode" onclick="createCode()"></div>
						<div class="code_right" onclick="createCode()">看不清楚</div>
					</div>
					<div class="Identifying_input_a">
						<input type="text" name="Identifying_input" id="Identifying_input" />
					</div>
				</div>
				<div class="btn_all">
					<div class="btn_back">返回登录</div>
					<div id="next_c" style="display: none;">下一步</div>
				</div>
				<div class="new_user">
					<p>欢迎使用中国移动WIFI</p>
					<%-- <c:if test="${obj.map.site.is_probative=='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}分钟</p>
					</c:if>
					<c:if test="${obj.map.site.is_probative!='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}小时</p>
					</c:if> --%>
				</div>
				<div class="RightFooter">中国移动通信有限公司</div>
				<%-- 					<div class="RightFooter">服务支持：${obj.map.site.adminer} | 飞讯无限提供计费服务</div>
 --%>
			</div>
			<!--二次登陆模块-->
			<div class="pwd-rightPhone rightPhone_a second"
				style="display: none;">
				<div class="pwd-rightTitle" value="${obj.map.site.id }">欢迎使用${obj.map.site.site_name }Unite
					Wi-Fi</div>
				<div class="pwd-second"></div>
				<div class="btn_all">
					<div class="btn_back2">切换账号</div>
					<div id="next_h" style="display: none;">登录</div>
				</div>
				<div class="new_user">
					<p>欢迎使用中国移动WIFI</p>
					<%-- <c:if test="${obj.map.site.is_probative=='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}分钟</p>
					</c:if>
					<c:if test="${obj.map.site.is_probative!='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}小时</p>
					</c:if> --%>
				</div>
				<div class="RightFooter">中国移动通信有限公司</div>
				<%-- <div class="RightFooter">服务支持：${obj.map.site.adminer} | 飞讯无限提供计费服务</div> --%>
			</div>
			<!--设置新密码-->
			<div class="pwd-right setPassword" style="display: none;">
				<div class="pwd-rightTitle">设置新密码</div>
				<div style="font-size: 20px; margin-bottom: 20px;">
					此密码将作为您上网的唯一密码，请牢记此密码</div>
				<div class="pwd-rightInput">
					<input type="password" name="password_a" id="password_a"
						placeholder="请输入注册密码" /> <input type="text" name="password2_a"
						id="password2_a" placeholder="请输入注册密码" />
					<div id="show_a" onclick="hidePsw1();"></div>
					<div id="hide_a" onclick="showPsw1();"></div>
					<div id="next_d">完成并登录</div>
				</div>
				<div class="new_user">
					<p>欢迎使用中国移动WIFI</p>
					<%-- <c:if test="${obj.map.site.is_probative=='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}分钟</p>
					</c:if>
					<c:if test="${obj.map.site.is_probative!='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}小时</p>
					</c:if> --%>
				</div>
				<div class="RightFooter">中国移动通信有限公司</div>
				<%-- 					<div class="RightFooter">服务支持：${obj.map.site.adminer} | 飞讯无限提供计费服务</div>
 --%>
				<div class="aginlogin" style="display: none;">
					<h1 value="${obj.map.site.id }">欢迎使用${obj.map.site.site_name }
						Wi-Fi</h1>
					<div class="phoneinput">
						<input type="tel" id="tel" maxlength="11"
							onkeyup="this.value=this.value.replace(/\D/g,'')" value="" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" id="telphone" value="${obj.map.user.userName}" />
	<input type="hidden" id="user" value="${obj.map.user}" />
	<input type="hidden" id="siteType" value="${obj.map.type}">
	<input type="hidden" id="site" value="${obj.map.site}">
	<input type="hidden" id="mac" value="${obj.map.type.mac}">
	<input type="hidden" id="mac1" value="${obj.mac}">
	<input type="hidden" id="ip" value="${obj.map.type.ip}">
	<input type="hidden" id="username" value="${obj.map.user.userName}">
	<input type="hidden" id="passwordzhi" value="${obj.map.user.passWord}">
	<input type="hidden" id="password" value="${obj.map.user.passWord}">
	<script src="${path }/js/jquery-1.11.2.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path }/js/buyType.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path }/layer/layer/layer.js"></script>
	<script src="${path }/js/setPassword.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path }/js/RSA.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${path }/js/BigInt.js"></script>
	<script type="text/javascript" src="${path }/js/Barrett.js"></script>
</body>
</html>
