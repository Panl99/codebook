代码、示例、练习：https://github.com/tomwhite/hadoop-book/

Hadoop是什么？
高性能海量数据存储和分析平台。

Hadoop可以干什么？（应用场景）
对配有大量硬盘的数据库进行大规模的数据分析。预测2020年全球数据总量将达到44ZB（1ZB=1000EB=1000000PB=10亿TB）。

为什么要使用Hadoop？
计算机硬盘的发展趋势：寻址时间的提升远远不如传输速率的提升。如果数据访问模式中包含大量的硬盘寻址，那么读取大量数据集就要花费更长的时间。MapReduce更适合解决需要以批处理方式分析整个数据集的问题，适合一次写入，多次读取数据的应用。

Hadoop尽量在计算节点上存储数据，以实现数据的本地快速访问

Hadoop通过显式的网络拓扑结构保留网络带宽

怎样使用Hadoop？
Hadoop相关性	项目	描述
数据格式	Avro	Hadoop的一种跨语言数据序列化库

Parquet	一种有效的用于嵌套式数据的列式存储格式
数据摄入（将自己数据传入Hadoop系统中）	Flume	支持流数据大批量摄入

Sqoop	支持在结构化数据存储和HDFS之间高效批量传输数据
数据处理	Hive	是一种数据仓库，用于管理HDFS中存储的数据并提供基于SQL的查询语言

Spark	一个面向大规模数据处理的集群计算框架，提供一个有向无环图引擎，以及支持Scala、Java、Python语言的API

Pig	一种用于开发大数据集的数据流语言

Crunch	一套高层次的Java API，用于写可以运行在MapReduce或Spark上的数据处理管线程序
数据库	HBase	一种使用HDFS作为底层存储的、分布式的、面向列的实时数据库
分布式协调服务	ZooKeeper	一种分布式高可用的协调服务，提供用于构建分布式应用的原语集


























一个集群多个框架
MapReduce实现离线批处理
Impala实现实时交互式查询分析
Storm实现流式数据实时分析
Spark实现迭代计算


一、MapReduce：
https://blog.csdn.net/A_lex_/article/details/90416551

二、HDFS（Hadoop分布式文件系统）
https://blog.csdn.net/A_lex_/article/details/91058440

三、YARN（Hadoop集群资源管理系统）
https://blog.csdn.net/A_lex_/article/details/91474264

四、Hadoop的I/O操作
Hadoop自带一套原子操作用于数据I/O操作。

1、数据完整性

检测数据是否损坏：在数据第一次引入系统时计算校验和（checksum），并在数据通过一个不可靠的通道进行传输时再次计算校验和，对比两次的校验和可判断数据是否损坏。

2、压缩

一系列操作日后补充**

3、序列化

序列化：指将结构化对象转化为字节流以便在网络上传输或写到磁盘进行永久储存的过程。

反序列化：将字节流转换回结构化对象的逆过程。

Hadoop中，系统间多个节点上进程间通过RPC通信

Hadoop的序列化格式：Writable，接口代码待学习补充**

另一种序列化框架：Avro

4、基于文件的数据结构

二进制数据：Hadoop的SequenceFile类











# YARN（Hadoop集群资源管理系统）

## 1、YARN运行机制 ##
YARN通过两类长期运行的守护进程提供核心服务：  

- **资源管理器（ResourceManager）**：管理集群上的资源。
- **节点管理器（NodeManager）**：运行在集群所有节点上，并且能够启动和监控各容器(container)。
- **资源请求**：当启动一个容器用于处理HDFS数据块（在MapReduce中运行一个map任务）时，应用会向该节点申请容器，存储该数据块三个副本的节点，或是存储这些副本的机架中的一个节点，如果都申请失败，则申请集群中的任意节点。

## 2、YARN的好处 ##

- **可扩展性**：YARN可以在更大规模的集群上运行，可以扩展到节点数10000、任务树100000；
- **高可用性**：当服务守护进程失败时，通过为另一个守护进程复制接管工作所需的状态以便其继续提供服务；
- **利用率高**：一个节点管理器管理一个资源池，而不是指定固定数目的slot；一个应用能够按需请求资源不会浪费资源；
- **多租户**：

## 3、YARN中的调度 ##

**YARN调度器工作**：根据既定策略微应用分配资源。

**YARN中三种调度器**：

