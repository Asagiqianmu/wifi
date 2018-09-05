<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>   
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pathjs" value="${ctx}/web_js" />
<c:set var="pathcss" value="${ctx}/web_css" />
<c:set var="statics" value="${ctx}/static" />

<!DOCTYPE HTML>
<html>
<head lang="zh-CN">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>选择套餐</title>
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<%-- <link rel="stylesheet" type="text/css" href="${pathcss}/public.css" /> --%>
<link rel="stylesheet" type="text/css" href="${pathcss}/reg.css" />
<script type="text/javascript" src="${pathjs}/jquery.min.js"></script>
<script type="text/javascript" src="${pathjs}/json.js"></script>
<!-- <script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script> -->
<style type="text/css">
body {
	padding: 0px;
	margin: 0px;
	/* background: rgba(0,0,0,0.5) url("../static/images/bg-1.png") no-repeat; */
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
	background: #fff;
    font-weight: bold;
	
}

.intro {
	font-size: 14px;
	height: 100%;
	width: 80%;
	margin: 20px auto;
	/* list-style: none; */
	border: 1px solid #34c1e6;
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
	border: 1px solid #34c1e6;
	margin-bottom: 20px;
}

.on {
	color:#fff;
	border: 1px solid #34c1e6;
	background-color: #34c1e6;
}


.but_pay {
	height: 3.5rem;
	line-height: 3.5rem;
	width: 100%;
	overflow: hidden;
	border: 0;
	text-align: center;
	font-size: 16px;
	bottom: 0;
	position: fixed;
	color:#fff;
	background-color: #9e9e9e;
}

