package com.jsti.tunnel.monitor.dao.master;

import com.jsti.tunnel.monitor.bean.TmDispositionProcess;
import com.jsti.tunnel.monitor.bean.TmDispositionResult;

import java.util.List;

public interface TmDispositionResultDao {
    List<TmDispositionResult> selectList(TmDispositionResult tmDispositionResult);

    TmDispositionResult selectById(Long id);

    void insert(TmDispositionResult tmDispositionResult);

    void update(TmDispositionResult tmDispositionResult);

    void delete(Long id);
}