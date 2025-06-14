# RabbitMQ概述

## 消息队列

消息队列传递消息一般有两种方式：

1. 点对点模式（P2P）：基于队列，生产者发送消息到队列，消费者从队列接收消息。一对一关系。
2. 发布/订阅模式（Pub/Sub）：发布者发布消息到主题（topic），消息订阅者从主题订阅消息。一对多关系。

消息中间件的作用：

1. **解耦**，应用程序之间不直接通信，而是基于存储和转发的应用程序之间的异步数据发送。可以使开发人员无需了解RPC 和网络通信协议的细节。
2. **存储**，面对数据处理失败，可以将数据持久化到最终处理成功。规避数据丢失风险。
3. **扩展性**，消息中间件解耦了应用的处理过程，只要另外增加处理过程 就可以提高消息入队和处理效率，无需修改代码、调节参数等。
4. **削峰**，可以处理因访问量突增导致的访问压力。
5. **可恢复性**，消息中间件降低进程间的耦合度，即使有处理消息的进程挂掉，加入的消息也可在系统回复后进行处理。
6. **顺序保证**，支持消息处理的顺序。
7. **缓冲**，可通过缓冲层控制消息处理的速度。
8. **异步通信**，消息中间件的异步处理机制，可以控制消息的处理时间。

## RabbitMQ简介

