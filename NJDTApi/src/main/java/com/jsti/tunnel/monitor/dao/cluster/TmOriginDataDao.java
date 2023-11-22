package com.jsti.tunnel.monitor.dao.cluster;

import com.jsti.tunnel.monitor.bean.TmOriginData;
import com.jsti.tunnel.monitor.pojo.TmOriginDataParams;

import java.util.List;

public interface TmOriginDataDao {
    int deleteByPrimaryKey(Long id);

    TmOriginData selectByPrimaryKey(Long id);

    List<TmOriginData> selectList(TmOriginDataParams tmOriginDataPojo);

}