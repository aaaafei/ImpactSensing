package com.jsti.tunnel.monitor.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.jsti.system.err.BusinessException;
import com.jsti.system.err.ErrorCode;
import com.jsti.system.securityTools.JwtTokenUtil;
import com.jsti.system.securityTools.RedisCacheUtil;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.util.CacheUtil;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名：TestController
 * .
 */
@ControllerAdvice
public class BaseController {

	protected final String moduleName = this.getClass().getSimpleName().replace("Controller","");
	
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private RedisCacheUtil redisCacheUtil;
	@Value("${jwt.header}")
	private String tokenHeader;
	@Value("${jwt.tokenHead}")
	private String tokenHead;
    /**
     * 返回前台结果结构体
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    public String returnResult(int result_code,String result_desc, Object o){
    	Map<String,Object> result = new HashMap<>();
    	result.put("result_code", result_code);
    	result.put("result_desc", result_desc);
    	result.put("result_data", o);
        return JSON.toJSON(result).toString();
    }
    
    /**
     * 返回前台结果结构体
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    public String returnSuccessResult(String result_desc){
    	Map<String,Object> result = new HashMap<>();
    	result.put("result_code", 0);
    	result.put("result_desc", result_desc);
        return JSON.toJSON(result).toString();
    }
    
    /**
     * 返回前台结果结构体
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    public String returnSuccessResult(){
    	Map<String,Object> result = new HashMap<>();
    	result.put("result_code", 0);
    	result.put("result_desc", "success");
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm");
    }
    
    /**
     * 返回前台结果结构体
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    public String returnSuccessResult(Object o){
        return returnSuccessResult(o,"yyyy-MM-dd HH:mm");
    }

	public String returnSuccessResult(Object o, String dateFormat){
		Map<String,Object> result = new HashMap<>();
		result.put("result_code", 0);
		result.put("result_desc", "success");
		result.put("result_data", o);
		return JSON.toJSONStringWithDateFormat(result,dateFormat);
	}

    /**
     * 返回前台结果结构体
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    public String returnSuccessResultTime(Object o){
    	Map<String,Object> result = new HashMap<>();
    	result.put("result_code", 0);
    	result.put("result_desc", "success");
    	result.put("result_data", o);
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm:00");
    }
    
    /**
     * 返回前台结果分页结构体
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    public String returnSuccessPageResult(Object o){
        return returnSuccessPageResult(o,"yyyy-MM-dd HH:mm");
    }

	public String returnSuccessPageResult(Object o,String dateFormat){
		Map<String,Object> result = new HashMap<>();
		result.put("result_code", 0);
		result.put("result_desc", "success");
		result.put("result_data", new PageInfo((List)o));
		return JSON.toJSONStringWithDateFormat(result,dateFormat);
	}


    
    /**
     * 返回前台结果结构体
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    public String returnSuccessResult(String result_desc, Object o){
    	Map<String,Object> result = new HashMap<>();
    	result.put("result_code", 0);
    	result.put("result_desc", result_desc);
    	result.put("result_data", o);
        return JSON.toJSONStringWithDateFormat(result,"yyyy-MM-dd HH:mm");
    }
    
    /**
     * 返回前台结果结构体
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    public String returnSuccessResult(String result_desc, Object o, String dateFormat){
    	Map<String,Object> result = new HashMap<>();
    	result.put("result_code", 0);
    	result.put("result_desc", result_desc);
    	result.put("result_data", o);
        return JSON.toJSONStringWithDateFormat(result,dateFormat);
    }
    /** 基于@ExceptionHandler异常处理 */  
    @ExceptionHandler  
    public String exp(HttpServletRequest request, Exception ex) {  
        // 根据不同错误提示不同的错误  
    	ErrorCode code;
        if(ex instanceof NullPointerException) {  
        	code = new ErrorCode(10001);
        } else if (ex instanceof NumberFormatException) {  
        	code = new ErrorCode(10002);
        } else if (ex instanceof IndexOutOfBoundsException) {  
        	code = new ErrorCode(10003);  
        } else if (ex instanceof ArithmeticException) {  
        	code = new ErrorCode(10004);  
        } else if (ex instanceof FileNotFoundException) {  
        	code = new ErrorCode(10005);  
        } else if (ex instanceof IllegalArgumentException) {  
        	code = new ErrorCode(10006); 
        } else if (ex instanceof HttpMessageNotReadableException) {
        	code = new ErrorCode(10007); 
        } else if (ex instanceof BusinessException) {
        	code = ((BusinessException) ex).getErrCode();
        } else if(ex.getCause() instanceof TooManyResultsException){
        	code = new ErrorCode(10008);
        } else if (ex instanceof MethodArgumentNotValidException) {
        	BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
        	code = new ErrorCode(20001,getValidatorErrors(bindingResult)); 
        }
        else if(ex instanceof BadCredentialsException){
        		code = new ErrorCode(10009,ex.getMessage());
		} else{
        	code = new ErrorCode(11000);  
        }
        logger.error(code.getDesc(), ex);
        return returnResult(code.getCode(),code.getDesc(),null);
    }
    
    public static String getValidatorErrors(BindingResult bindingResult){
    	StringBuffer sb = new StringBuffer();
    	for(ObjectError err:bindingResult.getAllErrors()){
   		 	sb.append("["+err.getDefaultMessage()+"]");
       	}
       	return sb.toString();
    }
    
    public User getCurrentUser(HttpServletRequest request){
    	String authHeader = request.getHeader(this.tokenHeader);
    	String authToken = authHeader.substring(tokenHead.length());
    	String username = jwtTokenUtil.getUsernameFromToken(authToken);
		if (username != null) {
			UserDetails u = redisCacheUtil.getUserByUserName(username);
			User u_t = JSON.parseObject(JSON.toJSONString(u),User.class) ;
			User user = CacheUtil.userMap.get(new Long(u_t.getId()));
			user.setRoles(u_t.getRoles());
			return user;
		}
		return null;
    }
}
