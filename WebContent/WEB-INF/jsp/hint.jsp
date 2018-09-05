<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pathjs" value="${ctx}/web_js" />
<c:set var="pathcss" value="${ctx}/web_css" />
<c:set var="statics" value="${ctx}/static" />
<!DOCTYPE HTML>
<html>
<head lang="zh-CN">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>充值完成</title>
<link type="text/css" href="${pathcss}/base.css" rel="stylesheet" />
<!-- <link rel="stylesheet"
	href="http://wifi.weixin.qq.com/resources/css/style-simple-follow.css" /> -->
<link rel="stylesheet" href="${pathcss}/style-simple-follow.css" />

<style type="text/css">
.chat_h {
	padding: 45px 0 30px 0;
	text-align: center;
}

.chat_h .going {
	color: #333333;
	font-size: 18px;
	margin: 25px 50px 0 50px;
	text-align: center;
	line-height: 1.6;
}

.chat_h .goes {
	color: #666666;
	font-size: 15px;
	margin-top: 15px;
}
.chat_h img{

	width: 55px;
}
.bar_progress {
	margin: 25px 30px 0 30px;
	background: #eeeeee;
	height: 7px;
	border-radius: 8px;
}

.bar_progress div {
	height: 7px;
	display: block;
	border-radius: 8px;
	width: 0;
}

.chat_txt {
	font-size: 12px;
	color: #e55d5d;
	margin: 15px 30px 25px 30px;
	text-align: left;
	line-height: 1.8;
}

.time {
	color: #666666;
	font-size: 14px;
	margin-top: 20%;
}
</style>
</head>

<body>
	<div class="chats">
		<div class="chat_h" id="phone">
			<img src="${statics}/images/fxwxbg.png" alt="" />
			<div class="going">充值查询中..</div>
			<div class="time">充值完成后，请重新连接wifi进行登录认证，以便恢复上网时长。</div>
			<input name="ordernum" value='${obj.ordernum}' type="hidden" id="ordernum">
		</div>
	</div>
</body>
<script type="text/javascript" src="${pathjs}/jquery.min.js"></script>
<script type="text/javascript">
	var orderNum = $("#ordernum").val();
	var checkurl = "${ctx}" + "/PROT/wechatPayOrderQuery";//会员包下单
	var s1 = setInterval(function(){ 
		$.ajax({
			type : "post",
			url : checkurl,
			async : false,
			data : {
				out_trade_no : orderNum
			},
			success : function(data) {
				console.log("查询订单结果："+data.return_code);
				if (data.return_code == "SUCCESS") {
					$(".going").html("充值成功！");
					window.clearTimeout(s1);
				}else{
					var aa = $(".going").html();
					if(aa == "充值查询中.."){
						$(".going").html("充值查询中...");
					}else{
						$(".going").html("充值查询中..");
					}
						
				}
			}
		}); }, 2000);
	
	//获取cookie
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
	
	function setCookie(name, value, liveMinutes) {  
		if (liveMinutes == undefined || liveMinutes == null) {
			liveMinutes = 60 * 2;
		}
		if (typeof (liveMinutes) != 'number') {
			liveMinutes = 60 * 2;//默认120分钟
		}
		var minutes = liveMinutes * 60 * 1000;
		var exp = new Date();
		exp.setTime(exp.getTime() + minutes + 8 * 3600 * 1000);
		//path=/表示全站有效，而不是当前页
		document.cookie = name + "=" + value + ";path=/;expires=" + exp.toUTCString();
	}
	
	function formatDuring(mss) {
	    var minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60));
	    var seconds = parseInt((mss % (1000 * 60)) / 1000);
	    return minutes + " 分钟 " + seconds + " 秒 ";
	}
	
	var s2 = setInterval(function(){ 
		var time1 = getCookie("time");
		var url = getCookie("ar3");
		if(time1 != ""){
			var time2 = new Date().getTime();
			if(time2 - time1 >= 5*60*1000){
				setCookie("time", "", 120);
				setCookie("ar2", "", 120);
				setCookie("ar3", "", 120);
				window.clearTimeout(s2);
				window.location.href = url;
			}else{
				var time3 = 5*60*1000 - (time2 - time1);
				$(".time").html("充值完成后，"+ formatDuring(time3) +"秒后，请重新连接wifi进行登录认证，以便恢复上网时长。");
			}
		}	
	}, 1000);
</script>
</html>
