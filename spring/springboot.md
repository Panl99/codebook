
# 目录
- [项目构建](#项目构建)
- [起步依赖](#起步依赖)
- [自动配置](#自动配置)
- [自定义配置](#自定义配置)
    - [覆盖SpringBoot自动配置-TODO](#覆盖SpringBoot自动配置)
    - [通过属性文件外置配置](#通过属性文件外置配置)
        - [自动配置微调](#自动配置微调)
        - [应用程序Bean的配置外置-TODO](#应用程序Bean的配置外置)
        - [使用Profile进行配置](#使用Profile进行配置)
- [Actuator：监控与管理](#Actuator监控与管理)
- [注解](#注解)
    - [SpringBoot提供的自动配置中使用的条件化注解](#SpringBoot提供的自动配置中使用的条件化注解)

[返回目录](#目录)


**SpringBoot是一个简化Spring开发的框架。可以用来快速构建和监护spring应用开发。**

# 项目构建
- 代码位置：src/main/java
- 资源位置：src/main/resources
    - 静态资源：src/main/resources/static
    - 模板文件：src/main/resources/templates
- 测试代码位置：src/test/java

# 起步依赖
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.test.mall</groupId>
    <artifactId>mall</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>
	<!--<packaging>jar</packaging>-->

    <modules>
        <module>mall-test</module>
    </modules>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.3.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
	<!--起步依赖-->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```

[返回目录](#目录)

# 自动配置
- 每当应用启动时，SpringBoot的自动配置都会自动做出将近200个涵盖安全、集成、持久化、Web开发等方面的配置选择，避免自己写配置。

# 自定义配置
## 覆盖SpringBoot自动配置
// TODO

[返回目录](#目录)

## 通过属性文件外置配置
- SpringBoot自动配置的Bean提供300多个用于微调的属性，调整设置时，只要在环境变量、Java系统属性、JNDI、命令行参数、属性文件中进行指定就可以了。
    - 比如：命令行禁用Banner`java -jar ***.jar --spring.main.show-banner=false`
- **SpringBoot多属性源加载顺序(优先级高到低)：**  
(1) 命令行参数  
(2) java:comp/env里的JNDI属性  
(3) JVM系统属性  
(4) 操作系统环境变量  
(5) 随机生成的带random.*前缀的属性（在设置其他属性时，可以引用它们，比如${random.long}）  
(6) 应用程序以外的application.properties或者appliaction.yml文件  
(7) 打包在应用程序内的application.properties或者appliaction.yml文件  
(8) 通过@PropertySource标注的属性源  
(9) 默认属性  
- **application.properties和application.yml文件能放在以下四个位置（优先级高到低）：**  
(1) 外置，在相对于应用程序运行目录的/config子目录里。  
(2) 外置，在应用程序运行的目录里。  
(3) 内置，在config包内。  
(4) 内置，在Classpath根目录。  
- **同一优先级位置同时有application.properties和application.yml，那么application.yml里的属性会覆盖application.properties里的属性。**

[返回目录](#目录)

### 自动配置微调
- **禁用模板缓存**：Thymeleaf模板默认缓存可以提升应用性能，应用重启模板配置变更才会生效。
    - 可以设置命令行参数：`java -jar xxx.jar --spring.thymeleaf.cache=false`
    - 或者配置application.yml每次运行都会禁用缓存：
        ```yaml
        spring:
          thymeleaf:
            cache: false
        ```
    - 通过环境变量禁用Thymeleaf缓存：`export spring_thymeleaf_cache=false`
- **配置嵌入式服务器**：运行SpringBoot应用时，应用会启动一个嵌入式的服务器（默认Tomcat），监听8080端口。如果有多个应用同时运行时就要设置监听端口防止冲突。
    - 命令行：`java -jar xxx.jar --server.port=8000`
    - application.yml：
        ```yaml
        server:
          port: 8000
        ```
- **配置日志**：SpringBoot默认使用Logback记录日志。
    - 若使用Log4j2：排除默认日志依赖，添加Log4j2依赖
        ```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
      
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
        </dependency>
        ```
    - 在`src/main/resources`下创建文件`logback.xml`：可以控制日志配置
        ```xml
        <configuration>
          <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
            <encoder>
              <pattern>
                %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
              </pattern>
            </encoder>
          </appender>
          <logger name="root" level="INFO" />
          <root level="INFO">
            <appender-ref ref="STDOUT" />
          </root>
        </configuration>
        ```
    - 使用application.yml配置，设置根日志级别WARN、SpringSecurity为DEBUG，输出日志到`var/logs/readinglist.log`，默认10M切分一次：
        ```yaml
        logging:
          path: /var/logs/
          file: readinglist.log
          level:
            root: WARN
            org:
              springframework:
                security: DEBUG
        ```
- **配置数据源**：
    - 使用application.yml配置：
        ```yaml
        spring:
          datasource:
            url: jdbc:mysql://localhost/readinglist
            username: dbuser
            password: dbpass
            driver-class-name: com.mysql.jdbc.Driver #指定JDBC驱动,一般不需要
        ```

[返回目录](#目录)

### 应用程序Bean的配置外置
//TODO

[返回目录](#目录)
  
### 使用Profile进行配置
- 向application.yml里添加spring.profiles.active属性：
```
spring:
  profiles:
    active: prod
```
- 命令行：  
```
java -jar xxx.jar --spring.profiles.active=prod
```
**多环境配置**  
命名格式：application-{profile}.properties 或 application-{profile}.yml  
- 开发环境：application-dev.yml
- 生产环境：application-prod.yml
- 测试环境：application-test.yml

**同一YAML文件进行多环境配置**  
```
logging:
  level:
    root: INFO
---
spring:
  profiles: dev
  
logging:
  level:
    root: DEBUG
---
spring:
  profiles: prod
  
logging:
  path: /tmp/
  file: BookWorm.log
  level:
    root: WARN
```

[返回目录](#目录)

# Actuator监控与管理
- **添加依赖**  
    ```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    ```
- **Actuator提供13个端点**  

HTTP方法 | 路径 | 描述  
---|---|---  
GET | /autoconfig | 提供了一份自动配置报告，记录哪些自动配置条件通过了，哪些没通过
GET | /configprops | 描述配置属性（包含默认值）如何注入Bean
GET | /beans | 描述应用程序上下文里全部的Bean，以及它们的关系
GET | /dump | 获取线程活动的快照
GET | /env | 获取全部环境属性
GET | /env/{name} | 根据名称获取特定的环境属性值
GET | /health | 报告应用程序的健康指标，这些值由HealthIndicator的实现类提供
GET | /info | 获取应用程序的定制信息，这些信息由info打头的属性提供
GET | /mappings | 描述全部的URI路径，以及它们和控制器（包含Actuator端点）的映射关系
GET | /metrics | 报告各种应用程序度量信息，比如内存用量和HTTP请求计数
GET | /metrics/{name} | 报告指定名称的应用程序度量值
GET | /trace | 提供基本的HTTP请求跟踪信息（时间戳、HTTP头等）
POST | /shutdown | 关闭应用程序，要求endpoints.shutdown.enabled设置为true  

- **查看配置明细**  
向/beans（在本地运行时是`http://localhost:8080/beans`）发起GET请求后，返回一个描述每个Bean信息的JSON文档。  
返回的JSON包含信息：
    - bean：Spring应用程序上下文中的Bean名称或ID。
    - resource：.class文件的具体路径。
    - dependencies：依赖的Bean名称。
    - scope：Bean的作用域（通常是单例，这也是默认作用域）。
    - type：Bean的Java类型。

[返回目录](#目录)

# 注解
注解 | 位置 | 使用 | 作用  
-|-|-|-  
@SpringBootApplication | 类上 |  | 开启Spring的组件扫描和SpringBoot的自动配置功能
@Entity | 类上 |  | 表明它是一个JPA（java持久化接口）实体
@Id | 字段上 |  | 说明这个字段是实体的唯一标识
@GeneratedValue | 字段上 | @GeneratedValue(strategy=GenerationType.AUTO) | 这个字段的值是自动生成的
@Controller | 类上 |  | 这样组件扫描会自动将其注册为Spring应用程序上下文里的一个Bean
@RequestMapping | 类上 | @RequestMapping(value="/{reader}", method=RequestMethod.GET) | 将其中所有的处理器方法都映射到了“/”这个URL路径上
@Configuration | 类上 |  | 从其他配置类里导入了一些额外配置
@Conditional | | |
@ConfigurationProperties | 类上 | @ConfigurationProperties(prefix="amazon") | 说明该Bean的属性应该是（通过setter方法）从配置属性值注入的
@Profile | 类上 | @Profile("prod") | @Profile注解要求运行时激活prod Profile，从而应用该配置

[返回目录](#目录)

## SpringBoot提供的自动配置中使用的条件化注解
条件化注解 | 配置生效条件  
-|-  
@ConditionalOnBean | 配置了某个特定Bean
@ConditionalOnMissingBean | 没有配置特定的Bean
@ConditionalOnClass | Classpath里有指定的类，@ConditionalOnClass({ TestA.class, TestB.class })
@ConditionalOnMissingClass | Classpath里缺少指定的类
@ConditionalOnExpression | 给定的Spring Expression Language（SpEL）表达式计算结果为true
@ConditionalOnJava | Java的版本匹配特定值或者一个范围值
@ConditionalOnJndi | 参数中给定的JNDI位置必须存在一个，如果没有给参数，则要有JNDI InitialContext
@ConditionalOnProperty | 指定的配置属性要有一个明确的值
@ConditionalOnResource | Classpath里有指定的资源
@ConditionalOnWebApplication | 这是一个Web应用程序
@ConditionalOnNotWebApplication | 这不是一个Web应用程序

[返回目录](#目录)