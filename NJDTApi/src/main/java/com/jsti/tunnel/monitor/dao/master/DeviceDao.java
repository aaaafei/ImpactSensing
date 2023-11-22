package com.jsti.tunnel.monitor.dao.master;

import java.util.List;
import java.util.Map;

import com.jsti.tunnel.monitor.bean.Device;
import com.jsti.tunnel.monitor.pojo.TmDeviceParams;

public interface DeviceDao {
    void add(Device device);
    void delete(List<String> ids);
    void update(Device device);
    List<Device> getPageData(TmDeviceParams tmDeviceParams);
    Device getObjByID(String id);
}
