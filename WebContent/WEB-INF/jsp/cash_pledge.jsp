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
<title>支付押金</title>
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<%-- <link rel="stylesheet" type="text/css" href="${pathcss}/public.css" /> --%>
<link rel="stylesheet" type="text/css" href="${pathcss }/reg.css" />
<script type="text/javascript" src="${pathjs}/jquery.min.js"></script>
<script src="http://pv.sohu.com/cityjson"></script>
<style type="text/css">
body {
	padding: 0px;
	margin: 0px;
}

.shop_info {
	font-size: 15px;
	color: #777777;
}

.shops_TopNames {
	padding: 12px 0px;
	color: #333333;
	font-size: 18px;
	text-align: center;
}

.intro {
	font-size: 14px;
	height: 100%;
	width: 80%;
	margin: 20px auto;
	/* list-style: none; */
	border: 1px solid #9bb8d0;
	padding: 12px;
	border-style: dashed;
	overflow: hidden;
	text-overflow: ellipsis;
}

.item {
	margin-top: 5px;
}

.choice_list {
	padding-top: 20px;
	padding-bottom: 3.5rem
}

.list {
	border-radius: 10px;
	height: 3.5rem;
	line-height: 3.5rem;
	width: 80%;
	margin: auto;
	color: #000;
	font-size: 16px;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	padding-right: 12px;
	padding-left: 12px;
	border: 1px solid #9bb8d0;
	margin-bottom: 20px;
}

.on {
	border: 1px solid #2c97ef;
	background-color: #b2dcff;
}

.on .seven_spant {
	
}

.but_pay {
	height: 3.5rem;
	line-height: 3.5rem;
	width: 100%;
	overflow: hidden;
	border-top: 1px solid #e2e2e2;
	text-align: center;
	font-size: 16px;
	bottom: 0;
	position: fixed;
	background-color: #fff;
}

.but_pay:ACTIVE {
	background-color: #efefef;
}

.result {
	display: none;
	height: 186px;
	width: 80%;
	position: fixed;
	border-radius: 10px;
	background-color: #fff;
	margin: auto;
	left: 0;
	right: 0;
	top: 0;
	bottom: 0;
	z-index: 11;
}

.result_head {
	height: 3.5rem;
	line-height: 3.5rem;
	text-align: center;
	border-bottom: 1px solid #e0e0e0;
}

.result_center {
	height: 4.5rem;;
	line-height: 4.5rem;;
	text-align: center;
}

.result_footer {
	color: #ea281c;
	height: 3.5rem;
	line-height: 3.5rem;
	text-align: center;
	border-top: 1px solid #e0e0e0;
}

.result_footer:active {
	background-color: #efefef;
	border-bottom-right-radius: 10px;
	border-bottom-left-radius: 10px;
}

.seven_days {
	float: left;
}

.seven_price {
	width: 2.4rem;
	float: right;
	height: 50px;
}

.seven_spano {
	color: #2c97ef;
	font-size: 13px;
	margin-top: -7px;
	height: 16px;
}

.seven_spant {
	font-size: 13px;
	color: #a0a0a0;
	height: 16px;
	text-decoration: line-through;
}

.seven_line {
	height: 45px;
	width: 1px;
	background-color: #9bb8d0;
	float: right;
	margin-right: 10px;
	margin-top: 7px;
}

.payment {
	width: 100%;
	z-index: 11;
	background-color: #fff;
	position: fixed;
	bottom: -205px;
}

.money {
	height: 3.5rem;
	line-height: 3.5rem;
	border-bottom: 1px solid #e0e0e0;
	padding: 0px 10px;
}

.money_number {
	float: right;
}

.pay_way {
	margin-top: 10px;
}

.pay_way_number {
	padding: 0px 10px;
}

.wechatpay {
	padding: 20px 10px;
}

.wechatimg {
	float: left;
	width: 40px;
}

.wechattext {
	float: left;
	margin-top: 3px;
}

.sure_pay {
	height: 3.5rem;
	line-height: 3.5rem;
	border-top: 1px solid #e0e0e0;
	text-align: center;
	font-size: 16px;
	clear: left;
	margin-top: 20px;
	color: #2c97ef;
}

.sure_pay:active {
	background-color: #efefef;
}

.circle {
	width: 20px;
	height: 20px;
	border-radius: 50%;
	border: 1px solid #848080;
	float: right;
}

.circle_n {
	width: 18px;
	height: 18px;
	border-radius: 50%;
	margin: auto;
	margin-top: 1px;
}

.circleon {
	background-color: #2c97ef;
}

.barrier {
	background-color: #000;
	opacity: 0.3;
	height: 100%;
	width: 100%;
	position: fixed;
	z-index: 10;
	display: none;
}

.barrier1 {
	background-color: #000;
	opacity: 0.3;
	height: 100%;
	width: 100%;
	position: fixed;
	z-index: 10;
	display: none;
}

