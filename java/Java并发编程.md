# 并发编程的挑战

并发编程的目的：是为了让程序更快地运行，而不是启动更多的线程让程序最大限度的并发执行。

多线程执行任务的挑战：
1. 上下文切换问题。
2. 死锁问题。
3. 硬件、软件资源受限问题。

## 并发问题-上下文切换

cpu通过给每个线程分配`cpu时间片`来实现**多线程执行代码**。

**时间片**是CPU分配给各个线程的时间，因为时间片非常短，所以CPU通过不停地切换线程执行，
让我们感觉多个线程是同时执行的，时间片一般是几十毫秒（ms）。

CPU通过时间片分配算法来循环执行任务，当前任务执行一个时间片后会切换到下一个任务。
但是，在切换前会保存上一个任务的状态，以便下次切换回这个任务时，可以再加载这个任务的状态。
所以任务从保存到再加载的过程就是一次**上下文切换**。

上下文切换会影响多线程的执行速度。

当并发执行累加操作不超过百万次时，速度会比串行执行累加操作要慢，因为线程有创建和上下文切换的开销。

### 上下文切换次数和时长
使用`vmstat`可以测量上下文切换的次数。
使用`Lmbench3`可以测量上下文切换的时长。

**上下文每1秒切换1000多次。**

### 减少上下文切换
- **无锁并发编程**：多线程竞争锁时，会引起上下文切换，所以多线程处理数据时，可以用一些办法来避免使用锁，如将数据的ID按照Hash算法取模分段，不同的线程处理不同段的数据。
- **CAS算法**：Java的Atomic包使用CAS算法来更新数据，而不需要加锁。
- **使用最少线程**：避免创建不需要的线程，比如任务很少，但是创建了很多线程来处理，这样会造成大量线程都处于等待状态。
- **协程**：在单线程里实现多任务的调度，并在单线程里维持多个任务间的切换。

实战：减少线上大量WAITING的线程，来减少上下文切换次数：

1. 用jstack命令dump线程信息，看看pid为3117的进程里的线程都在做什么。
`sudo -u admin /opt/ifeve/java/bin/jstack 31177 > /home/tengfei.fangtf/dump17`
2. 统计所有线程分别处于什么状态。  
发现300多个线程处于WAITING（onobjectmonitor）状态。
```
[tengfei.fangtf@ifeve ~]$ grep java.lang.Thread.State dump17 | awk '{print $2$3$4$5}'
| sort | uniq -c
39 RUNNABLE
21 TIMED_WAITING(onobjectmonitor)
6 TIMED_WAITING(parking)
51 TIMED_WAITING(sleeping)
305 WAITING(onobjectmonitor)
3 WAITING(parking)
```
3.打开dump文件查看处于WAITING（onobjectmonitor）的线程在做什么。  
发现这些线程基本全是JBOSS的工作线程，在await。说明JBOSS线程池里线程接收到的任务太少，大量线程都闲着。
``` 
"http-0.0.0.0-7001-97" daemon prio=10 tid=0x000000004f6a8000 nid=0x555e in
Object.wait() [0x0000000052423000]
java.lang.Thread.State: WAITING (on object monitor)
at java.lang.Object.wait(Native Method)
- waiting on <0x00000007969b2280> (a org.apache.tomcat.util.net.AprEndpoint$Worker)
at java.lang.Object.wait(Object.java:485)
at org.apache.tomcat.util.net.AprEndpoint$Worker.await(AprEndpoint.java:1464)
- locked <0x00000007969b2280> (a org.apache.tomcat.util.net.AprEndpoint$Worker)
at org.apache.tomcat.util.net.AprEndpoint$Worker.run(AprEndpoint.java:1489)
at java.lang.Thread.run(Thread.java:662)
```
4. 减少JBOSS的工作线程数，找到JBOSS的线程池配置信息，将maxThreads降到100。
``` 
<maxThreads="250" maxHttpHeaderSize="8192"
emptySessionPath="false" minSpareThreads="40" maxSpareThreads="75"
maxPostSize="512000" protocol="HTTP/1.1"
enableLookups="false" redirectPort="8443" acceptCount="200" bufferSize="16384"
connectionTimeout="15000" disableUploadTimeout="false" useBodyEncodingForURI= "true">
```
5. 重启JBOSS，再dump线程信息，然后统计WAITING（onobjectmonitor）的线程，
发现减少了175个。WAITING的线程少了，系统上下文切换的次数就会少，因为每一次从WAITTING到RUNNABLE都会进行一次上下文的切换。
``` 
[tengfei.fangtf@ifeve ~]$ grep java.lang.Thread.State dump17 | awk '{print $2$3$4$5}'
| sort | uniq -c
44 RUNNABLE
22 TIMED_WAITING(onobjectmonitor)
9 TIMED_WAITING(parking)
36 TIMED_WAITING(sleeping)
130 WAITING(onobjectmonitor)
1 WAITING(parking)
```

## 并发问题-死锁

避免死锁常见方法：
- 避免一个线程同时获取多个锁。
- 避免一个线程在锁内同时占用多个资源，尽量保证每个锁只占用一个资源。
- 尝试使用定时锁，使用lock.tryLock（timeout）来替代使用内部锁机制。
- 对于数据库锁，加锁和解锁必须在一个数据库连接里，否则会出现解锁失败的情况。


# Java并发机制的底层实现原理

Java代码在编译后会变成Java字节码，字节码被类加载器加载到JVM里，JVM执行字节码，最终需要转化为汇编指令在CPU上执行，Java中所使用的并发机制依赖于JVM的实现和CPU的指令。

