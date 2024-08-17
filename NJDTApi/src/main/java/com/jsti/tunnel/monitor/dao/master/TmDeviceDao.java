package com.jsti.tunnel.monitor.dao.master;

import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.pojo.DeviceStatus;
import com.jsti.tunnel.monitor.pojo.MonitorItemPojo;
import com.jsti.tunnel.monitor.pojo.TmDeviceParams;
import com.jsti.tunnel.monitor.pojo.TmDeviceStatusParams;

import java.util.List;
import java.util.Map;

public interface TmDeviceDao {
    List<TmDevice> selectList(TmDeviceParams tmDeviceParams);
    List<Integer> selectIdsByCodes(List<String> codes);
    TmDevice selectByPrimaryKey(Long id);
    void deleteByPrimaryKey(Long id);
    void insert(TmDevice record);
    void updateByPrimaryKey(TmDevice record);
    void updateByCode(TmDevice record);
}