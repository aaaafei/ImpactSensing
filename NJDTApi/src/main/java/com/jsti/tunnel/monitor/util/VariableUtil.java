package com.jsti.tunnel.monitor.util;

/**
 * 变量类工具
 */
public class VariableUtil {

    public static String setDefault(String value, String defaultValue) {
        if(value == null || value.equals("") || value.isEmpty()) {
            value = defaultValue;
        }
        return value;
    }
}
