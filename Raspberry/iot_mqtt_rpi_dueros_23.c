#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/time.h>
#include <unistd.h>
#include <time.h>
#include <pthread.h>
#include <stdarg.h>
#include <sys/stat.h>
#include <fcntl.h>

#include "MQTTClient.h" //mqtt协议库
#include "cJSON.h" //cJSON库

#define ADDRESS "tcp://183.230.40.39:6002"//这里替换成OneNET的服务器IP和端口号，或者百度物接入IoT的
#define CLIENTID "20369341" //clientID在OneNET上是 设备ID
#define TOPIC "yibuwulianwang"//主题, OneNET上是数据流模板里面的数据流名称
#define CHANPINID "102504" //产品ID
#define CHANPINPASSWD "duerosiotrpi"//授权信息

#define QOS 1 //此标识指示发送消息的交付质量等级
#define TIMEOUT 120  //心跳时间 以秒为单位，定义服务器端从客户端接收消息的最大时间间隔。
volatile MQTTClient_deliveryToken deliveredtoken; //传输代号

MQTTClient client;
MQTTClient_connectOptions conn_opts = MQTTClient_connectOptions_initializer;//连接选项
MQTTClient_message pubmsg = MQTTClient_message_initializer;//消息引用
MQTTClient_deliveryToken token;

//MQTTClient_create（创建客户端）
//MQTTClient_connect（连接服务端）
//MQTTClient_publishMessage（客户端->服务端发送消息）
//MQTTClient_subscribe（客户端订阅某个主题）

//楼层
char *floors[10][2]={
  "一楼","Yilou",
  "二楼","Erlou",
  "三楼","Sanlou",
  "四楼","Silou",
  "五楼","Wulou",
  "六楼","Liulou",
  "七楼","Qilou",
  "八楼","Balou",
  "九楼","Jiulou",
  "十楼","Shilou"
};

//房间
char *positions[13][2]={
  "宿舍","Sushe",
  "卧室","Woshi",
  "客厅","Keting",
  "书房","Shufang",
  "仓库","Cangku",
  "工作室","Gongzuoshi",
  "办公室","Bangongshi",
  "浴室","Yushi",
  "厨房","Chufang",
  "阳台","Yangtai",
  "厕所","Cesuo",
  "走廊","Zoulang",
  "楼梯","Louti",
};

int manipulate_leng=9;

//操作词
char *manipulate[9][2]={
  "关闭","Close",
  "打开","Open",
  "关","Close",
  "开","Open",
  "获取","Get",
  "设置","Set",
  "返回","Return",
  "更新","Updata",
  "登录","Login",
};

int domesticAppliance_leng=85;

//家用电器
char *domesticAppliance[85][2]={
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
  "声音报警","Shengyinbaojing",
  "小度","Xiaodu",
  "小白","Xiaobai"
};

int  str_leng=15;

char * str[15][2]={
  "关机","bash /home/pi/DuerOS-MQTT/myautostart/Shutdown.sh",
  "重启","bash /home/pi/DuerOS-MQTT/myautostart/Reboot.sh",
  "打开浏览器","bash /home/pi/DuerOS-MQTT/myautostart/Open_Chrominum.sh",
  "关闭浏览器","bash /home/pi/DuerOS-MQTT/myautostart/Close_Chrominum.sh",
  "百度","bash chromium-browser www.baidu.com&",
  "电影","bash chromium-browser http://v.baidu.com/movie&",
  "战狼二","chromium-browser https://v.qq.com/x/cover/wi8e2p5kirdaf3j.html?ptag=baidu.video.paymovie&frp=v.baidu.com%2Fmovie_intro%2F&vfm=bdvtx",
  "打开终端","bash /home/pi/DuerOS-MQTT/myautostart/Open_Lxterminal.sh",
  "关闭终端","bash /home/pi/DuerOS-MQTT/myautostart/Close_Lxterminal.sh",
  "打开任务管理器","bash /home/pi/DuerOS-MQTT/myautostart/Open_Lxtask.sh",
  "关闭任务管理器","bash /home/pi/DuerOS-MQTT/myautostart/Close_Lxtask.sh",
  "打开小度","bash /home/pi/DuerOS-MQTT/myautostart/StartDuer.sh",//sudo systemctl start duer&
  "关闭小度","bash /home/pi/DuerOS-MQTT/myautostart/StopDuer.sh",
  "打开小白","bash /home/pi/DuerOS-MQTT/myautostart/StartXiaobai.sh",///home/pi/DuerOS-Python-Client/./wakeup_trigger_start.sh
  "关闭小白","bash /home/pi/DuerOS-MQTT/myautostart/Reboot.sh"//进程刚掉不管用，可以尝试exit()
};

