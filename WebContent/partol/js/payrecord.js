$(function(){
	var siteId = $('#siteId').val();
    var userId = $('#userId').val();
	createAllHtml(userId,siteId,1);
	
	
	//Default Action
	$(".tab_content").hide(); //Hide all content
	$("ul.tabs li:first").addClass("on").show(); //Activate first tab
	$(".tab_content:first").show(); //Show first tab content

	//On Click Event
	$("ul.tabs li").click(function() {
		$("ul.tabs li").removeClass("on"); //Remove any "active" class
		$(this).addClass("on"); //Add "active" class to selected tab
		$(".tab_content").hide(); //Hide all tab content
		var activeTab = $(this).find("a").attr("href"); //Find the rel attribute value to identify the active tab + content
		$(activeTab).fadeIn(); //Fade in the active content
		var n = $("ul.tabs li").index(this);
		var str =  $("ul.tabs li a").eq(n).text();
		var siteId = $('#siteId').val();
		var userId = $('#userId').val();
		 $("#page").css("display","none");
		createHtml(str,userId,siteId,1);
		return false;
	});
	 
});


function createHtml(str,userId,siteId,currentPage){
	
	if(str=="全部"){
		createAllHtml(userId,siteId,currentPage);
	}else if(str=="待支付"){
		noPaymentState(userId,siteId,currentPage);
	}else if(str=="待入账"){
		 prompt("暂无数据");
	}else if(str=="已完成"){
		finishOrder(userId,siteId,currentPage);
	}else{
		disabledOrder(userId,siteId,currentPage);
	}
}


/**
 * 全部的缴费记录
 * @param str
 * @returns
 */
