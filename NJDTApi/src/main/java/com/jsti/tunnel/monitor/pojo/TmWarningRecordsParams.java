package com.jsti.tunnel.monitor.pojo;

import com.jsti.tunnel.monitor.bean.TmWarningRecords;
import lombok.Data;

import java.util.Date;

@Data
public class TmWarningRecordsParams extends TmWarningRecords {
    private Date timeRangeBegin;
    private Date timeRangeEnd;
    private boolean latestBatch;
}
