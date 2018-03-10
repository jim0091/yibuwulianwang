package com.iot.androidmqttforiot;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MqttCallbackManager implements MqttCallback {
    private MainActivity mainActivity;

    MqttCallbackManager(MainActivity main){
        this.mainActivity = main;
    }

    //断开连接
    @Override
    public void connectionLost(Throwable throwable) {
        mainActivity.setEvenEnum(EvenEnum.MQTT_CONNECT_FALSE);
    }
    //接收到消息
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        mainActivity.setEvenEnum(EvenEnum.MQTT_CLIENT_RECEIVE_TRUE);
        DispachMessage(topic, mqttMessage);
    }
    //publish成功后的回调
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        mainActivity.setEvenEnum(EvenEnum.MQTT_SERVER_RECEIVE_TRUE);
    }

    /****
     * 对收到的消息精心处理
     * @param type 主题
     * @param data 数据
     */
    public void DispachMessage(String type, Object data) {
        MqttMessage msg = (MqttMessage) data;
        String payload = new String(msg.getPayload());
        mainActivity.printlnUpTextView("来自主题：" + type + "\n服务质量等级："+msg.getQos()+"\n消息内容：" + String.valueOf(payload));
    }
}