//把收到的json的key转成中文
char* jsonKeyToChinese(char *result){
  int x;
  for(x=0;x<manipulate_leng;x++){
    if(NULL != strstr(result,manipulate[x][1])){
      //system(str[x][1]);
      return manipulate[x][0];
    }
  }
  return "";
}

//把收到的json的Volue转成中文
char* jsonVolueToChinese(char *result){
  int x;
  for(x=0;x<domesticAppliance_leng;x++){
    if(NULL != strstr(result,domesticAppliance[x][1])){
      //system(str[x][1]);
      return domesticAppliance[x][0];
    }
  }
  return "";
}

/**
*生成UUID
* Create random UUID
* @param buf - buffer to be filled with the uuid string
*/
char *random_uuid( char buf[37] )
{
  const char *c = "89ab";
  char *p = buf;
  int n;
  for( n = 0; n < 16; ++n )
  {
    int b = rand()%255;
    switch( n )
    {
      case 6:
      sprintf(p, "4%x", b%15 );
      break;
      case 8:
      sprintf(p, "%c%x", c[rand()%strlen(c)], b%15 );
      break;
      default:
      sprintf(p, "%02x", b);
      break;
    }

    p += 2;
    switch( n )
    {
      case 3:
      case 5:
      case 7:
      case 9:
      *p++ = '-';
      break;
    }
  }
  *p = 0;
  return buf;
}

char* getUUID(){
  static char guid[37];//37
  random_uuid(guid);
  return guid;
}

//发送消息的方法
int publishMsg(char publishData[])
{
  pubmsg.payload = publishData;
  pubmsg.payloadlen = strlen(publishData);
  pubmsg.qos = QOS;
  pubmsg.retained = 0;
  MQTTClient_publishMessage(client, TOPIC, &pubmsg, &token);//发送数据的函数
}

int publishMsgStr(char* publishData)
{
  char json_str[2048];
  memset(json_str,0,sizeof(json_str)); //清空数组
  sprintf(json_str, publishData);////sprintf 是个变参函数。使用sprintf 对于写入buffer的字符数是没有限制的，这就存在了buffer溢出的可能性。
  publishMsg(json_str);
}

//创建自己的log流
FILE *file_OpenMyLog = NULL;

