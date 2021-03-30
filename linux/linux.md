> [Linux命令行与shell脚本编程大全（第3版）](../resources/static/doc/Linux命令行与shell脚本编程大全.第3版.pdf)

# 目录
- [shell](#shell)
    - [流程控制](#流程控制)
        - [if](#if)
        - [for](#for)
        - [while](#while)
    - [字符串截取](#字符串截取)
    - [数组](#数组)
    - [运算符](#运算符)
        - [算数运算符](#算数运算符)
        - [关系运算符](#关系运算符)
        - [布尔运算符](#布尔运算符)
        - [逻辑运算符](#逻辑运算符)
        - [字符串运算符](#字符串运算符)
        - [表达式计算expr](#表达式计算expr)
        - [文件检测](#文件检测)
        - [test命令](#test命令)
    - [传参](#传参)
    - [输出](#输出)
    - [输入输出重定向](#输入输出重定向)
    - [加载外部脚本](#加载外部脚本)
    - [awk](#awk)
    - [xargs](#xargs)
    - [编辑文件sed](#编辑文件sed)
    - [EOF](#EOF)
- [实战](#实战)
    - [常用操作](#常用操作)
    - [Bash脚本模板](#bash-script-template)
- [linux](#linux)
    - [常用命令](#常用命令)
        - `ls`,`cd`,`pwd`,`mkdir`,`rmdir`,`touch`,`cp`,`rm`,`mv`,`cat`,`tac`,`nl`,`more`,`less`,`head`,`tail`,`grep`,`find`,`date`
        - [Linux常用命令-菜鸟](https://www.runoob.com/w3cnote/linux-common-command-2.html)
    - [用户-用户组](#用户-用户组)
    - [磁盘管理](#磁盘管理)
    - [vim](#vim)
- [问题解决](#问题解决)
    - [syntax error near unexpected token](#syntax-error-near-unexpected-token)

[目录](#目录)

# shell
## 流程控制
### if
```shell script
#!/bin/bash
if [ $(ps -ef | grep -c "ssh") -gt 1 ]; then echo "true"; fi

num1=$[2*3]
num2=$[1+5]
if test $[num1] -eq $[num2]
then
    echo '两个数字相等!'  #
else
    echo '两个数字不相等!'
fi
```
[目录](#目录)
### for
```shell script
#!/bin/bash
for str in 'This is a string'
do
    echo $str
done
```
- 动态读取入参
```shell script
for var in $@
do
    echo $var
done
```
[目录](#目录)
### while
```shell script
#!/bin/bash
echo 'Hello World!'

count=3
echo $count
logpath='home/test/shell/shell-test.log'
url='https://www.test.com/blog/shell-test'
# 输出字符串长度
echo ${#logpath} ${#url}
# 注意条件前后空格
while [ $count -gt 0 ]
do 
	echo $logpath `\n` $url
	
	count=`expr $count - 1`
	echo $count
done

echo 'end...'
```
```shell script
#!/bin/bash
logpath='home/test/shell/shell-test.log'

# 读取logpath文件
while read line
do 
	echo $line
done < $logpath
```

[目录](#目录)

## 字符串截取
```shell script
#!/bin/bash

logpath='home/test/shell/shell-test.log'
url='https://www.test.com/blog/shell-test'
# 输出字符串长度
echo ${#logpath} ${#url}  #30 36

# 截取最后一个/后的值
log_suffix=${logpath##*/}
echo $log_suffix  #shell-test.log
# 截取第一个/后的值
url_suffix=${url#*/}
echo $url_suffix  #/www.test.com/blog/shell-test

# 截取第一个/前的值
log_prefix=${logpath%%/*}
echo $log_prefix  #home
# 截取最后一个/前的值
url_prefix=${url%/*}
echo $url_prefix  #https://www.test.com/blog

# 自定义截取
sublogpath=${logpath:0:5}
suburl=${url:0:8}
echo $sublogpath '***' $suburl  #home/ *** https://
# 从左边第9个字符开始到最后
sublogpath=${logpath:8}
suburl=${url:8}
echo $sublogpath '***' $suburl  #t/shell/shell-test.log *** www.test.com/blog/shell-test
# 从右边第9个字符开始截取5个字符
sublogpath=${logpath:0-9:5}
suburl=${url:0-9:5}
echo $sublogpath '***' $suburl  #-test *** hell-

```
[目录](#目录)
## 数组
- 只支持一维数组
- `array=(value0 value1 value2 value3)`
    - 单独定义：`array[0]=value0`
- 读取数组：`valuen=${array_name[n]}`
    - 获取数组中的所有元素：`${array[@]}`
    - 获取数组长度：`length=${#array[@]}`或`length=${#array[*]}`
    - 获取单个元素长度：`length=${#array[n]}`

[目录](#目录)

## 运算符
### 算数运算符
```shell script
#!/bin/bash

a=10
b=20

val=`expr $a + $b`
echo "a + b : $val" #a + b : 30
val=`expr $a - $b`
echo "a - b : $val" #a - b : -10
val=`expr $a \* $b`
echo "a * b : $val" #a * b : 200
val=`expr $b / $a`
echo "b / a : $val" #b / a : 2
val=`expr $b % $a`
echo "b % a : $val" #b % a : 0
```
[目录](#目录)
### 关系运算符
- `-eq`：相等
- `-ne`：不相等
- `-gt`：大于
- `-lt`：小于
- `-ge`：大于等于
- `-le`：小于等于
```shell script
#!/bin/bash

a=10
b=20
if [ $a -eq $b ]
then
   echo "$a -eq $b : a 等于 b"
else
   echo "$a -eq $b: a 不等于 b"
fi
```
[目录](#目录)
### 布尔运算符
- `!`：非，`[ ! false ]`：true
- `-o`：或运算，`[ $a -lt 20 -o $b -gt 100 ]`：a小于20 或 b大于100
- `-a`：与运算，`[ $a -lt 20 -a $b -gt 100 ]`：a小于20 且 b大于100

[目录](#目录)
### 逻辑运算符
- `&&`：逻辑and
- `||`：逻辑or
```shell script
#!/bin/bash

a=10
b=20

if [[ $a -lt 100 && $b -gt 100 ]]
then
   echo "返回 true"
else
   echo "返回 false"  #a小于100 且 b大于100
fi

if [[ $a -lt 100 || $b -gt 100 ]]
then
   echo "返回 true" #a小于100 且 b大于100
else
   echo "返回 false"
fi
```
[目录](#目录)
### 字符串运算符
- `[ $a = $b ]` 两个字符串是否相等
- `[ $a != $b ]` 两个字符串是否不相等
- `[ -z $a ]` 字符串长度是否为0
- `[ -n "$a" ]` 字符串长度是否不为 0
- `[ $a ]` 字符串是否为空
```shell script
#!/bin/bash

a="abc"
b="efg"

if [ $a = $b ]
then
   echo "$a = $b : a 等于 b"
else
   echo "$a = $b: a 不等于 b"  #
fi
```
[目录](#目录)
### 表达式计算expr
- 表达式计算工具
- 注意：
    - 表达式跟运算符之间要有空格
    - 表达式要被``包含
```shell script
#!/bin/bash

val=`expr 2 + 2`
echo "两数之和为 : $val" #两数之和为 : 4
echo `expr $val - 1`  #3
```
[目录](#目录)
### 文件检测
- `[ -d $file ]` 文件是否是目录
- `[ -r $file ]` 文件是否可读
- `[ -w $file ]` 文件是否可写
- `[ -x $file ]` 文件是否可执行
- `[ -f $file ]` 文件是否是普通文件（既不是目录，也不是设备文件）
- `[ -s $file ]` 文件是否为空（文件大小是否大于0）
- `[ -e $file ]` 文件（包括目录）是否存在

[目录](#目录)
### test命令
- 用于检查某个条件是否成立，可以进行数值、字符、文件三个方面的测试
```shell script
#!/bin/bash
num1=100
num2=100
if test $[num1] -eq $[num2]
then
    echo '两个数相等！' #
else
    echo '两个数不相等！'
fi
```
[目录](#目录)

## 传参
- `$#` 参数个数
- `$*` 显示所有参数,以"$1 $2 … $n"的形式输出所有参数
- `$@` 显示所有参数,以"$1" "$2" … "$n" 的形式输出所有参数
- `$$` 脚本运行的当前进程ID号
- `$!` 后台运行的最后一个进程的ID号
- `$?` 显示最后命令的退出状态。0表示没有错误，其他任何值表明有错误。
```shell script
#!/bin/bash
echo "-- \$* 演示 ---"
for i in "$*"; do
    echo $i
done

echo "-- \$@ 演示 ---"
for i in "$@"; do
    echo $i
done
#输出
$ ./test.sh 1 2 3
-- $* 演示 ---
1 2 3
-- $@ 演示 ---
1
2
3
```
[目录](#目录)

## 输出
- `echo`
    - `echo "It is a test" > myfile`
    - ```echo `date` ```
- `printf`
    - printf 命令模仿 C 程序库（library）里的 printf() 程序
    ```shell script
    #!/bin/bash
    printf "%-10s %-8s %-4s\n" 姓名 性别 体重kg  
    printf "%-10s %-8s %-4.2f\n" 郭靖 男 66.1234 
    printf "%-10s %-8s %-4.2f\n" 杨过 男 48.6543 
    printf "%-10s %-8s %-4.2f\n" 郭芙 女 47.9876 
    
    #输出:
    姓名     性别   体重kg
    郭靖     男      66.12
    杨过     男      48.65
    郭芙     女      47.99
    ```

[目录](#目录)

## 输入输出重定向
- 输出重定向
    - 覆盖文件内容
    ```shell script
    $ echo "www.test.com" > file
    $ cat file
    www.test.com
    ```
    - 追加到文件末尾
    ```shell script
    $ echo "www.test2.com" >> file
    $ cat file
    www.test.com
    www.test2.com
    ```
- 输入重定向
    - 从文件获取输入
    ```shell script
    #统计 users 文件的行数，会输出文件名
    $ wc -l users
           2 users
    #统计 users 文件的行数，不会输出文件名
    $  wc -l < users
           2 
    #执行command1，从文件infile读取内容，然后将输出写入到outfile中
    command1 < infile > outfile
    ```
- 一般情况下，每个 Unix/Linux 命令运行时都会打开三个文件：
    - 标准输入文件(stdin)：stdin的文件描述符为**0**，Unix程序默认从stdin读取数据。
    - 标准输出文件(stdout)：stdout 的文件描述符为**1**，Unix程序默认向stdout输出数据。
    - 标准错误文件(stderr)：stderr的文件描述符为**2**，Unix程序会向stderr流中写入错误信息。
    ```shell script
    # 标准错误文件stderr 重定向到 file
    $ command 2 > file
    
    # 将 stdout 和 stderr 合并后重定向到 file
    $ command > file 2>&1
    
    # 执行命令，但不希望在屏幕上显示输出结果，那么可以将输出重定向到 /dev/null：
    # /dev/null 是一个特殊的文件，写入到它的内容都会被丢弃；将命令的输出重定向到它，会起到"禁止输出"的效果。
    $ command > /dev/null
    
    # 如果希望屏蔽 stdout 和 stderr，可以这样写：
    $ command > /dev/null 2>&1
    ```

[目录](#目录)

## 加载外部脚本
- `. filename` 或 `source filename`
```shell script
#!/bin/bash
# test1.sh

url="http://www.test1.com"
```
```shell script
#!/bin/bash
# test2.sh

#使用 . 号来引用test1.sh 文件
. ./test1.sh

# 或者使用以下包含文件代码
# source ./test1.sh

echo "path：$url"

#输出：
$ ./test2.sh 
path：http://www.test1.com
```

[目录](#目录)

## awk
```shell script
# test.log
2 this is a test
3 Are you like awk
This's a test
10 There are orange,apple,mongo
```
- 用法1：`awk '{[pattern] action}' {filenames}`   # 行匹配语句 awk '' 只能用单引号
```shell script
# 1、每行按空格或TAB分割，输出文本中的1、4项
$ awk '{print $1,$4}' test.log
#-------------------输出--------------------------
2 a
3 like
This's
10 orange,apple,mongo

# 2、格式化输出
$ awk '{printf "%-8s %-10s\n",$1,$4}' test.log
#---------------------输出------------------------
2        a
3        like
This's
10       orange,apple,mongo
```
- 用法2：`awk -F`  #-F相当于内置变量FS, 指定分割字符
```shell script
# 1、使用","分割
$  awk -F, '{print $1,$2}'   test.log
#--------------------输出-------------------------
2 this is a test
3 Are you like awk
This's a test
10 There are orange apple

# 2、或者使用内建变量
$ awk 'BEGIN{FS=","} {print $1,$2}'     test.log
-----------------------输出----------------------
2 this is a test
3 Are you like awk
This's a test
10 There are orange apple

# 3、使用多个分隔符.先使用空格分割，然后对分割结果再使用","分割
$ awk -F '[ ,]'  '{print $1,$2,$5}'   test.log
#-----------------------输出----------------------
2 this test
3 Are awk
This's a
10 There apple
```
- 用法3：`awk -v`  # 设置变量
```shell script
$ awk -va=1 '{print $1,$1+a}' test.log
#--------------------输出-------------------------
2 3
3 4
This's 1
10 11

$ awk -va=1 -vb=s '{print $1,$1+a,$1b}' test.log
#-------------------输出--------------------------
2 3 2s
3 4 3s
This's 1 This'ss
10 11 10s
```
- 用法4：`awk -f {awk脚本} {文件名}`
```shell script
$ awk -f cal.awk test.log
```
- 过滤第一列大于2的行
```shell script
$ awk '$1>2' test.log    #命令

#输出
3 Are you like awk
This's a test
10 There are orange,apple,mongo
```
- 过滤第一列等于2的行
```shell script
$ awk '$1==2 {print $1,$3}' test.log    #命令

#输出
2 is
```
- 过滤第一列大于2并且第二列等于'Are'的行
```shell script
$ awk '$1>2 && $2=="Are" {print $1,$2,$3}' test.log    #命令

#输出
3 Are you
```
- 输出第二列包含 "th"，并打印第二列与第四列
```shell script
$ awk '$2 ~ /th/ {print $2,$4}' test.log

#---------------------------------------------
this a
```
- 输出包含 "re" 的行
```shell script
$ awk '/re/ ' test.log

# ~ 表示模式开始。// 中是模式。
#---------------------------------------------
3 Are you like awk
10 There are orange,apple,mongo
```
- 忽略大小写
```shell script
$ awk 'BEGIN{IGNORECASE=1} /this/' test.log

#---------------------------------------------
2 this is a test
This's a test
```
- 模式取反
```shell script
$ awk '$2 !~ /th/ {print $2,$4}' test.log
#---------------------------------------------
Are like
a
There orange,apple,mongo

$ awk '!/th/ {print $2,$4}' test.log
#---------------------------------------------
Are like
a
There orange,apple,mongo
```

[目录](#目录)

## xargs
给命令传递参数的过滤器，将标准输入（stdin）数据转化为命令行参数，一般和管道`|`一起使用。

- `-a`：file 从文件中读入作为sdtin
- `-e`：flag ，注意有的时候可能会是-E，flag必须是一个以空格分隔的标志，当xargs分析到含有flag这个标志的时候就停止。
- `-p`：当每次执行一个argument的时候询问一次用户。
- `-n`：num 后面加次数，表示命令在执行的时候一次用的argument的个数，默认是用所有的。
- `-t`：表示先打印命令，然后再执行。
- `-i`：或者是-I，这得看linux支持了，将xargs的每项名称，一般是一行一行赋值给 {}，可以用 {} 代替。
- `-r`：no-run-if-empty 当xargs的输入为空的时候则停止xargs，不用再去执行了。
- `-s`：num 命令行的最大字符数，指的是 xargs 后面那个命令的最大命令行字符数。
- `-L`：num 从标准输入一次读取 num 行送给 command 命令。
- `-l`：同 -L。
- `-d`：delim 分隔符，默认的xargs分隔符是回车，argument的分隔符是空格，这里修改的是xargs的分隔符。
- `-x`：exit的意思，主要是配合-s使用。。
- `-P`：修改最大的进程数，默认是1，为0时候为as many as it can ，这个例子我没有想到，应该平时都用不到的吧。

```shell script
# cat test.log
a b c d e
f g h i j
k l

#多行输入单行输出：
# cat test.txt | xargs
a b c d e f g h i j k l

# cat test.txt | xargs -n3
a b c
d e f
g h i
j k l

# echo "nameXnameXnameXname" | xargs -dX
name name name name

# echo "nameXnameXnameXname" | xargs -dX -n2
name name
name name

#复制所有图片文件到 /data/images 目录下：
ls *.jpg | xargs -n1 -I {} cp {} /data/images

#用 rm 删除太多的文件时候，可能得到一个错误信息：/bin/rm Argument list too long. 用 xargs 去避免这个问题：
find . -type f -name "*.log" -print0 | xargs -0 rm -f
# -print0：指定输出的文件列表以null分隔。
# xargs命令的-0参数表示用null当作分隔符

#统计一个源代码目录中所有 php 文件的行数：
find . -type f -name "*.php" -print0 | xargs -0 wc -l

#查找所有的 jpg 文件，并且压缩它们：
find . -type f -name "*.jpg" -print | xargs tar -czvf images.tar.gz

#假如你有一个文件包含了很多你希望下载的 URL，你能够使用 xargs下载所有链接：
cat url-list.txt | xargs wget -c
```

[目录](#目录)

## 编辑文件sed
- 参数：
    - `-e<script> 或--expression=<script>` 以选项中指定的script来处理输入的文本文件。
    - `-f<script文件> 或--file=<script文件>` 以选项中指定的script文件来处理输入的文本文件。
    - `-h 或--help` 显示帮助。
    - `-n 或--quiet或--silent` 仅显示script处理后的结果。
    - `-V 或--version` 显示版本信息。
- 动作：
    - `a`：新增， a 的后面可以接字串，而这些字串会在新的一行出现(目前的下一行)～
    - `c`：取代， c 的后面可以接字串，这些字串可以取代 n1,n2 之间的行！
    - `d`：删除， d 后面通常不接任何东西；
    - `i`：插入， i 的后面可以接字串，而这些字串会在新的一行出现(目前的上一行)；
    - `p`：打印，亦即将某个选择的数据印出。通常 p 会与参数 sed -n 一起运行～
    - `s`：取代，可以直接进行取代的工作，通常这个 s 的动作可以搭配正规表示法！例如 1,20s/old/new/g

```shell script
#!/usr/bin/env bash
LOGPATH='/home/test/shell/'
TEST_LOG="${LOGPATH}test.log"
#$TEST_LOG初始内容
## abc.
## def.
## ghi.
## jkl.

# 在TEST_LOG文件的第四行后添加一行，并将结果输出到标准输出
sed -e 4a\newLine $TEST_LOG
## ...
## jkl
## newLine

# 将 TEST_LOG 的内容列出并且列印行号，同时，请将第 2~5 行删除！
nl $TEST_LOG | sed -e '2,5d'
# 删除第 3 到最后一行($：代表最后一行)
nl $TEST_LOG | sed -e '3,$d'
# 在第二行后(亦即是加在第三行)加上『drink tea?』字样！
nl $TEST_LOG | sed -e '2a drink tea'
# 在第二行前加上『drink tea?』字样！
nl $TEST_LOG | sed -e '2i drink tea'
# 在第4行之后追加 3 行(2 行文字和 1 行空行)
sed -e '4 a newline\nnewline2\n' $TEST_LOG

# 将第2-5行的内容替换成为『No 2-5 number』
nl $TEST_LOG | sed '2,5c No 2-5 number'

# 搜索$TEST_LOG有root关键字的行，只输出匹配行
nl $TEST_LOG | sed -n '/root/p'

# 删除$TEST_LOG所有包含root的行，其他行输出
nl $TEST_LOG | sed  '/root/d'

# 搜索$TEST_LOG,找到root对应的行，执行后面花括号中的一组命令，每个命令之间用分号分隔
# 这里把bash替换为blueshell，再输出这行,q表示退出
nl $TEST_LOG | sed -n '/root/{s/bash/blueshell/;p;q}'

# 一条sed命令，删除$TEST_LOG第3行到末尾的数据，并把bash替换为blueshell
nl $TEST_LOG | sed -e '3,$d' -e 's/bash/blueshell/'


# 数据的搜寻并替换
# 格式：sed 's/要被取代的字串/新的字串/g'
# 替换多个格式：sed 's/要被取代的字串/新的字串/g;s/source2/target2/g;s/source3/target3/g'

# 查询本机ip，将IP前面和后面的部分删除，过滤出ip地址（inet addr:192.168.1.100 Bcast:192.168.1.255 Mask:255.255.255.0）
/sbin/ifconfig eth0 | grep 'inet addr' | sed 's/^.*addr://g' | sed 's/Bcast.*$//g'


# 修改文件内容
# 将$TEST_LOG中每行结尾的.替换成！
sed -i 's/\.$/\!/g' $TEST_LOG
# 在$TEST_LOG最后一行添加 #This is a test
sed -i '$a # This is a test' $TEST_LOG

```
- [菜鸟](https://www.runoob.com/linux/linux-comm-sed.html)

[目录](#目录)

## EOF
- End Of File：文件结束符，可自定义成其他的字符串，但要成对出现
- EOF通常与 << 结合使用，`<<EOF` 表示后续的输入作为子命令或子shell的输入，直到遇到EOF结束，再返回到主调Shell。
- 格式：
    ```shell script
    命令 <<EOF
    cmd1
    cmd2
    ...
    EOF
  
    例：在test.log中追加三行代码。
    cat >> /root/test.txt <<EOF 
    abc
    def
    ghi
    EOF   
    ```
- 特殊用法：
    ```shell script
    : << COMMENTBLOCK
       shell脚本代码段
    COMMENTBLOCK
    # 用来注释整段脚本代码。 : 是shell中的空语句。
    # 例如：这段脚本执行时，中间部分不会被执行。
    echo start
    :<<COMMENTBLOCK
    echo
    echo "this is a test"
    echo
    COMMENTBLOCK
    echo end
    ```

[目录](#目录)

# 实战
## 常用操作
```shell script
#!/bin/bash
# 关闭对使用POSIX规范的检测
set +o posix

LOGPATH='/home/test/shell/'
TEST_LOG="${LOGPATH}test.log"
SHELL_LOG="${LOGPATH}shell.log"
STR="abc_123.234.345.456:7890-def.test"

# 定义日志格式
log_info="eval echo \"[$0]\" @\`date +\"%Y%m%d %T\"\` [info]: "
log_error="eval echo \"[$0]\" @\`date +\"%Y%m%d %T\"\` [error]: "

# 当前时间
`date +"%Y%m%d%H%M%S"`

# 打印回车
echo -e "\n" >> $TEST_LOG

# 查询目录下的文件名
files=$(ls $LOGPATH)

# 字符串替换
str=${STR//./_} #将STR中的所有.替换成_
str=${STR/_/.} #将STR中的第一个_替换成.
# 通过sed替换字符，把$STR中的. : -都替换成_，末尾的.test删除
echo $STR | sed 's/./_/g;s/:/_/g;s/-/_/g;s/.test$//g'

# 读取TEST_LOG文件,追加到$SHELL_LOG
while read line
do 
	$log_info $line >> $SHELL_LOG
done < $TEST_LOG

# 输出追加到多个文件,并取消打印在控制台
echo $STR | tee -a $TEST_LOG $SHELL_LOG > /dev/null

# 求两个文件的差集：$TEST_LOG - $SHELL_LOG
sort -m <(sort $TEST_LOG | uniq) <(sort $SHELL_LOG | uniq) <(sort $SHELL_LOG | uniq) | uniq -u
## 求差集并写入到文件
sort -m <(sort $TEST_LOG | uniq) <(sort $SHELL_LOG | uniq) <(sort $SHELL_LOG | uniq) | uniq -u | tee -a $TEST_LOG $SHELL_LOG > /dev/null
## 1.求出差集并直接读取
sort -m <(sort $TEST_LOG | uniq) <(sort $SHELL_LOG | uniq) <(sort $SHELL_LOG | uniq) | uniq -u | while read line
do
    $log_info $line >> $TEST_LOG
done
## 2.或者使用grep，以$SHELL_LOG为基准，找出$TEST_LOG中不存在与$SHELL_LOG中的行
grep -vxFf $SHELL_LOG $TEST_LOG

# 查找/test/目录下(不包含子目录)2天以前的目录名
find /test/ -maxdepth 1 -type d -mtime +1

#行号
${LINENO}

```

[目录](#目录)

## Bash script template
- 一个基础、简单、安全的脚本模板
- 使用这个模板：
    - 直接复制粘贴
    - 需要修改4处：
        - 包含脚本说明的usage()文本
        - cleanup()内容
        - parse_params()中的参数，保留--help和--no-color，但是替换示例：-f、-p
        - 实际脚本逻辑

```shell script
#!/usr/bin/env bash
## 使用/usr/bin/env bash比/bin/bash兼容性更好

## 脚本遇错终止，返回错误（放在Bash脚本头部）
### -e：脚本遇到错误会终止执行(根据返回值是否是0判断)。
### -u：脚本遇到报错会停止并返回错误。
### -o pipefail：解决-e不适用于管道命令，只要一个子命令失败，管道就会失败，脚本就终止。
### -x：在输出结果之前，输出执行的那行命令（常用于调试复杂脚本）：set -euxo pipefail
### -E：If set, any trap on ERR is inherited by shell functions, command substitutions, and commands executed in a subshell environment. The ERR trap is normally not inherited in such cases.
set -Eeuo pipefail
## 脚本结束时，执行cleanup()函数，清除脚本创建的临时文件。
### trap 捕捉程序运行时的信号。（trap --list：列出所有信号）
### SIGINT：终止进程、中断进程（通常用户Ctrl-C时发出）
### SIGTERM：程序结束（通常程序正常退出）
### ERR：进程以非 0 状态码退出时发出的信号
### EXIT：进程退出时发出的信号
trap cleanup SIGINT SIGTERM ERR EXIT

## 定义脚本的位置目录，然后对其进行 cd 配置
### 如果脚本从同一目录中读取某个文件或执行另一个程序，可以这样调用：cat "$script_dir/my_file"
script_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null && pwd -P)

## Display helpful help：尽量让usage()函数相对靠近脚本的顶部，有两种作用：
### 要为不知道所有选项并且不想查看整个脚本来发现这些选项的人显示帮助。
### 当有人修改脚本时，保存一个最小的文档（因为两周后，你甚至不记得当初是怎么写的）。
usage() {
  cat <<EOF
Usage: $(basename "${BASH_SOURCE[0]}") [-h] [-v] [-f] -p param_value arg1 [arg2...]

Script description here.

Available options:

-h, --help      Print this help and exit
-v, --verbose   Print script debug info
-f, --flag      Some flag description
-p, --param     Some param description
EOF
  exit
}

## cleanup()函数不仅可以在最后调用，可以在任何时候调用
cleanup() {
  trap - SIGINT SIGTERM ERR EXIT
  # script cleanup here
}

## 在文本中使用颜色，只用于msg()函数（不用于echo）
setup_colors() {
  if [[ -t 2 ]] && [[ -z "${NO_COLOR-}" ]] && [[ "${TERM-}" != "dumb" ]]; then
    NOFORMAT='\033[0m' RED='\033[0;31m' GREEN='\033[0;32m' ORANGE='\033[0;33m' BLUE='\033[0;34m' PURPLE='\033[0;35m' CYAN='\033[0;36m' YELLOW='\033[1;33m'
  else
    NOFORMAT='' RED='' GREEN='' ORANGE='' BLUE='' PURPLE='' CYAN='' YELLOW=''
  fi
}

## 打印不是脚本输出的所有内容，包括：日志、消息
### 用msg()打印的消息被发送到stderr流并支持特殊的序列，比如颜色。如果stderr输出不是交互式终端，或者传递了一个标准参数，那么颜色将被禁用。
#### 用法：msg "This is a ${RED}very important${NOFORMAT} message, but not a script output value!"
### 要检查stderr是不是交互式终端时的行为，请在脚本中添加类似于上面的一行。然后执行它，将stderr重定向到stdout并通过管道将其发送到cat。管道操作使输出不再直接发送到终端，而是发送到下一个命令，因此颜色会被禁用。
#### $ ./test.sh 2>&1 | cat This is a very important message, but not a script output value!
msg() {
  echo >&2 -e "${1-}"
}

die() {
  local msg=$1
  local code=${2-1} # default exit status 1
  msg "$msg"
  exit "$code"
}

## 解析任意参数
parse_params() {
  # default values of variables set from params
  flag=0
  param=''

  while :; do
    case "${1-}" in
    -h | --help) usage ;;
    -v | --verbose) set -x ;;
    --no-color) NO_COLOR=1 ;;
    -f | --flag) flag=1 ;; # example flag
    -p | --param) # example named parameter
      param="${2-}"
      shift
      ;;
    -?*) die "Unknown option: $1" ;;
    *) break ;;
    esac
    shift
  done

  args=("$@")

  # check required params and arguments
  [[ -z "${param-}" ]] && die "Missing required parameter: param"
  [[ ${#args[@]} -eq 0 ]] && die "Missing script arguments"

  return 0
}

parse_params "$@"
setup_colors

# script logic here

msg "${RED}Read parameters:${NOFORMAT}"
msg "- flag: ${flag}"
msg "- param: ${param}"
msg "- arguments: ${args[*]-}"
```
- [link](http://blog.didispace.com/minimal-safe-bash-script-template/)

[目录](#目录)

# linux
## 常用命令
- `ls`: 列出目录及文件名
    - `-a` ：全部的文件，连同隐藏文件( 开头为 . 的文件) 一起列出来(常用)
    - `-d` ：仅列出目录本身，而不是列出目录内的文件数据(常用)
    - `-l` ：长数据串列出，包含文件的属性与权限等等数据；(常用)
- `cd`：切换目录
- `pwd`：显示目前的目录
    - `-P` ：显示出确实的路径，而非使用连结 (link) 路径。
- `mkdir`：创建一个新的目录
    - `-m` ：配置文件的权限喔！直接配置，不需要看默认权限 (umask) 的脸色～
    - `-p` ：帮助你直接将所需要的目录(包含上一级目录)递归创建起来！
- `rmdir`：删除一个空的目录
    - `-p` ：连同上一级『空的』目录也一起删除
- `touch` ：创建一个文件`touch test.txt`
    - 或者直接`vim test.txt`
- `cp`: 复制文件或目录
    - `-a`：相当於 -pdr 的意思，至於 pdr 请参考下列说明；(常用)
    - `-d`：若来源档为连结档的属性(link file)，则复制连结档属性而非文件本身；
    - `-f`：为强制(force)的意思，若目标文件已经存在且无法开启，则移除后再尝试一次；
    - `-i`：若目标档(destination)已经存在时，在覆盖时会先询问动作的进行(常用)
    - `-l`：进行硬式连结(hard link)的连结档创建，而非复制文件本身；
    - `-p`：连同文件的属性一起复制过去，而非使用默认属性(备份常用)；
    - `-r`：递归持续复制，用於目录的复制行为；(常用)
    - `-s`：复制成为符号连结档 (symbolic link)，亦即『捷径』文件；
    - `-u`：若 destination 比 source 旧才升级 destination ！
- `rm`: 移除文件或目录
    - `-f` ：就是 force 的意思，忽略不存在的文件，不会出现警告信息；
    - `-i` ：互动模式，在删除前会询问使用者是否动作
    - `-r` ：递归删除啊！最常用在目录的删除了！这是非常危险的选项！！！
- `mv`: 移动文件与目录，或修改文件与目录的名称
    - `-f` ：force 强制的意思，如果目标文件已经存在，不会询问而直接覆盖；
    - `-i` ：若目标文件 (destination) 已经存在时，就会询问是否覆盖！
    - `-u` ：若目标文件已经存在，且 source 比较新，才会升级 (update)

- `cat`  由第一行开始显示文件内容
    - `-A` ：相当於 -vET 的整合选项，可列出一些特殊字符而不是空白而已；
    - `-b` ：列出行号，仅针对非空白行做行号显示，空白行不标行号！
    - `-E` ：将结尾的断行字节 $ 显示出来；
    - `-n` ：列印出行号，连同空白行也会有行号，与 -b 的选项不同；
    - `-T` ：将 [tab] 按键以 ^I 显示出来；
    - `-v` ：列出一些看不出来的特殊字符
- `tac`  从最后一行开始显示，可以看出 tac 是 cat 的倒着写！
- `nl`   显示的时候，顺道输出行号！
    - `-b` ：指定行号指定的方式，主要有两种：
        - `-b a` ：表示不论是否为空行，也同样列出行号(类似 cat -n)；
        - `-b t` ：如果有空行，空的那一行不要列出行号(默认值)；
    - `-n` ：列出行号表示的方法，主要有三种：
        - `-n ln` ：行号在荧幕的最左方显示；
        - `-n rn` ：行号在自己栏位的最右方显示，且不加 0 ；
        - `-n rz` ：行号在自己栏位的最右方显示，且加 0 ；
    - `-w` ：行号栏位的占用的位数。
- `more` 一页一页的显示文件内容
    - `空白键 (space)`：代表向下翻一页；
    - `Enter`         ：代表向下翻『一行』；
    - `/字串`         ：代表在这个显示的内容当中，向下搜寻『字串』这个关键字；
    - `:f`            ：立刻显示出档名以及目前显示的行数；
    - `q`             ：代表立刻离开 more ，不再显示该文件内容。
    - `b 或 [ctrl]-b` ：代表往回翻页，不过这动作只对文件有用，对管线无用。
- `less` 与 more 类似，但是比 more 更好的是，他可以往前翻页！
    - `空白键`    ：向下翻动一页；
    - `[pagedown]`：向下翻动一页；
    - `[pageup]`  ：向上翻动一页；
    - `/字串`     ：向下搜寻『字串』的功能；
    - `?字串`     ：向上搜寻『字串』的功能；
    - `n`         ：重复前一个搜寻 (与 / 或 ? 有关！)
    - `N`         ：反向的重复前一个搜寻 (与 / 或 ? 有关！)
    - `q`         ：离开 less 这个程序；
- `head` 只看头几行
    - `-n` ：后面接数字，代表显示几行的意思
- `tail` 只看尾巴几行
    - `-n` ：后面接数字，代表显示几行的意思
    - `-f` ：表示持续侦测后面所接的档名，要等到按下[ctrl]-c才会结束tail的侦测
- `grep`：查找文件里符合条件的字符串
    - `-v`：不包含字符串的行。例如：`grep "abc" test.log |grep -v "def"`表示从test.log文件中找出包含abc且不包含def的行。
    - `-n`：显示行号。
    - `-i`：忽略大小写。
    - `-E`：多条件查找。`grep -E "aa|bb" test.log`查找包含aa或者bb的行
        - 等同于：`egrep "aa|bb" test.log`
- `zgrep`：可以查找压缩文件。
- `find`：在指定目录下查找文件。
    - `-mount`, `-xdev` : 只检查和指定目录在同一个文件系统下的文件，避免列出其它文件系统中的文件
    - `-anewer file` : 比文件 file 更晚被读取过的文件
    - `-cnewer file` :比文件 file 更新的文件
    - `-amin n` : 在过去 n 分钟内被读取过
    - `-cmin n `: 在过去 n 分钟内被修改过
    - `-atime n` : 在过去n天内被读取过的文件
    - `-ctime n`: 在过去n天内被修改过的文件
    - `-mtime +n`：n天之前的文件。`+0`：表示24小时之前，`+1`：表示48小时之前，`1`：表示24-48小时之内，`-1`：表示24小时之内。
    - `-empty` : 空的文件-gid n or -group name : gid 是 n 或是 group 名称是 name
    - `-ipath p`, `-path p` : 路径名称符合 p 的文件，ipath 会忽略大小写
    - `-name name`, `-iname name` : 文件名称符合 name 的文件。iname 会忽略大小写
    - `-size n` : 文件大小 是 n 单位，b 代表 512 位元组的区块，c 表示字元数，k 表示 kilo bytes，w 是二个位元组。
    - `-type c` : 文件类型是 c 的文件。
    - `d`: 目录
    - `c`: 字型装置文件
    - `b`: 区块装置文件
    - `p`: 具名贮列
    - `f`: 一般文件
    - `l`: 符号连结
    - `s`: socket
    - `-pid n` : process id 是 n 的文件
    ```shell script
    # 当前目录及其子目录下所有文件后缀为 .c 的文件列出来:
    find . -name "*.c"
    
    # 列出当前目录下的文件
    find . -type f
    # 列出当前目录下的目录
    find . -type d
    
    # 列出当前目录及其子目录下所有最近 20 天内更新过的文件
    find . -ctime -20
    
    # 查找 /var/log 目录中更改时间在 7 日以前的普通文件，并在删除之前询问它们：
    find /var/log -type f -mtime +7 -ok rm {} \;
    
    # 查找当前目录中文件属主具有读、写权限，并且文件所属组的用户和其他用户具有读权限的文件：
    find . -type f -perm 644 -exec ls -l {} \;
    
    # 查找系统中所有文件长度为 0 的普通文件，并列出它们的完整路径：
    find / -type f -size 0 -exec ls -l {} \;
    
    # 查找/test/目录下(不包含子目录)2天以前的目录名
    find /test/ -maxdepth 1 -type d -mtime +1
    ```
- `date`：打印当前时间
    - `-d`<字符串> 　显示字符串所指的日期与时间。字符串前后必须加上双引号。
    - `-s`<字符串> 　根据字符串来设置日期与时间。字符串前后必须加上双引号。
    - `-u` 　显示GMT。
    - `%H` 小时(00-23)
    - `%I` 小时(00-12)
    - `%M` 分钟(以00-59来表示)
    - `%s` 总秒数。起算时间为1970-01-01 00:00:00 UTC。
    - `%S` 秒(以本地的惯用法来表示)
    - `%a` 星期的缩写。
    - `%A` 星期的完整名称。
    - `%d` 日期(以01-31来表示)。
    - `%D` 日期(含年月日)。
    - `%m` 月份(以01-12来表示)。
    - `%y` 年份(以00-99来表示)。
    - `%Y` 年份(以四位数来表示)。
    ```shell script
    # 当前时间
    `date +"%Y%m%d%H%M%S"`
    
    # 显示下一天的日期
    date +%Y%m%d --date="+1 day"
    
    # 2周后的日期
    date -d '2 weeks'
  
    # date
    三 5月 12 14:08:12 CST 2010
    # date '+%c' 
    2010年05月12日 星期三 14时09分02秒
    # date '+%D' //显示完整的时间
    05/12/10
    # date '+%x' //显示数字日期，年份两位数表示
    2010年05月12日
    # date '+%T' //显示日期，年份用四位数表示
    14:09:31
    # date '+%X' //显示24小时的格式
    14时09分39秒
    ```
[目录](#目录)

## 用户-用户组
- `useradd 选项 用户名`：添加新的用户账号,`# useradd –d  /home/sam -m sam`,`# useradd -s /bin/sh -g group –G adm,root gem`
    - `-c` comment 指定一段注释性描述。
    - `-d` 目录 指定用户主目录，如果此目录不存在，则同时使用-m选项，可以创建主目录。
    - `-g` 用户组 指定用户所属的用户组。
    - `-G` 用户组，用户组 指定用户所属的附加组。
    - `-s` Shell文件 指定用户的登录Shell。
    - `-u` 用户号 指定用户的用户号，如果同时有-o选项，则可以重复使用其他用户的标识号。
- `userdel 选项 用户名`：删除帐号
    - `-r`，作用是把用户的主目录一起删除。
- `usermod 选项 用户名`:修改帐号,`# usermod -s /bin/ksh -d /home/z –g developer sam`
- `passwd 选项 用户名`:用户口令的管理
    - `-l` 锁定口令，即禁用账号。
    - `-u` 口令解锁。
    - `-d` 使账号无口令。
    - `-f` 强迫用户下次登录时修改口令。

- 用户组管理;
- `groupadd 选项 用户组` 增加一个新的用户组
    - `-g` GID 指定新用户组的组标识号（GID）。
    - `-o` 一般与-g选项同时使用，表示新用户组的GID可以与系统已有用户组的GID相同。
- `groupdel 用户组` 删除用户组
- `groupmod 选项 用户组` 修改用户组
    - `-g` GID 为用户组指定新的组标识号。
    - `-o` 与-g选项同时使用，用户组的新GID可以与系统已有用户组的GID相同。
    - `-n`新用户组 将用户组的名字改为新名字
    
- 与用户账号有关的系统文件:
- `/etc/passwd` 记录每个用户的一些基本属性
    `用户名:口令:用户标识号:组标识号:注释性描述:主目录:登录Shell`
- `/etc/shadow` 记录行与/etc/passwd中的一一对应，它由pwconv命令根据/etc/passwd中的数据自动产生
    `登录名:加密口令:最后一次修改时间:最小时间间隔:最大时间间隔:警告时间:不活动时间:失效时间:标志`
- `/etc/group` 用户组的所有信息
    `组名:口令:组标识号:组内用户列表`

[目录](#目录)

## 磁盘管理
- `df`：列出文件系统的整体磁盘使用量
    - `-a` ：列出所有的文件系统，包括系统特有的 /proc 等文件系统；
    - `-k` ：以 KBytes 的容量显示各文件系统；
    - `-m` ：以 MBytes 的容量显示各文件系统；
    - `-h` ：以人们较易阅读的 GBytes, MBytes, KBytes 等格式自行显示；
    - `-H` ：以 M=1000K 取代 M=1024K 的进位方式；
    - `-T` ：显示文件系统类型, 连同该 partition 的 filesystem 名称 (例如 ext3) 也列出；
    - `-i` ：不用硬盘容量，而以 inode 的数量来显示
- `du`：检查磁盘空间使用量
    - `-a` ：列出所有的文件与目录容量，因为默认仅统计目录底下的文件量而已。
    - `-h` ：以人们较易读的容量格式 (G/M) 显示；
    - `-s` ：列出总量而已，而不列出每个各别的目录占用容量；
    - `-S` ：不包括子目录下的总计，与 -s 有点差别。
    - `-k` ：以 KBytes 列出容量显示；
    - `-m` ：以 MBytes 列出容量显示；
- `fdisk`：用于磁盘分区
    - `-l` ：输出后面接的装置所有的分区内容。若仅有 fdisk -l 时， 则系统将会把整个系统内能够搜寻到的装置的分区均列出来。
- `mkfs [-t 文件系统格式] 装置文件名` 磁盘格式化
    - `-t` ：可以接文件系统格式，例如 ext3, ext2, vfat 等(系统有支持才会生效)
- `fsck [-t 文件系统] [-ACay] 装置名称` 磁盘检验,用来检查和维护不一致的文件系统。若系统掉电或磁盘发生问题，可利用fsck命令对文件系统进行检查。
    - `-t` : 给定档案系统的型式，若在 /etc/fstab 中已有定义或 kernel 本身已支援的则不需加上此参数
    - `-s` : 依序一个一个地执行 fsck 的指令来检查
    - `-A` : 对/etc/fstab 中所有列出来的 分区（partition）做检查
    - `-C` : 显示完整的检查进度
    - `-d` : 打印出 e2fsck 的 debug 结果
    - `-p` : 同时有 -A 条件时，同时有多个 fsck 的检查一起执行
    - `-R` : 同时有 -A 条件时，省略 / 不检查
    - `-V` : 详细显示模式
    - `-a` : 如果检查有错则自动修复
    - `-r` : 如果检查有错则由使用者回答是否修复
    - `-y` : 选项指定检测每个文件是自动输入yes，在不确定那些是不正常的时候，可以执行 # fsck -y 全部检查修复。
- `mount [-t 文件系统] [-L Label名] [-o 额外选项] [-n]  装置文件名  挂载点` 磁盘挂载与卸除
- `umount [-fn] 装置文件名或挂载点` 磁盘卸除
    - `-f` ：强制卸除！可用在类似网络文件系统 (NFS) 无法读取到的情况下；
    - `-n` ：不升级 /etc/mtab 情况下卸除。
    
[目录](#目录)

## vim
- `Esc`
    - `dd` 删除光标所在行
    - `yy` 复制光标所在行
    - `u` 撤销

[目录](#目录)

# 问题解决
## syntax error near unexpected token
- 现象：
    - `sh test.sh`使用sh执行脚本时报错：`syntax error near unexpected token `('`
    - 使用`bash`不报错
- 解决：
    在脚本首行添加：`set +o posix` 关闭对使用POSIX规范的检测。
- 参考：[https://blog.csdn.net/su20145104009/article/details/91458006](https://blog.csdn.net/su20145104009/article/details/91458006)

[目录](#目录)