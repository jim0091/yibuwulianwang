#include <WiFi.h>
#include <WiFiClient.h>
#include <WiFiServer.h>
#include <WiFiUdp.h>

#include <SoftwareSerial.h>

#include <Ethernet.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>

SoftwareSerial mySerial(10, 11); // RX, TX 不能用0,1
#define Taideng 16

String wifiSSID = "22@A532";//WIFISSID
String wifiPASS = "VkhKyq8k";//WIFI密码

const char* mqtt_server = "yibuwulianwang.com";
const int mqttport  = 1883;   //10086端口号
const char* sbid = "NodeMCU"; //1086设备ID
const char* cpid = "123509"; //产品ID
const char* jqxx = "sf123456"; //鉴权信息
const char* topic = "yibuwulianwang";

WiFiClient ethClient;

PubSubClient client(ethClient);

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
    if (requestName == "TurnOnRequest" ) {
      openPin(Taideng);
    } else if (requestName == "TurnOffRequest") {
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
void publishPayload() {
  client.publish("$dp", (uint8_t*)"NodeMCU", 30, false);
}


void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(115200);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }
  Serial.println("Goodnight moon!");
  // set the data rate for the SoftwareSerial port
  mySerial.begin(115200);
  mySerial.println("Hello, world!");
  mySerial.println("AT+CWJAP=\"22@A532\",\"VkhKyq8k\"");

  pinMode(Taideng, OUTPUT);//设置led引脚为输出模式
  
  client.setServer(mqtt_server, mqttport);
  client.setCallback(callback);
  client.subscribe(topic,1);
  client.connect(sbid);
}
void loop() { // run over and over
  if (mySerial.available()) {
    Serial.write(mySerial.read());
  }
  if (Serial.available()) {
    mySerial.write(Serial.read());
  }
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
}
