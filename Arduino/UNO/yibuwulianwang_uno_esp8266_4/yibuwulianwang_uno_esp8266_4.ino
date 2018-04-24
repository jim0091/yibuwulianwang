#include "C:\Users\Administrator\AppData\Local\Arduino15\packages\esp8266\hardware\esp8266\2.3.0\libraries\ESP8266WiFi\src\ESP8266WiFi.h"//<ESP8266wifi.h>

#define esp8266_reset_pin 5
#define SERVER_PORT "2121"
#define SSID "hdy2"
#define PASSWORD "hdy954310"

// 初始化，串口1和ESP8266交互，串口0做调试输出， pin5做reset
ESP8266wifi wifi(Serial1, Serial1, esp8266_reset_pin, Serial);

void processCommand(WifiMessage msg);
uint8_t wifi_started = false;

// 自定义的应用层命令，可根据自身应用修改
const char RST[] PROGMEM = "RST";
const char IDN[] PROGMEM = "*IDN?";

void setup() {

  // 调试串口启动，波特率可随意指定
  Serial.begin(115200);

  // ESP8266默认波特率为115200（可用AT指令修改）
  Serial1.begin(115200);
  while (!Serial) // 等待串口初始化成功
    ;
  Serial1.println("Starting wifi"); // 命令
  Serial.println("Starting wifi");  // 调试log输出

  wifi.setTransportToTCP();       // TCP模式（默认设置）
  wifi.endSendWithNewline(false); // 发送数据自动以\r\n结尾（默认设置）

  wifi_started = wifi.begin();    // WIFI启动
  if (wifi_started) { // 如果WIFI初始化成功
    wifi.connectToAP(SSID, PASSWORD); // 指定用户名密码连接WIFI
    wifi.startLocalServer(SERVER_PORT); // 本机（Arduino）监听2121端口（做服务端）

  } else {
    // ESP8266 isn't working..
    Serial.println("ESP8266 isn't working..");
  }
}

void loop() {

  static WifiConnection *connections;

  // check connections if the ESP8266 is there
  if (wifi_started)
    wifi.checkConnections(&connections); // 检查/获取当前所有连接的状况

  // check for messages if there is a connection
  for (int i = 0; i < MAX_CONNECTIONS; i++) { // 默认最大为3
    if (connections[i].connected) {
      // See if there is a message
      WifiMessage msg = wifi.getIncomingMessage();

      if (msg.hasData) { // 如果有数据
        processCommand(msg);
      }
    }
  }
}

void processCommand(WifiMessage msg) { // 数据处理函数
  char espBuf[MSG_BUFFER_MAX];
  int set;
  char str[16];

  Serial.print(msg.message);

  // 以下为应用层面的处理
  sscanf(msg.message, "%15s %d", str, &set);
  Serial.print(str);
  Serial.println(set);

  if ( !strcmp_P(str, IDN) ) {
    wifi.send(msg.channel, "ESP8266wifi Example");
  }
  // Reset system by temp enable watchdog
  else if ( !strcmp_P(str, RST) ) {
    wifi.send(msg.channel, "SYSTEM RESET...");
    // soft reset by reseting PC
    asm volatile ("  jmp 0");
  }
  // Unknown command
  else {
    wifi.send(msg.channel, "ERR");
  }
}
