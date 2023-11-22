package com.jsti.tunnel.monitor.dao.master;
 
import com.jsti.tunnel.monitor.bean.User;

import java.util.List;

/**
 * 文件名：UserInfoDao
 */

public interface UserDao {
    List<User> getAll();
    List<User> getUsers(User user);

    
    User getOne(Integer id);
    void insert(User user);
    void update(User user);
    void delete(Integer id);
    User findByUserName(String username);
 

    List<User> getUserByOrgRole(User user);
    List<User> getUserByRoles(User user);
    List<User> getByUserIds(List<Integer> ids);
}
