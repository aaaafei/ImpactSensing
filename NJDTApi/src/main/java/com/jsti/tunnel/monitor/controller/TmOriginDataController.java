package com.jsti.tunnel.monitor.controller;
 
import com.github.pagehelper.PageHelper;
import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.bean.TmOriginData;
import com.jsti.tunnel.monitor.bean.TmSubside;
import com.jsti.tunnel.monitor.pojo.TmOriginDataParams; 
import com.jsti.tunnel.monitor.service.TmDeviceService;
import com.jsti.tunnel.monitor.service.TmOriginDataService;
import com.jsti.tunnel.monitor.util.DeviceDataUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/tmOriginData")
public class TmOriginDataController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(TmOriginDataController.class);
	
    @Autowired
    TmOriginDataService tmOriginDataService;
    @Autowired
    TmDeviceService tmDeviceService; 

    @PostMapping("/getPageList/{curPage}/{pageSize}")
    public String getPageList(@RequestBody TmOriginDataParams tmOriginDataPojo, @PathVariable Integer curPage, @PathVariable Integer pageSize){
        PageHelper.startPage(curPage,pageSize);
        PageHelper.orderBy("ic desc");
        List<TmOriginData> list = tmOriginDataService.selectList(tmOriginDataPojo);
        return returnSuccessPageResult(list,"yyyy-MM-dd HH:mm");
    }

    @PostMapping("/getPageListCatalogH0/{curPage}/{pageSize}")
    public String getPageListCatalogH0(@RequestBody TmOriginDataParams tmOriginDataPojo, @PathVariable Integer curPage, @PathVariable Integer pageSize){
        PageHelper.startPage(curPage,pageSize);
        PageHelper.orderBy("ic desc");
        tmOriginDataPojo.setCatalog("H0");
        List<TmOriginData> list = tmOriginDataService.selectList(tmOriginDataPojo);
        return returnSuccessPageResult(list,"yyyy-MM-dd HH:mm");
    }




 
}
