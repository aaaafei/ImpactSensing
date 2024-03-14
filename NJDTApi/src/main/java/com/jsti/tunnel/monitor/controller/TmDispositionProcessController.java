package com.jsti.tunnel.monitor.controller;

import com.github.pagehelper.PageHelper;
import com.jsti.tunnel.monitor.bean.TmDispositionProcess;
import com.jsti.tunnel.monitor.bean.TmDispositionResult;
import com.jsti.tunnel.monitor.bean.TmWarningRecords;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.service.TmDispositionProcessService;
import com.jsti.tunnel.monitor.service.TmDispositionResultService;
import com.jsti.tunnel.monitor.service.TmWarningRecordsService;
import com.jsti.tunnel.monitor.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/tmdispositionprocess")
public class TmDispositionProcessController extends BaseController {

    @Autowired
    private TmDispositionProcessService tmDispositionProcessService;

    @Autowired
    private TmWarningRecordsService tmWarningRecordsService;

    @Autowired
    private UserService userService;

    private final static Logger logger = LoggerFactory.getLogger(TmDispositionProcessController.class);

    @PostMapping(path="/getPageData/{curPage}/{pageSize}",produces="application/json;charset=UTF-8")
    public String getPageData(@RequestBody TmDispositionProcess tmDispositionProcess, @PathVariable Integer curPage, @PathVariable Integer pageSize) {
        PageHelper.startPage(curPage,pageSize, "create_time desc");
        List<TmDispositionProcess> list = tmDispositionProcessService.selectList(tmDispositionProcess);
        return returnSuccessPageResult(list);
    }

    @PostMapping(path="/approve",produces="application/json;charset=UTF-8")
    public String approve(@RequestBody TmDispositionProcess tmDispositionProcess, HttpServletRequest request){
        User user = userService.getOne(super.getCurrentUser(request).getId());
        tmDispositionProcess.setResult("1");
        tmDispositionProcess.setCreateTime(new Date());
        tmDispositionProcess.setUserid(user.getId());
        tmDispositionProcess.setUsername(user.getTruename());
        tmDispositionProcessService.save(tmDispositionProcess);
        TmWarningRecords tmWarningRecords = tmWarningRecordsService.selectByPrimaryKey(tmDispositionProcess.getAlarmRecordId());
        tmWarningRecords.setStatus("99");
        tmWarningRecordsService.updateByPrimaryKey(tmWarningRecords);
        return returnSuccessResult();
    }

    @PostMapping(path="/refuse",produces="application/json;charset=UTF-8")
    public String refuse(@RequestBody TmDispositionProcess tmDispositionProcess, HttpServletRequest request){
        User user = userService.getOne(super.getCurrentUser(request).getId());
        tmDispositionProcess.setResult("0");
        tmDispositionProcess.setCreateTime(new Date());
        tmDispositionProcess.setUserid(user.getId());
        tmDispositionProcess.setUsername(user.getTruename());
        tmDispositionProcessService.save(tmDispositionProcess);
        return returnSuccessResult();
    }


}
