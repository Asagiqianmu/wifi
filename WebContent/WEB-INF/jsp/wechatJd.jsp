<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pathjs" value="${ctx}/web_js" />
<c:set var="pathcss" value="${ctx}/web_css" />
<c:set var="statics" value="${ctx}/static" />
<!DOCTYPE HTML>
<html>
<head lang="zh-CN">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>正在跳转到微信</title>
<link type="text/css" href="${pathcss}/base.css" rel="stylesheet" />
<!-- <link rel="stylesheet"
	href="http://wifi.weixin.qq.com/resources/css/style-simple-follow.css" /> -->
<link rel="stylesheet" href="${pathcss}/style-simple-follow.css" />

<style type="text/css">
.chat_h {
	padding: 45px 0 30px 0;
	text-align: center;
}

.chat_h .going {
	color: #333333;
	font-size: 18px;
	margin: 25px 50px 0 50px;
	text-align: center;
	line-height: 1.6;
}

.chat_h .goes {
	color: #666666;
	font-size: 15px;
	margin-top: 15px;
}

.bar_progress {
	margin: 25px 30px 0 30px;
	background: #eeeeee;
	height: 7px;
	border-radius: 8px;
}

.bar_progress div {
	background: url(${statics}/images/progress.png) repeat-x;
	height: 7px;
	display: block;
	border-radius: 8px;
	width: 0;
}

.chat_txt {
	font-size: 12px;
	color: #e55d5d;
	margin: 15px 30px 25px 30px;
	text-align: left;
	line-height: 1.8;
}

.time {
	color: #666666;
	font-size: 12px;
	margin-top: 20%;
}
</style>
<script type="text/javascript">
	var ctx = "${ctx}";
	var extend1 = '${obj.extend}';
</script>
</head>

<body>
	<div class="chats">
		<div class="chat_h" id="phone">
			<img src="${statics}/images/chat_go.png" alt="" />
			<div class="going">WIFI连接成功后需关注公众号才可完成进行上网</div>
			<div class="time">正在跳转到微信（若跳转未成功，请手动打开微信）</div>
			<div class="bar_progress">
				<div></div>
			</div>
		</div>

		<div class="chat_h chat_hs" id="pc" style="display: none;">
			<img src="${statics}/images/wifi_icon.png" alt="" />
			<div class="going">WIFI连接成功后即可进行上网</div>
			<div class="time">正在连接WIFI，请稍等...</div>
			<div class="bar_progress">
				<div></div>
			</div>
		</div>
		<input name="extend" value='${obj.json}' type="hidden" id="siteType">
		<input name="appId" value='${obj.extend.appId}' type="hidden" id="appId">
		<input name="nasid" value='${obj.extend.nasid}' type="hidden" id="nasid">
		<input name="params" value='${obj.param}' type="hidden" id="param">
	</div>
