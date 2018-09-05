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
<title>资料编辑</title>
<link rel="stylesheet" type="text/css" href="${path}/css/public.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/personal.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/jcDate.css" />
<!--图标样式-->
<!--<link rel="stylesheet" type="text/css" href="fonts/demo.css"/>-->
<link rel="stylesheet" type="text/css" href="${path}/fonts/iconfont.css" />
<script type="text/javascript" src="${path}/js/jquery1.min.js"></script>
<script type="text/javascript" src="${path}/js/jquery.base64.js"></script>
<script type="text/javascript" src="${path}/js/editprofile.js"></script>
<script type="text/javascript" src="${path}/js/jQuery-jcDate.js"></script>
<script type="text/javascript" src="${path}/js/uploadImage.js"></script>
<script type="text/javascript" src="${path}/js/exif.js"></script>
<script type="text/javascript" src="${path}/js/mobileBUGFix.mini.js"></script>
<script type="text/javascript" src="${path}/laydate/laydate.js"></script>
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
			var s="${obj.user.sex}";
		</script>
<style type="text/css">
#cart-page {
	height: 100%;
	position: absolute;
	left: 0;
	right: 0;
	top: 0;
	bottom: 0;
	background: white;
}

.laydate_body .laydate_y .laydate_yms ul li {
	width: 58px;
}

.loading {
	width: 100px;
	height: 50px;
	margin: 0 auto;
	position: fixed;
	bottom: 4px;
	left: 35%;
	z-index: 40001;
	display: none;
}

.alert {
	background: rgba(0, 0, 0, 0.35);
	width: 100%;
	height: 100%;
	position: fixed;
	top: 0;
	display: none;
	z-index: 9999;
}
</style>

</head>
<body>
	<div id="cart-page">
		<div class="edittop"></div>
		<div class="edithead">
			<div class="perheadpic" id="modPic">
				<div id="ImgBox" class="ImgBox">
					<c:if test="${not empty obj.user.imageUrl}">
						<img src="http://oss.kdfwifi.net/user_pic/${obj.user.imageUrl}"
							alt="" id="photo" />
					</c:if>
					<c:if test="${empty obj.user.imageUrl}">
						<img src alt="" id="photo" />
					</c:if>
				</div>
			</div>
		</div>



		<div class="editfliedate">
			<ul>
				<li id="editnamemodle"><span>昵称：</span>
				<p id="nick">${obj.user.userNickname}</p></li>
				<li id="setModel"><span>性别：</span>
					<p>
						<input type="text" name="" disabled="true" id="sex" value=""
							placeholder="选择您的性别" class="sexChose" />
					</p></li>
				<li><span>生日：</span>
				<p id="birthdateAdd">
						<input class="jcDate" placeholder="请输入您的生日" readonly="readonly"
							id="birthdate" value="${obj.user.birthdate}" />
					</p></li>
			</ul>
		</div>
		<div class="savefoot" onclick="saveData();">保存</div>
		<input type="hidden" id="editName" value="${obj.user.userName}">
		<input type="hidden" id="siteId" value="${obj.site.id}" name="name11">
		<div class="fillname">
			<div class="fillnamemoble"></div>
			<div class="fillnamewrap">
				<h2>填写昵称</h2>
				<div class="fillnameinput">
					<input type="text" name="hh" id="updatenick" maxlength="15" /><i
						class="iconfont icon-shibai"></i>
				</div>
				<div class="fillnamebutton clearfix">
					<a href="#" onclick="closeLayer('fillname')">取消</a><a href="#"
						onclick="closeLayer('fillname')" id="nickTure">确认</a>
				</div>
			</div>
		</div>
		<div class="fillsexwrap">
			<div class="fillnamemoble"></div>
			<div class="fillsexmoble">
				<dl>
					<dt>我的性别</dt>
					<dd class="sexman sex">男</dd>
					<dd class="sexwomen sex">女</dd>
				</dl>
			</div>
			<div class="fillsexmoble fillsexclose">
				<a href="#" onclick="closeLayer('fillsexwrap')">取消</a>
			</div>
		</div>
		<div class="fillPicwrap" id="fillwrap">
			<div class="fillnamemoble"></div>
			<div class="fillsexmoble">
				<div class="fillheadpic">
					<dl>
						<dd class="sexman fillphoto">
							<input type="file" accept="image/*" id="uploadImage"
								capture="camera" onchange="selectFileImage(this);" /> <span>相册</span>
						</dd>
					</dl>
				</div>
			</div>
			<div class="fillsexmoble fillsexclose">
				<a href="#" onclick="closeLayer('fillPicwrap')">取消</a>
			</div>
		</div>

		<div class="fillname1">
			<div class="fillnamemoble1"></div>
			<div class="fillnamewrap1">
				<h2>资料未完善</h2>
			</div>
		</div>
		<div class="fillnameInfo">
			<div class="fillnamemoble"></div>
			<div class="fillnamewrap" style="width: 180Px; height: 85px;">
				<h3 style="height: 50px; padding-top: 15px;">信息保存失败，请重新填写</h3>
				<hr>
				<div style="height: 10px; height: 30px;">
					<a
						style="font-size: 12px; text-align: center; width: 100%; height: 20px;"
						href="#" onclick="closeLayer('fillnameInfo')" id="nikelink">确认</a>
				</div>
			</div>
		</div>
		<!--预加载动画-->
		<div class="loading">
			<img src="${path}/img/loading.gif" class="loadPic" />
		</div>
		<div class="alert"></div>
		<script>
			;!function(){
			laydate({
			   elem: '#birthdate'
			})
			}();
			</script>
</body>

</html>