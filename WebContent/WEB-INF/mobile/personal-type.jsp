<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/partol" />
<!DOCTYPE html>
<html>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>个人中心页面 类型分类</title>
<script>
			;!(function(doc, win) {
				var docEl = doc.documentElement,
					resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
					recalc = function() {
						var clientWidth = docEl.clientWidth;
						if(!clientWidth) return;
						if(clientWidth >= 750) {
							docEl.style.fontSize = '100px';
						} else {
							docEl.style.fontSize = '100px';

							docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
						}
					};

				if(!doc.addEventListener) return;
				win.addEventListener(resizeEvt, recalc, false);
				doc.addEventListener('DOMContentLoaded', recalc, false);
			})(document, window);
		</script>
<link rel="stylesheet" type="text/css" href="${path}/css/public.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/personal1.css" />
<!--图标样式-->
<!--<link rel="stylesheet" type="text/css" href="fonts/demo.css"/>-->
<link rel="stylesheet" type="text/css" href="${path}/fonts/iconfont.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/swiper.min.css" />
<!--ios 弹框-->
<style>
.swiper-container1 {
	margin-left: auto;
	margin-right: auto;
	position: relative;
	margin-top: 48px !important;
	overflow: visible !important;
	z-index: 1;
}

.swiper-container-a {
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
</style>
</head>
<script type="text/javascript">
		var basePath="${ctx}";
	</script>
<body>
	<div id="cart-page">
		<div class="choose-type" id="choose-type">
			<!--新的购买首页布局开始-->

			<ul class="choose-type_son">
				<li class="active1 clearfix buyTime">
					<div class="selset-time1">
						<div class="choose-time1">
							<i class="iconfont icon-iconfontshichang  choose-time1"></i>
						</div>
						<div class="text1">
							<span>按时长购买</span><i class="iconfont icon-gou2 text-right"
								style="display: none;"></i>
						</div>
					</div>
				</li>
				<li class="active1 clearfix buyFlow">
					<div class="selset-time1">
						<div class="choose-time1">
							<i class="iconfont icon-liuliang choose-time1"></i>
						</div>
						<div class="text1">
							<span>按流量购买</span><i class="iconfont icon-gou2 text-right"
								style="display: none;"></i>
						</div>
					</div>
				</li>
				<li class="active1 clearfix buyInformation">
					<div id="selset-time1">
						<div class="choose-time1">
							<i class="iconfont icon-youhui choose-time1"></i>
						</div>
						<div class="text">
							<span>按优惠信息购买</span><i class="iconfont icon-gou2 text-right"
								style="display: none;"></i>
						</div>
					</div>
				</li>
			</ul>

			<!--新的购买首页布局结束-->



		</div>
		<!--==================================-->

		<ul style="display: none;" class="Describe">
			<li class="active2 clearfix  buy_sur1 on">
				<div class="selset-time2">
					<div class="choose-time2">
						<i class="iconfont icon-iconfontshichang  choose-time3"></i>
					</div>
					<div class="text2">
						<span>按时长购买</span><i class="iconfont icon-gou2 text-right2"></i>
					</div>
				</div>
			</li>
			<li class="active2 clearfix  buy_sur2">
				<div class="selset-time2">
					<div class="choose-time2">
						<i class="iconfont icon-liuliang choose-time3"></i>
					</div>
					<div class="text2">
						<span>按流量购买</span><i class="iconfont icon-gou2 text-right2"></i>
					</div>
				</div>
			</li>
			<li class="active2 clearfix  buy_sur3">
				<div id="selset-tim2">
					<div class="choose-time2">
						<i class="iconfont icon-youhui choose-time3"></i>
					</div>
					<div class="text2">
						<span>按优惠信息购买</span><i class="iconfont icon-gou2 text-right2"></i>
					</div>
				</div>
			</li>
		</ul>
		<ul style="display: none;" class="Describe_a">
			<li class="active3 clearfix">
				<div class="selset-time2">
					<div class="choose-time2">
						<i class="iconfont icon-iconfontshichang  choose-time3"></i>
					</div>
					<div class="text2">
						<span>按时长购买</span><i class="iconfont icon-gou2 text-right2"></i>
					</div>
				</div>
			</li>
			<li class="active3 clearfix">
				<div class="selset-time2">
					<div class="choose-time2">
						<i class="iconfont icon-liuliang choose-time3"></i>
					</div>
					<div class="text2">
						<span>按流量购买</span><i class="iconfont icon-gou2 text-right2"></i>
					</div>
				</div>
			</li>
			<li class="active3 clearfix">
				<div id="selset-tim2">
					<div class="choose-time2">
						<i class="iconfont icon-youhui choose-time3"></i>
					</div>
					<div class="text2">
						<span>按优惠信息购买</span><i class="iconfont icon-gou2 text-right2"></i>
					</div>
				</div>
			</li>
		</ul>


		<!--=====================================-->

		<!--支付方式开始-->
		<div id="pay" style="display: none;">
			<div class="paydetails-title">
				<!--<i class="iconfont icon-jiantouzuo"></i>-->
				支付详情
			</div>
			<div class="totalamount">
				<p>支付总金额</p>
				<h1>
					<i class="iconfont icon-yikeappshijiandizhiqianrenyuan76"></i><span
						class="allMoney"></span>
				</h1>
			</div>
			<div class="paydetails-name">
				${obj.site.site_name} <span style="margin-left: 2%;">￥</span> <span
					class="allMoney"></span>
			</div>
			<div class="choice-pay">
				<div class="choicepay-title">选择支付方式</div>
				<div class="choicepay-type">
					<ul>
						<li><label for="wechat"><img
								src="${path}/img/wechat.jpg" alt="" /><span>微信支付</span></label> <input
							type="radio" id="wechat" name="radio" checked="checked" value="1" />
						</li>
						<li><label for="alipay"><img
								src="${path}/img/alipay.jpg" alt="" /><span>支付宝支付</span></label> <input
							type="radio" id="alipay" name="radio" value="2" /></li>
						<li><label for="unionpay"><img
								src="${path}/img/unionpay.jpg" alt="" /><span>银行卡支付</span></label> <input
							type="radio" id="unionpay" name="radio" value="3" /></li>
					</ul>
				</div>
				<div class="choicepayimme1 topay">立即支付</div>
			</div>

		</div>
		<!--支付方式结束-->




		<!--按时长购买 套餐详情-->
		<div class="choose-wrap buytimewrap">
			<div class="choose-selset clearfix">
				<div class="selsettitle">时长套餐详情</div>
				<div class="package-details">
					<h1>
						<i class="iconfont icon-yikeappshijiandizhiqianrenyuan76"></i><span
							class="timeprice1"></span>
					</h1>
					<div class="details">
						<ul>
							<li><span>套餐名称</span>
								<p>
									<span id="tiemType1"></span><i
										class="iconfont icon-arrow-right"></i>
								</p></li>
							<li><span>购买数量</span>
								<p>
									<span id="numType1"></span> <i
										class="iconfont icon-arrow-right"></i>
								</p></li>
						</ul>
					</div>
					<div class="detailsnum">
						赠送 <span class="addTime"></span>
					</div>
				</div>
				<div class="selsetbotton clearfix">
					<a class="back_remove_a">取消</a> <a href="javascript:;"
						class="next_success_c success">完成</a>
				</div>
			</div>
		</div>
		<div class="choose-wrap trafficwrap">
			<div class="choose-selset clearfix">
				<div class="selsettitle">流量套餐详情</div>
				<div class="package-details">
					<h1>
						<i class="iconfont icon-yikeappshijiandizhiqianrenyuan76"></i><span
							class="flowprice1"></span>
					</h1>
					<div class="details">
						<ul>
							<li><span>套餐名称</span>
								<p>
									<span id="tiemType2"></span><i
										class="iconfont icon-arrow-right"></i>
								</p></li>
							<li><span>购买数量</span>
								<p>
									<span id="numType2"> </span><i
										class="iconfont icon-arrow-right"></i>
								</p></li>
						</ul>
					</div>
					<div class="detailsnum">
						赠送 <span class='addflow'></span>
					</div>
				</div>
				<div class="selsetbotton clearfix">
					<a class="back_remove_b">取消</a> <a class="success1 next_success_d">完成</a>
				</div>
			</div>
		</div>
		<div class="choose-wrap-box">
			<div class="choose-wrap buytime buytime_a">
				<div class="choose-selset clearfix">
					<div class="selsettitle">按时长购买数量及单位</div>
					<div class="package-details">
						<div class="cui-roller">
							<div id="wrapper">
								<div class="swiper-container">
									<div class="swiper-wrapper">
										<div class="swiper-slide">
											<p>1</p>
										</div>
										<div class="swiper-slide">
											<p>2</p>
										</div>
										<div class="swiper-slide">
											<p>3</p>
										</div>
										<div class="swiper-slide">
											<p>4</p>
										</div>
										<div class="swiper-slide">
											<p>5</p>
										</div>
										<div class="swiper-slide">
											<p>6</p>
										</div>

									</div>
								</div>
							</div>

							<div id="wrapper2">
								<div class="swiper-container1">
									<div class="swiper-wrapper">
										<c:forEach var="p" items="${obj.spc.list}">
											<c:if test="${p.price_type<=3 }">
												<div class="swiper-slide tLi">
													<p data-text="${p.describe}" mealNum="${p.giveMeal}"
														mealType="${p.giveMealUnit }" value="${p.id}"
														reommend="${p.recommendState}"
														charge_type="${p.charge_type }" prices="${p.unit_price}"
														price_type="${p.price_type}" price_num="${p.price_num }">${p.name}</p>
												</div>
											</c:if>
										</c:forEach>
									</div>
								</div>
							</div>
							<div class="cui-mask"></div>
							<div class="cui-lines">&nbsp;</div>
						</div>

					</div>
					<div class="selsetbotton clearfix">
						<a class="back_remove">取消</a> <a style="color: #258de6;"
							class="complete next_success_a" id="succ1">完成</a>
					</div>
				</div>
			</div>
			<!--按流量单位 套餐详情-->
			<div class="choose-wrap trafficdate trafficdate_a">
				<div class="choose-selset clearfix">
					<div class="selsettitle">选择流量购买数量及单位</div>
					<div class="package-details">
						<div class="cui-roller">
							<div id="wrapper">
								<div class="swiper-container">
									<div class="swiper-wrapper">
										<div class="swiper-slide">
											<p>1</p>
										</div>
										<div class="swiper-slide">
											<p>2</p>
										</div>
										<div class="swiper-slide">
											<p>3</p>
										</div>
										<div class="swiper-slide">
											<p>4</p>
										</div>
										<div class="swiper-slide">
											<p>5</p>
										</div>
										<div class="swiper-slide">
											<p>6</p>
										</div>

									</div>
								</div>
							</div>

							<div id="wrapper2">
								<div class="swiper-container1">
									<div class="swiper-wrapper">
										<c:forEach var="p" items="${obj.spc.list}">
											<c:if test="${p.price_type>3&& p.price_type<6}">
												<div class="swiper-slide fLi">
													<p data-text="${p.describe}" mealNum="${p.giveMeal}"
														mealType="${p.giveMealUnit }" value="${p.id}"
														reommend="${p.recommendState}"
														charge_type="${p.charge_type }" prices="${p.unit_price}"
														price_type="${p.price_type}" price_num="${p.price_num }">${p.name}</p>
												</div>
											</c:if>
										</c:forEach>
									</div>
								</div>
							</div>
							<div class="cui-mask"></div>
							<div class="cui-lines">&nbsp;</div>
						</div>

					</div>
					<div class="selsetbotton clearfix">
						<a class="back_remove">取消</a> <a href="#" style="color: #258de6;"
							class="next_success_a" id="succ2">完成</a>
					</div>
				</div>
			</div>
			<!--优惠信息套餐 套餐详情-->
			<div class="choose-wrap cheaper cheaper_a">
				<div class="choose-selset clearfix">
					<div class="selsettitle">
						套餐详情 <a class="back_remove"><i class="iconfont icon-cuo2"></i></a>
					</div>
					<div class="preferential">
						<ul>
							<!-- 	<li>
								<div class="prefettitle"> 优惠信息套餐1<span>￥19.00</span></div>
								<div class="preferinfro">
									3G流量套餐+20M
									<a class="success2">使用</a>
								</div>
							</li> -->
						</ul>
					</div>

				</div>
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
			<div class="swiper-container-a">
				<div class="swiper-wrapper">
					<div class="swiper-slide" style="position: relative;">
						<div
							style="position: absolute; top: -9%; left: 0; width: 100%; text-align: center;">
						</div>
						<div>
							<img class="rsImg" src="${path}/img/p1.jpg"
								style="max-height: 7rem;" />
						</div>
					</div>
					<div class="swiper-slide" style="position: relative;">
						<div
							style="position: absolute; top: -9%; left: 0; width: 100%; text-align: center;">
						</div>
						<div>
							<img class="rsImg" src="${path}/img/p2.jpg"
								style="max-height: 7rem;" />
						</div>
					</div>
					<div class="swiper-slide" style="position: relative;">
						<div
							style="position: absolute; top: -9%; left: 0; width: 100%; text-align: center;">
						</div>
						<div>
							<img class="rsImg" src="${path}/img/p3.jpg"
								style="max-height: 7rem;" />
						</div>
					</div>
					<div class="swiper-slide" style="position: relative;">
						<div
							style="position: absolute; top: -9%; left: 0; width: 100%; text-align: center;">
						</div>
						<div>
							<img class="rsImg" src="${path}/img/p4.jpg"
								style="max-height: 7rem;" />
						</div>
					</div>
					<div class="swiper-slide" style="position: relative;">
						<div
							style="position: absolute; top: -9%; left: 0; width: 100%; text-align: center;">
						</div>
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

			<div class="payguidefooter" onclick="closeLayer();">我知道了</div>
		</div>
	</div>
	<input id="siteId" type="hidden" value="${obj.site.id}">
	<input id="userId" type="hidden" value="${obj.user.id}">
	<script type="text/javascript" src="${path}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${path}/js/pay.js"></script>
	<!--ios 选择框-->
	<script type="text/javascript" src="${path}/js/zepto.min.js"></script>
	<script type="text/javascript" src="${path}/js/fastclick.js"></script>
	<script type="text/javascript" src="${path}/js/Iscoll.js"></script>
	<script type="text/javascript" src="${path}/js/swiper.min.js"></script>
	<script type="text/javascript"
		src="${path }/layer.mobile-v2.0/layer_mobile/layer.js"></script>
	<script src="${path}/js/swiper-3.4.2.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script>
			var swiper = new Swiper('.swiper-container', {
				pagination: '.swiper-pagination',
				paginationClickable: true,
				direction: 'vertical',
				height: 57,
			});
			var swiper = new Swiper('.swiper-container1', {
				pagination: '.swiper-pagination',
				paginationClickable: true,
				direction: 'vertical',
				height: 57,
			});
		</script>
	<script type="text/javascript">
	      function closeLayer(){
	        $('.payguidewrap').hide();
	      }
   		 </script>
	<script type="text/javascript">
		    var swiper = new Swiper('.swiper-container-a', {
		        pagination: '.swiper-pagination',
		        slidesPerView: 1,
		        paginationClickable: true,
		        spaceBetween: 80,
		        keyboardControl: true,
		        nextButton: '.swiper-button-next',
		        prevButton: '.swiper-button-prev',
		    });
    </script>
</body>
</html>