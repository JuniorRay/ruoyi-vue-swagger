#!/bin/sh
#<!--Create By Junior 2020/11/25 -->
service='/etc/systemd/system/junior-server.service'

systemctl disable junior-server.service

rm -rf $service
