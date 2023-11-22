package com.jsti.tunnel.monitor.pojo;

import com.jsti.tunnel.monitor.bean.TmCalculateModel;

import lombok.Data; 

@Data
public class DeviceNode extends TmCalculateModel { 
    private Integer device1_id; 
    private Integer device2_id; 
    private Integer device_type;
}
