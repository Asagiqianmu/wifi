<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.fxwx.util.OssSchoolManage"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/partol" />
<!doctype html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>个人中心页面</title>
<link rel="stylesheet" type="text/css" href="${path}/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/public.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/personal.css" />
<link rel="stylesheet" type="text/css" href="${path}/fonts/iconfont.css" />
<!--图标样式-->
<script type="text/javascript" src="${path}/js/jquery.min.js"></script>
<script type="text/javascript" src="${path}/js/personal.js"></script>
<script type="text/javascript"
	src="${path }/layer.mobile-v2.0/layer_mobile/layer.js"></script>

<!--进度条-->
<script type="text/javascript" src="${path}/js/jqmeter.min.js"></script>
<style type="text/css">
/*进度条*/
.progress {
	width: 44%;
	height: 10px;
	margin-left: 3%;
	margin-right: 4%;
	background: grey;
	padding: 2px;
	overflow: visible;
	border-radius: 20px;
	border-top: 1px solid grey;
	border-bottom: 1px solid #7992a8;
	float: left;
}

.progress .progress-bar {
	border-radius: 20px;
	position: relative;
	animation: animate-positive 2s;
}

.progress .progress-value {
	display: block;
	padding: 3px 7px;
	font-size: 13px;
	color: #fff;
	border-radius: 4px;
	background: #191919;
	border: 1px solid #000;
	position: absolute;
	top: -40px;
	right: -10px;
}

.progress .progress-value:after {
	content: "";
	border-top: 10px solid #191919;
	border-left: 10px solid transparent;
	border-right: 10px solid transparent;
	position: absolute;
	bottom: -6px;
	left: 26%;
}

.progress-bar.active1 {
	animation: reverse progress-bar-stripes 0.40s linear infinite,
		animate-positive 2s;
}

@
-webkit-keyframes animate-positive { 0% {
	width: 0;
}

}
@
keyframes animate-positive { 0% {
	width: 0;
}

}
.buy-rest i {
	float: left;
	font-size: 0.5rem;
	height: 0.27rem;
	line-height: 0.4rem;
	!
	important;
}

.progress .progress-value1 {
	display: block;
	padding: 0px 0px;
	font-size: 0.14rem;
	color: #333;
	border-radius: 4px;
	position: absolute;
	top: -25px;
	right: -15px;
	line-height: 16px;
}
</style>

</head>
<script type="text/javascript">
	var ctx = "${ctx}";
