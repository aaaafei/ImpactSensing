package com.jsti.tunnel.monitor.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.jsti.tunnel.monitor.bean.Constant;
import com.jsti.tunnel.monitor.bean.Organ;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.service.ConstantService;
import com.jsti.tunnel.monitor.service.DepartmentService;
import com.jsti.tunnel.monitor.service.UserService;

public class CacheUtil {

    public static Map<String,String> commonDataMap = new HashMap<String,String>();

	public static Map<Long,User> userMap = new HashMap<Long,User>();
	
	public static Map<Long,Organ> deptMap = new HashMap<Long,Organ>();
	
	public static Map<String,Constant> codeMap = new HashMap<String,Constant>();
	
	public static Map<String,List<Constant>> codeFlagMap = new HashMap<String,List<Constant>>();
	 
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CacheUtil.class);
	
	public static void refreshCodeMap(ConstantService constantService){
		//加载用户信息
		List<Constant> constantList = constantService.getAll();
		for(Constant c:constantList){
			CacheUtil.codeMap.put(c.getCode_flag()+"_"+c.getCode_value(), c);
			List<Constant> list = CacheUtil.codeFlagMap.get(c.getCode_flag());
			if(list==null) list = new ArrayList<Constant>();
			list.add(c);
			CacheUtil.codeFlagMap.put(c.getCode_flag(),list);
		}
		logger.info("加载字典信息成功，数据数："+CacheUtil.codeFlagMap.size());
	}
	
	public static void refreshUserMap(UserService userService){
		//加载用户信息
		List<User> userList = userService.getAll();
		for(User user:userList){
			CacheUtil.userMap.put(new Long(user.getId()), user);
		}
		logger.info("加载用户信息成功，数据数："+CacheUtil.userMap.size());
	}
	
	public static void refreshDeptMap(DepartmentService deptService){
		//加载部门信息
		List<Organ> deptList = deptService.getAll();
		for(Organ organ:deptList){
			CacheUtil.deptMap.put(new Long(organ.getId()), organ);
		}
		logger.info("加载部门信息成功，数据数："+CacheUtil.deptMap.size());
	}
	
 
	
	public static User getUserFromMap(Long key){
		if(key == null) return null;
		return  CacheUtil.userMap.get(key);
	}
	
	public static String getUserTurenameFromMap(Long key){
		if(getUserFromMap(key)!=null && key!=null){
			return getUserFromMap(key).getTruename();
		}
		return null;
	}
	
	public static Organ getOrganFromMap(Long key){
		if(key == null) return null;
		return  CacheUtil.deptMap.get(key);
	}
	
	public static String getOrgannameFromMap(Long key){
		if(getOrganFromMap(key)!=null){
			return getOrganFromMap(key).getOrganname();
		}
		return null;
	}
	
 
}
