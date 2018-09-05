<!DOCTYPE html>
<%@ page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
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
</script>
<script type="text/javascript" src="${path }/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx }/web_js/banner.js"></script>
<script type="text/javascript" src="${ctx }/web_js/config.js"></script>
<script type="text/javascript" src="${path }/js/jsencrypt.min.js"></script>
<script type="text/javascript" src="${path }/js/RSA.js"></script>
<script type="text/javascript" src="${path }/js/BigInt.js"></script>
<script type="text/javascript" src="${path }/js/Barrett.js"></script>
<script type="text/javascript" src="${ctx }/web_js/auth.js"></script>
<script type="text/javascript" src="${ctx }/web_js/encode.js"></script>
<script type="text/javascript" src="${path }/layer.mobile-v2.0/layer_mobile/layer.js"></script>
<script type="text/javascript" src="${ctx }/static/layer/layer/layer.js"></script>
<script type="text/javascript" src="${ctx }/web_js/validator.js"></script>
<script type="text/javascript" src="${ctx }/web_js/custom_input.js"></script>
<title>统一portal认证登录</title>
<link rel="stylesheet" type="text/css" href="${ctx }/web_css/public.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/web_css/index.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/web_css/portal.css" />
<script type="text/javascript">
	var city;
	var cid = 0;
	var allowSendSms = true;
	var note = true;
	
	var mobileHeight=window.innerHeight;
	console.log(mobileHeight);
	$(window).resize(function(){
	        if($(document).height() < mobileHeight){
	        $(".footer_about").css("display","none");
	    }else{
	        $(".footer_about").css("display","block");
	    } 
    });
	
	function aa(){
		if (note) {
			$("#BtnGetPwd").css("background", "url(${ctx }/static/images/Get_pass.png) 0% 0% / 100% 100% no-repeat");
		}
	}

	function bb(){
		if (note) {
			$("#BtnGetPwd").css("background", "url(${ctx }/static/images/Get_pass_fb.png) 0% 0% / 100% 100% no-repeat")
		} 
	}
		
	var nasid;
	$(function() {
		var user = "${obj.map.user}";
		var username = "${obj.map.user.userName}";
		nasid="${obj.nasid}";
		var isold = false;
		
		$("#phone").bind('input porpertychange',function(){
			if($("#phone").val() == username && username!=""){
				$("#password").val("******");
				$("#BtnLogin").val("检测到您是老用户，可直接登录。")
			}else{
				$("input[name='password']").val("");
				$("#BtnLogin").val("登录 Login");
			}
		}); 
		
		if(user != null && user != ""){
			$("#phone").val(username);
			$("#password").val("******");
			$("#BtnLogin").val("检测到您是老用户，可直接登录。")
			isold = true;
		}
		$('input').customInput();
		$('.toggle').each(function() {
			$('div:first', this).addClass('first');
			$('div:last', this).addClass('last');
		});
		
		$("#BtnGetPwd").click(function() {
			if (!allowSendSms) {
				return;
			}
			var mobile = $("#phone").val();
			if (mobile == "") {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("请先输入手机号码")
				return false;
			}
			if (!verifyMobile(mobile)) {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("手机号码格式不对");
				$("input[name='phone']").val("");
				$("input[name='password']").val("");
				
				return false;
			}
			
			$('#BtnGetPwd').attr('disabled',"true");
		 	var againSendSmsTime = 60;
			allowSendSms = false;
			note = false;
			$("#BtnGetPwd").css("background", "url(${ctx}/static/images/Get_pass_bg.png) 0% 0% / 100% 100% no-repeat");
			$("#BtnGetPwd").html('<div style="height:100%; line-height:100%;margin-top:18%;margin-left:0px;white-space:nowrap">发送成功('+againSendSmsTime+ '秒)<div>');
			var remainTime = againSendSmsTime;
			var timer = window.setInterval(
							function() {
								remainTime--;
								$("#BtnGetPwd").html('<div style="height:100%; line-height:100%;margin-top:18%;margin-left:0px;white-space:nowrap">发送中('+remainTime+ '秒)<div>');
								if (remainTime <= 0) {
									window.clearInterval(timer);
									if(!allowSendSms){
										allowSendSms = true;
									}
									note = true;
									$("#BtnGetPwd").html('');
									$("#BtnGetPwd").css("background", "url(${ctx}/static/images/Get_pass_fb.png) 0% 0% / 100% 100% no-repeat");
									$('#BtnGetPwd').removeAttr("disabled");
								}
							}, 1000); 
			   sendSMS(mobile);
		});

		$("#BtnLogin").click(function() {
			var mobile = $("#phone").val();
			if (mobile == "") {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("请先输入手机号码")
				$("input[name='password']").val("");
				return false;
			}
			if (!verifyMobile(mobile)) {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("手机号码格式不对")
				$("input[name='phone']").val("");
				$("input[name='password']").val("");
				return false;
			}
			var password = $("#password").val();
			if ($.trim(password) == "") {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("请输入密码")
				$("input[name='password']").val("");
				return false;
			}
             
			if (!$("#Remember").is(":checked")) {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("您还没有同意用户协议")
				return false;
			}
			
			$(".ZindexTip").show();
			if(!isold){
				new_login(mobile, password);
			}else{
				oldLogin(mobile, "${obj.map.user.passWord}", 5);
			}
		/* 	 $.ajax({
				type : "post",
				url : base + "/PROT/useDateIsExpire",  
				data : {
					"siteId":36,
					"userName":"13284808060",
					"pwd":"12345678",
				},
				dataType : "json",
				success : function(data) {
					if(data.result==202){
						window.location.href = base + "/PROT/";
					}else if(data.result=200){
						alert("success");
					}
				}
			});  */
		});

		$("#closeBtn").click(function() {
			$(".ZindexTip,.ZindexTipAlert").hide();
		});
		
	});
	
	$(function() {
		$("#pass_BtnLogin").click(function() {
			var mobile = $("#pass_phone").val();
			if (mobile == "") {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("请先输入手机号码")
				$("input[name='pass_password']").val("");
				return false;
			}
			if (!verifyMobile(mobile)) {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("手机号码格式不对")
				$("input[name='pass_phone']").val("");
				$("input[name='pass_password']").val("");
				return false;
			}
			var password = $("#pass_password").val();
			if ($.trim(password) == "") {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("请输入密码")
				$("input[name='pass_password']").val("");
				return false;
			}
             
			if (!$("#pass_Remember").is(":checked")) {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("您还没有同意用户协议")
				return false;
			}
		});
		
	});
