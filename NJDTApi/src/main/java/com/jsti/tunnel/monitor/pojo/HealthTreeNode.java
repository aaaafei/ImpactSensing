package com.jsti.tunnel.monitor.pojo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class HealthTreeNode {
	String name;
	Map<String, String> itemStyle;
	BigDecimal value;
	List<HealthTreeNode> children;
}
