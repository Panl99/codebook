# 目录
- [win10相关](#win10相关)
    - [win10多分屏](#win10多分屏)
    - [win10自带截屏](#win10自带截屏)
    - [xshell6无法连接centos8的静态ip](#xshell6无法连接centos8的静态ip)
    - [xshell6、xftp6强制更新才能用](#xshell6xftp6强制更新才能用)
    - [centos8安装](#centos8安装)
    - [centos8离线安装Nginx](#centos8离线安装Nginx)
- [Chrome使用](#Chrome使用)
    - [网站登录才能复制](#网站登录才能复制)
- [IDEA使用](#IDEA使用)
    - [快捷鍵](#快捷鍵)
    - [idea优化](#idea优化)
        - [启动优化](#启动优化)
        - [编码优化](#编码优化)
- [GitHub](#GitHub)
    - [修改github邮箱](#修改github邮箱)
    - [添加SSH公钥](#添加SSH公钥)


# win10相关
## win10多分屏
```
打开一个界面，按win+左键；
或者，拖动一个界面到显示器左边缘，鼠标接触到左边缘时，会看到一个半透明的背景，松开鼠标即可
```

## win10自带截屏
- 快捷键：`win + shift + s`

## xshell6无法连接centos8的静态ip
``` 
--需要修改宿主机对应的网络适配器的IP地址与centos8所设置的静态ip地址等在同一网段
如：centos8静态ip：192.168.1.20，宿主机对应ip需要在192.168.1.x，子网掩码、网关、dns地址相同即可。
目前：xshell可以ping通虚拟机地址，但虚拟机还无法ping通宿主机ip
```

## xshell6、xftp6强制更新才能用
```
--修改安装目录下nslicense.dll：7F 0C 81 F9 80 33 E1 01 0F 86 81为：7F 0C 81 F9 80 33 E1 01 0F 83 81
xshell/ftp5的为：7F 0C 81 F9 80 33 E1 01 0F 86 80
```

## centos8安装
```
安装net-tools：net-tools软件包包含基本的联网工具，包括ifconfig，netstat，route等。
    连接地址：https://centos.pkgs.org/8/centos-baseos-x86_64/net-tools-2.0-0.51.20160912git.el8.x86_64.rpm.html
    下载地址：http://mirror.centos.org/centos/8/BaseOS/x86_64/os/Packages/net-tools-2.0-0.51.20160912git.el8.x86_64.rpm
    手动安装：rpm -ivh net-tools-2.0-0.51.20160912git.el8.x86_64.rpm
    联网自动安装：yum install net-tools
```

[目录](#目录)


## centos8离线安装Nginx
### 查找镜像包

用压缩软件打开centos8镜像包，

进入baseos/package下：存在软件包

AppStream\Packages：gcc、g++包

![centos8离线安装nginx所需rpm包.png](./resources/static/images/centos8离线安装nginx所需rpm包.png)

### 安装gcc

> 真正安装顺序①②③④⑤⑥⑦⑧⑨⑩

1. ①rpm -ivh libmpc-1.0.2-9.el8.x86_64.rpm --force

2. ②rpm -ivh cpp-8.3.1-4.5.el8.x86_64.rpm --force

3. ③rpm -ivh isl-0.16.1-6.el8.x86_64.rpm --force

4. ⑨rpm -ivh gcc-8.3.1-4.5.el8.x86_64.rpm --force

   - ④rpm -ivh binutils-2.30-58.el8.x86_64.rpm --force
   - ⑧rpm -ivh glibc-devel-2.28-72.el8.x86_64.rpm --force
     - ⑦rpm -ivh glibc-headers-2.28-72.el8.x86_64.rpm --force
       - ⑤rpm -ivh kernel-headers-4.18.0-147.el8.x86_64.rpm --force
     - ⑥rpm -ivh libxcrypt-devel-4.1.1-4.el8.x86_64.rpm --force --nodeps（忽略依赖，跟glibc-devel互相依赖，死循环了）
       - rpm -ivh glibc-devel-2.28-72.el8.x86_64.rpm --force
       - /usr/bin/pkg-config [安装pkg-config](#安装pkg-config)
 

#### 安装pkg-config

1. rpm -ivh pkgconf-pkg-config-1.4.2-1.el8.x86_64.rpm --force
   1. rpm -ivh pkgconf-1.4.2-1.el8.x86_64.rpm --force
      1. rpm -ivh libpkgconf-1.4.2-1.el8.x86_64.rpm --force
   2. rpm -ivh pkgconf-m4-1.4.2-1.el8.noarch.rpm --force

### 安装gcc-g++

1. rpm -ivh libstdc++-devel-8.3.1-4.5.el8.x86_64.rpm --force
2. rpm -ivh gcc-c++-8.3.1-4.5.el8.x86_64.rpm --force

### 安装pcre pcre-devel

1. rpm -ivh pcre-8.42-4.el8.x86_64.rpm --force
2. rpm -ivh pcre-devel-8.42-4.el8.x86_64.rpm --force
   - rpm -ivh pcre-cpp-8.42-4.el8.x86_64.rpm --force
   - rpm -ivh pcre-utf16-8.42-4.el8.x86_64.rpm --force
   - rpm -ivh pcre-utf32-8.42-4.el8.x86_64.rpm --force
   - /usr/bin/pkg-config [安装pkg-config](#安装pkg-config)

### 安装zlib zlib-devel

1. 查询系统已安装：`rpm -qa | grep "zlib"`
   1. zlib-1.2.11-10.el8.x86_64

1. rpm -ivh zlib-devel-1.2.11-10.el8.x86_64.rpm --force

### 安装openssl openssl-devel

1. rpm -ivh openssl-1.1.1c-2.el8.x86_64.rpm --force
2. rpm -ivh openssl-devel-1.1.1c-2.el8.x86_64.rpm --force
   1. rpm -ivh keyutils-libs-devel-1.5.10-6.el8.x86_64.rpm --force
   2. rpm -ivh libcom_err-devel-1.44.6-3.el8.x86_64.rpm --force
   3. rpm -ivh libkadm5-1.17-9.el8.x86_64.rpm --force
   4. rpm -ivh libverto-devel-0.3.0-5.el8.x86_64.rpm --force
   5. rpm -ivh libselinux-devel-2.9-2.1.el8.x86_64.rpm --force
      1. rpm -ivh libsepol-devel-2.9-1.el8.x86_64.rpm --force
      2. pkgconfig(libpcre2-8) [安装pcre2](#安装pcre2)

#### 安装pcre2

1. rpm -ivh pcre2-10.32-1.el8.x86_64.rpm --force
2. rpm -ivh pcre2-devel-10.32-1.el8.x86_64.rpm --force
   1. rpm -ivh pcre2-utf16-10.32-1.el8.x86_64.rpm --force
   2. rpm -ivh pcre2-utf32-10.32-1.el8.x86_64.rpm --force
   
### 安装其他

1. rpm -ivh tar-1.30-4.el8.x86_64.rpm --force
2. rpm -ivh make-4.2.1-9.el8.x86_64.rpm --force

### 安装Nginx

1. tar -zxvf nginx-1.19.6.tar.gz -C /opt/

2. ```shell
   cd nginx-1.19.6
   ./configure --prefix=/opt/nginx --sbin-path=/opt/nginx/sbin/nginx
   make
   make install
   ```
#### 测试
- ps aux|grep nginx

```shell
[root@localhost sbin]# cd /opt/nginx/sbin/
[root@localhost sbin]# ./nginx 
[root@localhost sbin]# curl http://localhost
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
    body {
        width: 35em;
        margin: 0 auto;
        font-family: Tahoma, Verdana, Arial, sans-serif;
    }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>

<p>For online documentation and support please refer to
<a href="http://nginx.org/">nginx.org</a>.<br/>
Commercial support is available at
<a href="http://nginx.com/">nginx.com</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>
[root@localhost sbin]# 

```
#### 防火墙
- rpm -ivh firewalld-0.7.0-5.el8.noarch.rpm --force
- 在 windows 系统中访问 linux 中 nginx ，默认不能访问的，因为防火墙问题
    - 1 ）关闭防火墙
    - 2 ）开放访问的端口号 80 端口
- 查看开放的端口号`firewall-cmd --list-all`
- 设置开放的端口号
    - `firewall-cmd --add-service=http --permanent`
    - `firewall-cmd --add-port=80/tcp --permanent`
- 重启防火墙`firewall-cmd --reload`

[目录](#目录)

# Chrome使用
## 网站登录才能复制
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

[目录](#目录)

# IDEA使用
## 快捷鍵  
- `Ctrl + Alt + L`：格式化代码
- `Alt + Insert`：生成构造方法、Getter、Setter等
- `Ctrl + Shift + u`：大小写字母切换

## idea优化  
### 启动优化  
- 文件位置：`/idea安装位置/bin/idea.exe.vmoptions`  
- 修改参数：
    ```
    -Xms1024m
    -Xmx2048m
    -XX:ReservedCodeCacheSize=500m
    ```
### 编码优化  
- 自动导包：`Settings/Editor/General/Auto Import`勾选Java下all、Add unambiguous...、Optimize imports...
- 导包不合为*：`Settings/Editor/Code Style/Java`选择Imports，将Class count to use import with ‘*’ 和Names count to use static import with ‘*’ 两项设置成9999
- Tab替换为4个空格：`Settings/Editor/Code Style/Java`取消勾选Use tab character，Tabs and Indents设置为4
- 代码提示时忽略输入大小写：`Settings/Editor/General/Code Completion`取消勾选Match case
- 鼠标悬浮提示：`Settings/Editor/Code Editing`勾选Show quick documentation on mouse move
- 打开的文件不折叠，多行显示：`Settings/Editor/General/Editor Tabs`取消勾选Show tabs in one row
    ![idea取消隐藏打开的文件tab.png](resources/static/images/idea取消隐藏打开的文件tab.png)
- 字体设置：`Settings/Editor/Font`
- 注释模板设置：`Settings/Editor/File and Code Templates`选择Includes/File Header
    ```java
    /**
     * @create ${DATE} ${TIME}
     * @auther outman
     **/
    ```
- 编码格式：`Settings/Editor/File Encodings`选择Global Encodings、Project Encodings、Default encoding for properties files(右边自动转换勾上)为UTF-8
- 修改代码提示快捷键：`Settings/Keymap`，先点击Find Shortcut搜索Alt+/(或者选择Main menu/Code/Code Completion/Cyclic Expand Word)，把快捷键释放占用，再在Find Shortcut搜索Ctrl+空格(或者选择Main menu/Code/Code Completion/Basic)修改为Alt+/

[目录](#目录)

# GitHub
## 修改github邮箱
- settings -> Emails: Add email address
- 添加完设置为：Primary 
- **注意**：不要删除旧邮箱，删除后会出现以前的提交记录contributions不显示了(绿色小方块没有了)，恢复方法就是重新加回旧邮箱即可（不需要设置为主邮箱）。

## 添加SSH公钥
1. git bash输入：`ssh-keygen -t rsa -C "xxxxx@xxxxx.com"`
2. 一路回车
3. 查看：`C:\Users\lp\.ssh\id_rsa.pub` 填到gitee/设置/安全设置/SSH公钥
4. 测试gitee：`ssh -T git@gitee.com`

- [gitee-生成/添加SSH公钥](https://gitee.com/help/articles/4181#article-header0)

[目录](#目录)