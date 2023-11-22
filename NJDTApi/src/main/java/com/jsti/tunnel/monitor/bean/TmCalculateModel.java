package com.jsti.tunnel.monitor.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * tm_calculate_model
 * @author
 */
@Data
public class TmCalculateModel implements Serializable {
    private Integer id;

    /**
     * 隧道左线L或右线R
     */
    private String line;

    /**
     * 类型：G、C、P、S、N、M
     */
    private String type;

    /**
     * 编号
     */
    private String no;

    private String description;

    /**
     * 权重，存为字符串拿到BigDecimal中计算
     */
    private String weightiness;


    private Integer parent_category_id;

    /**
     * 值为0下面关联device，值为1下面还是关联tree category
     */
    private Boolean is_leaf;
    
    private BigDecimal health_value;

    private static final long serialVersionUID = 1L;
}
