package com.jsti.tunnel.monitor.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsti.system.err.BusinessException;
import com.jsti.tunnel.monitor.bean.Permission;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.pojo.RolePermision;
import com.jsti.tunnel.monitor.service.MenuService;
import com.jsti.tunnel.monitor.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件名：TestController
 * .
 */
/**
 * @author wenhongquan
 *
 */
@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private MenuService menuService;
    
    /**
     * 添加权限
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/addPermission",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String addPermission(@RequestBody Permission permission) throws Exception{
    	permissionService.insert(permission);
    	return returnResult(0, "添加成功", null);
    }
    
    @RequestMapping(value = "/addRolePermission",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String addPermission(@RequestBody RolePermision rolePermision) throws Exception{
    	permissionService.addrolepermision(rolePermision);
    	return returnResult(0, "添加成功", null);
    }
    
    @RequestMapping(value = "/deleteRolePermission",method=RequestMethod.DELETE,produces="application/json;charset=UTF-8")
    public String deletePermission(@RequestBody RolePermision rolePermision) throws Exception{
    	permissionService.deleterolepermision(rolePermision);
    	return returnResult(0, "刪除成功", null);
    }
    @RequestMapping(value = "/updateRolePermission",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
    public String updateRolePermission(@RequestBody RolePermision rolePermision) throws Exception{
    	permissionService.updaterolepermision(rolePermision);
    	return returnResult(0, "更新成功", null);
    }
    
    /**
     * 修改权限
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/updatePermission/{permissionid}",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
    public String updatePermission(@RequestBody Permission permission,@PathVariable int permissionid){
        try{
        	permission.setId(permissionid);
        	permissionService.update(permission);
        	return super.returnResult(0, "修改成功", null);
        }catch(Exception e){
        	throw new BusinessException(20003);
        }
    }
    
    /**
     * 删除权限（软删除）
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/deletePermission/{permissionid}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String deletePermission(@PathVariable Integer permissionid){
        try{
        	permissionService.delete((permissionid));
        	return returnResult(0, "删除成功", null);
        }catch(Exception e){
        	throw new BusinessException(20002);
        }
    }

    /**
     * 获取所用权限
     * @return
     */
    @RequestMapping(value = "/get/all",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String getAllPermission(){
        try{ 
        	List<Permission> permissions= permissionService.getPermissions(new  Permission());
        	
        	return returnResult(0, "获取成功", permissions);
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
    
    
    @RequestMapping(value = "/getPermissionByRole/{roleid}/{page}/{size}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String getPermissionByRole(@PathVariable Integer roleid,@PathVariable Integer page,@PathVariable Integer size,@RequestBody User user){
        try{
        	PageHelper.startPage(page, size); 
        	
        	List<Permission> permissions= permissionService.getByRoleId(roleid);
        	return returnResult(0, "获取成功", new PageInfo(permissions));
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
    
    @RequestMapping(value = "/getAllPermissionByRole/{roleid}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String getPermissionByRole(@PathVariable Integer roleid){
        try{
        	List<Permission> permissions= permissionService.getByRoleId(roleid);
        	return returnResult(0, "获取成功", permissions);
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
    
    
    
}