- **FIFO调度器**：将应用放在一个队列中，按照先入先出的顺序运行应用。这种策略简单易懂，不需要任何配置，但不适合共享集群
- **容量调度器**：一个独立的专门队列保证小作业已提交就可以启动。这种策略预留一定资源，牺牲了整个集群的利用率，大作业执行时间较长
- **公平调度器**：第一个大作业启动时获取到集群所有资源，当第二个小作业启动时会被分配到集群的一半资源，小作业执行完后释放资源并不再申请资源，大作业将再次获取全部集群资源。这种策略即可以高效利用集群资源，又保证小作业能及时完成

**容量调度器和公平调度器配置**：待补充

# HDFS #

## HDFS的概念和特性 ##
### HDFS：Hadoop分布式文件系统 ###

- **数据块**：每个磁盘都有默认的数据块大小，这是磁盘进行数据读/写的最小单位。HDFS中的块默认大小为128MB，这么大的目的是为了最小化寻址开销，因为如果块足够大，从磁盘传输数据的时间会明显大于定位这个块开始位置所需的时间。
- **namenode**：管理文件系统的命名空间，维护文件系统树及整棵树内所有文件和目录。这些信息以“命名空间镜像文件”和“编辑日志文件”两种形式永久保存在本地磁盘上。
- **datanode**：是文件系统的工作节点，根据需要存储并检索数据块，并定期向namenode发送它们所存储的块列表。  
如果namenode所在服务器损坏，那么文件系统上所有文件将会丢失。Hadoop提供两种机制进行容错：1、备份那些组成文件系统元数据持久状态的文件。2、运行一个辅助namenode用来定期合并编辑日志与命名空间镜像，以防止编辑日志过大。
- **块缓存**：datanode频繁从磁盘读取块，对应块以堆外块缓存的形式显示地缓存在datanode内存中。
- **HDFS的高可用性**：namenode是唯一存储元数据与文件到数据块映射的地方。若namenode失效，那么所有客户端包括MapReduce作业，均无法读、写、列举（list）文件。Hadoop2配置了一对“活动—备用”namenode，当活动namenode失效，备用namenode就会接管它的任务。

### HDFS特性 ###
HDFS以流式数据访问模式来存储超大文件，运行于商用硬件集群上。

- **超大文件**：几百MB，几百GB，甚至几百TB
- **流式数据访问**：一次写入，多次读取 是最高效的访问模式
- **高时间延迟的数据访问**：HDFS是为高数据吞吐量应用优化的，这可能会以提高时间延迟为代价。对于低延时的访问需求可以使用HBase。
- **不支持多用户写入，任意修改文件**：HDFS中的文件只支持单个写入，并且写操作总是以“只添加”方式在文件末尾写数据，不支持多个写入，也不支持在文件任意位置进行修改。

## HDFS的shell操作 ##

- **帮助**：hadoop fs -help
- **将本地文件复制到HDFS中**：% hadoop fs -copyFromLocal input/docs/test.txt \ hdfs://localhost/user/tom/test.txt
- **HDFS文件复制到本地**：hadoop fs copyToLocal test.txt test.copy.txt
- **新建目录**：hadoop fs -mkdir books
- **显示**：hadoop fs -ls .
- **列出本地文件系统跟目录下的文件**：hadoop fs -ls file:///

## HDFS的工作机制 ##

***//todo***

## HDFS的Java应用开发 ##

- java实现：hdfs.DistributedFileSystem
- 从Hadoop文件系统读取文件：使用java.net.URL对象打开数据流，从中获取数据
```
InputStream in= null；
try{
    in = new URL("hdfs://host/path").openStream();
    //process in
} finally {
    IOUtils.closeStream(in);
}
```​

一、什么是MapReduce
一种可用于数据处理的编程模型。可处理大规模的数据集。

MapReduce本质上是并行运行的，因此可以将大规模的数据分析任务分发给任何一个拥有足够多机器的数据中心

二、MapReduce怎样处理数据？
MapReduce任务分为两个阶段：map阶段、reduce阶段，每个阶段都是以键值对作为输入、输出。需编写两个函数（map函数、reduce函数）

使用reduce函数处理不同map输出中具有相同键的数据，combiner函数可以减少mapper和reducer之间的数据传输量。

例：max（10,20,30,15,25）=max（max（10,20,30），max（15,25））=max（30,25）=30

直接使用map函数输出数据到reduce函数中去处理，reduce接收到（10,20,30,15,25）然后处理得到最大的30；

