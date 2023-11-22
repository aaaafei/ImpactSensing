package com.jsti.tunnel.monitor.dao.master;

import com.jsti.tunnel.monitor.bean.Role;
import com.jsti.tunnel.monitor.pojo.RoleData;

import java.util.List;
import java.util.Map;

/**
 * 文件名：UserInfoDao
 * .
 */
public interface RoleDao  {
     List<Role> getRole(Role role);
     void insert(Role role);
     void update(Role role);
     void delete(Integer id);
    
    
     void roleBindPermission(Map<String,Integer> part);
     void roleUnBindPermission(Integer roleId);
     void roleBindUser(Map<String,Integer> part);
     void roleBindDepartment(Map<String,Integer> part);
     void roleBindJob(Map<String,Integer> part);
    
     List<Role> getRoleByDept(Integer departmentId);
     List<Role> getRoleByUser(Integer userId);
     List<Role> getRoleByPosition(Integer positionId);
    
     List<RoleData> getRolesCount(Role role);
    
     void roleUnBindUser(Integer userId);
    
     List<Role> getRoleByPermision(Integer permisionid);
    
}
