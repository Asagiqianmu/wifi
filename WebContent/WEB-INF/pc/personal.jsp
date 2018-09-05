<%@page import="javax.swing.text.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="path" value="${ctx}/PcPartol" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!--确保IE浏览器采用最新的渲染模式-->
<title>个人中心</title>
<link rel="stylesheet" href="${path }/bootstrap/css/bootstrap1.min.css">
<link rel="stylesheet" type="text/css"
	href="${path }/fonts/iconfont.css">
<link rel="stylesheet" type="text/css" href="${path }/css/reset.css" />
<link rel="stylesheet" type="text/css" href="${path }/css/recharge.css" />
<style type="text/css">
.layui-layer-btn .layui-layer-btn0 {
	margin-right: 100px;
}

.layui-layer-content {
	text-align: center;
}

.pay_close {
	position: absolute;
	right: 21px;
	top: 10px;
	color: #fff;
	font-size: 25px;
}
</style>
</head>
<body>
	<div class="main">
		<div class="common_head">
			<img src="${path}/img/recharge_head.png" />
		</div>
		<div class="personal_body clearfix" style="display: block;">
			<!--这是左侧套餐详情-->
			<div class="personal_body_left">
				<div class="title_head"></div>
				<div class="headPortrait">
					<div class="perheadpic">
						<!-- ${obj.map.user.imageUrl} -->
						<c:if
							test="${obj.map.user.imageUrl eq null ||  obj.map.user.imageUrl eq ''}">
							<img src="${path}/img/T.jpg" />
						</c:if>
						<c:if
							test="${obj.map.user.imageUrl ne null && obj.map.user.imageUrl ne ''}">
							<img
								src="http://oss.kdfwifi.net/user_pic/${obj.map.user.imageUrl}" />
						</c:if>
					</div>
					<div class="num_tel">
						${obj.map.user.userName}
						<!--18292059695-->
					</div>
					<div class="last_perheadpic">完善个人资料送免费送</div>
				</div>
				<div class="foor_left">
					<!-- ${obj.map.user.userName} -->
					<div class="clearfix" onclick="personal()">
						<div class="pullLeft">个人资料</div>
						<div class="pullRight">
							<i class="iconfont icon-arrow-right"></i>
						</div>
					</div>
					<div class="clearfix" onclick="message()">
						<div class="pullLeft">我的消息</div>
						<div class="pullRight">
							<i class="iconfont icon-arrow-right"></i>
						</div>
						<div class="num_tips"></div>
					</div>
					<div class="clearfix">
						<div class="pullLeft">账号管理</div>
						<div class="pullRight">
							<i class="iconfont icon-arrow-right"></i>
						</div>
					</div>
					<div class="clearfix">
						<div class="pullLeft">游戏中心</div>
						<div class="pullRight">
							<i class="iconfont icon-arrow-right"></i>
						</div>
					</div>
					<div class="clearfix last_border">
						<div class="pullLeft">联系客服</div>
						<div class="pullRight">
							<i class="iconfont icon-arrow-right"></i>
						</div>
					</div>
				</div>
			</div>

			<!--这是左侧套餐详情 end-->
			<!--这是右侧套餐详情-->
			<div class="personal_body_right personal_first">
				<div class="title_head"></div>
				<div class="heading">购买剩余</div>
				<div class="containerPsl">
					<div class="buy-time clearfix">
						<div class="buy-timeOne">
							<i class="iconfont icon-shijian"></i>
						</div>
						<div class="progress">
							<div
								class="progress-bar progress-bar-success progress-bar-striped active"
								id="hidepcent-a">
								<div class="progress-value1" style="margin-top: -12px;"
									id="hidepcent">0%</div>
							</div>
						</div>
						<div class="dayTime"></div>
					</div>
				</div>
				<div class="containerPsl">
					<div class="buy-time clearfix">
						<div class="buy-timeOne">
							<i class="iconfont icon-0067rili-copy"></i>
						</div>
						<div class="progress">
							<div
								class="progress-bar progress-bar-success progress-bar-striped active"
								id="showpcent-a">
								<div class="progress-value1" style="margin-top: -12px;"
									id="showpcent">0%</div>
							</div>
						</div>
						<div class="flow"></div>
					</div>
				</div>

				<ul class="personalModal">
					<li class="recharge_a" onclick="payCharge()">
						<p>
							<i class="iconfont icon-chongzhi"></i>
						</p>
						<p>
							<span>充值</span>
						</p>
					</li>
					<li class="editnamemodle"
						style="margin-top: 20px; line-height: 63px;" onclick="xiaxian();">
						<p>
							<i class="iconfont icon-xiaxian"></i>
						</p>
						<p>
							<span>下线</span>
						</p>
					</li>
					<li id="recharge" onclick="rechargeRecord()">
						<p>
							<i class="iconfont icon-jilu"></i>
						</p>
						<p>
							<span>充值记录</span>
						</p>
					</li>
					<li class="active" onclick="Interinta()">
						<p>
							<i class="iconfont  icon-wangluo"></i>
						</p>
						<p>
							<span>去上网</span>
						</p>
					</li>
				</ul>
			</div>

			<!--支付模块-->
			<div class="pay" style="display: none;">
				<div class="recharge_body_title">
					<div class="glyphicons"><</div>
					支付方式
				</div>
				<div class="containerPay">
					<div class="payTop">支付总金额</div>
					<div class="payMoney">
						￥<span> </span>
					</div>
					<div class="pay_row clearfix">
						<div class="pay_rowLeft"></div>
						<div class="pay_rowRight">
							￥<span> </span>
						</div>
					</div>
					<div class="pay_title">选择支付方式</div>
					<div class="pay_way">
						<div class="pay-zhifubo">
							<input type="radio" checked="checked" name="pay" value="2" /> <img
								src="${path}/img/zhifubao.png"
								style="width: 16px; height: 16px;" /> <span> 支付宝 </span>
						</div>
						<div class="pay-weixin">
							<input type="radio" name="pay" value="1" /> <img
								src="${path}/img/wechat.jpg" style="width: 18px; height: 18px;" />
							<span> 微信支付 </span>
						</div>
						<div class="pay-yinlian">
							<input type="radio" name="pay" value="3" /> <img
								src="${path}/img/7-14012623104L93.png"
								style="width: 16px; height: 16px;" /> <span> 银行卡 </span>
						</div>
					</div>
				</div>
				<div class="pay_footr pay_footrs">立即支付</div>
			</div>


			<!--充值记录-->
			<div class="rechargeRecord" style="display: none;">
				<div class="title_head">
					<div class="glyphicons glyphicon_a"><</div>
					充值记录
				</div>
				<div class="rechargeRecord_head">
					<ul class="tab-head clearfix">
						<li class="on">全部</li>
						<li>待支付</li>
						<li>已完成</li>
						<li>已失效</li>
					</ul>
				</div>
				<div class="rechargeRecord_tab">
					<div class="rechargeRecord_contear">
						<div class="tab_a"></div>

						<div class="tab_b"></div>

						<div class="tab_c"></div>
						<div class="tab_d"></div>
					</div>
					<div id="page"></div>
				</div>
			</div>


			<div class="tabContent">
				<div class="personal_body_right" id="tab1">
					<div class="title_head">
						<div class="glyphicons"><</div>
						<!--<spna class="glyphicon glyphicon-chevron-left"></spna>-->
						<div class="title_headSon">
							<p class="on">资料设置</p>
							<p>头像设置</p>
						</div>
					</div>

					<div id="personal_center">
						<div class="personal_centerLeft">
							<div class="row_center clearfix">
								<div class="row_centerLeft">用户名:</div>
								<div class="row_centerRight phoneUser">
									${obj.map.user.userName}
									<!--18292059695-->
								</div>
							</div>
							<div class="row_center clearfix">
								<div class="row_centerLeft">昵&nbsp;&nbsp; 称:</div>
								<div class="row_centerRight">
									<input type="text" name="input_user" id="input_user"
										placeholder="起一个独一无二的昵称吧" />
								</div>
							</div>
							<div class="row_center clearfix">
								<div class="row_centerLeft">性&nbsp;&nbsp; 别:</div>
								<div class="row_centerRight">
									<div class="sexMan">
										<input type="radio" checked="checked" name="sex" id="sex1"
											value="男" /> <span>男</span>
									</div>
									<div class="sexWoman">
										<input type="radio" name="sex" id="sex2" value="女" /> <span>女</span>
									</div>
								</div>
							</div>
							<div class="row_center clearfix">
								<div class="row_centerLeft">生&nbsp;&nbsp; 日:</div>
								<div class="row_centerRight">
									<input id="hello" class="laydate-icon" placeholder="生日当天送上网时长">
								</div>
							</div>
							<div id="submitBtn" onclick="addpersonal()">确 认</div>
						</div>
						<div class="personal_centerRight">
							<div class="Portrait">
								<img class="previewid" id="photo"
									src="http://oss.kdfwifi.net/user_pic/${obj.map.user.imageUrl}" />
							</div>
							<div class="Portrait_btn">
								<div class="Portrait_file">
									 点击上传 <input type="file" name="uploadimg" id="uploadimg"
										class="uploadimg"
										onchange="setImagePreview(this,'companycontain')" />
								</div>
								<div class="prese" onclick="imgSave()">保存头像</div>
							</div>
						</div>
					</div>
					<div class="personal_foor">更新资料</div>
				</div>
				<div class="personal_body_right" id="tab2">
					<div class="title_head">
						<div class="glyphicons"><</div>
					</div>
					<div id="news"></div>
					<div class="news_tips">暂无您的消息</div>
				</div>
				<div class="personal_body_right" id="tab3">
					<div class="title_head">
						<div class="glyphicons"><</div>
					</div>
					<img src="${path}/img/kaifazhong.png" />
					<p class="personal_rl">账号管理正在开发中，目前您可以在</p>
					<p>手机版个人中心中使用此功能！</p>
				</div>
				<div class="personal_body_right" id="tab4">
					<div class="title_head">
						<div class="glyphicons"><</div>
					</div>
					<img src="${path}/img/kaifazhong.png" />
					<p class="personal_rl">PC版游戏中心正在开发中，目前您可以在</p>
					<p>手机版个人中心中使用此功能！</p>
				</div>
				<div class="personal_body_right" id="tab5">
					<div class="title_head">
						<div class="glyphicons"><</div>
					</div>
					<img src="${path}/img/kefu.png" />
					<p class="personal_rl">飞讯无限官方客服电话</p>
					<p>${obj.map.site.adminer}</p>
				</div>
			</div>
			<!-- ==========personal_body  -->
		</div>

		<!--支付成功模块-->
		<div class="pay_succes">
			<div class="recharge_body_title"></div>
			<div class="pay_succes_title">支付成功！</div>
			<p class="pay_succes_p">成功支付到入账有时可能存在延迟,如未及时到账请</p>
			<p class="pay_succes_p">
				耐心等待，或拨打客服电话<span>15972935811</span>
			</p>
			<div class="checkBalance">查询余额</div>
		</div>

		<!--支付失败模块-->
		<div class="pay_error">
			<div class="recharge_body_title"></div>
			<div class="pay_succes_title">支付成功！</div>
			<p class="pay_succes_p">成功支付到入账有时可能存在延迟,如未及时到账请</p>
			<p class="pay_succes_p">
				耐心等待，或拨打客服电话<span>15972935811</span>
			</p>
			<div class="checkBalance">查询余额</div>
		</div>
		<div class="alert"></div>
		<div class="pay_Alert" style="display: none;">
			<div class="recharge_body_title">微信支付</div>
			<p class="pay_succes_p" style="margin-top: 100px; font-size: 25px;">
				请使用手机微信扫码完成支付</p>
			<div class="pay_AlertImg">
				<div id="code" style="width: 200px; margin: 56px 270px;"></div>
			</div>
			<div class="pay_close">x</div>
		</div>
		<input type="hidden" id="userId" value="${obj.map.user.id}"> <input
			type="hidden" id="siteId" value="${obj.map.site.id}"> <input
			type="hidden" id="siteType" value="${obj.map.type}"> <input
			type="hidden" id="sysType" value="${sessionScope.solarsys}">
		<input type="hidden" id="syas" value="${obj.map.syas}"> <input
			type="hidden" id="user" value="${obj.map.user}"> <input
			type="hidden" id="username" value="${obj.map.user.userName}">
		<input type="hidden" id="password" value="${obj.map.user.passWord}">
		<input type="hidden" id="allIp" value="${obj.map.type.uamip}">
		<input type="hidden" id="allPirce"> <input type="hidden"
			id="orderNum"> <input type="hidden" id="num"> <input
			type="hidden" id="priceName"> <input type="hidden" id="idss">
		<input type="hidden" id="addMealNum"> <input type="hidden"
			id="addMealUnit"> <input type="hidden" id="mealType">
	</div>
	<textarea id="companycontain" style="display: none;" type="text"
		name="userinfo"></textarea>
	<!--  <script type='text/javascript' src='http://cdn.staticfile.org/jquery/2.1.1/jquery.min.js'></script> -->
	<script src="${path }/js/jquery-1.11.2.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path }/js/qrcode.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path }/js/buyType.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path }/js/upload.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path }/layer/laydate/laydate.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path }/layer/layer/layer.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path }/js/delete.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path }/js/place.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${path }/layer/laypage/laypage.js"></script>
	<script type="text/javascript">
			    laydate({
			      elem: '#hello', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			      event: 'focus', //响应事件。如果没有传入event，则按照默认的click
			      max:  laydate.now(), //最大日期为今天
			      festival: true, //显示节日
			    });
		  </script>
	<script type="text/javascript">
		         $(".pay_close").on("click",function(){
		        		$(".pay_footrs").attr("disabled",false);
		        	$(".pay_Alert").hide(); 
		       	 clearInterval(intervalProcess);
		         });
                 </script>
	<script type="text/javascript">
			       //调用分页
			      laypage({
			        cont: 'page', //容器。值支持id名、原生dom对象，jquery对象,
			        pages: 10, //通过后台拿到的总页数
			        skin: 'molv', //皮肤
			        first: 1, //将首页显示为数字1,。若不显示，设置false即可
			        last: 8, //将尾页显示为总页数。若不显示，设置false即可
			        prev: '<', //若不显示，设置false即可
			        next: '>', //若不显示，设置false即可
			        jump: function(obj, first){ //触发分页后的回调
			            if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
			              demo(obj.curr);
			            }
			          }    
			        });
		     </script>
	<script type="text/javascript">
	    var base="${ctx}";
		var userName = $("#username").val(), siteId = $("#siteId").val();
		//跳转消息列表
		$(function() {
			//消息数量显示
			selMessage();
			var totalFlow;//购买总流量
			var usedFlow;//剩余流量
			var zDayHour;//购买的总小时
			var unusedTime;//剩余时间
			$.ajax({
				url : base + "/personal/getResidualTime",
				data : {
					userName : userName,
					siteId : siteId
				},
				dataType : "json",
				success : function(data) {
					$(".dayTime").text(data.dayTime);//显示总天数
					$(".flow").text(data.flow);//显示总流量
					totalFlow = data.totalFlow;//总流量
					usedFlow = data.syflow;//剩余流量
					zDayHour = data.zDayHour;//总时间
					unusedTime = data.unusedTime;//剩余时间
					var resdiueTime = unusedTime == 0 ? 0: (Math.floor((unusedTime / zDayHour) * 100)) / 100;//时间比例
					var resdiueFlow = usedFlow == 0 ? 0:(Math.floor((parseInt(usedFlow)/ parseInt(totalFlow))* 100)) /100;//流量比例
					$("#showpcent-a").css("width",
							resdiueFlow * 100 + "%");
					$("#showpcent").text(
							resdiueFlow * 100 + "%");
					$("#hidepcent-a").css("width",
							resdiueTime * 100 + "%");
					$("#hidepcent").text(
							resdiueTime * 100 + "%");
				}
			});
		});
		//消息
		function selMessage() {
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
						$('.num_tips').text(data.mcount);
					} else if (data.result == "0") {
						$('.num_tips').text(0);
					}
				}
			})
		}
		function message() {
			//消息列表
			$.ajax({
				url : base+ "/message/messageNotificationPc",
				data : {
					userName : userName,
					siteId : siteId
				},
				dataType : "json",
				success : function(data) {
					if(data.listMessage != undefined ){
					var htmls = '';
					for (var i = 0; i < data.listMessage.length; i++) {
						htmls += '<div class="newsRow remove">'
								+ '<div class="newsRow_head clearfix">'
								+ '<div class="left_newHead">系统消息</div>'
								+ '<div class="right_newHead delete" onclick="deletes();">'
								+ '<img src="${path}/img/new.jpg" />'
								+ '</div>'
								+ '</div>'
								+ '<div class="newsRow_conter">'
								+ '<p>'
								+ data.listMessage[i].createTimes
								+ '</p>'
								+ '<p style="display:none" id="deleteP">'
								+ data.listMessage[i].id
								+ '</p>'
								+ '<div>'
								+ data.listMessage[i].content
								+ '</div>'
								+ '</div>'
								+ '</div>';
					}
						$('#news').html(htmls);
						$('.news_tips').hide();
					}else{
						$('#news').hide();
						$('.news_tips').show();
					}
				}
						
			})
		}
		function Interinta() {
			var dumplist = [ "http://www.baidu.com" ];
			var n = Math.floor(Math.random() * dumplist.length + 1) - 1;
			window.location.href = dumplist[n];
		}
		function deletes() {
			var $thisParents = $(this).parents(".remove");
			layer.msg('你确定要删除吗？', {
				time : 0 //不自动关闭
				,
				btn : [ '确定', '取消' ],
				yes : function(index) {
					var id = $('#deleteP').text();
					ShowResult(id);
					layer.msg("删除成功！");
					$thisParents.remove();
				}
			});
		}
		//删除通知消息
		function ShowResult(id) {
			$.ajax({
				url : base + "/message/deleteMessage",
				data : {
					id : id
				},
				dataType : "json",
				success : function(data) {
					if (data.result != "1") {
						window.location.reload();
					} else {
						window.location.reload();
					}
				}
			})
		}
		//根句当前手机号查询当前用户个人信息
		function personal() {
			$(".rechargeRecord").hide();
			$.ajax({
				type : "post",
				url : base + "/personal/fillePortalUser",
				data : {
					userName : userName
				},
				success : function(data) {
					  $('#input_user').val(data.user.userNickname);
					if(data.user.birthdate !=  null && data.user.birthdate != ""){
						$('#hello').val(data.user.birthdate);
						$('#hello').attr("disabled",true);
					}else{
						$('#hello').attr("disabled",false);
					}
					if (data.user.sex == "0") {
						$('#sex2').attr('checked', true);
						$('#sex1,#sex2').attr('disabled', true);
					} else if (data.user.sex == "1") {
						$('#sex1').attr('checked', true);
						$('#sex2,#sex1').attr('disabled', true);
					}else if(data.user.sex == "-1"){
					}
				}
			})
		}
		//个人资料 编辑完成保存
		function addpersonal() {
			var nick = $("#input_user").val();
			var birthdate = $("#hello").val();
			var sex = $(".row_centerRight input[name='sex']:checked").val() == "女" ? "0" : $(".row_centerRight input[name='sex']:checked").val() == "男" ? "1" : "-1";
			var userName = $(".phoneUser").text();
			var siteId = $("#siteId").val();
			$.ajax({
				type : "post",
				url : base + "/personal/perfectInformation",
				data : {
					sex : sex, //性别
					birthdate : birthdate, //生日
					user_nickname : nick, //昵称
					userName : userName, //用户电话
					siteId : siteId //场所Id
				},
				success : function(data) {
					$('.savefoot').show();
					$('.loading').hide();
					if (data.result == true) {
						//送流量完成，直接跳入个人中心，刷新个人中心	  		 
						//jumpPerson(siteId,userName);
						window.location.reload();
					} else {
						$('.fillnameInfo').fadeIn();
						console.log("送时长失败");
					}
				},
			});
		}
		//个人资料编辑后，跳入person界面
		function jumpPerson(sId, ume) {
			window.location.href = base
					+ "/personal/editAfter?userName=" + ume
					+ "&siteId=" + sId;
		}
		function imgSave() { //保存按钮
			// 调用函数处理图片 
			var imgUrl = $('#photo').attr('src');
			if (imgUrl.indexOf("http://oss.kdfwifi.net/user_pic/") >= 0) {
				upImg(imgUrl);
			} else {
				upImg(imgUrl);
			}
		}
		//上传保存头像
		function upImg(url) {
			var userId = $('#userId').val();
			var userName = $('#username').val();
			$.ajax({
				type : "post",
				url : base + "/personal/updateImg",
				data : {
					userId : userId, //用户ID
					img : url, //用户头像
					userName : userName
				//用户
				},
				success : function(data) {
					if (data.result == true) {
						//送流量完成，直接跳入个人中心，刷新个人中心	  		 
						window.location.reload();
					} else {
						$('.fillnameInfo').fadeIn();
						console.log("送时长失败");
					}
				}
			})
		}
		function xiaxian(){
			var syas =  $("#sysType").val();
		//点击下线
			layer.confirm('您确定下线吗?', {
				btn : [ '确定', '取消' ]
			//按钮
			}, function() {
				$.ajax({
					url : base + "/personal/offLine",
					data : {
						sys : syas,
						allIp : $("#allIp").val()
					},
					dataType : "json",
					success : function(data) {
						if (data.result == "0") {
							$('.fillname').fadeOut();
							layer.msg('下线失败', {
								icon : 1
							});
						} else {
							$('.fillname').fadeOut();
							layer.msg('下线成功', {
								icon : 1
							});
							window.location.href = data.purl.offlineUrl;
						}
					}
				})
			})
		}
		function payCharge() {
			$(".pay").show();
			$(".personal_body").hide();
			/*去充值*/
			window.location.href = base+ "/priceConfig/toType?siteId=" + $("#siteId").val() + "&userId=" + $('#userId').val();
		}
	</script>
</body>
</html>