.but_pay:ACTIVE {
	background-color: #2d9fbd;
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
	width: 3.4rem;
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
	height: 16px;
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
	
	<div class="shops_TopNames">会员优惠套餐</div>
	<div class="cuttingling_one"></div>
	<div class="intro" id="describe">
		<h3>说明</h3>
		<li class="item">会员用户可选择相应套餐进行充值</li>
		<li class="item">请按需购买，本商品支持售后退款。</li>
		<li class="item">一机一账号，请勿将自己的账户交给他人使用，以免消耗您的套餐周期。</li>
	</div>
	<div class="cuttingling_two"></div>
	<div class="choice_list" id="price">
		 <c:forEach var="pp" items="${obj.priceConfig}">
			<div class="list">
				<input class="ppid" type="hidden" value="${pp.id}">
				<input class="ppunit_price"  type="hidden" value="${pp.unit_price}">
				<!-- 0:单位为小时  1：单位为天  2：单位为月  4：单位为兆  5：单位为G  6：为押金 -->
				<c:choose>
					<c:when test="${pp.price_type == 0}">
						<div class="seven_days" >${pp.price_num}${pp.name}</div>
						<div class="seven_price">
							<fmt:formatNumber type="number"  value="${pp.unit_price}" pattern="0.00" maxFractionDigits="2"/><span>￥</span> 
						</div>
					</c:when>
					<c:when test="${pp.price_type == 1}">
						<c:choose> 
							<c:when test="${pp.price_num == 15}">
								<div class="seven_days" >半个月</div>
							</c:when>
							<c:when test="${pp.price_type == 30}">
								<div class="seven_days" >一个月</div>
							</c:when>
							<c:otherwise>  <!--否则 -->    
								<div class="seven_days" >${pp.price_num}天</div>
							</c:otherwise>
						</c:choose>
						<div class="seven_price">
							<fmt:formatNumber type="number"  value="${pp.unit_price}" pattern="0.00" maxFractionDigits="2"/><span>元</span> 
						</div>
					</c:when>
					<c:when test="${pp.price_type == 2}">
						<c:choose> 
							<c:when test="${pp.price_num == 6}">
								<div class="seven_days" >半年</div>
							</c:when>
							<c:when test="${pp.price_type == 12}">
								<div class="seven_days" >一年</div>
							</c:when>
							<c:otherwise>  <!--否则 -->    
								<div class="seven_days" >${pp.price_num}个月</div>
							</c:otherwise>
						</c:choose>
						<div class="seven_price">
							<fmt:formatNumber type="number"  value="${pp.unit_price}" pattern="0.00" maxFractionDigits="2"/><span>元</span> 
						</div>
					</c:when>
					<c:when test="${pp.price_type == 4}">
						<div class="seven_days" >${pp.price_num}个${pp.name}流量包</div>
						<div class="seven_price">
							<fmt:formatNumber type="number"  value="${pp.unit_price}" pattern="0.00" maxFractionDigits="2"/><span>元</span>
						</div>
					</c:when>
					<c:when test="${pp.price_type == 5}">
						<div class="seven_days" >${pp.price_num}个${pp.name}流量包</div>
						<div class="seven_price">
							<fmt:formatNumber type="number"  value="${pp.unit_price}" pattern="0.00" maxFractionDigits="2"/><span>元</span>
						</div>
					</c:when>
					<c:otherwise> </c:otherwise>
				</c:choose>
			</div>
		</c:forEach>
	</div>
	<button class="but_pay" id="but_pay" disabled="disabled">立即开通</button>
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
		<input name="openid" value="${obj.openid}" type="hidden" id="openid">
		<input name="username" value='${obj.username}' type="hidden" id="username">
		<input name="siteId" value='${obj.siteId}' type="hidden" id="siteId">
		<input name="tag" value='${obj.tag}' type="hidden" id="tag">
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
	var ppid="";
	var ppunit_price="";
	var ppname = "";
	$(function() {
		var htmlStr="";
		var html="";
		var arr= new Array();
		$(".choice_list .list").click(function() {
			$(this).siblings('.list').removeClass('on'); // 删除其他兄弟元素的样式
			$(this).addClass('on'); // 添加当前元素的样式
			$("#but_pay").attr("disabled", false);
			$("#but_pay").css("background-color", "#34c1e6");
			ppid = $(this).children('.ppid').val();
			ppunit_price = $(this).children('.ppunit_price').val();
			ppname = $(this).children('.seven_days').html();
			//console.log(ppid);
			//console.log(ppunit_price);
		});
		$(".pay_way .circle_n").click(function() {

			$(this).siblings('.circle_n').removeClass('circleon'); // 删除其他兄弟元素的样式

			$(this).addClass('circleon'); // 添加当前元素的样式

		});
		
		/* $("#but_pay").click(function() {
			$(".barrier").css("display", "block");
			$(".payment").animate({
				bottom : '0px'
			});
		}); */
		$(".barrier").click(function() {
			$(".barrier,.result").css("display", "none");
			$(".payment").animate({
				bottom : '-205px'
			});
		});
		$(".sure_pay").click(function() {
			$(".barrier").css("display", "none");
			$(".result,.barrier1").css("display", "block");
			$(".center_text").html("支付成功");
			$(".payment").animate({
				bottom : '-205px'
			});
		})
		$("#result_footer").click(function() {
			$(".barrier1,.result").css("display", "none");
		});

		function onBridgeReady(a,b,c,d,e,f) {
			WeixinJSBridge.invoke('getBrandWCPayRequest', {
				"appId" : a, //公众号名称，由商户传入
				"timeStamp" : c, //时间戳，自1970年以来的秒数
				"nonceStr" : d, //随机串
				"package" : e, //预支付交易会话标识
				"signType" : f, //微信签名方式
				"paySign" : b //微信签名
			}, function(res) {
				if (res.err_msg == "get_brand_wcpay_request:ok") {//支付成功
					alert('支付成功');
				} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
					alert('支付取消');
				} else if (res.err_msg == "get_brand_wcpay_request:fail") {
					alert('支付失败');
				} //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
			});
		}

		function callpay(a,b,c,d,e,f) {
			if (typeof WeixinJSBridge == "undefined") {
				if (document.addEventListener) {
					document.addEventListener('WeixinJSBridgeReady', onBridgeReady,false);
				} else if (document.attachEvent) {
					document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
					document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
				}
			} else {
				onBridgeReady(a,b,c,d,e,f);
			}
		}
		
		$("#but_pay").click(function() {
			var openid = $("#openid").val();
			var username=$("#username").val();
			var siteId=$("#siteId").val();
			var userMac=$("#userMac").val();
			var tag=$("#tag").val();
			var totalFee=ppunit_price*100;//订单总金额，单位为分
			var url = "${ctx}" + "/PROT/wechatpayforMemberPack";  //微信公众号支付下单接口
			$.ajax({
				type : "post",
				url : url,
				dataType : "json",
				data : {
					"siteId":siteId,
					"openId":openid,
					"body":"飞讯WiFi会员充值——"+ppname,
					"totalFee":totalFee,
					"productId":ppid,//填site_price_config表记录的ID
					"username":username,
					"buyNum":1,
					"tag":tag
				},
				success : function(data) {
					if (data.code ==200) {//统一下单成功
						var appId = data.appId;
						var paySign = data.paySign;
						var timeStamp = data.timeStamp;
						var nonceStr = data.nonceStr;
						var packageStr = data.package;
						var signType = data.signType;
						callpay(appId,paySign,timeStamp,nonceStr,packageStr,signType);
					} else {
						alert("统一下单失败");
					}
				}
			});
		});
	});

</script>
</html>

