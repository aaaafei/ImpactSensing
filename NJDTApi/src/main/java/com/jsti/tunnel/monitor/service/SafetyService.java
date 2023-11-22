package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.Safety;

import java.util.List;
import java.util.Map;

public interface SafetyService {
    void addRealTimeData(Safety safety);

    void deleteAllRealTimeData();

    void updateRealTimeData(Safety safety);

    List<Safety> getRealTimeData(Map<String, Object> param);

    Safety getRealTimeObjByID(long id);

    long getDataNum4RealTime(Map<String, Object> param);

    void addHistoryData(Safety safety);

    List<Safety> getHistoryData(Map<String, Object> param);

    Safety getHistoryObjByID(long id);

    long getDataNum4History(Map<String, Object> param);
}
