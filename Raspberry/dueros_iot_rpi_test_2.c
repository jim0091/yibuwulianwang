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

#define CHANPINID "102504" //产品ID
#define TOPIC "DuerOS_LED"//主题在OneNET上是 数据流模板里面的数据流名称
#define CLIENTID "20369341" //clientID在OneNET上是 设备ID
#define CHANPINPASSWD "duerosiotrpi"//授权信息

#define QOS 1
#define TIMEOUT 10000L
volatile MQTTClient_deliveryToken deliveredtoken;

MQTTClient client;
MQTTClient_connectOptions conn_opts = MQTTClient_connectOptions_initializer;
MQTTClient_message pubmsg = MQTTClient_message_initializer;
MQTTClient_deliveryToken token;

int sign=0;//暂时定一个标志，代表有指令要发送
char cmd[1000];//1K字符数组来保存要stringcmp后的指令。

char *finalgi;

//二维数组  过滤不必要的词
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
  "开单","开灯",
  "光灯","关灯",
};

int mi_leng=20;
int da_leng = 100;
static char * position="宿舍";//树莓派记录所需要控制设备所在的位置 不发送到受控端 静态的 默认是宿舍

//操作词
char *manipulate[][20]={
  "关闭","Close",
  "打开","Open",
  "关","Close",
  "开","Open",
  "全部关","CloseAll",
  "全部开","OpenAll",
  "查询","Get",
  "获取","Get",
  "看看","Get",
  "设置","Set",
  "改变","Set",
};

//楼层
char *floors[][20]={
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
char *positions[][20]={
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
  "厕所","Cesuo"
  "走廊","Zoulang"
  "楼梯","Louti"
};

//家用电器
char *domesticAppliance[][100]={
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
  "国电网","Guojiadianwang",//控制
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
  "安防","Anfang",
  "门磁","Menci",
  "充电器","Chongdianqi",
  "红外报警","Hongwaibaojing",
  "烟雾报警","Yanwubaojing",
  "燃气报警","Ranqibaojing",
  "声音报警","Shengyinbaojing"
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

/*将str1中出现的所有的str2都替换为str3*/
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
//int index_gi;
//char * final_gi;
//概括指令最后的接口，并返回需要发送到服务端的指令
//char * generalizeInstructions(char * old_gi){
    //final_gi=NULL;
    //判断设备位置
    // for ( index_gi = 0; index_gi < mi_leng ; index_gi++) {//位置  sizeof(positions)
    //   if (NULL != strstr(old_gi,positions[index_gi][0])) {//在一字符串中查找指定的字符串
    //       position=positions[index_gi][0];//记录所控制的设备的位置
    //   }
    // }
    //判断操作属性：控制还是获取
    // for (index_gi = 0; index_gi < mi_leng ; index_gi++) {//sizeof(manipulate)
    //   if (NULL != strstr(old_gi,manipulate[index_gi][0])) {//在一字符串中查找指定的字符串
    //       final_gi=manipulate[index_gi][1];//记录所控制的设备的位置
    //   }
    // }
    //判断电器设备属性
    // for (index_gi = 0; index_gi < da_leng ; index_gi++) {//sizeof(manipulate)
    //   if (NULL != strstr(old_gi,domesticAppliance[index_gi][0])) {//在一字符串中查找指定的字符串
    //       final_gi=domesticAppliance[index_gi][1];
    //       //final_gi=strncat(final_gi,domesticAppliance[index_gi][1],strlen(domesticAppliance[index_gi][1]));//连接两个字符串
    //   }
    // }
    // return final_gi;
//}

char *manipulateStr;
  //判断电器设备属性
void getDomesticAppliances(char *myResult){
    int i;
    for (i = 0; i < 100 ; i++) {//sizeof(manipulate)
      if (NULL != strstr(myResult,domesticAppliance[i][0])) {//在一字符串中查找指定的字符串
          manipulateStr = manipulate[i][1];
          //final_gi=strncat(final_gi,domesticAppliance[index_gi][1],strlen(domesticAppliance[index_gi][1]));//连接两个字符串
      }
    }
    //return finalgi;
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
    printf("读取日志文件的管道打开失败！");
    return 1;
  }
  while (NULL!=fgets(buff, sizeof(buff), fstream))//这里fgets()是阻塞的
  {
    strpoint=lastIndexOf(&buff[0],"-命中唤醒词");
    if(strpoint!=-1)
    {
      sprintf(cmd,"%s", "{\"cmd\":\"DuerOS is Wake Up\"}");//想服务器后台发送数据的方法
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
        printf("识别的结果为：%s\n",result);
        for(i=0;i<180;i+=2)replace(result,word[i],word[i+1]);
        printf("过滤后的结果：%s\n",result);
        getDomesticAppliances(result);
        printf("最终得到指令：%s\n",manipulateStr);
        //printf("设备所在位置：%s\n",position);

        if (NULL != manipulateStr) {
          //sprintf(cmd,"%s", "{\"cmd\":getDomesticAppliance(result)}");
          sprintf(cmd,"%s", "{\"cmd\":\"OpenTD\"}");
          sign=1;//表示需要发送消息
        }

        // if(NULL != strstr(result,"台灯开") || NULL != strstr(result,"开台灯")){
        //   printf("*************开台灯************\n");
        //   sprintf(cmd,"%s", "{\"cmd\":\"OpenTD\"}");
        //   sign=1;
        // }
        // else if
        // (NULL != strstr(result,"关台灯") || NULL != strstr(result,"台灯关")){
        //   printf("-------------------关台灯--------------------\n");
        //   sprintf(cmd,"%s", "{\"cmd\":\"CloseTD\"}");
        //   sign=1;
        // }

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
