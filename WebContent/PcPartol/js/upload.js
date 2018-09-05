// 上传预览图片
function setImagePreview(obj, idname) {
	var objval = $(".uploadimg").get(0).files[0];
	var r = new FileReader();
	if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(obj.value)) {
		layer.msg("上传的图片格式不正确！");
		obj.value = "";
		return false;
	} else if(objval.size > 20242 * 20240) {
		layer.msg("请选择10M以下图片进行上传！");
		return false;
	} else {
		r.readAsDataURL(objval); //Base64
		var imgObjPreview = $(".previewid").get(0);
		$(r).load(function() {
			$("#" + idname).val(r.result);
			imgObjPreview.src = r.result;
		})

	}

}

//图片上传预览    IE是用了滤镜。
//function previewImage(file)
//{
//  var MAXWIDTH  = 90; 
//  var MAXHEIGHT = 90;
//  var div = document.getElementById('preview');
//  if (file.files && file.files[0])
//  {
//      div.innerHTML ='<img id=uploadimg onclick=$("#previewImg").click()>';
//      var img = document.getElementById('uploadimg');
//      img.onload = function(){
//        var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
//        img.width  =  rect.width;
//        img.height =  rect.height;
////         img.style.marginLeft = rect.left+'px';
//        img.style.marginTop = rect.top+'px';
//      }
//      var reader = new FileReader();
//      reader.onload = function(evt){img.src = evt.target.result;}
//      reader.readAsDataURL(file.files[0]);
//  }
//  else //兼容IE
//  {
//    var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
//    file.select();
//    var src = document.selection.createRange().text;
//    div.innerHTML = '<img id=uploadimg>';
//    var img = document.getElementById('uploadimg');
//    img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
//    var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
//    status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
//    div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
//  }
//}
//function clacImgZoomParam( maxWidth, maxHeight, width, height ){
//    var param = {top:0, left:0, width:width, height:height};
//    if( width>maxWidth || height>maxHeight ){
//        rateWidth = width / maxWidth;
//        rateHeight = height / maxHeight;
//        
//        if( rateWidth > rateHeight ){
//            param.width =  maxWidth;
//            param.height = Math.round(height / rateWidth);
//        }else{
//            param.width = Math.round(width / rateHeight);
//            param.height = maxHeight;
//        }
//    }
//    param.left = Math.round((maxWidth - param.width) / 2);
//    param.top = Math.round((maxHeight - param.height) / 2);
//    return param;
//}
