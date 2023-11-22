package com.jsti.tunnel.monitor.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * tm_other_alarm_records
 * @author 
 */
@Data
public class TmOtherAlarmRecords implements Serializable {
    private Long id;

    /**
     * 报警标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否设备故障
     */
    private Integer isDeviceError;

    /**
     * 设备编号
     */
    private String deviceid;

    /**
     * 发现时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reportTime;

    /**
     * 处置措施
     */
    private String disposal;

    private static final long serialVersionUID = 1L;
}