</body>
<!-- 唤起微信的js  必须的 -->
<script type="text/javascript" src="${pathjs}/jquery.min.js"></script>
<script type="text/javascript" src="${pathjs}/MD5.js"></script>
<script type="text/javascript">
	var loadIframe = null;
	var noResponse = null;
	var callUpTimestamp = 0;

	function putNoResponse(ev) {
		clearTimeout(noResponse);
	}

	function errorJump() {
		var now = new Date().getTime();
		if ((now - callUpTimestamp) > 4 * 1000) {
			return;
		}
		/* alert('该浏览器不支持自动跳转微信请手动打开微信\n如果已跳转请忽略此提示'); */
	}

	myHandler = function(error) {
		errorJump();
	};

	function createIframe() {
		var iframe = document.createElement("iframe");
		iframe.style.cssText = "display:none;width:0px;height:0px;";
		document.body.appendChild(iframe);
		loadIframe = iframe;
	}
	function notice() {
		$("#isMobile").show();
		$("#phoneico").hide();
		$("#phone").hide();
	}
	
	$(function() {
		var appId = "${obj.appId}";
		var extend = '${obj.extend}';
		var timestamp = "${obj.timestamp}"; //时间戳(毫秒)
		var shopId = "${obj.shopId}"; //AP设备所在门店的ID
		var extend = '${obj.extend}';
		var authUrl = "${obj.url}"; //认证服务端URL
		var mac = "${obj.mac}"; //用户手机mac地址 安卓设备必需
		var ssid = "${obj.ssid}"; //AP设备信号名称，必须
		var bssid = "${obj.bssid}"; //AP设备mac地址，非必须
		var sign = "${obj.sign}";
		var code = "${obj.code}";//使用时间是否到期返回码
		var base = "${ctx}";
		var key='${obj.data}';
		var siteId='${obj.siteId}';
		var userMac='${obj.userMac}';  
		var nasid='${obj.nasid}';
		var allMac='${obj.extend.allMac}';
		var userIp='${obj.userIp}';
		var tag='${obj.tag}';
		if(code==203){//老用户,使用时间没有到期
			Wechat_GotoRedirect(appId, extend, timestamp, sign, shopId,
					authUrl, mac, ssid, bssid);
			var s = setInterval(function checkUser() {
				$.ajax({
					type : "post",
					url : base + "/PROT/checkWxUser",
					async : false,
					data : {
						as : $("#siteType").val(),
						userMac : $("#userMac").val(),
						siteId : $("#siteId").val(),
						appId : $("#appId").val(),
						nasid : $("#nasid").val()
					},
					success : function(data) {
						if (data.code == 200) {
							window.clearTimeout(s);
							location.href = data.url;
						}
					}
				});
			}, 1000);
		}else if(code==202){//老用户,使用时间到期,跳转到支付页面  
			window.location.href = base+"/PROT/choiceTc?key="+key+"&siteId="+siteId+"&userMac="+userMac+"&nasid="+nasid+"&allMac="+allMac+"&userIp="+userIp+"&tag="+tag;
		}else if(code==204){//新用户 ,跳转到支付页面
			window.location.href = base+"/PROT/choiceTc?key="+key+"&siteId="+siteId+"&userMac="+userMac+"&nasid="+nasid+"&allMac="+allMac+"&userIp="+userIp+"&tag="+tag;
		}
	});
	
	/* Wechat_GotoRedirect(appId, extend, timestamp, sign, shopId,
	authUrl, mac, ssid, bssid); */
	/* var s=setInterval("checkUser(extend1)", 1000);   */
 
