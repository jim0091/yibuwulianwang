#include <SPI.h>
#include <Ethernet.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>

byte mac[]    = {  0xDE, 0xED, 0xBA, 0xFE, 0xFE, 0xED };
byte server[] = { 182, 61, 13, 69 }; // MQTT服务地址
byte ip[]     = { 192, 168, 1, 31 }; // 设备IP

#define Taideng 16

void callback(char* topic, byte* payload, unsigned int length) {
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

EthernetClient ethClient;
PubSubClient client(server, 1883, callback, ethClient);

void setup()
{
  Ethernet.begin(mac, ip);
  if (client.connect("arduinoClient")) {
    client.publish("yibuwulianwang","hello world");
    client.subscribe("yibuwulianwang");
  }
}

void loop()
{
  client.loop();
}
