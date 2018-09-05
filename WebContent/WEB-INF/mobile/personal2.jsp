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
<script type="text/javascript" src="${path }/js/banner.js"></script>
<script type="text/javascript"
	src="${path }/layer.mobile-v2.0/layer_mobile/layer.js"></script>

<!--进度条-->
<script type="text/javascript" src="${path}/js/jqmeter.min.js"></script>
<style type="text/css">
#banner {
	position: relative;
	width: 100%;
	height: 2.91rem;
	overflow: hidden;
}

#banner_list {
	height: 100%;
}

#banner_list img {
	border: 0px;
	width: 100%;
	height: 100%;
}

#banner ul {
	position: absolute;
	z-index: 9;
	margin: 0;
	padding: 0;
	left: 50%;
	margin-left: -0.58rem;
	right: 0;
	bottom: 0.09rem;
	width: 1.79rem;
}

#banner ul li {
	padding: 0px 0.05rem;
	width: 0.15rem;
	height: 0.15rem;
	border-radius: 50%;
	border: 1px #fff solid;
	color: #FFF;
	cursor: pointer;
	float: left;
	margin: 0 8px;
}

#banner ul li.on {
	background-color: #fff;
}

.phone, .Countdown {
	margin-left: 10%;
	margin-top: 5%;
	font-size: 18px;
}

.user-prompt {
	border: 1px #efefef solid;
	margin: 0 0.15rem;
	margin-top: 1rem;
	text-align: center;
}

.user-prompt p {
	margin: 0.1rem;
	background: #efefef;
	font-size: 0.28rem;
	padding: 0.2rem 0;
}

.copyright {
	position: absolute;
	bottom: 0.15rem;
	left: 0;
	z-index: 9999;
	right: 0;
	text-align: center;
}
</style>

</head>
<script type="text/javascript">
	var ctx = "${ctx}";
</script>
<body unload="alert('window closed')" onload="time_fun()">
	<div class="main">
		<div id="banner">
			<div id="banner_list">
				<c:if test="${obj.map.site.bannerUrl==''}">
					<li><img src="${path}/img/ban1.jpg" width="100%" height="100%" /></li>
					<li><img src="${path}/img/bg.jpg" width="100%" height="100%" /></li>
					<li><img src="${path}/img/1.jpg" width="100%" height="100%" />
					</li>
				</c:if>
				<c:if test="${obj.map.site.bannerUrl!=''}">
					<c:forEach items="${obj.map.site.bannerUrl.split(':')}" var="p">
						<li><img src="${ctx}/${p}" width="100%" height="100%" /></li>
					</c:forEach>
				</c:if>
			</div>
		</div>
		<div class="personal-data perposition">
			<div class="phone">
				登录账号：<span class="numPhone">${obj.map.user.userName}</span>
			</div>
			<div class="Countdown">
				上网计时：<span id="mytime"></span>
			</div>
		</div>
		<div class="user-prompt" style="width: 80%; margin: 15% auto;">
			<p id="aaaa">欢迎使用中国移动WIFI</p>
		</div>

	</div>
	<div class="copyright">中国移动通信有限公司</div>
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