若使用combiner函数，则combiner先找出不同分片map的最大值，然后输出各分片最大值到reduce去处理，同得到结果；

但：combiner函数不适用如：求平均值的情况。

三、MapReduce的工作机制
1、MapReduce的作业运行机制
通过调用Job对象的submit()方法来运行MapReduce作业（也可调用waitForCompletion()方法提交之前未提交过的作业，并等待完成）。

作业提交：

submit方法创建一个内部的JobSummiter实例，并且调用其submitJobInternal()方法。

JobSummiter提交作业过程：

向资源管理器请求一个新应用id作为MapReduce作业id。

检查作业输出说明。如没有指定输出目录或者目录已存在，则不提交作业，把错误抛回给MapReduce程序；

计算作业的输入分片。如果分片无法计算（如路径不存在）则不提交作业，把错误抛回给MapReduce程序；

将运行作业所需的资源复制到一个以作业id命名的目录下的共享文件系统中。

通过调用资源管理器的submitApplication()方法提交作业。

提交作业后waitForCompletion()方法每秒轮询作业进度，若有改变，则把进度报告到控制台。

作业完成后，如果成功，则显示作业计数器；如果失败，则记录导致作业失败的错误到控制台。

作业初始化：

资源管理器收到调用它的submitApplication()方法的消息后，便将请求传递给YARN调度器。

调度器分配一个容器，然后资源管理器在节点管理器的管理下在容器中启动MapReduce作业的application master进程。

application master对作业的初始化是通过创建多个薄记对象来保持对作业进度的跟踪。

然后接受来自共享文件系统的、在客户端计算的输入分片。

然后对每个分片创建一个map任务对象和多个reduce任务对象（由mapreduce.job.reduces属性（通过setNumReduceTasks()方法设置）确定的）。任务id在此时分配。

若作业很小，application master会将其与自己在同一JVM上运行任务。当application master判断在新容器中分配和运行任务的开销大于并行运行他们的开销时就会发生此情况，这种作业称为uberized。小作业是指小于10个mapper、且只有1个reducer、且输入大小小于1个HDFS块的作业。（可通过mapreduce.job.ubertask.maxmaps、mapreduce.job.ubertask.maxreduces、mapreduce.job.ubertask.maxbytes设置；启用Uber任务：mapreduce.job.ubertask.enable=true）

在任务运行前，application master调用setupJob()方法设置OutputCommitter（默认FileOutputCommitter表示将建立作业的最终输出目录和任务输出的临时工作空间）。

任务分配：

application master为作业中所有map任务和reduce任务向资源管理器请求容器（当作业不适合作为uber任务运行）。

先为map任务发起请求，直到有5%的map任务已完成时，才会为reduce发出请求。

reduce任务能在集群中任意位置运行，而map任务的请求有着数据本地化局限。

请求为任务指定了内存需求和CPU数。（默认每个map任务和reduce任务都分配到1024MB的内存和1个虚拟内核（可通过mapreduce.map.memory.mb、mapreduce.reduce.memory.mb、mapreduce.map.cpu.vcores、mapreduce.reduce.cpu.vcoresp.memory.db设置））

任务执行：

当资源管理器的调度器为任务分配了一个特定节点上的容器，application master就会通过与节点管理器通信来启动容器。

该任务由主类YarnChild的一个java应用程序来执行，在运行前，要将任务所需的资源本地化，最后运行map或reduce任务。

Streaming任务使用标准输入输出流与进程通信，在任务执行过程中，java进程都会把输入键-值对传给外部进程，外部进程通过用户自定义的map函数和reduce函数来执行它，并把输出键-值对传回java进程。

进度和状态的更新：

map任务或reduce任务运行时，子进程和其父application master通过umbilical接口通信，每隔3秒，任务通过此接口向application master报告进度和状态，application master会形成一个作业的汇聚视图。

在作业期间，客户端每秒钟轮询一次application master以接收最新状态（轮询间隔通过mapreduce.client.progressmonitor.pollinterval设置）；或者通过Job的getStatus()方法得到一个JobStatus实例，该实例中包含作业的所有状态信息。

作业完成：

在application master收到作业最后一个任务的完成通知后，就会把作业的状态设置为“成功”。

Job在轮询状态时知道了任务已完成，会打印一条消息告知用户，然后从waitForCompletion()方法返回。Job统计信息和计数值此时输出到控制台。

