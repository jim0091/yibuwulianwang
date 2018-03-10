package com.iot.androidmqttforiot;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/19.
 * Android MQTT Connect OneNET
 */

public class MqttManager {
    private static final String TAG = "MqttManager";
    private MqttAndroidClient mqttAndroidClient;// mqtt客户端
    private MqttConnectOptions mqttConectOption;// mqtt设置
    private MqttCallback mqttCallback;// 客户端回调
    private Context mContext;// 上下文
    private static MainActivity mainActivity;
    private boolean isRunThread = true;

    public MqttAndroidClient getMqttAndroidClient() {
        return mqttAndroidClient;
    }

    //初始化连接
    MqttManager(Context context, MainActivity main) {
        mContext = context;
        mainActivity = main;
        clientConnectServer();
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    //重连
    public void reConnectServer() {
        if(!mqttAndroidClient.isConnected()){
            setRunThread(true);
            clientConnectServer();
        }
    }

    //连接
    private void clientConnectServer() {
        //判断客户端是否创建
        if (mqttAndroidClient == null) {
            mqttConectOption = new MqttConnectOptions();
            mqttConectOption.setUserName(SettingActivity.getProduct_ID());//MQTT连接鉴权
            mqttConectOption.setPassword(SettingActivity.getProduct_APIKey().toCharArray());
            mqttConectOption.setConnectionTimeout(300);//默认连接超时时间，目前无法通过设置修改
            mqttConectOption.setKeepAliveInterval(Integer.parseInt(SettingActivity.getServer_keepAlive()));//会话心跳
            mqttConectOption.setWill(SettingActivity.getEquipment_Topic(), "close".getBytes(), Integer.parseInt(SettingActivity.getServer_Qos()), true);//掉线则通知
            mqttConectOption.setCleanSession(false);
            //初始化客户端
            mqttAndroidClient = new MqttAndroidClient(mContext, ("tcp://" + SettingActivity.getServer_IP() + ":" + SettingActivity.getServer_Port()), SettingActivity.getEquipment_ID());
            //设置回调
            mqttAndroidClient.setCallback(new MqttCallbackManager(mainActivity));
            mainActivity.setEvenEnum(EvenEnum.MQTT_CLIENT_OPEN_TRUE);
            mainActivity.printlnUpTextView("服务器IP：" + SettingActivity.getServer_IP());
            mainActivity.printlnUpTextView("服务器Port：" + SettingActivity.getServer_Port());
            mainActivity.printlnUpTextView("产品ID：" + SettingActivity.getProduct_ID());
            mainActivity.printlnUpTextView("产品Key：" + SettingActivity.getProduct_APIKey());
            mainActivity.printlnUpTextView("设备ID：" + SettingActivity.getEquipment_ID());
            mainActivity.printlnUpTextView("设备Key：" + SettingActivity.getEquipment_APIKey());
            mainActivity.printlnUpTextView("主题：" + SettingActivity.getEquipment_Topic());
        }

//        if (null != mqttAndroidClient && !mqttAndroidClient.isConnected()) {
//            //匿名连接线程
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (isRunThread) {
//                        try {
//                            if (!mqttAndroidClient.isConnected()) {
//                                mqttAndroidClient.connect(mqttConectOption);//连接服务器
//                            }else {
//                                mainActivity.printlnUpTextView("客户端与MQTT服务器连接成功。");
//                                mainActivity.dispachEvent(EvenEnum.MQTT_CONNECT_TRUE);
//                                subscribeMqttTopic();
//                                stopRunThread();
//                            }
//                            Thread.sleep(1000);
//                        } catch (Exception e) {
//                            Log.e(TAG, "run: 连接错误" + e);
//                        }
//                    }
//                }
//            }).start();
//        }
        //判断客户端是否连接服务器
        if (null != mqttAndroidClient && !mqttAndroidClient.isConnected()) {
            Thread mThread = new Thread(new ClientServer());
            mThread.start();
        }
    }

    /**
     * stop thread running
     */
    public void setRunThread(boolean runThread) {
        isRunThread = runThread;
    }

    public boolean isRunThread() {
        return isRunThread;
    }

    /***
     * 内部线程连接客户端和订阅主题
     */
    private class ClientServer implements Runnable {
        @Override
        public void run() {
            mainActivity.setEvenEnum(EvenEnum.MQTT_CONNECT_MQTT_NOW);
            while (isRunThread()) {
                try {
                    Thread.sleep(1000);
                    if (!mqttAndroidClient.isConnected()) {
                        mqttAndroidClient.connect(mqttConectOption);//连接服务器
                    } else {
                        mainActivity.setEvenEnum(EvenEnum.MQTT_CONNECT_TRUE);
                        subscribeMqttTopic();
                        setRunThread(false);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mainActivity.setEvenEnum(EvenEnum.MQTT_EXCEPTION);
                    mainActivity.printlnUpTextView("Thread Client Server 线程连接服务器异常：\n" + e.toString());
                    Log.e(TAG, "run: 连接错误" + e);
                }
            }
        }
    }

    /***
     * 订阅主题
     */
    private void subscribeMqttTopic() {
        try {
            mqttAndroidClient.subscribe(SettingActivity.getEquipment_Topic(), Integer.parseInt(SettingActivity.getServer_Qos()));
            mainActivity.setEvenEnum(EvenEnum.MQTT_SUBSCRIBE_TRUE);
        } catch (Exception e) {
            Log.e(TAG, "subscribeMqttTopic: 订阅主题异常" + e);
            mainActivity.setEvenEnum(EvenEnum.MQTT_SUBSCRIBE_FALSE);
        }
    }

    /****
     * 发布消息
     */
    public void publish(String topic, String payload) {
        MqttMessage msg = new MqttMessage();
        msg.setPayload(payload.getBytes());
        msg.setRetained(true);
        msg.setQos(Integer.parseInt(SettingActivity.getServer_Qos()));
        if (mqttAndroidClient.isConnected() && null != mqttAndroidClient) {
            try {
                mqttAndroidClient.publish(topic, msg);
                mainActivity.setEvenEnum(EvenEnum.MQTT_PUBLISH_TRUE);
                mainActivity.printlnUpTextView("正在发布消息：" + payload);
            } catch (MqttException e) {
                Log.e(TAG, "发布消息失败" + e);
                mainActivity.setEvenEnum(EvenEnum.MQTT_PUBLISH_FALSE);
            }
        } else {
            Log.e(TAG, "网络未连接，尝试重新连接！");
            mainActivity.setEvenEnum(EvenEnum.MQTT_CONNECT_FALSE);
        }
    }

    /***
     * 简单的打包Json格式数据类
     * @param json_key json格式数据报头
     * @param json_volue json格式数据报尾
     */
    public void sendData_Json(String json_key, String json_volue) {
        if (mqttAndroidClient.isConnected() && null != mqttAndroidClient) {
            String msg = "{\"" + json_key + "\":\"" + json_volue + "\"}";//发送Json格式数据：控制指令
            publish(SettingActivity.getEquipment_Topic(), msg);
        } else {
            mainActivity.setEvenEnum(EvenEnum.MQTT_CONNECT_FALSE);
        }
    }

    //数据上传到onenet，不对其他设备发布
    public void sendData_Str(String key, String volue) {
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
        String timeStr = dateFormat.format(date);
        if (mqttAndroidClient.isConnected() && null != mqttAndroidClient) {
            String msg = "{\"datastreams\":[{\"id\":\"" + SettingActivity.getEquipment_Topic() + "\",\"datapoints\":[{\"at\":\"" + timeStr + "\",\"value\":" + "{\"" + key + "\":\"" + volue + "\"}" + "}]}]}";
            int msglen = msg.length();
            buf.append((char) 0x01);
            buf.append((char) 0x00);
            buf.append((char) msglen);
            buf.append(msg);
            publish("$dp", buf.toString());//$dp:表示上传到平台，没有回调
        } else {
            mainActivity.setEvenEnum(EvenEnum.MQTT_CONNECT_FALSE);
        }
    }


    /***
     * 发送数字型数字
     * @param num
     */
    private void sendData_Int(int num) {
        Date date = new Date();
        if (mqttAndroidClient.isConnected() && null != mqttAndroidClient) {
            StringBuffer buf = new StringBuffer();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
            String timeStr = dateFormat.format(date);
            String msg = "{\"datastreams\":[{\"id\":\"" + SettingActivity.getEquipment_Topic() + "\",\"datapoints\":[{\"at\":\"" + timeStr + "\",\"value\":" + num + "}]}]}";
            int msglen = msg.length();
            buf.append((char) 0x01);
            buf.append((char) 0x00);
            buf.append((char) msglen);
            buf.append(msg);
            publish("$dp", buf.toString());//"$dp"被系统保留
        } else {
            mainActivity.setEvenEnum(EvenEnum.MQTT_CONNECT_FALSE);
        }
    }
}
