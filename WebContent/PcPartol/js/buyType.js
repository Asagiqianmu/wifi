/**
 * Created by WangHao on 2017/3/29.
 */
// 时长单位,购买数量,总结额联动效果
var flag;
var reg = /[0-9A-Za-z]/;
var key;
var pag = 0;
var checkState;
var num;
var price_name
var description;
var addMealNum;
var addMealUnit;
var priceConfigId;
var price_num;
var amount;
var mealType;
var dmoney;
var money1;
var intervalProcess;
var order;
$(function() {
	/* 格式化用户名为121****2323格式 */
	function doFomat(username) {
		var name = username.substring(0, 3);
		var end = username.substring(7, username.length);
		var allname = name + "****" + end;
		$(".pwd-second").text(allname);
	}
	// 判断手机号是否存在
	var user = $('#user').val();
	var telphones = $('#username').val();
	var passwords = $('#passwordzhi').val();
	if (telphones == "") {
		$('.second').hide();
		$('.cell_phone').show();
	} else {
		$('.second').show();
		$('.cell_phone').hide();
		doFomat(telphones);
		$("#next_h").on("click", function() {
			login(passwords, '2');
		});
	}
	var phone = $('.num_tel').text();
	var mphone1 = phone.replace(/\s/g, '');
	var mphone_first = mphone1.substring(0, 3);
	var mphone_last = mphone1.substring(7, 11);
	var mphone = mphone1.substring(3, 7);
	mphone = mphone.replace(mphone, "****")
	// 此时mphone 的值就是中间四位显示为*号的了；
	$('.num_tel').text(mphone_first + mphone + mphone_last)
	// 二次登陆
	var phone1 = $('.pwd-second').text();
	var mphone2 = phone1.replace(/\s/g, '');
	var mphone_first2 = mphone2.substring(0, 3);
	var mphone_last2 = mphone2.substring(7, 11);
	var mphone2 = mphone2.substring(3, 7);
	mphone2 = mphone2.replace(mphone2, "****");

	$(".increase,.decrease").click(function() {
		var num = $("#num").val();
		num = parseFloat(num);
		total = num * money;
		total = Math.floor(total * 100) / 100;
		$(".total").text(total);
	});

	$(".increase2,.decrease2").click(function() {
		var num = $("#num1").val();
		num = parseFloat(num);
		total = num * money1;
		total = Math.floor(total * 100) / 100;
		$(".total_a").text(total);
	});

	// 首页点击充值方式的显示
	$(".recharge_body_time").click(function() {
		$(".recharge_body").hide();
		$(".buyType_body").show();
		$(".left_container>div").removeClass("on").eq(0).addClass("on");
		$(".pay").hide();
		$("#time").show();
		$("#flow,#info").hide();
		recharge_body_times();

	});

	$(".recharge_body_flow").click(function() {
		
		if($("#select2").length==0){
			$(".left_container>div").removeClass("on").eq(1).addClass("on");
			$(".recharge_body").hide();
			$(".buyType_body").show();
			$("#info").show();
			$(".pay").hide();
			$("#time,#flow").hide();
			return false;
		}
		$(".recharge_body").hide();
		$(".buyType_body").show();
		$(".left_container>div").removeClass("on").eq(1).addClass("on");
		$("#flow").show();
		$(".pay").hide();
		$("#time,#info").hide();
		recharge_body_flows();
	});

	$(".recharge_body_info").click(function() {
		$(".left_container>div").removeClass("on").eq(2).addClass("on");
		$(".recharge_body").hide();
		$(".buyType_body").show();
		$("#info").show();
		$(".pay").hide();
		$("#time,#flow").hide();
	});

	// 左边充值方式添加样式
	$(".buyType_time").click(function() {
		$("#time").show();
		$(".pay").hide();
		$("#flow,#info").hide();
		recharge_body_times();
	});

	$(".buyType_flow").click(function() {
		if($("#select2").length==0){
			$("#info").show();
			$(".pay").hide();
			$("#time,#flow").hide();
			return false;
		}
		$("#flow").show();
		$(".pay").hide();
		$("#time,#info").hide();
		recharge_body_flows();
	});

	$(".buyType_info").click(function() {
		$("#info").show();
		$(".pay").hide();
		$("#time,#flow").hide();
	});

	$(".left_container>div").removeClass("on").eq(0).addClass("on");
	$(".left_container>div").click(
			function() {
				$(".left_container>div").removeClass("on").eq($(this).index())
						.addClass("on");
			});

	// 修改密码的验证 phone
	$("#password").on("change", function() {
		var password = $("#password").val();
		if (password == "") {
			layer.msg('请填写登录密码');
			return false;
		} else if (password.length < 6) {
			layer.msg('请填写6位及以上登录密码');
			return false;
		} else {
			$("#next_f,#next_d").show();
		}
	});
	$("#next_f").click(function() {
		var password = $("#password").val();
		if (password == "") {
			layer.msg('请填写登录密码');
			return false;
		} else if (password.length < 6) {
			layer.msg('请填写6位及以上登录密码');
			return false;
		} else {
			$("#next_d").show();
			login(password, "2");
		}

	});

	// 去支付 充值
	$(".pay_footr_a").on("click", function() {
		for (var i = 0; i < $(".pay_way input").length; i++) {
			if ($(".pay_way input")[i].checked) {
				payWay(i);
				break;
			}
		}
	});
	// 待支付去支付 充值
	$(".pay_footrs").on("click", function() {
		for (var i = 0; i < $(".pay_way input").length; i++) {
			if ($(".pay_way input")[i].checked) {
				payWays(i);
				break;
			}
		}
	});

	$(".btn_back").click(function() {
		$('#Identifying_input').val("");
		$(".enp_a").show()
		$(".identifying").hide();
	});

	// 手机号码验证
	$("#next_a").on("click", function() {
		var phone = $("#phone").val();
		if (!/^(13[0-9]|14[0-9]|17[0-9]|16[0-9]|15[0-9]|18[0-9])\d{8}$/i.test(phone)) {
			layer.msg('请填写正确的手机号码');
			return false;
		} else if (phone == "") {
			layer.msg('请填写手机号码');
			return false;
		} else {
			var str = $("#phone").val();
			checkPhone();// 手机验证
		}
	});

	// 输入验证码跳转个人中心
	$("#next_b").on("click", function() {
		codes($("#next_b").attr("state"));
	});

	// 跳转设置新密码
	$(".findPassword").on("click", function() {
		$(".identifying").show();
		$(".enp_a").hide();
	});

	// 设置完密码
	$("#next_d").on("click", function() {
		var password = $("#password_a").val();
		if (password == "") {
			layer.msg('请填写登录密码');
			return false;
		} else if (password.length < 6) {
			layer.msg('请填写6位及以上登录密码');
			return false;
		} else {
			$("#next_d").show();
			login(password, "1");
		}
	});

	// 修改密码验证码验证
	$("#next_c").on("click", function() {
		var str = $("#checkCode").text();
		var str1 = str.replace(/\s/g, '');
		var Identifying_input = $("#Identifying_input").val();
		if (str1 == Identifying_input) {
			$(".security_code").show();
			$(".identifying").hide();
			$(".num_phone").text($('#phone').val());
			Identifying('2');// 获取验证码
		} else {
			layer.msg("验证码错误", function() {
				$(".cell_phone").hide();
			});
		}
	});

	$(".pay_Alert_foot").on('click', function() {
		$(".alert").hide();
		$(".pay_Alert").hide();
		$(".pay").show();
	});

	// 切换账号
	$(".btn_back2").on("click", function() {
		$(".cell_phone").show();
		$(".second").hide();
	});

	// 个人中心左侧字体变化与右侧切换
	$(".foor_left>div").click(function() {
		$(".personal_body_right,.pay,.rechargeRecord").hide();
		$(this).addClass("on").siblings().removeClass("on");
		$(".tabContent>div").hide().eq($(this).index()).show();
	})
	// 头像设置和资料设置的切换
	$(".title_headSon>p").click(function() {
		$(this).addClass("on").siblings().removeClass("on");
		$("#personal_center>div").hide().eq($(this).index()).show();
	});

	// 个人中心右侧底部切换
	$(".personalModal li").removeClass("active").eq(0).addClass("active");
	$(".personalModal li").mouseenter(
			function() {
				$(".personalModal li").removeClass("active")
						.eq($(this).index()).addClass("active");
			});

	// 返回个人中心默认样式
	$(".glyphicons").click(function() {
		$("#tab1,#tab2,#tab3,#tab4,#tab5,.pay").hide();
		$(".personal_first").show();
	});

	$(".glyphicon_a").click(function() {
		$(".rechargeRecord").hide();
		$(".personal_body").show();
	});

	// 支付金额
	$(".timePay").click(function() {
		var total = $(".total_b").text();
		$(".pay").show();
		$(".buyType_body_right").hide();
		$(".payMoney>span,.pay_rowRight>span").text(total);
		$("#rowLefts").text($('#siteName').val() + "-" + "时长套餐");
		amount = total;
		num = $("#num").val();
		mealType = "1";
	});

	$(".flowPay").click(function() {
		var total_a = $(".total_a").text();
		$(".pay").show();
		$(".buyType_body_right").hide();
		$(".payMoney>span,.pay_rowRight>span").text(total_a);
		$("#rowLefts").text($('#siteName').val() + "-" + "流量套餐");
		amount = total_a;
		num = $("#num1").val();
		mealType = "2";
	});

	$(".BtnRight").click(
			function() {
				var total = $(this).parents(".container_row_a").find(
						".marginRight>span").text();
				$(".pay").show();
				$(".buyType_body_right").hide();
				$(".payMoney>span,.pay_rowRight>span").text(total);
			});
	//关闭微信扫码支付窗口
	 $(".pay_close").on("click",function(){
		 clearInterval(intervalProcess);
		 $(".pay_footr_a").attr("disabled",false);
     	$(".pay_Alert").hide(); 
      });
});
// 结束
function recharge_body_flows() {
	var money1 = $("#money1").text();
	$('.MealNumrowRight')
			.text($('#select2 option').eq(0).attr('mealNum') + "M");
	$("#money1").text($('#select2 option').eq(0).attr('prices'));
	money1 = parseFloat($("#money1").text());
	$("#money1").text(money1);
	$('.total_a').text(money1);

	// 支付所用参数值
	priceConfigId = $('#select2 option').eq(0).attr('valueId');
	num = $("#num1").val();
	price_num = $('#select2 option').eq(0).attr('price_num');
	price_name = $('#select2 option').eq(0).attr('price_name');
	addMealNum = $('#select2 option').eq(0).attr('mealNum');
	addMealUnit = $('#select2 option').eq(0).attr('mealType');

	$("#select2").on(
			"change",
			function() {
				var sel_a = document.getElementById("select2");
				var selected_val_a = sel_a.options[sel_a.selectedIndex].value;
				if (selected_val_a == "4") {
					$("#num1").val("1");
					$("#money1")
							.text($('#select2 option').eq(0).attr('prices'));
					money1 = parseFloat($("#money1").text());
					$('.MealNumrowRight').text(
							$('#select2 option').eq(0).attr('mealNum') + "M");
					$('.total_a').text(money1);
					$("#money1").text(money1);

					// 支付所用参数值
					priceConfigId = $('#select2 option').eq(0).attr('valueId');
					price_num = $('#select2 option').eq(0).attr('price_num');
					price_name = $('#select2 option').eq(0).attr('price_name');
					addMealNum = $('#select2 option').eq(0).attr('mealNum');
					addMealUnit = $('#select2 option').eq(0).attr('mealType');

				}
				if (selected_val_a == "5") {
					$("#num1").val("1");
					$("#money1")
							.text($('#select2 option').eq(1).attr('prices'));
					money1 = parseFloat($("#money1").text());
					$('.MealNumrowRight').text(
							$('#select2 option').eq(1).attr('mealNum') + "G");
					$('.total_a').text(money1);
					$("#money1").text(money1);

					// 支付所用参数值
					priceConfigId = $('#select2 option').eq(1).attr('valueId');
					price_num = $('#select2 option').eq(1).attr('price_num');
					price_name = $('#select2 option').eq(1).attr('price_name');
					addMealNum = $('#select2 option').eq(1).attr('mealNum');
					addMealUnit = $('#select2 option').eq(1).attr('mealType');

				}
			})

	$(".increase,.decrease").click(function() {
		var num = $("#num").val();
		num = parseFloat(num);
		total = num * money;
		total = Math.floor(total * 100) / 100;
		$(".total").text(total);
	});

	$(".increase2,.decrease2").click(function() {
		var num = $("#num1").val();
		num = parseFloat(num);
		total = num * money1;
		total = Math.floor(total * 100) / 100;
		$(".total_a").text(total);
	});

	$("#num1").keyup(function() {
		var reg = /^[0-9]+$/;
		var num = $("#num1").val();
		if (reg.test(num)) {
			num = parseFloat(num);
			total = num * money1;
			total = Math.floor(total * 100) / 100;
			$(".total_a").text(total);
		} else {
			layer.msg('请不要输入非法数字');
		}

	});
}
function recharge_body_times() {
	// 购买方式取值select
	var money = $("#money").text();
	$('.containerMealNum')
			.text($('#select option').eq(0).attr('mealNum') + "时");
	$("#money").text($('#select option').eq(0).attr('prices'));
	money = parseFloat($("#money").text());
	$('.total_b').text(money);
	$('#money').text(money);

	// 支付所用参数值
	priceConfigId = $('#select option').eq(0).attr('valueId');
	num = $("#num").val();
	price_num = $('#select option').eq(0).attr('price_num');
	price_name = $('#select option').eq(0).attr('price_name');
	addMealNum = $('#select option').eq(0).attr('mealNum');
	addMealUnit = $('#select option').eq(0).attr('mealType');

	$("#select").on(
			"change",
			function() {
				var sel = document.getElementById("select");
				var selected_val = sel.options[sel.selectedIndex].value;
				if (selected_val == "0") {
					$("#num").val("1");
					$("#money")
							.text(
									$('#select option').eq(selected_val).attr(
											'prices'));
					$('.containerMealNum').text(
							$('#select option').eq(selected_val)
									.attr('mealNum')
									+ "小时");
					money = parseFloat($("#money").text());
					$('.total_b').text(money);
					$('#money').text(money);
					// 支付所用参数值
					priceConfigId = $('#select option').eq(selected_val).attr(
							'valueId');
					price_num = $('#select option').eq(selected_val).attr(
							'price_num');
					price_name = $('#select option').eq(selected_val).attr(
							'price_name');
					addMealNum = $('#select option').eq(selected_val).attr(
							'mealNum');
					addMealUnit = $('#select option').eq(selected_val).attr(
							'mealType');

				}
				if (selected_val == "1") {
					$("#num").val("1");
					$("#money")
							.text(
									$('#select option').eq(selected_val).attr(
											'prices'));
					money = parseFloat($("#money").text());
					$('.containerMealNum').text(
							$('#select option').eq(selected_val)
									.attr('mealNum')
									+ "天");
					$('.total_b').text(money);
					$('#money').text(money);
					// 支付所用参数值
					priceConfigId = $('#select option').eq(selected_val).attr(
							'valueId');
					price_num = $('#select option').eq(selected_val).attr(
							'price_num');
					price_name = $('#select option').eq(selected_val).attr(
							'price_name');
					addMealNum = $('#select option').eq(selected_val).attr(
							'mealNum');
					addMealUnit = $('#select option').eq(selected_val).attr(
							'mealType');

				}
				if (selected_val == "2") {
					$("#num").val("1");
					$("#money")
							.text(
									$('#select option').eq(selected_val).attr(
											'prices'));
					money = parseFloat($("#money").text());
					$('.containerMealNum').text(
							$('#select option').eq(selected_val)
									.attr('mealNum')
									+ "月");
					$('.total_b').text(money);
					$('#money').text(money);
					// 支付所用参数值
					priceConfigId = $('#select option').eq(selected_val).attr(
							'valueId');
					price_num = $('#select option').eq(selected_val).attr(
							'price_num');
					price_name = $('#select option').eq(selected_val).attr(
							'price_name');
					addMealNum = $('#select option').eq(selected_val).attr(
							'mealNum');
					addMealUnit = $('#select option').eq(selected_val).attr(
							'mealType');

				}
			})
	$(".increase,.decrease").click(function() {
		var num = $("#num").val();
		num = parseFloat(num);
		total = num * money;
		total = Math.floor(total * 100) / 100;
		$(".total").text(total);
	});

	$(".increase2,.decrease2").click(function() {
		var num = $("#num1").val();
		num = parseFloat(num);
		total = num * money1;
		total = Math.floor(total * 100) / 100;
		$(".total_a").text(total);
	});
	$("#num").keyup(function() {
		var num = $("#num").val();
		var reg = /^[0-9]+$/;
		if (reg.test(num)) {
			num = parseFloat(num);
			total = num * money;
			total = Math.floor(total * 100) / 100;
			$(".total").text(total);
		} else {
			layer.msg('请不要输入非法数字');
		}
	});
}
function rechargeRecord() {
	var siteId = $('#siteId').val();
	var userId = $('#userId').val();
	createAllHtml(userId, siteId, 1);
	$(".tab-head").find("li").each(function() {
		$(this).click(function() {
			$(this).addClass("on").siblings().removeClass("on");
			$(".rechargeRecord_contear>div").hide().eq($(this).index()).show();
			createHtml($(this).text(), userId, siteId, 1);
		})
	})
}

