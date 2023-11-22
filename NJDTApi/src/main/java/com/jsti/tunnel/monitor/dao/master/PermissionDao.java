package com.jsti.tunnel.monitor.dao.master;

import com.jsti.tunnel.monitor.bean.Permission;
import com.jsti.tunnel.monitor.pojo.RolePermision;

import java.util.List;

/**
 * 文件名：PermissionDao
 * 版本信息：日期：2017/3/31 .
 */
public interface PermissionDao {
     List<Permission> findAll();
     List<Permission> getPermission(Permission permission);
     void insert(Permission permission);
     void update(Permission permission);
     void delete(Integer id);
     List<Permission> findByAdminUserId(int userId);
     List<Permission> getByRoleId(int roleId);
    
     void addrolepermision(RolePermision rolePermision);
    
     void deleterolepermision(RolePermision rolePermision);
     void updaterolepermision(RolePermision rolePermision);
    
    
    
}
