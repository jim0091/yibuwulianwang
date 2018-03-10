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


//#include "cJSON.h"

#include "MQTTClient.h" ///mqtt协议库

#define ADDRESS "tcp://183.230.40.39:6002"//这里替换成OneNET的服务器IP和端口号，或者百度物接入IoT的
#define CLIENTID "20369341" //clientID在OneNET上是 设备ID
#define TOPIC "DuerOS_LED"//主题, OneNET上是数据流模板里面的数据流名称
#define CHANPINID "102504" //产品ID
#define CHANPINPASSWD "duerosiotrpi"//授权信息

#define QOS 1 //此标识指示发送消息的交付质量等级
#define TIMEOUT 10000L  //心跳时间 以秒为单位，定义服务器端从客户端接收消息的最大时间间隔。
volatile MQTTClient_deliveryToken deliveredtoken; //传输代号
MQTTClient client;
MQTTClient_connectOptions conn_opts = MQTTClient_connectOptions_initializer;//连接选项
MQTTClient_message pubmsg = MQTTClient_message_initializer;//消息
MQTTClient_deliveryToken token;

//MQTTClient_create（创建客户端）
//MQTTClient_connect（连接服务端）
//MQTTClient_publishMessage（客户端->服务端发送消息）
//MQTTClient_subscribe（客户端订阅某个主题）

int sign=0;//暂时定一个标志，等于1表示有指令要发送

char cmd[1000];//1K字符数组来保存要发送的指令。stringcmp

//创建自己的log
FILE *file_OpenMyLog = NULL;

//一维数组
char *word[200]={
  //两个词一组排好，把左边的替换成右边的，""代表删除左边的词
  "给我","",
  "把我","",
  "的","",
  "就","",
  "是","",
  "麻烦","",
  "请您","",
  "请你","",
  "求你","",
  "求您","",
  "我们","",
  "他们","",
  "她们","",
  "客人","",
  "朋友","",
  "闺蜜","",
  "主人","",
  "你","",
  "您","",
  "我","",
  "把","",
  "一个","",
  "个","",
  "快点","",
  "赶快","",
  "立即","",
  "马上","",
  "迅速","",
  "火速","",
  "开启","打开",
  "开下","打开",
  "关掉","关闭",
  "关了","关闭",
  "关上","关闭",
  "关闭","关闭",
  "一下","",
  "下","",
  "想看","开",
  "要看一下","开",
  "要看","开",
  "马上","",
  "现在","",
  "一会","",
  "儿","",
  "会","",
  "一点","点",
  "点亮","开",
  "点","",
  "电视放开","电视开",
  "放电视","开电视",
  "烧热水","开热水器",
  "烧洗澡水","开热水器",
  "澡澡","澡",
  "想洗澡","开热水器",
  "要洗澡","开热水器",
  "什么","",
  "啥都","",
  "都","",
  "啥","",
  "也","",
  "看不见","开灯",
  "太黑","开灯",
  "太暗","开灯",
  "了","",
  "等一下","",
  "等","",
  "去","",
  "嘛","",
  "呗","",
  "都","",
  "好","",
  "吧","",
  "吗","",
  "啊","",
  "可不","",
  "可以","",
  "可","",
  "能不","",
  "能","",
  "要放电视","开电视",
  "不要放电视","关电视",
  "不开电视","关电视",
  "家里","",
  "家庭","",
  "用电器","电器",
  "电器关","关",
  "全部关","关",
  "电器","",
  "全关","关所有",
  "开单","开灯",
  "光灯","开灯",
  "暗","开灯"
};

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

int manipulate_leng=6;

