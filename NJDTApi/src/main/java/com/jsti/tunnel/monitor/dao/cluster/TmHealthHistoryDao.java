package com.jsti.tunnel.monitor.dao.cluster;

import java.util.List;
import java.util.Map;

import com.jsti.tunnel.monitor.bean.TmHealthHistory;

public interface TmHealthHistoryDao {
	List<TmHealthHistory> getData(Map<String, Object> params);
	void add(TmHealthHistory tmHealthHistory);
	TmHealthHistory getLastOne(Map<String, Object> params);
}
