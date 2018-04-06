/*
* @Author: Marte
* @Date:   2017-11-16 01:51:24
* @Last Modified by:   Marte
* @Last Modified time: 2017-11-16 01:54:07
*/

#include <stdio.h>

#include <stdio.h>
#include <string.h>


#define CMD "ip a |grep inet |grep -v inet6 |grep -v '127.0.0.1' |sed 's/brd.*$//g' |sed 's/^ *//g' |cut -d ' ' -f2"


void main (void)
{
char cmd[1024], ip_list[256][32];
FILE *pfile;
int ips=0, i;


strcpy (cmd,CMD);
pfile=popen (cmd,"r");


while ( !feof (pfile) )
{
fgets (ip_list[ips], sizeof(char)*32, pfile);
ips++;
}


printf ("IPS: %d\n", ips-1);
for ( i=0; i<(ips-1); i++ )
printf ("IP [%d] is: %s",i, ip_list[i]);


pclose (pfile);
}