//自己的日志文件
int printfAndWriteMyLogFun (const char *format,...) {
  va_list arg;//VA_LIST 是在C语言中解决变参问题的一组宏，所在头文件：#include <stdarg.h>，用于获取不确定个数的参数。
  int done;
  char pStr[1000];
  memset(pStr,0,sizeof(pStr)); //清空数组
  sprintf(pStr,format);

  va_start (arg, format);//获取可变参数列表的第一个参数的地址（arg是类型为va_list的指针，format是可变参数最左边的参数）：
  //done = vfprintf (stdout, format, arg);
  //VA_ARG宏，获取可变参数的当前参数，返回指定类型并将指针指向下一参数（t参数描述了当前参数的类型）：
  //#define va_arg(ap,t) ( *(t *)((ap += _INTSIZEOF(t)) - _INTSIZEOF(t)) )

  // printf("%d", va_arg(arg,int));
  // printf("%f", va_arg(arg, double));
  // printf("%s", va_arg(arg,char*));

  time_t time_log = time(NULL);
  struct tm* tm_log = localtime(&time_log);
  //时间格式
  fprintf(file_OpenMyLog, "%04d-%02d-%02d %02d:%02d:%02d ", tm_log->tm_year + 1900, tm_log->tm_mon + 1, tm_log->tm_mday, tm_log->tm_hour, tm_log->tm_min, tm_log->tm_sec);

  done = vfprintf (file_OpenMyLog, format, arg);

  va_end (arg);//清空va_list可变参数列表：

  fflush(file_OpenMyLog);
  return done;
}
//打开本地应用
void openNativeApplication(char *result){
  int x;
  for(x=0;x<str_leng;x++){
    if(NULL != strstr(result,str[x][0])){
      system(str[x][1]);
    }
  }
}
//从字符串中截取一段字符串
//https://www.cnblogs.com/SharkBin/p/4234016.html
char* substring(char* ch,int pos,int length)
{
  char* pch=ch;
  //定义一个字符指针，指向传递进来的ch地址。
  char* subch=(char*)calloc(sizeof(char),length+1);
  //通过calloc来分配一个length长度的字符数组，返回的是字符指针。
  int i;
  //只有在C99下for循环中才可以声明变量，这里写在外面，提高兼容性。
  pch=pch+pos;
  //是pch指针指向pos位置。
  for(i=0;i<length;i++)
  {
    subch[i]=*(pch++);
    //循环遍历赋值数组。
  }
  subch[length]='\0';//加上字符串结束符。
  return subch;       //返回分配的字符数组地址。
}
//获取时间
char* getTime(){
  time_t time_T;
  time_T = time(NULL);
  struct tm *tmTime;
  // tm对象格式的时间
  tmTime = localtime(&time_T);
  char* format = "%Y-%m-%d %H:%M:%S";
  static char strTime[100];
  strftime(strTime, sizeof(strTime), format, tmTime);
  return strTime;
}

//打包json
char* packRoot(char* name,char* deviceId)
{
  cJSON * root =  cJSON_CreateObject();
  cJSON_AddItemToObject(root,"messageType", cJSON_CreateString("Request"));
  cJSON_AddItemToObject(root,"clientId", cJSON_CreateString(CLIENTID));
  cJSON_AddItemToObject(root,"topic", cJSON_CreateString(TOPIC));
  cJSON_AddItemToObject(root,"namespace", cJSON_CreateString("Control"));
  cJSON_AddItemToObject(root,"name", cJSON_CreateString(name));
  cJSON_AddItemToObject(root,"deviceId", cJSON_CreateString(deviceId));
  cJSON_AddItemToObject(root,"deviceType", cJSON_CreateString("light"));
  cJSON_AddItemToObject(root,"messageId", cJSON_CreateString(getUUID()));
  cJSON_AddItemToObject(root,"payloadVersion", cJSON_CreateString("1"));
  cJSON_AddItemToObject(root,"publishTime", cJSON_CreateString(getTime()));
  cJSON_AddItemToObject(root,"remarks", cJSON_CreateString("RPI-DuerOS"));
  //printf("%s\n", cJSON_Print(root));
  return cJSON_Print(root);
}

//得出最终发送到服务端的控制设备的指令
char* packInstructions(char *res){
  char *instructions="";//保存开关指令
  char *domesticAppliances="";//保存设备指令
  int i;
  for ( i = 0; i < manipulate_leng; i++) {
    if (NULL != strstr(res,manipulate[i][0])) {
      instructions=manipulate[i][1];
    }
  }
  for ( i = 0; i < domesticAppliance_leng; i++) {
    if (NULL != strstr(res,domesticAppliance[i][0])) {
      domesticAppliances=domesticAppliance[i][1];
    }
  }
  if(instructions!="" && domesticAppliances!=""){
    return packRoot(instructions,domesticAppliances);
  }
  else return "";
}

//掉线回调
void connectionLost(void *context, char *cause)
{
  printfAndWriteMyLogFun("\nConnection OneNet lost! Cause:%s\n", cause);
  //MQTTClient c = (MQTTClient)context;
  //printf("%s -> Callback: connection lost\n", (c == test6_c1) ? "Client-1" : "Client-2");
  MQTTClient_connect(client, &conn_opts);//直接重连，最笨最直接的办法
  //exit(0);//暂时先强退吧，后面有空把断线重连之类的代码写进去
}

//消息输出完成
void deliveryComplete(void *context, MQTTClient_deliveryToken dt)
{
  //printf("Message with token value %d delivery confirmed\n", dt);
  deliveredtoken = dt;
}

