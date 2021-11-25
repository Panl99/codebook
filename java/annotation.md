# 目录
- [Java注解](#Java注解)
- [Spring注解](#Spring注解)
- [SpringBoot注解](#SpringBoot注解)
    - [SpringBoot提供的自动配置中使用的条件化注解](#SpringBoot提供的自动配置中使用的条件化注解)
    - [参数校验注解](#spring-boot-starter-validation)
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

## spring-boot-starter-validation

参数校验：`Bean Validation`的内嵌的注解

| 注解                        | 详细信息                                                 |
| :-------------------------- | :------------------------------------------------------- |
| @Null                       | 被注释的元素必须为 null                                  |
| @NotNull                    | 被注释的元素必须不为 null                                |
| @AssertTrue                 | 被注释的元素必须为 true                                  |
| @AssertFalse                | 被注释的元素必须为 false                                 |
| @Min(value)                 | 被注释的元素必须是一个数字，其值必须大于等于指定的最小值 |
| @Max(value)                 | 被注释的元素必须是一个数字，其值必须小于等于指定的最大值 |
| @DecimalMin(value)          | 被注释的元素必须是一个数字，其值必须大于等于指定的最小值 |
| @DecimalMax(value)          | 被注释的元素必须是一个数字，其值必须小于等于指定的最大值 |
| @Size(max, min)             | 被注释的元素的大小必须在指定的范围内                     |
| @Digits (integer, fraction) | 被注释的元素必须是一个数字，其值必须在可接受的范围内     |
| @Past                       | 被注释的元素必须是一个过去的日期                         |
| @Future                     | 被注释的元素必须是一个将来的日期                         |
| @Pattern(value)             | 被注释的元素必须符合指定的正则表达式                     |

`Hibernate Validator`在原有的基础上也内嵌了几个注解

| 注解      | 详细信息                               |
| :-------- | :------------------------------------- |
| @Email    | 被注释的元素必须是电子邮箱地址         |
| @Length   | 被注释的字符串的大小必须在指定的范围内 |
| @NotEmpty | 被注释的字符串的必须非空               |
| @Range    | 被注释的元素必须在合适的范围内         |

### 示例：

[https://mp.weixin.qq.com/s/hs3HF62F-iDSuZx4LjQ2fA](https://mp.weixin.qq.com/s/hs3HF62F-iDSuZx4LjQ2fA)

#### 简单校验

```java
@Data
public class ArticleDTO {

    @NotNull(message = "文章id不能为空")
    @Min(value = 1,message = "文章ID不能为负数")
    private Integer id;

    @NotBlank(message = "文章内容不能为空")
    private String content;

    @NotBlank(message = "作者Id不能为空")
    private String authorId;

    @Future(message = "提交时间不能为过去时间")
    private Date submitTime;
}

// 以上约束标记完成之后，要想完成校验，需要在controller层的接口标注@Valid注解以及声明一个BindingResult类型的参数来接收校验的结果。
/**
 * 添加文章
 */
@PostMapping("/add")
public String add(@Valid @RequestBody ArticleDTO articleDTO, BindingResult bindingResult) throws JsonProcessingException {
    //如果有错误提示信息
    if (bindingResult.hasErrors()) {
        Map<String , String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach( (item) -> {
            String message = item.getDefaultMessage();
            String field = item.getField();
            map.put( field , message );
        } );
        //返回提示信息
        return objectMapper.writeValueAsString(map);
    }
    return "success";
}

```

#### 分组校验

举个栗子：上传文章不需要传文章`ID`，但是修改文章需要上传文章`ID`，并且用的都是同一个`DTO`接收参数，此时的约束条件该如何写呢？

​		此时就需要对这个文章`ID`进行分组校验，上传文章接口是一个分组，不需要执行`@NotNull`校验，修改文章的接口是一个分组，需要执行`@NotNull`的校验。

```java
@Data
public class ArticleDTO {

    /**
     * 文章ID只在修改的时候需要检验，因此指定groups为修改的分组
     */
    @NotNull(message = "文章id不能为空",groups = UpdateArticleDTO.class )
    @Min(value = 1,message = "文章ID不能为负数",groups = UpdateArticleDTO.class)
    private Integer id;

    /**
     * 文章内容添加和修改都是必须校验的，groups需要指定两个分组
     */
    @NotBlank(message = "文章内容不能为空",groups = {AddArticleDTO.class,UpdateArticleDTO.class})
    private String content;

    @NotBlank(message = "作者Id不能为空",groups = AddArticleDTO.class)
    private String authorId;

    /**
     * 提交时间是添加和修改都需要校验的，因此指定groups两个
     */
    @Future(message = "提交时间不能为过去时间",groups = {AddArticleDTO.class,UpdateArticleDTO.class})
    private Date submitTime;
    
    //修改文章的分组
    public interface UpdateArticleDTO{}

    //添加文章的分组
    public interface AddArticleDTO{}

}

// JSR303本身的@Valid并不支持分组校验，但是Spring在其基础提供了一个注解@Validated支持分组校验。@Validated这个注解value属性指定需要校验的分组。
/**
 * 添加文章
 * @Validated：这个注解指定校验的分组信息
 */
@PostMapping("/add")
public String add(@Validated(value = ArticleDTO.AddArticleDTO.class) @RequestBody ArticleDTO articleDTO, BindingResult bindingResult) throws JsonProcessingException {
    //如果有错误提示信息
    if (bindingResult.hasErrors()) {
        Map<String , String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach( (item) -> {
            String message = item.getDefaultMessage();
            String field = item.getField();
            map.put( field , message );
        } );
        //返回提示信息
        return objectMapper.writeValueAsString(map);
    }
    return "success";
}
```

#### 嵌套校验

嵌套校验简单的解释就是一个实体中包含另外一个实体，并且这两个或者多个实体都需要校验。

嵌套校验很简单，只需要在嵌套的实体属性标注`@Valid`注解，则其中的属性也将会得到校验，否则不会校验。

```java
/**
 * 文章分类
 */
@Data
public class CategoryDTO {
    @NotNull(message = "分类ID不能为空")
    @Min(value = 1,message = "分类ID不能为负数")
    private Integer id;

    @NotBlank(message = "分类名称不能为空")
    private String name;
}
// 文章的实体类中有个嵌套的文章分类CategoryDTO属性，需要使用@Valid标注才能嵌套校验
@Data
public class ArticleDTO {

    @NotBlank(message = "文章内容不能为空")
    private String content;

    @NotBlank(message = "作者Id不能为空")
    private String authorId;

    @Future(message = "提交时间不能为过去时间")
    private Date submitTime;

    /**
     * @Valid这个注解指定CategoryDTO中的属性也需要校验
     */
    @Valid
    @NotNull(message = "分类不能为空")
    private CategoryDTO categoryDTO;
  }
// Controller层的添加文章的接口同上，需要使用@Valid或者@Validated标注入参，同时需要定义一个BindingResult的参数接收校验结果。
// 嵌套校验针对分组查询仍然生效，如果嵌套的实体类（比如CategoryDTO）中的校验的属性和接口中@Validated注解指定的分组不同，则不会校验。
// JSR-303针对集合的嵌套校验也是可行的，比如List的嵌套校验，同样需要在属性上标注一个@Valid注解才会生效
@Data
public class ArticleDTO {
    /**
     * @Valid这个注解标注在集合上，将会针对集合中每个元素进行校验
     */
    @Valid
    @Size(min = 1,message = "至少一个分类")
    @NotNull(message = "分类不能为空")
    private List<CategoryDTO> categoryDTOS;
  }
// 总结：嵌套校验只需要在需要校验的元素（单个或者集合）上添加@Valid注解，接口层需要使用@Valid或者@Validated注解标注入参。
```

#### 使用AOP接受校验结果

参数在校验失败的时候会抛出的`MethodArgumentNotValidException`或者`BindException`两种异常，可以在全局的异常处理器中捕捉到这两种异常，将提示信息或者自定义信息返回给客户端。

```java
// 这里没有详细的贴出其他的异常捕获，仅仅贴一下参数校验的异常捕获（具体的返回信息需要自己封装）
@RestControllerAdvice
public class ExceptionRsHandler {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 参数校验异常步骤
     */
    @ExceptionHandler(value= {MethodArgumentNotValidException.class , BindException.class})
    public String onException(Exception e) throws JsonProcessingException {
        BindingResult bindingResult = null;
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException)e).getBindingResult();
        } else if (e instanceof BindException) {
            bindingResult = ((BindException)e).getBindingResult();
        }
        Map<String,String> errorMap = new HashMap<>(16);
        bindingResult.getFieldErrors().forEach((fieldError)->
                errorMap.put(fieldError.getField(),fieldError.getDefaultMessage())
        );
        return objectMapper.writeValueAsString(errorMap);
    }

}
```

#### 自定义校验注解

**例如：传入的数字要在列举的值范围中，否则校验失败。**

```java
@Documented
@Constraint(validatedBy = { EnumValuesConstraintValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@NotNull(message = "不能为空")
public @interface EnumValues {
    /**
     * 提示消息
     */
    String message() default "传入的值不在范围内";

    /**
     * 分组
     * @return
     */
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * 可以传入的值
     * @return
     */
    int[] values() default { };
}

// 根据Bean Validation API 规范的要求有如下三个属性是必须的：
	// message：定义消息模板，校验失败时输出
	// groups：用于校验分组
	// payload：Bean Validation API 的使用者可以通过此属性来给约束条件指定严重级别.这个属性并不被API自身所使用。
	// 除了以上三个必须要的属性，添加了一个values属性用来接收限制的范围。
// @Constraint注解指定了通过哪个校验器去校验。
// 自定义校验注解可以复用内嵌的注解，比如@EnumValues注解头上标注了一个@NotNull注解，这样@EnumValues就兼具了@NotNull的功能。
```

自定义校验器

`@Constraint`注解指定了校验器为`EnumValuesConstraintValidator`

```java
/**
 * 校验器
 */
public class EnumValuesConstraintValidator implements ConstraintValidator<EnumValues,Integer> {
    /**
     * 存储枚举的值
     */
    private  Set<Integer> ints=new HashSet<>();

    /**
     * 初始化方法
     * @param enumValues 校验的注解
     */
    @Override
    public void initialize(EnumValues enumValues) {
        for (int value : enumValues.values()) {
            ints.add(value);
        }
    }

    /**
     *
     * @param value  入参传的值
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        //判断是否包含这个值
        return ints.contains(value);
    }
}
// 自定义校验器需要实现ConstraintValidator<A extends Annotation, T>这个接口，第一个泛型是校验注解，第二个是参数类型
// 如果约束注解需要对其他数据类型进行校验，则可以的自定义对应数据类型的校验器，然后在约束注解头上的@Constraint注解中指定其他的校验器。
```

使用自定义注解

```java
@Data
public class AuthorDTO {
    @EnumValues(values = {1,2},message = "性别只能传入1或者2")
    private Integer gender;
}
```





[目录](#目录)

# SpringCloud注解

# Lombok注解
- **`@Data`** 类上，自动化方法生成：生成缺失的方法，生成所有以final修饰的参数的构造器。
- **`@Slf4j`** 类上，日志声明，

[目录](#目录)