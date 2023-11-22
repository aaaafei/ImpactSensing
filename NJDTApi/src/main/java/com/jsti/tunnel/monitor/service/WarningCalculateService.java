package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.pojo.MonitorItemPojo;

import java.util.List;

public interface WarningCalculateService {

    List<TmDevice> getRealtimeMonitorData();

    // 不均匀沉降预警级别计算
    void updateWarningLevel4MonitorItem1(List<TmDevice> list);
    // 其他类型的预警级别计算
    void updateWarningLevel4MonitorItem2(List<TmDevice> list, MonitorItemPojo monitorItemPojo);
}