作业完成时，application master和任务容器清理其工作状态，OutputCommitter的commitJob()方法会被调用。

2、失败
任务失败：

若application master一段时间未收到进度更新，会将任务标记为失败，任务JVM进程会被杀死。

设置超时时间（通常10分钟）：mapreduce.task.timeout（毫秒），0表示关闭超时判定。

map、reduce任务最多尝试次数设置：mapreduce.map.maxattempts、mapreduce.reduce.maxattempts（默认为4）

map、reduce任务允许失败最大百分比设置：mapreduce.map.failures.maxpercent、mapreduce.reduce.failures.maxpercent

application master运行失败：

MapReduce application master最多尝试次数设置：mapreduce.am.max-attempts（默认2），需同时设置YARN的

YARN application master最多尝试次数设置：yarn.resourcemanager.am.max-attempts

节点管理器运行失败：

资源管理器运行失败：

3、shuffle和排序
shuffle：系统执行排序、将map输出作为输入传给reducer的过程。（此过程可优化MapReduce）

map端：

map函数输出时，利用缓冲方式写到内存并进行预排序。

每个map任务都有一个环形内存缓冲区用于存储任务输出（缓冲区大小设置：mapreduce.task.io.sort.mb（默认100MB））。

当缓冲区内容达到阈值（mapreduce.map.sort.spill.percent（默认0.80/80%）），会起一个后台线程将内容溢出（spill）到磁盘，溢出过程中，map继续输出到缓冲区，若此期间缓冲区被填满，map会被阻塞直到写磁盘完成。溢出写过程按轮询方式将缓冲区内容写到mapreduce.cluster.local.dir所在子目录下指定的目录中。

写磁盘前，线程会根据最终数据要传的reducer将数据划分成相应的分区，每个分区中，后台线程按键在内存中排序，如果有一个combiner函数，它会在排序后的输出上运行，运行combiner函数可以使map输出结果更紧凑，减少写到磁盘的数据和传递给reducer的数据。

每次内存缓冲区达到溢出阈值，就会新建一个溢出文件（spill file），因此在map任务写完最后一个输出记录后会产生几个溢出文件。在任务完成前，溢出文件会被合并成一个已分区已排序的输出文件。（一次最多合并多少流设置：mapreduce.task.io.sort.factor（默认10））。如果至少存在3个溢出文件（设置：mapreduce.map.combine.minspills）时，combiner就会在输出文件写到磁盘前再次运行。如果只有1-2个溢出文件，map输出规模减少，不值得再调用combiner而产生的开销，因此不会为该map输出再运行combiner。

可以对压缩map输出写到磁盘，这样写磁盘速度快、节约磁盘空间、减少传给reducer的数据量。（默认输出不是压缩的，设置：mapreduce.map.out.compress为true，压缩库设置：mapreduce.map.output.compress.codec）

reducer通过HTTP得到输出文件的分区。（设置文件分区的工作线程数量：mapreduce.shuffle.max.threads（默认0表示最大线程数为机器中处理器数量的两倍），此设置针对每一个节点管理器）

reduce端：

map输出文件位于运行map任务的tasktracker的本地磁盘，map任务完成后tasktracker要为分区文件运行reduce任务。由于每个map任务完成时间不同，当各集群map任务完成后，reduce任务就会去复制map输出。（reduce复制线程数设置：mapreduce.reduce.shuffle.parallelcopies（默认5））（reducer从哪台机器获取map输出：map任务完成后会使用心跳机制通知它们的application master，这样reducer会使用一个线程定时访问master去获取输出主机位置）

map输出相当小时，会被复制到reduce任务JVM的内存（缓冲区大小设置：mapreduce.reduce.shuffle.input.buffer.percent，指定堆空间百分比）；否则会被复制到磁盘。当缓冲区达到阈值（缓冲区阈值设置：mapreduce.reduce.shuffle.merge.percent）或map输出达到阈值（map输出阈值设置：mapreduce.reduce.merge.inmem.threshold），则会合并后溢出写入磁盘（合并期间可以使用combiner降低写入磁盘数据量）。

复制完所有map输出后，reduce对map输出进行合并使其顺序排序。（合并过程是循环进行的，比如有50个map输出，合并因子是10，合并过程会进行5趟，分别将10个文件合并成1个文件，最后会生成5个中间文件（合并因子设置：mapreduce.task.io.sort.factor））

