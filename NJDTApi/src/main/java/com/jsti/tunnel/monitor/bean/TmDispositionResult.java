package com.jsti.tunnel.monitor.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TmDispositionResult {

    private Long id;

    private Long alarmRecordId;

    private String alarmType;

    private Integer orgid;

    private String orgname;

    private Integer teamLeaderUserid;

    private String teamLeaderUsername;

    private Integer disposePriciple;

    private String disposePricipleUsername;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date dispatchDatetime;

    private String pics;

    private String disposalResult;

    private Integer eliminationApplyUserid;

    private String eliminationApplyUsername;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date applyDatetime;
}
