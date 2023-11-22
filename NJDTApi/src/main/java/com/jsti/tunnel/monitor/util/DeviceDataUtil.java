package com.jsti.tunnel.monitor.util;

import java.math.BigDecimal;

public class DeviceDataUtil {

    public static String transformData(String data){
        String l = "149e-6";
        BigDecimal b_l = new BigDecimal(l);
        BigDecimal b_data = new BigDecimal(data);
        BigDecimal b_y = b_l.multiply(b_data);
        b_y = b_y.setScale(5,BigDecimal.ROUND_UP);
        String str = b_y.toString();
        if(str.length()>15){
            str = str.substring(0,15);
        }
        return str;
    }

    /**
     * LK3+765,桩号转为数字
     * @param stakeNumber
     * @return
     */
    public static int getMilesFromStakeNumber(String stakeNumber) {
        String number1 = stakeNumber.split("\\+")[0];
        number1 = number1.substring(2);
        String number2 = stakeNumber.split("\\+")[1];
        number2 = String.format("%03d", Integer.parseInt(number2));
        return Integer.parseInt(number1+number2);
    }
    
    /**
     * 	数据滤波
     * @param data
     * @param deviceCode
     * @return
     */
    public static BigDecimal filterPhisicalData(BigDecimal data, String deviceCode){
    	if(deviceCode.startsWith("DM")){
    		BigDecimal max = new BigDecimal(25*1.25);
    		BigDecimal min = new BigDecimal(-25*1.25);
    		if (data.compareTo(max)>0){
    			return null;
    		}
    		if (data.compareTo(min)<0){
    			return null;
    		}
    	}
    	if(deviceCode.startsWith("SG")){
    		BigDecimal max = new BigDecimal(3000);
    		BigDecimal min = new BigDecimal(-3000);
    		if (data.compareTo(max)>0){
    			return null;
    		}
    		if (data.compareTo(min)<0){
    			return null;
    		}
    	}
    	if(deviceCode.startsWith("SL")){
    		BigDecimal max = new BigDecimal(600);
    		BigDecimal min = new BigDecimal(-600);
    		if (data.compareTo(max)>0){
    			return null;
    		}
    		if (data.compareTo(min)<0){
    			return null;
    		}
    	}
    	return data;
    }

    public static void main(String args[]) {
        BigDecimal data = new BigDecimal("23");
        BigDecimal rst = filterPhisicalData(data, "DM-HM/PJ/L/001");
        System.out.println(rst);
    }
}
