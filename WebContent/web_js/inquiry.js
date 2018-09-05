var base = "${ctx}";

function initdata(base, data) {
	var htr = "";
	$.each(data, function(n) {
		// 循环获取数据
		var ssid = data[n].ssid;
		var address = data[n].address;
		var site_name = data[n].site_name;
		var str = address.substr(0, address.length);
		var str1 = ssid.substr(0, ssid.length);
	/*	htr += '<li class="inquirylistRow clearfix">'
			 + '<div class="inquirylistRowR clearfix">'
				+ '<div class="inquirylistRowRL">' + str1 + '</div>'
				+ '<div style="margin-left: 6px;">' + '<img src="' + base
				+ '/static/images/wifi_img.png"/>' + '</div>' + '</div>'
				+ '<div class="inquirylistRowL">'
				+ '<div class="inquirylistRowLTop">' + site_name + '</div>'
				+ '<div class="inquirylistRowLBottom">' + str + '</div>'
				+ '</div>'
				+ '</li>';*/
		 htr += '<li class="inquirylistRow clearfix">'+
		'<div class="inquirylistRowL">'+
		'<div class="inquirylistRowLTop">' + site_name + '</div>'+
		'<div class="inquirylistRowLBottom">' + str + '</div>'+
		'</div>'+
		'<div class="inquirylistRowR clearfix">'+
		'<div class="inquirylistRowRL">' + str1 + '</div>'+
		'<div style="margin-left: 6px;">'+
		'<img src="' + base
		+ '/static/images/wifi_img.png"/>'+
		'</div>'+
		'</div>'+
		'</li>';
		
	});
	$("#inquirylist").html(htr);
}
function searchHotSpot(content) {
	$.ajax({
		type : "post",
		url : base + "/PROT/searchHotSpot",
		data : {
			content : content,
		},
		dataType : "json",
		success : function(data) {
			$("#inquirylist").empty();
			var sdata = eval(data);
			initdata(base, sdata);
		}
	});
}