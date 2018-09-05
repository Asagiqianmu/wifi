<!DOCTYPE html>
<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.fxwx.util.OssSchoolManage"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/partol" />
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
			;! function(doc, win) {
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
			}(document, window);
		</script>
<title>portal页面 注册页面 输入手机号</title>
<link rel="stylesheet" type="text/css" href="${path }/css/public.css" />
<link rel="stylesheet" type="text/css" href="${path }/css/reg2.css" />
</head>
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
					<div class="newlogin">
						<h1>输入手机号即可轻松上网</h1>
						<div class="phoneinput">
							<input type="tel" class="tel" id="Mobile" onkeyup="FillMobile()"
								maxlength="13" placeholder="请输入手机号"
								onfocus="this.placeholder=''"
								onclick="this.value=this.value.replace(/^[\d\s]+$/,'')" />
						</div>
					</div>
					<div class="loginpassword" style="display: none;">
						<h1>输入验证码</h1>
						<div class="inputphone">
							<span id="phone"></span>
							<p id="minute">
								<i id="mm">120</i>秒后重发
							</p>
							<a href="javascript:void(0);" id="retext">点击重新发送</a>
						</div>
						<div class="validate clearfix">
							<input type="text" maxlength='4' id="valiHtml"
								onkeyup="this.value=this.value.replace(/\D/g,'')" />
							<div class="validate-text clearfix">
								<i class='active'></i> <i class=''></i> <i class=''></i> <i
									class=''></i>
							</div>
							<p id="fuckCode">验证码不正确，请重新输入</p>
						</div>
					</div>
					<div class="loginnewpassword" style="display: none;">
						<h1>设置新密码</h1>
						<p class="note">此密码将作为您上网的唯一密码，请牢记此密码</p>
						<div class="phoneinput password">
							<img class="passwordpic" src="${path }/img/open.png"
								src1="${path }/img/open.png" src2="${path }/img/lock.png"></img>
							<input type="password" id="password1" class="pass" maxlength="10"
								onkeyup="value=value.replace(/[^a-zA-Z0-9]/ig,'')"
								placeholder="设置新密码" onfocus="this.placeholder=''"
								onblur="this.placeholder='设置新密码'" />
							<div class="passnote">密码长度不能小于6位</div>
						</div>
					</div>
					<div class="loginword" style="display: none;">
						<h1>输入登录密码</h1>
						<div class="loginwordphone">
							<span id="phone"></span>
						</div>
						<div class="phoneinput password">
							<img class="passwordpic" src="${path }/img/open.png"
								src1="${path }/img/open.png" src2="${path }/img/lock.png"></img>
							<input type="password" id="password2" class="pass" maxlength="10"
								onkeyup="value=value.replace(/[^a-zA-Z0-9]/ig,'')"
								placeholder="请输入密码" onfocus="this.placeholder=''"
								onblur="this.placeholder='请输入密码'" />
							<div class="passnote"></div>
							<a href="#" id="fogpicpass">忘记密码</a>
						</div>
					</div>

					<div class="loginpicpassword" style="display: none;">
						<h1>输入图片验证码</h1>
						<div class="pic-validation">
							<input type="hidden" id="code2" value="" />
							<div id="vCode2"
								style="width: 2.2rem; height: 0.7rem; border: 1px solid #ccc; display: inline-block; line-height: .7rem;"></div>
						</div>
						<div class="validate clearfix">
							<input type="text" maxlength='4' id="inputCode"
								onkeyup="this.value=this.value.replace(/[^\w\.\/]/ig,'')" />
							<div class="validate-text clearfix" id="valiId">
								<i class='active'></i> <i class=''></i> <i class=''></i> <i
									class=''></i>
							</div>
							<p id="funckcode">验证码不正确，请重新输入</p>
						</div>
					</div>
					<div class="aginlogin" style="display: none;">
						<h1 value="${obj.map.site.id }">欢迎使用${obj.map.site.site_name }
							Wi-Fi</h1>
						<div class="phoneinput">
							<input type="tel" id="tel" maxlength="11"
								onkeyup="this.value=this.value.replace(/\D/g,'')" value="" />
						</div>
					</div>
					<div class="nextphone">
						<input type="button" value="下一步" id="next" disabled />
						<!--预加载动画-->
						<div class="loading">
							<img src="${path }/img/loading.gif" class="loadPic" />
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
	<div class="copyright">中国移动通信有限公司</div>
	<%-- <c:if test="${obj.map.site.adminer==null}">
			<div class="copyright">服务支持：${obj.map.site.adminer} | 飞讯无限提供计费服务</div>	
		</c:if>
		<c:if test="${obj.map.site.adminer!=null}">
			<div class="copyright">服务支持：${obj.map.site.adminer} | 飞讯无限提供计费服务</div>	
		</c:if>  --%>
	<input type="hidden" id="siteType" value="${obj.map.type}">
	<input type="hidden" id="site" value="${obj.map.site}">
	<input type="hidden" id="mac" value="${obj.mac}">
	<input type="hidden" id="ip" value="${obj.map.type.ip}">
	<input type="hidden" id="mac1" value="${obj.mac}">
	<script type="text/javascript" src="${path }/js/jquery.min.js"></script>
	<script type="text/javascript" src="${path }/js/banner.js"></script>
	<script type="text/javascript" src="${path }/js/reg2.js"></script>
	<script type="text/javascript" src="${path }/js/code.js"></script>
	<script type="text/javascript" src="${path }/js/jsencrypt.min.js"></script>
	<script type="text/javascript" src="${path }/js/RSA.js"></script>
	<script type="text/javascript" src="${path }/js/BigInt.js"></script>
	<script type="text/javascript" src="${path }/js/Barrett.js"></script>
	<script type="text/javascript"
		src="${path }/layer.mobile-v2.0/layer_mobile/layer.js"></script>
</body>
<script>
    onload = function (){
            var container2 = document.getElementById("vCode2");
            var code2 = new vCode(container2, {
                len: 4,
                bgColor: "#444444",
                colors: [
                    "#DDDDDD",
                    "#DDFF77",
                    "#77DDFF",
                    "#99BBFF",
                    //"#7700BB", 
                    "#EEEE00"
                ]
            });
        }
    </script>
<script type="text/javascript">
    var res="${obj.map.auth}",
		message="${obj.map.msg}",
		code="${obj.map.code}";
    	if(res=="failed"&&message!=null&&message!=""){
    		if(code==200) {
		    	prompt(message);
    		}else if(code==201) {
		    	prompt(message);
			}else if(code==203){
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