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
|虚拟机栈|Java方法执行的线程内存模型：每个方法被执行的时候，Java虚拟机都会同步创建一个栈帧用于存储局部变量表、操作数栈、动态连接、方法出口等信息。每一个方法被调用直至执行完毕的过程，就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。线程私有|与线程相同|如果线程请求的栈深度大于虚拟机所允许的深度，将抛出StackOverflowError异常；如果Java虚拟机栈容量可以动态扩展，当栈扩展时无法申请到足够的内存会抛出OutOfMemoryError异常。|
|本地方法栈|与虚拟机栈作用类似，区别为虚拟机栈为虚拟机执行Java方法（也就是字节码）服务，而本地方法栈则是为虚拟机使用到的本地方法服务。|随线程|同虚拟机栈，会在栈深度溢出或者栈扩展失败时分别抛出StackOverflowError和OutOfMemoryError异常|
|Java堆||||
|方法区||||
|运行时常量池|属于方法区，用于存放编译期生成的各种字面量与符号引用，在类加载后存放到方法区的运行时常量池中| |无法申请到内存时会抛出OutOfMemoryError异常|

**直接内存**：非虚拟机运行时数据区的一部分，内存大小分配不受Java堆大小限制，但会受本机总内存大小及处理器寻址空间限制。容易在设置-Xmx等参数信息时忽略直接内存，使得各内存区域和大于物理内存限制，导致动态扩展时出现OutOfMemoryError异常。来源：NIO类会使用Native函数库直接分配堆外内存，然后通过一个存储在Java堆里面的DirectByteBuffer对象作为这块内存的引用进行操作，来避免在Java堆和Native堆中来回复制数据以提升性能。



- **内存溢出**
```

```


#### 8.3、垃圾回收
- **垃圾回收**
```

```
- **内存分配**
```

```
#### 8.4、性能调优

```

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

## N、解析文件
#### 解析json
#### 解析xml
#### 解析Excel




