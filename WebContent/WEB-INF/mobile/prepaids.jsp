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
<title>充值</title>
<link rel="stylesheet" type="text/css" href="${path}/css/public.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/personal.css" />
<link rel="stylesheet" type="text/css" href="${path}/fonts/iconfont.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/swiper.min.css" />
<style>
.swiper-container {
	width: 100%;
	height: 100%;
	margin-left: auto;
	margin-right: auto;
}

.swiper-slide {
	text-align: center;
	font-size: 0.16rem;
	background: #fff;
	/* Center slide text vertically */
	display: -webkit-box;
	display: -ms-flexbox;
	display: -webkit-flex;
	display: flex;
	-webkit-box-pack: center;
	-ms-flex-pack: center;
	-webkit-justify-content: center;
	justify-content: center;
	-webkit-box-align: center;
	-ms-flex-align: center;
	-webkit-align-items: center;
	align-items: center;
}

.totalamount h1 i {
	font-size: 0.5rem !important;
}
</style>
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
		var basePath="${ctx}";
	</script>
<script type="text/javascript" src="${path}/js/jquery.min.js"></script>
<script src="${path}/js/swiper-3.4.2.min.js" type="text/javascript"
	charset="utf-8"></script>
</head>
<script type="text/javascript">
   		 $(function(){
   			var swiper = new Swiper('.swiper-container', {
   		        pagination: '.swiper-pagination',
   		        slidesPerView: 1,
   		        paginationClickable: true,
   		        spaceBetween: 80,
   		        keyboardControl: true,
   		        nextButton: '.swiper-button-next',
   		        prevButton: '.swiper-button-prev',
   		    });
   		} );
      	
        </script>
<body>
	<div
		style="width: 100%; height: 100%; position: absolute; left: 0; top: 0px;">
		<div id="cart-page">
			<div class="paydetails-title" onclick="history.go(-1)">
				<i class="iconfont icon-jiantouzuo"></i> 支付详情
			</div>
			<div class="totalamount">
				<p>支付总金额</p>
				<h1>
					<i class="iconfont icon-yikeappshijiandizhiqianrenyuan76"></i><span
						class="allMoney">${obj.map.amount}</span>
				</h1>
			</div>
			<div class="paydetails-name">
				${obj.map.name} <span>￥${obj.map.price}</span>
			</div>
			<div class="choice-pay">
				<div class="choicepay-title">选择支付方式</div>
				<div class="choicepay-type">
					<ul>
						<li><label for="wechat"><img
								src="${path}/img/wechat.jpg" alt="" /><span>微信支付</span></label> <input
							type="radio" id="wechat" name="radio" checked="" /></li>
						<li><label for="alipay"><img
								src="${path}/img/alipay.jpg" alt="" /><span>支付宝支付</span></label> <input
							type="radio" id="alipay" name="radio" /></li>
						<li><label for="unionpay"><img
								src="${path}/img/unionpay.jpg" alt="" /><span>银行卡支付</span></label> <input
							type="radio" id="unionpay" name="radio" /></li>
					</ul>
				</div>
				<div class="choicepayimme">
					<a class="oncePay">立即支付</a>
				</div>
			</div>

		</div>

		<div class="payguidewrap" style="display: none;">
			<div class="payguideLayer"></div>
			<div class="payguide">
				<div class="payguidetitle">
					<img src="${path}/img/kdf.jpg" />
					<h2>微信支付需要您先关注飞讯无限公众号</h2>
					<p>宽未来</p>
				</div>

				<!-- Swiper -->
				<div class="swiper-container">
					<div class="swiper-wrapper">
						<div class="swiper-slide" style="position: relative;">
							<div
								style="position: absolute; top: -9%; left: 0; width: 100%; text-align: center;">
								第一步：点击微信右上角加号</div>
							<div>
								<img class="rsImg" src="${path}/img/p1.jpg"
									style="max-height: 7rem;" />
							</div>
						</div>
						<div class="swiper-slide" style="position: relative;">
							<div
								style="position: absolute; top: -9%; left: 0; width: 100%; text-align: center;">
								第二步：点击公众号搜索</div>
							<div>
								<img class="rsImg" src="${path}/img/p2.jpg"
									style="max-height: 7rem;" />
							</div>
						</div>
						<div class="swiper-slide" style="position: relative;">
							<div
								style="position: absolute; top: -9%; left: 0; width: 100%; text-align: center;">
								第三步：搜索框内输入“宽未来”</div>
							<div>
								<img class="rsImg" src="${path}/img/p3.jpg"
									style="max-height: 7rem;" />
							</div>
						</div>
						<div class="swiper-slide" style="position: relative;">
							<div
								style="position: absolute; top: -9%; left: 0; width: 100%; text-align: center;">
								第四步：点击关注</div>
							<div>
								<img class="rsImg" src="${path}/img/p4.jpg"
									style="max-height: 7rem;" />
							</div>
						</div>
						<div class="swiper-slide" style="position: relative;">
							<div
								style="position: absolute; top: -9%; left: 0; width: 100%; text-align: center;">
								第五步：点击充值缴费中的自己充值</div>
							<div>
								<img class="rsImg" src="${path}/img/p5.jpg"
									style="max-height: 7rem;" />
							</div>
						</div>
					</div>
					<!-- Add Pagination -->
					<div class="swiper-pagination"></div>
					<!-- Add Arrows -->
					<div class="swiper-button-next"></div>
					<div class="swiper-button-prev"></div>
				</div>
			</div>
			<div class="payguidefooter" onclick="closeLayer();">我知道了</div>
		</div>
	</div>

	<input id="siteId" type="hidden" value="${obj.site.id}">
	<input id="userId" type="hidden" value="${obj.user.id}">
	<span id="maps" style="display: none;">${obj.map}</span>
</body>
<script type="text/javascript">
	      function closeLayer(){
	    	  $('.payguidewrap').hide();
	      }
   		 </script>
<script src="${path}/js/qumentpay.js" type="text/javascript"
	charset="utf-8"></script>
</html>