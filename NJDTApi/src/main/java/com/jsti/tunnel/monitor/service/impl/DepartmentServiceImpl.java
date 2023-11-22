package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.*;
import com.jsti.tunnel.monitor.dao.master.DepartmentDao;
import com.jsti.tunnel.monitor.dao.master.UserDao;
import com.jsti.tunnel.monitor.service.BaseService;
import com.jsti.tunnel.monitor.service.DepartmentService; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名：UserServiceImpl 版本信息：日期：2017/3/30 Copyright  Corporation 2017
 * 版权所有.
 */
@Service
public class DepartmentServiceImpl extends BaseService implements DepartmentService {


	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private UserDao userDao;

	@Override
	public List<Organ> getOrgans(Organ organ, User u) {

		List<Organ> organs = departmentDao.getOrgan(organ);
		List<Organ> olist = new ArrayList<>();//根节点集合
		Map<Integer, List<Organ>> map = new HashMap<>();//父节点id、子节点集合键值对
		// 找出所有父
		for (Organ o : organs) {
			o.setLabel(o.getOrganname());
			if (o.getParentid() != null && o.getParentid()!=0) {
				List<Organ> organList = map.get(o.getParentid());
				if (organList == null) {
					organList = new ArrayList<>();
				}
				organList.add(o);
				map.put(o.getParentid(), organList);
			}
		}
		// 找出子
		for (Integer oid : map.keySet()) {
			for (Organ m2 : organs) {
				if (m2.getId() == oid) {
					m2.setChildren(map.get(oid));
					if (m2.getParentid() == null || m2.getParentid()==0) {
						olist.add(m2);
					}
					if (m2.getChildren() == null) {
						m2.setLeaf(true);
					}
				}
			}
		}

		return getOragnsByPermison(olist,u);
	}

	@Override
	public List<Organ> getOrgansAndUsers(Integer limitPermison,User u) {
		List<Organ> organs = departmentDao.getOrgan(null);
		List<Organ> olist = new ArrayList<>();
		Map<Integer, List<Organ>> map = new HashMap<>();
		// 找出所有父
		for (Organ o : organs) {
			//set organ.users
			User user = new User();
			user.setOrganid(o.getId());
			o.setUsers(userDao.getUsers(user));

			o.setLabel(o.getOrganname());
			if (o.getParentid() != null && o.getParentid()!=0) {
				List<Organ> organList = map.get(o.getParentid());
				if (organList == null) {
					organList = new ArrayList<>();
				}
				organList.add(o);
				map.put(o.getParentid(), organList);
			}
		}
		// 找出子
		for (Integer oid : map.keySet()) {
			for (Organ m2 : organs) {
				if (m2.getId() == oid) {
					m2.setChildren(map.get(oid));
					if (m2.getParentid() == null || m2.getParentid()==0) {
						olist.add(m2);
					}
					if (m2.getChildren() == null) {
						m2.setLeaf(true);
					}
				}
			}
		}

		//limitPermison:0 不限制，1 限制
		if(limitPermison==0)
			return getOragnsByPermison(olist,null);
		else
			return getOragnsByPermison(olist,u);
	}

	@Override
	public void insert(Organ organ) {
		departmentDao.insert(organ);
	}

	@Override
	public void update(Organ organ) {
		departmentDao.update(organ);
	}

	@Override
	public void delete(Integer id) {
		departmentDao.delete(id);
	}

 

	@Override
	public List<Organ> getAll() {
		List<Organ> list = departmentDao.getAll();
		for(Organ organ : list){
			organ.setLabel(organ.getOrganname());
		}
		return list;
	}
 
	
	private List<Organ> getOragnsByPermison(List<Organ> oragns,User user){
		if(user == null ) return oragns;
		boolean hasrole = false;
		for(Role r:user.getRoles()){
			if(r.getName().equals("ROLE_ADMIN") || r.getName().equals("ROLE_JICHA_ADMIN")){//管理员和稽查管理员可查看所有用户（by tiger）
				hasrole = true;
				break;
			}
		}
		if(hasrole){
			return oragns;
		}
		
		//找出自己
		Organ temp = null;
		for(Organ o :oragns){
			temp = findOrgan(o,user.getOrganid());
			if(temp!=null) break;	
		}

		if(temp!=null){
			oragns = new ArrayList<>();
			oragns.add(temp);
		} 
	    	
	    return oragns;
	}
	
	@Override
	public List<Organ> getOrgans(Organ organ) {
		return departmentDao.getOrgan(organ);
	}

	private Organ findOrgan(Organ o,Integer organid)
	{
		if(o.getId().intValue() == organid.intValue()){
			return o;
		}else{
			if(o.getChildren()!=null && o.getChildren().size()>0){
				for(Organ s:o.getChildren()){
					Organ oo = findOrgan(s, organid);
					if(oo!=null) return oo;
				}
				return null;
			}else{
				return null;
			}
		}
	}

 

	@Override
	public Organ getOrgansByDeptid(Organ organ) {
		// TODO Auto-generated method stub
		List<Organ> organs = departmentDao.getOrgan(organ);
		if(organs !=null && organs.size()>0) {
			return organs.get(0);
		}
		return null;
	}

 

 
 
 

 
}
