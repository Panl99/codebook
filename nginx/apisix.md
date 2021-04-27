文档：[https://github.com/apache/apisix/tree/master/docs/zh/latest](https://github.com/apache/apisix/tree/master/docs/zh/latest)


# APISIX概述

## APISIX简介

**APISIX** 是一个动态、实时、高性能的 API 网关， 提供负载均衡、动态上游、灰度发布、服务熔断、身份认证、可观测性等丰富的流量管理功能。

## APISIX特点

**APISIX** 可以当做流量入口，来处理所有的业务数据，包括动态路由、动态上游、动态证书、 A/B 测试、金丝雀发布(灰度发布)、蓝绿部署、限流限速、抵御恶意攻击、监控报警、服务可观测性、服务治理等。

- **全平台**
  - 云原生: 平台无关，没有供应商锁定，无论裸机还是 Kubernetes，APISIX 都可以运行。
  - 运行环境: OpenResty 和 Tengine 都支持。
  - 支持 ARM64。
- **支持协议丰富**
  - [TCP/UDP 代理](https://github.com/apache/apisix/blob/master/docs/zh/latest/stream-proxy.md): 动态 TCP/UDP 代理。
  - [Dubbo 代理](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/dubbo-proxy.md): 动态代理 HTTP 请求到 Dubbo 后端。
  - [动态 MQTT 代理](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/mqtt-proxy.md): 支持用 `client_id` 对 MQTT 进行负载均衡，同时支持 MQTT [3.1.*](http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html) 和 [5.0](https://docs.oasis-open.org/mqtt/mqtt/v5.0/mqtt-v5.0.html) 两个协议标准。
  - [gRPC 代理](https://github.com/apache/apisix/blob/master/docs/zh/latest/grpc-proxy.md)：通过 APISIX 代理 gRPC 连接，并使用 APISIX 的大部分特性管理你的 gRPC 服务。
  - [gRPC 协议转换](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/grpc-transcode.md)：支持协议的转换，这样客户端可以通过 HTTP/JSON 来访问你的 gRPC API。
  - Websocket 代理
  - Proxy Protocol
  - Dubbo 代理：基于 Tengine，可以实现 Dubbo 请求的代理。
  - HTTP(S) 反向代理
  - [SSL](https://github.com/apache/apisix/blob/master/docs/zh/latest/https.md)：动态加载 SSL 证书。
- **全动态能力**
  - [热更新和热插件](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins.md): 无需重启服务，就可以持续更新配置和插件。
  - [代理请求重写](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/proxy-rewrite.md): 支持重写请求上游的`host`、`uri`、`schema`、`enable_websocket`、`headers`信息。
  - [输出内容重写](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/response-rewrite.md): 支持自定义修改返回内容的 `status code`、`body`、`headers`。
  - [Serverless](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/serverless.md): 在 APISIX 的每一个阶段，你都可以添加并调用自己编写的函数。
  - 动态负载均衡：动态支持有权重的 round-robin 负载平衡。
  - 支持一致性 hash 的负载均衡：动态支持一致性 hash 的负载均衡。
  - [健康检查](https://github.com/apache/apisix/blob/master/docs/zh/latest/health-check.md)：启用上游节点的健康检查，将在负载均衡期间自动过滤不健康的节点，以确保系统稳定性。
  - 熔断器: 智能跟踪不健康上游服务。
  - [代理镜像](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/proxy-mirror.md): 提供镜像客户端请求的能力。
  - [流量拆分](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/traffic-split.md): 允许用户逐步控制各个上游之间的流量百分比。
- **精细化路由**
  - [支持全路径匹配和前缀匹配](https://github.com/apache/apisix/blob/master/docs/en/latest/router-radixtree.md#how-to-use-libradixtree-in-apisix)
  - [支持使用 Nginx 所有内置变量做为路由的条件](https://github.com/apache/apisix/blob/master/docs/en/latest/router-radixtree.md#how-to-filter-route-by-nginx-builtin-variable)，所以你可以使用 `cookie`, `args` 等做为路由的条件，来实现灰度发布、A/B 测试等功能
  - 支持[各类操作符做为路由的判断条件](https://github.com/iresty/lua-resty-radixtree#operator-list)，比如 `{"arg_age", ">", 24}`
  - 支持[自定义路由匹配函数](https://github.com/iresty/lua-resty-radixtree/blob/master/t/filter-fun.t#L10)
  - IPv6：支持使用 IPv6 格式匹配路由
  - 支持路由的[自动过期(TTL)](https://github.com/apache/apisix/blob/master/docs/zh/latest/admin-api.md#route)
  - [支持路由的优先级](https://github.com/apache/apisix/blob/master/docs/en/latest/router-radixtree.md#3-match-priority)
  - [支持批量 Http 请求](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/batch-requests.md)
- **安全防护**
  - 多种身份认证方式: [key-auth](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/key-auth.md), [JWT](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/jwt-auth.md), [basic-auth](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/basic-auth.md), [wolf-rbac](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/wolf-rbac.md)。
  - [IP 黑白名单](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/ip-restriction.md)
  - [Referer 白名单](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/referer-restriction.md)
  - [IdP 支持](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/openid-connect.md): 支持外部的身份认证服务，比如 Auth0，Okta，Authing 等，用户可以借此来对接 Oauth2.0 等认证方式。
  - [限制速率](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/limit-req.md)
  - [限制请求数](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/limit-count.md)
  - [限制并发](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/limit-conn.md)
  - 防御 ReDoS(正则表达式拒绝服务)：内置策略，无需配置即可抵御 ReDoS。
  - [CORS](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/cors.md)：为你的 API 启用 CORS。
  - [URI 拦截器](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/uri-blocker.md)：根据 URI 拦截用户请求。
  - [请求验证器](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/request-validation.md)。
- **运维友好**
  - OpenTracing 可观测性: 支持 [Apache Skywalking](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/skywalking.md) 和 [Zipkin](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/zipkin.md)。
  - 对接外部服务发现：除了内置的 etcd 外，还支持 [Consul](https://github.com/apache/apisix/blob/master/docs/en/latest/discovery/consul_kv.md) 和 [Nacos](https://github.com/apache/apisix/blob/master/docs/en/latest/discovery/nacos.md)，以及 [Eureka](https://github.com/apache/apisix/blob/master/docs/zh/latest/discovery.md)。
  - 监控和指标: [Prometheus](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/prometheus.md)
  - 集群：APISIX 节点是无状态的，创建配置中心集群请参考 [etcd Clustering Guide](https://etcd.io/docs/v3.4.0/op-guide/clustering/)。
  - 高可用：支持配置同一个集群内的多个 etcd 地址。
  - [控制台](https://github.com/apache/apisix-dashboard): 操作 APISIX 集群。
  - 版本控制：支持操作的多次回滚。
  - CLI: 使用命令行来启动、关闭和重启 APISIX。
  - [单机模式](https://github.com/apache/apisix/blob/master/docs/zh/latest/stand-alone.md): 支持从本地配置文件中加载路由规则，在 kubernetes(k8s) 等环境下更友好。
  - [全局规则](https://github.com/apache/apisix/blob/master/docs/zh/latest/architecture-design/global-rule.md)：允许对所有请求执行插件，比如黑白名单、限流限速等。
  - 高性能：在单核上 QPS 可以达到 18k，同时延迟只有 0.2 毫秒。
  - [故障注入](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/fault-injection.md)
  - [REST Admin API](https://github.com/apache/apisix/blob/master/docs/zh/latest/admin-api.md): 使用 REST Admin API 来控制 Apache APISIX，默认只允许 127.0.0.1 访问，你可以修改 `conf/config.yaml` 中的 `allow_admin` 字段，指定允许调用 Admin API 的 IP 列表。同时需要注意的是，Admin API 使用 key auth 来校验调用者身份，**在部署前需要修改 `conf/config.yaml` 中的 `admin_key` 字段，来保证安全。**
  - 外部日志记录器：将访问日志导出到外部日志管理工具。([HTTP Logger](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/http-logger.md), [TCP Logger](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/tcp-logger.md), [Kafka Logger](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/kafka-logger.md), [UDP Logger](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugins/udp-logger.md))
  - [Helm charts](https://github.com/apache/apisix-helm-chart)
- **高度可扩展**
  - [自定义插件](https://github.com/apache/apisix/blob/master/docs/zh/latest/plugin-develop.md): 允许挂载常见阶段，例如`init`, `rewrite`，`access`，`balancer`,`header filter`，`body filter` 和 `log` 阶段。
  - 自定义负载均衡算法：可以在 `balancer` 阶段使用自定义负载均衡算法。
  - 自定义路由: 支持用户自己实现路由算法。


## APISIX基本概念


# APISIX使用

## APISIX安装

### 源码编译方式安装

适用于所有系统

1. 安装运行时依赖：OpenResty 和 etcd，以及编译的依赖：luarocks。参考[依赖安装文档](https://github.com/apache/apisix/blob/master/docs/zh/latest/install-dependencies.md)

2. 下载最新的源码发布包：

   ```shell
   $ mkdir apisix-2.5
   $ wget https://downloads.apache.org/apisix/2.5/apache-apisix-2.5-src.tgz
   $ tar zxvf apache-apisix-2.5-src.tgz -C apisix-2.5
   ```

3. 安装运行时依赖的 Lua 库：

   ```shell
   $ make deps
   ```

4. 检查 APISIX 的版本号：

   ```shell
   $ ./bin/apisix version
   ```

5. 启动 APISIX:

   ```shell
   $ ./bin/apisix start
   ```

**源码编译：**

```shell
$ git clone git@github.com:apache/apisix.git
$ cd apisix
$ make deps
```



### [Docker 镜像安装](https://hub.docker.com/r/apache/apisix)

适用所有系统

```shell
$ docker pull apache/apisix
```

Docker 镜像中不包含 etcd，可以参考 [docker compose 的示例](https://github.com/apache/apisix-docker/tree/master/example)来启动一个测试集群。

**Docker镜像编译：**

```shell
$ git clone https://github.com/apache/apisix-docker.git
$ cd apisix-docker
$ sudo docker build -f alpine-dev/Dockerfile .
```



### RPM 包安装

只适用于 CentOS 7

1. 安装依赖：OpenResty, etcd 和 OpenSSL develop library，参考[依赖安装文档](https://github.com/apache/apisix/blob/master/docs/zh/latest/install-dependencies.md#centos-7)
2. 安装 APISIX：

```shell
$ sudo yum install -y https://github.com/apache/apisix/releases/download/2.5/apisix-2.5-0.x86_64.rpm
```

3. 检查 APISIX 的版本号：

```shell
$ apisix version
```

4. 启动 APISIX:

```shell
$ apisix start
```

**注意**：APISIX 从 v2.0 开始不再支持 etcd v2 协议，且 etcd 最低支持版本为 v3.4.0



## APISIX实战

[入门指南](https://github.com/apache/apisix/blob/master/docs/zh/latest/getting-started.md)



# APISIX原理

## APISIX模块原理

## APISIX源码分析



# APISIX优化

## APISIX参数优化



## APISIX二次开发

