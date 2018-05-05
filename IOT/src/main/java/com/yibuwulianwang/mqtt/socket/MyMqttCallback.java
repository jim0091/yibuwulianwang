package com.yibuwulianwang.mqtt.socket;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yibuwulianwang.config.Config;
import com.yibuwulianwang.handle.Handle;
import com.yibuwulianwang.json.bool.JsonBoolean;

public class MyMqttCallback implements MqttCallback {
	static int  connects=0;
	public void connectionLost(Throwable cause) {
		if(connects>=5) {
			Handle.printMqttLog("MyMqttCallback.connectionLost()","MQTT多次重连失败，可能存在客户端ID冲突："+Config.EMQTT_CLIEND_ID);
    		try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		connects=0;
		}
        if(!MyMqttClient.getSampleClient().isConnected() && connects<5) {
        	try {
				connects++;
				MyMqttClient.getSampleClient().connect(MyMqttClient.getConnOpts());
        		Handle.printMqttLog("MyMqttCallback.connectionLost()","MQTT正在重连-cause:"+cause.toString());
			} catch (MqttSecurityException e) {
        		Handle.printMqttLog("MyMqttCallback.connectionLost()","MQTT正在重-MqttSecurityException-"+e.getStackTrace());
				//e.printStackTrace();
			} catch (MqttException e) {
				//e.printStackTrace();
        		Handle.printMqttLog("MyMqttCallback.connectionLost()","MQTT正在重连-MqttException-"+e.getStackTrace());
			}
        }
        if(MyMqttClient.getSampleClient().isConnected()) {
    		Handle.printMqttLog("MyMqttCallback.connectionLost()","MQTT重连成功。");
    	}else {
    		Handle.printMqttLog("MyMqttCallback.connectionLost()","MQTT重连失败！");
    	}
    }  

    public void deliveryComplete(IMqttDeliveryToken token) {
		Handle.printMqttLog("MyMqttCallback.deliveryComplete()","MQTT消息发送结果："+token.isComplete());
    }  

    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        System.out.println("接收消息主题 : " + topic);  
//        System.out.println("接收消息Qos : " + message.getQos());  
//        System.out.println("接收消息内容 : " + new String(message.getPayload()));
    	  String str = new String(message.getPayload());
    	if(JsonBoolean.isJSONValid(str)) {
    		//com.yibuwulianwang.mqtt.publish.MqttPayloadRoot payload = JSON.parseObject(str,com.yibuwulianwang.mqtt.publish.MqttPayloadRoot.class);
		com.yibuwulianwang.json.me.MyPayloadJson payload = JSON.parseObject(str,new TypeReference<com.yibuwulianwang.json.me.MyPayloadJson>() {});
        	try {
        		if(!payload.getClientId().equals(Config.EMQTT_CLIEND_ID)) {//判断是不是自己发出去的
            		Handle.printMqttLog("MyMqttCallback.messageArrived()","MQTT收到Json消息："+str);
            		ProcessingMessages.processingMsg(str);
            	}
    		} catch (Exception e) {
    			Handle.printMqttLog("MyMqttCallback.messageArrived()", "Json Analysis Exception");
    		}
    	}else {
    		Handle.printMqttLog("MyMqttCallback.messageArrived()","MQTT收到字符串消息："+str);
    	}
    } 
}
