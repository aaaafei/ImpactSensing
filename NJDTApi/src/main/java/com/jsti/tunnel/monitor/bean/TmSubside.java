package com.jsti.tunnel.monitor.bean;

import lombok.Data;

import java.util.Date;

@Data
public class TmSubside {

    private int id;
    private String subside_range;
    private String device1_code;
    private String device1_result_value;
    private String device1_realtime_physical_data;
    private String device1_initial_value;
    private String device1_stake_number;
    private String device2_code;
    private String device2_result_value;
    private String device2_stake_number;
    private String device2_realtime_physical_data;
    private String device2_initial_value;
    private int range_distance;
    private String subside_value;
    private String subside_value_unit_label;
    private Date collect_time;
}

