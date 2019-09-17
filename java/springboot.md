# SpringBoot
SpringBoot是一个简化Spring开发的框架。可以用来快速构建和监护spring应用开发
## 一、起步依赖
代码位置：src/main/java  
资源位置：src/main/resources；静态资源：src/main/resources/static  
测试代码位置：src/test/java  
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

## 二、自动配置
**Spring Boot能从多种属性源获得属性（优先级高到低），包括：**  
(1) 命令行参数  
(2) java:comp/env里的JNDI属性  
(3) JVM系统属性  
(4) 操作系统环境变量  
(5) 随机生成的带random.*前缀的属性（在设置其他属性时，可以引用它们，比如${random.long}）  
(6) 应用程序以外的application.properties或者appliaction.yml文件  
(7) 打包在应用程序内的application.properties或者appliaction.yml文件  
(8) 通过@PropertySource标注的属性源  
(9) 默认属性  
**application.properties和application.yml文件能放在以下四个位置（优先级高到低）：**  
(1) 外置，在相对于应用程序运行目录的/config子目录里。  
(2) 外置，在应用程序运行的目录里。  
(3) 内置，在config包内。  
(4) 内置，在Classpath根目录。  
**同一优先级位置同时有application.properties和application.yml，那么application.yml里的属性会覆盖application.properties里的属性。**  
#### 使用Profile配置：  
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
**使用特定Profile文件**  
命名格式：application-{profile}.properties 或 application-{profile}.yml  
如：application-dev.yml、application-prod.yml、application-test.yml  
**使用多Profile YAML文件进行配置**  
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

## 三、Actuator

## 四、命令行界面

## 注解
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
  
#### SpringBoot提供的自动配置中使用的条件化注解：  
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