//收到消息处理函数
int messageArrived(void *context, char *topicName, int topicLen, MQTTClient_message *message)
{
  int index_JSON[5];
  memset(index_JSON,0,sizeof(index_JSON));//清空一下msg数组
  char* msg;
  int i,a=0;
  char* payloadptr;
  //C语言中定义了一个结构体，然后申明一个指针指向这个结构体，那么我们要用指针取出结构体中的数据，就要用到“->”.
  payloadptr = message->payload;
  printfAndWriteMyLogFun("收到MQTT消息:%s\n",payloadptr);
  //openNativeApplication(json_str);//打开本地应用操作

  MQTTClient_freeMessage(&message);
  MQTTClient_free(topicName);
  return 1;
}

/*对日志抓取和解析指令*/
int Pretreatment()
{
  FILE *file_OpenDuerOSLog=NULL;
  char buff[10240];//定义数组来保存获得的日志内容
  char result[10240];//定义字节组数来保存语音识别结果和DuerOS回复结果
  int strpoint;
  int j=0,i=0,y=0;
  memset(buff,0,sizeof(buff)); //清空数组
  memset(result,0,sizeof(result));
  char* msgHeader = "[App-INFO ]-[2018-04-06 00:47:22.805]-----ApplicationManager notifySdkContext:";
  //打开一个通向tail的管道，1行1行的读取dueros的日志文件
  if(NULL==(file_OpenDuerOSLog=popen("tail -1f /duer/duer_linux.log","r")))
  {
    printf("tail -1f /duer/duer_linux.log Err！\n");
    return 1;
  }
  //[App-INFO ]-[2018-04-06 00:47:22.805]-----ApplicationManager notifySdkContext:{"to_client":{"header":{"namespace":"dlp.screen","name":"RenderVoiceInputText","messageId":"d0871bc0-5d3a-4801-a879-187688b92bef"},"payload":{"text":"打开台灯","type":"FINAL"}}}
  while (NULL!=fgets(buff, sizeof(buff), file_OpenDuerOSLog))//这里fgets()是阻塞的，从文件结构体指针stream中读取数据，每次读取一行。
  {
    if(NULL != strstr(buff,"小度小度"))
    {
      printfAndWriteMyLogFun("%s\n","DuerOS WakeUp !" );
    }
    else if(NULL != strstr(buff,"FINAL") && NULL != strstr(buff,"notifySdkContext"))
    {
      int pos = strlen(msgHeader);
      int len = (strlen(buff)-pos);
      char* msgJSON = substring(buff,pos,len);
      //https://www.cnblogs.com/catgatp/p/6379955.html
      cJSON * root = NULL;
      cJSON * item = NULL;//cjson对象
      root = cJSON_Parse(msgJSON);
      if (!root)
      {
        printf("Error before: [%s]\n",cJSON_GetErrorPtr());
      }
      else
      {
        //printf("%s\n\n", cJSON_Print(root));
        item = cJSON_GetObjectItem(root, "to_client");
        item = cJSON_GetObjectItem(item, "payload");
        item = cJSON_GetObjectItem(item, "text");
        char* jsonKey = item->string;
        char* jsonVolue = item->valuestring;
        //printf("%s:%s\n",jsonKey ,jsonVolue);   //看一下cjson对象的结构体中这两个成员的意思
        //printf("%s\n", packRoot(jsonKey,jsonVolue));
        //printf("%s\n",packInstructions(jsonVolue) );
        printfAndWriteMyLogFun("识别结果：%s\n",jsonVolue);
        char* pushJson = packInstructions(jsonVolue);
        if("" != pushJson){
          publishMsgStr(pushJson);
          printfAndWriteMyLogFun("树莓派发送指令：%s\n",pushJson);
        }
      }
    }
  }
  pclose(file_OpenDuerOSLog);
  return 0;
}

