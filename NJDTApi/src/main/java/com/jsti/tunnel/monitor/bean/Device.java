package com.jsti.tunnel.monitor.bean;

import lombok.Data;

@Data
public class Device {
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
    private String device_type;
    //    设备安装线路
    private String line;
    //    设备安装区间
    private String segment;
    //    设备安装桥墩编号、里程编号
    private String stake_number;
    //    设备具体安装位置
    private String install_position;
    private String serial_number;
    private int enabled;

}
