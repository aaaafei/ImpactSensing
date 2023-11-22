package com.jsti.tunnel.monitor.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsti.system.err.BusinessException;
import com.jsti.tunnel.monitor.bean.Constant;
import com.jsti.tunnel.monitor.service.ConstantService;
import com.jsti.tunnel.monitor.util.CacheUtil;

/**
 * 文件名：TestController
 * .
 */
@RestController
@RequestMapping("/constant")
public class ConstantController extends BaseController {

    @Autowired
    private ConstantService constantService;

   
    /**
     * 添加常量
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/add")
    public String add(@Valid @RequestBody Constant constant){
    	if(constant.getValid()==1) constant.setValid(1);
    	constantService.insert(constant);
    	CacheUtil.refreshCodeMap(constantService);
    	return super.returnSuccessResult("添加成功");
    }
    
    /**
     * 修改常量
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/update")
    public String update(@RequestBody Constant constant){
    	if(constant.getId()==null){
    		throw new BusinessException(20002);
    	}
    	constantService.update(constant);
    	CacheUtil.refreshCodeMap(constantService);
    	return super.returnSuccessResult("修改成功");
    }
    
    /**
     * 删除常量
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/delete")
    public String delete(@RequestBody Constant constant){
    	if(constant.getId()==null){
    		throw new BusinessException(20002);
    	}
    	constantService.delete(constant.getId());
    	CacheUtil.refreshCodeMap(constantService);
    	return super.returnSuccessResult("删除成功");
    }
    
    
    /**
     * 通过常量标识获取某一类型常量
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/getConstantByFlag")
    public String getConstantByFlag(@RequestBody Constant constant){
    	if(StringUtils.isEmpty(constant.getCode_flag())){
    		throw new BusinessException(20101);
    	}
    	List<Constant> constantList = constantService.getByFlag(constant.getCode_flag());
    	return super.returnSuccessResult(constantList);
    }
    
    /**
     * 通过常量标识获取某一类型常量
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/getConstantByFlag/{flag}")
    public String getConstantByFlag(@PathVariable String flag){
    	if(StringUtils.isEmpty(flag)){
    		throw new BusinessException(20101);
    	}
    	List<Constant> constantList = constantService.getByFlag(flag);
    	return super.returnSuccessResult(constantList);
    }
    
    /**
     * 通过常量标识和字典值获取常量
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/getConstantByFlagAndValue")
    public String getConstantByFlagAndValue(@RequestBody Constant constant){
    	if(StringUtils.isEmpty(constant.getCode_flag())){
    		throw new BusinessException(20101);
    	}
    	if(constant.getCode_value()==null){
    		throw new BusinessException(20102);
    	}
    	Constant con = constantService.getByFlagAndValue(constant);
    	return super.returnSuccessResult(con);
    }
    
    /**
     * 通过名称模糊查询
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/getConstantByName/{page}/{size}")
    public String getConstantByName(@PathVariable Integer page,@PathVariable Integer size, @RequestBody Constant constant){
    	PageHelper.startPage(page, size); 
    	List<Constant> constantList;
    	if(StringUtils.isEmpty(constant.getFlag_name())){
//    		throw new BusinessException(20103);
    		constantList = constantService.getAll();
    	}else{
    		constantList = constantService.getByName(constant.getFlag_name());
    	}
    	return super.returnSuccessResult(new PageInfo(constantList));
    }
}
