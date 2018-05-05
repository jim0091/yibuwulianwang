package com.yibuwulianwang.skill.json.baidu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BaiduDiscoveryResponse {
	public  Header packHeader() {
		Header headerObj = new Header();
		headerObj.setNamespace("DuerOS.ConnectedHome.Discovery");
		headerObj.setName("DiscoverAppliancesResponse");
		headerObj.setMessageId(UUID.randomUUID().toString());
		headerObj.setPayloadVersion(String.valueOf(1));
		return headerObj;
	}
	
	/****
	 * payload的第�?个数�? 设备信息
	 */
	public DiscoveredAppliances packDiscoveredAppliances1() {
		DiscoveredAppliances discoveredAppliancesObj = new DiscoveredAppliances();
		AdditionalApplianceDetails additionalApplianceDetailsObj = new AdditionalApplianceDetails();
		List<String> actionsList = new ArrayList<String>();// 操作类型----空调
		actionsList.add("turnOn");// 打开
		actionsList.add("timingTurnOn");// 定时打开
		actionsList.add("turnOff");// 关闭
		actionsList.add("timingTurnOff");// 定时关闭
		actionsList.add("incrementTemperature");// 升高温度
		actionsList.add("decrementTemperature");// 降低温度
		actionsList.add("setTemperature");// 设置温度
		actionsList.add("incrementFanSpeed");// 增加风�??
		actionsList.add("decrementFanSpeed");// 减小风�??
		actionsList.add("setFanSpeed");// 设置风�??
		actionsList.add("setMode");// 设置模式
		actionsList.add("unSetMode");// 取消设置的模�?
		actionsList.add("timingSetMode");// 定时设置模式
		actionsList.add("timingUnsetMode");// 定时取消设置的模�?
		actionsList.add("setSwing");// 设置摇摆功能
		actionsList.add("setPurification");// 设置�?化功�?
		actionsList.add("setNegativeIon");// 设置负离子功�?
		discoveredAppliancesObj.setActions(actionsList);
		List<String> applianceTypesList = new ArrayList<String>();// 支持的设备�?�场景类型�??
		applianceTypesList.add("AIR_CONDITION");
		discoveredAppliancesObj.setApplianceTypes(applianceTypesList);
		additionalApplianceDetailsObj.setExtraDetail1("美的中央空调");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail2("这个�?个美的中央空�?2");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail3("这个�?个美的中央空�?3");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail4("这个�?个美的中央空�?4");//可以为空
		//设备标识�?,标识符在用户拥有的所有设备上必须是唯�?的�?�此外，标识符需要在同一设备的多个发现请求之间保持一致�?�标识符可以包含任何字母或数字和以下特殊字符：_ - = # ; : ? @ &。标识符不能超过256个字�?.
		discoveredAppliancesObj.setApplianceId("Kongtiao");//UUID.randomUUID().toString().split("-")[0]
		discoveredAppliancesObj.setModelName("zhongyangkongtiao");//设备型号名称,是字符串类型，长度不能超�?128个字符�??
		discoveredAppliancesObj.setVersion("zhongyangkongtiao-1");//供应商提供的设备版本。是字符串类型，长度不能超过128个字符�??
		discoveredAppliancesObj.setFriendlyName("空调");//用户用来识别设备的名称�?? 是字符串类型，不能包含特殊字符和标点符号，长度不能超�?128个字符�??
		discoveredAppliancesObj.setFriendlyDescription("美的中央空调，居家模式，红外控制�?");//设备相关的描述，描述内容提需要提及设备厂商，使用场景及连接方式，长度不超�?128个字符�??
		discoveredAppliancesObj.setIsReachable(true);//设备当前是否能够到达。true表示设备当前可以到达，false表示当前设备不能到达�?
		discoveredAppliancesObj.setAdditionalApplianceDetails(additionalApplianceDetailsObj);
		discoveredAppliancesObj.setManufacturerName("美的");
		return discoveredAppliancesObj;
	}
	
	/****
	 * payload的第�?个数�? 设备信息
	 */
	public DiscoveredAppliances packDiscoveredAppliances2() {
		DiscoveredAppliances discoveredAppliancesObj = new DiscoveredAppliances();
		AdditionalApplianceDetails additionalApplianceDetailsObj = new AdditionalApplianceDetails();
		List<String> actionsList = new ArrayList<String>();// 操作类型----空调
		actionsList.add("turnOn");// 打开
		actionsList.add("timingTurnOn");// 定时打开
		actionsList.add("turnOff");// 关闭
		actionsList.add("timingTurnOff");// 定时关闭
		discoveredAppliancesObj.setActions(actionsList);
		List<String> applianceTypesList = new ArrayList<String>();// 支持的设备�?�场景类型�??
		applianceTypesList.add("LIGHT");
		discoveredAppliancesObj.setApplianceTypes(applianceTypesList);
		additionalApplianceDetailsObj.setExtraDetail1("台灯");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail2("这个�?个美的中央空�?2");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail3("这个�?个美的中央空�?3");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail4("这个�?个美的中央空�?4");//可以为空
		//设备标识�?,标识符在用户拥有的所有设备上必须是唯�?的�?�此外，标识符需要在同一设备的多个发现请求之间保持一致�?�标识符可以包含任何字母或数字和以下特殊字符：_ - = # ; : ? @ &。标识符不能超过256个字�?.
		discoveredAppliancesObj.setApplianceId("Taideng");//UUID.randomUUID().toString().split("-")[0]
		discoveredAppliancesObj.setModelName("Taideng");//设备型号名称,是字符串类型，长度不能超�?128个字符�??
		discoveredAppliancesObj.setVersion("Taideng-1");//供应商提供的设备版本。是字符串类型，长度不能超过128个字符�??
		discoveredAppliancesObj.setFriendlyName("台灯");//用户用来识别设备的名称�?? 是字符串类型，不能包含特殊字符和标点符号，长度不能超�?128个字符�??
		discoveredAppliancesObj.setFriendlyDescription("台灯，居家模式，红外控制�?");//设备相关的描述，描述内容提需要提及设备厂商，使用场景及连接方式，长度不超�?128个字符�??
		discoveredAppliancesObj.setIsReachable(true);//设备当前是否能够到达。true表示设备当前可以到达，false表示当前设备不能到达�?
		discoveredAppliancesObj.setAdditionalApplianceDetails(additionalApplianceDetailsObj);
		discoveredAppliancesObj.setManufacturerName("小米");
		return discoveredAppliancesObj;
	}
	
	
	/****
	 * payload的第�?个数�? 设备信息
	 */
	public DiscoveredAppliances packDiscoveredAppliances3() {
		DiscoveredAppliances discoveredAppliancesObj = new DiscoveredAppliances();
		AdditionalApplianceDetails additionalApplianceDetailsObj = new AdditionalApplianceDetails();
		List<String> actionsList = new ArrayList<String>();// 操作类型----空调
		actionsList.add("turnOn");// 打开
		actionsList.add("timingTurnOn");// 定时打开
		actionsList.add("turnOff");// 关闭
		actionsList.add("timingTurnOff");// 定时关闭
		discoveredAppliancesObj.setActions(actionsList);
		List<String> applianceTypesList = new ArrayList<String>();// 支持的设备�?�场景类型�??
		applianceTypesList.add("LIGHT");
		discoveredAppliancesObj.setApplianceTypes(applianceTypesList);
		additionalApplianceDetailsObj.setExtraDetail1("居家模式");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail2("这个�?个美的中央空�?2");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail3("这个�?个美的中央空�?3");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail4("这个�?个美的中央空�?4");//可以为空
		//设备标识�?,标识符在用户拥有的所有设备上必须是唯�?的�?�此外，标识符需要在同一设备的多个发现请求之间保持一致�?�标识符可以包含任何字母或数字和以下特殊字符：_ - = # ; : ? @ &。标识符不能超过256个字�?.
		discoveredAppliancesObj.setApplianceId("Jujiamoshi");//UUID.randomUUID().toString().split("-")[0]
		discoveredAppliancesObj.setModelName("Jujiamoshi");//设备型号名称,是字符串类型，长度不能超�?128个字符�??
		discoveredAppliancesObj.setVersion("Jujiamoshi-1");//供应商提供的设备版本。是字符串类型，长度不能超过128个字符�??
		discoveredAppliancesObj.setFriendlyName("居家模式");//用户用来识别设备的名称�?? 是字符串类型，不能包含特殊字符和标点符号，长度不能超�?128个字符�??
		discoveredAppliancesObj.setFriendlyDescription("居家模式");//设备相关的描述，描述内容提需要提及设备厂商，使用场景及连接方式，长度不超�?128个字符�??
		discoveredAppliancesObj.setIsReachable(true);//设备当前是否能够到达。true表示设备当前可以到达，false表示当前设备不能到达�?
		discoveredAppliancesObj.setAdditionalApplianceDetails(additionalApplianceDetailsObj);
		discoveredAppliancesObj.setManufacturerName("居家模式");
		return discoveredAppliancesObj;
	}
	
	public DiscoveredAppliances packDiscoveredAppliances4() {
		DiscoveredAppliances discoveredAppliancesObj = new DiscoveredAppliances();
		AdditionalApplianceDetails additionalApplianceDetailsObj = new AdditionalApplianceDetails();
		List<String> actionsList = new ArrayList<String>();// 操作类型----空调
		actionsList.add("turnOn");// 打开
		actionsList.add("timingTurnOn");// 定时打开
		actionsList.add("turnOff");// 关闭
		actionsList.add("timingTurnOff");// 定时关闭
		discoveredAppliancesObj.setActions(actionsList);
		List<String> applianceTypesList = new ArrayList<String>();// 支持的设备�?�场景类型�??
		applianceTypesList.add("LIGHT");
		discoveredAppliancesObj.setApplianceTypes(applianceTypesList);
		additionalApplianceDetailsObj.setExtraDetail1("安防模式");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail2("这个�?个美的中央空�?2");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail3("这个�?个美的中央空�?3");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail4("这个�?个美的中央空�?4");//可以为空
		//设备标识�?,标识符在用户拥有的所有设备上必须是唯�?的�?�此外，标识符需要在同一设备的多个发现请求之间保持一致�?�标识符可以包含任何字母或数字和以下特殊字符：_ - = # ; : ? @ &。标识符不能超过256个字�?.
		discoveredAppliancesObj.setApplianceId("Anfanmoshi");//UUID.randomUUID().toString().split("-")[0]
		discoveredAppliancesObj.setModelName("Anfanmoshi");//设备型号名称,是字符串类型，长度不能超�?128个字符�??
		discoveredAppliancesObj.setVersion("Anfanmoshi-1");//供应商提供的设备版本。是字符串类型，长度不能超过128个字符�??
		discoveredAppliancesObj.setFriendlyName("安防模式");//用户用来识别设备的名称�?? 是字符串类型，不能包含特殊字符和标点符号，长度不能超�?128个字符�??
		discoveredAppliancesObj.setFriendlyDescription("安防模式");//设备相关的描述，描述内容提需要提及设备厂商，使用场景及连接方式，长度不超�?128个字符�??
		discoveredAppliancesObj.setIsReachable(true);//设备当前是否能够到达。true表示设备当前可以到达，false表示当前设备不能到达�?
		discoveredAppliancesObj.setAdditionalApplianceDetails(additionalApplianceDetailsObj);
		discoveredAppliancesObj.setManufacturerName("安防模式");
		return discoveredAppliancesObj;
	}
	
	public DiscoveredAppliances packDiscoveredAppliances5() {
		DiscoveredAppliances discoveredAppliancesObj = new DiscoveredAppliances();
		AdditionalApplianceDetails additionalApplianceDetailsObj = new AdditionalApplianceDetails();
		List<String> actionsList = new ArrayList<String>();// 操作类型----空调
		actionsList.add("turnOn");// 打开
		actionsList.add("timingTurnOn");// 定时打开
		actionsList.add("turnOff");// 关闭
		actionsList.add("timingTurnOff");// 定时关闭
		actionsList.add("setSwing");
		discoveredAppliancesObj.setActions(actionsList);
		List<String> applianceTypesList = new ArrayList<String>();// 支持的设备�?�场景类型�??
		applianceTypesList.add("FAN");
		discoveredAppliancesObj.setApplianceTypes(applianceTypesList);
		additionalApplianceDetailsObj.setExtraDetail1("安防模式");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail2("这个�?个美的中央空�?2");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail3("这个�?个美的中央空�?3");//可以为空
		//additionalApplianceDetailsObj.setExtraDetail4("这个�?个美的中央空�?4");//可以为空
		//设备标识�?,标识符在用户拥有的所有设备上必须是唯�?的�?�此外，标识符需要在同一设备的多个发现请求之间保持一致�?�标识符可以包含任何字母或数字和以下特殊字符：_ - = # ; : ? @ &。标识符不能超过256个字�?.
		discoveredAppliancesObj.setApplianceId("Fengshan");//UUID.randomUUID().toString().split("-")[0]
		discoveredAppliancesObj.setModelName("Fengshan");//设备型号名称,是字符串类型，长度不能超�?128个字符�??
		discoveredAppliancesObj.setVersion("Fengshan-1");//供应商提供的设备版本。是字符串类型，长度不能超过128个字符�??
		discoveredAppliancesObj.setFriendlyName("风扇");//用户用来识别设备的名称�?? 是字符串类型，不能包含特殊字符和标点符号，长度不能超�?128个字符�??
		discoveredAppliancesObj.setFriendlyDescription("安防模式");//设备相关的描述，描述内容提需要提及设备厂商，使用场景及连接方式，长度不超�?128个字符�??
		discoveredAppliancesObj.setIsReachable(true);//设备当前是否能够到达。true表示设备当前可以到达，false表示当前设备不能到达�?
		discoveredAppliancesObj.setAdditionalApplianceDetails(additionalApplianceDetailsObj);
		discoveredAppliancesObj.setManufacturerName("安防模式");
		return discoveredAppliancesObj;
	}
	
	/****
	 * payload的第二个数组 分组信息
	 */
	public DiscoveredGroups packDiscoveredGroups1() {
		DiscoveredGroups discoveredGroupsObj1 = new DiscoveredGroups();
		AdditionalGroupDetails additionalGroupDetails = new AdditionalGroupDetails();
		List<String> applianceIdsList = new ArrayList<String>();
		applianceIdsList.add(packDiscoveredAppliances1().getApplianceId());//分组�?包含设备ID的数组，要求设备ID必须是已经发现的设备中的ID，否则会同步失败，每个分组设备ID数量不超�?50�?
		discoveredGroupsObj1.setGroupName("客厅");//用户用来识别分组的名�?, 
		discoveredGroupsObj1.setGroupNotes("艾韵智能三楼办公室美的空调控制组");
		discoveredGroupsObj1.setApplianceIds(applianceIdsList);
		additionalGroupDetails.setExtraDetail1("分组控制艾韵智能三楼办公室中央空�?");
		//additionalGroupDetails.setExtraDetail2("分组控制艾韵智能三楼办公室中央空�?2");
		//additionalGroupDetails.setExtraDetail3("分组控制艾韵智能三楼办公室中央空�?3");
		//additionalGroupDetails.setExtraDetail4("分组控制艾韵智能三楼办公室中央空�?4");
		discoveredGroupsObj1.setAdditionalGroupDetails(additionalGroupDetails);
		return discoveredGroupsObj1;
	}
	
	public DiscoveredGroups packDiscoveredGroups2() {
		DiscoveredGroups discoveredGroupsObj2 = new DiscoveredGroups();
		AdditionalGroupDetails additionalGroupDetails = new AdditionalGroupDetails();
		List<String> applianceIdsList = new ArrayList<String>();
		applianceIdsList.add(packDiscoveredAppliances2().getApplianceId());//分组�?包含设备ID的数组，要求设备ID必须是已经发现的设备中的ID，否则会同步失败，每个分组设备ID数量不超�?50�?
		discoveredGroupsObj2.setGroupName("卧室");//用户用来识别分组的名�?, 
		discoveredGroupsObj2.setGroupNotes("卧室小米台灯");
		discoveredGroupsObj2.setApplianceIds(applianceIdsList);
		additionalGroupDetails.setExtraDetail1("卧室小米台灯");
		//additionalGroupDetails.setExtraDetail2("分组控制艾韵智能三楼办公室中央空�?2");
		//additionalGroupDetails.setExtraDetail3("分组控制艾韵智能三楼办公室中央空�?3");
		//additionalGroupDetails.setExtraDetail4("分组控制艾韵智能三楼办公室中央空�?4");
		discoveredGroupsObj2.setAdditionalGroupDetails(additionalGroupDetails);
		return discoveredGroupsObj2;
	}
	
	public DiscoveredGroups packDiscoveredGroups3() {
		DiscoveredGroups discoveredGroupsObj2 = new DiscoveredGroups();
		AdditionalGroupDetails additionalGroupDetails = new AdditionalGroupDetails();
		List<String> applianceIdsList = new ArrayList<String>();
		applianceIdsList.add(packDiscoveredAppliances2().getApplianceId());//分组�?包含设备ID的数组，要求设备ID必须是已经发现的设备中的ID，否则会同步失败，每个分组设备ID数量不超�?50�?
		discoveredGroupsObj2.setGroupName("居家模式");//用户用来识别分组的名�?, 
		discoveredGroupsObj2.setGroupNotes("居家模式");
		discoveredGroupsObj2.setApplianceIds(applianceIdsList);
		additionalGroupDetails.setExtraDetail1("居家模式");
		//additionalGroupDetails.setExtraDetail2("分组控制艾韵智能三楼办公室中央空�?2");
		//additionalGroupDetails.setExtraDetail3("分组控制艾韵智能三楼办公室中央空�?3");
		//additionalGroupDetails.setExtraDetail4("分组控制艾韵智能三楼办公室中央空�?4");
		discoveredGroupsObj2.setAdditionalGroupDetails(additionalGroupDetails);
		return discoveredGroupsObj2;
	}
	
	public DiscoveredGroups packDiscoveredGroups4() {
		DiscoveredGroups discoveredGroupsObj2 = new DiscoveredGroups();
		AdditionalGroupDetails additionalGroupDetails = new AdditionalGroupDetails();
		List<String> applianceIdsList = new ArrayList<String>();
		applianceIdsList.add(packDiscoveredAppliances2().getApplianceId());//分组�?包含设备ID的数组，要求设备ID必须是已经发现的设备中的ID，否则会同步失败，每个分组设备ID数量不超�?50�?
		discoveredGroupsObj2.setGroupName("安防模式");//用户用来识别分组的名�?, 
		discoveredGroupsObj2.setGroupNotes("安防模式");
		discoveredGroupsObj2.setApplianceIds(applianceIdsList);
		additionalGroupDetails.setExtraDetail1("安防模式");
		//additionalGroupDetails.setExtraDetail2("分组控制艾韵智能三楼办公室中央空�?2");
		//additionalGroupDetails.setExtraDetail3("分组控制艾韵智能三楼办公室中央空�?3");
		//additionalGroupDetails.setExtraDetail4("分组控制艾韵智能三楼办公室中央空�?4");
		discoveredGroupsObj2.setAdditionalGroupDetails(additionalGroupDetails);
		return discoveredGroupsObj2;
	}
	
	/****
	 * Payload信息 payload对象
	 */
	public Payload packPayload() {
		Payload payloadObj = new Payload();
		List<DiscoveredAppliances> discoveredAppliancesList = new ArrayList<DiscoveredAppliances>();
		List<DiscoveredGroups> discoveredGroupsList = new ArrayList<DiscoveredGroups>();
		//设备1
		discoveredAppliancesList.add(packDiscoveredAppliances1());
		discoveredGroupsList.add(packDiscoveredGroups1());
		//设备2
		discoveredAppliancesList.add(packDiscoveredAppliances2());
		discoveredGroupsList.add(packDiscoveredGroups2());
		
		discoveredAppliancesList.add(packDiscoveredAppliances3());
		discoveredGroupsList.add(packDiscoveredGroups3());

		discoveredAppliancesList.add(packDiscoveredAppliances4());
		discoveredGroupsList.add(packDiscoveredGroups4());
		
		discoveredAppliancesList.add(packDiscoveredAppliances5());
		
		payloadObj.setDiscoveredAppliances(discoveredAppliancesList);
		payloadObj.setDiscoveredGroups(discoveredGroupsList);
		return payloadObj;
	}
	
	public Root packRoot() {
		Root root = new Root();
		root.setHeader(packHeader());
		root.setPayload(packPayload());
		return root;
	}
	
	public static void main(String[] args) {
		System.out.println(new JSONObject().toJSONString(new BaiduDiscoveryResponse().packRoot()));
	}
	
}
