package com.jsti.tunnel.monitor.service;

import java.util.List;

import com.jsti.tunnel.monitor.bean.TmCalculateModel;

public interface TmCalculateModelService {
    int deleteByPrimaryKey(Integer id);

    int insert(TmCalculateModel record);

    int insertSelective(TmCalculateModel record);

    TmCalculateModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TmCalculateModel record);

    int updateByPrimaryKey(TmCalculateModel record);
    
    List<TmCalculateModel> selectDataList(TmCalculateModel tmCalculateModel);
    
    List<TmCalculateModel> selectRootNodeDataList(TmCalculateModel tmCalculateModel);
}
