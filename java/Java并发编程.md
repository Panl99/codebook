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