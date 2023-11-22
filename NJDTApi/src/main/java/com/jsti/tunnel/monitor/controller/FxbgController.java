package com.jsti.tunnel.monitor.controller;
 
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageHelper; 
import com.jsti.tunnel.monitor.bean.Fxbg;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.service.FxbgService;
import com.jsti.tunnel.monitor.util.FileUtil;

@RestController
@RequestMapping(value="/fxbg")
public class FxbgController extends BaseController {


    @Autowired
    FxbgService fxbgService;
    
    @Value("${storage.file.path}")
    String storageFilePath;
    
    /**
     * 根据条件获取分页数据
     * @param curPage
     * @param pageSize
     * @param
     * @return
     */
    @RequestMapping(value="/getPageData/{curPage}/{pageSize}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getPageData(HttpServletRequest request,@PathVariable Integer curPage, @PathVariable Integer pageSize, @RequestBody Fxbg fxbg){
    	Map<String,Object> params = new HashMap<>();
        if(fxbg != null){
        	String _name = fxbg.getName()!=null?fxbg.getName():"";
        	String _bglx = fxbg.getBglx()!=null?fxbg.getBglx():"";
        	String _fullPath = fxbg.getFull_path()!=null?fxbg.getFull_path():"";
            if(!"".equals(_name))
                params.put("name",_name);
            if(!"".equals(_bglx))
                params.put("bglx",_bglx);
            if(!"".equals(_fullPath))
                params.put("full_path",_fullPath);
        }
        PageHelper.startPage(curPage,pageSize);
        List<Fxbg> list = fxbgService.getPageData(params);
        System.out.println(list);
        return  returnSuccessPageResult(list,"yyyy-MM-dd");
    }

    @RequestMapping(value="getObjByUUID/{uuid}")
    public String getObjByUUID(@PathVariable String uuid){
        Fxbg fxbg = fxbgService.getObjByUUID(uuid);
        return returnSuccessResult(fxbg,"yyyy-MM-dd");
    }

    /**
     * * 添加
     * @param req
     * @param multiReq
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/add",method = RequestMethod.POST)
    public String add(HttpServletRequest req, MultipartHttpServletRequest multiReq){
        String attachment_path =  FileUtil.saveUploadFile(storageFilePath,multiReq.getFile("full_path"),moduleName) ;
        Fxbg fxbg =  new Fxbg();
        fxbg.setBglx(multiReq.getParameter("bglx"));
        if(!"".contentEquals(attachment_path)) {
        	//fxbg.setName(attachment_path.substring(attachment_path.lastIndexOf("\\")+1,attachment_path.lastIndexOf(".")));//不保存后缀名
        	fxbg.setName(attachment_path.substring(attachment_path.lastIndexOf("\\")+1));//保存后缀名
        }
        fxbg.setFull_path(attachment_path);
        fxbg.setReport_date(multiReq.getParameter("report_date"));
        User user = getCurrentUser(req);
        fxbg.setCreator(user.getUsername());
        fxbgService.add(fxbg);
        return returnSuccessResult("添加成功！");
    }

    @RequestMapping(value="/update",method = RequestMethod.POST)
    public String update(HttpServletRequest req, MultipartHttpServletRequest multiReq){
        Fxbg fxbg =  new Fxbg();
        fxbg.setUuid(multiReq.getParameter("uuid"));
        fxbg.setName(multiReq.getParameter("name"));
        fxbg.setBglx(multiReq.getParameter("bglx"));
        fxbg.setReport_date(multiReq.getParameter("report_date"));
        fxbg.setFull_path(null);
        User user = getCurrentUser(req);
        fxbg.setCreator(user.getUsername());
        fxbgService.update(fxbg);
        return returnSuccessResult("保存成功！");
    }

    /**
     * 根据uuid删除数据
     * @param uuids
     * @return
     */
    @RequestMapping(value = "/delete/{uuids}" , method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String delete(@PathVariable String uuids){
        List<String> uuidList =  Arrays.asList(uuids.split(","));//不能用";"，传到后台会被截断
        fxbgService.delete(uuidList);
        return returnSuccessResult("删除成功！");
    }

}