$("#recharge").click(function() {
	$(".personal_first").hide();
	$(".rechargeRecord").show();
});
// 缴费记录切换
function createHtml(str, userId, siteId, currentPage) {

	if (str == "全部") {
		createAllHtml(userId, siteId, currentPage);
	}
	if (str == "待支付") {
		noPaymentState(userId, siteId, currentPage);
	}
	if (str == "已完成") {
		finishOrder(userId, siteId, currentPage);
	}
	if (str == "已失效") {
		disabledOrder(userId, siteId, currentPage);
	}
}

/**
 * 全部的缴费记录
 * 
 * @param str
 * @returns
 */
function createAllHtml(userId, siteId, currentPage) {
	$
			.ajax({
				type : "post",
				url : base + "/record/allOrderRecards",
				data : {
					userId : userId,
					siteId : siteId,
					currentPage : currentPage
				},
				success : function(data) {
					if (data.code == 200) {
						var list = data.data.list;
						var html = '';
						for (var i = 0; i < list.length; i++) {
							// if(list[i].state != "已失效"){//PC处理已失效
							html += "<div class='paidcontall clearfix'><div class='paid_state'>";
							html += "    <span>订单号:" + list[i].orderNum
									+ "</span> <a";
							html += "	style='color: #007AFF; cursor: default;'>"
									+ list[i].state + "</a>";
							html += "</div>";
							html += "<div class='paid-adress clearfix'>";
							html += "    <div class='paidtext'>";
							html += "	<i class='iconfont icon-liuliang'></i>";
							html += "    <p>" + list[i].siteName + "-"
									+ list[i].priceName + "</p>";
							html += "    </div>";
							html += "    <div class='paidnum'>";
							html += "	<i class='iconfont icon-qian-copy'></i>"
									+ parseFloat(list[i].allPirce)
									/ parseInt(list[i].buyNum) + "<span>×"
									+ list[i].buyNum + "</span>";
							html += "    </div>";
							html += "</div>";
							html += "<div class='total'>";
							html += "<em>合计:</em><i class='iconfont icon-qian-copy'></i>"
									+ list[i].allPirce;
							html += "</div></div>";
							// }
						}
						$(".tab_a").html(html);
						if (list.length > 2) {
							$("#page").css("display", "block");
							// 显示分页
							laypage({
								cont : 'page', // 容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div
												// id="page1"></div>
								pages : data.data.allPages, // 通过后台拿到的总页数
								curr : currentPage || 1, // 当前页
								jump : function(obj, first) { // 触发分页后的回调
									if (!first) { // 点击跳页触发函数自身，并传递当前页：obj.curr
										createAllHtml(userId, siteId, obj.curr);
									}
								}
							});
						} else {
							$("#page").css("display", "none");
						}
						if (list.length == 0) {
							prompt("暂无数据");
						}
					} else {
						prompt("暂无数据");
					}
				}
			});
}
/**
 * 获得待支付订单详情
 */
