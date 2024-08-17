package com.jsti.tunnel.monitor.controller;

import com.github.pagehelper.PageHelper;
import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.bean.TmSubside;
import com.jsti.tunnel.monitor.pojo.DeviceStatus;
import com.jsti.tunnel.monitor.pojo.TmDeviceParams;
import com.jsti.tunnel.monitor.pojo.TmDeviceStatusParams;
import com.jsti.tunnel.monitor.service.TmDeviceService;
import com.jsti.tunnel.monitor.service.TmSubsideService;
import com.jsti.tunnel.monitor.util.DeviceDataUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tmDevice")
public class TmDeviceController extends BaseController{

    @Autowired
    TmDeviceService tmDeviceService;

    /**
     * method: post
     * url: /tmDevice/getDeviceData
     * description: 获取设备数据
     * @param
     * @return
     */
    @PostMapping(path="/getDeviceDataById/{id}",produces="application/json;charset=UTF-8")
    public String getDeviceDataById(@PathVariable String id){
        TmDeviceParams tmDeviceParams = new TmDeviceParams();
        tmDeviceParams.setId(Integer.parseInt(id));
        List<TmDevice> list = tmDeviceService.selectList(tmDeviceParams);
        if (list == null) {
            return returnResult(0,"Error",null);
        }
        return returnSuccessResult(list.get(0));
    }

    @PostMapping(path="/getDeviceDataByCode/{code}",produces="application/json;charset=UTF-8")
    public String getDeviceDataByCode(@PathVariable String code){
        TmDeviceParams tmDeviceParams = new TmDeviceParams();
        tmDeviceParams.setCode(code);
        List<TmDevice> list = tmDeviceService.selectList(tmDeviceParams);
        if (list == null) {
            return returnResult(0,"Error",null);
        }
        return returnSuccessResult(list.get(0));
    }


    

    @PostMapping(path="/getDevicePageData/{curPage}/{pageSize}",produces="application/json;charset=UTF-8")
    public String getDevicePageData(@RequestBody TmDeviceParams tmDeviceParams, @PathVariable Integer curPage, @PathVariable Integer pageSize){
        PageHelper.startPage(curPage,pageSize);
        List<TmDevice> list = tmDeviceService.selectList(tmDeviceParams);
        return returnSuccessPageResult(list);
    }



    @PostMapping(path="/saveDevice")
    public String saveDevice(@RequestBody TmDevice tmDevice, HttpServletRequest request){
        tmDevice.setCollectTime(null);
        tmDevice.setRealtimePhysicalData(null);
        if (tmDevice.getId() == 0){
            tmDeviceService.insert(tmDevice);
        }else {
            tmDeviceService.updateByPrimaryKey(tmDevice);
        }
        return returnSuccessResult();
    }

    @PostMapping(path="/deleteDevice/{id}/{code}")
    public String deleteDevice(@PathVariable Long id, @PathVariable String code){
        TmDevice tmDevice = tmDeviceService.selectByPrimaryKey(id);
        if (!tmDevice.getCode().equals(code)) {
            returnResult(-1,"删除失败","");
        }
        tmDeviceService.deleteByPrimaryKey(id);
        return returnSuccessResult();
    }



}
