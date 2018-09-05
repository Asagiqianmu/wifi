
	 // 这里使用最原始的js语法实现，可对应换成jquery语法进行逻辑控制  
    var visible=document.getElementById('hide');//text block  
    var invisible=document.getElementById('show');//password block
    var inputVisible = document.getElementById('password2');  
    var inputInVisible = document.getElementById('password'); 
    
    var visible1=document.getElementById('hide_a');//text block  
    var invisible1=document.getElementById('show_a');//password block
    var inputVisible1 = document.getElementById('password2_a'); 
    var inputInVisible1 = document.getElementById('password_a'); 
    
    //隐藏text block，显示password block  
    function showPsw(){  
        var val = inputInVisible.value;//将password的值传给text  
        inputVisible.value = val;    
        visible.style.display = "none";   
        inputInVisible.style.display="none";
        inputVisible.style.display="block";
        invisible.style.display="block";
    }  
    //隐藏password，显示text    
    function hidePsw(){  
        var val=inputVisible.value;//将text的值传给password    
        inputInVisible.value = val;  
        visible.style.display = "block";   
        inputInVisible.style.display="block";
        inputVisible.style.display="none";
        invisible.style.display="none";   
    }
    
     //隐藏text block，显示password block  
    function showPsw1(){  
        var val = inputInVisible1.value;//将password的值传给text  
        inputVisible1.value = val;    
        visible1.style.display = "none";   
        inputInVisible1.style.display="none";
        inputVisible1.style.display="block";
        invisible1.style.display="block";
    }  
    //隐藏password，显示text    
    function hidePsw1(){  
        var val=inputVisible1.value;//将text的值传给password    
        inputInVisible1.value = val;  
        visible1.style.display = "block";   
        inputInVisible1.style.display="block";
        inputVisible1.style.display="none";
        invisible1.style.display="none";   
    }

    
    
    
    //随机生成图片验证码
function createCode() {
	code = "";
	var codeLength = 4; //验证码的长度
	var checkCode = document.getElementById("checkCode");
	var codeChars = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'); //所有候选组成验证码的字符，当然也可以用中文的
	for(var i = 0; i < codeLength; i++) {
		var charNum = Math.floor(Math.random() * 26);
		code += codeChars[charNum];
	}
	if(checkCode) {
		checkCode.innerHTML = code;
	}
}
createCode();


