package com.jsti.tunnel.monitor.pojo;

import com.jsti.tunnel.monitor.bean.TmOriginData;
import lombok.Data;

import java.util.Date;

@Data
public class TmOriginDataPojo extends TmOriginData {
    Date beginDate;
    Date endDate;
}
