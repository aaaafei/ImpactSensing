package com.jsti.tunnel.monitor.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageHelper;
import com.jsti.tunnel.monitor.bean.Device;
import com.jsti.tunnel.monitor.bean.Fxbg;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.service.DeviceService;
import com.jsti.tunnel.monitor.util.CacheUtil;
import com.jsti.tunnel.monitor.util.FileUtil;

@RestController
@RequestMapping(value="/device")
public class DeviceController extends BaseController {


    @Autowired
    DeviceService deviceService;
    
    @Value("${storage.file.path}")
    String storageFilePath;
    
    /**
     * 根据条件获取分页数据
     * @param curPage
     * @param pageSize
     * @param announcement
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
        List<Device> list = deviceService.getPageData(params);
        System.out.println(list);
        return  returnSuccessPageResult(list,"yyyy-MM-dd");
    }

    @RequestMapping(value="getObjByUUID/{uuid}")
    public String getObjByUUID(@PathVariable String uuid){
        Device device = deviceService.getObjByID(uuid);
        return returnSuccessResult(device,"yyyy-MM-dd");
    }

    /**
     * * 添加
     * @param req
     * @param multiReq
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/add",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String add(@RequestBody Device device) throws Exception{
    	System.out.println(device);
        deviceService.add(device);
        return returnSuccessResult("添加成功！");
    }

    @RequestMapping(value="/update",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String update(@RequestBody Device device){
    	System.out.println(device);
        deviceService.update(device);
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
        deviceService.delete(uuidList);
        return returnSuccessResult("删除成功！");
    }

}