function noPaymentState(userId, siteId, currentPage) {
	$
			.ajax({
				type : "post",
				url : base + "/record/noPaymentState",
				data : {
					userId : userId,
					siteId : siteId,
					currentPage : currentPage
				},
				success : function(data) {
					if (data.code == 200) {
						var list = data.data.list;
						var html = '';
						for (var i = 0; i < list.length; i++) {
							var json = JSON.parse(list[i].paramString);
							html += "<div class='paidcontall clearfix'><div class='paid_state'>";
							html += "    <span>订单号:<i class='orderNum'>"
									+ list[i].orderNum + "</i></span> <a";
							html += "	style='color: #007AFF; cursor: default;'>"
									+ list[i].state + "</a>";
							html += "</div>";
							html += "<div class='paid-adress clearfix'>";
							html += "    <div class='paidtext'>";
							html += "	<i class='iconfont icon-liuliang'></i>";
							html += "    <p class='namess'>" + list[i].siteName
									+ "-" + list[i].priceName + "</p>";
							html += "    <p class='priceName' style='display: none;'>"
									+ list[i].priceName + "</p>";
							html += "    <p class='idss' style='display: none;'>"
									+ json.payType + "</p>";
							html += "    <p class='addMealNum' style='display: none;'>"
									+ json.addMealNum + "</p>";
							html += "    <p class='addMealUnit' style='display: none;'>"
									+ json.addMealUnit + "</p>";
							html += "    <p class='mealType' style='display: none;'>"
									+ json.mealType + "</p>";
							html += "    <p class='allPirce' style='display: none;'>"
									+ json.priceNum + "</p>";
							html += "    </div>";
							html += "    <div class='paidnum'>";
							html += "	<i class='iconfont icon-qian-copy'></i><i >"
									+ parseFloat(list[i].allPirce)
									/ parseInt(list[i].buyNum)
									+ "</i><i>"
									+ '×'
									+ "</i><span class='num'>"
									+ list[i].buyNum + "</span>";
							html += "    </div>";
							html += "</div>";
							html += "<div class='total'>";
							html += " <div id='total_first'><em>合计:</em><i class='iconfont icon-qian-copy'></i><i class='amount'>"
									+ list[i].allPirce + "</i></div>";
							html += "<div   class='total_absolutea' >确定</div>";
							html += "</div></div>";
						}
						$(".tab_b").html(html);
						if (list.length > 2) {
							$("#page").css("display", "block");
							// 显示分页
							laypage({
								cont : 'page', // 容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div
												// id="page1"></div>
								pages : data.data.allPages, // 通过后台拿到的总页数
								curr : currentPage || 1, // 当前页
								jump : function(obj, first) { // 触发分页后的回调
									if (!first) { // 点击跳页触发函数自身，并传递当前页：obj.curr
										noPaymentState(userId, siteId, obj.curr);
									}
								}
							});
						} else {
							$("#page").css("display", "none");
						}
						// 去支付/充值
						$(".total_absolutea").click(
								function() {
									$(".pay").show();
									$(".rechargeRecord").hide();
									var amount = $(this)
											.parents(".paidcontall").find(
													".amount").text();
									var namess = $(this)
											.parents(".paidcontall").find(
													".namess").text();
									var allPirce = $(this).parents(
											".paidcontall").find(".allPirce")
											.text();
									var orderNum = $(this).parents(
											".paidcontall").find(".orderNum")
											.text();
									var num = $(this).parents(".paidcontall")
											.find(".num").text();
									var priceName = $(this).parents(
											".paidcontall").find(".priceName")
											.text();
									var idss = $(this).parents(".paidcontall")
											.find(".idss").text();
									var siteId = $("#siteId").val();
									var userId = $("#userId").val();
									var addMealNum = $(this).parents(
											".paidcontall").find(".addMealNum")
											.text();
									var addMealUnit = $(this).parents(
											".paidcontall")
											.find(".addMealUnit").text();
									var mealType = $(this).parents(
											".paidcontall").find(".mealType")
											.text();
									$('.payMoney>span').text(amount);
									$('.pay_rowRight>span').text(amount);
									$('.pay_rowLeft').text(namess);
									$('#allPirce').val(allPirce);// 单价
									$('#orderNum').val(orderNum);// 订单号
									$('#num').val(num);// 数量
									$('#priceName').val(priceName);// 套餐名
									$('#idss').val(idss);
									$('#addMealNum').val(addMealNum);
									$('#addMealUnit').val(addMealUnit);
									$('#mealType').val(mealType);
								});
						if (data.data.allPages == 0) {
							prompt("暂无数据");
						}
					} else {
						prompt("暂无数据");
					}
				}
			});
}
/**
 * 待支付去支付 function noPayment(n){ var siteId=$("#siteId").val(),
 * userId=$("#userId").val(), orderNum=$(".tab_b .paid_state
 * span").eq(n).find("i").text(), price=$(".tab_b .paid-adress
 * .paidnum").eq(n).find("i").eq(1).text(), num=$(".tab_b .paid-adress
 * .paidnum").eq(n).find("span").text().replace(/[^0-9]+/g,''), name=$(".tab_b
 * .paid-adress").eq(0).find("p").text(), amount=$(".tab_b
 * .total").eq(n).find("i").eq(1).text(); var
 * param="["+"siteId"+":"+"'"+siteId+"'"+","+"userId"+":"+"'"+userId+"'"+","+"orderNum"+":"+"'"+orderNum+"'"+","+"price"+":"+"'"+price+"'"+","+"num"+":"+"'"+num+"'"+","+"amount"+":"+"'"+amount+"'"+","+"name"+":"+"'"+name+"'"+"]";
 * window.location.href=base+"/record/toPay?maps="+param; }
 */

