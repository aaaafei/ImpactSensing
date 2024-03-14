package com.jsti.tunnel.monitor.dao.master;

import com.jsti.tunnel.monitor.bean.TmDispositionProcess;

import java.util.List;

public interface TmDispositionProcessDao {
    List<TmDispositionProcess> selectList(TmDispositionProcess tmDispositionProcess);

    TmDispositionProcess selectById(Long id);

    void insert(TmDispositionProcess tmDispositionProcess);

    void update(TmDispositionProcess tmDispositionProcess);

    void delete(Long id);
}