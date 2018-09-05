
$(function(){
	doFomat($("#username").val());	
	/*去充值*/
	$(".active").click(function(){
		window.location.href=ctx+"/priceConfig/toType?siteId="+$("#siteId").val()+"&userId="+$("#userId").val();
	});
	/*查询缴费记录*/
	$(".payrecomed").click(function(){
		window.location.href=ctx+"/record/recordPage?siteId="+$("#siteId").val()+"&userId="+$("#userId").val();
	});
	
	selMessage();
	
	//给商家打电话
	$("#touchCustom").click(function() {
		$(".mask").show();
		$(".opacity").show();
		$(".business-phone").show();

		$(".bp-cancle").click(function() {
			$(".mask").hide();
			$(".opacity").hide();
			$(".business-phone").hide();
		});
	});
	//游戏中心
	$("#gameCenter").click(function() {
		prompt("暂时开发中");
					
	});
	//账号管理
	$("#accountMange").click(function() {
		prompt("暂时开发中");
	});
});
//消息
function selMessage(){
	//数量显示
	$.ajax({
		url : base + "/message/messageCount",
		data : {
			userName : userName,
			siteId : siteId
		},
		dataType : "json",
		success : function(data) {
			if (data.result == "1") {
				$('#emCount').text(data.mcount);
			} else if (data.result == "0") {
				$('#emCount').text(0);
				//$('.icon-icon08').unbind('click');
			}
		}
	})
}
function rightButton(){//跳入个人信息编辑界面
	window.location.href=base+"/personal/editprofile?userName="+$("#username").val()+"&siteId="+$("#siteId").val();
}
/*格式化用户名为121****2323格式*/
function doFomat(username){
	var name = username.substring(0,3); 
	var end = username.substring(7,username.length);
	var allname = name+"****"+end;
	$(".perheadtext p").text(allname);
}
function user(){
	window.location.href=base+"/personal/getUser";
}

function surfInterint(){
	var dumplist=["http://www.gonet.cc/m"];
	var n=Math.floor(Math.random()* dumplist.length + 1)-1;
	window.location.href=dumplist[n];
}
function relationService(){
	window.location.href=base+"/personal/contactCustomerService"
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
        style:'font-size: 0.25rem;text-align: center;padding: 0px 10px!important;'
      });
}




