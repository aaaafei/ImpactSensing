package com.jsti.tunnel.monitor.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsti.tunnel.monitor.bean.TmHealthHistory;
import com.jsti.tunnel.monitor.dao.cluster.TmHealthHistoryDao;
import com.jsti.tunnel.monitor.service.TmHealthHistoryService;

@Service
public class TmHealthHistoryServiceImpl implements TmHealthHistoryService{

	@Autowired
	TmHealthHistoryDao tmHealthHistoryDao;  
	
	@Override
	public List<TmHealthHistory> getData(Map<String, Object> params) { 
		return tmHealthHistoryDao.getData(params);
	}

	@Override
	public void add(TmHealthHistory tmHealthHistory) {
		tmHealthHistoryDao.add(tmHealthHistory);
	}

	@Override
	public TmHealthHistory getLastOne(Map<String, Object> params) {
		return tmHealthHistoryDao.getLastOne(params);
	}
	
}
