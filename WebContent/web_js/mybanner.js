//将当前显示的BANNER图片的索引值设置为0，也就是默认是第一个图片的序号
var t = n = aa = 0, count,tt;
$(document).ready(function() {
	tt = mobileType();
	getAD();
	if(count == 0 || count == undefined){
		count = $("#banner_list img").length;
	}
	// 设置默认显示第一张BANNER图，其他的隐藏
	$("#banner_list img:not(:first-child)").hide();
	// 设置默认的定时器 每4s执行一次showauto函数
	t = setInterval("showAuto()", 4000);
	// 容器#banner鼠标移入后 清清除定时器 （也就是鼠标移动到BANNER上面停止切换），移开则继续切换
	$("#banner").hover(function() {
		clearInterval(t);
	}, function() {
		t = setInterval("showAuto()", 2000);
	});
})

// 定义自动切换的函数
function showAuto() {
	// 如果当前切换的图片索引值大于所有图片的数量
	// 也就是当切换到最后一张图片再继续切换的时候 则将当前图片索引设置为0（第一张）
	// 否则则将当前图片的索引值+1
	n = n >= (count - 1) ? 0 : ++n;
	// 重新触发当前BANNER的click事件
	$("#banner li").eq(n).trigger('click');
	var a = 0;
	if(aa == 1){
		a = $("#ad"+n).val();
	}

}

function liclick(i) {
	// 将i的值赋值给新变量n（也就是当前显示图片的索引值序号）
	n = i;
	// 如果被点击的数字按钮的值大于等于BANNER的总数则退出当前语句
	if (i >= count) {
		return;
	}
	// 将所有的BANNER图淡入隐藏 当前点击按钮对应的BANNER图片淡入显示
	$("#banner_list img").fadeOut(200).eq(i).fadeIn(1000);
	$("#banner_list a").hide().stop(true, true).eq(n).fadeIn("slow");
	// 将容器#banner的背景设置为空
	document.getElementById("banner").style.background = "";
	// 将当前点击按钮的样式设置为高亮样式on
	$("#banner li").eq(i).toggleClass("on");
	// 清除其它点击按钮的高亮样式
	$("#banner li").eq(i).siblings().removeAttr("class");
}

function imgclick(i) {
	var a = 0;
	if(aa == 1){
		a = $("#ad"+i).val();
	}
	var d = $("#banner_list a").eq(i).attr('href');
	operation(a, tt, mac, d, 2);
}

// 判断是安卓还是IOS 1表示Android，0表示IOS
function mobileType() {
	var u = navigator.userAgent, app = navigator.appVersion;
	if (/AppleWebKit.*Mobile/i.test(navigator.userAgent)
			|| (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/
					.test(navigator.userAgent))) {
		if (window.location.href.indexOf("?mobile") < 0) {
			try {
				if (/iPhone|mac|iPod|iPad/i.test(navigator.userAgent)) {
					return '0';
				} else {
					return '1';
				}
			} catch (e) {
			}
		}
	} else if (u.indexOf('iPad') > -1) {
		return '0';
	} else {
		return '1';
	}
}

function getAD() {
	$.ajax({
		url : base + "/PROT/getAD",
		data : {},
		dataType : "json",
		type : "post",
		success : function(data) {
			if (data != null && data.length > 0) {
				aa = 1;
				var htmlStr = "";
				var liStr = "";
				var inputstr = "";
				count = data.length;
				$.each(data, function(index, obj) {
					var aurl = tt == 0 ? obj.iosUrl : obj.aUrl;
					if(index == 0){
						htmlStr = '<img src="'+url+'/'+ obj.image + '" title="'+obj.title+'" onclick="imgclick('+index+')" width="100%" height="100%" /><a href="' + aurl + '" target="_blank"></a>';
						liStr = '<li class="on" onclick="liclick('+index+')"></li>';
					}else{
						htmlStr += '<img src="'+url+'/'+ obj.image + '" title="'+obj.title+'" onclick="imgclick('+index+')" width="100%" height="100%" /><a href="' + aurl + '" target="_blank"></a>';
						liStr += '<li onclick="liclick('+index+')"></li>';
					}
					inputstr += '<input type="hidden" id="ad'+index+'" value="'+obj.id+'"/>';
				});
				$("#banner_list").html(htmlStr);
				$("#banner ul").html(liStr);
				$(".main").append(inputstr);
				var a = $("#ad0").val();
				operation(a, tt, mac, '#', 1);
			}
		}
	});
}

function operation(a, b, c, d,e) {
	if(a == 0){
		window.open(d);
		return;
	}
	var newWindow;
	if(e == 2){
		newWindow = window.open();
	}
	$.ajax({
		type : "post",
		url : base + "/PROT/operation",
		data : {
			adId : a,
			type : b,
			usermac : c,
			op : e
		},
		dataType : "json",
		success : function(data) {
			if(e == 2){
				//修改新窗口的url
	            newWindow.location.href = d;
			}
			
		}
	});
}
