package com.jsti.tunnel.monitor.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsti.tunnel.monitor.bean.Role;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.dao.master.RoleDao;
import com.jsti.tunnel.monitor.pojo.RoleData;
import com.jsti.tunnel.monitor.service.BaseService;
import com.jsti.tunnel.monitor.service.RoleService;

/**
 * 文件名：UserServiceImpl
 * .
 */
@Service
public class RoleServiceImpl extends BaseService implements RoleService {

  


    @Autowired
    private RoleDao roleDao;
     


	@Override
	public List<Role> getRoles(Role role,User u) {
		
		List<Role> roles = roleDao.getRole(role);
		
		if(u == null || u.getRoles()==null || u.getRoles().size() ==0) return roles;
		boolean hasrole = false;
		for(Role r:u.getRoles()){
			if(r.getName().equals("ROLE_ADMIN")){
				hasrole = true;
				break;
			}
		}
		if(hasrole) return roles;
		//获取用户最高权限
		int level=Integer.MAX_VALUE;
		List<String> names = new ArrayList<>();
		for(Role r:u.getRoles()){
			if(r.getLevel()<level) level = r.getLevel();
			names.add(r.getName());
		}
		
		
		List<Role> roletemp = new ArrayList<Role>();
		for(Role r:roles){
			if(r.getLevel()!=null && r.getLevel()>=level){
				boolean hasrole1 = false;
				for(String s:names){
					if(s.contains(r.getName())){
						hasrole1 = true;
					}
				}
				if(hasrole1){
					roletemp.add(r);
				}
				
			}
			
			
		}
		
		roles = roletemp;
		return roles;
	}


	@Override
	public void insert(Role role) {
		roleDao.insert(role);
	}


	@Override
	public void update(Role role) {
		roleDao.update(role);
	}


	@Override
	public void delete(Integer id) {
		roleDao.delete(id);
	}



	@Override
	public List<Role> getRoleByDept(Integer departmentId) {
		return roleDao.getRoleByDept(departmentId);
	}


	@Override
	public List<Role> getRoleByUser(Integer userId) {
		return roleDao.getRoleByUser(userId);
	}


	@Override
	public List<Role> getRoleByPosition(Integer positionId) {
		return getRoleByPosition(positionId);
	}


	@Override
	public void roleBindPermission(Integer roleId, Integer permissionId) {
		Map<String, Integer> part = new HashMap<String,Integer>();
		part.put("roleId", roleId);
		part.put("permissionId", permissionId);
		roleDao.roleBindPermission(part);
	}


	@Override
	public void roleBindUser(Integer roleId, Integer userId) {
		Map<String, Integer> part = new HashMap<String,Integer>();
		part.put("roleId", roleId);
		part.put("userId", userId);
		roleDao.roleBindUser(part);
	}


	@Override
	public void roleBindDepartment(Integer roleId, Integer departmentId) {
		Map<String, Integer> part = new HashMap<String,Integer>();
		part.put("roleId", roleId);
		part.put("departmentId", departmentId);
		roleDao.roleBindDepartment(part);
	}


	@Override
	public void roleBindJob(Integer roleId, Integer jobId) {
		Map<String, Integer> part = new HashMap<String,Integer>();
		part.put("roleId", roleId);
		part.put("jobId", jobId);
		roleDao.roleBindJob(part);
	}


	@Override
	public List<RoleData> getRoleDatas(Role role) {
		return roleDao.getRolesCount(role);
	}


	@Override
	public void roleUnBindUser(Integer userId) {
		roleDao.roleUnBindUser(userId);
		
	}

	@Override
	public List<Role> getRoleByPermision(Integer permisionid){
		return roleDao.getRoleByPermision(permisionid);
	}


	@Override
	public void roleUnBindPermission(Integer roleId) {
		// TODO Auto-generated method stub
		roleDao.roleUnBindPermission(roleId);
	}
    
}