最后reduce阶段会对已排序输出中的每个键调用reduce函数，直接把数据输入reduce函数。（生成的5个文件不会再合并），输出会直接写到输出文件系统（HDFS）。

配置调优（调优shuffle来提高MapReduce性能）：

总原则，保证map、reduce函数足够内存运行（尽量少），给shuffle尽量多的内存空间。

map、reduce任务节点上的JVM内存尽可能设置大些。（JVM内存设置：mapred.child.java.opts）

map端：避免多次溢出写磁盘（估算map输出大小，合理设置mapreduce.task.io.sort.*减少写次数）。MapReduce计数器（“SPILLED_RECORDS”，包括map、reduce两端的溢出写）计算在作业运行整个阶段溢出写磁盘的记录数，可用于帮助调优。

reduce端：理想状态是中间数据全部驻留在内存中，但默认不会发生，一般会把内存留给reduce函数，但若reduce函数内存需求不大，可以设置mapreduce.reduce.merge.inmem.threshold为0，设置mapreduce.reduce.input.buffer.percent为1.0或更低值来提升性能。

map端调优属性：

属性名	默认值	类型	属性说明
mapreduce.task.io.sort.mb	100	int	排序map输出时所使用的内存缓冲区大小，单位：MB
mapreduce.map.sort.spill.percent	0.80	float	map输出内存缓冲和用来开始磁盘溢出写过程的记录边界索引两者使用比例的阈值
mapreduce.task.io.sort.factor	10	int	排序文件时，一次最多合并的流数。（reduce也用，常加到100）
mapreduce.map.combine.minspills	3	int	运行combiner所需的最少溢出文件数
mapreduce.map.output.compress	false	Boolean	是否压缩map输出
mapreduce.map.output.compress.codec	
org.apache.hadoop.io.

compress.DefaultCodec

Class name	用于map输出的压缩编解码器
mapreduce.shuffle.max.threads	0	int	每个节点管理器的工作线程数，用于将map输出到reducer。（这是集群范围的设置，不能由单个作业设置。0表示使用Netty默认值，即两倍于可用的处理器数）
























reduce端调优属性：

属性名	默认值	类型	属性说明
mapreduce.reduce.shuffle.parallelcopies	5	int	把map输出复制到reducer的线程数
mapreduce.reduce.shuffle.maxfetchfailures	10	int	在声明失败之前，reducer获取一个map输出所花费的最大时间
mapreduce.task.io.sort.factor	10	int	排序文件时，一次最多合并的流数。（map也用，常加到100）
mapreduce.reduce.shuffle.input.buffer.percent	0.70	float	在shuffle复制阶段，分配给map输出的缓冲区占堆空间的百分比
mapreduce.reduce.shuffle.merge.percent	0.66	float	map输出缓冲区（mapred.job.shuffle.input.buffer.percent）的阈值使用比例，用于启动合并输出和磁盘溢出写的过程
mapreduce.reduce.merge.in.mem.threshold	
1000

int	启动合并输出和磁盘溢出写过程的map输出的阈值数。（0或更小值表示没有阈值限制，溢出写：mapreduce.reduce.shuffle.merge.percent）
mapreduce.reduce.input.buffer.percent	0.0	float	在reduce过程中，在内存中保存map输出的空间占整个堆空间的比例。reduce阶段开始时，内存中的map输出大小不能大于此值。默认reduce任务开始前，所有map输出都合并到磁盘上，以便为reducer提供更多的内存，若是reducer需要的内存较少，可扩大此值来最小化访问磁盘次数






























4、任务执行（MapReduce用户对任务执行的控制）
任务执行环境：

任务执行环境属性：

属性名称	属性说明	类型	示例
mapreduce.job.id	作业id	string	job_200811201130_0004
mapreduce.task.id	任务id	string	task_200811201130_0004_m_000003
mapreduce.task.attemp.id

任务尝试id	string	attempt_2008112011300004_m_000003_0
mapreduce.task.partition	作业中任务索引	int	3
mapreduce.task.ismap	此任务是否map任务	boolean	true














 Streaming环境变量：

推测执行：

Hadoop不会尝试诊断或修复执行慢的任务，在一个任务运行比预期慢的时候，他会尽量检测并启动另一个相同的任务作备份。

如果启动两个重复任务，它们会相互竞争资源，所以调度器会跟踪作业中所有相同类型任务的进度，并只启动速度明显低于平均水平的那一小部分任务的推测副本。原任务和推测任务只要有一个执行完成，另一个都会被中止。

