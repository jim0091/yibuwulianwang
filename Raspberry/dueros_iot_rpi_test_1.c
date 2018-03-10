#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <signal.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/time.h>

#include <unistd.h>
#include <time.h>
//mqtt协议库
#include "MQTTClient.h"

#define ADDRESS "tcp://183.230.40.39:6002"//这里替换成OneNET的服务器IP和端口号，或者百度物接入IoT的
#define CLIENTID "20369341" //clientID在OneNET上是 设备ID
#define TOPIC "DuerOS_LED"//主题在OneNET上是 数据流模板里面的数据流名称

#define CHANPINID "102504" //产品ID
#define CHANPINPASSWD "hdy954310osled"//授权信息

//#define PAYLOAD "Hello World!"//这里是我们要测试发送的内容

#define QOS 1
#define TIMEOUT 10000L
volatile MQTTClient_deliveryToken deliveredtoken;

//这两行也弄成全局的吧
MQTTClient client;
MQTTClient_connectOptions conn_opts = MQTTClient_connectOptions_initializer;
//这里添加了两行
MQTTClient_message pubmsg = MQTTClient_message_initializer;
MQTTClient_deliveryToken token;

int sign=0;//暂时定一个标志，代表有指令要发送
char cmd[1000];//1K字符数组来保存要stringcmp后的指令。

//二维数组
char *word[180]={
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
  "打开","开",
  "开下","开",
  "关掉","关",
  "关了","关",
  "关上","关",
  "关闭","关",
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
  "开单","开灯"
};

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

//发送消息时候调用
int publish()
{
  pubmsg.payload = cmd;
  pubmsg.payloadlen = strlen(cmd);
  pubmsg.qos = QOS;
  pubmsg.retained = 0;
  MQTTClient_publishMessage(client, TOPIC, &pubmsg, &token);
}

/*对日志抓取和解析指令*/
int Pretreatment()
{
  FILE *fstream=NULL;
  char buff[1024];//定义1K数组来保存获得的日志内容
  char result[1000];//定义1000字节组数来保存语音识别结果
  int strpoint;
  int j=0;
  int i=0;
  memset(buff,0,sizeof(buff)); //清空数组
  memset(result,0,sizeof(result));
  //打开一个通向tail的管道
  if(NULL==(fstream=popen("tail -f /duer/duer_linux.log","r")))
  {
    printf("管道打不开！");
    return 1;
  }
  while (NULL!=fgets(buff, sizeof(buff), fstream))//这里fgets()是阻塞的
  {
    strpoint=lastIndexOf(&buff[0],"-命中唤醒词");
    if(strpoint!=-1)
    {
      sprintf(cmd,"%s", "{\"cmd\":\"WakeUP\"}");//想服务器后台发送数据的方法
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
        printf("识别结果为：%s\n",result);
        for(i=0;i<180;i+=2)replace(result,word[i],word[i+1]);
        printf("替换后的结果：%s\n",result);

        if(NULL != strstr(result,"台灯开") || NULL != strstr(result,"开台灯")){
          printf("*************开台灯************\n");
          sprintf(cmd,"%s", "{\"cmd\":\"OpenTD\"}");
          sign=1;
        }
        else if
        (NULL != strstr(result,"关台灯") || NULL != strstr(result,"台灯关")){
          printf("-------------------关台灯--------------------\n");
          sprintf(cmd,"%s", "{\"cmd\":\"CloseTD\"}");
          sign=1;
        }

        // if((result,"开台灯")|| stringcmp(result,"台灯开")) {
        //  printf("*************开台灯************\n");
        //  sprintf(cmd,"%s", "{\"cmd\":\"OpenTD\"}");
        //   sign=1;
        //  }
        // else
        // if(stringcmp(result,"关台灯")|| stringcmp(result,"台灯关")) {
        //  printf("-------------------关台灯--------------------\n");
        //  sprintf(cmd,"%s", "{\"cmd\":\"CloseTD\"}");
        //sign=1;
        //}

      }
    }
    if(sign==1)
    {
      publish();
      sign=0;
    }
  }
  pclose(fstream);
  return 0;
}


int ping()
{
  FILE *fstream=NULL;
  char buff[512];//定义1K数组来保存获得的日志内容
  memset(buff,0,sizeof(buff)); //清空数组
  int k=0;
  int strpoint;
  //打开一个通向tail的管道
  while(k==0)
  {
    if(NULL==(fstream=popen("ping www.baidu.com","r")))
    {
      printf("没有连接网络！");
      return 0;
    }
    while (NULL!=fgets(buff, sizeof(buff), fstream))//这里fgets()是阻塞的
    {
      printf(buff);
      strpoint=lastIndexOf(&buff[0]," bytes from ");
      if(strpoint!=-1)
      {
        k=1;
        break;
      }
    }
    pclose(fstream);
    if(k==0)
    sleep(6);
  }
  return 1;
}



//貌似是发送消息后的回调
void delivered(void *context, MQTTClient_deliveryToken dt)
{
  //printf("Message with token value %d delivery confirmed\n", dt);
  deliveredtoken = dt;
}

//收到消息后的回调
int msgarrvd(void *context, char *topicName, int topicLen, MQTTClient_message *message)
{
  int i;
  char* payloadptr;
  printf("收到消息！\n");
  printf(" 主题为: %s\n", topicName);
  printf(" 消息内容: ");
  payloadptr = message->payload;
  for(i=0; i<message->payloadlen; i++)
  {
    putchar(*payloadptr++);
  }
  putchar('\n');
  MQTTClient_freeMessage(&message);
  MQTTClient_free(topicName);
  return 1;
}
//断线后的回调
void connlost(void *context, char *cause)
{
  printf("\nConnection lost\n");
  printf(" cause: %s\n", cause);
  exit(0);//暂时先强退吧，后面有空把断线重连之类的代码写进去
}
/*主函数*/
int main(int argc, char* argv[])
{
  int rc;
  int ch;
  memset(cmd,0,sizeof(cmd));//清空一下cmd数组
  ping();//一直ping 百度官网，ping通了再执行后面的数据。
  MQTTClient_create(&client, ADDRESS, CLIENTID,
    MQTTCLIENT_PERSISTENCE_NONE, NULL);
    conn_opts.keepAliveInterval = 130;//OneNET的keepalive需要120秒以上
    conn_opts.cleansession = 1;
    conn_opts.username = CHANPINID;//这里添加用户名参数，在OneNET上对应的是 产品ID
    conn_opts.password = CHANPINPASSWD;//这里添加密码参数，在OneNET上对应之前提到过的 鉴权信息
    MQTTClient_setCallbacks(client, NULL, connlost, msgarrvd, delivered);
    if ((rc = MQTTClient_connect(client, &conn_opts)) != MQTTCLIENT_SUCCESS)
    {
      printf("Failed to connect, return code %d\n", rc);
      exit(EXIT_FAILURE);
    }
    printf("Subscribing to topic %s\nfor client %s using QoS%d\n\n"
    "Press Q<Enter> to quit\n\n", TOPIC, CLIENTID, QOS);
    MQTTClient_subscribe(client, TOPIC, QOS);//这里按平常的说法就是 加入QQ群了

    while(1)
    {
      Pretreatment();//这里去读取识别结果和解析文本
    }
    MQTTClient_disconnect(client, 10000);
    MQTTClient_destroy(&client);
    return rc;
  }
