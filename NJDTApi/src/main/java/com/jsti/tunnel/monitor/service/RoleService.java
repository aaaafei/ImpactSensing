package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.Role;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.pojo.RoleData;

import java.util.List;

/**
 * 文件名：UserService .
 */
public interface RoleService {
	List<Role> getRoles(Role role,User u);

	void insert(Role role);

	void update(Role role);

	void delete(Integer id);

	void roleBindPermission(Integer roleId,Integer permissionId);
	
	void roleUnBindPermission(Integer roleId);

	void roleBindUser(Integer roleId,Integer userId);

	void roleBindDepartment(Integer roleId,Integer departmentId);

	void roleBindJob(Integer roleId,Integer jobId);

	List<Role> getRoleByDept(Integer departmentId);

	List<Role> getRoleByUser(Integer userId);

	List<Role> getRoleByPosition(Integer positionId);
	List<RoleData> getRoleDatas(Role role);
	
	void roleUnBindUser(Integer userId);
	
	List<Role> getRoleByPermision(Integer permisionid);
}
