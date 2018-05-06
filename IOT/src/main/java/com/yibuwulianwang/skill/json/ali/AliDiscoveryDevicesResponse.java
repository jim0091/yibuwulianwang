package com.yibuwulianwang.skill.json.ali;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;

public class AliDiscoveryDevicesResponse {
	public Header packHeader() {
		Header header  = new Header();
		header.setNamespace("AliGenie.Iot.Device.Discovery");
		header.setName("DiscoveryDevicesResponse");
		header.setMessageId(UUID.randomUUID().toString());
		header.setPayLoadVersion(1);
		return header;
	}
	public Devices packDevices1() {
		Devices devices = new Devices();
		devices.setDeviceId("meidikongtiao");//设备Id
		devices.setDeviceType(DevicesCategory.aircondition.toString());//设备类型
		devices.setDeviceName("空调");//名称
		devices.setBrand("美的");//名称
		devices.setModel("meidikongtiao");//型号
		devices.setZone("客厅");//位置
		devices.setIcon("http://yibuwulianwang.com/intelligence/img/air.jpg");//图片
		Properties properties1 = new Properties();
		properties1.setName("powerstate");//电源状�??
		properties1.setValue(Volue.getPowerstate());
		Properties properties2 = new Properties();//温度
		properties2.setName("temperature");
		properties2.setValue(String.valueOf(Volue.getTemperature()));
		//返回当前设备支持的属性状态列表，产品支持的属性列表参�?
		List<Properties> propertiesList = new ArrayList<Properties>();
		propertiesList.add(properties1);
		propertiesList.add(properties2);
		devices.setProperties(propertiesList);
		//产品支持的操�?
		List<String> actionsList = new ArrayList<String>();
		actionsList.add("TurnOn");
		actionsList.add("TurnOff");
		actionsList.add("SetTemperature");
		actionsList.add("AdjustUpTemperature");
		actionsList.add("AdjustDownTemperature");
		actionsList.add("SetMode");
		actionsList.add("OpenFunction");
		actionsList.add("CloseFunction");
		devices.setActions(actionsList);
		Extensions extensions = new Extensions();
		extensions.setExtension1(devices.getBrand());
		extensions.setExtension2(devices.getZone());
		devices.setExtensions(extensions);
		return devices;
	}
	
	public Devices packDevices2() {
		Devices devices = new Devices();
		devices.setDeviceId("gelifengshang");//设备Id
		devices.setDeviceType(DevicesCategory.fan.toString());//设备类型
		devices.setDeviceName("风扇");//名称
		devices.setBrand("格力");//名称
		devices.setModel("gelifengshang");//型号
		devices.setZone("客厅");//位置
		devices.setIcon("http://yibuwulianwang.com/intelligence/img/fan.jpg");//图片
		Properties properties1 = new Properties();
		properties1.setName("powerstate");//电源状�??
		properties1.setValue(Volue.getPowerstate());
		Properties properties2 = new Properties();//档次
		properties2.setName("windspeed");
		properties2.setValue(String.valueOf(Volue.getWindspeed()));
		//返回当前设备支持的属性状态列表，产品支持的属性列表参�?
		List<Properties> propertiesList = new ArrayList<Properties>();
		propertiesList.add(properties1);
		propertiesList.add(properties2);
		devices.setProperties(propertiesList);
		//产品支持的操�?
		List<String> actionsList = new ArrayList<String>();
		actionsList.add("TurnOn");
		actionsList.add("TurnOff");
		actionsList.add("SetWindSpeed");
		actionsList.add("AdjustUpWindSpeed");
		actionsList.add("AdjustDownWindSpeed");
		actionsList.add("SetMode");
		devices.setActions(actionsList);
		Extensions extensions = new Extensions();
		extensions.setExtension1(devices.getBrand());
		extensions.setExtension2(devices.getZone());
		devices.setExtensions(extensions);
		return devices;
	}
	public Devices packDevices3() {
		Devices devices = new Devices();
		devices.setDeviceId("meidifengshan");//设备Id
		devices.setDeviceType("fan");//设备类型
		devices.setDeviceName("风扇");//名称
		devices.setBrand("美的");//名称
		devices.setModel("meidifengshan");//型号
		devices.setZone("卧室");//位置
		devices.setIcon("http://yibuwulianwang.com/intelligence/img/fan.jpg");//图片
		Properties properties1 = new Properties();
		properties1.setName("powerstate");//电源状�??
		properties1.setValue(Volue.getPowerstate());
		Properties properties2 = new Properties();//档次
		properties2.setName("windspeed");
		properties2.setValue(String.valueOf(Volue.getWindspeed()));
		//返回当前设备支持的属性状态列表，产品支持的属性列表参�?
		List<Properties> propertiesList = new ArrayList<Properties>();
		propertiesList.add(properties1);
		propertiesList.add(properties2);
		devices.setProperties(propertiesList);
		//产品支持的操�?
		List<String> actionsList = new ArrayList<String>();
		actionsList.add("TurnOn");
		actionsList.add("TurnOff");
		actionsList.add("SetWindSpeed");
		actionsList.add("AdjustUpWindSpeed");
		actionsList.add("AdjustDownWindSpeed");
		actionsList.add("SetMode");
		devices.setActions(actionsList);
		Extensions extensions = new Extensions();
		extensions.setExtension1(devices.getBrand());
		extensions.setExtension2(devices.getZone());
		devices.setExtensions(extensions);
		return devices;
	}
	
