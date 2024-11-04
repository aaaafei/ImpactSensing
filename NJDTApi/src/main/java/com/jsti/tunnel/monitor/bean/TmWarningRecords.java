package com.jsti.tunnel.monitor.bean;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tm_warning_records
 * @author 
 */
@Data
public class TmWarningRecords implements Serializable {
    private Long id;

    private String deviceid;

    private String deviceCode;
    
    private String val;

    private String warningValue;

    private Date collectTime;

    private String warningLevel;

    private String remark;

    private static final long serialVersionUID = 1L;

    private String monitorType;

    private String segment;

    private String line;

    private String stakeNumber;

    private String installPosition;

    private String status;

    private Integer first;
}