package com.jsti.tunnel.monitor.service;

import java.util.List;
import java.util.Map;

import com.jsti.tunnel.monitor.bean.Device;

public interface DeviceService {
    void add(Device device);
    void delete(List<String> ids);
    void update(Device device);
    List<Device> getPageData(Map<String,Object> params);
    long getDataNum(Map<String,Object> params);
    Device getObjByID(String uuid);
}
