# 目录
- [一、kafka概念](#一、kafka概念)
    - [kafka特点](#kafka特点)
- [二、安装kafka](#二、安装kafka)
    - [broker配置](#broker配置)
    - [选择硬件-TODO](#选择硬件)
    - [kafka集群](#kafka集群)
        - [配置kafka集群](#配置kafka集群)
    - [生产环境注意事项](#生产环境注意事项)
- [三、kafka生产者](#三、kafka生产者)
    - [消息发送流程](#消息发送流程)
    - [创建kafka生产者](#创建kafka生产者)
    - [发送消息到kafka](#发送消息到kafka)
    - [生产者配置](#生产者配置)
    - [序列化器](#序列化器)
        - [自定义序列化器](#自定义序列化器)
        - [其它序列化框架](#其它序列化框架)
    - [分区](#分区)
        - [自定义分区](#自定义分区)
- [四、kafka消费者](#四、kafka消费者)
    - [创建Kafka消费者](#创建Kafka消费者)
        - [消息轮询请求数据](#消息轮询请求数据)
    - [消费者的配置](#消费者的配置)
    - [提交和偏移量](#提交和偏移量)
    - [再均衡监听器](#再均衡监听器)
    - [退出](#退出)
    - [反序列化器](#反序列化器)
    - [独立消费者(没有群组的消费者)](#独立消费者(没有群组的消费者))

- [事务](#事务)
    - [消息传输保障](#消息传输保障)
    - [幂等](#幂等)
    - [事务实现-TODO](#事务实现)

[返回目录](#目录)

# 一、kafka概念
- 一个高性能的发布-订阅消息系统。  
- 一个典型的Kafka体系架构包含：若干Producer、若干Broker、若干Consumer、一个Zookeeper集群。
    - `Zookeeper`：负责kafka集群元数据管理、控制器选举等操作。
    - `Producer`将消息发送到`Broker`，`Broker`将收到的消息存储到磁盘，`Consumer`从`Broker`订阅并消费消息。
- `Producer`：生产者，负责生产消息，然后发送到Kafka中。
- `Consumer`：消费者，从Kafka中接收消息，进行处理。
- `Broker`：服务代理节点，对于Kafka，`broker`可看作一个独立的Kafka服务节点(或Kafka服务实例)。
    - `Broker` 接收来自生产者的消息，为消息设置偏移量，并提交消息到磁盘保存。
    - `Broker` 为消费者提供服务，对读取分区的请求作出响应，返回已经提交到磁盘上的消息。
- `Topic`：主题，Kafka中消息以主题为单位归类，生产者将消息发送到特定主题，消费者订阅主题并消费。
- `Partition`：分区，`Topic`可以分为多个`Partition`，一个分区只属于单个主题。同一主题下的不同分区消息不同。
- `Offset`：偏移量，是消息在分区中唯一标识，Kafka通过偏移量保证消息在分区中的顺序性。

[返回目录](#目录)

## kafka特点
- **支持多个生产者**：适合从多个前端系统收集数据，并以统一的格式对外提供数据。
- **支持多个消费者**：支持多个消费者从一个单独的消息流上读取数据，且消费者之间互不影响。（其他队列系统的消息一旦被一个客户端读取，其他客户端就无法在读取它）
- **可持久化数据，实现非实时地读取消息**：kafka根据主题设置保留规则，将数据保存到磁盘。消费者可从上次处理中断的地方继续处理消息。
- **可伸缩性**：kafka具有灵活的伸缩性，对在线集群进行扩展不会影响整体系统的可用性。
- **高性能**：通过横向扩展生产者、消费者、broker，kafka可以轻松的处理巨大的消息流。且能保证亚秒级的消息延迟。

[返回目录](#目录)

# 二、安装kafka
- **选择系统**：Linux
- **安装Java**：/usr/java/jdk1.8.0_51
- **安装Zookeeper**：kafka使用zookeeper保存集群的元数据信息和消费者信息。（kafka发行版自带zookeeper，可直接从脚本启动）  
- **单机版**：  
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190612232640414.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FfbGV4Xw==,size_16,color_FFFFFF,t_70)  
- **zookeeper集群**：  
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190612232716776.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FfbGV4Xw==,size_16,color_FFFFFF,t_70)
- **安装Kafka Broker**  
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190612232747159.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FfbGV4Xw==,size_16,color_FFFFFF,t_70)

[返回目录](#目录)

## broker配置
- **常规配置**：
    - **`broker.id`**：可设为任意整数，默认0，在kafka集群中唯一，建议设置成与机器名相关的整数便于维护。  
    - **`port`**：默认9092端口，可设置为其他可用的端口（注意：若设为1024以下端口，需使用root权限启动kafka，不建议）  
    - **`zookeeper.connect`**：指定保存broker元数据的zookeeper地址。（如localhost:2181 表示zookeeper运行在本地2181端口上），参数格式：hostname:port/path，hostname表示zookeeper服务器的机器名或IP地址；port表示zookeeper客户端连接端口；/path表示可选的zookeeper路径，作为集群的chroot环境，若不指定默认为根路径。  
    - **`log.dirs`**：消息日志目录。用逗号分隔的本地文件系统路径。如果指定多个路径，broker会根据“最少使用”原则把同一分区的日志片段保存在同一路径下。  
    - **`num.recovery.threads.per.data.dir`**：配置线程池处理日志，用于如下情况：  
        - 服务器正常启动，用于打开每个分区的日志片段；  
        - 服务器崩溃后重启，用于检查和截断每个分区的日志片段；  
        - 服务器正常关闭，用于关闭日志片段。  
        - 注意：举例：若此参数设为8，log.dir指定了3个路径，则总共需要24个线程。  
    - **`auto.create.topics.enable`**：自动创建主题（设为true），用于如下情况：  
        - 当一个生产者开始往主题写入消息时；  
        - 当一个消费者开始从主题读取消息时；  
        - 当任一个客户端向主题发送元数据请求时。  
        - 若显式创建主题（手动或其他配置），可将此参数设为false。

- **主题的默认配置**  
新版本kafka需要使用管理工具对默认参数进行重置。  
    - **`num.partitions`**：主题包含的分区数。默认是1，可以增加主题分区数量，但不能减少。可以估算出主题吞吐量和消费者吞吐量，用主题吞吐量除以消费者吞吐量来计算分区个数。  
    - **`log.retention.ms`**：消息保留时间。
        - 默认**`log.retention.hours`配置时间**，默认168小时（即一周）
        - 还有`log.retention.minutes`。  
    - **`log.retention.bytes`**：消息保留大小。作用于每个分区（如设置为1GB，有8个分区，则可以保留8GB数据）  
    - **`log.segment.bytes`**：日志片段关闭大小，关闭后重新创建新的日志片段。  
    - **`log.segment.ms`**：日志片段关闭时间。  
    - **`message.max.bytes`**：单个消息大小（压缩后）。默认1000000（即1MB），超过后消息丢失且返回错误。  

[返回目录](#目录)

## 选择硬件
- **磁盘吞吐量**：
- **磁盘容量**：
- **内存**：
- **网络**：
- **CPU**：

[返回目录](#目录)

## kafka集群
- 跨服务器进行负载均衡 
- 可以使用复制功能避免单节点故障造成数据丢失

[返回目录](#目录)

### 配置kafka集群
- **需要`broker`数量**：影响因素：  
    - 需要多少磁盘空间保留数据，以及单个broker有多少空间可用；  
    - 集群处理请求的能力。
- **`broker`配置**：broker加集群需要配置两个参数：  
    - 所有`broker`配置相同的`zookeeper.connect`，该参数指定保存元数据的zookeeper群组和路径；  
    - 每个`broker`设置唯一`broker.id`。
- **操作系统调优**：  
    - **虚拟内存**：  
    - **磁盘**：  
    - **网络**：

[返回目录](#目录)

## 生产环境注意事项
- **垃圾回收器选项**：  
- **数据中心布局**：  
- **共享zookeeper**：

[返回目录](#目录)

# 三、kafka生产者
## 消息发送流程
- 创建一个ProducerRecord对象，包含topic和消息内容（还可指定键和分区）；
- 序列化器对键和值对象进行序列化，然后发送ProducerRecord对象到分区器；
- 如果在第1步指定了分区，则直接返回分区；如果未指定分区，分区器会根据ProducerRecord对象的键指定一个分区；
- 消息被加到一个记录批次中，这个批次的所有消息会被发送到相同的主题和分区；
- 有一个独立的线程负责将这些记录批次发送到broker上；
- broker收到消息返回一个响应，写入成功返回一个RecordMetaData对象（包含topic、分区信息、消息在分区中的偏移量），写入失败返回一个错误，生产者收到错误后会重新发送，如果接连失败将会返回错误信息。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190616171635916.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FfbGV4Xw==,size_16,color_FFFFFF,t_70)

[返回目录](#目录)

## 创建kafka生产者
- kafka生产者3个必选属性:
    - **`bootstrap.servers`**：指定broker的地址清单（地址格式：host:port）。  
    不必包含所有broker地址，生产者会从给定的broker中查找其他broker信息，建议至少设置两个（其中一个宕机不会影响生产者继续连接到集群）。
    - **`key.serializer`**：指定一个实现`org.apache.kafka.common.serialization.Serializer`接口的类，该类会将键对象序列化成字节数组。kafka客户端默认提供：`ByteArraySerializer`、`StringSerializer`、`IntegerSerializer`。
    - **`value.serializer`**：指定的类会将值序列化。
```
//创建一个属性对象
private Properties kafkaProps = new Properties();
//设置broker地址
kafkaProps.put("bootstrap.servers", "broker1:9092,broker2:9092");
//设置键、值的序列化类
kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//创建一个生产者对象，为键、值设置类型为String，将属性对象传给进去。
producer = new KafkaProducer<String, String>(kafkaProps);
```

[返回目录](#目录)

## 发送消息到kafka
发送消息的三种方式:  
- **发送并忘记（fire-and-forget）**：将消息发送给服务器，但不关心它是否正常到达。（一般消息会正常到达，因为kafka高可用，且生产者有重试机制。偶尔会丢失消息）
    ```java
    //创建一个ProducerRecord对象，指定topic、键、值。（键、值类型要和序列化器以及生产者对象一直）
    ProducerRecord<String, String> record = new ProducerRecord<> ("CustomerCountry", "Precision Products", "France");
    try {
        //调用生产者的send()方法发送。消息会先被放进缓存，然后使用单独线程发送到服务端。
         producer.send(record);
    } catch (Exception e) {
        //打印堆栈信息可以知道发送消息前是否有异常，如SerializationException（序列化消息失败）、BufferExhaustedException或TimeoutException（缓冲区已满）、InterruptException（发送线程被中断）
        e.printStackTrace();
    }
    ```

- **同步发送**：使用send()方法发送会返回一个Future对象，调用其get()方法等待就可知道消息是否发送成功。
    ```java
    ProducerRecord<String, String> record = new ProducerRecord<> ("CustomerCountry", "Precision Products", "France");
    try {
        //调用get()方法等待kafka响应，如果服务器返回错误，get()方法会抛异常；没错会返回RecordMetadata对象（可以用它获取消息的偏移量）。
         producer.send(record).get();
    } catch (Exception e) {
        e.printStackTrace();
    }
    ```

- **异步发送**：调用send()方法发送消息，并指定一个回调函数，服务器在返回响应时调用该函数。
    ```java
    //创建一个回调类和方法
    private class DemoProducerCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
    }
    ProducerRecord<String, String> record = new ProducerRecord<> ("CustomerCountry", "Precision Products", "France");
    //发送消息，并传入回调对象
    producer.send(record, new DemoProducerCallback());
    ```

[返回目录](#目录)

## 生产者配置
生产者的其他配置参数：  
- **`acks`**：指定必须有多少个分区副本收到消息，生产者才会认为消息写入成功。  
    - **`asks=0`**，生产者不知道服务器有没有收到消息，不会等待服务器响应，因此吞吐量较大。
    - **`asks=1`**，集群leader节点收到消息会返回成功，若leader节点挂掉会返回错误。
    - **`asks=all`**，所有参与复制的节点收到消息才会返回成功。这种模式最安全，但延迟高。
- **`buffer.memory`**：指定生产者内存缓冲区大小，生产者用来缓冲要发到服务器的消息。
- **`compression.type`**：指定消息发送给broker之前使用的压缩方法。默认不压缩。
    - 可以设置为**snappy（占cpu少，较好的性能和压缩比）、gzip（占cpu大，更高的压缩比）、lz4**。
- **`retries`**：指定生产者重发消息次数。
    - **`retry.backoff.ms`**可设置重试间隔（默认100ms）。
- **`batch.size`**：指定一个批次占内存大小（大小指字节数非消息数。多个消息被发往同一分区，生产者会将他们放在同一批次中）。批次被填满，所有消息就会被发送，但生产者不一定都会等批次填满才发送消息。
- **`linger.ms`**：指定发送批次之前等待更多消息加入批次的时间（KafkaProducer会在批次填满或linger.ms达到上限时将消息发送出去）。
- **`client.id`**：任意字符串，服务器通过它识别消息来源。
- **`max.in.flight.requests.per.connection`**：指定生产者在收到服务器响应前能发送多少消息。值越高，占内存越高，但吞吐量会提升，设为1可以保证往服务器写消息的顺序跟发送的顺序一致。
- **`timeout.ms`**：指定broker等待同步副本返回消息确认的时间，与**asks**的配置对应。
- **`request.timeout.ms`**：指定生产者在发送数据时等待服务器返回响应的时间。
- **`metadata.fetch.timeout.ms`**：指定生产者在获取元数据时等待服务器返回响应的时间。
- **`max.block.ms`**：指定在调用send()方法或使用partitionsFor()方法获取元数据时 生产者的阻塞时间。（当生产者缓冲区已满、或者没有可用的元数据时，这些方法会阻塞，达到阻塞时间会抛超时异常）。
- **`max.request.size`**：指定生产者发送的请求大小。
- **`receive.buffer.bytes`**：指定TCP socket接收数据包的缓冲区大小。
- **`send.buffer.bytes`**：指定TCP socket发送数据包的缓冲区大小。

[返回目录](#目录)

## 序列化器
### 自定义序列化器
```java
//创建一个客户类
public class Customer {
    private int customerId;
    private String customerName;
    
    public Customer(int id, String name) {
        this.customerId = id;
        this.customerName = name;
    }
    public int getId() {
        return customerId;
    }
    public String getName() {
        return customerName;
    }
}
```
```java
//为客户类创建一个序列化器
import org.apache.kafka.common.errors.SerializationException;
import java.nio.ByteBuffer;
import java.util.Map;

public class CustomerSerializer implements Serializer<Customer> {
    @Override
    public void configure(Map configs, boolean isKey){
        //不做任何配置
    }
    
    /**
    Customer对象被序列化成：
    表示customerId的4字节整数
    表示customerName长度的4字节整数（如果customerName为空，则长度为0）
    表示customerName的N个字节
    */
    @Override
    public byte[] serialize(String topic, Customer data) {
        try {
            byte[] serializedName;
            int stringSize;
            if (data == null){
                return null;
            } else {
                if (data.getName() != null){
                    serializedName = data.getName().getBytes("UTF-8");
                    stringSize = serializedName.length;
                } else {
                    serializedName = new Bytes[0];
                    stringSize = 0;
                }
            }
            ByteBuffer buffer = Bytebuffer.allocate(4 + 4 + stringSize);
            buffer.putInt(data.getId);
            buffer.putInt(stringSize);
            buffer.put(serializedName);
            return buffer.array();
        } catch (Exception e) {
            throw new SerializationException("Error when serializing Customer to byte[] " + e);
        }
    }
    
    @Override
    public void close() {
        //不需要关闭
    }
}
```

[返回目录](#目录)

### 其它序列化框架
- **Avro**：编程语言无关的序列化格式。数据会被序列化成二进制文件或json文件。  
    - Avro通过schema定义，schema通过json描述。  
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190616214255789.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FfbGV4Xw==,size_16,color_FFFFFF,t_70)  
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190616214332178.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FfbGV4Xw==,size_16,color_FFFFFF,t_70)  
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190616214445154.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FfbGV4Xw==,size_16,color_FFFFFF,t_70)  

- **Thrift**

[返回目录](#目录)

## 分区
- **键的两个作用：**
    - 作为消息的附加信息；
    - 可以用来决定消息应该被写到topic的哪个分区（键相同的消息将被写到同一分区）。
- **键可以为null**，加上使用默认分区器，消息会被分区器使用轮询的方法随机均衡的发送到topic的任意可用分区。
- **键不为空**，并且使用默认分区器，kafka会使用散列算法对键进行散列，将散列值映射到特定分区（一般同一个键会被映射到同一分区（即使分区不可用，这种情况较少），但要保证topic分区数不变）。

[返回目录](#目录)

### 自定义分区
```java
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;

//为"Banana"分配一个单独的分区
public class BananaPartitioner implements Partitioner {
    public void configure(Map<String, ?> configs) { }
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
        //只接受字符串作为键
        if ((keyBytes == null) || (!(key instanceof String))) {
            throws new InvalidRecordException("we expect all messages to have customer name as key");
        }
        //Banana总是被分配到最后一个分区
        if (((String) key).equals("Banana")) {
            return numPartitions;
        }
        //其他记录散列到其他分区
        return (Math.abs(Utils.murmur2(keyBytes)) % (numPartitions - 1))
    }
    public void close() { }
}
```

[返回目录](#目录)

# 四、kafka消费者
- **消费者群组**：kafka消费者从属于消费者群组，一个群组里的消费者订阅的是同一个主题，每个消费者接收主题一部分分区的消息。
- **再均衡**：分区的所有权从一个消费者转移到另一个消费者。

## 创建Kafka消费者
```java
//创建一个属性对象
Properties kafkaProps = new Properties();
//设置broker地址
kafkaProps.put("bootstrap.servers", "broker1:9092,broker2:9092");
//设置消费者所属群组
kafkaProps.put("group.id", "CountryCounter");
//设置键、值的反序列化类
kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

//创建一个消费者对象，为键、值设置类型为String，将属性对象传给进去。
KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(kafkaProps);
//订阅Topic，简单创建一个只包含单个元素的列表，topic为"customerCountries"。（也可以传入正则）
consumer.subscribe(Collections.singletonList("customerCountries"));
//如订阅test相关主题：consumer.subscribe("test.*");
```

[返回目录](#目录)

### 消息轮询请求数据
- 群组协调、分区再均衡、发送心跳、获取数据
```java
//通过简单的消息轮询向服务器请求数据
try {
    while (true) {
        //消费者必须持续对kafka轮询，否则会被认为死亡，它的分区会被移交给其它消费者，超时时间100毫秒
        ConsumerRecords<String, String> records = consumer.poll(100);
        //遍历poll()返回的消息列表
        for (ConsumerRecord<String, String> record : records) {
            log.debug("topic = %s, partition = %s, offset = %s, customer = %s, country = %s \n, record.topic(), record.partition(), record.offset(), record.key(), record.value()");
            int updatedCount = 1;
            if (custCountryMap.countainsValue(record.value())) {
                updatedCount = custCountryMap.get(record.value()) + 1;
            }
            custCountryMap.put(record.value(), updateCount);
            JSONObject json = new JSONObject(custCountryMap);
            //真实场景保存到数据存储系统
            System.out.println(json.toString(4));
        }
    }
} finally {
    //关闭消费者
   consumer.close();
}
```

[返回目录](#目录)

## 消费者的配置
- 消费者其它配置参数
    - **`fetch.min.bytes`**：指定消费者从服务器获取记录的最小字节数（数据量达到时才返回给消费者）。
    - **`fetch.max.wait.ms`**：指定broker的等待时间（默认500ms）。
    - **`max.partition.fetch.bytes`**：指定服务器从每个分区返回给消费者的最大字节数（默认1MB）。
    - **`session.timeout.ms`**：指定消费者被认为死亡之前可以与服务器断开连接的时间，（默认3s）。
    - **`auto.offset.reset`**：指定消费者在读取一个没有偏移量的分区或者偏移量无效的情况下该如何处理（默认latest，消费者从最新的记录开始读取数据。earliest表示消费者从起始位置开始读取分区记录）。
    - **`enable.auto.commit`**：指定消费者是否自动提交偏移量（默认true）。
    - **`partition.assignment.strategy`**：设置分区策略，哪些分区会被分配给哪些消费者。
    - **`Range`**：把主题若干连续分区分给消费者。
    - **`RoundRobin`**：把主题所有分区逐个分配给消费者。
    - **`client.id`**：任意字符串，broker用来识别从客户端发来的消息。
    - **`max.poll.records`**：用于控制单次调用call()方法能返回的记录数量，可以控制轮询需要处理的数据量。
    - **`receive.buffer.bytes` | `send.buffer.bytes`**：设置TCP缓冲区大小。

[返回目录](#目录)

## 提交和偏移量
- **提交**：更新分区当前位置的操作。
- **自动提交**：`enable.auto.commit=true`
- **提交当前偏移量**：`consumer.commitSync();`
- **异步提交**：`consumer.commitAsync();`
- **提交特定偏移量**

[返回目录](#目录)

## 再均衡监听器
- 消费者在退出和进行分区再均衡前会做一些清理工作。在调用subscribe()方法时传入ConsumerRebalanceListener对象。
## 退出
- 调用**`consumer.wakeup()`**
## 反序列化器
## 独立消费者(没有群组的消费者)

[返回目录](#目录)

# 事务
## 消息传输保障
- 最多一次：消息可能会丢失，但不会重复传输。
- 最少一次：消息不会丢失，但可能重复传输。
- 只有一次：每条消息仅且一次传输。

## 幂等
- 幂等：对接口的多次调用所产生的结果总是一致的。
- Kafka幂等性可以避免生产者重试时重复写入消息。
- 开启幂等性功能：
    - 设置生产者客户端参数：`properties.put("enable.idempotence", true)`
    - 或者：`properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true)`

## 事务实现


[返回目录](#目录)
