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
<title>轮播图</title>
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<script type="text/javascript" src="${pathjs }/jquery.min.js"></script>
<script type="text/javascript" src="${pathjs }/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="${statics}/css/bootstrap.min.css" />

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
.main{
	width: 100%;
	height: 100%;
	position:fixed;
	background-color: #404040;
}
.conter{
	width: 90%;
	height: 95%;
	transform: translateX(5%) translateY(2.5%);
	-ms-transform:translateX(5%) translateY(2.5%); 	/* IE 9 */
	-moz-transform:translateX(5%) translateY(2.5%); 	/* Firefox */
	-webkit-transform:translateX(5%) translateY(2.5%); /* Safari 和 Chrome */
	-o-transform:translateX(5%) translateY(2.5%); 
	background-color:#fff;
	background: url(${statics}/images/slideshow_bg.png) no-repeat 100%;
}

.closee{
	position: fixed;
	float: right;
	right: -15px;
    top: -14px;
}

.closee img{
	width: 2.5rem;
}

.images img {
    width: 80%;
    margin-left: 10%;
 	transform: translateY(5%);
	-ms-transform:translateY(5%);
	-moz-transform:translateY(5%); 
	-webkit-transform:translateY(5%); 
	-o-transform:translateY(5%);
}


.carousel-inner{
	position: initial;
	overflow: initial;
}

.carousel-indicators{
    bottom: -15px;
	/* background-color: #000; */
}

.carousel-indicators li{
	border: 1px solid #fff;
}

.carousel-indicators .active{
	background-color: #fff;
}

.buttonn{
	width: 100%;
}

.buttonn_on{
	position: absolute;
	width: 100px;
	height: 50px;
	background-color: #000;
	margin-left: 50%;
}

@media (min-width: 320px){

		.images img {
	    width: 80%;
	    margin-left: 10%;
/* 	 	transform: translateY(10%);
		-ms-transform:translateY(10%);
		-moz-transform:translateY(10%); 
		-webkit-transform:translateY(10%); 
		-o-transform:translateY(10%); */
		}

 }
 @media (min-width: 360px){

		.images img {
	    width: 80%;
	    margin-left: 10%;
 	 	transform: translateY(15%);
		-ms-transform:translateY(15%);
		-moz-transform:translateY(15%); 
		-webkit-transform:translateY(15%); 
		-o-transform:translateY(15%); 
		}

 }
@media (min-width: 768px){

		.images img {
	    width: 65%;
	    margin-left: 17.5%;
	 	transform: translateY(10%);
		-ms-transform:translateY(10%);
		-moz-transform:translateY(10%); 
		-webkit-transform:translateY(10%); 
		-o-transform:translateY(10%);
		}

 }
</style>
</head>
<body>
	<div class="main">
		<div class="conter">
			<div class="closee" id="close"><img alt="" src="${statics}/images/close.png"></div>
			<div class="carousel-inner images">
				
				<ol class="carousel-indicators">
		            <li data-target="#main_ad" data-slide-to="0" class="active"></li>
		            <li data-target="#main_ad" data-slide-to="1"></li>
		            <li data-target="#main_ad" data-slide-to="2"></li>
		            <li data-target="#main_ad" data-slide-to="3"></li>
		            <li data-target="#main_ad" data-slide-to="4"></li>
     	 		</ol>
     	 		
				<div class="item active">
					<img alt="" src="${statics}/images/slideshow1.png">
				</div>
				<div class="item">
					<img alt="" src="${statics}/images/slideshow2.png">
				</div>
				<div class="item">
					<img alt="" src="${statics}/images/slideshow3.png">
				</div>
				<div class="item">
					<img alt="" src="${statics}/images/slideshow4.png">
				</div>
				<div class="item">
					<img alt="" src="${statics}/images/slideshow5.png">
				</div>
				
				
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">

		$(document).ready(function(){
			var startX,endX;//声明触摸的两个变量
			var offset = 100;//声明触摸距离的变量
			$('.conter').on('touchstart',function (e) {
				startX= e.originalEvent.touches[0].clientX;//当触摸开始时的x坐标；
			});
			$('.conter').on('touchmove',function (e) {
				endX = e.originalEvent.touches[0].clientX;//当触摸离开时的x坐标；
			});
			$('.conter').on('touchend',function (e) {
				//当触摸完成时进行的事件；
				var distance = Math.abs(startX - endX);//不论正负，取值为正值；
				if (distance > offset){
				    if(startX > endX){
				        $(this).carousel('next');//当开始的坐标大于结束的坐标时，滑动到下一附图
				    }else{
				      	$(this).carousel('prev');//当开始的坐标小于结束的坐标时，滑动到上一附图
				    }    	
				}
			});	
		});
		
		$("#close").click(function(){
			$(".conter").css("display","none");
		})
</script>
</html>