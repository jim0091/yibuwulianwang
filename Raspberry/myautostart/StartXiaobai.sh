#!bin/bash 
echo 'systemctl stop duer'
sudo systemctl stop duer
echo 'wakeup_trigger_start.sh'
cd /home/pi/DuerOS-Python-Client
./wakeup_trigger_start.sh&
