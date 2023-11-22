package com.jsti.tunnel.monitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jsti.tunnel.monitor.bean.Constant;
import com.jsti.tunnel.monitor.bean.Permission;
import com.jsti.tunnel.monitor.bean.Role;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.dao.master.PermissionDao;
import com.jsti.tunnel.monitor.dao.master.RoleDao;
import com.jsti.tunnel.monitor.dao.master.UserDao;
import com.jsti.tunnel.monitor.pojo.SimpleGrantedAuthority;
import com.jsti.tunnel.monitor.service.BaseService;
import com.jsti.tunnel.monitor.service.UserService;
import com.jsti.tunnel.monitor.util.CodeUtil;

/**
 * 文件名：UserServiceImpl
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
 

    @Override
    public List<User> getAll() {
    	List<User> users = userDao.getAll();
    	if(users!=null && users.size()>0){
    		for(User u :users){
    			u.setPassword(null);
    		}
    	}
        return users;
    }

	@Override
	public List<User> getByUserIds(List<Integer> ids) {
		return userDao.getByUserIds(ids);
	}


	@Override
	public List<User> getUsers(User user) {
		//获取权限
		
		List<User> users= userDao.getUsers(user);
		if(users!=null && users.size()>0){
			for(User u : users){
				u.setPassword(null);
				List<Permission> permissions = permissionDao.findByAdminUserId(u.getId());
	            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
	            List<Role> roles = roleDao.getRoleByUser(u.getId());
	            u.setRoles(roles);
	            for (Permission permission : permissions) {
	                if (permission != null && permission.getName() != null) {
	                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
	                    grantedAuthorities.add(grantedAuthority);
	                }
	            }
	            u.setGrantedAuthorities(grantedAuthorities);
			}
		}
		return users;
		
		
		
	}


    @Override
//    @Cacheable(value = "usercache",keyGenerator = "keyGenerator")
    public User getOne(Integer id) {
    	return userDao.getOne(id);
    }

    @Override
    public void insert(User user) {
    	userDao.insert(user);
    }

    @Override
    public void update(User user) {
    	userDao.update(user);
    }

    @Override
    public void delete(Integer id) {
    	userDao.delete(id);
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDao.findByUserName(username);
        if (user != null) {
            List<Permission> permissions = permissionDao.findByAdminUserId(user.getId());
            List<Role> roles = roleDao.getRoleByUser(user.getId());
            user.setRoles(roles);
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (Permission permission : permissions) {
                if (permission != null && permission.getName() != null) {

                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
                    grantedAuthorities.add(grantedAuthority);
                }
            }
            user.setGrantedAuthorities(grantedAuthorities);
            return user;
        } else {
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }

    }

	@Override
	public int queryCountByJob(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<User> getAllCheckmans() {
		// TODO Auto-generated method stub
		return userDao.getAll();
	}
	
	@Override
	public List<User> getFsUserByDeptId(User user) {
		// TODO Auto-generated method stub
//		Constant t =  CodeUtil.getCodeByFlagAndValue("feestation_check_jobs", "1");
//		
//		String[] postions = t.getCode_name().split(",");
//		List<Integer> positionList = new ArrayList<Integer>();
//		for(int i =0 ;i<postions.length;i++){
//			positionList.add(Integer.parseInt(postions[i]));
//		}
//
//		user.setPosition_ids(positionList);
		return userDao.getUsers(user);
	}

	@Override
	public List<User> getWGUserByDeptId(User user) {
		Constant t =  CodeUtil.getCodeByFlagAndValue("wrecker_group_check_jobs", "1");
		
		String[] postions = t.getCode_name().split(",");
		List<Integer> positionList = new ArrayList<Integer>();
		for(int i =0 ;i<postions.length;i++){
			positionList.add(Integer.parseInt(postions[i]));
		}

		user.setPosition_ids(positionList);
		return userDao.getUsers(user);
	}

	@Override
	public List<User> getUserByOrgRole(User user){
    	List<User> users = userDao.getUserByOrgRole(user);
    	for(User u : users){
    		u.setPassword(null);
		}
    	return users;
	}

	@Override
	public List<User> getUserByRoles(User user){
    	List<User> users = userDao.getUserByRoles(user);
		for(User u : users){
			u.setPassword(null);
		}
		return users;
	}
}