推测任务只是优化措施，并不能使作业的运行更可靠。默认推测执行时启用的。

推测执行的目的在于减少作业执行时间，但这是以集群效率为代价，因此需要看情况关闭此功能。

推测任务执行属性：

属性名称	默认值	类型	描述
mapreduce.map.speculative	true	Boolean	如果任务运行变慢，该属性决定是否要启动map任务的另一个实例
mapreduce.reduce.speculative	true	Boolean	如果任务运行变慢，该属性决定是否要启动reduce任务的另一个实例
Yarn.app.mapreduce.am.job.speculator.class	
Org.apache.hadoop.map.reduce.v2.

app.speculate.DefaultSpeculator

Class	Speculator类实现推测执行策略（MapReduce2）
Yarn.app.mapreduce.am.job.estimator.class	
Org.apache.hadoop.map.reduce.v2.

app.speculate.LegacyTaskRuntimeEstimator

Class	Speculator实例使用TaskRuntimeEstimator的实现，提供任务运行时间的估计值（MapReduce2）




















四、MapReduce的类型与格式
1、MapReduce的类型
map、combiner、reduce函数格式：

map：（k1，v1）—>list（k2，v2）

combiner：（k2，list（v2））—>list（k2，v2）

reduce：（k2，list（v2））—>list（k3，v3）

其中（k1，v1）为map函数输入键-值，（k2，v2）为输出键-值，且map函数的输入键-值类型一般不同于输出键-值类型，但map函数输出键-值类型必须与reduce输入键-值类型相同。

默认的MapReduce作业：

默认的Streaming作业：

2、输入、输出格式
输入分片与记录：

一个输入分片就是一个由单个map操作来处理的输入块。每个分片被划分为若干记录，每个记录就是一个键-值对。

文本输入、输出：

TextInputFormat：每条记录是1行输入，键是LongWritable类型，存储改行在整个文件中的字节偏移量，值是这行的内容（被打包成Text对象）。

KeyValueTextInputFormat：可设置某种分界符分割键值对（mapreduce.input.keyvaluelinerecordreader.key.value.separator默认制表符）

NLineInputFormat：希望mapper收到固定行数的输入使用它，键是文件中行的字节偏移量，值是行本身。（设置N行：mapreduce.input.lineinputformat.linespermap）

xml：

输出：TextOutputFormat

二进制输入、输出：

SequenceFileInputFormat：输入顺序文件格式数据使用它。键和值由顺序文件决定，只需保证map输入的类型匹配。

SequenceFileAsTextInputFormat：将顺序文件的键和值转换为Text对象（调用toString()方法）

SequenceFileAsBinaryInputFormat：顺序文件的键和值作为二进制对象。

FixedLengthInputFormat：用于从文件中读取固定宽度的二进制记录。

输出：

SequenceFileOutputFormat

SequenceFileAsBinaryOutputFormat

MapFileOutputFormat

多个输入、输出：

数据库输入、输出

五、排序
部分排序

全排序：

先创建一系列排好序的文件，在串联这些文件，最后生成一个全局排序的文件。该方法关键在于如何划分各个分区。

辅助排序：





MapReduce程序运行流程解析。
MapTask并发数的决定机制。
MapReduce中的combiner组件应用。
MapReduce中的序列化框架及应用。
MapReduce中的排序。
MapReduce中的自定义分区实现。
MapReduce的shuffle机制。
MapReduce利用数据压缩进行优化。
MapReduce程序与YARN之间的关系。
MapReduce参数优化。
MapReduce应用开发
WordCount

Hadoop配置

配置文件名：configuration.xml
文件位置：org.apache.hadoop.conf包下

<?xml version="1.0"?>
<configuration>
	<property>
		<name>color</name>
		<value>yellow</value>
		<description>Color</description>
	</property>
	<property>
		<name>size</name>
		<value>10</value>
		<description>Size</description>
	</property>
	<property>
		<name>weight</name>
		<value>heavy</value>
		<final>true</final>
		<description>Weight</description>
	</property>
	<property>
		<name>size-weight</name>
		<value>${size},${weight}</value>
		<description>Size and Weight</description>
	</property>
</configuration>

访问configuration.xml属性

Configuration conf = new Configuration();
conf.addResource("configuration.xml");
conf.get("color");"yellow"
