package com.jsti.tunnel.monitor.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsti.system.err.BusinessException;
import com.jsti.system.securityTools.RedisCacheUtil;
import com.jsti.tunnel.monitor.bean.Role;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.service.AuthService;
import com.jsti.tunnel.monitor.service.RoleService;
import com.jsti.tunnel.monitor.service.UserService;
import com.jsti.tunnel.monitor.util.CacheUtil;
import com.jsti.tunnel.monitor.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    
    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${defaultPassword}")
    private String defaultPassword;

    @RequestMapping(value = "/auth/login",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String login(@RequestBody User user){
//    	MD5Util.md5(plainText)
        User u =new User();
        //\u000du = authService.login(user.getUsername(), MD5Util.md5(user.getPassword()));
    	u.setOrganname(CacheUtil.getOrgannameFromMap(new Long(u.getOrganid())));
    	u.setPassword(null);
        return returnSuccessResult("登陆成功", u);
    }
    

    @RequestMapping(value = "/logout",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String logout(){
    	//获取用户名
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	if(username!=null){
    		redisCacheUtil.removeForUserName(username);
    	}
    	//返回成功
    	return returnResult(0, "退出成功", null);
    }


    
    /**
     * 添加用户
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/addUser",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String addUser(@RequestBody User user) throws Exception{
    	user.setPassword(new Md5PasswordEncoder().encodePassword(defaultPassword, null));
    	userService.insert(user);
    	List<User> us=userService.getUsers(user);
    	if(us.size()==1){
    		if(user.getRoleids()!=null){
        		for(String id:user.getRoleids()){
        			roleService.roleBindUser(Integer.parseInt(id), us.get(0).getId());
        		}
        	}
    	}
    	CacheUtil.refreshUserMap(userService);
    	return returnResult(0, "添加成功", null);
    }
    
    /**
     * 修改用户
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/updateUser/{userid}",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String updateUser(@RequestBody User user,@PathVariable int userid){
        try{
        	user.setId(userid);
        	if(user.getRoleids()!=null){
        		roleService.roleUnBindUser(user.getId());
        		for(String id:user.getRoleids()){
        			roleService.roleBindUser(Integer.parseInt(id), user.getId());
        		}
        	}
        	userService.update(user);
        	CacheUtil.refreshUserMap(userService);
        	return super.returnResult(0, "修改成功", null);
        }catch(Exception e){
        	throw new BusinessException(20003);
        }
    }
    
    /**
     * 删除用户（软删除）
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/deleteUser/{userids}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String deleteUser(@PathVariable("userids")  String userids){
        try{
        	List<String> listID = Arrays.asList(userids.split(","));
        	listID.forEach(id -> {
        		userService.delete(Integer.parseInt(id));
        	}); 
        	CacheUtil.refreshUserMap(userService);
        	return returnResult(0, "删除成功", null);
        }catch(Exception e){
        	throw new BusinessException(20002);
        }
    }
    
    /**
     * 获取所用用户
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/get/all/{page}/{size}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String getallUsers(@PathVariable Integer page,@PathVariable Integer size ,HttpServletRequest request){
        try{
        	
        	PageHelper.startPage(page, size); 
        	List<User> users= userService.getAll();
        	
        	
        	return returnResult(0, "获取成功", new PageInfo(users));
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
    
    /**
     * 获取所用用户
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/get/list/{page}/{size}",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String getUsersByParams(@PathVariable Integer page,@PathVariable Integer size, @RequestBody User user){
        try{
        	
        	PageHelper.startPage(page, size); 
//        	List<User> users= userService.getAll();
        	List<User> users= userService.getUsers(user);
        	
        	return returnResult(0, "获取成功", new PageInfo(users));
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
    
    @RequestMapping(value = "/getUserList",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String getUsers(@RequestBody User user){
        try{
        	List<User> users= userService.getUsers(user);
        	
        	return returnResult(0, "获取成功", users);
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }

    @RequestMapping(value = "/getUserInfo",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String getUserInfo(HttpServletRequest request){
        User user = getCurrentUser(request);
        user.setOrganname(CacheUtil.getOrgannameFromMap(new Long(user.getOrganid())));
        user.setPassword(null);
        return returnResult(0, "获取成功", user);
    }


    @RequestMapping(value = "/getUserById/{userid}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String getUser(@PathVariable Integer userid){
        User u = userService.getOne(userid);
    	return returnResult(0, "获取成功", u);
    }
    
    @RequestMapping(value = "/getAllCheckman",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String getAllCheckman(){
        try{
        	List<User> users= userService.getAllCheckmans();
        	return returnResult(0, "获取成功", users);
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
    
    @RequestMapping(value = "/getFsUserList",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String getFsUsers(@RequestBody User user){
        try{
        	List<User> users= userService.getFsUserByDeptId(user);
        	
        	return returnResult(0, "获取成功", users);
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
    
    @RequestMapping(value = "/getWGUserList",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String getWGUserList(@RequestBody User user){
        try{
        	List<User> users= userService.getWGUserByDeptId(user);
        	
        	return returnResult(0, "获取成功", users);
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }


    @GetMapping("/password")
    public String modifyPassword(HttpServletRequest req){
        User user = getCurrentUser(req);
        UserDetails u = redisCacheUtil.getUserByUserName(user.getUsername());
        String oldpwd = req.getParameter("oldpwd");
        String newpwd = req.getParameter("newpwd");
        if(MD5Util.md5(oldpwd).equals(u.getPassword())){
            user.setPassword(new Md5PasswordEncoder().encodePassword(newpwd, null));
            userService.update(user);
            CacheUtil.refreshUserMap(userService);
            return returnSuccessResult("密码修改成功");
        }
        else{
            return returnResult(10009,"原密码输入错误！",null);
        }

    }

    @GetMapping("/resetPWD/{userId}")
    public String resetPWD(@PathVariable Integer userId){
        User u = new User();
        u.setId(userId);
        u.setPassword(new Md5PasswordEncoder().encodePassword(defaultPassword,null));
        userService.update(u);
        CacheUtil.refreshUserMap(userService);
        return returnSuccessResult("密码重置成功");
    }

    @GetMapping("/getUserByOrgRole/{orgId}/{roleCodes}")
    public String getUserByOrgRole(@PathVariable Integer orgId,@PathVariable String roleCodes){
        User user = new User();
        user.setOrganid(orgId);
        List<Role> roles = new ArrayList<>();
        for(String roleCode : roleCodes.split(",")){
            Role role = new Role();
            role.setName(roleCode);
            roles.add(role);
        }
        user.setRoles(roles);
        List<User> users = userService.getUserByOrgRole(user);
        return returnSuccessResult(users);
    }
}
