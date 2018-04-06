# -*- coding:utf-8 -*-
# File: cputemp.py
#向平台已经创建的数据流发送数据点
import urllib2
import json
import time
import os
import datetime

# Return CPU temperature as a character string
#树莓派CPU温度
def getCPUtemperature():
    res = os.popen('vcgencmd measure_temp').readline()
    return(res.replace("temp=","").replace("'C\n",""))

# Return RAM information (unit=kb) in a list
# Index 0: total RAM  总计
# Index 1: used RAM   使用
# Index 2: free RAM   剩下
def getRAMinfo():
    p = os.popen('free')
    i = 0
    while 1:
        i = i + 1
        line = p.readline()
        if i==2:
            return(line.split()[1:4])

# Return % of CPU used by user as a character string
#树莓派CPU使用率
def getCPUuse():
    return(str(os.popen("top -n1 | awk '/Cpu\(s\):/ {print $2}'").readline().strip()))

# Return information about disk space as a list (unit included)
# Index 0: total disk space
# Index 1: used disk space
# Index 2: remaining disk space
# Index 3: percentage of disk used
#将磁盘空间的信息作为列表返回
def getDiskSpace():
    p = os.popen("df -h /")
    i = 0
    while 1:
        i = i +1
        line = p.readline()
        if i==2:
            return(line.split()[1:5])

#获取IP地址
def get_ip():
    #注意外围使用双引号而非单引号,并且假设默认是第一个网卡,特殊环境请适当修改代码
    ip = os.popen("ifconfig | grep 'inet addr:' | grep -v '127.0.0.1' | cut -d: -f2 | awk '{print $1}' | head -1").read()
    #print(ip)
    return ip;

#获取温湿度
def get_temp():
        # 打开文件
        file = open("/sys/class/thermal/thermal_zone0/temp")
        # 读取结果，并转换为浮点数
        temp = float(file.read()) / 1000
        # 关闭文件
        file.close()
        # 向控制台打印结果
        #print "CPU的温度值为: %.3f" %temp
        # 返回温度值
        return temp


# CPU informatiom
CPU_temp = getCPUtemperature() #树莓派CPU温度
CPU_usage = getCPUuse()#树莓派CPU使用率

# RAM information
# Output is in kb, here I convert it in Mb for readability
RAM_stats = getRAMinfo()
RAM_total = round(int(RAM_stats[0]) / 1000,1)
RAM_used = round(int(RAM_stats[1]) / 1000,1)
RAM_free = round(int(RAM_stats[2]) / 1000,1)

# Disk information
DISK_stats = getDiskSpace()
DISK_total = DISK_stats[0]
DISK_used = DISK_stats[1]
DISK_perc = DISK_stats[3]

if __name__ == '__main__':
    print('')
    print('CPU温度 = '+CPU_temp)
    print('CPU使用率= '+CPU_usage)
    print('')
    print('RAM 总计= '+str(RAM_total)+' MB')
    print('RAM 使用 = '+str(RAM_used)+' MB')
    print('RAM 剩余 = '+str(RAM_free)+' MB')
    print('')
    print('DISK 总计 = '+str(DISK_total)+'B')
    print('DISK 使用 = '+str(DISK_used)+'B')
    print('DISK 使用占百分比 = '+str(DISK_perc))
    print('')
    print('IP = '+get_ip())
    print('CPU 温度= '+str(get_temp())+'°C')
