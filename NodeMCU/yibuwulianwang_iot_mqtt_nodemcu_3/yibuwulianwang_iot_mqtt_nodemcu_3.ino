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
const char* sbid = "NodeMCU"; //1086设备ID
const char* cpid = "123509"; //产品ID
const char* jqxx = "sf123456"; //鉴权信息
const char* topic = "yibuwulianwang";

long lastMsg = 0;
char msg[50];
int value = 0;
char tmp[28];
char d[3];
float h;//湿度
float t;//温度
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
void sendwd() {
  // readHumidity() 这里是读取当前的湿度
  h = dht.readHumidity();
  // readTemperature() 读取当前的温度，单位C
  t = dht.readTemperature();
  int ih;
  ih = (int)h;
  int it;
  it = (int)t;
  snprintf(tmp, sizeof(tmp), "{\"wd\":%d,\"sd\":%d}", it, ih);
  uint16_t streamLen = strlen(tmp);
  d[0] = '\x03';
  d[1] = (streamLen >> 8);
  d[2] = (streamLen & 0xFF);
  snprintf(msg, sizeof(msg), "%c%c%c%s", d[0], d[1], d[2], tmp);
  client.publish("$dp", (uint8_t*)msg, streamLen + 3, false);
  client.publish(topic, (uint8_t*)msg, streamLen + 3, false);
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
  if (now - lastMsg > 5000) {//定时5秒上报一次温度数据
    lastMsg = now;
    sendwd();
  }
}

