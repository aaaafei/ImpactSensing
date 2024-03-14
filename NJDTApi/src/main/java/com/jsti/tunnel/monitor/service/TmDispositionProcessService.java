package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.TmDispositionProcess;

import java.util.List;

public interface TmDispositionProcessService {

    List<TmDispositionProcess> selectList(TmDispositionProcess tmDispositionProcess);

    TmDispositionProcess selectById(Long id);

    void save(TmDispositionProcess tmDispositionProcess);

    void delete(Long id);
}
