package com.jsti.tunnel.monitor.service;

import java.util.List;
import java.util.Map;

import com.jsti.tunnel.monitor.bean.TmHealthHistory;

public interface TmHealthHistoryService {
	List<TmHealthHistory> getData(Map<String, Object> params);
	void add(TmHealthHistory tmHealthHistory);
	TmHealthHistory getLastOne(Map<String, Object> params);
}
