package com.jsti.tunnel.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.jsti.tunnel.monitor.dao.master.CommonDao;
import com.jsti.tunnel.monitor.dao.master.ConstantDao;

/**
 * 文件名：BaseService
 * .
 */
public class BaseService {
	@Autowired
    protected CommonDao commonDao;
	@Autowired
	protected ConstantDao constantDao;
}
