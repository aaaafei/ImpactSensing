package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.TmDispositionResult;

import java.util.List;

public interface TmDispositionResultService {

    List<TmDispositionResult> selectList(TmDispositionResult tmDispositionResult);

    TmDispositionResult selectById(Long id);

    void save(TmDispositionResult tmDispositionResult);

    void delete(Long id);
}
