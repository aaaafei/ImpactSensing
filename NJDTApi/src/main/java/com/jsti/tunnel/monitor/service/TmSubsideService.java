package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.TmSubside;

import java.util.List;
import java.util.Map;

public interface TmSubsideService {

    List<TmSubside> selectSubside(Map<String, Object> param);
}
