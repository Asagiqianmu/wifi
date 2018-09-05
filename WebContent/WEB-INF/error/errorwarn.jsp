<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.fxwx.util.OssSchoolManage"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="img" value="${ctx}/static/images" />

<!DOCTYPE html>
<html>
<head>
<title>错误页面</title>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,height=device-height,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<script type="text/javascript">
var img = "${img}";
</script>
<style type="text/css">
#tip {
	width: 310px;
/* 	margin: 0 auto;
	margin-top: 20%; */
}

#tip>img {
	width: 100%;
    margin: 100px auto;
    margin-left: 0;
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    -webkit-transform: translateX(-50%);
    -moz-transform: translateX(-50%) translateY(-50%);
    -ms-transform: translateX(-50%) translateY(-50%);
}

#text_tip, #text_top {
	color: red;
	font-size: 14px;
	margin-top: 15%;
	text-align: center;
}

.copyright {
	position: absolute;
	bottom: 0;
	left: 0;
	right: 0;
	font-size: 12px;
	text-align: center;
}

@media only screen and (min-width: 401px) and (max-width: 720px) {
	#tip {
		width: 310px;
	}
	.copyright {
		position: absolute;
		bottom: 35px;
		font-size: 14px;
	}
}

@media only screen and (min-width: 721px)and (max-width: 879px) {
	#tip {
		width: 500px;
		margin: 0 auto;
		margin-top: 20%;
	}
	.copyright {
		position: absolute;
		bottom: 30px;
		font-size: 14px;
	}
	#text_tip {
		margin-top: 8%;
	}
	#text_top {
		margin-top: 8%;
	}
}

@media ( min-width : 879px)and (max-width: 1067px) {
	#text_tip {
		margin-top: 8%;
	}
	#text_top {
		margin-top: 8%;
	}
	#tip {
		width: 600px;
		margin: 0 auto;
		margin-top: 10%;
	}
	.copyright {
		position: absolute;
		bottom: 35px;
		font-size: 16px;
	}
}

@media ( min-width : 1067px) {
	#text_tip {
		margin-top: 8%;
	}
	#text_top {
		margin-top: 8%;
	}
	#tip {
		width: 600px;
		margin: 0 auto;
		margin-top: 5%;
	}
	#tip>img {
		width: 40%;
		margin: 10px auto;
		margin-left: 27%;
	}
	.copyright {
		position: absolute;
		bottom: 35px;
		font-size: 16px;
	}
}
</style>
</head>
<body>
	<div id="text_top">错误提示页面</div> 
	<div id="tip">
		<img alt="Unite-WiFi" src="${img}/404.gif" border=0>
	</div>
	<c:forEach items="${obj.map}" var="p">

		<div id="text_tip">${p.value}</div>
	</c:forEach>
	<div class="copyright">服务支持：15972935811| 飞讯无限提供计费服务</div>
</body>
</html>