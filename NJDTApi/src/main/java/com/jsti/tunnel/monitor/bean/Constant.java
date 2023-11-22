package com.jsti.tunnel.monitor.bean;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * 
 * 常量（字典）实体类
 * Constant
 * 
 * tiger
 * tiger
 * 2017年5月4日 上午9:55:12
 * 
 * @version 1.0.0
 *
 */
@Data
public class Constant{
	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -7765958097423155178L;
	private Long id;
	@NotNull(message="字典值不能为空")
	private Integer code_value;
	@NotEmpty(message="字典名称不能为空")
	private String code_name;
	@NotEmpty(message="字典标识不能为空")
	private String code_flag;
	@NotEmpty(message="标识名称不能为空")
	private String flag_name;
	private int valid;
	private String remark;
	private int sort_no;
	
	Constant(){}
	public Constant(String code_flag, int code_value){
		this.setCode_value(code_value);
		this.setCode_flag(code_flag);
	}
}
