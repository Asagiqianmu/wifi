<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
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
<title>系统消息</title>
<link rel="stylesheet" type="text/css" href="${path }/css/public.css" />
<link rel="stylesheet" type="text/css" href="${path }/css/personal.css" />
<!--图标样式-->
<link rel="stylesheet" type="text/css"
	href="${path }/fonts/iconfont.css" />
<script type="text/javascript" src="${path}/js/jquery.min.js"></script>
<script>
		 var base = "${ctx}";
			(function(doc, win) {
				var docEl = doc.documentElement,
					resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
					recalc = function() {
						var clientWidth = docEl.clientWidth;
						if(!clientWidth) return;
						if(clientWidth >= 750) {
							docEl.style.fontSize = '100px';
						} else {
							docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
						}
					};
				if(!doc.addEventListener) return;
				win.addEventListener(resizeEvt, recalc, false);
				doc.addEventListener('DOMContentLoaded', recalc, false);
			})(document, window);
			$(function(){
				var id = $('.sysmesstop').attr("value");
				if(id == undefined){
					$(".no-msg").css("display","block");
					$(".no-hei").css("display","block");
				}
			});
		   function ShowResult(){
			   var id = $('.sysmesstop').attr("value");
			   $.ajax({
					url : base + "/message/deleteMessage",
					data : {
						id : id
					},
					dataType : "json",
					success : function(data) {
					     if(data.result != "1"){
					    	 window.location.reload();
					     }else{
					    	 window.location.reload();	
					     }	
						}
					})
		   }
		</script>
</head>

<body>
	<div class="main">
		<div class="sys-title">系统消息列表</div>
		<div class="no-hei" style="height: 70px;"></div>
		<div style="height: 50px;"></div>
		<div class="no-msg" style="font-size: 14px;">系统暂无消息</div>
		<c:forEach var="message" items="${obj.listMessage}">
			<div class="messsystem">
				<div class="sysmesstop" value="${message.id}">
					<i class="iconfont icon-iconfontcolor33"></i> 系统消息 <a
						class="iconfont icon-shanchu fr" onclick="ShowResult()"></a>
				</div>
				<div class="sysnote">
					<span>${message.createTimes}</span>
					<p>${message.content}</p>
				</div>
			</div>
		</c:forEach>
	</div>
</body>
</html>