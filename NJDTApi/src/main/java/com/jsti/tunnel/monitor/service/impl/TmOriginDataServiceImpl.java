package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.TmOriginData;
import com.jsti.tunnel.monitor.dao.cluster.TmOriginDataDao;
import com.jsti.tunnel.monitor.pojo.TmOriginDataParams;
import com.jsti.tunnel.monitor.service.TmOriginDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TmOriginDataServiceImpl implements TmOriginDataService {

    @Autowired
    TmOriginDataDao tmOriginDataDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return tmOriginDataDao.deleteByPrimaryKey(id);
    }

    @Override
    public TmOriginData selectByPrimaryKey(Long id) {
        return tmOriginDataDao.selectByPrimaryKey(id);
    }

    @Override
    public List<TmOriginData> selectList(TmOriginDataParams tmOriginDataPojo) {
        return tmOriginDataDao.selectList(tmOriginDataPojo);
    }

}
