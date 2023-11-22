package com.jsti.tunnel.monitor.util;


import java.util.TreeSet;

public class ArrayMinValueUtil {

    public static <T> T getArrayMinValue(T[] arr){
        if(arr!=null && arr.length>0){

            TreeSet<T> ts = new TreeSet<>();
            for (int i = 0; i < arr.length; i++) {
                ts.add(arr[i]);
            }
            return ts.first();
        }
        return null;
    }

}
