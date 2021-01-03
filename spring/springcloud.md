- [Spring Cloud configuration properties](https://docs.spring.io/spring-cloud/docs/2020.0.0/reference/html/configprops.html)
    - [Spring-Cloud-configuration-properties.xlsx](../resources/static/doc/Spring-Cloud-configuration-properties.xlsx)
- [Spring Cloud 2020.0.0](https://docs.spring.io/spring-cloud/docs/2020.0.0/reference/html/)

# 目录
- [Spring Cloud](#SpringCloud)
- [Spring Cloud Config](#SpringCloudConfig)
- [Eureka-2.0已闭源](#Eureka)
- [Consul](#Consul)
- [Hystrix](#Hystrix)
    - [客户端负载均衡模式](#客户端负载均衡模式)
    - [断路器模式](#断路器模式)
        - [断路器实现](#断路器实现)
        - [调用远程资源失败过多断路器设置](#调用远程资源失败过多断路器设置)
    - [后备模式](#后备模式)
        - [构建后备策略](#构建后备策略)
    - [舱壁模式](#舱壁模式)
        - [舱壁模式实现](#舱壁模式实现)
- [Zuul -TODO](#Zuul)
- [Feign](#Feign)
- [Ribbon](#Ribbon)
- [Spring Cloud Bus -TODO](#SpringCloudBus)
- [Spring Cloud Cluster -TODO](#SpringCloudCluster)
- [Spring Cloud Stream -TODO](#SpringCloudStream)

[目录](#目录)

# SpringCloud
- SpringCloud是一个解决微服务架构实施的综合性解决框架，它整合了许多被广泛实践和证明过的框架作为实施的基础部件，并在该体系基础上创建了一些优秀的边缘组件。通过一些简单的注解，就可以快速的在应用中配置一下常用模块并构建庞大的分布式系统。  
- SpringCloud是一个基于SpringBoot实现的微服务架构开发工具，为微服务架构中涉及的配置管理、服务治理、断路器、智能路由、微代理、控制总线、全局锁、决策竞选、分布式会话、集群状态管理等操作提供一种简单的开发方式。

# SpringCloudConfig
- 配置管理工具

# Eureka
- 服务治理组件，包含服务注册中心、服务注册与发现机制的实现

[目录](#目录)

# Consul

[目录](#目录)

# Hystrix
- 容错管理组件，实现断路器模式，帮助服务依赖中出现延迟和为故障提供强大的容错能力
- 客户端弹性模式：远程服务发生错误时保护远程资源的客户端免于崩溃。目的使客户端“快速失败”，避免消耗资源（如：数据库连接、线程池），防止远程服务的问题向客户端的消费者传播。
    - 客户端负载均衡模式
    - 断路器模式
    - 后备模式
    - 舱壁模式
- 依赖：
    ```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        <version>2.2.5.RELEASE</version>
    </dependency>
    <!--下依赖停更，不建议使用-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-hystrix</artifactId>
        <version>1.4.7.RELEASE</version>
    </dependency>
    ```
- 启动注解：`@EnableCircuitBreaker`（用于启动类Application.java）

[目录](#目录)
    
## 客户端负载均衡模式
- 让客户端从服务注册中心查找服务所有实例，然后缓存服务实例的物理位置。
- 当消费者调用该服务实例时，客户端负载均衡器将从它维护的服务实例池中返回实例的一个位置。
- 客户端负载均衡器位于服务客户端和服务消费者之间，可以检测服务实例的健康状态，当检测到服务实例不健康时，会将它从服务实例池中移除，并禁止服务调用访问该实例。
- **[Ribbon](#Ribbon)** 提供客户端负载均衡功能。

## 断路器模式
- 远程服务被调用时间时间过长，断路器会中断此次调用。
- 断路器会监视所有对远程资源的调用，如果对某一远程资源调用失败过多，断路器会快速断开调用，并阻止再次调用失败的远程资源。

### 断路器实现
- 标记方法由Hystrix断路器管理的注解：`@HystrixCommand`
    - 该注解会标示Spring生成一个动态代理包装该方法，并会通过专门用于处理远程调用的线程池来管理对该方法的所有调用。
    - 当调用被`@HystrixCommand`标注的方法超过`1000ms`（默认1秒）时，断路器会中断对该方法的调用。
- 自定义断路器超时时间：`@HystrixCommand(commandProperties = {@HystrixProperty(name = "executin.isolation.thread.timeoutInMilliseconds", value = "3000")})`
    - 设置最大超时时间`3s`。
- 类级属性设置注解：`@DefaultProperties`
    - 例如：类中所有资源超时时间均为10s
        ```java
        @DefaultProperties(
            commandProperties = {
                @HystrixProperty(name = "executin.isolation.thread.timeoutInMilliseconds", value = "10000")
            }
        )
        public class MyService {   }
        ```
    
### 调用远程资源失败过多断路器设置
```java
@HystrixCommand(
    fallbackMethod = "buildFallbackLicenseList",
    threadPoolKey = "licenseByOrgThreadPool",   //线程池名称
    threadPoolProperties = {
         @HystrixProperty(name = "coreSize", value="30"),    //线程池中最大线程数
         @HystrixProperty(name = "maxQueueSize", value="10")    //定义一个位于线程池前的队列，可以对传入的请求排队
    },
    commandProperties = {
         @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"), //断路器跳闸之前，10s之内连续调用数量
         @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75"),  //断路器跳闸之前，调用失败百分比
         @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),   //断路器跳闸之后，Hystrix尝试进行服务调用之前的等待时间
         @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"),   //Hystrix监视服务调用问题的窗口大小，默认10000ms(10s)
         @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5")    //Hystrix在一个监控窗口中维护的度量桶的数量，监视窗口内桶数越多，监控故障时间越低
    }
)
// 如上最后两个配置，Hystrix使用15s的窗口，将统计数据收集到长度为3s的5个桶中。
// 注意：检查的统计窗口越小 且窗口中保留的桶的数量越多，就越会加剧高请求服务的CPU利用率 和内存利用率。
```

[目录](#目录)

## 后备模式
- 远程调用失败时，消费者会使用替代方式来执行操作，而不是生成异常。
- 通常涉及，从另一数据源查询数据，或将请求排队后续再来处理。

### 构建后备策略
- 添加属性`fallbackMethod`：`@HystrixCommand(fallbackMethod = "buildFallbackLicenseList")`
    - 当调用该方法失败时，就会调用后备方法。
- 实现后备方法（必须与原始方法位于同一类中，并且与原始方法的参数一致：原始方法的参数都会传递给后备方法），例如：
    ```java
    private List<License> buildFallbackLicenseList(String organizationId){
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("0000000-00-00000")
                .withOrganizationId( organizationId )
                .withProductName("Sorry no licensing information currently available");
    
        fallbackList.add(license);
        return fallbackList;
    }
    ```

[目录](#目录)

## 舱壁模式
- 应用于必须与多个远程资源交互的服务。
- 使用舱壁模式：可以把远程资源的调用分别分配到各自线程池中，降低了一个缓慢的远程资源调用拖垮整个应用程序的风险。
    - 默认，所有Hystrix命令会共享同一线程池来处理请求。
    - 每个远程资源都是隔离的，并分配给线程池。
    - 一个服务响应缓慢，那么这种服务调用的线程池会饱和并停止处理请求，但其他服务的调用会被分配给其他线程池而不受影响。

### 舱壁模式实现
- 解决：在大量请求下，一个服务出现性能问题导致Java容器所有线程被刷爆，无法再继续处理请求，导致Java容器崩溃。
- 实现隔离的线程池：
    ```java
    @HystrixCommand(
        fallbackMethod = "buildFallbackLicenseList",  //后备方法
        threadPoolKey = "licenseByOrgThreadPool",   //线程池名称
        threadPoolProperties = {
             @HystrixProperty(name = "coreSize", value="30"),    //线程池中最大线程数
             @HystrixProperty(name = "maxQueueSize", value="10")    //定义一个位于线程池前的队列，可以对传入的请求排队
        }
    )
    ```

[目录](#目录)

# Zuul
- 网关组件，提供智能路由、访问过滤等功能

[目录](#目录)

# Feign
- 声明式、模板化的REST客户端，可以更加便捷、优雅的调用HTTP API
- 使用简单：
    - 创建一个接口
    - 给接口添加一些注解
- 启动注解：`@EnableFeignClients`
- 依赖：
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
    <version>2.2.5.RELEASE</version>
</dependency>
```
- [官方文档：https://docs.spring.io/spring-cloud-openfeign/docs/2.2.5.RELEASE/reference/html/](https://docs.spring.io/spring-cloud-openfeign/docs/2.2.5.RELEASE/reference/html/)

[目录](#目录)

# Ribbon
- 客户端负载均衡器。
- 为Ribbon配置服务提供者地址列表后，Ribbon就会基于设置的负载均衡算法，自动的帮助服务消费者去请求。

[目录](#目录)

# SpringCloudBus
- 事件、消息总线，用于传播集群中的状态变化或事件，以触发后续的处理

[目录](#目录)

# SpringCloudCluster
- 针对ZooKeeper、Redis、Hazelcast、Consul的选举算法和通用状态模式的实现

[目录](#目录)

# SpringCloudStream

[目录](#目录)