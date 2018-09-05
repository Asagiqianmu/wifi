$(function() {
	$("#resetBtn").click(function() {
		var oldPassword = $("#oldPassword").val();
		var newPassword = $("#newPassword").val();
		var ResetNewPassword = $("#ResetNewPassword").val();
		if (oldPassword == "") {
			messgae('当前密码不能为空',1);
			return false;
		}
		if (newPassword == "" || ResetNewPassword == "") {
			messgae('新密码不能为空',1);
			return false
		}
		if (newPassword.length<6 || newPassword.length>20) {
			messgae('密码长度为6-20位之间',1);
			return false;
		}
		if (ResetNewPassword != newPassword) {
			messgae('两次密码不一致',1);
			return false
		}
		toupdate();
	});
	
	var form = document.forms[0];

	$("#oldPassword").blur(function() {

		var oldPassword = $("#oldPassword").val();
		if (oldPassword == "") {
			messgae('当前密码不能为空',1);
		}
	});
	$("#newPassword").blur(function() {
		var newPassword = form.newPassword.value;
		var level = -1;
		if (newPassword == "") {
			messgae('新密码不能为空',1);
			return false;
		} else {
			if (newPassword.length<6 || newPassword.length>20) {
				messgae('密码长度为6-20位之间',1);
			} else {
//				  当用户填写正确时，对密码强度做处理
//				  分别判断输入内容是否包含字母、数字和特殊字符
//				  有任何一个level自增
				if (/[a-z]/i.test(newPassword)) {
					level++;
				}
				if (/[0-9]/i.test(newPassword)) {
					level++;
				}
				if (/[^a-z0-9]/i.test(newPassword)) {
					level++;
				} else {
					key = true;
				}
			}
		}
		//	 放在失去焦点事件最后
		//  依据level的值，动态改变强度显示的内容
		var arrLevel = document.getElementById("level").children;
		for (var i = 0; i < arrLevel.length; i++) {
			if (i == level) {
				arrLevel[i].className = "selected";
			} else {
				arrLevel[i].className = "";
			}
		}
	});
});
$("#ResetNewPassword").blur(function() {
	var newPassword = $("#newPassword").val();
	var ResetNewPassword = $("#ResetNewPassword").val();
	if (ResetNewPassword == "") {
		//提示
		messgae('请再次输入新密码不能为空',1);
		return false;
	}
	if (newPassword != ResetNewPassword) {
		messgae('两次密码不一致',1);
	}
});
function messgae(content,time) {
	layer.open({
		content : content,
		skin : 'msg',
		time : time,
		style : 'font-size:14px;'
	});
}

function toupdate(){
	var name=$(".name").text().replace(/\s/g,""),
	passWord = $("#oldPassword").val(),
	newPassWord = $("#newPassword").val();
	$.ajax({
		type:"post",
		url:ctx+"/login/userPwd",
		dataType:"json",
		data:{
			userName:name,
			password:passWord,
			newPassWord:newPassWord
		},
		success:function(data){
			if(data.code==200){
				$(".resetPassword input").val("");
				messgae(data.msg,2);
				window.location.href=$("#url").val()+"&state=1";
			}else if(data.code==201){
				messgae(data.msg,1);
			}
		}
		
	})
}
