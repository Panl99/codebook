#目录
- [win10相关](#win10相关)
    - [win10多分屏](#win10多分屏)
    - [win10自带截屏](#win10自带截屏)
    - [xshell6无法连接centos8的静态ip](#xshell6无法连接centos8的静态ip)
    - [xshell6、xftp6强制更新才能用](#xshell6xftp6强制更新才能用)
    - [centos8安装](#centos8安装)
- [Chrome使用](#Chrome使用)
    - [网站登录才能复制](#网站登录才能复制)
- [IDEA使用](#IDEA使用)
    - [快捷鍵](#快捷鍵)
    - [idea优化](#idea优化)
        - [启动优化](#启动优化)
        - [编码优化](#编码优化)


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