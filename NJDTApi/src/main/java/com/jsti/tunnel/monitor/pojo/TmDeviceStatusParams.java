package com.jsti.tunnel.monitor.pojo;

import java.util.List;

import lombok.Data;

@Data
public class TmDeviceStatusParams {

	private String line;
	
	private String status;
	
	private List<String> statusList;
}
