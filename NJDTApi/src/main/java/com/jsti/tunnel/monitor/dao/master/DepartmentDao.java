package com.jsti.tunnel.monitor.dao.master;

import com.jsti.tunnel.monitor.bean.*;

import java.util.List;

/**
 * 文件名：UserInfoDao
 */
public interface DepartmentDao  {
	List<Organ> getAll();
    List<Organ> getOrgan(Organ organ);
    void insert(Organ organ);
    void update(Organ organ);
    void delete(Integer id);
   
}
