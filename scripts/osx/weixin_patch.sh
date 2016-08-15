#!/bin/bash
#
# 这是一个demo shell用于说明如何处理微信的解包和打包
#
java -jar apktool-cli.jar d weixin6322android821.apk -r
echo ">> 解压smali代码完成 "
unzip weixin6322android821.apk r/* -d weixin6322android821/unknown
echo ">> 解压资源文件完成 "
cd weixin6322android821/unknown
echo "==========================="
find r
echo "==========================="
echo ">> 将上文两个横线之间的文件复制到apktool.yml的unknownFiles字段中并保存即可"