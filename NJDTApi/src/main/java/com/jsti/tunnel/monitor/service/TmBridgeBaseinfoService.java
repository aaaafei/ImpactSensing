package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.TmBridgeBaseinfo;

import java.util.List;

public interface TmBridgeBaseinfoService{

    List<TmBridgeBaseinfo> selectList(TmBridgeBaseinfo tmBridgeBaseinfo);

    TmBridgeBaseinfo selectByUuid(String uuid);

    void save(TmBridgeBaseinfo tmBridgeBaseinfo);

    void delete(String uuid);
}