	public Devices packDevices4() {
		Devices devices = new Devices();
		devices.setDeviceId("dajinkongtiao");//设备Id
		devices.setDeviceType(DevicesCategory.aircondition.toString());//设备类型
		devices.setDeviceName("空调");//名称
		devices.setBrand("大金");//名称
		devices.setModel("dajinkongtiao");//型号
		devices.setZone("卧室");//位置
		devices.setIcon("http://yibuwulianwang.com/intelligence/img/air.jpg");//图片
		Properties properties1 = new Properties();
		properties1.setName("powerstate");//电源状�??
		properties1.setValue(Volue.getPowerstate());
		Properties properties2 = new Properties();//温度
		properties2.setName("temperature");
		properties2.setValue(String.valueOf(Volue.getTemperature()));
		//返回当前设备支持的属性状态列表，产品支持的属性列表参�?
		List<Properties> propertiesList = new ArrayList<Properties>();
		propertiesList.add(properties1);
		propertiesList.add(properties2);
		devices.setProperties(propertiesList);
		//产品支持的操�?
		List<String> actionsList = new ArrayList<String>();
		actionsList.add("TurnOn");
		actionsList.add("TurnOff");
		actionsList.add("SetTemperature");
		actionsList.add("AdjustUpTemperature");
		actionsList.add("AdjustDownTemperature");
		actionsList.add("SetMode");
		actionsList.add("OpenFunction");
		actionsList.add("CloseFunction");
		devices.setActions(actionsList);
		Extensions extensions = new Extensions();
		extensions.setExtension1(devices.getBrand());
		extensions.setExtension2(devices.getZone());
		devices.setExtensions(extensions);
		return devices;
	}
	
	public Payload packPayload() {
		Payload payload = new Payload();
		List<Devices> devicesList = new ArrayList<Devices>();
		devicesList.add(packDevices1());//添加�?个设�?
		devicesList.add(packDevices2());//添加�?个设�?
		devicesList.add(packDevices3());
		devicesList.add(packDevices4());
		payload.setDevices(devicesList);
		return payload;
	}
	
	public Root packRoot() {
		Root root = new Root();
		root.setHeader(packHeader());
		root.setPayload(packPayload());
		return root;
	}
	
	public static void main(String[] args) {
		JSONObject json = new JSONObject();
		System.out.println(json.toJSONString(new AliDiscoveryDevicesResponse().packRoot()));
	}
	
}
