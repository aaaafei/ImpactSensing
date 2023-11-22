package com.jsti.tunnel.monitor.controller;

import com.jsti.system.err.BusinessException; 
import com.jsti.tunnel.monitor.bean.Organ;
import com.jsti.tunnel.monitor.bean.User; 
import com.jsti.tunnel.monitor.service.DepartmentService;
import com.jsti.tunnel.monitor.util.CacheUtil;
import com.jsti.tunnel.monitor.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/dept")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;
    
    /**
     * 添加部门
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/addDept",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String addDept(@RequestBody Organ organ){
        
    	departmentService.insert(organ);
    	
    	CacheUtil.refreshDeptMap(departmentService);
    	return returnResult(0, "添加成功", null);
    }
    
    /**
     * 修改部门
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/updateDept/{organid}",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String updateDept(@RequestBody Organ organ,@PathVariable int organid){
        try{
        	organ.setId(organid);
        	departmentService.update(organ);
        	CacheUtil.refreshDeptMap(departmentService);
        	return super.returnResult(0, "修改成功", null);
        }catch(Exception e){
        	throw new BusinessException(20003);
        }
    }
    
    /**
     * 删除部门（软删除）
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/deleteDept/{organid}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String deleteDept(@PathVariable Integer organid){
        try{
        	departmentService.delete((organid));
        	CacheUtil.refreshDeptMap(departmentService);
        	return returnResult(0, "删除成功", null);
        }catch(Exception e){
        	throw new BusinessException(20002);
        }
    }
    
    /**
     * 获取所用部门
     * @return
     */
    @PostMapping(value = "/get/all",produces="application/json;charset=UTF-8")
    public String getAll(){
        try{
            List<Organ> organs= departmentService.getOrgans(null,null);
        	return returnResult(0, "获取成功", organs);
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }

    @GetMapping(value = "/get/all",produces="application/json;charset=UTF-8")
    public String getAllDept(HttpServletRequest request){
        try{
        	User u = getCurrentUser(request);
            boolean isAdmin = UserUtil.IsAdmin(u);//是否管理员登录
            List<Organ> organs = null;
            if(!isAdmin){
                Organ organ=new Organ();
                organ.setStatus(0);//未逻辑删除的数据
                organs= departmentService.getOrgans(organ,u);
            }
            else{
                organs= departmentService.getOrgans(null,null);
            }

        	return returnResult(0, "获取成功", organs);
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }

    /**
     * 获取部门，根据参数limitPermison控制是否根据当前用户权限，限制获取部门，限制的情况下：管理员可以获取所有部门，否则只获取到本部门
     * @param request
     * @param limitPermison
     * @return
     */
    @RequestMapping(value = "/get/deptsAndUsers/{limitPermison}")
    public String getDeptsAndContainUsers(HttpServletRequest request,@PathVariable Integer limitPermison){
        User u = getCurrentUser(request);
        //limitPermison:0 不限制，1 限制
        List<Organ> organs= departmentService.getOrgansAndUsers(limitPermison,u);
        return returnResult(0, "获取成功", organs);
    }
    
    @RequestMapping(value = "/getDeptByParent/{pid}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String getDeptByParent(@PathVariable Integer pid){
        try{
        	Organ organ = new Organ();
        	organ.setParentid(pid);
        	List<Organ> organs= departmentService.getOrgans(organ,null);
        	return returnResult(0, "获取成功",(organs));
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
    
    @RequestMapping(value = "/getDeptById/{organid}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String getDeptById(@PathVariable Integer organid){
        try{
        	Organ organ = new Organ();
        	organ.setId(organid);
        	
        	Organ organs = departmentService.getOrgansByDeptid(organ);
//        	if(organs==null||organs.size()<1 ) throw  new BusinessException(20001);
        	return returnResult(0, "获取成功", organs);
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
   
 
 
    
 
    
    @RequestMapping(value = "/getDeptInfosByDeptId",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String getDeptInfosByDeptId(HttpServletRequest request,@RequestBody Organ organ){
    	List<Organ> organs = departmentService.getOrgans(organ);
    	return returnResult(0, "获取成功", organs);
    }

    /**
     * 根据检索条件获取部门信息
     * @param organ
     * @return
     */
    @RequestMapping(value="/getDeptInfo",method= RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String getDeptInfos(@RequestBody Organ organ){
        List<Organ> list = departmentService.getOrgans(organ);
        return returnSuccessResult(list);
    }

  
    
}
