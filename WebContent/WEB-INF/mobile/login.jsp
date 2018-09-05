<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.fxwx.util.OssSchoolManage"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/partol" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<meta name="msapplication-tap-highlight" content="no">
<!-- 避免IE使用兼容模式 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- 针对手持设备优化，主要是针对一些老的不识别viewport的浏览器，比如黑莓 -->
<meta name="HandheldFriendly" content="true">
<!-- 微软的老式浏览器 -->
<meta name="MobileOptimized" content="320">
<!-- uc强制竖屏 -->
<meta name="screen-orientation" content="portrait">
<!-- QQ强制竖屏 -->
<meta name="x5-orientation" content="portrait">
<!-- UC强制全屏 -->
<meta name="full-screen" content="yes">
<!-- QQ强制全屏 -->
<meta name="x5-fullscreen" content="true">
<!-- UC应用模式 -->
<meta name="browsermode" content="application">
<!-- QQ应用模式 -->
<meta name="x5-page-mode" content="app">
<meta name="format-detection" content="telephone=no">

<meta http-equiv="x-rim-auto-match" content="none">
<!-- windows phone 点击无高光 -->
<meta name="msapplication-tap-highlight" content="no">
<title>登录页面</title>
<link rel="stylesheet" type="text/css" href="${path }/css/public.css" />
<link rel="stylesheet" type="text/css" href="${path }/css/reg.css" />
</head>
<script type="text/javascript">
			var ctx="${ctx}";
		</script>
<%
			String params= request.getQueryString();
		%>
<style>
.loading {
	width: 100px;
	height: 50px;
	margin: 0 auto;
}
</style>
<body>
	<div class="main">
		<div id="banner">
			<div id="banner_list">
				<c:if test="${obj.map.site.bannerUrl==''}">
					<li><img src="${path}/img/ban1.jpg" width="100%" height="100%" /></li>
					<li><img src="${path}/img/bg.jpg" width="100%" height="100%" /></li>
					<li><img src="${path}/img/1.jpg" width="100%" height="100%" />
					</li>
				</c:if>
				<c:if test="${obj.map.site.bannerUrl!=''}">
					<c:forEach items="${obj.map.site.bannerUrl.split(':')}" var="p">
						<li><img src="${ctx}/${p}" width="100%" height="100%" /></li>
					</c:forEach>
				</c:if>
			</div>
		</div>
		<div class="loginwrap">
			<div class="login">
				<form>
					<div class="aginlogin">
						<h1 value="${obj.map.site.id }">欢迎使用${obj.map.site.site_name }
							Wi-Fi</h1>
						<div class="phoneinput">
							<input type="tel" id="tel" maxlength="11"
								onkeyup="this.value=this.value.replace(/\D/g,'')"
								disabled="disabled" /> <input type="hidden" id="telphone"
								value="${obj.map.user.userName}" /> <input type="hidden"
								id="password" value="${obj.map.user.passWord}" /> <input
								type="hidden" id="siteType" value="${obj.map.type}" /> <input
								type="hidden" id="site" value="${obj.map.site}" />
						</div>
					</div>
					<div class="next-validate clearfix">
						<a href="${ctx}/login/unifiedEntrance?<%=params%>&state=-1">切换账号</a>
						<div class="nextloading">
							<input type="button" id="next" value="登  录" class="blue" />

						</div>
					</div>
				</form>
			</div>
			<div class="user-prompt">
				<p>欢迎使用中国移动WIFI</p>
				<%-- <c:if test="${obj.map.site.is_probative=='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}分钟</p>
					</c:if>
					<c:if test="${obj.map.site.is_probative!='30'}">
						<p>新用户可免费试用${obj.map.site.is_probative}小时</p>
					</c:if> --%>
			</div>
		</div>
		<div class="fillname">
			<div class="fillnamemoble"></div>
			<div class="fillnamewrap">
				<h2 id="cue"></h2>
				<div class="fillnamebutton clearfix">
					<a href="#" onclick="closeLayer()" id="nikelink">确认</a>
				</div>
			</div>
		</div>
		<!--预加载动画-->
		<div class="loading">
			<img src="${path}/img/loading.gif" class="loadPic" />
		</div>
	</div>

	<div class="copyright">中国移动通信有限公司</div>
	<%-- <c:if test="${obj.map.site.adminer==null}">
			<div class="copyright">服务支持：${obj.map.site.adminer} | 飞讯无限提供计费服务</div>	
		</c:if>
		<c:if test="${obj.map.site.adminer!=null}">
			<div class="copyright">服务支持：${obj.map.site.adminer} | 飞讯无限提供计费服务</div>	
		</c:if>  --%>
	<script type="text/javascript" src="${path }/js/jquery.min.js"></script>
	<script type="text/javascript" src="${path }/js/banner.js"></script>
	<script type="text/javascript" src="${path }/js/login.js"></script>
	<script type="text/javascript"
		src="${path }/layer.mobile-v2.0/layer_mobile/layer.js"></script>

	<script>
		 ;!function(doc, win){	 
			    	var docEl = doc.documentElement,
		            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
		            recalc = function () {
		                var clientWidth = docEl.clientWidth;
		                if (!clientWidth) return;
		                if(clientWidth>=750){
		                    docEl.style.fontSize = '100px';
		                }else{
		                    docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
		                }
		            };		
			        if (!doc.addEventListener) return;
			        win.addEventListener(resizeEvt, recalc, false);
			        doc.addEventListener('DOMContentLoaded', recalc, false);
			    }(document, window);
		</script>
</body>
<script type="text/javascript">
	$(".fillname").hide();
	var res="${obj.map.auth}",
		message="${obj.map.msg}",
		code="${obj.map.code}";
	if(res=="failed"&&message!=null&&message!=""){
			if(code==200){
				prompt(message);
			}else if(code==201) {
		 		prompt(message);
			}else if(code==202){//欠费
				prompt(message);
			}else if(code==205){
				prompt(message);
			}else if(code==206){
				prompt(message);
			}else if(code==208){
				prompt(message);
			}else if(code==304){
				//账号已登录
				prompt(message);
			}else if(code==306){
				//修改ip失败
				prompt(message);
			}
		 	message="";
		 	res="";
	}
	function closeLayer(){
		$(".fillname").hide();
	}
</script>
</html>