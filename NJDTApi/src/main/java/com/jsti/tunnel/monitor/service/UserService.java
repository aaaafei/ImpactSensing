package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * 文件名：UserService
 * .
 */
public interface UserService extends UserDetailsService {
    List<User> getAll();
    List<User> getByUserIds(List<Integer> ids);
    List<User> getUsers(User user);
    User getOne(Integer id);
    void insert(User user);
    void update(User user);
    void delete(Integer id);
    UserDetails loadUserByUsername(String username);
    int queryCountByJob(User user);
    List<User> getAllCheckmans();
    List<User> getFsUserByDeptId(User user);
    List<User> getWGUserByDeptId(User user);
    List<User> getUserByOrgRole(User user);
    List<User> getUserByRoles(User user);
}
