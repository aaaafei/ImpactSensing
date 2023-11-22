package com.jsti.tunnel.monitor.util;

import java.util.List;

import com.jsti.tunnel.monitor.bean.Constant;

public class CodeUtil {
	
	public static Constant getCodeByFlagAndValue(String flag, String value){
		if(flag == null) return null;
		String key = flag+"_"+value;
		return  CacheUtil.codeMap.get(key);
	}
	
	public static List<Constant> getCodeByFlag(String flag){
		if(flag == null) return null;
		String key = flag;
		return  CacheUtil.codeFlagMap.get(key);
	}
	
	public static String getCodeNameByFlagAndValue(String flag, String value){
		if(flag == null) return null;
		String key = flag+"_"+value;
		Constant c =  CacheUtil.codeMap.get(key);
		if(c!=null) return c.getCode_name();
		return null;
	}
}