/**
 * 已完成订单详情
 * 
 * @param userId
 * @param siteId
 * @param currentPage
 */
function finishOrder(userId, siteId, currentPage) {
	$
			.ajax({
				type : "post",
				url : base + "/record/finishOrder",
				data : {
					userId : userId,
					siteId : siteId,
					currentPage : currentPage
				},
				success : function(data) {
					if (data.code == 200) {
						var list = data.data.list;
						var html = '';
						for (var i = 0; i < list.length; i++) {
							html += "<div class='paidcontall clearfix'><div class='paid_state'>";
							html += "    <span>订单号:" + list[i].orderNum
									+ "</span> <a href='paydetails.html'";
							html += "	style='color: #007AFF; cursor: default;'>"
									+ list[i].state + "</a>";
							html += "</div>";
							html += "<div class='paid-adress clearfix'>";
							html += "    <div class='paidtext'>";
							html += "	<i class='iconfont icon-liuliang'></i>";
							html += "    <p>" + list[i].siteName + "-"
									+ list[i].priceName + "</p>";
							html += "    </div>";
							html += "    <div class='paidnum'>";
							html += "	<i class='iconfont icon-qian-copy'></i>"
									+ parseFloat(list[i].allPirce)
									/ parseInt(list[i].buyNum) + "<span>×"
									+ list[i].buyNum + "</span>";
							html += "    </div>";
							html += "</div>";
							html += "<div class='total'>";
							html += "    <em>合计:</em><i class='iconfont icon-qian-copy'></i>"
									+ list[i].allPirce + "";
							html += "</div></div>";
						}
						$(".tab_c").html(html);
						if (list.length > 2) {
							$("#page").css("display", "block");
							// 显示分页
							laypage({
								cont : 'page', // 容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div
												// id="page1"></div>
								pages : data.data.allPages, // 通过后台拿到的总页数
								curr : currentPage || 1, // 当前页
								jump : function(obj, first) { // 触发分页后的回调
									if (!first) { // 点击跳页触发函数自身，并传递当前页：obj.curr
										finishOrder(userId, siteId, obj.curr);
									}
								}
							});
						} else {
							$("#page").css("display", "none");
						}
						if (list.length == 0) {
							prompt("暂无数据");
						}
					} else {
						prompt("暂无数据");
					}
				}
			});
}

