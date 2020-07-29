
# 目录
- [mysql常用函数](#mysql常用函数)
- [mysql使用](#mysql使用)
    - [连接mysql](#连接mysql)
    - [数据定义语言DDL](#数据定义语言DDL)
        - [库操作](#库操作)
        - [表操作](#表操作)
    - [数据操作语言DML](#数据操作语言DML)
        - [插入](#插入)
        - [修改](#修改)
        - [删除](#删除)
        - [查询](#查询)
    - [存储过程](#存储过程)
    - [函数](#函数)
    - [游标](#游标)
    - [触发器](#触发器)
    - [视图](#视图)
    - [事件](#事件)
- [事务](#事务)
    - [事务处理](#事务处理)
    - [锁](#锁)
- [备份与恢复](#备份与恢复)
    - [备份](#备份)
    - [恢复](#恢复)
- [二进制日志](#二进制日志)
- [复制](#复制)
- [性能优化](#性能优化)

[返回目录](#目录)

# mysql常用函数

- **转换为小写[lower]、转换为大写[upper]：**  
`select lower('ABC'), upper('abc') from dual; => abc,ABC`
- **返回字符串长度[length]：**  
`select length('abc') from dual; => 3`
- **左填充[lpad]，右填充[rpad]：**  
`SELECT RPAD(LPAD('abc',5,'*'),8,'*') FROM DUAL; => **abc***`
- **去除字符串两端空格[trim]，去除左边[ltrim]，去除右边[rtim]：**  
`SELECT TRIM(' ABC ') FROM DUAL; => 'ABC' ; `
`SELECT TRIM('s' FROM 'strings') FROM DUAL; => tring`
`SELECT LTRIM(' ABC ') FROM DUAL; => 'ABC ' ; `
`SELECT RTRIM(' ABC ') FROM DUAL; => ' ABC' ; `
- **获取子字符串[substr]：**  
`SELECT SUBSTR('ABCDEF',2,3) FROM DUAL; => BCD  --从第2个位置开始取3个`
- **字符串替换[replace]：**  
`SELECT REPLACE('aabbcc','bb','dd') FROM DUAL;  => aaddcc`
- **绝对值[abs]：**  
`SELECT ABS(100),ABS(-100) FROM DUAL; => 100 100`
- **平均值[avg]：**  
`SELECT AVG(DISTINCT age) FROM tableName;  --对不同的值求平均值`
`SELECT AVG(ALL age) FROM tableName;  --对所有值求平均值`
- **最大值[max]，最小值[min]：**  
`SELECT MAX(DISTINCT age) FROM tableName;` 
`SELECT MAX(ALL age) FROM tableName; `
`SELECT MIN(DISTINCT age) FROM tableName;` 
`SELECT MIN(ALL age) FROM tableName;`
- **四舍五入[round],按照指定精度截取[trunc]：**  
`SELECT ROUND(5.5),ROUND(-5.5) FROM DUAL; => 6,-6`
`SELECT TRUNC(5.5),TRUNC(-5.5) FROM DUAL; => 5,-5`
`SELECT trunc(123.4567,-2) trunc1,trunc(123.4567,2) from dual; => 100,123.45`
- **判断正负[sign]：**  
`SELECT SIGN(10),SIGN(-10),SIGN(0) FROM DUAL;  => 1,-1,0`
- **向上取整[ceil]，向下取整[floor]：**  
`SELECT CEIL(12.34) FROM DUAL;  => 13`
`SELECT FLOOR(12.34) FROM DUAL;  => 12`
- **余数[mod]：**  
`SELECT MOD(5,2),MOD(5,5),MOD(2,5) FROM DUAL;  => 1,0,2`
- **返回根[sqrt]：**  
`SELECT SQRT(9) FROM DUAL; => 3`
- **增加/减去月份[PERIOD_ADD]/返回两个月份差值[PERIOD_DIFF]：**  
`SELECT PERIOD_ADD('201801',2) from dual; => 201803`
`SELECT PERIOD_ADD('201801',-2) from dual; => 201711`
`SELECT PERIOD_DIFF('201801','201805') from dual; => -4`
- **返回当前时间[NOW()]：**  
`SELECT NOW() from dual;  => 2018-07-26 20:13:44`
- **返回日期最后一天[last_day]：**  
`SELECT last_day(now()) from dual;  => 当前月最后一天：2018-07-31`
- **时间转换为字符串[date_format]：**  
`select date_format(now(), '%Y%m%d') from dual;  => 20180102`
- **字符串转换为日期格式[str_to_date]：**  
`SELECT str_to_date('20171226','%Y%m%d') from dual; => 2017-12-26`
`SELECT CAST("2017-08-29" AS DATE); => 2017-08-29`
- **将字符转换为数字[to_number]：**  
`SELECT '2018'+0 from dual; => 2018`
`SELECT CONVERT('2018',SIGNED); => 2018`
- **格式化数字[FORMAT(x,n)],将 x 保留到小数点后 n 位，最后一位四舍五入：**  
`SELECT FORMAT(250500.5634, 2) from dual; => 250,500.56`
- **排序[order by]：**  
`SELECT age,height FROM tableName ORDER BY age,height DESC; => age升序height降序`
- **分组[group by]，分组条件[having]：**  
`SELECT deptno,avg(sal) FROM tableNmae GROUP BY deptno HAVING avg(sal)>1000;  => 查询平均工资大于1000的部门编号和平均工资`
- **返回指定字符的ASCII值[ascii]，返回指定数字对应字符[char]：**  
`SELECT ascii('A'), ascii('a'), char(66) from dual; =>65,97,B`
- **返回指定字符的位置[LOCATE、instr、find_in_set]：**  
`SELECT LOCATE('d','abcdefgd',5); =>8 (第5位后 d 的位置)`
`SELECT instr('abcdefgd','d') from dual; =>4`
`SELECT find_in_set('d','a,b,c,d,e,f,g,d'); =>4`
- **字符串翻转[REVERSE ]：**  
`SELECT REVERSE('abcdefgd'); =>dgfedcba`
- **case when:**  
`SELECT CASE WHEN sex = '1' THEN '男' WHEN sex = '2' THEN '女' ELSE '其他' END from tableName;`
- **ifnull<=>oracle的nvl:**  
`SELECT ifnull('a','b') from dual; => a不为null返回a,a为null返回b`
- **连接:**  
`SELECT concat('a','b','c') from dual; => abc `

[返回目录](#目录)

# mysql使用
## 连接mysql
`mysql -h localhost -P 3306 -u root -p`
- 查看当前用户：`whoami`
- 断开连接：`exit;` 或者`Ctrl+D`
- 撤销命令：`\c` 或者`Ctrl+C`

[返回目录](#目录)
## 数据定义语言DDL
操作数据库和表
### 库操作
- 创建数据库：`create database company`,名称包含特殊字符：`create database \`my.contacts\``
- 切换数据库：`use company`
- 列出所有库：`show databases;`
- 查看链接到了哪个库：`select database();`

[返回目录](#目录)
### 表操作
- 数据类型：
    - 数字：TINYINT、SMALLINT、MEDIUMINT、INT、BIGINT、BIT
    - 浮点数：DECIMAL、FLOAT、DOUBLE
    - 字符串：CHAR、VARCHAR、BINARY、VARBINARY、BLOB、TEXT、ENUM、SET
    - JSON数据类型
- 创建表：
```mysql
CREATE TABLE IF NOT EXISTS `company`.`customers` (
  `id` int unsigned AUTO_INCREMENT,
  `first_name` varchar(20) DEFAULT NULL COMMENT '名',
  `last_name` varchar(20) DEFAULT NULL COMMENT '姓',
  `country` varchar(20) DEFAULT NULL COMMENT '国家',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='用户表';
```
- 查看所有存储引擎：`show engines\G`
- 列出所有表：`show tables;`
- 查看表结构：`show create table customers\G` 或者：`desc customers;`
- 克隆表结构：`create table new_customers like customers;`

[返回目录](#目录)
## 数据操作语言DML
insert、update、delete、select表数据操作
### 插入
```mysql
insert ignore into `company`.`customers`(first_name,last_name,country)
values
{'mike','Christensen','USA'},
{'Andy','Hollands','Australia'},
{'Ravi','Vedantam','India'},
{'Rajiv','Perera','Sri Lanka'};
```
- ignore：如果该行已经存在，并给出了ignore子句，则新数据将被忽略。
- 处理重复项：
    - `replace`：行存在则删除行并插入新行，行不存在则replace<=>insert。
    `replace into customers values {1,'mike','Christensen','Australia'};`
    - 使用`on duplicate key update`：行已存在，并且主键重复，则更新已有行。
    `insert into customers values {1,'mike','Christensen','India'} on duplicate key update country=country+values(country);`

[返回目录](#目录)
### 修改
```mysql
update customers set first_name='Rajiv',last_name='UK' where id=4;
```

[返回目录](#目录)
### 删除
```mysql
delete from customers where id=4 and first_name='Rajiv';
```
- 删除表的所有行最快方法是使用`truncating table`，该操作属于DDL操作，一旦数据清空就不能回滚了。
    - `truncating table customers;`

[返回目录](#目录)
### 查询
- 查询表`departments`所有数据：`select * from departments;`
- 查询表`employees`员工数量：`select count(*) from employees;`
- 查询表`employees`中first_name为a，且last_name为b的员工emp_no：`select emp_no from employees where first_name='a' and last_name='b';`
- **in:** 找出姓氏为a、b、c的所有员工数：`select count(*) from employees where last_name in ('a','b','c');`
- **between...and:** 找出2000年12月入职的所有员工数：`select count(*) from employees where hire_date between '2000-12-01' and '2000-12-31';`
- **not:** 找出不是在2000年12月入职的所有员工数：`select count(*) from employees where hire_date not between '2000-12-01' and '2000-12-31';`
- **%:** 模糊匹配，找出名字以a开头的员工数：`select count(*) from employees where first_name like 'a%';`
    - 找出名字以a开头c结尾的员工数：`select count(*) from employees where first_name like 'a%c';`
    - 找出名字包含b的员工数：`select count(*) from employees where first_name like '%b%';`
- **_:** 精准匹配一个字符，找出名字以任意两个字符开头、后跟随ab、后再跟随任意字符的员工数：`select count(*) from employees where first_name like '__ab%';`
- **正则：**
    ```mysql
    *：0次或多次重复
    +：1次或多次重复
    ?：可选字符
    .：任何字符
    \.：区间
    ^：以...开始
    $:以...结束
    [abc]:只有a、b、c
    [^abc]:不包含a、b、c
    [a-z]:字符a-z
    [0-9]:数字0-9
    \d:任何数字
    \D:任何非数字字符
    \s:任何空格
    \S:任何非空白字符
    \w:任何字母或数字
    \W:任何非字母和数字
    {m}:m次重复
    {m,n}:m-n次重复
    ```
    - 找出名字以a开头的所有员工数：`select count(*) from employees where first_name RLIKE '^a';`
    - 找出名字以bc结尾的所有员工数：`select count(*) from employees where first_name REGEXP 'bc$';`
    - 找出名字不包含abc的所有员工数：`select count(*) from employees where first_name NOT REGEXP '[abc]';`
- **limit:** 找出在2000年之前入职的任意10个员工：`select first_name,last_name from employees where hire_date < '2000-01-01' limit 10;`
- **order by:** 找出薪水最高的前5个员工：`select emp_no,salary from salaries order by salary desc limit 5;` `desc,asc`
- **group by:** 找出男、女员工数：`select gender,count(*) as count from employees group by gender;`
    - 找出最常见5个名字及个数：`select first_name,count(first_name) as count from employees group by first_name order by count desc limit 5;`
- **sum:** 找出每年给员工薪水总额，并按薪水高低倒序：`select YEAR(from_date),SUM(salary) as sum from salaries group by YEAR(from_date) order by sum desc;`
- **average:** 找出平均工资最高的10个员工：`select emp_no,AVG(salary) as avg from salaries group by emp_no order by avg desc limit 10;`
- **having:** 找出平均工资超过10000的员工：`select emp_no,AVG(salary) as avg from salaries group by emp_no having avg>100000 order by avg desc;`
- **distinct:** 找出所有title，去重：`select distinct title from titles;`
- **表关联**
    - 部门表：`departments`，员工表：`employees`，员工-部门映射表：`dept_manager`
    - **join：** 找到员工号为110022的姓名和部门编码：
        ```mysql
        select 
            emp.emp_no,emp.first_name,emp.last_name,dept.dept_name 
        from 
            employees as emp
        join dept_manager as dept_mgr on emp.emp_no=dept_mgr.emp_no and emp.emp_no=110022
        join departments as dept on dept_mgr.dept_no=dept.dept_no;
        ```
    - 找出每个部门的平均工资：
        ```mysql
        select 
          dept_name,AVG(s.salary) as avg_salary
        from 
          salaries as s
        join dept_emp as de on s.emp_no=de.emp_no
        join departments as dept on de.dept_no=dept.dept_no
        group by de.dept_no
        order by avg_salary
        desc;
        ```
- **子查询：**
    - 找出工资最高的员工：
        ```mysql
        select emp_no from salaries where salary=(select MAX(salary) from salaries);
        ```
    - 找出表list1有，表list2没有的员工：
        ```mysql
        select * from employees_list1 where emp_no not in(select emp_no from employees_list2);
        #或者
        select l1.* from employees_list1 as l1 
        left join employees_list2 l2 on l1.emp_no=l2.emp_no
        where l2.emp_no is null;
        ```

[返回目录](#目录)
## 存储过程

[返回目录](#目录)
## 函数

[返回目录](#目录)
## 游标

[返回目录](#目录)
## 触发器

[返回目录](#目录)
## 视图

[返回目录](#目录)
## 事件

[返回目录](#目录)

# 事务
## 事务处理

[返回目录](#目录)
## 锁

[返回目录](#目录)

# 备份与恢复
## 备份

[返回目录](#目录)
## 恢复

[返回目录](#目录)

# 二进制日志

[返回目录](#目录)

# 复制

[返回目录](#目录)

# 性能优化

[返回目录](#目录)
