> [Netty实战.pdf](../resources/static/doc/Netty实战.pdf)  
> [github:netty](https://github.com/netty/netty)  


# Netty概念及体系结构

Netty是一个异步事件驱动的Java网络应用框架。主要用于开发高性能、高可靠性的网络服务器和客户端。

- 异步通信
- 事件驱动

## Netty特点
- 设计
    - 统一的API，支持多种传输类型，阻塞的和非阻塞的
    - 简单而强大的线程模型
    - 真正的无连接数据报套接字支持
    - 链接逻辑组件以支持复用
- 易于使用
    - 详实的Javadoc和大量的示例集
    - 不需要超过JDK 1.6+③的依赖。（一些可选的特性可能需要Java 1.7+和/或额外的依赖）
- 性能
    - 拥有比Java的核心API更高的吞吐量以及更低的延迟
    - 得益于池化和复用，拥有更低的资源消耗
    - 最少的内存复制
- 健壮性
    - 不会因为慢速、快速或者超载的连接而导致OutOfMemoryError
    - 消除在高速网络中NIO应用程序常见的不公平读/写比率
- 安全性
    - 完整的SSL/TLS以及StartTLS支持
    - 可用于受限环境下，如Applet和OSGI
- 社区驱动
    - 发布快速而且频繁
  

## Netty核心组件

### Channel
Channel 是 Java NIO 的一个基本构造。

它代表一个到实体（如一个硬件设备、一个文件、一个网络套接字或者一个能够执行一个或者多个不同的I/O操作的程序组件）的开放连接，如读操作和写操作。

可以把 Channel 看作是传入（入站）或者传出（出站）数据的载体。因此，它可以被打开或者被关闭，连接或者断开连接。

### 回调
一个回调其实就是一个方法，一个指向已经被提供给另外一个方法的方法的引用。这使得后者可以在适当的时候调用前者。

Netty 在内部使用了回调来处理事件；
当一个回调被触发时，相关的事件可以被一个interface：`ChannelHandler` 的实现处理。

### Future
Future 提供了另一种在操作完成时通知应用程序的方式。这个对象可以看作是一个异步操作的结果的占位符；它将在未来的某个时刻完成，并提供对其结果的访问。

JDK 内置的Future所提供的实现，只允许手动检查对应的操作是否已经完成，或者一直阻塞直到它完成。这是非常繁琐的，所以 Netty 提供了它自己的实现：`ChannelFuture`，用于在执行异步操作的时候使用。

ChannelFuture提供了几种额外的方法能够注册一个或者多个ChannelFutureListener实例。
监听器的回调方法operationComplete()，将会在对应的操作完成时被调用。
然后监听器可以判断该操作是成功地完成了还是出错了。如果是后者，可以检索产生的Throwable。
简而言之，由ChannelFutureListener提供的通知机制消除了手动检查对应的操作是否完成的必要。

每个 Netty 的出站 I/O 操作都将返回一个 ChannelFuture；它们都不会阻塞。

### 事件和ChannelHandler
Netty 使用不同的事件来通知我们状态的改变或者是操作的状态。
这使得我们能够基于已经发生的事件来触发适当的动作。这些动作可能是：
- 记录日志；
- 数据转换；
- 流控制；
- 应用程序逻辑。

Netty按照入站 或 出站 数据流的相关性进行分类。
- 由入站数据或者相关的状态更改而触发的事件包括：
  - 连接已被激活或者连接失活；
  - 数据读取；
  - 用户事件；
  - 错误事件。
- 出站事件是未来将会触发的某个动作的操作结果，这些动作包括：
  - 打开或者关闭到远程节点的连接；
  - 将数据写到或者冲刷到套接字。

每个事件都可以被分发给 `ChannelHandler` 类中的某个用户实现的方法。

Netty 提供了大量预定义的可以开箱即用的 ChannelHandler 实现，包括用于各种协议（如 HTTP 和 SSL/TLS）的 ChannelHandler。
在内部，ChannelHandler 自己也使用了事件 和 Future，使得它们也成为了应用程序将使用的相同抽象的消费者。


## NettyDemo

### Netty服务器

所有的 Netty 服务器都需要以下两部分：
- 至少一个 ChannelHandler， 该组件实现了服务器对从客户端接收的数据的处理，即它的业务逻辑。
  [NettyServerHandlerDemo.java](https://github.com/Panl99/demo/tree/master/demo-netty/src/main/java/com/lp/demo/netty/netty/base/NettyServerHandlerDemo.java)
- 引导， 这是配置服务器的启动代码。至少，它会将服务器绑定到它要监听连接请求的端口上。
  [NettyServerDemo.java](https://github.com/Panl99/demo/tree/master/demo-netty/src/main/java/com/lp/demo/netty/netty/base/NettyServerDemo.java)

- ChannelHandler，父接口，它的实现负责接收并响应事件通知。
- ChannelInboundHandler 接口，用来定义响应入站事件的方法。
- ChannelInboundHandlerAdapter 默认实现 ChannelInboundHandler。
  - channelRead() — 对于每个传入的消息都要调用；
  - channelReadComplete() — 通知ChannelInboundHandler最后一次对channelRead()的调用是当前批量读取中的最后一条消息；
  - exceptionCaught() — 在读取操作期间，有异常抛出时会调用。
    如果不捕获异常，会发生什么呢？
    每个 Channel 都拥有一个与之相关联的 ChannelPipeline，其持有一个 ChannelHandler 的
    实例链。在默认的情况下，ChannelHandler 会把对它的方法的调用转发给链中的下一个 Channel-
    Handler。因此，如果 exceptionCaught()方法没有被该链中的某处实现，那么所接收的异常将会被
    传递到 ChannelPipeline 的尾端并被记录。为此，你的应用程序应该提供至少有一个实现了
    exceptionCaught()方法的 ChannelHandler。（6.4 节详细地讨论了异常处理）。

引导过程中所需要的步骤如下：
- 创建一个 ServerBootstrap 的实例以引导和绑定服务器；
- 创建并分配一个 NioEventLoopGroup 实例以进行事件的处理，如接受新连接以及读/写数据；
- 指定服务器绑定的本地的 InetSocketAddress；
- 使用一个 EchoServerHandler 的实例初始化每一个新的 Channel；
- 调用 ServerBootstrap.bind()方法以绑定服务器。


### Netty客户端

Netty客户端数据处理器：[NettyClientHandlerDemo.java](https://github.com/Panl99/demo/tree/master/demo-netty/src/main/java/com/lp/demo/netty/netty/base/NettyClientHandlerDemo.java)

Netty客户端引导：[NettyClientDemo.java](https://github.com/Panl99/demo/tree/master/demo-netty/src/main/java/com/lp/demo/netty/netty/base/NettyClientDemo.java)

SimpleChannelInboundHandler 与 ChannelInboundHandler  
为什么我们在客户端使用的是 `SimpleChannelInboundHandler`，服务端使用的 `ChannelInboundHandlerAdapter` 呢？  
这和两个因素的相互作用有关：①业务逻辑如何处理消息；②Netty 如何管理资源。
- 在客户端，当 channelRead0()方法完成时，你已经有了传入消息，并且已经处理完它了。当该方法返回时，SimpleChannelInboundHandler 负责释放指向保存该消息的 ByteBuf 的内存引用。
- 在服务端，你仍然需要将传入消息回送给发送者，而 write()操作是异步的，直到 channelRead()方法返回后可能仍然没有完成。
  
为此，NettyServerHandlerDemo扩展了 ChannelInboundHandlerAdapter，其在这个时间点上不会释放消息。
消息在 NettyServerHandlerDemo 的 channelReadComplete()方法中，当 writeAndFlush()方法被调用时被释放。



## Netty的组件和设计

### Netty网络抽象的代表组件
- `Channel`：Socket；
- `EventLoop`：控制流、多线程处理、并发；
- `ChannelFuture`：异步通知。

**Channel 接口：**
- 基本的 I/O 操作（bind()、connect()、read()和 write()）依赖于底层网络传输所提供的原语。
- 在基于 Java 的网络编程中，其基本的构造是 class Socket。
- Netty 的 Channel 接口所提供的 API，大大地降低了直接使用 Socket 类的复杂性。
- 此外，Channel 也是拥有许多预定义的、专门化实现的广泛类层次结构的根。 （EmbeddedChannel、LocalServerChannel、NioDatagramChannel、NioSctpChannel、NioSocketChannel）

Channel 的方法

方法名 | 描述
--- | ---
eventLoop | 返回分配给 Channel 的 EventLoop
pipeline | 返回分配给 Channel 的 ChannelPipeline
isActive | 如果 Channel 是活动的，则返回 true 。活动的意义可能依赖于底层的传输。例如，一个 Socket 传输一旦连接到了远程节点便是活动的，而一个 Datagram 传输一旦被打开便是活动的
localAddress | 返回本地的 SokcetAddress
remoteAddress | 返回远程的 SocketAddress
write | 将数据写到远程节点。这个数据将被传递给 ChannelPipeline ，并且排队直到它被冲刷
flush | 将之前已写的数据冲刷到底层传输，如一个 Socket
writeAndFlush | 一个简便的方法，等同于调用 write() 并接着调用 flush()

**EventLoop 接口：** 

EventLoop 定义了 Netty 的核心抽象，用于处理连接的生命周期中所发生的事件。

![Channel、EventLoop、Thread以及EventLoopGroup之间的关系](../resources/static/images/Channel、EventLoop、Thread以及EventLoopGroup之间的关系.png)

- 一个 EventLoopGroup 包含一个或者多个 EventLoop；
- 一个 EventLoop 在它的生命周期内只和一个 Thread 绑定；
- 所有由 EventLoop 处理的 I/O 事件都将在它专有的 Thread 上被处理；
- 一个 Channel 在它的生命周期内只注册于一个 EventLoop；
- 一个 EventLoop 可能会被分配给一个或多个 Channel。

**ChannelFuture 接口：**

异步的，用于在某个操作完成之后的某个时间点确定其结果的方法。

其 addListener()方法注册了一个 ChannelFutureListener，以便在某个操作完成时（无论是否成功）得到通知。

可以将 ChannelFuture 看作是将来要执行的操作的结果的占位符。
它究竟 什么时候 被执行则可能取决于若干的因素，因此不可能准确地预测，但是可以肯定的是它 将会 被执行。
此外，所有**属于同一个 Channel 的操作都被保证其将以它们被调用的顺序被执行**。


### 管理数据流以及执行应用程序处理逻辑的组件
- `ChannelHandler`：所有处理入站和出站数据的应用程序逻辑的容器。
- `ChannelPipeline`：提供了 ChannelHandler 链的容器，并定义了用于在该链上传播入站和出站事件流的 API。
  当 Channel 被创建时，它会被自动地分配到它专属的 ChannelPipeline。

如果事件的运动方向是从客户端到服务器端，那么我们称这些事件为出站的，反之则称为入站的。

如果一个消息或者任何其他的入站事件被读取，那么它会从 ChannelPipeline 的头部开始流动，并被传递给第一个 ChannelInboundHandler。
这个 ChannelHandler 不一定会实际地修改数据，具体取决于它的具体功能，在这之后，数据将会被传递给链中的下一个 ChannelInboundHandler。
最终，数据将会到达 ChannelPipeline 的尾端，届时，所有处理就都结束了。

数据的出站运动（即正在被写的数据）在概念上也是一样的。在这种情况下，数据将从 ChannelOutboundHandler 链的尾端开始流动，直到它到达链的头部为止。
在这之后，出站数据将会到达网络传输层，这里显示为 Socket。通常情况下，这将触发一个写操作。

在Netty中，有两种发送消息的方式。
- 直接写到Channel中，这种方式将会导致消息从ChannelPipeline 的尾端开始流动，
- 写到和ChannelHandler相关联的ChannelHandlerContext对象中，这种方式将导致消息从 ChannelPipeline 中的下一个 ChannelHandler 开始流动。

Netty 以适配器类的形式提供了大量默认的 ChannelHandler 实现，其旨在简化应用程序处理逻辑的开发过程。

为什么需要适配器类？  
有一些适配器类可以将编写自定义的 ChannelHandler 所需要的努力降到最低限度，因为它们提供了定义在对应接口中的所有方法的默认实现。  
下面这些是编写自定义 ChannelHandler 时经常会用到的适配器类：  
- ChannelHandlerAdapter
- ChannelInboundHandlerAdapter
- ChannelOutboundHandlerAdapter
- ChannelDuplexHandler

**ChannelHandler 的典型用途包括：**
- 将数据从一种格式转换为另一种格式；
- 提供异常的通知；
- 提供 Channel 变为活动的或者非活动的通知；
- 提供当 Channel 注册到 EventLoop 或者从 EventLoop 注销时的通知；
- 提供有关用户自定义事件的通知。

### 编解码器
当通过 Netty 发送或者接收一个消息的时候，就将会发生一次数据转换。
- 入站消息，会被从字节解码转换为另一种格式，通常是一个 Java 对象。
- 出站消息，将从它的当前格式被编码为字节。

对应于特定的需要，Netty 为编码器和解码器提供了不同类型的抽象类。
可以将消息转为一种中间格式，而不是字节。

所有由 Netty 提供的编码器/解码器适配器类都实现了 ChannelOutboundHandler 或者 ChannelInboundHandler 接口。
- 对于入站数据来说，channelRead 方法/事件已经被重写了。
  对于每个从入站Channel 读取的消息，这个方法都将会被调用。随后，它将调用由预置解码器所提供的 decode()方法，并将已解码的字节转发给 ChannelPipeline 中的下一个 ChannelInboundHandler。
- 出站消息的模式是相反方向的：编码器将消息转换为字节，并将它们转发给下一个ChannelOutboundHandler。

### 引导
Netty 的引导类为应用程序的网络层配置提供了容器，这涉及将一个进程绑定到某个指定的端口（引导一个服务器，ServerBootstrap），
或者将一个进程连接到另一个运行在某个指定主机的指定端口上的进程（引导一个客户端，Bootstrap）。



## 传输

Netty 所提供的传输:
- [NIO](#NIO-非阻塞IO)(io.netty.channel.socket.nio): 使用 java.nio.channels 包作为基础——基于选择器的方式
- [Epoll](#Epoll-用于Linux的本地非阻塞传输)(io.netty.channel.epoll): 由 JNI 驱动的 epoll() 和非阻塞 IO。
  这个传输支持只有在Linux上可用的多种特性，如 SO_REUSEPORT， 比NIO 传输更快，而且是完全非阻塞的
- [OIO](#OIO-旧的阻塞IO)(io.netty.channel.socket.oio): 使用 java.net 包作为基础——使用阻塞流
- [Local](#用于JVM内部通信的Local传输)(io.netty.channel.local): 可以在 VM 内部通过管道进行通信的本地传输
- [Embedded](#Embedded传输)(io.netty.channel.embedded): Embedded 传输，允许使用 ChannelHandler 而又不需要一个真正的基于网络的传输。
  这在测试你的ChannelHandler 实现时非常有用


### NIO-非阻塞IO

选择器背后的基本概念是充当一个注册表，在那里你将可以请求在 Channel 的状态发生变化时得到通知。可能的状态变化有：
- 新的 Channel 已被接受并且就绪；
- Channel 连接已经完成；
- Channel 有已经就绪的可供读取的数据；
- Channel 可用于写数据。

选择器运行在一个检查状态变化并对其做出相应响应的线程上，在应用程序对状态的改变做出响应之后，选择器将会被重置，并将重复这个过程。

常量值代表了由`class java.nio.channels.SelectionKey`定义的位模式。
这些位模式可以组合起来定义一组应用程序正在请求通知的状态变化集。

选择操作的位模式:

名称 | 描述
--- | ---
OP_ACCEPT | 请求在接受新连接并创建 Channel 时获得通知
OP_CONNECT | 请求在建立一个连接时获得通知
OP_READ | 请求当数据已经就绪，可以从 Channel 中读取时获得通知
OP_WRITE | 请求当可以向 Channel 中写更多的数据时获得通知。这处理了套接字缓冲区被完全填满时的情况，这种情况通常发生在数据的发送速度比远程节点可处理的速度更快的时候

**零拷贝**（zero-copy）是一种目前只有在使用 NIO 和 Epoll 传输时才可使用的特性。
它使你可以快速高效地将数据从文件系统移动到网络接口，而不需要将其从内核空间复制到用户空间，其在像 FTP 或者 HTTP 这样的协议中可以显著地提升性能。
但是，并不是所有的操作系统都支持这一特性。特别地，它对于实现了数据加密或者压缩的文件系统是不可用的(只能传输文件的原始内容)。反过来说，传输已被加密的文件则不是问题。

### Epoll-用于Linux的本地非阻塞传输

Netty为Linux提供了一组NIO API，其以一种和它本身的设计更加一致的方式使用epoll，并且以一种更加轻量的方式使用中断。
在高负载下它的性能要优于JDK的NIO实现。

在代码中使用 epoll 替代 NIO：
- 将 `NioEventLoopGroup` 替换为 `EpollEventLoopGroup`，
- 将 `NioServerSocketChannel.class` 替换为 `EpollServerSocketChannel.class` 即可。

### OIO-旧的阻塞IO

Netty 的 OIO 传输实现可以通过常规的传输 API 使用，是建立在 java.net 包的阻塞实现之上的，不是异步的。

### 用于JVM内部通信的Local传输

Netty 提供了一个 Local 传输，用于在同一个 JVM 中运行的客户端和服务器程序之间的异步通信。

在这个传输中，和服务器 Channel 相关联的 SocketAddress 并没有绑定物理网络地址；相反，只要服务器还在运行，它就会被存储在注册表里，并在 Channel 关闭时注销。
因为这个传输并不接受真正的网络流量，所以它并不能够和其他传输实现进行互操作。因此，客户端希望连接到（在同一个JVM中）使用了这个传输的服务器端时也必须使用它。除了这个限制，它的使用方式和其他的传输一模一样。

### Embedded传输

Netty 提供了一种额外的传输，使得你可以将一组 ChannelHandler 作为帮助器类嵌入到其他的 ChannelHandler 内部。
通过这种方式，你将可以扩展一个 ChannelHandler 的功能，而又不需要修改其内部代码。

Embedded 传输的关键是一个被称为 EmbeddedChannel 的具体的 Channel 实现。


### 支持的传输和网络协议

传 输 | TCP | UDP | SCTP* | UDT
--- | --- | --- | --- | ---
NIO | × | × | × | ×
Epoll（仅Linux） | × | × | — | —
OIO | × | × | × | ×

在 Linux 上启用 SCTP 需要内核的支持，并且需要安装用户库。 只有SCTP传输有这些特殊要求。

如果只是为了支持更高的并发连接数，服务器平台可能需要配置得和客户端不一样：
- **非阻塞代码库**：如果代码库中没有阻塞调用（或者你能够限制它们的范围），那么在 Linux 上使用 **NIO 或者 epoll** 始终是个好主意。
  虽然 NIO/epoll 旨在处理大量的并发连接，但是在处理较小数目的并发连接时，它也能很好地工作，尤其是考虑到它在连接之间共享线程的方式。
- **阻塞代码库**：如果代码库严重地依赖于阻塞 I/O，那么在尝试将其直接转换为 Netty 的 NIO 传输时，可以考虑分阶段迁移：
  先从 **OIO** 开始，等你的代码修改好之后，再迁移到 NIO（或者 epoll(Linux)）。
- 在同一个 JVM 内部的通信：在同一个 JVM 内部的通信，不需要通过网络暴露服务，是**Local** 传输的完美用例。这将消除所有真实网络操作的开销，同时仍然使用你的 Netty 代码库。
  如果随后需要通过网络暴露服务，那么你将只需要把传输改为 NIO 或者 OIO 即可。
- 测试你的ChannelHandler实现：如果你想要为自己的ChannelHandler实现编写单元测试，那么请考虑使用**Embedded** 传输。
  这既便于测试代码，而又不需要创建大量的模拟（mock）对象。你的类将仍然符合常规的 API 事件流，保证该 ChannelHandler 在和真实的传输一起使用时能够正确地工作。


## ByteBuf

网络数据的基本单位总是字节，Java NIO提供ByteBuffer作为它的字节容器（用起来复杂、繁琐）。

Netty的ByteBuf可以替代ByteBuffer，功能更强大，灵活性更高。

### ByteBuf的API
Netty的数据处理API通过两个组件暴露：
- abstract class ByteBuf
- interface ByteBufHolder

ByteBuf API优点：
- 它可以被用户自定义的缓冲区类型扩展；
- 通过内置的复合缓冲区类型实现了透明的零拷贝；
- 容量可以按需增长（类似于 JDK 的 StringBuilder）；
- 在读和写这两种模式之间切换不需要调用 ByteBuffer 的 flip()方法；
- 读和写使用了不同的索引；
- 支持方法的链式调用；
- 支持引用计数；
- 支持池化。

其他类可用于管理ByteBuf实例的分配，以及执行各种对于数据容器本身 和它所持有的数据的操作。

### ByteBuf类——Netty的数据容器

所有的网络通信都涉及字节序列的移动，Netty的ByteBuf高效易用的数据结构 通过使用不同的索引来简化它对所包含的数据的访问。

### ByteBuf是如何工作的
ByteBuf维护了两个不同的索引：一个用于读取，一个用于写入。
- 当从 ByteBuf 读取时，它的 readerIndex 将会被递增已经被读取的字节数。
- 当写入 ByteBuf 时，它的 writerIndex 也会被递增。

![Netty-ByteBuf-一个空ByteBuf的布局结构和状态](../resources/static/images/Netty-ByteBuf-一个空ByteBuf的布局结构和状态.png)

如果打算读取字节直到 readerIndex 达到 和 writerIndex 同样的值时会发生什么？
- 会触发一个 IndexOutOfBoundsException。因为会到达 “可以读取的” 数据的末尾，就像要读取超出数组末尾的数据一样。

- 名称以 read 或者 write 开头的 ByteBuf 方法，将会推进其对应的索引，
- 名称以 set 或者 get 开头的操作则不会。但这些方法将在作为一个参数传入的一个相对索引上执行操作。
- 可以指定 ByteBuf 的最大容量。试图移动写索引（即 writerIndex）超过这个值将会触发一个异常。（默认的限制是 Integer.MAX_VALUE。）

### ByteBuf的使用模式
1. 堆缓冲区
   - 将数据存储在 JVM 的堆空间中。
   - 这种模式被称为支撑数组（backing array），它能在没有使用池化的情况下提供快速的分配和释放。
   - 非常适合于有遗留的数据需要处理的情况。
```java
public static void heapBuffer() {
    ByteBuf heapBuf = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
    //检查 ByteBuf 是否有一个支撑数组
    if (heapBuf.hasArray()) { //当 hasArray() 方法返回 false 时，尝试访问支撑数组将触发一个 UnsupportedOperationException 。这个模式类似于 JDK 的 ByteBuffer 的用法。
        //如果有，则获取对该数组的引用
        byte[] array = heapBuf.array();
        //计算第一个字节的偏移量
        int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
        //获得可读字节数
        int length = heapBuf.readableBytes();
        //使用数组、偏移量和长度作为参数调用你的方法
        handleArray(array, offset, length);
    }
}
```
2. 直接缓冲区
   - 我们期望用于对象创建的内存分配永远都来自于堆中，避免在每次调用本地 I/O 操作之前（或者之后）将缓冲区的内容复制到一个中间缓冲区（或者从中间缓冲区把内容复制到缓冲区）。
   - 直接缓冲区对于网络数据传输优点：直接缓冲区的内容将驻留在常规的会被垃圾回收的堆之外。（ByteBuffer的Javadoc中指出的）
     - 如果数据包含在一个在堆上分配的缓冲区中，那么事实上，在通过套接字发送它之前，JVM将会在内部把你的缓冲区复制到一个直接缓冲区中。
   - 直接缓冲区的主要缺点是：①相对于基于堆的缓冲区，它们的分配和释放都较为昂贵。②因为数据不是在堆上，所以需要进行一次复制。

显然，与使用支撑数组相比，这涉及的工作更多。因此，如果事先知道容器中的数据将会被作为数组来访问，你可能更愿意使用堆内存。

3. 复合缓冲区
- 为多个 ByteBuf 提供一个聚合视图，在这里可以根据需要添加或者删除 ByteBuf 实例，是JDK的ByteBuffer不具备的特性。
- 通过一个 ByteBuf 子类`CompositeByteBuf` 实现这种模式，它提供了一个将多个缓冲区表示为单个合并缓冲区的虚拟表示。
  - CompositeByteBuf 中的 ByteBuf 实例可能同时包含 直接内存分配 和非直接内存分配。
  - 如果其中只有一个实例，那么对 CompositeByteBuf 上的 hasArray() 方法的调用将返回该组件上的 hasArray() 方法的值；否则它将返回 false 。
  - 举例说明CompositeByteBuf的好处，让我们考虑一下一个由两部分：头部和主体 组成的将通过 HTTP 协议传输的消息。
    这两部分由应用程序的不同模块产生，将会在消息被发送的时候组装。该应用程序可以选择为多个消息重用相同的消息主体。当这种情况发生时，对于每个消息都将会创建一个新的头部。
    因为我们不想为每个消息都重新分配这两个缓冲区，所以使用 CompositeByteBuf 是一个完美的选择。它在消除了没必要的复制的同时，暴露了通用的 ByteBuf API。
  ![Netty-ByteBuf-持有一个头部和主体的CompositeByteBuf](../resources/static/images/Netty-ByteBuf-持有一个头部和主体的CompositeByteBuf.png)

使用 CompositeByteBuf 的复合缓冲区模式:    
```java
CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
ByteBuf headerBuf = ...; // can be backing or direct
ByteBuf bodyBuf = ...; // can be backing or direct
messageBuf.addComponents(headerBuf, bodyBuf); // 将 ByteBuf 实例追加到 CompositeByteBuf
.....
messageBuf.removeComponent(0); // 删除位于索引位置为 0（第一个组件）的 ByteBuf
for (ByteBuf buf : messageBuf) { // 循环遍历所有的ByteBuf 实例
    System.out.println(buf.toString());
}
```
CompositeByteBuf 可能不支持访问其支撑数组，因此访问 CompositeByteBuf 中的数据类似于（访问）直接缓冲区的模式：
```java
CompositeByteBuf compBuf = Unpooled.compositeBuffer();
int length = compBuf.readableBytes(); // 获得可读字节数
byte[] array = new byte[length]; // 分配一个具有可读字节数长度的新数组
compBuf.getBytes(compBuf.readerIndex(), array); // 将字节读到该数组中
handleArray(array, 0, array.length); // 使用偏移量和长度作为参数使用该数组
```

## 字节级操作

1. 随机访问索引
```java
ByteBuf buffer = ...;
for (int i = 0; i < buffer.capacity(); i++) {
  byte b = buffer.getByte(i); // ByteBuf 的索引是从零开始
  System.out.println((char) b);
}
```
- 这种方式访问数据既不会改变readerIndex 也不会改变writerIndex。
- 通过调用readerIndex(index) 或者 writerIndex(index)来手动移动这两者。

2. 顺序访问索引
- ByteBuf 是同时具有读索引和写索引的。但JDK的ByteBuffer只有一个索引，读写时需要调用flip()方法在读模式和写模式之间进行切换。
   
![Netty-ByteBuf-被读索引和写索引划分为三个区域](../resources/static/images/Netty-ByteBuf-被读索引和写索引划分为三个区域.png)
   
3. 可丢弃字节
- 已读过的字节，可以通过调用`discardReadBytes()`方法丢弃它们并回收空间。
- 可丢弃字节的分段初始大小为 0，存储在 readerIndex 中，会随着 read 操作的执行而增加（get*操作不会移动 readerIndex）。
- 丢弃已读字节后，回收的空间会变为可写空间。在调用discardReadBytes()之后，只是移动了可以读取的字节以及writerIndex，而没有对所有可写入的字节进行擦除写，所以对可写分段的内容并没有任何的保证。
![Netty-ByteBuf-丢弃已读字节之后的ByteBuf](../resources/static/images/Netty-ByteBuf-丢弃已读字节之后的ByteBuf.png)
  - 频繁地调用`discardReadBytes()`方法以确保可写分段的最大化，但是这很有可能会导致内存复制，因为可读字节必须被移动到缓冲区的开始位置。所以在必要的时候才丢弃已读字节，比如内存空间比较宝贵。

4. 可读字节
- ByteBuf的可读字节分段存储了实际数据。
- 新分配的、包装的、复制的缓冲区的 默认的readerIndex 值为 0。
- 任何名称以read、skip开头的操作都将检索 或者跳过位于当前readerIndex的数据，并将它增加已读字节数。

- 如果被调用的方法需要一个 ByteBuf 参数作为写入的目标，并且没有指定目标索引参数，那么该目标缓冲区的 writerIndex 也将被增加，例如：
  `readBytes(ByteBuf dest);`
- 如果在缓冲区的可读字节数用完的时候 读取数据，会导致索引越界异常（IndexOutOfBoundsException）。
```java
// 读取所有可读字节

ByteBuf buffer = ...;
while (buffer.isReadable()) {
    System.out.println(buffer.readByte());
}
```

5. 可写字节
- 可写字节分段：一个空白的、写入就绪的内存区域。
- 新分配的缓冲区的 writerIndex 的默认值为0。
- 任何名称以 write 开头的操作 都将从当前的 writerIndex 处开始写入数据，并将它增加已经写入的字节数。

- 如果写操作的目标也是 ByteBuf，并且没有指定源索引的值，则源缓冲区的 readerIndex 也同样会被增加相同的大小。
  `writeBytes(ByteBuf dest);`
- 如果往目标写入超过目标容量的数据，将导致索引越界异常（IndexOutOfBoundsException）
```java
// 一个用随机整数值填充缓冲区，直到它空间不足为止
// writeableBytes()方法在这里被用来确定该缓冲区中是否还有足够的空间。
ByteBuf buffer = ...;
while (buffer.writableBytes() >= 4) {
    buffer.writeInt(random.nextInt());
}
```

6. 索引管理
- 标记和重置 ByteBuf 的 readerIndex 和 writerIndex：
  - `markReaderIndex()`
  - `markWriterIndex()`
  - `resetWriterIndex()`
  - `resetReaderIndex()`
  
  这些和InputStream 上的 mark(int readlimit)和 reset()方法调用类似，只是没有readlimit 参数来指定标记什么时候失效。
- 将索引移动到指定位置：
  - `readerIndex(int)`
  - `writerIndex(int)`
  
  无效的位置都将导致一个 IndexOutOfBoundsException。
- 将 readerIndex 和 writerIndex 都设置为 0：`clear()`，但并不会清除内存中的内容。
  调用 clear()比调用 `discardReadBytes()`轻量得多，因为它将只是重置索引 而不会复制任何的内存。





