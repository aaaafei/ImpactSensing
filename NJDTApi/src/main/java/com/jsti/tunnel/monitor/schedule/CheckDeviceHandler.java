package com.jsti.tunnel.monitor.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.github.pagehelper.PageHelper;
import com.jsti.tunnel.monitor.bean.TmOriginData;
import com.jsti.tunnel.monitor.bean.TmWarningRecords;
import com.jsti.tunnel.monitor.pojo.TmOriginDataParams;
import com.jsti.tunnel.monitor.service.TmOriginDataService;
import com.jsti.tunnel.monitor.service.TmWarningRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jsti.tunnel.monitor.bean.TmDevice;
import com.jsti.tunnel.monitor.service.TmDeviceService;

@Component
public class CheckDeviceHandler {
 

    @Autowired
    private TmDeviceService tmDeviceService;

	@Autowired
	private TmOriginDataService tmOriginDataService;

	@Autowired
	private TmWarningRecordsService tmWarningRecordsService;
    
    private static CheckDeviceHandler checkDeviceHandler;
    @PostConstruct
    public void init(){
    	checkDeviceHandler = this;
    	checkDeviceHandler.tmDeviceService = this.tmDeviceService;
		checkDeviceHandler.tmOriginDataService = this.tmOriginDataService;
		checkDeviceHandler.tmWarningRecordsService = this.tmWarningRecordsService;
    }
    
    public void checkDeviceStatus(){
		PageHelper.startPage(1,100);
		PageHelper.orderBy("ic desc");
		TmOriginDataParams tmOriginDataPojo = new TmOriginDataParams();
		tmOriginDataPojo.setCatalog("S6");
		List<TmOriginData> dataList = checkDeviceHandler.tmOriginDataService.selectList(tmOriginDataPojo);
		Map<String, TmOriginData> deviceDataMap = new HashMap<>();
		for (TmOriginData dataItem : dataList) {
			// 最新的值写入device表
			if (deviceDataMap.keySet().contains(dataItem.getClientimei())) {
				if (deviceDataMap.get(dataItem.getClientimei()).getTimstamp().after(dataItem.getTimstamp())) {
					continue;
				}
			}
			TmDevice device = new TmDevice();
			device.setCode(dataItem.getClientimei());
			device.setStatus(dataItem.getCatalogval().equals("1")?"1":"0");
			device.setCollectTime(dataItem.getTimstamp());
			device.setRealtimePhysicalData(dataItem.getCatalogval());
			device.setVoltage(dataItem.getVoltage());
			checkDeviceHandler.tmDeviceService.updateByCode(device);
			deviceDataMap.put(dataItem.getClientimei(), dataItem);
		}

		PageHelper.startPage(1,100);
		PageHelper.orderBy("id desc");
		List<TmWarningRecords> warningRecordsList = checkDeviceHandler.tmWarningRecordsService.selectDataList(null);
		List<String> deviceCollectTimeList = warningRecordsList.stream()
				.map(item -> item.getDeviceid() + "_" + item.getCollectTime().getTime())
				.collect(Collectors.toList());
		TmOriginData lastData = dataList.get(0);
		for (TmOriginData dataItem : dataList) {
			TmWarningRecords tmWarningRecords = new TmWarningRecords();
			if (lastData.getCatalogval().equals("1") && !dataItem.getCatalogval().equals("1")) {
				tmWarningRecords.setFirst(1);
			}
			// 异常记录写入warningRecords表
			if (dataItem.getCatalogval().equals("1")) {
				continue;
			}
			String dataId = dataItem.getClientimei() + "_" + dataItem.getTimstamp().getTime();
			if (deviceCollectTimeList.contains(dataId)) {
				continue;
			}

			tmWarningRecords.setDeviceid(dataItem.getClientimei());
			tmWarningRecords.setCollectTime(dataItem.getTimstamp());
			tmWarningRecords.setVal(dataItem.getCatalogval());
			tmWarningRecords.setRemark("运行异常，现场可能受撞击，请现场查看");
			//

			checkDeviceHandler.tmWarningRecordsService.insert(tmWarningRecords);
		}

    }
}
