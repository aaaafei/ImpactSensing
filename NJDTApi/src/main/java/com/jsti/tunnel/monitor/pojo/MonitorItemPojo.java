package com.jsti.tunnel.monitor.pojo;

import lombok.Data;

@Data
public class MonitorItemPojo {

    public static final String DMHFSSL = "断面环缝伸缩量";
    public static final String DMJHFSSL = "断面间环缝伸缩量";
    public static final String DMZFSSL = "断面纵缝伸缩量";
    public static final String GPCT = "管片错台";
    public static final String DMJGYL = "断面结构应力";
    public static final String ZXBJYCJ = "纵向不均匀沉降";
    public static final String GPQXPZ = "管片倾斜与偏转";
    public static final String DZDXY = "地震动响应";

    private String code;
    private String name;
}
