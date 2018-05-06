package com.yibuwulianwang.mqtt.publish;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yibuwulianwang.agreement.InstructionConversion;
import com.yibuwulianwang.config.Config;
import com.yibuwulianwang.handle.Handle;
import com.yibuwulianwang.json.me.MyPayloadJson;
import com.yibuwulianwang.mqtt.socket.MyMqttClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * curl -X POST -v --basic -u "admin:public" -H "Content-Type: application/json" -d 
 * '{"topic":"yibuwulianwang","payload":"hello iot","qos":1,"retain":false,"client_id":"C_1492145414740"}' 
 * http://yibuwulianwang.com:18000/api/v2/mqtt/publish
 */
public class PublishMqttPayload {
	
	private static com.yibuwulianwang.json.me.MyPayloadJson packPayloadRoot(String namespace,String name,String deviceId) {
		MyPayloadJson root = new MyPayloadJson();
		root.setMessageType("Request");
		root.setClientId(Config.EMQTT_CLIEND_ID);
		root.setTopic(Config.EMQTT_TOPIC);
		root.setNamespace(namespace);
		root.setName(name);
		root.setDeviceId(deviceId);
		root.setDeviceType("All");
		//UUID.randomUUID().toString().split("-")[0]
		//new MD5Generator().toString()
		root.setMessageId(UUID.randomUUID().toString());
		root.setPublishTime(Handle.getDate());
		root.setRemarks("Server");
		return root;
	}

	private static BodyRoot packRoot(String str) {
		BodyRoot root = new BodyRoot();
		root.setTopic(Config.EMQTT_TOPIC);
		root.setClient_id(Config.EMQTT_CLIEND_ID);
		root.setQos(1);
		root.setRetain(false);
		root.setPayload(str);
		return root;
	}
	
	public static void httpAPIPublishMqttPayload(String json) {
		final Base64.Encoder encoder = Base64.getEncoder();
		byte[] textByte;
		// 编码
		String encodedText=null;
		try {
			textByte = Config.EMQTT_USER_PASSWORD.getBytes("UTF-8");
			encodedText = encoder.encodeToString(textByte);// 编码
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(3000).build();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

		HttpPost httppost = new HttpPost(Config.EMQTT_HTTP_API_URL);

		httppost.setHeader("Authorization", "Basic " + encodedText);
		httppost.setHeader("Content-Type", "application/json");
		// httppost.setHeader("Content-Length", String.valueOf(postStrBytes.length));

		new JSONObject();
		String postjson = JSON.toJSONString(packRoot(json));
		// 构建消息实体
		StringEntity entity = new StringEntity(postjson, Charset.forName("UTF-8"));
		entity.setContentEncoding("UTF-8");
		// 发送json格式的请求
		entity.setContentType("application/json");
		httppost.setEntity(entity);

		HttpResponse response;
		try {
			response = httpClient.execute(httppost);
			// 消息返回码
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Handle.printResponseLog("publishMqttPayload","REQUEST EMQTT HTTP API ERR:" + statusCode);
			} else {
				Handle.printResponseLog("publishMqttPayload","REQUEST EMQTT HTTP API SUCCESS:" + statusCode);
			}
			HttpEntity httpEntity = response.getEntity();
			String result = null;
			if (httpEntity != null) {
				result = EntityUtils.toString(httpEntity, "utf-8");
				EntityUtils.consume(httpEntity);
				Handle.printResponseLog("publishMqttPayload","EMQTT HTTP API RESPONSE RESULT:"+result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void baiduPublishMqtt(String namespace,String name,String deviceId) {
		new JSONObject();
		String json = JSON.toJSONString(packPayloadRoot(InstructionConversion.getBaiduAnalysisMe(namespace),InstructionConversion.getBaiduAnalysisMe(name),deviceId));
		MyMqttClient.publishMqttPayload(json);
	}
	
	public static void aliPublishMqtt(String namespace,String name,String deviceId) {
		new JSONObject();
		String json = JSON.toJSONString(packPayloadRoot(InstructionConversion.getAliAnalysisMe(namespace),InstructionConversion.getAliAnalysisMe(name),deviceId));
		MyMqttClient.publishMqttPayload(json);
	}
	
}
