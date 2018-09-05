//将当前显示的BANNER图片的索引值设置为0，也就是默认是第一个图片的序号
var t = n = 0, count;
$(document).ready(function(){
    //定义获取四张BANNER图
    count=$("#banner_list img").length; 
    //设置默认显示第一张BANNER图，其他的隐藏
    $("#banner_list img:not(:first-child)").hide();
    //设置默认标题为第一张图标的alt信息
    //$("#banner_info").html($("#banner_list a:first-child").find("img").attr('alt'));
    //点击默认的标题，链接到第一张BANNER图的a链接地址
    // $("#banner_info").click(function(){
    //     window.open($("#banner_list a:first-child").attr('href'), "_blank")
    // });
    //这里为切换数字按钮点击事件
    $("#banner li").click(function() {
        var i = $(this).index();
        //将i的值赋值给新变量n（也就是当前显示图片的索引值序号）
        n = i;
        //如果被点击的数字按钮的值大于等于BANNER的总数则退出当前语句
        if (i >= count) return;
        //设置标题为当前点击按钮对应的BANNER图片的alt信息
       // $("#banner_info").html($("#banner_list a").eq(i).find("img").attr('alt'));
        //删除标题绑定的事件并重新设置标题点击后 打开点击按钮对应的BANNER图片的a链接
        //$("#banner_info").unbind().click(function(){window.open($("#banner_list a").eq(i).attr('href'), "_blank")})
        //将所有的BANNER图淡入隐藏 当前点击按钮对应的BANNER图片淡入显示
        $("#banner_list img").fadeOut(200).eq(i).fadeIn(1000);
        //$("#banner_img a").hide().stop(true,true).eq(icon-1).fadeIn("slow");
        //将容器#banner的背景设置为空
        //document.getElementById("banner").style.background="";
        //将当前点击按钮的样式设置为高亮样式on
        $(this).toggleClass("on");
        //清除其它点击按钮的高亮样式
        $(this).siblings().removeAttr("class");
    });
     
    //设置默认的定时器  每4s执行一次showauto函数
    t = setInterval("showAuto()", 4000);
    //容器#banner鼠标移入后 清清除定时器 （也就是鼠标移动到BANNER上面停止切换），移开则继续切换
    //$("#banner").hover(function(){clearInterval(t)}, function(){t = setInterval("showAuto()", 2000);});
})
 
//定义自动切换的函数
function showAuto()
{
    //如果当前切换的图片索引值大于所有图片的数量 
    //也就是当切换到最后一张图片再继续切换的时候 则将当前图片索引设置为0（第一张）
    //否则则将当前图片的索引值+1
    n = n >=(count - 1) ? 0 : ++n;
    //重新触发当前BANNER的click事件
    $("#banner li").eq(n).trigger('click');
}