package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.pojo.TreePojo;

public interface MonitorItemService {

    TreePojo getTreeDataByMonitorItem();

    TreePojo getTreeDataBySegment();

}
