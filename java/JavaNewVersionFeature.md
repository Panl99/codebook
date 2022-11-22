Java新版本新增特性

日常开发能直观感受到的变化

> [https://mp.weixin.qq.com/s/ytzngjnB6SVfURjarhOOqw](https://mp.weixin.qq.com/s/ytzngjnB6SVfURjarhOOqw)

[Java.java](Java.java)

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
