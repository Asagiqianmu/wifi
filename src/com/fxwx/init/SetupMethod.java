package com.fxwx.init;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import com.fxwx.util.listener.FtpFileSchedule;

/**
 * 启动、定制控制类
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2017年10月24日 下午1:54:06
 */
public class SetupMethod implements Setup {

	public void destroy(NutConfig arg0) {
//		Ioc ioc = arg0.getIoc();
        //停止Ftp定时器 
//		FtpFileSchedule mailSchedule=ioc.get(FtpFileSchedule.class);
//        mailSchedule.shutdownSchedule();
	}

	public void init(NutConfig arg0) {
//		Ioc ioc = arg0.getIoc();
        
        //启动Ftp定时器
//		FtpFileSchedule mailSchedule=ioc.get(FtpFileSchedule.class);
//        mailSchedule.startSchedule();
	}
}
