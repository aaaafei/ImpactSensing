package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.dao.master.TmDeviceDao;
import com.jsti.tunnel.monitor.pojo.DeviceStatus;
import com.jsti.tunnel.monitor.pojo.MonitorItemPojo;
import com.jsti.tunnel.monitor.pojo.TmDeviceParams;
import com.jsti.tunnel.monitor.pojo.TmDeviceStatusParams;
import com.jsti.tunnel.monitor.service.TmDeviceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TmDeviceServiceImpl implements TmDeviceService {

    @Autowired
    TmDeviceDao tmDeviceDao;

    @Override
    public List<TmDevice> selectList(TmDeviceParams tmDeviceParams) {
        return tmDeviceDao.selectList(tmDeviceParams);
    }

    @Override
    public TmDevice selectByPrimaryKey(Long id) {
        return tmDeviceDao.selectByPrimaryKey(id);
    }

    @Override
    public TmDevice selectByDeviceCode(String deviceCode) {
        TmDeviceParams tmDeviceParams = new TmDeviceParams();
        tmDeviceParams.setCode(deviceCode);
        List<TmDevice> list = tmDeviceDao.selectList(tmDeviceParams);
        if (list==null) return new TmDevice();
        return list.get(0);
    }

    @Override
    public void insert(TmDevice tmDevice) {

    }

    @Override
    public void updateByPrimaryKey(TmDevice tmDevice) {

    }

    @Override
    public void deleteByPrimaryKey(Long id) {

    }


}
