package com.jsti.tunnel.monitor.schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jsti.tunnel.monitor.bean.Safety;
import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.bean.TmOriginData;
import com.jsti.tunnel.monitor.service.SafetyService;
import com.jsti.tunnel.monitor.service.TmDeviceService;
import com.jsti.tunnel.monitor.service.TmOriginDataService;

/**
 * 生成模拟值、模拟安全度 
 *
 */
@Component
public class MockingHandler {
 
    @Autowired
    private TmDeviceService tmDeviceService;
    @Autowired
    private TmOriginDataService tmOriginDataService;
    @Autowired
    private SafetyService safetyService;
   
    private static MockingHandler mockingHandler;
    @PostConstruct
    public void init(){
    	mockingHandler = this;
    	mockingHandler.tmDeviceService = this.tmDeviceService;
    	mockingHandler.tmOriginDataService = this.tmOriginDataService;
    	mockingHandler.safetyService = this.safetyService;
    }

    public void handlerMocking(String code, Date datetime){
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("code", code);
                
    }
    
    public void handlerMockingSafety(String code, Date datetime){
    	Map<String, String> segmentMap = new HashMap<String, String>();
    	segmentMap.put("PJ", "浦口竖井");
    	segmentMap.put("PD", "浦口大堤");
    	segmentMap.put("JZ", "江中");
    	segmentMap.put("FX", "覆土厚度最小");
    	segmentMap.put("MD", "梅子洲大堤");
    	segmentMap.put("MJ", "梅子洲竖井");
    	String segmentName = segmentMap.get(code.split("/")[0]);
    	String line = code.split("/")[1];
    	int temp = 0;
    	String value = "";
    	Random r = new Random();
		temp = r.nextInt(15)+480;
		value = String.valueOf((float)temp/100);
		Safety safety = new Safety();
		safety.setSafety_label("A");
		safety.setSafety_value(Float.valueOf(value));
		safety.setDevice_intact_rate(1);
		safety.setDevice_intact_rate(1);
		safety.setSegment_name(segmentName);
		safety.setLine(line);
		safety.setCollect_time(datetime);
		mockingHandler.safetyService.addHistoryData(safety);
		
		Map<String, Object> param = new HashMap<>();
		param.put("segment_name", segmentName);
		param.put("line", line);
		Safety realtimeSafety = mockingHandler.safetyService.getRealTimeData(param).get(0);
		realtimeSafety.setSafety_value(Float.valueOf(value));
		realtimeSafety.setDevice_intact_rate(1);
		realtimeSafety.setDevice_intact_rate(1);
		realtimeSafety.setCollect_time(datetime);
		mockingHandler.safetyService.updateRealTimeData(realtimeSafety);
    }
}
