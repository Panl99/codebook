> [Go软件下载地址](https://golang.google.cn/) <br/>
> [Go官方文档](https://go-zh.org/doc/)  <br/>
> [Go指南(在线练习)](https://tour.go-zh.org/welcome/1)  <br/>
> [Go语言实战](../resources/static/doc/Go语言实战.pdf)  <br/>
> [Go程序设计语言]()  <br/>
> [Go社区文档](https://learnku.com/go/docs)  <br/>
> [书栈网Go文档](https://www.bookstack.cn/explore?cid=10&tab=popular)  <br/>
> [Go实践文档](https://mehdihadeli.github.io/awesome-go-education/)  <br/>

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


## 格式约定
- `gofmt`，Go默认会格式化代码，也可使用`go fmt`手动格式化。
- `godoc`，文档处理，块注释使用`/* */`，行注释使用`//`

- 命名规则：包名小写(后可加数字)，一个单词，简短、简明，不要使用下划线、大小写混合
    - 例子：once.Do；once.Do(setup)很好读，写成once.DoOrWaitUntilDone(setup)并不会有所改善。长名字并不会自动使得事物更易读。具有帮助性的文档注释往往会比格外长的名字更有用。
- Get方法：Go不自动支持Get方法和Set方法，需要自己提供。
    - 但是Get方法命名时不要添加Get单词，但是Set方法需添加Set单词。
    - 如：有一个域叫做owner（小写，不被导出），则Get方法应该叫做Owner（大写，被导出），而不是GetOwner。对于要导出的，使用大写名字，提供了区别域和方法的钩子。Set方法，如果需要，则可以叫做SetOwner。
        ```go
        owner := obj.Owner()
        if owner != user {
            obj.SetOwner(user)
        }
        ```
- 接口名：
    - 单个方法的接口使用方法名加上“er”后缀来命名，或者类似的修改来构造一个施动者名词：Reader，Writer，Formatter，CloseNotifier等。
    - 一般尽量避免命名跟一些已知的通用名字一样，除非其具有相同的签名和含义。如Read，Write，Close，Flush，String等
    - 如果要命名的方法名含义跟一个众所周知的类型一样，那么就使用相同的名字和签名；例如，字符串转换方法起名为String，而不是ToString。
- 混合大小写：Go约定使用`MixedCaps`或者`mixedCaps`的形式，而不是下划线来书写多个单词的名字。

- 分号：
    - Go使用分号`;`来终结语句。但是源码中并没有分号。词法分析器会在扫描时，使用简单的规则自动插入分号，因此源码中基本就不用分号了。
    - 规则：如果在换行之前的最后一个符号为一个标识符（包括像int和float64这样的单词），一个基本的文字（例如数字、字符串常量），或者如一个符号`break continue fallthrough return ++ -- ) }`，则词法分析器总是会在符号之后插入一个分号。
    - 总结：一行代码的结尾：换行出现在可以结束一条语句的符号之后
    - 通常 Go 程序只在诸如 for 循环子句这样的地方使用分号， 以此来将初始化器、条件及增量元素分开。如果你在一行中写多个语句，也需要用分号隔开。
    - 所以不要将一个控制结构（if、for、switch 或 select）的左大括号放在下一行。
        ```go
        // 正确格式
        if i < f() {
            g()
        }
      
        // 错误格式
        if i < f()  // 错误！这里会被添加一个分号结束语句
        {           //  错误！
            g()
        }
        ```
## 控制结构
// TODO 
    
    
## 数组

## 切片

## 映射

## 并发

# Go框架

## [GoFrame](https://goframe.org/pages/viewpage.action?pageId=1114119)
**GoFrame是一款模块化、高性能、企业级的Go基础开发框架。**  
**GoFrame不是一款WEB/RPC框架**，而是一款通用性的基础开发框架，是Golang标准库的一个增强扩展级，包含通用核心的基础开发组件。  
优点是实战化、模块化、文档全面、模块丰富、易用性高、通用性强、面向团队。


## [Gin](https://gin-gonic.com/docs/)
**Gin是一款HTTP Web框架。** ~~它具有类似 Martini 的 API，但性能比 Martini 快 40 倍。~~


## [go-zero](https://go-zero.dev/cn/)
**go-zero 是一个集成了各种工程实践的 web 和 rpc 框架。**  
通过弹性设计保障了大并发服务端的稳定性，经受了充分的实战检验。  
go-zero 包含极简的 API 定义和生成工具 **goctl**，可以根据定义的 api 文件一键生成 Go, iOS, Android, Kotlin, Dart, TypeScript, JavaScript 代码，并可直接运行。

# [Go导航](https://hao.studygolang.com/)

## [Go代理](https://goproxy.cn/)
