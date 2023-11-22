package com.jsti.tunnel.monitor.bean;

import java.util.List;

import lombok.Data;

/**
 * 文件名：Permission 版本信息：日期：2017/3/31 .
 */
@Data
public class Permission {
	private Integer id;
	// 权限名称
	private String name;
	// 权限名称
	private String label;
	// 权限描述
	private String description;
	// 父节点id
	private Integer pid;
	// 父节点名称
	private String pName;
	// 资源id
	private Integer resourceid;
	// 资源名称
	private String resourceName;
	// 类型 1-菜单
	private Integer type;
	// 0 正常 -1 删除
	private Integer status;
	//1-读写 2-读 3-删除 
	private Integer permission_type;

	private List<Permission> childs;
	
	private List<Permission> children;
}
