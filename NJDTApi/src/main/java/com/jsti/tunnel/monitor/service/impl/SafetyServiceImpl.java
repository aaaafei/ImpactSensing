package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.Safety;
import com.jsti.tunnel.monitor.dao.master.SafetyDao;
import com.jsti.tunnel.monitor.service.SafetyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SafetyServiceImpl implements SafetyService {
    @Autowired
    private SafetyDao safetyDao;

    @Override
    public void addRealTimeData(Safety safety) {
        safetyDao.addRealTimeData(safety);
    }

    @Override
    public void deleteAllRealTimeData() {
        safetyDao.deleteAllRealTimeData();
    }

    @Override
    public void updateRealTimeData(Safety safety) {
        safetyDao.updateRealTimeData(safety);
    }

    @Override
    public List<Safety> getRealTimeData(Map<String, Object> param) {
        return safetyDao.getRealTimeData(param);
    }

    @Override
    public Safety getRealTimeObjByID(long id) {
        return safetyDao.getRealTimeObjByID(id);
    }

    @Override
    public long getDataNum4RealTime(Map<String, Object> param) {
        return safetyDao.getDataNum4RealTime(param);
    }

    @Override
    public void addHistoryData(Safety safety) {
        safetyDao.addHistoryData(safety);
    }

    @Override
    public List<Safety> getHistoryData(Map<String, Object> param) {
        return safetyDao.getHistoryData(param);
    }

    @Override
    public Safety getHistoryObjByID(long id) {
        return safetyDao.getHistoryObjByID(id);
    }

    @Override
    public long getDataNum4History(Map<String, Object> param) {
        return safetyDao.getDataNum4History(param);
    }
}
