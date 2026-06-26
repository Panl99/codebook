

Protobuf（Protocol Buffers）是Google开发的高效、跨语言的数据序列化框架。
`.proto`文件作为其核心配置文件，通过定义数据结构和服务接口实现高效通信，
相比JSON/XML等文本协议，Protobuf在性能、空间占用、跨语言支持方面具有显著优势。

> [通信协议Protobuf入门到精通](https://zhuanlan.zhihu.com/p/17364993510)

# 核心特性

优点
- **高效性**：二进制格式，体积小，序列化/反序列化速度快
- **跨语言**：支持多种编程语言
- **向前/向后兼容**：通过字段编号机制实现
- **代码生成**：自动生成数据访问类
- **结构化数据**：强类型定义

缺点
- **可读性差**：二进制格式不易阅读
- **需要预编译**：需要预定义 `.proto` 文件

# 文件结构与核心语法

## 基础结构

`.proto`文件由五大核心模块构成：

| 模块类型   | 示例代码                      | 说明                  |
|--------|---------------------------|---------------------|
| 语法声明   | syntax = "proto3";        | 指定Proto版本（推荐proto3） |
| 包名定义   | package example;          | 避免命名冲突              |
| 消息定义   | message User {...}        | 定义数据结构              |
| 服务定义   | service UserService {...} | 定义RPC接口             |

## 消息定义详解

消息（Message）是数据传输的基本单元，支持多种字段类型：

```protobuf
syntax = "proto3";                    // 指定Protobuf版本

package com.lp.iot.protobuf;          //  Protobuf 命名空间，作用范围是所有使用该 .proto 文件的语言（C++, Python, Go, Java等），防止跨文件的Message/Enum/Service重名，建议保持公司/项目唯一，便于跨语言通信。

option java_package = "com.lp.protobuf.model";      // 定义生成的Java类包路径（不指定时使用package的路径）
option java_outer_classname = "UserProto";          // 定义生成的外层类名（用于嵌套消息），（不定义时，将 .proto 文件名转换为驼峰命名（如 foo_bar.proto -> FooBar），当自动生成（或指定）的包装类名字，与文件中任何一个Message、Enum或Service的名字相同时，编译器会自动加上 OuterClass 后缀）

// User消息定义。消息（Message）是数据传输的基本单元，支持多种字段类型。
message User {                        // 注意：User 和 java_outer_classname 名称不要冲突，否则编译错误：protoc did not exit cleanly. Review output for more information.
  string name = 1;                    // 基础类型
  int32 age = 2;
  
  repeated string hobbies = 3;        // 数组/列表类型
  
  map<string, int32> attributes = 4;  // 键值对类型
  
  oneof contact {                     // 互斥字段组
    string email = 5;
    string phone = 6;
  }                                 
}

// User服务定义
service UserService {
  rpc CreateUser(UserRequest) returns (UserResponse);
}

message UserRequest { string user_id = 1; }
message UserResponse { User user = 1; }
```

## 关键特性解析

| 规则类型     | 示例                             | 编码特性                                              |
|----------|--------------------------------|---------------------------------------------------|
| optional | string name = 1;               | 可选字段，proto3默认所有字段为optional（proto3中已弃用optional关键字） |
| repeated | repeated string hobbies = 3    | 生成数组/列表类型                                         |
| map      | map<string,int32> attributes=4 | 键值对存储                                             |
| oneof    | oneof contact{...}             | 互斥字段组，节省存储空间                                      |

数据类型：

| .proto 类型   | C++ 类型    | Java 类型    | Python 类型  |
|-------------|-----------|------------|------------|
| double      | double    | double     | float      |
| float       | float     | float      | float      |
| int32       | int32     | int        | int        |
| int64       | int64     | long       | int/long   |
| bool        | bool      | boolean    | bool       |
| string      | string    | String     | str        |
| bytes       | string    | ByteString | bytes      |

基本数据类型：

- bool‌：布尔类型，可以存储true或false。
- double‌：双精度浮点数，可以存储带小数的数值。
- float‌：单精度浮点数，也可以存储带小数的数值。
- int32‌：32位有符号整数。
- int64‌：64位有符号整数。
- uint32‌：32位无符号整数。
- uint64‌：64位无符号整数。
- sint32‌：32位有符号整数，使用变长编码优化存储空间。
- sint64‌：64位有符号整数，使用变长编码优化存储空间。
- fixed32‌：32位无符号整数，总是使用4个字节存储。
- fixed64‌：64位无符号整数，总是使用8个字节存储。
- sfixed32‌：32位有符号整数，总是使用4个字节存储。
- sfixed64‌：64位有符号整数，总是使用8个字节存储。
- string‌：字符串类型，可以包含任意顺序的Unicode字符，但不包括零字符（即UTF-8编码的文本）。
- bytes‌：字节序列，可以包含任意二进制数据。

复合类型：
- enum‌：枚举类型，定义一个命名的整型常量集合。
- message‌：自定义消息类型，可以包含其他消息、字段或复合类型。可构建复杂数据结构。



## 最佳实践

- 字段编号分配：高频字段使用1-15编号（1字节编码），16-2047作为不常用字段，不要随意更改字段编号
- 二进制优化：repeated字段默认使用packed编码
- 服务设计：将相关操作封装在同一个service中
- 向后兼容
  - 不要删除已使用的字段编号
  - 新字段使用新的编号
  - 已删除的字段可添加 reserved 声明
- 版本管理
  - 在文件名或包名中包含版本信息
  - 使用语义化版本控制

### 版本兼容性策略

| 场景   | 解决方案                   |
|------|------------------------|
| 字段废弃 | reserved 2,15;         |
| 编号优化 | 1-15号字段优先分配给高频字段       |
| 模块复用 | import "common.proto"; |

## 跨语言支持机制

通过`protoc`编译器生成多语言代码：

```
bash

# 生成 Java 代码

protoc --java_out=. user.proto

# 生成Go代码示例
protoc --go_out=. --go_opt=paths=source_relative user.proto

# 生成多种语言
protoc --java_out=. --python_out=. --cpp_out=. user.proto
```

生成文件包含：

- `user.pb.go`：消息结构体定义
- `user_grpc.pb.go`：RPC服务接口


# 示例

## 添加依赖

```xml
<!-- Protobuf Java 支持 -->
<protobuf.version>4.35.1</protobuf.version>
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
    <version>${protobuf.version}</version>
</dependency>
<!-- 可选：支持更便捷的 Protobuf 消息转换 -->
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java-util</artifactId>
    <version>${protobuf.version}</version>
</dependency>


<build>
  <extensions>
    <!-- OS 检测插件，用于 protobuf 插件 -->
    <extension>
      <groupId>kr.motd.maven</groupId>
      <artifactId>os-maven-plugin</artifactId>
      <version>1.7.1</version>
    </extension>
  </extensions>
  <plugins>
    <!-- Spring Boot Maven 插件 -->
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
  
    <!-- Protobuf 编译插件 -->
    <plugin>
      <groupId>org.xolstice.maven.plugins</groupId>
      <artifactId>protobuf-maven-plugin</artifactId>
      <version>0.6.1</version>
      <configuration>
        <!-- 工具版本 --><!--protoc编译器工具的格式规范，groupId:artifactId:version[:type[:classifier]]。如果指定了该参数，protoc将尝试按照指定的参数对文件proto文件进行编译-->
        <protocArtifact>com.google.protobuf:protoc:${protobuf.version}:exe:${os.detected.classifier}</protocArtifact>
        <!--默认值，proto源文件路径-->
        <protoSourceRoot>${project.basedir}/src/main/proto</protoSourceRoot>
        <!--默认值，proto目标java文件路径-->
        <outputDirectory>${project.build.directory}/generated-sources/protobuf/java</outputDirectory>
        <!--设置是否在生成java文件之前清空outputDirectory的文件，默认值为true，设置为false时也会覆盖同名文件-->
        <!--意思protoc翻译proto时，会将输出目录清空。message和service类型需要在maven中执行两次生成操作。如果开启这个选项，第二次生成会将第一次生成的结果清空-->
        <clearOutputDirectory>false</clearOutputDirectory>
        <!--<pluginId>grpc-java</pluginId>&lt;!&ndash;protobuf:compile-custom required&ndash;&gt;-->
      </configuration>
      <executions>
        <execution>
          <!--在执行mvn compile的时候会执行以下操作-->
          <goals>
            <!--生成OuterClass类-->
            <goal>compile</goal>
            <!--生成Grpc类-->
            <!--<goal>compile-custom</goal>-->
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```
- idea右侧maven的插件中出现protobuf插件
  - 执行`protobuf:compile`第二个run选项编译生成`message`类的Java代码；（或maven执行`mvn protobuf:compile`）

### 使用自定义编译插件编译gRPC服务

> [使用protobuf-maven-plugin生成grpc项目](https://cloud.tencent.com/developer/article/2420894)

如果只是编译proto文件，可以不使用该插件，使用上面的配置即可。当要使用自定义插件的时候，必须将goal设置为compile-custom。
- 执行`protobuf:compile-custom`第二个run选项编译生成`service`抽象类的Java代码；（或maven执行`mvn protobuf:compile-custom`）

设置好goal后，可以配置插件的信息，如下：
- `pluginId`: 唯一标识protoc的插件，不能为内置的protoc插件，比如`grpc-java`插件
- `pluginArtifact`: 识别插件版本
- `pluginParameter`: 传递给插件的参数
```xml

<properties>
    <io-grpc.version>1.82.0</io-grpc.version>
</properties>

<dependencies>
    <dependency>
      <groupId>io.grpc</groupId>
      <artifactId>grpc-netty-shaded</artifactId>
      <version>${io-grpc.version}</version>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>io.grpc</groupId>
      <artifactId>grpc-protobuf</artifactId>
      <version>${io-grpc.version}</version>
    </dependency>
    <dependency>
      <groupId>io.grpc</groupId>
      <artifactId>grpc-stub</artifactId>
      <version>${io-grpc.version}</version>
    </dependency>
    <dependency> <!-- necessary for Java 9+ -->
      <groupId>org.apache.tomcat</groupId>
      <artifactId>annotations-api</artifactId>
      <version>6.0.53</version>
      <scope>provided</scope>
    </dependency>
<!--    <dependency>-->
<!--      <groupId>org.apache.tomcat</groupId>-->
<!--      <artifactId>tomcat-annotations-api</artifactId>-->
<!--      <version>11.0.22</version>-->
<!--      <scope>compile</scope>-->
<!--    </dependency>-->
</dependencies>

<configuration>
  <pluginId>grpc-java</pluginId>
  <pluginArtifact>io.grpc:protoc-gen-grpc-java:${io-grpc.version}:exe:${os.detected.classifier}</pluginArtifact>
  <!--更多配置信息可以查看https://www.xolstice.org/protobuf-maven-plugin/compile-mojo.html-->
</configuration>

<executions>
  <execution>
    <goals>
      <!--生成Grpc类-->
      <goal>compile-custom</goal>
    </goals>
  </execution>
</executions>
```

## 序列化与反序列化示例

```java
import com.google.protobuf.InvalidProtocolBufferException;
import com.lp.protobuf.model.UserProto;

public class ProtoBufDemo {
    
    public static void main(String[] args){
      
        // 1. 构建User对象
        UserProto.User user = UserProto.User.newBuilder()
            .setId(123)
            .setName("Alice")
            .setEmail("alice@example.com")
            .setAge(25)
            .build();
    
        // 2. 序列化为字节数组
        byte[] data = user.toByteArray();
        System.out.println("Serialized data length: "+ data.length);
    
        // 3. 反序列化
        try {
          UserProto.User parsedUser = UserProto.User.parseFrom(data);
          System.out.println("Parsed User: "+ parsedUser);
          System.out.println("Name: "+ parsedUser.getName());
          System.out.println("Email: "+ parsedUser.getEmail());
          System.out.println("Age: "+ parsedUser.getAge());
        } catch (InvalidProtocolBufferException e) {
          e.printStackTrace();
        }
    }
}
```

## Protobuf与JSON互转

```java
import com.google.protobuf.util.JsonFormat;
import com.lp.protobuf.model.UserProto;

public class JsonConversionDemo {

    public static void main(String[] args) throws Exception {
        // 1. 构建User对象
        UserProto.User user = UserProto.User.newBuilder()
                .setId(123)
                .setName("Alice")
                .setEmail("alice@example.com")
                .setAge(25)
                .build();

        // Protobuf转JSON
        String json = JsonFormat.printer().print(user);
        System.out.println("Protobuf to JSON: " + json);

        // JSON转Protobuf
        UserProto.User.Builder builder = UserProto.User.newBuilder();
        JsonFormat.parser().merge(json, builder);
        UserProto.User parsedUser = builder.build();
        System.out.println("JSON to Protobuf: " + parsedUser);
    }

}
```



# proto文件规范


```
├── common/                          # 公共基础类型
│   ├── header.proto                # 消息头定义
│   ├── device_status.proto         # 设备状态
│   ├── ack.proto                   # 确认响应
│   └── enums.proto                 # 通用枚举
├── upload/                          # 上行消息（设备→云）
│   ├── device_upload.proto         # 上传消息主结构
│   ├── sensor_data.proto           # 传感器数据
│   └── event_data.proto            # 事件数据
├── command/                         # 下行消息（云→设备）
│   ├── device_command.proto        # 命令消息主结构
│   ├── config_update.proto         # 配置更新
│   ├── fota_update.proto           # 固件升级
│   └── control_command.proto       # 控制命令
└── response/                        # 响应消息
    └── command_response.proto      # 命令响应
```
---

## 🎯设计规范的目的

### 1. **模块化分离（Modular Separation）**
- **目的**：将不同职责的消息类型分离到独立目录，提高可维护性和可扩展性
- **实现**：
  - `common/`：存放所有模块共享的基础类型（消息头、枚举、设备状态等）
  - `upload/`：专门处理设备到云平台的上行数据（传感器数据、事件上报）
  - `command/`：专门处理云平台到设备的下行指令（配置更新、FOTA、控制命令）
  - `response/`：统一处理设备响应消息
- **优势**：
  - ✅ 职责清晰，每个目录专注单一业务领域
  - ✅ 降低耦合，修改某个模块不影响其他模块
  - ✅ 便于团队协作，不同开发人员可并行工作

### 2. **版本化管理（Versioning）**
- **目的**：支持协议演进和多版本共存，避免破坏性变更影响现有设备
- **实现**：使用 `v1/` 目录标识协议版本，未来可并行存在 `v2/`、`v3/` 等
- **版本演进策略**：
  - **向后兼容**：新增字段使用可选字段或 `oneof` 扩展，不删除已有字段
  - **废弃标记**：使用 `reserved` 关键字标记已废弃的字段编号
  - **多版本共存**：v1 设备和 v2 设备可同时接入，服务端根据 `Header.version` 路由
- **优势**：
  - ✅ 平滑升级，新旧设备可同时运行
  - ✅ 灰度发布，逐步推广新协议
  - ✅ 回滚能力，出现问题可快速切回旧版本

### 3. **单向依赖原则（Unidirectional Dependency）**
- **目的**：避免循环依赖，确保编译顺序清晰
- **实现**：
  - 业务模块（upload/command）依赖 common 模块
  - common 模块不依赖任何业务模块
  - 同一层级模块间互不依赖
- **依赖关系图**：

- **优势**：
  - ✅ 编译顺序明确，避免循环引用错误
  - ✅ 层次清晰，易于理解和维护
  - ✅ 便于单元测试，可独立测试各模块

### 4. **消息完整性（Message Integrity）**
- **目的**：每条消息包含完整的上下文信息，支持追踪、认证、QoS 保障
- **实现**：
  - 所有上行消息包含 `Header`（消息ID、时间戳、trace_id、QoS等级）
  - 支持 `need_ack` 标志位实现可靠传输
  - 内置 `auth_token` 支持设备认证
- **优势**：
  - ✅ 全链路追踪，便于故障排查和性能分析
  - ✅ 可靠传输，关键消息不丢失
  - ✅ 安全认证，防止非法设备接入

---

## ✨设计规范的优势

### 1. **高内聚低耦合**
- ✅ 每个 proto 文件职责单一，修改某个功能不影响其他模块
- ✅ 新增消息类型只需在对应目录添加文件，无需修改现有结构
- ✅ 支持按模块独立测试和验证

### 2. **易于扩展**
- ✅ 使用 `oneof` 字段支持多种载荷类型，新增类型只需扩展 oneof 分支
- ✅ 通过 import 机制复用公共类型，避免重复定义
- ✅ 版本号目录支持平滑升级，新旧设备可共存

### 3. **跨语言兼容**
- ✅ Protobuf 原生支持 Java、Python、Go、C++、JavaScript 等主流语言
- ✅ 自动生成序列化和反序列化代码，减少人工编码错误
- ✅ 二进制格式比 JSON/XML 更紧凑，适合物联网低带宽场景

### 4. **性能优化**
- ✅ 二进制序列化体积比 JSON 小 3-10 倍
- ✅ 序列化/反序列化速度比 JSON 快 5-20 倍
- ✅ 支持流式解析，适合大数据量批量传输

### 5. **标准化通信模式**
- ✅ 明确区分上行（upload）和下行（command）消息流向
- ✅ 统一的 Header 结构支持链路追踪、监控告警
- ✅ QoS 等级支持与 MQTT 等消息中间件无缝集成

---

## 🌍行业标准对标

### ✅ 符合的物联网通用实践

#### 1. **AWS IoT Core 协议设计**
- **对标点**：
  - AWS IoT Device Shadow 使用类似的 JSON 结构，但推荐使用 Protobuf 优化性能
  - 消息分层：`state.reported` / `state.desired` 对应我们的 `upload` / `command`
  - 支持消息追踪（trace_id）和版本管理
- **差异点**：
  - AWS 默认使用 JSON，我们使用 Protobuf（性能更优）
  - AWS 通过 Topic 表达语义，我们通过消息头+载荷结构表达

#### 2. **Azure IoT Hub 消息路由**
- **对标点**：
  - Azure 使用 System Properties + Application Properties 分离元数据和业务数据
  - 我们的 `Header` 设计与之类似，支持消息类型、QoS、时间戳等元数据
  - 支持设备到云（D2C）和云到设备（C2D）双向通信
- **差异点**：
  - Azure 使用 AMQP/MQTT/HTTP 多种协议，我们专注于 MQTT + Protobuf
  - Azure 的设备孪生（Device Twin）功能更强大，我们简化为 DeviceStatus

#### 3. **阿里云 IoT Platform**
- **对标点**：
  - 阿里云物模型采用"属性/服务/事件"三元组设计
  - 我们的 `sensor_data` 对应属性上报，`event_data` 对应事件上报
  - `device_command` 对应服务调用，支持参数化配置
- **差异点**：
  - 阿里云使用 Thing Specification Language (TSL)，我们使用 Protobuf
  - 阿里云强调物模型可视化编辑，我们强调代码即文档

#### 4. **MQTT + Protobuf 组合**
- **行业最佳实践**：
  - EMQ X、HiveMQ 等主流 MQTT Broker 均支持 Protobuf 载荷
  - MQTT Topic 表达路由语义，Protobuf Payload 承载业务数据
  - QoS 等级与 MQTT QoS 0/1/2 映射一致
- **优势**：
  - ✅ MQTT 轻量级，适合低功耗设备
  - ✅ Protobuf 高效，适合低带宽网络
  - ✅ 两者结合是物联网通信的黄金组合

#### 5. **Google Cloud IoT Core**
- **对标点**：
  - Google 官方推荐 Protobuf 作为设备通信协议
  - 使用 `google.protobuf.Timestamp` 等标准类型确保时间精度
  - 支持设备状态管理和配置下发
- **差异点**：
  - Google Cloud IoT Core 已停止新用户注册，但设计理念仍具参考价值

### ⚠️ 与行业标准的差异点

| 特性 | 本设计 | 行业标准 | 说明 |
|------|--------|----------|------|
| 消息封装 | 自定义 Header | MQTT User Properties | 我们提供更结构化的元数据 |
| 认证方式 | auth_token 字段 | JWT/X.509 证书 | 建议结合 TLS + 证书增强安全性 |
| 错误处理 | ACK/NACK 枚举 | MQTT PUBACK/SUBACK | 应用层确认与传输层确认互补 |
| 批量传输 | BatchSensorData | MQTT 分片/压缩 | 支持历史数据批量上报 |
| 数据格式 | Protobuf（二进制） | JSON（文本） | Protobuf 性能更优，但可读性较差 |

---