# 目录
- [shell](#shell)
    - [while](#while)
    - [字符串截取](#字符串截取)

# shell
## while
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