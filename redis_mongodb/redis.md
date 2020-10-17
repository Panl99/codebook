> Redis实战  
> Redis设计与实现  
> [https://github.com/josiahcarlson/redis-in-action](https://github.com/josiahcarlson/redis-in-action)

# 目录
> [常见问题-TODO](#常见问题)
> > - [缓存雪崩](#缓存雪崩)
> > - [缓存击穿](#缓存击穿)
> > - [缓存穿透](#缓存穿透)
> > - [降低Redis内存占用](#降低Redis内存占用)
- [Redis特性](#Redis特性)
- [Redis命令](#命令)
    - [字符串命令](#字符串命令)
    - [列表命令](#列表命令)
    - [集合命令](#集合命令)
    - [散列命令](#散列命令)
    - [有序集合命令](#有序集合命令)
    - [发布/订阅命令](#发布/订阅命令)
    - [其他命令](#其他命令)
- [复制](#复制)
    - [复制功能的实现](#复制功能的实现)
    - [主从服务器心跳检测机制](#主从服务器心跳检测机制)
- [持久化](#持久化)
    - [快照持久化](#快照持久化)
        - [使用快照持久化](#使用快照持久化)
    - [AOF持久化](#AOF持久化)
- [事务](#事务)
- [Redis哨兵-Redis高可用性解决方案](#Redis哨兵)
    - [Sentinel初始化](#Sentinel初始化)
    - [Sentinel监视服务器原理](#Sentinel监视服务器原理)
    - [Sentinel系统对主服务器进行故障转移](#Sentinel系统对主服务器进行故障转移)
        - [节点选举](#节点选举)
- [集群](#集群)
    - [节点](#节点)
    - [槽指派](#槽指派)
        - [重新分片](#重新分片)
    - [复制与故障转移](#复制与故障转移)
        - [故障转移](#故障转移)
- [分布式锁-TODO](#分布式锁)
  
- [实战](#实战)
    - [使用Redis管理用户登录会话](#使用Redis管理用户登录会话)
    - [使用Redis实现购物车-TODO](#使用Redis实现购物车)

------
- [安装](#安装)
    - [Windows下安装Redis](#Windows下安装Redis)
    - [Jedis驱动的安装](#Jedis驱动的安装)
- [Jedis命令](#Jedis命令)
    - [Jedis-key](#Jedis-key)
    - [Jedis-String](#Jedis-String)
    - [Jedis-List](#Jedis-List)
    - [Jedis-Set](#Jedis-Set)
    - [Jedis-sortedSet](#Jedis-sortedSet)
    - [Jedis-Hash](#Jedis-Hash)
- [Redis常用命令集](#Redis常用命令集)
    - [连接操作命令](#连接操作命令)
    - [持久化命令](#持久化命令)
    - [远程服务控制](#远程服务控制)
    - [对value操作的命令](#对value操作的命令)
    - [String](#String)
    - [List](#List)
    - [Set](#Set)
    - [Hash](#Hash)
    - [Redis发布订阅命令](#Redis发布订阅命令)
    - [Redis事务命令](#Redis事务命令)
    - [查看keys个数](#查看keys个数)
    - [清空数据库](#清空数据库)
    

[目录](#目录)

# 常见问题
## 缓存雪崩

- 大量的key由于设置相同的过期时间，导致缓存在同一时刻集中失效，造成DB突然请求量变大，造成雪崩。
- 解决方案：
  - 设置缓存过期时间加上随机值，让每个key的过期时间分布开，不会同一时刻失效。

## 缓存击穿

- 一个存在的key，在缓存过期的那一刻，有大量请求访问，造成击穿到DB，导致DB压力瞬间增大。
- 解决方案：
  - 在访问key之前，通过SETNX设置一个短期key来锁住当前key的访问，访问结束后再删掉该短期key。

## 缓存穿透

- 访问一个不存在的key，缓存不起作用，请求会穿透到DB，流量大时DB就会挂掉。
- 解决方案：
  - 采用布隆过滤器，使用一个足够大的bitmap，用于存储可能访问的key，不存在的key直接过滤掉。

[目录](#目录)

## 降低Redis内存占用

- 可以减少创建快照、加载快照所需的时间。
- 提高载入AOF文件、重写AOF文件时的效率。
- 减少从服务器同步数据所需的时间。
- 在相同的硬件条件下，可以存储更多的数据。

### 短结构
- 当列表、散列、有序集合的长度较短(或者体积较小)时，Redis可以使用**压缩列表(ziplist)** 的紧凑存储方式来存储这些结构。
    - 压缩列表 是以序列化的方式存储数据，每次读写都要进行编解码，还可能对内存中的数据移动。
- 体积较小的集合，如果整数包含的所有成员可以被解释为十进制整数，并且又处于平台的有符号整数范围内，且集合成员少，Redis可以使用有序整数数组的方式存储集合（**整数集合**）。
- 上述两种方式的问题：
    - 当一个结构突破为压缩列表 或整数集合设置的限制条件时，紧凑结构的体积变大，操作速度会变慢，因此Redis会自动将它转为底层结构类型，导致性能降低。

[目录](#目录)

### 分片结构
- 将数据划分为更小的结构，然后根据数据所属的部分将数据发送到对应位置。

[目录](#目录)

### 打包存储二进制位和字节
- 使用分片Redis字符串 存储大量简短 并带有连续id的数据信息。

[目录](#目录)


# Redis特性
- 非关系型数据库。
- 与高性能键值缓存服务器memcached性能相近，但是Redis支持数据类型丰富。
- 发布/订阅、主从复制、持久化、脚本。

[目录](#目录)

# 命令
## 字符串命令
- `get`：获取给定键的值。
- `set`：设置给定键的值。
- `del`：删除给定键的值。（可用于所有数据类型）
------
- `incr keyname`：给键keyname存储的值加1。（键要是不存在，值会被当为0处理）
- `decr keyname`：给键keyname存储的值减1。
- `incrby keyname amount`：给键keyname存储的值加上整数amount。
- `decrby keyname amoun`t：给键keyname存储的值减去整数amount。
- `incrbyfloat keyname amount`：给键keyname存储的值加上浮点数amount。（Redis2.6以上支持）
------
- 其他命令TODO

[目录](#目录)

## 列表命令
- `lpush`、`rpush`：添加元素到列表左端、右端。
- `lpop`、`rpop`：从列表左端、右端取出元素。
- `lindex`：获取列表在给定位置的元素。
- `lrange`：获取列表在给定范围所有元素。
- `ltrim keyname start end`：修剪列表只保留[start,end]内的元素。
------
- 其他命令TODO

[目录](#目录)

## 集合命令
- `sadd`：添加元素到集合，返回添加到集合中本不存在的元素数量。
- `srem`：从集合删除元素，返回移除元素的数量。
- `sismember`：检查一个元素是否存在于集合中。
- `smembers`：获取集合中所有元素。
- `scard keyname`：返回集合keyname包含的元素数量。
- `srandmember keyname [count]`：从集合中随机返回一个或多个元素。count为正数时，返回的随机元素不会重复；count为负数时，返回的随机元素可能重复。
- `spop keyname`：随机移除集合中一个匀速，并返回被移除元素。
- `smove source—key dest-key item`：将集合source—key中的元素item移除，并添加到集合dest-key中；如果item被成功移除，返回1，否则返回0。
------
- `sdiff`：`sdiff keyname [keyname ...]`：返回存在于第一个集合、但不存在于其他集合中的元素。
- `sdiffstore`：`sdiffstore dest-key key-name [key-name ...]`：将存在于第一个集合、但不存在于其他集合中的元素 存储到dest-key键中。
- `sinter`：`sinter key-name [key-name ...]`：返回存在于所有集合中的元素。
- `sinterstore`：`sinterstore dest-key key-name [key-name ...]`：将存在于所有集合中的元素存储到dest-key键中。
- `sunion`：`sunion key-name [key-name ...]`：返回至少存在于一个集合中的元素。
- `sunionstore`：`sunionstore dest-key key-name [key-name ...]`：将至少存在于一个集合中的元素存储到dest-key键中。

[目录](#目录)

## 散列命令
- `hset`：添加键值对到散列。
- `hget`：获取指定散列键的值。
- `hgetall`：获取散列中所有键值对。
- `hdel`：从散列中删除键值对。
------
- `hmget`：`hmget key-name key [key ...]`：从散列获取一个或多个键的值。
- `hmset`：`hmset key-name key value [key value ...]`：给散列中一个或多个键设置值。
- `hdel`：`hdel key-name key [key ...]`：删除散列中一个或多个键值对，返回删除成功的数量。
- `hlen`：`hlen key-name`：返回散列中键值对数量。
------
- `hexists key-name key`：检查给定键是否存在于散列中。
- `hkeys key-name`：获取散列包含的所有键。
- `hvals key-name`：获取散列包含的所有值。
- `hgetall key-name`：获取散列包含的所有键值对。
- `hincrby key-name key increment`：将键key存储的值加上整数increment。
- `hincrbyfloat key-name key increment`：将键key存储的值加上浮点数increment。

[目录](#目录)

## 有序集合命令
- `zadd`：添加成员到有序集合中。
- `zrange key-name start end [withscores]`：返回有序集合中排名在start到end之间的成员；可选withscores表示命令会将成员分值一起返回。
- `zrem`：从有序集合中删除指定成员，返回移除成员数量。
- `zrangebyscore`：获取有序集合在给定分值范围内的所有元素。
- `zcrad key-name`：返回有序集合包含的成员数量。
- `zincrby key-name increment member`：将member成员的分值加上increment。
- `zount key-name min max`：返回分值介于min到max之间的成员数量。
- `zrank key-name member`：返回成员member在有序集合中的排名。
- `zscore key-name member`：返回成员member的分值。
------
- 其他命令TODO

[目录](#目录)

## 发布/订阅命令
- `subscribe`：`subscribe channel [channel ...]`：订阅给定的一个或多个频道。
- `unsubscribe`：`unsubscribe [channel [channel ...]]`：退订给定的一个或多个频道，没指定则退订所有。
- `publish`：`publish channel message`：向给定频道发送消息。
- `psubscribe`：`psubscribe pattern [pattern ...]`：订阅与给定模式相匹配的所有频道。
- `punsubscribe`：`punsubscribe [pattern [pattern ...]]`：退订给定模式，没指定则退订所有模式。

[目录](#目录)

## 其他命令
- `sort`：`sort source-key [by pattern] [limit offset count] [get pattern [get pattern ...]] [asc|desc] [alpha] [store dest-key]`：排序
- `multi`：声明一个事务开始
- `exec`：声明一个事务结束
- `persist key-name`：移除键的过期时间。
- `ttl key-name`：查看给定键还有多少秒过期。
- `expire key-name seconds`：让给定键在指定秒数后过期。
- `expireat key-name timestamp`：将给定键的过期时间设置为指定的unix时间戳。
- `pttl key-name`：查看给定键距离过期时间还有多少毫秒。
- `pexpire key-name milliseconds`：让给定键在在指定毫秒数后过期。
- `pexpireat key-name timestamp-milliseconds`：将给定键过期时间设置为指定的毫秒级精度的unix时间戳。

[目录](#目录)

# 复制
- 用户可以通过`SLAVEOF命令` 或者启动Redis时`slaveof配置`，让一个从服务器去复制主服务器。
    - `SLAVEOF <master_ip> <master_port>`
    - 例如：`127.0.0.1:12345> SLAVEOF 127.0.0.1 6379`：12345服务器将成为6379的从服务器。

## 复制功能的实现
- 版本：2.8+
- 使用`PSYNC`命令代替旧版SYNC命令来执行复制时的同步操作。
    - `PSYNC`命令分为两种模式：完整重同步、部分重同步。
        - 完整重同步：用于处理初次复制的情况(与SYNC命令步骤相同)。
            - 从服务器向主服务器发送SYNC命令。
            - 收到SYNC命令后，主服务器执行BGSAVE命令，在后台生成一个RDB文件，并使用一个缓冲区记录从现在开始执行的所有写命令。
            - 主服务器BGSAVE执行完后，将生成的RDB文件发给从服务器，从服务器接收并更新自己数据库状态 到主服务器执行BGSAVE命令时的数据库状态。
            - 主服务器将记录在缓冲区的所有写命令发给从服务器，从服务器执行这些写命令，并更新自己数据库状态。
        - 部分重同步：用于处理断线后重复制情况：当从服务器断线后重连主服务器时，如果条件允许，主服务器会将主从服务器断开期间执行的写命令发送给从服务器，从服务器只要接收并执行这些写命令就可以更新数据库与当前主服务器的数据一致。
- 复制的实现：
    - 设置主服务器的地址和端口：`127.0.0.1:12345> SLAVEOF 127.0.0.1 6379`
    - 建立套接字连接：从服务器根据命令的ip+port创建连向主服务器的套接字连接。（“从服务器是主服务器的客户端”）
    - 发送PING命令：从服务器12345向主服务器6379发送PING命令。
    - 身份验证：从服务器12345收到主服务器6379的PONG回复后 决定是否进行身份验证(取决于从服务器是否设置了`masterauth`选项)。
    - 发送端口信息：身份验证后，从服务器执行命令`REPLCONF listening-port <port-number>`向主服务器发送自己的监听端口。
    - 同步：从服务器向主服务器发送`PSYNC`命令将自己的数据库同步到主服务器数据库当前状态。
    - 命令传播：完成同步后，主服务器进入命令传播阶段，将自己执行的写命令发给从服务器，从服务器一直接受就可以与主服务器保持一致了。

[目录](#目录)

## 主从服务器心跳检测机制
- 在命令传播阶段，从服务器默认1秒1次向主服务器发送命令：`REPLCONF ACK <replication_offset>`
    - `replication_offset`为从服务器当前的复制偏移量。
    - 作用：
        - 检测主服务器的网络连接状态。
            - 主服务器超过1秒没有收到从服务器的`REPLCONF ACK`命令，就会知道主从之间的连接出问题了。
        - 辅助实现`min-slaves`选项。
            - `min-slaves-to-write`和`min-slaves-max-lag`可以防止主服务器在不安全的情况下执行写命令。
            - 例如：`min-slaves-to-write 3`和`min-slaves-max-lag 10`：表示从服务器数量少于3个，或者3个从服务器的延迟值(lag)都大于等于10秒时，主服务器拒绝执行写命令。
        - 检测命令丢失。
            - 如果网络故障，主服务器发给从服务器的写命令中途丢失，那么当从服务器向主服务器发送`REPLCONF ACK`命令时，主服务器会发现从服务器当前的复制偏移量少于自己的复制偏移量，就会在复制积压缓冲区里找到从服务器缺少的数据，并重新发给从服务器。

[目录](#目录)

# 持久化
- Redis提供两种持久化方法将数据存储到硬盘中。
- `SAVE`命令会阻塞Redis服务器进程，期间服务器不能处理任何命令请求，直到RDB文件创建完毕。
- `BGSAVE`命令会派生出一个子进程来创建RDB文件，服务器进程继续处理命令请求。

## 快照持久化
- **快照**(snapshotting)：可以将存于某一时刻的所有数据都写入硬盘中。
- 创建快照的方法：
    - 客户端向Redis发送`BGSAVE`命令来创建一个快照。
    - 客户端向Redis发送`SAVE`命令来创建一个快照。
    - 如果设置了`save`配置选项（比如`save 60 10000`），那么从Redis最近一次创建快照之后算起，当条件满足时（60秒之内写入10000次），Redis就会自动触发`BGSAVE`命令。
        - 如果设置了多个`save`配置选项，那么任意一个条件满足时Redis都会触发一次`BGSAVE`命令。
    - 当Redis通过`SHUTDOWN`命令收到关闭服务器请求时（或者收到标准`TERM`信号时），会执行一个`SAVE`命令，阻塞所有客户端，不再执行客户端的命令，当`SAVE`执行完后关闭服务器。
    - 当一台Redis服务器连接另一台Redis服务器，发送`SYNC`命令来开始一次复制操作时，如果主服务器当前没有在执行`BGSAVE`操作（或者刚执行完），那么主服务器就会执行`BGSAVE`命令。
- 如果系统发生崩溃，将丢失最近一次生成快照之后更改的所有数据。
    - 因此，快照持久化只适用于即使丢失一部分数据也不会造成影响的应用。

[目录](#目录)

### 使用快照持久化
- 个人开发：
    - 设置`save 900 1`：在900秒内至少执行1次写入操作，Redis会自动开始一次新的`BGSAVE`操作。
        - 目的：降低快照持久化带来的资源消耗。
- 日志聚合计算:
    - 分析能够承受丢失多长时间内产生的数据，如果是1小时，可以设置`save 3600 1`。
    - 解决如何恢复因故障而被中断的日志处理操作：//TODO
- 大数据：

[目录](#目录)

## AOF持久化
- **只追加文件**(append-only file)：会在执行写命令时，将被执行的写命令复制到硬盘中。
- AOF持久化 可以通过`appendonly yes`配置选项来打开。
- appendfsync配置选项对AOF文件的同步频率的影响：
    - `always`：每个Redis写命令都要同步写入硬盘。这样会严重降低Redis的速度。
    - `everysec`：每秒执行一次同步，显示地将多个写命令同步到硬盘。
        - **`appendfsync everysec`**兼顾安全和性能，即使系统崩溃，最多也只丢失1秒的数据。
    - `no`：让操作系统决定应该何时进行同步。
        - `appendfsync no`对性能几乎无影响，但系统崩溃的话，不确定丢失数据的数量。
        - 一般不推荐
- AOF持久化缺点：
    - 随着Redis不断运行，写命令不断地被记录到AOF文件，导致AOF文件体积不断增大，甚至用完所有磁盘空间。
        - 解决：**AOF重写**：向Redis发送`BGREWRITEAOF`命令，来移除AOF文件中的冗余命令来**重写**AOF文件，使AOF文件体积变小。
        - 可以设置`auto-aof-rewrite-percentage`和`auto-aof-rewrite-min-size`来自动执行`BGREWRITEAOF`。
            - 例如：`auto-aof-rewrite-percentage 100`和`auto-aof-rewrite-min-size 64mb`并开启AOF持久化：表示当AOF文件大于64MB，且体积比上一次重写之后大了至少一倍（100%），Redis将执行`BGREWRITEAOF`。
    - Redis重启后，需要重新执行AOF文件记录的所有写命令来还原数据集，如果AOF文件体积很大，还原操作的时间可能会很长。

[目录](#目录)

# 事务
- Redis通过`MULTI`、`EXEC`、`WATCH`等命令实现事务功能。

- 让一个客户端在不被其他客户端打断的情况下执行多个命令。
- 在redis中，被`MULTI`和`EXEC`命令包裹的所有命令会一个接一个执行，直到所有执行完。
    - 当redis从一个客户端收到`MULTI`命令开始，会将这个客户端之后发送的命令放到一个队列，直到这个客户端发送`EXEC`命令为止。

[目录](#目录)

# Redis哨兵
- Sentinel(哨兵)是Redis高可用性解决方案。
    - 由一个或多个哨兵实例组成的哨兵系统，可以监视任意多个主服务器 和属下的所有从服务器，并在被监视的主服务器下线时，自动将其属下的某个从服务器升为新的主服务器，由新的主服务器继续处理请求。
- Sentinel本质上是一个运行在特殊模式下的Redis服务器。

## Sentinel初始化
- 启动一个Sentinel：`redis-sentinel /path/to/your/sentinel.conf`
    - 或者：`redis-server /path/to/your/sentinel.conf --sentinel`
    - 启动步骤：
        - 初始化服务器。
        - 将一部分普通Redis服务器使用的代码替换成Sentinel专用代码。
        - 初始化Sentinel状态。
        - 根据配置文件，初始化Sentinel的监视主服务器列表。
        - 创建连向被监视主服务器的网络连接。

[目录](#目录)

## Sentinel监视服务器原理
- 获取主服务器信息：
    - Sentinel默认会以每`10秒1次`的频率向被监视的主服务器发送INFO命令，根据响应获取当前主服务器信息 （及其属下所有从服务器信息）。
- 获取从服务器信息：
    - Sentinel发现新的从服务器时，Sentinel会创建连接到从服务器的连接，之后，默认会以每`10秒1次`的频率向从服务器发送INFO命令，根据响应获取从服务器信息。
    
- 向主服务器和从服务器发送信息：
    - 默认，Sentinel以`2秒1次`的频率，通过**命令连接**向所有被监视的主服务器和从服务器发送命令：`PUBLISH __sentinel__:hello "<s_ip>,<s_port>,<s_runid>,<s_epoch>,<m_name>,<m_ip>,<m_port>,<m_epoch>"`
        - 以`s_`开头的为Sentinel的信息
        - 以`m_`开头的为主服务器信息
- 接收来自主服务器和从服务器的频道信息：
    - 当Sentinel与一个主服务器(或从服务器)建立订阅连接后，Sentinel会通过**订阅连接**向服务器发送命令：`SUBSCRIBE __sentinel__:hello`
    - 当Sentinel从`__sentinel__:hello`频道收到信息时，会解析出其中的Sentinel IP、PORT、运行ID等信息。

- 检测主观下线状态：
    - 默认，Sentinel会以`1秒1次`的频率向所有与它创建**命令连接**的实例发送PING命令，根据实例返回的PONG命令判断实例是否在线。
    - 如果实例在Sentinel配置文件中设置的`down-after-milliseconds`毫秒内连续返回无效回复，Sentinel会将此实例标记为主观下线状态。
- 检测客观下线状态：
    - 当Sentinel讲一个主服务器判断为主观下线后，会向其他监视该主服务器的Sentinel进行询问该主服务器是否是下线状态，当Sentinel标记下线数量足够多时，`Sentinel会将主服务器判定为客观下线，并对主服务器进行故障转移操作`。

[目录](#目录)

## Sentinel系统对主服务器进行故障转移
### 节点选举
- 当一个主服务器被判定为客观下线时，监视这个主服务器的各个Sentinel会进行协商选举一个领头Sentinel，并由领头Sentinel对下线主服务器进行故障转移操作。
- 选举领头Sentinel方法：
    - 所有在线的Sentinel都有被选为领头的资格。
    - 每次进行领头Sentinel选举后，不论选举是否成功，所有Sentinel的配置纪元(configuration epoch)的值都会自增一次。
    - ...
    - 领头Sentinel需要半数以上Sentinel支持。
    - 在指定时间内，没有一个Sentinel被选出，那么在一段时间后会再次选举，直到选出领头Sentinel为止。
- 一次选举领头Sentinel过程：
    - 条件：三个Sentinel(A、B、C)正在监视同一主服务器，并且已通过发送命令`SENTINEL is-master-down-by-addr`确认主服务器进入客观下线状态。
    - 三个Sentinel再次向其他Sentinel发送命令`SENTINEL is-master-down-by-addr`，与之前不同，此次命令带有Sentinel自己的运行ID。
    - 如果接收这个命令的Sentinel还没有设置局部领头Sentinel的话，就会将收到的发送命令给它的Sentinel设置为自己的局部领头Sentinel，并返回响应信息。
    - 接收到响应的Sentinel会根据回复统计出有多少个Sentinel将自己设置为局部领头Sentinel。
    - 根据命令请求的先后顺序不同，某个Sentinel就会胜出，胜出的领头Sentinel就可以开始对主服务器进行故障转移操作了。

- [故障转移](#故障转移)

[目录](#目录)


# 集群
- Redis集群通过分片来进行数据共享，并提供复制和故障转移功能。
- Redis服务器启动时是否开启集群配置：`cluster-enabled yes`

## 节点
- 一个Redis集群一般包含多个节点，要组成一个可工作集群，需要将各独立节点连接起来。
- 连接各节点命令：`CLUSTER MEET <ip> <port>`。
    - 向一个节点node发送命令`CLUSTER MEET <ip> <port>`，实现node节点与ip和port指定的节点握手，使得node节点将ip和port指定的节点加入到node节点所在的集群中。
        ```
        假设有三个独立节点：127.0.0.1:7000、127.0.0.1:7001、127.0.0.1:7002
        首先使用客户端连接7000端口的节点：redis-cli -c -p 7000
        查看当前集群节点：CLUSTER NODES
        向节点7000发送命令添加7001节点到当前集群：CLUSTER MEET 127.0.0.1 7001
        添加7002同理
        ```
- 集群状态下，节点会继续使用单机状态下的服务器组件，集群模式下才会使用的数据，节点会保存在：`cluster.h/clusterNode`、`cluster.h/clusterLink`、`cluster.h/clusterState`结构中。

[目录](#目录)

## 槽指派
- Redis集群通过分片方式保存数据库中的键值对：集群的整个数据库被分为16384个槽(slot)，数据库中每个键属于其中一个槽，集群中每个节点可以处理0-16384个槽。
- 集群是上线状态(ok)：数据库中的16384个槽都有节点在处理。
    - 集群是下线状态(fail)：数据库中任何一个槽都没有得到处理。
- 将槽指派给节点负责：`CLUSTER ADDSLOTS <slot> <slot...>`
    - 例如：`CLUSTER ADDSLOTS 0 1 2 3 4 ... 5000`
    - 要让三个节点所在的集群进入上线状态：继续执行命令将剩下的槽分别指派给节点7001 和 7002
    - 查看集群状态：`CLUSTER INFO`
- 节点负责处理哪些槽记录在：`cluster.h/clusterNode`的`slots`属性和`numsolt`属性中。
- 集群中所有槽的指派信息记录在：`cluster.h/clusterState`的`slots`数组中。
- **查看键属于哪个槽**：`CLUSTER KEYSLOT <key>`

[目录](#目录)

### 重新分片
- 指将已分配给某节点的槽重新分配给另一节点。
- 重新分片操作可以在线进行，集群不需要下线。
- 重新分片操作：
    - redis-trib对目标节点发送`CLUSTER SETSLOT <slot> IMPORTING <source_id>`命令，让目标节点准备好从源节点导入(import)属于槽slot的键值对。
    - redis-trib对源节点发送`CLUSTER SETSLOT <slot> MIGRATING <target_id>`命令，让源节点准备好将属于槽slot的键值对迁移(migrate)到目标节点。
    - redis-trib向源节点发送`CLUSTER GETKEYSINSLOT <slot> <count>`命令，获取最多count个属于槽slot的键值对的键名。
    - 对于上述获取到的每个键名，redis-trib都向源节点发送`MIGRATE <target_ip> <target_port> <key_name> 0 <timeout>`命令，将选中的键原子地从源节点迁移至目标节点。
    - 重复上述两个步骤，直到源节点的属于槽slot的键值对都被迁移至目标节点。
    - redis-trib向集群中的任一节点发送`CLUSTER SETSLOT <slot> NODE <target_id>`命令，将槽slot指派给目标节点，这一指派信息会通过消息通知到整个集群。
- ASK错误：在重新分片期间，被迁移槽的一部分键保存在源节点，一部分键已经迁移到目标节点，此时客户端向源节点发送一个命令，要处理属于被迁移槽的键时：
    - 源节点先查自己的库，看有没有键，如果有，就执行客户端发来的命令。
    - 如果源节点没有找到键，键有可能被迁移到目标节点，源节点会向客户端返回ASK错误，引导客户端对目标节点发送命令。

[目录](#目录)

## 复制与故障转移
- Redis集群中的节点分为主节点(master)和从节点(slave)。
    - **主节点**用于处理槽。
    - **从节点**用于复制某个主节点，并在被复制主节点下线时，代替下线主节点处理命令请求。
    - 例如：7000、7001、7002为主节点，现添加7003、7004为7000的从节点。
        - 当7000节点下线时，7003被选为新的主节点，接管原7000节点负责的槽。
        - 而7004节点改为复制7003节点。
- **设置从节点**：`CLUSTER REPLICATE <node_id>`

### 故障转移
- 故障检测：
    - 集群中每个节点都会定期向集群中的其他节点发送PING消息，如果接收节点没有在指定时间回复PONG消息，那么发送节点就会将接收节点标记为疑似下线(PFAIL)。
    - 一个集群中，半数以上负责处理槽的主节点都将某主节A点报告为疑似下线，那么主节点A将被标记为已下线(FAIL)，并向集群其他主节点进行通知，所有收到的节点都会将节点A标记为已下线。
    
- 故障转移：
    - 当一个从节点发现自己正在复制的主节点已下线时，从节点开始对下线主节点进行故障转移：
        - 复制下线主节点的从节点中会进行选举，选出一个新的主节点。
        - 被选举的从节点会执行`SLAVEOF no one`命令，成为主节点。
        - 新的主节点会撤销所有对已下线主节点的槽指派，并将这些槽全部指派给自己。
        - 新的主节点会向集群广播一条PONG消息，通知其他节点自己变成了主节点。
        - 新的主节点开始接收和处理自己负责的槽的命令请求，故障转移完成。
    - **选举新的主节点**：
        - //TODO
        - 类似[节点选举](#节点选举)

[目录](#目录)

# 分布式锁
- 使用`SETNX`命令获取锁(只会在键不存在的情况下为键设置值)，获取失败会一直重试，直到获取成功或者超时。
- 

[目录](#目录)

## 计数信号量

## 任务队列

## 可靠的消息传递
- 规避网络断线
- 防止Redis因消息积压耗费过多内存
- 见6.5

[目录](#目录)

## 文件分发

# 基于搜索


# 实战
- 围绕大型网上商店
    - 每天500万不同用户
    - 网站点击数1亿次
    - 从网站购买10万件商品
    - 网站负载量：平均写入1200次/s，高峰写入6000次/s
- 所提供解决方案可以在几GB内存的redis服务器使用
- 所提供解决方案目的在于提高系统响应实时请求的性能
- 所提供解决方案简单修改后可用于生产

## 使用Redis管理用户登录会话
- 存储登录信息到cookie的两种方法：
    - 签名(signed)cookie：存储用户名、用户id、用户最后一次登录的时间等信息。
    - 令牌(token)cookie：在cookie中存储一串随机字节作为令牌，服务器根据令牌在数据库中查找令牌拥有者。
- 这里使用令牌cookie引用数据库中存储用户登录信息的条目，还可以将用户访问时长、已浏览商品数量等信息存储到数据库。
- 1、使用一个散列来存储登录cookie令牌和已登录用户之间的映射，检查登录cookie：
```python
def check_token(conn, token):
    return conn.hget('login:', token)
```
- 2、大部分工作在于更新令牌，需要对用户每次的浏览信息进行更新：
    - 使用该方法写入记录20000次/s，性能比数据库提升10-100倍
```python
def update_token(conn, token, user, item=None):
    # 获取当前时间戳
    timestamp = time.time()
    # 维持令牌与已登录用户之间的映射
    conn.hset('login:', token, user)
    # 记录令牌最后一次出现的时间
    conn.zadd('recent:', token, timestamp)
    if item:
        # 记录用户浏览过的商品
        conn.zadd('viewed:'+token, item, timestamp)
        # 移除旧记录，只保留用户最近浏览的25个商品
        conn.zremrangebyrank('viewed:'+token, 0, -26)
```
- 3、限制会话数量，只保存最新的1000万个会话，清除旧会话：
```python
QUIT = False
LIMIT = 10000000

def clean_sessions(conn):
    while not QUIT:
        # 找出目前已有的令牌数量
        size = conn.zcard('recent:')
        if size <= LIMIT:
            time.sleep(1)
            continue
        # 获取需要移除的令牌
        end_index = min(size - LIMIT, 100)
        tokens = conn.zrange('recent:', 0, end_index-1)
        # 构建将被删除令牌的键
        session_keys = []
        for token in tokens:
            session_keys.append('viewed:'+token)
        # 移除旧令牌
        conn.delete(*session_keys)
        conn.hdel('login:', *tokens)
        conn.zrem('recent:', *tokens)
```

[目录](#目录)

## 使用Redis实现购物车

[目录](#目录)

# 安装
想要在 Java 中使用 Redis，我们首先需要安装 redis 服务及 Java redis 驱动。

## Windows下安装Redis
[下载地址：https://github.com/MSOpenTech/redis/releases](https://github.com/MSOpenTech/redis/releases。)

Redis 支持 32 位和 64 位。这个需要根据你系统平台的实际情况选择，这里我们下载 Redis-x64-xxx.zip压缩包到 C 盘，解压后，将文件夹重新命名为 redis。
![](https://img-blog.csdnimg.cn/20190414150417449.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FfbGV4Xw==,size_16,color_FFFFFF,t_70)
打开一个 cmd 窗口 使用cd命令切换目录到 C:\redis 运行 redis-server.exeredis.windows.conf 。

如果想方便的话，可以把 redis 的路径加到系统的环境变量里，这样就省得再输路径了，后面的那个 redis.windows.conf 可以省略，如果省略，会启用默认的。输入之后，会显示如下界面：
![](https://img-blog.csdnimg.cn/20190414150511399.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FfbGV4Xw==,size_16,color_FFFFFF,t_70)

这时候另启一个cmd窗口，原来的不要关闭，不然就无法访问服务端了。  
切换到redis目录下运行 redis-cli.exe -h 127.0.0.1 -p 6379 。  
设置键值对 set myKey abc  
取出键值对 get myKey  
![](https://img-blog.csdnimg.cn/20190414150550348.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FfbGV4Xw==,size_16,color_FFFFFF,t_70)

[目录](#目录)

## Jedis驱动的安装
[首先你需要下载最新驱动包：https://mvnrepository.com/artifact/redis.clients/jedis](https://mvnrepository.com/artifact/redis.clients/jedis)
- 在你的 classpath 中包含该驱动包。
- 连接到 redis 服务：
```
import redis.clients.jedis.Jedis;
public class RedisJava {
   public static void main(String[] args) {
       //连接本地的 Redis 服务
       Jedis jedis = new Jedis("localhost");
       System.out.println("连接成功");
       //查看服务是否运行
       System.out.println("服务正在运行: "+jedis.ping());
   }
}
```
- 编译以上 Java 程序，确保驱动包的路径是正确的。
```
连接成功
服务正在运行: PONG
```

[目录](#目录)

# Jedis命令
## Jedis-key
```
private void KeyOperate() 
   { 
       System.out.println("======================key=========================="); 
       // 清空数据 
       System.out.println("清空库中所有数据："+jedis.flushDB());
       // 判断key否存在 
       System.out.println("判断key999键是否存在："+shardedJedis.exists("key999")); 
       System.out.println("新增key001,value001键值对："+shardedJedis.set("key001", "value001")); 
       System.out.println("判断key001是否存在："+shardedJedis.exists("key001"));
       // 输出系统中所有的key
       System.out.println("新增key002,value002键值对："+shardedJedis.set("key002", "value002"));
       System.out.println("系统中所有键如下：");
       Set<String> keys = jedis.keys("*"); 
       Iterator<String> it=keys.iterator() ;   
       while(it.hasNext()){   
           String key = it.next();   
           System.out.println(key);   
       }
       // 删除某个key,若key不存在，则忽略该命令。
       System.out.println("系统中删除key002: "+jedis.del("key002"));
       System.out.println("判断key002是否存在："+shardedJedis.exists("key002"));
       // 设置 key001的过期时间
       System.out.println("设置 key001的过期时间为5秒:"+jedis.expire("key001", 5));
       try{ 
           Thread.sleep(2000); 
       } 
       catch (InterruptedException e){ 
       } 
       // 查看某个key的剩余生存时间,单位【秒】.永久生存或者不存在的都返回-1
       System.out.println("查看key001的剩余生存时间："+jedis.ttl("key001"));
       // 移除某个key的生存时间
       System.out.println("移除key001的生存时间："+jedis.persist("key001"));
       System.out.println("查看key001的剩余生存时间："+jedis.ttl("key001"));
       // 查看key所储存的值的类型
       System.out.println("查看key所储存的值的类型："+jedis.type("key001"));
       /*
        * 一些其他方法：1、修改键名：jedis.rename("key6", "key0");
        *             2、将当前db的key移动到给定的db当中：jedis.move("foo", 1)
        */
   }
```
```
运行结果：
======================key==========================
清空库中所有数据：OK
判断key999键是否存在：false
新增key001,value001键值对：OK
判断key001是否存在：true
新增key002,value002键值对：OK
系统中所有键如下：
key002
key001
系统中删除key002: 1
判断key002是否存在：false
设置 key001的过期时间为5秒:1
查看key001的剩余生存时间：3
移除key001的生存时间：1
查看key001的剩余生存时间：-1
查看key所储存的值的类型：string
```

[目录](#目录)

## Jedis-String
```
private void StringOperate() 
   {  
       System.out.println("======================String_1=========================="); 
       // 清空数据 
       System.out.println("清空库中所有数据："+jedis.flushDB());

       System.out.println("=============增=============");
       jedis.set("key001","value001");
       jedis.set("key002","value002");
       jedis.set("key003","value003");
       System.out.println("已新增的3个键值对如下：");
       System.out.println(jedis.get("key001"));
       System.out.println(jedis.get("key002"));
       System.out.println(jedis.get("key003"));
       
       System.out.println("=============删=============");  
       System.out.println("删除key003键值对："+jedis.del("key003"));  
       System.out.println("获取key003键对应的值："+jedis.get("key003"));
       
       System.out.println("=============改=============");
       //1、直接覆盖原来的数据
       System.out.println("直接覆盖key001原来的数据："+jedis.set("key001","value001-update"));
       System.out.println("获取key001对应的新值："+jedis.get("key001"));
       //2、直接覆盖原来的数据  
       System.out.println("在key002原来值后面追加："+jedis.append("key002","+appendString"));
       System.out.println("获取key002对应的新值"+jedis.get("key002")); 
  
       System.out.println("=============增，删，查（多个）=============");
       /** 
        * mset,mget同时新增，修改，查询多个键值对 
        * 等价于：
        * jedis.set("name","ssss"); 
        * jedis.set("jarorwar","xxxx"); 
        */  
       System.out.println("一次性新增key201,key202,key203,key204及其对应值："+jedis.mset("key201","value201",
                       "key202","value202","key203","value203","key204","value204"));  
       System.out.println("一次性获取key201,key202,key203,key204各自对应的值："+
                       jedis.mget("key201","key202","key203","key204"));
       System.out.println("一次性删除key201,key202："+jedis.del(new String[]{"key201", "key202"}));
       System.out.println("一次性获取key201,key202,key203,key204各自对应的值："+
               jedis.mget("key201","key202","key203","key204")); 
       System.out.println();
               
           
       //jedis具备的功能shardedJedis中也可直接使用，下面测试一些前面没用过的方法
       System.out.println("======================String_2=========================="); 
       // 清空数据 
       System.out.println("清空库中所有数据："+jedis.flushDB());       
      
       System.out.println("=============新增键值对时防止覆盖原先值=============");
       System.out.println("原先key301不存在时，新增key301："+shardedJedis.setnx("key301", "value301"));
       System.out.println("原先key302不存在时，新增key302："+shardedJedis.setnx("key302", "value302"));
       System.out.println("当key302存在时，尝试新增key302："+shardedJedis.setnx("key302", "value302_new"));
       System.out.println("获取key301对应的值："+shardedJedis.get("key301"));
       System.out.println("获取key302对应的值："+shardedJedis.get("key302"));
       
       System.out.println("=============超过有效期键值对被删除=============");
       // 设置key的有效期，并存储数据 
       System.out.println("新增key303，并指定过期时间为2秒"+shardedJedis.setex("key303", 2, "key303-2second")); 
       System.out.println("获取key303对应的值："+shardedJedis.get("key303")); 
       try{ 
           Thread.sleep(3000); 
       } 
       catch (InterruptedException e){ 
       } 
       System.out.println("3秒之后，获取key303对应的值："+shardedJedis.get("key303")); 
       
       System.out.println("=============获取原值，更新为新值一步完成=============");
       System.out.println("key302原值："+shardedJedis.getSet("key302", "value302-after-getset"));
       System.out.println("key302新值："+shardedJedis.get("key302"));
       
       System.out.println("=============获取子串=============");
       System.out.println("获取key302对应值中的子串："+shardedJedis.getrange("key302", 5, 7));         
   }
```
```
运行结果：
======================String_1==========================
清空库中所有数据：OK
=============增=============
已新增的3个键值对如下：
value001
value002
value003
=============删=============
删除key003键值对：1
获取key003键对应的值：null
=============改=============
直接覆盖key001原来的数据：OK
获取key001对应的新值：value001-update
在key002原来值后面追加：21
获取key002对应的新值value002+appendString
=============增，删，查（多个）=============
一次性新增key201,key202,key203,key204及其对应值：OK
一次性获取key201,key202,key203,key204各自对应的值：[value201, value202, value203, value204]
一次性删除key201,key202：2
一次性获取key201,key202,key203,key204各自对应的值：[null, null, value203, value204]

======================String_2==========================
清空库中所有数据：OK
=============新增键值对时防止覆盖原先值=============
原先key301不存在时，新增key301：1
原先key302不存在时，新增key302：1
当key302存在时，尝试新增key302：0
获取key301对应的值：value301
获取key302对应的值：value302
=============超过有效期键值对被删除=============
新增key303，并指定过期时间为2秒OK
获取key303对应的值：key303-2second
3秒之后，获取key303对应的值：null
=============获取原值，更新为新值一步完成=============
key302原值：value302
key302新值：value302-after-getset
=============获取子串=============
获取key302对应值中的子串：302
```

[目录](#目录)

## Jedis-List
```
private void ListOperate() 
   { 
       System.out.println("======================list=========================="); 
       // 清空数据 
       System.out.println("清空库中所有数据："+jedis.flushDB()); 

       System.out.println("=============增=============");
       shardedJedis.lpush("stringlists", "vector"); 
       shardedJedis.lpush("stringlists", "ArrayList"); 
       shardedJedis.lpush("stringlists", "vector");
       shardedJedis.lpush("stringlists", "vector");
       shardedJedis.lpush("stringlists", "LinkedList");
       shardedJedis.lpush("stringlists", "MapList");
       shardedJedis.lpush("stringlists", "SerialList");
       shardedJedis.lpush("stringlists", "HashList");
       shardedJedis.lpush("numberlists", "3");
       shardedJedis.lpush("numberlists", "1");
       shardedJedis.lpush("numberlists", "5");
       shardedJedis.lpush("numberlists", "2");
       System.out.println("所有元素-stringlists："+shardedJedis.lrange("stringlists", 0, -1));
       System.out.println("所有元素-numberlists："+shardedJedis.lrange("numberlists", 0, -1));
       
       System.out.println("=============删=============");
       // 删除列表指定的值 ，第二个参数为删除的个数（有重复时），后add进去的值先被删，类似于出栈
       System.out.println("成功删除指定元素个数-stringlists："+shardedJedis.lrem("stringlists", 2, "vector")); 
       System.out.println("删除指定元素之后-stringlists："+shardedJedis.lrange("stringlists", 0, -1));
       // 删除区间以外的数据 
       System.out.println("删除下标0-3区间之外的元素："+shardedJedis.ltrim("stringlists", 0, 3));
       System.out.println("删除指定区间之外元素后-stringlists："+shardedJedis.lrange("stringlists", 0, -1));
       // 列表元素出栈 
       System.out.println("出栈元素："+shardedJedis.lpop("stringlists")); 
       System.out.println("元素出栈后-stringlists："+shardedJedis.lrange("stringlists", 0, -1));
       
       System.out.println("=============改=============");
       // 修改列表中指定下标的值 
       shardedJedis.lset("stringlists", 0, "hello list!"); 
       System.out.println("下标为0的值修改后-stringlists："+shardedJedis.lrange("stringlists", 0, -1));
       System.out.println("=============查=============");
       // 数组长度 
       System.out.println("长度-stringlists："+shardedJedis.llen("stringlists"));
       System.out.println("长度-numberlists："+shardedJedis.llen("numberlists"));
       // 排序 
       /*
        * list中存字符串时必须指定参数为alpha，如果不使用SortingParams，而是直接使用sort("list")，
        * 会出现"ERR One or more scores can't be converted into double"
        */
       SortingParams sortingParameters = new SortingParams();
       sortingParameters.alpha();
       sortingParameters.limit(0, 3);
       System.out.println("返回排序后的结果-stringlists："+shardedJedis.sort("stringlists",sortingParameters)); 
       System.out.println("返回排序后的结果-numberlists："+shardedJedis.sort("numberlists"));
       // 子串：  start为元素下标，end也为元素下标；-1代表倒数一个元素，-2代表倒数第二个元素
       System.out.println("子串-第二个开始到结束："+shardedJedis.lrange("stringlists", 1, -1));
       // 获取列表指定下标的值 
       System.out.println("获取下标为2的元素："+shardedJedis.lindex("stringlists", 2)+"\n");
   }
```
```
运行结果：
======================list==========================
清空库中所有数据：OK
=============增=============
所有元素-stringlists：[HashList, SerialList, MapList, LinkedList, vector, vector, ArrayList, vector]
所有元素-numberlists：[2, 5, 1, 3]
=============删=============
成功删除指定元素个数-stringlists：2
删除指定元素之后-stringlists：[HashList, SerialList, MapList, LinkedList, ArrayList, vector]
删除下标0-3区间之外的元素：OK
删除指定区间之外元素后-stringlists：[HashList, SerialList, MapList, LinkedList]
出栈元素：HashList
元素出栈后-stringlists：[SerialList, MapList, LinkedList]
=============改=============
下标为0的值修改后-stringlists：[hello list!, MapList, LinkedList]
=============查=============
长度-stringlists：3
长度-numberlists：4
返回排序后的结果-stringlists：[LinkedList, MapList, hello list!]
返回排序后的结果-numberlists：[1, 2, 3, 5]
子串-第二个开始到结束：[MapList, LinkedList]
获取下标为2的元素：LinkedList
```

[目录](#目录)

## Jedis-Set
```
private void SetOperate() 
   { 

       System.out.println("======================set=========================="); 
       // 清空数据 
       System.out.println("清空库中所有数据："+jedis.flushDB());
       
       System.out.println("=============增=============");
       System.out.println("向sets集合中加入元素element001："+jedis.sadd("sets", "element001")); 
       System.out.println("向sets集合中加入元素element002："+jedis.sadd("sets", "element002")); 
       System.out.println("向sets集合中加入元素element003："+jedis.sadd("sets", "element003"));
       System.out.println("向sets集合中加入元素element004："+jedis.sadd("sets", "element004"));
       System.out.println("查看sets集合中的所有元素:"+jedis.smembers("sets")); 
       System.out.println();
       
       System.out.println("=============删=============");
       System.out.println("集合sets中删除元素element003："+jedis.srem("sets", "element003"));
       System.out.println("查看sets集合中的所有元素:"+jedis.smembers("sets"));
       /*System.out.println("sets集合中任意位置的元素出栈："+jedis.spop("sets"));//注：出栈元素位置居然不定？--无实际意义
       System.out.println("查看sets集合中的所有元素:"+jedis.smembers("sets"));*/
       System.out.println();
       
       System.out.println("=============改=============");
       System.out.println();
       
       System.out.println("=============查=============");
       System.out.println("判断element001是否在集合sets中："+jedis.sismember("sets", "element001"));
       System.out.println("循环查询获取sets中的每个元素：");
       Set<String> set = jedis.smembers("sets");   
       Iterator<String> it=set.iterator() ;   
       while(it.hasNext()){   
           Object obj=it.next();   
           System.out.println(obj);   
       }  
       System.out.println();
       
       System.out.println("=============集合运算=============");
       System.out.println("sets1中添加元素element001："+jedis.sadd("sets1", "element001")); 
       System.out.println("sets1中添加元素element002："+jedis.sadd("sets1", "element002")); 
       System.out.println("sets1中添加元素element003："+jedis.sadd("sets1", "element003")); 
       System.out.println("sets1中添加元素element002："+jedis.sadd("sets2", "element002")); 
       System.out.println("sets1中添加元素element003："+jedis.sadd("sets2", "element003")); 
       System.out.println("sets1中添加元素element004："+jedis.sadd("sets2", "element004"));
       System.out.println("查看sets1集合中的所有元素:"+jedis.smembers("sets1"));
       System.out.println("查看sets2集合中的所有元素:"+jedis.smembers("sets2"));
       System.out.println("sets1和sets2交集："+jedis.sinter("sets1", "sets2"));
       System.out.println("sets1和sets2并集："+jedis.sunion("sets1", "sets2"));
       System.out.println("sets1和sets2差集："+jedis.sdiff("sets1", "sets2"));//差集：set1中有，set2中没有的元素
       
   }
```
```
运行结果：
======================set==========================
清空库中所有数据：OK
=============增=============
向sets集合中加入元素element001：1
向sets集合中加入元素element002：1
向sets集合中加入元素element003：1
向sets集合中加入元素element004：1
查看sets集合中的所有元素:[element001, element002, element003, element004]

=============删=============
集合sets中删除元素element003：1
查看sets集合中的所有元素:[element001, element002, element004]

=============改=============

=============查=============
判断element001是否在集合sets中：true
循环查询获取sets中的每个元素：
element001
element002
element004

=============集合运算=============
sets1中添加元素element001：1
sets1中添加元素element002：1
sets1中添加元素element003：1
sets1中添加元素element002：1
sets1中添加元素element003：1
sets1中添加元素element004：1
查看sets1集合中的所有元素:[element001, element002, element003]
查看sets2集合中的所有元素:[element002, element003, element004]
sets1和sets2交集：[element002, element003]
sets1和sets2并集：[element001, element002, element003, element004]
sets1和sets2差集：[element001]
```

[目录](#目录)

## Jedis-sortedSet
```
private void SortedSetOperate() 
   { 
       System.out.println("======================zset=========================="); 
       // 清空数据 
       System.out.println(jedis.flushDB()); 
       
       System.out.println("=============增=============");
       System.out.println("zset中添加元素element001："+shardedJedis.zadd("zset", 7.0, "element001")); 
       System.out.println("zset中添加元素element002："+shardedJedis.zadd("zset", 8.0, "element002")); 
       System.out.println("zset中添加元素element003："+shardedJedis.zadd("zset", 2.0, "element003")); 
       System.out.println("zset中添加元素element004："+shardedJedis.zadd("zset", 3.0, "element004"));
       System.out.println("zset集合中的所有元素："+shardedJedis.zrange("zset", 0, -1));//按照权重值排序
       System.out.println();
       
       System.out.println("=============删=============");
       System.out.println("zset中删除元素element002："+shardedJedis.zrem("zset", "element002"));
       System.out.println("zset集合中的所有元素："+shardedJedis.zrange("zset", 0, -1));
       System.out.println();
       
       System.out.println("=============改=============");
       System.out.println();
       
       System.out.println("=============查=============");
       System.out.println("统计zset集合中的元素中个数："+shardedJedis.zcard("zset"));
       System.out.println("统计zset集合中权重某个范围内（1.0——5.0），元素的个数："+shardedJedis.zcount("zset", 1.0, 5.0));
       System.out.println("查看zset集合中element004的权重："+shardedJedis.zscore("zset", "element004"));
       System.out.println("查看下标1到2范围内的元素值："+shardedJedis.zrange("zset", 1, 2));

   }
```
```
运行结果：
======================zset==========================
OK
=============增=============
zset中添加元素element001：1
zset中添加元素element002：1
zset中添加元素element003：1
zset中添加元素element004：1
zset集合中的所有元素：[element003, element004, element001, element002]

=============删=============
zset中删除元素element002：1
zset集合中的所有元素：[element003, element004, element001]

=============改=============

=============查=============
统计zset集合中的元素中个数：3
统计zset集合中权重某个范围内（1.0——5.0），元素的个数：2
查看zset集合中element004的权重：3.0
查看下标1到2范围内的元素值：[element004, element001]
```

[目录](#目录)

## Jedis-Hash
```
private void HashOperate() 
   { 
       System.out.println("======================hash==========================");
       //清空数据 
       System.out.println(jedis.flushDB()); 
       
       System.out.println("=============增=============");
       System.out.println("hashs中添加key001和value001键值对："+shardedJedis.hset("hashs", "key001", "value001")); 
       System.out.println("hashs中添加key002和value002键值对："+shardedJedis.hset("hashs", "key002", "value002")); 
       System.out.println("hashs中添加key003和value003键值对："+shardedJedis.hset("hashs", "key003", "value003"));
       System.out.println("新增key004和4的整型键值对："+shardedJedis.hincrBy("hashs", "key004", 4l));
       System.out.println("hashs中的所有值："+shardedJedis.hvals("hashs"));
       System.out.println();
       
       System.out.println("=============删=============");
       System.out.println("hashs中删除key002键值对："+shardedJedis.hdel("hashs", "key002"));
       System.out.println("hashs中的所有值："+shardedJedis.hvals("hashs"));
       System.out.println();
       
       System.out.println("=============改=============");
       System.out.println("key004整型键值的值增加100："+shardedJedis.hincrBy("hashs", "key004", 100l));
       System.out.println("hashs中的所有值："+shardedJedis.hvals("hashs"));
       System.out.println();
       
       System.out.println("=============查=============");
       System.out.println("判断key003是否存在："+shardedJedis.hexists("hashs", "key003"));
       System.out.println("获取key004对应的值："+shardedJedis.hget("hashs", "key004"));
       System.out.println("批量获取key001和key003对应的值："+shardedJedis.hmget("hashs", "key001", "key003")); 
       System.out.println("获取hashs中所有的key："+shardedJedis.hkeys("hashs"));
       System.out.println("获取hashs中所有的value："+shardedJedis.hvals("hashs"));
       System.out.println();
             
   }
```
```
运行结果：
======================hash==========================
OK
=============增=============
hashs中添加key001和value001键值对：1
hashs中添加key002和value002键值对：1
hashs中添加key003和value003键值对：1
新增key004和4的整型键值对：4
hashs中的所有值：[value001, value002, value003, 4]

=============删=============
hashs中删除key002键值对：1
hashs中的所有值：[value001, value003, 4]

=============改=============
key004整型键值的值增加100：104
hashs中的所有值：[value001, value003, 104]

=============查=============
判断key003是否存在：true
获取key004对应的值：104
批量获取key001和key003对应的值：[value001, value003]
获取hashs中所有的key：[key004, key003, key001]
获取hashs中所有的value：[value001, value003, 104]
```

[目录](#目录)

# Redis常用命令集
## 连接操作命令
- quit：关闭连接（connection）
- auth：简单密码认证
- help cmd： 查看cmd帮助，例如：help quit

## 持久化命令
- save：将数据同步保存到磁盘
- bgsave：将数据异步保存到磁盘
- lastsave：返回上次成功将数据保存到磁盘的Unix时戳
- shundown：将数据同步保存到磁盘，然后关闭服务

## 远程服务控制
- info：提供服务器的信息和统计
- monitor：实时转储收到的请求
- slaveof：改变复制策略设置
- config：在运行时配置Redis服务器

## 对value操作的命令
- exists(key)：确认一个key是否存在
- del(key)：删除一个key
- type(key)：返回值的类型
- keys(pattern)：返回满足给定pattern的所有key
- randomkey：随机返回key空间的一个
- keyrename(oldname, newname)：重命名key
- dbsize：返回当前数据库中key的数目
- expire：设定一个key的活动时间（s）
- ttl：获得一个key的活动时间
- select(index)：按索引查询
- move(key, dbindex)：移动当前数据库中的key到dbindex数据库
- flushdb：删除当前选择数据库中的所有key
- flushall：删除所有数据库中的所有key

## String
- set(key, value)：给数据库中名称为key的string赋予值value
- get(key)：返回数据库中名称为key的string的value
- getset(key, value)：给名称为key的string赋予上一次的value
- mget(key1, key2,…, key N)：返回库中多个string的value
- setnx(key, value)：添加string，名称为key，值为value
- setex(key, time, value)：向库中添加string，设定过期时间time
- mset(key N, value N)：批量设置多个string的值
- msetnx(key N, value N)：如果所有名称为key i的string都不存在
- incr(key)：名称为key的string增1操作
- incrby(key, integer)：名称为key的string增加integer
- decr(key)：名称为key的string减1操作
- decrby(key, integer)：名称为key的string减少integer
- append(key, value)：名称为key的string的值附加value
- substr(key, start, end)：返回名称为key的string的value的子串

## List
- rpush(key, value)：在名称为key的list尾添加一个值为value的元素
- lpush(key, value)：在名称为key的list头添加一个值为value的 元素
- llen(key)：返回名称为key的list的长度
- lrange(key, start, end)：返回名称为key的list中start至end之间的元素
- ltrim(key, start, end)：截取名称为key的list
- lindex(key, index)：返回名称为key的list中index位置的元素
- lset(key, index, value)：给名称为key的list中index位置的元素赋值
- lrem(key, count, value)：删除count个key的list中值为value的元素
- lpop(key)：返回并删除名称为key的list中的首元素
- rpop(key)：返回并删除名称为key的list中的尾元素
- blpop(key1, key2,… key N, timeout)：lpop命令的block版本。
- brpop(key1, key2,… key N, timeout)：rpop的block版本。
- rpoplpush(srckey, dstkey)：返回并删除名称为srckey的list的尾元素，并将该元素添加到名称为dstkey的list的头部

## Set
- sadd(key, member)：向名称为key的set中添加元素member
- srem(key, member) ：删除名称为key的set中的元素member
- spop(key) ：随机返回并删除名称为key的set中一个元素
- smove(srckey, dstkey, member) ：移到集合元素
- scard(key) ：返回名称为key的set的基数
- sismember(key, member) ：member是否是名称为key的set的元素
- sinter(key1, key2,…key N) ：求交集
- sinterstore(dstkey, (keys)) ：求交集并将交集保存到dstkey的集合
- sunion(key1, (keys)) ：求并集
- sunionstore(dstkey, (keys)) ：求并集并将并集保存到dstkey的集合
- sdiff(key1, (keys)) ：求差集
- sdiffstore(dstkey, (keys)) ：求差集并将差集保存到dstkey的集合
- smembers(key) ：返回名称为key的set的所有元素
- srandmember(key) ：随机返回名称为key的set的一个元素

## Hash
- hset(key, field, value)：向名称为key的hash中添加元素field
- hget(key, field)：返回名称为key的hash中field对应的value
- hmget(key, (fields))：返回名称为key的hash中field i对应的value
- hmset(key, (fields))：向名称为key的hash中添加元素field 
- hincrby(key, field, integer)：将名称为key的hash中field的value增加integer
- hexists(key, field)：名称为key的hash中是否存在键为field的域
- hdel(key, field)：删除名称为key的hash中键为field的域
- hlen(key)：返回名称为key的hash中元素个数
- hkeys(key)：返回名称为key的hash中所有键
- hvals(key)：返回名称为key的hash中所有键对应的value
- hgetall(key)：返回名称为key的hash中所有的键（field）及其对应的value

## Redis发布订阅命令
- PSUBSCRIBE pattern [pattern ...] ：订阅一个或多个符合给定模式的频道。
- PUBSUB subcommand [argument [argument ...]] ：查看订阅与发布系统状态。
- PUBLISH channel message ：将信息发送到指定的频道。
- PUNSUBSCRIBE [pattern [pattern ...]] ：退订所有给定模式的频道。
- SUBSCRIBE channel [channel ...] ：订阅给定的一个或多个频道的信息。
- UNSUBSCRIBE [channel [channel ...]] ：指退订给定的频道。

## Redis事务命令
- DISCARD ：取消事务，放弃执行事务块内的所有命令。
- EXEC ：执行所有事务块内的命令。
- MULTI ：标记一个事务块的开始。
- UNWATCH ：取消 WATCH 命令对所有 key 的监视。
- WATCH key [key ...] ：监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断。

## 查看keys个数
- keys *      // 查看所有keys
- keys prefix_*     // 查看前缀为"prefix_"的所有keys

## 清空数据库
- flushdb   // 清除当前数据库的所有keys
- flushall    // 清除所有数据库的所有keys

[目录](#目录)