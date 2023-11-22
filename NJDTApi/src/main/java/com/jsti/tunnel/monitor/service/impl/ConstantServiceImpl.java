package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.Constant;
import com.jsti.tunnel.monitor.dao.master.ConstantDao;
import com.jsti.tunnel.monitor.service.BaseService;
import com.jsti.tunnel.monitor.service.ConstantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件名：UserServiceImpl
 * .
 */
@Service
public class ConstantServiceImpl extends BaseService implements ConstantService {
    @Autowired
    private ConstantDao constantDao;


	public List<Constant> getAll() {
		// TODO Auto-generated method stub
		return constantDao.getAll();
	}


	public Constant getById(Long id) {
		// TODO Auto-generated method stub
		return constantDao.getOne(id);
	}


	public List<Constant> getByFlag(String flag) {
		// TODO Auto-generated method stub
		return constantDao.getByFlag(flag);
	}


	public Constant getByFlagAndValue(Constant constant) {
		// TODO Auto-generated method stub
		return constantDao.getByFlagAndValue(constant);
	}


	public void insert(Constant constant) {
		// TODO Auto-generated method stub
		constantDao.insert(constant);
	}


	public void update(Constant constant) {
		// TODO Auto-generated method stub
		constantDao.update(constant);
	}


	public void delete(Long id) {
		// TODO Auto-generated method stub
		constantDao.delete(id);
	}


	@Override
	public List<Constant> getByName(String flagName) {
		// TODO Auto-generated method stub
		return constantDao.getByName(flagName);
	}
    
}