/**
 * 已失效订单详情
 * 
 * @param userId
 * @param siteId
 * @param currentPage
 */
function disabledOrder(userId, siteId, currentPage) {
	$
			.ajax({
				type : "post",
				url : base + "/record/disabledOrder",
				data : {
					userId : userId,
					siteId : siteId,
					currentPage : currentPage
				},
				success : function(data) {
					if (data.code == 200) {
						var list = data.data.list;
						var html = '';
						for (var i = 0; i < list.length; i++) {
							html += "<div class='paidcontall clearfix'><div class='paid_state'>";
							html += "    <span>订单号:" + list[i].orderNum
									+ "</span> <a href='paydetails.html'";
							html += "	style='color: #007AFF; cursor: default;'>"
									+ list[i].state + "</a>";
							html += "</div>";
							html += "<div class='paid-adress clearfix'>";
							html += "    <div class='paidtext'>";
							html += "	<i class='iconfont icon-liuliang'></i>";
							html += "    <p>" + list[i].siteName + "-"
									+ list[i].priceName + "</p>";
							html += "    </div>";
							html += "    <div class='paidnum'>";
							html += "	<i class='iconfont icon-qian-copy'></i>"
									+ parseFloat(list[i].allPirce)
									/ parseInt(list[i].buyNum) + "<span>×"
									+ list[i].buyNum + "</span>";
							html += "    </div>";
							html += "</div>";
							html += "<div class='total'>";
							html += "    <em>合计:</em><i class='iconfont icon-qian-copy'></i>"
									+ list[i].allPirce + "";
							html += "</div></div>";
						}
						$(".tab_d").html(html);
						if (list.length > 2) {
							$("#page").css("display", "block");
							// 显示分页
							laypage({
								cont : 'page', // 容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div
												// id="page1"></div>
								pages : data.data.allPages, // 通过后台拿到的总页数
								curr : currentPage || 1, // 当前页
								jump : function(obj, first) { // 触发分页后的回调
									if (!first) { // 点击跳页触发函数自身，并传递当前页：obj.curr
										disabledOrder(userId, siteId, obj.curr);
									}
								}
							});
						}
						if (list.length == 0) {
							prompt("暂无数据");
						}
					} else {
						prompt("暂无数据");
					}
				}
			});
}