RabbitMQ是采用Erlang语言实现[AMQP](#AMQP)的消息中间件。

起源于金融系统，用于在分布式系统中存储转发消息。

## RabbitMQ特点

1. **高可靠**：使用持久化、传输确认、发布确认...等机制来保证可靠性。

2. **易扩展**：多个RabbitMQ可组成一个集群，可动态扩展集群中的节点。

3. **高可用**：队列可以在集群中的机器上设置镜像，使得在部分节点出现问题的情况下队列仍然可用。
4. **路由灵活**：在消息进入队列之前，通过交换器来路由消息。
   - 对于典型的路由功能，RabbitMQ 己经提供了一些内置的交换器来实现。
   - 针对更复杂的路由功能，可以将多个交换器绑定在一起， 也可以通过插件机制来实现自己的交换器。
5. **多协议支持**：原生支持AMQP 协议，还支持STOMP , MQTT 等多种消息中间件协议。
6. **功能特性丰富**：
   - 支持多种语言：Java、Python、PHP...等
   - 提供管理界面：可以监控、管理消息和集群中的节点等
   - 丰富的插件：提供许多插件来扩展RabbitMQ功能，支持自定义插件。



## RabbitMQ基本概念

1. server：又称broker，接收客户端连接，实现AMQP的消息队列服务器实体。
2. connection：和具体broker网络连接：
   - 无论是生产者还是消费者，都需要和 RabbitMQ Broker 建立连接，这个连接就是一条 TCP 连接，也就是Connection。  
   - 一旦 TCP 连接建立起来，客户端紧接着可以创建一个 AMQP 信道（Channel），每个信道都会被指派一个唯一的 ID。信道是建立在 Connection 之上的虚拟连接，RabbitMQ 处理的每条 AMQP 指令都是通过信道完成的。
3. channel：网络通道，基本上所有操作都在channel中进行，channel是消息读写的通道，每个channel表示一个会话任务，客户端可以建立多个channel。  
   - [可以直接使用Connection就能完成信道的工作，为什么还要引入信道呢？](#可以直接使用Connection就能完成信道的工作，为什么还要引入信道呢？)
4. message：消息，服务器和应用之间传递的数据。
   - 由properties和body组成。
   - properties可以对消息进行修饰，比如：消息的优先级，延迟等特性。
   - body是消息的实体内容。
5. Virtual host：虚拟主机，用于逻辑隔离，最上层消息的路由。一个Virtual host可以包含多个Exchange和Queue，同一个Virtual host下Exchange和Queue名字唯一。
6. **Exchange**：交换机，它指定消息按什么规则，路由到哪个队列。生产者发送消息到交换机，交换机根据路由键转发消息到一个或多个绑定的队列。  
   - 交换机有4种类型：
      - fanout：该类型的交换器不关心BindingKey，会将所有发送到该交换器的消息路由到所有与该交换器绑定的队列中。
      - direct：把消息路由到绑定键(BindingKey) 跟 路由键(RoutingKey)相同的队列中。（完全匹配）
      - topic：把消息路由到绑定键(BindingKey) 跟 路由键(RoutingKey)匹配的队列中。（模糊匹配）
        - 绑定键(BindingKey) 和 路由键(RoutingKey)使用点号`.`分隔字符串。
        - 绑定键(BindingKey)中，`*`匹配一个单词；`#`匹配多个单词(也可以是0个)。
      - headers：根据发送消息内容中的headers属性匹配路由。（性能差，不实用）
   
   - [为什么不直接将消息路由到队列，要一个交换机来转发呢？](#为什么不直接将消息路由到队列，要一个交换机来转发呢？)
7. **Banding**：绑定，将交换机和队列按路由规则关联起来，绑定时指定一个绑定键BindingKey，绑定键跟路由键匹配时消息会被路由到队列。（通常情况下，绑定键就是路由键）
8. **RoutingKey**：路由键，生产者将消息发送给交换机时指定，指定消息的路由规则，RoutingKey 需要与交换器类型和绑定键（BindingKey）联合使用才能最终生效，虚拟机根据它来确定如何路由一条消息。
9. **Queue**：消息队列，用于存储消息。
   - RabbitMQ 中消息都只能存储在队列中。
   - 这一点和 Kafka 这种消息中间件相反。Kafka 将消息存储在 topic（主题）这个逻辑层面，而相对应的队列逻辑只是 topic 实际存储文件中的位移标识。


大多数时候，包括官方文档和 RabbitMQ Java API 中都把 BindingKey 和 RoutingKey 看作 RoutingKey，为了避免混淆，可以这么理解：
- 在使用绑定的时候，其中需要的路由键是 BindingKey。涉及的客户端方法如：
   - channel.exchangeBind，对应的 AMQP 命令为 Exchange.Bind
   - channel.queueBind，对应的 AMQP 命令为 Queue.Bind
- 在发送消息的时候，其中需要的路由键是 RoutingKey。涉及的客户端方法如：
   - channel.basicPublish，对应的 AMQP 命令为 Basic.Publish

由于某些历史的原因，包括现存能搜集到的资料显示：大多数情况下习惯性地将 BindingKey 写成 RoutingKey，尤其是在使用 direct 类型的交换器的时候。


Rabbitmq模型架构：

![rabbitmq模型架构.png](../resources/static/images/rabbitmq-rabbitmq模型架构.png)

## RabbitMQ运转流程

### 生产者发送消息过程

1. 生产者连接到 RabbitMQ Broker，建立一个连接（Connection），开启一个信道（Channel）。
2. 生产者声明一个交换器，并设置相关属性，比如交换机类型、是否持久化等。
3. 生产者声明一个队列并设置相关属性，比如是否排他、是否持久化、是否自动删除等 。
4. 生产者通过路由键将交换器和队列绑定起来。
5. 生产者发送消息至 RabbitMQ Broker，其中包含路由键、交换器等信息。
6. 相应的交换器根据接收到的路由键查找相匹配的队列。
7. 如果找到，则将从生产者发送过来的消息存入相应的队列中。
8. 如果没有找到，则根据生产者配置的属性选择丢弃还是回退给生产者。
9. 关闭信道。
10. 关闭连接。

### 消费者接收消息过程

1. 消费者连接到 RabbitMQ Broker，建立一个连接（Connection），开启一个信道（Channel）。
2. 消费者向 RabbitMQ Broker 请求消费相应队列中的消息，可能会设置相应的回调函数，以及做一些准备工作。
3. 等待 RabbitMQ Broker 回应并投递相应队列中的消息，消费者接收消息。
4. 消费者确认（ack）接收到的消息。
5. RabbitMQ 从队列中删除相应已经被确认的消息。
6. 关闭信道。
7. 关闭连接。

## 死信队列

[https://www.jianshu.com/p/986ee5eb78bc](https://www.jianshu.com/p/986ee5eb78bc)

- [死信队列](#死信队列DLX)：DLX（`dead-letter-exchange`）

- 利用DLX，当消息在一个队列中变成死信 `(dead message)` 之后，它能被重新publish到另一个Exchange，这个Exchange就是DLX

**消息变成死信的情况**：

- 消息被拒绝(basic.reject / basic.nack)，并且requeue = false
- 消息TTL过期
- 队列达到最大长度



## AMQP

Advanced Message Queuing Protocol ，高级消息队列协议

RabbitMQ是遵从AMQP协议的，换句话说，RabbitMQ就是AMQP协议的 Erlang 的实现（当然 RabbitMQ 还支持 STOMP2、MQTT3等协议）。

AMQP 的模型架构和 RabbitMQ 的模型架构是一样的，生产者将消息发送给交换器，交换器和队列绑定。当生产者发送消息时所携带的 RoutingKey 与绑定时的 BindingKey 相匹配时，消息即被存入相应的队列之中。消费者可以订阅相应的队列来获取消息。

RabbitMQ 中的交换器、交换器类型、队列、绑定、路由键等都是遵循的 AMQP 协议中相应的概念。目前以 AQMP 0-9-1 为基准进行介绍。

AMQP 协议本身包括三层：
1. Module Layer：位于协议最高层，主要定义了一些供客户端调用的命令，客户端可以利用这些命令实现自己的业务逻辑。例如，客户端可以使用 Queue.Declare 命令声明一个队列或者使用 Basic.Consume 订阅消费一个队列中的消息。
2. Session Layer：位于中间层，主要负责将客户端的命令发送给服务器，再将服务端的应答返回给客户端，主要为客户端与服务器之间的通信提供可靠性同步机制和错误处理。
3. Transport Layer：位于最底层，主要传输二进制数据流，提供帧的处理、信道复用、错误检测和数据表示等。

AMQP 说到底还是一个通信协议，通信协议都会涉及报文交互，
从 low-level 举例来说，AMQP 本身是应用层的协议，其填充于 TCP 协议层的数据部分。
而从 high-level 来说，AMQP 是通过协议命令进行交互的。AMQP 协议可以看作一系列结构化命令的集合，这里的命令代表一种操作，类似于 HTTP 中的方法（GET、POST、PUT、DELETE 等）。


# RabbitMQ使用

## RabbitMQ安装





## RabbitMQ基本使用

### 连接RabbitMQ

```java
ConnectionFactory factory = new ConnectionFactory(); 
factory.setUsername(USERNAME); 
factory.setPassword(PASSWORD); 
factory.setVirtualHost(virtualHost); 
factory.setHost(IP_ADDRESS); 
factory.setPort(PORT); 
Connection conn = factory.newConnection();

// 使用 URI 的方式：
ConnectionFactory factory = new ConnectionFactory();
factory.setUri("amqp://userName:password@ipAddress:portNumber/virtualHost");
Connection conn = factory.newConnection();
// Connection 接口被用来创建一个 Channel：
Channel channel = conn.createChannel();
// 在创建之后，Channel 可以用来发送或者接收消息了。
```

- Connection 可以用来创建多个 Channel 实例，但是 Channel 实例不能在线程间共享，应用程序应该为每一个线程开辟一个 Channel。
- 某些情况下 Channel 的操作可以并发运行，但是在其他情况下会导致在网络上出现错误的通信帧交错，同时也会影响发送方确认（publisher confirm）机制的运行，所以多线程间共享 Channel 实例是非线程安全的。
- 在使用 Channel 时判断其是否已处于关闭状态，可以捕获异常`com.rabbitmq.client.ShutdownSignalException`，也要捕获 `IOException` 或者 `SocketException`，以防 Connection 意外关闭。
```java
public void validMethod(Channel channel) {
    try { 
        ... 
        channel.basicQos(1); 
     } catch (ShutdownSignalException sse) { 
         // possibly check if channel was closed by the time we started action and reasons for closing it
    } catch (IOException ioe) {
        // check why connection was closed 
        ...
    }
}
```

### 使用交换机和队列

交换机和队列是 AMQP 中的 high-level 层面的构建模块，应用在使用它们时要确保已经存在，并在使用之前声明（declare）它们。

1. 声明（declare）一个交换机和队列：
```java
// 创建了一个持久化的、非自动删除的、绑定类型为 direct 的交换器
channel.exchangeDeclare(exchangeName, "direct", true); // 参数：交换机名，交换机类型，是否持久
// 创建了一个非持久化的、排他的、自动删除的队列（此队列的名称由 RabbitMQ 自动生成）
String queueName = channel.queueDeclare().getQueue(); 
// 使用路由键将队列和交换器绑定起来。
channel.queueBind(queueName, exchangeName, routingKey);
```

上面声明的队列具备如下特性：
- 只对当前应用中同一个 Connection 层面可用，
- 同一个 Connection 的不同 Channel 可共用，
- 并且也会在应用连接断开时自动删除。

2. 在应用中共享一个队列，声明如下：
```java
channel.exchangeDeclare(exchangeName, "direct", true); 
// (String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) // 参数：队列名，是否持久，是否互斥(排他、独占)，是否自动删除，参数
channel.queueDeclare(queueName, true, false, false, null); // 该队列声明为持久化的、非独占的、非自动删除的
channel.queueBind(queueName, exchangeName, routingKey);
```

- Channel的API方法都是可以重载的，比如exchangeDeclare、queueDeclare，根据参数不同，可以有不同的重载形式，根据自身的需要进行调用。
- 生产者和消费者都可以声明一个交换器或者队列。
  - 如果尝试声明一个已经存在的交换器或者队列，只要声明的参数完全匹配现存的交换器或者队列，RabbitMQ 就可以什么都不做，并成功返回。
  - 如果声明的参数不匹配则会抛出异常。

#### 声明交换机exchangeDeclare方法详解

exchangeDeclare 有多个重载方法，这些重载方法都是由下面这个方法中缺省的某些参数构成的：

```java
Exchange.DeclareOk exchangeDeclare(String exchange, 
                                   String type, 
                                   boolean durable, 
                                   boolean autoDelete, 
                                   boolean internal, 
                                   Map<String, Object> arguments) throws IOException;
```
- `Exchange.DeclareOk`：该返回值类型用来标识成功声明一个交换器。
- `exchange`：交换器的名称。
- `type`：交换器的类型，常见的如 fanout、direct、topic
- `durable`：是否持久化。持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。
- `autoDelete`：是否自动删除。自动删除的前提是至少有一个队列或者交换器与这个交换器绑定，之后所有与这个交换器绑定的队列或者交换器都与此解绑。注意不能错误地把这个参数理解为：“当与此交换器连接的客户端都断开时，RabbitMQ 会自动删除本交换器”。
- `internal`：是否是内置的。如果设置为 true，则表示是内置的交换器，客户端程序无法直接发送消息到这个交换器中，只能通过交换器路由到交换器这种方式。
- `arguments`：其他一些结构化参数，比如 alternate-exchange。


exchangeDeclare 的其他重载方法如下：
1. `Exchange.DeclareOk exchangeDeclare(String exchange, String type) throws IOException;`
2. `Exchange.DeclareOk exchangeDeclare(String exchange, String type, boolean durable) throws IOException;`
3. `Exchange.DeclareOk exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments) throws IOException;`

**exchangeDeclareNoWait** 比 exchangeDeclare 多设置了一个 nowait 参数，这个 nowait 参数指的是 AMQP 中 Exchange.Declare 命令的参数，意思是不需要服务器返回，注意这个方法的返回值是 void，
而普通的 exchangeDeclare 方法的返回值是 Exchange.DeclareOk, 意思是在客户端声明了一个交换器之后，需要等待服务器的返回（服务器会返回 Exchange.Declare-Ok 这个 AMQP 命令）。  
**(不建议使用，声明完一个交换器之后（实际服务器还并未完成交换器的创建），那么此时客户端紧接着使用这个交换器，必然会发生异常。如果没有特殊的缘由和应用场景，不建议使用这个方法。)**

**exchangeDeclarePassive** `Exchange.DeclareOk exchangeDeclarePassive(String name) throws IOException;`  
这个方法在实际应用过程中还是非常有用的，它主要用来检测相应的交换器是否存在。如果存在则正常返回；如果不存在则抛出异常：404 channel exception，同时 Channel 也会被关闭。


删除交换器的方法：
1. `Exchange.DeleteOk exchangeDelete(String exchange) throws IOException;`  exchange 表示交换器的名称
2. `void exchangeDeleteNoWait(String exchange, boolean ifUnused) throws IOException;`
3. `Exchange.DeleteOk exchangeDelete(String exchange, boolean ifUnused) throws IOException;`  ifUnused 用来设置是否在交换器没有被使用的情况下删除。true：交换机没有使用时才会被删；false：无论是否使用都要删除。


#### 声明队列queueDeclare方法详解

1. `Queue.DeclareOk queueDeclare() throws IOException;`  默认创建一个由 RabbitMQ 命名的（类似这种amq.gen-LhQz1gv3GhDOv8PIDabOXA 名称，这种队列也称之为匿名队列）、排他的、自动删除的、非持久化的队列。
2. `Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) throws IOException;`

- `queue`：队列的名称。
- `durable`：设置是否持久化。为 true 则设置队列为持久化。持久化的队列会存盘，在服务器重启的时候可以保证不丢失相关信息。
- `exclusive`：设置是否排他。为 true 则设置队列为排他的。如果一个队列被声明为排他队列，该队列仅对首次声明它的连接可见，并在连接断开时自动删除。这里需要注意3点：
  - 排他队列是基于连接（Connection）可见的，同一个连接的不同信道（Channel）是可以同时访问同一连接创建的排他队列；
  - “首次” 是指如果一个连接已经声明了一个排他队列，其他连接是不允许建立同名的排他队列的，这个与普通队列不同；
  - 即使该队列是持久化的，一旦连接关闭或者客户端退出，该排他队列都会被自动删除，这种队列适用于一个客户端同时发送和读取消息的应用场景。
- `autoDelete`：设置是否自动删除。为 true 则设置队列为自动删除。自动删除的前提是： 
  - 至少有一个消费者连接到这个队列，之后所有与这个队列连接的消费者都断开时，才会自动删除。
  - 不能把这个参数错误地理解为：“当连接到此队列的所有客户端断开时，这个队列自动删除”，因为生产者客户端创建这个队列，或者没有消费者客户端与这个队列连接时，都不会自动删除这个队列。
- `arguments`：设置队列的其他一些参数，如 x-message-ttl、x-expires、x-max-length、x-max-length-bytes、x-dead-letter-exchange、x-dead-letter-routing-key、x-max-priority 等。

注意点：  
生产者和消费者都能够使用 queueDeclare 来声明一个队列，但是如果消费者在同一个信道上订阅了另一个队列，就无法再声明队列了。必须先取消订阅，然后将信道置为“传输”模式，之后才能声明队列。

**queueDeclareNoWait** 方法：`void queueDeclareNoWait(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) throws IOException;`  
不常用，方法的返回值也是 void，表示不需要服务端的任何返回。同样也需要注意，在调用完 queueDeclareNoWait 方法之后，紧接着使用声明的队列时有可能会发生异常情况。

**queueDeclarePassive** ：`Queue.DeclareOk queueDeclarePassive(String queue) throws IOException;`  
常用，这个方法用来检测相应的队列是否存在。如果存在则正常返回，如果不存在则抛出异常：404 channel exception，同时 Channel 也会被关闭。

删除队列方法：
1. `Queue.DeleteOk queueDelete(String queue) throws IOException;` 
2. `Queue.DeleteOk queueDelete(String queue, boolean ifUnused, boolean ifEmpty) throws IOException;`
3. `void queueDeleteNoWait(String queue, boolean ifUnused, boolean ifEmpty) throws IOException;` 

- queue 表示队列的名称
- ifUnused 用来设置是否在队列没有被使用的情况下删除。true：没有使用时才会被删；false：无论是否使用都要删除。
- ifEmpty 设置为 true 表示在队列为空（队列里面没有任何消息堆积）的情况下才能够删除。

**queuePurge** 区别于 queueDelete，这个方法用来清空队列中的内容，而不删除队列本身。`Queue.PurgeOk queuePurge(String queue) throws IOException;`

#### 绑定队列和交换机queueBind方法详解

绑定队列和交换器的方法：
1. `Queue.BindOk queueBind(String queue, String exchange, String routingKey) throws IOException;`
2. `Queue.BindOk queueBind(String queue, String exchange, String routingKey, Map<String, Object> arguments) throws IOException;`
3. `void queueBindNoWait(String queue, String exchange, String routingKey, Map<String, Object> arguments) throws IOException;`

- queue：队列名称；
- exchange：交换器的名称；
- routingKey：用来绑定队列和交换器的路由键；
- argument：定义绑定的一些参数。

解绑已经被绑定的队列和交换器：
1. `Queue.UnbindOk queueUnbind(String queue, String exchange, String routingKey) throws IOException;`
2. `Queue.UnbindOk queueUnbind(String queue, String exchange, String routingKey, Map<String, Object> arguments) throws IOException;`


#### 绑定交换机和交换机exchangeBind方法详解

绑定交换机和交换机：
1. `Exchange.BindOk exchangeBind(String destination, String source, String routingKey) throws IOException;`
2. `Exchange.BindOk exchangeBind(String destination, String source, String routingKey, Map<String, Object> arguments) throws IOException;`
3. `void exchangeBindNoWait(String destination, String source, String routingKey, Map<String, Object> arguments) throws IOException;`

绑定之后，消息从 source 交换器转发到 destination 交换器，某种程度上来说 destination 交换器可以看作一个队列。
```java
channel.exchangeDeclare("source", "direct", false, true, null); 
channel.exchangeDeclare("destination", "fanout", false, true, null); 
channel.exchangeBind("destination", "source", "exKey"); 
channel.queueDeclare("queue", false, false, true, null); 
channel.queueBind("queue", "destination", ""); 
channel.basicPublish("source", "exKey", null, "exToExDemo".getBytes());

// 生产者发送消息至交换器 source 中，交换器 source 根据路由键找到与其匹配的另一个交换器 destination，并把消息转发到 destination 中，进而存储在 destination 绑定的队列 queue 中
```

[rabbitmq-绑定交换机和交换机](../resources/static/images/rabbitmq-绑定交换机和交换机.png)

#### 何时创建

RabbitMQ 的消息存储在队列中，交换器的使用并不真正耗费服务器的性能，而队列会。如果要衡量 RabbitMQ 当前的 QPS 只需看队列的即可。
在实际业务应用中，需要对所创建的队列的流量、内存占用、网卡占用有一个清晰的认知，预估其平均值和峰值，以便在固定硬件资源的情况下能够进行合理有效的分配。

按照 RabbitMQ 官方建议，生产者和消费者都应该尝试创建（这里指声明操作）队列。这是一个很好的建议，但不适用于所有的情况。
如果业务本身在架构设计之初已经充分地预估了队列的使用情况，完全可以在业务程序上线之前在服务器上创建好（比如通过页面管理、RabbitMQ 命令或者更好的是从配置中心下发），这样业务程序也可以免去声明的过程，直接使用即可。

预先创建好资源还有一个好处是，可以确保交换器和队列之间正确地绑定匹配。
很多时候，由于人为因素、代码缺陷等，发送消息的交换器并没有绑定任何队列，那么消息将会丢失；
或者交换器绑定了某个队列，但是发送消息时的路由键无法与现存的队列匹配，那么消息也会丢失。
当然可以配合 mandatory 参数或者备份交换器来提高程序的健壮性。

预估好队列的使用情况非常重要，如果在后期运行过程中超过预定的阈值，可以根据实际情况对当前集群进行扩容或者将相应的队列迁移到其他集群。迁移的过程也可以对业务程序完全透明。
此种方法也更有利于开发和运维分工，便于相应资源的管理。

如果集群资源充足，而即将使用的队列所占用的资源又在可控的范围之内，为了增加业务程序的灵活性，也完全可以在业务程序中声明队列。

至于是使用预先分配创建资源的静态方式还是动态的创建方式，需要从业务逻辑本身、公司运维体系和公司硬件资源等方面考虑。



### 发送消息

1. 可以使用 Channel 类的 basicPublish 方法：
```java
byte[] messageBodyBytes = "Hello, world!".getBytes(); 
channel.basicPublish(exchangeName, routingKey, null, messageBodyBytes);
```
2. 使用 mandatory 参数更好的控制发送，或者发送一些特定属性的信息：
```java
channel.basicPublish(exchangeName, routingKey, mandatory, MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);

// 这条消息的投递模式（delivery mode）设置为 2，即消息会被持久化（即存入磁盘）在服务器中。同时这条消息的优先级（priority）设置为 1，content-type 为“text/plain”。
```
3. 自定义属性的消息：
```java
channel.basicPublish(exchangeName,
                    routingKey,
                    new AMQP.BasicProperties.Builder()
                        .contentType("text/plain")
                        .deliveryMode(2)
                        .priority(1)
                        .userId("hidden")
                        .build()),
                    messageBodyBytes);
```
4. 带有 headers 的消息：
```java
Map<String, Object> headers = new HashMap<String, Object>();
headers.put("localtion","here");
headers.put("time","today");
channel.basicPublish(exchangeName,
                    routingKey,
                    new AMQP.BasicProperties.Builder()
                        .headers(headers)
                        .build()),
                    messageBodyBytes);
```
5. 带有过期时间（expiration）的消息：
```java
channel.basicPublish(exchangeName,
                    routingKey,
                    new AMQP.BasicProperties.Builder()
                        .expiration("60000")
                        .build()),
                    messageBodyBytes);
```

对于 basicPublish 有几个重载方法：
1. `void basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body) throws IOException;`
2. `void basicPublish(String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body) throws IOException;`
3. `void basicPublish(String exchange, String routingKey, boolean mandatory, boolean immediate, BasicProperties props, byte[] body) throws IOException;`

- exchange：交换器的名称，指明消息需要发送到哪个交换器中。如果设置为空字符串，则消息会被发送到 RabbitMQ 默认的交换器中。
- routingKey：路由键，交换器根据路由键将消息存储到相应的队列之中。
- props：消息的基本属性集，其包含 14 个属性成员，分别如下，其中常用的几种都在上面的示例中进行了演示。 
  - contentType
  - contentEncoding
  - headers(Map<String,Object>)
  - deliveryMode
  - priority
  - correlationId
  - replyTo
  - expiration
  - messageId
  - timestamp
  - type
  - userId
  - appId
  - clusterId
- byte[] body：消息体（payload），真正需要发送的消息。
- mandatory 和 immediate 的详细内容请参考[消息发布后的流向](#消息发布后的流向)


### 消费消息

RabbitMQ 的消费模式分两种：[推模式(Push)、拉模式(Pull)](https://blog.csdn.net/u011709538/article/details/131404741) 。推模式采用 Basic.Consume 进行消费，而拉模式则是调用 Basic.Get 进行消费。

#### 推模式

在推模式中，可以通过持续订阅的方式来消费消息，使用到的相关类有：
```java
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
```
接收消息一般通过实现 Consumer 接口或者继承 DefaultConsumer 类来实现。
- 当调用与 Consumer 相关的 API 方法时，不同的订阅采用不同的消费者标签（consumerTag）来区分彼此，在同一个 Channel 中的消费者也需要通过唯一的消费者标签以作区分，关键消费代码如下：
    ```java
    boolean autoAck = false; 
    channel.basicQos(64); 
    channel.basicConsume(queueName, autoAck, "myConsumerTag", new DefaultConsumer(channel) { 
         @Override 
         public void handleDelivery(String consumerTag, 
                                    Envelope envelope, 
                                    AMQP.BasicProperties properties, 
                                    byte[] body) throws IOException { 
             String routingKey = envelope.getRoutingKey(); 
             String contentType = properties.getContentType(); 
             long deliveryTag = envelope.getDeliveryTag(); 
             // (process the message components here ...) 
             channel.basicAck(deliveryTag, false); 
         } 
    });
    
    // 注意，上面代码中显式地设置 autoAck 为 false，然后在接收到消息之后进行显式 ack 操作（channel.basicAck），对于消费者来说这个设置是非常必要的，可以防止消息不必要地丢失。
    ```

Channel 类中 basicConsume 方法有如下几种形式：
1. `String basicConsume(String queue, Consumer callback) throws IOException;`
2. `String basicConsume(String queue, boolean autoAck, Consumer callback) throws IOException;`
3. `String basicConsume(String queue, boolean autoAck, Map<String, Object> arguments, Consumer callback) throws IOException;`
4. `String basicConsume(String queue, boolean autoAck, String consumerTag, Consumer callback) throws IOException;`
5. `String basicConsume(String queue, boolean autoAck, String consumerTag, boolean noLocal, boolean exclusive, Map<String, Object> arguments, Consumer callback) throws IOException;`

- queue：队列的名称；
- autoAck：设置是否自动确认。建议设成 false，即不自动确认；
- consumerTag：消费者标签，用来区分多个消费者；
- noLocal：设置为 true 则表示不能将同一个 Connection 中生产者发送的消息传送给这个 Connection 中的消费者；
- exclusive：设置是否排他；
- arguments：设置消费者的其他参数；
- callback：设置消费者的回调函数。用来处理 RabbitMQ 推送过来的消息，比如 DefaultConsumer，使用时需要客户端重写（override）其中的方法。


对于消费者客户端来说重写 handleDelivery 方法是十分方便的。更复杂的消费者客户端会重写更多的方法，具体如下：
```java
void handleConsumeOk(String consumerTag);
void handleCancelOk(String consumerTag);
void handleCancel(String consumerTag) throws IOException;
void handleShutdownSignal(String consumerTag, ShutdownSignalException sig);
void handleRecoverOk(String consumerTag);
```
- 比如 handleShutdownSignal方法,当Channel或者Connection关闭的时候会调用。
- 再者，handleConsumeOk 方法会在其他方法之前调用，返回消费者标签。
- 重写 handleCancelOk 和 handleCancel 方法，这样消费端可以在显式地或者隐式地取消订阅的时候调用。也可以通过 channel.basicCancel 方法来显式地取消一个消费者的订阅：`channel.basicCancel(consumerTag);`
- 注意上面这行代码会首先触发 handleConsumerOk 方法，之后触发 handleDelivery 方法，最后才触发 handleCancelOk 方法。

和生产者一样，消费者客户端同样需要考虑线程安全的问题。消费者客户端的这些 callback 会被分配到与 Channel 不同的线程池上，这意味着消费者客户端可以安全地调用这些阻塞方法，比如 channel.queueDeclare、channel.basicCancel 等。

每个 Channel 都拥有自己独立的线程。最常用的做法是一个 Channel 对应一个消费者, 也就是意味着消费者彼此之间没有任何关联。当然也可以在一个 Channel 中维持多个消费者, 但是要注意一个问题，如果 Channel 中的一个消费者一直在运行，那么其他消费者的 callback 会被“耽搁”。


#### 拉模式

拉模式的消费方式。通过 channel.basicGet 方法可以单条地获取消息，其返回值是 GetResponse。Channel 类的 basicGet 方法没有其他重载方法，只有：
`GetResponse basicGet(String queue, boolean autoAck) throws IOException;`
其中 queue 代表队列的名称，如果设置 autoAck 为 false，那么同样需要调用 channel.basicAck 来确认消息已被成功接收。

拉模式的关键代码如:
```java
GetResponse response = channel.basicGet(QUEUE_NAME, false); 
System.out.println(new String(response.getBody())); 
channel.basicAck(response.getEnvelope().getDeliveryTag(),false);
```

注意点：  
Basic.Consume 将信道（Channel）置为接收模式，直到取消队列的订阅为止。
在接收模式期间，RabbitMQ 会不断地推送消息给消费者，当然推送消息的个数还是会受到 Basic.Qos 的限制。
如果只想从队列获得单条消息而不是持续订阅，建议还是使用 Basic.Get 进行消费。
但是不能将 Basic.Get 放在一个循环里来代替 Basic.Consume，这样做会严重影响 RabbitMQ 的性能。如果要实现高吞吐量，消费者理应使用 Basic.Consume 方法。



### 消费端的确认与拒绝

#### 确认消息

为了保证消息从队列可靠地达到消费者，RabbitMQ 提供了消息确认机制（message acknowledgement）。消费者在订阅队列时，可以指定 autoAck 参数：
- 当 autoAck 等于 false 时，RabbitMQ 会等待消费者显式地回复确认信号后才从内存（或者磁盘）中移去消息（实质上是先打上删除标记，之后再删除）。
- 当 autoAck 等于 true 时，RabbitMQ 会自动把发送出去的消息置为确认，然后从内存（或者磁盘）中删除，而不管消费者是否真正地消费到了这些消息。

采用消息确认机制后，只要设置 autoAck 参数为 false，消费者就有足够的时间处理消息（任务），不用担心处理消息过程中消费者进程挂掉后消息丢失的问题，因为 RabbitMQ 会一直等待持有消息直到消费者显式调用 Basic.Ack 命令为止。

当 autoAck 参数置为 false，对于 RabbitMQ 服务端而言，队列中的消息分成了两个部分：  
- 一部分是等待投递给消费者的消息；
- 一部分是已经投递给消费者，但是还没有收到消费者确认信号的消息。 

如果 RabbitMQ 一直没有收到消费者的确认信号，并且消费此消息的消费者已经断开连接，则 RabbitMQ 会安排该消息重新进入队列，等待投递给下一个消费者，当然也有可能还是原来的那个消费者。

RabbitMQ 不会为未确认的消息设置过期时间，它判断此消息是否需要重新投递给消费者的唯一依据 是消费该消息的消费者连接是否已经断开，这么设计的原因是 RabbitMQ 允许消费者消费一条消息的时间可以很久很久。

RabbitMQ 的 Web 管理平台上可以看到当前队列中： 
- Ready 状态：等待投递给消费者的消息数
- Unacknowledged 状态：已经投递给消费者但是未收到确认信号的消息数


#### 拒绝消息

1. 一次拒绝一条消息：消费端可以调用`channel.basicReject`方法来拒绝消息：`void basicReject(long deliveryTag, boolean requeue) throws IOException;`
    - `deliveryTag`：可以看作消息的编号，它是一个 64 位的长整型值，最大值是9223372036854775807。
    - `requeue`：拒绝后是否重入队列，
      - 设置为 true， RabbitMQ 会重新将这条消息存入队列，以便可以发送给下一个订阅的消费者；
      - 设置为 false，则 RabbitMQ 立即会把消息从队列中移除，而不会把它发送给新的消费者。
2. 批量拒绝消息：消费端可以调用`channel.basicNack`方法来拒绝消息：`void basicNack(long deliveryTag, boolean multiple, boolean requeue) throws IOException;`
    - `multiple`：
      - 设置为 false，则表示拒绝编号为 deliveryTag 的这一条消息，这时候 basicNack 和 basicReject 方法一样；
      - 设置为 true，则表示拒绝 deliveryTag 编号之前所有未被当前消费者确认的消息。
3. 重新发送还未被确认的消息：`channel.basicRecover` 方法用来请求 RabbitMQ 重新发送还未被确认的消息重新进入队列。
    - `Basic.RecoverOk basicRecover() throws IOException;`
    - `Basic.RecoverOk basicRecover(boolean requeue) throws IOException;`
        - 如果 requeue 参数设置为 true（不设置时默认true），则未被确认的消息会被重新加入到队列中，这样对于同一条消息来说，可能会被分配给与之前不同的消费者。
        - 如果 requeue 参数设置为 false，那么同一条消息会被分配给与之前相同的消费者。

注意点：  
将 channel.basicReject 或者 channel.basicNack 中的 requeue 设置为 false，可以启用 “死信队列” 的功能。[死信队列](#死信队列DLX)可以通过检测被拒绝或者未送达的消息来追踪问题。


### 关闭连接

需要关闭连接，释放资源：
- `channel.close();` 显式关闭Channel
- `conn.close();` 关闭Connection时，Channel会自动关闭 

AMQP 协议中的 Connection 和 Channel 采用同样的方式来管理网络失败、内部错误、显式地关闭连接。  
Connection 和 Channel 所具备的生命周期如下：  
- Open：开启状态，代表当前对象可以使用。
- Closing：正在关闭状态。当前对象被显式地通知调用关闭方法（shutdown），这样就产生了一个关闭请求让其内部对象进行相应的操作，并等待这些关闭操作的完成。
- Closed：已经关闭状态。当前对象已经接收到所有的内部对象已完成关闭动作的通知，并且其也关闭了自身。

Connection 和 Channel 最终都是会成为 Closed 的状态，不论是程序正常调用的关闭方法，或者是客户端的异常，再或者是发生了网络异常。

在 Connection 和 Channel 中，与关闭相关的方法有：
- `addShutdownListener(ShutdownListener listener)`
- `removeShutdownListener(ShutdownListener listener)`
    - 当 Connection 或者 Channel 的状态转变为 Closed 的时候会调用 ShutdownListener。
    - 如果将一个 ShutdownListener 注册到一个已经处于 Closed 状态的对象（这里特指Connection和Channel对象）时，会立刻调用ShutdownListener。
- `getCloseReason` 方法可以获取对象关闭的原因；
- `isOpen` 方法检测对象当前是否处于开启状态；
- `close(int closeCode, String closeMessage)` 方法显式地通知当前对象执行关闭操作。

ShutdownListener 的使用参考：
```java
import com.rabbitmq.client.ShutdownSignalException; 
import com.rabbitmq.client.ShutdownListener; 

connection.addShutdownListener(new ShutdownListener() { 
    public void shutdownCompleted(ShutdownSignalException cause) { 
    ... 
    } 
});
```
当触发 ShutdownListener 的时候，就可以获取到 ShutdownSignalException，这个 ShutdownSignalException 包含了关闭的原因，这里原因也可以通过调用前面所提及的 getCloseReason 方法获取。
ShutdownSignalException 提供了多个方法来分析关闭的原因：
- isHardError 方法可以知道是 Connection 的还是 Channel 的错误；
- getReason 方法可以获取 cause 相关的信息
```java
public void shutdownCompleted(ShutdownSignalException cause) { 
    if (cause.isHardError()) { 
        Connection conn = (Connection)cause.getReference(); 
        if (!cause.isInitiatedByApplication()) { 
            Method reason = cause.getReason(); 
            ... 
        } 
        ... 
    } else { 
        Channel ch = (Channel)cause.getReference(); 
        ... 
    } 
}
```

### [RabbitMQ的6种消息模式 + 消息补偿消息幂等](https://www.jianshu.com/p/5aba0843345d)


## RabbitMQ实战

### 消息发布后的流向

mandatory 和 immediate 是 channel.basicPublish 方法中的两个参数，它们都有当消息传递过程中不可达目的地时将消息返回给生产者的功能。

RabbitMQ 提供的备份交换器（Alternate Exchange）可以将未能被交换器路由的消息（没有绑定队列或者没有匹配的绑定）存储起来，而不用返回给客户端。

#### mandatory参数

- 当 mandatory 参数设为 true 时，交换器无法根据自身的类型和路由键找到一个符合条件的队列，那么 RabbitMQ 会调用 Basic.Return 命令将消息返回给生产者。
- 当 mandatory 参数设为 false 时，出现上述情形，则消息直接被丢弃。

那么生产者如何获取到没有被正确路由到合适队列的消息呢？这时候可以通过调用 channel.addReturnListener 来添加 ReturnListener 监听器实现。
```java
channel.basicPublish(EXCHANGE_NAME, "", true, 
                    MessageProperties.PERSISTENT_TEXT_PLAIN, 
                    "mandatory test".getBytes()); 
channel.addReturnListener(new ReturnListener() { 
    public void handleReturn(int replyCode, String replyText, 
                            String exchange, String routingKey, 
                            AMQP.BasicProperties basicProperties, 
                            byte[] body) throws IOException { 
        String message = new String(body); 
        System.out.println("Basic.Return 返回的结果是："+message); 
    } 
});

// 代码的最后输出应该是：Basic.Return 返回的结果是：mandatory test
```

#### ~~immediate参数~~

- 当 immediate 参数设为 true 时，如果交换器在将消息路由到队列时发现队列上并不存在任何消费者，那么这条消息将不会存入队列中。当与路由键匹配的所有队列都没有消费者时，该消息会通过 Basic.Return 返回至生产者。

概括来说，
- mandatory 参数告诉服务器至少将该消息路由到一个队列中，否则将消息返回给生产者。
- immediate 参数告诉服务器，如果该消息关联的队列上有消费者，则立刻投递；如果所有匹配的队列上都没有消费者，则直接将消息返还给生产者，不用将消息存入队列而等待消费者了。

RabbitMQ 3.0 版本开始不再支持 immediate 参数，对此 RabbitMQ 官方解释是：immediate 参数会影响镜像队列的性能，增加了代码复杂性，建议采用 TTL 和 DLX 的方法替代。


#### 备份交换器

Alternate Exchange，简称AE，

生产者在发送消息的时候如果不设置 mandatory 参数，那么消息在未被路由的情况下将会丢失；如果设置了 mandatory 参数，那么需要添加 ReturnListener 的编程逻辑，生产者的代码将变得复杂。
如果既不想复杂化生产者的编程逻辑，又不想消息丢失，那么可以使用备份交换器，这样可以将未被路由的消息存储在 RabbitMQ 中，再在需要的时候去处理这些消息。

可以通过在声明交换器（调用 channel.exchangeDeclare 方法）的时候添加 alternate-exchange 参数来实现，也可以通过策略（Policy）的方式实现。
如果两者同时使用，则前者的优先级更高，会覆盖掉 Policy 的设置。

```java
Map<String, Object> args = new HashMap<String, Object>(); 
args.put("alternate-exchange", "myAe"); 

channel.exchangeDeclare("normalExchange", "direct", true, false, args);
channel.queueDeclare("normalQueue", true, false, false, null); 
channel.queueBind("normalQueue", "normalExchange", "normalKey");

channel.exchangeDeclare("myAe", "fanout", true, false, null);
channel.queueDeclare("unroutedQueue", true, false, false, null);
channel.queueBind("unroutedQueue", "myAe", "");

// myAe设置为normalExchange 的备份交换器
// 通过Policy方式设置：rabbitmqctl set_policy AE "^normalExchange$" '{"alternate-exchange": "myAE"}'
```
- 如果此时发送一条消息到 normalExchange 上，当路由键等于“normalKey”的时候，消息能正确路由到 normalQueue 这个队列中。
- 如果路由键设为其他值，比如“errorKey”，即消息不能被正确地路由到与 normalExchange 绑定的任何队列上，此时就会发送给 myAe，进而发送到 unroutedQueue 这个队列。
  
备份交换器跟普通交换器基本相同，需注意：消息被重新发送到备份交换器时的路由键和从生产者发出的路由键是一样的。
- 场景：如果备份交换器的类型是 direct，并且有一个与其绑定的队列，假设绑定的路由键是 key1，当某条携带路由键为 key2 的消息被转发到这个备份交换器的时候，备份交换器没有匹配到合适的队列，则消息丢失。如果消息携带的路由键为 key1，则可以存储到队列中。

备份交换器的几种特殊情况：
- 如果设置的备份交换器不存在，客户端和 RabbitMQ 服务端都不会有异常出现，此时消息会丢失。
- 如果备份交换器没有绑定任何队列，客户端和 RabbitMQ 服务端都不会有异常出现，此时消息会丢失。
- 如果备份交换器没有任何匹配的队列，客户端和 RabbitMQ 服务端都不会有异常出现，此时消息会丢失。
- 如果备份交换器和 mandatory 参数一起使用，那么 mandatory 参数无效。


### 过期时间TTL

Time to Live，RabbitMQ 可以对消息和队列设置 TTL。

#### 设置消息TTL

有2种方式：
1. 通过队列属性设置，队列中所有消息都有相同的过期时间。
2. 对消息本身进行单独设置，每条消息的 TTL 可以不同。
   
如果两种方法一起使用，则消息的 TTL 以两者之间较小的那个数值为准。消息在队列中的生存时间一旦超过设置的 TTL 值时，就会变成 “死信”（Dead Message），消费者将无法再收到该消息（不是绝对的）。

1. 通过队列属性设置消息 TTL 的方法是在 channel.queueDeclare 方法中加入 **`x-message-ttl`** 参数实现的，单位是毫秒。
```java
Map<String, Object> argss = new HashMap<String, Object>(); 
argss.put("x-message-ttl",6000); 
channel.queueDeclare(queueName, durable, exclusive, autoDelete, argss);

// 通过Policy方式设置：rabbitmqctl set_policy TTL ".*" '{"message-ttl":60000}' --apply-to queues
// 调用HTTP接口设置：$ curl -i -u root:root -H "content-type:application/json"-X PUT -d'{"auto_delete":false,"durable":true,"arguments":{"x-message-ttl": 60000}}' http://localhost:15672/api/queues/{vhost}/{queuename}
```

- 如果不设置 TTL，则表示此消息不会过期；
- 如果将 TTL 设置为 0，则表示除非此时可以直接将消息投递到消费者，否则该消息会被立即丢弃，
    - 这个特性可以部分替代 RabbitMQ 3.0 版本之前的 immediate 参数，之所以部分代替，是因为 immediate 参数在投递失败时会用 Basic.Return 将消息返回（这个功能可以用[死信队列](#死信队列DLX)来实现）。

2. 针对每条消息设置 TTL 的方法是在 channel.basicPublish 方法中加入 **`expiration`** 的属性参数，单位为毫秒。
```java
AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder(); 
builder.deliveryMode(2);//持久化消息 
builder.expiration("60000");//设置 TTL=60000ms 
AMQP.BasicProperties properties = builder.build(); 
channel.basicPublish(exchangeName, routingKey, mandatory, properties, "ttlTestMessage".getBytes());
```
或者
```java
AMQP.BasicProperties properties = new AMQP.BasicProperties(); 
Properties.setDeliveryMode(2); 
properties.setExpiration("60000"); 
channel.basicPublish(exchangeName, routingKey, mandatory, properties, "ttlTestMessage".getBytes());
```
或者 通过HTTP接口设置
```java
// $ curl -i -u root:root -H "content-type:application/json" -X POST -d '{"properties":{"expiration":"60000"},"routing_key":"routingkey", "payload":"my body", "payload_encoding":"string"}' http://localhost:15672/api/exchanges/{vhost}/{exchangename}/publish
```

- 对于第 1 种设置队列 TTL 属性的方法，一旦消息过期，就会从队列中抹去，
- 而在第 2 种方法中，即使消息过期，也不会马上从队列中抹去，因为每条消息是否过期是在即将投递到消费者之前判定的。

为什么这两种方法处理的方式不一样？  
- 因为第 1 种方法里，队列中已过期的消息肯定在队列头部，RabbitMQ 只要定期从队头开始扫描是否有过期的消息即可。
- 而第 2 种方法里，每条消息的过期时间不同，如果要删除所有过期消息势必要扫描整个队列，所以不如等到此消息即将被消费时再判定是否过期，如果过期再进行删除即可。


#### 设置队列TTL

通过 channel.queueDeclare 方法中的 **`x-expires`** 参数可以控制队列被自动删除前处于未使用状态的时间。未使用的意思是队列上没有任何的消费者，队列也没有被重新声明，并且在过期时间段内也未调用过 Basic.Get 命令。

设置队列里的 TTL 可以应用于类似 RPC 方式的回复队列，在 RPC 中，许多队列会被创建出来，但是却是未被使用的。

RabbitMQ 会确保在过期时间到达后将队列删除，但是不保障删除的动作有多及时。在 RabbitMQ 重启后，持久化的队列的过期时间会被重新计算。

用于表示过期时间的 **`x-expires`** 参数以毫秒为单位，并且服从和 x-message-ttl 一样的约束条件，不过不能设置为 0。比如该参数设置为 1000，则表示该队列如果在 1 秒钟之内未使用则会被删除。

创建一个过期时间为 30 分钟的队列：
```java
Map<String, Object> args = new HashMap<String, Object>(); 
args.put("x-expires", 1800000);
channel.queueDeclare("myqueue", false, false, false, args);
```


### 死信队列DLX

Dead-Letter-Exchange，简称DLX，死信交换器，

当消息在一个队列中变成死信（dead message）之后，它能被重新被发送到另一个交换器中，这个交换器就是 DLX，绑定 DLX 的队列就称之为死信队列。

消息变成死信一般是由于以下几种情况：
- 消息被拒绝（Basic.Reject/Basic.Nack），并且设置 requeue 参数为 false；
- 消息过期；
- 队列达到最大长度。

DLX 也是一个正常的交换器，和一般的交换器没有区别，它能在任何的队列上被指定，实际上就是设置某个队列的属性。
当这个队列中存在死信时，RabbitMQ 就会自动地将这个消息重新发布到设置的 DLX 上去，进而被路由到另一个队列，即死信队列。
可以监听这个队列中的消息以进行相应的处理，这个特性与将消息的 TTL 设置为 0 配合使用可以弥补 [immediate 参数](#immediate参数)的功能。

通过在 channel.queueDeclare 方法中设置 **`x-dead-letter-exchange`** 参数来为这个队列添加 DLX：
```java
channel.exchangeDeclare("dlx_exchange", "direct");//创建 DLX: dlx_exchange 
Map<String, Object> args = new HashMap<String, Object>(); 
args.put("x-dead-letter-exchange", " dlx_exchange ");
// 为这个 DLX 指定路由键，如果没有特殊指定，则使用原队列的路由键：
args.put("x-dead-letter-routing-key", "dlx-routing-key");
// 为队列 myqueue 添加 DLX 
channel.queueDeclare("myqueue", false, false, false, args);


// 通过Policy方式设置：rabbitmqctl set_policy DLX ".*" '{"dead-letter-exchange":" dlx_exchange "}' --apply-to queues
```

创建一个队列，为其设置 TTL 和 DLX 等：
```java
channel.exchangeDeclare("exchange.dlx", "direct", true); 
channel.exchangeDeclare("exchange.normal", "fanout", true); 

Map<String, Object> args = new HashMap<String, Object>(); 
args.put("x-message-ttl", 10000); 
args.put("x-dead-letter-exchange", "exchange.dlx"); 
args.put("x-dead-letter-routing-key", "routingkey"); 

channel.queueDeclare("queue.normal", true, false, false, args); 
channel.queueBind("queue.normal", "exchange.normal", ""); 

channel.queueDeclare("queue.dlx", true, false, false, null); 
channel.queueBind("queue.dlx", "exchange.dlx", "routingkey"); 

channel.basicPublish("exchange.normal", "rk", MessageProperties.PERSISTENT_TEXT_PLAIN, "dlx".getBytes());
```
生产者首先发送一条携带路由键为 “rk” 的消息，然后经过交换器 exchange.normal 顺利地存储到队列 queue.normal 中。
由于队列 queue.normal 设置了过期时间为 10s，在这 10s 内没有消费者消费这条消息，那么判定这条消息为过期。
由于设置了 DLX，过期之时，消息被丢给交换器 exchange.dlx 中，这时找到与 exchange.dlx 匹配的队列 queue.dlx，最后消息被存储在 queue.dlx 这个死信队列中。

![rabbitmq-死信队列](../resources/static/images/rabbitmq-死信队列.png)


### 延迟队列

- 存储延迟消息的队列称为延迟队列。
- 延迟消息：当消息被发送以后，不想让消费者立刻拿到消息，而是等待特定时间后，消费者才能拿到这个消息进行消费。

使用场景：
- 在订单系统中，一个用户下单之后通常有 30 分钟的时间进行支付，如果 30 分钟之内没有支付成功，那么这个订单将进行异常处理，这时就可以使用延迟队列来处理这些订单了。
- 用户希望通过手机远程遥控家里的智能设备在指定的时间进行工作。这时候就可以将用户指令发送到延迟队列，当指令设定的时间到了再将指令推送到智能设备。

RabbitMQ 本身没有直接支持延迟队列的功能，但是可以通过 DLX + TTL 来实现延迟队列的功能。

上图 4-4 中，不仅展示的是死信队列的用法，也是延迟队列的用法，对于 queue.dlx 这个死信队列来说，同样可以看作延迟队列。
假设一个应用中需要将每条消息都设置为 10 秒的延迟，生产者通过 exchange.normal 这个交换器将发送的消息存储在 queue.normal 这个队列中。
消费者订阅的不是 queue.normal 这个队列，而是 queue.dlx 这个队列。当消息从 queue.normal 这个队列中过期之后被存入 queue.dlx 这个队列中，消费者就恰巧消费到了延迟 10 秒的这条消息。

在真实应用中，对于延迟队列可以根据延迟时间的长短分为多个等级，一般分为 5 秒、10
秒、30 秒、1 分钟、5 分钟、10 分钟、30 分钟、1 小时这几个维度，当然也可以再细化一下。

下图 4-5，为了简化说明，这里只设置了 5 秒、10 秒、30 秒、1 分钟这四个等级。
根据应用需求的不同，生产者在发送消息的时候通过设置不同的路由键，以此将消息发送到与交换器绑定的不同的队列中。
这里队列分别设置了过期时间为 5 秒、10 秒、30 秒、1 分钟，同时也分别配置了 DLX 和相应的死信队列。
当相应的消息过期时，就会转存到相应的死信队列（即延迟队列）中，这样消费者根据业务自身的情况，分别选择不同延迟等级的延迟队列进行消费。

![rabbitmq-延迟队列](../resources/static/images/rabbitmq-延迟队列.png)


### 优先级队列

高优先级队列的消息会被优先消费。通过设置队列的 **`x-max-priority`** 参数来实现。
```java
Map<String, Object> args = new HashMap<String, Object>(); 
args.put("x-max-priority", 10); 
channel.queueDeclare("queue.priority", true, false, false, args);
```
之后，需要在发送时在消息中设置消息当前的优先级。
```java
AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder(); 
builder.priority(5); 
AMQP.BasicProperties properties = builder.build(); 
channel.basicPublish("exchange_priority","rk_priority",properties,("messages").getBytes());
```
上面的代码中设置消息的优先级为 5。默认最低为 0，最高为队列设置的最大优先级。  
优先级高的消息可以被优先消费，这个也是有前提的：如果在消费者的消费速度大于生产者的速度 且 Broker 中没有消息堆积的情况下，对发送的消息设置优先级也就没有什么实际意义。因为生产者刚发送完一条消息就被消费者消费了，那么就相当于 Broker 中至多只有一条消息，对于单条消息来说优先级是没有什么意义的。


### RPC实现

一般在 RabbitMQ 中进行 RPC 是很简单。客户端发送请求消息，服务端回复响应的消息。
为了接收响应的消息，需要在请求消息中发送一个回调队列（参考下面代码中的 replyTo）。
可以使用默认的队列如下：
```java
String callbackQueueName = channel.queueDeclare().getQueue(); 
BasicProperties props = new BasicProperties.Builder().replyTo(callbackQueueName).build(); 
channel.basicPublish("", "rpc_queue", props, message.getBytes()); 
// then code to read a response message from the callback_queue...
```
- `replyTo`：通常用来设置一个回调队列。
- `correlationId`：用来关联请求（request）和其调用 RPC 之后的回复（response）。

如果像上面的代码中一样，为每个 RPC 请求创建一个回调队列，则是非常低效的。有一个通用的解决方案：可以为每个客户端创建一个单一的回调队列。

这样就产生了一个新的问题，对于回调队列而言，在其接收到一条回复的消息之后，它并不知道这条消息应该和哪一个请求匹配。
这里就用到 correlationId 这个属性了，应该为每一个请求设置一个唯一的 correlationId。之后在回调队列接收到回复的消息时，可以根据这个属性匹配到相应的请求。如果回调队列接收到一条未知correlationId的回复消息，可以简单地将其丢弃。

为什么要将回调队列中的位置消息丢弃而不是仅仅将其看作失败？  
这样可以针对这个失败做一些弥补措施。参考下图 4-7，考虑这样一种情况，RPC 服务器可能在发送给回调队列（amq.gen-LhQz1gv3GhDOv8PIDabOXA）并且在确认接收到请求的消息（rpc_queue 中的消息）之后挂掉了，那么只需重启下 RPC 服务器即可，RPC 服务会重新消费 rpc_queue 队列中的请求，这样就不会出现 RPC 服务端未处理请求的情况。  
这里的回调队列可能会收到重复消息的情况，这需要客户端能够优雅地处理这种情况，并且 RPC 请求也需要保证其本身是幂等的（补充：根据 3.5 节的介绍，消费者消费消息一般是先处理业务逻辑，再使用 Basic.Ack 确认已接收到消息以防止消息不必要地丢失）。  

![rabbitmq-RPC处理流程图](../resources/static/images/rabbitmq-RPC处理流程图.png)

1. 当客户端启动时，创建一个匿名的回调队列（名称由 RabbitMQ 自动创建，上图 4-7 中的回调队列为 amq.gen-LhQz1gv3GhDOv8PIDabOXA）。
2. 客户端为 RPC 请求设置 2 个属性：replyTo 用来告知 RPC 服务端回复请求时的目的队列，即回调队列；correlationId 用来标记一个请求。
3. 请求被发送到 rpc_queue 队列中。
4. RPC 服务端监听 rpc_queue 队列中的请求，当请求到来时，服务端会处理并且把带有结果的消息发送给客户端。接收的队列就是 replyTo 设定的回调队列。
5. 客户端监听回调队列，当有消息时，检查 correlationId 属性，如果与请求匹配，那就是结果了。

示例：RPC客户端通过RPC来调用服务端的方法来获取相应的斐波那契值：  

服务端关键代码：
```java
public class RPCServer { 
    private static final String RPC_QUEUE_NAME = "rpc_queue"; 
    public static void main(String args[]) throws Exception { 
        //省略了创建 Connection 和 Channel 的过程，具体可以参考 1.4.4 节
        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null); 
        channel.basicQos(1); 
        System.out.println(" [x] Awaiting RPC requests"); 
        Consumer consumer = new DefaultConsumer(channel) { 
            @Override 
            public void handleDelivery(String consumerTag, 
                                        Envelope envelope, 
                                        AMQP.BasicProperties properties, 
                                        byte[] body) throws IOException { 
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties 
                                                    .Builder() 
                                                    .correlationId(properties.getCorrelationId()) 
                                                    .build(); 
                String response = ""; 
                try { 
                    String message = new String(body, "UTF-8"); 
                    int n = Integer.parseInt(message); 
                    System.out.println(" [.] fib(" + message + ")"); 
                    response += fib(n); 
                } catch (RuntimeException e) { 
                    System.out.println(" [.] " + e.toString()); 
                } finally { 
                    channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8")); 
                    channel.basicAck(envelope.getDeliveryTag(), false); 
                } 
            } 
        }; 
        channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
    }
    
    private static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }
}
```

RPC客户端关键代码：
```java
public class RPCClient { 
    private Connection connection; 
    private Channel channel; 
    private String requestQueueName = "rpc_queue"; 
    private String replyQueueName; 
    private QueueingConsumer consumer; 
    public RPCClient() throws IOException, TimeoutException { 
        //省略了创建 Connection 和 Channel 的过程，具体可以参考 1.4.4 节
        replyQueueName = channel.queueDeclare().getQueue(); 
        consumer = new QueueingConsumer(channel); 
        channel.basicConsume(replyQueueName, true, consumer); 
    } 
    public String call(String message) throws IOException, 
                                            ShutdownSignalException, ConsumerCancelledException, 
                                            InterruptedException { 
        String response = null; 
        String corrId = UUID.randomUUID().toString(); 
        BasicProperties props = new BasicProperties.Builder() 
                                                    .correlationId(corrId) 
                                                    .replyTo(replyQueueName) 
                                                    .build(); 
        channel.basicPublish("", requestQueueName, props, message.getBytes()); 
        while(true){ 
            QueueingConsumer.Delivery delivery = consumer.nextDelivery(); 
            if(delivery.getProperties().getCorrelationId().equals(corrId)){ 
                response = new String(delivery.getBody()); 
                break; 
            } 
        }
        return response;
    }
    
    public void close() throws Exception{
        connection.close();
    }
    
    public static void main(String args[]) throws Exception{
        RPCClient fibRpc = new RPCClient();
        System.out.println(" [x] Requesting fib(30)");
        String response = fibRpc.call("30");
        System.out.println(" [.] Got '"+response+"'");
        fibRpc.close();
    }
}
```

### 持久化

持久化可以提高 RabbitMQ 的可靠性，以防在异常情况（重启、关闭、宕机等）下的数据丢失。

RabbitMQ 的持久化分为三个部分：交换器的持久化、队列的持久化、消息的持久化。

1. 交换器的持久化，是通过在声明交换器时将 durable 参数置为 true 实现的，详细可以参考[声明交换机exchangeDeclare方法详解](#声明交换机exchangeDeclare方法详解)。
   - 如果交换器不设置持久化，那么在 RabbitMQ 服务重启之后，相关的交换器元数据会丢失，不过消息不会丢失，只是不能将消息发送到这个交换器中了。对一个长期使用的交换器来说，建议将其置为持久化的。
2. 队列的持久化，是通过在声明队列时将 durable 参数置为 true 实现的，详细内容可以参考[声明队列queueDeclare方法详解](#声明队列queueDeclare方法详解)。
   - 如果队列不设置持久化，那么在 RabbitMQ 服务重启之后，相关队列的元数据会丢失，此时数据也会丢失。当队列都没有了，消息又能存在哪里呢？
3. 消息的持久化，通过将消息的投递模式（BasicProperties 中的 deliveryMode 属性）设置为 2 即可实现消息的持久化。
   - 队列的持久化能保证其本身的元数据不会因异常情况而丢失，但是并不能保证内部所存储的消息不会丢失。要确保消息不会丢失，需要将其设置为持久化。
   - 前面示例中多次提及的 MessageProperties.PERSISTENT_TEXT_PLAIN 实际上是封装了这个属性：
        ```java
        public static final BasicProperties PERSISTENT_TEXT_PLAIN = new BasicProperties("text/plain", 
                                                                                         null, 
                                                                                         null, 
                                                                                         2, //deliveryMode 
                                                                                         0, null, null, null, 
                                                                                         null, null, null, null, 
                                                                                         null, null);
        ```
        更多发送消息细节参考：[发送消息](#发送消息)

设置了队列 和 消息 的持久化，当 RabbitMQ 服务重启之后，消息依旧存在。
- 单单只设置 队列持久化，重启之后消息会丢失；
- 单单只设置 消息的持久化，重启之后队列消失，继而消息也丢失。单单设置消息持久化而不设置队列的持久化显得毫无意义。

注意点：  
可以将所有的消息都设置为持久化，但是这样会严重影响 RabbitMQ 的性能（随机）。写入磁盘的速度比写入内存的速度慢得不只一点点。
对于可靠性不是那么高的消息可以不采用持久化处理以提高整体的吞吐量。在选择是否要将消息持久化时，需要在可靠性和吐吞量之间做一个权衡。

将交换器、队列、消息都设置了持久化之后就能百分之百保证数据不丢失了吗？  
答案是否定的。  
- 首先从消费者来说，如果在订阅消费队列时将 autoAck 参数设置为 true，那么当消费者接收到相关消息之后，还没来得及处理就宕机了，这样也算数据丢失。  
  - 这种情况很好解决，将 autoAck 参数设置为 false，并进行手动确认，详细可以参考[确认消息](#确认消息)。
- 其次，在持久化的消息正确存入 RabbitMQ 之后，还需要有一段时间（虽然很短，但是不可忽视）才能存入磁盘之中。RabbitMQ 并不会为每条消息都进行同步存盘（调用内核的 fsync 方法）的处理，可能仅仅保存到操作系统缓存之中而不是物理磁盘之中。如果在这段时间内 RabbitMQ 服务节点发生了宕机、重启等异常情况，消息保存还没来得及落盘，那么这些消息将会丢失。  
  - 这个问题怎么解决呢？这里可以引入 RabbitMQ 的镜像队列机制（详细参考 9.4 节），相当于配置了副本，如果主节点（master）在此特殊时间内挂掉，可以自动切换到从节点（slave），这样有效地保证了高可用性，除非整个集群都挂掉。  
  - 虽然这样也不能完全保证 RabbitMQ 消息不丢失，但是配置了镜像队列要比没有配置镜像队列的可靠性要高很多，在实际生产环境中的关键业务队列一般都会设置镜像队列。  
  - 还可以在发送端引入事务机制或者发送方确认机制来保证消息已经正确地发送并存储至 RabbitMQ 中，前提还要保证在调用 channel.basicPublish 方法的时候交换器能够将消息正确路由到相应的队列之中。详细可以参考[生产者确认](#生产者确认)。
    

### 生产者确认

生产者确认的作用是 消息被生产者发送出去后，确认正确的到达服务器了。

默认情况下，如果不进行特殊配置，发送消息的操作是不会返回任何信息给生产者的，也就是生产者是不知道消息有没有正确地到达服务器。
如果在消息到达服务器之前已经丢失，持久化操作也解决不了这个问题，因为消息根本没有到达服务器，何谈持久化？

RabbitMQ 针对这个问题，提供了两种解决方式：
- 通过事务机制实现；
- 通过发送方确认（publisher confirm）机制实现。

#### 事务机制

RabbitMQ 客户端中与事务机制相关的方法有三个： 
- `channel.txSelect`：用于将当前的信道设置成事务模式
- `channel.txCommit`：用于提交事务
- `channel.txRollback`：用于事务回滚

在通过 channel.txSelect 方法开启事务之后，就可以发布消息给 RabbitMQ 了，  
如果事务提交成功，则消息一定到达了 RabbitMQ 中，  
如果在事务提交执行之前由于 RabbitMQ 异常崩溃或者其他原因抛出异常，这个时候可以将其捕获，进而通过执行 channel.txRollback 方法来实现事务回滚。  
注意这里的 RabbitMQ 中的事务机制与大多数数据库中的事务概念并不相同，需要注意区分。  
```java
channel.txSelect(); 
channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY, 
                    MessageProperties.PERSISTENT_TEXT_PLAIN, 
                    "transaction messages".getBytes()); 
channel.txCommit();
```

可以发现开启事务机制与不开启相比多了四个步骤：
- 客户端发送 Tx.Select，将信道置为事务模式；
- Broker 回复 Tx.Select-Ok，确认已将信道置为事务模式；
- 在发送完消息之后，客户端发送 Tx.Commit 提交事务；
- Broker 回复 Tx.Commit-Ok，确认事务提交。

异常情况下的事务机制运转流程：
```java
try { 
    channel.txSelect(); 
    channel.basicPublish(exchange, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes()); 
    int result = 1 / 0; // throws java.lang.ArithmeticException
    channel.txCommit(); 
} catch (Exception e) { 
    e.printStackTrace(); 
    channel.txRollback(); 
}
```

如果要发送多条消息，则将 channel.basicPublish 和 channel.txCommit 等方法包裹进循环内即可：
```java
channel.txSelect(); 
for(int i=0;i<LOOP_TIMES;i++) { 
    try { 
        channel.basicPublish("exchange", "routingKey", null, ("messages" + i).getBytes()); 
        channel.txCommit(); 
    } catch (IOException e) { 
        e.printStackTrace(); 
        channel.txRollback(); 
    } 
}
```

事务确实能够解决消息发送方和 RabbitMQ 之间消息确认的问题，只有消息成功被 RabbitMQ 接收，事务才能提交成功，否则便可在捕获异常之后进行事务回滚，与此同时可以进行消息重发。  
但是使用事务机制会 “吸干” RabbitMQ 的性能，那么有没有更好的方法既能保证消息发送方确认消息已经正确送达，又能基本上不带来性能上的损失呢？
- 从 AMQP 协议层面来看并没有更好的办法，但是 RabbitMQ 提供了一个改进方案，即发送方确认机制，详情请看下一节的[发送方确认机制](#发送方确认机制)。


#### 发送方确认机制

AMQP 协议层面提供的事务机制会严重降低 RabbitMQ 的消息吞吐量，这里就引入了一种轻量级的方式：发送方确认（publisher confirm）机制。

生产者将信道设置成 confirm（确认）模式，一旦信道进入 confirm 模式，所有在该信道上面发布的消息都会被指派一个唯一的 ID（从 1 开始），一旦消息被投递到所有匹配的队列之后，RabbitMQ 就会发送一个确认（Basic.Ack）给生产者（包含消息的唯一 ID），这就使得生产者知晓消息已经正确到达了目的地了。如果消息和队列是可持久化的，那么确认消息会在消息写入磁盘之后发出。
RabbitMQ 回传给生产者的确认消息中的 deliveryTag 包含了确认消息的序号，此外 RabbitMQ 也可以设置 channel.basicAck 方法中的 multiple 参数，表示到这个序号之前的所有消息都已经得到了处理，可以参考下图 4-10。注意辨别这里的确认和消费时候的确认之间的异同。

![rabbitmq-发送方确认机制](../resources/static/images/rabbitmq-发送方确认机制.png)

事务机制在一条消息发送之后会使发送端阻塞，以等待 RabbitMQ 的回应，之后才能继续发送下一条消息。  
相比之下，发送方确认机制最大的好处在于它是异步的，一旦发布一条消息，生产者应用程序就可以在等信道返回确认的同时继续发送下一条消息，当消息最终得到确认之后，生产者应用程序便可以通过回调方法来处理该确认消息，如果 RabbitMQ 因为自身内部错误导致消息丢失，就会发送一条 nack（Basic.Nack）命令，生产者应用程序同样可以在回调方法中处理该 nack 命令。

生产者通过调用 **`channel.confirmSelect`** 方法（即 Confirm.Select 命令）将信道设置为 confirm 模式，之后 RabbitMQ 会返回 Confirm.Select-Ok 命令表示同意生产者将当前信道设置为 confirm 模式。
所有被发送的后续消息都被 ack 或者 nack 一次，不会出现一条消息既被 ack 又被 nack 的情况，并且 RabbitMQ 也并没有对消息被 confirm 的快慢做任何保证。

```java
try { 
    channel.confirmSelect(); //将信道置为 publisher confirm 模式
    // 之后正常发送消息
    channel.basicPublish("exchange", "routingKey", null, "publisher confirm test".getBytes()); 
    if (!channel.waitForConfirms()) {
        System.out.println("send message failed");
        // do something else.... 
    }
} catch (InterruptedException e) {
    e.printStackTrace();
}
```

如果发送多条消息，只需要将 channel.basicPublish 和 channel.waitForConfirms 方法包裹在循环里面即可，可以参考事务机制，不过不需要把 channel.confirmSelect 方法包裹在循环内部。
```java
channel.confirmSelect(); //将信道置为 publisher confirm 模式
for(int i=0; i<LOOP_TIMES; i++) { 
    try {
        // 之后正常发送消息
        channel.basicPublish("exchange", "routingKey", null, "publisher confirm test".getBytes()); 
        if (!channel.waitForConfirms()) { // 这是一种串行同步等待的方式，
            System.out.println("send message failed");
            // do something else.... 
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```

对于 channel.waitForConfirms 而言，在 RabbitMQ 客户端中它有 4 个同类的方法：
1. `boolean waitForConfirms() throws InterruptedException;`
2. `boolean waitForConfirms(long timeout) throws InterruptedException, TimeoutException;`
3. `void waitForConfirmsOrDie() throws IOException, InterruptedException;`
4. `void waitForConfirmsOrDie(long timeout) throws IOException, InterruptedException, TimeoutException;`

- 如果信道没有开启 publisher confirm 模式，则调用任何 waitForConfirms 方法都会报出 java.lang.IllegalStateException。
- 对于没有参数的 waitForConfirms 方法来说，其返回的条件是客户端收到了相应的 Basic.Ack/.Nack 或者被中断。参数 timeout 表示超时时间，一旦等待 RabbitMQ 回应超时就会抛出 java.util.concurrent.TimeoutException 的异常。
- 两个 waitForConfirmsOrDie 方法在接收到 RabbitMQ 返回的 Basic.Nack 之后会抛出 java.io.IOException。业务代码可以根据自身的特性灵活地运用这四种方法来保障消息的可靠发送。

前面提到过 RabbitMQ 引入了 publisher confirm 机制来弥补事务机制的缺陷，提高了整体的吞吐量，那么我们来对比下两者之间的 QPS，测试代码可以参考上面的示例代码。
测试环境：客户端和 Broker 机器配置：CPU 为 24 核、主频为 2600Hz、内存为 64GB、硬盘为 1TB。客户端发送的消息体大小为 10B，单线程发送，并且消息都进行持久化处理。

![rabbitmq-生产者确认-两种确认机制性能对比](../resources/static/images/rabbitmq-生产者确认-两种确认机制性能对比.png)

测试结果显示 发送方确认机制 比 事务机制的QPS没有提高多少。
- 原因：上面的示例代码中 publisher confirm 模式是每发送一条消息后就调用 channel.waitForConfirms 方法，之后等待服务端的确认，这实际上是一种串行同步等待的方式。
  - 和事务机制一样，发送消息之后等待服务端确认，之后再发送消息。
  - 两者的存储确认原理相同，尤其对于持久化的消息来说，两者都需要等待消息确认落盘之后才会返回（调用 Linux 内核的 fsync 方法）。
  - 在同步等待的方式下，publisher confirm 机制发送一条消息需要通信交互的命令是 2 条：Basic.Publish 和 Basic.Ack；事务机制是 3 条：Basic.Publish、Tx.Commmit/.Commit-Ok（或者 Tx.Rollback/.Rollback-Ok），事务机制多了一个命令帧报文的交互，所以 QPS 会略微下降。

注意点：
1. **事务机制和 publisher confirm 机制两者是互斥的，不能共存**。
   - 如果企图将已开启事务模式的信道再设置为 publisher confirm 模式，RabbitMQ 会报错：{amqp_error, precondition_failed, "cannot switch from tx to confirm mode", 'confirm.select'}；
   - 或者如果企图将已开启 publisher confirm 模式的信道再设置为事务模式，RabbitMQ 也会报错：{amqp_error, precondition_failed, "cannot switch from confirm to tx mode", 'tx.select' }。
2. **事务机制和 publisher confirm 机制确保的是消息能够正确地发送至 RabbitMQ，这里的 “发送至 RabbitMQ” 的含义是指消息被正确地发往至 RabbitMQ 的交换器**，
   - 如果此交换器没有匹配的队列，那么消息也会丢失。所以在使用这两种机制的时候要确保所涉及的交换器能够有匹配的队列。
   - 更进一步地讲，发送方要配合 mandatory 参数或者备份交换器一起使用来提高消息传输的可靠性。
   
publisher confirm 的优势 在于并不一定需要同步确认。这里我们改进了一下使用方式，总结有如下两种：
- **批量 confirm 方法**：每发送一批消息后，调用 channel.waitForConfirms 方法，等待服务器的确认返回。
- **异步 confirm 方法**：提供一个回调方法，服务端确认了一条或者多条消息后客户端会回调这个方法进行处理。

在批量 confirm 方法中，客户端程序需要定期或者定量（达到多少条），亦或者两者结合起来调用 channel.waitForConfirms 来等待 RabbitMQ 的确认返回。
相比于前面示例中的普通 confirm 方法，批量极大地提升了 confirm 的效率，但是问题在于出现返回 Basic.Nack 或者超时情况时，客户端需要将这一批次的消息全部重发，这会带来明显的重复消息数量，
并且当消息经常丢失时，批量 confirm 的性能应该是不升反降的。
```java
try { 
    channel.confirmSelect(); 
    int MsgCount = 0; 
    while (true) { 
        channel.basicPublish("exchange", "routingKey", null, "batch confirm test".getBytes()); 
        // 将发送出去的消息存入缓存中，缓存可以是一个 ArrayList 或者 BlockingQueue 之类的
        if (++MsgCount >= BATCH_COUNT) { 
            MsgCount = 0; 
            try { 
                if (channel.waitForConfirms()) { 
                //将缓存中的消息清空
                } 
                //将缓存中的消息重新发送
                
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
                //将缓存中的消息重新发送
            } 
        } 
    } 
} catch (IOException e) { 
    e.printStackTrace(); 
}
```


异步 confirm 方法的编程实现最为复杂。在客户端 Channel 接口中提供的 addConfirmListener 方法可以添加 ConfirmListener 这个回调接口，这个 ConfirmListener 接口包含两个方法：  
handleAck 和 handleNack，分别用来处理 RabbitMQ 回传的 Basic.Ack 和 Basic.Nack。在这两个方法中都包含有一个参数 deliveryTag（在 publisher confirm 模式下用来标记消息的唯一有序序号）。  
我们需要为每一个信道维护一个 “unconfirm” 的消息序号集合，每发送一条消息，集合中的元素加 1。每当调用 ConfirmListener 中的 handleAck 方法时，“unconfirm” 集合中删掉相应的一条（multiple 设置为 false）或者多条（multiple 设置为 true）记录。  
从程序运行效率上来看，这个“unconfirm”集合最好采用有序集合 SortedSet 的存储结构。事实上，Java 客户端 SDK 中的 waitForConfirms 方法也是通过 SortedSet 维护消息序号的。  
```java
channel.confirmSelect(); 
channel.addConfirmListener(new ConfirmListener() { 
    public void handleAck(long deliveryTag, boolean multiple) throws IOException { 
        System.out.println("Nack, SeqNo: " + deliveryTag + ", multiple: " + multiple); 
        if (multiple) { 
            confirmSet.headSet(deliveryTag - 1).clear(); 
        } else { 
            confirmSet.remove(deliveryTag); 
        } 
    } 
    public void handleNack(long deliveryTag, boolean multiple) throws IOException { 
        if (multiple) { 
            confirmSet.headSet(deliveryTag - 1).clear(); 
        } else { 
            confirmSet.remove(deliveryTag); 
        } 
        //注意这里需要添加处理消息重发的场景
        
    } 
}); 

//下面是演示一直发送消息的场景
while (true) { 
    long nextSeqNo = channel.getNextPublishSeqNo(); 
    channel.basicPublish(ConfirmConfig.exchangeName, ConfirmConfig.routingKey, 
                        MessageProperties.PERSISTENT_TEXT_PLAIN, 
                        ConfirmConfig.msg_10B.getBytes()); 
    confirmSet.add(nextSeqNo); 
}
```

最后我们将事务、普通 confirm、批量 confirm 和异步 confirm 这 4 种方式放到一起来比较一下彼此的 QPS。测试环境和数据和上图 4-12 中的测试相同，具体测试对比如下图 4-13 所示。

![rabbitmq-发送方确认机制-四种确认方式性能对比](../resources/static/images/rabbitmq-发送方确认机制-四种确认方式性能对比.png)

可以看到：
- 批量 confirm 和异步 confirm 这两种方式所呈现的性能要比其余两种好得多。
- 事务机制和普通 confirm 的方式吐吞量很低，但是编程方式简单，不需要在客户端维护状态（这里指的是维护 deliveryTag 及缓存未确认的消息）。
- 批量 confirm 方式的问题在于遇到 RabbitMQ 服务端返回 Basic.Nack 需要重发批量消息而导致的性能降低。
- 异步 confirm 方式编程模型最为复杂，而且和批量 confirm 方式一样需要在客户端维护状态。

在实际生产环境中采用何种方式，根据实际情况考虑，不过**强烈建议使用异步 confirm 的方式**。


### 消费端处理要点

#### 消息分发

当 RabbitMQ 队列拥有多个消费者时，队列收到的消息将以轮询（round-robin）的分发方式发送给消费者。每条消息只会发送给订阅列表里的一个消费者。
这种方式非常适合扩展，而且它是专门为并发程序设计的。如果现在负载加重，那么只需要创建更多的消费者来消费处理消息即可。

很多时候轮询的分发机制也不是那么优雅。默认情况下，如果有 n 个消费者，那么 RabbitMQ 会将第 m 条消息分发给第 m%n（取余的方式）个消费者，RabbitMQ 不管消费者是否消费并已经确认（Basic.Ack）了消息。
试想一下，如果某些消费者任务繁重，来不及消费那么多的消息，而某些其他消费者由于某些原因（比如业务逻辑简单、机器性能卓越等）很快地处理完了所分配到的消息，进而进程空闲，这样就会造成整体应用吞吐量的下降。

那么该如何处理这种情况呢？  
可以使用 **`channel.basicQos(int prefetchCount)`** 这个方法来 **限制信道上的消费者所能保持的最大未确认消息的数量**。

举例说明，在订阅消费队列之前，消费端程序调用了 channel.basicQos(5)，之后订阅了某个队列进行消费。RabbitMQ 会保存一个消费者的列表，每发送一条消息都会为对应的消费者计数，如果达到了所设定的上限，那么 RabbitMQ 就不会向这个消费者再发送任何消息。
直到消费者确认了某条消息之后，RabbitMQ 将相应的计数减1，之后消费者可以继续接收消息，直到再次到达计数上限。这种机制可以类比于 TCP/IP 中的 “滑动窗口”。

注意点：  
1. Basic.Qos 的使用对于拉模式的消费方式无效。
2. channel.basicQos 有三种类型的重载方法：
    - `void basicQos(int prefetchCount) throws IOException;`
    - `void basicQos(int prefetchCount, boolean global) throws IOException;`
    - `void basicQos(int prefetchSize, int prefetchCount, boolean global) throws IOException;`

前面介绍的都只用到了 prefetchCount 这个参数，当 prefetchCount 设置为 0 则表示没有上限。  
还有 prefetchSize 这个参数表示消费者所能接收未确认消息的总体大小的上限，单位为 B，设置为 0 则表示没有上限。  

对于一个信道来说，它可以同时消费多个队列，当设置了 prefetchCount 大于 0 时，这个信道需要和各个队列协调以确保发送的消息都没有超过所限定的 prefetchCount 的值，这样会使 RabbitMQ 的性能降低，尤其是这些队列分散在集群中的多个 Broker 节点之中。  

RabbitMQ 为了提升相关的性能，在 AMQP 0-9-1 协议之上重新定义了 global 这个参数，对比如下表所示：  

global参数 | AMQP 0-9-1 | RabbitMQ
---|---|---
false | 信道上所有的消费者都需要遵从prefetchCount的限定值 | 信道上新的消费者需要遵从 prefetchCount 的限定值
true | 当前通信链路（Connection）上所有的消费者都需要遵从 prefetchCount 的限定值 | 信道上所有的消费者都需要遵从 prefetchCount 的限定值

前面章节中的 channel.basicQos 方法的示例都是针对单个消费者的，而对于同一个信道上的多个消费者而言，如果设置了 prefetchCount 的值，那么都会生效。
```java
Channel channel = ...; 
Consumer consumer1 = ...; 
Consumer consumer2 = ...; 
channel.basicQos(10); // Per consumer limit 
channel.basicConsume("my-queue1", false, consumer1); 
channel.basicConsume("my-queue2", false, consumer2);
// 两个消费者，各自的能接收到的未确认消息的上限都为 10
```

如果在订阅消息之前，既设置了 global 为 true 的限制，又设置了 global 为 false 的限制，那么哪个会生效呢？  
RabbitMQ 会确保两者都会生效。  
举例说明，当前有两个队列 queue1 和 queue2：queue1 有 10 条消息，分别为 1 到 10；queue2 也有 10 条消息，分别为 11 到 20。有两个消费者分别消费这两个队列。
```java
Channel channel = ...; 
Consumer consumer1 = ...; 
Consumer consumer2 = ...; 
channel.basicQos(3, false); // Per consumer limit 
channel.basicQos(5, true); // Per channel limit 
channel.basicConsume("queue1", false, consumer1); 
channel.basicConsume("queue2", false, consumer2);
```
那么
- 这里每个消费者最多只能收到 3 个未确认的消息，
- 两个消费者能收到的未确认的消息个数之和的上限为 5。
- 在未确认消息的情况下，如果 consumer1 接收到了消息 1、2、3，那么consumer2至多只能收到11、12。
- 如果像这样同时使用两种global的模式，则会增加RabbitMQ 的负载，因为 RabbitMQ 需要更多的资源来协调完成这些限制。
- 如无特殊需要，最好只使用 global 为 false 的设置，这也是默认的设置。


#### 消息顺序性

消息的顺序性是指消费者消费到的消息和发送者发布的消息的顺序是一致的。

目前很多资料显示 RabbitMQ 的消息能够保障顺序性，这是不正确的，或者说这个观点有很大的局限性。  
在不使用任何 RabbitMQ 的高级特性，也没有消息丢失、网络故障之类异常的情况发生，并且只有一个消费者的情况下，最好也只有一个生产者的情况下可以保证消息的顺序性。  
如果有多个生产者同时发送消息，无法确定消息到达 Broker 的前后顺序，也就无法验证消息的顺序性。  

那么哪些情况下 RabbitMQ 的消息顺序性会被打破呢？下面介绍几种常见的情形：
- 如果生产者使用了事务机制，在发送消息之后遇到异常进行了事务回滚，那么需要重新补偿发送这条消息，如果补偿发送是在另一个线程实现的，那么消息在生产者这个源头就出现了错序。
- 同样，如果启用 publisher confirm 时，在发生超时、中断，又或者是收到 RabbitMQ 的 Basic.Nack 命令时，那么同样需要补偿发送，结果与事务机制一样会错序。
  或者这种说法有些牵强，我们可以固执地认为消息的顺序性保障是从存入队列之后开始的，而不是在发送的时候开始的。

- 如果生产者发送的消息设置了不同的超时时间，并且也设置了死信队列，整体上来说相当于一个延迟队列，那么消费者在消费这个延迟队列的时候，消息的顺序必然不会和生产者发送消息的顺序一致。

- 如果消息设置了优先级，那么消费者消费到的消息也必然不是顺序性的。

- 如果一个队列按照前后顺序分有 msg1、msg2、msg3、msg4 这 4 个消息，同时有 ConsumerA 和 ConsumerB 这两个消费者同时订阅了这个队列。队列中的消息轮询分发到各个消费者之中，   
  ConsumerA 中的消息为 msg1 和 msg3，ConsumerB 中的消息为 msg2、msg4。   
  ConsumerA 收到消息 msg1 之后并不想处理而调用了 Basic.Nack/.Reject 将消息拒绝，与此同时将 requeue 设置为 true，这样这条消息就可以重新存入队列中。   
  消息 msg1 之后被发送到了 ConsumerB 中，此时 ConsumerB 已经消费了 msg2、msg4，之后再消费 msg1，这样消息顺序性也就错乱了。   
  或者消息 msg1 又重新发往 ConsumerA 中，此时 ConsumerA 已经消费了 msg3，那么再消费 msg1，消息顺序性也无法得到保障。  
- 同样可以用在 Basic.Recover 这个 AMQP 命令中。

包括但不仅限于以上几种情形会使 RabbitMQ 消息错序。如果要保证消息的顺序性，需要业务方使用 RabbitMQ 之后做进一步的处理，比如在消息体内添加全局有序标识（类似 Sequence ID）来实现。

#### 弃用QueueingConsumer

在前面的章节中所介绍的订阅消费的方式都是通过继承 DefaultConsumer 类来实现的。在 1.4.4 节提及了 QueueingConsumer 这个类，并且建议不要使用这个类来实现订阅消费。
QueueingConsumer 在 RabbitMQ 客户端 3.x 版本中用得如火如荼，但是在 4.x 版本开始就被标记为@Deprecated，想必这个类中有些无法弥补的缺陷。

不妨先看一下 QueueingConsumer 的用法，示例代码如下：
```java
QueueingConsumer consumer = new QueueingConsumer(channel);
//channel.basicQos(64);//使用 QueueingConsumer 的时候一定要添加！避免内存溢出
channel.basicConsume(QUEUE_NAME, false, "consumer_zzh",consumer);

while (true) {
    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
    String message = new String(delivery.getBody());
    System.out.println(" [X] Received '" + message + "'");
    channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
}
```
乍一看也没什么问题，而且实际生产环境中如果不是太“傲娇”地使用也不会造成什么大问题。QueueingConsumer 本身有几个大缺陷，需要在使用时特别注意。
1. 首当其冲的就是内存溢出的问题，如果由于某些原因，队列之中堆积了比较多的消息，就可能导致消费者客户端内存溢出假死，于是发生恶性循环，队列消息不断堆积而得不到消化。
    - 采用上边代码进行测试，首先向一个队列发送 200 多 MB 的消息，然后进行消费。在客户端调用 channel.basicConsume 方法订阅队列的时候，RabbitMQ 会持续地将消息发往 QueueingConsumer 中，QueueingConsumer 内部使用 LinkedBlockingQueue 来缓存这些消息。  
      通过 JVisualVM 可以看到堆内存一直在增加，这里只测试了发送 200MB 左右的消息，如果发送更多的消息，那么这个堆内存会变得更大，直到出现 java.lang.OutOfMemoryError 的报错。
    - 这个内存溢出的问题可以使用 Basic.Qos 来得到有效的解决，Basic.Qos 可以限制某个消费者所保持未确认消息的数量，也就是间接地限制了 QueueingConsumer 中的 LinkedBlockingQueue 的大小。注意一定要在调用 Basic.Consume 之前调用 Basic.Qos 才能生效。
2. QueueingConsumer 还包含（但不仅限于）以下一些缺陷：
    - QueueingConsumer 会拖累同一个 Connection 下的所有信道，使其性能降低；
    - 同步递归调用 QueueingConsumer 会产生死锁；
    - RabbitMQ 的自动连接恢复机制（automatic connection recovery）不支持 Queueing Consumer 的这种形式；
    - QueueingConsumer 不是事件驱动的。

为了避免不必要的麻烦，**建议在消费的时候尽量使用继承 DefaultConsumer 的方式**，具体使用方式可以参考代码[推模式](#推模式)。


### 消息传输保障

消息可靠传输一般是业务系统接入消息中间件时首要考虑的问题，一般消息中间件的消息传输保障分为三个层级。
- At most once：最多一次。消息可能会丢失，但绝不会重复传输。
- At least once：最少一次。消息绝不会丢失，但可能会重复传输。
- Exactly once：恰好一次。每条消息肯定会被传输一次且仅传输一次。

RabbitMQ 支持其中的 “最多一次” 和 “最少一次”。

“最少一次” 投递实现需要考虑以下这个几个方面的内容：
1. 消息生产者需要开启事务机制或者 publisher confirm 机制，以确保消息可以可靠地传输到 RabbitMQ 中。
2. 消息生产者需要配合使用 mandatory 参数或者备份交换器来确保消息能够从交换器路由到队列中，进而能够保存下来而不会被丢弃。
3. 消息和队列都需要进行持久化处理，以确保 RabbitMQ 服务器在遇到异常情况时不会造成消息丢失。
4. 消费者在消费消息的同时需要将 autoAck 设置为 false，然后通过手动确认的方式去确认已经正确消费的消息，以避免在消费端引起不必要的消息丢失。

“最多一次” 的方式就无须考虑以上那些方面，生产者随意发送，消费者随意消费，不过这样很难确保消息不会丢失。

“恰好一次” 是 RabbitMQ 目前无法保障的。  
- 考虑这样一种情况，消费者在消费完一条消息之后向 RabbitMQ 发送确认 Basic.Ack 命令，此时由于网络断开或者其他原因造成 RabbitMQ 并没有收到这个确认命令，那么 RabbitMQ 不会将此条消息标记删除。在重新建立连接之后，消费者还是会消费到这一条消息，这就造成了重复消费。
- 再考虑一种情况，生产者在使用 publisher confirm 机制的时候，发送完一条消息等待 RabbitMQ 返回确认通知，此时网络断开，生产者捕获到异常情况，为了确保消息可靠性选择重新发送，这样 RabbitMQ 中就有两条同样的消息，在消费的时候，消费者就会重复消费。

那么 RabbitMQ 有没有去重的机制来保证“恰好一次”呢？  
答案是并没有，  
不仅是 RabbitMQ，目前大多数主流的消息中间件都没有消息去重机制，也不保障“恰好一次”。  
去重处理一般是在业务客户端实现，比如引入 GUID（Globally Unique Identifier）的概念。针对 GUID，如果从客户端的角度去重，那么需要引入集中式缓存，必然会增加依赖复杂度，另外缓存的大小也难以界定。  
建议在实际生产环境中，业务方根据自身的业务特性进行去重，比如业务消息本身具备幂等性，或者借助 Redis 等其他产品进行去重处理。


总结提升数据可靠性有以下一些途径：
- 设置 mandatory 参数或者备份交换器~~（immediate 参数已被淘汰）~~；
- 设置 publisher confirm 机制~~或者事务机制~~；
- 设置交换器、队列和消息都为持久化；
- 设置消费端对应的 autoAck 参数为 false 并在消费完消息之后再进行消息确认。



## RabbitMQ最佳实践

[RabbitMQ的6种消息模式 + 消息补偿消息幂等](https://www.jianshu.com/p/5aba0843345d)

1. 声明交换机
   
2. 声明队列

3. 绑定交换机和队列


总结提升数据可靠性有以下一些途径：
- 设置 mandatory 参数或者备份交换器~~（immediate 参数已被淘汰）~~；
- 设置 publisher confirm 机制~~或者事务机制~~；
- 设置交换器、队列和消息都为持久化；
- 设置消费端对应的 autoAck 参数为 false 并在消费完消息之后再进行消息确认。



# RabbitMQ原理

## RabbitMQ源码分析





## RabbitMQ模块原理



## 疑问

### 为什么不直接将消息路由到队列，要一个交换机来转发呢？

AMQP 协议中的核心思想就是生产者和消费者的解耦，生产者从不直接将消息发送给队列。生产者通常不知道是否一个消息会被发送到队列中，只是将消息发送到一个交换机。先由 Exchange 来接收，然后 Exchange 按照特定的策略转发到 Queue 进行存储。Exchange 就类似于一个交换机，将各个消息分发到相应的队列中。

在实际应用中我们只需要定义好 Exchange 的路由策略，而生产者则不需要关心消息会发送到哪个 Queue 或被哪些 Consumer 消费。在这种模式下**生产者只面向 Exchange 发布消息，消费者只面向 Queue 消费消息**，Exchange 定义了消息路由到 Queue 的规则，将各个层面的消息传递隔离开，使每一层只需要关心自己面向的下一层，降低了整体的耦合度。


### 可以直接使用Connection就能完成信道的工作，为什么还要引入信道呢？

一个场景中，一个应用程序中有很多个线程需要从 RabbitMQ 中消费消息，或者生产消息，那么必然需要建立很多个 Connection，也就是许多个 TCP 连接。

然而对于操作系统而言，建立和销毁 TCP 连接是非常昂贵的开销，如果遇到使用高峰，性能瓶颈也随之显现。

RabbitMQ 采用类似 NIO（Non-blocking I/O）的做法，选择 TCP 连接复用，不仅可以减少性能开销，同时也便于管理。


每个线程把持一个信道，所以信道复用了 Connection 的 TCP 连接。
同时 RabbitMQ 可以确保每个线程的私密性，就像拥有独立的连接一样。当每个信道的流量不是很大时，复用单一的 Connection 可以在产生性能瓶颈的情况下有效地节省 TCP 连接资源。但是当信道本身的流量很大时，这时候多个信道复用一个 Connection 就会产生性能瓶颈，进而使整体的流量被限制了。
此时就需要开辟多个 Connection，将这些信道均摊到这些 Connection 中，至于这些相关的调优策略需要根据业务自身的实际情况进行调节。

# RabbitMQ优化

## RabbitMQ参数优化


并发数 = CPU核数 * （x+y）/x，其中x是线程执行时间，y是阻塞等待时间



## RabbitMQ二次开发

