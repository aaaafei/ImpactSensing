package com.jsti.tunnel.monitor.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsti.tunnel.monitor.bean.Permission;
import com.jsti.tunnel.monitor.dao.master.PermissionDao;
import com.jsti.tunnel.monitor.pojo.RolePermision;
import com.jsti.tunnel.monitor.service.BaseService;
import com.jsti.tunnel.monitor.service.PermissionService;

/**
 * 文件名：UserServiceImpl
 * .
 */
@Service 
public class PermissionServiceImpl extends BaseService implements PermissionService {
 

    @Autowired
    private PermissionDao permissionDao;
     
    
	@Override
	public List<Permission> getPermissions(Permission permission) {
		
		List<Permission> permisions = permissionDao.getPermission(permission);

		List<Permission> olist = new ArrayList<>();
		Map<Integer, List<Permission>> map = new HashMap<>();

		// 找出所有父
		for (Permission o : permisions) {
			o.setLabel(o.getName());

			List<Permission>  permissionList = map.get(o.getPid());
			if(permissionList==null){
				permissionList = new ArrayList<>();
			}
			if(o.getPid()!=null ){
				permissionList.add(o);
				map.put(o.getPid(), permissionList);//将当前节点放入map，key为父节点id
			}else{
				if(!map.containsKey(o.getId()))
					map.put(o.getId(), permissionList);
			}
		}

		// 找出子
		for (Integer oid : map.keySet()) {
			for (Permission m2 : permisions) {
				if (m2.getId() == oid.intValue()) {
					m2.setChildren(map.get(oid));
					if (m2.getPid() == null ) {
						olist.add(m2);
					}
				}
			}
		}

		return olist;
	}

	@Override
	public void insert(Permission permission) {
		permissionDao.insert(permission);
	}

	@Override
	public void update(Permission permission) {
		permissionDao.update(permission);
	}

	@Override
	public void delete(Integer id) {
		permissionDao.delete(id);
	}

	@Override
	public List<Permission> getByRoleId(int roleId) {
		return permissionDao.getByRoleId(roleId);
	}

	@Override
	public void addrolepermision(RolePermision rolePermision) {
		if(rolePermision.getPermission_type() == null){
			rolePermision.setPermission_type(1);
		}
		permissionDao.addrolepermision(rolePermision);
	}

	@Override
	public void deleterolepermision(RolePermision rolePermision) {
		permissionDao.deleterolepermision(rolePermision);
	}

	@Override
	public void updaterolepermision(RolePermision rolePermision) {
		permissionDao.updaterolepermision(rolePermision);
	}

	@Override
	public List<Permission> getPermissions(int userId) {
		// TODO Auto-generated method stub
		return permissionDao.findByAdminUserId(userId);
	}

	@Override
	public Permission getPermissionByResourceId(Permission permission) {
		// TODO Auto-generated method stub
		List<Permission> permissions = permissionDao.getPermission(permission);
		if(permissions!= null && permissions.size()>0) {
			return permissions.get(0);
		}
		return null;
	}

  
}
