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
- [linux](#linux)
    - [常用命令](#常用命令)
    - [用户-用户组](#用户-用户组)
    - [磁盘管理](#磁盘管理)

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
[目录](#目录)

## 字符串截取
```shell script
#!/bin/bash
echo 'Hello World!'

logpath='home/test/shell/shell-test.log'
url='https://www.test.com/blog/shell-test'
# 输出字符串长度
echo ${#logpath} ${#url}

# 截取最后一个/后的值
log_suffix=${logpath##*/}
echo $log_suffix
# 截取第一个/后的值
url_suffix=${url#*/}
echo $url_suffix

# 截取最后一个/前的值
log_prefix=${logpath%%/*}
echo $log_prefix
# 截取第一个/前的值
url_prefix=${url%/*}
echo $url_prefix

# 自定义截取
sublogpath=${logpath:0:5}
suburl=${url:0:8}
echo $sublogpath $suburl
# 从左边第9个字符开始到最后
sublogpath=${logpath:8}
suburl=${url:8}
echo $sublogpath '***' $suburl
# 从右边第9个字符开始截取5个字符
sublogpath=${logpath:0-9:5}
suburl=${url:0-9:5}
echo $sublogpath '***' $suburl

echo 'end...'
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
- `$@` 参数个数
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

- 文件内容查看：
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
