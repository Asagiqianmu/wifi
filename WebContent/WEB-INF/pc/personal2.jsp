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
<title>个人中心</title>
<link rel="stylesheet" href="${path }/bootstrap/css/bootstrap1.min.css">
<link rel="stylesheet" type="text/css"
	href="${path }/fonts/iconfont.css">
<link rel="stylesheet" type="text/css" href="${path }/css/reset.css" />
<link rel="stylesheet" type="text/css" href="${path }/css/recharge.css" />
<style type="text/css">
#center {
	position: fixed;
	width: 348px;
	height: 268px;
	background: #FFF;
	border-radius: 5px;
	top: 50%;
	left: 50%;
	margin-left: -195px;
	margin-top: -155px;
	z-index: 40001;
}

.phone, .Countdown {
	font-size: 18px;
	margin-top: 20px;
	margin-left: 119px;
}

.user-prompt {
	border: 1px #efefef solid;
	margin: 0 0px;
	text-align: center;
}

.user-prompt p {
	margin: 0px;
	background: #efefef;
	font-size: 0.28rem;
	padding: 0.2rem 0;
}

.copyright {
	position: absolute;
	bottom: 30px;
	left: 0;
	font-size: 14px;
	z-index: 9999;
	right: 0;
	text-align: center;
}
</style>
</head>
<body unload="alert('window closed')" onload="time_fun()">
	<div>
		<div class="user-prompt"
			style="width: 80%; margin: 0 auto; margin-bottom: 20%;">
			<p id="aaaa">欢迎使用中国移动WIFI</p>
		</div>



	</div>
	<div id="center">
		<div class="phone">
			登录账号：<span class="numPhone">${obj.map.user.userName}</span>
		</div>
		<div class="Countdown">
			上网计时：<span id="mytime"></span>
		</div>
	</div>
	<div class="copyright">中国移动通信有限公司</div>

	<script>
    	var phone = $('.numPhone').text();
	    var phone=phone.replace(/\s/g,'');
	    var mphone_first=phone.substring(0,3);
	    var mphone_last=phone.substring(7,11);
	    var mphone = phone.substring(3,7);
	        mphone=mphone.replace(mphone,"****")
	//  此时mphone 的值就是中间四位显示为*号的了；
	    $('.numPhone').text(mphone_first+mphone+mphone_last)
    </script>
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
