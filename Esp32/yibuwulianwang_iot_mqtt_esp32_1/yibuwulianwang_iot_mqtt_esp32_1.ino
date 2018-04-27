#ifndef UNIT_TEST
#include <Arduino.h>
#endif

#include <WiFi.h>
//https://pubsubclient.knolleary.net/api.html#publish1
#include <PubSubClient.h>
#include <ArduinoJson.h>

#define Taideng 23

String wifiSSID = "22@A532";//WIFISSID
String wifiPASS = "VkhKyq8k";//WIFI密码

const char* mqtt_server = "yibuwulianwang.com";
const int mqttport  = 1883;   //10086端口号
const char* sbid = "NodeMCU"; //1086设备ID
const char* cpid = "123509"; //产品ID
const char* jqxx = "sf123456"; //鉴权信息
const char* topic = "yibuwulianwang";

WiFiClient espClient;
PubSubClient client(espClient);

//Wifi连接
int setup_wifi() {
  delay(10);
  //WiFi.begin();
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ssid:");
  Serial.println(wifiSSID.c_str());
  Serial.print("Connecting to password:");
  Serial.println(wifiPASS.c_str());
  WiFi.begin(wifiSSID.c_str(), wifiPASS.c_str());
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println(WiFi.status());
    Serial.print(".");
  }
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  return 1;
}

//订阅消息回调
void callback(char* topic, byte* payload, unsigned int length) {
  //判断订阅主题设置GPIO
  String data = "";
  for (int i = 0; i < length; i++) {
    data = data + (char)payload[i];
  }
  Serial.print("data:");
  Serial.println(data);
  StaticJsonBuffer<2000> jsonBuffer;
  JsonObject& getJsonVolue = jsonBuffer.parseObject(data);
  if (!getJsonVolue.success()) {
    Serial.println("parseObject() failed");
    return;
  }
  //根据key获得volue
  String  requestName  = getJsonVolue["name"];
  String  deviceId  = getJsonVolue["deviceId"];
  if ( deviceId == "Taideng") {
    if (requestName == "TurnOn" ) {
      openPin(Taideng);
    } else if (requestName == "TurnOff") {
      closePin(Taideng);
    }
  }
}

//打开操作
void openPin(int pin) {
  digitalWrite(pin, HIGH);
  //std::string https://blog.csdn.net/rongrongyaofeiqi/article/details/52442169
  String str =  "NodeMCU  Write HIGH"+ pin;     //初始化string类型，并具体赋值 
  const char* constc = nullptr;         //初始化const char*类型，并赋值为空
  constc = str.c_str();                //string类型转const char*类型
  //printf_s("%s\n", str.c_str());        //打印string类型数据 .c_str()
  //printf_s("%s\n", constc);             //打印const char*类型数据
  publishData("NodeMCU Write HIGH");
  Serial.print("Open:");
  Serial.println(pin);
}
//关闭操作
void closePin(int pin) {
  digitalWrite(pin, LOW);
  String str = "NodeMCU Write LOW Pin:" + pin;     //初始化string类型，并具体赋值
  const char* constc = nullptr;         //初始化const char*类型，并赋值为空
  constc = str.c_str();                //string类型转const char*类型
  //printf_s("%s\n", str.c_str());        //打印string类型数据 .c_str()
  //printf_s("%s\n", constc);             //打印const char*类型数据
  publishData("NodeMCU Write LOW");
  Serial.print("Close:");
  Serial.println(pin);
}

//物联网重连
void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect(sbid, cpid, jqxx)) {
      Serial.println("connected");
      client.subscribe(topic);//订阅设备控制
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

//获取温度湿度并发布给10086
void publishData(const char* str) {
  //client.publish("$dp", (uint8_t*)"NodeMCU", 30, false);
  client.publish(topic, str);
}

void setup() {
  Serial.begin(115200);
  delay(10);
  pinMode(Taideng, OUTPUT);//设置led引脚为输出模式
  if (1 == setup_wifi()) {
    client.setServer(mqtt_server, mqttport);
    client.setCallback(callback);
  };
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
  //  long lastMsg = millis();
  //  delay(11000);
  //  long now = millis();
  //  if (now - lastMsg > 10000) {//定时5秒上报一次温度数据
  //    lastMsg = now;
  //    publishData();
  //  }
}

