package com.jsti.tunnel.monitor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsti.tunnel.monitor.bean.TmCalculateValueConfig;
import com.jsti.tunnel.monitor.dao.master.TmCalculateValueConfigDao;
import com.jsti.tunnel.monitor.service.TmCalculateValueConfigService;

@Service
public class TmCalculateValueConfigServiceImpl implements TmCalculateValueConfigService {

	@Autowired
	TmCalculateValueConfigDao tmCalculateValueConfigDao;
	
	public TmCalculateValueConfig selectByPrimaryKey(Integer id) {
		return tmCalculateValueConfigDao.selectByPrimaryKey(id);
	}
	
    public int insert(TmCalculateValueConfig record) {
    	return tmCalculateValueConfigDao.insert(record);
    }

    public int insertSelective(TmCalculateValueConfig record) {
    	return tmCalculateValueConfigDao.insertSelective(record);
    }
	
    public int deleteByLeafId(Integer leaf_id) {
    	return tmCalculateValueConfigDao.deleteByLeafId(leaf_id);
    }
    
    public List<Integer> selectModelIdsByDevice1Id(List<Integer> list){
    	return tmCalculateValueConfigDao.selectModelIdsByDevice1Id(list);
    }
    
    public List<TmCalculateValueConfig> selectData(TmCalculateValueConfig record){
    	return tmCalculateValueConfigDao.selectData(record);
    }
    
    
    
	
}
