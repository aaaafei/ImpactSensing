package com.jsti.tunnel.monitor.controller;

import com.jsti.tunnel.monitor.bean.TmCalculateModel;
import com.jsti.tunnel.monitor.bean.TmCalculateValueConfig;
import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.bean.TmHealthHistory;
import com.jsti.tunnel.monitor.bean.TmOriginData;
import com.jsti.tunnel.monitor.pojo.DeviceNode;
import com.jsti.tunnel.monitor.pojo.HealthTreeNode;
import com.jsti.tunnel.monitor.pojo.DevicesHealthyParams;
import com.jsti.tunnel.monitor.pojo.TreePojo;
import com.jsti.tunnel.monitor.service.TmCalculateModelService;
import com.jsti.tunnel.monitor.service.TmCalculateValueConfigService;
import com.jsti.tunnel.monitor.service.TmDeviceService;
import com.jsti.tunnel.monitor.service.TmHealthHistoryService;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calculateModel")
public class TmCalculateModelController extends BaseController {

	
	
	@Autowired
	TmCalculateModelService tmCalculateModelService;
	
	@Autowired
	TmCalculateValueConfigService tmCalculateValueConfigService;
	
	@Autowired
	TmDeviceService tmDeviceService;
	
	@Autowired
	TmHealthHistoryService tmHealthHistoryService;
	

	@GetMapping("/getRootNodeList/{line}")
	public String getRootNodeList(@PathVariable String line) {
		TmCalculateModel tmCalculateModel = new TmCalculateModel();
		tmCalculateModel.setLine(line);
		List<TmCalculateModel> list = tmCalculateModelService.selectRootNodeDataList(tmCalculateModel);
    	List<TreePojo> treePojos =  new ArrayList<>();
    	for (TmCalculateModel model : list) {
			TreePojo pojo = new TreePojo();
			pojo.setCode(model.getId().toString());
			pojo.setLabel(model.getNo() +"-" + model.getDescription());
			treePojos.add(pojo);
		}
    	Map<String,Object> map = new HashedMap();
    	map.put("tableList",list);
    	map.put("treePojos", treePojos);
        return returnSuccessResult(map);
	}

    @PostMapping("/getSonNodeListByParentId")
    public String getSonNodeListByParentId(@RequestBody TmCalculateModel tmCalculateModel){
    	List<TmCalculateModel> list = tmCalculateModelService.selectDataList(tmCalculateModel);
    	List<TreePojo> treePojos =  new ArrayList<>();
    	for (TmCalculateModel model : list) {
			TreePojo pojo = new TreePojo();
			pojo.setCode(model.getId().toString());
			pojo.setLabel(model.getNo() +"-" + model.getDescription());
			treePojos.add(pojo);
		}    	Map<String,Object> map = new HashedMap();
    	map.put("tableList",list);
    	map.put("treePojos", treePojos);
        return returnSuccessResult(map);
    }
    
    @PostMapping("/saveNode")
    public String saveNode(@RequestBody DeviceNode node) { 
    	if(!"M".equals(node.getType())) 
    		node.setIs_leaf(false);//非子节点
    	else {
    		node.setIs_leaf(true);//子节点 
    	}
    	if(node.getId()==null) {
    		//add 
        	int id =tmCalculateModelService.insert((TmCalculateModel)node); 
        	//save calculate_value_config
        	if("M".equals(node.getType())){
        		TmCalculateValueConfig valueConfig = new TmCalculateValueConfig();
        		valueConfig.setDevice1_id(node.getDevice1_id());
        		valueConfig.setDevice2_id(node.getDevice2_id());
        		valueConfig.setLeaf_id(id);
        		valueConfig.setType(node.getDevice_type());
        		tmCalculateValueConfigService.insert(valueConfig);
        	} 
    	}
    	else {
    		//update
    		
    		tmCalculateValueConfigService.deleteByLeafId(node.getId());
    		tmCalculateModelService.updateByPrimaryKey((TmCalculateModel)node); 
        	//save calculate_value_config
        	if("M".equals(node.getType())){
        		TmCalculateValueConfig valueConfig = new TmCalculateValueConfig();
        		valueConfig.setDevice1_id(node.getDevice1_id());
        		valueConfig.setDevice2_id(node.getDevice2_id());
        		valueConfig.setLeaf_id(node.getId());
        		valueConfig.setType(node.getDevice_type());
        		tmCalculateValueConfigService.insert(valueConfig);
        	}
    	}
    
    	return returnSuccessResult();		
	}
    
    
    @GetMapping("/getMConfigById/{id}")
    public String getMConfigById(@PathVariable Integer id) {
    	TmCalculateValueConfig tmCalculateValueConfig = tmCalculateValueConfigService.selectByPrimaryKey(id);
    	return returnSuccessResult(tmCalculateValueConfig);
    }
    
    
    
    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Integer id) {
    	tmCalculateModelService.deleteByPrimaryKey(id);
    	tmCalculateValueConfigService.deleteByLeafId(id);
    	return returnSuccessResult();
    }
    
    
    @GetMapping("/getHeathTreeValues/{id}")
    public String getHeathTreeValues(@PathVariable Integer id) {  
		TmCalculateModel rootNode =  tmCalculateModelService.selectByPrimaryKey(id);
		Map<String, Object> rst = new HashMap<>();
		rst.put("line", rootNode.getLine());
		rst.put("rootNo", rootNode.getNo());
		HealthTreeNode healthTreeNode = new HealthTreeNode();
		healthTreeNode.setName(rootNode.getNo()+"("+rootNode.getWeightiness()+")");
		healthTreeNode.setValue(rootNode.getHealth_value());
		Map<String, String> itemStyle = new HashMap<>(); 
		itemStyle.put("borderColor", getStatusColor(rootNode.getHealth_value()));
		healthTreeNode.setItemStyle(itemStyle);
		healthTreeNode.setChildren(getChildrenList(rootNode.getId()));
		rst.put("node",healthTreeNode);
		return returnSuccessResult(rst);
		 
    }
    
    List<HealthTreeNode> getChildrenList(int parent_category_id){
    	TmCalculateModel param = new TmCalculateModel();
    	param.setParent_category_id(parent_category_id);
    	List<TmCalculateModel> childrenList = tmCalculateModelService.selectDataList(param);
    	if(childrenList.size()>0) {
    		List<HealthTreeNode> healthTreeNodes = new ArrayList<>();
    		for(TmCalculateModel tmp : childrenList) {
    			HealthTreeNode hNode = new HealthTreeNode();
    			hNode.setName(tmp.getNo()+"("+tmp.getWeightiness()+")");
    			hNode.setValue(tmp.getHealth_value());
    			Map<String, String> itemStyle = new HashMap<>(); 
    			itemStyle.put("borderColor", getStatusColor(tmp.getHealth_value()));
    			hNode.setItemStyle(itemStyle);
    			hNode.setChildren(getChildrenList(tmp.getId()));
    			healthTreeNodes.add(hNode);
    		}
    		return healthTreeNodes;
    	}
    	else {
    		return null;
    	}
    }
    
    static String getStatusColor(BigDecimal value){ 
    	return null;
	}
    
    @PostMapping("/getDevicesHealthy")
    public String getDevicesHealthy(@RequestBody DevicesHealthyParams params) {  

        return returnSuccessResult(null);
     
    }
    
    
    

}
