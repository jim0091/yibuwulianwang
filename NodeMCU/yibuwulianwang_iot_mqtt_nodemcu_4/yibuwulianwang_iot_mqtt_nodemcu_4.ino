#ifndef UNIT_TEST
#include <Arduino.h>
#endif

#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "DHT.h"

#define DHTTYPE DHT11   // 设置库为DHT 11
#define DHTPIN 4 //温度使用 D2
#define Taideng 16 //台灯

String wifiSSID = "22@A532";//WIFISSID
String wifiPASS = "VkhKyq8k";//WIFI密码

const char* mqtt_server = "yibuwulianwang.com";
const int mqttport  = 1883;   //10086端口号
const char* clientId = "NodeMCU"; //1086设备ID
const char* cpid = "123509"; //产品ID
const char* jqxx = "sf123456"; //鉴权信息
const char* topic = "yibuwulianwang";

long lastMsg = 0;

DHT dht(DHTPIN, DHTTYPE);
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
    if (requestName=="TurnOn" ) {
      openPin(Taideng);
    } else if (requestName=="TurnOff") {
      closePin(Taideng);
    }
  }
}

//打开操作
void openPin(int pin) {
  digitalWrite(pin, HIGH);
  Serial.print("Open:");
  Serial.println(pin);
}
//关闭操作
void closePin(int pin) {
  digitalWrite(pin, LOW);
  Serial.print("Close:");
  Serial.println(pin);
}

//物联网重连
void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect(clientId, cpid, jqxx)) {
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
void sendwd() {
  long mil = millis();
  long mic = micros();
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.createObject();
  float hum;//湿度
  float temp;//温度
  // readHumidity() 这里是读取当前的湿度
  hum = dht.readHumidity();
  // readTemperature() 读取当前的温度，单位C
  temp = dht.readTemperature();
  int hum_int;
  hum_int = (int)hum;
  int temp_int;
  temp_int = (int)temp;
  Serial.print("hum:");
  Serial.print(hum_int);
  Serial.print(" temp:");
  Serial.println(temp_int);
  root["messageType"] = "Response";
  root["name"] = "temphum";
  root["messageType"] = "Response";
  root["deviceId"] = "Temphum";
  root["messageType"] = "Response";
  root["name"] = "putTemphum";
  root["publishTime"] =mic;
  root["clientId"] =clientId ;
  root["temperature"] = (String)temp_int;
  root["humidity"] = (String)hum_int;
  char output[500];
  root.printTo(output);
  char tempHumBuf[500];
  //snprintf(tempHumBuf, sizeof(tempHumBuf), "{\"temp\":%d,\"hum\":%d}", hum_int,temp_int);
  snprintf(tempHumBuf, sizeof(tempHumBuf), "%s",output);
  client.publish(topic,tempHumBuf);
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
  long now = millis();
  if (now - lastMsg > 60000) {//定时5秒上报一次温度数据
    lastMsg = now;
    sendwd();
  }
}

