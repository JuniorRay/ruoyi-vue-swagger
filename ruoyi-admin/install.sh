#!/bin/sh
#<!--Create By Junior 2020/11/25 -->
service='/etc/systemd/system/junior-server.service'

echo '[Unit]' > $service
echo 'Description=SpringBoot Project junior-server Application' >> $service
echo 'After=network.target syslog.target' >> $service
echo '' >> $service
echo '[Service]' >> $service
echo 'Type=forking' >> $service
echo 'WorkingDirectory=/opt/junior-server' >> $service
echo 'ExecStart=/opt/junior-server/start.sh sysd' >> $service
echo 'ExecStop=/opt/junior-server/stop.sh sysd' >> $service
echo 'Restart=always' >> $service
echo '' >> $service
echo '[Install]' >> $service
echo 'WantedBy=multi-user.target' >> $service

systemctl enable junior-server.service


#启动脚本名称
file='start.sh'

echo '#!/bin/bash' > $file
# 启动入口类，该脚本文件用于别的项目时要改这里
echo 'MAIN_CLASS=com.ruoyi.RuoYiApplication' >> $file
echo 'APP_BASE_PATH=$(cd `dirname $0`; pwd)' >> $file
echo 'CP=${APP_BASE_PATH}/conf:${APP_BASE_PATH}/lib/*' >> $file
echo 'java -Xverify:none ${JAVA_OPTS} -cp ${CP} ${MAIN_CLASS} > logs/console.log 2>&1 &' >> $file

#停止脚本名称
file='stop.sh'

echo '#!/bin/bash' > $file
# 启动入口类，该脚本文件用于别的项目时要改这里
echo 'MAIN_CLASS=com.ruoyi.RuoYiApplication' >> $file
echo 'kill `pgrep -f ${MAIN_CLASS}` 2>/dev/null' >> $file

chmod +x *.sh

mkdir logs
