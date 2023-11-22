package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.TmWarningRecords;

import java.util.List;
import java.util.Map;

public interface TmWarningRecordsService {
    void deleteByPrimaryKey(Long id);
    void insert(TmWarningRecords record);
    TmWarningRecords selectByPrimaryKey(Long id);
    void updateByPrimaryKey(TmWarningRecords record);
    List<TmWarningRecords> selectDataList(Map<String,Object> params);
}