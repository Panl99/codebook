#目录
- [xshell6无法连接centos8的静态ip](#xshell6无法连接centos8的静态ip)
- [xshell6、xftp6强制更新才能用](#xshell6xftp6强制更新才能用)
- [centos8安装](#centos8安装)
- [win10多分屏](#win10多分屏)
- [网站登录才能复制](#网站登录才能复制)

# xshell6无法连接centos8的静态ip
``` 
--需要修改宿主机对应的网络适配器的IP地址与centos8所设置的静态ip地址等在同一网段
如：centos8静态ip：192.168.1.20，宿主机对应ip需要在192.168.1.x，子网掩码、网关、dns地址相同即可。
目前：xshell可以ping通虚拟机地址，但虚拟机还无法ping通宿主机ip
```
# xshell6、xftp6强制更新才能用
```
--修改安装目录下nslicense.dll：7F 0C 81 F9 80 33 E1 01 0F 86 81为：7F 0C 81 F9 80 33 E1 01 0F 83 81
xshell/ftp5的为：7F 0C 81 F9 80 33 E1 01 0F 86 80
```
# centos8安装
```
安装net-tools：net-tools软件包包含基本的联网工具，包括ifconfig，netstat，route等。
    连接地址：https://centos.pkgs.org/8/centos-baseos-x86_64/net-tools-2.0-0.51.20160912git.el8.x86_64.rpm.html
    下载地址：http://mirror.centos.org/centos/8/BaseOS/x86_64/os/Packages/net-tools-2.0-0.51.20160912git.el8.x86_64.rpm
    手动安装：rpm -ivh net-tools-2.0-0.51.20160912git.el8.x86_64.rpm
    联网自动安装：yum install net-tools
```
# win10多分屏
```
打开一个界面，按win+左键；
或者，拖动一个界面到显示器左边缘，鼠标接触到左边缘时，会看到一个半透明的背景，松开鼠标即可
```

# 网站登录才能复制
- 这类网站大多都是通过监听dom的copy事件实现的
- 解决：F12，输入下面代码移除eventlistener。
```
(function() {
  let arr = Array.from(document.querySelectorAll("article.article"));
  if (arr && arr.length <= 0) { return; } 
  let articleContent = arr[0];
  let eventHolder = getEventListeners(articleContent);
  if (!eventHolder || !eventHolder.copy) { return; }
  eventHolder.copy.forEach(e => {
    articleContent.removeEventListener('copy',e.listener)
  })
})();
```