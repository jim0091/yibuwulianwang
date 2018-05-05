package com.yibuwulianwang.skill.ali;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
//"互联网给人们的生活带来了诸多的便利，似乎给了更多人不出家门的理由，一个手机�?�一台电脑就�?么都能解决�?�宅男宅女�?�蚁族�?�空巢青年，
//这些名词的背后，是越来越多年轻人迷恋网络的便捷，走入越来越窄、越来越宅的世界。其实，不管是外卖�?�网购�?�电子支付这些技术如何成熟和发达�?
//它们都应该只是服务于生活，�?�不该成为生活的全部。�?�场景日的诞生初心，就是希望引导更多人走出户外，回归现实生活。不管是情侣约会、亲子互动�??
//陪伴亲人还是寻求缘分，对于各类情感生活来说，只靠虚拟的网络世界远远是不够的�?��?�场景日将线上科�?和高新智能设备结合线下生活，创建出个性化的场景，
//更好的满足人们的不同种类的情感需求�?��?�场景日”源于联璧科�?自主打�?�的�?个节日，贯穿联璧“场景互联网生�?��?�理念的系列活动，不断助力场景消费市场，传�?�场景生态�?�维�?
//回归线下情感消费。�?�场景日”源于线下服务型消费或售卖的市场�?求�?�打造的�?个节日，
//由始终走在�?�双创�?�之前的联璧科技通过“场景互联网生�?��?�充分发挥�?�服务�?�的线下商业价�?�，同时也让更多人参与感受到场景式服务带来的高品质生活�??"
public class PackSkillResponse {
	public ReturnValue packReturnValue(String str) {
		ReturnValue returnValue = new ReturnValue();
		returnValue.setReply(str);
		returnValue.setResultType("RESULT");
		List<Actions> actionsList = new ArrayList<Actions>();
		//Actions actions = new Actions();
		//actions.setName("audioPlayGenieSource");
		//Properties properties = new Properties();
		//properties.setAudioGenieId("chuangjingkong");
		//actions.setProperties(properties);
		//actionsList.add(actions);
		returnValue.setActions(actionsList); 
		returnValue.setProperties(null);
		returnValue.setExecuteCode("SUCCESS");
		returnValue.setMsgInfo("");
		return returnValue;
	}
	
	public Root packRoot(String str) {
		Root root  = new Root();
		root.setReturnCode("0");
		root.setReturnErrorSolution("");
		root.setReturnMessage("");
		root.setReturnValue(packReturnValue(str));
		return root;
	}
	
	public static void main(String[] args) {
		System.out.println(new JSONObject().toJSONString(new PackSkillResponse().packRoot("test")));
	}
}