// 手机校验
function checkPhone() {
	var userName = $("#phone").val(), url = base + "/login/checkPhone";
	$.ajax({
		type : "POST",
		url : url,
		data : {
			'userName' : userName
		},
		success : function(data) {
			var res = data.result;
			tojumpByresult(res);
		}
	});
};

// 根据用户状态跳动到相对应的页面
function tojumpByresult(n) {
	if (n == 1) {// 第二次登录设置密码
		$('#next_b').attr("state", n);
		$(".security_code").show();
		$(".cell_phone").css('display', 'none');
		$(".num_phone").text($('#phone').val());
		Identifying('1');// 获取验证码
		checkState = '1';
	}
	if (n == 2) {// 第三次登录
		var phone = $('#phone').val();
		$('#next_b').attr("state", n);
		$(".enp_a").show();
		$(".cell_phone").css('display', 'none');
		pag = 1;// 代表用户输出密码登录
		checkState = '2';
	}
	if (n == 0) {
		$('#next_b').attr("state", n);
		$(".security_code").show();
		$(".cell_phone").css('display', 'none');
		$(".num_phone").text($('#phone').val());
		Identifying('0');// 获取验证码
		checkState = '0';
	}
}
// 获得手机验证码
function getMessage(state) {
	var phone = $("#phone").val().replace(/\s/g,"");
	var mac = $("#mac1").val();
	var ip = $("#ip").val();
	bodyRSA(); // 加密方式
	if(mac==''){
		mac = $("#mac").val();
	}
	var results = phone + "," + mac + "," + ip;
	var parms = encryptedString(key, encodeURIComponent(results.replace(/\s/g,"")));
	$.ajax({
		url : base + "/code/msgRandCode",
		data : {
			parms : parms,
			style : state
		},
		dataType : "json",
		success : function(data) {
			if (data.result != 0) {
				boolean = false;
				prompt(data.msg);
			}
		},
	});
}
// 发送验证码
function Identifying(state) {
	var i = 120;
	getMessage(state);
	timer = setInterval(function() {
		i--;
		$('#btn_code').val('已发送(' + i + ')').prop('disabled', true).css(
				'background-color', 'gainsboro');
		if (i == 0) {
			$('#btn_code').val('获取验证码').prop('disabled', false).css(
					'background-color', '');
			clearInterval(timer);
			$('#btn_code').click(function() {
				getMessage(state);
			})
		}
	}, 1000);
}
// 页面发送验证码加密方式
function bodyRSA() {
	setMaxDigits(130);
	key = new RSAKeyPair(
			"10001",
			"",
			"81bfe67f578dfef2d972875accd55ff5dcbee0f6c18eb5b43d1d7e9a6b9ed63698d788892f91568a8c72ef8617eb469985d3df5c2fc32e1fd881f90052415bdf1a60ba0a8752c4a0974efad6bfaf08e640a923d416c109760b84b9f9302cbc99515c40874ff1d7be94bf171f0324d7113f7d3ab359f57e937e0663a29549de2b");
}
/* 提示框 */
function prompt(msg) {
	layer.msg(msg);
}
// 第二次跟验证码校验，修改密码验证码校验
function codes(n) {
	var phone = $('#phone').val().replace(/\s/g,"");// 获取手机号
	var code = $("#Identifying_input_a").val();// 获取验证码号
	$.ajax({
		type : "post",
		url : base + "/login/codes",
		data : {
			userName : phone,
			code : code
		},
		dataType : "json",
		success : function(data) {
			$('.loading').fadeOut();
			$("#fuckCode").hide();
			if (data.result == "0") {
				if (n == 1) {// 判断是第二次登录手机号
//					$('.setPassword').show();
//					$('.security_code').hide();
					pag = 2;// 代表用户第二次登录修改密码并登录
					login(code, 1);
				} 
//				
//				else if (n == 0) {// 判断首次手机号 验证码正确后。直接跳转个人中心页面
//					login(code, n);
//				} else if (n == 2) {
//					$('.security_code').hide();// 隐藏验证码。显示设置新密码
//					$('.setPassword').show();
//					pag = 3;// 代表用户忘记密码
//				}
			} else {// 验证码校验失败
				prompt("验证码输入有误")
			}
		},
	});
}

