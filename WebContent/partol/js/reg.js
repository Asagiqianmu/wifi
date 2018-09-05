var flag;
var reg = /[0-9A-Za-z]/;
var key ; 
var pag=0;
var checkState;

$(document).ready(function() {
  var h = $(window).height();
  $(window).resize(function() {
    if($(window).height() < h) {
      $('.copyright').hide();
    }
    if($(window).height() >= h) {
      $('.copyright').show();
    }
  });
  //忘记密码
  $('#fogpicpass').click(function() { // 点击忘记密码 
		showDiv('loginpicpassword', 'loginword'); // 隐藏输入登录密码  显示图片验证码
		$('#next').val("下一步").fadeIn();
	});
});
var tel = '';
var valnum = ''; // 定义用户输入验证码
var code; // 定义验证码  
var templateCode;//发送短信模板
//获取对应的对象--function函数.
function get(id) {
	return document.getElementById(id);
}
//去掉所有空格--String类的属性.
String.prototype.trim = function() {
	return this.replace(/\s+/g, "");
};

function second(){
	  mm = setInterval(function(){
	    var num = parseInt($('#mm').text());
	    if(num == 0) {
	      clearInterval(mm);
	      $("#mm").parent().hide();
	      $("#retext").show(); //显示重新发送验证码
	      return false;
	    } else{
	      $('#mm').text(num -= 1);  
	    }    
	  },1000);
	}
$('#retext').click(function() {
	  $(this).hide();
	  $("#mm").html(120).parent().show();
	  second();
	  getMessage(checkState);
});
//手机短信   验证码
function setyzm(t,n) {
	v = t.val();
	if(n==0){
		if(!/^[0-9]*$/.test(v)) return false;
	}
	if(n==1){
		if(!/^[A-Za-z0-9]+$/.test(v)) return false;
	}
	var info = v.toString().split('');
	t.next().children().each(function() {
		var idx = $(this).index();
		$(this).html(info[idx]==undefined?'':info[idx]);
		if($(this).html() == '') {
			//$(this).addClass('active').siblings().removeClass('active');
		}
	});
	return v;
};
//图片验证码
$("#inputCode").keyup(function() {
	var vb = setyzm($(this),1);
	var pageCode=$("#code2").val();
	if(vb.length == 6) {		
		checkState='2';
		if(vb.toLowerCase()==pageCode.toLowerCase()) {
			$('.loading').fadeIn();
			$('#next').fadeOut();
			$("#inputCode").unbind("keyup");//去除绑定键盘事件
			var t = setTimeout(function() {				
				showDiv("loginpassword", "loginpicpassword");
				setInterval(second(), 1000);					
					getMessage('2');//获得手机短信验证码
					$("#next").fadeIn();
					$('.loading').fadeOut();
					//flag=false;							
			}, 1000);
		} else {
			$('.loading').fadeIn();
			$('#next').fadeOut();
			var t = setTimeout(function() {
				$("#inputCode").next().next().fadeIn();
				$('#inputCode').val('');
				$('#inputCode').next().find('i').text("");
				$('#valiId').find('i').eq(0).addClass("active");
				$('#valiId').find('i').eq(5).removeClass("active");
			}, 1000);
			$('.loading').fadeOut();
			$('#next').fadeIn();
			$('#next').attr('disabled', true);
		}
	}
});

$(".passwordpic").click(function() {
	var src = $(".passwordpic").attr("src");
	var src1 = $(".passwordpic").attr("src1");
	var src2 = $(".passwordpic").attr("src2");
	if(src == src1) {
		$(".passwordpic").attr("src", src2);
		$(".password > input").attr("type", "text");
	} else {
		$(".passwordpic").attr("src", src1);
		$(".password > input").attr("type", "password");
	}
});

/*手机号处理*/
$('#Mobile').bind('input propertychange',function(){
	 var tel = $('#Mobile').val().replace(/\s/g,"");
	 var tel1='';
	 if(tel.length==11){
		 for (var i = 0; i < tel.length; i++) {
			if(i==3){
				tel1=tel.substring(0,3)+' '+tel.substring(3,tel.length);
			}
			if(i==6){
				tel1=tel1.substring(0,8)+' '+tel1.substring(8,tel1.length);
			} 
		}
		 $('#Mobile').val(tel1);
	 }
});
/*手机号验证*/
function FillMobile() {
	var telReg=/^[0-9]*$/;
	var mobile = $("#Mobile").val().replace(/\s/g,""); //页面上输入手机号码的文本框的Id.
	var ev = window.event || arguments.callee.caller.arguments[0];
	var target = ev.target || ev.srcElement;
	//if(!telReg.test(mobile)) return false;
//	tel = mobile.value;
	tel = mobile.replace(/\ +/g, '');
//	console.log(get("Mobile"));
	if(ev.keyCode == 8) {
		$('#next').attr('disabled', true);
	};
	if(tel.length == 11 && /^1[3578][0-9]{9}$/.test(tel)) {
		$('#next').attr('disabled', false);
		checkPhone();
	}else if(/^([6|9])\d{7}$|^[0][9]\d{8}$|^[6]([8|6])\d{5}$/.test(tel)){
		$('#next').attr('disabled', false);
		$('#next').unbind("click");
		$('#next').click(function(){
			login("",0);
		});
	}
}

