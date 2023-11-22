package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.TmOriginData;
import com.jsti.tunnel.monitor.pojo.TmOriginDataParams;

import java.util.List;

public interface TmOriginDataService {
    int deleteByPrimaryKey(Long id);

    TmOriginData selectByPrimaryKey(Long id);

    List<TmOriginData> selectList(TmOriginDataParams tmOriginDataPojo);

}
