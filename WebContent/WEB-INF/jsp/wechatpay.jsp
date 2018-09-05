<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<%@ page isELIgnored="false" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="statics" value="${ctx}/static" />
<c:set var="pathjs" value="${ctx}/web_js" />
<!DOCTYPE HTML>
<html>
<head lang="zh-CN">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>充值中心</title>
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" type="text/css" href="${statics}/css/bootstrap.min.css" />
<script type="text/javascript" src="${pathjs }/jquery.min.js"></script>
<script type="text/javascript" src="${pathjs }/validator.js"></script>
<script type="text/javascript">
	var ctx = "${ctx}";
</script>

<style type="text/css">
body {
	margin: 0;
	padding: 0;
	font-family: "Microsoft YaHei", Arial, Helvetica, sans-serif, "宋体";
}

.head_img {
	/* height: 200px; */
	
}

.img {
	width: 100%;
	/* height: 200px; */
}

.menubar {
	width: 100%;
	height: 45px;
	font-size: 16px;
	line-height: 45px;
	text-align: center;
}

.menubar div {
	width: 50%;
	float: left;
}

.on {
	transition: width 2s;
	border-bottom: 3px solid #34c1e6;
}

.info {
	margin-top: 30px;
}

.info_title {
	width: 80%;
	height: 34px;
	line-height: 34px;
	font-size: 18px;
	text-align: left;
	margin-left: 10%;
    color: #34c1e6;
}

.info_tel {
	width: 80%;
	height: 45px !important;
	line-height: 45px;
	font-size: 16px;
	text-align: left;
	padding-left: 5px;
	margin-bottom: 20px;
	margin-left: 10%;
	border-radius: 5px;
	border: 1px solid #34c1e6;
}

.info_tel:focus {
	height: 42px;
	outline: none;
	border: 1px solid #34c1e6;
}

.address_info {
	width: 80%;
	height: 44px;
	line-height: 44px;
	font-size: 16px;
	text-align: left;
	margin-left: 10%;
	border-radius: 5px;
	border: 1px solid #34c1e6;
}

.address_info:focus {
	height: 44px;
	outline: none;
	border: 1px solid #34c1e6;
}

.center_but {
	width: 80%;
	height: 45px;
	line-height: 45px;
	color: #fff;
	background-color: #34c1e6;
	text-align: center;
	margin-left: 10%;
	border-radius: 5px;
	border: 1px solid #34c1e6;
}

.center_but_disabled {
	background-color: #9e9e9e;
	border: 1px solid #9e9e9e;
}

.center_but:focus {
	outline: none;
	border: 1px solid #34c1e6;
}

.center_but:ACTIVE {
	background-color: #009fb3;
	-moz-box-shadow: 0px 0px 6px #333333;
	-webkit-box-shadow: 0px 0px 6px #333333;
	box-shadow: 0px 0px 6px #333333;
}

.tip {
	width: 80%;
	margin-top: 30px;
	margin-left: 10%;
	font-size: 14px;
	color: red;
	padding-left: 10px;
}

.tipp {
	width: 80%;
	margin-top: 30px;
	margin-left: 10%;
	font-size: 14px;
	color: red;
	padding-left: 10px;
}

.footer {
	font-size: 12px;
	text-align: center;
}

.tel_center {
	display: block;
	margin-bottom: 50px;
}

.wechat_center {
	display: none;
	margin-bottom: 50px;
}

.model_cover {z-index：20;
	position: fixed;
	top: 0;
	bottom: 0;
	left: 0;
	right: 0;
	opacity: 0.5;
	background-color: black;
	display: none;
}

.model {z-index：20;
	width: 80%;
	height: 230px;
	border-radius: 5px;
	position: fixed;
	top: 30%;
	left: 10%;
	background-color: #fff;
	display: none;
}

.model_head {
	width: 100%;
	height: 45px;
	line-height: 45px;
	text-align: center;
	font-size: 16px;
	color: black;
	font-weight: bold;
}

.center_box {
	padding-left: 10%;
	padding-top: 25px;
}

.center_number {
	
}

.center_number div {
	width: 80px;
	float: left;
	text-align: justify;
}

.center_site {
	margin-top: 10px;
}

.center_site div {
	width: 80px;
	float: left;
	text-align: justify;
}

.center_address {
	margin-top: 10px;
}

.center_address div {
	width: 80px;
	float: left;
	text-align: justify;
}