function createAllHtml(userId,siteId,currentPage){
	 $.ajax({
		 type:"post",
		 url:ctx+"/record/allOrderRecards",
		 data:{
			 userId:userId,
			 siteId:siteId,
			 currentPage:currentPage
		 },
		 success:function(data){
			 if(data.code==200){
				 var list = data.data.list;
				 var html= '';
				 for (var i = 0; i < list.length; i++) {
					 html+="<div class='paidcontall clearfix'><div class='paid_state'>";
					 html+="    <span>订单号:"+list[i].orderNum+"</span> <a";
					 html+="	style='color: #007AFF; cursor: default;'>"+list[i].state+"</a>";
					 html+="</div>";
					 html+="<div class='paid-adress clearfix'>";
					 html+="    <div class='paidtext'>";
					 html+="	<i class='iconfont icon-liuliang'></i>";
					 html+="    <p>"+list[i].siteName+"-"+list[i].priceName+"</p>";
					 html+="    </div>";
					 html+="    <div class='paidnum'>";
					 html+="	<i class='iconfont icon-qian-copy'></i>"+parseFloat(list[i].allPirce)/parseInt(list[i].buyNum)+"<span>×"+list[i].buyNum+"</span>";
					 html+="    </div>";
					 html+="</div>";
					 html+="<div class='total'>";
					 html+="<em>合计:</em><i class='iconfont icon-qian-copy'></i>"+list[i].allPirce;
					 html+="</div></div>";
				 }
				 $("#tab1").html(html);
				 if(list.length  > 2){
					 $("#page").css("display","block");
						//显示分页
						laypage({
							cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
							pages:data.data.allPages, //通过后台拿到的总页数
							curr: currentPage || 1, //当前页
							jump: function(obj, first){ //触发分页后的回调
								if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
									createAllHtml(userId,siteId,obj.curr);
								}
							}
						});
				 }else{
					 $("#page").css("display","none");
				 }
				 
				 if(list.length==0){
					 prompt("暂无数据");
				 }
			 }else{
				 prompt("暂无数据");
			 }
		 }
	 });
	 
}
	 /**
	  * 获得待支付订单详情
	  */
	 function noPaymentState(userId,siteId,currentPage){
		 $.ajax({
			 type:"post",
			 url:ctx+"/record/noPaymentState",
			 data:{
				 userId:userId,
				 siteId:siteId,
				 currentPage:currentPage
			 },
			 success:function(data){
				 if(data.code==200){
					 $("#tab2").empty("");
					 var list = data.data.list;
					 var html= '';
					 for (var i = 0; i < list.length; i++) {
						 html+="<div class='paidcontall clearfix'><div class='paid_state'>";
						 html+="    <span>订单号:<i>"+list[i].orderNum+"</i></span> <a";
						 html+="	style='color: #007AFF; cursor: default;'>"+list[i].state+"</a>";
						 html+="</div>";
						 html+="<div class='paid-adress clearfix'>";
						 html+="    <div class='paidtext'>";
						 html+="	<i class='iconfont icon-liuliang'></i>";
						 html+="    <p>"+list[i].siteName+"-"+list[i].priceName+"</p>";
						 html+="    </div>";
						 html+="    <div class='paidnum'>";
						 html+="	<i class='iconfont icon-qian-copy'></i><i>"+parseFloat(list[i].allPirce)/parseInt(list[i].buyNum)+"</i><span>×"+list[i].buyNum+"</span>";
						 html+="    </div>";
						 html+="</div>";
						 html+="<div class='total'>";
						 html+=" <div id='total_first'><em>合计:</em><i class='iconfont icon-qian-copy'></i><i>"+list[i].allPirce+"</i></div>";
						 html+="<a><div id='total_absolutea'>确定</div> </a>";
						 html+="</div></div>";
					 }
					 $("#tab2").html(html);
					 if( list.length > 2){
						 $("#page").css("display","block");
							//显示分页
							laypage({
								cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
								pages:data.data.allPages, //通过后台拿到的总页数
								curr: currentPage || 1, //当前页
								jump: function(obj, first){ //触发分页后的回调
									if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
										noPaymentState(userId,siteId,obj.curr);
									}
								}
							});
							}else{
								$("#page").css("display","none");
							}
					 $("#total_absolutea").unbind("click");
					 $("#total_absolutea").click(function(){
						 var n= $(this).index();
						 noPayment(n);
					 });
					 
					 if(list.length==0){
						 prompt("暂无数据");
					 }
				 }else{
					 prompt("暂无数据");
				 }
			 }
		 });
	 }
	 /**
	  * 待支付去支付
	  */
	 function noPayment(n){
		 var siteId=$("#siteId").val(),
		 	 userId=$("#userId").val(),	
		 	 orderNum=$("#tab2 .paid_state span").eq(n).find("i").text(),
		 	 price=$("#tab2 .paid-adress .paidnum").eq(n).find("i").eq(1).text(),
		 	 num=$("#tab2 .paid-adress .paidnum").eq(n).find("span").text().replace(/[^0-9]+/g,''),
		 	 name=$("#tab2 .paid-adress").eq(0).find("p").text(),
		 	 amount=$("#tab2 .total").eq(n).find("i").eq(1).text();
		 var param="["+"siteId"+":"+"'"+siteId+"'"+","+"userId"+":"+"'"+userId+"'"+","+"orderNum"+":"+"'"+orderNum+"'"+","+"price"+":"+"'"+price+"'"+","+"num"+":"+"'"+num+"'"+","+"amount"+":"+"'"+amount+"'"+","+"name"+":"+"'"+name+"'"+"]";
		 window.location.href=ctx+"/record/toPay?maps="+param;
	 }
	 
	 
	 /**
	  * 已完成订单详情
	  * @param userId
	  * @param siteId
	  * @param currentPage
	  */
	 function finishOrder(userId,siteId,currentPage){
		 $.ajax({
			 type:"post",
			 url:ctx+"/record/finishOrder",
			 data:{
				 userId:userId,
				 siteId:siteId,
				 currentPage:currentPage
			 },
			 success:function(data){
				 if(data.code==200){
					 $("#tab3").empty("");
					 var list = data.data.list;
					 var html= '';
					 for (var i = 0; i < list.length; i++) {
						 html+="<div class='paidcontall clearfix'><div class='paid_state'>";
						 html+="    <span>订单号:"+list[i].orderNum+"</span> <a href='paydetails.html'";
						 html+="	style='color: #007AFF; cursor: default;'>"+list[i].state+"</a>";
						 html+="</div>";
						 html+="<div class='paid-adress clearfix'>";
						 html+="    <div class='paidtext'>";
						 html+="	<i class='iconfont icon-liuliang'></i>";
						 html+="    <p>"+list[i].siteName+"-"+list[i].priceName+"</p>";
						 html+="    </div>";
						 html+="    <div class='paidnum'>";
						 html+="	<i class='iconfont icon-qian-copy'></i>"+parseFloat(list[i].allPirce)/parseInt(list[i].buyNum)+"<span>×"+list[i].buyNum+"</span>";
						 html+="    </div>";
						 html+="</div>";
						 html+="<div class='total'>";
						 html+="    <em>合计:</em><i class='iconfont icon-qian-copy'></i>"+list[i].allPirce+"";
						 html+="</div></div>";
					 }
					 $("#tab3").html(html);
					 if(list.length > 2){
						 $("#page").css("display","block");
							//显示分页
							laypage({
								cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
								pages:data.data.allPages, //通过后台拿到的总页数
								curr: currentPage || 1, //当前页
								jump: function(obj, first){ //触发分页后的回调
									if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
										finishOrder(userId,siteId,obj.curr);
									}
								}
							});
							}else{
								$("#page").css("display","none");
							}
					 if(list.length==0){
						 prompt("暂无数据");
					 }
				 }else{
					 prompt("暂无数据");
				 }
			 }
		 });
	 }
	 
	 /**
	  * 已失效订单详情
	  * @param userId
	  * @param siteId
	  * @param currentPage
	  */
	 function disabledOrder(userId,siteId,currentPage){
		 $.ajax({
			 type:"post",
			 url:ctx+"/record/disabledOrder",
			 data:{
				 userId:userId,
				 siteId:siteId,
				 currentPage:currentPage
			 },
			 success:function(data){
				 if(data.code==200){
					 $("#tab4").empty("");
					 var list = data.data.list;
					 var html= '';
					 for (var i = 0; i < list.length; i++) {
						 html+="<div class='paidcontall clearfix'><div class='paid_state'>";
						 html+="    <span>订单号:"+list[i].orderNum+"</span> <a href='paydetails.html'";
						 html+="	style='color: #007AFF; cursor: default;'>"+list[i].state+"</a>";
						 html+="</div>";
						 html+="<div class='paid-adress clearfix'>";
						 html+="    <div class='paidtext'>";
						 html+="	<i class='iconfont icon-liuliang'></i>";
						 html+="    <p>"+list[i].siteName+"-"+list[i].priceName+"</p>";
						 html+="    </div>";
						 html+="    <div class='paidnum'>";
						 html+="	<i class='iconfont icon-qian-copy'></i>"+parseFloat(list[i].allPirce)/parseInt(list[i].buyNum)+"<span>×"+list[i].buyNum+"</span>";
						 html+="    </div>";
						 html+="</div>";
						 html+="<div class='total'>";
						 html+="    <em>合计:</em><i class='iconfont icon-qian-copy'></i>"+list[i].allPirce+"";
						 html+="</div></div>";
					 }
					 $("#tab4").html(html);
					 if(list.length > 2){
						 $("#page").css("display","block");
						 //显示分页
					    laypage({
					      cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
					      pages:data.data.allPages, //通过后台拿到的总页数
					      curr: currentPage || 1, //当前页
					      jump: function(obj, first){ //触发分页后的回调
					        if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
					        	disabledOrder(userId,siteId,obj.curr);
					        }
					      }
					    });
					 }
					 if(list.length==0){
						 prompt("暂无数据");
					 }
				 }else{
					 prompt("暂无数据");
				 }
			 }
		 });
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