//操作词
char *manipulate[6][2]={
  "关闭","Close",
  "打开","Open",
  "获取","Get",
  "设置","Set",
  "返回","Return",
  "更新","Updata",
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
  "关机","bash /home/pi/Desktop/myautostart/Shutdown.sh",
  "重启","bash /home/pi/Desktop/myautostart/Reboot.sh",
  "打开浏览器","bash /home/pi/Desktop/myautostart/Open_Chrominum.sh",
  "关闭浏览器","bash /home/pi/Desktop/myautostart/Close_Chrominum.sh",
  "百度","bash chromium-browser www.baidu.com&",
  "电影","bash chromium-browser http://v.baidu.com/movie&",
  "战狼二","chromium-browser https://v.qq.com/x/cover/wi8e2p5kirdaf3j.html?ptag=baidu.video.paymovie&frp=v.baidu.com%2Fmovie_intro%2F&vfm=bdvtx",
  "打开终端","bash /home/pi/Desktop/myautostart/Open_Lxterminal.sh",
  "关闭终端","bash /home/pi/Desktop/myautostart/Close_Lxterminal.sh",
  "打开任务管理器","bash /home/pi/Desktop/myautostart/Open_Lxtask.sh",
  "关闭任务管理器","bash /home/pi/Desktop/myautostart/Close_Lxtask.sh",
  "打开小度","bash /home/pi/Desktop/myautostart/StartDuer.sh",//sudo systemctl start duer&
  "关闭小度","bash /home/pi/Desktop/myautostart/StopDuer.sh",
  "打开小白","bash /home/pi/Desktop/myautostart/StartXiaobai.sh",///home/pi/DuerOS-Python-Client/./wakeup_trigger_start.sh
  "关闭小白","bash /home/pi/Desktop/myautostart/Reboot.sh"//进程刚掉不管用，可以尝试exit()
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

//打开本地应用
void openNativeApplication(char *result){
  int x;
  for(x=0;x<str_leng;x++){
    if(NULL != strstr(result,str[x][0])){
      system(str[x][1]);
    }
  }
}

//自己的日志文件
int writeMyLog (FILE* pFile, const char *format,...) {
  va_list arg;
  int done;

  va_start (arg, format);
  //done = vfprintf (stdout, format, arg);

  time_t time_log = time(NULL);
  struct tm* tm_log = localtime(&time_log);
  //时间格式
  fprintf(pFile, "%04d-%02d-%02d %02d:%02d:%02d ", tm_log->tm_year + 1900, tm_log->tm_mon + 1, tm_log->tm_mday, tm_log->tm_hour, tm_log->tm_min, tm_log->tm_sec);

  done = vfprintf (pFile, format, arg);

  va_end (arg);

  fflush(pFile);
  return done;
}

char *instructions="";//保存开关指令
char *domesticAppliances="";//保存设备指令
//得出最终发送到服务端的控制设备的指令
void setInstructions(char *res){
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
}

/*字符串对比*/
int stringcmp(char *a,char *b)
{
  int i=0;
  for(i=0;a[i]!='\0' && b[i]!='\0';i++)//'\0'：字符串结束符
  if(a[i]!=b[i]){
    break;
  }
  if(a[i]==b[i])
  return 1;
  else
  return 0;
}

/*将str1字符串中第一次出现的str2字符串替换成str3*/
void replaceFirst(char *str1,char *str2,char *str3)
{
  char str4[strlen(str1)+1];
  char *p;
  strcpy(str4,str1);
  if((p=strstr(str1,str2))!=NULL)/*p指向str2在str1中第一次出现的位置*/
  {
    while(str1!=p&&str1!=NULL)/*将str1指针移动到p的位置*/
    {
      str1++;
    }
    str1[0]='\0';/*将str1指针指向的值变成/0,以此来截断str1,舍弃str2及以后的内容，只保留str2以前的内容*/
    strcat(str1,str3);/*在str1后拼接上str3,组成新str1*/
    strcat(str1,strstr(str4,str2)+strlen(str2));/*strstr(str4,str2)是指向str2及以后的内容(包括str2),strstr(str4,str2)+strlen(str2)就是将指针向前移动strlen(str2)位，跳过str2*/
  }
}

/*将str1出现的所有的str2都替换为str3*/
void replace(char *str1,char *str2,char *str3)
{
  while(strstr(str1,str2)!=NULL)
  {
    replaceFirst(str1,str2,str3);
  }
}

/*返回str1中最后一次出现str2的位置(下标),不存在返回-1*/
int lastIndexOf(char *str1,char *str2) {
  char *p=str1;
  int i=0,len=strlen(str2);
  p=strstr(str1,str2);
  if(p==NULL) return -1;
  while(p!=NULL) {
    for(;str1!=p;str1++)i++;
    p=p+len;
    p=strstr(p,str2);
  }
  return i;
}

//发送消息的方法
int publish()
{
  pubmsg.payload = cmd;
  pubmsg.payloadlen = strlen(cmd);
  pubmsg.qos = QOS;
  pubmsg.retained = 0;
  MQTTClient_publishMessage(client, TOPIC, &pubmsg, &token);//发送数据的函数
}

/*对日志抓取和解析指令*/
int Pretreatment()
{
  FILE *file_OpenDuerOSLog=NULL;
  char buff[1024];//定义1K数组来保存获得的日志内容
  char result[1000];//定义1000字节组数来保存语音识别结果
  int strpoint;
  int j=0;
  int i=0;
  int y=0;
  memset(buff,0,sizeof(buff)); //清空数组
  memset(result,0,sizeof(result));
  //打开一个通向tail的管道，读取dueros的日志文件
  if(NULL==(file_OpenDuerOSLog=popen("tail -f /duer/duer_linux.log","r")))
  {
    //printf("管道打不开！");
    printf("打开 /duer/duer_linux.log 失败！\n");
    writeMyLog(file_OpenMyLog, "%s\n", "打开 /duer/duer_linux.log 失败！");
    return 1;
  }

  while (NULL!=fgets(buff, sizeof(buff), file_OpenDuerOSLog))//这里fgets()是阻塞的
  {
    strpoint=lastIndexOf(&buff[0],"-命中唤醒词");
    if(strpoint!=-1)
    {
      //printf指的是字符串格式化命令，主要功能是把格式化的数据写入某个字符串中。
      //sprintf 是个变参函数。使用sprintf 对于写入buffer的字符数是没有限制的，这就存在了buffer溢出的可能性。
      sprintf(cmd,"%s", "{\"DuerOS\":\"WakeUp\"}");//想服务器后台发送数据的方法
      writeMyLog(file_OpenMyLog, "%s\n", "----------DuerOS WakeUp !----------");
      sign=1;
    }
    else
    {
      strpoint=lastIndexOf(&buff[0],"-识别结果:");
      if(strpoint!=-1)
      {
        j=0;
        //下面这段代码用于实现字符串截取功能，提取语音识别结果
        for(j=strpoint+14;buff[j]!='\0';j++)result[j-strpoint-14]=buff[j];
        result[j-strpoint-15]='\0';
        printf("识别结果：%s\n",result);
        writeMyLog(file_OpenMyLog, "识别结果：%s\n",result);
        openNativeApplication(result);
        for(i=0;i<180;i+=2)replace(result,word[i],word[i+1]);
        printf("结果过滤：%s\n",result);
        writeMyLog(file_OpenMyLog, "结果过滤：%s\n",result);
        setInstructions(result);
        if(instructions!="" && domesticAppliances!=""){
          char jsonData[300];
          memset(jsonData,0,sizeof(jsonData)); //清空数组
          strcat(jsonData,"{\"");
          strcat(jsonData,instructions);
          strcat(jsonData,"\":\"");
          strcat(jsonData,domesticAppliances);
          strcat(jsonData,"\"}");
          sprintf(cmd,"%s",jsonData);//打包指令到数组
          //printf("Raspberry Pi Send Data：%s\n",dest);
          printf("树莓派发送指令：%s\n",jsonData);
          writeMyLog(file_OpenMyLog, "树莓派发送指令：%s\n",jsonData);
          sign=1;//表示有指令需要发送
          memset(jsonData,0,sizeof(jsonData)); //清空数组
          instructions="";
          domesticAppliances="";
        }else{
          printf("未发送控制指令！\n");
          writeMyLog(file_OpenMyLog, "%s\n","树莓派未发送控制指令！");
          instructions="";
          domesticAppliances="";
        }
      }
    }
    if(sign==1)
    {
      publish();
      sign=0;
    }

    //以下是抓取dueros的回复
    char duerosAnswer[1000];//定义1000字节组数来保存DuerOS的回答
    //memset(duerosAnswer,0,sizeof(duerosAnswer));
    if(13==lastIndexOf(&buff[0],"introduction")){
      for(y=0;y<sizeof(buff);y++)
      {
        result[y]=buff[y];
      }
      strncpy(duerosAnswer,result+28,sizeof(duerosAnswer));
      printf("%s%s\n","DuerOS回复：",duerosAnswer);
      char DuerOS_JsonData[1000];
      memset(cmd,0,sizeof(cmd)); //清空数组
      memset(DuerOS_JsonData,0,sizeof(DuerOS_JsonData)); //清空数组
      strcat(DuerOS_JsonData,"{\"");
      strcat(DuerOS_JsonData,"DuerOS");
      strcat(DuerOS_JsonData,"\":\"");
      strcat(DuerOS_JsonData,duerosAnswer);
      strcat(DuerOS_JsonData,"\"}");
      printf("%s\n",DuerOS_JsonData );
      sprintf(cmd,"%s",DuerOS_JsonData);//打包指令到数组
      sign=2;//表示有指令需要发送
      memset(DuerOS_JsonData,0,sizeof(DuerOS_JsonData)); //清空数组
      writeMyLog(file_OpenMyLog, "DuerOS回复：%s\n",duerosAnswer);
    }
    else if
    (10==lastIndexOf(&buff[0],"content")){
      for(y=0;y<sizeof(buff);y++)
      {
        result[y]=buff[y];
      }
      strncpy(duerosAnswer,result+20,sizeof(buff));
      printf("%s%s\n","DuerOS回复：",duerosAnswer);
      writeMyLog(file_OpenMyLog, "DuerOS回复：%s\n",duerosAnswer);
    } else{
      continue;
    }
    if(sign==2)
    {
      publish();
      sign=0;
    }

  }
  pclose(file_OpenDuerOSLog);
  fclose(file_OpenMyLog);
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
      printf("网络连接失败，无法连接OneNET！");
      writeMyLog(file_OpenMyLog, "网络连接失败，无法连接OneNET！\n");
      return 0;
    }
    while (NULL!=fgets(ping_buff, sizeof(ping_buff), file_Ping))//这里fgets()是阻塞的
    {
      printf(ping_buff);
      strpoint=lastIndexOf(&ping_buff[0]," bytes from ");
      if(strpoint!=-1)
      {
        k=1;
        break;
      }
    }
    writeMyLog(file_OpenMyLog, "网络连接成功，正在连接OneNET！\n");
    pclose(file_Ping);//关闭通道
    if(k==0)
    sleep(6);
  }
  return 1;
}

