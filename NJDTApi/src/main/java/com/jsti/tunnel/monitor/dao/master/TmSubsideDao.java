package com.jsti.tunnel.monitor.dao.master;

import com.jsti.tunnel.monitor.bean.TmSubside;

import java.util.List;
import java.util.Map;

public interface TmSubsideDao {

    List<TmSubside> selectSubside(Map<String, Object> param);


}