.cuttingling_one {
	width: 100%;
	height: 2px;
	background-color: #f0f0f0;
}

.cuttingling_two {
	width: 100%;
	height: 5px;
	background-color: #f0f0f0;
}
</style>
</head>
<body>
	<div class="barrier" disabled="disabled"></div>
	<div class="barrier1" disabled="disabled"></div>

	<div class="shops_TopNames">开通/激活路由器</div>
	<div class="cuttingling_one"></div>
	<div class="intro">
		<h3>说明</h3>
		<li class="item">开通/激活路由器,请联系工作人员</li>
		<li class="item">姓名：周先生</li>
		<li class="item">电话号码：15414447211</li>
		<li class="item">会员用户可享受WiFi带来的极速体验</li>
		<li class="item">你将购买的商品为虚拟商品，购买后不支持退款，请斟酌购买。</li>
		<li class="item">如需退还押金，请联系工作人员。</li>
		<li class="item">*本商品暂时只支持微信支付*</li>
	</div>
	<div class="cuttingling_two"></div>
	<div class="choice_list">
		<div class="list on">
			<div class="seven_days">支付押金</div>
			<div class="seven_price">
				<div class="seven_spano">¥10.00</div>
				<div class="seven_spant">¥50.00</div>
			</div>
			<div class="seven_line"></div>
		</div>
	</div>
	<div class="but_pay" id="but_pay">立即支付</div>
	<div class="result">
		<div class="result_head">
			<span>支付结果提示</span>
		</div>
		<div class="result_center">
			<div class="center_text"></div>
		</div>
		<div class="result_footer" id="result_footer">
			<div>确认</div>
		</div>
	</div>
	<div class="payment">
		<input name="siteId" value='${obj.siteId}' type="hidden" id="siteId">
		<input name="userMac" value='${obj.userMac}' type="hidden" id="userMac">
		<input name="apMac" value='${obj.apMac}' type="hidden" id="apMac">
		<input name="priceConfigId" value='${obj.priceConfig.id}' type="hidden" id="priceConfigId">
		<input name="apMac" value='${obj.apMac}' type="hidden" id="apMac">
		<div class="money">
			支付金额：<span class="money_number">¥10.00</span>
		</div>
		<div class="pay_way">
			<span class="pay_way_number">请选择支付方式</span>
			<div class="wechatpay">
				<img class="wechatimg" src="${statics}/images/wechatpay.png">
				<div class="wechattext">微信支付</div>
				<div class="circle">
					<div class="circle_n circleon"></div>
				</div>
			</div>
			<div class="alipay"></div>
			<div class="sure_pay">
				<span disabled="disabled">确认支付</span>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var ip;
	function get_client_ip(){
		if(returnCitySN["cip"] != "unknown"){
			ip = returnCitySN["cip"];
		}
		return ip;
	}
	
	$(function() {
		$(".choice_list .list").click(function() {

			$(this).siblings('.list').removeClass('on'); // 删除其他兄弟元素的样式

			$(this).addClass('on'); // 添加当前元素的样式
		});
		$(".pay_way .circle_n").click(function() {

			$(this).siblings('.circle_n').removeClass('circleon'); // 删除其他兄弟元素的样式

			$(this).addClass('circleon'); // 添加当前元素的样式

		});
		$("#but_pay").click(function() {
			$(".barrier").css("display", "block");
			$(".payment").animate({
				bottom : '0px'
			});
		});
		$(".barrier").click(function() {
			$(".barrier,.result").css("display", "none");
			$(".payment").animate({
				bottom : '-205px'
			});
		});

		$("#result_footer").click(function() {
			$(".barrier1,.result").css("display", "none");
		})
	});

	var priceConfigId;
	$(".sure_pay").click(function() {
		var siteId=$("#siteId").val();
		var userMac=$("#userMac").val();
		var siteId=$("#siteId").val();
		var nasid=$("#nasid").val();
		var userIp=$("#userIp").val();
		var payType=$("#payType").val();
		var key=$("#key").val();
		var tag=$("#tag").val();
		var base = "${ctx}";  
		var siteId=$("#siteId").val();
		var userMac=$("#userMac").val();
		var apMac=$("#apMac").val();
		var priceConfigId=$("#priceConfigId").val();
		var totalFee=ppunit_price*100;//订单总金额，单位为分
		$.ajax({
			type : "post",
			url : base + "/PROT/unifiedorder",
			data : {
				"siteId" : siteId,
				"body" : "设备租用押金",
				"detail" : "使用网络设备缴纳押金",
				"totalFee" : totalFee,
				"productId" : priceConfigId,
				"buyNum" : 1,
				"apMac":apMac,
				"ip" : userMac,
			},
			dataType : "json",
			success : function(data) {
				if (data.code ==200) {//统一下单成功
					window.location.href = data.mweb_url;
				} else {
					alert("统一下单失败");
				}
			}
		});
	});
</script>
</html>

