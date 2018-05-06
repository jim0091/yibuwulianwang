package com.yibuwulianwang.skill.json.baidu;

import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
/***
 * 构�?�控制设备的回复
 * @author Administrator
 *
 */
public class BaiduControlResponse {
	
	static String name;
	
	public BaiduControlResponse(String name){
		this.name = name;
	}
	
	public  Header packHeader() {
		Header headerObj = new Header();
		headerObj.setNamespace("DuerOS.ConnectedHome.Control");
		headerObj.setName(this.name.replace("Request", "Confirmation"));
		headerObj.setMessageId(UUID.randomUUID().toString());
		headerObj.setPayloadVersion(String.valueOf(1));
		return headerObj;
	}
	
	public Payload packPayload() {
		Payload payloadObj = new Payload();
		return payloadObj;
	}
	
	public Root packRoot() {
		Root root = new Root();
		root.setHeader(packHeader());
		root.setPayload(packPayload());
		return root;
	}
	
	public static void main(String[] args) {
		System.out.println(new JSONObject().toJSONString(new BaiduControlResponse("TurnOnRequest").packRoot()));
	}
}