</script>
</head>
<body>
	<div class="main">
		<div id="banner">
			<div id="banner_list">
				<c:if test="${obj.map.site.bannerUrl==''}">
					<li><img src="${ctx }/school_pic/background1.jpg" width="100%" height="100%" /></li>
					<li><img src="${ctx }/school_pic/background2.jpg" width="100%" height="100%" /></li>
					<li><img src="${ctx }/school_pic/background3.jpg" width="100%" height="100%" /></li>
				</c:if>
				<c:if test="${obj.map.site.bannerUrl!=''}">
					<c:forEach items="${obj.map.site.bannerUrl.split(':')}" var="p">
						<li><img src="${ctx}/${p}" width="100%" height="100%" /></li>
					</c:forEach>
				</c:if>
			</div>
		</div>
		<div class="loginwrap smslogin">
			<div class="login">
					<h1>手机号登录即可轻松上网</h1>
					<div class="login_put">
						
						<div class="put_heard" ";>
						<div>手&nbsp;&nbsp;&nbsp;机&nbsp;&nbsp;&nbsp;号</div>
						<div>Telephone</div>
						
						</div>	
							
							<!-- <div style="height: 17px; line-height: 18px;">Telephone&nbsp;</div> -->
						<!-- <div class="put_title"style="letter-spacing: 0; text-indent: 0; width: 34%;">
							<div style="font-size: 0.1rem;height: 17px; line-height: 18px;">手机 号/Telephone</div>
							<div style="height: 17px; line-height: 18px;">Telephone&nbsp;</div>
						</div> -->
						
						<input type="text" id="phone" name="phone" class="phone_inputs1" maxlength="11" />
					</div>

					<div class="pass_put">
						<div class="pass_left" style="z-index:2">
							<div class="pass_title" >
								<div>密&nbsp;&nbsp;&nbsp;码</div>
								<div>Password</div>
							</div>
							<div class="pass_put_left">	
								<input type="password" id="password" name="password"
								class="phone_inputs2" maxlength="8" style="z-index:-2" />
								<button  type="button" class="get_pass" id="BtnGetPwd" style="z-index:-2; background:url('${ctx}/static/images/Get_pass_fb.png') no-repeat top;background-size: 100% 100%;"
								 	ontouchstart="aa()" ontouchend="bb()" >
								</button>
							</div>
						</div>
					</div>
					<div class="clear"></div>
					<div class="input_box left_input_box" id="ua">
						<input type="checkbox" name="Remember" id="Remember"
							checked="checked" value="Remember" /> <label for="Remember"></label>
						<span>同意遵守</span><a href="${ctx}/PROT/userAgreement">用户协议/Agree&nbsp;To&nbsp;User&nbsp;Agreement</a>
					</div>
					<div class="tip"></div>
					<div><input type="button" id="BtnLogin" class="login_btn" value="登录&nbsp;Login " /></div>
					<div id="pass_login" class="pass_login_but">密码登录>></div>
					<!--<div>
						<input type="button" id="BtnLogin" class="login_btn" value="登录&nbsp;Login " 
						  ontouchstart="style.background='-webkit-linear-gradient(top,#38A5DA,#7DC9ED)'"
						ontouchend="style.background='-webkit-linear-gradient(top,#7DC9ED,#38A5DA)'" />
					</div>--> 
			</div>
		</div>
		
		<div class="loginwrap passlogin">
			<div class="login">
					<h1>用户登录</h1>
					<div class="login_put">
						<div class="put_heard" ";>
						<div>手&nbsp;&nbsp;&nbsp;机&nbsp;&nbsp;&nbsp;号</div>
						<div>Telephone</div>
						</div>	
						<input type="text" id="pass_phone" name="pass_phone" class="phone_inputs1" maxlength="11" />
					</div>

					<div class="pass_put">
						<div class="pass_left" style="z-index:2">
							<div class="pass_title" >
								<div>密&nbsp;&nbsp;&nbsp;码</div>
								<div>Password</div>
							</div>
							<div class="pass_put_left">	
								<input type="password" id="pass_password" name="pass_password"
								class="phone_inputs2" maxlength="8" style="z-index:-2" />
							</div>
						</div>
					</div>
					<div class="clear"></div>
					<div class="input_box left_input_box" id="ua">
						<input type="checkbox" name="pass_Remember" id="pass_Remember"
							checked="checked" value="pass_Remember" /> <label for="pass_Remember"></label>
						<span>同意遵守</span><a href="${ctx}/PROT/userAgreement">用户协议/Agree&nbsp;To&nbsp;User&nbsp;Agreement</a>
					</div>
					<div class="tip"></div>
					<div><input type="button" id="pass_BtnLogin" class="login_btn" value="登录&nbsp;Login " /></div>
					<div id="sms_login" class="sms_login_but"><<短信登录</div>
			</div>
		</div>
		
	</div>
	<input type="hidden" id="telphone" value="${obj.map.user.userName}" />
	<input type="hidden" id="password" value="${obj.map.user.passWord}" />
	<input type="hidden" id="siteType" value="${obj.map.type}" />
	<input type="hidden" id="site" value="${obj.map.site}" />
	<input type="hidden" id="siteId" value="${obj.map.site.id }" />
	<%-- <input type="hidden" id="mac" value="${obj.map.type.allMac}"> --%>
	<input type="hidden" id="res" value="1">
	<%-- <c:if test="${obj.map.site.adminer==null}">
		<div class="copyright">飞讯无限提供服务支持：${obj.map.site.adminer}</div>
	</c:if>
	<c:if test="${obj.map.site.adminer!=null}">
		<div class="copyright">飞讯无限提供服务支持：${obj.map.site.adminer}</div>
	</c:if> --%>
	<!--底部-->
	<div class="footer_about">
		<ul>
			<li><a id="cp" href="${ctx}/PROT/common_problem">常见问题<br />&nbsp;Common&nbsp;Problem
			</a></li><!-- common_problem -->
			<li><a id="in" href="${ctx}/PROT/instruction">使用说明<br />Instructions
			</a></li>
			<li><a href="${ctx}/PROT/feedback">意见反馈<br />Feedback
			</a></li>
		</ul>
	</div>

	<!--登录框提示语-->
	<div class="ZindexTip"></div>
	<div class="ZindexTipAlert">
		<div class="center_bg">
			<img class="center_bgImg" src="${ctx}/static/images/logotip.png" />
			<img class="center_bgImg2" style="width: 90%; margin-left: 5%;"
				src="${ctx}/static/images/alertbg.png" />>
			<div id="alertText">手机号码格式不对</div>

		</div>
		<div id="closeBtn">知 道 了</div>
	</div>
	<div class="loading_img">
		<img class="gif_img" src="${ctx}/static/images/loading_img.gif" />
	</div>
