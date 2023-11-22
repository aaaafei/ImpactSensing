package com.jsti.tunnel.monitor.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public final static String PERIOD_DAY = "天";
    public final static String PERIOD_WEEK = "周";
    public final static String PERIOD_HALFMONTH = "半月";
    public final static String PERIOD_MONTH = "月";
    public final static String PERIOD_QUARTER = "季度";
	/**
	 * string date parse Date
	 * @param strDate 
	 * @return Date (Exception null)
	 */
	public static Date strParseDate(String strDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date strParseDate(String strDate,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String DateFormatStr(Date date , String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try{
			return sdf.format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "" ;
	}
	
	public static String DateFormatStr(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			return sdf.format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "" ;
	}

    /**
             * 根据起始时间和周期计算结束时间
     */
    public static Date getNextPeriodTime(Date start_time,String period){
        Calendar cal = Calendar.getInstance();
        cal.setTime(start_time);
        if(period.equals(PERIOD_DAY)){
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
        if(period.equals(PERIOD_WEEK)){
            cal.add(Calendar.WEEK_OF_YEAR,1);
        }
        if(period.equals(PERIOD_HALFMONTH)){
            // NOTE 计算当前月中值，判断当前日期是否处于月中值5天附近，若在，则结束日为月底（下个月的1号），
            //      若不在，则结束日为月中值
            // 当前startdate所在月的总天数
            int daysOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            // 当前startdate所在月的中间值
            int middleOfMonth = 16;
            // startdate的天数值
            int startdateDayValue = cal.get(Calendar.DAY_OF_MONTH);
            if(startdateDayValue < (middleOfMonth-5)){
                cal.add(Calendar.DAY_OF_MONTH,middleOfMonth-startdateDayValue);
            }else if(startdateDayValue > (middleOfMonth+5)){
                cal.add(Calendar.DAY_OF_MONTH,daysOfMonth-startdateDayValue+middleOfMonth);
            }else{
                cal.add(Calendar.DAY_OF_MONTH,daysOfMonth-startdateDayValue+1);
            }
        }
        if(period.equals(PERIOD_MONTH)){
            cal.add(Calendar.MONTH,1);
        }
        if(PERIOD_QUARTER.equals(period)) {
        	//季度
        	 cal.add(Calendar.MONTH,3);
        }
        
        return cal.getTime();
    }
    
    public static Date getDateZero(Date datetime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(datetime);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        return cal.getTime();
    }
    
    public static Date getLatestBatchDatetimeFrontward(Date datetime) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(datetime);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    	if (cal.get(Calendar.MINUTE) >= 50 && cal.get(Calendar.MINUTE) <= 59) {
    		cal.add(Calendar.MINUTE, 60-cal.get(Calendar.MINUTE));
    	}else if (cal.get(Calendar.MINUTE) >= 0 && cal.get(Calendar.MINUTE) <= 10){
    		cal.set(Calendar.MINUTE, 0);
    	}else if (cal.get(Calendar.MINUTE) >= 20 && cal.get(Calendar.MINUTE) <= 40){
    		cal.set(Calendar.MINUTE, 30);
    	}
    	return cal.getTime();
    }

    public static void main(String args[]) {
        System.out.println(getLatestBatchDatetimeFrontward(new Date()));
    }
}
