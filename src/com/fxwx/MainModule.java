package com.fxwx;

import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import com.fxwx.filter.Impl.AuthFilterImpl;
import com.fxwx.init.SetupMethod;



@SetupBy(SetupMethod.class)
@Modules(scanPackage=true)
@IocBy(type=ComboIocProvider.class, args={
	"*js", 
	"ioc/",
	"*anno",
	"com.fxwx",
	"*tx" 
})
//编码格式
@Encoding(input="utf8",output="utf8")
//初始化
@Filters({@By(type=AuthFilterImpl.class)})
public class MainModule {
}