</script>
<body unload="alert('window closed')">
	<div class="main">
		<div class="mask opacity"></div>
		<div class="personal-title">
			<i class="iconfont icon-icon08"></i><em id="emCount"></em>
		</div>
		<div class="personal-data perposition">
			<ul>
				<li>
					<div class="personal-head">
						<div class="perheadpic">
							<c:if test="${obj.map.user.imageUrl!=''}">
								<img
									src="http://oss.kdfwifi.net/user_pic/${obj.map.user.imageUrl}"
									alt="" id="photo" />
							</c:if>
							<c:if test="${ obj.map.user.imageUrl==''}">
								<img src="${ctx}/system/user/img.jpg" alt="" id="photo" />
							</c:if>
						</div>
						<div class="perheadtext">
							<p></p>
							<input type="hidden" id="username"
								value="${obj.map.user.userName}"> <input type="hidden"
								id="userId" value="${obj.map.user.id}"> <input
								type="hidden" id="siteId" value="${obj.map.site.id}"> <input
								type="hidden" id="siteType" value="${obj.map.type}"> <input
								type="hidden" id="sysType" value="${sessionScope.solarsys}">
							<input type="hidden" id="allIp" value="${obj.map.type.uamip}">
							<span> 完善个人资料送免费时长</span>
						</div>
					</div>
					<div class="arrow-rightjump">
						<i class="iconfont icon-arrow-right" onclick="rightButton()"></i>
					</div>
				</li>
			</ul>
		</div>
		<div class="buy-rest">
			<h3>购买剩余</h3>
			<div class="buy-time clearfix">
				<i class="iconfont icon-shijian"></i>
				<div class="progress">
					<div
						class="progress-bar progress-bar-success progress-bar-striped active1"
						id="hidepcent-a">
						<div class="progress-value1" id="hidepcent">0%</div>
					</div>
				</div>
				<p id="dayTime"></p>
			</div>
			<div class="buy-flow clearfix">
				<i class="iconfont icon-0067rili-copy"></i>
				<div class="progress">
					<div
						class="progress-bar progress-bar-success progress-bar-striped active1"
						id="showpcent-a">
						<div class="progress-value1" id="showpcent">0%</div>
					</div>
				</div>
				<p id="flow"></p>
			</div>

		</div>
		<div class="repaid clearfix">
			<ul>
				<li class="active"><a href="javascript:;"><i
						class="iconfont icon-chongzhi"></i><span>充值</span></a></li>
				<li class="payrecomed"><a href="javascript:;"><i
						class="iconfont icon-jilu" id="fontSize"></i><span>充值记录</span></a></li>
				<li class="offline"><a href="javascript:;" id="editnamemodle"><i
						class="iconfont icon-xiaxian"></i><span>下线</span></a></li>
				<li><a href="javascript:;"><i class="iconfont icon-wangluo"
						onclick="surfInterint()"></i><span>去上网</span></a></li>
			</ul>
		</div>
		<div class="personal-data">
			<ul>
				<li id="accountMange">账号管理
					<div class="arrow-right">
						<i class="iconfont icon-arrow-right"></i>
					</div>
				</li>
				<li id="gameCenter">游戏中心
					<div class="arrow-right">
						<i class="iconfont icon-arrow-right"></i>
					</div>
				</li>
				<li id="touchCustom">联系客服
					<div class="arrow-righttel">
						<i class="iconfont icon-arrow-right"></i>
					</div>
				</li>
			</ul>
		</div>
		<div class="fillname">
			<div class="fillnamemoble"></div>
			<div class="fillnamewrap">
				<h2>您确定要wifi下线吗</h2>
				<div class="fillnamebutton clearfix">
					<a href="#" id="close" onclick="closeLayer('fillname')">取消</a> <a
						href="#" onclick="determine()" id="nikelink">确认</a>
				</div>
			</div>
		</div>
		<!--联系客服-->
		<div class="business-phone">
			<p class="bp-title">联系客服</p>
			<a href="tel:${obj.map.site.adminer}">${obj.map.site.adminer}</a>
			<p class="bp-cancle">取消</p>
		</div>
	</div>
	<script type="text/javascript">
		var code="${obj.map.code}";
		if(code==202){
			//询问框
			layer.open({
				content : '你的帐户余额为0，请先充值再来上网。',
				btn : [ '取消', '立即充值' ],
				yes : function(index) {
					//location.reload();//刷新的效果
					layer.close(index);
				},
				no : function() {
					window.location.href=ctx+"/priceConfig/toType?siteId="+$("#siteId").val()+"&userId="+$("#userId").val();
				}
			});
			
		}
	</script>

	<script>
		var base = "${ctx}";
		(function(doc, win) {
			var docEl = doc.documentElement, resizeEvt = 'orientationchange' in window ? 'orientationchange'
					: 'resize', recalc = function() {
				var clientWidth = docEl.clientWidth;
				if (!clientWidth)
					return;
				if (clientWidth >= 750) {
					docEl.style.fontSize = '100px';
				} else {
					docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
				}
			};
			if (!doc.addEventListener)
				return;
			win.addEventListener(resizeEvt, recalc, false);
			doc.addEventListener('DOMContentLoaded', recalc, false);
		})(document, window);
	</script>
	<script type="text/javascript">
		var base = "${ctx}";
		var userName = $("#username").val(), siteId = $("#siteId").val();
		//显示wifi下线弹出框
		$('#editnamemodle').click(function() {
			$('.fillname').fadeIn();
		});
		//关闭wifi下线弹出框
		function closeLayer(close) {
			$("." + close).fadeOut();
		}
		//点击确定按钮进行下线操作
		function determine() {
			$.ajax({
				url : base + "/personal/offLine",
				data : {
					sys:$("#sysType").val(),
					allIp:$("#allIp").val()
				},
				dataType : "json",
				success : function(data) {
					if (data.result == "0") {
						$('.fillname').fadeOut();
					} else {
						$('.fillname').fadeOut();
						window.location.href = data.purl.offlineUrl;
					}
				}
			})
		}
		//跳转消息列表
		$('.icon-icon08').click(
				function() {
					window.location.href = base
							+ '/message/messageNotification?userName='
							+ userName + '&siteId=' + siteId;
				});
		$(function() {
			var totalFlow;//购买总流量
			var usedFlow;//已使用流量
			var zDayHour;//购买的总小时
			var unusedTime;//已使用的小时
			$.ajax({
				url : base + "/personal/getResidualTime",
				data : {
					userName : userName,
					siteId : siteId
				},
				dataType : "json",
				
				success : function(data) {
					$("#dayTime").text(data.dayTime);//显示总天数
					$("#flow").text(data.flow);//显示总流量
					totalFlow = data.totalFlow;//总流量
					usedFlow = data.syflow;//剩余流量
					zDayHour = data.zDayHour;//总时间
					unusedTime = data.unusedTime;//剩余时间
					var resdiueTime=unusedTime==0?0:(Math.floor((unusedTime/zDayHour) * 100) / 100);//时间比例
					var resdiueFlow=usedFlow==0?0:(Math.floor(parseInt(usedFlow)/parseInt(totalFlow)*100)/100);//流量比例
					$("#showpcent-a").css("width",resdiueFlow*100+"%");
					$("#showpcent").text(resdiueFlow*100+"%");
					$("#hidepcent-a").css("width",resdiueTime*100+"%");
					$("#hidepcent").text(resdiueTime*100+"%"); 
					
				}
			});
			
		});
	</script>
</body>
</html>
