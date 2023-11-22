package com.jsti.tunnel.monitor.bean;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class TmHealthHistory {
	private Integer model_id;
	private BigDecimal health_value;
	private Date calculate_time;
}
