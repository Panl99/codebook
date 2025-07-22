# 目录

- [CompletableFuture](#CompletableFuture)
- [Java异步编程难题拆解👇👇👇](https://blog.csdn.net/sinat_28461591/category_12981736.html)
    - [深入理解 Java CompletableFuture：核心原理剖析与企业级使用场景实战](#深入理解Java CompletableFuture：核心原理剖析与企业级使用场景实战)
    - [CompletableFuture VS Future：性能、编程模型与线程控制全维度对比实战](#CompletableFuture VS Future：性能、编程模型与线程控制全维度对比实战)
    - [thenApply、thenCompose 与 thenCombine 全解析：构建高可用异步任务链的实战方法论](#对thenApply、thenCompose、thenCombine全解析：构建高可用异步任务链的实战方法论)
    - [使用 supplyAsync 和 runAsync 的最佳实践与常见坑点解析](#使用supplyAsync、runAsync的最佳实践与常见坑点解析)
    - [常见异步组合模型拆解：并行、顺序、依赖树、聚合处理的工程实现与最佳实践](#常见异步组合模型拆解：并行、顺序、依赖树、聚合处理的工程实现与最佳实践)
    - [深入拆解 CompletableFuture 异常处理机制：handle、exceptionally、whenComplete 全面解析与实战策略](#深入拆解CompletableFuture异常处理机制：handle、exceptionally、whenComplete全面解析与实战策略)
    - [深入理解 CompletableFuture 的线程模型：默认 ForkJoinPool 与自定义线程池实战解析](#深入理解CompletableFuture的线程模型：默认ForkJoinPool与自定义线程池实战解析)
    - [构建响应式流水线：使用 thenCompose 实现异步依赖编排全流程解析](#构建响应式流水线：使用thenCompose实现异步依赖编排全流程解析)
    - [基于 CompletableFuture 的典型IO异步封装实战：远程接口调用全流程优化解析](#基于CompletableFuture的典型IO异步封装实战：远程接口调用全流程优化解析)
        - [远程调用中常见的同步瓶颈与资源阻塞问题](#远程调用中常见的同步瓶颈与资源阻塞问题)
        - [基于 CompletableFuture 的异步 IO 封装核心思路](#基于CompletableFuture的异步IO封装核心思路)
        - [HTTP 接口异步封装实战（以 WebClient / OkHttp 为例）](#HTTP接口异步封装实战)
        - [RPC 微服务异步封装实战（以 Dubbo / gRPC 为例）](#RPC微服务异步封装实战)
        - [超时控制、限流熔断与异常恢复策略集成](#超时控制、限流熔断、异常恢复策略集成)
        - [上下文透传与 TraceId 链路一致性维护](#上下文透传与TraceId链路一致性维护)
        - [多接口并发请求聚合处理模型（allOf + thenCombine）](#多接口并发请求聚合处理模型（allOf+thenCombine）)
        - [构建通用异步 IO 客户端组件的工程实战路径](#构建通用异步IO客户端组件的工程实战路径)
    - [CompletableFuture 中的阻塞陷阱：join/get 使用限制与替代方案](#CompletableFuture中的阻塞陷阱：join/get使用限制与替代方案)
    - [异步任务堆积与线程耗尽问题定位与治理策略：基于 CompletableFuture 的系统化诊断与优化实践](#异步任务堆积与线程耗尽问题定位与治理策略：基于CompletableFuture的系统化诊断与优化实践)
    - [任务链中异步错误传播失败的根因排查与解决：CompletableFuture 异常链路的稳定性实战解析](#任务链中异步错误传播失败的根因排查与解决：CompletableFuture异常链路的稳定性实战解析)
    - [业务中大量小任务异步组合导致性能抖动问题优化实战](#业务中大量小任务异步组合导致性能抖动问题优化实战)
    - [CompletableFuture 与数据库连接池资源耗尽问题协同分析：异步编排下的资源瓶颈复现与优化实践](#CompletableFuture与数据库连接池资源耗尽问题协同分析：异步编排下的资源瓶颈复现与优化实践)
    - [异步调用返回顺序不可控导致业务逻辑异常的修复思路与工程实践](#异步调用返回顺序不可控导致业务逻辑异常的修复思路与工程实践)
    - [多 CompletableFuture 聚合中某个任务失败导致整体 hang 住的处理策略](#多CompletableFuture聚合中某个任务失败导致整体hang住的处理策略)
- [美团：CompletableFuture示例](#美团CompletableFuture示例)

----------------------------------------------------------------------------------------------------------------------------------------------------------

# CompletableFuture

- [异步非阻塞：CompletableFuture](https://github.com/Panl99/demo/tree/master/demo-action/src/main/java/com/lp/demo/action/java_in_action/CompletableFutureDemo.java)

子线程会跟随主线程任务结束而结束，CompletableFuture.allOf(List<CompletableFuture>).join()阻塞所有线程直到所有线程执行完毕。

[目录](#目录)

----------------------------------------------------------------------------------------------------------------------------------------------------------
[🔗Java异步编程难题拆解👇👇👇](https://blog.csdn.net/sinat_28461591/category_12981736.html)

## 深入理解Java CompletableFuture：核心原理剖析与企业级使用场景实战
> [🔗链接](https://blog.csdn.net/sinat_28461591/article/details/148463038)

本文结合一线开发实践，系统性地剖析 CompletableFuture 的底层运行机制、线程模型、核心方法行为，并通过多个真实使用场景（如服务聚合、资源隔离、异常恢复等）展示其在高并发系统中的应用效果，为工程团队在设计异步架构时提供指导。

目录：
1. CompletableFuture 背景与诞生初衷
    - 异步编程的痛点回顾（Future、线程池、回调地狱）
    - [CompletableFuture 设计目标与定位](#CompletableFuture设计目标与定位)
2. 核心类结构与工作机制详解
    - [内部组成：Completion、UniCompletion、ForkJoinPool 机制](#内部组成：Completion、UniCompletion、ForkJoinPool机制)
    - [多线程环境下的状态流转与内存模型保障](#多线程环境下的状态流转与内存模型保障)
    - 自定义线程池与默认 ForkJoinPool 的差异
3. 基础 API 全解与调用语义
    - [supplyAsync/runAsync、thenApply/thenCompose、handle 等方法解读](#对supplyAsync/runAsync、thenApply/thenCompose、handle等方法解读)
    - 方法链中的同步/异步语义区别（是否传 executor）
    - [使用 thenCompose 构建任务依赖链的标准做法](#使用thenCompose构建任务依赖链的标准做法)
4. 性能行为分析：与 Future/ExecutorService 对比测试
    - 多任务并发执行下的吞吐率对比（含真实 benchmark 数据）：结论：CompletableFuture 在高频场景下表现更优，特别是在任务链可异步解耦的场景中（CompletableFuture + Async + 自定义线程池）。
    - 非阻塞性能与上下文切换开销分析
      - CompletableFuture 利用回调机制避免阻塞，降低线程上下文切换成本。
      - 相较于传统线程池调度，ForkJoinPool 更适合短生命周期任务（如 CPU 密集型计算）。
      - 不过如果链路过长且处理逻辑复杂，仍可能出现线程抖动或内存压力，应合理设计链路深度。
    - 线程数与任务大小对系统资源的影响评估  
      最佳实践建议：
      - 使用小任务拆分大任务，分散负载；
      - 将耗时任务移出公共线程池，配置独立 ExecutorService；
      - 控制链式深度，避免构建上百层嵌套链式调用，影响栈帧稳定性。 
5. 企业项目中的典型使用场景
    - [服务聚合调用（RPC并发执行）](#服务聚合调用（RPC并发执行）)
    - [数据预加载与缓存异步刷新](#数据预加载与缓存异步刷新)
    - [与数据库/外部服务的 IO 并行交互](#与数据库/外部服务的IO并行交互)
    - [实时日志/监控上传异步处理解耦主链路](#实时日志/监控上传异步处理解耦主链路)
6. 异步异常处理机制实战
    - [handle、exceptionally、whenComplete 的差异性行为复现](#handle、exceptionally、whenComplete的差异性行为复现)
    - [多任务组合中局部失败如何优雅处理](#多任务组合中局部失败如何优雅处理)
    - [异步链中异常传播导致业务不一致的案例拆解](#异步链中异常传播导致业务不一致的案例拆解)
7. 线程池配置与资源隔离建议
    - 不同业务使用不同线程池的隔离实践
    - 如何防止 ForkJoinPool 被业务任务长时间阻塞
    - [Executor 配置建议（核心线程数、队列大小、拒绝策略）](#Executor配置建议（核心线程数、队列大小、拒绝策略）)
8. [从零构建一个可复用的 CompletableFuture 工具组件](#从零构建一个可复用的CompletableFuture工具组件)
    - 封装统一的异步任务执行框架
    - 上下文透传（如 TraceId、用户信息）
    - 埋点与监控接入点预留策略


#### CompletableFuture设计目标与定位

- **链式编程模型**：通过 `thenApply`、`thenCompose`、`handle` 等方法，实现多个异步任务的串联与组合，代码逻辑更加线性、清晰。
- **非阻塞执行**：异步任务默认在 `ForkJoinPool.commonPool()` 中执行，支持自定义线程池；通过回调机制完成结果处理，避免线程阻塞。
- **灵活的异常恢复机制**：支持链路中途任一节点出错的局部处理，不影响其他独立分支的执行。
- **多任务组合能力**：如 `allOf`、`anyOf`，可以聚合多个异步任务的结果或监听多个任务中任意一个完成。


#### 内部组成：Completion、UniCompletion、ForkJoinPool机制

CompletableFuture 底层由多个关键组件组成，它的异步能力和任务链管理正是依赖这些结构：
- `Completion`：核心抽象类，用于定义异步计算完成后需要执行的操作。
- `UniCompletion`：表示单一源（一个前置任务）的回调阶段，是最常见的回调类型，典型如 `thenApply`。
- `BiCompletion`：表示两个源任务依赖的回调，例如 `thenCombine`。
- `ForkJoinPool.commonPool()`：Java 8 引入的共享线程池，默认用于执行异步任务。其设计遵循工作窃取算法（Work Stealing），在任务量不均时可提高 CPU 利用率。

一个 CompletableFuture 实例在底层维护一个状态位和多个 Completion 对象链表。当任务完成或失败后，会触发相关回调链表的逐一执行。


#### 多线程环境下的状态流转与内存模型保障

CompletableFuture 的状态由 result 字段表示，其值可能为：
- `null`：表示未完成
- `result != null && !(result instanceof Throwable)`：表示正常完成
- `result instanceof Throwable`：表示任务异常

所有状态变更均通过 CAS（Compare-And-Swap）机制完成，确保线程安全。在并发环境下，无论多少线程等待一个任务完成，状态变更始终是原子且唯一的。任务完成后，所有等待线程会被唤醒并执行各自注册的回调。


#### 对supplyAsync/runAsync、thenApply/thenCompose、handle等方法解读

CompletableFuture 提供了丰富的 API 用于启动异步任务、处理结果、捕捉异常。其中核心方法按功能划分如下：

1. 启动异步任务：
    - `supplyAsync(Supplier<T>)`：用于有返回值的异步任务，典型应用如异步获取数据。
    - `runAsync(Runnable)`：用于无返回值的异步任务，如异步日志、清理等操作。
    - 这两个方法都可接收一个自定义 `Executor` 作为第二参数。
2. 结果处理方法（链式处理）：
    - `thenApply(Function)`：接收上一个任务结果并返回新结果，同步调用。
    - `thenApplyAsync(Function)`：异步处理上一个结果，默认使用 ForkJoinPool 或指定的 Executor。
    - `thenCompose(Function)`：适用于需要基于上一个结果返回新的 CompletableFuture 场景，实现扁平化链式调用。
3. 多任务组合处理：
    - `thenCombine(future2, BiFunction)`：等待两个任务都完成后聚合结果。
    - `allOf(futures…)`：等待所有任务完成，无返回结果。
    - `anyOf(futures…)`：任一任务完成即返回。
4. 异常处理：
    - `handle(BiFunction)`：无论是否异常都会执行，并可访问异常对象。
    - `exceptionally(Function)`：仅在异常时执行，用于错误恢复。
    - `whenComplete(BiConsumer)`：仅用于观察，不改变最终结果。

#### 使用thenCompose构建任务依赖链的标准做法

很多场景中，第二个异步任务依赖第一个结果，比如：
```java
CompletableFuture<User> userFuture = getUserAsync(userId);
CompletableFuture<List<Order>> orderFuture = userFuture.thenCompose(user -> getOrdersByUserAsync(user));
```
这是典型的 “扁平化” 编排模式，避免了：`.thenApply(user -> getOrdersByUserAsync(user)) // 返回的是 CompletableFuture<CompletableFuture<List<Order>>>`这种嵌套结果的困扰。

在需要执行多个层级异步请求时，应尽可能使用 `thenCompose` 而非 `thenApply`，保持链路清晰、易于追踪。


#### 服务聚合调用（RPC并发执行）

在微服务架构下，一个网关层或 BFF 层常需要聚合多个下游服务的响应，如：
- 用户信息服务
- 订单服务
- 推荐服务
```java
CompletableFuture<User> userFuture = CompletableFuture.supplyAsync(() -> userClient.getUser(uid), executor);
CompletableFuture<Order> orderFuture = CompletableFuture.supplyAsync(() -> orderClient.getOrder(uid), executor);
CompletableFuture<Recommendation> recFuture = CompletableFuture.supplyAsync(() -> recClient.getRecommendations(uid), executor);

CompletableFuture.allOf(userFuture, orderFuture, recFuture).join();
```
此种写法广泛用于聚合层服务开发中，经过合理线程池配置与异常兜底机制，可实现毫秒级聚合响应。

#### 数据预加载与缓存异步刷新

业务场景中常存在如下模式：
- 页面渲染提前预加载数据
- 热点缓存过期时自动刷新后台数据

使用 CompletableFuture 可在业务主链路不中断的情况下并发刷新数据：
```java
cache.get("productInfo").orElseGet(() -> {
    CompletableFuture.runAsync(() -> {
        Product p = productService.fetchLatest(pid);
        cache.put("productInfo", p);
    }, cacheRefreshExecutor);
    return fallbackProduct();
});
```
既能提高可用性，又能减少阻塞链路的资源占用。


#### 与数据库/外部服务的IO并行交互

以电商详情页为例，需同时加载以下数据：
- 商品基础信息（数据库）
- 库存状态（Redis）
- 价格（下游定价服务）

若同步串行完成，瓶颈在于 IO 交互。而通过 CompletableFuture 将多个 IO 异步并发处理，可大幅压缩端到端响应时间。
```java
CompletableFuture<Product> productFuture = supplyAsync(() -> dao.getProduct(pid), ioExecutor);
CompletableFuture<Stock> stockFuture = supplyAsync(() -> redis.getStock(pid), ioExecutor);
CompletableFuture<Price> priceFuture = supplyAsync(() -> priceClient.getPrice(pid), ioExecutor);
```
结合 thenCombine 聚合结果，再统一构建返回对象。

#### 实时日志/监控上传异步处理解耦主链路

主链路上日志、埋点、监控、事件通知等“边缘逻辑”常见问题是阻塞主业务线程，使用 `CompletableFuture.runAsync()` 可实现异步处理，提升主链路流畅性：
```java
CompletableFuture.runAsync(() -> logClient.send(logInfo), asyncLogExecutor);
```
在异步链上接入监控服务（如自定义的 APM Agent）还能在不中断链路的前提下实现完整链路跟踪。


#### handle、exceptionally、whenComplete的差异性行为复现

方法名    |是否可访问异常    |是否影响结果返回    |是否可恢复执行
---|---|---|---
handle          |✅              |✅              |✅
exceptionally    |✅（仅异常）    |✅              |✅
whenComplete    |✅              |❌（不可变更）    |❌

示例差异：
```java
CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("fail");
}).handle((res, ex) -> {
    if (ex != null) return "default";
    return res;
}); // 恢复执行，结果为"default"

CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("fail");
}).whenComplete((res, ex) -> {
    log.error("error", ex);
}); // 不影响主链路，异常仍抛出
```

#### 多任务组合中局部失败如何优雅处理

在使用 `CompletableFuture.allOf()` 组合多个任务时，一旦某个任务抛出异常，整个任务链将提前终止，需通过单个任务异常隔离方式避免影响整体结果：
```java
CompletableFuture<Order> orderFuture = CompletableFuture.supplyAsync(() -> orderService.get(), executor)
    .exceptionally(ex -> {
        log.warn("fallback to empty order", ex);
        return new Order(); // 降级
    });
```
最终聚合结果时，需判断子任务状态，避免因某一任务异常影响其他任务结果获取。

#### 异步链中异常传播导致业务不一致的案例拆解

真实项目案例（金融系统）中，出现如下问题：
- 异步链中某个任务失败未正确传播异常，主线程调用 `.get()` 时抛出 `ExecutionException`
- 异常被吞噬导致业务状态未一致提交，事务日志未补全
- 根因是某个 `thenApply` 执行同步方法，在某些情况下抛出空指针，而没有使用 `handle` 包装处理

修复方案：
1. 所有非幂等链路，使用 `handle` 包裹最终节点，确保异常被感知。
2. 补充 `CompletableFuture.exceptionallyCompose`（Java 12）或手动封装兜底逻辑。
3. 建议引入统一异步链路异常追踪框架（如埋点标识+TraceId 传递+APM 关联）。

最佳实践建议：
- 对关键路径中所有异步节点设置可观察异常处理逻辑；
- 对非主链路、异步副本逻辑设置降级 fallback；
- 在任务聚合中提前注册任务状态位或 wrap 为统一处理结构。

#### Executor配置建议（核心线程数、队列大小、拒绝策略）

一个合理的线程池配置，需根据以下维度评估：
- **CPU 密集型任务**：核心线程数 ≈ CPU 核心数 + 1，使用 `SynchronousQueue` 或短队列
- **IO 密集型任务**：核心线程数 ≈ CPU 核心数 × 2 ~ 4，队列长度可稍长

拒绝策略推荐：
- `CallerRunsPolicy`：任务由提交者线程执行，降低提交速率
- `DiscardPolicy`：静默丢弃，不推荐用于关键链路
- 自定义策略：可结合日志告警或限流处理

示例配置：
```java
ThreadPoolExecutor rpcExecutor = new ThreadPoolExecutor(
    16, // core
    32, // max
    60, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(1000),
    new NamedThreadFactory("rpc-async"),
    new ThreadPoolExecutor.CallerRunsPolicy()
);
```
务必设置线程池监控与报警机制，防止任务堆积后系统雪崩。

### 从零构建一个可复用的CompletableFuture工具组件

**封装统一的异步任务执行框架**
- 绑定线程上下文
- 捕捉异常并记录日志
- 扩展链路追踪或限流监控
```java
public class AsyncTaskExecutor {

    private static final Executor rpcExecutor = buildRpcExecutor();

    public static <T> CompletableFuture<T> supply(Supplier<T> supplier, String traceId) {
        return CompletableFuture.supplyAsync(() -> {
            MDC.put("traceId", traceId);
            try {
                return supplier.get();
            } finally {
                MDC.clear();
            }
        }, rpcExecutor).exceptionally(ex -> {
            log.error("Async failed", ex);
            return null;
        });
    }
}
```

**上下文透传（如 TraceId、用户信息）**

默认线程池与 CompletableFuture 不具备上下文感知能力，ThreadLocal 内容无法跨线程自动传递。可通过如下方式封装：
```java
String traceId = MDC.get("traceId");
CompletableFuture.supplyAsync(() -> {
    MDC.put("traceId", traceId);
    // do work
    MDC.clear();
    return result;
});
```
也可结合阿里开源组件 TransmittableThreadLocal 自动完成上下文复制。

在涉及分布式调用链路时，这种上下文透传至关重要，尤其在链路追踪（如 SkyWalking、Zipkin）中常用。

**埋点与监控接入点预留策略**

在工具类或组件框架中，可统一引入：
- 调用耗时统计
- 异常类型采样
- 异步任务数监控
- 线程池使用率统计
```java
public static <T> CompletableFuture<T> timedTask(Supplier<T> supplier, Executor executor, String metricName) {
    long start = System.currentTimeMillis();
    return CompletableFuture.supplyAsync(() -> {
        try {
            return supplier.get();
        } finally {
            Metrics.record(metricName, System.currentTimeMillis() - start);
        }
    }, executor);
}
```
配合监控平台如 Prometheus + Grafana 可实现完整异步性能视图输出。

[目录](#目录)


## CompletableFuture VS Future：性能、编程模型与线程控制全维度对比实战

[目录](#目录)

## 对thenApply、thenCompose、thenCombine全解析：构建高可用异步任务链的实战方法论


[目录](#目录)

## 使用supplyAsync、runAsync的最佳实践与常见坑点解析

[目录](#目录)

## 常见异步组合模型拆解：并行、顺序、依赖树、聚合处理的工程实现与最佳实践

[目录](#目录)

## 深入拆解CompletableFuture异常处理机制：handle、exceptionally、whenComplete全面解析与实战策略

[目录](#目录)


## 深入理解CompletableFuture的线程模型：默认ForkJoinPool与自定义线程池实战解析

[目录](#目录)

## 构建响应式流水线：使用thenCompose实现异步依赖编排全流程解析

[目录](#目录)

## 基于CompletableFuture的典型IO异步封装实战：远程接口调用全流程优化解析
> [🔗链接](https://blog.csdn.net/sinat_28461591/article/details/148599328)


关键词：CompletableFuture、异步封装、IO密集、远程接口、服务调用、线程池隔离、降级容错、响应优化、Java并发编程、异步编排

摘要：  
在企业级 Java 项目中，调用外部 HTTP 接口或微服务 RPC 是最常见的 IO 密集操作场景。传统阻塞式写法不仅影响吞吐率，还容易造成主线程阻塞和系统资源浪费。  
本文将以实际生产服务为背景，系统讲解如何基于 CompletableFuture 对远程接口调用进行异步封装，包括线程池隔离、上下文透传、异常容错、响应聚合等关键实践，结合最新线程模型调优与异步链构建策略，提升整体系统性能与稳定性。

目录：
1. [远程调用中常见的同步瓶颈与资源阻塞问题](#远程调用中常见的同步瓶颈与资源阻塞问题)
2. [基于 CompletableFuture 的异步 IO 封装核心思路](#基于CompletableFuture的异步IO封装核心思路)
3. [HTTP 接口异步封装实战（以 WebClient / OkHttp 为例）](#HTTP接口异步封装实战)
4. [RPC 微服务异步封装实战（以 Dubbo / gRPC 为例）](#RPC微服务异步封装实战)
5. [超时控制、限流熔断与异常恢复策略集成](#超时控制、限流熔断、异常恢复策略集成)
6. [上下文透传与 TraceId 链路一致性维护](#上下文透传与TraceId链路一致性维护)
7. [多接口并发请求聚合处理模型（allOf + thenCombine）](#多接口并发请求聚合处理模型（allOf+thenCombine）)
8. [构建通用异步 IO 客户端组件的工程实战路径](#构建通用异步IO客户端组件的工程实战路径)

### 远程调用中常见的同步瓶颈与资源阻塞问题

在典型的 Java 企业服务中，远程接口调用（包括 HTTP、RPC、数据库访问等）普遍存在响应时间长、阻塞线程多、资源占用不均的问题，尤其在用户请求高并发场景下容易成为性能瓶颈。

一、同步调用导致线程堆积

传统同步调用方式使用阻塞 IO，线程必须等待远端响应返回才能继续后续处理。例如：
```java
UserInfo userInfo = userService.getUser(uid); // 阻塞等待
```
当 QPS 上升时，大量线程处于 WAITING 状态，占用线程池资源，系统响应能力下降。

二、线程资源浪费与超时不可控

多个接口串行调用，如：
```java
UserInfo user = userService.getUser(uid);
List<Item> items = itemService.getItems(user.getTags());
```
上述逻辑一旦某个服务响应慢（如 300ms），整体接口处理时间将线性增长，造成性能劣化。

三、典型业务瓶颈案例

- 在某电商后台系统中，首页展示依赖多个服务接口（用户画像、商品推荐、活动推荐、物流状态等）。
  原始串行调用下，TP99 达 1.2s，用户首屏体验严重下滑。
  通过引入 CompletableFuture 异步并行后，TP99 降至 300ms 以内。

### 基于CompletableFuture的异步IO封装核心思路

基于 CompletableFuture 将 IO 密集型接口调用封装为异步任务，在服务内部并行调度与合并处理，提高系统响应效率。

一、目标流程：
1. 异步发起远程调用，释放主线程；
2. 支持链式任务编排，实现多接口依赖关系管理；
3. 引入线程池隔离机制，避免公共线程资源耗尽；
4. 具备标准异常处理、上下文透传能力；
5. 可被复用、易于扩展。

二、基础封装模式：将 IO 操作转换为 CompletableFuture
```java
public CompletableFuture<UserInfo> getUserAsync(String uid) {
    return CompletableFuture.supplyAsync(() -> userService.getUser(uid), ioThreadPool);
}
```
其中 `ioThreadPool` 是专门用于 IO 密集任务的线程池实例，避免与核心业务逻辑线程池混用。

三、任务链构建示例

- 将多个接口调用以链式组合：
```java
getUserAsync(uid)
    .thenCompose(user -> getItemsAsync(user.getTags()))
    .thenApply(items -> buildResponse(items));
```

- 如果多个任务独立：
```java
CompletableFuture<UserInfo> userFuture = getUserAsync(uid);
CompletableFuture<Activity> activityFuture = getActivityAsync();

userFuture.thenCombine(activityFuture, (user, activity) -> buildResponse(user, activity));
```

四、线程隔离建议
- IO 密集任务线程池建议配置：coreSize = 20-100，queueSize = 1000+；
- 防止与 ForkJoinPool 共用线程资源；
- 配合 RejectedExecutionHandler 做熔断策略。

五、与 Reactor、WebFlux 等响应式框架配合
- 在响应式项目中（如 Spring WebFlux），可使用 Mono.fromFuture(getUserAsync(...)) 将异步封装与响应流打通，实现真正的端到端非阻塞调用链。
- 异步封装的本质在于把原本“阻塞卡点”变成“可组合任务单元”，通过标准化构建逻辑流程，打通任务编排、错误恢复与线程治理的全流程，为系统注入可扩展的异步能力基础。


### HTTP接口异步封装实战

在典型的微服务架构或 BFF 层中，调用下游 HTTP 接口（如 REST API、OpenAPI）是高频操作。通过 CompletableFuture 对其进行异步封装，可以有效提升吞吐率，降低主线程阻塞风险。

一、Spring WebClient 异步封装方式

Spring WebFlux 提供的 WebClient 支持响应式非阻塞模型，结合 CompletableFuture 可实现更广泛场景下的整合。 封装形式如下：
```java
public CompletableFuture<UserInfo> fetchUserInfo(String uid) {
    return webClient.get()
        .uri("/user/{uid}", uid)
        .retrieve()
        .bodyToMono(UserInfo.class)
        .toFuture(); // 转为 CompletableFuture
}
```

优势：
- 全链路非阻塞；
- 易于集成 Spring Reactor；
- 与异步编排链天然兼容。

二、OkHttp 异步回调包装为 CompletableFuture

OkHttp 提供的是回调式异步接口，需要手动封装为 CompletableFuture：
```java
public CompletableFuture<String> getAsync(String url) {
    CompletableFuture<String> future = new CompletableFuture<>();

    Request request = new Request.Builder().url(url).build();
    httpClient.newCall(request).enqueue(new Callback() {
        public void onResponse(Call call, Response response) throws IOException {
            future.complete(response.body().string());
        }
        public void onFailure(Call call, IOException e) {
            future.completeExceptionally(e);
        }
    });

    return future;
}
```

封装后可直接参与链式调用：
```java
getAsync("https://api.example.com/info")
    .thenApply(json -> parseUser(json))
    .exceptionally(ex -> fallbackUser());
```

三、接入建议与注意事项
- 建议为每个 HTTP 组件配置独立线程池，避免资源抢占；
- 设置合理超时时间，防止接口拖垮主链；
- 结合 exceptionally 做超时/降级逻辑；
- 对关键接口添加埋点指标（如 RT、成功率、异常类型分布）；

HTTP 接口的异步封装，是提升系统响应速度与异步编排能力的基础，推荐统一封装为组件形式供业务使用。

### RPC微服务异步封装实战

相比 HTTP 接口，RPC 框架如 Dubbo、gRPC 在异步调用方面也逐步具备完善支持，适合在服务间高性能通信中构建异步流水线。

一、Dubbo 的 CompletableFuture 支持（3.x+）

Dubbo 3.x 开始支持原生 CompletableFuture 接口返回值：
```java
@DubboReference
private UserService userService;

CompletableFuture<UserInfo> future = userService.getUserAsync(uid);
```

Dubbo 会自动将异步响应转为 CompletableFuture，并进行线程模型隔离，适合直接参与链式组合：
```java
userService.getUserAsync(uid)
    .thenCompose(user -> recommendService.getByTags(user.getTags()))
    .thenApply(recommendList -> buildView(user, recommendList));
```

注意事项：
- 建议使用 async = true 标注服务；
- 服务提供端需支持异步响应结构；
- 避免在异步回调中执行阻塞逻辑。

二、gRPC 的异步调用模型集成

gRPC 使用回调方式处理异步响应，可通过 SettableFuture 或 CompletableFuture 进行适配：
```java
public CompletableFuture<UserInfo> getUserInfoAsync(String uid) {
    CompletableFuture<UserInfo> future = new CompletableFuture<>();

    stub.getUser(UserRequest.newBuilder().setUid(uid).build(), new StreamObserver<UserInfo>() {
        public void onNext(UserInfo userInfo) {
            future.complete(userInfo);
        }

        public void onError(Throwable t) {
            future.completeExceptionally(t);
        }

        public void onCompleted() {}
    });

    return future;
}
```
集成后可构建统一的异步调用链，支持 thenCompose、exceptionally、timeout 等流程。

三、工程化建议

- 为每个 RPC 客户端配置独立线程池隔离；
- 建议将封装方法置于 service-invoker 层统一管理；
- 集成超时控制、异常打点与熔断策略；
- 与 Tracing 系统集成上下文传递（如 TraceId、用户标识）；

通过 CompletableFuture 异步封装 RPC 接口，既能保持高性能通信优势，又可实现统一编排与可观测性的增强，是现代微服务架构中异步治理的关键路径。

### 超时控制、限流熔断、异常恢复策略集成

在异步远程调用中，除了基本的任务执行逻辑外，健壮的容错体系是保障服务稳定性的关键。基于 CompletableFuture，可以灵活实现超时控制、限流熔断和异常恢复等机制，增强链路弹性。

一、异步超时控制策略

CompletableFuture 本身不内置超时机制，但可结合 orTimeout（Java 9+）或手动创建 “定时失败任务” 实现超时保护：
```java
// Java 9+
remoteCall().orTimeout(500, TimeUnit.MILLISECONDS);

// Java 8
public static <T> CompletableFuture<T> withTimeout(CompletableFuture<T> future, long timeoutMillis) {
    CompletableFuture<T> timeoutFuture = new CompletableFuture<>();
    scheduler.schedule(() -> timeoutFuture.completeExceptionally(new TimeoutException()), timeoutMillis, TimeUnit.MILLISECONDS);
    return future.applyToEither(timeoutFuture, Function.identity());
}
```
这样能避免接口长时间阻塞资源，提升系统抗压能力。

二、限流与熔断器适配

将异步调用包装在限流器/熔断器逻辑中，实现入口级保护：
```java
public <T> CompletableFuture<T> withCircuitBreaker(Supplier<CompletableFuture<T>> supplier) {
    if (breaker.allowRequest()) {
        try {
            return supplier.get().whenComplete((res, ex) -> breaker.recordResult(ex));
        } catch (Exception ex) {
            breaker.recordFailure();
            throw ex;
        }
    } else {
        return CompletableFuture.failedFuture(new RuntimeException("Circuit breaker open"));
    }
}
```
推荐集成 Sentinel、Resilience4j 等支持异步的限流熔断组件，并统一封装策略参数。

三、异常恢复策略集成

结合 exceptionally、handle 等 API，可实现异步链的异常兜底与降级逻辑：
```java
getUserAsync(uid)
    .exceptionally(ex -> defaultUser())
    .thenCompose(user -> getItemsAsync(user.getTags()));
```

或统一定义恢复函数：
```java
Function<Throwable, UserInfo> recover = ex -> new UserInfo("default");
...
getUserAsync(uid).exceptionally(recover);
```

工程上建议在业务中定义统一的 FallbackRegistry，并由组件层注入对应的 fallback 逻辑。


### 上下文透传与TraceId链路一致性维护

在异步调用链路中，ThreadLocal 类型的上下文变量（如 TraceId、用户ID、MDC 日志变量）往往无法自动透传，导致日志缺失、链路追踪断裂。合理的上下文传播机制是高可观测性系统的基本要求。

一、线程上下文丢失的问题根源

CompletableFuture 默认在异步线程池（如 ForkJoinPool）中执行任务，这些线程与主线程上下文不同，ThreadLocal 不会自动共享。例如：
```java
MDC.put("traceId", "abc123");
CompletableFuture.runAsync(() -> {
    log.info("traceId: {}", MDC.get("traceId")); // null
});
```

二、使用 TransmittableThreadLocal（TTL）实现透传

阿里开源的 [TTL](https://github.com/alibaba/transmittable-thread-local) 项目支持在线程池与异步框架中传递上下文，配合 CompletableFuture 使用：
```java
Runnable task = () -> log.info(MDC.get("traceId"));
TtlRunnable ttlTask = TtlRunnable.get(task);
CompletableFuture.runAsync(ttlTask, executor);
```

也支持自动增强线程池：
```java
Executor ttlExecutor = TtlExecutors.getTtlExecutor(realExecutor);
```

可确保 MDC、RequestContextHolder 等上下文变量在异步任务中保持一致。

三、上下文透传封装策略

建议统一封装带有上下文能力的异步执行器：
```java
public class ContextAwareExecutor {
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(TtlWrappers.wrap(supplier), ttlExecutor);
    }
}
```
将所有异步调用统一通过该组件执行，避免忘记透传问题。

四、可观测性增强建议

- 为每个异步任务注入 TraceId，并打点耗时；
- 集成 Zipkin、Skywalking、Jaeger 等链路追踪系统；
- 在异步链末端记录任务链完整路径与结果状态。

通过上下文透传机制，CompletableFuture 的链式异步编排得以保持链路一致性，不仅提升日志与监控能力，也为后续问题定位提供了充足的上下文信息支持。


### 多接口并发请求聚合处理模型（allOf+thenCombine）

在实际业务开发中，经常会遇到 “多个下游接口并发调用并聚合结果” 的场景，例如首页加载需要同时请求用户画像、商品推荐、活动信息等。这类并发聚合处理非常适合用 `CompletableFuture` 的 `allOf` 和 `thenCombine` 模型实现。

一、使用 `allOf` 聚合多个异步任务

当多个接口彼此独立，只需等待所有任务完成后聚合结果，推荐使用 `CompletableFuture.allOf`：
```java
CompletableFuture<User> userFuture = getUserAsync(uid);
CompletableFuture<List<Item>> itemsFuture = getItemsAsync(uid);
CompletableFuture<Activity> activityFuture = getActivityAsync();

CompletableFuture<Void> allDone = CompletableFuture.allOf(userFuture, itemsFuture, activityFuture);

return allDone.thenApply(v -> {
    User user = userFuture.join();
    List<Item> items = itemsFuture.join();
    Activity activity = activityFuture.join();
    return buildHomePage(user, items, activity);
});
```

- 优点 是任务完全并发执行，整体延迟接近最慢接口耗时；
- 缺点 是使用 `join()` 获取结果时需要显式处理异常。

二、使用 `thenCombine` 聚合少量互不依赖任务

对于两个接口并发且需组合结果的情况，可使用 `thenCombine`：
```java
getUserAsync(uid).thenCombine(getCouponAsync(uid),
    (user, coupon) -> buildCouponBanner(user, coupon));
```

适合两两组合场景，链式逻辑更清晰，但不适合任务过多时使用（代码冗余）。

三、结合异常处理的聚合逻辑

在实际项目中，一定存在部分接口失败的可能。可通过封装 `handle()` 保证每个任务都不抛异常，并带上 fallback：
```java
CompletableFuture<User> safeUserFuture = getUserAsync(uid)
    .exceptionally(ex -> getDefaultUser());

CompletableFuture<List<Item>> safeItemsFuture = getItemsAsync(uid)
    .exceptionally(ex -> Collections.emptyList());
```

再通过 `allOf` 聚合。建议配合埋点记录失败服务名称、耗时、降级标记等指标，提升系统可观测性。


### 构建通用异步IO客户端组件的工程实战路径

随着异步编排场景不断增长，建议将异步调用能力标准化封装为通用组件，以便在企业工程中快速复用、易于维护、统一治理。

一、通用组件的功能设计目标

- 支持 CompletableFuture 异步封装标准；
- 支持线程池/上下文透传；
- 内置超时控制、熔断限流机制；
- 提供统一埋点与日志接入；
- 可扩展支持 HTTP、RPC、DB 等多种协议。

二、组件核心封装接口

```java
public interface AsyncInvoker {
    <T> CompletableFuture<T> invoke(Supplier<T> supplier, AsyncMeta meta);
}
```
其中 `AsyncMeta` 包含：
- 超时时间 timeout
- 调用链 TraceId
- 熔断策略名
- 调用类型标识（HTTP/Dubbo/DB）

实现类统一封装如：
```java
public class DefaultAsyncInvoker implements AsyncInvoker {
    public <T> CompletableFuture<T> invoke(Supplier<T> supplier, AsyncMeta meta) {
        Supplier<T> wrapped = wrapContext(supplier, meta);
        return CompletableFuture.supplyAsync(wrapped, threadPool)
            .orTimeout(meta.timeout, TimeUnit.MILLISECONDS)
            .exceptionally(ex -> handleFallback(meta, ex));
    }
}
```

三、业务系统中接入方式示例

```java
CompletableFuture<User> userFuture = asyncInvoker.invoke(
    () -> userService.getUser(uid),
    AsyncMeta.http("user-service").timeout(300)
);

CompletableFuture<List<Item>> itemsFuture = asyncInvoker.invoke(
    () -> itemService.getItems(uid),
    AsyncMeta.rpc("item-service").timeout(200)
);
```

封装后可实现：
- 所有异步调用带有统一限流、日志、链路 ID；
- 支持监控平台自动采集异步耗时、QPS、异常类型；
- 易于热更新熔断/线程池配置参数；
- 与 Spring Boot 配置中心、Actuator 兼容接入。

通用组件化的目标，是将 CompletableFuture 从 “语法工具” 提升为 “平台能力”，并将治理、监控、扩展性融入到每一次异步调用中，实现真正企业级异步基础设施建设。


[目录](#目录)


## CompletableFuture中的阻塞陷阱：join/get使用限制与替代方案

[目录](#目录)

## 异步任务堆积与线程耗尽问题定位与治理策略：基于CompletableFuture的系统化诊断与优化实践

[目录](#目录)

## 任务链中异步错误传播失败的根因排查与解决：CompletableFuture异常链路的稳定性实战解析


[目录](#目录)


## 业务中大量小任务异步组合导致性能抖动问题优化实战

[目录](#目录)

## CompletableFuture与数据库连接池资源耗尽问题协同分析：异步编排下的资源瓶颈复现与优化实践


[目录](#目录)

## 异步调用返回顺序不可控导致业务逻辑异常的修复思路与工程实践



[目录](#目录)

## 多CompletableFuture聚合中某个任务失败导致整体hang住的处理策略


[目录](#目录)



----------------------------------------------------------------------------------------------------------------------------------------------------------

## 美团CompletableFuture示例
> [🔗链接](https://mp.weixin.qq.com/s/GQGidprakfticYnbVYVYGQ)

使用CompletableFuture也是构建依赖树的过程。一个CompletableFuture的完成会触发另外一系列依赖它的CompletableFuture的执行：

![CompletableFuture示例执行流程](../../resources/static/images/CompletableFuture示例执行流程.png)

如上图所示，这里描绘的是一个业务接口的流程，其中包括CF1\CF2\CF3\CF4\CF5共5个步骤，并描绘了这些步骤之间的依赖关系，每个步骤可以是一次RPC调用、一次数据库操作或者是一次本地方法调用等，在使用CompletableFuture进行异步化编程时，图中的每个步骤都会产生一个CompletableFuture对象，最终结果也会用一个CompletableFuture来进行表示。

根据CompletableFuture依赖数量，可以分为以下几类：**`零依赖`、`一元依赖`、`二元依赖`、`多元依赖`**。

- `零依赖`：**CF1，CF2**不依赖其他CompletableFuture来创建新的CompletableFuture。
  ```java
  // 接口接收到请求后，首先发起两个异步调用CF1、CF2，三种方式：  
  ExecutorService executor = Executors.newFixedThreadPool(5);
  
  //1、使用runAsync或supplyAsync发起异步调用
  CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
      return "result1";
  }, executor);
  
  //2、CompletableFuture.completedFuture()直接创建一个已完成状态的CompletableFuture
  CompletableFuture<String> cf2 = CompletableFuture.completedFuture("result2");
  
  //3、先初始化一个未完成的CompletableFuture，然后通过complete()、completeExceptionally()，完成该CompletableFuture
  CompletableFuture<String> cf = new CompletableFuture<>();
  cf.complete("success");
  ```
  第三种方式的一个典型使用场景，就是将回调方法转为CompletableFuture，然后再依赖CompletableFuture的能力进行调用编排，示例如下：
  ```java
  @FunctionalInterface
  public interface ThriftAsyncCall {
      void invoke() throws TException;
  }
  
  /**
   * 该方法为美团内部rpc注册监听的封装，可以作为其他实现的参照
   * OctoThriftCallback 为thrift回调方法
   * ThriftAsyncCall 为自定义函数，用来表示一次thrift调用（定义如上）
   */
  public static <T> CompletableFuture<T> toCompletableFuture(final OctoThriftCallback<?,T> callback, ThriftAsyncCall thriftCall) {
      //新建一个未完成的CompletableFuture
      CompletableFuture<T> resultFuture = new CompletableFuture<>();
      //监听回调的完成，并且与CompletableFuture同步状态
      callback.addObserver(new OctoObserver<T>() {
          @Override
          public void onSuccess(T t) {
              resultFuture.complete(t);
          }
          @Override
          public void onFailure(Throwable throwable) {
              resultFuture.completeExceptionally(throwable);
          }
      });
      if (thriftCall != null) {
          try {
              thriftCall.invoke();
          } catch (TException e) {
              resultFuture.completeExceptionally(e);
          }
      }
      return resultFuture;
  }
  ```
- `一元依赖`：**CF3，CF5**分别依赖于CF1和CF2，这种对于单个CompletableFuture的依赖可以通过`thenApply`、`thenAccept`、`thenCompose`等方法来实现
  ```java
  CompletableFuture<String> cf3 = cf1.thenApply(result1 -> {
      //result1为CF1的结果
      //......
      return "result3";
  });
  CompletableFuture<String> cf5 = cf2.thenApply(result2 -> {
      //result2为CF2的结果
      //......
      return "result5";
  });
  ```
- `二元依赖`：**CF4**同时依赖于两个CF1和CF2，这种二元依赖可以通过`thenCombine`等回调来实现
  ```java
  CompletableFuture<String> cf4 = cf1.thenCombine(cf2, (result1, result2) -> {
      //result1和result2分别为cf1和cf2的结果
      return "result4";
  });
  ```
- `多元依赖`：整个流程的结束**CF6**依赖于三个步骤CF3、CF4、CF5，这种多元依赖可以通过`allOf`或`anyOf`方法来实现，区别是当需要多个依赖全部完成时使用`allOf`，当多个依赖中的任意一个完成即可时使用`anyOf`
  ```java
  CompletableFuture<Void> cf6 = CompletableFuture.allOf(cf3, cf4, cf5);
  CompletableFuture<String> result = cf6.thenApply(v -> {
      //这里的join并不会阻塞，因为传给thenApply的函数是在CF3、CF4、CF5全部完成时，才会执行。
      result3 = cf3.join();
      result4 = cf4.join();
      result5 = cf5.join();
      //根据result3、result4、result5组装最终result;
      return "result";
  });
  ```

**实践总结：**

1. **代码执行在哪个线程上？**  
   要合理治理线程资源，最基本的前提条件就是要在写代码时，清楚地知道每一行代码都将执行在哪个线程上。下面我们看一下CompletableFuture的执行线程情况。  
   CompletableFuture实现了CompletionStage接口，通过丰富的回调方法，支持各种组合操作，每种组合场景都有同步和异步两种方法。
    - 同步方法（即不带Async后缀的方法）有两种情况。
        - 如果注册时被依赖的操作已经执行完成，则直接由当前线程执行。
        - 如果注册时被依赖的操作还未执行完，则由回调线程执行。
    - 异步方法（即带Async后缀的方法）：可以选择是否传递线程池参数Executor运行在指定线程池中；当不传递Executor时，会使用ForkJoinPool中的共用线程池CommonPool（CommonPool的大小是CPU核数-1，如果是IO密集的应用，线程数可能成为瓶颈）。
2. **异步回调要强制传线程池，且根据实际情况做线程池隔离（避免核心与非核心业务都竞争同一个池中的线程，减少不同业务之间的相互干扰）。**
3. **线程池循环引用会导致死锁**
   ```java
   public Object doGet() {
       ExecutorService threadPool1 = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100));
       CompletableFuture cf1 = CompletableFuture.supplyAsync(() -> {
           //do sth
           return CompletableFuture.supplyAsync(() -> {
               System.out.println("child");
               return "child";
           }, threadPool1).join();//子任务
       }, threadPool1);
       return cf1.join();
   }
   ```
   如上代码块所示，doGet方法第三行通过supplyAsync向threadPool1请求线程，并且内部子任务又向threadPool1请求线程。threadPool1大小为10，当同一时刻有10个请求到达，则threadPool1被打满，子任务请求线程时进入阻塞队列排队，但是父任务的完成又依赖于子任务，这时由于子任务得不到线程，父任务无法完成。主线程执行cf1.join()进入阻塞状态，并且永远无法恢复。

   为了修复该问题，需要将父任务与子任务做线程池隔离，两个任务请求不同的线程池，避免循环依赖导致的阻塞。
4. **异步RPC调用注意不要阻塞IO线程池**  
   服务异步化后很多步骤都会依赖于异步RPC调用的结果，这时需要特别注意一点，如果是使用基于NIO（比如Netty）的异步RPC，则返回结果是由IO线程负责设置的，即回调方法由IO线程触发，CompletableFuture同步回调（如thenApply、thenAccept等无Async后缀的方法）如果依赖的异步RPC调用的返回结果，那么这些同步回调将运行在IO线程上，而整个服务只有一个IO线程池，这时需要保证同步回调中不能有阻塞等耗时过长的逻辑，否则在这些逻辑执行完成前，IO线程将一直被占用，影响整个服务的响应。
5. **异常处理**  
   CompletableFuture提供了异常捕获回调exceptionally，相当于同步调用中的try\catch。  
   有一点需要注意，CompletableFuture在回调方法中对异常进行了包装。大部分异常会封装成CompletionException后抛出，真正的异常存储在cause属性中，因此如果调用链中经过了回调方法处理那么就需要用Throwable.getCause()方法提取真正的异常。但是，有些情况下会直接返回真正的异常（[Stack Overflow的讨论](https://stackoverflow.com/questions/49230980/does-completionstage-always-wrap-exceptions-in-completionexception) ），最好使用工具类提取异常
   ```java
   .exceptionally(err -> {//通过exceptionally 捕获异常，这里的err已经被thenApply包装过，因此需要通过Throwable.getCause()提取异常
        log.error("WmOrderRemarkService.getCancelTypeAsync Exception orderId={}", orderId, ExceptionUtils.extractRealException(err));
        return 0;
   });
   ```
   自定义的工具类ExceptionUtils，用于CompletableFuture的异常提取，在使用CompletableFuture做异步编程时，可以直接使用该工具类处理异常。
   ```java
   public class ExceptionUtils {
       public static Throwable extractRealException(Throwable throwable) {
           //这里判断异常类型是否为CompletionException、ExecutionException，如果是则进行提取，否则直接返回。
           if (throwable instanceof CompletionException || throwable instanceof ExecutionException) {
               if (throwable.getCause() != null) {
                    return throwable.getCause();
               }
           }
           return throwable;
       }
   }
   ```


**通用的工具方法：**

1. **自定义函数**
```java
@FunctionalInterface
public interface ThriftAsyncCall {
    void invoke() throws TException ;
}
```
2. **CompletableFuture处理工具类**
```java
/**
 * CompletableFuture封装工具类
 */
@Slf4j
public class FutureUtils {
    /**
     * 该方法为美团内部rpc注册监听的封装，可以作为其他实现的参照
     * OctoThriftCallback 为thrift回调方法
     * ThriftAsyncCall 为自定义函数，用来表示一次thrift调用（定义如上）
     */
    public static <T> CompletableFuture<T> toCompletableFuture(final OctoThriftCallback<?, T> callback, 
                                                               ThriftAsyncCall thriftCall) {
        CompletableFuture<T> thriftResultFuture = new CompletableFuture<>();
        callback.addObserver(new OctoObserver<T>() {
            @Override
            public void onSuccess(T t) {
                thriftResultFuture.complete(t);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thriftResultFuture.completeExceptionally(throwable);
            }
        });
        if (thriftCall != null) {
            try {
                thriftCall.invoke();
            } catch (TException e) {
                thriftResultFuture.completeExceptionally(e);
            }
        }
        return thriftResultFuture;
    }

    /**
     * 设置CF状态为失败
     */
    public static <T> CompletableFuture<T> failed(Throwable ex) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(ex);
        return completableFuture;
    }

    /**
     * 设置CF状态为成功
     */
    public static <T> CompletableFuture<T> success(T result) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        completableFuture.complete(result);
        return completableFuture;
    }

    /**
     * 将List<CompletableFuture<T>> 转为 CompletableFuture<List<T>>
     */
    public static <T> CompletableFuture<List<T>> sequence(Collection<CompletableFuture<T>> completableFutures) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
    }

    /**
     * 将List<CompletableFuture<List<T>>> 转为 CompletableFuture<List<T>>
     * 多用于分页查询的场景
     */
    public static <T> CompletableFuture<List<T>> sequenceList(Collection<CompletableFuture<List<T>>> completableFutures) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .flatMap(listFuture -> listFuture.join().stream())
                        .collect(Collectors.toList()));
    }

    /**
     * 将List<CompletableFuture<Map<K, V>>> 转为 CompletableFuture<Map<K, V>>
     * @param mergeFunction 自定义key冲突时的merge策略
     */
    public static <K, V> CompletableFuture<Map<K, V>> sequenceMap(Collection<CompletableFuture<Map<K, V>>> completableFutures, 
                                                                  BinaryOperator<V> mergeFunction) {
        return CompletableFuture
                .allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream().map(CompletableFuture::join)
                        .flatMap(map -> map.entrySet().stream())
                        .collect(Collectors.toMap(Entry::getKey, Entry::getValue, mergeFunction)));
    }

    /**
     * 将List<CompletableFuture<T>> 转为 CompletableFuture<List<T>>，并过滤调null值
     */
    public static <T> CompletableFuture<List<T>> sequenceNonNull(Collection<CompletableFuture<T>> completableFutures) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .filter(e -> e != null)
                        .collect(Collectors.toList()));
    }

    /**
     * 将List<CompletableFuture<List<T>>> 转为 CompletableFuture<List<T>>，并过滤掉null值
     * 多用于分页查询的场景
     */
    public static <T> CompletableFuture<List<T>> sequenceListNonNull(Collection<CompletableFuture<List<T>>> completableFutures) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .flatMap(listFuture -> listFuture.join().stream().filter(e -> e != null))
                        .collect(Collectors.toList()));
    }

    /**
     * 将List<CompletableFuture<Map<K, V>>> 转为 CompletableFuture<Map<K, V>>
     * @param filterFunction 自定义过滤策略
     */
    public static <T> CompletableFuture<List<T>> sequence(Collection<CompletableFuture<T>> completableFutures,
                                                          Predicate<? super T> filterFunction) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .filter(filterFunction)
                        .collect(Collectors.toList()));
    }

    /**
     * 将List<CompletableFuture<List<T>>> 转为 CompletableFuture<List<T>>
     * @param filterFunction 自定义过滤策略
     */
    public static <T> CompletableFuture<List<T>> sequenceList(Collection<CompletableFuture<List<T>>> completableFutures,
                                                              Predicate<? super T> filterFunction) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .flatMap(listFuture -> listFuture.join().stream().filter(filterFunction))
                        .collect(Collectors.toList()));
    }

    /**
     * 将CompletableFuture<Map<K,V>>的list转为 CompletableFuture<Map<K,V>>。 多个map合并为一个map。 如果key冲突，采用新的value覆盖。
     */
    public static <K, V> CompletableFuture<Map<K, V>> sequenceMap(Collection<CompletableFuture<Map<K, V>>> completableFutures) {
        return CompletableFuture
                .allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream().map(CompletableFuture::join)
                        .flatMap(map -> map.entrySet().stream())
                        .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b)));
    }
}
```
3. **异常提取工具类**
```java
public class ExceptionUtils {
    /**
     * 提取真正的异常
     */
    public static Throwable extractRealException(Throwable throwable) {
        if (throwable instanceof CompletionException || throwable instanceof ExecutionException) {
            if (throwable.getCause() != null) {
                return throwable.getCause();
            }
        }
        return throwable;
    }
}
```   
4. **打印日志**
```java
@Slf4j
public abstract class AbstractLogAction<R> {
    protected final String methodName;
    protected final Object[] args;

    public AbstractLogAction(String methodName, Object... args) {
        this.methodName = methodName;
        this.args = args;
    }

    protected void logResult(R result, Throwable throwable) {
        if (throwable != null) {
            boolean isBusinessError = throwable instanceof TBase || (throwable.getCause() != null && throwable.getCause() instanceof TBase);
            if (isBusinessError) {
                logBusinessError(throwable);
            } else if (throwable instanceof DegradeException || throwable instanceof DegradeRuntimeException) {//这里为内部rpc框架抛出的异常，使用时可以酌情修改
                if (RhinoSwitch.getBoolean("isPrintDegradeLog", false)) {
                    log.error("{} degrade exception, param:{} , error:{}", methodName, args, throwable);
                }
            } else {
                log.error("{} unknown error, param:{} , error:{}", methodName, args, ExceptionUtils.extractRealException(throwable));
            }
        } else {
            if (isLogResult()) {
                log.info("{} param:{} , result:{}", methodName, args, result);
            } else {
                log.info("{} param:{}", methodName, args);
            }
        }
    }

    private void logBusinessError(Throwable throwable) {
        log.error("{} business error, param:{} , error:{}", methodName, args, throwable.toString(), ExceptionUtils.extractRealException(throwable));
    }

    private boolean isLogResult() {
        //这里是动态配置开关，用于动态控制日志打印，开源动态配置中心可以使用nacos、apollo等，如果项目没有使用配置中心则可以删除
        return RhinoSwitch.getBoolean(methodName + "_isLogResult", false);
    }
}
```   
4.1. **日志处理实现类**
```java
/**
 * 发生异常时，根据是否为业务异常打印日志。
 * 跟CompletableFuture.whenComplete配合使用，不改变completableFuture的结果（正常OR异常）
 */
@Slf4j
public class LogErrorAction<R> extends AbstractLogAction<R> implements BiConsumer<R, Throwable> {
    public LogErrorAction(String methodName, Object... args) {
        super(methodName, args);
    }

    @Override
    public void accept(R result, Throwable throwable) {
        logResult(result, throwable);
    }
}
```
4.2. **打印日志方式**
```java
completableFuture
    .whenComplete(new LogErrorAction<>("orderService.getOrder", params));
```   
5. **异常情况返回默认值**
```java
/**
 * 当发生异常时返回自定义的值
 */
public class DefaultValueHandle<R> extends AbstractLogAction<R> implements BiFunction<R, Throwable, R> {
    private final R defaultValue;
    /**
     * 当返回值为空的时候是否替换为默认值
     */
    private final boolean isNullToDefault;

    /**
     * @param methodName      方法名称
     * @param defaultValue 当异常发生时自定义返回的默认值
     * @param args            方法入参
     */
    public DefaultValueHandle(String methodName, R defaultValue, Object... args) {
        super(methodName, args);
        this.defaultValue = defaultValue;
        this.isNullToDefault = false;
    }

    /**
     * @param isNullToDefault
     * @param defaultValue 当异常发生时自定义返回的默认值
     * @param methodName      方法名称
     * @param args            方法入参
     */
    public DefaultValueHandle(boolean isNullToDefault, R defaultValue, String methodName, Object... args) {
        super(methodName, args);
        this.defaultValue = defaultValue;
        this.isNullToDefault = isNullToDefault;
    }

    @Override
    public R apply(R result, Throwable throwable) {
        logResult(result, throwable);
        if (throwable != null) {
            return defaultValue;
        }
        if (result == null && isNullToDefault) {
            return defaultValue;
        }
        return result;
    }

    public static <R> DefaultValueHandle.DefaultValueHandleBuilder<R> builder() {
        return new DefaultValueHandle.DefaultValueHandleBuilder<>();
    }

    public static class DefaultValueHandleBuilder<R> {
        private boolean isNullToDefault;
        private R defaultValue;
        private String methodName;
        private Object[] args;

        DefaultValueHandleBuilder() {
        }

        public DefaultValueHandle.DefaultValueHandleBuilder<R> isNullToDefault(final boolean isNullToDefault) {
            this.isNullToDefault = isNullToDefault;
            return this;
        }

        public DefaultValueHandle.DefaultValueHandleBuilder<R> defaultValue(final R defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public DefaultValueHandle.DefaultValueHandleBuilder<R> methodName(final String methodName) {
            this.methodName = methodName;
            return this;
        }

        public DefaultValueHandle.DefaultValueHandleBuilder<R> args(final Object... args) {
            this.args = args;
            return this;
        }

        public DefaultValueHandle<R> build() {
            return new DefaultValueHandle<R>(this.isNullToDefault, this.defaultValue, this.methodName, this.args);
        }

        public String toString() {
            return "DefaultValueHandle.DefaultValueHandleBuilder(isNullToDefault=" + this.isNullToDefault + ", defaultValue=" + this.defaultValue + ", methodName=" + this.methodName + ", args=" + Arrays.deepToString(this.args) + ")";
        }
    }
}
```   
5.1. **默认返回值应用示例**
```java
completableFuture
        .handle(new DefaultValueHandle<>("orderService.getOrder", Collections.emptyMap(), params));
```


其它组件：

- [Vert.x](Vert.x.md)：轻量级事件驱动框架，采用Multi-reactor线程模型，通过事件循环（Event Loop）处理并发请求，实现了异步非阻塞IO。

[目录](#目录)



