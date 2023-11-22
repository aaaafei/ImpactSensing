package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.Permission;
import com.jsti.tunnel.monitor.pojo.RolePermision;

import java.util.List;

/**
 * 文件名：UserService .
 */
public interface PermissionService {
	List<Permission> getPermissions(Permission permission);

	void insert(Permission permission);

	void update(Permission permission);

	void delete(Integer id);

	List<Permission> getByRoleId(int roleId);

	void addrolepermision(RolePermision rolePermision);

	void deleterolepermision(RolePermision rolePermision);

	void updaterolepermision(RolePermision rolePermision);
	
	List<Permission> getPermissions(int userId);
	
	Permission getPermissionByResourceId(Permission permission);
}
