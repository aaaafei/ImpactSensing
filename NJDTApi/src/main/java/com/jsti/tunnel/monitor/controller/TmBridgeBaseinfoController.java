package com.jsti.tunnel.monitor.controller;

import com.github.pagehelper.PageHelper;
import com.jsti.tunnel.monitor.bean.TmBridgeBaseinfo;
import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.pojo.TmDeviceParams;
import com.jsti.tunnel.monitor.service.TmBridgeBaseinfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/tmbridgebaseinfo")
public class TmBridgeBaseinfoController extends BaseController {

    @Autowired
    private TmBridgeBaseinfoService tmBridgeBaseinfoService;

    private final static Logger logger = LoggerFactory.getLogger(TmBridgeBaseinfoController.class);

    @PostMapping(path="/getPageData/{curPage}/{pageSize}",produces="application/json;charset=UTF-8")
    public String getPageData(@RequestBody TmBridgeBaseinfo tmBridgeBaseinfoParams, @PathVariable Integer curPage, @PathVariable Integer pageSize) {
        PageHelper.startPage(curPage,pageSize, "sortno");
        List<TmBridgeBaseinfo> list = tmBridgeBaseinfoService.selectList(tmBridgeBaseinfoParams);
        return returnSuccessPageResult(list);
    }

    @PostMapping(path="/getTmBridgeBaseinfo/{uuid}",produces="application/json;charset=UTF-8")
    public String getTmBridgeBaseinfo(@PathVariable String uuid){
        TmBridgeBaseinfo item = tmBridgeBaseinfoService.selectByUuid(uuid);
        return returnSuccessResult(item);
    }

    @PostMapping(path="/saveTmBridgeBaseinfo")
    public String saveTmBridgeBaseinfo(@RequestBody TmBridgeBaseinfo tmBridgeBaseinfo, HttpServletRequest request){
        tmBridgeBaseinfoService.save(tmBridgeBaseinfo);
        return returnSuccessResult();
    }

    @PostMapping(path="/deleteTmBridgeBaseinfo/{uuid}")
    public String deleteTmBridgeBaseinfo(@PathVariable String uuid, HttpServletRequest request){
        tmBridgeBaseinfoService.delete(uuid);
        return returnSuccessResult();
    }
}
