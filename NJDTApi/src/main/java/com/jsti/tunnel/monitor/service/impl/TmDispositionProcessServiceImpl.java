package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.TmBridgeBaseinfo;
import com.jsti.tunnel.monitor.bean.TmDispositionProcess;
import com.jsti.tunnel.monitor.dao.master.TmBridgeBaseinfoDao;
import com.jsti.tunnel.monitor.dao.master.TmDispositionProcessDao;
import com.jsti.tunnel.monitor.service.BaseService;
import com.jsti.tunnel.monitor.service.TmBridgeBaseinfoService;
import com.jsti.tunnel.monitor.service.TmDispositionProcessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TmDispositionProcessServiceImpl extends BaseService implements TmDispositionProcessService {

    @Autowired
    TmDispositionProcessDao tmDispositionProcessDao;


    @Override
    public List<TmDispositionProcess> selectList(TmDispositionProcess tmDispositionProcess) {
        return tmDispositionProcessDao.selectList(tmDispositionProcess);
    }

    @Override
    public TmDispositionProcess selectById(Long id) {
        return tmDispositionProcessDao.selectById(id);
    }

    @Override
    public void save(TmDispositionProcess tmDispositionProcess) {
        if (tmDispositionProcess.getId() == null) {
            tmDispositionProcessDao.insert(tmDispositionProcess);
        }else {
            tmDispositionProcessDao.update(tmDispositionProcess);
        }
    }

    @Override
    public void delete(Long id) {
        tmDispositionProcessDao.delete(id);
    }
}
