

# 常见的物联网协议
包含：
- 物理层协议：LoRaWAN、NB-IoT
- 数据链路层协议：LoRaWAN、NB-IoT
- 应用层协议：MQTT、CoAP、LwM2M

## MQTT协议

`Message Queue Telemetry Transport`：消息队列遥测传输协议。

特点：
- 实现简单。
- 提供数据传输的QoS。
- 轻量、低带宽占用。
- 支持任意类型数据传输。
- 可保持的会话（session）。

特性：
- 基于**TCP协议**的应用层协议。
- 采用 C/S 架构。
- 使用 订阅/发布 模式，将消息的发送方 和接收方 解耦。
- 提供三种消息QoS（Quality of Service）：最多一次、最少一次、只有一次。
- 消息的发送 和接收 都是异步的。

MQTT协议的架构由 Broker 和连接到Broker的多个Client组成。
![IoT-MQTT通信模型](../resources/static/images/IoT-MQTT通信模型.png)

## MQTT-SN协议

`MQTT for Sensor Network`：是MQTT协议的传感器版本。

- MQTT-SN运行在**UDP协议**上，同时保留了MQTT协议的大部分信令和特性，如订阅和发布等。
- MQTT-SN协议引入了MQTT-SN网关这一角色，网关负责把MQTT-SN协议转换为MQTT协议，并和远端的MQTT Broker进行通信。
- MQTT-SN协议支持网关的自动发现。

![IoT-MQTT-SN协议通信模型](../resources/static/images/IoT-MQTT-SN协议通信模型.png)

## CoAP协议

`Constrained Application Protocol`：是一种运行在资源比较紧张的设备上的协议。

- CoAP协议通常也是运行在**UDP协议**上的。
- CoAP协议设计得非常小巧，最小的数据包只有4个字节。
- CoAP协议采用C/S架构，使用类似于HTTP协议的请求-响应的交互模式。
  设备可以通过类似于`coap://192.168.1.150:5683/2ndfloor/temperature`的URL来标识一个实体，
  并使用类似于HTTP的PUT、GET、POST、DELETE请求指令来获取或者修改这个实体的状态。
- CoAP提供一种观察模式，观察者可以通过 OBSERVE指令向CoAP服务器指明观察的实体对象。
  当实体对象的状态发生变化时，观察者就可以收到实体对象的最新状态，类似于MQTT协议中的订阅功能。

![IoT-CoAP协议通信模型](../resources/static/images/IoT-CoAP协议通信模型.png)

## LwM2M协议

`Lightweight Machine-To-Machine`：轻量级设备对设备的协议。

- 使用REST ful接口，提供设备的接入、管理、通信功能，也适用于资源比较紧张的设备。
- LwM2M协议底层使用 CoAP协议传输数据和信令。
  而在LwM2M协议的架构中，CoAP协议可以运行在UDP或者SMS（短信）之上，通过DTLS（数据报传输层安全）来实现数据的安全传输。

![IoT-LwM2M协议架构](../resources/static/images/IoT-LwM2M协议架构.png)

在没有移动数据网络覆盖的地区，比如偏远地区的水电站，用短信作为信息传输的载体已经有比较长的历史了。

LwM2M协议架构主要包含3种实体：
`LwM2M Bootstrap Server`负责引导LwM2M Client注册并接入`LwM2M Server`，
之后LwM2M Server和`LwM2M Client`就可以通过协议指定的接口进行交互了。

## HTTP协议

常用在资源比较充足的设备。比如：运行安卓操作系统的设备。

## LoRaWAN协议

一种低功率广域网协议，是物理层/数据链路层协议，主要解决设备如何接入互联网的问题，不运行在IP网络上。

用在有线网络、4G、WiFi不能覆盖的场景。

LoRa（Long Range）是一种无线通信技术，它具有使用距离远、功耗低的特点。

比如在隧道施工中，用户就可以使用LoRaWAN技术进行组网，在工程设备上安装支持LoRA的模块。
通过LoRa的中继设备将数据发往位于隧道外部的、有互联网接入的LoRa网关，LoRa网关再将数据封装成可以在IP网络中通过TCP协议或者UDP协议传输的数据协议包（比如MQTT协议），然后发往云端的数据中心。


## NB-IoT协议

`Narrow Band Internet of Things`：窄带物联网协议。和LoRaWAN协议一样，是将设备接入互联网的物理层/数据链路层的协议。

和LoRA不同的是，NB-IoT协议构建和运行在蜂窝网络上，消耗的带宽较低，可以直接部署到现有的GSM网络或者LTE网络。
设备安装支持NB-IoT的芯片和相应的物联网卡，然后连接到NB-IoT基站就可以接入互联网。
而且NB-IoT协议不像LoRaWAN协议那样需要网关进行协议转换，接入的设备可以直接使用IP网络进行数据传输。

NB-IoT协议相比传统的基站，增益提高了约20dB，可以覆盖到地下车库、管道、地下室等之前信号难以覆盖的地方。


# MQTT协议详解

**MQTT协议与传统消息队列区别：**
- 传统消息队列：发送消息前 必须先创建相应的队列。  MQTT协议：不需要预先创建要发布的主题(Topic)。
- 传统消息队列：未被消费的消息会保存在某个队列中 直至被某个消费者消费。  MQTT协议：如果发布的消息没有被任何客户端订阅，消息将被丢弃。
- 传统消息队列：一个消息只能被一个客户端获取。  MQTT协议：一个消息可以被多个订阅者获取。并且MQTT不支持指定消息被单一客户端获取。

