package com.yibuwulianwang.mqtt.socket;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSONObject;
import com.yibuwulianwang.config.Config;
import com.yibuwulianwang.handle.Code;
import com.yibuwulianwang.handle.Handle;
import com.yibuwulianwang.handle.Status;
import com.yibuwulianwang.json.bool.JsonBoolean;

@Controller
@RequestMapping("/mqtt")
public class MyMqttClient {
	private static MemoryPersistence persistence = null;
	private static MqttClient sampleClient = null;
	private static MqttConnectOptions connOpts = null;
	private static MqttTopic topic11 = null;
	static {
		try {
			persistence = new MemoryPersistence();
			sampleClient = new MqttClient(Config.BROKER, Config.EMQTT_CLIEND_ID, persistence);
			connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(Config.USER_NAME);
			connOpts.setPassword(Config.USER_PASSWORD.toCharArray());
			// 设置超时
			connOpts.setConnectionTimeout(10);
			// 设置心跳
			connOpts.setKeepAliveInterval(130);
			// 设置回调
			sampleClient.setCallback(new MyMqttCallback());

			topic11 = sampleClient.getTopic(Config.EMQTT_TOPIC);
			// 创建连接
			sampleClient.connect(connOpts);

			if (sampleClient.isConnected()) {
				Handle.printMqttLog("/mqtt", "Mqtt Client Success Connecting To Mqtt Broker Server");
			}

			// 订阅主题
			sampleClient.subscribe(Config.EMQTT_TOPIC, Config.QOS);
			// 断开连接
			// sampleClient.disconnect();

		} catch (MqttException me) {
			Handle.printMqttLog("/mqtt","reason " + me.getReasonCode()+"msg " + me.getMessage()
			+"loc " + me.getLocalizedMessage()+"cause " + me.getCause()+"excep " + me);
			//me.printStackTrace();
		}
	}


	public static void publishMqttPayload(String json) {
		try {
			MqttMessage message = new MqttMessage(json.getBytes());
			message.setRetained(true);//设置是否保留消息
			message.setQos(Config.QOS);
			if (sampleClient.isConnected()) {
				sampleClient.publish(Config.EMQTT_TOPIC, message);
			} else {
				// 客户端连接
				sampleClient.connect(connOpts);
				publishMqttPayload(json);
			}
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
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
					MyMqttClient.publishMqttPayload(json);
					Handle.printRequestLog(request.getRequestURI(),"mqtt publish json payload:"+json);
					String str = new JSONObject().toJSONString(new Status(request.getRequestURI(), "PublishMqttJson Success",Code.HTTP_API_OK));
					Handle.printResponseLog(request.getRequestURI(), str);
					resp.getWriter().append(str);
				}else {
					resp.getWriter().append(new JSONObject().toJSONString(new Status(request.getRequestURI(),"Json Exception",Code.HTTP_API_EXP)));
				}
			} catch (Exception e) {
				resp.getWriter().append(new JSONObject().toJSONString(new Status(request.getRequestURI(),"PublishMqttJson Exception",Code.HTTP_API_EXP)));
			}
		}
	}
	
	public static MqttClient getSampleClient() {
		return sampleClient;
	}

	public static MqttConnectOptions getConnOpts() {
		return connOpts;
	}
}
