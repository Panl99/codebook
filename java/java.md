> [Java 8实战](https://github.com/Panl99/codebook/blob/master/resources/static/books/Java%208%E5%AE%9E%E6%88%98.pdf)  
> [Java 8函数式编程](https://github.com/Panl99/codebook/blob/master/resources/static/books/Java%208%E5%87%BD%E6%95%B0%E5%BC%8F%E7%BC%96%E7%A8%8B.pdf)  

# github-toc
- [1.1、String常用方法](#11string常用方法)
- [日期和时间](#日期和时间)
    -[标准库API](#标准库api)
        -[java.util.Date](#javautildate)
- [8、JVM、GC](#8jvmgc)
    - [8.1、自己编译jdk](#81自己编译jdk)
    - [8.2、java内存管理机制](#82java内存管理机制)
    - [8.3、垃圾回收](#83垃圾回收)
    - [8.4、性能调优](#84性能调优)
    - [8.5、类加载机制](#85类加载机制)
    - [8.6、高并发](#86高并发)
- [函数式编程](#函数式编程)
    - [Lambda](#lambda)
        - [Lambda表达式语法](#lambda表达式语法)
        - [在哪里以及如何使用Lambda](#在哪里以及如何使用lambda)
    - [方法引用](#方法引用)
        - [构建方法引用](#构建方法引用)
        - [复合Lambda表达式 ](#复合lambda表达式 )
    - [流(java.util.stream.Stream)](#stream)
        - [流的基本操作](#流的基本操作)
        - [用流收集数据](#用流收集数据)
        - [Optional类(java.util.Optional<T>)](#optional类javautiloptional)     
- [设计模式](#设计模式)
    - [使用Lambda重构设计模式](#使用lambda重构设计模式)
        - [策略模式](#策略模式)
        - [工厂模式](#工厂模式)
        - [观察者模式](#观察者模式)
        - [模板方法](#模板方法)
        - [责任链模式](#责任链模式)
    - [单例模式](https://github.com/Panl99/demo/blob/master/src/main/java/com/outman/demo/leetcode/Singleton.java)
- [定时任务：ScheduledThreadPoolExecutor](https://github.com/Panl99/demo/blob/master/src/main/java/com/outman/demo/leetcode/ScheduledThreadPoolExecutorDemo.java)
- [异步非阻塞：CompletableFuture](https://github.com/Panl99/demo/blob/master/src/main/java/com/outman/demo/leetcode/CompletableFutureDemo.java)

# 目录
- [一、Java基础](#一、Java基础)
    - [1、Java基础](#1、Java基础)
        - [1.1、String常用方法](#1.1、String常用方法)
        - [1.2、流程控制-×](#1.2、流程控制)
        
- [日期和时间](#日期和时间)
    -[标准库API](#标准库API)
        -[java.util.Date](#java.util.Date)
    
    - [2、集合](#2、集合)
    - [3、I/O](#3、I/O)
    - [4、面向对象-×](#4、面向对象)
    - [5、异常处理-×](#5、异常处理)
    - [6、多线程-×](#6、多线程)
    - [7、泛型-×](#7、泛型)
    - [8、JVM、GC](#8、JVM、GC)
        - [8.1、自己编译jdk](#8.1、自己编译jdk)
        - [8.2、java内存管理机制](#8.2、java内存管理机制)
        - [8.3、垃圾回收](#8.3、垃圾回收)
        - [8.4、性能调优](#8.4、性能调优)
        - [8.5、类加载机制](#8.5、类加载机制)
        - [8.6、高并发](#8.6、高并发)
    - [9、正则-×](#9、正则)
    
- [函数式编程](#函数式编程)
    - [Lambda](#Lambda)
        - [Lambda表达式语法](#Lambda表达式语法)
        - [在哪里以及如何使用Lambda](#在哪里以及如何使用Lambda)
        - [编译器对Lambda做类型检查、类型推断、限制-×](#编译器对Lambda做类型检查、类型推断、限制)
    - [方法引用](#方法引用)
        - [构建方法引用](#构建方法引用)
        - [复合Lambda表达式 ](#复合Lambda表达式 )
    - [流(java.util.stream.Stream)](#Stream)
        - [流的基本操作](#流的基本操作)
        - [用流收集数据](#用流收集数据)
        - [并行流处理数据-×](#并行流处理数据)
        - [Optional类(java.util.Optional<T>)](#Optional类(java.util.Optional<T>))
    
- [12、异步编程-×](#异步编程)
- [序列化，反序列化-×](#序列化，反序列化)
- [网络-×](#网络)
- [数据结构-×](#数据结构)
- [反射-×](#反射)

- [设计模式](#设计模式)
    - [使用Lambda重构设计模式](#使用Lambda重构设计模式)
        - [策略模式](#策略模式)
        - [工厂模式](#工厂模式)
        - [观察者模式](#观察者模式)
        - [模板方法](#模板方法)
        - [责任链模式](#责任链模式)
- [N、解析文件-×](#N、解析文件)
    - [解析json-×](#解析json)
    - [解析xml-×](#解析xml)
    - [解析Excel-×](#解析Excel)

[返回目录](#目录)

[github-toc](#github-toc)

# 一、Java基础
## 1、Java基础
### 1.1、String常用方法
方法|返回值类型|描述|实例
|---|---|---|---|
|.charAt(int index)|char|||
|.chars()|IntStream|||
|.codePointAt(int index)|int|||
|.codePointBefore(int index)|int|||
|.codePointCount(int beginIndex, int endIndex)|int|||
|.codePoints()|IntStream|||
|.compareTo(String anotherString)|int|||
|.compareToIgnoreCase(String str)|int|||
|.concat(String str)|String|||
|.contains(CharSequence s)|boolean|||
|.contentEquals(CharSequence cs)|boolean|||
|.contentEquals(StringBuffer sb)|boolean|||
|.endsWith(String suffix)|boolean|||
|.equals(Object anObject)|boolean|判断两个字符串是否相等|"abc".equals("abc"),//true|
|.equalsIgnoreCase(String anotherString)|boolean|忽略大小写判断两个字符串是否相等|"abc".equalsIgnoreCase("ABc"),//true|
|.getBytes()|byte[]|||
|.getBytes(Charset charset)|byte[]|||
|.getBytes(String charsetName)|byte[]|||
|.getChars(int srcBegin, int srcEnd, char dst[], int dstBegin)|void|||
|.getClass()|Class<?>|||
|.hashCode()|int|||
|.indexOf(int ch)|int|||
|.indexOf(int ch, int fromIndex)|int|||
|.indexOf(String str)|int|||
|.indexOf(String str, int fromIndex)|int|||
|.intern()|native|||
|.isEmpty()|boolean|判空，字符串长度是否为0|"".isEmpty(),//true|
|.lastIndexOf(int ch)|int|||
|.lastIndexOf(int ch, int fromIndex)|int|||
|.lastIndexOf(String str)|int|||
|.lastIndexOf(String str, int fromIndex)|int|||
|.length()|int|||
|.matches(String regex)|boolean|||
|.notify()|void|||
|.notifyAll()|void|||
|.offsetByCodePoints(int index, int codePointOffset)|int|||
|.regionMatches(int toffset, String other, int ooffset, int len)|boolean|||
|.regionMatches(boolean ignoreCase, int toffset, String other, int ooffset, int len)|boolean|||
|.replace(char oldChar, char newChar)|String|||
|.replace(CharSequence target, CharSequence replacement)|String|||
|.replaceAll(String regex, String replacement)|String|||
|.replaceFirst(String regex, String replacement)|String|||
|.split(String regex, int limit)|String[]|||
|.split(String regex)|String[]|||
|.startsWith(String prefix, int toffset)|boolean|||
|.startsWith(String prefix)|boolean|||
|.substring(int beginIndex)|String|||
|.substring(int beginIndex, int endIndex)|String|||
|.subSequence(int beginIndex, int endIndex)|CharSequence|||
|.toCharArray()|char[]|||
|.toString()|String|||
|.toLowerCase()|String|||
|.toLowerCase(Locale locale)|String|||
|.toUpperCase()|String|||
|.toUpperCase(Locale locale)|String|||
|.trim()|String|||
|.valueOf(Object obj)|String||
|.wait()|void|||
|.wait(long timeout, int nanos)|void|||
|//todo|||


#### 1.2、流程控制

# 日期和时间
## 标准库API
- java.util包：Date、Calendar、TimeZone
- java.time包(Java 8引入)：LocalDateTime、ZonedDateTime、ZoneId等
### java.util.Date
- 实际上存储了一个long类型的以毫秒表示的时间戳。
```java
    public static void main(String[] args) {
        // 获取当前时间:
        Date date = new Date();
        System.out.println(date.getYear() + 1900); // 必须加上1900
        System.out.println(date.getMonth() + 1); // 0~11，必须加上1
        System.out.println(date.getDate()); // 1~31，不能加1
        // 转换为GMT时区:
        System.out.println(date.toGMTString());
        // 转换为本地时区:
        System.out.println(date.toLocaleString());

        // 格式化输出：
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
    }
```

[返回目录](#目录)

## 2、集合
![集合框架](https://www.runoob.com/wp-content/uploads/2014/01/2243690-9cd9c896e0d512ed.gif)

[返回目录](#目录)

## 3、I/O

[返回目录](#目录)

## 4、面向对象
### 继承
### 多态
### 抽象
### 封装

[返回目录](#目录)

## 5、异常处理

## 6、多线程
`//8.6`
## 7、泛型

[返回目录](#目录)

## 8、JVM、GC
### 8.1、自己编译jdk 
- **获取OpenJDK 12源码：** [https://hg.openjdk.java.net/jdk/jdk12/](https://hg.openjdk.java.net/jdk/jdk12/)
```
1、访问网址，点击左边菜单中的“Browse”显示源码根目录
2、再点击左边的“zip”链接即可下载当前版本打包好的源码，到本地直接解压即可。
```
- **编译前提：** 
```
1、建议在Linux系统上编译，本次采用Ubuntu，可参考源码中的doc/building.html文档
2、建议采用64位操作系统，会编译出64位OpenJDK，编译32位添加参数--with-target-bits=32
3、需要2~4G内存，6~8G空闲磁盘空间
4、所有文件不要放在中文目录下
```
- **构建编译环境：**
```
1、安装GCC 7.8：sudo apt-get install build-essential
2、安装依赖第三方工具：
    FreeType：sudo apt-get install libfreetype6-dev
    CUPS：sudo apt-get install libcups2-dev
    X11：sudo apt-get install libx11-dev libxext-dev libxrender-dev libxrandr-dev libxtst-dev libxt-dev
    ALSA：sudo apt-get install libasound2-dev
    libffi：sudo apt-get install libffi-dev
    Autoconf：sudo apt-get install autoconf
3、假设要编译大版本号为N的JDK，还要另外准备一个大版本号至少为N-1的、已经编译好的JDK，用来编译java代码：安装OpenJDK 11：sudo apt-get install openjdk-11-jdk
```
- **进行编译**
```
1、编译参数说明：
    参数使用：bash configure [options]
    查看编译参数：bash configure --help
    --with-debug-level=<level>：设置编译的级别，可选值为release、fastdebug、slowde-bug，默认release，越往后进行的优化措施就越少，带的调试信息就越多
    --enable-debug：等效于--with-debug-level=fastdebug
    --with-native-debug-symbols=<method>：确定调试符号信息的编译方式，可选值为none、internal、external、zipped
    --with-version-string=<string>：设置编译JDK的版本号，这个参数还有--with-version-<part>=<value>的形式，其中part可以是pre、opt、build、major、minor、security、patch之一，用于设置版本号的某一个部分。
    --with-jvm-variants=<variant>[，<variant>...]：编译特定模式（Variants）的HotSpot虚拟机，可以多个模式并存，可选值为server、client、minimal、core、zero、custom
    --with-jvm-features=<feature>[，<feature>...]：针对--with-jvm-variants=custom时的自定义虚拟机特性列表（Features），可以多个特性并存，由于可选值较多，请参见help命令输出
    --with-target-bits=<bits>：指明要编译32位还是64位的Java虚拟机
    --with-<lib>=<path>：用于指明依赖包的具体路径，通常使用在安装了多个不同版本的Bootstrap JDK和依赖包的情况。其中lib的可选值包括boot-jd、freetype、cups、x、alsa、libffi、jtreg、libjpeg、giflib、libpng、lcms、zlib
    --with-extra-<flagtype>=<flags>：用于设定C、C++和Java代码编译时的额外编译器参数，其中flagtype可选值为cflags、cxxflags、ldflags，分别代表C、C++和Java代码的参数
    --with-conf-name=<name>：指定编译配置名称
2、编译FastDebug版、仅含Server模式的HotSpot虚拟机：bash configure --enable-debug --with-jvm-variants=server
3、在configure命令以及后面的make命令的执行过程中，会在“build/配置名称”目录下产生如下目录结构。如果多次编译，或者目录结构成功产生后又再次修改了配置，必须先使用“make clean”和“make dist-clean”命令清理目录，才能确保新的配置生效。编译产生的目录结构以及用途如下所示：
    buildtools/：用于生成、存放编译过程中用到的工具
    hotspot/：HotSpot虚拟机编译的中间文件
    images/：使用make *-image产生的镜像存放在这里
    jdk/：编译后产生的JDK就放在这里
    support/：存放编译时产生的中间文件
    test-results/：存放编译后的自动化测试结果
    configure-support/：这三个目录是存放执行configure、make和test的临时文件
    make-support/
    test-support/
4、依赖检查通过后便可以输入“make images”执行整个OpenJDK编译了，这里“images”是“productimages”编译目标（Target）的简写别名，这个目标的作用是编译出整个JDK镜像，除了“productimages”以外，其他编译目标还有：
    hotspot：只编译HotSpot虚拟机
    hotspot-<variant>：只编译特定模式的HotSpot虚拟机
    docs-image：产生JDK的文档镜像
    test-image：产生JDK的测试镜像
    all-images：相当于连续调用product、docs、test三个编译目标
    bootcycle-images：编译两次JDK，其中第二次使用第一次的编译结果作为Bootstrap JDK
    clean：清理make命令产生的临时文件
    dist-clean：清理make和configure命令产生的临时文件
5、编译完成之后，进入OpenJDK源码的“build/配置名称/jdk”目录下就可以看到OpenJDK的完整编译结果了，把它复制到JAVA_HOME目录，就可以作为一个完整的JDK来使用
```

[返回目录](#目录)

### 8.2、java内存管理机制
- **java内存区域划分**

|运行时数据区|用途|生命周期|异常|
|---|---|---|---|
|程序计数器|字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令，线程私有|随线程|无|
|虚拟机栈|Java方法执行的线程内存模型：每个方法被执行的时候，Java虚拟机都会同步创建一个栈帧用于存储***局部变量表、操作数栈、动态连接、方法出口等信息***。每一个方法被调用直至执行完毕的过程，就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。线程私有|与线程相同|如果线程请求的栈深度大于虚拟机所允许的深度，将抛出StackOverflowError异常；如果Java虚拟机栈容量可以动态扩展，当栈扩展时无法申请到足够的内存会抛出OutOfMemoryError异常。|
|本地方法栈|与虚拟机栈作用类似，区别为虚拟机栈为虚拟机执行Java方法（也就是字节码）服务，而本地方法栈则是为虚拟机使用到的本地方法服务。|随线程|同虚拟机栈，会在栈深度溢出或者栈扩展失败时分别抛出StackOverflowError和OutOfMemoryError异常|
|Java堆|虚拟机所管理的最大的内存区域，用于存放***对象的实例***，被所有线程共享。|虚拟机启动时创建|如果在Java堆中没有内存完成实例分配，并且堆也无法再扩展时，Java虚拟机将会抛出OutOfMemoryError异常|
|方法区|用于存储***已被虚拟机加载的类型信息、常量、静态变量、即时编译器编译后的代码缓存等数据***，被所有线程共享| |无法分配新内存需求时抛出OutOfMemoryError异常|
|运行时常量池|属于方法区，用于存放编译期生成的各种字面量与符号引用，在类加载后存放到方法区的运行时常量池中| |无法申请到内存时会抛出OutOfMemoryError异常|

**直接内存**：非虚拟机运行时数据区的一部分，内存大小分配不受Java堆大小限制，但会受本机总内存大小及处理器寻址空间限制。容易在设置-Xmx等参数信息时忽略直接内存，使得各内存区域和大于物理内存限制，导致动态扩展时出现OutOfMemoryError异常。来源：NIO类会使用Native函数库直接分配堆外内存，然后通过一个存储在Java堆里面的DirectByteBuffer对象作为这块内存的引用进行操作，来避免在Java堆和Native堆中来回复制数据以提升性能。

**对象的创建、内存分布、访问定位：**
```
1、创建对象：
    当JVM遇到一条字节码new指令时，首先将去检查这个指令的参数是否能在常量池中定位到一个类的符号引用，并且检查这个符号引用代表的类是否已被加载、解析和初始化过。如果没有，那必须先执行相应的类加载过程；
    在类加载检查通过后，JVM将为新生对象分配内存，内存大小在类加载完后就已确定，由于Java堆的内存是占用、空闲混在一起，所以JVM维护一个列表来记录内存块是否可用，在分配时找一块足够大的划分给对象实例，这种分配方式叫做“空闲列表”；如果java堆的内存比较规整，可以使用“指针碰撞”方式来分配；java堆是否规整由垃圾收集器是否有空间压缩整理能力决定；
    在并发情况下，分配内存保证线程安全2种方式：1、对分配内存空间的动作进行同步处理：JVM采用CAS配上失败重试来保证更新操作的原子性。2、把内存分配的动作按照线程划分在不同的空间中进行：“本地线程分配缓冲”：每个线程在java堆中预先分配一小块内存，哪个线程需要分配内存，就在那个线程的本地缓冲区进行分配，本地缓冲区用完后再分配新的缓存区时才进行同步锁定；JVM通过-XX: +/-UseTLAB参数来设置是否使用本地线程分配缓冲策略；
    内存分配完后，JVM需将分配到的内存空间（不包括对象头）初始化为零；之后对对象头进行设置，是否启用偏向锁等；
    new指令之后会接着执行<init>()方法，按照程序员的意愿对对象进行初始化。
2、内存分布：
    在HotSpot虚拟机中，对象在堆内存中的分布可以划分为3部分：对象头（Header）、实例数据（Instance Data）、对齐填充（Padding）；
    对象头包括两类信息:
        (1) 用于存储对象自身的运行时数据，如：hash码、GC分代年龄、锁状态标志、线程持有的所、偏向线程ID、偏向时间戳等
        (2) 类型指针，即对象指向它的类型元数据的指针，JVM通过这个指针来确定该对象是哪个类的实例
    实例数据：真正存储的有效信息，即代码中定义的各种类型字段内容，存储顺序受JVM分配策略参数-XX: FieldsAllocationStyle和字段在java源码中定义顺序影响
    对齐填充：非必然存在，无特别含义，仅起到占位符作用
3、访问定位：
    java程序通过栈上的reference数据来操作堆上的具体对象，访问方式可分为：使用句柄、直接指针
    使用句柄访问，java堆可能会划分一块内存作为句柄池，reference中存储的就是对象的句柄地址，句柄中包含对象实例数据和类型数据各自具体的地址信息
    使用直接指针访问，java堆中对象的内存分布就需要考虑如何放置访问类型数据的相关信息，reference中存储的直接是对象地址
    好处：
        句柄访问好处就是reference中存储的是稳定句柄地址，在对象被移动时只会改变句柄中的实例数据指针，而reference本身不需要被修改
        直接指针访问好处就是速度更快，它节省了一次指针定位的时间开销
```

- **内存溢出**
```
1、Java堆溢出
    VM Args：-Xms20M -Xmx20M -XX:+HeapDumpOnOutOfMemoryError
        将堆的最小值-Xms参数与最大值-Xmx参数设置为一样即可避免堆自动扩展，参数-XX：+HeapDumpOnOutOf-MemoryError可以让虚拟机在出现内存溢出异常的时候Dump出当前的内存堆转储快照以便进行事后分析
    要解决这个内存区域的异常，首先通过内存映像分析工具（如Eclipse MemoryAnalyzer）对Dump出来的堆转储快照进行分析。第一步首先应确认内存中导致OOM的对象是否是必要的，也就是要先分清楚到底是出现了内存泄漏（Memory Leak）还是内存溢出（MemoryOverflow）
        如果是内存泄漏，可进一步通过工具查看泄漏对象到GC Roots的引用链，找到泄漏对象是通过怎样的引用路径、与哪些GC Roots相关联，才导致垃圾收集器无法回收它们，根据泄漏对象的类型信息以及它到GC Roots引用链的信息，一般可以比较准确地定位到这些对象创建的位置，进而找出产生内存泄漏的代码的具体位置。
        如果不是内存泄漏，换句话说就是内存中的对象确实都是必须存活的，那就应当检查Java虚拟机的堆参数（-Xmx与-Xms）设置，与机器的内存对比，看看是否还有向上调整的空间。再从代码上检查是否存在某些对象生命周期过长、持有状态时间过长、存储结构设计不合理等情况，尽量减少程序运行期的内存消耗。
2、虚拟机栈和本地方法栈溢出
    栈容量只能由-Xss参数来设定，是由于HotSpot虚拟机中并不区分虚拟机栈和本地方法栈，因此对于HotSpot来说，-Xoss参数（设置本地方法栈大小）虽然存在，但实际上是没有任何效果的。
    无论是由于栈帧太大还是虚拟机栈容量太小，当新的栈帧内存无法分配的时候，HotSpot虚拟机抛出的都是StackOverflowError异常。
    操作系统分配给每个进程的内存是有限制的，HotSpot虚拟机提供了参数可以控制Java堆和方法区这两部分的内存的最大值，那剩余的内存即为操作系统限制容量减去最大堆容量，再减去最大方法区容量，由于程序计数器消耗内存很小，可以忽略掉，如果把直接内存和虚拟机进程本身耗费的内存也去掉的话，剩下的内存就由虚拟机栈和本地方法栈来分配了。因此为每个线程分配到的栈内存越大，可以建立的线程数量自然就越少，建立线程时就越容易把剩下的内存耗尽，
3、方法区和运行时常量池溢出
    在JDK 8以后，永久代便完全退出了历史舞台，元空间作为其替代者登场
    -XX：MaxMetaspaceSize：设置元空间最大值，默认是-1，即不限制，或者说只受限于本地内存大小。
    -XX：MetaspaceSize：指定元空间的初始空间大小，以字节为单位，达到该值就会触发垃圾收集进行类型卸载，同时收集器会对该值进行调整：如果释放了大量的空间，就适当降低该值；如果释放了很少的空间，那么在不超过-XX：MaxMetaspaceSize（如果设置了的话）的情况下，适当提高该值。
    -XX：MinMetaspaceFreeRatio：作用是在垃圾收集之后控制最小的元空间剩余容量的百分比，可减少因为元空间不足导致的垃圾收集的频率。类似的还有-XX：Max-MetaspaceFreeRatio，用于控制最大的元空间剩余容量的百分比。
4、本机直接内存溢出
    可通过-XX：MaxDirectMemorySize参数来指定，如果不去指定，则默认与Java堆最大值（由-Xmx指定）一致
```

- **JVM性能监控、故障处理工具**

|工具|功能|命令|参数|
|---|---|---|---|
|jps|JVM Process Status Tool：可以列出正在运行的虚拟机进程，并显示虚拟机执行主类（Main Class，main()函数所在的类）名称以及这些进程的本地虚拟机唯一ID|jps [options] [hostid]|-q，-m，-l，-v|
|jstat|JVM Statistics Monitoring Tool：用于监视虚拟机各种运行状态信息的命令行工具，可以显示本地或者远程虚拟机进程中的类加载、内存、垃圾收集、即时编译等运行时数据|jstat [ option vmid [interval[s/ms] [count]] ]|-class，-gc，-gcnew，-gcold，-gcpermcapacity，-compiler，...|
|jinfo|Configuration Info for Java：实时查看和调整虚拟机各项参数|jinfo [ option ] pid|-flag|
|jmap|Memory Map for Java：用于生成堆转储快照（一般称为heapdump或dump文件），还可以查询finalize执行队列、Java堆和方法区的详细信息，如空间使用率、当前用的是哪种收集器等。|jmap [ option ] vmid|-dump，-finalizerinfo，-heap，-histo，-permstat，-F|
|jhat|JVM Heap Analysis Tool：与jmap搭配使用，来分析jmap生成的堆转储快照。|||
|jstack|Stack Trace for Java：用于生成虚拟机当前时刻的线程快照（一般称为threaddump或者javacore文件）|jstack [ option ] vmid|-F，-l，-m|

[返回目录](#目录)

### 8.3、垃圾回收
- **垃圾回收**
```
1、哪些内存需要回收？
    Java堆和方法区动态分配的内存
    判断堆中的对象是否已死：
        (1) 引用计数算法（主流JVM未使用）：在对象中添加一个引用计数器，每当有一个地方引用它时，计数器值就加一；当引用失效时，计数器值就减一；任何时刻计数器为零的对象就是不可能再被使用的。
        (2) 可达性分析算法（java使用）：通过一系列称为“GC Roots”的根对象作为起始节点集，从这些节点开始，根据引用关系向下搜索，搜索过程所走过的路径称为“引用链”，如果某个对象到GC Roots间没有任何引用链相连，即从GC Roots到这个对象不可达时，则证明此对象是不可能再被使用的。
            java中固定可作为GC Roots的对象包括：
                在虚拟机栈（栈帧中的本地变量表）中引用的对象，譬如各个线程被调用的方法堆栈中使用到的参数、局部变量、临时变量等。
                在方法区中类静态属性引用的对象，譬如Java类的引用类型静态变量。
                在方法区中常量引用的对象，譬如字符串常量池（String Table）里的引用。
                在本地方法栈中JNI（即通常所说的Native方法）引用的对象。
                Java虚拟机内部的引用，如基本数据类型对应的Class对象，一些常驻的异常对象（比如NullPointExcepiton、OutOfMemoryError）等，还有系统类加载器。
                所有被同步锁（synchronized关键字）持有的对象。
                反映Java虚拟机内部情况的JMXBean、JVMTI中注册的回调、本地代码缓存等。
    对象死亡的两次标记：
        如果对象在进行可达性分析后发现没有与GC Roots相连接的引用链，那它将会被第一次标记，随后进行一次筛选，筛选的条件是此对象是否有必要执行finalize()方法。假如对象没有覆盖finalize()方法，或者finalize()方法已经被虚拟机调用过，那么虚拟机将这两种情况都视为“没有必要执行”。
        如果这个对象被判定为确有必要执行finalize()方法，那么该对象将会被放置在一个名为F-Queue的队列之中，并在稍后由一条由虚拟机自动建立的、低调度优先级的Finalizer线程去执行它们的finalize()方法。稍后收集器将对F-Queue中的对象进行第二次小规模的标记，对象是否真正死亡被回收取决于在finalize()方法中是否重新与引用链上的对象建联拯救自己。
            任何一个对象的finalize()方法都只会被系统自动调用一次，对象第二次面临回收时finalize()方法不会执行，自救失败对象会被回收。
            所以应避免使用finalize()方法来拯救对象。
            “关闭外部资源”请使用try-finally方式。

    方法区的垃圾回收
        主要包括：废弃的常量、无用类
            判断常量是否废弃与堆中的对象相同：没有任何字符串对象引用它，虚拟机中也没有其他地方引用它。
            判定一个类型是否属于无用类需要包含三方面：
                该类所有的实例都已经被回收，也就是Java堆中不存在该类及其任何派生子类的实例。
                加载该类的类加载器已经被回收，这个条件除非是经过精心设计的可替换类加载器的场景，如OSGi、JSP的重加载等，否则通常是很难达成的。
                该类对应的java.lang.Class对象没有在任何地方被引用，无法在任何地方通过反射访问该类的方法。
            关于是否要对类型进行回收，HotSpot虚拟机提供了-Xnoclassgc参数进行控制，还可以使用-verbose：class以及-XX：+TraceClass-Loading、-XX：+TraceClassUnLoading查看类加载和卸载信息
```
```
2、什么时候、怎么回收？
    垃圾收集算法：追踪式垃圾收集(Tracing GC)
    分代收集理论：收集器应该将Java堆划分出不同的区域，然后将回收对象依据其年龄（年龄即对象熬过垃圾收集过程的次数）分配到不同的区域之中存储。
        一般把java堆分为新生代、老年代，在新生代中，每次垃圾收集时都发现有大批对象死去，而每次回收后存活的少量对象，将会逐步晋升到老年代中存放
        但，对象不是孤立的，对象之间存在跨代调用，，
        如果某个新生代对象存在跨代引用，由于老年代对象难以消亡，该引用会使得新生代对象在收集时同样得以存活，进而在年龄增长之后晋升到老年代中，这时跨代引用也随即被消除了。
    (1)标记-清除算法：
        首先标记出所有需要回收的对象，在标记完成后，统一回收掉所有被标记的对象，也可以反过来，标记存活的对象，统一回收所有未被标记的对象。
        两个缺点：第一执行效率不稳定，标记和清除过程的执行效率随对象数量增长而降低；第二内存空间碎片化，标记、清除后会产生大量不连续内存碎片
    (2)标记-复制算法：
        它将可用内存按容量划分为大小相等的两块，每次只使用其中的一块。当这一块的内存用完了，就将还存活着的对象复制到另外一块上面，然后再把已使用过的内存空间一次清理掉。
        缺点：一、内存中存活大量的对象时会产生大量的内存间复制开销；二、可用内存缩小为原来一半
        可用于回收新生代
    (3)标记-整理算法：
        让所有存活的对象都向内存空间一端移动，然后直接清理掉边界以外的内存
        缺点：对象大量存活时，移动存活对象并更新对象引用极为沉重

    垃圾收集器：
        目标、特性、原理、使用场景
    新生代收集器：
        Serial收集器：单线程、客户端模式
        ParNew收集器：多线程、服务端模式、目前只能与CMS配合
        Parallel Scavenge收集器：多线程、标记-复制算法
    老年代收集器：
        Serial Old收集器：单线程、客户端模式、标记-整理算法
        Parallel Old收集器：多线程、标记-整理算法
        CMS（Concurrent Mark Sweep）：并发收集、低停顿；标记-清除算法、对处理器资源非常敏感、无法处理“浮动垃圾”
        
    G1（Garbage First收集器）：服务端、衡量标准不再是它属于哪个分代，而是哪块内存中存放的垃圾数量最多，回收收益最大
        初始标记-并发标记-最终标记-筛选回收      

    衡量垃圾收集器三要素：内存占用、吞吐量、延迟      
```
- **内存分配**
```
1、多数情况下，对象优先在新生代Eden区分配，当Eden区空间不足时，虚拟机将进行一次Minor GC
    -XX：+PrintGCDetails，该HotSpot虚拟机参数在JVM进行垃圾回收时打印内存回收日志，在进程退出时输出当前内存各区域分配情况。
    -XX：Survivor-Ratio=8，决定了新生代中Eden区与一个Survivor区的空间比例是8∶1
2、大对象直接进入老年代
    大对象：指需要大量连续内存空间的Java对象，如很长的字符串、元素数量巨多的数组。
    避免大对象：分配空间时容易导致还有不少内存，但却需要足够的连续空间安置 而提前触发垃圾收集；并且复制大对象需要高额内存开销。
    -XX：PretenureSizeThreshold，指定大于该设置值的对象直接在老年代分配，目的是避免在Eden区及两个Survivor区之间来回复制，产生大量的内存复制操作。（注意：参数只限于Serial和ParNew两款新生代收集器）
3、长期存活的对象将进入老年代
    -XX：MaxTenuringThreshold[=15]，对象晋升老年代的年龄阈值，对象在Survivor区中每熬过一次Minor GC，年龄就增加1岁，超过阈值会晋升到老年代中
4、动态对象年龄判断
    如果在Survivor空间中相同年龄所有对象大小的总和大于Survivor空间的一半，年龄大于或等于该年龄的对象就可以直接进入老年代

5、空间分配担保
    在Minor GC之前，JVM必须先检查老年代中最大可用的连续空间是否大于新生代中所有对象总空间，
        大于，进行Minor GC，
        小于，JVM会先检查-XX：HandlePromotionFailure[=true/false]设置的值是否允许担保失败，
            允许，继续检查老年代最大可用的连续空间是否大于历次晋升到老年代对象的平均大小，
                大于，进行一次Minor GC。
                小于，进行一次Full GC。
            不允许，进行一次Full GC。
```

[返回目录](#目录)

### 8.4、性能调优

- **大内存硬件上的程序部署策略**
```
背景：一个15万PV/日左右的在线文档类型网站，服务器的硬件为四路志强处理器、16GB物理内存，操作系统为64位CentOS 5.4，Resin作为Web服务器。
    1个虚拟机实例，使用-Xmx和-Xms参数将Java堆大小固定在12GB，垃圾收集停顿导致网站经常不定期出现长时间失去响应
    在该系统软硬件条件下，HotSpot虚拟机是以服务端模式运行，默认使用的是吞吐量优先收集器，回收12GB的Java堆，一次FullGC的停顿时间就高达14秒
    由于程序设计的原因，访问文档时会把文档从磁盘提取到内存中，导致内存中出现很多由文档序列化产生的大对象，这些大对象大多在分配时就直接进入了老年代，没有在Minor GC中被清理掉。这种情况下即使有12GB的堆，内存也很快会被消耗殆尽，由此导致每隔几分钟出现十几秒的停顿
解决：
    程序部署问题：过大的堆内存进行回收带来长时间停顿
        可以使用控制延迟为目标的Parallel Scavenge/Old收集器，但要把应用的Full GC频率控制到足够低，以便可以在深夜触发Full GC或重启服务器来清除内存
            控制Full GC频率关键是控制老年代的稳定，即大多数对象要符合“朝生夕灭”原则，生存时间不应太长，尤其不能有大批量、长生存的对象产生
        单JVM实例管理大内存还需要：
            如使用G1收集器增量回收、大内存需要64位JVM支持、应用程序稳定
        可以使用多个虚拟机建立逻辑集群来利用硬件资源：
            即在一台物理机上开启多个应用服务器进程，分别分配不同端口，在前端搭建一个负载均衡器以反向代理方式分配访问请求
            问题：节点竞争全局资源，如磁盘竞争-并发写操作容易导致I/O异常；不能高效利用某些资源池，如各节点都自建连接池；大量使用本地缓存的应用，会造成集群内存浪费
```
- **集群间同步导致内存溢出**
```
背景：一个基于B/S的MIS系统，硬件为两台双路处理器、8GB内存的HP小型机，应用中间件是WebLogic9.2，每台机器启动了3个WebLogic实例，构成一个6个节点的亲合式集群
    由于是亲和式集群，节点间没有session同步，但有需求要部分数据在节点间共享，便建立全局缓存JBossCache，之后不定期出现多次内存溢出问题
解决：
    先确保没有什么异常操作后，给服务配置-XX：+HeapDumpOnOutOfMemoryError参数来运行，结果发现堆转储快照中存在大量org.jgroups.protocols.pbcast.NAKACK对象。
        JBossCache是基于自家的JGroups进行集群间的数据通信，JGroups使用协议栈的方式来实现收发数据包的各种所需特性自由组合，数据包接收和发送时要经过每层协议栈的up()和down()方法，其中的NAKACK栈用于保障各个包的有效顺序以及重发。
        由于信息有传输失败需要重发的可能性，在确认所有注册在GMS（Group Membership Service）的节点都收到正确的信息前，发送的信息必须在内存中保留。而此MIS的服务端中有一个负责安全校验的全局过滤器，每当接收到请求时，均会更新一次最后操作时间，并且将这个时间同步到所有的节点中去，使得一个用户在一段时间内不能在多台机器上重复登录。在服务使用过程中，往往一个页面会产生数次乃至数十次的请求，因此这个过滤器导致集群各个节点之间网络交互非常频繁。当网络情况不能满足传输要求时，重发数据在内存中不断堆积，很快就产生了内存溢出。
    该问题既有JBossCache的缺陷，也有MIS系统实现方式上的缺陷。应避免使用JBossCache这种非集中式集群缓存来实现数据同步，可以频繁读操作，但不应频繁写操作带来较大的网络同步开销
```
- **堆外内存导致内存溢出**
```
背景：大量的NIO操作需要使用到直接内存
    直接内存：可通过-XX：MaxDirectMemorySize调整大小，内存不足时抛出OutOf-MemoryError或者OutOfMemoryError：Direct buffer memory。
```
- **外部命令导致系统缓慢**
```
背景：通过操作系统的mpstat工具发现处理器使用率很高，但是系统中占用绝大多数处理器资源的程序并不是该应用本身
    每个用户请求的处理都需要执行一个外部Shell脚本来获得系统的一些信息。执行这个Shell脚本是通过Java的Runtime.getRuntime().exec()方法来调用的。
        频繁调用时创建进程的开销较大，资源消耗严重。
            应该去掉shell脚本执行语句，使用java的API去获取相关信息
```
- **服务器虚拟机进程崩溃**
```
背景：集群节点的虚拟机进程频繁自动关闭，留下一个hs_err_pid###.log文件
    异常信息：
        java.net.SocketException: Connection reset
        at java.net.SocketInputStream.read(SocketInputStream.java:168)
        at java.io.BufferedInputStream.fill(BufferedInputStream.java:218)
        at java.io.BufferedInputStream.read(BufferedInputStream.java:235)
        at org.apache.axis.transport.http.HTTPSender.readHeadersFromSocket(HTTPSender.java:583)
        at org.apache.axis.transport.http.HTTPSender.invoke(HTTPSender.java:143)
        ... 99 more
    由于MIS系统的用户多，待办事项变化很快，为了不被OA系统速度拖累，使用了异步的方式调用Web服务，但由于两边服务速度的完全不对等，时间越长就累积了越多Web服务没有调用完成，导致在等待的线程和Socket连接越来越多，最终超过虚拟机的承受能力后导致虚拟机进程崩溃
解决：
    通知OA门户方修复无法使用的集成接口，并将异步调用改为生产者/消费者模式的消息队列实现
```
- **不恰当的数据结构导致内存占用过大**
```
背景：一个后台RPC服务器，使用64位Java虚拟机，内存配置为-Xms4g-Xmx8g-Xmn1g，使用ParNew加CMS的收集器组合。
    平时对外服务的Minor GC时间约在30毫秒以内，完全可以接受。但业务上需要每10分钟加载一个约80MB的数据文件到内存进行数据分析，这些数据会在内存中形成超过100万个HashMap<Long，Long>Entry，在这段时间里面Minor GC就会造成超过500毫秒的停顿
    在分析数据文件期间，800MB的Eden空间很快被填满引发垃圾收集，但Minor GC之后，新生代中绝大部分对象依然是存活的。我们知道ParNew收集器使用的是复制算法，这个算法的高效是建立在大部分对象都“朝生夕灭”的特性上的，如果存活对象过多，把这些对象复制到Survivor并维持这些对象引用的正确性就成为一个沉重的负担，因此导致垃圾收集的暂停时间明显变长。
解决：
    不修改程序，从GC调优角度解决：（治标）
        可以考虑直接将Survivor空间去掉（加入参数-XX：SurvivorRatio=65536、-XX：MaxTenuringThreshold=0或者-XX：+Always-Tenure），让新生代中存活的对象在第一次Minor GC后立即进入老年代，等到Major GC的时候再去清理它们。
    修改程序：（治本）
        分析HashMap<Long，Long>空间效率，两个长整型实际存放数据占2*8=16byte，但实际耗费内存：(Long(24byte)×2)+Entry(32byte)+HashMapRef(8byte)=88byte，空间效率为16字节/88字节=18%，这确实太低
```
- **由安全点导致长时间停顿**
```
背景：一个比较大的承担公共计算任务的离线HBase集群，运行在JDK 8上，使用G1收集器。
    每天都有大量的MapReduce或Spark离线分析任务对其进行访问，同时有很多其他在线集群Replication过来的数据写入，因为集群读写压力较大，而离线分析任务对延迟又不会特别敏感，所以将-XX：MaxGCPauseMillis参数设置到了500毫秒。不过运行一段时间后发现垃圾收集的停顿经常达到3秒以上，而且实际垃圾收集器进行回收的动作就只占其中的几百毫秒
解决：    
    加入参数-XX：+PrintSafepointStatistics和-XX：PrintSafepointStatisticsCount=1去查看安全点日志
        日志显示当前虚拟机的操作（VM Operation，VMOP）是等待所有用户线程进入到安全点，但是有两个线程特别慢，导致发生了很长时间的自旋等待，所以垃圾收集线程无法开始工作，只能空转（自旋）等待。
            第一步把这两个慢的线程找出来：添加-XX：+SafepointTimeout和-XX：SafepointTimeoutDelay=2000两个参数，让虚拟机在等到线程进入安全点的时间超过2000毫秒时就认定为超时，这样就会输出导致问题的线程名称
        最终查明导致这个问题是HBase中一个连接超时清理的函数，由于集群会有多个MapReduce或Spark任务进行访问，而每个任务又会同时起多个Mapper/Reducer/Executer，其每一个都会作为一个HBase的客户端，这就导致了同时连接的数量会非常多。
            更为关键的是，清理连接的索引值就是int类型，所以这是一个可数循环，HotSpot不会在循环中插入安全点。当垃圾收集发生时，如果RpcServer的Listener线程刚好执行到该函数里的可数循环时，则必须等待循环全部跑完才能进入安全点，此时其他线程也必须一起等着，所以从现象上看就是长时间的停顿
    解决：把循环索引的数据类型从int改为long即可，（需要掌握安全点和垃圾收集的知识）
```

[返回目录](#目录)

### 8.5、类加载机制
- **类文件结构**
```
//TODO 6.3
```
- **虚拟机类加载机制**
```
1、类加载时机
  一个类型从被加载到虚拟机内存中开始，到卸载出内存为止，它的整个生命周期将会经历加载（Loading）、验证（Verification）、准备（Preparation）、解析（Resolution）、初始化（Initialization）、使用（Using）和卸载（Unloading）七个阶段，其中验证、准备、解析三个部分统称为连接（Linking）。
    加载：类加载时机由虚拟机自由把握
    初始化：6种情况下必须立即对类进行初始化
        (1) 遇到new、getstatic、putstatic或invokestatic这四条字节码指令时，如果类型没有进行过初始化，则需要先触发其初始化阶段。
            常见java场景：使用new实例化对象；读取、设置一个类型的静态字段（已在编译时放到常量池的静态字段除外）；调用一个类的静态方法。
        (2) 使用java.lang.reflect包的方法对类型进行反射调用的时候，如果类型没有进行过初始化，则需要先触发其初始化。
        (3) 当初始化类的时候，如果发现其父类还没有进行过初始化，则需要先触发其父类的初始化。
        (4) 当虚拟机启动时，用户需要指定一个要执行的主类（包含main()方法的那个类），虚拟机会先初始化这个主类。
        (5) 当使用JDK 7新加入的动态语言支持时，如果一个java.lang.invoke.MethodHandle实例最后的解析结果为REF_getStatic、REF_putStatic、REF_invokeStatic、REF_newInvokeSpecial四种类型的方法句柄，并且这个方法句柄对应的类没有进行过初始化，则需要先触发其初始化。
        (6) 当一个接口中定义了JDK 8新加入的默认方法（被default关键字修饰的接口方法）时，如果有这个接口的实现类发生了初始化，那该接口要在其之前被初始化。
      例：对于只static修饰的静态字段，只有直接定义这个字段的类才会被初始化，通过其子类来引用父类中定义的静态字段，只会触发父类的初始化而不会触发子类的初始化。
        static final修饰的常量，直接被引用时定义它的类没有初始化，是因为在编译阶段该常量值已被放入常量池中，已转化为调用类对常量池的引用。
```
```
2、类加载过程
    加载：JVM的三个动作
        (1) 通过一个类的全限定名来获取定义此类的二进制字节流。（类加载器）
        (2) 将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构。
        (3) 在内存中生成一个代表这个类的java.lang.Class对象，作为方法区这个类的各种数据的访问入口。
      非数组类的加载阶段既可以使用Java虚拟机里内置的引导类加载器来完成，也可以由用户自定义的类加载器去完成，开发人员通过定义自己的类加载器去控制字节流的获取方式（重写一个类加载器的findClass()或loadClass()方法），实现根据自己的想法来赋予应用程序获取运行代码的动态性。
      数组类的加载阶段是由JVM直接在内存中动态构造出来的，但数组类的元素类型仍需类加载器来完成加载。
    验证：确保Class文件的字节流中所包含的信息符合《java虚拟机规范》要求，不会危害虚拟机自身安全
        (1) 文件格式验证：验证字节流是否符合Class文件格式的规范，并且能被当前版本的虚拟机处理
        (2) 元数据验证：对字节码描述的信息进行语义分析，以保证其描述的信息符合《Java语言规范》的要求
        (3) 字节码验证：通过数据流分析和控制流分析，确定程序语义是合法的、符合逻辑的
        (4) 符号引用验证：发生在虚拟机将符号引用转化(解析阶段)为直接引用时，可以看作是对类自身以外（常量池中的各种符号引用）的各类信息进行匹配性校验，通俗来说就是，该类是否缺少或者被禁止访问它依赖的某些外部类、方法、字段等资源
    准备：正式为类中定义的变量（即静态变量，被static修饰的变量）分配内存并设置类变量初始值的阶段
        进行内存分配的仅包括类变量，而不包括实例变量，实例变量将会在对象实例化时随着对象一起分配在Java堆中
    解析：是Java虚拟机将常量池内的符号引用替换为直接引用的过程
        符号引用（Symbolic References）：符号引用以一组符号来描述所引用的目标，符号可以是任何形式的字面量，只要使用时能无歧义地定位到目标即可
        直接引用（Direct References）：直接引用是可以直接指向目标的指针、相对偏移量或者是一个能间接定位到目标的句柄
      解析动作主要针对类或接口、字段、类方法、接口方法、方法类型、方法句柄、调用点限定符 这7类符号引用进行
    初始化：JVM真正开始执行类中编写的Java程序代码，初始化阶段就是执行类构造器<clinit>()方法的过程
        <clinit>()方法是由编译器自动收集类中的所有类变量的赋值动作和静态语句块（static{}块）中的语句合并产生的，编译器收集的顺序是由语句在源文件中出现的顺序决定的，静态语句块中只能访问到定义在静态语句块之前的变量，定义在它之后的变量，在前面的静态语句块可以赋值，但是不能访问。
        <clinit>()方法不需要显式地调用父类构造器，Java虚拟机会保证在子类的<clinit>()方法执行前，父类的<clinit>()方法已经执行完毕。因此在Java虚拟机中第一个被执行的<clinit>()方法的类型肯定是java.lang.Object。
        由于父类的<clinit>()方法先执行，也就意味着父类中定义的静态语句块要优先于子类的变量赋值操作
        <clinit>()方法对于类或接口来说并不是必需的，如果一个类中没有静态语句块，也没有对变量的赋值操作，那么编译器可以不为这个类生成<clinit>()方法。
        接口中不能使用静态语句块，但仍然有变量初始化的赋值操作，因此接口与类一样都会生成<clinit>()方法。但接口与类不同的是，执行接口的<clinit>()方法不需要先执行父接口的<clinit>()方法，因为只有当父接口中定义的变量被使用时，父接口才会被初始化。此外，接口的实现类在初始化时也一样不会执行接口的<clinit>()方法。
        Java虚拟机必须保证一个类的<clinit>()方法在多线程环境中被正确地加锁同步，如果多个线程同时去初始化一个类，那么只会有其中一个线程去执行这个类的<clinit>()方法，其他线程都需要阻塞等待，直到活动线程执行完毕<clinit>()方法。
```
```
3、类加载器
    实现“通过一个类的全限定名来获取定义此类的二进制字节流”这个动作的代码叫做--“类加载器”
    两个类是否相等：是否被同一个类加载器加载
    
    双亲委派模型：
        从JVM角度看，类加载器分两种：1、启动类加载器（属JVM一部分），由C++实现；2、其他类加载器，独立于JVM外部，由java实现，全部继承抽象类java.lang.ClassLoader
        从java开发角度看，Java一直保持着三层类加载器、双亲委派的类加载架构，jdk8以前
            3层类加载器：
                (1)启动类加载器：负责加载存放在<JAVA_HOME>\lib目录，或者被-Xbootclasspath参数所指定的路径中存放的，而且是Java虚拟机能够识别的（按照文件名识别，如rt.jar、tools.jar，名字不符合的类库即使放在lib目录中也不会被加载）类库加载到虚拟机的内存中。
                    启动类加载器无法被Java程序直接引用，用户在编写自定义类加载器时，如果需要把加载请求委派给引导类加载器去处理，那直接使用null代替即可，
                (2)扩展类加载器：这个类加载器是在类sun.misc.Launcher$ExtClassLoader中以Java代码的形式实现的。它负责加载<JAVA_HOME>\lib\ext目录中，或者被java.ext.dirs系统变量所指定的路径中所有的类库。
                (3)应用程序类加载器：这个类加载器由sun.misc.Launcher$AppClassLoader来实现。它负责加载用户类路径（ClassPath）上所有的类库。由于应用程序类加载器是ClassLoader类中的getSystem-ClassLoader()方法的返回值，所以有些场合中也称它为“系统类加载器”。
        双亲委派模型：各种类加载器之间的层次关系叫做类加载器的“双亲委派模型”：启动类加载器<-扩展类加载器<-应用程序类加载器<-自定义类加载器1,2
            双亲委派模型要求除了顶层的启动类加载器外，其余的类加载器都应有自己的父类加载器，通过组合关系来复用父加载器的代码。
            工作流程：
                如果一个类加载器收到类加载的请求，它首先不会自己去尝试加载这个类，而是把这个请求委派给父类加载器去完成，每个层次的类加载器都是如此，因此所有的加载请求最终都是传送到最顶层的启动类加载器中，只有当父加载器反馈自己无法完成这个加载请求（它的搜索范围中没有找到所需的类）时，子加载器才会尝试自己去完成加载。
            好处：类随类加载器一起具备了一种优先级关系，可以保证如Object类在各种类加载器环境中是同一个类，因为它都会由最顶端启动类加载器进行加载。
            实现：
                protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                    // 先检查请求的类是否已经被加载过了
                    Class c = findLoadedClass(name);
                    if (c == null) {
                        try {
                            if (parent != null) {
                                // 调用父加载器加载类
                                c = parent.loadClass(name, false);
                            } else {
                                // 使用启动类加载器作为父加载器
                                c = findBootstrapClassOrNull(name);
                            }
                        } catch (ClassNotFoundException e) {
                            // 如果父类加载器抛出ClassNotFoundException，说明父类加载器无法完成加载请求
                        }
                        if (c == null) {
                            // 在父类加载器无法加载时，再调用本身的findClass方法来进行类加载
                            c = findClass(name);
                        }
                    }
                    if (resolve) {
                        resolveClass(c);
                    }
                    return c;
                }
    OSGi实现模块化热部署的关键是它自定义的类加载器机制的实现，每一个程序模块（OSGi中称为Bundle）都有一个自己的类加载器，当需要更换一个Bundle时，就把Bundle连同类加载器一起换掉以实现代码的热替换。
        在OSGi环境下，类加载器不再双亲委派模型推荐的树状结构，而是进一步发展为更加复杂的网状结构，当收到类加载请求时，OSGi将按照下面的顺序进行类搜索：
            1）将以java.*开头的类，委派给父类加载器加载。
            2）否则，将委派列表名单内的类，委派给父类加载器加载。
            3）否则，将Import列表中的类，委派给Export这个类的Bundle的类加载器加载。
            4）否则，查找当前Bundle的ClassPath，使用自己的类加载器加载。
            5）否则，查找类是否在自己的Fragment Bundle中，如果在，则委派给Fragment Bundle的类加载器加载。
            6）否则，查找Dynamic Import列表的Bundle，委派给对应Bundle的类加载器加载。
            7）否则，类查找失败。
```

[返回目录](#目录)

### 8.6、高并发
系统运行耗费时间项：磁盘IO、网络通信、数据库访问  
TPS：每秒事务处理数，代表1秒内服务端平均能响应的请求总数
- **Java内存模型(JMM)**  
***内存模型：*** 可以理解为在特定的操作协议下，对特定的内存或高速缓存进行读写访问的过程抽象。    
    线程对所有变量的操作都在工作内存中进行，线程间无法直接访问对方工作内存，线程间通过主内存来传值。  
***内存间交互：*** 把一个变量从主内存拷贝到工作内存（顺序执行read+load），把变量从工作内存同步回主内存（顺序执行store+write）。  
    **8种内存访问操作：**
    - lock（锁定）：作用于主内存的变量，它把一个变量标识为一条线程独占的状态。
    - unlock（解锁）：作用于主内存的变量，它把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定。
    - read（读取）：作用于主内存的变量，它把一个变量的值从主内存传输到线程的工作内存中，以便随后的load动作使用。
    - load（载入）：作用于工作内存的变量，它把read操作从主内存中得到的变量值放入工作内存的变量副本中。
    - use（使用）：作用于工作内存的变量，它把工作内存中一个变量的值传递给执行引擎，每当虚拟机遇到一个需要使用变量的值的字节码指令时将会执行这个操作。
    - assign（赋值）：作用于工作内存的变量，它把一个从执行引擎接收的值赋给工作内存的变量，每当虚拟机遇到一个给变量赋值的字节码指令时执行这个操作。
    - store（存储）：作用于工作内存的变量，它把工作内存中一个变量的值传送到主内存中，以便随后的write操作使用。
    - write（写入）：作用于主内存的变量，它把store操作从工作内存中得到的变量的值放入主内存的变量中。  
    **8种内存访问操作规则：**
    - 不允许read和load、store和write操作之一单独出现，即不允许一个变量从主内存读取了但工作内存不接受，或者工作内存发起回写了但主内存不接受的情况出现。
    - 不允许一个线程丢弃它最近的assign操作，即变量在工作内存中改变了之后必须把该变化同步回主内存。
    - 不允许一个线程无原因地（没有发生过任何assign操作）把数据从线程的工作内存同步回主内存中。
    - 一个新的变量只能在主内存中“诞生”，不允许在工作内存中直接使用一个未被初始化（load或assign）的变量，换句话说就是对一个变量实施use、store操作之前，必须先执行assign和load操作。
    - 一个变量在同一个时刻只允许一条线程对其进行lock操作，但lock操作可以被同一条线程重复执行多次，多次执行lock后，只有执行相同次数的unlock操作，变量才会被解锁。
    - 如果对一个变量执行lock操作，那将会清空工作内存中此变量的值，在执行引擎使用这个变量前，需要重新执行load或assign操作以初始化变量的值。
    - 如果一个变量事先没有被lock操作锁定，那就不允许对它执行unlock操作，也不允许去unlock一个被其他线程锁定的变量。
    - 对一个变量执行unlock操作之前，必须先把此变量同步回主内存中（执行store、write操作）。

    ~~备注：基于理解难度和严谨性考虑，最新的JSR-133文档中，已经放弃了采用这8种操作去定义Java内存模型的访问协议，缩减为read、write、lock和unlock 4种（仅是描述方式改变了，Java内存模型并没有改变）。~~

***对于volatile型变量的特殊规则***  
```
当一个变量被定义成volatile之后，它将具备两项特性：    
1、保证此变量对所有线程的可见性（同步机制），这里的“可见性”是指当一条线程修改了这个变量的值，新值对于其他线程来说是可以立即得知的。而普通变量并不能做到这一点，普通变量的值在线程间传递时均需要通过主内存来完成。比如，线程A修改一个普通变量的值，然后向主内存进行回写，另外一条线程B在线程A回写完成了之后再对主内存进行读取操作，新变量值才会对线程B可见。  
    但：并非基于volatile变量的运算在并发下都是线程安全的，如java里面的“运算操作符”并非原子操作，这导致volatile变量的运算在并发下一样是不安全的。  
    由于volatile变量只能保证可见性，在不符合以下两条规则的运算场景中，仍然要通过加锁（使用synchronized、java.util.concurrent中的锁或原子类）来保证原子性：    
        - 运算结果并不依赖变量的当前值，或者能够确保只有单一的线程修改变量的值。    
        - 变量不需要与其他的状态变量共同参与不变约束。    
2、禁止指令重排序优化，普通的变量仅会保证在该方法的执行过程中所有依赖赋值结果的地方都能获取到正确的结果，而不能保证变量赋值操作的顺序与程序代码中的执行顺序一致。因为在同一个线程的方法执行过程中无法感知到这点
```
使用场景：
```
// 1、使用volatile变量来控制并发，当shutdown()方法被调用时，能保证所有线程中执行的doWork()方法都立即停下来。（线程可见性）
volatile boolean shutdownRequested;
public void shutdown() {
    shutdownRequested = true;
}
public void doWork() {
    while (!shutdownRequested) {
        // 代码的业务逻辑
    }
}

// 2、双锁检测单例模式（禁止指令重排）
public class Singleton {
    private volatile static Singleton instance;
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
    public static void main(String[] args) {
        Singleton.getInstance();
    }
}
```
Java内存模型中对volatile变量定义的特殊规则的定义：
```
//假定T表示一个线程，V和W分别表示两个volatile型变量，那么在进行read、load、use、assign、store和write操作时需要满足如下规则：
1、只有当线程T对变量V执行的前一个动作是load的时候，线程T才能对变量V执行use动作；并且，只有当线程T对变量V执行的后一个动作是use的时候，线程T才能对变量V执行load动作。线程T对变量V的use动作可以认为是和线程T对变量V的load、read动作相关联的，必须连续且一起出现。
    这条规则要求在工作内存中，每次使用V前都必须先从主内存刷新最新的值，用于保证能看见其他线程对变量V所做的修改。
2、只有当线程T对变量V执行的前一个动作是assign的时候，线程T才能对变量V执行store动作；并且，只有当线程T对变量V执行的后一个动作是store的时候，线程T才能对变量V执行assign动作。线程T对变量V的assign动作可以认为是和线程T对变量V的store、write动作相关联的，必须连续且一起出现。
    这条规则要求在工作内存中，每次修改V后都必须立刻同步回主内存中，用于保证其他线程可以看到自己对变量V所做的修改。
3、假定动作A是线程T对变量V实施的use或assign动作，假定动作F是和动作A相关联的load或store动作，假定动作P是和动作F相应的对变量V的read或write动作；与此类似，假定动作B是线程T对变量W实施的use或assign动作，假定动作G是和动作B相关联的load或store动作，假定动作Q是和动作G相应的对变量W的read或write动作。如果A先于B，那么P先于Q。
    这条规则要求volatile修饰的变量不会被指令重排序优化，从而保证代码的执行顺序与程序的顺序相同。
```
***针对long和double型变量的特殊规则***
```
Java内存模型要求lock、unlock、read、load、assign、use、store、write这八种操作都具有原子性，但是对于64位的数据类型（long和double），在模型中特别定义了一条宽松的规定：
    long和double的非原子性协定：允许虚拟机将没有被volatile修饰的64位数据的读写操作划分为两次32位的操作来进行，即允许虚拟机实现自行选择是否要保证64位数据类型的load、store、read和write这四个操作的原子性。
```

- **Java线程**  
```
Java线程调度： 指系统为线程分配处理器使用权的过程，调度主要方式分为：协同式线程调度、抢占式线程调度。
    协同式线程调度：线程的执行时间由线程本身来控制，线程把自己的工作执行完了之后，要主动通知系统切换到另外一个线程上去。
        好处：实现简单，切换操作对线程可见，一般没有线程同步问题。
        坏处：线程执行时间不可控，如果一个线程处理完后不告知系统切换线程，就会导致阻塞在那里。
    抢占式线程调度(java使用)：那么每个线程将由系统来分配执行时间，线程的切换不由线程本身来决定。
        好处：执行时间可控，不易阻塞
        Java中Thread::yield()方法可以主动让出执行时间；线程执行时间可通过设置线程优先级来完成
状态转换：
    Java定义了6种线程状态，在任意一个时间点中，一个线程只能有且只有其中的一种状态，并且可以通过特定的方法在不同状态之间转换
        新建（New）：创建后尚未启动的线程处于这种状态。
        运行（Runnable）：包括操作系统线程状态中的Running和Ready，也就是处于此状态的线程有可能正在执行，也有可能正在等待着操作系统为它分配执行时间。
        无限期等待（Waiting）：处于这种状态的线程不会被分配处理器执行时间，它们要等待被其他线程显式唤醒。以下方法会让线程陷入无限期的等待状态：
            没有设置Timeout参数的Object::wait()方法；
            没有设置Timeout参数的Thread::join()方法；
            LockSupport::park()方法。
        限期等待（Timed Waiting）：处于这种状态的线程也不会被分配处理器执行时间，不过无须等待被其他线程显式唤醒，在一定时间之后它们会由系统自动唤醒。以下方法会让线程进入限期等待状态：
            Thread::sleep()方法；
            设置了Timeout参数的Object::wait()方法；
            设置了Timeout参数的Thread::join()方法；
            LockSupport::parkNanos()方法；
            LockSupport::parkUntil()方法。
        阻塞（Blocked）：线程被阻塞了，“阻塞状态”与“等待状态”的区别是“阻塞状态”在等待着获取到一个排它锁，这个事件将在另外一个线程放弃这个锁的时候发生；而“等待状态”则是在等待一段时间，或者唤醒动作的发生。在程序等待进入同步区域的时候，线程将进入这种状态。
        结束（Terminated）：已终止线程的线程状态，线程已经结束执行。
    线程状态转换关系图：略
```
***线程安全***
```
定义：当多个线程同时访问一个对象时，如果不用考虑这些线程在运行时环境下的调度和交替执行，也不需要进行额外的同步，或者在调用方进行任何其他的协调操作，调用这个对象的行为都可以获得正确的结果，那就称这个对象是线程安全的。
实现：
    1、互斥同步(阻塞同步)：同步是指在多个线程并发访问共享数据时，保证共享数据在同一个时刻只被一条（或者是一些，当使用信号量的时候）线程使用。互斥是实现同步的一种手段，临界区、互斥量（Mutex）和信号量（Semaphore）都是常见的互斥实现方式。
        Java中最基本的互斥同步手段就是synchronized关键字，这是一种块结构（Block Structured）的同步语法。
            被synchronized修饰的同步块对同一条线程来说是可重入的。这意味着同一线程反复进入同步块也不会出现自己把自己锁死的情况。
            被synchronized修饰的同步块在持有锁的线程执行完毕并释放锁之前，会无条件地阻塞后面其他线程的进入。这意味着无法像处理某些数据库中的锁那样，强制已获取锁的线程释放锁；也无法强制正在等待锁的线程中断等待或超时退出。
        java.util.concurrent.locks.Lock接口是Java的另一种全新的互斥同步手段。基于Lock接口，用户能够以非块结构（Non-Block Structured）来实现互斥同步
            重入锁（ReentrantLock）是Lock接口最常见的一种实现，它与synchronized一样是可重入的。ReentrantLock与synchronized相比增加了一些高级功能，主要有以下三项：等待可中断、可实现公平锁、锁可以绑定多个条件。
                等待可中断：是指当持有锁的线程长期不释放锁的时候，正在等待的线程可以选择放弃等待，改为处理其他事情。可中断特性对处理执行时间非常长的同步块很有帮助。
                公平锁：是指多个线程在等待同一个锁时，必须按照申请锁的时间顺序来依次获得锁；而非公平锁则不保证这一点，在锁被释放时，任何一个等待锁的线程都有机会获得锁。synchronized中的锁是非公平的，ReentrantLock在默认情况下也是非公平的，但可以通过带布尔值的构造函数要求使用公平锁。
                锁绑定多个条件：是指一个ReentrantLock对象可以同时绑定多个Condition对象。在synchronized中，锁对象的wait()跟它的notify()或者notifyAll()方法配合可以实现一个隐含的条件，如果要和多于一个的条件关联的时候，就不得不额外添加一个锁；而ReentrantLock则无须这样做，多次调用newCondition()方法即可。
    2、非阻塞同步：不管风险，先进行操作，如果没有其他线程争用共享数据，那操作就直接成功了；如果共享的数据的确被争用，产生了冲突，那再进行其他的补偿措施，最常用的补偿措施是不断地重试，直到出现没有竞争的共享数据为止。
        这种乐观并发策略的实现不再需要把线程阻塞挂起。
    3、无同步方案：
        可重入代码(纯代码)、线程本地存储
```
~~***锁优化***~~

[返回目录](#目录)

## 9、正则
pattern



# 函数式编程
## Lambda
### Lambda表达式语法  
    (parameters) -> expression  
    (parameters) -> { statements; }
```
(String s) -> s.length()  //具有一个String类型的参数并返回一个int。Lambda没有return语句，因为已经隐含了return，使用return要加花括号
(Apple a) -> a.getWeight() > 150  //有一个Apple 类型的参数并返回一个boolean（苹果的重量是否超过150克）
(int x, int y) -> {  //具有两个int类型的参数而没有返回值（void返回）。注意Lambda表达式可以包含多行语句，这里是两行
    System.out.println("Result:");
    System.out.println(x+y);
}
() -> 42  //没有参数， 返回一个int
() -> void  //代表了参数列表为空，且返回void的函数。
(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight())  //具有两个Apple类型的参数，返回一个int：比较两个Apple的重量

布尔表达式： (List<String> list) -> list.isEmpty()  函数式接口：Predicate<List<String>>
创建对象： () -> new Apple(10)  函数式接口：Supplier<Apple>
消费一个对象：   函数式接口：Consumer<Apple>
(Apple a) -> {
    System.out.println(a.getWeight());
}
从一个对象中选择/抽取： (String s) -> s.length()  函数式接口：Function<String, Integer>或ToIntFunction<String>
组合两个值： (int a, int b) -> a * b  函数式接口：IntBinaryOperator
比较两个对象： (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight())  函数式接口：Comparator<Apple>或BiFunction<Apple, Apple, Integer>或ToIntBiFunction<Apple, Apple>
```

[返回目录](#目录)

### 在哪里以及如何使用Lambda
1. Lambda表达式可以被赋给一个变量
2. 传递给一个 接受函数式接口作为参数 的方法  
- **函数式接口上使用Lambda**  
    函数式接口：只定义一个抽象方法的接口。  
        Lambda表达式允许直接以内联的形式为函数式接口的抽象方法提供实现，并把整个表达式作为函数式接口的实例。（要比使用匿名内部类简洁许多）  
        可以使用@FunctionalInterface标注函数式接口，若被标记接口不是函数式接口，编译器会返回错误：“Multiple non-overriding abstract methods found in interface Foo”表示存在多个抽象方法。
```java
//使用Lambda
Runnable r1 = () -> System.out.println("Hello World 1");

//使用匿名类
Runnable r2 = new Runnable(){
    public void run(){
        System.out.println("Hello World 2");
    }
};

//运行
public static void process(Runnable r){
    r.run();
}
process(r1);
process(r2);
//利用直接传递的Lambda方式
process(() -> System.out.println("Hello World 3"));
```
```java
//资源处理实例：打开一个资源，做一些处理，然后关闭资源。
// 1、使用try-resource方式，只能读取文件第一行
public static String processFile() throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
        return br.readLine();
    }
}

// 2、灵活读取
//1：创建一个能匹配BufferedReader -> String，且可以抛出IOException异常的接口
@FunctionalInterface
public interface BufferedReaderProcessor {
    String process(BufferedReader b) throws IOException;
}
//2：将函数式接口作为参数，并处理BufferedReader对象
public static String processFile(BufferedReaderProcessor p) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
        return p.process(br);
    }
}
//3：接收BufferedReader参数并返回String的Lambda
String oneLine = processFile((BufferedReader br) -> br.readLine());
String twoLines = processFile((BufferedReader br) -> br.readLine() + br.readLine());
```
```java
Java中函数式接口：
Comparable、Runnable、Callable   Predicate、Consumer、Function

//1、java.util.function.Predicate<T>接口定义了一个名叫test的抽象方法，它接受泛型T对象，并返回一个boolean。
// 在需要表示一个涉及类型T的布尔表达式时，就可以使用这个接口。比如，可以定义一个接受String对象的Lambda表达式
//    @FunctionalInterface
//    public interface Predicate<T>{
//        boolean test(T t);
//    }
public static <T> List<T> filter(List<T> list, Predicate<T> p) {
    List<T> results = new ArrayList<>();
    for(T s: list){
        if(p.test(s)){
            results.add(s);
        }
    }
    return results;
}
Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicate);

//2、java.util.function.Consumer<T>定义了一个名叫accept的抽象方法，它接受泛型T的对象，没有返回（void）。
// 如果需要访问类型T的对象，并对其执行某些操作，就可以使用这个接口。比如，可以用它来创建一个forEach方法，接受一个Integers的列表，并对其中每个元素执行操作。
// 如下可以使用这个forEach方法，并配合Lambda来打印列表中的所有元素。
//@FunctionalInterface
//public interface Consumer<T>{
//    void accept(T t);
//}
public static <T> void forEach(List<T> list, Consumer<T> c){
    for(T i: list){
        c.accept(i);
    }
}
forEach(
        Arrays.asList(1,2,3,4,5), 
        (Integer i) -> System.out.println(i)
        );

//3、java.util.function.Function<T, R>接口定义了一个叫作apply的方法，它接受一个泛型T的对象，并返回一个泛型R的对象。
// 如果需要定义一个Lambda，将输入对象的信息映射到输出，就可以使用这个接口（比如提取苹果的重量，或把字符串映射为它的长度）。
// 如下利用它来创建一个map方法，以将一个String列表映射到包含每个String长度的Integer列表。
//@FunctionalInterface
//public interface Function<T, R>{
//    R apply(T t);
//}
public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
    List<R> result = new ArrayList<>();
    for(T s: list){
        result.add(f.apply(s));
    }
    return result;
}
// [7, 2, 6]
List<Integer> l = map(
        Arrays.asList("lambdas","in","action"),
        (String s) -> s.length()
);
```

[返回目录](#目录)

### 编译器对Lambda做类型检查、类型推断、限制
~~略~~

## 方法引用
使用方式：目标引用放在分隔符::前，方法的名称放在后面，方法不需要括号，因为没有实际调用这个方法。  
针对单一方法的Lambda
```java
//例如排序
先前：inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
之后：inventory.sort(comparing(Apple::getWeight)); //使用方法引用和java.util.Comparator.comparing；Apple::getWeight就是引用了Apple类中定义的方法getWeight。
```
|Lambda|等效方法引用|
|---|---|
|(Apple a) -> a.getWeight()|Apple::getWeight|
|() -> Thread.currentThread().dumpStack()|Thread.currentThread()::dumpStack|
|(str, i) -> str.substring(i)|String::substring|
|(String s) -> System.out.println(s)|System.out::println|
### 构建方法引用  
    (1) 指向静态方法的方法引用（例如Integer的parseInt方法，写作Integer::parseInt）。  
    (2) 指向任意类型实例方法的方法引用（例如String 的length 方法，写作String::length）。  
    (3) 指向现有对象的实例方法的方法引用（假设你有一个局部变量expensiveTransaction用于存放Transaction类型的对象，它支持实例方法getValue，那么你就可以写expensiveTransaction::getValue）  

[返回目录](#目录)

### 复合Lambda表达式  
```java
1、比较器复合：
    //对库存进行排序，比较苹果的重量
    Comparator<Apple> c = Comparator.comparing(Apple::getWeight);
    //对苹果按重量递减排序
    inventory.sort(comparing(Apple::getWeight).reversed());
    //两个苹果一样重时，进一步按国家排序
    inventory.sort(comparing(Apple::getWeight)
             .reversed()
             .thenComparing(Apple::getCountry));
2、谓词复合：negate(非)、and、or
and和or方法是按照在表达式链中的位置，从左向右确定优先级的。a.or(b).and(c)可以看作(a || b) && c。
    //不是红的苹果
    Predicate<Apple> notRedApple = redApple.negate();
    //红的并且大于150克的苹果
    Predicate<Apple> redAndHeavyApple = redApple.and(a -> a.getWeight() > 150);
    //或者是大于150克的红苹果，或者是绿苹果
    Predicate<Apple> redAndHeavyAppleOrGreen = redApple.and(a -> a.getWeight() > 150)
                                                       .or(a -> "green".equals(a.getColor()));
3、函数复合：
andThen方法会返回一个函数，它先对输入应用一个给定函数，再对输出应用另一个函数。
compose方法先把给定的函数用作compose的参数里面给的那个函数，然后再把函数本身用于结果。
    //合成一个函数h，先给数字加1，再给结果乘2，数学上写作g(f(x))
    Function<Integer, Integer> f = x -> x + 1;
    Function<Integer, Integer> g = x -> x * 2;
    Function<Integer, Integer> h = f.andThen(g);
    int result = h.apply(1);//返回4
    //使用compose方法的话等于f(g(x))，上边函数将返回3
函数复合方式实战：
    //如对用String表示的一封信做文本转换
    public class Letter{
        public static String addHeader(String text){
            return "From Raoul, Mario and Alan: " + text;
        }
        public static String addFooter(String text){
            return text + " Kind regards";
        }
        public static String checkSpelling(String text){
            return text.replaceAll("labda", "lambda");
        }
    }
    //通过复合这些方法来创建各种转型流水线，比如：先加上抬头，然后进行拼写检查，最后加上一个落款。
    Function<String, String> addHeader = Letter::addHeader;
    Function<String, String> transformationPipeline = addHeader.andThen(Letter::checkSpelling)
                                                               .andThen(Letter::addFooter);
```    

[返回目录](#目录)

## Stream
### 流的基本操作
流：从支持数据处理操作的源生成的元素序列  
流只能消费一次
```java
流的使用包含三部分：
    一个数据源（如集合）来执行一个查询；
    一个中间操作链，形成一条流的流水线；
    一个终端操作，执行流水线，并能生成结果。

List<String> names = menu.stream() //获取流
                         .filter(d -> d.getCalories() > 300) //中间操作(返回Stream)，过滤大于300卡路里的菜
                         .map(Dish::getName) //中间操作(返回Stream)，获取菜名
                         .limit(3) //中间操作(返回Stream)，取前3个
                         .collect(toList()); //终端操作(返回非Stream)，关闭流，生成列表。

List<Dish> menu = Arrays.asList(
        new Dish("pork", false, 800, Dish.Type.MEAT),
        new Dish("beef", false, 700, Dish.Type.MEAT),
        new Dish("chicken", false, 400, Dish.Type.MEAT),
        new Dish("french fries", true, 530, Dish.Type.OTHER),
        new Dish("rice", true, 350, Dish.Type.OTHER),
        new Dish("season fruit", true, 120, Dish.Type.OTHER),
        new Dish("pizza", true, 550, Dish.Type.OTHER),
        new Dish("prawns", false, 300, Dish.Type.FISH),
        new Dish("salmon", false, 450, Dish.Type.FISH) );
public class Dish {
    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;
    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public boolean isVegetarian() {
        return vegetarian;
    }
    public int getCalories() {
        return calories;
    }
    public Type getType() {
        return type;
    }
    @Override
    public String toString() {
        return name;
    }
    public enum Type { MEAT, FISH, OTHER }
}
```
- **中间操作**

操作|参数|描述  
---|---|---  
filter|返回boolean的函数|过滤
map|Function<T, R>|T -> R
flatmap|Function<T, R>|方法让你把一个流中的每个值都换成另一个流，然后把所有的流连接起来成为一个流。
limit(n)|int|返回截至前n个元素的流，如果源是一个Set，limit的结果不会以任何顺序排列。
sorted|Comparator<T>|(T, T) -> int
distinct|无|去重
skip(n)|int|返回一个丢掉前n个元素的流。如果流中元素不足n个，则返回一个空流。 
数值流：mapToInt、mapToDouble和mapToLong|IntStream、DoubleStream和LongStream|将流转化为数值流，转回一般流可以使用 数值流.boxed()方法
构建流：| |①由值创建流：Stream<String> stream = ***Stream.of***("Java 8 ", "Lambdas ", "In ", "Action"); 创建空流：***Stream.empty();***
| | |②由数组创建流：int[] numbers = {2, 3, 5, 7, 11, 13}; int sum = ***Arrays.stream***(numbers).sum();
| | |③由文件生成流：java.nio.file.Files中的很多静态方法都会返回一个流，如：Files.lines
| | |④由函数生成流：创建无限流：Stream.iterate和Stream.generate。如：Stream.iterate(0, n -> n + 2).limit(10).forEach(System.out::println);生成了一个正偶数流，iterate应该在依次生成一系列值的时候使用。Stream.generate(Math::random).limit(5).forEach(System.out::println);生成一个有五个0到1之间的随机双精度数的流

- **终端操作**  

操作|返回类型|目的
---|---|---
forEach|void|消费流中的每个元素并对其应用Lambda。
count|long|返回流中元素的个数。
collect|集合(如List、Map、Integer)|把流归约成一个集合。
anyMatch|boolean|是否至少匹配一个元素。
allMatch|boolean|是否匹配所有元素
noneMatch|boolean|没有匹配任何元素
findAny|Optional<T>|返回当前流中的任意元素。java.util.Optional是一个容器类，代表一个值存在或不存在。
findFirst|Optional<T>|返回第一个元素。
reduce| |归约，接收两个参数：①初始值，②一个BinaryOperator<T>来将两个元素结合起来产生一个新值。如求和操作，①int sum = numbers.stream().reduce(0, (a, b) -> a + b);②int sum = numbers.stream().reduce(0, Integer::sum);
| | |reduce求最大/最小值，如：Optional<Integer> max = numbers.stream().reduce(Integer::max);  reduce(Integer::min);

[返回目录](#目录)

### 用流收集数据
```java
//todo
```
- **Collectors类的静态工厂方法**  

工厂方法|返回类型|作用|示例
---|---|---|---
toList|List<T>|把流中所有项目收集到一个List|List<Dish> dishes = menuStream.collect(toList());
toSet|Set<T>|把流中所有项目收集到一个Set，删除重复项|Set<Dish> dishes = menuStream.collect(toSet());
toCollection|Collection<T>|把流中所有项目收集到给定的供应源创建的集合|Collection<Dish> dishes = menuStream.collect(toCollection(),ArrayList::new);
counting|Long|计算流中元素的个数|long howManyDishes = menuStream.collect(counting());
summingInt|Integer|对流中项目的一个整数属性求和|int totalCalories = menuStream.collect(summingInt(Dish::getCalories));
averagingInt|Double|计算流中项目Integer 属性的平均值|double avgCalories = menuStream.collect(averagingInt(Dish::getCalories));
summarizingInt|IntSummaryStatistics|收集关于流中项目Integer 属性的统计值，例如最大、最小、总和与平均值|IntSummaryStatistics menuStatistics = menuStream.collect(summarizingInt(Dish::getCalories));
joining|String|连接对流中每个项目调用toString 方法所生成的字符串|String shortMenu = menuStream.map(Dish::getName).collect(joining(", "));
maxBy|Optional<T>|一个包裹了流中按照给定比较器选出的最大元素的Optional，或如果流为空则为Optional.empty()|Optional<Dish> fattest = menuStream.collect(maxBy(comparingInt(Dish::getCalories)));
minBy|Optional<T>|一个包裹了流中按照给定比较器选出的最小元素的Optional，或如果流为空则为Optional.empty()|Optional<Dish> lightest = menuStream.collect(minBy(comparingInt(Dish::getCalories)));
reducing|归约操作产生的类型|从一个作为累加器的初始值开始，利用BinaryOperator 与流中的元素逐个结合，从而将流归约为单个值|int totalCalories = menuStream.collect(reducing(0, Dish::getCalories, Integer::sum));
collectingAndThen|转换函数返回的类型|包裹另一个收集器，对其结果应用转换函数|int howManyDishes = menuStream.collect(collectingAndThen(toList(), List::size));
groupingBy|Map<K, List<T>>|根据项目的一个属性的值对流中的项目作问组，并将属性值作为结果Map 的键|Map<Dish.Type,List<Dish>> dishesByType = menuStream.collect(groupingBy(Dish::getType));
partitioningBy|Map<Boolean,List<T>>|根据对流中每个项目应用谓词的结果来对项目进行分区|Map<Boolean,List<Dish>> vegetarianDishes = menuStream.collect(partitioningBy(Dish::isVegetarian));

[返回目录](#目录)

### 并行流处理数据
```java
//todo
```

[返回目录](#目录)

### Optional类(java.util.Optional<T>)
```
对存在或缺失(null)的变量值进行建模：
变量存在时，Optional类只是对类简单封装。
变量不存在时，缺失的值会被建模成一个“空”的Optional对象，由方法Optional.empty()返回。
```
- **声明一个空的Optional**  
```java
Optional<Car> optCar = Optional.empty();
```
- **依据一个非空值创建Optional**  
```java
//如果car是一个null，这段代码会立即抛出一个NullPointerException，而不是等到访问car的属性值时才返回一个错误。
Optional<Car> optCar = Optional.of(car); 
```
- **可接受null的Optional**  
```java
//如果car是null，那么得到的Optional对象就是个空对象。
Optional<Car> optCar = Optional.ofNullable(car);
```
- **使用map 从Optional 对象中提取和转换值**  
```java
//要从insurance公司对象中提取公司的名称。提取名称之前，你需要检查insurance对象是否为null
String name = null;
if(insurance != null){
    name = insurance.getName();
}

//使用Optional提供的map方法
Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
Optional<String> name = optInsurance.map(Insurance::getName);
```
- **使用flatMap 链接Optional 对象**  
```java
//使用Optional获取car的Insurance名称
public String getCarInsuranceName(Optional<Person> person) {
    return person.flatMap(Person::getCar)
                 .flatMap(Car::getInsurance)
                 .map(Insurance::getName)
                 .orElse("Unknown");
}
//使用Optional解引用串接的Person/Car/Insurance对象

```
- **默认行为及解引用Optional 对象**  

- **使用filter 剔除特定的值**  
```java
//需要检查保险公司的名称是否为“Cambridge-Insurance”
Insurance insurance = ...;
if(insurance != null && "CambridgeInsurance".equals(insurance.getName())){
    System.out.println("ok");
}
//使用Optional对象的filter方法
//filter方法接受一个谓词作为参数。如果Optional对象的值存在，并且它符合谓词的条件，filter方法就返回其值；否则它就返回一个空的Optional对象。
Optional<Insurance> optInsurance = ...;
optInsurance.filter(insurance -> "CambridgeInsurance".equals(insurance.getName()))
            .ifPresent(x -> System.out.println("ok"));
```
- **Optional类的方法** 

方法|描述
---|---
empty|返回一个空的Optional 实例
filter|如果值存在并且满足提供的谓词，就返回包含该值的Optional 对象；否则返回一个空的Optional 对象
flatMap|如果值存在，就对该值执行提供的mapping 函数调用，返回一个Optional 类型的值，否则就返回一个空的Optional 对象
get|如果该值存在，将该值用Optional 封装返回，否则抛出一个NoSuchElementException 异常
ifPresent|如果值存在，就执行使用该值的方法调用，否则什么也不做
isPresent|如果值存在就返回true，否则返回false
map|如果值存在，就对该值执行提供的mapping 函数调用
of|将指定值用Optional 封装之后返回，如果该值为null，则抛出一个NullPointerException异常
ofNullable|将指定值用Optional 封装之后返回，如果该值为null，则返回一个空的Optional 对象
orElse|如果有值则将其返回，否则返回一个默认值
orElseGet|如果有值则将其返回，否则返回一个由指定的Supplier 接口生成的值
orElseThrow|如果有值则将其返回，否则抛出一个由指定的Supplier 接口生成的异常

- **Optional实战示例**
```java
//1、用Optional 封装可能为null 的值
//假设你有一个Map<String, Object>方法，访问由key索引的值时，如果map中没有与key关联的值，该次调用就会返回一个null。
Object value = map.get("key");
//采用Optional.ofNullable方法：
Optional<Object> value = Optional.ofNullable(map.get("key"));

//2、异常与Optional 的对比
public static Optional<Integer> stringToInt(String s) {
    try {
        return Optional.of(Integer.parseInt(s));
    } catch (NumberFormatException e) {
        return Optional.empty();
    }
}
//***可以将多个类似的方法封装到一个工具类OptionalUtility中。通过直接调用OptionalUtility.stringToInt方法，将String转换为一个Optional<Integer>对象，而不再需要用try/catch了。

```

[返回目录](#目录)


# 异步编程
## CompletableFuture

# 序列化，反序列化

# 网络

# 数据结构

# 反射

[返回目录](#目录)

# 设计模式
## 使用Lambda重构设计模式
### 策略模式  
解决某类算法的通用方案，包含三部分内容：  
一个代表某个算法的接口（它是策略模式的接口）。  
一个或多个该接口的具体实现，它们代表了算法的多种实现（比如，实体类Concrete-StrategyA或者ConcreteStrategyB）。  
一个或多个使用策略对象的客户。  
```java
//假设希望验证输入的内容是否根据标准进行了恰当的格式化（比如只包含小写字母或数字）。
//1、可以从定义一个验证文本（以String的形式表示）的接口入手：
public interface ValidationStrategy {
    boolean execute(String s);
}
//2、定义了该接口的一个或多个具体实现：
public class IsAllLowerCase implements ValidationStrategy {
    public boolean execute(String s){
        return s.matches("[a-z]+");
    }
}
public class IsNumeric implements ValidationStrategy {
    public boolean execute(String s){
        return s.matches("\\d+");
    }
}
//3、在程序中使用这些略有差异的验证策略：
public class Validator{
    private final ValidationStrategy strategy;
    public Validator(ValidationStrategy v){
        this.strategy = v;
    }
    public boolean validate(String s){
        return strategy.execute(s);
    }
}
Validator numericValidator = new Validator(new IsNumeric());
boolean b1 = numericValidator.validate("aaaa");//返回false
Validator lowerCaseValidator = new Validator(new IsAllLowerCase ());
boolean b2 = lowerCaseValidator.validate("bbbb");//返回true

//使用Lambda（ValidationStrategy是一个函数接口）
Validator numericValidator = new Validator((String s) -> s.matches("[a-z]+"));
boolean b1 = numericValidator.validate("aaaa");
Validator lowerCaseValidator = new Validator((String s) -> s.matches("\\d+"));
boolean b2 = lowerCaseValidator.validate("bbbb");
```

[返回目录](#目录)

### 工厂模式
无需暴露实例化的逻辑就能完成对象的创建。  
```java
//1、创建一个工厂类，它包含一个负责实现不同对象的方法
public class ProductFactory {
    public static Product createProduct(String name){
        switch(name){
            case "loan": return new Loan();
            case "stock": return new Stock();
            case "bond": return new Bond();
            default: throw new RuntimeException("No such product " + name);
        }
    }
}
//2、创建产品
Product p = ProductFactory.createProduct("loan");

//使用Lambda
//创建一个Map，将产品名映射到对应的构造函数
final static Map<String, Supplier<Product>> map = new HashMap<>();
static {
    map.put("loan", Loan::new);
    map.put("stock", Stock::new);
    map.put("bond", Bond::new);
}
//利用这个Map来实例化不同的产品
public static Product createProduct(String name){
    Supplier<Product> p = map.get(name);
    if(p != null) {
        return p.get();
    } else {
        throw new IllegalArgumentException("No such product " + name);
    }
}
//引用构造函数
Supplier<Product> loanSupplier = Loan::new;
Loan loan = loanSupplier.get();
```

[返回目录](#目录)

### 观察者模式  
某些事件发生时，需要自动地通知其他多个对象(观察者)  
```java
//通知系统，报纸机构订阅了新闻，当接收的新闻中包含的关键字时，能得到特别通知。
//1、创建一个观察者接口，且仅有一个名为notify的方法，一旦接收到一条新的新闻，该方法就会被调用：
interface Observer {
    void notify(String tweet);
}
//2、声明不同的观察者，依据新闻中不同的关键字分别定义不同的行为：
class NYTimes implements Observer{
    public void notify(String tweet) {
        if(tweet != null && tweet.contains("money")){
            System.out.println("Breaking news in NY! " + tweet);
        }
    }
}
class Guardian implements Observer{
    public void notify(String tweet) {
        if(tweet != null && tweet.contains("queen")){
            System.out.println("Yet another news in London... " + tweet);
        }
    }
}
class LeMonde implements Observer{
    public void notify(String tweet) {
        if(tweet != null && tweet.contains("wine")){
            System.out.println("Today cheese, wine and news! " + tweet);
        }
    }
}
//3、定义一个接口Subject，registerObserver方法可以注册一个新的观察者，notifyObservers方法通知它的观察者一个新闻的到来。
interface Subject{
    void registerObserver(Observer o);
    void notifyObservers(String tweet);
}
//4、实现Feed类
class Feed implements Subject{
    private final List<Observer> observers = new ArrayList<>();
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }
    public void notifyObservers(String tweet) {
        observers.forEach(o -> o.notify(tweet));
    }
}
//5、Feed类在内部维护了一个观察者列表，一条新闻到达时，它就进行通知。
Feed f = new Feed();
f.registerObserver(new NYTimes());
f.registerObserver(new Guardian());
f.registerObserver(new LeMonde());
f.notifyObservers("The queen said her favourite book is Java 8 in Action!");

//使用Lambda
f.registerObserver((String tweet) -> {
    if(tweet != null && tweet.contains("money")){
        System.out.println("Breaking news in NY! " + tweet);
    }
});
f.registerObserver((String tweet) -> {
    if(tweet != null && tweet.contains("queen")){
        System.out.println("Yet another news in London... " + tweet);
    }
});
```

[返回目录](#目录)

### 模板方法  
需要采用某个算法的框架，同时又希望有一定的灵活度，能对它的某些部分进行改进  
```java
//processCustomer方法搭建了在线银行算法的框架：获取客户提供的ID，然后提供服务给用户。不同的支行可以通过继承OnlineBanking类，对该方法提供差异化的实现。
abstract class OnlineBanking {
    public void processCustomer(int id){
        Customer c = Database.getCustomerWithId(id);
        makeCustomerHappy(c);
    }
    abstract void makeCustomerHappy(Customer c);
}

//使用Lambda
public void processCustomer(int id, Consumer<Customer> makeCustomerHappy){
    Customer c = Database.getCustomerWithId(id);
    makeCustomerHappy.accept(c);
}
//可以直接插入不同的行为，不再需要继承OnlineBanking类
new OnlineBankingLambda().processCustomer(1337, (Customer c) -> System.out.println("Hello " + c.getName());
```

[返回目录](#目录)

### 责任链模式
一个处理对象可能需要在完成一些工作之后，将结果传递给另一个对象，这个对象接着做一些工作，再转交给下一个处理对象，以此类推。  
```java
//这种模式通常是通过定义一个代表处理对象的抽象类来实现的，在抽象类中会定义一个字段来记录后续对象。一旦对象完成它的工作，处理对象就会将它的工作转交给它的后继。
public abstract class ProcessingObject<T> {
    protected ProcessingObject<T> successor;
    public void setSuccessor(ProcessingObject<T> successor){
        this.successor = successor;
    }
    public T handle(T input){
        T r = handleWork(input);
        if(successor != null){
            return successor.handle(r);
        }
        return r;
    }
    abstract protected T handleWork(T input);
}
//创建两个处理对象，进行一些文本处理工作。
public class HeaderTextProcessing extends ProcessingObject<String> {
    public String handleWork(String text){
        return "From Raoul, Mario and Alan: " + text;
    }
}
public class SpellCheckerProcessing extends ProcessingObject<String> {
    public String handleWork(String text){
        return text.replaceAll("labda", "lambda");
    }
}
//将这两个处理对象结合起来，构造一个操作序列！
ProcessingObject<String> p1 = new HeaderTextProcessing();
ProcessingObject<String> p2 = new SpellCheckerProcessing();
p1.setSuccessor(p2);
String result = p1.handle("Aren't labdas really sexy?!!");
System.out.println(result);

//使用Lambda
UnaryOperator<String> headerProcessing = (String text) -> "From Raoul, Mario and Alan: " + text;
UnaryOperator<String> spellCheckerProcessing = (String text) -> text.replaceAll("labda", "lambda");
Function<String, String> pipeline = headerProcessing.andThen(spellCheckerProcessing);
String result = pipeline.apply("Aren't labdas really sexy?!!");
```

[返回目录](#目录)

# N、解析文件
## 解析json
## 解析xml
## 解析Excel

[返回目录](#目录)


