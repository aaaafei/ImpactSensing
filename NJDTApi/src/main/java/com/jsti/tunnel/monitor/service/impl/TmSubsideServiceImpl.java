package com.jsti.tunnel.monitor.service.impl;

import com.jsti.tunnel.monitor.bean.TmSubside;
import com.jsti.tunnel.monitor.dao.master.TmSubsideDao;
import com.jsti.tunnel.monitor.service.TmSubsideService;
import com.jsti.tunnel.monitor.util.DeviceDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class TmSubsideServiceImpl implements TmSubsideService {

    @Autowired
    TmSubsideDao tmSubsideDao;

    @Override
    public List<TmSubside> selectSubside(Map<String, Object> param) {
        List<TmSubside> list = tmSubsideDao.selectSubside(param);
        for (TmSubside item : list) {
            int miles1 = DeviceDataUtil.getMilesFromStakeNumber(item.getDevice1_stake_number());
            int miles2 = DeviceDataUtil.getMilesFromStakeNumber(item.getDevice2_stake_number());
            int distance = miles1 - miles2;
            BigDecimal b_d1_r_value = new BigDecimal(item.getDevice1_realtime_physical_data());
            BigDecimal b_d1_i_value = new BigDecimal(item.getDevice1_initial_value());
            BigDecimal b_d1_result_value = b_d1_r_value.subtract(b_d1_i_value);
            BigDecimal b_d2_r_value = new BigDecimal(item.getDevice2_realtime_physical_data());
            BigDecimal b_d2_i_value = new BigDecimal(item.getDevice2_initial_value());
            BigDecimal b_d2_result_value = b_d2_r_value.subtract(b_d2_i_value);
            BigDecimal b_result_value = b_d1_result_value.subtract(b_d2_result_value).divide(new BigDecimal(distance*1000),BigDecimal.ROUND_HALF_UP);
            b_result_value = b_result_value.setScale(5,BigDecimal.ROUND_UP);
            item.setDevice1_result_value(b_d1_result_value.toString());
            item.setDevice2_result_value(b_d2_result_value.toString());
            item.setSubside_value(b_result_value.toString());
            item.setSubside_range(item.getDevice1_stake_number()+"-" + item.getDevice2_stake_number());
            item.setRange_distance(distance);
        }
        return list;
    }
}
