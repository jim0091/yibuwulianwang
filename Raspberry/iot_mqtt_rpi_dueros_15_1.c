#include <stdio.h>    
#include <stdlib.h> 
#include <string.h> 
#include <errno.h>    
#include <sys/wait.h>   
#include <pthread.h>   
#include <stdarg.h>
#include <time.h>

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

//"打开浏览器","python /home/pi/Desktop/Test/open_chrominum_2.py",
//"打开浏览器","bash /home/pi/myAutoStartTest/Open_Chrominum.sh",
//"打开浏览器","bash chromium-browser&",
//"关闭浏览器","bash  kill $(ps aux | grep -m 1 'chromium-browser')",
//"关闭浏览器","bash  sudo pkill -f chromium-browser",

int  str_leng=11;

char * str[11][2]={
	"关机","bash /home/pi/Desktop/myautostart/Shutdown.sh",
	"重启","bash /home/pi/Desktop/myautostart/Reboot.sh",
	"打开浏览器","bash /home/pi/Desktop/myautostart/Open_Chrominum.sh",
	"关闭浏览器","bash /home/pi/Desktop/myautostart/Close_Chrominum.sh",
	"百度","bash  chromium-browser www.baidu.com&",
	"电影","bash  chromium-browser http://v.baidu.com/movie&",
	"战狼二","chromium-browser https://v.qq.com/x/cover/wi8e2p5kirdaf3j.html?ptag=baidu.video.paymovie&frp=v.baidu.com%2Fmovie_intro%2F&vfm=bdvtx",
	"打开终端","bash  /home/pi/Desktop/myautostart/Open_Lxterminal.sh",
	"关闭终端","bash  /home/pi/Desktop/myautostart/Close_Lxterminal.sh",
	"打开任务管理器","bash  /home/pi/Desktop/myautostart/Open_Lxtask.sh",
	"关闭任务管理器","bash  /home/pi/Desktop/myautostart/Close_Lxtask.sh",
};

int write_log (FILE* pFile, const char *format,...) {
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
 
int main()    
{    
 FILE* pFile = fopen("/home/pi/Desktop/myautostart/My_DuerOS_Linxu_Log.log", "a");
 FILE *fstream=NULL;
 char buff[1024];//定义1K数组来保存获得的日志内容
 char result[1000];//定义1000字节组数来保存语音识别结果
 int strpoint;
 int j=0;
 int x=0,y=0;
 memset(buff,0,sizeof(buff)); //清空数组
 memset(result,0,sizeof(result));
 //打开一个通向tail的管道
 if(NULL==(fstream=popen("tail -f /duer/duer_linux.log","r"))) 
 { 
     fprintf(stderr,"execute command failed: %s",strerror(errno)); 
     return 1; 
 }
 
 char jieguo[1000];//定义1000字节组数来保存语音识别结果
 memset(jieguo,0,sizeof(jieguo));
 
 while (NULL!=fgets(buff, sizeof(buff), fstream))//这里fgets()是阻塞的
 {
 	strpoint=lastIndexOf(&buff[0],"-命中唤醒词");
	if(strpoint != -1)		
	{
		//printf("----------------DuerOS 唤醒----------------\n");
		write_log(pFile, "%s\n", "----------------DuerOS 唤醒----------------");
	}
	else
	{
		strpoint=lastIndexOf(&buff[0],"-识别结果:");
		if(strpoint!=-1)		
		{
			//printf("%d\n",strpoint);
			j=0;
			//下面这段代码用于实现字符串截取功能，提取语音识别结果
			for(j=strpoint+14;buff[j]!='\0';j++)
			{
			result[j-strpoint-14]=buff[j];
			}
			result[j-strpoint-15]='\0';
			//printf("%s%s\n","用户：",result);
			write_log(pFile, "%s%s\n","用户：", result);
				for(x=0;x<str_leng;x++){
					if(NULL != strstr(result,str[x][0])){
						system(str[x][1]);
					}
				}
		}
		
	}
		static int  bool_a;
		char dest[1000];
		if(13==lastIndexOf(&buff[0],"introduction")){	
			//printf("%d\n",strpoint);
			for(y=0;y<sizeof(buff);y++)
			{
				result[y]=buff[y];
			}
			strncpy(dest,result+28,sizeof(buff));
			//printf("%s%s\n","DuerOS：",dest);
			write_log(pFile, "%s%s\n", "DuerOS：",dest);
		}
		else if
		(10==lastIndexOf(&buff[0],"content")){
				//printf("%d\n",strpoint);
				for(y=0;y<sizeof(buff);y++)
				{	
					result[y]=buff[y];
				}
				strncpy(dest,result+20,sizeof(buff));
				//printf("%s%s\n","DuerOS：",dest);
				write_log(pFile, "%s%s\n", "DuerOS：",dest);
		} else{
			continue;
		}
 }
 pclose(fstream);
 fclose(pFile);
 return 0;
}
