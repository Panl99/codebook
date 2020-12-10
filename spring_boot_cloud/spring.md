> [Spring常见问题](https://mp.weixin.qq.com/s/u3U1l3HvG6Dm6UJRB1epIA)

# 目录
- [为什么使用Spring](#为什么使用Spring)
- [Spring模块](#Spring模块)
- [IOC](#IOC)
    - [依赖注入](#依赖注入)
- [AOP](#AOP)
- [Spring MVC](#SpringMVC)
    - [SpringMVC流程](#SpringMVC流程)
- [Spring事务](#Spring事务)
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

# Spring事务
## 事务的特性
- 原子性（Atomicity）：事务是一个原子操作，由一系列动作组成。事务的原子性确保动作要么全部完成，要么完全不起作用。
- 一致性（Consistency）：一旦事务完成（不管成功还是失败），系统必须确保它所建模的业务处于一致的状态，而不会是部分完成部分失败。在现实中的数据不应该被破坏。
- 隔离性（Isolation）：可能有许多事务会同时处理相同的数据，因此每个事务都应该与其他事务隔离开来，防止数据损坏。
- 持久性（Durability）：一旦事务完成，无论发生什么系统错误，它的结果都不应该受到影响，这样就能从任何系统崩溃中恢复过来。通常情况下，事务的结果被写到持久化存储器中。

## 事务的传播机制
事务的传播性一般用在事务嵌套的场景，比如一个事务方法里面调用了另外一个事务方法，那么两个方法是各自作为独立的方法提交还是内层的事务合并到外层的事务一起提交，这就是需要事务传播机制的配置来确定怎么样执行。  
常用的事务传播机制如下：  
- `PROPAGATION_REQUIRED` Spring默认的传播机制，能满足绝大部分业务需求，如果外层有事务，则当前事务加入到外层事务，一块提交，一块回滚。如果外层没有事务，新建一个事务执行
- `PROPAGATION_REQUIRES_NEW` 该事务传播机制是每次都会新开启一个事务，同时把外层事务挂起，当当前事务执行完毕，恢复上层事务的执行。如果外层没有事务，执行当前新开启的事务即可
- `PROPAGATION_SUPPORTS` 如果外层有事务，则加入外层事务，如果外层没有事务，则直接使用非事务方式执行。完全依赖外层的事务
- `PROPAGATION_NOT_SUPPORTED` 该传播机制不支持事务，如果外层存在事务则挂起，执行完当前代码，则恢复外层事务，无论是否异常都不会回滚当前的代码
- `PROPAGATION_NEVER` 该传播机制不支持外层事务，即如果外层有事务就抛出异常
- `PROPAGATION_MANDATORY` 与NEVER相反，如果外层没有事务，则抛出异常
- `PROPAGATION_NESTED` 该传播机制的特点是可以保存状态保存点，当前事务回滚到某一个点，从而避免所有的嵌套事务都回滚，即各自回滚各自的，如果子事务没有把异常吃掉，基本还是会引起全部回滚的。

## 事务的隔离级别
事务的隔离级别定义一个事务可能受其他并发务活动活动影响的程度，可以把事务的隔离级别想象为这个事务对于事物处理数据的自私程度。  
在一个典型的应用程序中，多个事务同时运行，经常会为了完成他们的工作而操作同一个数据。并发虽然是必需的，但是会导致以下问题：  
- `脏读（Dirty read）` 脏读发生在一个事务读取了被另一个事务改写但尚未提交的数据时。如果这些改变在稍后被回滚了，那么第一个事务读取的数据就会是无效的。
- `不可重复读（Nonrepeatable read）` 不可重复读发生在一个事务执行相同的查询两次或两次以上，但每次查询结果都不相同时。这通常是由于另一个并发事务在两次查询之间更新了数据。
- `幻读（Phantom reads）` 幻读和不可重复读相似。当一个事务（T1）读取几行记录后，另一个并发事务（T2）插入了一些记录时，幻读就发生了。在后来的查询中，第一个事务（T1）就会发现一些原来没有的额外记录。

在理想状态下，事务之间将完全隔离，从而可以防止这些问题发生。然而，完全隔离会影响性能，因为隔离经常涉及到锁定在数据库中的记录（甚至有时是锁表）。完全隔离要求事务相互等待来完成工作，会阻碍并发。因此，可以根据业务场景选择不同的隔离级别。
- `ISOLATION_DEFAULT`	使用后端数据库默认的隔离级别
- `ISOLATION_READ_UNCOMMITTED`	允许读取尚未提交的更改。可能导致脏读、幻读或不可重复读。
- `ISOLATION_READ_COMMITTED`	（Oracle 默认级别）允许从已经提交的并发事务读取。可防止脏读，但幻读和不可重复读仍可能会发生。
- `ISOLATION_REPEATABLE_READ`	（MYSQL默认级别）对相同字段的多次读取的结果是一致的，除非数据被当前事务本身改变。可防止脏读和不可重复读，但幻读仍可能发生。
- `ISOLATION_SERIALIZABLE`	完全服从ACID的隔离级别，确保不发生脏读、不可重复读和幻影读。这在所有隔离级别中也是最慢的，因为它通常是通过完全锁定当前事务所涉及的数据表来完成的。
  
## 只读
- 如果一个事务只对数据库执行读操作，那么该数据库就可能利用那个事务的只读特性，采取某些优化措施。通过把一个事务声明为只读，可以给后端数据库一个机会来应用那些它认为合适的优化措施。由于只读的优化措施是在一个事务启动时由后端数据库实施的， 因此，只有对于那些具有可能启动一个新事务的传播行为（PROPAGATION_REQUIRES_NEW、PROPAGATION_REQUIRED、 ROPAGATION_NESTED）的方法来说，将事务声明为只读才有意义。
   
## 事务超时
- 为了使一个应用程序很好地执行，它的事务不能运行太长时间。因此，声明式事务的下一个特性就是它的超时。
- 假设事务的运行时间变得格外的长，由于事务可能涉及对数据库的锁定，所以长时间运行的事务会不必要地占用数据库资源。这时就可以声明一个事务在特定秒数后自动回滚，不必等它自己结束。
- 由于超时时钟在一个事务启动的时候开始的，因此，只有对于那些具有可能启动一个新事务的传播行为（PROPAGATION_REQUIRES_NEW、PROPAGATION_REQUIRED、ROPAGATION_NESTED）的方法来说，声明事务超时才有意义。
   
## 回滚规则
- 在默认设置下，事务只在出现运行时异常（runtime exception）时回滚，而在出现受检查异常（checked exception）时不回滚（这一行为和EJB中的回滚行为是一致的）。
- 不过，可以声明在出现特定受检查异常时像运行时异常一样回滚。同样，也可以声明一个事务在出现特定的异常时不回滚，即使特定的异常是运行时异常。

## Spring声明式事务配置参考
事物配置中有哪些属性可以配置?以下只是简单的使用参考  

- 事务的传播性：`@Transactional(propagation=Propagation.REQUIRED)`
- 事务的隔离级别：`@Transactional(isolation = Isolation.READ_UNCOMMITTED)`
读取未提交数据(会出现脏读, 不可重复读) 基本不使用

- 只读：`@Transactional(readOnly=true)`
该属性用于设置当前事务是否为只读事务，设置为true表示只读，false则表示可读写，默认值为false。
- 事务的超时性：`@Transactional(timeout=30)`
- 回滚：
    - 指定单一异常类：`@Transactional(rollbackFor=RuntimeException.class)`
    - 指定多个异常类：`@Transactional(rollbackFor={RuntimeException.class, Exception.class})`

## 事务配置
```xml
<!--  
     事务管理： Spring声明式事务管理 。
     spring默认的事务隔离级别是用的数据库的默认事务隔离级别，不同数据库级别也不尽相同。
     使用事务，要引入aop和tx的命名空间 ：即文件头第4行。
    (开启注解事务，使用时在方法上加上注解@Transactional(...)即可 )
-->
<!-- 事务管理器 -->
<bean id="transactionManager"
    class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource" />
</bean>

<!-- 通知传播行为 -->
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <tx:method name="insert*" propagation="REQUIRED" />
        <tx:method name="delete*" propagation="REQUIRED" />
        <tx:method name="upd*" propagation="REQUIRED" />
        <!-- 只读表示对数据的操作是读取，报异常后不用回滚（不是只读的数据出异常，事务会回滚）。 -->
        <tx:method name="select*" propagation="SUPPORTS" read-only="true" />
        <tx:method name="get" propagation="SUPPORTS" read-only="true" />
    </tx:attributes>
</tx:advice>

<!-- 配置事务切入点 -->
<aop:config>
    <aop:pointcut id="servicePointcut" expression="execution(* com.service.impl..*ServiceImpl.*(..))"/>
    <aop:advisor advice-ref="txAdvice" pointcut-ref="servicePointcut"/>
</aop:config>
```

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
## Spring注解使用
1. 在`applicationContext.xml`配置文件**导入命名空间及规范**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:tx="http://www.springframework.org/schema/tx"
    xsi:schemaLocation="http://www.springframework.org/schema/beans 
        http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
        http://www.springframework.org/schema/context 
        http://www.springframework.org/schema/context/spring-context-3.0.xsd
        http://www.springframework.org/schema/tx 
        http://www.springframework.org/schema/tx/spring-tx-3.0.xsd
        http://www.springframework.org/schema/aop 
        http://www.springframework.org/schema/aop/spring-aop-3.0.xsd">
</beans>
```
2. 在`applicationContext.xml`配置文件**配置扫描包**
```xml
<!-- 指定Spring IoC容器扫描的包，自动扫描代码中的注解 -->
<context:component-scan base-package="com.outman.spring" />
```
3. 使用注解
```java
@Controller
public class TestController {
    @Autowired
    RestTemplate rest;
    @RequestMapping(value="/rest")
    public String rest() {
        return "test";
    }
}
```

[返回目录](#目录)

## Spring常用注解

类别 | 注解 | 位置 | 使用 | 作用  
---|---|---|---|---  
| Bean声明| @Component | |  | 定义基础层的通用组件，没有明确角色
|         | @Service | |  | 定义业务逻辑层的服务组件
|         | @Repository | |  | 在数据访问层定义数据资源服务
|         | @Controller | |  | 定义控制器，表现层使用
| Bean注入| @Autowired | |  | 服务依赖注入，一般用于注入@Component、@Service定义的组件
|         | @Resource | |  | 服务依赖注入，一般用于注入@Repository定义的组件
| 配置类注解 | @Configuration | |  | 声明该类为配置类，其中@Value 属性可以直接和配置文件属性映射
|           | @Bean | |  | 注解在方法上，声明该方法返回值为一个Bean示例
| AOP注解 | @EnableAspectJAutoProxy | |  | 开启Spring对AspectJ代理的支持
|         | @ | |  | 
|         | @ | |  | 
|         | @ | |  | 


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



