package com.yibuwulianwang.skill.json.ali;

import java.util.UUID;

import com.alibaba.fastjson.JSONObject;

public class AliControlDevicesResponse {
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
	
	public Root packControlResponse(JSONObject json,String name) {
		Root root = new Root();
		String deviceId = json.getString("deviceId");
		Volue.setPowerstate(json.getString("value"));
		root.setHeader(packHeader(name));
		root.setPayload(packPayload(deviceId));
		return root;
	}
	
}
