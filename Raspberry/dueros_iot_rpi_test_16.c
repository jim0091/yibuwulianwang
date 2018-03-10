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

#include "cJSON.h" //cJSON库

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

void cJSON_JieXi_Me(char* str,char* key){
  char* jsonKey;
  char* jsonVolue;
  cJSON * root = NULL;
  cJSON * item = NULL;//cjson对象
  //（1）首先调用cJSON_Parse()函数，解析JSON数据包，并按照cJSON结构体的结构序列化整个数据包。使用该函数会通过malloc()函数在内存中开辟一个空间，使用完成需要手动释放。
  root = cJSON_Parse(str);

  if (!root)
  {
    printf("Error before: [%s]\n",cJSON_GetErrorPtr());
  }
  else
  {
    // （2）调用cJSON_GetObjectItem()函数，可从cJSON结构体中查找某个子节点名称（键名称），如果查找成功可把该子节点序列化到cJSON结构体中。
    if(NULL != strstr(str,key)){
      printf("%s%s%s\n", "获取cjson对象key",key,"的值:");
      item = cJSON_GetObjectItem(root, key);
      printf("%s\n", cJSON_Print(item));
      // （3）如果需要使用cJSON结构体中的内容，可通过cJSON结构体中的valueint和valuestring取出有价值的内容（即键的值）
      jsonKey=item->string;
      jsonVolue=item->valuestring;
      printf("%s:%s\n",jsonKey, jsonVolue);
    }
    // （4）通过cJSON_Delete()，释放cJSON_Parse()分配出来的内存空间。
    cJSON_Delete(root);
  }
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
  //打开一个通向tail的管道，读取dueros的日志文件
  if(NULL==(file_OpenDuerOSLog=popen("tail -f /duer/duer_linux.log","r")))
  {
    printf("tail -f /duer/duer_linux.log Err！\n");
    return 1;
  }
  while (NULL!=fgets(buff, sizeof(buff), file_OpenDuerOSLog))//这里fgets()是阻塞的
  {
    if(-1 != (strpoint=lastIndexOf(&buff[0],"小度小度")))
    {
      //sprintf 是个变参函数。使用sprintf 对于写入buffer的字符数是没有限制的，这就存在了buffer溢出的可能性。
      // char jsonData1[300];
      // memset(jsonData1,0,sizeof(jsonData1)); //清空数组
      // sprintf(jsonData1,"%s", "{\"DuerOS\":\"WakeUp\"}");
      printf("%s\n","DuerOS WakeUp !" );
    }
    else if( -1 != (strpoint=lastIndexOf(&buff[0],"notifySdkContext:")))
    {
      for(j=strpoint+14;buff[j]!='\0';j++)//"\0"表示字符串结束符
      {
        result[j-strpoint-17]=buff[j];
      }
      result[j-strpoint-15]='\0';
      if(NULL != strstr(result,"FINAL")){
        printf("识别结果：%s\n",result);
        cJSON_JieXi_Me(result,"text");
      }
    }
    else if( -1 != (strpoint=lastIndexOf(&buff[0],"onReceivedDirective:")))
    {
      for(j=strpoint+14;buff[j]!='\0';j++)//"\0"表示字符串结束符
      {
        result[j-strpoint-20]=buff[j];
      }
      result[j-strpoint-15]='\0';
      printf("%d\n", strpoint);
      printf("回复结果：%s\n",result);
      // if(NULL != strstr(result,"FINAL")){
      //   printf("回复结果：%s\n",result);
      // }
    }
    else{
      continue;
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
