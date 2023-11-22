package com.jsti.tunnel.monitor.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsti.tunnel.monitor.bean.Fxbg;
import com.jsti.tunnel.monitor.dao.master.FxbgDao;
import com.jsti.tunnel.monitor.service.FxbgService;
import com.jsti.tunnel.monitor.util.FileUtil;

@Service
public class FxbgServiceImpl implements FxbgService {

    @Autowired
    FxbgDao fxbgDao;

    @Override
    public void add(Fxbg announcement) {
        fxbgDao.add(announcement);
    }

    @Override
    public void delete(List<String> uuids) {
    	//先删除对应附件
    	for(String uuid : uuids) {
    		Fxbg fxbg = this.getObjByUUID(uuid);
    		if(fxbg!=null) {
    			String full_path = fxbg.getFull_path()!=null?fxbg.getFull_path():"";
    			if(!"".equals(full_path)) {
    				FileUtil.deleteFile(full_path);
    			}
    		}
    	}
    	//删除数据库记录
        fxbgDao.delete(uuids);
    }

    @Override
    public void update(Fxbg announcement) {
        fxbgDao.update(announcement);
    }

    @Override
    public List<Fxbg> getPageData(Map<String, Object> params) {
        return fxbgDao.getPageData(params);
    }

    @Override
    public long getDataNum(Map<String, Object> params) {
        return fxbgDao.getDataNum(params);
    }

    @Override
    public Fxbg getObjByUUID(String uuid){
        return fxbgDao.getObjByUUID(uuid);
    }
}
