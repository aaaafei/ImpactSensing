package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.pojo.MonitorItemPojo;
import com.jsti.tunnel.monitor.service.TmDeviceService;
import com.jsti.tunnel.monitor.service.WarningCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarningCalculateServiceImpl implements WarningCalculateService {
    @Autowired
    TmDeviceService tmDeviceService;
    @Override
    public List<TmDevice> getRealtimeMonitorData() {
        List<TmDevice> list = tmDeviceService.selectList(null);
        return list;
    }

    @Override
    public void updateWarningLevel4MonitorItem1(List<TmDevice> list) {

    }

    @Override
    public void updateWarningLevel4MonitorItem2(List<TmDevice> list, MonitorItemPojo monitorItemPojo) {

    }
}
