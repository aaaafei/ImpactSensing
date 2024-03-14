package com.jsti.tunnel.monitor.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TmDispositionProcess {

    private Long id;

    private Long alarmRecordId;

    private String alarmType;

    private String node;

    private Integer userid;

    private String username;

    private String result;

    private String comment;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
}