function showDiv(show_cls, hide_cls) {
	$('.' + show_cls).fadeIn();
	$('.' + hide_cls).fadeOut();
}
//后跳渲染页面
//登录
function login(password,result) {
	var userName = $("#Mobile").val();
	var code =$("#valiHtml").val();
	$.ajax({
		type:"post",
		url : base + "/login/uniteLogin",
		data : {
			userName : userName,
			password : "",
			code:code,
			siteId:$(".aginlogin h1").attr("value"),
			result:result,
			as : getJson()
		},
		dataType : "json",
		success : function(data) {
			$('.loading').fadeOut();
			$('.passnote').fadeOut();
			if(data.result==200){
				$('.loading').fadeIn();
				window.location.href = data.url;
				return;
			}else if(data.result==201) {//场所不存在
				prompt(data.msg);
			}else if(data.result==203){
				window.location.href = base+"/login/toLock?locakTime="+data.times+"&userName="+userName+"&changeurl="+escape(window.location.href);
			}else if(data.result==205){//用户名或密码错误
				$('#password2').val("");
				$("#next").val("完成并登录").fadeIn();
				prompt(data.msg);
			}else if(data.result==206){
				prompt(data.msg);
			}else if(data.result==208){
				prompt(data.msg);
			}else if(data.result==209){
				prompt(data.msg);
			}else if(data.result==300){
				prompt(data.msg);
			}else if(data.result==301){
				//账号被锁
				window.location.href = base+"/login/toLock?changeurl="+escape(window.location.href)+"&locakTime="+data.times+"&userName="+userName;
			}else if(data.result==302){
				//实名认证
				$('.passnote').text(data.msg);
				$('.passnote').fadeIn();
			}else if(data.result==304){
				//账号已登录
				prompt(data.msg);
			}
		}
	});
}

/*************************************************************修改登陆*******************************************************************/

//手机校验
function checkPhone() {
	var userName = $("#Mobile").val(),
		url=base + "/login/checkPhone";
	 $.ajax({
		 type: "POST",
		 url :url ,
		 data : {
			 'userName' : userName
		 },
		 success : function(data) {
			var res=data.result;
			$('#next').unbind("click");
			$('#next').click(function(){
				$('.loading').fadeIn();
				$("#next").fadeOut();
				tojumpByresult(res);
			});
		 }
	 });
}
/*手机验证码 校验*/
$('#valiHtml').bind('input propertychange',function(){
	var vb = setyzm($(this),0);
	/*手机验证码 校验*/
	if(vb.length == 6) {
		$("#fuckCode").hide();
		$('#next').attr('disabled', false);
		$('#next').unbind("click");
		$('#next').click(function(){
			$("#next").fadeOut();
			$('.loading').fadeIn();
			codes($("#next").attr("state"));
		});
		
	}
});

//根据用户状态跳动到相对应的页面
function tojumpByresult(n){
	$('.loading').fadeOut();
	$('#next').attr('disabled', true).attr("state",n);
	if(n == 1){
		$("#next").fadeIn();
		$("#next").val("登陆");
//		templateCode="2-----已注册，未设置密码";
		var phone = $('#Mobile').val().trim();
		if(phone=='13510176121'|| phone=='15818683451'){
			$("#next").val("完成并登录").fadeIn();
			$('#phone').html(phone);
			showDiv('loginword','newlogin');//隐藏 输入手机号 显示输入登录密码
			pag=1;//代表用户输出密码登录
		}else{
			$('#phone').html(phone);	
			showDiv('loginpassword','newlogin');//隐藏 输入手机号 显示手机验证吗 
			second();
			getMessage('1');//获取验证码 	
			checkState='1';
		}
		
	}
//	else if(n == 2){//第三次登录
//		$("#next").val("完成并登录").fadeIn();
//		var phone = $('#Mobile').val();
//		$('#phone').html(phone);
//		showDiv('loginword','newlogin');//隐藏 输入手机号 显示输入登录密码
//		pag=1;//代表用户输出密码登录
//	}else if(n==0){
//		templateCode="1--注册中";
//		$('#next').fadeIn();
//		$('.loading').fadeOut();
//		$('#phone').html( $('#Mobile').val());
//		getMessage('0');//获取验证码	
//		showDiv('loginpassword','newlogin');//隐藏输入手机框     显示手机验证码 
//		second();	
//		checkState='0';
//	}
}

