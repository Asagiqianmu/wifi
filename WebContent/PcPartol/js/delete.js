// 删除操作
$(function(){
	$(".delete").click(function(){
		var $thisParents = $(this).parents(".remove");
		layer.msg('你确定要删除吗？', {
			time: 0 //不自动关闭
			,btn: ['确定', '取消']
			,yes: function(index){
		    	layer.msg("删除成功！");
		    	$thisParents.remove();
		  	}
		});
	})
});