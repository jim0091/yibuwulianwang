#include <HashMap.h>//HashMap库

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

//床头灯引脚
#define CHUANGTOUDENG 16

#define AIO_SERVER      "183.230.40.39"//对应OneNET服务器ip地址
#define AIO_SERVERPORT  6002 //对应OneNET服务器端口
#define AIO_USERNAME    "102504" //对应OneNET的产品ID
#define TOPIC "DuerOS_LED"//主题对应OneNET上的数据流模板
//只需要创建一个产品，一个数据流模板，但是必须是不同的设备
#define AIO_USERID    "20382955"//对应OneNET的设备ID()
#define AIO_KEY   "duerosiotmcu2"//对应OneNET的鉴权信息

WiFiClient client;

// Setup the MQTT client class by passing in the WiFi client and MQTT server and login details.
//设置MQTT服务器和用户登录信息，MQTT客户端通过在无线设备登录服务器。
Adafruit_MQTT_Client mqtt(&client, AIO_SERVER, AIO_SERVERPORT, AIO_USERID, AIO_USERNAME, AIO_KEY);

/****************************** Feeds ***************************************/

Adafruit_MQTT_Publish photocell = Adafruit_MQTT_Publish(&mqtt,TOPIC);
// Setup a feed called 'onoff' for subscribing to changes to the button
Adafruit_MQTT_Subscribe onoffbutton = Adafruit_MQTT_Subscribe(&mqtt,TOPIC, MQTT_QOS_1);

/***
* hashMap初始化
* 泛型集合：设备对应英文名，对应nodemcu引脚
* http://playground.arduino.cc/Code/HashMap
*/
//define HashMap
HashMap<String, int> hashMap;

void setupHashMap(){

  //setup hashmap
  hashMap.put("Chuangtoudeng",CHUANGTOUDENG);
  hashMap.put("test",200);
  hashMap.put("qwer",1234);
  hashMap.put("abc",123);
  hashMap.put("AlphaBeta",20);

  //根据key获得下标
  //Serial.println( hashMap.getIndexOf("Chuangtoudeng"),DEC );
  //根据key获得volue
  //Serial.println( hashMap.getValueOf("Chuangtoudeng") );

  Serial.println(hashMap.valueFor("Chuangtoudeng"), DEC );

}

