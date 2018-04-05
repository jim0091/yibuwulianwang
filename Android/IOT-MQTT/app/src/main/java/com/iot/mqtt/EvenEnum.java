package com.iot.mqtt;

/**
 * Created by Administrator on 2017/11/20.
 * 连接状态
 */

public enum EvenEnum {
    MQTT_CONNECT_TRUE,// mqtt连接成功
    MQTT_CONNECT_FALSE,// mqtt连接失败
    MQTT_SUBSCRIBE_TRUE,// 订阅成功
    MQTT_SUBSCRIBE_FALSE,// 订阅失败
    MQTT_PUBLISH_TRUE,// 发布成功
    MQTT_PUBLISH_FALSE,// 发布失败
    MQTT_CLIENT_OPEN_TRUE,// 客户端开启成功
    MQTT_CLIENT_OPEN_FALSE,// 客户端开启失败
    MQTT_RECLIENT_TRUE, // 正在重新连接
    MQTT_RECLIENT_FALSE,// 取消重新连接
    MQTT_SERVER_RECEIVE_TRUE, // 服务器收到消息
    MQTT_CLIENT_RECEIVE_TRUE, // 客户端收到消息
    MQTT_EXCEPTION,  //出现异常
    MQTT_AUTO_CONNECT_TRUE, //自动重连
    MQTT_INTERNET_TRUE, //联网成功
    MQTT_INTERNET_FALSE, //联网失败
    MQTT_SETTING_LOGIN_TRUE, //Android设备登录MQTT服务器信息不为空
    MQTT_SETTING_LOGIN_FLASE, //Android设备登录MQTT服务器信息为空
    MQTT_INTERNET_SETTING_FALSE,//取消前往设置网络
    MQTT_INTERNET_OPEN_FALSE,//网络为开启
    MQTT_CONNECT_MQTT_NOW,// MqttManager正在连接mqtt服务器
}
