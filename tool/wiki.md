#### 1、xshell6无法连接centos8的静态ip
``` 
--需要修改宿主机对应的网络适配器的IP地址与centos8所设置的静态ip地址等在同一网段
如：centos8静态ip：192.168.1.20，宿主机对应ip需要在192.168.1.x，子网掩码、网关、dns地址相同即可。
目前：xshell可以ping通虚拟机地址，但虚拟机还无法ping通宿主机ip
```
#### 2、xshell6、xftp6强制更新才能用
```
--修改安装目录下nslicense.dll：7F 0C 81 F9 80 33 E1 01 0F 86 81为：7F 0C 81 F9 80 33 E1 01 0F 83 81
xshell/ftp5的为：7F 0C 81 F9 80 33 E1 01 0F 86 80
```
#### 3、centos8安装：
```
安装net-tools：net-tools软件包包含基本的联网工具，包括ifconfig，netstat，route等。
    连接地址：https://centos.pkgs.org/8/centos-baseos-x86_64/net-tools-2.0-0.51.20160912git.el8.x86_64.rpm.html
    下载地址：http://mirror.centos.org/centos/8/BaseOS/x86_64/os/Packages/net-tools-2.0-0.51.20160912git.el8.x86_64.rpm
    手动安装：rpm -ivh net-tools-2.0-0.51.20160912git.el8.x86_64.rpm
    联网自动安装：yum install net-tools
```
#### 4、【win10多分屏】
```
打开一个界面，按win+左键；
或者，拖动一个界面到显示器左边缘，鼠标接触到左边缘时，会看到一个半透明的背景，松开鼠标即可
```