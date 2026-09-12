#!/bin/sh
if [ "$1" = "build" ];then
    mkdir /home/changsheng/project/project25093/project
    cp -a /home/changsheng/project/project25093/server/. /home/changsheng/project/project25093/project/
    cd /home/changsheng/project/project25093/project
    rm -rf /home/changsheng/project/project25093/server
    echo "执行成功"
fi