</body>
<script type="text/javascript">
	var address = "${obj.map.site.address}";
	var s = address.indexOf("省"); 
	var c = address.indexOf("市"); 
	city = address.substring(s+1,c+1);
	if(city != "深圳市"){
		$("#ua>a").attr("href","${ctx}/PROT/userAgreementA");
		$("#cp").attr("href","${ctx}/PROT/common_problemA");
		$("#in").attr("href","${ctx}/PROT/instructionA");
		cid = 1;
	}
	
	var res = "${obj.map.auth}", message = "${obj.map.msg}", code = "${obj.map.code}";
	if (res == "failed" && message != null && message != "") {
		if (code == 200) {
			prompt(message);
		} else if (code == 201) {
			prompt(message);
		} else if (code == 203) {
			prompt(message);
		} else if (code == 205) {
			prompt(message);
		} else if (code == 206) {
			prompt(message);
		} else if (code == 208) {
			prompt(message);
		} else if (code == 304) {
			//账号已登录
			prompt(message);
		} else if (code == 306) {
			//修改ip失败
			prompt(message);
		}
		message = "";
		res = "";
	}
	function closeLayer() {
		$(".fillname").hide();
	}
	
	$("#pass_login").click( function(){
		$(".passlogin").show();
		$(".smslogin").hide();
		
	})
	$("#sms_login").click( function(){
		$(".passlogin").hide();
		$(".smslogin").show();
		
	})
	
	
</script>

</html>