//断线后的回调
void connectionLost(void *context, char *cause)
{
  printf("\nConnection OneNet lost! Cause:%s\n", cause);
  writeMyLog(file_OpenMyLog, "Connection OneNet lost! Cause:%s\n", cause);
  //MQTTClient c = (MQTTClient)context;
  //printf("%s -> Callback: connection lost\n", (c == test6_c1) ? "Client-1" : "Client-2");
  linkOneNET();//直接重连，最笨最直接的办法
  //exit(0);//暂时先强退吧，后面有空把断线重连之类的代码写进去
}

//消息输出完成
void deliveryComplete(void *context, MQTTClient_deliveryToken dt)
{
  //printf("Message with token value %d delivery confirmed\n", dt);
  deliveredtoken = dt;
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

//收到消息处理函数
int messageArrived(void *context, char *topicName, int topicLen, MQTTClient_message *message)
{
  int index_JSON[5];
  memset(index_JSON,0,sizeof(index_JSON));//清空一下msg数组
  char* msg;
  int i,a=0;
  char* payloadptr;
  printf("OneNET 收到消息！订阅主题为: %s\n", topicName);
  writeMyLog(file_OpenMyLog, "OneNET 收到消息！订阅主题为: %s \n", topicName);
  printf("消息内容: ");
  //C语言中定义了一个结构体，然后申明一个指针指向这个结构体，那么我们要用指针取出结构体中的数据，就要用到“->”.
  payloadptr = message->payload;
  //printf("%s\n", payloadptr);
  msg=payloadptr;
   for(i=0; i<message->payloadlen; i++)
  {
    //putchar(*payloadptr++);
    //strcat(msg,*payloadptr++);

    if('\"'==*payloadptr++){
      index_JSON[a]=i;
      a=a+1;
    }
  }

  int slen_key = index_JSON[1]-index_JSON[0]-1;
  int slen_volue = index_JSON[3]-index_JSON[2]-1;
  char* key = substring(msg,2,slen_key);
  char* volue = substring(msg,index_JSON[2]+1,slen_volue);
  printf("Json格式: key: %s volue: %s\n", key,volue);
  char json_dest[300];
  memset(json_dest,0,sizeof(json_dest));//清空一下cmd数组
  strcat(json_dest,jsonKeyToChinese(key));
  strcat(json_dest,jsonVolueToChinese(volue));
  char* json_str = json_dest;
  printf("中文格式： %s\n", json_str);
  openNativeApplication(json_str);

  MQTTClient_freeMessage(&message);
  MQTTClient_free(topicName);
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
      printf("Failed to connect, return code %d\n", rc);
      exit(EXIT_FAILURE);
    }
    printf("Subscribing to topic %s\nfor client %s using QoS%d\n\n"
    "Press Q<Enter> to quit\n\n", TOPIC, CLIENTID, QOS);
    //订阅服务请求
    MQTTClient_subscribe(client, TOPIC, QOS);//这里按平常的说法就是 加入QQ群了  订阅消息
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

    // 打开/sys/class/thermal/thermal_zone0/temp
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
  int main(int argc, char* argv[])
  {
    //创建并且打开自己的log
    file_OpenMyLog = fopen("/home/pi/Desktop/myautostart/MyLog.log", "a");//a：所有权限
    memset(cmd,0,sizeof(cmd));//清空一下cmd数组
    printf("RaspberryPiTemp: %.2f\n", getRaspiTemp());
    ping();//一直ping 百度官网，ping通了再执行后面的程序
    int rc = linkOneNET();
    return rc;
  }
