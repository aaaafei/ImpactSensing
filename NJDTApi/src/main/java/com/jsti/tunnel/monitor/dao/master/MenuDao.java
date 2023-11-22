package com.jsti.tunnel.monitor.dao.master;

import com.jsti.tunnel.monitor.bean.Menu;

import java.util.List;

/**
 * 文件名：UserInfoDao
 * .
 */
public interface MenuDao  {
     List<Menu> getMenu(Menu menu);
     List<Menu> getMenuByParent(Menu menu);
     void insert(Menu menu);
     void update(Menu menu);
     void delete(Integer id);
}
