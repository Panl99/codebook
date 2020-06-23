> [章亦春-Nginx教程](http://openresty.org/download/agentzh-nginx-tutorials-zhcn.html)  
> [深入理解Nginx：模块开发与架构解析（第2版）](https://github.com/Panl99/codebook/blob/master/nginx_lvs/深入理解Nginx模块开发与架构解析第2版LinuxUnix技术丛书-4.pdf)
# 一、关于Nginx
## 1.1、Nginx特点
- **更快：** 不论单次请求还是高峰大量的并发请求，Nginx都可以快速的响应。
- **高扩展性：** Nginx的设计极具扩展性，它完全是由多个不同功能、不同层次、不同类型且耦合度极低的模块组成。Nginx的模块都是嵌入到二进制文件中执行的，所以无论官方发布的模块还是第三方模块都一样具备很好的性能。
- **高可靠性：** Nginx常用模块非常稳定，每个worker进程相对独立，master进程在1个worker进程出错时可以快速“拉起”新的worker子进程提供服务。
- **低内存消耗：** 一般10000个非活跃的HTTP Keep-Alive连接在Nginx中仅消耗2.5MB的内存。
- **高并发：** Nginx能支持10万以上的并发请求。
- **热部署：** master管理进程与worker工作进程分离设计可支持热部署，支持更新配置、更换日志等功能。
- **开源协议友好：** BSD开源协议不仅允许用户免费使用，还允许直接使用、修改源码并发布。

## 1.2、安装使用Nginx
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

# 二、Nginx配置
## 2.1、Nginx进程间关系
- 1个master进程管理多个worker进程
- worker进程数量=服务器cpu核心数（默认情况，可配置）
- 真正提供互联网服务的是worker进程，master进程只负责监控管理worker进程
    1. master只专注于管理工作，如启动、停止、重新加载配置文件、平滑升级等服务，需要较大权限
    2. 多个worker进程处理请求可以提高服务健壮性
    3. 1个worker进程同时处理的请求数只受限于内存大小，多个worker进程处理并发请求时几乎没有同步锁的限制，worker进程一般不会睡眠，因此worker进程数等于cpu核心数时，进程间切换代价最小
    
## 2.2、配置语法
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

## 2.3、Nginx基本配置
**按使用功能分为四类：**  
- **用于调试、定位问题的配置**

|配置项|语法|默认值|描述|
|---|---|---|---|
|是否以守护进程方式运行Nginx|daemon on/off;|on|守护进程（daemon）是脱离终端并且在后台运行的进程。它脱离终端是为了避免进程执行过程中的信息在任何终端上显示，这样一来，进程也不会被任何终端所产生的信息所打断|
|是否以master/worker方式工作|master_process on/off;|on|Nginx以一个master进程管理多个worker进程的方式运行，如果关闭，则不会fork出worker进程处理请求，会使用master进程自身处理请求|
|error日志设置|***error_log "path file" "level";***|error_log logs/error.log error;|"path file"可以是一个具体的文件，也可以是/dev/null(不输出日志)，也可以是stderr会输出到标准错误文件中；日志级别依次增大：debug、info、notice、warn、error、crit、alert、emerg（debug，必须在configure时加入--with-debug配置项）|
|仅对指定的客户端输出debug级别的日志|debug_connection "IP/CIDR";|无|这个配置项实际上属于事件类配置，因此，它必须放在events{...}中才有效。它的值可以是IP地址或CIDR地址，可用于定位高并发请求。（使用debug_connection前，需在执行configure时已经加入了--with-debug参数，否则不会生效）|
|限制coredump核心转储文件的大小|worker_rlimit_core "size";|无|当Nginx进程出现一些非法操作（如内存越界）导致进程直接被操作系统强制结束时，会生成核心转储core文件，可以从core文件获取当时的堆栈、寄存器等信息。|
|指定coredump文件生成目录|working_directory "path";|无|worker进程的工作目录。唯一用途就是设置coredump文件所放置的目录|
- **正常运行必备配置**

|配置项|语法|默认值|描述|
|---|---|---|---|
|定义环境变量|env "VAR/VAR=VALUE"|如：env TESTPATH=/tmp/;|这个配置项可以让用户直接设置操作系统上的环境变量|
|嵌入其他配置文件|***include "path file"***|如：include mime.types; include vhost/*.conf;|将其他配置文件嵌入到当前的nginx.conf文件中|
|pid文件的路径|pid "path/file";|pid logs/nginx.pid;|保存master进程ID的pid文件存放路径。默认与configure执行时的参数“--pid-path”所指定的路径是相同的|
|Nginx worker进程运行的用户及用户组|user "username" "groupname";|user nobody nobody;|user用于设置master进程启动后，fork出的worker进程运行在哪个用户和用户组下。若用户在configure命令执行时使用了参数--user=username和--group=groupname，此时nginx.conf将使用参数中指定的用户和用户组。|
|指定Nginx worker进程可以打开的最大文件个数|***worker_rlimit_nofile limit;***|无|设置一个worker进程可以打开的最大文件个数。|
|限制信号队列|worker_rlimit_sigpending limit;|无|设置每个用户发往Nginx的信号队列的大小。也就是说，当某个用户的信号队列满了，这个用户再发送的信号量会被丢掉。|
- **优化性能配置**

|配置项|语法|默认值|描述|
|---|---|---|---|
|Nginx worker进程个数|***worker_processes number;***|worker_processes 1;|在master/worker运行方式下，定义worker进程的个数。每个worker单线程调用各模块功能，如果模块间不阻塞调用，则配置CPU内核个数个worker进程，否则多配点worker进程|
|绑定Nginx worker进程到指定的CPU内核|worker_cpu_affinity cpumask"cpumask..."|如：worker_processes 4; worker_cpu_affinity 1000 0100 0010 0001;|worker进程绑定指定的cpu内核，可以独享一个cpu，防止多个worker抢同一个cpu，以实现内核调度上完全的并发。（仅对Linux系统有效）|
|Nginx worker进程优先级设置|worker_priority nice;|worker_priority 0;|该配置项用于设置Nginx worker进程的nice优先级。|
|SSL硬件加速|ssl_engine "device";|无|如果服务器上有SSL硬件加速设备，那么就可以进行配置以加快SSL协议的处理速度。查看是否有SSL硬件加速设备：openssl engine -t|
|系统调用gettimeofday的执行频率|timer_resolution "t";|如：“timer_resolution 100ms；”表示至少每100ms才调用一次gettimeofday。|默认情况下，每次内核的事件调用（如epoll、select、poll、kqueue等）返回时，都会执行一次gettimeofday，实现用内核的时钟来更新Nginx中的缓存时钟。但在目前的大多数内核中，如x86-64体系架构，gettimeofday只是一次vsyscall，仅仅对共享内存页中的数据做访问，并不是通常的系统调用，代价并不大，一般不必使用这个配置。|

- **事件类配置**  

|配置项|语法|默认值|描述|
|---|---|---|---|
|是否打开accept锁|accept_mutex "on/off"|on|accept_mutex是Nginx的负载均衡锁，这把锁可以让多个worker进程轮流地、序列化地与新的客户端建立TCP连接。当某一个worker进程建立的连接数量达到worker_connections配置的最大连接数的7/8时，会大大地减小该worker进程试图建立新TCP连接的机会，以此实现所有worker进程之上处理的客户端请求数尽量接近。如果关闭它，那么建立TCP连接的耗时会更短，但worker进程之间的负载会非常不均衡。|
|lock文件的路径|lock_file "path/file";|lock_file logs/nginx.lock;|accept锁可能需要这个lock文件，如果accept锁关闭，lock_file配置完全不生效。如果打开了accept锁，并且由于编译程序、操作系统架构等因素导致Nginx不支持原子锁，这时才会用文件锁实现accept锁，这样lock_file指定的lock文件才会生效。|
|使用accept锁后到真正建立连接之间的延迟时间|accept_mutex_delay "Nms";|accept_mutex_delay 500ms;|在使用accept锁后，同一时间只有一个worker进程能够取到accept锁。这个accept锁不是阻塞锁，如果取不到会立刻返回。如果有一个worker进程试图取accept锁而没有取到，它至少要等accept_mutex_delay定义的时间间隔后才能再次试图取锁。|
|批量建立新连接|***multi_accept "on/off";***|multi_accept off;|当事件模型通知有新连接时，尽可能地对本次调度中客户端发起的所有TCP请求都建立连接。|
|选择事件模型|***use*** "kqueue/rtsig/***epoll***/'/dev/poll'/select/poll/eventport";|Nginx会自动使用最适合的事件模型。|对于Linux操作系统来说，可供选择的事件驱动模型有poll、select、epoll三种。epoll是性能最高的一种|
|每个worker的最大连接数|***worker_connections "number";***|无|定义每个worker进程可以同时处理的最大连接数。|

# 3、HTTP模块
## 3.1、用HTTP核心模块配置一个静态web服务器 
- [nginx.conf](https://github.com/Panl99/codebook/blob/master/nginx_lvs/nginx.conf)  

**主要介绍：ngx_http_core_module模块**  
- 所有的HTTP配置项都必须直属于http块、server块、location块、upstream块或if块等  

**按功能划分：**  
- **虚拟主机与请求的分发**

|配置项|语法|默认值|配置块|描述|
|---|---|---|---|---|
|监听端口|listen address:port "default(deprecated in 0.8.21)/default_server/'backlog=num/rcvbuf=size/sndbuf=size/accept_filter=filter/deferred/bind/ipv6only=<on/off>/ssl'";|listen 80;|server|在listen后可以只加IP地址、端口或主机名|
|主机名称|server_name name[...];|server_name "";|server|server_name后可以跟多个主机名称，在开始处理一个HTTP请求时，Nginx会取出header头中的Host，与每个server中的server_name进行匹配，以此决定到底由哪一个server块来处理这个请求。\n 如果host与多个server_name都匹配，选择server块处理优先级按照：全匹配>通配符\*在首位>通配符\*在末位>正则匹配；\n 如果都不匹配，按照：优先选择在listen配置项后加入"default/default_server"的server块 > 找到匹配listen端口的第一个server块。|
|存储server name的散列表的每个桶占用内存大小|server_names_hash_bucket_size "size";|32/64/128;|http、server、location|为了提高快速寻找到相应server name的能力，Nginx使用散列表来存储server name。server_names_hash_bucket_size设置了每个散列桶占用的内存大小。|
|server_names_hash_max_size|server_names_hash_max_size "size";|512|http、server、location|server_names_hash_max_size越大，消耗的内存就越多，但散列key的冲突率则会降低，检索速度也更快。|
|重定向主机名称的处理|server_name_in_redirect on/off;|on|http、server、location|该配置需要配合server_name使用，在使用on打开时，表示在重定向请求时会使用server_name里配置的第一个主机名代替原先请求中的Host头部，而使用off关闭时，表示在重定向请求时使用请求本身的Host头部。|
|location|location"=/~/~*/^~/@"/uri/{...}|无|server|location会尝试根据用户请求中的URI来匹配上面的/uri表达式，如果可以匹配，就选择location{}块中的配置来处理用户请求，多个location匹配时选第一个处理。匹配规则：=表示把URI作为字符串与参数中的uri做完全匹配，\~表示匹配URI时是字母大小写敏感的，\~*表示匹配URI时忽略字母大小写问题，^~表示匹配URI时只需要其前半部分与uri参数匹配即可，@表示仅用于Nginx服务内部请求之间的重定向，带有@的location不直接处理用户请求|
- **文件路径的定义**

|配置项|语法|默认值|配置块|描述|
|---|---|---|---|---|
||||||
||||||
||||||
||||||
||||||
||||||
- **内存及磁盘资源的分配**

|配置项|语法|默认值|配置块|描述|
|---|---|---|---|---|
||||||
||||||
||||||
||||||
||||||
||||||
- **网络连接的设置**

|配置项|语法|默认值|配置块|描述|
|---|---|---|---|---|
||||||
||||||
||||||
||||||
||||||
||||||
- **MIME类型的设置**

|配置项|语法|默认值|配置块|描述|
|---|---|---|---|---|
||||||
||||||
||||||
||||||
||||||
||||||
- **对客户端请求的限制**

|配置项|语法|默认值|配置块|描述|
|---|---|---|---|---|
||||||
||||||
||||||
||||||
||||||
||||||
- **文件操作的优化**

|配置项|语法|默认值|配置块|描述|
|---|---|---|---|---|
||||||
||||||
||||||
||||||
||||||
||||||
- **对客户端请求的特殊处理**

|配置项|语法|默认值|配置块|描述|
|---|---|---|---|---|
||||||
||||||
||||||
||||||
||||||
||||||

## 3.2、反向代理配置
**反向代理（reverse proxy）** 方式是指用代理服务器来接受Internet上的连接请求，然后将请求转发给内部网络中的上游服务器，并将从上游服务器上得到的结果返回给Internet上请求连接的客户端，此时代理服务器对外的表现就是一个Web服务器。
- Nginx反向代理特点：当客户端发来HTTP请求时，Nginx并不会立刻转发到上游服务器，而是先把用户的请求（包括HTTP包体）**完整**地接收到Nginx所在服务器的硬盘或者内存中，然后再向上游服务器发起连接，把缓存的客户端请求转发到上游服务器。  
- 缺点：延长了一个请求的处理时间，并增加了用于缓存请求内容的内存和磁盘空间。
- 优点：降低了上游服务器的负载，尽量把压力放在Nginx服务器上。减轻了上游服务器的并发压力。

#### 负载均衡基本配置
**（1）upstream块**  
语法： upstream name{...}  
配置块： http  
upstream块定义了一个上游服务器的集群，便于反向代理中的proxy_pass使用。例如：  
```javascript
upstream backend {
    server backend1.example.com;
    server backend2.example.com;
    server backend3.example.com;
} 
server {
    location / {
        proxy_pass http://backend;
    }
}
```
**（2）server**  
语法： server name[parameters];  
配置块： upstream  
server配置项指定了一台上游服务器的名字，这个名字可以是域名、IP地址端口、UNIX句柄等，在其后还可以跟下列参数。
- weight=number：设置向这台上游服务器转发的权重，默认为1。
- max_fails=number：该选项与fail_timeout配合使用，指在fail_timeout时间段内，如果向当前的上游服务器转发失败次数超过number，则认为在当前的fail_timeout时间段内这台上游服务器不可用。max_fails默认为1，如果设置为0，则表示不检查失败次数。
- fail_timeout=time：fail_timeout表示该时间段内转发失败多少次后就认为上游服务器暂时不可用，用于优化反向代理功能。它与向上游服务器建立连接的超时时间、读取上游服务器的响应超时时间等完全无关。fail_timeout默认为10秒。
- down：表示所在的上游服务器永久下线，只在使用ip_hash配置项时才有用。
- backup：在使用ip_hash配置项时它是无效的。它表示所在的上游服务器只是备份服务器，只有在所有的非备份上游服务器都失效后，才会向所在的上游服务器转发请求。
例如：
```javascript
upstream backend {
    server backend1.example.com weight=5;
    server 127.0.0.1:8080 max_fails=3 fail_timeout=30s;
    server unix:/tmp/backend3;
}
```
**（3）ip_hash**  
语法： ip_hash;  
配置块： upstream  
在有些场景下，我们可能会希望来自某一个用户的请求始终落到固定的一台上游服务器中。例如，假设上游服务器会缓存一些信息，如果同一个用户的请求任意地转发到集群中的任一台上游服务器中，那么每一台上游服务器都有可能会缓存同一份信息，这既会造成资源的浪费，也会难以有效地管理缓存信息。ip_hash就是用以解决上述问题的，它首先根据客户端的IP地址计算出一个key，将key按照upstream集群里的上游服务器数量进行取模，然后以取模后的结果把请求转发到相应的上游服务器中。这样就确保了同一个客户端的请求只会转发到指定的上游服务器中。  
ip_hash与weight（权重）配置不可同时使用。如果upstream集群中有一台上游服务器暂时不可用，不能直接删除该配置，而是要down参数标识，确保转发策略的一贯性。例如：  
```javascript
upstream backend {
    ip_hash;
    server backend1.example.com;
    server backend2.example.com;
    server backend3.example.com down;
    server backend4.example.com;
}
```
**（4）记录日志时支持的变量**  
如果需要将负载均衡时的一些信息记录到access_log日志中，那么在定义日志格式时可以使用负载均衡功能提供的变量，见表2-2。  
表2-2　访问上游服务器时可以使用的变量  

|变量名|意义|
|---|---|
|$upstream_addr|处理请求的上游服务器地址|
|$upstream_cache_status|表示是否命中缓存，取值范围：MISS、EXPIRED、UPDATING、STALE、HIT|
|$upstream_status|上游服务器返回的响应中的HTTP响应码|
|$upstream_response_time|上游服务器的响应时间，精度到毫秒|
|$upstream_http_$HEADER|HTTP的头部，如$upstream_http_host|

例如，可以在定义access_log访问日志格式时使用表2-2中的变量：  
```javascript
log_format timing '$remote_addr - $remote_user [$time_local] $request' 'upstream_response_time $upstream_response_time' 'msec $msec request_time $request_time';
log_format up_head '$remote_addr - $remote_user [$time_local] $request' 'upstream_http_content_type $upstream_http_content_type';
```
#### 反向代理基本配置
**（1）proxy_pass**  
语法： proxy_pass URL;  
配置块： location、if  
此配置项将当前请求反向代理到URL参数指定的服务器上，URL可以是主机名或IP地址加端口的形式，例如：  
```javascript
proxy_pass http://localhost:8000/uri/;
```
也可以是UNIX句柄：  
```javascript
proxy_pass http://unix:/path/to/backend.socket:/uri/;
```
还可以如上节负载均衡中所示，直接使用upstream块，例如：  
```javascript
upstream backend {
    …
} 
server {
    location / {
        proxy_pass http://backend;
    }
}
```
用户可以把HTTP转换成更安全的HTTPS，例如：  
```javascript
proxy_pass https://192.168.0.1;
```
默认情况下反向代理是不会转发请求中的Host头部的。如果需要转发，那么必须加上配置：  
```javascript
proxy_set_header Host $host;
```
**（2）proxy_method**  
语法： proxy_method method;  
配置块： http、server、location  
此配置项表示转发时的协议方法名。例如设置为：  
```javascript
proxy_method POST;
```
那么客户端发来的GET请求在转发时方法名也会改为POST。  
**（3）proxy_hide_header**  
语法： proxy_hide_header the_header;  
配置块： http、server、location  
Nginx会将上游服务器的响应转发给客户端，但默认不会转发以下HTTP头部字段：Date、Server、X-Pad和X-Accel-*。使用proxy_hide_header后可以任意地指定哪些HTTP头部字段不能被转发。例如：  
```javascript
proxy_hide_header Cache-Control;
proxy_hide_header MicrosoftOfficeWebServer;
```
**（4）proxy_pass_header**  
语法： proxy_pass_header the_header;  
配置块： http、server、location  
与proxy_hide_header功能相反，proxy_pass_header会将原来禁止转发的header设置为允许转发。例如：  
```javascript
proxy_pass_header X-Accel-Redirect;
```
**（5）proxy_pass_request_body**  
语法： proxy_pass_request_body on|off;  
默认： proxy_pass_request_body on;  
配置块： http、server、location  
作用为确定是否向上游服务器发送HTTP包体部分。  
**（6）proxy_pass_request_headers**  
语法： proxy_pass_request_headers on|off;  
默认： proxy_pass_request_headers on;  
配置块： http、server、location  
作用为确定是否转发HTTP头部。  
**（7）proxy_redirect**  
语法： proxy_redirect[default|off|redirect replacement];  
默认： proxy_redirect default;  
配置块： http、server、location  
当上游服务器返回的响应是重定向或刷新请求（如HTTP响应码是301或者302）时，proxy_redirect可以重设HTTP头部的location或refresh字段。
例如，如果上游服务器发出的响应是302重定向请求，location字段的URI是http://localhost:8000/two/some/uri/ ，那么在下面的配置情况下，实际转发给客户端的location是http://frontendonesome/uri/ 。
```javascript
proxy_redirect http://localhost:8000/two/  http://frontendone;
```
这里还可以使用ngx-http-core-module提供的变量来设置新的location字段。例如：  
```javascript
proxy_redirect http://localhost:8000/  http://$host:$server_port/;
```
也可以省略replacement参数中的主机名部分，这时会用虚拟主机名称来填充。例如：  
```javascript
proxy_redirect http://localhost:8000/two/one;
```
使用off参数时，将使location或者refresh字段维持不变。例如：
```javascript
proxy_redirect off;
```
使用默认的default参数时，会按照proxy_pass配置项和所属的location配置项重组发往客户端的location头部。例如，下面两种配置效果是一样的：  
```javascript
location one {
    proxy_pass http://upstream:port/two/;
    proxy_redirect default;
} 
location one {
    proxy_pass http://upstream:port/two/;
    proxy_redirect http://upstream:port/two/one;
}
```
**（8）proxy_next_upstream**  
语法：proxy_next_upstream[error|timeout|invalid_header|http_500|http_502|http_503|http_504|http_404|off];  
默认： proxy_next_upstream error timeout;  
配置块： http、server、location  
此配置项表示当向一台上游服务器转发请求出现错误时，继续换一台上游服务器处理这个请求。前面已经说过，上游服务器一旦开始发送应答，Nginx反向代理服务器会立刻把应答包转发给客户端。因此，一旦Nginx开始向客户端发送响应包，之后的过程中若出现错误也是不允许换下一台上游服务器继续处理的。这很好理解，这样才可以更好地保证客户端只收到来自一个上游服务器的应答。  
proxy_next_upstream的参数用来说明在哪些情况下会继续选择下一台上游服务器转发请求。  
- error：当向上游服务器发起连接、发送请求、读取响应时出错。
- timeout：发送请求或读取响应时发生超时。
- invalid_header：上游服务器发送的响应是不合法的。
- http_500：上游服务器返回的HTTP响应码是500。
- http_502：上游服务器返回的HTTP响应码是502。
- http_503：上游服务器返回的HTTP响应码是503。
- http_504：上游服务器返回的HTTP响应码是504。
- http_404：上游服务器返回的HTTP响应码是404。
- off：关闭proxy_next_upstream功能—出错就选择另一台上游服务器再次转发。  

Nginx的反向代理模块还提供了很多种配置，如设置连接的超时时间、临时文件如何存储，以及最重要的如何缓存上游服务器响应等功能。这些配置可以通过阅读ngx_http_proxy_module模块的说明了解，只有深入地理解，才能实现一个高性能的反向代理服务器。  


## 3.3、将HTTP模块编译到Nginx中
1. 将源码文件放在一个目录下（如：/nginx/），并在此目录下创建一个文件命名为config；
2. 在configure脚本执行时添加参数：--add-module=/nginx/；
3. config文件编写：（仅开发HTTP模块，包含下3个变量即可）
    - ngx_addon_name：仅在configure执行时使用，一般设置为模块名称。
    - HTTP_MODULES：保存所有的HTTP模块名称，每个HTTP模块间使用空格符连接。重设此变量时不要进行覆盖，可以引用。
    - NGX_ADDON_SRCS：用于指定新增模块的源代码，多个待编译的源代码间使用空格符连接。可以使用$ngx_addon_dir变量，等价于configure执行时--add-module=/nginx/参数。  
    例：
    ```
    ngx_addon_name=ngx_http_mytest_module
    HTTP_MODULES="$HTTP_MODULES ngx_http_mytest_module"
    NGX_ADDON_SRCS="$NGX_ADDON_SRCS $ngx_addon_dir/ngx_http_mytest_module.c"
    ```
4. 使用configure脚本将第三方模块加入到Nginx中：
    ```
    执行： 
    . auto/modules
    . auto/make
    ```
## 3.4、定义一个HTTP模块


## event模块

## 负载均衡机制