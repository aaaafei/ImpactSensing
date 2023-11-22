package com.jsti.tunnel.monitor.enumeration;

import java.math.BigDecimal; 

public enum MonitorType {
    L_yingbian(new BigDecimal(300),"断面结构应力"),//+300 -2000
    L_zongshen(new BigDecimal(80537),"断面纵缝伸缩量"),//abs
    L_zongcuo(new BigDecimal(10),"管片纵缝错台"),//abs
    L_niuzhuan(new BigDecimal(1),"管片倾斜与偏转"),//abs
    L_huanshen(new BigDecimal(12),"断面环缝伸缩量"),//abs
    L_huancuo(new BigDecimal(10),"管片环缝错台"),//abs
    L_duanjian(new BigDecimal(12),"断面间环缝伸缩量"),//abs
    L_chenjiang((new BigDecimal(1)).divide(new BigDecimal(500),6,BigDecimal.ROUND_HALF_UP),"纵向不均匀沉降");//abs


    private BigDecimal value;
    private String description;

    MonitorType(BigDecimal v,String d){
        this.value = v;
        this.description = d;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
    
     
   

}
