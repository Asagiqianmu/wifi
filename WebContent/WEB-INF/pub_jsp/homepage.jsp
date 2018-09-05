<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<title>登陆完成</title>
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/portal.css" />
<!--这是轮播图  swiper插件css-->
<link rel="stylesheet" href="${ctx}/dist/css/swiper.min.css">
<script src="${ctx}/partol/js/jquery.min.js" type="text/javascript"
	charset="utf-8"></script>
<!-- Swiper JS -->
<script src="${ctx}/dist/js/swiper.min.js"></script>
</head>
<body class="homepage" unload="alert('window closed')"
	onload="time_fun()">
	<!-- <div class="finishHead1">
			180.240.89.119
		</div>
		<div  class="finishHead2">
			CMCC_Unite
		</div>
		<div style="width: 100%;background: #fff;">
			<div class="finishNav clearfix">
	            <div  class="finishNavL">
	               <div class="finishNavBack">
	               	<
	               </div>
	            </div>
	            <div  class="finishNavC">登陆</div>
	            <div  class="finishNavR">完成</div>
			</div>
		</div> -->
	<!--这是轮播图  swiper插件-->
	<div id="FinishSwiper">
		<!-- Swiper -->
		<div class="swiper-container">
			<div class="swiper-wrapper">
				<div class="swiper-slide">
					<img src="${ctx}/static/images/bg.png" />
				</div>
				<div class="swiper-slide">
					<img src="${ctx}/static/images/portal_bg.png" />
				</div>
			</div>
			<!-- Add Pagination -->
			<div class="swiper-pagination"></div>
		</div>
	</div>

	<div class="loginID clearfix">
		<div class="loginIdName">登录账号 :</div>
		<div class="loginIdText">${sessionScope.userName}</div>
	</div>
	<div class="timing clearfix">
		<div class="timingName">
			上网计时 :</span>
		</div>
		<div class="timingText">
			<span id="mytime"></span>
		</div>
	</div>
	<!-- <div class="tips clearfix">
			<div class="TipsText">欢迎使用免费WIFI</div>
		</div> -->
	<div class="TipsText">欢迎使用免费WIFI</div>
	<a href="${ctx}/login/inquiry"> <!--这个路径你自己引  -->
		<div class="finishHotspot">
			<img src="${ctx}/static/images/Hot-query@2x.png" />
		</div>
		<div
			style="margin-top: 10px; text-align: center; color: #fff; font-size: .12rem;">
			周边热点</div>
	</a>
	<div class="finishBottom">中国移动通信有限公司</div>

	<!-- Initialize Swiper -->
	<script>
	    var swiper = new Swiper('.swiper-container', {
	        pagination: '.swiper-pagination',
	        paginationClickable: true,
	        autoplay: 3000
	    });
    </script>

	<script type="text/javascript">
    	$(function  () {
    	//这是手机号码中间的4位****
	    	var phone = $('.loginIdText').text(); 
		    var mphone1=phone.replace(/\s/g,'');
		    var mphone_first=mphone1.substring(0,3);
		    var mphone_last=mphone1.substring(7,11);
		    var mphone = mphone1.substring(3,7);
		    mphone=mphone.replace(mphone,"****")
		//  此时mphone 的值就是中间四位显示为*号的了；
		    $('.loginIdText').text(mphone_first+mphone+mphone_last)
    	});
    </script>
	<!-- 计时器js函数 -->
	<script>
        function two_char(n) {
            return n >= 10 ? n : "0" + n;
        }
        function time_fun() {
            var sec=0;
            setInterval(function () {
                sec++;
                var date = new Date(0, 0)
                date.setSeconds(sec);
                var h = date.getHours(), m = date.getMinutes(), s = date.getSeconds();
                document.getElementById("mytime").innerText = two_char(h) + ":" + two_char(m) + ":" + two_char(s);
            }, 1000);
        }
    </script>

	<script type="text/javascript">
			    //禁止页面按F5刷新
			document.onkeydown = function(e){  
				e = window.event || e;  
				var keycode = e.keyCode || e.which;  
				if(keycode == 116){  
				   if(window.event){// ie  
				       try{e.keyCode = 0;}catch(e){}  
				       e.returnValue = false;  
				   }else{// firefox  
				       e.preventDefault();  
				   }  
			}  
	    }
		    
	 </script>
</body>
</html>
