<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/partol" />
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
<link type="text/css" href="${path}/css/base.css" rel="stylesheet" />
<link rel="stylesheet"
	href="http://wifi.weixin.qq.com/resources/css/style-simple-follow.css" />
<!-- 唤起微信的js 必须的 -->
<script type="text/javascript"
	src="http://wifi.weixin.qq.com/resources/js/wechatticket/wechatutil.js"></script>
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
	background: url(${path}/img/progress.png) repeat-x;
	height: 7px;
	display: block;
	border-radius: 8px;
	width：0；
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
  	var ctx="${ctx}";
	var extend1='${obj.extend}'; 
	var map='${obj.map}';   
console.log(extend1);
console.log(map);
  </script>
</head>

<body>
	<div class="chats">
		<div class="chat_h" id="phone">
			<img src="${path}/img/chat_go.png" alt="" />
			<div class="going">WIFI连接成功后需关注公众号才可完成进行上网</div>
			<div class="time">正在跳转到微信（若跳转未成功，请手动打开微信）</div>
			<div class="bar_progress">
				<div></div>
			</div>
		</div>

		<div class="chat_h chat_hs" id="pc" style="display: none;">
			<img src="${path}/img/wifi_icon.png" alt="" />
			<div class="going">WIFI连接成功后即可进行上网</div>
			<div class="time">正在连接WIFI，请稍等...</div>
			<div class="bar_progress">
				<div></div>
			</div>

		</div>
		<input name="extend" value='${obj.json}' type="hidden" id="siteType">
	</div>
</body>
<!-- 唤起微信的js  必须的 -->
<script type="text/javascript" src="${path }/js/jquery.min.js"></script>
<script type="/text/javascript" src="${path }/js/pcauth.js"></script>
<script type="text/javascript" src="${path }/js/MD5.js"></script>
<script type="text/javascript">
    var loadIframe = null;
	var noResponse = null;
	var callUpTimestamp = 0;
	 
	function putNoResponse(ev){
		 clearTimeout(noResponse);
	}	
	
	 function errorJump()
	 {
		 var now = new Date().getTime();
		 if((now - callUpTimestamp) > 4*1000){
			 return;
		 }
		 alert('该浏览器不支持自动跳转微信请手动打开微信\n如果已跳转请忽略此提示');
	 }
	 
	 myHandler = function(error) {
		 errorJump();
	 };
	
	 
	 function createIframe(){
		 var iframe = document.createElement("iframe");
	     iframe.style.cssText = "display:none;width:0px;height:0px;";
	     document.body.appendChild(iframe);
	     loadIframe = iframe;
	 }
		 function notice()
		{
			$("#isMobile").show();
			$("#phoneico").hide();
			$("#phone").hide();
		}
		
