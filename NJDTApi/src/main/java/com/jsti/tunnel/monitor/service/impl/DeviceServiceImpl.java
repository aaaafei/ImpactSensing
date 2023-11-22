package com.jsti.tunnel.monitor.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsti.tunnel.monitor.bean.Device;
import com.jsti.tunnel.monitor.dao.master.DeviceDao;
import com.jsti.tunnel.monitor.service.DeviceService;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    DeviceDao deviceDao;

    @Override
    public void add(Device device) {
    	deviceDao.add(device);
    }

    @Override
    public void delete(List<String> ids) {
    	//删除数据库记录
    	deviceDao.delete(ids);
    }

    @Override
    public void update(Device device) {
    	deviceDao.update(device);
    }

    @Override
    public List<Device> getPageData(Map<String, Object> params) {
        return null;
    }

    @Override
    public long getDataNum(Map<String, Object> params) {
        return 0;
    }

    @Override
    public Device getObjByID(String uuid){
        return deviceDao.getObjByID(uuid);
    }
}
