package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.Menu;
import com.jsti.tunnel.monitor.bean.User;

import java.util.List;

/**
 * 文件名：UserService
 * .
 */
public interface MenuService {
    List<Menu> getMenus(Menu menu,User user,Integer judge);
    List<Menu> getMenuByParent(Menu menu);
    List<Menu> getAllMenu();
    void insert(Menu menu);
    void update(Menu menu);
    void delete(Integer id);
}
