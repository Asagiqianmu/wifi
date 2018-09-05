package com.fxwx.util.listener;

import org.nutz.ioc.loader.annotation.IocBean;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 这个监听器用于服务启动的时候定时将认证日志上报给任子行FTP服务器
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2017年10月24日 下午1:02:54
 */
@IocBean
public class FtpFileSchedule {
	SchedulerFactory sf = null;
    Scheduler sched = null;
    JobDetail job = null;
	/**
     * 启动定时器
     */
    public void startSchedule(){
        sf = new StdSchedulerFactory();
        try {
            sched=sf.getScheduler();
            job=JobBuilder.newJob(FtpZipFileJob.class).build();
            CronTrigger cTrigger=new FtpCronTrigger().cronTrigger;
            sched.scheduleJob(job, cTrigger);
            sched.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    /**
     * 停止定时器
     */
    public void shutdownSchedule() {
        if (null != sched) {
            try {
                sched.shutdown();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }
}
