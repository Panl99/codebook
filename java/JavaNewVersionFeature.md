Java新版本新增特性

日常开发能直观感受到的变化

> [https://mp.weixin.qq.com/s/ytzngjnB6SVfURjarhOOqw](https://mp.weixin.qq.com/s/ytzngjnB6SVfURjarhOOqw)

[Java.java](Java.java)


# Java21(LTS)

- 430: String Templates (Preview) `字符串模板（预览）`
- 431: Sequenced Collections `顺序集合`
- 439: Generational ZGC `ZGC世代`
- 440: Record Patterns `记录模式`
- 441: Pattern Matching for switch `开关模式匹配`
- 442: Foreign Function & Memory API (Third Preview) `外部函数和内存API（第三次预览）`
- 443: Unnamed Patterns and Variables (Preview) `未命名模式和变量（预览）`
- 444: Virtual Threads `虚拟线程`
- 445: Unnamed Classes and Instance Main Methods (Preview) `未命名类和实例主方法（预览版）`
- 446: Scoped Values (Preview) `范围值（预览）`
- 448: Vector API (Sixth Incubator) `矢量API（第六个培养箱）`
- 449: Deprecate the Windows 32-bit x86 Port for Removal `弃用Windows 32位x86端口以进行删除`
- 451: Prepare to Disallow the Dynamic Loading of Agents `准备禁止动态加载代理`
- 452: Key Encapsulation Mechanism API `密钥封装机制API`
- 453: Structured Concurrency (Preview) `结构化并发（预览版）`

> [https://openjdk.org/projects/jdk/21/](https://openjdk.org/projects/jdk/21/)


注意：
- 虚拟线程可以提高系统的吞吐量，不能提高运行速度，也不适用于 CPU 计算密集型任务

# Java17(LTS)

- Spring 6 和 Spring Boot 3 依赖 JDK17


# Java16

- 没什么明显可见的变化。

- 增强ZGC，ZGC获得了 46个增强功能 和25个错误修复，控制stw时间不超过10毫秒

# Java15

- 增加ZGC和 Shenandoah垃圾收集器。
- 新增封闭（Sealed ）类的特性，可以指定某些类才可以继承。

- ZGC (JEP 377) 和Shenandoah (JEP 379) 不再是实验性功能。默认的 GC 仍然是G1。

# Java14

- 新增record类型，便捷的创建一个POJO类。
- 空指针异常时显示哪里为空了。
- 增加一套安全的堆外内存访问接口，可以轻松访问堆外内存，不用再搞Unsafe操作了。

- 删除CMS垃圾回收器;
- 弃用 ParallelScavenge + SerialOld GC 的垃圾回收算法组合;
- 将 ZGC 垃圾回收器移植到 macOS 和 windows 平台

# Java13

- Switch语法增强，可以在`->`之后编写代码块。
- 支持文本块。

# Java12

- Switch语法合并相同逻辑的case，并支持返回值。
- instanceof直接类型转化，不需要再进行一步强转。

# Java11(LTS)

- Lambda支持var 自动推断变量类型。
  
- 推出ZGC新一代垃圾回收器（实验性）,目标是GC暂停时间不会超过10ms，既能处理几百兆的小堆，也能处理几个T的大堆。

# Java10

- 支持var 自动推断变量类型。
  
- 并行全垃圾回收器 G1，通过并行Full GC, 改善G1的延迟。目前对G1的full GC的实现采用了单线程-清除-压缩算法。JDK10开始使用并行化-清除-压缩算法。

# Java9

- 接口中支持定义私有方法，之后可以在默认方法中调用。
- 匿名内部类支持`<>`运算符。
- `try-with-resources`支持引用try外部变量来关闭资源。
  
- 设置G1为JVM默认垃圾收集器
