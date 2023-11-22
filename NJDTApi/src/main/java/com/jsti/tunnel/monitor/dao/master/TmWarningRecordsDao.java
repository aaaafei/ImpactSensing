package com.jsti.tunnel.monitor.dao.master;

import com.jsti.tunnel.monitor.bean.TmWarningRecords;

import java.util.List;
import java.util.Map;

public interface TmWarningRecordsDao {
    void deleteByPrimaryKey(Long id);
    void insert(TmWarningRecords record);
    TmWarningRecords selectByPrimaryKey(Long id);
    List<TmWarningRecords> selectDataList(Map<String,Object> params);
    void updateByPrimaryKey(TmWarningRecords record);
}