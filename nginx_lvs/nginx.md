> [章亦春-Nginx教程](http://openresty.org/download/agentzh-nginx-tutorials-zhcn.html)  
> [深入理解Nginx：模块开发与架构解析（第2版）]
# 一、Nginx
## 1、关于Nginx
#### 1.1、Nginx特点
- **更快：** 不论单次请求还是高峰大量的并发请求，Nginx都可以快速的响应。
- **高扩展性：** Nginx的设计极具扩展性，它完全是由多个不同功能、不同层次、不同类型且耦合度极低的模块组成。Nginx的模块都是嵌入到二进制文件中执行的，所以无论官方发布的模块还是第三方模块都一样具备很好的性能。
- **高可靠性：** Nginx常用模块非常稳定，每个worker进程相对独立，master进程在1个worker进程出错时可以快速“拉起”新的worker子进程提供服务。
- **低内存消耗：** 一般10000个非活跃的HTTP Keep-Alive连接在Nginx中仅消耗2.5MB的内存。
- **高并发：** Nginx能支持10万以上的并发请求。
- **热部署：** master管理进程与worker工作进程分离设计可支持热部署，支持更新配置、更换日志等功能。
- **开源协议友好：** BSD开源协议不仅允许用户免费使用，还允许直接使用、修改源码并发布。

#### 1.2、安装使用Nginx
- Linux2.6以上版本才支持epoll，查询Linux内核版本：uname -a
- 使用Nginx必备软件：
    1. GCC编译器：用来编译C语言，安装方式：yum install -y gcc
    2. G++编译器：用来编译C++编写的Nginx HTTP模块，安装方式：yum install -y gcc-c++
    3. PCRE库：用来解析正则表达式（nginx.conf），安装方式：yum install -y pcre pcre-devel
    4. zlib库：用来对HTTP包的内容做gzip格式压缩，安装方式：yum install -y zlib zlib-devel
    5. OpenSSL库：支持在更安全的SSL协议上传输HTTP，支持MD5、SHA1等散列函数，安装方式：yum install -y openssl openssl-devel
    6. 其他模块所需要的的第三方库
- 磁盘目录：
    1. Nginx源码存放目录
    2. Nginx编译产生的中间文件存放目录
    3. 部署目录：存放Nginx运行所需的二进制文件、配置文件等，默认目录：/usr/local/nginx
    4. 日志存放目录
- Linux内核参数优化：
- 获取安装Nginx：
    1. [Nginx官网下载](http://nginx.org/en/download.html)
    2. 将下载的nginx-x.x.x.tar.gz源码压缩包放到Nginx源码目录，解压：tar -zxvf nginx-x.x.x.tar.gz
    3. 编译安装Nginx：进入nginx-x.x.x目录执行以下3个命令：
        1. ./configure：检测操作系统内核和已安装软件、解析参数、生成中间目录及根据参数生成各种C源码文件、Makefile文件等
        2. make：根据configure命令生成的Makefile文件编译Nginx工程，生成目标文件、最终的二进制文件
        3. make install：根据configure执行时的参数将Nginx部署到指定安装目录，包括相关目录的创建和二进制文件、配置文件的复制
    4. 默认情况下Nginx被安装在：usr/local/nginx
        二进制文件路径：usr/local/nginx/sbin/nginx
        配置文件路径：usr/local/nginx/conf/nginx.conf
- configure分析：略
- Nginx命令：
    1. 启动：
        1. 默认启动：usr/local/nginx/sbin/nginx，会读取usr/local/nginx/conf/nginx.conf
        2. 指定配置文件启动：usr/local/nginx/sbin/nginx -c tmpnginx.conf
        3. 指定全局配置项启动：usr/local/nginx/sbin/nginx -g "pid var/nginx/test.pid;"，会把pid文件写到var/nginx/test.pid
            停止Nginx服务：usr/local/nginx/sbin/nginx -g "pid var/nginx/test.pid;" -s stop
    2. 检查配置文件是否有错：
        1. usr/local/nginx/sbin/nginx -t
        2. 检查时不输出error级别以下信息：usr/local/nginx/sbin/nginx -t -q
    3. 显示版本信息：
        1. usr/local/nginx/sbin/nginx -v
        2. 显示编译阶段信息（版本、参数）：usr/local/nginx/sbin/nginx -V
    4. 停止：
        1. usr/local/nginx/sbin/nginx -s stop
        2. kill -s SIGTERM/SIGINT <nginx master进程ID>，效果同上
        3. 优雅停止：usr/local/nginx/sbin/nginx -s quit，先关闭监听端口，再停止接收新请求，最后处理完正在处理的请求后退出进程
        4. 优雅停止kill：kill -s SIGQUIT/SIGWINCH <nginx master pid>，效果同上
    5. 重新加载配置：
        1. usr/local/nginx/sbin/nginx -s reload
        2. kill -s SIGHUP <nginx master pid>
    6. 日志文件回滚;
        1. usr/local/nginx/sbin/nginx -s reopen
        2. kill -s SIGUSR1 <nginx master pid>
    7. 平滑升级Nginx：
        1. 通知旧版本Nginx准备升级：kill -s SIGUSR2 <nginx master pid>
        2. 启动新版本Nginx，执行"1"
        3. 停止旧版本Nginx：kill -s SIGQUIT <nginx master pid>
    8. 帮助：usr/local/nginx/sbin/nginx -h

## 2、Nginx配置
#### 2.1、Nginx进程间关系
- 1个master进程管理多个worker进程
- worker进程数量=服务器cpu核心数（默认情况，可配置）
- 真正提供互联网服务的是worker进程，master进程只负责监控管理worker进程
    1. master只专注于管理工作，如启动、停止、重新加载配置文件、平滑升级等服务，需要较大权限
    2. 多个worker进程处理请求可以提高服务健壮性
    3. 1个worker进程同时处理的请求数只受限于内存大小，多个worker进程处理并发请求时几乎没有同步锁的限制，worker进程一般不会睡眠，因此worker进程数等于cpu核心数时，进程间切换代价最小
    
#### 2.2、配置语法
- 块配置项用大括号包括，配置项名后用空格分隔
- 块配置项可以嵌套，内层块会继承外层块，当内外层块配置冲突时-取决于解析这个配置项的模块
- 每行配置结尾需要加分号;
- 配置项中的语法符号，使用单引号''或者双引号""括住
- \# 注释符
- 参数值单位：
    1. 空间：K/k(KB)，M/m(MB)
    2. 时间：ms（毫秒），s（秒），m（分钟），h（小时），d（天），w（周，包含7天），M（月，包含30天），y（年，包含365天）
- $ 引用变量符
- 大部分模块都必须在nginx.conf中读取某个配置项后才会在Nginx启用

#### 2.3、Nginx基本配置
**按使用功能分为四类：**  
- **用于调试、定位问题的配置**

|配置项|语法|默认值|描述|
|---|---|---|---|
|是否以守护进程方式运行Nginx|daemon on/off;|on|守护进程（daemon）是脱离终端并且在后台运行的进程。它脱离终端是为了避免进程执行过程中的信息在任何终端上显示，这样一来，进程也不会被任何终端所产生的信息所打断|
|是否以master/worker方式工作|master_process on/off;|on|Nginx以一个master进程管理多个worker进程的方式运行，如果关闭，则不会fork出worker进程处理请求，会使用master进程自身处理请求|
|error日志设置|error_log "path file" "level";|error_log logs/error.log error;|"path file"可以是一个具体的文件，也可以是/dev/null(不输出日志)，也可以是stderr会输出到标准错误文件中；日志级别依次增大：debug、info、notice、warn、error、crit、alert、emerg（debug，必须在configure时加入--with-debug配置项）|
|仅对指定的客户端输出debug级别的日志|debug_connection "IP/CIDR";|无|这个配置项实际上属于事件类配置，因此，它必须放在events{...}中才有效。它的值可以是IP地址或CIDR地址，可用于定位高并发请求。（使用debug_connection前，需在执行configure时已经加入了--with-debug参数，否则不会生效）|
|限制coredump核心转储文件的大小|worker_rlimit_core "size";|无|当Nginx进程出现一些非法操作（如内存越界）导致进程直接被操作系统强制结束时，会生成核心转储core文件，可以从core文件获取当时的堆栈、寄存器等信息。|
|指定coredump文件生成目录|working_directory "path";|无|worker进程的工作目录。唯一用途就是设置coredump文件所放置的目录|
- **正常运行必备配置**

|配置项|语法|默认值|描述|
|---|---|---|---|
|||||
- **优化性能配置**
- **事件类配置**  


## HTTP模块

## event模块

## 负载均衡机制