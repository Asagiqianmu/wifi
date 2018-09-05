function sendSMS(mobile) {
	var phone = mobile;
	var mac = getQueryString("wlanusermac");
	var ip = getQueryString("wlanuserip");
	if (mac == null) {
		mac = getQueryString("usermac");
		ip = getQueryString("userip");
	}
	// 加密方式
	setMaxDigits(130);
	var key = new RSAKeyPair(
			"10001",
			"",
			"81bfe67f578dfef2d972875accd55ff5dcbee0f6c18eb5b43d1d7e9a6b9ed63698d788892f91568a8c72ef8617eb469985d3df5c2fc32e1fd881f90052415bdf1a60ba0a8752c4a0974efad6bfaf08e640a923d416c109760b84b9f9302cbc99515c40874ff1d7be94bf171f0324d7113f7d3ab359f57e937e0663a29549de2b");
	var results = phone + "," + mac + "," + ip;
	var parms = encryptedString(key, encodeURIComponent(results.replace(/\s/g, "")));
	$.ajax({
		type : "post",
		url : base + "/PROT/checkPhone",
		async : false,
		data : {
			mac : $("#mac").val(),
			userName : $("#phone").val()
		},
		dataType : "json",
		success : function(data) {
			if (data.result != 5) {
				$.ajax({
					url : base + "/code/msgRandCode",
					data : {
						parms : parms,
						style : 0
					},
					dataType : "json",
					success : function(data) {
						if (data.result != 0) {
							allowSendSms = true;
							$(".ZindexTip,.ZindexTipAlert")
									.show();
							$("#alertText").html("发送验证码失败");
						} else {
							$(".ZindexTip,.ZindexTipAlert")
									.show();
							$("#alertText").html("验证码发送成功");
						}
					}
				});
			} else {
				$(".ZindexTip,.ZindexTipAlert").show();
				$("#alertText").html("已经验证您是老用户<br/>可以直接登录！")
				$("#res").val(data.result);
				$("#password").val("***********");
				$("#password").prop("disabled", "true");
			}
		}
	});

}

function oneKeyLogin() {
	var str = "ajax_Login.action";
	var data_str = encodeToUTF8(str);
	$.ajax({
				type : "GET",
				url : data_str,
				success : oneKey_fun,
				error : null,
				timeout : 5000,
				dataType : "json",
				cache : false
			});
}
function oneKey_fun(msg) {
	$("#oneKeyLogin").removeAttr("disabled");
	if (msg.ret == 0) {
		/*
		 * var weburl="ok.jsp?u="+msg.u+"&i="+msg.i+"&m="+msg.msg+"&l="+msg.l;
		 * window.location.replace(weburl);
		 */
	} else if (msg.ret == 10) {
		alert("参数获取失败，请重试！！");
		/*
		 * var weburl="http://www.openportal.com.cn";
		 * window.location.replace(weburl);
		 */
	} else {
		alert(msg.msg);
	}
}

var username;
var password;

/**
 * 老用户登录
 * 
 * @param phone
 * @param pwd
 * @param code
 * @param resuit
 */
function oldLogin(phone, pwd, result,nasid) {
	// reuslt=$("#res").val();
	// alert(pwd);
	// window.location.href = base+"/PROT/homepage";
	$.ajax({
				type : "post",
				url : base + "/PROT/uniteLogin",
				data : {
					userName : phone,
					password : pwd,
					code : pwd,
					siteId : $("#siteId").val(),
					result : result,
					as : getJson()
				},
				dataType : "json",
				success : function(data) {
					if (data.result == 100) {
						window.location.href = data.url;
						return;
					} else if (data.result == 200) {
						window.location.href = base + "/PROT/homepage";
						return;
					}else if (data.result == 201) {// 使用时间到期
						window.location.href = base + "/PROT/choiceTc";
					}else {
						$(".ZindexTip,.ZindexTipAlert").show();
						$("#alertText").text("登录失败！" + data.result)
					}
				}
			});
}

/**
 * 新用户登录
 * 
 * @param mobile
 * @param password
 */
function new_login(mobile, password) {
	phone = mobile;// 获取手机号
	code = password;// 获取验证码号
	$.ajax({
				type : "post",
				url : base + "/PROT/codes2",
				data : {
					userName : phone,
					code : password
				},
				dataType : "json",
				success : function(data) {
					if (data.result == "0") {
						oldLogin(phone, password, 1,nasid);
					} else {
						$(".ZindexTip,.ZindexTipAlert").show();
						$("#alertText").text("密码不正确!")
					}
				}
			});
}
/* 拼接json串 */
function getJson() {
	var type = $("#siteType").val();
	var newType = type.substring(type.indexOf("{") + 1, type.indexOf("}"));

	var arrayType = newType.split(",");
	var params = "";
	for (var i = 0; i < arrayType.length; i++) {
		if (arrayType[i].split("=")[1] != null) {
			params += '\\"' + arrayType[i].split("=")[0] + '\\"' + ':' + '\\"'
					+ arrayType[i].split("=")[1] + '\\"' + ',';
		}
	}
	params = '{' + params.substring(0, params.length - 1) + '}';

	return params.replace(/\s/g, "");
}
function login_fun(msg) {
	if (msg.ret == 0) {
		$.post(encodeToUTF8("statistics/saveUserLog"), {
					"username" : username,
					"userIp" : getQueryString("wlanuserip"),
					"userMac" : getQueryString("wlanusermac"),
					"apMac" : getQueryString("wlanapmac"),
					"authType" : 20,
					"NasOperType" : "2"
				}, function(data) {
					window.location.href = "http://wap.szwxns.com/";
				});
	} else if (msg.ret == 10) {
		alert("参数获取失败，请重试！！");
	} else {
		alert(msg.msg);
	}
}
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