//判断网络使用能ping通百度
int ping()
{
  FILE *file_Ping=NULL;
  char ping_buff[512];//定义1K数组来保存获得的日志内容
  memset(ping_buff,0,sizeof(ping_buff)); //清空数组保存
  int k=0;
  int strpoint;
  //打开一个通向tail的管道
  while(k==0)
  {
    if(NULL==(file_Ping=popen("ping www.baidu.com","r")))
    {
      printfAndWriteMyLogFun("打开网络通道失败，无法连接 MQTT 服务器！");
      return 0;
    }
    while (NULL!=fgets(ping_buff, sizeof(ping_buff), file_Ping))//这里fgets()是阻塞的
    {
      printf(ping_buff);
      if(NULL != strstr(ping_buff," bytes from "))
      {
        k=1;
        break;
      }else{
        printfAndWriteMyLogFun("正在连接 MQTT 服务器中.....");
      }
      if(k==0){
        sleep(6);
      }
    }
    printfAndWriteMyLogFun("网络连接成功，正在连接 MQTT 服务器\n");
    pclose(file_Ping);//关闭通道
  }
  return 1;
}

//连接OneNET服务器
int linkOneNET() {
  int rc;
  int ch;
  MQTTClient_create(&client, ADDRESS, CLIENTID,
    MQTTCLIENT_PERSISTENCE_NONE, NULL);//创建一个MQTT客户端
    conn_opts.keepAliveInterval = 130;//OneNET的keepalive需要120秒以上
    conn_opts.cleansession = 1;
    conn_opts.username = CHANPINID;//这里添加用户名参数，在OneNET上对应的是 产品ID
    conn_opts.password = CHANPINPASSWD;//这里添加密码参数，在OneNET上对应之前提到过的 鉴权信息

    MQTTClient_setCallbacks(client, NULL, connectionLost,messageArrived, deliveryComplete);

    if ((rc = MQTTClient_connect(client, &conn_opts)) != MQTTCLIENT_SUCCESS)//如果连接不成功，可能是产品信息有错
    {
      printfAndWriteMyLogFun("Failed to connect, return code %d\n", rc);
      exit(EXIT_FAILURE);
    }
    printfAndWriteMyLogFun("Subscribing to topic %s\nfor client %s using QoS%d\n\n"
    "Press Q<Enter> to quit\n\n", TOPIC, CLIENTID, QOS);
    //订阅服务请求
    MQTTClient_subscribe(client, TOPIC, QOS);//这里按平常的说法就是 加入QQ群了  订阅消息
    publishMsgStr(packRoot("Login",CLIENTID));
    printfAndWriteMyLogFun("%s%s\n","DuerOS登录MQTT服务器:",packRoot("Login",CLIENTID));
    while(1)
    {
      Pretreatment();//这里去读取识别结果和解析文本，有指令即发送指令
    }
    MQTTClient_disconnect(client, 10000);
    MQTTClient_destroy(&client);
    return rc;
  }
  //获取树莓派温湿度
  double getRaspiTemp(){
    int fd;
    int MAX_SIZE=32;
    double temp = 0;
    char buf[32];

    // 打开/sys/class/thermal/thermal_zone0/temp:获取树莓派CPU温湿度
    fd = open("/sys/class/thermal/thermal_zone0/temp", O_RDONLY);
    if (fd < 0) {
      fprintf(stderr, "failed to open thermal_zone0/temp\n");
      return -1;
    }

    // 读取内容
    if (read(fd, buf, MAX_SIZE) < 0) {
      fprintf(stderr, "failed to read temp\n");
      return -1;
    }

    // 转换为浮点数打印
    temp = atoi(buf) / 1000.0;
    //printf("temp: %.2f\n", temp);

    // 关闭文件
    close(fd);

    return temp;
  }
  /*主函数*/
  void main()
  {
    //创建并且打开自己的log
    char* fileUrl="/home/pi/DuerOS-MQTT/MyLog.log";
    file_OpenMyLog = fopen(fileUrl, "a");//a：所有权限
    printfAndWriteMyLogFun("RaspberryPi Temp: %.2f\n", getRaspiTemp());
    printf("%s%s\n","查看日志：sudo tail -f ",fileUrl);
    if(1==ping()){//一直ping 百度官网，ping通了再执行后面的程序
      linkOneNET();
    }else{
      printfAndWriteMyLogFun("网络连接失败，请检查网络！！！！！！\n");
    }
  }
