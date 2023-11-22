package com.jsti.tunnel.monitor.controller;

import com.jsti.tunnel.monitor.bean.Safety;
import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.pojo.DeviceStatus;
import com.jsti.tunnel.monitor.service.SafetyService;
import com.jsti.tunnel.monitor.service.TmDeviceService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/safety")
public class SafetyController extends BaseController{

    private final static Logger logger = LoggerFactory.getLogger(SafetyController.class);
    @Autowired
    SafetyService safetyService;
    @Autowired
    TmDeviceService tmDeviceService;

    @RequestMapping(value = "/getRealtimeData", produces = "application/json;charset=UTF-8")
    public String getRealtimeData(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        List<Safety> list = safetyService.getRealTimeData(param);
        // 更新safety_realtime  设备完好率
    	Map<String,Object> map = new HashMap<>();
    	List<TmDevice> listDevice = tmDeviceService.selectList(null);
    	for(TmDevice itemDevice : listDevice) {
    		String key = itemDevice.getSegment()+"#"+itemDevice.getLine();
    		if (!map.containsKey(key)) {
    			DeviceStatus deviceStatus = new DeviceStatus();
    			deviceStatus.setSegment(itemDevice.getSegment());
    			deviceStatus.setLine(itemDevice.getLine());
    			deviceStatus.setNum(0);
    			deviceStatus.setNormalNum(0);
    			map.put(key, deviceStatus);
    		}
    		DeviceStatus deviceStatus = (DeviceStatus) map.get(key);
    		deviceStatus.setNum(deviceStatus.getNum()+1);
    		if (!itemDevice.getStatus().equals("0")) {
    			deviceStatus.setNormalNum(deviceStatus.getNormalNum()+1);
    		}
    	}
    	for (Safety item: list) {
    		String line = item.getLine().equalsIgnoreCase("L")?"左线":"右线";
    		String key = item.getSegment_name()+"#"+line;
    		DeviceStatus deviceStatus = (DeviceStatus) map.get(key);
    		if (deviceStatus == null) continue;
//    		DecimalFormat df = new DecimalFormat("#.0"); 
//    		String normalRate = df.format(deviceStatus.getNormalNum()/(deviceStatus.getNum()*1f)*100);
    		float device_intact_rate = deviceStatus.getNormalNum()/(deviceStatus.getNum()*1f);
    		BigDecimal bdf = new BigDecimal(device_intact_rate);
    		item.setDevice_intact_rate(bdf.setScale(3,BigDecimal.ROUND_HALF_UP).floatValue());
    	}
        return super.returnSuccessResult(list);
    }

    @RequestMapping(value = "/getHistoryData", produces = "application/json;charset=UTF-8")
    public String getHistoryData(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        if (StringUtils.isNotEmpty(request.getParameter("begin_time"))) {
            param.put("begin_time", request.getParameter("begin_time"));
        }
        if (StringUtils.isNotEmpty(request.getParameter("end_time"))) {
            param.put("end_time", request.getParameter("end_time"));
        }
        if(StringUtils.isNotEmpty(request.getParameter("line"))){
            param.put("line", request.getParameter("line"));
        }
        List<Safety> list = safetyService.getHistoryData(param);
        return returnSuccessResult(list);
    }
}
