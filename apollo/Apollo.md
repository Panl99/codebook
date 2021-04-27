# Apollo概述

## Apollo简介

Apollo是携程开源的配置管理中心，能集中管理应用不同环境、不同集群的配置，配置修改后能实时推送到应用端(支持回调)，并且具备规范的权限、流程治理等特性。

Apollo支持4个维度管理Key-Value格式的配置：

1. application (应用)
2. environment (环境)
3. cluster (集群)
4. namespace (命名空间)

Apollo服务端开发使用的是Java，基于Spring Cloud和Spring Boot框架。客户端目前提供了Java和.Net两种实现。

[https://github.com/ctripcorp/apollo](https://github.com/ctripcorp/apollo)

## Apollo特点

- **统一管理不同环境、不同集群的配置**
  - Apollo提供了一个统一界面集中式管理不同环境（environment）、不同集群（cluster）、不同命名空间（namespace）的配置。
  - 同一份代码部署在不同的集群，可以有不同的配置，比如zookeeper的地址等
  - 通过命名空间（namespace）可以很方便地支持多个不同应用共享同一份配置，同时还允许应用对共享的配置进行覆盖
- **配置修改实时生效（热发布）**
  - 用户在Apollo修改完配置并发布后，客户端能实时（1秒）接收到最新的配置，并通知到应用程序
- **版本发布管理**
  - 所有的配置发布都有版本概念，从而可以方便地支持配置的回滚
- **灰度发布**
  - 支持配置的灰度发布，比如点了发布后，只对部分应用实例生效，等观察一段时间没问题后再推给所有应用实例
- **权限管理、发布审核、操作审计**
  - 应用和配置的管理都有完善的权限管理机制，对配置的管理还分为了编辑和发布两个环节，从而减少人为的错误。
  - 所有的操作都有审计日志，可以方便地追踪问题
- **客户端配置信息监控**
  - 可以在界面上方便地看到配置在被哪些实例使用
- **提供Java原生客户端**
  - 提供了Java的原生客户端，方便应用集成
  - 支持Spring Placeholder, Annotation和Spring Boot的ConfigurationProperties，方便应用使用（需要Spring 3.1.1+）
  - 同时提供了Http接口，非Java应用也可以方便地使用
- **提供开放平台API**
  - Apollo自身提供了比较完善的统一配置管理界面，支持多环境、多数据中心配置管理、权限、流程治理等特性。不过Apollo出于通用性考虑，不会对配置的修改做过多限制，只要符合基本的格式就能保存，不会针对不同的配置值进行针对性的校验，如数据库用户名、密码，Redis服务地址等
  - 对于这类应用配置，Apollo支持应用方通过开放平台API在Apollo进行配置的修改和发布，并且具备完善的授权和权限控制
- **部署简单**
  - 配置中心作为基础服务，可用性要求非常高，这就要求Apollo对外部依赖尽可能地少
  - 目前唯一的外部依赖是MySQL，所以部署非常简单，只要安装好Java和MySQL就可以让Apollo跑起来
  - Apollo还提供了打包脚本，一键就可以生成所有需要的安装包，并且支持自定义运行时参数

## Apollo基本概念

### 配置的基本属性

- **配置是独立于程序的只读变量**
  - 配置首先是独立于程序的，同一份程序在不同的配置下会有不同的行为。
  - 其次，配置对于程序是只读的，程序通过读取配置来改变自己的行为，但是程序不应该去改变配置。
- **配置伴随应用的整个生命周期**
  - 配置贯穿于应用的整个生命周期，应用在启动时通过读取配置来初始化，在运行时根据配置调整行为。
- **配置可以有多种加载方式**
  - 配置也有很多种加载方式，常见的有程序内部hard code，配置文件，环境变量，启动参数，基于数据库等
- **配置需要治理**
  - 权限控制
    - 由于配置能改变程序的行为，不正确的配置甚至能引起灾难，所以对配置的修改必须有比较完善的权限控制
  - 不同环境、集群配置管理
    - 同一份程序在不同的环境（开发，测试，生产）、不同的集群（如不同的数据中心）经常需要有不同的配置，所以需要有完善的环境、集群配置管理
  - 框架类组件配置管理
    - 还有一类比较特殊的配置 - 框架类组件配置，比如CAT客户端的配置。
    - 虽然这类框架类组件是由其他团队开发、维护，但是运行时是在业务实际应用内的，所以本质上可以认为框架类组件也是应用的一部分。
    - 这类组件对应的配置也需要有比较完善的管理方式。

### Apollo核心概念

1. **application (应用)**
   - 就是实际使用配置的应用，Apollo客户端在运行时需要知道当前应用是谁，从而可以去获取对应的配置
   - 每个应用都需要有唯一的身份标识 -- `appId`，应用身份是跟着代码走的，所以需要在代码中配置。
2. **environment (环境)**
   - 配置对应的环境，Apollo客户端在运行时需要知道当前应用处于哪个环境，从而可以去获取应用的配置
   - 环境和代码是无关的，同一份代码部署在不同的环境就应该能够获取到不同环境的配置
   - 所以环境默认是通过读取机器上的配置（`server.properties`中的`env`属性）指定的，为了开发方便，也支持运行时通过`System Property`等指定。
3. **cluster (集群)**
   - 一个应用下不同实例的分组，比如典型的可以按照数据中心分，把上海机房的应用实例分为一个集群，把北京机房的应用实例分为另一个集群。
   - 对不同的cluster，同一个配置可以有不一样的值，如zookeeper地址。
   - 集群默认是通过读取机器上的配置（`server.properties`中的`idc`属性）指定的，也支持运行时通过`System Property`指定。
4. **namespace (命名空间)**
   - 一个应用下不同配置的分组，是配置项的集合，类似于一个配置文件，不同类型的配置存放在不同的文件中，如数据库配置文件，RPC配置文件，应用自身的配置文件等
   - 应用可以直接读取到公共组件的配置namespace，如DAL，RPC等
   - 应用也可以通过继承公共组件的配置namespace来对公共组件的配置做调整，如DAL的初始数据库连接数。



# Apollo使用

## Apollo安装

### Quick Start快速安装

Quick Start只针对本地测试使用，如果要部署到生产环境，参考[分布式部署指南](#分布式部署指南)。

1. 准备：

   - Git，Quick Start需要有bash环境
   - Java 1.8+ （检查：java -version）
   - MySQL 5.6.5+（检查：SHOW VARIABLES WHERE Variable_name = 'version';）
   - 下载Quick Start安装包：[https://github.com/nobodyiam/apollo-build-scripts](https://github.com/nobodyiam/apollo-build-scripts)
   - 如果想修改源码重新打包：
     - 修改apollo-configservice, apollo-adminservice和apollo-portal的pom.xml，注释掉spring-boot-maven-plugin和maven-assembly-plugin
     - 在根目录下执行`mvn clean package -pl apollo-assembly -am -DskipTests=true`
     - 复制apollo-assembly/target下的jar包，rename为apollo-all-in-one.jar

2. 创建数据库：

   Apollo服务端共需要两个数据库：`ApolloPortalDB`和`ApolloConfigDB`

   - 创建`ApolloPortalDB`：

     - 通过MySQL客户端导入[sql/apolloportaldb.sql](https://github.com/nobodyiam/apollo-build-scripts/blob/master/sql/apolloportaldb.sql)即可。`source /your_local_path/sql/apolloportaldb.sql`
     - 导入成功后，可以通过执行以下sql语句来验证：```select `Id`, `AppId`, `Name` from ApolloPortalDB.App;```

   - 创建`ApolloConfigDB`：

     - 通过MySQL客户端导入[sql/apolloconfigdb.sql](https://github.com/nobodyiam/apollo-build-scripts/blob/master/sql/apolloconfigdb.sql)即可。`source /your_local_path/sql/apolloconfigdb.sql`
     - 导入成功后，可以通过执行以下sql语句来验证：``` select `NamespaceId`, `Key`, `Value`, `Comment` from ApolloConfigDB.Item;```

   - 配置数据库连接信息：

     - 编辑[demo.sh](https://github.com/nobodyiam/apollo-build-scripts/blob/master/demo.sh)，修改`ApolloPortalDB`和`ApolloConfigDB`相关的数据库连接串信息。

     ```sh
     #apollo config db info
     apollo_config_db_url="jdbc:mysql://localhost:3306/ApolloConfigDB?characterEncoding=utf8&serverTimezone=Asia/Shanghai"
     apollo_config_db_username=用户名
     apollo_config_db_password=密码（如果没有密码，留空即可）
     
     # apollo portal db info
     apollo_portal_db_url="jdbc:mysql://localhost:3306/ApolloPortalDB?characterEncoding=utf8&serverTimezone=Asia/Shanghai"
     apollo_portal_db_username=用户名
     apollo_portal_db_password=密码（如果没有密码，留空即可）
     ```
注意：不要修改demo.sh的其它部分

3. 启动Apollo配置中心

   - Quick Start脚本会在本地启动3个服务，分别使用8070, 8080, 8090端口，确保这3个端口当前没有被使用。

   - 执行脚本启动：`./demo.sh start`  当看到如下输出后，就说明启动成功了！

   ```sh
   ==== starting service ====
   Service logging file is ./service/apollo-service.log
   Started [10768]
   Waiting for config service startup.......
   Config service started. You may visit http://localhost:8080 for service status now!
   Waiting for admin service startup....
   Admin service started
   ==== starting portal ====
   Portal logging file is ./portal/apollo-portal.log
   Started [10846]
   Waiting for portal startup......
   Portal started. You can visit http://localhost:8070 now!
   ```

4. 使用Apollo

   - 打开[http://localhost:8070](http://localhost:8070/)，用户名：Apollo，密码：admin

   - 运行客户端程序：

     - 一个简单的[Demo客户端](https://github.com/ctripcorp/apollo/blob/master/apollo-demo/src/main/java/com/ctrip/framework/apollo/demo/api/SimpleApolloConfigDemo.java)来演示从Apollo配置中心获取配置。
     - 运行`./demo.sh client`启动Demo客户端
     - 看到提示：

     ```sh
     Apollo Config Demo. Please input key to get the value. Input quit to exit.
     >
     ```

     - 输入`timeout`，会看到如下信息：

     ```sh
     > timeout
     > [SimpleApolloConfigDemo] Loading key : timeout with value: 100
     ```

   - 调试：

     - 修改`client/log4j2.xml`中的level为DEBUG

     ```xml
     <logger name="com.ctrip.framework.apollo" additivity="false" level="trace">
         <AppenderRef ref="Async" level="DEBUG"/>
     </logger>
     ```



### [Docker方式部署Quick Start](https://ctripcorp.github.io/apollo/#/zh/deployment/quick-start-docker)



### [分布式部署指南](https://ctripcorp.github.io/apollo/#/zh/deployment/distributed-deployment-guide)



## Apollo实战

### 客户端获取配置

```java
Config config = ConfigService.getAppConfig();
Integer defaultRequestTimeout = 200;
Integer requestTimeout = config.getIntProperty("requestTimeout", defaultRequestTimeout);
```

### 客户端监听配置变化

```java
Config config = ConfigService.getAppConfig();
config.addChangeListener(new ConfigChangeListener() {
  @Override
  public void onChange(ConfigChangeEvent changeEvent) {
    for (String key : changeEvent.changedKeys()) {
      ConfigChange change = changeEvent.getChange(key);
      System.out.println(String.format(
        "Found change - key: %s, oldValue: %s, newValue: %s, changeType: %s",
        change.getPropertyName(), change.getOldValue(),
        change.getNewValue(), change.getChangeType()));
     }
  }
});
```

### Spring集成Apollo

```java
@Configuration
@EnableApolloConfig
public class AppConfig {}
```

```java
@Component
public class SomeBean {
    //timeout的值会自动更新
    @Value("${request.timeout:200}")
    private int timeout;
}
```

### 自定义Cluster--TODO

### 自定义Namespace--TODO



# Apollo原理

## Apollo源码分析

[芋道源码--Apollo源码解析](https://www.iocoder.cn/categories/Apollo/)，验证码：coke



## Apollo模块原理

### Apollo总体设计

- Config Service提供配置的读取、推送等功能，服务对象是Apollo客户端
- Admin Service提供配置的修改、发布等功能，服务对象是Apollo Portal（管理界面）
- Config Service和Admin Service都是多实例、无状态部署，所以需要将自己注册到Eureka中并保持心跳
- 在Eureka之上我们架了一层Meta Server用于封装Eureka的服务发现接口
- Client通过域名访问Meta Server获取Config Service服务列表（IP+Port），而后直接通过IP+Port访问服务，同时在Client侧会做load balance、错误重试
- Portal通过域名访问Meta Server获取Admin Service服务列表（IP+Port），而后直接通过IP+Port访问服务，同时在Portal侧会做load balance、错误重试
- 为了简化部署，我们实际上会把Config Service、Eureka和Meta Server三个逻辑角色部署在同一个JVM进程中

**为什么采用Eureka作为服务注册中心，而不是使用传统的zk、etcd呢？**

- 它提供了完整的Service Registry和Service Discovery实现
  - 首先是提供了完整的实现，并且也经受住了Netflix自己的生产环境考验，相对使用起来会比较省心。
- 和Spring Cloud无缝集成
  - Apollo本身就使用了Spring Cloud和Spring Boot，同时Spring Cloud还有一套非常完善的开源代码来整合Eureka，所以使用起来非常方便。
  - 另外，Eureka还支持在应用自身的容器中启动，也就是说应用启动完之后，既充当了Eureka的角色，同时也是服务的提供者。这样就极大的提高了服务的可用性。
  - **这一点是我们选择Eureka而不是zk、etcd等的主要原因，为了提高配置中心的可用性和降低部署复杂度，我们需要尽可能地减少外部依赖。**
- Open Source
  - 最后一点是开源，由于代码是开源的，所以非常便于了解它的实现原理和排查问题。

### 各模块概要介绍

**Config Service**

- 提供配置获取接口
- 提供配置更新推送接口（基于Http long polling）
  - 服务端使用[Spring DeferredResult](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/context/request/async/DeferredResult.html)实现异步化，从而大大增加长连接数量
  - 目前使用的tomcat embed默认配置是最多10000个连接（可以调整），使用了4C8G的虚拟机实测可以支撑10000个连接，所以满足需求（一个应用实例只会发起一个长连接）。
- 接口服务对象为Apollo客户端

**Admin Service**

- 提供配置管理接口
- 提供配置修改、发布等接口
- 接口服务对象为Portal

**Meta Server**

- Portal通过域名访问Meta Server获取Admin Service服务列表（IP+Port）
- Client通过域名访问Meta Server获取Config Service服务列表（IP+Port）
- Meta Server从Eureka获取Config Service和Admin Service的服务信息，相当于是一个Eureka Client
- 增设一个Meta Server的角色主要是为了封装服务发现的细节，对Portal和Client而言，永远通过一个Http接口获取Admin Service和Config Service的服务信息，而不需要关心背后实际的服务注册和发现组件
- Meta Server只是一个逻辑角色，在部署时和Config Service是在一个JVM进程中的，所以IP、端口和Config Service一致

**Eureka**

- 基于[Eureka](https://github.com/Netflix/eureka)和[Spring Cloud Netflix](https://cloud.spring.io/spring-cloud-netflix/)提供服务注册和发现
- Config Service和Admin Service会向Eureka注册服务，并保持心跳
- 为了简单起见，目前Eureka在部署时和Config Service是在一个JVM进程中的（通过Spring Cloud Netflix）

**Portal**

- 提供Web界面供用户管理配置
- 通过Meta Server获取Admin Service服务列表（IP+Port），通过IP+Port访问服务
- 在Portal侧做load balance、错误重试

**Client**

Apollo提供的客户端程序，为应用提供配置获取、实时更新等功能

- 通过Meta Server获取Config Service服务列表（IP+Port），通过IP+Port访问服务
- 在Client侧做load balance、错误重试

### Apollo客户端的实现原理

1. 客户端和服务端保持了一个长连接，从而能第一时间获得配置更新的推送。
2. 客户端还会定时从Apollo配置中心服务端拉取应用的最新配置。
   - 这是一个fallback机制，为了防止推送机制失效导致配置不更新
   - 客户端定时拉取会上报本地版本，所以一般情况下，对于定时拉取的操作，服务端都会返回304 - Not Modified
   - 定时频率默认为每5分钟拉取一次，客户端也可以通过在运行时指定System Property: `apollo.refreshInterval`来覆盖，单位为分钟。
3. 客户端从Apollo配置中心服务端获取到应用的最新配置后，会保存在内存中
4. 客户端会把从服务端获取到的配置在本地文件系统缓存一份
   - 在遇到服务不可用，或网络不通的时候，依然能从本地恢复配置
5. 应用程序从Apollo客户端获取最新的配置、订阅配置更新通知

**配置更新推送实现**

前面提到了Apollo客户端和服务端保持了一个长连接，从而能第一时间获得配置更新的推送。

长连接实际上是通过Http Long Polling实现的，具体而言：

- 客户端发起一个Http请求到服务端
- 服务端会保持住这个连接60秒
  - 如果在60秒内有客户端关心的配置变化，被保持住的客户端请求会立即返回，并告知客户端有配置变化的namespace信息，客户端会据此拉取对应namespace的最新配置
  - 如果在60秒内没有客户端关心的配置变化，那么会返回Http状态码304给客户端
- 客户端在收到服务端请求后会立即重新发起连接，回到第一步

考虑到会有数万客户端向服务端发起长连，在服务端使用了async servlet(Spring DeferredResult)来服务Http Long Polling请求。

### 可用性方面

配置中心作为基础服务，可用性要求非常高，下面的表格描述了不同场景下Apollo的可用性：

| 场景                   | 影响                                 | 降级                                                         | 原因                                                         |
| ---------------------- | ------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 某台config service下线 | 无影响                               |                                                              | Config service无状态，客户端重连其它config service           |
| 所有config service下线 | 客户端无法读取最新配置，Portal无影响 | 客户端重启时,可以读取本地缓存配置文件。如果是新扩容的机器，可以从其它机器上获取已缓存的配置文件，具体信息可以参考[Java客户端使用指南 - 1.2.3 本地缓存路径](https://ctripcorp.github.io/#/zh/usage/java-sdk-user-guide?id=_123-本地缓存路径) |                                                              |
| 某台admin service下线  | 无影响                               |                                                              | Admin service无状态，Portal重连其它admin service             |
| 所有admin service下线  | 客户端无影响，portal无法更新配置     |                                                              |                                                              |
| 某台portal下线         | 无影响                               |                                                              | Portal域名通过slb绑定多台服务器，重试后指向可用的服务器      |
| 全部portal下线         | 客户端无影响，portal无法更新配置     |                                                              |                                                              |
| 某个数据中心下线       | 无影响                               |                                                              | 多数据中心部署，数据完全同步，Meta Server/Portal域名通过slb自动切换到其它存活的数据中心 |
| 数据库宕机             | 客户端无影响，Portal无法更新配置     | Config Service开启[配置缓存](https://ctripcorp.github.io/#/zh/deployment/distributed-deployment-guide?id=_323-config-servicecacheenabled-是否开启配置缓存)后，对配置的读取不受数据库宕机影响 |                                                              |



# Apollo优化

## Apollo参数优化



## Apollo二次开发

