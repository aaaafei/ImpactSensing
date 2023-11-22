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
 
    @Value("${mockingData.enable}")
    private String mockingData;
    
    @Value("${mockingData.stationid}")
    private String stationid;
    
    @Value("${mockingSafety.enable}")
    private String mockingSafety;

    
    /**
     * 生成模拟值、模拟安全度 
     * 每30分钟执行一次
     */ 
    @Scheduled(cron = "0 0/30 * * * ? ")
    public void scheduleMockingData() { 
        Date datetime = new Date();
        java.util.List<String> stationidList = Arrays.asList(stationid.split(","));
        Map<String, String> stationMap = new HashMap<String, String>();
        stationMap.put("1","PJ/R");
        stationMap.put("2","PD/R");
        stationMap.put("3","JZ/R");
        stationMap.put("4","FX/R");
        stationMap.put("5","MD/R");
        stationMap.put("6","MJ/R");
        stationMap.put("7","PJ/L");
        stationMap.put("8","PD/L");
        stationMap.put("9","JZ/L");
        stationMap.put("10","FX/L");
        stationMap.put("11","MD/L");
        stationMap.put("12","MJ/L");
        MockingHandler mockingHandler = new MockingHandler();
        for (String id : stationidList) {
        	if(mockingData.equals("1")){
        		mockingHandler.handlerMocking(stationMap.get(id), datetime);
        	}
        	if(mockingSafety.equals("1")){
        		mockingHandler.handlerMockingSafety(stationMap.get(id), datetime);
        	}
        	
        }
    }
    
    // 每小时的05,35分执行一次
    @Scheduled(cron = "0 5,35 * * * ? ")
    public void scheduleCheckDeviceStatus() {
    	CheckDeviceHandler checkDeviceHandler = new CheckDeviceHandler();
    	checkDeviceHandler.checkDeviceStatus();
    }
}
