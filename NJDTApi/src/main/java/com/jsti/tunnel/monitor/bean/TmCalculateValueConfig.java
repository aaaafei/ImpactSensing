package com.jsti.tunnel.monitor.bean;

import java.io.Serializable;
import lombok.Data;

/**
 * tm_calculate_value_config
 * @author 
 */
@Data
public class TmCalculateValueConfig implements Serializable {
    /**
     * 计算模型的最底层叶子节点id，数值来源于监测获取的值经过计算获取
     */
    private Integer leaf_id;
    
    
    TmDevice device;
    
    

    /**
     * 设备的父分类id
     */
    private Integer device1_id;

    private Integer device2_id;

    /**
     * 1：常规类型取device1_id指向设备的计算获取的值，device2_id值为空 2：静力水准仪 (device1-device2)/(桩号差*1000)
     */
    private Integer type;

    private static final long serialVersionUID = 1L;
}