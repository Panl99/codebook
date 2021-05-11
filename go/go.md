> [Go软件下载地址](https://golang.google.cn/) <br/>
> [Go官方文档](https://go-zh.org/doc/)  <br/>
> [Go指南(在线练习)](https://tour.go-zh.org/welcome/1)  <br/>
> [Go语言实战](../resources/static/doc/Go语言实战.pdf)  <br/>
> [Go程序设计语言]()  <br/>

# Go
- Go是编译性语言，Go的工具链将程序的源文件转变成机器相关的原生二进制指令。
- Go原生地支持Unicode，所以它可以处理所有国家的语言。

# Go语法
- Hello World
```
package main

import "fmt"

func main() {
    fmt.Println("Hello, World")
}
```
- 运行helloworld：`$ go run helloworld.go` `$`为命令提示符
    - 结果输出：Hello, World
- 编译生成可执行多次的二进制程序：`$ go build helloworld.go` 将生成一个`helloworld`的二进制程序
    - 执行二进制程序：`$ ./helloworld`


## 数组

## 切片

## 映射

## 并发

