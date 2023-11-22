package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.TmWarningRecords;
import com.jsti.tunnel.monitor.dao.master.TmWarningRecordsDao;
import com.jsti.tunnel.monitor.service.TmWarningRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TmWarningRecordsServiceImpl implements TmWarningRecordsService {

    @Autowired
    TmWarningRecordsDao tmWarningRecordsDao;

    @Override
    public void deleteByPrimaryKey(Long id) {
        tmWarningRecordsDao.deleteByPrimaryKey(id);
    }

    @Override
    public void insert(TmWarningRecords record) {
        tmWarningRecordsDao.insert(record);
    }

    @Override
    public TmWarningRecords selectByPrimaryKey(Long id) {
        return tmWarningRecordsDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKey(TmWarningRecords record) {
        tmWarningRecordsDao.updateByPrimaryKey(record);
    }

    @Override
    public List<TmWarningRecords> selectDataList(Map<String,Object> params){
        return tmWarningRecordsDao.selectDataList(params);
    }

}
