<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/PcPartol" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!--确保IE浏览器采用最新的渲染模式-->
<title>充值类型</title>
<link rel="stylesheet" type="text/css" href="${path}/fonts/iconfont.css">
<link rel="stylesheet" type="text/css" href="${path}/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/reset.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/recharge.css" />
<script type="text/javascript">
		var base = "${ctx}";
	</script>
<style type="text/css">
.spinner {
	margin-top: 19px;
}

.pay_close {
	position: absolute;
	right: 21px;
	top: 10px;
	color: #fff;
	font-size: 25px;
}
</style>
</head>
<body>
	<div class="main">
		<div class="common_head">
			<img src="${path}/img/recharge_head.png" />
		</div>
		<!--这是首页-->
		<div class="recharge_body">
			<div class="recharge_body_title">充值方式</div>
			<div class="recharge_body_row">
				<div class="recharge_body_time">
					<i class="iconfont icon-iconfontshichang  choose-time1"></i> <span>按时长购买</span>
				</div>
				<div class="recharge_body_flow">
					<i class="iconfont icon-liuliang choose-time1"></i> <span>按流量购买</span>
					<!--36740176920349-->
				</div>
				<div class="recharge_body_info">
					<i class="iconfont icon-youhui choose-time1"></i> <span>按优惠信息购买</span>
				</div>
			</div>
		</div>
		<div class="buyType_body clearfix" style="display: none;">
			<!--这是左侧套餐详情-->
			<div class="buyType_body_left">
				<div class="title_head">充值方式</div>
				<div class="left_container">
					<div class="buyType_row buyType_time">
						<i class="iconfont icon-iconfontshichang  choose-time2"></i> 按时长购买
					</div>
					<div class="buyType_row buyType_flow">
						<i class="iconfont icon-liuliang choose-time2"></i> 按流量购买
					</div>
					<div class="buyType_row buyType_info">
						<i class="iconfont icon-youhui choose-time2"></i> 按优惠信息购买
					</div>
				</div>
			</div>

			<!--这是左侧套餐详情 end-->
			<!--这是右侧套餐详情-->
			<div class="buyType_body_right" id="time" style="display: none;">
				<div class="title_head">套餐详情</div>
				<i class="iconfont icon-iconfontshichang  choose-time3"></i>
				<div class="title_footr timePay  clearfix">立即购买</div>
				<div class="buyType_body_container">
					<div class="container_row  clearfix">
						<div class="container_rowLeft">时长单位</div>
						<div class="container_rowRight">
							<select class="select" id="select">
								<c:forEach items="${obj.spc.list}" var="p">
									<c:if test="${p.price_type<=3}">
										<option value="${p.price_type}" data-text="${p.describe}"
											mealNum="${p.giveMeal}" mealType="${p.giveMealUnit }"
											valueId="${p.id}" reommend="${p.recommendState}"
											charge_type="${p.charge_type }" prices="${p.unit_price}"
											price_type="${p.price_type}" price_num="${p.price_num}"
											price_name="${p.name}">${p.name}</option>
									</c:if>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="container_row">

					<div class="container_rowLeft">购买单价</div>
					<div class="container_rowRight">
						<span id="money"></span>元
					</div>
				</div>
				<div class="container_row clearfix">
					<div class="container_rowLeft ">购买数量</div>
					<div class="container_rowRight">
						<input id="num" type="text" class="spinnerExample" />
					</div>
					<!--{{allnum}}-->
				</div>
				<div class="container_row">
					<div class="container_rowLeft">赠送时长</div>
					<div class="container_rowRight">
						<span class="containerMealNum" style="color: #adadad;"> </span>
					</div>
				</div>
				<div class="container_rowLast">
					应付总额：<span><span class="total total_b"></span>元</span>
				</div>
			</div>

			<div class="buyType_body_right" id="flow">
				<div class="title_head">套餐详情</div>
				<i class="iconfont icon-liuliang choose-time3"></i>
				<div class="title_footr flowPay  clearfix">立即购买</div>
				<div class="buyType_body_container">
					<div class="container_row  clearfix">
						<div class="container_rowLeft">时长单位</div>
						<div class="container_rowRight">
							<c:forEach items="${obj.spc.list}" var="p">
								<c:if test="${p.price_type>3 && p.price_type<6}">
									<select class="select" id="select2">
										<option value="${p.price_type}" data-text="${p.describe}"
											mealNum="${p.giveMeal}" mealType="${p.giveMealUnit }"
											valueId="${p.id}" reommend="${p.recommendState}"
											charge_type="${p.charge_type }" prices="${p.unit_price}"
											price_type="${p.price_type}" price_num="${p.price_num}"
											price_name="${p.name}">${p.name}</option>
									</select>
								</c:if>
							</c:forEach>
						</div>
					</div>
				</div>
				<div class="container_row">
					<div class="container_rowLeft">购买单价</div>
					<div class="container_rowRight">
						<span id="money1"></span>元
					</div>
				</div>
				<div class="container_row">
					<div class="container_rowLeft">购买数量</div>
					<div class="container_rowRight">
						<input id="num1" type="text" class="spinnerExample" />
					</div>
					<!--{{allnum}}-->
				</div>
				<div class="container_row">
					<div class="container_rowLeft">赠送时长</div>
					<div class="container_rowRight">
						<span class="MealNumrowRight" style="color: #adadad;"> </span>
					</div>
				</div>
				<div class="container_rowLast">
					应付总额：<span><span class="total total_a"></span>元</span>
				</div>
			</div>

			<div class="buyType_body_right" id="info">
				<div class="title_head">套餐详情</div>
				<i class="iconfont icon-youhui choose-time3"></i>
				<div class="info_container">
					<div class="info_container_body">

						<div class="container_row_a">
							<div class="rowTop clearfix">暂未开放该套餐</div>
						</div>
					</div>
				</div>
			</div>
			<!--支付模块-->
			<div class="pay">
				<div class="recharge_body_title">支付方式</div>
				<div class="containerPay">
					<div class="payTop">支付总金额</div>
					<div class="payMoney">
						￥ <span> ${obj.map.amount} </span>
					</div>
					<div class="pay_row clearfix">
						<div class="pay_rowLeft" id="rowLefts">${obj.map.name}</div>
						<div class="pay_rowRight">
							￥ <span> ${obj.map.price} </span>
						</div>
					</div>
					<div class="pay_title">选择支付方式</div>
					<div class="pay_way">
						<div class="pay-zhifubo">
							<input type="radio" checked="checked" name="pay" value="1" /> <img
								src="${path}/img/zhifubao.png"
								style="width: 16px; height: 16px;" /> <span> 支付宝 </span>
						</div>
						<div class="pay-weixin">
							<input type="radio" name="pay" value="0" /> <img
								src="${path}/img/wechat.jpg" style="width: 18px; height: 18px;" />
							<span> 微信支付 </span>
						</div>
						<div class="pay-yinlian">
							<input type="radio" name="pay" value="2" /> <img
								src="${path}/img/7-14012623104L93.png"
								style="width: 16px; height: 16px;" /> <span> 银行卡 </span>
						</div>
					</div>
				</div>
				<div class="pay_footr pay_footr_a">立即支付</div>
			</div>
		</div>
		<div class="buyType_body_right_a" id="info_a">
			<div class="title_head">套餐详情3</div>
			<p style="margin-top: 155px;">您所在的场所目前没有优惠信息</p>
			<p>请按时长或流量购买...</p>
		</div>
		<!--这是右侧套餐详情 end-->
		<!--支付成功模块-->
		<div class="pay_succes">
			<div class="recharge_body_title"></div>
			<div class="pay_succes_title">支付成功！</div>
			<p class="pay_succes_p">成功支付到入账有时可能存在延迟,如未及时到账请</p>
			<p class="pay_succes_p">
				耐心等待，或拨打客服电话<span>15972935811</span>
			</p>
			<div class="checkBalance">查询余额</div>
		</div>
		<div class="alert"></div>
		<div class="pay_Alert" style="display: none;">
			<div class="recharge_body_title">微信支付</div>
			<p class="pay_succes_p" style="margin-top: 100px; font-size: 25px;">
				请使用手机微信扫码完成支付</p>
			<div class="pay_AlertImg">
				<div id="code" style="width: 200px; margin: 56px 270px;"></div>
			</div>
			<div class="pay_close">x</div>
		</div>

	</div>
	<input id="siteId" type="hidden" value="${obj.site.id}">
	<input id="userId" type="hidden" value="${obj.user.id}">
	<input type="hidden" id="username" value="${obj.user.userName}">
	<input id="siteName" type="hidden" value="${obj.site.site_name}">
	<span id="maps" style="display: none;">${obj.map}</span>
	<script src="${path}/js/jquery-1.11.2.min.js" type="text/javascript"
		charset="utf-8"></script>
	<!--加减js-->
	<script src="${path }/js/qrcode.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path}/js/jquery.spinner.js" type="text/javascript"
		charset="utf-8"></script>
	<script type="text/javascript">$('.spinnerExample').spinner({});</script>
	<script src="${path}/js/buyType.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path }/layer/layer/layer.js" type="text/javascript"
		charset="utf-8"></script>
</body>
</html>

