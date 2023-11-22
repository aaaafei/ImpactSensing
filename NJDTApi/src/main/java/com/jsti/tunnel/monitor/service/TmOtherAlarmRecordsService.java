package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.TmOtherAlarmRecords;

import java.util.List;
import java.util.Map;

public interface TmOtherAlarmRecordsService {
    void deleteByPrimaryKey(Long id);
    void insert(TmOtherAlarmRecords record);
    TmOtherAlarmRecords selectByPrimaryKey(Long id);
    void updateByPrimaryKey(TmOtherAlarmRecords record);
    List<TmOtherAlarmRecords> selectDataList(Map<String,Object> params);
}