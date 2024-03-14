package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.TmBridgeBaseinfo;
import com.jsti.tunnel.monitor.dao.master.TmBridgeBaseinfoDao;
import com.jsti.tunnel.monitor.service.BaseService;
import com.jsti.tunnel.monitor.service.TmBridgeBaseinfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TmBridgeBaseinfoServiceImpl extends BaseService implements TmBridgeBaseinfoService {

    @Autowired
    TmBridgeBaseinfoDao tmBridgeBaseinfoDao;

    @Override
    public List<TmBridgeBaseinfo> selectList(TmBridgeBaseinfo tmBridgeBaseinfo) {
        return tmBridgeBaseinfoDao.selectList(tmBridgeBaseinfo);
    }

    @Override
    public TmBridgeBaseinfo selectByUuid(String uuid) {
        return tmBridgeBaseinfoDao.selectByUuid(uuid);
    }

    @Override
    public void save(TmBridgeBaseinfo tmBridgeBaseinfo) {
        if (StringUtils.isEmpty(tmBridgeBaseinfo.getUuid())) {
            String uuid = UUID.randomUUID().toString();
            tmBridgeBaseinfo.setUuid(uuid);
            tmBridgeBaseinfoDao.insert(tmBridgeBaseinfo);
        }
        tmBridgeBaseinfoDao.update(tmBridgeBaseinfo);
    }

    @Override
    public void delete(String uuid) {
        tmBridgeBaseinfoDao.delete(uuid);
    }
}
