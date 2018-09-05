  var loadIframe = null;
	var noResponse = null;
	var callUpTimestamp = 0;
$(function () {
//    var ua = navigator.userAgent.toLowerCase();
//    if (/iphone|ipad|ipod/.test(ua)) {
//        $(".content-center img:eq(1)").attr('src','images/zhuangtai2.png');
//        $(".pn_img_download").attr('src','images/qu-appstore.png');
//        //alert("iphone");
//    } else if (/android/.test(ua)) {
//        $(".content-center img:eq(1)").attr('src','images/zhuangtai1.png');
//        $(".content-center .pn_img_download").attr('src','images/mianliuliangxiazai.png');
//        //alert("android");
//    }else {
//        $(".content-center img:eq(1)").attr('src','images/zhuangtai3.png');
//        $(".pn_img_download").attr('src','images/da-kai-pinganyoulian.png');
//    }
    //$('.pn-back').tap(function () {
    //    alert('123');
    //    $('body').css({
    //        backgroundColor: 'red'
    //    })
    //    window.history.back()
    //});
	$("#openApp").unbind("click");
	$("#openApp").click(function(){
//		 var sUserAgent = navigator.userAgent.toLowerCase();
//		 var type=0;
//		 if( sUserAgent.match(/android/i) == "android"){
//			 type=1;
//		 }
		 $.ajax({
				type:"post",
				url:ctx+"/login/openAppCpc",
				async:false,
				data:{
					usermac:$("#usermac").val()
				},
				success:function(data){
					jsonpCallback();
				}
			}); 
		 
		
	});
	
	
});

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
 function notice(){
	$("#isMobile").show();
	$("#phoneico").hide();
	$("#phone").hide();
}
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
 function jsonpCallback(){
	
	createIframe();
	var sUserAgent = navigator.userAgent.toLowerCase(); 
	var bIsAndroid = sUserAgent.match(/android/i) == "android";
 		//alert('人家得：WeChat will call up : ' + result.success + '  data:' + result.data);	
 		if(!IsPC()){
			if(bIsAndroid){
				location.href= $("#andorid").val();
			    window.setTimeout(function(){
		           window.location.href =ctx+"/login/toAppLogin?params="+$("#param").val();// "http://cache.kdfwifi.cn/LINKME-szyd.apk"; /***打开app的协议，有安卓同事提供***/
		        },2000);
			 }else{
				 location.href= $("#ios").val();
			     window.setTimeout(function(){
			    	 window.location.href = ctx+"/login/goIosDownLoad?param="+$("#param").val();
			    	// toPassLoad();
		          // window.location.href = "https://itunes.apple.com/cn/app/%E5%B9%B3%E5%AE%89%E4%BC%98%E8%81%94-%E5%AE%89%E5%85%A8%E4%B8%87%E8%83%BDwifi%E4%B8%8A%E7%BD%91%E6%B5%81%E9%87%8F%E7%AE%A1%E5%AE%B6/id1233701630?mt=8"; /***打开app的协议，有ios同事提供***/
		         },2000);
			} 
 		}else{
 			alert("不支持pc");
 		}
} 

//为ios用户下载时放行
function toPassLoad(){
	$.ajax({
		type:post,
		url:ctx+"/login/toPass",
		async:false,
		data:{
			param:$("#param").val()
		},
		success:function(data){
			if(data.code==0){
		           window.location.href = "https://itunes.apple.com/cn/app/%E5%B9%B3%E5%AE%89%E4%BC%98%E8%81%94-%E5%AE%89%E5%85%A8%E4%B8%87%E8%83%BDwifi%E4%B8%8A%E7%BD%91%E6%B5%81%E9%87%8F%E7%AE%A1%E5%AE%B6/id1233701630?mt=8"; /***打开app的协议，有ios同事提供***/
			}
		}
	});
	
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