package com.jsti.tunnel.monitor.dao.master;

import java.util.List;

import com.jsti.tunnel.monitor.bean.TmCalculateValueConfig;

public interface TmCalculateValueConfigDao {
	TmCalculateValueConfig selectByPrimaryKey(Integer id);
	
    int insert(TmCalculateValueConfig record);

    int insertSelective(TmCalculateValueConfig record);
    
    int deleteByLeafId(Integer leaf_id);
    
    List<Integer> selectModelIdsByDevice1Id(List<Integer> list);
    
    List<TmCalculateValueConfig> selectData(TmCalculateValueConfig record);
}