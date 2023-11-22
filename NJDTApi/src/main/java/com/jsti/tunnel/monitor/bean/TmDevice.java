package com.jsti.tunnel.monitor.bean;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tm_device
 * @author 
 */
@Data
public class TmDevice implements Serializable {
    private long id;
    //    设备编码
    private String code;
    //    设备名称
    private String name;
    //    设备型号
    private String model;
    //    监测项目
    private String type;
    //    设备IP
    private String clientip;
    //    设备端口号
    private String clientport;
    //    设备类型
    private String deviceType;
    //    设备安装线路
    private String line;
    //    设备安装区间
    private String segment;
    //    设备安装桥墩编号、里程编号
    private String stakeNumber;
    //    设备具体安装位置
    private String installPosition;

    private String serialNumber;

    private Date collectTime;

    private String subsecond;

    private String realtimePhysicalData;

    private String status;

    private String  pic1;

    private int enabled;
}