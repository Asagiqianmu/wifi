$(function() {
	doFomat($("#telphone").val());//格式化用户账号
	
	$("#next").unbind("click");
	$("#next").click(function(){
		$('.loading').fadeIn();
		$(".next-validate").hide();
		$("#next").attr("disabled",true);
		unifiedEntrance();
	});
});
/*格式化用户名为121****2323格式*/
function doFomat(username){
	var name = username.substring(0,3); 
	var end = username.substring(7,username.length);
	var allname = name+"****"+end;
	$("#tel").val(allname);
}

/*一键登录*/
function unifiedEntrance(){
	$.ajax({
		type:"post",
		url:ctx+"/login/uniteLogin",
		data:{
			userName:$("#telphone").val(),
			password:$("#password").val(),
			siteId:$(".aginlogin h1").attr("value"),
			result:2,
			as:getJson()
		},
		success:function(data){
			$('.loading').fadeOut();
			$('.passnote').fadeOut();
			$(".next-validate").show();
			if(data.result==200){
				window.location.href = data.url;
				return;
			}else if(data.result==201) {
				prompt(data.msg);
			}else if(data.result==209) {
				prompt(data.msg);
			}else if(data.result==203){
				window.location.href = ctx+"/login/toLock?locakTime="+data.times+"&userName="+$("#telphone").val()+"&changeurl="+escape(window.location.href);
//				$('.passnote').text(data.msg);
//				$('.passnote').fadeIn();
			}else if(data.result==205){
				prompt(data.msg);
			}else if(data.result==206){
				prompt(data.msg);
			}else if(data.result==208){
				prompt(data.msg);
			}else if(data.result==300){
				prompt(data.msg);
			}else if(data.result==301){
				//账号被锁
				window.location.href = ctx+"/login/toLock?locakTime="+data.times+"&userName="+$("#telphone").val()+"&changeurl="+escape(window.location.href);
			}else if(data.result==302){
				//实名认证
			}else if(data.result==304){
				//账号已登录
				prompt(data.msg);
			}
			
		}
		
		
	})
	
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
        style:'font-size: 0.25rem;text-align: center;'
      });
}

