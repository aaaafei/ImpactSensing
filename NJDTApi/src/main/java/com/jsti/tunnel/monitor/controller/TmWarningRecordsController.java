package com.jsti.tunnel.monitor.controller;

import com.github.pagehelper.PageHelper;
import com.jsti.tunnel.monitor.bean.TmWarningRecords;
import com.jsti.tunnel.monitor.pojo.TmWarningRecordsParams;
import com.jsti.tunnel.monitor.service.TmWarningRecordsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("tmWarningRecords")
public class TmWarningRecordsController  extends BaseController{

    @Autowired
    TmWarningRecordsService tmWarningRecordsService;

    @PostMapping("/getPageList/{curPage}/{pageSize}")
    public String getPageList(@RequestBody TmWarningRecordsParams pojo,@PathVariable Integer curPage, @PathVariable Integer pageSize){
        Map<String,Object> params = new HashMap<>();
        if(pojo.getDeviceid()!=null) {
            params.put("deviceid",pojo.getDeviceid());
        }
        if(pojo.getWarningLevel()!=null) {
            params.put("warning_level",pojo.getWarningValue());
        }
        if(pojo.getTimeRangeBegin()!=null) {
            params.put("timeRangeBegin",pojo.getTimeRangeBegin());
        }
        if(pojo.getTimeRangeEnd()!=null) {
            params.put("timeRangeEnd",pojo.getTimeRangeEnd());
        }
        if(pojo.getSearch()!=null) {
            params.put("search",pojo.getSearch());
        }
        PageHelper.startPage(curPage,pageSize);

        List<TmWarningRecords> list = tmWarningRecordsService.selectDataList(params);
        return super.returnSuccessPageResult(list);
    }

}
