package com.jsti.tunnel.monitor.schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.service.TmDeviceService;

@Component
public class CheckDeviceHandler {
 

    @Autowired
    private TmDeviceService tmDeviceService;
    
    private static CheckDeviceHandler checkDeviceHandler;
    @PostConstruct
    public void init(){
    	checkDeviceHandler = this;
    	checkDeviceHandler.tmDeviceService = this.tmDeviceService;
    }
    
    public void checkDeviceStatus(){
    	Date now = new Date();
    	Map<String, Object> params = new HashMap<String, Object>();
    	List<TmDevice> list = checkDeviceHandler.tmDeviceService.selectList(null);
    	for(TmDevice itemDevice : list) {

    		itemDevice.setStatus("0");
			checkDeviceHandler.tmDeviceService.updateByPrimaryKey(itemDevice);
    	}
    	
    }
}
