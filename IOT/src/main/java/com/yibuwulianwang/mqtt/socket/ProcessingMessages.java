package com.yibuwulianwang.mqtt.socket;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yibuwulianwang.api.TempHumController;
import com.yibuwulianwang.mysql.JavaConnMySql;
import com.yibuwulianwang.mysql.service.TempHumService;

public class ProcessingMessages {
	@Autowired
    private TempHumService tempHumService;
	
	public static void processingMsg(String json) {
		com.yibuwulianwang.json.me.MyPayloadJson payload = JSON.parseObject(json,new TypeReference<com.yibuwulianwang.json.me.MyPayloadJson>() {});
		String name = payload.getName();
		switch (name) {
		case "putTemphum":
			JavaConnMySql.insertdata(payload.getTemperature(), payload.getHumidity());//插入温湿度到数据库
			break;
		default:
			break;
		}
	}
}
