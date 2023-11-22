package com.jsti.tunnel.monitor.service.impl;

import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsti.tunnel.monitor.bean.TmCalculateModel;
import com.jsti.tunnel.monitor.dao.master.TmCalculateModelDao;
import com.jsti.tunnel.monitor.service.TmCalculateModelService;

@Service
public class TmCalculateModelServiceImpl implements TmCalculateModelService{

	@Autowired
	TmCalculateModelDao tmCalculateModelDao;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return tmCalculateModelDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(TmCalculateModel record) { 
		tmCalculateModelDao.insert(record);
		return record.getId();
	}

	@Override
	public int insertSelective(TmCalculateModel record) {
		tmCalculateModelDao.insertSelective(record);
		return record.getId();
	}

	@Override
	public TmCalculateModel selectByPrimaryKey(Integer id) { 
		return tmCalculateModelDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(TmCalculateModel record) { 
		return tmCalculateModelDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TmCalculateModel record) { 
		return tmCalculateModelDao.updateByPrimaryKey(record);
	}

	@Override
	public List<TmCalculateModel> selectDataList(TmCalculateModel tmCalculateModel) { 
		return tmCalculateModelDao.selectDataList(tmCalculateModel);
	}
	
	@Override
	public	List<TmCalculateModel> selectRootNodeDataList(TmCalculateModel tmCalculateModel){
		return tmCalculateModelDao.selectRootNodeDataList(tmCalculateModel);
	}

}