function passwordLogin(password) {
	var str = "ajax_weixinsms.action?pwd=" + password;
	var data_str = encodeToUTF8(str);
	$.ajax({
				type : "GET",
				url : data_str,
				success : password_fun,
				error : null,
				timeout : 5000,
				dataType : "json",
				cache : false
			});
}

function password_fun(msg) {
	$("#btngdpass").removeAttr("disabled");
	if (msg.ret == 0) {
		/*
		 * var weburl="ok.jsp?u="+msg.u+"&i="+msg.i+"&m="+msg.msg+"&l="+msg.l;
		 * window.location.replace(weburl);
		 */
	} else if (msg.ret == 10) {
		alert("参数获取失败，请重试！！");
		/*
		 * var weburl="http://www.openportal.com.cn";
		 * window.location.replace(weburl);
		 */
	} else if (msg.ret == 1) {
		alert("网络暂时不可用，请重试！！");
		/*
		 * var weburl="http://www.openportal.com.cn";
		 * window.location.replace(weburl);
		 */
	} else if (msg.ret == 2) {
		alert('上网密码错误！！');
	} else {
		alert('系统不是该验证模式！！');
	}
}

function smsLogin(phone, yzm) {
	var str = "ajax_sms.action?phone=" + phone + "&yzm=" + yzm;
	var data_str = encodeToUTF8(str);
	$.ajax({
				type : "GET",
				url : data_str,
				success : sms_fun,
				error : null,
				timeout : 5000,
				dataType : "json",
				cache : false
			});
}

function sms_fun(msg) {
	if (msg.ret == 0) {
		/*
		 * var weburl="ok.jsp?u="+msg.u+"&i="+msg.i+"&m="+msg.msg+"&l="+msg.l;
		 * window.location.replace(weburl);
		 */
	} else if (msg.ret == 10) {
		alert("参数获取失败，请重试！！");
		/*
		 * var weburl="http://www.openportal.com.cn";
		 * window.location.replace(weburl);
		 */
	} else if (msg.ret == 1) {
		alert("网络暂时不可用，请重试！！");
		/*
		 * var weburl="http://www.openportal.com.cn";
		 * window.location.replace(weburl);
		 */
	} else if (msg.ret == 2) {
		alert(msg.msg);
	} else {
		alert('系统不是该验证模式！！');
	}
}

function weixinLogin() {
	$("#weixinLogin").removeAttr("disabled");
	var str = "ajax_weixin.action";
	var data_str = encodeToUTF8(str);
	$.ajax({
				type : "GET",
				url : data_str,
				success : weixin_fun,
				error : null,
				timeout : 5000,
				dataType : "json",
				cache : false
			});
}

function weixin_fun(msg) {
	if (msg.ret == 0) {
		appId = msg.appId;
		extend = msg.extend;
		timestamp = msg.timestamp;
		sign = msg.sign;
		shop_id = msg.shop_id;
		authUrl = msg.authUrl;
		mac = msg.mac;
		ssid = msg.ssid;
		bssid = msg.bssid;
		/*
		 * var
		 * weburl="wx.html?appId="+appId+"&extend="+extend+"&timestamp="+timestamp+"&sign="+sign+"&shop_id="+shop_id+"&authUrl="+authUrl+"&mac="+mac+"&ssid="+ssid+"&bssid="+bssid;
		 * window.location.replace(weburl);
		 */
	} else if (msg.ret == 10) {
		alert("获取连接参数失败，等待页面刷新！！");
		/*
		 * var weburl="http://www.openportal.com.cn";
		 * window.location.replace(weburl);
		 */
	} else if (msg.ret == 1) {
		alert("网络暂时不可用，请重试！！");
		/*
		 * var weburl="http://www.openportal.com.cn";
		 * window.location.replace(weburl);
		 */
	} else if (msg.ret == 2) {
		alert("你已经通过验证,或者下线后重试！！");
		/*
		 * var weburl="index_choose"; window.location.replace(weburl);
		 */
	} else {
		alert("系统不是微信验证模式！！");
		/*
		 * var weburl="index_choose"; window.location.replace(weburl);
		 */
	}
}