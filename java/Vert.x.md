
> [https://vertxchina.github.io/vertx-translation-chinese/core/Core.html](https://vertxchina.github.io/vertx-translation-chinese/core/Core.html)

特性    |RxJava    |Vert.x    |Spring WebFlux
---|---|---|---
核心定位    |响应式编程库    |响应式工具包 + 微服务框架    |Spring生态的响应式Web框架
编程模型    |Observable/Flowable/Single等    |回调/Promise/Future + RxJava/Coroutines    |Flux/Mono (Project Reactor)
网络/HTTP   |❌ 不提供    |✅ 内置高性能HTTP/WebSocket/TCP服务器    |✅ 基于Netty/Undertow等
线程模型    |依赖调度器(Schedulers)    |✅ 内置多事件循环(Event Loop) + Worker线程    |✅ 基于事件循环(默认Netty)
依赖管理    |轻量级库    |模块化工具包    |需Spring Boot生态
适用场景    |异步流处理、复杂事件组合    |高性能网络应用、微服务、全栈响应式    |Spring项目响应式改造、Reactive Web服务



# 特性

- 事件驱动：基于事件循环机制，有效管理并发，提升应用性能。
- 多语言支持：允许使用Java、JavaScript、Groovy、Ruby等多种语言编写微服务。
- 轻量级：极低的内存占用，适合微服务架构。
- 模块化：丰富的组件生态，易于扩展和集成。




# 组件

## Core

Vert.x Core 提供了下列功能:

- 编写 TCP 客户端和服务端
- 编写支持 WebSocket 的 HTTP 客户端和服务端
- 事件总线
- 共享数据 —— 本地的Map和分布式集群Map
- 周期性、延迟性动作
- 部署和撤销 Verticle 实例
- 数据报套接字
- DNS客户端
- 文件系统访问
- 高可用性
- 集群

Vert.x Core中的功能相当底层，不提供诸如数据库访问、授权或高层Web应用的功能。

可以在Vert.x ext（扩展包）（Vert.x的扩展包是Vert.x的子项目集合，类似Web、Web Client、Data Access等）中找到这些功能。

```xml
<dependency>
    <groupId>io.vertx</groupId>
    <artifactId>vertx-core</artifactId>
    <version>{vertx.version}</version>
</dependency>
```


## Web


## Web Client


## Data Access


## Integration


## Event Bus Bridge


## Authentication、Authorisation


## Reactive


## Microservices


## IoT

### MQTT Server

这个组件提供了一个服务器，它能处理远程MQTT客户端连接，通信和信息交换。 它的API提供了,当接受到客户端发送的raw protocol消息时相应的事件和提供一些发送信息到客户端的功能。 
它不是一个功能齐全的MQTT broker，但可以用来建立类似的东西或者协议转换。
```xml
<dependency>
    <groupId>io.vertx</groupId>
    <artifactId>vertx-mqtt-server</artifactId>
    <version>{vertx.version}</version>
</dependency>
```

## Devops


## Clustering


## Services

### Service Factories

### Service Proxy




# 常见问题

## 1.阻塞事件循环

问题描述：在事件循环线程中执行阻塞操作（如长时间的计算或IO操作）会导致整个事件循环暂停，影响应用性能。

解决方案：使用Vert.x提供的异步API或工作线程执行阻塞操作。

```java
vertx.executeBlocking(promise -> {
    // 阻塞操作
    long result = someBlockingMethod();
    promise.complete(result);
}, res -> {
    if (res.succeeded()) {
        System.out.println("结果：" + res.result());
    } else {
        res.cause().printStackTrace();
    }
});
```

## 2. 忽视异常处理
   
问题描述：Vert.x中的异步操作通常通过Handler回调，如果忽略异常处理，可能会导致问题难以追踪。

解决方案：总是检查Handler的失败情况，并适当处理异常。

```java
vertx.createHttpClient()
    .getNow(8080, "localhost", "/")
    .exceptionHandler(err -> {
        System.err.println("请求发生错误: " + err.getMessage());
    })
    .handler(response -> {
        // 处理响应
    });
```

## 3. 资源泄露

问题描述：未正确关闭或释放资源，尤其是在处理网络连接或文件操作时，可能导致内存泄漏。

解决方案：使用Vert.x的自动资源管理特性，如HTTP客户端的请求自动完成，或显式关闭资源。

```java
HttpServer server = vertx.createHttpServer();
server.requestHandler(req -> {
    req.response().end("Hello World!");
}).listen(8080, res -> {
    if (res.succeeded()) {
        System.out.println("服务器启动成功");
    } else {
        res.cause().printStackTrace();
    }
});
// 在应用结束时，考虑关闭服务器
```



# 术语翻译

- Client：客户端
- Server：服务器
- Primitive：基本（描述类型）
- Writing：编写（有些地方译为开发）
- Fluent：流式的
- Reactor：反应器，Multi-Reactor即多反应器
- Options：配置项，作为参数时候翻译成选项
- Context：上下文环境
- Undeploy：撤销（反部署，对应部署）
- Unregister：注销（反注册，对应注册）
- Destroyed：销毁
- Handler/Handle：处理器/处理，有些特定处理器未翻译，如Completion Handler等。
- Block：阻塞
- Out of Box：标准环境（开箱即用）
- Timer：计时器
- Event Loop Pool：事件轮询线程池，大部分地方未翻译
- Worker Pool：工作者线程池，大部分地方未翻译
- Sender：发送者
- Consumer：消费者
- Receiver/Recipient：接收者
- Entry：条目（一条key=value的键值对）
- Map：动词翻译成 “映射”，名词为数据结构未翻译
- Logging：动词翻译成 “记录”，名词翻译成日志器
- Trust Store：受信存储
- Frame：帧
- Event Bus：事件总线
- Buffer：缓冲区（一些地方使用的 Vert.x 中的 Buffer 类则不翻译）
- Chunk：块（HTTP 数据块，分块传输、分块模式中会用到）
- Pump：泵（平滑流式数据读入内存的机制，防止一次性将大量数据读入内存导致内存溢出）
- Header：请求/响应头
- Body：请求/响应体（有些地方翻译成请求/响应正文）
- Pipe：管道
- Round-Robin：轮询
- Application-Layer Protocol Negotiation：ALPN，应用层协议协商
- Wire：报文
- Flush：刷新（指将缓冲区中已有的数据一次性压入，用这种方式清空缓冲区，传统上翻译成刷新）
- Cipher Suite：密码套件
- Datagram：数据报
- Socket：套接字（有些地方未翻译，直接用的 Socket）
- Multicast：多播（组播）
- Concurrent Composition：并发合并
- High Availability：高可用性
- Multiplexing：多路复用
- Fail-Over：故障转移
- Hops：跳数（一台路由器/主机到另外一台路由器/主机所经过的路由器的数量，经过路由转发次数越多，跳数越大）
- Launcher：启动器


