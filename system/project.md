
# 简单单体结构

hello-springboot/
├── pom.xml                                                     # Maven 配置
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── hellospringboot/
│   │   │               ├── annotation/                         # 注解
│   │   │               │   └── WebLogAspect.java
│   │   │               ├── aop/                                # 切面
│   │   │               │   └── WebLogAspect.java
│   │   │               ├── config/                             # 配置类
│   │   │               │   ├── WebMvcConfig.java
│   │   │               │   ├── RedisConfig.java
│   │   │               │   └── SecurityConfig.java
│   │   │               ├── constant/                           # 常量
│   │   │               │   └── AppConstant.java
│   │   │               ├── controller/                         # 控制层
│   │   │               │   ├── UserController.java
│   │   │               │   └── OrderController.java
│   │   │               ├── dto/                                # 数据传输对象
│   │   │               │   ├── UserDTO.java
│   │   │               │   ├── UserRequest.java
│   │   │               │   └── UserResponse.java
│   │   │               ├── entity/                             # 实体类
│   │   │               │   ├── User.java
│   │   │               │   └── Order.java
│   │   │               ├── enums/                              # 枚举类
│   │   │               │   └── UserStatus.java
│   │   │               ├── event/                              # 事件
│   │   │               │   └── UserEvent.java
│   │   │               ├── exception/                          # 自定义异常
│   │   │               │   ├── BizException.java
│   │   │               │   └── GlobalExceptionHandler.java
│   │   │               ├── handler/                            # 处理器(processor)
│   │   │               │   └── UserStatus.java
│   │   │               ├── interceptor/                        # 拦截器
│   │   │               │   └── AuthJwtInterceptor.java
│   │   │               ├── mapper/                             # 数据访问层（MyBatis）
│   │   │               │   └── UserMapper.java
│   │   │               ├── mapstruct/                          # 对象映射（对象转换工具）
│   │   │               │   └── UserStructMapper.java
│   │   │               ├── qo/                                 # 数据查询对象
│   │   │               │   └── UserQO.java
│   │   │               ├── service/                            # 业务层
│   │   │               │   ├── UserService.java                # 接口
│   │   │               │   └── impl/
│   │   │               │       └── UserServiceImpl.java        # 实现
│   │   │               ├── strategy/                           # 策略
│   │   │               │   ├── Strategy.java  
│   │   │               │   ├── StrategyFactory.java
│   │   │               │   ├── StrategyService.java
│   │   │               │   └── UserStrategyService.java
│   │   │               ├── util/                               # 工具类
│   │   │               │   └── DateUtil.java
│   │   │               ├── vo/                                 # 视图对象
│   │   │               │   └── UserVO.java
│   │   │               └── HelloSpringbootApplication.java     # 启动类（放在根包）
│   │   └── resources/
│   │       ├── mapper/                                         # MyBatis XML
│   │       │   └── UserMapper.xml
│   │       ├── static/                                         # 静态资源
│   │       │   └── xxx
│   │       ├── templates/                                      # 模板文件
│   │       │   └── xxx
│   │       ├── application.yml                                 # 主配置
│   │       ├── application-dev.yml                             # 开发环境
│   │       ├── application-test.yml                            # 测试环境
│   │       ├── application-pre.yml                             # 预发环境
│   │       └── application-prod.yml                            # 生产环境
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── hellospringboot/
│                       └── HelloSpringbootApplicationTests.java
└── README.md


- 打包：`mvn clean package -DskipTests`
- 运行：`java -jar target/hello-springboot-1.0.0.jar --server.port=8008 --spring.profiles.active=prod`
