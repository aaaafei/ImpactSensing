package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.TmDispositionProcess;
import com.jsti.tunnel.monitor.bean.TmDispositionResult;
import com.jsti.tunnel.monitor.dao.master.TmDispositionProcessDao;
import com.jsti.tunnel.monitor.dao.master.TmDispositionResultDao;
import com.jsti.tunnel.monitor.service.BaseService;
import com.jsti.tunnel.monitor.service.TmDispositionProcessService;
import com.jsti.tunnel.monitor.service.TmDispositionResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TmDispositionResultServiceImpl extends BaseService implements TmDispositionResultService {

    @Autowired
    TmDispositionResultDao tmDispositionResultDao;


    @Override
    public List<TmDispositionResult> selectList(TmDispositionResult tmDispositionResult) {
        return tmDispositionResultDao.selectList(tmDispositionResult);
    }

    @Override
    public TmDispositionResult selectById(Long id) {
        return tmDispositionResultDao.selectById(id);
    }

    @Override
    public void save(TmDispositionResult tmDispositionResult) {
        if (tmDispositionResult.getId() == null) {
            tmDispositionResultDao.insert(tmDispositionResult);
        }else {
            tmDispositionResultDao.update(tmDispositionResult);
        }
    }

    @Override
    public void delete(Long id) {
        tmDispositionResultDao.delete(id);
    }
}
