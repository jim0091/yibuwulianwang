package com.yibuwulianwang.skill.json.ali;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
/***
 * 响应查询请求
 * @author Administrator
 *
 */
public class AliQueryDevicesResponse {
	public Header packHeader(String name) {
		Header header = new Header();
		header.setNamespace("AliGenie.Iot.Device.Control");
		header.setName(name+"Response");
		header.setMessageId(UUID.randomUUID().toString());
		header.setPayLoadVersion(1);
		return header;
	}
	public Payload packPayload(String deviceId) {
		Payload payload = new Payload();
		payload.setDeviceId(deviceId);
		return payload;
	}
	
	public Properties packProperties1() {
		Properties properties = new Properties();
		properties.setName("temperature");
		properties.setValue(String.valueOf(Volue.getTemperatureAdd()));
		return properties;
	}
    public Properties packProperties2() {
 		Properties properties = new Properties();
 		properties.setName("windspeed");
 		properties.setValue(String.valueOf(Volue.getWindspeedAdd()));//  1对应 1�?, 2对应2档， 3 对应 3�?
 		return properties;
 	}
    
    public Properties packProperties3() {
 		Properties properties = new Properties();
 		properties.setName("powerstate");// 电源状�??
 		properties.setValue(Volue.getPowerstate());
 		return properties;
 	}
    
	public Root packQueryResponse(JSONObject json,String name) {
		Root root = new Root();
		String deviceId = json.getString("deviceId");
		if(deviceId.contains("kongtiao")) {
			root.setHeader(packHeader(name));
			root.setPayload(packPayload(deviceId));
			List<Properties> PropertiesList = new ArrayList<Properties>();
			PropertiesList.add(packProperties1());
			PropertiesList.add(packProperties3());
			root.setProperties(PropertiesList);
		}else {
			root.setHeader(packHeader(name));
			root.setPayload(packPayload(deviceId));
			List<Properties> PropertiesList = new ArrayList<Properties>();
			PropertiesList.add(packProperties2());
			PropertiesList.add(packProperties3());
			root.setProperties(PropertiesList);
		}
		
		return root;
	}
}
