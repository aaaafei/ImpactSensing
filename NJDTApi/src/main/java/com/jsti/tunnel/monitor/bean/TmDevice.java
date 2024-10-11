package com.jsti.tunnel.monitor.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date collectTime;

    private String subsecond;

    private String realtimePhysicalData;

    private String voltage;

    private String status;

    private String longitude;

    private String latitude;

    private String  pic1;

    private Integer enabled;
}