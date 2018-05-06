package com.yibuwulianwang.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yibuwulianwang.handle.Code;
import com.yibuwulianwang.handle.Handle;
import com.yibuwulianwang.handle.Status;
import com.yibuwulianwang.json.baidu.control.BaiduControlRoot;
import com.yibuwulianwang.json.bool.JsonBoolean;
import com.yibuwulianwang.mqtt.publish.PublishMqttPayload;
import com.yibuwulianwang.mqtt.socket.MyMqttClient;
import com.yibuwulianwang.skill.ali.PackSkillResponse;
import com.yibuwulianwang.skill.json.ali.AliControlDevicesResponse;
import com.yibuwulianwang.skill.json.ali.AliDiscoveryDevicesResponse;
import com.yibuwulianwang.skill.json.ali.AliQueryDevicesResponse;
import com.yibuwulianwang.skill.json.baidu.BaiduControlResponse;
import com.yibuwulianwang.skill.json.baidu.BaiduDiscoveryResponse;
import com.yibuwulianwang.skill.json.baidu.Payload;

@Controller
@RequestMapping("/api")
public class SmartHomeController {
	
	// 查找
	@RequestMapping(method = RequestMethod.GET)
	private Object getApi() {
		return "api";
	}

	@RequestMapping("/baidu")
	protected void baiduPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		BufferedReader reader = request.getReader();
		String json = reader.readLine();
		//PublishMqttPayload.publishMqtt(json);
		Handle.printRequestLog(request.getRequestURI(), json);
		//String text = JSON.toJSONString(obj); //序列化
		//VO vo = JSON.parseObject("{...}", VO.class); //反序列化
		if (json == null) {
			String jsonStr1=new JSONObject().toJSONString(new Status(request.getRequestURI(),"null",Code.HTTP_API_NULL));
			Handle.printResponseLog(request.getRequestURI(), jsonStr1);
			resp.getWriter().append(jsonStr1);
		}else if(JsonBoolean.isJSONValid(json)) {
			//反序列化
			//com.yibuwulianwang.json.baidu.control.BaiduControlRoot baiduControJson = JSON.parseObject(json,com.yibuwulianwang.json.baidu.control.BaiduControlRoot.class);
			com.yibuwulianwang.json.baidu.control.BaiduControlRoot baiduControJson = JSON.parseObject(json,new TypeReference<com.yibuwulianwang.json.baidu.control.BaiduControlRoot>() {});
				// payload消息
				String namespace = baiduControJson.getHeader().getNamespace();//列表:发现设备，控制设备，查询数据
				String name = baiduControJson.getHeader().getName();// 操作类型
				try {
					if (namespace.equals("DuerOS.ConnectedHome.Discovery")) {
						Handle.printRequestLog(request.getRequestURI(), namespace+"百度技能请求设备发现设备");
						String str = new JSONObject().toJSONString(new BaiduDiscoveryResponse().packRoot());
						Handle.printResponseLog(request.getRequestURI(), str);
						PublishMqttPayload.baiduPublishMqtt(namespace, name,"Null");//namespace.split("\\.")[2]
						resp.getWriter().append(str);
					} else if (namespace.equals("DuerOS.ConnectedHome.Control")) {
						Handle.printRequestLog(request.getRequestURI(), namespace+"百度技能请求设备控制设备");
						String str = new JSONObject().toJSONString(new BaiduControlResponse(name).packRoot());
						Handle.printResponseLog(request.getRequestURI(), str);
						String applianceId = baiduControJson.getPayload().getAppliance().getApplianceId();//设备ID
						PublishMqttPayload.baiduPublishMqtt(namespace,name,applianceId);
						resp.getWriter().append(str);
					} else if (namespace.equals("DuerOS.ConnectedHome.Query")) {
						Handle.printRequestLog(request.getRequestURI(), namespace+"百度技能请求查询设备状态属性");
						String str = new JSONObject().toJSONString(new BaiduControlResponse(name).packRoot());//查询未做响应
						Handle.printResponseLog(request.getRequestURI(), str);
						resp.getWriter().append(str);
					} else {
						Handle.printRequestLog(request.getRequestURI(), namespace+"未知请求");
					}
				} catch (Exception e) {
					resp.getWriter().append(new JSONObject().toJSONString(new Status(request.getRequestURI(),"Json Exception",Code.HTTP_API_EXP)));
				}
			}else {
				resp.getWriter().append(new JSONObject().toJSONString(new Status(request.getRequestURI(),"Payload Exception",Code.HTTP_API_EXP)));
			}
	}

	@RequestMapping("/ali")
	protected void aliPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		BufferedReader reader = request.getReader();
		String json = reader.readLine();
		Handle.printRequestLog(request.getRequestURI(), json);
		if (json == null) {
			String jsonStr1=new JSONObject().toJSONString(new Status(request.getRequestURI(),"null",Code.HTTP_API_NULL));
			Handle.printResponseLog(request.getRequestURI(), jsonStr1);
			resp.getWriter().append(jsonStr1);
		} else {
			// payload消息
			JSONObject ali_json = JSONObject.parseObject(json);
			JSONObject ali_header = ali_json.getJSONObject("header");
			String namespace = ali_header.getString("namespace");// namespace列表:发现设备，控制设备，查询数据
			String name = ali_header.getString("name");// 操作类型
			JSONObject ali_payload = ali_json.getJSONObject("payload");
			try {
				if (namespace.equals("AliGenie.Iot.Device.Discovery")) {
					Handle.printRequestLog(request.getRequestURI(), namespace+"-阿里技能请求设备发现设备");
					String str = new JSONObject().toJSONString(new AliDiscoveryDevicesResponse().packRoot());
					Handle.printResponseLog(request.getRequestURI(), str);
					resp.getWriter().append(str);
				} else if (namespace.equals("AliGenie.Iot.Device.Control")) {
					Handle.printRequestLog(request.getRequestURI(), namespace+"-阿里技能请求设备控制设备");
					String str = new JSONObject().toJSONString(new AliControlDevicesResponse().packControlResponse(ali_payload, name));
					Handle.printResponseLog(request.getRequestURI(), str);
					resp.getWriter().append(str);
				} else if (namespace.equals("AliGenie.Iot.Device.Query")) {
					Handle.printRequestLog(request.getRequestURI(), namespace+"-阿里技能请求查询设备状态属性");
					String str = new JSONObject().toJSONString(new AliQueryDevicesResponse().packQueryResponse(ali_payload, name));
					Handle.printResponseLog(request.getRequestURI(), str);
					resp.getWriter().append(str);
				} else {
					Handle.printRequestLog(request.getRequestURI(), namespace+"-未知请求");
				}
			} catch (Exception e) {
				resp.getWriter().append(new JSONObject().toJSONString(new Status(request.getRequestURI(),"Exception",Code.HTTP_API_EXP)));
			}
		}
	}
	
	
	@RequestMapping("/publish/json")
	protected void publishJson(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		BufferedReader reader = request.getReader();
		String json = reader.readLine();
		Handle.printRequestLog(request.getRequestURI(), json);
		if (json == null) {
			String jsonStr1=new JSONObject().toJSONString(new Status(request.getRequestURI(),"null",Code.HTTP_API_NULL));
			Handle.printResponseLog(request.getRequestURI(), jsonStr1);
			resp.getWriter().append(jsonStr1);
		} else {
			
			try {
				if(JsonBoolean.isJSONValid(json)) {
					PublishMqttPayload.httpAPIPublishMqttPayload(json);//使用http api
					Handle.printRequestLog(request.getRequestURI(),"http api publish json payload:"+json);
					String str = new JSONObject().toJSONString(new Status(request.getRequestURI(), "Http API Publish Mqtt Json Success",Code.HTTP_API_OK));
					Handle.printResponseLog(request.getRequestURI(), str);
					resp.getWriter().append(str);
				}else {
					resp.getWriter().append(new JSONObject().toJSONString(new Status(request.getRequestURI(),"Http API Publish Json Exception",Code.HTTP_API_EXP)));
				}
			} catch (Exception e) {
				resp.getWriter().append(new JSONObject().toJSONString(new Status(request.getRequestURI(),"Http API Publish Mqtt Json Exception",Code.HTTP_API_EXP)));
			}
		}
	}
}
