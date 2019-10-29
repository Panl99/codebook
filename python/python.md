# Python
## 一、基本
#### 1、数据类型（6个）：
**不可变：** Number（数字）、String（字符串）、Tuple（元组）；  
**可变：** List（列表）、Dictionary（字典）、Set（集合）

- **数字：int、float、bool、complex（复数）。**  
    `print(2 ** 5, 2 / 4, 2 // 4)    # 2的5次方，结果：32，0.5，0`

- **字符串：用单引号'或双引号"括起来，同时使用反斜杠\转义特殊字符。**
```
    # String[头下标:尾下标]
    str = "python test"
    print(str[0:-1])    # python tes,输出第一个到倒数第二个的所有字符
    print(str[2:])      # thon test,输出从第三个开始的后的所有字符
    print(str * 2)      # python testpython test,输出字符串两次
    print(str + "Ha")   # python testHa,连接字符串
```

- **列表：写在方括号[]之间、用逗号分隔开的元素列表。**
```
    # List[头下标:尾下标]
    list = ['abcd', 786, 2.23, 'python', 70.2]
    list2 = [123, 'python']
    a = [1, 2, 3, 4, 5, 6]

    # 截取第二个到第三个元素
    print(list[1:3])    # [786, 2.23]
    # 截取第三个元素到最后一个元素
    print(list[2:])     # [2.23, 'python', 70.2]

    # 输出两次列表
    print(list2 * 2)    # [123, 'python', 123, 'python']
    # 连接列表
    print(list + list2)     # ['abcd', 786, 2.23, 'python', 70.2, 123, 'python']

    # 改变列表第一个到第三个元素
    a[1:4] = [13, 14, 15]
    print(a)        # [1, 13, 14, 15, 5, 6]
    # 置空第一个到第三个元素
    a[1:4] = []
    print(a)        # [1, 5, 6]

    # 截取，加步长
    print(list[1:4:2])  # [786, 'python']
    # 反转，1：-1表示最后一个元素，2：空，表示移动到列表尾部，3：-1表示步长为逆向
    print(list[-1::-1])  # [70.2, 'python', 2.23, 786, 'abcd']
```

- **元组（tuple）：与列表类似(语法可参考列表)，不同之处在于元组的元素不能修改(但它可以包含可变的对象，如list列表)。**
**元组写在小括号()里，元素之间用逗号隔开**
```
    # 0个元素元组，空元祖
    tup1 = ()

    # 一个元素，需要在元素后添加逗号
    tup2 = (20,) 
```

- **集合（set）：基本功能是进行成员关系测试和删除重复元素。可以使用大括号{ }或者set()函数创建集合。**
**注意：创建空集合必须用 set() ，{ }是用来创建空字典。**
```
    ch = {'a', 'b', 'c', 'd', 'a', 'e'}
    # 输出集合，重复的元素被自动去掉
    print(ch)   # {'b', 'a', 'c', 'e', 'd'}

    # 成员测试
    if 'c' in ch :
        print('c 在集合中')
    else :
        print('c 不在集合中')

    # set可以进行集合运算
    a = set('abracadabra')
    b = set('alacazam')
    print(a)
    print(a - b)     # a 和 b 的差集(a有b没有)，{'d', 'b', 'r'}
    print(a | b)     # a 和 b 的并集，{'b', 'm', 'd', 'a', 'r', 'z', 'l', 'c'}
    print(a & b)     # a 和 b 的交集，{'a', 'c'}
    print(a ^ b)     # a 和 b 中不同时存在的元素(a有b没有，或b有a没有)，{'d', 'b', 'z', 'r', 'l', 'm'}
```

- **字典（dictionary）：用{ }标识，它是一个无序的键(key):值(value)的集合。**
**key必须使用不可变类型，且同一字典中key唯一。**
```
    dict = {}
    dict['name'] = "张三"
    dict[2]     = "lisi"
     
    tinydict = {'name': 'zhangsan','age':20}
     
    print (dict['name'])       # 张三
    print (dict[2])           # lisi
    print (tinydict)          # {'name': 'zhangsan', 'age': 20}
    print (tinydict.keys())   # dict_keys(['name', 'age'])
    print (tinydict.values()) # dict_values(['zhangsan', 20])

    print({x: x**2 for x in (2, 4, 6)}) # {2: 4, 4: 16, 6: 36}
```
-----------------------------------------------
##### 数据类型转换
```
    # int(x [,base])    # 将x转换为一个整数,base表示进制
    # float(x)  # 将x转换到一个浮点数
    # str(x)   # 将对象 x 转换为字符串
    # repr(x)   # 将对象 x 转换为表达式字符串，返回string字符串
    # eval(str)用来执行一个字符串表达式，并返回表达式的值
    x = 2
    print(eval( '3 * x' ))  # 6
    # tuple(s)  # 将列表s转换为一个元组
    # list(s)   # 将元组或字符串转换为列表
    # set(s)    # 转换为可变集合
    # dict(d)   # 创建一个字典。d 必须是一个 (key, value)元组序列。
    # frozenset(s)  # 转换为不可变集合,不能再添加或删除任何元素。
    # chr(x)    # 将一个整数转换为一个对应的 ASCII 字符，x为10进制或16进制的数字
    # ord(x)    # 将一个字符转换为十进制整数
    # hex(x)    # 将10进制整数转换成16进制，以字符串形式表示
    # oct(x)    # 将一个整数转换成8进制字符串
```

##### 运算符
```
Python运算符优先级：
1、指数（**）
2、按位翻转（~，+，-）
3、乘，除，取模，取整除（*，/，%，//）
4、加，减（+，-）
5、右移，左移（>>，<<）
6、位“AND”（&）
7、位运算符（^，|）
8、比较运算符（<=，<，>，>=）
9、等于运算符（<>，==，!=）
10、赋值运算符（=，%=，/=，//=，-=，+=，*=，**=）
11、身份运算符（is，is not）# is 用于判断两个变量引用对象是否为同一个， == 用于判断引用变量的值是否相等。
12、成员运算符（in，not in）
13、逻辑运算符（not，and，or）# x and y：如果x为False，返回 False，否则返回 y。x or y：如果x是True，返回x，否则返回y。
```

#### 2、分支、循环

#### 3、函数、模块

#### 4、文件、异常

## 二、数据分析
#### 1、NumPy：N维数组表达基础库

#### 2、Pandas：数据分析高层次应用库

#### 3、SciPy：数学、科学、工程计算

#### 4、数据可视化
**Matplotlib：二维数据可视化**

#### 5、文本处理

## 三、网络爬虫
#### 1、Requests：网络爬虫功能库，页面爬取

#### 2、Scrapy：网络爬虫框架

#### 3、pyspider：web页面爬取系统

## 四、Web解析
#### 1、Beautiful Soup：HTML/XML解析库

#### 2、Re：正则（标准库，不需要安装）

#### 3、Python-Goose：提取文章类型的web页面

## 五、Web开发
#### 1、Django：web应用框架，MTV模式

#### 2、Flask：微框架，适用于仅几个页面

#### 3、pyramid：中等规模web应用框架

## 六、机器学习
#### 1、TensorFlow：AlphaGo背后框架

#### 2、Scikit-learn：

#### 3、MxNet：基于神经网络的深度学习