// 登录 ，后跳渲染页面
function login(passwordss, result) {
	var userName = $("#phone").val();
	if (userName != null && userName != "") {
		userName = $("#phone").val();
	} else {
		userName = $("#username").val();
	}
	var code = $("#Identifying_input_a").val();
	$.ajax({
		type : "post",
		url : base + "/login/uniteLogin",
		data : {
			userName : userName,
			password : passwordss,
			code : code,
			siteId : $(".aginlogin h1").attr("value"),
			result : result,
			as : getJson()
		},
		dataType : "json",
		success : function(data) {
			$('.loading').fadeOut();
			$('.passnote').fadeOut();
			if (data.result == 200) {
				$('.loading').fadeIn();
				window.location.href = data.url;
				return;
			} else if (data.result == 201) {// 场所不存在
				prompt(data.msg);
			} else if (data.result == 203) {
				window.location.href = base + "/login/toLockPc?locakTime="
						+ data.times + "&userName=" + userName + "&changeurl="
						+ escape(window.location.href);
			} else if (data.result == 205) {// 用户名或密码错误
				prompt(data.msg);
			} else if (data.result == 206) {
				prompt(data.msg);
			} else if (data.result == 208) {
				prompt(data.msg);
			} else if (data.result == 209) {
				prompt(data.msg);
			} else if (data.result == 300) {
				prompt(data.msg);
			} else if (data.result == 301) {
				// 账号被锁
				window.location.href = base + "/login/toLockPc?changeurl="
						+ escape(window.location.href) + "&locakTime="
						+ data.times + "&userName=" + userName;
				// }else if(data.result==302){
				// //实名认证
				// $('.passnote').text(data.msg);
				// $('.passnote').fadeIn();
			} else if (data.result == 304) {
				// 账号已登录
				prompt(data.msg);
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

// 支付方式 payWayType区分是支付宝还是微信银联 1--微信 0---支付宝 2---银联 。payMealType区分购买的套餐是流量还是时长
// 0--时长 1---流量
function payWay(payWayType) {
	var siteId = $("#siteId").val();// 场所ID
	var userId = $("#userId").val();// 用户ID
	if (payWayType == 2) {
		window.location.href = base + "/pcQuickPayment/pcPayment?nums=" + num
				+ "&amount=" + amount + "&priceConfig=" + priceConfigId
				+ "&siteId=" + siteId + "&userId=" + userId + "&price_num="
				+ price_num + "&price_name=" + price_name + "&addMealNum="
				+ addMealNum + "&addMealUnit=" + addMealUnit + "&mealType="
				+ mealType;
	}
	if (payWayType == 0) {
		window.location.href = base + "/rechargeLog/gopay?nums=" + num
				+ "&amount=" + amount + "&priceConfig=" + priceConfigId
				+ "&siteId=" + siteId + "&userId=" + userId + "&price_num="
				+ price_num + "&price_name=" + price_name + "&addMealNum="
				+ addMealNum + "&addMealUnit=" + addMealUnit + "&mealType="
				+ mealType;

	}
	if (payWayType == 1) {
		wechatpayPc(num, amount, priceConfigId, siteId, userId, price_num,
				price_name, addMealNum, addMealUnit, mealType);
	}
}
function wechatpayPc(nums, amount, priceConfig, siteId, userId, price_num,
		price_name, addMealNum, addMealUnit, mealType) {
	$.ajax({
		type : "post",
		url : base + "/whPc/wechatpayPc",
		data : {
			nums : nums,
			amount : amount,
			priceConfig : priceConfig,
			siteId : siteId,
			userId : userId,
			price_num : price_num,
			price_Name : price_name,
			addMealNum : addMealNum,
			addMealUnit : addMealUnit,
			mealType : mealType
		},
		dataType : "json",
		success : function(data) {
			if (data.code == "200") {
				$("#code").html("");
				$("#code").qrcode({
					render : "table", // table方式
					width : 250, // 宽度
					height : 250, // 高度
					text : data.payUrl
				// 任意内容
				});
				$(".pay_Alert").show();
				$(".pay_footr_a").attr("disabled",true);
				order=data.orderNum;
				intervalProcess = setInterval( "checkPayResultForWchat()" , 1000 );
			} else if (data.code == "201") {
				layer.msg(data.msg);
			} else if (data.code == "202") {
				layer.msg(data.msg);
			}
		}
	})
}
// 支付方式payWayType区分是支付宝还是微信银联 1--微信 0---支付宝 2---银联 。payMealType区分购买的套餐是流量还是时长
// 0--时长 1---流量
function payWays(payWayType) {
	var siteId = $("#siteId").val();// 场所ID
	var userId = $("#userId").val();// 用户ID
	var amount = $('.payMoney>span').text();// 总价
	var namess = $('.pay_rowLeft').text();// 套餐名称
	var allPirce = $('#allPirce').val();// 单价
	var num = $('#num').val();// 数量
	var orderNum = $('#orderNum').val();// 订单号
	var priceName = $('#priceName').val();
	var idss = $('#idss').val();
	var addMealNum = $('#addMealNum').val();
	var addMealUnit = $('#addMealUnit').val();
	var mealType = $('#mealType').val();
	if (payWayType == 2) {
		window.location.href = base + "/pcQuickPayment/toPcPayment?siteId="
				+ siteId + "&userId=" + userId + "&orderNum=" + orderNum
				+ "&nums=" + num + "&price_Name=" + priceName + "&allPirce="
				+ allPirce + "&amount=" + amount + "&idss=" + idss
				+ "&addMealNum=" + addMealNum + "&addMealUnit=" + addMealUnit
				+ "&mealType=" + mealType;
	}
	if (payWayType == 0) {
		window.location.href = base + "/rechargeLog/toGoPay?siteId=" + siteId
				+ "&userId=" + userId + "&orderNum=" + orderNum + "&nums="
				+ num + "&price_Name=" + priceName + "&allPirce=" + allPirce
				+ "&amount=" + amount + "&idss=" + idss + "&addMealNum="
				+ addMealNum + "&addMealUnit=" + addMealUnit + "&mealType="
				+ mealType;
	}
	if (payWayType == 1) {
		// layer.msg("暂未开放");
		wechatpayPcs(siteId, userId, orderNum, num, priceName, allPirce,
				price_name, amount, idss, addMealNum, addMealUnit, mealType);
		//				window.location.href= base+"/wechatpayPc/towechatpayPc?siteId="+siteId+"&userId="+userId+"&orderNum="+orderNum+"&nums="+num+"&price_Name="+priceName+"&allPirce="+allPirce+"&amount="+amount+"&idss="+idss+"&addMealNum="+addMealNum+"&addMealUnit="+addMealUnit+"&mealType="+mealType;
	}
}
//待支付
function wechatpayPcs(siteId, userId, orderNum, num, priceName, allPirce,
		price_name, amount, idss, addMealNum, addMealUnit, mealType) {
	$.ajax({
		type : "post",
		url : base + "/whPc/toWechat",
		data : {
			siteId : siteId,
			userId : userId,
			orderNum : orderNum,
			nums : num,
			price_Name : priceName,
			allPirce : allPirce,
			amount : amount,
			idss : idss,
			addMealNum : addMealNum,
			addMealUnit : addMealUnit,
			mealType : mealType
		},
		dataType : "json",
		success : function(data) {
			if (data.code == "200") {
				$(".pay_footrs").attr("disabled",true);
				$("#code").html("");
				$("#code").qrcode({
					render : "table", //table方式 
					width : 250, //宽度 
					height : 250, //高度 
					text : data.payUrl
				//任意内容 
				});
				$(".pay_Alert").show();
				intervalProcess = setInterval( checkPayResultForWchat(data.orderNum) , 1000 );
			} else if (data.code == "201") {
				layer.msg(data.msg);
			} else if (data.code == "202") {
				layer.msg(data.msg);
			}
		}
	})
}
/*校验pc扫码支付是否已经完成*/
function checkPayResultForWchat(){
	$.ajax({
		type:"post",
		url:base+"/whPc/checkPay",
		data:{
			orderNum:order
		},
		success:function(data){
			if(data.code==200){
				clearInterval(intervalProcess);
				window.location.href=base+"/personal/editAfter?userName="+$("#username").val()+"&siteId="+$("#siteId").val();
			}
		}
	})
}
