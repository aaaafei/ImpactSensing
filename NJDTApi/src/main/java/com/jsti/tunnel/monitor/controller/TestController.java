package com.jsti.tunnel.monitor.controller;

import com.alibaba.fastjson.JSON;
import com.jsti.tunnel.monitor.bean.Msg;
import com.jsti.tunnel.monitor.bean.TmCalculateModel;
import com.jsti.tunnel.monitor.bean.TmCalculateValueConfig;
import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.bean.TmHealthHistory;
import com.jsti.tunnel.monitor.bean.TmOriginData;
import com.jsti.tunnel.monitor.enumeration.MonitorType;
import com.jsti.tunnel.monitor.pojo.TmOriginDataParams;
import com.jsti.tunnel.monitor.service.SafetyService;
import com.jsti.tunnel.monitor.service.TmCalculateModelService;
import com.jsti.tunnel.monitor.service.TmCalculateValueConfigService;
import com.jsti.tunnel.monitor.service.TmDeviceService;
import com.jsti.tunnel.monitor.service.TmHealthHistoryService;
import com.jsti.tunnel.monitor.service.TmOriginDataService;
import com.jsti.tunnel.monitor.util.ArrayMinValueUtil;
import com.jsti.tunnel.monitor.util.FileWriterMethodUtil;
import com.jsti.tunnel.monitor.util.TunnelHealthCalculateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件名：TestController
 * .
 */
@Controller
public class TestController {




}
