package com.jsti.tunnel.monitor.bean;

import lombok.Data;

import java.util.List;

/**
 * 文件名：Job 版本信息：日期：2017/4/18 .
 */
@Data

public class Menu {
	// 编号
	private Integer id;
	// 名称
	private String menuname;
	// 名称
	private String label;
	// 地址
	private String url;
	// 父id
	private Integer parentId;
	// 图标类名称
	private String iconClass;
	// 菜单类型 10—UI 20—Api
	private Integer type;
	// 排序编号
	private Integer sortNo;
	
	private Integer status;

	private String remark;

	private List<Menu> children;
}
