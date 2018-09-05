<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/partol" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>充值记录</title>
<link rel="stylesheet" type="text/css" href="${path}/css/public.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/personal1.css" />
<link rel="stylesheet" type="text/css" href="${path}/fonts/iconfont.css" />
<script>
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
		</script>
<script type="text/javascript">
			var ctx = "${ctx}";
		</script>
</head>

<body style="background: #f5f5f5;">
	<div class="main" style="margin-bottom: 18%;">
		<div class="paidtitle">
			<ul class="tabs">
				<li class="on"><a href="#tab1">全部</a></li>
				<li><a href="#tab2">待支付</a></li>
				<li><a href="#tab3">已完成</a></li>
				<li><a href="#tab4">已失效</a></li>
			</ul>
		</div>
		<div class="padtaball tab_content clearfix" id="tab1">
			<div class="paidcontall clearfix"></div>
		</div>
		<div class="padtaball tab_content clearfix" id="tab2">
			<div class="paidcontall clearfix"></div>
		</div>
		<div class="padtaball tab_content clearfix" id="tab3">
			<div class="paidcontall clearfix"></div>
		</div>
		<div class="padtaball tab_content clearfix" id="tab4">
			<div class="paidcontall clearfix"></div>
		</div>
		<div class="padtaball tab_content clearfix" id="tab5">
			<div class="paidcontall clearfix"></div>
		</div>
	</div>
	<div id="page"></div>
	<input type="hidden" value="${obj.site.id}" id="siteId" />
	<input type="hidden" value="${obj.user.id}" id="userId" />
	<script src="${path}/laypage-v1.3/laypage/laypage.js"
		type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${path}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${path}/js/payrecord.js"></script>
	<script type="text/javascript"
		src="${path }/layer.mobile-v2.0/layer_mobile/layer.js"></script>
</body>

</html>