##快捷鍵  
Ctrl+Alt+L，格式化代码
Alt+Insert，生成构造方法、Getter、Setter等

##idea优化  
- **启动优化**  
文件位置：  
`/idea安装位置/bin/idea.exe.vmoptions`  
修改参数：
```
-Xms1024m
-Xmx2048m
-XX:ReservedCodeCacheSize=500m
```
- **编码优化**  
```
自动导包：Settings/Editor/General/Auto Import勾选Java下all、Add unambiguous...、Optimize imports...
导包不合为*：Settings/Editor/Code Style/Java选择Imports，将Class count to use import with ‘*’ 和Names count to use static import with ‘*’ 两项设置成9999
Tab替换4个空格：Settings/Editor/Code Style/Java取消勾选Use tab character，Tabs and Indents设置为4
代码提示时忽略输入大小写：Settings/Editor/General/Code Completion取消勾选Match case
鼠标悬浮提示：Settings/Editor/Code Editing勾选Show quick documentation on mouse move
打开的文件不折叠，多行显示：Settings/Editor/General/Editor Tabs取消勾选Show tabs in one row
字体设置：Settings/Editor/Font
注释模板设置：Settings/Editor/File and Code Templates选择Includes/File Header
    /**
     * @create ${DATE} ${TIME}
     * @auther outman
     **/
编码格式：Settings/Editor/File Encodings选择Global Encodings、Project Encodings、Default encoding for properties files(右边自动转换勾上)为UTF-8
修改代码提示快捷键：Settings/Keymap，先点击Find Shortcut搜索Alt+/(或者选择Main menu/Code/Code Completion/Cyclic Expand Word)，把快捷键释放占用，再在Find Shortcut搜索Ctrl+空格(或者选择Main menu/Code/Code Completion/Basic)修改为Alt+/
```