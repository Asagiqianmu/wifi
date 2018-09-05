<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<title>意见反馈</title>
<meta name="viewport"
	content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no;">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" href="${ctx }/web_css/reset.css" />
<link rel="stylesheet" href="${ctx }/web_css/wireless.css" />
<link rel="stylesheet" href="${ctx }/web_css/portal.css" />
<script type="text/javascript"  src="${ctx }/partol/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx }/static/layer/layer/layer.js"></script>
<script type="text/javascript"  src="${ctx }/web_js/wireless.js"></script>
<script type="text/javascript"  src="${ctx }/web_js/validator.js"></script>
<script type="text/javascript">
	$(function() {
		$(".c_til").click(function() {
			$(".c_con").toggle(50);
		});
	})
	function intoSelectedType(obj, typeNum) {
		$(".chose .c_til span").text($(obj).text());
		$(".c_con").toggle(50);
		$("input[name='type']").val(typeNum);
	}
	function submitForm() {
		var type = $("input[name='type']").val();
		if (type == "-1") {
			$(".ZindexTip,.ZindexTipAlert").show();
			$("#alertText").text("请先选择反馈类型")
			setTimeout(function() {
				$(".ZindexTip,.ZindexTipAlert").hide();
			}, 1000); /*几秒后消失，时间自己定  1000是一秒*/
			return false;
		}
		var content = $("textarea[name='content']").val();
		if ($.trim(content) == "" || content == "意见或建议（140字以内）") {
			$(".ZindexTip,.ZindexTipAlert").show();
			$("#alertText").text("请输入反馈内容")
			setTimeout(function() {
				$(".ZindexTip,.ZindexTipAlert").hide();
			}, 1000); /*几秒后消失，时间自己定  */
			return false;
		}
		var wifiSite = $("input[name='wifiSite']").val();
		if ($.trim(wifiSite) == "" || wifiSite == "请输入你的地点") {
			$(".ZindexTip,.ZindexTipAlert").show();
			$("#alertText").text("请输入你的地点")
			setTimeout(function() {
				$(".ZindexTip,.ZindexTipAlert").hide();
			}, 1000); /*几秒后消失，时间自己定  */
			return false;
		}
		var contactWay = $("input[name='contactWay']").val();
		if (!verifyMobile(contactWay)) {
			$(".ZindexTip,.ZindexTipAlert").show();
			$("#alertText").text("请输入有效的手机号码")
			setTimeout(function() {
				$(".ZindexTip,.ZindexTipAlert").hide();
			}, 1000); /*几秒后消失，时间自己定  */
			return false;
		}
		
		$.ajax({
			type:"post",
			url:"${ctx}/login/save_feedback",
			data:{
				"type" : type,
				"content" : content,
				"wifiSite" : wifiSite,
				"contactWay" : contactWay
			},
			success:function(data){
				if (data.msg == "ok") {
					$('.select_video').fadeIn("fast").delay(2000).hide(200);
					$('.select_bg').fadeIn("slow").delay(2000).hide(200,
							function() {
								history.go(-1);
							});
				} else {
					$(".ZindexTip,.ZindexTipAlert").show();
					$("#alertText").text("保存失败，请重试")
					setTimeout(function() {
						$(".ZindexTip,.ZindexTipAlert").hide();
					}, 1000); /*几秒后消失，时间自己定  */
					return false;
				}
			}
			
			
		})
		
		/* $.post("${ctx}/logon/save_feedback", {
			"type" : type,
			"content" : content,
			"wifiSite" : wifiSite,
			"contactWay" : contactWay
		}, function(data) {
			if (data.msg == "ok") {
				$('.select_video').fadeIn("fast").delay(2000).hide(200);
				$('.select_bg').fadeIn("slow").delay(2000).hide(200,
						function() {
							history.go(-1);
						});
			} else {
				alert("保存失败，请重试");
			}
		}); */
	}
</script>
</head>
<body>
	<div class="bodyheight">

		<!--头部-->
		<div class="select_bg"></div>
		<div class="select_video">
			<div class="select_box">
				<div class="select_titile">谢谢您的反馈！</div>
				<p>我们将在第一时间处理您的反馈信息！</p>
			</div>
		</div>
		<!--标题1-->
		<div class="header_feed header_feed_but">
			<div class="back_but" id="home_buts">
				<a href="javascript:history.go(-1);"><img
					src="${ctx}/static/images/back.png" /></a>
			</div>
		</div>
		<div class="clear"></div>
		<div class="header_feed header_feed_bg">
			<p>意见反馈</p>
		</div>
		<!--主体-->

		<div class="feed_box">
			<div class="feed_title">反馈类型：</div>
			<div class="chose">
				<div class="c_til">
					<span>请选择</span><img src="${ctx}/static/images/chose.png"
						id="c_img" />
				</div>
				<div class="c_con">
					<ul>
						<li onclick="intoSelectedType(this,1);">网络问题</li>
						<li onclick="intoSelectedType(this,2);">使用问题</li>
						<li onclick="intoSelectedType(this,3);">其它</li>
					</ul>
				</div>
			</div>
			<input type="hidden" name="type" value="-1" />

			<div class="clear"></div>
			<div class="feed_title">反馈内容：</div>
			<textarea name="content" id="textarea" class="feedtext" cols="45"
				rows="5" maxlength="140">意见或建议（140字以内）</textarea>

			<div class="feed_title">wifi所在位置：</div>
			<input type="text" class="map" name="wifiSite" value="请输入你的地点"
				maxlength="100" />

			<div class="feed_title">手机号码：</div>
			<input type="text" class="number" name="contactWay" value="请输入手机号" />

			<br /> <br />
			<div class="back_portal" onclick="submitForm();">确定</div>
		</div>


	</div>

	<!--登录框提示语-->
	<div class="ZindexTip"></div>
	<div class="ZindexTipAlert">
		<div class="center_bg" style="height: 88%;">
			<img class="center_bgImg" src="${ctx}/static/images/logotip.png" /> <img
				class="center_bgImg2" style="width: 90%; margin-left: 5%;"
				src="${ctx}/static/images/alertbg.png" />
			<!-- 这里面通过js控制文本切换提示语 -->
			<div id="alertText">手机号码格式不对</div>
		</div>
	</div>
</body>
</html>