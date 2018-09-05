<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />


<!DOCTYPE html>
<html>
<head>
<title>Portal认证</title>
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<meta charset="utf-8">

<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/Wireless_Nanshan.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/portal.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/input.css" />
<script type="text/javascript">
	var base = "${ctx}";
</script>
<script type="text/javascript" src="${ctx}/partol/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/static/layer/layer/layer.js"></script>
<script type="text/javascript"
	src="${ctx}/static/js/Wireless_Nanshan.js"></script>
<script type="text/javascript" src="${ctx}/static/js/validator.js"></script>
<script type="text/javascript" src="${ctx}/static/js/custom_input.js"></script>
<script type="text/javascript" src="${ctx}/dist/js/auth.js"></script>
<script type="text/javascript" src="${ctx}/dist/js/encode.js"></script>
<script type="text/javascript" src="${ctx}/partol/js/RSA.js"></script>
<script type="text/javascript" src="${ctx}/partol/js/BigInt.js"></script>
<script type="text/javascript" src="${ctx}/partol/js/Barrett.js"></script>
<script type="text/javascript" src="${ctx}/partol/js/jsencrypt.min.js"></script>
<script type="text/javascript">
	$(function() {
		var allowSendSms = true;
		var user = "${obj.map.user}";
		var username = "${obj.map.user.userName}";
		var isold = false;
		
		$("#phone").bind('input porpertychange',function(){
			if($("#phone").val() == username){
				$("#password").val("******");
				$("#BtnLogin").val("检测到您是老用户，可直接登录。")
			}else{
				$("input[name='password']").val("");
				$("#BtnLogin").val("登录 Login")
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
			/* $("#BtnGetPwd").css("background", "#259cd7"); */
			$("#BtnGetPwd").html("发送成功<br />(" + againSendSmsTime + "秒)");

			var remainTime = againSendSmsTime;
			var timer = window
					.setInterval(
							function() {
								remainTime--;
								$("#BtnGetPwd").html("发送成功<br />("
												+ remainTime
												+ "秒)");
								if (remainTime <= 0) {
									window.clearInterval(timer);
									if(!allowSendSms){
										allowSendSms = true;
									}
									$("#BtnGetPwd").html('获取密码<br /><span>Get&nbsp;Password</span>');
									/* $("#BtnGetPwd").css("background", "url(/UnitePortal/static/images/Get_pass_fb.png) 0% 0% / 100% 100% no-repeat"); */
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
				return false;
			}
			if (!verifyMobile(mobile)) {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("手机号码格式不对")
				return false;
			}

			var password = $("#password").val();
			if ($.trim(password) == "") {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("请输入密码")
				return false;
			}

			if (!$("#Remember").is(":checked")) {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").text("您还没有同意用户协议")
				return false;
			}
			
			$(".ZindexTip,.loading_img").show();
			if(!isold){
				new_login(mobile, password);
				
			}else{
				oldLogin(mobile, "${obj.map.user.passWord}", 5);
			}
		});

		$("#closeBtn").click(function() {
			$(".ZindexTip,.ZindexTipAlert").hide();
		});

		$("#BtnLogin2").click(function() {
			accountLogin("13113026420", "9548");
		}); 

		var btnDownloadText = "立即下载";
		var btnDownloadPrompt = "正在下载……";
		$("#BtnDownloadApp").click(
				function() {
					if ($(this).text() == btnDownloadPrompt) {
						return;
					}
					$(this).text(btnDownloadPrompt);
					$.post("/wxnsportal/nsydportal/getDownloadAppURL?t="
							+ new Date().getTime(), {}, function(data) {
						$("#BtnDownloadApp").text(btnDownloadText);
						if (data == "") {
							alert("亲，APP客户端正在为您努力开发中……");
							return;
						}
						window.location.href = data.appUrl;
					});
				});

	})
</script>
<style type="text/css">
.put_title>div {
	width: 100%;
	font-size: 1.2rem;
	text-align: center;
}
</style>
</head>
<body class="MaxZindex">
	<div>
		<div class="nav clearfix">
			<div class="navLeft">
				<div class="navLeftImg">
					<img src="${ctx}/static/images/navLogo.gif" />
				</div>
				<div class="navLeftName">智慧深圳</div>
			</div>
			<div class="navRight">
				<div class="navRightTop">
					深圳<span style="color: #f0e02d;">公共</span>无线局域网
				</div>
				<div class="navRightBottom">Shenzhen&nbsp;&nbsp;Public&nbsp;WLAN</div>
			</div>
		</div>
		<!--登录框-->
		<div class="login_box">
			<div class="login">

				<div class="login_put">
					<div class="put_title"
						style="letter-spacing: 0; text-indent: 0; width: 21%;">
						<div style="font-size: 1.4rem">手机号：</div>
						<div style="height: 17px; line-height: 14px;">Telephone</div>
					</div>
					<input type="text" id="phone" name="" class="phone_inputs"
						style="width: 78%;" />
				</div>

				<div class="pass_put">
					<div class="pass_left">
						<div class="put_title"
							style="width: 32%; letter-spacing: 0; text-indent: 0;">
							<div style="font-size: 1.4rem">密 &nbsp; 码：</div>
							<div style="height: 17px; line-height: 14px;">Password</div>
						</div>
						<input style="width: 67%;" type="password" id="password" name=""
							class="phone_inputs" value="" style="" maxlength="8" />
					</div>
					<div class="pass_right">
						<p id="BtnGetPwd" class="logoin_buts" >
							获取密码<br /><span>Get&nbsp;Password</span>
						</p>
					</div>
					<div class="clear"></div>
				</div>



				<br />
				<div class="clear"></div>

				<div class="input_box left_input_box">
					<input type="checkbox" name="Remember" id="Remember"
						checked="checked" value="Remember" style="display: none;">
					<label for="Remember"></label><span>同意遵守</span><a
						href="${ctx}/PROT/userAgreement">用户协议/Agree&nbsp;To&nbsp;User&nbsp;Agreement</a>
				</div>

				<!--<button id="BtnLogin" class="login_btn">登录&nbsp;Login</button>-->
				<input id="BtnLogin" class="login_btn" >

			</div>
		</div>
		<input type="hidden" id="telphone" value="${obj.map.user.userName}" />
		<input type="hidden" id="password" value="${obj.map.user.passWord}" />
		<input type="hidden" id="siteType" value="${obj.map.type}" /> <input
			type="hidden" id="site" value="${obj.map.site}" /> <input
			type="hidden" id="siteId" value="${obj.map.site.id }" /> <input
			type="hidden" id="mac" value="${obj.map.type.allMac}"> <input
			type="hidden" id="res" value="1">



		<!--下载按钮
		<div class="down_bj"></div>
		<div class="down">
			<span>下载APP，体验一键上网
				<button id="BtnDownloadApp" class="down_btn">立即下载</button>
			</span>
		</div>-->


		<!--网络临时维护中，给您带来不便敬请谅解
		
		<div class="down">
			<span style="color: red;font-size: 15px;font-weight: bolder;">网络临时维护中，给您带来不便敬请谅解</span>  
	    </div>-->

		<!--底部-->
		<div class="footer_about">
			<ul>
				<li><a href="${ctx}/PROT/common_problem">常见问题<br />Common&nbsp;&nbsp;problem
				</a></li>
				<li><a href="${ctx}/PROT/instruction">使用说明<br />Instructions
				</a></li>
				<li><a href="${ctx}/PROT/feedback">意见反馈<br />Feedback
				</a></li>
			</ul>
		</div>

	</div>
	<!--登录框提示语-->
	<div class="ZindexTip"></div>
	<div class="ZindexTipAlert">
		<div class="center_bg">
			<img class="center_bgImg" src="${ctx}/static/images/logotip.png" />
			<img class="center_bgImg2" style="width: 90%; margin-left: 5%;"
				src="${ctx}/static/images/alertbg.png" />
			<!-- 这里面通过js控制文本切换提示语 -->
			<div id="alertText">手机号码格式不对</div>

		</div>
		<div id="closeBtn">知道了</div>
	</div>
</body>
</html>