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