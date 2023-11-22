package com.jsti.tunnel.monitor.util;

import com.jsti.tunnel.monitor.pojo.WeightValue;

import java.math.BigDecimal;

public class WeightAverageUtil {

    public static BigDecimal weightAverage(WeightValue[] weightValues){
        BigDecimal v = new BigDecimal(0);
        for (WeightValue weightValue : weightValues) {
            v.add(weightValue.getValue().multiply(weightValue.getWeight()));
        }
        return v;
    }

}
