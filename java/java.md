# 一、Java基础
## 1、Java基础
#### 1.1、String常用方法
方法|描述|实例,//返回值
:-|:-|:-
.equals()|相等|"abc".equals("abc"),//true
|||


#### 1.2、流程控制

## 2、集合
![集合框架](https://www.runoob.com/wp-content/uploads/2014/01/2243690-9cd9c896e0d512ed.gif)

## 3、I/O流

## 4、面向对象
#### 继承
#### 多态
#### 抽象
#### 封装

## 5、异常处理

## 6、多线程

## 7、泛型

## 8、JVM、GC
#### 8.1、自己编译jdk 
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

#### 8.2、java内存管理机制
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

#### 8.3、垃圾回收
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
#### 8.4、性能调优

- **大内存硬件上的程序部署策略**
```
背景：一个15万PV/日左右的在线文档类型网站，服务器的硬件为四路志强处理器、16GB物理内存，操作系统为64位CentOS 5.4，Resin作为Web服务器。
```

#### 8.5、类加载机制
- **类文件结构**
```

```
- **虚拟机类加载机制**
```

```
#### 8.6、高并发-Java内存模型与线程

## 9、正则
pattern

## 10、Lambda

## 11、函数式编程

## 序列化，反序列化

## 网络

## 设计模式

## 数据结构

## 反射

## N、解析文件
#### 解析json
#### 解析xml
#### 解析Excel




