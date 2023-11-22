package com.jsti.tunnel.monitor.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsti.tunnel.monitor.bean.TmOriginData;

import lombok.Data;

import java.util.Date;

@Data
public class TmOriginDataParams extends TmOriginData {
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date beginDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date endDate;
	boolean needDataFilter = true;
}
