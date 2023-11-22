package com.jsti.tunnel.monitor.controller;

import com.jsti.tunnel.monitor.pojo.TreePojo;
import com.jsti.tunnel.monitor.service.MonitorItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitorItem")
public class MonitorItemController extends BaseController{

    @Autowired
    MonitorItemService monitorItemService;

    @RequestMapping(value = "/getTreeDataByMonitorItem", produces = "application/json;charset=UTF-8")
    public String getTreeDataByMonitorItem(){
        TreePojo treePojo = monitorItemService.getTreeDataByMonitorItem();
        return returnSuccessResult(treePojo);
    }

    @RequestMapping(value = "/getTreeDataBySegment", produces = "application/json;charset=UTF-8")
    public String getTreeDataBySegment(){
        TreePojo treePojo = monitorItemService.getTreeDataBySegment();
        return returnSuccessResult(treePojo);
    }
}