.model_center {
	width: 100%;
	height: 138px;
	border-bottom: 1px solid #dedede;
	border-top: 1px solid #dedede;
}

.model_footer {
	width: 100%;
	height: 45px !important;
	line-height: 45px !important;
	text-align: center;
}

.sure {
	font-size: 16px;
	width: 49%;
	float: left;
	color: #34c1e6;
	font-weight: 700;
	border-left: 1px solid #dedede;
}

.close {
	font-size: 16px;
	width: 49%;
	float: left;
	color: #000000;
	opacity: .5;
}
</style>
</head>
<body>
	<div class="main">
		<div class="head">
			<div class="head_img">
				<img class="img" alt="" src="${statics}/images/xunfei_wifi.png">
			</div>
		</div>
		<div class="menubar">
			<div id="tel_pay" class="on">手机号充值</div>
			<div id="wechat_pay" class="">微信号充值</div>
		</div>
		<div class="tel_center">
			<div class="info">
				<div class="info_title">手机号:</div>
				<input id="info_tel" class="info_tel" type="text" maxlength="11"
					placeholder="请输入手机号" onkeyup="isunm()">
			</div>
			<div class="address">
				<div class="info_title">选择上网场所:</div>
				<select id="address_tel" class="address_info">
					<option>请输入手机号获取上网场所</option>
					<!--<option>2</option>
					<option>3</option> -->
				</select>
			</div>
			<div class="tip"></div>
			<!-- <div class="center_but" >下一步
			</div> -->
			<input type="button" id="tel_but"
				class="center_but center_but_disabled" value="下一步"
				disabled="disabled">
		</div>
		<div class="wechat_center">
			<div class="info">
				<div class="info_title">微信昵称:</div>
				<input id="wechat_name" class="info_tel" type="text" disabled="disabled" value="暂无信息">
			</div>
			<div class="address">
				<div class="info_title">选择上网场所:</div>
				<select id="address_wechat" class="address_info">
					<!--<option>5</option>
					<option>6</option> -->
				</select>
			</div>
			<div class="tipp"></div>
			<!-- <div class="center_but">下一步
			</div> -->
			<input type="button" id="wechat_but"
				class="center_but center_but_disabled" value="下一步"
				disabled="disabled">
		</div>
		<!-- <div class="footer">武汉飞讯无限信息技术有限公司</div> -->
		<!-- 确认信息弹框 -->
		<div class="model_cover"></div>
		<div class="model">
			<div class="model_head">信息确认</div>
			<div class="model_center">
				<div class="center_box">
					<div class="center_number">
						<div>账户名：</div>
						<span>暂无数据</span>
					</div>
					<div class="center_address">
						<div>上网地址：</div>
						<span>暂无数据</span>
					</div>
					<div class="center_site">
						<div>上网场所：</div>
						<span>暂无数据</span>
					</div>
				</div>
			</div>
			<div class="model_footer">
				<div class="close" id="close">关闭</div>
				<div class="sure" id="sure">确定</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
var openid;
var userinfo;
var list;
$(function() {
	var htmlstr;
	var code_val;
	var url = window.location.search; //获取url中"?"符后的字串
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
	    var str = url.substr(1);
	    strs = str.split("&");
	    for(var i = 0; i < strs.length; i ++) {
	    	if(strs[i].split("=")[0] == "code"){
	    		code_val = strs[i].split("=")[1];
	    	}
	    }
	}
	
	$.ajax({
		type : "post",
		url : ctx + "/PROT/wechatGetUserInfo",
		async : false,
		data : {"code":code_val},
		dataType : "json",
		success : function(data) {
			console.log(data);
			var access_token = data.access_token;
			openid = data.openid;
			userinfo = data.userinfo;
			list = data.list;
			$("#wechat_name").val( userinfo.nickname);
				$("#address_wechat").html("");
				if(list!=null){
					$.each(list,function(index, obj) {
							htmlstr = '<option  value='+obj.address+','+obj.id+'>'
									+ obj.site_name
									+ '</option>';
							$("#address_wechat").append(htmlstr);
					});
					$("#wechat_but").attr("disabled", false)
							.removeClass("center_but_disabled");
				}else{
					htmlstr = '<option>上网场所暂无记录!</option>';
					$("#address_wechat").html(htmlstr);
					$("#wechat_but").attr("disabled", true).addClass(
							"center_but_disabled"); 
				}
		}
	});
});
	
 	var vtype = 2;
	$(".tel_center").css("display","block");

	var tel = $("#info_tel");
	var address = $("#address_tel");
	var weaddress = $("#address_wechat");
	var siteId = "";
	// 手机号页面 js 
	
