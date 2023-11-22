package com.jsti.system.druidTools;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
/**
 * 文件名：DruidStatViewServlet
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/druid/*",
        initParams={
                @WebInitParam(name="allow",value=""),// IP白名单 (没有配置或者为空，则允许所有访问)
                @WebInitParam(name="deny",value="10.60.16.110"),// IP黑名单 (存在共同时，deny优先于allow)
                @WebInitParam(name="loginUsername",value="xh"),// 用户名
                @WebInitParam(name="loginPassword",value="123456"),// 密码
                @WebInitParam(name="resetEnable",value="false")// 禁用HTML页面上的“Reset All”功能
        })
public class DruidStatViewServlet extends StatViewServlet {

}