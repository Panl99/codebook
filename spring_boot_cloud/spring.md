# Spring（[Spring常见问题](https://mp.weixin.qq.com/s/u3U1l3HvG6Dm6UJRB1epIA)）
Spring是一个轻量级的开发框架，旨在降低应用开发的复杂度，具有分层体系结构，可以集成其他框架。

## 为什么使用Spring //TODO

## Spring模块
- Spring Core：基础,可以说 Spring 其他所有的功能都需要依赖于该类库。主要提供IOC依赖注入功能。
- Spring Aspects：该模块为与AspectJ的集成提供支持。
- Spring AOP：提供了面向切面的编程实现。
- Spring JDBC：Java数据库连接。
- Spring JMS：Java消息服务。
- Spring ORM：用于支持Hibernate等ORM工具。
- Spring Web：为创建Web应用程序提供支持。
- Spring Test：提供了对 JUnit 和 TestNG 测试的支持

#### Spring IOC（Inverse of Control:控制反转）
应用本身不负责依赖对象的创建和维护，而是由外部容器负责，控制权由应用转移到外部容器。
#### 依赖注入
在运行期间，由外部容器动态的将依赖对象注入到组件中  
**Spring依赖注入方式：**  
- 构造函数注入  
- setter注入  
- 注解方式  
#### AOP
将那些与业务无关，却为业务模块所共同调用的逻辑或责任封装起来，便于减少系统的重复代码，降低模块间的耦合度，并有利于未来的可拓展性和可维护性。  
使用动态代理的方式在执行方法前后或出现异常之后加入相关逻辑。  
**用来：**  
- 事务处理  
- 权限控制  
- 日志管理  

## Spring支持的bean作用域 
- singleton：唯一 bean 实例，Spring 中的 bean 默认都是单例的。
- prototype：每次请求都会创建一个新的 bean 实例。
- request：每一次HTTP请求都会产生一个新的bean，该bean仅在当前HTTP request内有效。
- session：每一次HTTP请求都会产生一个新的 bean，该bean仅在当前 HTTP session 内有效。
- ~~global-session：全局session作用域，仅仅在基于portlet的web应用中才有意义，Spring5已经没有了。~~  

## Spring自动装配(AutoWire) //TODO

## Spring MVC流程
- 用户发送请求到前端控制器DispatcherServlet；
- DispatcherServlet收到请求后调用HandlerMapping处理器映射器；
- 处理器映射器根据请求url找到具体的处理器，生成处理器对象和处理器拦截器(如果有则生成)，一并返回给DispatcherServlet；
- DispatcherServlet之后通过HandlerAdapter处理器适配器执行处理器Controller(后端控制器)，执行完后返回ModelAndView；
- DispatcherServlet将ModelAndView传给ViewReslover视图解析器进行解析；
- DispatcherServlet对解析后的View进行渲染，并返回用户。

## Spring中使用的设计模式
- 工厂设计模式：Spring使用工厂模式通过BeanFactory、ApplicationContext创建bean对象。
- 代理设计模式：Spring AOP功能的实现。
- 单例设计模式：Spring中的Bean默认都是单例的。
- 观察者模式：Spring事件驱动模型就是观察者模式很经典的一个应用。
- 适配器模式：Spring AOP的增强或通知(Advice)使用到了适配器模式，spring MVC中也是用到了适配器模式适配Controller。
- ......

## Spring注解 //TODO
注解 | 位置 | 使用 | 作用  
-|-|-|-  
@SpringBootApplication | 类上 |  | 开启Spring的组件扫描和SpringBoot的自动配置功能

//TODO
怎样开启注解装配  
前端控制器和后置处理器的区别  
Spring MVC流程  
Spring MVC核心入口类

# Spring Data JPA 持久化数据
## 添加依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
    <version>2.3.0.RELEASE</version>
</dependency>
```
## 将领域对象标注为实体
//TODO
## 声明 JPA repository
//TODO
#### 自定义 JPA repository
//TODO

# Spring Security
## 添加依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <version>2.3.0.RELEASE</version>
</dependency>
```
## 安全特性
- 所有的HTTP请求路径都需要认证；
- 不需要特定的角色和权限；
- 没有登录页面；
- 认证过程是通过HTTP basic认证对话框实现的；
- 系统只有一个用户，用户名为user。（密码随机生成，被写入应用日志中，大致为： `Using default security password：xxx`）

**要确保应用的安全性，至少还需要配置如下功能：**  
- 通过登录页面来提示用户进行认证，而不是使用HTTP basic认证对话框；
- 提供多个用户，并提供一个注册页面，以便新用户注册进来；
- 对不同的请求路径，执行不同的安全规则。如：主页和注册页不需要认证。

## 配置Spring Security
//TODO
#### 基于内存的用户存储
//TODO
#### 基于JDBC的用户存储
//TODO
#### 基于LDAP作为后端的用户存储
//TODO
#### 自定义用户认证
//TODO

## 保护Web请求