package com.jsti.tunnel.monitor.pojo;

import com.jsti.tunnel.monitor.bean.TmOtherAlarmRecords;
import lombok.Data;

import java.util.Date;

@Data
public class TmOtherAlarmRecordsParams extends TmOtherAlarmRecords {
    private Date timeRangeBegin;
    private Date timeRangeEnd;
}
