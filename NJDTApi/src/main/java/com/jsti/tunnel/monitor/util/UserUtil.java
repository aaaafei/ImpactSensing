package com.jsti.tunnel.monitor.util;

import com.jsti.tunnel.monitor.bean.Role;
import com.jsti.tunnel.monitor.bean.User;

import java.util.ArrayList;
import java.util.List;

public class UserUtil {

    /**
     * 判断是否是管理员账户
     * @param user
     * @return
     */
    public static boolean IsAdmin(User user){
        boolean isAdmin = false;//是否管理员登录
        List<Role> roles = user.getRoles();
        for(Role role : roles){
            if(role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_COMPANY_ADMIN")){
                isAdmin = true;
                break;
            }
        }
        return isAdmin;
    }
    
    public static boolean IsTargeRole(User user,List<String> rolesName){
        boolean isTargeRole = false;//是否管理员登录
        List<Role> roles = user.getRoles();
        for(Role role : roles){ 
        	for(String rolename : rolesName) {
        		 if(role.getName().equals(rolename) ){
                 	isTargeRole = true;
                     break;
                 }
        	}   
        }
        return isTargeRole;
    }

    
    public static boolean IsDataManager(User user){
        boolean isRole = false;
        List<Role> roles = user.getRoles();
        for(Role role : roles){
            if(role.getName().equals("ROLE_DATA_MANAGER")){
                isRole = true;
                break;
            }
        }
        return isRole;
    }

    public static boolean isGroup(User user){
        boolean isRole = false;
        List<Role> roles = user.getRoles();
        for(Role role : roles){
            if(role.getName().toUpperCase().equals("ROLE_GROUP")){
                isRole = true;
                break;
            }
        }
        return isRole;
    }

    public static List<String> getRoleNames(User user) {
        List<String> list = new ArrayList<>();
        List<Role> roles = user.getRoles();
        for(Role role : roles){
            list.add(role.getName());
        }
        return list;
    }
}
