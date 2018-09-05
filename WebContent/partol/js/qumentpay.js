var num,price_name,description,addMealNum,addMealUnit,priceConfigId,price_num,amount,mealType;

//支付方式
//payWayType区分是支付宝还是微信银联 0--微信  1---支付宝  2---银联  。payMealType区分购买的套餐是流量还是时长  0--时长  1---流量
function payWay(payWayType){
	var siteId=$("#siteId").val(),
		userId=$("#userId").val();
	var param=$("#maps").text().replace("{","[").replace("}","]");
	if(payWayType==2){
		window.location.href= basePath+"/quickPayment/toPayment?siteId="+siteId+"&userId="+userId+"&maps="+param;
	}
	if(payWayType==1){
		window.location.href= basePath+"/pay/toAlipay?siteId="+siteId+"&userId="+userId+"&maps="+param;
	}
	if(payWayType==0){
//		$(".payguidewrap").toggle();
	window.location.href= basePath+"/wh/toWechat?siteId="+siteId+"&userId="+userId+"&maps="+param;
	}
}

$(window).load(function (){
	$(".payguidewrap").hide();
	$(".oncePay").click(function(){
		for (var i = 0; i < $(".choicepay-type input").length; i++) {
			if($(".choicepay-type input")[i].checked){
				payWay(i);
				break;
			}
		}
	});
	
}); 



