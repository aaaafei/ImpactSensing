package com.jsti.tunnel.monitor.dao.master;

import com.jsti.tunnel.monitor.bean.TmBridgeBaseinfo;

import java.util.List;

public interface TmBridgeBaseinfoDao {

    List<TmBridgeBaseinfo> selectList(TmBridgeBaseinfo tmBridgeBaseinfo);

    TmBridgeBaseinfo selectByUuid(String uuid);

    void insert(TmBridgeBaseinfo tmBridgeBaseinfo);

    void update(TmBridgeBaseinfo tmBridgeBaseinfo);

    void delete(String uuid);
}
