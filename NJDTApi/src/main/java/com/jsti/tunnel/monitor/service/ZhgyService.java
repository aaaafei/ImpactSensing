package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.Fxbg;

import java.util.List;
import java.util.Map;

public interface ZhgyService {
    void add(Fxbg fxbg);
    void delete(List<String> uuids);
    void update(Fxbg fxbg);
    List<Fxbg> getPageData(Map<String,Object> params);
    long getDataNum(Map<String,Object> params);
    Fxbg getObjByUUID(String uuid);
}
