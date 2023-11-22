package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.pojo.TreePojo;
import com.jsti.tunnel.monitor.service.MonitorItemService;
import com.jsti.tunnel.monitor.service.TmDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*; 
import java.util.stream.Collectors;

@Service
public class MonitorItemServiceImpl implements MonitorItemService{

    @Autowired
    TmDeviceService tmDeviceService;
    @Override
    public TreePojo getTreeDataByMonitorItem() {

        return null;
    }

    @Override
    public TreePojo getTreeDataBySegment() {

        return null;
    }
}