## MQTT协议的通信模型

MQTT是通过 `发布/订阅模式`来通信的。

消息的发布者 和消费者 通过此模式解耦，中间通过`Broker`来存储和转发消息。消息发布方和订阅方都是`Client`。

MQTT通信流程：
1. 发布者(Publisher)和订阅者(Subscriber) 都建立了到Broker的TCP连接。
2. 订阅者告知Broker他要订阅的消息主题（Topic）。
3. 发布者指定消息主题，并将消息发送到Broker。
4. Broker接收到消息后，将消息转发给订阅此Topic的订阅者们。
5. 订阅者们从Broker获取到消息。
6. 如果某个订阅者此时处于离线状态，那么Broker会保留此消息，在该订阅者再次上线时转发给它。


## MQTT Broker

一个MQTT Broker应该具备的能力：
- 可以支持对Client的接入授权，并对Client进行权限控制。
- 可以横向扩展，比如支持集群，以满足海量的Client接入。
- 有较好的扩展性，可以比较方便的接入现有的业务系统。
- 方便监控，满足高可用性。

**常见的MQTT Broker：**
1. `Mosquitto`：C语言编写，开源，支持单机，单机配置运行简单，拓展麻烦。
2. **`EMQ X`**：Erlang语言编写，有开源版本 和付费版本，支持集群，可以通过插件方式进行功能拓展。
3. `HiveMQ`：Java语言编写，只有付费版，支持集群，可以通过插件方式进行功能拓展。
4. `VerneMQ`：Erlang语言编写，开源（瑞士公司提供商业服务），支持集群，可以通过插件方式进行功能拓展。


## MQTT协议数据包格式

MQTT协议使用的是：二进制数据包。

一个MQTT协议数据包包含3部分：
- 固定报头（Fix Header）：存在于所有的MQTT协议数据包中，用于表示数据包类型、对应标识，表明数据包大小。
- 可变报头（Variable Header）：存在于部分类型的MQTT协议数据包中，具体内容有相应类型的数据包决定。
- 消息体（有效载荷:Payload）：存在于部分MQTT协议数据包中，存储消息的具体数据。


![IoT-MQTT协议数据包的固定头](../resources/static/images/IoT-MQTT协议数据包的固定头.png)

1. **数据包类型**：MQTT协议数据包的**固定头的第一个字节的高四位** 表示该数据包的类型。
![IoT-MQTT协议消息类型](../resources/static/images/IoT-MQTT协议数据包类型.jpg)
2. **数据包标识位**：MQTT协议数据包的**固定头的第一个字节的低四位**。不用类型数据包 标识位定义不一样。
![IoT-MQTT协议数据包标识位](../resources/static/images/IoT-MQTT协议数据包标识位.jpg)
    - DUP1: 控制报文的重复分发标志
    - QoS2：PUBLISH报文的服务质量等级
    - RETAIN3: PUBLISH报文的保留标志
3. **数据包剩余长度**：从固定位第二个字节开始，用于标识当前数据包剩余长度的字段，= 可变头长度 + 消息体长度。
    - 这个字段最少1个字节，最多4个字节。
    - 其中，每个字节的最高位叫延续位（Continuation Bit），表示在这个字节之后是否还有一个用于标识剩余长度的字节。剩下的低7位用于标识值，范围：0~127。
    - 例如，剩余长度字段的第一个字节的最高位为1，那么意味着剩余长度至少还有1个字节，然后继续读下一个字节，下一个字节的最高位为0，那么剩余长度字段到此为止，一共2个字节。
![IoT-MQTT协议数据包剩余长度字段](../resources/static/images/IoT-MQTT协议数据包剩余长度字段.png)

所以，这4个字节最多可以标识的包长度为：（0xFF,0xFF,0xFF,0x7F）=268 435 455字节，即256MB，这是MQTT协议中数据包的最大长度。



## QoS（服务质量）

1. **QoS0：至多一次；只发送一次，收没收到不管。**
   - Client和Broker之间网络比较稳定的情况。
   - 可以接受丢失部分消息也不影响的场景。
   - 不需要离线消息。

2. **QoS1：至少一次；发送失败会重试，直到收到为止，消息可能重复。**
   - 需要接收所有消息，并可以接受处理重复消息。
   - 无法接受QoS2带来的额外开销，QoS1发送消息速度比QoS2快很多。

3. **QoS2：只有一次；发送失败会重试，但会保证Receiver只会收到一次。**
   - 必须要收到所有消息，且不能处理重复的消息，且可以接受QoS2带来的额外开销。



## MQTT.fx模拟器

1. 新建Profile，填写相关信息。

![mqttfx填写Profile.png](../resources/static/images/mqttfx填写Profile.png)

2. 填写用户名和密码。

![mattfx填写用户名密码.png](../resources/static/images/mattfx填写用户名密码.png)

3. 连接

![mqttfx连接.png](../resources/static/images/mqttfx连接.png)

4. 消息推送

![mqttfx消息推送.png](../resources/static/images/mqttfx消息推送.png)

5. 消息订阅

![mqttfx消息订阅.png](../resources/static/images/mqttfx消息订阅.png)

![mqttfx消息订阅多个topic.png](../resources/static/images/mqttfx消息订阅多个topic.png)

6. 扫描当前所有topic

![mqttfx扫描所有topic.png](../resources/static/images/mqttfx扫描所有topic.png)

7. 查看日志

![mqttfx查看日志.png](../resources/static/images/mqttfx查看日志.png)





