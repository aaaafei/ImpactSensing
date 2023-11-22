package com.jsti.tunnel.monitor.controller;

import com.github.pagehelper.PageHelper;
import com.jsti.tunnel.monitor.bean.TmOtherAlarmRecords;
import com.jsti.tunnel.monitor.pojo.TmOtherAlarmRecordsParams;
import com.jsti.tunnel.monitor.service.TmOtherAlarmRecordsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tmOtherAlarmRecords")
public class TmOtherAlarmRecordsController extends BaseController {
    @Autowired
    TmOtherAlarmRecordsService tmOtherAlarmRecordsService;

    @PostMapping("/getPageDataList/{curPage}/{pageSize}")
    public String getPageDataList(@RequestBody TmOtherAlarmRecordsParams pojo, @PathVariable Integer curPage,@PathVariable Integer pageSize){
        Map<String,Object> params = new HashMap<>();
        if(pojo.getTitle()!=null) params.put("title",pojo.getTitle());
        if(pojo.getIsDeviceError()!=null) params.put("is_device_error",pojo.getIsDeviceError());
        if(pojo.getDeviceid()!=null) params.put("deviceid",pojo.getDeviceid());
        if(pojo.getTimeRangeBegin()!=null) params.put("timeRangeBegin",pojo.getTimeRangeBegin());
        if(pojo.getTimeRangeEnd()!=null) params.put("timeRangeEnd",pojo.getTimeRangeEnd());
        PageHelper.startPage(curPage,pageSize);
        List<TmOtherAlarmRecords> list = tmOtherAlarmRecordsService.selectDataList(params);
        return super.returnSuccessPageResult(list,"yyyy-MM-dd HH:mm:ss");
    }

    @PostMapping(path="/saveItem")
    public String saveDevice(@RequestBody TmOtherAlarmRecords tmOtherAlarmRecords, HttpServletRequest request){
        if (tmOtherAlarmRecords.getId()!=null){
        	tmOtherAlarmRecordsService.updateByPrimaryKey(tmOtherAlarmRecords);
        }else {
        	tmOtherAlarmRecordsService.insert(tmOtherAlarmRecords);
        }
        return returnSuccessResult();
    }
    
    @PostMapping(path="/deleteItem")
    public String deleteDevice(HttpServletRequest request){
        Long id = Long.valueOf(request.getParameter("id"));
        tmOtherAlarmRecordsService.deleteByPrimaryKey(id);
        return returnSuccessResult();
    }

}
