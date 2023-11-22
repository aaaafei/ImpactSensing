package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.TmOtherAlarmRecords;
import com.jsti.tunnel.monitor.dao.master.TmOtherAlarmRecordsDao;
import com.jsti.tunnel.monitor.service.TmOtherAlarmRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TmOtherAlarmRecordsServiceImpl implements TmOtherAlarmRecordsService {

    @Autowired
    TmOtherAlarmRecordsDao tmOtherAlarmRecordsDao;

    @Override
    public void deleteByPrimaryKey(Long id) {
        tmOtherAlarmRecordsDao.deleteByPrimaryKey(id);
    }

    @Override
    public void insert(TmOtherAlarmRecords record) {
        tmOtherAlarmRecordsDao.insert(record);
    }

    @Override
    public TmOtherAlarmRecords selectByPrimaryKey(Long id) {
        return tmOtherAlarmRecordsDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKey(TmOtherAlarmRecords record) {
        tmOtherAlarmRecordsDao.updateByPrimaryKey(record);
    }

    @Override
    public List<TmOtherAlarmRecords> selectDataList(Map<String, Object> params) {
        return tmOtherAlarmRecordsDao.selectDataList(params);
    }
}
