package com.jsti;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.jsti.tunnel.monitor.service.ConstantService;
import com.jsti.tunnel.monitor.service.DepartmentService;
import com.jsti.tunnel.monitor.service.UserService;
import com.jsti.tunnel.monitor.service.impl.ConstantServiceImpl;
import com.jsti.tunnel.monitor.service.impl.DepartmentServiceImpl;
import com.jsti.tunnel.monitor.service.impl.UserServiceImpl;
import com.jsti.tunnel.monitor.util.CacheUtil;

public class SystemInit implements ApplicationListener<ContextRefreshedEvent> {

	ConstantService constantService;
	UserService userService;
	DepartmentService deptService;
 
	//private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SystemInit.class);
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		
		constantService = arg0.getApplicationContext().getBean(ConstantServiceImpl.class);
		//加载常量信息
		CacheUtil.refreshCodeMap(constantService);
		
//		List<Constant> constantList = constantService.getAll();
//		for(Constant c:constantList){
//			CacheUtil.codeMap.put(c.getCode_flag()+"_"+c.getCode_value(), c);
//		}
//		logger.info("加载用户信息成功，数据数："+CacheUtil.userMap.size());
		
		userService = arg0.getApplicationContext().getBean(UserServiceImpl.class);
		//加载用户信息
		CacheUtil.refreshUserMap(userService);
//		List<User> userList = userService.getAll();
//		for(User user:userList){
//			CacheUtil.userMap.put(new Long(user.getId()), user);
//		}
//		logger.info("加载用户信息成功，数据数："+CacheUtil.userMap.size());
		//加载部门信息
		deptService = arg0.getApplicationContext().getBean(DepartmentServiceImpl.class);
		CacheUtil.refreshDeptMap(deptService);
//		List<Organ> deptList = deptService.getAll();
//		for(Organ organ:deptList){
//			CacheUtil.deptMap.put(new Long(organ.getId()), organ);
//		}
//		logger.info("加载部门信息成功，数据数："+CacheUtil.deptMap.size());
		
//		jobService = arg0.getApplicationContext().getBean(JobServiceImpl.class);
//		CacheUtil.refreshJobMap(jobService);
	}
}
