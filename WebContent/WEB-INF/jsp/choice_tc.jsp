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
<script type="text/javascript" src="https://pv.sohu.com/cityjson"></script>
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
	margin: 0px auto 20px auto;
	/* list-style: none; */
	border: 1px solid #34c1e6;
	padding: 12px;
	border-style: dashed;
	overflow: hidden;
	text-overflow: ellipsis;
}
.intro span{
    font-weight: 1000;
    font-size: 15px;
	}

.time{
	width: 80%;
	height: 25px;
	margin: auto;
	text-align: center;
	padding:2px 12px;
	line-height: 25px;
	background-color: #fff;
}
.time span{
	font-size: 14px;
	color:red;

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
	border:0;
	text-align: center;
	font-size: 16px;
	bottom: 0;
	position: fixed;
	color:#fff;
	background-color: #9e9e9e;
}

.but_pay:active {
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
<%!
//获取访问ip的方法
public String getIpAddr(HttpServletRequest request) { 
    String ip = request.getHeader("x-forwarded-for");    
    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
        ip = request.getHeader("Proxy-Client-IP");    
    }    
    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
        ip = request.getHeader("WL-Proxy-Client-IP");    
    }    
    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
        ip = request.getRemoteAddr();    
    }    
    return ip;    
}
//使用方法获取ip

%>

</head>
<body>
	<div class="barrier" disabled="disabled"></div>
	<div class="barrier1" disabled="disabled"></div>

	<div class="shops_TopNames">会员优惠套餐</div>
	<div class="cuttingling_one"></div>
	
	<div class="time"><span></span></div>
	<div class="intro" id="describe">
		<span>说明</span>
		<li class="item">会员用户可选择相应套餐进行充值</li>
		<li class="item">请按需购买，本商品支持售后退款。</li>
		<li class="item">一机一账号，请勿将自己的账户交给他人使用，以免消耗您的套餐周期。</li>
		<li class="item">微信公众号充值更方便，搜索微信公众号：feixun-wifi</li>
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
		<input name="siteId" value='${obj.siteId}' type="hidden" id="siteId">
		<input name="key" value='${obj.key}' type="hidden" id="key">
		<input name="userMac" value='${obj.userMac}' type="hidden" id="userMac">
		<input name="nasid" value='${obj.nasid}' type="hidden" id="nasid">
		<input name="allMac" value='${obj.allMac}' type="hidden" id="allMac">
		<input name="userIp" value='${obj.userIp}' type="hidden" id="userIp">
		<input name="username" value='${obj.username}' type="hidden" id="username">
		<input name="tag" value='${obj.tag}' type="hidden" id="tag">
		<input name="totalFee"  type="hidden" id="totalFee">
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
	var ppid="";
	var ppunit_price="";
	var ppname = "";
	$(function() {
		ip = returnCitySN["cip"];
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
		
		var tag;
		var key;
		$("#but_pay").click(function() {
			var siteId=$("#siteId").val();
			var userMac=$("#userMac").val();
			var siteId=$("#siteId").val();
			var nasid=$("#nasid").val();
			var allMac=$("#allMac").val();
			var userIp=$("#userIp").val();
			var payType=$("#payType").val();
			var username=$("#username").val();
			key=$("#key").val();
			tag=$("#tag").val();
			var totalFee=ppunit_price*100;//订单总金额，单位为分
			
			var url = "${ctx}" + "/PROT/payforMemberPack";//会员包下单
			$.ajax({
				type : "post",
				url : url,
				dataType : "json",
				data : {
					"siteId":siteId,
					"body":"飞讯WiFi会员充值"+ppname,
					"detail":"用户充值会员套餐",
					"totalFee":totalFee,
					"productId":ppid,//填site_price_config表记录的ID
					"mac":userMac,
					"nasid":nasid,
					"allMac":allMac,
					"userIp":ip,
					"username":username,
					"payType":payType,
					"buyNum":1,
					"tag":tag
				},
				success : function(data) {
					if (data.code ==200) {//统一下单成功
						var r_url = getCookie("ar3");
						window.location.href = data.mweb_url;
					} else {
						alert("统一下单失败");
					}
				}
			});
		});
		
		function formatDuring(mss) {
		    var minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60));
		    var seconds = parseInt((mss % (1000 * 60)) / 1000);
		    return minutes + " 分钟 " + seconds + " 秒 ";
		}
		
		var si = setInterval(function(){ 
			var time1 = getCookie("time");
			if(time1 != ""){
				var time2 = new Date().getTime();
				if(time2 - time1 >= 5*60*1000){
					$(".time span").html("充值已超时，请重新登录进入充值。");
				}else{
					var time3 = 5*60*1000 - (time2 - time1);
					$(".time span").html("充值倒计时："+formatDuring(time3));
				}
			}	
		}, 1000);
	});
</script>
</html>

