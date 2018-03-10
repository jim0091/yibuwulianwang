//nodemcu的eps8266库
#include <ESP8266WiFi.h>
//解析JSON的库
#include <ArduinoJson.h>
//mqtt库
#include "Adafruit_MQTT.h"
#include "Adafruit_MQTT_Client.h"

#include <ESP8266WiFiMulti.h>
#include <ESP8266HTTPClient.h>

//定时器之类的
#include <Ticker.h>


#define WIFINAME  "hdy"
#define WIFIPW  "hdy954310"

//LED 引脚
#define LED 16
#define AIO_SERVER      "183.230.40.39"//对应OneNET服务器ip地址
#define AIO_SERVERPORT  6002 //对应OneNET服务器端口
#define AIO_USERNAME    "102504" //对应OneNET的产品ID
#define TOPIC "DuerOS_LED"//主题对应OneNET上的数据流模板

//只需要创建一个产品，一个数据流模板，但是必须是不同的设备
#define AIO_USERID    "20381924"//对应OneNET的设备ID()
#define AIO_KEY   "duerosiotmcu"//对应OneNET的鉴权信息

WiFiClient client;

// Setup the MQTT client class by passing in the WiFi client and MQTT server and login details.
//设置MQTT服务器和用户登录信息，MQTT客户端通过在无线设备登录服务器。
Adafruit_MQTT_Client mqtt(&client, AIO_SERVER, AIO_SERVERPORT, AIO_USERID, AIO_USERNAME, AIO_KEY);

/****************************** Feeds ***************************************/

Adafruit_MQTT_Publish photocell = Adafruit_MQTT_Publish(&mqtt, TOPIC);
// Setup a feed called 'onoff' for subscribing to changes to the button
Adafruit_MQTT_Subscribe onoffbutton = Adafruit_MQTT_Subscribe(&mqtt, TOPIC, MQTT_QOS_1);

/***
  初始化方法，有点类似c的main()，各种初始化都可以写这里
*/
void setup() {
  Serial.begin(115200);//串口通信波特率
  delay(10);//延时函数
  pinMode(BUILTIN_LED, OUTPUT);//nodemcu模块蓝灯
  pinMode(LED, OUTPUT);//设置led引脚为输出模式
  Serial.print("Start search WiFi:");
  Serial.println(WIFINAME);
  WiFi.begin(WIFINAME, WIFIPW);//连接WiFi
  Serial.print("Searching now WiFi:");
  Serial.println(WIFINAME);
  while (WiFi.status() != WL_CONNECTED)//判断WiFi是否连接，知道连接WiFi成功
  {
    delay(500);
    Serial.println("WiFi is in connection...");
  }
  Serial.println("WiFi Connection success！");
  Serial.print("Connected,IP Address:");
  Serial.println(WiFi.localIP());//获取nodemcu IP
  Serial.println(F("Adafruit MQTT demo"));

  onoffbutton.setCallback(onoffcallback);

  // Setup MQTT subscription for time feed.
  mqtt.subscribe(&onoffbutton);

}


//服务器收到消息后的回调，控制端指令就在这里接收处理了
void onoffcallback(char *data, uint16_t len) {
  Serial.println(data);
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.parseObject(data);
  if (!root.success()) {
    Serial.println("parseObject() failed");
    return;
  }
  String  cmd  = root["cmd"];
  //这里可以各种if判断cmd变量收到的是什么指令
  if (cmd.equals("OpenTaideng")) //如果收到打开电视信号
  {
    digitalWrite(LED, 1); //这个是控制gpio口的高低电平的，1是高电平，0是低电平，继电器模块就可以接这里。
  }
  if (cmd.equals("CloseTaideng")) //如果收到打开电视信号
  {
    digitalWrite(LED, 0); //这个是控制gpio口的高低电平的，1是高电平，0是低电平，继电器模块就可以接这里。
  }
}


bool sk;//表示是否已经连接

void loop() {

  //确保链接到MQTT服务还活着（它确保第一次链接和短线自动重连）详情查看MQTT_connect详细定义
  sk = MQTT_connect();

  //等待订阅消息和回调子循环（尽量在这里消耗时间）
  mqtt.processPackets(10000);
  if (sk)
  {
    //ping 服务器来保持mqtt连接活性
    if (! mqtt.ping()) {
      Serial.println("lose mqqt connect!");
      mqtt.disconnect();
    }
  }
}

// 功能连接和重新连接以MQTT服务器（应该在循环函数中调用，如果连接，它会很小心）
bool MQTT_connect() {
  int8_t ret;
  // Stop if already connected.
  if (mqtt.connected()) {
    return true;
  }
  Serial.println("ReConnecting to MQTT... ");
  if (mqtt.connect() == 0)
    Serial.println("MQTT Connected!");
  else
    return false;
  return true;
}

