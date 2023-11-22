package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.pojo.DeviceStatus;
import com.jsti.tunnel.monitor.pojo.MonitorItemPojo;
import com.jsti.tunnel.monitor.pojo.TmDeviceParams;
import com.jsti.tunnel.monitor.pojo.TmDeviceStatusParams;

import java.util.List;
import java.util.Map;

public interface TmDeviceService {
    List<TmDevice> selectList(TmDeviceParams tmDeviceParams);
    TmDevice selectByPrimaryKey(Long id);
    TmDevice selectByDeviceCode(String deviceCode);
    void insert(TmDevice tmDevice);
    void updateByPrimaryKey(TmDevice tmDevice);
    void deleteByPrimaryKey(Long id);
}
