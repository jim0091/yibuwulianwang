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
#include "cJSON.h"


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

int manipulate_leng=7;

//操作词
char *manipulate[7][2]={
  "关闭","Close",
  "打开","Open",
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

//打包json
char* packRoot(char* key,char* volue)
{
    cJSON * root =  cJSON_CreateObject();
    cJSON_AddItemToObject(root,key, cJSON_CreateString(volue));
    printf("%s\n", cJSON_Print(root));

    return cJSON_Print(root);
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
  //打开一个通向tail的管道，读取dueros的日志文件
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
      //sprintf 是个变参函数。使用sprintf 对于写入buffer的字符数是没有限制的，这就存在了buffer溢出的可能性。
      // char jsonData1[300];
      // memset(jsonData1,0,sizeof(jsonData1)); //清空数组
      // sprintf(jsonData1,"%s", "{\"DuerOS\":\"WakeUp\"}");
      printf("%s\n","DuerOS WakeUp !" );
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
        printf("%s\n\n", cJSON_Print(root));
        item = cJSON_GetObjectItem(root, "to_client");//
        item = cJSON_GetObjectItem(item, "payload");
        item = cJSON_GetObjectItem(item, "text");
        char* jsonKey = item->string;
        char* jsonVolue = item->valuestring;
        printf("%s:%s\n",jsonKey ,jsonVolue);   //看一下cjson对象的结构体中这两个成员的意思
        printf("%s\n", packRoot(jsonKey,jsonVolue));
      }
    }
  }
  pclose(file_OpenDuerOSLog);
  return 0;
}


/*主函数*/
void main()
{
  Pretreatment();
}
