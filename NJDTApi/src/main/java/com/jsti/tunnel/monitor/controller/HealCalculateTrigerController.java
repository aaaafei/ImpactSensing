package com.jsti.tunnel.monitor.controller;

import com.jsti.system.err.ErrorCodeEnum;
import com.jsti.tunnel.monitor.bean.*;
import com.jsti.tunnel.monitor.enumeration.MonitorType;
import com.jsti.tunnel.monitor.pojo.TmOriginDataParams;
import com.jsti.tunnel.monitor.service.*;
import com.jsti.tunnel.monitor.util.ArrayMinValueUtil;
import com.jsti.tunnel.monitor.util.DateUtil;
import com.jsti.tunnel.monitor.util.TunnelHealthCalculateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/healCalculateTriger")
public class HealCalculateTrigerController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(HealCalculateTrigerController.class);
	
	@Value("${mockingSafety.enable}")
    private String mockingSafety;

	@Autowired
	TmCalculateModelService tmCalculateModelService;
	@Autowired
	TmCalculateValueConfigService tmCalculateValueConfigService;
	@Autowired
	TmDeviceService tmDeviceService;
	@Autowired
	TmHealthHistoryService tmHealthHistoryService;
	@Autowired
	SafetyService safetyService;
	@Autowired
	TmWarningRecordsService tmWarningRecordsService;

	@Autowired
	TmOriginDataService tmOriginDataService;

	@GetMapping("/SafetyRealtimeCalculate/{line}/{no}")
	public String SafetyRealtimeCalculate(@PathVariable String line,@PathVariable String no) throws Exception{
		TmCalculateModel model = new TmCalculateModel();
		model.setLine(line);
		model.setNo(no);//同一个区段的传感器是在同一时间采集上来的
		List<TmCalculateModel> nodes = tmCalculateModelService.selectRootNodeDataList(model);
		TmCalculateModel rootNode =  null;//区段根节点
		if(nodes!=null && nodes.size()==1) {
			rootNode = nodes.get(0);
			Map<String, String> rst = new HashMap<String, String>();
			rst.put("line", line);
			rst.put("rootNo", no);
			BigDecimal safetyValue = calculateNodeValue(rootNode);
			rst.put("safetyValue", safetyValue.toString());

			Map<String, String> no_segment = new HashMap<String, String>() {{
				put("G1", "浦口竖井");
				put("G2", "浦口大堤");
				put("G3", "江中");
				put("G4", "覆土厚度最小");
				put("G5", "梅子洲大堤");
				put("G6", "梅子洲竖井");
			}};

			Map<String, Object> safetyMapParams = new HashMap<String,Object>(){{
				put("segment_name", no_segment.get(no));
				put("line", line);
			}};
			List<Safety> listSafety = safetyService.getRealTimeData(safetyMapParams);
			if(listSafety.size()>0) {
				Safety safety = listSafety.get(0);
				safety.setSafety_value(safetyValue.floatValue());
				String safetyLable = "E";
		        if(safetyValue.floatValue()>4.2) {
		        	safetyLable="A";
		          }
		          else if(safetyValue.floatValue()>3.4){
			        	safetyLable="B";
		          }
		          else if(safetyValue.floatValue()>2.6){
			        	safetyLable="C";
		          }
		          else if(safetyValue.floatValue()>1.8){
			        	safetyLable="D";
		          }
				safety.setSafety_label(safetyLable);
				safety.setCollect_time(new Date());
				if(!mockingSafety.equals("1")){
					safetyService.updateRealTimeData(safety);
					safetyService.addHistoryData(safety);
				}

			}


			return returnSuccessResult(rst);
		}
		else {
			Map<String, String> rst = new HashMap<String, String>();
			rst.put("line", line);
			rst.put("rootNo", no);
			rst.put("description","区段根节点信息检索返回值不正确");
			return returnResult(11000,ErrorCodeEnum.getPropByKey("11000"),rst);
		}
	}

	private BigDecimal calculateNodeValue(TmCalculateModel node) throws Exception {
		return null;
	}


	@GetMapping("/CalculateHistoryHeathValue/{begin}/{endBefore}")
	public String CalculateHistoryHeathValue(@PathVariable String begin,@PathVariable String endBefore) throws Exception {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date collect_time = sdf.parse(begin);//30min采一次的规律 "2021-01-04 00:00:00"
		Date calculate_end_time = sdf.parse(endBefore);//"2021-01-04 00:30:00"

		while (collect_time.before(calculate_end_time)){
			TmCalculateModel model = new TmCalculateModel();
			List<TmCalculateModel> nodes = tmCalculateModelService.selectRootNodeDataList(model);
			for (int i = 0; i < nodes.size(); i++) {
				TmCalculateModel rootNode = nodes.get(i);
				BigDecimal safetyValue = calculateNodeValue(rootNode,collect_time);
				Map<String, String> no_segment = new HashMap<String, String>() {{
					put("G1", "浦口竖井");
					put("G2", "浦口大堤");
					put("G3", "江中");
					put("G4", "覆土厚度最小");
					put("G5", "梅子洲大堤");
					put("G6", "梅子洲竖井");
				}};

				Map<String, Object> safetyMapParams = new HashMap<String,Object>(){{
					put("segment_name", no_segment.get(rootNode.getNo()));
					put("line", rootNode.getLine());
				}};
				List<Safety> listSafety = safetyService.getRealTimeData(safetyMapParams);
				if(listSafety.size()>0) {
					Safety safety = listSafety.get(0);
					safety.setSafety_value(safetyValue.floatValue());
					String safetyLable = "E";
					if(safetyValue.floatValue()>4.2) {
						safetyLable="A";
					}
					else if(safetyValue.floatValue()>3.4){
						safetyLable="B";
					}
					else if(safetyValue.floatValue()>2.6){
						safetyLable="C";
					}
					else if(safetyValue.floatValue()>1.8){
						safetyLable="D";
					}
					safety.setSafety_label(safetyLable);
					safety.setCollect_time(collect_time);
					safetyService.addHistoryData(safety);
				}
			}

			Calendar cal_collect = Calendar.getInstance();
			cal_collect.setTime(collect_time);
			cal_collect.add(Calendar.MINUTE,30);
			collect_time = cal_collect.getTime();
			//System.err.println(sdf.format(collect_time));
		}

		return returnSuccessResult();
	}


	private BigDecimal calculateNodeValue(TmCalculateModel node,Date collect_time) throws Exception {

		return null;
	}

	private TmHealthHistory getLastHealthHistory(String model_id,Date tmEnd) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("model_id",model_id);
		params.put("end_date", tmEnd);
		return tmHealthHistoryService.getLastOne(params);
	}

	/**
	 * 桩号 --> x米
	 * @param stakeNumber
	 * @return
	 */
	private BigDecimal stakeNumber2Meter(String stakeNumber) {
		String[] stakeNumberStrings = stakeNumber.split("\\+");
		String k = stakeNumberStrings[0].substring(2);
		Integer number = Integer.parseInt(stakeNumberStrings[1]);
		return new BigDecimal(k).multiply(new BigDecimal(1000)).add(new BigDecimal(number));
	}


}
