Java新版本新增特性

日常开发能直观感受到的变化

> [https://mp.weixin.qq.com/s/ytzngjnB6SVfURjarhOOqw](https://mp.weixin.qq.com/s/ytzngjnB6SVfURjarhOOqw)

[Java.java](Java.java)

# Java16

- 没什么明显可见的变化。

# Java15

- 增加ZGC和 Shenandoah垃圾收集器。
- 新增封闭（Sealed ）类的特性，可以指定某些类才可以继承。

# Java14

- 新增record类型，便捷的创建一个POJO类。
- 空指针异常时显示哪里为空了。
- 增加一套安全的堆外内存访问接口，可以轻松访问堆外内存，不用再搞Unsafe操作了。

# Java13

- Switch语法增强，可以在`->`之后编写代码块。
- 支持文本块。

# Java12

- Switch语法合并相同逻辑的case，并支持返回值。
- instanceof直接类型转化，不需要再进行一步强转。

# Java11(LTS)

- Lambda支持var 自动推断变量类型。

# Java10

- 支持var 自动推断变量类型。

# Java9

- 接口中支持定义私有方法，之后可以在默认方法中调用。
- 匿名内部类支持`<>`运算符。
- `try-with-resources`支持引用try外部变量来关闭资源。
