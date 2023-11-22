package com.jsti.tunnel.monitor.dao.master;

import com.jsti.tunnel.monitor.bean.Constant;

import java.util.List;

/**
 * 文件名：UserInfoDao
 * .
 */
public interface ConstantDao  {
     List<Constant> getAll();
     Constant getOne(Long id);
     void insert(Constant constant);
     void update(Constant constant);
     void delete(Long id);
     List<Constant> getByFlag(String flag);
     Constant getByFlagAndValue(Constant constant);
     List<Constant> getByName(String flagName);
}