/* 	function verifyMobile(val) {
		
		var regex = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1})|(19[0-9]{1}))+\d{8})$/g;
		if (!regex.test(val)) {
			return false;
		}
		return true;
		
	} */
	function isunm() {
		var phone = tel.val();
		if (phone == "") {
			$(".tip").html("手机号不能为空");
			$("#tel_but").attr("disabled", true)
					.addClass("center_but_disabled");
			htmlstr = '<option>请输入手机号获取上网场所</option>';
			$("#address_tel").html(htmlstr);
			return;
		} else if (!verifyMobile(phone)) {
			$(".tip").html("手机号格式不正确");
			$("#tel_but").attr("disabled", true)
					.addClass("center_but_disabled");
			htmlstr = '<option>请输入手机号获取上网场所</option>';
			$("#address_tel").html(htmlstr);
			return;
			
		} else {
			var htmlstr = "";
			$(".tip").html("");
			$.ajax({
				type : "post",
				url : ctx + "/PROT/cloudSiteInfo",
				data : {"userName" : phone},
				dataType : "json",
				success : function(data) {
					if (data.code == 200) {
						$("#address_tel").html("");
						$.each(
							data.cloudSites,
							function(index, obj) {
								$(".tip").html("");
								htmlstr = '<option  value='+obj.address+','+obj.id+'>'
										+ obj.site_name
										+ '</option>';
								$("#address_tel").append(
										htmlstr);
						});
						$("#tel_but").attr("disabled", false)
								.removeClass("center_but_disabled");
					} else {
						$(".tip").html("手机号暂无记录");
						htmlstr = '<option>上网场所暂无记录</option>';
						$("#address_tel").html(htmlstr);
						$("#tel_but").attr("disabled", true).addClass(
								"center_but_disabled");
					}
				}
			})
		};

		//手机号  下一步 
		$("#tel_but").click(function() {
				vtype = 2;
				$(".model_cover").css("display", "block");
				$(".model").css("display", "block");
				var tel = $("#info_tel").val();
				var site = $("#address_tel option:selected").val();
				var sitename = site.split(",")[0];
				siteId = site.split(",")[1];
				var address = $("#address_tel option:selected").text();
				$(".center_number>span").text(tel);
				$(".center_address>span").text(sitename);
				$(".center_site>span").text(address);
		})
	}
	
	$("#close").click(function() {
		$(".model_cover").css("display", "none");
		$(".model").css("display", "none");

	})

	$("#sure").click(function() {
		if(vtype == 1){
			var telphone = list.openid;
		}
		if(vtype == 2){
			var telphone = $("#info_tel").val();
		}
			
			$(".model_cover").css("display", "none");
			$(".model").css("display", "none");
			window.location.href = ctx
					+ "/PROT/wechatchoiceTc?openid="+openid+"&username=" + telphone
					+ "&siteId=" + siteId + "&tag=" + vtype + "";
	});
	
	//微信号页面js
	$("#wechat_but").click(function() {
				vtype = 1;
				$(".model_cover").css("display", "block");
				$(".model").css("display", "block");
				var wechat = $("#wechat_name").val();
				var site = $("#address_wechat option:selected").val();
				var sitename = site.split(",")[0];
				siteId = site.split(",")[1];
				var address = $("#address_wechat option:selected").text();
				$(".center_number>span").text(wechat);
				$(".center_address>span").text(sitename);
				$(".center_site>span").text(address);
			
		})
	
	weaddress.change(function() {
		if (weaddress.val() == "") {
			$(".tipp").html("请选择上网场所");
			$("#wechat_but").attr("disabled", true).addClass(
					"center_but_disabled");
		} else {
			$(".tipp").html("");
			$("#wechat_but").attr("disabled", false).removeClass(
					"center_but_disabled");
		}
	})

	// 导航栏控制 
	$("#tel_pay").click(function() {
		$("#wechat_pay").removeClass("on");
		$("#tel_pay").addClass("on");
		$(".tel_center").css("display", "block");
		$(".wechat_center").css("display", "none");
		vtype = 2;
	});

	$("#wechat_pay").click(function() {
		$("#tel_pay").removeClass("on");
		$("#wechat_pay").addClass("on");
		$(".wechat_center").css("display", "block");
		$(".tel_center").css("display", "none");
		vtype = 1;
	})
</script>
</html>