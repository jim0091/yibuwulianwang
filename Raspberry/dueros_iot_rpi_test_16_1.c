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

/*对日志抓取和解析指令*/
int Pretreatment()
{
  FILE *file_OpenDuerOSLog=NULL;
  char buff[5120];//定义数组来保存获得的日志内容
  char result[5120];//定义字节组数来保存语音识别结果和DuerOS回复结果
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
        int len = (sizeof(buff)-pos);
        printf("pos:%d,len:%d\n",pos,len);
        char* msgJSON = substring(buff,pos,len);
        printf("%s\n",msgJSON);
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