## volatile的应用

volatile作用：
- 声明一个变量对所有线程可见。
- 禁止指令重排。（对变量的赋值操作顺序与程序代码执行顺序一致）

## synchronized的实现原理与应用

synchronized的作用范围：
- 对于普通同步方法，锁是当前实例对象。
- 对于静态同步方法，锁是当前类的Class对象。
- 对于同步方法块，锁是synchronized括号里配置的对象。

### synchronized的实现原理
从JVM规范中可以看到Synchronized在JVM里的实现原理，
JVM基于进入和退出Monitor对象来实现方法同步和代码块同步，但两者的实现细节不一样。
代码块同步是使用`monitorenter`和`monitorexit`指令实现的，而方法同步是使用另外一种方式实现的，细节在JVM规范里并没有详细说明。但是，方法的同步同样可以使用这两个指令来实现。

monitorenter指令是在编译后插入到同步代码块的开始位置，而monitorexit是插入到方法结束处和异常处，JVM要保证每个monitorenter必须有对应的monitorexit与之配对。
任何对象都有一个monitor与之关联，当且一个monitor被持有后，它将处于锁定状态。
线程执行到monitorenter指令时，将会尝试获取对象所对应的monitor的所有权，即尝试获得对象的锁。

### Java对象头
synchronized用的锁是存在Java对象头的。
- 如果对象是数组类型，虚拟机使用3个字宽存储对象头；
- 如果对象是非数组类型，虚拟机则用2字宽存储对象头；
- 在32位虚拟机中，1字宽 = 4字节（32bit）。

## 原子操作的实现原理

处理器提供 总线锁定 和 缓存锁定 两个机制来保证复杂内存操作的原子性。

- 总线锁：就是使用处理器提供的一个LOCK＃信号，当一个处理器在总线上输出此信号时，其他处理器的请求将被阻塞住，那么该处理器可以独占共享内存。
    - 解决了 共享变量被多个处理器同时进行改写操作。
- 缓存锁：内存区域如果被缓存在处理器的缓存行中，并且在Lock操作期间被锁定，那么当它执行锁操作回写到内存时，处理器不在总线上声言LOCK＃信号，而是修改内部的内存地址，并允许它的缓存一致性机制来保证操作的原子性，
因为缓存一致性机制会阻止同时修改由两个以上处理器缓存的内存区域数据，当其他处理器回写已被锁定的缓存行的数据时，会使缓存行无效。
    - 解决 总线锁定把CPU和内存之间的通信锁住了，这使得锁定期间，其他处理器不能操作其他内存地址的数据，
    - 在同一时刻，我们只需保证对某个内存地址的操作是原子性即可
    
两种情况下处理器不会使用缓存锁定：
- 当操作的数据不能被缓存在处理器内部，或操作的数据跨多个缓存行（cache line）时，则处理器会调用总线锁定。
- 有些处理器不支持缓存锁定。就算锁定的内存区域在处理器的缓存行中也会调用总线锁定。


# Java内存模型

## Java内存模型基本概念

### 并发编程的两个关键问题
1. 线程之间如何**通信**，（指线程之间以何种机制来交换信息，命令式编程中通信机制有两种：**共享内存**、**消息传递**）。
    - 在共享内存的并发模型中，线程之间共享程序的公共状态，通过读-写内存中的公共状态来进行隐性的通信。
    - 在消息传递的并发模型中，线程之间没有公共状态，线程之间必须通过发送消息来显示进行通信。
2. 线程之间如何**同步**，（指程序中用于控制不同线程间操作发生相对顺序的机制）。
    - 在共享内存的并发模型中，同步是显式进行的。开发者必须显式指定某个方法或某段代码需要在线程之间互斥执行。
    - 在消息传递的并发模型中，同步是隐式进行的。因为消息的发送必须在消息的接收之前。

**Java的并发采用的是 共享内存模型**  
- Java线程之间的通信总是隐式进行的，整个通信过程对开发者是完全透明的。
- 如果开发者不理解多线程之间的隐式通信机制，就可能会遇到各种内存可见性问题。
    
### Java内存模型的抽象结构
- 共享变量：实例域、静态域、数组元素。（所有实例域、静态域、数组元素都存储在堆内存中，堆内存在线程之间是共享的）。
- 局部变量：方法定义参数、异常处理器参数 不会在线程之间共享，它们不会存在内存可见性问题，也不受内存模型的影响。

Java线程之间的通信由Java内存模型(`JMM`)控制，JMM决定一个线程对共享变量的写入 何时对另一个线程可见。
- 从抽象的角度来看，JMM定义了线程和主内存之间的抽象关系：
  线程之间的共享变量存储在主内存中，每个线程都有一个私有的本地内存，本地内存中存储了该线程以读/写共享变量的副本。
  本地内存是JMM的一个抽象概念，并不真实存在。它涵盖了缓存、写缓冲区、寄存器以及其他的硬件和编译器优化。
  
线程A、线程B之间通信步骤：
1. 线程A把本地内存A中更新过的共享变量刷新到主内存中去。
2. 线程B到主内存中读取线程A更新的共享变量。

### 重排序
重排序是指编译器和处理器为了优化程序性能而对指令序列进行重新排序的一种手段。

TODO

## Java内存模型中的顺序一致性

TODO

## 同步原语
synchronized、volatile、final
3个同步原语的内存语义 及重排序规则在处理器中的实现。

TODO

## Java内存模型的设计

TODO

