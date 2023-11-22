package com.jsti.tunnel.monitor.pojo;

import java.util.Date;
  

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DevicesHealthyParams{
	String deviceCodes;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date beginDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date endDate;
}
