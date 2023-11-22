package com.jsti.tunnel.monitor.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件名：TestController
 * .
 */
@RestController
@RequestMapping("/comm")
public class CommonController extends BaseController{

	 @RequestMapping(value = "/getTime")
    public String getTime(Model model, HttpServletRequest request){
        Date now = new Date();
        return super.returnSuccessResult("添加成功",now,"yyyy-MM-dd HH:mm:ss");
    }

}
