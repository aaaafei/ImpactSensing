package com.jsti.tunnel.monitor.controller;

import com.github.pagehelper.PageHelper;
import com.jsti.tunnel.monitor.bean.TmBridgeBaseinfo;
import com.jsti.tunnel.monitor.bean.TmDispositionResult;
import com.jsti.tunnel.monitor.bean.TmWarningRecords;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.service.TmBridgeBaseinfoService;
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
@RequestMapping("/tmdispositionresult")
public class TmDispositionResultController extends BaseController {

    @Autowired
    private TmDispositionResultService tmDispositionResultService;

    @Autowired
    private TmWarningRecordsService tmWarningRecordsService;

    @Autowired
    private UserService userService;

    private final static Logger logger = LoggerFactory.getLogger(TmDispositionResultController.class);

    @PostMapping(path="/getPageData/{curPage}/{pageSize}",produces="application/json;charset=UTF-8")
    public String getPageData(@RequestBody TmDispositionResult tmDispositionResult, @PathVariable Integer curPage, @PathVariable Integer pageSize) {
        PageHelper.startPage(curPage,pageSize, "sortno");
        List<TmDispositionResult> list = tmDispositionResultService.selectList(tmDispositionResult);
        return returnSuccessPageResult(list);
    }

    @PostMapping(path="/getTmDispositionResult/{id}",produces="application/json;charset=UTF-8")
    public String getTmDispositionResult(@PathVariable String id){
        TmDispositionResult item = tmDispositionResultService.selectById(Long.parseLong(id));
        return returnSuccessResult(item);
    }

    @PostMapping(path="/getTmDispositionResultForAlarm/{alarmId}",produces="application/json;charset=UTF-8")
    public String getTmDispositionResultForAlarm(@PathVariable String alarmId){
        TmDispositionResult tmDispositionResultParam = new TmDispositionResult();
        tmDispositionResultParam.setAlarmRecordId(Long.parseLong(alarmId));
        tmDispositionResultParam.setAlarmType("1");
        List<TmDispositionResult> list = tmDispositionResultService.selectList(tmDispositionResultParam);
        if (list.isEmpty()) {
            return returnSuccessResult(new TmDispositionResult());
        }
        TmDispositionResult tmDispositionResult = list.get(0);
        if (tmDispositionResult.getTeamLeaderUserid() != null){
            tmDispositionResult.setTeamLeaderUsername(userService.getOne(tmDispositionResult.getTeamLeaderUserid()).getTruename());
        }
        if (tmDispositionResult.getDisposePriciple() != null){
            tmDispositionResult.setDisposePricipleUsername(userService.getOne(tmDispositionResult.getDisposePriciple()).getTruename());
        }
        if (tmDispositionResult.getEliminationApplyUserid() != null){
            tmDispositionResult.setEliminationApplyUsername(userService.getOne(tmDispositionResult.getEliminationApplyUserid()).getTruename());
        }
        return returnSuccessResult(tmDispositionResult);
    }

    @PostMapping(path="/saveTmDispositionResult")
    public String saveTmDispositionResult(@RequestBody TmDispositionResult tmDispositionResult, HttpServletRequest request){
        tmDispositionResultService.save(tmDispositionResult);
        return returnSuccessResult();
    }

    @PostMapping(path="/deleteTmDispositionResult/{id}")
    public String deleteTmDispositionResult(@PathVariable String id, HttpServletRequest request){
        tmDispositionResultService.delete(Long.parseLong(id));
        return returnSuccessResult();
    }

    @PostMapping(path="/saveDispatch")
    public String saveDispatch(@RequestBody TmDispositionResult tmDispositionResult, HttpServletRequest request){
        User user = super.getCurrentUser(request);
        tmDispositionResult.setTeamLeaderUserid(user.getId());
        tmDispositionResult.setDispatchDatetime(new Date());
        tmDispositionResultService.save(tmDispositionResult);
        TmWarningRecords tmWarningRecords = tmWarningRecordsService.selectByPrimaryKey(tmDispositionResult.getAlarmRecordId());
        tmWarningRecords.setStatus("2");
        tmWarningRecordsService.updateByPrimaryKey(tmWarningRecords);
        return returnSuccessResult();
    }

    @PostMapping(path="/saveApply")
    public String saveApply(@RequestBody TmDispositionResult tmDispositionResult, HttpServletRequest request){
        User user = super.getCurrentUser(request);
        tmDispositionResult.setEliminationApplyUserid(user.getId());
        tmDispositionResult.setApplyDatetime(new Date());
        tmDispositionResultService.save(tmDispositionResult);
        TmWarningRecords tmWarningRecords = tmWarningRecordsService.selectByPrimaryKey(tmDispositionResult.getAlarmRecordId());
        tmWarningRecords.setStatus("3");
        tmWarningRecordsService.updateByPrimaryKey(tmWarningRecords);
        return returnSuccessResult();
    }

}
