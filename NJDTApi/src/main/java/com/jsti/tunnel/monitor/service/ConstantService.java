package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.Constant;

import java.util.List;

/**
 * 
 *
 * ConstantService
 * 
 * tiger
 * tiger
 * 2017年5月4日 上午10:41:40
 * 
 * @version 1.0.0
 *
 */
public interface ConstantService {
    List<Constant> getAll();
    Constant getById(Long id);
    List<Constant> getByFlag(String flag);
    List<Constant> getByName(String flagName);
    Constant getByFlagAndValue(Constant constant);
    void insert(Constant constant);
    void update(Constant constant);
    void delete(Long id);
}
