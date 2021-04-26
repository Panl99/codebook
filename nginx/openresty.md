- [章亦春-Nginx教程](http://openresty.org/download/agentzh-nginx-tutorials-zhcn.html)  
- [OpenResty最佳实践](https://github.com/Panl99/openresty-best-practices)
- [Nginx Lua开发实战]()

# 目录
- [OpenResty概述](#OpenResty概述)
    - [OpenResty组成](#OpenResty组成)
    - [OpenResty安装](#OpenResty安装)
    - [Nginx多实例](#Nginx多实例)
- [共享内存区域](#共享内存区域)
    
- [Lua](#Lua)
    - [Lua安装](#Lua安装)
    - [Lua基本用法](#Lua基本用法)
    - [Lua通用库](#Lua通用库)

# OpenResty概述
OpenResty 是一个基于Nginx 与Lua 的高性能Web 平台，集成了大量精良的Lua 库、第三方模块以及大多数的依赖项，用于方便地搭建能够处理超高并发、扩展性极高的动态Web 应用、Web 服务和动态网关。  

可以使用Lua 脚本语言调动Nginx 支持的各种C 以及Lua 模块，快速构造出可以支持10K 乃至1000K 以上单机并发连接的高性能Web 应用系统。  

## OpenResty组成
- 标准Lua 5.1 解释器；
- Drizzle Nginx 模块；
- Postgres Nginx 模块；
- Iconv Nginx 模块。

**注意：**
- 上面4 个模块默认并未启用，需要分别加入`--with-lua51`、`--with-http_drizzle_module`、`--with-http_postgres_module` 和`--with-http_iconv_module` 编译选项开启它们。
- 在1.5.8.1 版本之前，OpenResty 默认使用标准Lua5.1 解释器。所以对于老版本，需要显式地加入`--with-luajit` 编译选项（1.5.8.1以后的版本已默认开启）来启用LuaJIT 组件。
- 非必要时，不推荐启用标准Lua 5.1 解释器，而应尽量使用LuaJIT 组件。

**OpenResty支持的模块：**
```shell script
LuaJIT;
ArrayVarNginxModule;
AuthRequestNginxModule;
CoolkitNginxModule;
DrizzleNginxModule;
EchoNginxModule;
EncryptedSessionNginxModule;
FormInputNginxModule;
HeadersMoreNginxModule;
IconvNginxModule;
StandardLualnterpreter;
MemcNginxModule;
Nginx;
NginxDevelKit;
LuaCjsonLibrary;
LuaNginxModule;
LuaRdsParserLibrary;
LuaRedisParserLibrary;
LuaRestyCoreLibrary;
LuaRestyDNSLibrary;
LuaRestyLockLibrary;
LuaRestyLrucacheLibrary;
LuaRestyMemcachedLibrary;
LuaRestyMySQLLibrary;
LuaRestyRedisLibrary;
LuaRestyStringLibrary;
LuaRestyUploadLibrary;
LuaRestyUpstreamHealthcheckLibrary;
LuaRestyWebSocketLibrary;
LuaRestyLimitTrafficLibrary;
LuaUpstreamNginxModule;
PostgresNginxModule;
RdsCsvNginxModule;
RdsJsonNginxModule;
RedisNginxModule;
Redis2NginxModule;
RestyCLI;
OPM;
SetMiscNginxModule;
SrcacheNginxModule;
XssNginxModule。
```

## OpenResty安装
- 官网： `https://openresty.org/cn/installation.html`
- 添加资源库：
    - 创建一个名为`/etc/yum.repos.d/OpenResty.repo`的文件，内容如下：
        ```shell script
        [openresty]
        name=Official OpenResty Repository
        baseurl=https://copr-be.cloud.fedoraproject.org/results/openresty/openresty/epel-$releasever-$basearch/
        skip_if_unavailable=True
        gpgcheck=1
        gpgkey=https://copr-be.cloud.fedoraproject.org/results/openresty/openresty/pubkey.gpg
        enabled=1
        enabled_metadata=1
        ```
    - 也可以直接运行命令添加仓库： `sudo yum-config-manager --add-repo https://openresty.org/yum/centos/OpenResty.repo`
    - 国内用户可以把baseurl 改成后面链接，速度会更快：`baseurl= https://openresty.org/yum/openresty/openresty/epel-$releasever$basearch/`
    - 或者运行下面命令直接添加仓库：`sudo yum-config-manager --add-repo https://openresty.org/yum/cn/centos/OpenResty.repo`
- 列出资源库中所有的OpenResty 包： `sudo yum --disablerepo="*"--enablerepo="openresty" list available`
- 安装： `sudo yum install openresty`
    - 使用yum 安装OpenResty 可能会因为缺少GeoIP 库失败，需要先安装GeoIP：`yum install GeoIP-devel`
    - GeoIP 库的安装可能会因为仓库里没有Extra 库而失败，需要先添加Extra 库： `yum install epel-release`
- 测试：
    - 启动Nginx： `/usr/local/openresty/nginx/sbin/nginx -p /usr/local/openresty/nginx/`，在浏览器里输入`http://127.0.0.1（或主机IP）`，看到 `Welcome to OpenResty！` 表示已经启动成功。
    - 修改`/usr/local/openresty/nginx/conf/nginx.conf`，测试Lua 是否正常工作：
        ```shell script
        worker_processes 1;
        error_log logs/error.log;
      
        events {
          worker connections 1024;
        }
      
        http {
            server {
                listen 8080;
                location / {
                    default_type text/html;
                    content_by_lua 'ngx.say("<p>hello, world</p>")';
                }
            }
        }
        ```
    - 测试配置文件正确性： `/usr/local/openresty/nginx/sbin/nginx -p /usr/local/openresty/nginx/ -t`
    - 重新加载配置文件： `/usr/local/openresty/nginx/sbin/nginx -p /usr/local/openresty/nginx/ -s reload`
    - 在浏览器里输入`http://127.0.0.1:8080`，如果看到了`hello world`就表示可以正常工作了。
    - 也可以使用curl命令： `curl http://localhost:8080`，<p>hello, world</p>

## Nginx多实例
- OpenRestry 安装成功后，包里的Nginx 可以部署多个实例，可以实例化多个不同的服务：或用于对外提供服务，或用于不同的开发任务，或用于学习。
- 只需要把OpenRestry 中的Nginx 目录复制一份就可以启动不同的实例： `cp -r /usr/local/openrestry/nginx /usr/local/openrestry/nginx_9090`
- 然后修改`nginx_9090/conf/nginx.conf`，把端口从8080 修改为9090 ，把"hello world"修改为"hello world2"，修改完成后启动实例： `/usr/local/openrestry/nginx_9090/sbin/nginx -p /usr/local/openrestry/nginx_9090/`
- 在浏览器里输入`http://127.0.0.l:9090 `，可以得到：`hello world2` 表示新实例启动成功。

[目录](#目录)


# 共享内存区域
通过 `lua_shared_dict` 指令可以声明一个共享内存区域，可以在多个 worker 进程间共享，单位支持 k、m，然后配合 ngx.shared.DICT api函数来操作。`nginx -s reload` 后共享内存的数据还在。

这个共享内存功能非常有用，极大的便利了worker 进程间的通信和协作，而且还提供了类似 redis 的数据结构，可以当做一个简易的数据库，而且没有通信开销。

共享内存在 Openresty 里用途很广，基于它可以实现流量统计、缓存、进程锁等高级功能。

```
http {
     lua_shared_dict dogs 10m;
     server {
         location /set {
             content_by_lua_block {
                 local dogs = ngx.shared.dogs
                 dogs:set("Jim", 8)
                 ngx.say("STORED")
             }
         }
         location /get {
             content_by_lua_block {
                 local dogs = ngx.shared.dogs
                 ngx.say(dogs:get("Jim"))
             }
         }
     }
 }
```
```shell script
[root@192 ~]# curl localhost/set
STORED
[root@192 ~]# curl localhost/get
8
[root@192 ~]# openresty -p /var/openresty -s reload
[root@192 ~]# curl localhost/get
8
```

下面将介绍一些操作共享内存的函数，这些函数都是原子操作，只要在同一个内存区域，在多个worker 进程间并发都是安全的。

**ngx.shared.DICT.set** `syntax: success, err, forcible = ngx.shared.DICT:set(key, value, exptime?, flags?)`

无条件设置一个共享内存的值，返回3个值：
- success：布尔值，是否成功
- err：错误信息
- forcible：布尔值，是否由于内存不足把其他有效的值挤出去了

- value 值可以是 布尔值、数字、字符串、nil，同过get返回的保留这些类型。
- exptime 可选值用来设置过期时间(秒)，精度为 0.001s。默认值 0 代表不过期。
- flags 可选值，一个标记，无符号32位整形，默认为0，通过 get 也可以返回。

当内存不够分配，那么会强制移除最老的项根据过期时间排序，如果移除了还是由于某种原因不能新增数据，那么 success 为fasle，错误信息存入 err。
```
local cats = ngx.shared.cats
local succ, err, forcible = cats.set(cats, "Marry", "it is a nice cat!")
```
也可以是下面这种方式
```
local cats = ngx.shared.cats
local succ, err, forcible = cats:set("Marry", "it is a nice cat!")
```

**ngx.shared.DICT.safe_set** `syntax: ok, err = ngx.shared.DICT:safe_set(key, value, exptime?, flags?)`

同上，只是当内存不足时不会强制移除没过期的项目来腾出空间，直接返回 nil 和 错误信息。

**ngx.shared.DICT.add**  `syntax: success, err, forcible = ngx.shared.DICT:add(key, value, exptime?, flags?)`

同 ngx.shared.DICT.set，只是如果 key 已经存在且没过期，返回 fasle，err 设置为 "exists"。

**ngx.shared.DICT.safe_add** `syntax: ok, err = ngx.shared.DICT:safe_add(key, value, exptime?, flags?)`

同上，只是当内存不足时不会强制移除没过期的项目来腾出空间，直接返回 nil 和 错误信息。

**ngx.shared.DICT.get** `syntax: value, flags = ngx.shared.DICT:get(key)`

获取共享内存的值，如果 key 不存在或者过期，返回 nil。出错返回 nil 和 错误信息。
```
local sh_dog = ngx.shared.dogs
local value,flags = sh_dog.get(sh_dog, "Jim")
```
另一种获取方式
```
local sh_dog = ngx.shared.dogs
local value,flags = sh_dog:get("Jim")
```

**ngx.shared.DICT.get_stale** `syntax: value, flags, stale = ngx.shared.DICT:get_stale(key)`

同上，就算过期了也返回(只要没清除)。stale 代表是否过期了。

**ngx.shared.DICT.replace** `syntax: success, err, forcible = ngx.shared.DICT:replace(key, value, exptime?, flags?)`

类似 set，只是 key 必须存在且没过期才会替换成功，否则返回 false，err为 "not found"。

**ngx.shared.DICT.delete** `syntax: ngx.shared.DICT:delete(key)`

无条件删除，等同于 `ngx.shared.DICT:set(key, nil)`

**ngx.shared.DICT.incr** `syntax: newval, err, forcible? = ngx.shared.DICT:incr(key, value, init?, init_ttl?)`

将 key 里面的数字值增加 value，并返回增加后的数值。value 和 init 可以是正负整数，正负浮点数。

如果 key 里面原来的值不是数字，返回 nil，err设置为 "not a number"。

如果 key 不存在或者过期：
1. 如果提供了 init，那么将会新增 key，并初始化值为 init+value，如果内存不足，还是会挤掉最老的值，如果挤掉了，那么 forcible 就为 true。init_ttl 代表新增的 key 的过期时间，默认为 0，不过期。
2. 如果没提供，返回 nil，err设置为 "not found"。

**操作列表**

**ngx.shared.DICT.lpush** `syntax: length, err = ngx.shared.DICT:lpush(key, value)`

从key对应的列表的最左边插入 value，返回插入后列表的元素个数。如果内存不足，直接返回 nil，err设置为 "no memory"。

如果 key 不存在，那么会新建空列表后再插入，如果 key 已经存在但是不是列表，返回 nil，err为 "value not a list"。

**ngx.shared.DICT.rpush** `syntax: length, err = ngx.shared.DICT:rpush(key, value)`

同上，只是从右边插入。

**ngx.shared.DICT.lpop** `syntax: val, err = ngx.shared.DICT:lpop(key)`

移除由key指定的列表最左边的值并返回，key 不存在返回 nil，key存在但是不是列表，返回nil，err为 "value not a list"。

**ngx.shared.DICT.rpop** `syntax: val, err = ngx.shared.DICT:rpop(key)`

同上，只是从右边移除。

**ngx.shared.DICT.llen** `syntax: len, err = ngx.shared.DICT:llen(key)`

返回指定 key 列表的元素个数，key 不存在返回0，存在但是不是列表返回 nil，err 为 "value not a list"。

**ngx.shared.DICT.ttl**
```
syntax: ttl, err = ngx.shared.DICT:ttl(key)
requires: resty.core.shdict or resty.core
```
返回指定 key 的过期时间(秒)，精度为 0.001，返回 0 代表永不过期。如果 key 不存在或者过期，返回 nil，err为 "not found"。
```
lua_shared_dict dogs 10m;
     server {
         location /set {
             content_by_lua_block {
                 local dogs = ngx.shared.dogs
                 dogs:set("Jim", 8, 100)
                 ngx.say("STORED")
             }
         }
         location /get {
             content_by_lua_block {
             	 require "resty.core"
                 local dogs = ngx.shared.dogs
                 local ttl,err = dogs:ttl("Jim")
                 
                 ngx.say(ttl)
             }
         }
     }
```
```shell script
[root@192 ~]# curl localhost/set
STORED

[root@192 ~]# curl localhost/get
98.622
[root@192 ~]# curl localhost/get
96.542
[root@192 ~]# curl localhost/get
95.744
```

**ngx.shared.DICT.expire**
```
syntax: success, err = ngx.shared.DICT:expire(key, exptime)
requires: resty.core.shdict or resty.core
```
更新 key 的过期时间单位秒，精度0.001，0代表永不过期。如果 key 不存在返回 nil，err为 "not found"。

**ngx.shared.DICT.flush_all** `syntax: ngx.shared.DICT:flush_all()`

标记所有的key为过期。

**ngx.shared.DICT.flush_expired** `syntax: flushed = ngx.shared.DICT:flush_expired(max_count?)`

将过期的key删除并释放内存，max_count 限制删除的数量，默认为0，代表不限制。

**ngx.shared.DICT.get_keys** `syntax: keys = ngx.shared.DICT:get_keys(max_count?)`

从字典里获取key，默认只会返回前 1024 个，如果指定了 max_count，那么返回 max_count 个，如果 max_count 为0，返回全部。

注意：因为该操作会锁住共享内存，如果 key 很多不建议使用该函数，因为会让需要用到该共享内存的 worker 进程阻塞。

**ngx.shared.DICT.capacity**
```
syntax: capacity_bytes = ngx.shared.DICT:capacity()
requires: resty.core.shdict or resty.core
```
返回共享内存的容量(由 lua_shared_dict 指定)，单位字节。

**ngx.shared.DICT.free_space**
```
syntax: free_page_bytes = ngx.shared.DICT:free_space()
requires: resty.core.shdict or resty.core
```
获取共享内存的空闲空间，由于内部 nginx 是用 slab 分配器分配的内存，返回0可能还是有空间来存储新的值。

简单的例子
```
 lua_shared_dict dogs 10m;
 server {
     location /set {
         content_by_lua_block {
             local dogs = ngx.shared.dogs
             dogs:set("Jim", 8, 100)
             dogs:lpush("list_1", 3)
             dogs:lpush("list_1", 2)
             dogs:lpush("list_1", 1)
             dogs:rpush("list_1", 4)
             
             dogs:incr("Jim", 4)
         }
     }
     location /get {
         content_by_lua_block {
             require "resty.core"
             local dogs = ngx.shared.dogs
             local ttl,err = dogs:ttl("Jim")
             local l = dogs:get("list_1")
             local val1,err = dogs:lpop("list_1")
             local val2,err = dogs:lpop("list_1")
             
             
             ngx.say("ttl:",ttl)
             ngx.say("get list type:",type(l))
             ngx.say("lpop1:", val1)
             ngx.say("lpop2:", val2)
         }
     }
 }
```
```shell script
[root@192 ~]# curl localhost/set
[root@192 ~]# curl localhost/get
ttl:98.44
get list type:nil
lpop1:1
lpop2:2
```
[openresty - lua API(6) - 共享内存区域](http://www.daileinote.com/computer/openresty/11)

[目录](#目录)


# Lua
- 轻量级：Lua使用标准C语言编写并以源码形式开放，编译后仅一百余字节，可以很方便嵌入其他程序中。
- 可扩展：Lua提供了非常容易使用的扩展接口 和机制，由宿主语言(如C/C++)提供功能，Lua可以像内置功能一样使用它们。
- 其他特性：
    - 支持面向过程 和函数式编程。
    - 自动内存管理：通过只提供的一种类型表(table)，实现数组、hash标、集合、对象。
    - 语言内置模式匹配

## Lua安装
- Linux下载源码包、解压、编译：
    ```shell script
    curl -R -O http://www.lua.org/ftp/lua-5.3.0.tar.gz
    tar zxf lua-5.3.0.tar.gz
    cd lua-5.3.0
    make linux test
    make install
    ```
- 测试：
    - 创建HelloWorld.lua文件，文件内容： `print("Hello World!");`
    - 执行命令： `lua HelloWorld.lua`
        - 输出结果：Hello World!

## Lua基本用法
### Lua语法
- Lua语法与C/C++相似
- 开启Lua交互模式：`lua -i`
- lua脚本编程：将代码保存为xxx.lua，执行`lua xxx.lua`
    - 或者指定lua解释器来执行脚本(执行：./test.lua)：
        ```shell script
        #!/usr/local/bin/lua
        print ("Hello World!")
        ```

**注释**：
- 单行注释： `--注释`
- 多行注释：
    ```shell script
    --[[
    多行注释
    多行注释
    --]]
    ```

**标识符**：
- 标识符以字母或下划线开头，后加0-n个字母、下划线、数字，不能其他特殊字符（如：@、#、$、%等）
- 最好不要使用下划线 + 大写字母的标识符，防止与保留字冲突
- 区分大小写

**保留字**：and、break、false、true、local、if、else、elseif、end、in、repeat、function、or、until、nil、not、return、then、while、do、for

**全局变量**：
- 默认即全局变量
- 访问没有初始化的变量，返回结果为 nil
- 删除一个全局变量，只需要将变量值赋为 nil
    
### Lua数据类型
1. **nil**： 无效值，在条件表达式中相当于false。只有nil 这一个值
2. **boolean**： 包含两个值：false 和true
3. **number**： 表示双精度类型的实浮点数
4. **string**： 字符串由一对双引号或单引号表示。或者两个方括号`[[]]`来表示“一块”字符串：
5. **function**： 由C 或Lua 编写的函数
6. **userdata**： 表示任意存储在变盘中的C 数据结构
7. **thread**： 执行的独立线程，用于执行协同程序
8. **table**：Lua 中的表实际上是一个“关联数组”，索引可以是数字或字符串。Lua中，表通过构造表达式完成。最简单的表达式是{}，用来创建一个空表

- 使用type()函数获取变量或值的类型： `print(type("Hello world"))  --> string`

### Lua变量
- 全局变量：`a = 5`
- 局部变量：`local b = 6`
- 表中的域

- 赋值：
    - 多变量赋值： a, b = 10, 2*x  -->  a=10; b=2*x
    - 变量个数 跟值的个数不一致：
        - 变量个数 > 值的个数，按变量个数补足nil 。
        - 变量个数 < 值的个数，多余的值会被忽略。

- 索引：对table的索引使用方括号[]。Lua也提供了．（点）操作
    - t[i]
    - t.i  --当索引为字符串类型时的一种简化写法

### Lua循环
//TODO
### Lua流程控制
//TODO
### Lua函数
//TODO
### Lua运算符
//TODO
### Lua字符串
//TODO
### Lua数组
//TODO
### Lua迭代器
//TODO
### Lua表
//TODO
### Lua模块与包
//TODO
### Lua元表
//TODO
### Lua协同程序
//TODO
### Lua错误处理
//TODO
### Lua调试
//TODO
### Lua垃圾回收
//TODO
### Lua面向对象
//TODO
### Lua数据库访问
//TODO

[目录](#目录)

## Lua通用库
### 字符串库
//TODO
### 表库
//TODO
### 文件I/O库
//TODO
### 数学库
//TODO
### 操作系统库
//TODO

[目录](#目录)