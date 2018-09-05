//上传ACL类型模块
 var i = 0,
			idnum = '';
$("#blacklistAdd").on("click",function  () {
	          i++;
		     idnum = 'baseprice' + i;
var str='<li class="clearfix add_row">'+
'						<div style="width: 8%;">'+
'							<input type="text" name="blacklistTextNumber'+i+'" class="tab1 inu_null blacklistTextNumber"/>'+
'						</div>'+
'						<div style="width: 10%;">'+
'							<input type="text" name="blacklistDescribe'+i+'"  class="tab2 blacklistDescribe"/>'+
'						</div>'+
'						<div style="width: 10%;">'+
'							<input type="text" name="blacklistIp'+i+'" class="tab3 blacklistIp"/>'+
'						</div>'+
'						<div style="width: 10%;">'+
'							<input type="text" name="blacklistIime'+i+'" class="tab4 blacklistIime"/>'+
'						</div>'+
'						<div style="width:34%;">'+
'							<label>'+
'								sun <input type="checkbox" name="checkbox_sun" class="checkbox_sun" />'+
'							</label>'+
'							<label>'+
'								mon <input type="checkbox" name="checkbox_mon" class="checkbox_mon" />'+
'							</label>'+
'							<label>'+
'								tue <input type="checkbox" name="checkbox_tue" class="checkbox_tue"  />'+
'							</label>'+
'							<label>'+
'								wed <input type="checkbox" name="checkbox_wed" class="checkbox_wed" />'+
'							</label>'+
'							<label>'+
'								thu <input type="checkbox" name="checkbox_thu" class="checkbox_thu"/>'+
'							</label>'+
'							<label>'+
'								fri <input type="checkbox" name="checkbox_fri" class="checkbox_fri" />'+
'							</label>'+
'							<label>'+
'								sha <input type="checkbox" name="checkbox_sha" class="checkbox_sha"/>'+
'							</label>'+
'						</div>'+
'						<div class="ServiceOperate"  style="width: 16%;">'+
'							<div class="tabBtn_a">'+
'							   <input type="button" name="affirm" class="affirm" value="确认" />'+
'							   <input type="button" name="cancel" class="cancel" value="取消" />	'+
'							</div>'+
'							<div class="tabBtn_b" style="display: none;">'+
'								<input type="button" name="editor" class="editor" value="编辑" />'+
'							    <input type="button" name="delete" class="delete" value="删除" />'+
'							</div>'+
'							'+
'						</div>'+
'						<div  style="width: 5%;">'+
'							<input type="checkbox" name="row_checked" class="row_checked" style="margin-left: 11px;" />'+
'						</div>'+
'					</li>';

 $(".blacklistText").append(str);	
 
  //每一行删除addRow
	$(".delete").on("click",function  () {
		 $(this).parents(".add_row").fadeOut("show");
		 layer.msg("删除成功");
	});
	//规定的input不能输入
	$(".inu_null").blur(function  () {
	   var reg=/[0-9]/g;
     var text=$(this).val();
		if (reg.test(text) && text !="" ) {
            console.log(text)
		} else{
			$(this).val(" ")
			 layer.msg("不能为空或只能输入数字");
			 return false;
		}
	});
	
	
   $(".affirm").click(function  () {
      var valRow=$(this).parents(".add_row").find('input[type="text"]');
      var selRow=$(this).parents(".add_row").find('select');
       var tab1=$(this).parents(".add_row").find('.tab1').val();
       var tab2=$(this).parents(".add_row").find('.tab2').val();
       var tab3=$(this).parents(".add_row").find('.tab3').val();
       var tab4=$(this).parents(".add_row").find('.tab4').val();
       
       if (tab1 !="" && tab2 !="" && tab3 !="" && tab4 !="" ) {
        valRow.attr("disabled",true);
        selRow.attr("disabled",true).css("background-color","#dedede");
       $(this).parents(".add_row").find('.tabBtn_b').show();
        $(this).parents(".add_row").find('.tabBtn_a').hide();
       } else{
	      layer.msg("每一项不能为空");
	      return false;
       }
 
	});
	$(".editor_b").click(function  () {
     var valRow=$(this).parents(".add_row").find('input[type="text"]');
     var selRow=$(this).parents(".add_row").find('select');
         valRow.attr("disabled",false);
         selRow.attr("disabled",false).css("background-color","");
         $(this).parents(".add_row").find('.tabBtn_a').show();
        $(this).parents(".add_row").find('.tabBtn_b').hide();
	});
	
 
 
});

//点击全选或全不选
$("#all_checked").click(function() {
   	var xz = $(this).prop("checked");//判断全选按钮的选中状态
    var ck = $("input[name=row_checked]").prop("checked",xz);  //让class名为qx的选项的选中状态和全选按钮的选中状态一致。 
});
//全部或选取的删除
$("#blacklistDel").on("click",function  () {
        var ck = $("input[name=row_checked]").prop("checked");
        $("input[name=row_checked]").each(function(){
        	$check=$(this).prop("checked");
        	if($check){
        		$(this).parent().parent().remove();
        	}
        });
        layer.msg("删除成功");
})