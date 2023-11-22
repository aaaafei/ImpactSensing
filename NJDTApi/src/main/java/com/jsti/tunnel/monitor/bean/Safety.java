package com.jsti.tunnel.monitor.bean;

import lombok.Data;

import java.util.Date;

@Data
public class Safety {
    private long id;
    private float safety_value;
    private String safety_label;
    private long segment_id;
    private String segment_name;
    private float device_intact_rate;
    private String line;
    private Date collect_time;
}