//第二次跟验证码校验，修改密码验证码校验
function  codes(n){
	var phone= $('#Mobile').val();//获取手机号
	var code = $("#valiHtml").val();//获取验证码号
	$.ajax({  
		type:"post",
		url: base + "/login/codes",
		data : {			
			userName : phone,
			code : code
		},  
		dataType:"json",  
		success:function(data){ 
			$('.loading').fadeOut();
			$("#fuckCode").hide();
			if(data.result == "0"){
				if(n == 1){//判断是第二次登录手机号
//					showDiv('loginnewpassword','loginpassword');//隐藏验证码。显示设置新密码
//					$("#next").val("完成并登录").fadeIn();
//					$('#next').attr('disabled', true);
//					pag=2;//代表用户第二次登录修改密码并登录
					login(code,1);
				}
//				else if(n == 0){//判断首次手机号 验证码正确后。直接跳转个人中心页面
//					$("#next").fadeOut();
//					$('.loading').fadeIn();
//					login(code,n);
//				}else if(n == 2){
//					showDiv('loginnewpassword','loginpassword');//隐藏验证码。显示设置新密码
//					$("#next").val("完成并登录").fadeIn();
//					$('#next').attr('disabled', true);
//					pag=3;//代表用户忘记密码
//				}
			}else{//验证码校验失败
				if(n == 1){//判断是第二次登录手机号
					//并且提示验证码有误输入校验验证码失败重新刷新页面，显示重新发送验证码按钮
					$('#next').fadeIn();
					$("#valiHtml").next().next().fadeIn();				
					$("#valiHtml").val('');	
					$("#valiHtml").next().find('i').text("");
					$('.validate-text').find('i').removeClass("active");
					$('.validate-text').find('i').eq($(this).index()).addClass("active");
					$('#next').attr('disabled', true);
				}else if(n == 0){//判断首次手机号 验证码正确后。直接跳转个人中心页面
					$('#next').fadeIn();
					$("#valiHtml").next().next().fadeIn();				
					$("#valiHtml").val('');	
					$("#valiHtml").next().find('i').text("");
					$('.validate-text').find('i').removeClass("active");
					$('.validate-text').find('i').eq($(this).index()).addClass("active");
					$('#next').attr('disabled', true);
				}else if(n == 2){
					$("#next").fadeIn();
					//并且提示验证码有误输入校验验证码失败重新刷新页面，显示重新发送验证码按钮
					$("#valiHtml").next().next().fadeIn();				
					$("#valiHtml").val('');	
					$("#valiHtml").next().find('i').text("");
					$('.validate-text').find('i').removeClass("active");
					$('.validate-text').find('i').eq($(this).index()).addClass("active");
					$('#next').attr('disabled', true);
				}
			}
		},  
	});   
}
//第二次登录 输入密码
$('.pass').bind('input propertychange',function(){
	num = $(this).val().trim();
	$(this).val(num);
	if(num.length >= 6) {
		$('#next').fadeIn();
		$('#next').attr('disabled', false);
		//点击完成并登陆事件按钮
		$('#next').unbind('click');
		$('#next').click(function(){
			var n=$("#next").attr("state");
			$('#next').attr('disabled', true);
			$('#next').fadeOut();
			$('.loading').fadeIn();
			if(pag==3){//忘记密码登录
				updatePass(n);
			}else{//修改密码以及输入密码登录
				login($("#password2").val()==''?$("#password1").val():$("#password2").val(),n);
			}
		});
	}else{
		$('#next').attr('disabled', true);
	}
});

/***************************************************************修改结束*****************************************************************/

//获得手机验证码
function getMessage(state){
	var phone = $("#Mobile").val().replace(/\s/g,"");
	var mac =$("#mac1").val();
	var ip = $("#ip").val();
	bodyRSA(); //加密方式
	if(mac==''){
		mac = $("#mac").val();
	}
	var results =phone+","+mac+","+ip;
	var parms = encryptedString(key, encodeURIComponent(results.replace(/\s/g,"")));
	$.ajax({  
		url:base + "/code/msgRandCode",
		data : {			
			parms:parms,
			style:state
			
		},  
		dataType:"json", 
		success:function(data){ 
			if(data.result !=0){
				boolean = false;
				prompt(data.msg);
			}else{
				prompt(data.msg);
			}
		},  
	} );   
}

//修改密码（忘记密码）
function updatePass(n){  
  var pass = $("#password1").val();
  var phone= $('#Mobile').val();
  $.ajax({  
    url: base + "/login/forgetPass",
    type:'post',  
    data : {      
      "pass" : pass,
      "phone":phone
    },  
    success:function(data){
      if(data.result == 1){
    	  login(pass,n);
      }else{
    	  prompt("网络故障,请稍后重试");
      }
    }          
  }); 
}
function bodyRSA(){  
    setMaxDigits(130);  
    key = new RSAKeyPair("10001","","81bfe67f578dfef2d972875accd55ff5dcbee0f6c18eb5b43d1d7e9a6b9ed63698d788892f91568a8c72ef8617eb469985d3df5c2fc32e1fd881f90052415bdf1a60ba0a8752c4a0974efad6bfaf08e640a923d416c109760b84b9f9302cbc99515c40874ff1d7be94bf171f0324d7113f7d3ab359f57e937e0663a29549de2b");   
} 

/*拼接json串*/
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
/*提示框*/
function prompt(msg){
	layer.open({
        content: msg,
        skin: 'msg',
        time: 2, //2秒后自动关闭
        style:'width:70%;font-size: 0.25rem;text-align: center;height: 1rem;'
      });
}