/***
* 初始化方法，有点类似c的main()，各种初始化都可以写这里
*/
void setup() {
  setupHashMap();
  Serial.begin(115200);//串口通信波特率
  delay(10);//延时函数
  //pinMode(BUILTIN_LED, OUTPUT);//nodemcu模块蓝灯
  pinMode(CHUANGTOUDENG, OUTPUT);//设置led引脚为输出模式
  Serial.print("Start search WiFi:");
  Serial.println(WIFINAME);
  WiFi.begin(WIFINAME, WIFIPW);//连接WiFi
  Serial.print("Searching now WiFi:");
  Serial.println(WIFINAME);
  while (WiFi.status() != WL_CONNECTED)//判断WiFi是否连接，知道连接WiFi成功
  {
    delay(1000);
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

//操作词
String manipulate[5][2]={
  "关闭","Close",
  "打开","Open",
  "获取","Get",
  "设置","Set",
  "返回","Return",
};

//家用电器
String domesticAppliance[83][2]={
  "电视","Dianshi",
  "冰箱","Bingxiang",
  "洗衣机","Xiyiji",
  "音响","Yinxiang",
  "油烟机","Youyanji",
  "热水器","Reshuiqi",
  "消毒柜","Xiaodugui",
  "洗碗机","Xiwanji",
  "红酒柜","Hongjiugui",
  "取暖器","Qunuanqi",
  "空气净化器","Jinghuaqi",
  "空气检测仪","Kongqijianceyi",
  "加湿器","Jiashiqi",
  "吸尘器","Xichenqi",
  "电熨斗","Dianyundou",
  "清洁机","Qingjieji",
  "除湿机","Chushiji",
  "干衣机","Ganyiji",
  "收音机","Shouyinji",
  "电风扇","Dianfengshan",
  "冷风扇","Lengfengshan",
  "净水器","Jingshuiqi",
  "饮水机","Yinhsuiji",
  "榨汁机","Zhazhiji",
  "电饭煲","Dianfanbao",
  "保温盒","Baowenhe",
  "电压力锅","Dianyaliguo",
  "面包机","Mianbaoji",
  "咖啡机","Kafeiji",
  "微波炉","Weibolu",
  "电烤箱","Diankaoxiang",
  "电磁炉","Diancilu",
  "烧烤盘","Shaokaopan",
  "煎蛋器","Jiandanqi",
  "酸奶机","Suannaiji",
  "电炖锅","Siandunguo",
  "煮水壶","Zhushuihu",//电水壶
  "解毒机","Jieduji",
  "解冻机","Jiedongji",
  "煎药壶","Jianyaohu",
  "剃须刀","Tixudao",
  "剃毛器","Timaoqi",
  "电吹风","Dianchuifeng",
  "美容器","Meirongqi",
  "理发器","Lifaqi",
  "美发器","Meifaqi",//弄卷发直发
  "按摩椅","Anmoyi",
  "按摩器","Anmoqi",
  "足浴盆","Zuyupen",
  "血压计","Xueyaji",
  "身高仪","Shengaoyi",
  "体重秤","Tizhongcheng",
  "血糖仪","Xuetangyi",
  "体温计","Tiwenji",
  "排气扇","Paiqishan",
  "洁身器","Jieshenqi",
  "插座","Chazuo",
  "床头灯","Chuangtoudeng",
  "台灯","Taideng",
  "吊灯","Diaodeng",
  "电灯","Diandeng",
  "温湿计","Wenshiji",
  "水龙头","Shuikoutou",
  "国家电网","Guojiadianwang",//控制
  "自来水","Zilaishui",
  "燃气","Ranqi",
  "煤气","Meiqi",
  "暖气","Nuanqi",
  "电表","Dianbiao",//获取表的内容
  "水表","Shuibiao",
  "燃气表","Ranqibiao",
  "煤气表","Meiqibiao",
  "暖气表","Nuanqibiao",
  "门铃","Menling",
  "门锁","Mensuo",
  "窗帘","Chuanglian",
  "安防","Anfang",
  "门磁","Menci",
  "充电器","Chongdianqi",
  "红外报警","Hongwaibaojing",
  "烟雾报警","Yanwubaojing",
  "燃气报警","Ranqibaojing",
  "声音报警","Shengyinbaojing"
};

//获取设备对应的中文名
String getJsonChineseVloue(String vloue){
  String cnVloue;
  for (int i = 0; i < sizeof(domesticAppliance); i++) {
    if (vloue.equals(domesticAppliance[i][1])) {
      cnVloue=domesticAppliance[i][0];
    }
  }
  return cnVloue;
}

//打开操作
void openPin(int pin){
  digitalWrite(pin,HIGH);
  Serial.print("Open:");
  Serial.println(pin);
}
//关闭操作
void closePin(int pin){
  digitalWrite(pin,LOW);
  Serial.print("Close:");
  Serial.println(pin);
}
//获取操作
void getPin(int pin,String volue){
  int readPin = digitalRead(pin);//获取引脚电平值
  Serial.print("Get:");
  Serial.println(pin);
  if(readPin==1){
    returnJson(manipulate[4][1],getJsonChineseVloue(volue)+"已经打开。");
  }else{
    returnJson(manipulate[4][1],getJsonChineseVloue(volue)+"已经关闭。");
  }
}
//设置操作
void setPin(int pin,int setVolue){
  digitalWrite(pin,setVolue);
  Serial.print("Set:");
  Serial.println(pin);
}

//服务器收到消息后的回调，控制端指令就在这里接收处理了
void onoffcallback(char *data, uint16_t len) {
  //JSON 对象是一个无序的名称/值对集合。一个对象以“{”（左括号）开始，“}”（右括号）结束。
  //每个“名称”后跟一个“:”（冒号）；“名称/值对”之间使用“,”（逗号）分隔。
  //StaticJsonBuffer<200> jsonBuffer;
  //char json[] ="{\"sensor\":\"gps\",\"time\":1351824120,\"data\":[48.756080,2.302038]}";
  //JsonObject& root = jsonBuffer.parseObject(json);
  //if (!root.success()) {
  //Serial.println("parseObject() failed");
  //return;
  //}
  //const char* sensor = root["sensor"];
  //long time = root["time"];
  //double latitude = root["data"][0];
  //double longitude = root["data"][1];
  // Print values.
  //Serial.println(sensor);
  //Serial.println(time);
  //Serial.println(latitude, 6);
  //Serial.println(longitude, 6);
  //解析json ：http://blog.csdn.net/y511374875/article/details/73511728

  Serial.print("Receive the Json message:");
  Serial.println(data);
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& getJsonVolue = jsonBuffer.parseObject(data);
  if (!getJsonVolue.success()){
    Serial.println("parseObject() failed");
    return;
  }
  //根据key获得volue
  String  open  = getJsonVolue["Open"];
  String  close  = getJsonVolue["Close"];
  String  get  = getJsonVolue["Get"];
  String  set  = getJsonVolue["Set"];

  if(open != NULL){
    openPin(hashMap.valueFor(open));
  }
  if(close != NULL){
    closePin(hashMap.valueFor(close));
  }
  if(get != NULL){
    getPin(hashMap.valueFor(get),get);
  }
  if(set != NULL){
    setPin(hashMap.valueFor(set),0);
  }
}

//返回查询的结果
void returnJson(String key,String vloue) {
  String returnStr = buildJson(key,vloue);
  char jsonStr[200];
  returnStr.toCharArray(jsonStr,200);//打包成字符数组
  mqtt.publish(TOPIC,jsonStr);//发送消息
}

//格式化成jsoon
String buildJson(String key ,String vloue) {
  String data = "{\"";
  data+=key;
  data+="\":\"";
  data+=vloue;
  data+="\"}";
  return data;
}


bool sk;//表示是否已经连接

void loop() {

  //确保链接到MQTT服务还活着（它确保第一次链接和短线自动重连）详情查看MQTT_connect详细定义
  sk= MQTT_connect();

  //等待订阅消息和回调子循环（尽量在这里消耗时间）
  mqtt.processPackets(10000);
  if(sk)
  {
    //ping 服务器来保持mqtt连接活性
    if(! mqtt.ping()) {
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
  if(mqtt.connect() == 0)
  Serial.println("MQTT Connected!");
  else
  return false;
  return true;
}
