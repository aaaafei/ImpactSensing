package com.jsti.tunnel.monitor.controller;

import com.jsti.tunnel.monitor.bean.Safety;
import com.jsti.tunnel.monitor.service.SafetyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/indexHome")
public class IndexHomeController extends BaseController{

    private final static Logger logger = LoggerFactory.getLogger(IndexHomeController.class);

    @Autowired
    SafetyService safetyService;

    @RequestMapping(value = "/getRealtimeData", produces = "application/json;charset=UTF-8")
    public String getRealtimeData(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        List<Safety> list = safetyService.getRealTimeData(param);
        List<Map> rtnList = new ArrayList<>();
        // segment, 保持原来的顺序输出
        Map<String, List<Safety>> map1 = list.stream().collect(Collectors.groupingBy(Safety::getSegment_name, LinkedHashMap::new, Collectors.toCollection(ArrayList::new)));
        for (String segmentName: map1.keySet()) {

        }

        return super.returnSuccessResult(list);
    }
}