/* 		function getParameterByName(name) {  
		    var match = RegExp('[?&]' + name + '=([^&]*)')  
		                    .exec(window.location.search);  
		    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));  
		} */
		$(function(){
        	/* $("#phoneico").show();
			$("#phone").show();
			$("#pc").hide();
			$("body").removeClass("body_bg"); */
			var appId          = "${obj.appId}";
			var secretkey      = "${obj.secret}";
			//var extend	       = '${obj.extend}';//开发者自定义参数集合
			var extend = 			'${obj.extend}';
			var timestamp      = new Date().getTime();    //时间戳(毫秒)
			var shop_id        = "${obj.shopId}";               //AP设备所在门店的ID
			var authUrl        = "${obj.url}";        //认证服务端URL
			var mac            = "${obj.mac}";     //用户手机mac地址 安卓设备必需
			var ssid           = "${obj.ssid}";         //AP设备信号名称，非必须
			var bssid          = "${obj.bsid}";  	 
		/* 	var appId          = "wxc5fb6a6dabc34dfb";
		    var secretkey      = "fc1af456d5962b006989ceefe4d1f537";
		    var extend         = "www.fangbei.org";       //开发者自定义参数集合
		    var timestamp      = new Date().getTime();    //时间戳(毫秒)
		    var shop_id        = "3474729";               //AP设备所在门店的ID
		    var authUrl        = "http://wifi.weixin.qq.com/assistant/wifigw/auth.xhtml?httpCode=200";        //认证服务端URL
		    var mac            = "3c:91:57:c5:cc:af";     //用户手机mac地址 安卓设备必需
		    var ssid          = "A01-S001-R044";      */     //AP设备信号名称，非必须 */
		   // var bssid          = "00:e0:61:4c:a7:c5";     //AP设备mac地址，非必须
		     var sign = hex_md5(appId + extend + timestamp + shop_id + authUrl + mac + ssid + secretkey);
		     Wechat_GotoRedirect(appId, extend, timestamp, sign, shop_id, authUrl, mac, ssid);
		     
		    
		     
	});
		function checkUser(extendse){
/* 	$.ajax({
				type:"post",
				url:"", ctx+"/login/checkWxUser" 
				async:false,
				data:{
					as:$("#siteType").val()
				},
				success:function(data){
					if(data.code==200){
						// alert(data.url)
						 window.clearTimeout(s); 
						 location.href=data.url;
					}
					
				}
			})  */
			var data = map;
			if(data.result==100){
				window.location.href = data.url;
				return;
			}else{
				return;
			}
		}
		var s= setInterval("checkUser(extend1)",1000);
		
		//转byte
		function stringToByte(str) {
			var bytes = new Array();
			var len, c;
			len = str.length;
			for(var i = 0; i < len; i++) {
				c = str.charCodeAt(i);
				if(c >= 0x010000 && c <= 0x10FFFF) {
					bytes.push(((c >> 18) & 0x07) | 0xF0);
					bytes.push(((c >> 12) & 0x3F) | 0x80);
					bytes.push(((c >> 6) & 0x3F) | 0x80);
					bytes.push((c & 0x3F) | 0x80);
				} else if(c >= 0x000800 && c <= 0x00FFFF) {
					bytes.push(((c >> 12) & 0x0F) | 0xE0);
					bytes.push(((c >> 6) & 0x3F) | 0x80);
					bytes.push((c & 0x3F) | 0x80);
				} else if(c >= 0x000080 && c <= 0x0007FF) {
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
		    if(typeof arr === 'string') {  
		        return arr;  
		    }  
		    var str = '',  
		        _arr = arr;  
		    for(var i = 0; i < _arr.length; i++) {  
		        var one = _arr[i].toString(2),  
		            v = one.match(/^1+?(?=0)/);  
		        if(v && one.length == 8) {  
		            var bytesLength = v[0].length;  
		            var store = _arr[i].toString(2).slice(7 - bytesLength);  
		            for(var st = 1; st < bytesLength; st++) {  
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
		function Wechat_GotoRedirect(appId, extend, timestamp, sign, shopId, authUrl, mac, ssid, bssid){
			//将回调函数名称带到服务器端
			var url = "http://wifi.weixin.qq.com/operator/callWechatBrowser.xhtml?appId=" + appId 
								+ "&extend=" + extend 
								+ "&timestamp=" + timestamp 
								+ "&sign=" + sign;	
			
			//如果sign后面的参数有值，则是新3.1发起的流程
			if(authUrl && shopId){
				url = "http://wifi.weixin.qq.com/operator/callWechat.xhtml?appId=" + appId 
								+ "&extend=" + extend 
								+ "&timestamp=" + timestamp 
								+ "&sign=" + sign
								+ "&shopId=" + shopId
								+ "&authUrl=" + encodeURIComponent(authUrl)
								+ "&mac=" + mac
								+ "&ssid=" + ssid;
				
			}			
			//通过dom操作创建script节点实现异步请求  
			var script = document.createElement('script');  
			script.setAttribute('src', url);  
			document.getElementsByTagName('head')[0].appendChild(script);
			//alert(url+"===");
		}

//注册回调函数
/* function jsonpCallback(result){  
	if(result && result.success){
		alert('WeChat will call up : ' + result.success + '  data:' + result.data);			    
	    var ua=navigator.userAgent;              
		if (ua.indexOf("iPhone") != -1 ||ua.indexOf("iPod")!=-1||ua.indexOf("iPad") != -1) {   //iPhone             
			document.location = result.data;
		}else{			
		    createIframe();
		    callUpTimestamp = new Date().getTime();
		    loadIframe.src=result.data;
			noResponse = setTimeout(function(){
				errorJump();
	      	},3000);
		}			    
	}else if(result && !result.success){
		alert(result.data);
	}
}  */
 function jsonpCallback(result){
	//alert(result.data)
	var sUserAgent = navigator.userAgent.toLowerCase(); 
	var bIsAndroid = sUserAgent.match(/android/i) == "android";
	console.log(result)
	if(result && result.success){
 		//alert('人家得：WeChat will call up : ' + result.success + '  data:' + result.data);	
		if(sUserAgent.match(/micromessenger/i)){
			if(bIsAndroid){
				location.href= 'weixin://connectToFreeWifi';
			}else{
				document.location= result.data;
			}
		}else{
				document.location = result.data;
		}
	}else{
		alert('数据获取失败,请刷新后重试!');
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
	if (bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM || bIsIpad) {
		return false;
	} else {
		return true;
	} 
}

   $(".bar_progress div").animate({width:"0%"},3000,function(){
	    setTimeout(function(){
	        $(".bar_progress div").fadeIn(3000);
	    });
	}); 
   
	function getJson(){
		 var type=$("#siteType").val();
		 var newType= type.substring(type.indexOf("{")+1,type.indexOf("}"));
		 
		 var arrayType=newType.split(",");
		 var params="";
		 for (var i = 0; i < arrayType.length; i++) {
			if(arrayType[i].split("=")[1]!=null){
				params+='\\"'+arrayType[i].split("=")[0]+'\\"'+':'+'\\"'+arrayType[i].split("=")[1]+'\\"'+',';
			}
		}
		params='{'+params.substring(0, params.length-1)+'}';
		
		return  params.replace(/\s/g,"");
	}
	 /* var interval = setInterval(increment,1000);
    var current = 10;
    function increment(){
        current++;
        $(".bar_progress div").animate({width:current+'%'},1000);
        if(current == 99) {
            clearInterval(interval);
        }
    } */
</script>
</html>
