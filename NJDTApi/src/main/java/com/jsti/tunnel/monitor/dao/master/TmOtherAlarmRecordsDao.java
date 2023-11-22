package com.jsti.tunnel.monitor.dao.master;

import com.jsti.tunnel.monitor.bean.TmOtherAlarmRecords;

import java.util.List;
import java.util.Map;

public interface TmOtherAlarmRecordsDao {
    void deleteByPrimaryKey(Long id);
    void insert(TmOtherAlarmRecords record);
    TmOtherAlarmRecords selectByPrimaryKey(Long id);
    void updateByPrimaryKey(TmOtherAlarmRecords record);
    List<TmOtherAlarmRecords> selectDataList(Map<String,Object> params);
}