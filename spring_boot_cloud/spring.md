> [Spring常见问题](https://mp.weixin.qq.com/s/u3U1l3HvG6Dm6UJRB1epIA)

# 目录
- [为什么使用Spring](#为什么使用Spring)
- [Spring模块](#Spring模块)
- [IOC](#IOC)
    - [依赖注入](#依赖注入)
- [AOP](#AOP)
- [Spring MVC](#SpringMVC)
    - [SpringMVC流程](#SpringMVC流程)
- [访问数据库](#访问数据库)
- [Web应用开发](#Web应用开发)
    - [使用REST](#使用REST)
    - [Spring Security](#SpringSecurity)
    - [Spring Data JPA](#SpringDataJPA)
- [Spring注解](#Spring注解)
- [Spring中使用的设计模式](#Spring中使用的设计模式)

[返回目录](#目录)

# 为什么使用Spring
**Spring是一个轻量级的开发框架，旨在降低应用开发的复杂度，具有分层体系结构，可以集成其他框架。**

[返回目录](#目录)

# Spring模块
- Spring Core：基础,可以说 Spring 其他所有的功能都需要依赖于该类库。主要提供IOC依赖注入功能。
- Spring Aspects：该模块为与AspectJ的集成提供支持。
- Spring AOP：提供了面向切面的编程实现。
- Spring JDBC：Java数据库连接。
- Spring JMS：Java消息服务。
- Spring ORM：用于支持Hibernate等ORM工具。
- Spring Web：为创建Web应用程序提供支持。
- Spring Test：提供了对 JUnit 和 TestNG 测试的支持

[返回目录](#目录)

# IOC
- Inverse of Control:控制反转
- 应用本身不负责依赖对象的创建和维护，而是由外部容器负责，控制权由应用转移到外部容器。

[返回目录](#目录)

## 依赖注入
在运行期间，由外部容器动态的将依赖对象注入到组件中  
- **Spring依赖注入方式：**  
    - 构造函数注入
    - setter注入
    - 注解方式
        - `@Component`+`@Autowired`，@Component注解就相当于定义了一个Bean，它有一个可选的名称，默认是mailService，即小写开头的类名。@Autowired相当于把指定类型的Bean注入到指定的字段中。和XML配置相比，@Autowired大幅简化了注入，因为它不但可以写在set()方法上，还可以直接写在字段上，甚至可以写在构造方法中。
            ```java
            @Component
            public class UserService {
                @Autowired
                MailService mailService;
            
                ...
            }
            ```

[返回目录](#目录)

# AOP
- 将那些与业务无关，却为业务模块所共同调用的逻辑或责任封装起来，便于减少系统的重复代码，降低模块间的耦合度，并有利于未来的可拓展性和可维护性。  
- 使用动态代理的方式在执行方法前后或出现异常之后加入相关逻辑。  
- **用来：**
    - 事务处理
        - Spring提供的`@Transactional`可以声明在Bean上，表示希望在一个数据库事务中被调用：
            ```java
            @Component
            public class UserService {
                // 有事务:
                @Transactional
                public User createUser(String name) {
                    ...
                }
                // 无事务:
                public boolean isValidName(String name) {
                    ...
                }
                // 有事务:
                @Transactional
                public void updateUser(User user) {
                    ...
                }
            }
            ```
            - 或者直接在class级别注解，表示“所有public方法都被声明”：
                ```java
                @Component
                @Transactional
                public class UserService {
                    ...
                }
                ```     
    - 权限控制
    - 日志管理
        - 不推荐使用：无差别全覆盖，即某个包下面的所有Bean的所有方法都会被这个check()方法拦截。
        - 引入依赖，依赖会自动引入AspectJ，使用AspectJ实现AOP比较方便。
            ```xml
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-aspects</artifactId>
                <version>${spring.version}</version>
            </dependency>
            ```
        - 定义一个LoggingAspect：
            - `@Before`后面的字符串是告诉AspectJ应该在何处执行该方法，这里写的意思是：执行UserService的每个public方法前执行doAccessCheck()代码。
            - `@Around`可以决定是否执行目标方法，因此，我们在doLogging()内部先打印日志，再调用方法，最后打印日志后返回结果。
            - `@Component`表示它本身也是一个Bean。
            - `@Aspect`表示它的`@Before`标注的方法需要注入到UserService的每个public方法执行前，`@Around`标注的方法需要注入到MailService的每个public方法执行前后。
            ```java
            @Aspect
            @Component
            public class LoggingAspect {
                // 在执行UserService的每个方法前执行:
                @Before("execution(public * com.itranswarp.learnjava.service.UserService.*(..))")
                public void doAccessCheck() {
                    System.err.println("[Before] do access check...");
                }
            
                // 在执行MailService的每个方法前后执行:
                @Around("execution(public * com.itranswarp.learnjava.service.MailService.*(..))")
                public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
                    System.err.println("[Around] start " + pjp.getSignature());
                    Object retVal = pjp.proceed();
                    System.err.println("[Around] done " + pjp.getSignature());
                    return retVal;
                }
            }
            ```
        - 给@Configuration类加上一个`@EnableAspectJAutoProxy`注解：
            - Spring的IoC容器看到这个注解，就会自动查找带有`@Aspect`的Bean，然后根据每个方法的`@Before`、`@Around`等注解把AOP注入到特定的Bean中。
            ```java
            @Configuration
            @ComponentScan
            @EnableAspectJAutoProxy
            public class AppConfig {
                ...
            }
            ```
        - 执行代码，我们可以看到以下输出：
            ```
            [Before] do access check...
            [Around] start void com.itranswarp.learnjava.service.MailService.sendRegistrationMail(User)
            Welcome, test!
            [Around] done void com.itranswarp.learnjava.service.MailService.sendRegistrationMail(User)
            [Before] do access check...
            [Around] start void com.itranswarp.learnjava.service.MailService.sendLoginMail(User)
            Hi, Bob! You are logged in at 2020-02-14T23:13:52.167996+08:00[Asia/Shanghai]
            [Around] done void com.itranswarp.learnjava.service.MailService.sendLoginMail(User)
            ```

[返回目录](#目录)

# SpringMVC
## SpringMVC流程
- 用户发送请求到前端控制器DispatcherServlet；
- DispatcherServlet收到请求后调用HandlerMapping处理器映射器；
- 处理器映射器根据请求url找到具体的处理器，生成处理器对象和处理器拦截器(如果有则生成)，一并返回给DispatcherServlet；
- DispatcherServlet之后通过HandlerAdapter处理器适配器执行处理器Controller(后端控制器)，执行完后返回ModelAndView；
- DispatcherServlet将ModelAndView传给ViewReslover视图解析器进行解析；
- DispatcherServlet对解析后的View进行渲染，并返回用户。

[返回目录](#目录)

# 访问数据库

[返回目录](#目录)

# Web应用开发
## 使用REST

[返回目录](#目录)

## SpringSecurity
- 添加依赖
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
        <version>2.3.0.RELEASE</version>
    </dependency>
    ```
- 安全特性
    - 所有的HTTP请求路径都需要认证；
    - 不需要特定的角色和权限；
    - 没有登录页面；
    - 认证过程是通过HTTP basic认证对话框实现的；
    - 系统只有一个用户，用户名为user。（密码随机生成，被写入应用日志中，大致为： `Using default security password：xxx`）
    
    - **要确保应用的安全性，至少还需要配置如下功能：**  
        - 通过登录页面来提示用户进行认证，而不是使用HTTP basic认证对话框；
        - 提供多个用户，并提供一个注册页面，以便新用户注册进来；
        - 对不同的请求路径，执行不同的安全规则。如：主页和注册页不需要认证。

- 配置Spring Security
//TODO
- 基于内存的用户存储
//TODO
- 基于JDBC的用户存储
//TODO
- 基于LDAP作为后端的用户存储
//TODO
- 自定义用户认证
//TODO

[返回目录](#目录)

## SpringDataJPA
- 添加依赖
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
        <version>2.3.0.RELEASE</version>
    </dependency>
    ```
- 将领域对象标注为实体
//TODO
- 声明 JPA repository
//TODO
- 自定义 JPA repository
//TODO

[返回目录](#目录)

# Spring注解
注解 | 位置 | 使用 | 作用  
---|---|---|---  
@SpringBootApplication | 类上 |  | 开启Spring的组件扫描和SpringBoot的自动配置功能

[返回目录](#目录)

# Spring中使用的设计模式
- 工厂设计模式：Spring使用工厂模式通过BeanFactory、ApplicationContext创建bean对象。
- 代理设计模式：Spring AOP功能的实现。
- 单例设计模式：Spring中的Bean默认都是单例的。
- 观察者模式：Spring事件驱动模型就是观察者模式很经典的一个应用。
- 适配器模式：Spring AOP的增强或通知(Advice)使用到了适配器模式，spring MVC中也是用到了适配器模式适配Controller。
- ......

[返回目录](#目录)

## Spring支持的bean作用域 
- singleton：唯一 bean 实例，Spring 中的 bean 默认都是单例的。
- prototype：每次请求都会创建一个新的 bean 实例。
- request：每一次HTTP请求都会产生一个新的bean，该bean仅在当前HTTP request内有效。
- session：每一次HTTP请求都会产生一个新的 bean，该bean仅在当前 HTTP session 内有效。
- ~~global-session：全局session作用域，仅仅在基于portlet的web应用中才有意义，Spring5已经没有了。~~  

## Spring自动装配(AutoWire) //TODO


//TODO
怎样开启注解装配  
前端控制器和后置处理器的区别  
Spring MVC流程  
Spring MVC核心入口类