/* 	function checkUser(extendse) {
		$.ajax({
			type : "post",
			url : ctx + "/PROT/checkWxUser",
			async : false,
			data : {
				as : $("#siteType").val()
			},
			success : function(data) {
				if (data.code == 200) {
					// alert(data.url)
					window.clearTimeout(s);
					location.href = data.url;
				}else if(data.code==202){//使用时间到期
					$.ajax({
						type : "post",
						url : base + "/PROT/toPass",
						async : false,
						data : {
							param : $("#param").val()
						},
						success : function(data) {
							if (data.code == 0) {
								window.clearTimeout(s);//清除定时器
								window.location.href = base + "/PROT/choiceTc";
							} 
						}
					});
				}
			}
		});
	}
	var s = setInterval("checkUser(extend1)", 1000); */

	//转byte
	function stringToByte(str) {
		var bytes = new Array();
		var len, c;
		len = str.length;
		for (var i = 0; i < len; i++) {
			c = str.charCodeAt(i);
			if (c >= 0x010000 && c <= 0x10FFFF) {
				bytes.push(((c >> 18) & 0x07) | 0xF0);
				bytes.push(((c >> 12) & 0x3F) | 0x80);
				bytes.push(((c >> 6) & 0x3F) | 0x80);
				bytes.push((c & 0x3F) | 0x80);
			} else if (c >= 0x000800 && c <= 0x00FFFF) {
				bytes.push(((c >> 12) & 0x0F) | 0xE0);
				bytes.push(((c >> 6) & 0x3F) | 0x80);
				bytes.push((c & 0x3F) | 0x80);
			} else if (c >= 0x000080 && c <= 0x0007FF) {
				bytes.push(((c >> 6) & 0x1F) | 0xC0);
				bytes.push((c & 0x3F) | 0x80);
			} else {
				bytes.push(c & 0xFF);
			}
		}
		return bytes;
	}
	//转String
	function byteToString(arr) {
		if (typeof arr === 'string') {
			return arr;
		}
		var str = '', _arr = arr;
		for (var i = 0; i < _arr.length; i++) {
			var one = _arr[i].toString(2), v = one.match(/^1+?(?=0)/);
			if (v && one.length == 8) {
				var bytesLength = v[0].length;
				var store = _arr[i].toString(2).slice(7 - bytesLength);
				for (var st = 1; st < bytesLength; st++) {
					store += _arr[st + i].toString(2).slice(2);
				}
				str += String.fromCharCode(parseInt(store, 2));
				i += bytesLength - 1;
			} else {
				str += String.fromCharCode(_arr[i]);
			}
		}
		return str;
	}

	function Wechat_GotoRedirect(appId, extend, timestamp, sign, shopId,
			authUrl, mac, ssid, bssid) {
		//将回调函数名称带到服务器端
		var url = "https://wifi.weixin.qq.com/operator/callWechatBrowser.xhtml?appId="
				+ appId
				+ "&extend="
				+ extend
				+ "&timestamp="
				+ timestamp
				+ "&sign=" + sign;

		//如果sign后面的参数有值，则是新3.1发起的流程
		if (authUrl && shopId) {
			url = "https://wifi.weixin.qq.com/operator/callWechat.xhtml?appId="
					+ appId + "&extend=" + extend + "&timestamp=" + timestamp
					+ "&sign=" + sign + "&shopId=" + shopId + "&authUrl="
					+ encodeURIComponent(authUrl) + "&mac=" + mac + "&ssid="
					+ ssid + "&bssid=" + bssid;
		}

		//通过dom操作创建script节点实现异步请求  
		var script = document.createElement('script');
		script.setAttribute('src', url);
		document.getElementsByTagName('head')[0].appendChild(script);
	}

	//注册回调函数
	function jsonpCallback(result) {
		console.log(result)
		if (result && result.success) {
			/* alert('WeChat will call up :::: ' + result.success + '  data:'
					+ result.data);  */
			var ua = navigator.userAgent;
			if (ua.indexOf("iPhone") != -1 || ua.indexOf("iPod") != -1
					|| ua.indexOf("iPad") != -1) { //iPhone             
				document.location = result.data;
			} else {
				if ('false' == 'true') {
					/* alert('[强制]该浏览器不支持自动跳转微信请手动打开微信\n如果已跳转请忽略此提示'); */
					return;
				}
				createIframe();
				callUpTimestamp = new Date().getTime();
				loadIframe.src = result.data;
				noResponse = setTimeout(function() {
					errorJump();
				}, 3000);
			}
		} else if (result && !result.success) {
			/* alert('WeChat will call up : ' + result.success + '  data:'
					+ result.data); */
		}
	}

	//判断是否为PC 
	function IsPC() {
		var sUserAgent = navigator.userAgent.toLowerCase();
		var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
		var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
		var bIsMidp = sUserAgent.match(/midp/i) == "midp";
		var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
		var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
		var bIsAndroid = sUserAgent.match(/android/i) == "android";
		var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
		var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
		if (bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE
				|| bIsWM || bIsIpad) {
			return false;
		} else {
			return true;
		}
	}

	$(".bar_progress div").animate({
		width : "100%"
	}, 3000, function() {
		setTimeout(function() {
			$(".bar_progress div").fadeIn(3000);
		});
	});

	function getJson() {
		var type = $("#siteType").val();
		var newType = type.substring(type.indexOf("{") + 1, type.indexOf("}"));

		var arrayType = newType.split(",");
		var params = "";
		for (var i = 0; i < arrayType.length; i++) {
			if (arrayType[i].split("=")[1] != null) {
				params += '\\"' + arrayType[i].split("=")[0] + '\\"' + ':'
						+ '\\"' + arrayType[i].split("=")[1] + '\\"' + ',';
			}
		}
		params = '{' + params.substring(0, params.length - 1) + '}';

		return params.replace(/\s/g, "");
	}
</script>
</html>
