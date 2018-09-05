var num,price_name,description,addMealNum,addMealUnit,priceConfigId,price_num,amount,mealType;
$(function(){
	num="",price_name="",description="",addMealNum="",addMealUnit="",priceConfigId="",price_num="",price="",mealType="";
	//获取滑动的值
	$("#succ1").on("click", function() {
		$(".Describe_a,.buytimewrap").show();
        $(".choose-wrap-box").hide();
//        $(".Describe_a").hide();
		num = $(".swiper-container .swiper-slide-active").eq(0).find("p").html(),//购买数量
	 	price_name = $(".swiper-container1 .swiper-slide-active").eq(0).find("p").html(),//购买名称
	 	description=$(".swiper-container1 .swiper-slide-active").eq(0).find("p").attr("data-text"),//套餐描述
		addMealNum=$(".swiper-container1 .swiper-slide-active").eq(0).find("p").attr("mealNum"),//优惠增送的数量
		addMealUnit=$(".swiper-container1 .swiper-slide-active").eq(0).find("p").attr("mealType"),//优惠赠送的单位
		priceConfigId=$(".swiper-container1 .swiper-slide-active").eq(0).find("p").attr("value"),//场所收费配置Id
		price_num=$(".swiper-container1 .swiper-slide-active").eq(0).find("p").attr("price_num"),//购买套餐配置数量
		amount=$(".swiper-container1 .swiper-slide-active").eq(0).find("p").attr("prices"),//购套餐单价
		mealType=1;//购买套餐的类型 1 -- 时长套餐  , 2---流量套餐
			
		$("#tiemType1").text(price_name);
		$("#numType1").text(num);
		sumMoney(0,num,amount);
		addMealNum=='0'?$(".detailsnum").css("display","none"):$(".addTime").html(addMealUnit==0?addMealNum+"时":addMealUnit==1?addMealNum+"天":addMealNum+"月");
		
	});
	$("#succ2").on("click", function() {
		$(".Describe_a,.trafficwrap").show();
        $(".choose-wrap-box").hide();
//        $(".Describe_a").hide();
		num = $(".swiper-container .swiper-slide-active").eq(1).find("p").html(),//购买数量
	 	price_name = $(".swiper-container1 .swiper-slide-active").eq(1).find("p").html(),//购买名称
	 	description=$(".swiper-container1 .swiper-slide-active").eq(1).find("p").attr("data-text"),//套餐描述
		addMealNum=$(".swiper-container1 .swiper-slide-active").eq(1).find("p").attr("mealNum"),//优惠增送的数量
		addMealUnit=$(".swiper-container1 .swiper-slide-active").eq(1).find("p").attr("mealType"),//优惠赠送的单位
		priceConfigId=$(".swiper-container1 .swiper-slide-active").eq(1).find("p").attr("value"),//场所收费配置Id
		price_num=$(".swiper-container1 .swiper-slide-active").eq(1).find("p").attr("price_num"),//购买套餐配置数量
		amount=$(".swiper-container1 .swiper-slide-active").eq(1).find("p").attr("prices"),//购套餐单价
		mealType=2;//购买套餐的类型 1 -- 时长套餐  , 2---流量套餐
		$("#tiemType2").text(price_name);
		$("#numType2").text(num);
		sumMoney(1,num,amount);
		addMealNum=='0'?$(".detailsnum").css("display","none"):$(".addflow").html(addMealUnit==4?addMealNum+"M":addMealNum+"G");
	});
	
	//购买时常显示支付模块
	$(".success").on("click",function() {
		$("#choose-type").hide();
		$(".choose-wrap").hide();
		$(".Describe").hide();
		$(".Describe_a").hide();
		$("#pay").show();
		$(".allMoney").html($(".timeprice1").html());
		
	});
	//购买流量显示支付模块
	$(".success1").on("click",function() {
		$("#choose-type").hide();
		$(".choose-wrap").hide();
		$(".Describe").hide();
		$(".Describe_a").hide();
		$("#pay").show();
		$(".allMoney").html($(".flowprice1").html());
		
	});
	//购买套餐显示支付模块
	$(".success2").on("click",function() {
		$("#choose-type").hide();
		$(".choose-wrap").hide();
		$(".Describe").hide();
		$(".Describe_a").hide();
		$("#pay").show();
		$(".allMoney").html();
	});
	
	//去支付
	$(".topay").click(function(){
		for (var i = 0; i < $(".choicepay-type input").length; i++) {
			if($(".choicepay-type input")[i].checked){
				payWay(i);
				break;
			}
		}
	});
	
	$(".buyTime").on("click",function  () {
        $(".buytime").show();
        $(".Describe").show();
        $(".buytime_a").show();
        $(".active2").eq(0).addClass("on").siblings().removeClass("on");
        $(".trafficdate,.cheaper,.choose-type_son").hide();
		 var len=$(".tLi").length;
		 if(len==0){
			 $(".choose-type_son").show();
			 $(".buytime,.trafficdate,.Describe,.cheaper").hide();
			 prompt("暂未开放该套餐");
			 return false;
		 } 
      });
      $(".buyFlow").on("click",function  () {
        $(".trafficdate").show();
        $(".Describe").show();
        $(".trafficdate_a").show();
        $(".active2").eq(1).addClass("on").siblings().removeClass("on");
        $(".buytime,.cheaper,.choose-type_son").hide();
		 var len=$(".fLi").length;
		 if(len==0){
			 $(".choose-type_son").show();
			 $(".buytime,.trafficdate,.Describe,.cheaper").hide();
			 prompt("暂未开放该套餐");
			 return false;
		 }
      });
      $(".back_remove_b").on("click",function  () {
    	  $(".buytime").show();
          $(".Describe,.choose-wrap-box").show();
          $(".buytime_a").show();
          $(".active2").eq(0).addClass("on").siblings().removeClass("on");
          $(".Describe_a,.trafficwrap").hide();
      });
      $(".buyInformation").on("click",function  () {
        $(".cheaper").show();
        $(".Describe").show();
        $(".cheaper_a").show();
        $(".active2").eq(2).addClass("on").siblings().removeClass("on");
        $(".buytime,.trafficdate,.choose-type_son").hide();
		 var len=$(".preferential>ul>li").length;
		 if(len=="0"){
			 $(".choose-type_son").show();
			 $(".buytime,.trafficdate,.Describe,.cheaper").hide();
			 prompt("暂未开放该套餐");
		 	 return false; 
		 }
      });
      $(".back_remove_a").click(function  () {
        $(".buytime").show();
        $(".Describe,.choose-wrap-box").show();
        $(".buytime_a").show();
        $(".active2").eq(0).addClass("on").siblings().removeClass("on");
          $(".Describe_a,.buytimewrap").hide();
      });
      $(".back_remove_b").click(function  () {
        $(".buytime").show();
        $(".Describe,.choose-wrap-box").show();
        $(".trafficdate_a").show();
        $(".active2").eq(1).addClass("on").siblings().removeClass("on");
          $(".Describe_a,.buytimewrap").hide();
      });
      $(".back_remove").click(function  () {
        $(".Describe,.buytime_a,.trafficdate_a,.cheaper_a").hide();
        $(".choose-type_son").show();
      });
      
  	$(".buy_sur1").on("click",function  () {
		var len=$(".tLi").length;
		$(".buytime,.trafficdate,.Describe,.cheaper").hide();
		$(".Describe").show();
		 if(len==0){
			 prompt("暂未开放该套餐");
			 return false;
		 }
		 $(".buytime_a").show();
	});
	$(".buy_sur2").on("click",function  () {
		 var len=$(".fLi").length;
		 $(".buytime,.trafficdate,.Describe,.cheaper").hide();
		 $(".Describe").show();
		 if(len==0){
			 prompt("暂未开放该套餐");
			 return false;
		 }
		 $(".trafficdate_a").show();
	});
	$(".buy_sur3").on("click",function  () {
		 var len=$(".preferential>ul>li").length;
		 $(".buytime,.trafficdate,.Describe,.cheaper_a").hide();
		 $(".Describe").show();
		 if(len==0){
			 prompt("暂未开放该套餐");
			 return false;
		 }
		 $(".cheaper_a").show();
	});
      
  	$('.active2').click(function() {
		$(this).addClass('on');
		$(this).addClass("on").find(".text i").css('display', 'inline-block');
		$(this).siblings().removeClass("on").find(".text i").hide();
		//$(".choose-wrap-box > div").eq($(this).index()).fadeTo("slow", 1).siblings().hide();
		
	});
	/*****************************************************************************/
	
});
//支付方式
function showSuss(show_cls, hide_cls) {
	$('.' + show_cls).fadeIn();
	$('.' + hide_cls).fadeOut();
}
function closeLayer(close){
	$("."+ close).fadeOut()	
}
//计算总额
function sumMoney(type,num,price) { //type  0----时长  1----流量
	var re = /\D/g;
	if(re.test(num)){
		submitFlag=false;
		return;
	}
	var sumMoney = (price * num).toFixed(2);
	if(type==0){//时长
		$(".timeprice1").html(sumMoney);
	}else if(type==1){//流量
		$(".flowprice1").html(sumMoney);
	}else if(type==2){//套餐
		
	}
}
function checkSubmit(){
	var num=$(".payNum input").val();
	if(num==0||num==""||num==null){
		submitFlag=false;
		msg(0,"请输入整数");
	}
	return submitFlag;
}
//支付方式
//payWayType区分是支付宝还是微信银联 0--微信  1---支付宝  2---银联  。payMealType区分购买的套餐是流量还是时长  0--时长  1---流量
function payWay(payWayType){
//	var amount =$(".paySum span").text();//总金额
//	var priceConfigId=$(".payTypeList .on").attr("value");//场所收费配置Id
//	var num = $(".payNum input").val();//购买数量
//	var price_num=$(".payTypeList .on").attr("price_num");//购买套餐配置数量
//	var price_name=$(".payType span").text().trim();//购买名称
//	var addMealNum=$(".payTypeList .on").attr("mealNum");//优惠赠送的数量
//	var addMealUnit=$(".payTypeList .on").attr("mealType");//优惠赠送的单位
//	var mealType=mType;//购买套餐的类型 1 -- 时长套餐  , 2---流量套餐
	var siteId=$("#siteId").val(),
		userId=$("#userId").val();
	if(payWayType==2){
		window.location.href= basePath+"/quickPayment/clickPayment?nums="+num+"&amount="+amount+"&priceConfig="+priceConfigId+"&siteId="+siteId+"&userId="+userId+
							"&price_num="+price_num+"&price_name="+price_name+"&addMealNum="+addMealNum+"&addMealUnit="+addMealUnit+"&mealType="+mealType;
	}
	if(payWayType==1){
		window.location.href= basePath+"/pay/aliPay?nums="+num+"&amount="+amount+"&priceConfig="+priceConfigId+"&siteId="+siteId+"&userId="+userId+
		"&price_num="+price_num+"&price_Name="+price_name+"&addMealNum="+addMealNum+"&addMealUnit="+addMealUnit+"&mealType="+mealType;

	}
	if(payWayType==0){
		window.location.href= basePath+"/wh/wechatpay?nums="+num+"&amount="+amount+"&priceConfig="+priceConfigId+"&siteId="+siteId+"&userId="+userId+
		"&price_num="+price_num+"&price_Name="+price_name+"&addMealNum="+addMealNum+"&addMealUnit="+addMealUnit+"&mealType="+mealType;
	}
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

