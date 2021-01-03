# 目录
- [Java注解](#Java注解)
- [Spring注解](#Spring注解)
- [SpringBoot注解](#SpringBoot注解)
    - [SpringBoot提供的自动配置中使用的条件化注解](#SpringBoot提供的自动配置中使用的条件化注解)
- [SpringCloud注解](#SpringCloud注解)
- [Lombok注解](#Lombok注解)


# Java注解

# Spring注解

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
|         | @Aspect | |  | 声明一个切面，使用@After、@Before、@Around定义通知(Advice)，可直接将拦截规则(切点)作为参数
|         | @After | |  | 在方法执行之后执行
|         | @Before | |  | 在方法执行之前执行
|         | @Around | |  | 在方法执行之前 和执行之后都执行
|         | @PointCut | |  | 声明一个切点
| Bean属性 | @Scope | |  | 设置Spring容器Bean实例的生命周期，取值有：singleton、prototype、request、session、global session
|         | @PostConstruct | |  | 声明方法在构造函数执行完后开始执行
|         | @PreDestroy | |  | 声明方法在Bean销毁之前执行
|         | @Value | |  | 为属性注入值
|         | @PropertySource | |  | 声明 和加载配置文件
| 异步操作注解 | @EnableAsync | |  | 声明在类上，开启对异步任务的支持
|            | @Async | |  | 声明方法是一个异步任务，Spring后台基于线程池异步执行该方法
| 定时任务相关 | @EnableScheduling | |  | 声明在调度类上，开启对任务调度的支持
|            | @Scheduled | |  | 声明一个定时任务，包括cron、fixDelay、fixRate等参数
| 开启功能支持 | @EnableAspectJAutoProxy | |  | 开启对AspectJ自动代理的支持
|            | @EnableAsync | |  | 开启对异步方法的支持
|            | @EnableScheduling | |  | 开启对计划任务的支持
|            | @EnableWebMVC | |  | 开启对Web MVC的配置支持
|            | @EnableConfigurationProperties | |  | 开启对@ConfigurationProperties注解配置 Bean的支持
|            | @EnableJpaRepositories | |  | 开启对SpringData JPA Repository的支持
|            | @EnableTransactionManagement | |  | 开启对事务的支持
|            | @EnableCaching | |  | 开启对缓存的支持
| 测试相关注解 | @RunWith | |  | 运行器，Spring中通常对JUnit的支持
|            | @ContextConfiguration | |  | 用来加载配置ApplicationContext，其中classes属性用来加载配置类
| SpringMVC注解 | @Controller | |  | 声明控制器类
|            | @RequestMapping | |  | 声明映射Web请求的地址和参数，包括访问路径和参数
|            | @ResponseBody | |  | 支持将返回值放在Response Body 体中返回，通常用于返回Json数据到前端
|            | @RequestBody | |  | 允许Request 的参数在Request Body 体中
|            | @PathVariable | |  | 用于接收基于路径的参数，通常作为RESTful接口的实现
|            | @RestController | |  | 组合注解，相当于@Controller 和@ResponseBody的组合
|            | @ExceptionHandler | |  | 用于全局控制器的异常处理
|            | @InitBinder | |  | WebDataBinder 用来自动绑定前台请求的参数到模型(Model)中

[目录](#目录)

# SpringBoot注解
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

[目录](#目录)

# SpringCloud注解

# Lombok注解
- **`@Data`** 类上，自动化方法生成：生成缺失的方法，生成所有以final修饰的参数的构造器。
- **`@Slf4j`** 类上，日志声明，

[目录](#目录)