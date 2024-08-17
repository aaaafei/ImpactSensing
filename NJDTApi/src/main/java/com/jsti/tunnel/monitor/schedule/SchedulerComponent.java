package com.jsti.tunnel.monitor.schedule;
 

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务组件
 */
@Component
@EnableScheduling
public class SchedulerComponent {
 
    // 每分执行一次
    @Scheduled(cron = "0 * * * * ? ")
    public void scheduleCheckDeviceStatus() {
    	CheckDeviceHandler checkDeviceHandler = new CheckDeviceHandler();
    	checkDeviceHandler.checkDeviceStatus();
    }
}
