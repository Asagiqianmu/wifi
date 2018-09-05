<%@ page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<% String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort(); 
	pageContext.setAttribute("basePath", basePath);
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="url" value="${basePath }" />
<!DOCTYPE html>
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
<!-- windows phone 点击无高光 -->
<meta name="msapplication-tap-highlight" content="no">
<script>
	var base = "${ctx}";
	var url = "${url}";
	var mac = "${usermac}";
</script>
<script type="text/javascript" src="${ctx }/partol/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx }/web_js/mybanner.js"></script>
<script type="text/javascript" src="${ctx }/web_js/config.js"></script>
<script type="text/javascript" src="${ctx }/web_js/json.js"></script>
<!-- <script type="text/javascript">
	$("#userName").ready(function() {
		var str = "${userName}";
		var str_user = str.substr(0, 3) + "****" + str.substr(7);
		document.getElementById("userName").innerHTML = str_user;
	});
</script> -->
<title>认证完成</title>
<link rel="stylesheet" type="text/css" href="${ctx }/web_css/public.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/web_css/index.css" />
</head>
<body>

	<div class="main">
		<div id="banner">
			<ul>   
		        <li class="on" onclick="liclick(0)"></li>
		        <li onclick="liclick(1)"></li>
		        <li onclick="liclick(2)"></li>
		    </ul>
			<div id="banner_list">
				<img src="${ctx }/school_pic/background1.jpg" title="01" onclick="imgclick(0)" width="100%"
					height="100%" /><a href="http://www.10086.cn/index/gd/index_200_755.html" target="_blank"></a>
				<img src="${ctx }/school_pic/background2.jpg" title="02" onclick="imgclick(1)" width="100%"
					height="100%" /><a href="http://www.10086.cn/index/gd/index_200_755.html" target="_blank"></a>
				<img src="${ctx }/school_pic/background3.jpg" title="03" onclick="imgclick(2)" width="100%"
					height="100%" /><a href="http://www.10086.cn/index/gd/index_200_755.html" target="_blank"></a>
			</div>
		</div>
		
		<div class="user_name">
			<span class="user_title" style="color:#34c1e6">WiFi认证成功！</span>
			<div class="center">
				<img class="user_img" src="${ctx }/static/images/user.png"><span>个人中心</span>
			</div>
		<!-- 	<ul class="user_ul">
				<a><li>会员</li></a>
				<li>退出</li>
			</ul> -->
			
		</div>
		<!-- <div class="rz_wifi">
			<span>想在此展示您对她（他）的一句问候吗？</span><br/>
			<span>想在此给她（他）一个惊喜吗？</span><br/>
			<span>想在此宣传您的产品吗？</span><br/>
			<span>咨询QQ：2217065328</span><br/>
		</div> -->
		

		<!-- 		
		<div class="footer"> @武汉飞讯无限信息技术有限公司版权所有 </div>
		 -->
		 <div class="footer">
		 	<c:import url="https://cpu.baidu.com/block/app/f227e05e/12742"></c:import> 
			<%-- <c:import url="https://cpu.baidu.com/block/app/f227e05e/12749"></c:import>  --%>
		</div>
		<!-- <div class="footer2"> -->
		 	<c:import url="http://new.taobc.com/api/wap_03.html" charEncoding="UTF-8" ></c:import> 
		<!-- </div> -->
		<div style="clear: both;"></div>
	</div>

	<c:if test="${obj.map.site.adminer==null}">
		<%-- <div class="copyright">@武汉飞讯无限信息技术有限公司 版权所有${obj.map.site.adminer}</div> --%>
	</c:if>
</body>
<script type="text/javascript">
	$(function() {
		var a_ = $(".footer div div div ul a");
		var src = a_.attr('href');
		a_.attr("href","https://cpu.baidu.com"+src); 
		var a_1 = $(".footer2 div div a");
		var src1 = a_1.attr('href');
		a_1.attr("href","http:"+src1); 
	});
	
	function getCookie(cname) {
	    var name = cname + "=";
	    var ca = document.cookie.split(';');
	    for(var i=0; i<ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0)==' ') c = c.substring(1);
	        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
	    }
	    return "";
	}
	var user = getCookie("ar0");
	var siteID = getCookie("ar1");
	
	$(".center").click(function(){
		window.location.href = "${ctx}/PROT/personalCenter?userName="+user+"&siteId="+siteID;
	});
	
</script>
</html>