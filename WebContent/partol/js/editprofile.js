var nick="";//昵称
var i=0;
function showSuss(show_cls, hide_cls) {
	$('.' + show_cls).fadeIn();
	$('.' + hide_cls).fadeOut();
}
function closeLayer(close){	
	$("."+ close).fadeOut();	
}

$(function(){
	var bir=$(".jcDate").val();
	if(bir!=null&&bir!=''){
		$(".jcDate").prop("disabled",true);
	}
	nick=$('#nick').text();
	$("#sex").val(s=="0"?"女":s=="1"?"男":"");
	if(s==""|| (s!=0 && s!=1)){
		//选择性别
		$("#setModel").click(function(){
			$('.fillsexwrap').fadeIn();		
		});
	}
	if($("#birthdate").val()==""){
		$("#birthdate").attr("disabled",false);
	}
	//弹出遮罩层
	$('#editnamemodle').click(function(){	//点击昵称时	
		$('.fillname').fadeIn();//填写昵称
		$('#updatenick').val($('#nick').text());
	});
	
	//取消遮罩层
	$('#closemodle').click(function(){//点击取消时
		$('.fillname').fadeOut();
	});
	
	$("#modPic").click(function(){
		$('.fillPicwrap').fadeIn();
	});
	
	$(".sex").click(function(){//点击性别
		var sexVal = $(this).text();//获得所选的值
		$(".sexChose").val(sexVal);
		$(".fillsexwrap").fadeOut();		
	});

	//修改昵称点击确定时
	$("#nickTure").click(function(){
		$('#nick').text(""); //清空之前p标签的值
		$('#nick').text($('#updatenick').val()); //将input框的值赋值p标签	
		nick=$('#nick').text(); //昵称的值		
	});
	$(".savefoot").on("tab",function(){
		saveData();
	});
	
});
function saveData(){ //保存按钮
	$(".savefoot").hide();
	$('.loading').show();
	// 调用函数处理图片 
	var imgUrl=$('#photo').attr('src');
	if(imgUrl.indexOf("http://oss.kdfwifi.net/user_pic/")>=0){
		upImg(imgUrl);
	}else{
		/*dealImage(imgUrl,{width : 81,height : 81},
			function(base){
				if(i==1){
					upImg(base);
				}
			});*/
		
		upImg(imgUrl);
	}
}

function upImg(str){
	$('#next').attr('disabled', true);
	var birthdate=$("#birthdate").val();
	var nick=$('#nick').text();
	var sex=$("#sex").val()=="女"?"0":$("#sex").val()=="男"?"1":"-1";
	if($("#birthdate").val()==null||$("#birthdate").val()==""||$('#nick').text()==null||$('#nick').text()==""||$(".sexChose").val()==null||$(".sexChose").val()==""){
		$('.fillname1').fadeIn();
		 setTimeout("$('.fillname1').fadeOut()",2000);
		return;
	}
	var userName=$("#editName").val();
	var siteId=$("#siteId").val();
	$.ajax({  
		type:"post",
		url: base + "/personal/perfectInformation",
		data :{      
			sex : sex, //性别
			birthdate:birthdate, //生日
			user_nickname:nick, //昵称
			image_url:str, //头像
			userName:userName, //用户电话
			siteId: siteId//场所Id
		}, 
		success:function(data){
			$('.savefoot').show();
			$('.loading').hide();
			if(data.result==true){   
				//送流量完成，直接跳入个人中心，刷新个人中心	  		 
				jumpPerson(siteId,userName);
			}else{
				$('.fillnameInfo').fadeIn();
				console.log("送时长失败");
			}
		},  	   
	}); 
}

//个人资料编辑后，跳入person界面
function jumpPerson(sId,ume){
	window.location.href=base+"/personal/editAfter?userName="+ume+"&siteId="+sId;
}

//格式化手机号
function doFomat(username){
	var name = username.substring(0,3); 
	var end = username.substring(7,username.length);
	var allname = name+"****"+end;
	$("#tel").val(allname);
}
//个人信息更新失败时，提示框关闭
function closeLayer(close){
    $("."+ close).fadeOut();  
  }

/**
 * 图片压缩，默认同比例压缩
 * @param {Object} path 
 *   pc端传入的路径可以为相对路径，但是在移动端上必须传入的路径是照相图片储存的绝对路径
 * @param {Object} obj
 *   obj 对象 有 width， height， quality(0-1)
 * @param {Object} callback
 *   回调函数有一个参数，base64的字符串数据
 
function dealImage(path, obj, callback){
	 var img = new Image();
	 img.src = path;
	 img.onload = function(){
			 i++;
		  var that = this;
		  // 默认按比例压缩
		  var w = that.width,
		   h = that.height,
		   scale = w / h;
		   w = obj.width || w;
		   h = obj.height || (w / scale);
		  var quality = 0.7;  // 默认图片质量为0.7
		  //生成canvas
		  var canvas = document.createElement('canvas');
		  var ctxas = canvas.getContext('2d');
		  // 创建属性节点
		  var anw = document.createAttribute("width");
		  anw.nodeValue = w;
		  var anh = document.createAttribute("height");
		  anh.nodeValue = h;
		  canvas.setAttributeNode(anw);
		  canvas.setAttributeNode(anh); 
		  ctxas.drawImage(that, 0, -270, w, h);
		  // 图像质量
		  if(obj.quality && obj.quality <= 1 && obj.quality > 0){
		   quality = obj.quality;
		  }
		  // quality值越小，所绘制出的图像越模糊
		  var base64 = canvas.toDataURL('image/jpg',quality);
		  // 回调函数返回base64的值
		  callback(base64);
	 };
	 
}
*/
