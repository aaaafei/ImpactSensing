package com.jsti.tunnel.monitor.service;
 
import com.jsti.tunnel.monitor.bean.Organ;
import com.jsti.tunnel.monitor.bean.User; 

import java.util.List;

/**
 * 文件名：UserService .
 */
public interface DepartmentService {
	List<Organ> getAll();
	
	List<Organ> getOrgans(Organ organ,User u);

	List<Organ> getOrgansAndUsers(Integer limitPermison,User u);
	
	Organ getOrgansByDeptid(Organ organ);

	void insert(Organ organ);

	void update(Organ organ);

	void delete(Integer id);
	
 
	
	// 根据条件获取公司下道管或者收费站信息
	List<Organ> getOrgans(Organ organ);
 
}
