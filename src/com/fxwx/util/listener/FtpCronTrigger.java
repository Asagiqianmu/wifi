package com.fxwx.util.listener;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;

public class FtpCronTrigger {
	public CronTrigger cronTrigger = null;
	
    public FtpCronTrigger(){
        //间隔5分钟的执行一次
        String cron = "0 0/5 * * * ?";
        CronScheduleBuilder schedule = CronScheduleBuilder.cronSchedule(cron);
        cronTrigger = TriggerBuilder.newTrigger().withSchedule(schedule).build();
    }
}
