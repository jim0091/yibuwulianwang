package com.yibuwulianwang.skill;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSONObject;
import com.yibuwulianwang.handle.Handle;
import com.yibuwulianwang.skill.ali.PackSkillResponse;
import com.yibuwulianwang.skill.json.ali.AliControlDevicesResponse;

//http://doc-bot.tmall.com/docs/doc.htm?spm=0.0.0.0.zmwQjH&treeId=393&articleId=106952&docType=1


//{
//    "sessionId": "08d3167d-1878-4205-9392-633f63038ec5",//会话ID，session内的对话此ID相同
//    "utterance": "场景日是什么",//用户输入语句
//    "skillId": 13174,//技能ID
//    "skillName": "场景日",//技能名称
//    "intentId": 19299,//意图ID
//    "intentName": "场景日",//意图名称
//    "requestData": {},//业务请求附带参数,来自于设备调用语义理解服务额外携带的信息，只做透传
//    "slotEntities": [],//从用户语句中抽取出的slot参数信息
//    "botId": 25852,//应用ID，来自于创建的应用或者技能
//    "domainId": 17280//领域ID
//}

@Controller
@RequestMapping("/ali/skill")
public class AliSkillController {

	private static final String what = "是什么";
	private static final String why = "为什么";
	private static final String how = "怎么样";
	private static final String ayznWhat = "艾韵智能（深圳）有限公司于2008年11月07日成立，专注于场景物联整体解决方案及核心智能设备提供商。"
			+ "通过物联网，云计算，大数据及人工智能等先进技术，基于智能设备前端研发及场景内容服务，打造智能场景物联平台。"
			+ "帮助物联网平台整合前，后端，让平台，商家和用户实现智慧运营，智慧健康，智慧教育，智慧生活的物联体系。";
	private static final String ayznWhere = "深圳市南山区桃源街道塘兴路集悦城A区21栋";
	private static final String ayznTime = "2008年11月07日";
	private static final String ayznWho = "黄安福";

	@RequestMapping("/cjr/what")
	protected void what(HttpServletRequest request, HttpServletResponse resp) {
		Handle.request(request, resp, new PackSkillResponse().packRoot(what));
	}

	@RequestMapping("/cjr/why")
	protected void why(HttpServletRequest request, HttpServletResponse resp) {
		Handle.request(request, resp, new PackSkillResponse().packRoot(why));
	}

	@RequestMapping("/cjr/how")
	protected void how(HttpServletRequest request, HttpServletResponse resp) {
		Handle.request(request, resp, new PackSkillResponse().packRoot(how));
	}

	@RequestMapping("/ayzn/what")
	protected void ayznWhat(HttpServletRequest request, HttpServletResponse resp) {
		Handle.request(request, resp, new PackSkillResponse().packRoot(ayznWhat));
	}

	@RequestMapping("/ayzn/where")
	protected void ayznWhere(HttpServletRequest request, HttpServletResponse resp) {
		Handle.request(request, resp, new PackSkillResponse().packRoot(ayznWhere));
	}

	@RequestMapping("/ayzn/who")
	protected void ayznWho(HttpServletRequest request, HttpServletResponse resp) {
		Handle.request(request, resp, new PackSkillResponse().packRoot(ayznWho));
	}

	@RequestMapping("/ayzn/time")
	protected void ayznTime(HttpServletRequest request, HttpServletResponse resp) {
		Handle.request(request, resp, new PackSkillResponse().packRoot(ayznTime));
	}
}
