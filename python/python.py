import os

# 数据类型（6个）：Number（数字）、String（字符串）、Tuple（元组）；
# List（列表）、Dictionary（字典）、Set（集合）

# 数字支持 int、float、bool、complex（复数）。
def number_operation():
    # 2的5次方，结果：32，0.5，0
    print(2 ** 5, 2 / 4, 2 // 4)

# 字符串用单引号'或双引号"括起来，同时使用反斜杠\转义特殊字符。
def string_operation():
    # String[头下标:尾下标]
    str = "python test"
    print(str[0:-1])    # python tes,输出第一个到倒数第二个的所有字符
    print(str[2:])      # thon test,输出从第三个开始的后的所有字符
    print(str * 2)      # python testpython test,输出字符串两次
    print(str + "Ha")   # python testHa,连接字符串

# 列表是写在方括号[]之间、用逗号分隔开的元素列表。
def list_operation():
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

# 元组（tuple）与列表类似(语法可参考列表)，不同之处在于元组的元素不能修改(但它可以包含可变的对象，如list列表)。
# 元组写在小括号()里，元素之间用逗号隔开
def tuple_operation():
    # 0个元素元组，空元祖
    tup1 = ()
    # 一个元素，需要在元素后添加逗号
    tup2 = (20,) 

# 集合（set）基本功能是进行成员关系测试和删除重复元素。可以使用大括号{ }或者set()函数创建集合。
# 注意：创建空集合必须用 set() ，{ }是用来创建空字典。
def set_operation():
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

# 字典（dictionary）用{ }标识，它是一个无序的键(key):值(value)的集合。
# key必须使用不可变类型，且同一字典中key唯一。
def dictionary_operation():
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

# 1、判断是否是一个目录
def isDir():
    path = ".\\"
    if os.path.isdir(path):
        print("%s is a dir" % path)
    else:
        print("%s is not a dir" % path)


def main():
    # print("***main***")
    # string_operation()
    # print("**********分割符***********")
    # list_operation()
    # set_operation()
    dictionary_operation()


if __name__ == '__main__':
    main()
