package com.jsti.system.err;

import java.util.ResourceBundle;

public class ErrorCodeEnum {
	
	private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("errcode");
    
//	private static Map<String,String> errorCodeMap = new HashMap<String,String>();
//	
//	static{
//		setAllErrorCode();
//	}
	/**
     * 获取属性值
     * 
     * @return
     */
    public static String getPropByKey(String key)
    {
    	resourceBundle.keySet();
        return resourceBundle.getString(key);
    }
    
//    public static Map<String,String> setAllErrorCode()
//    {
//    	Set<String> keySet= resourceBundle.keySet();
//    	for(String key:keySet){
//    		errorCodeMap.put(key, getPropByKey(key));
//    	}
//    	return errorCodeMap;
//    }
    
}
