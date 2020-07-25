
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

- **字符首字母大写[initcap]、转换为小写[lower]、转换为大写[upper]：**  
`select initcap('abc'), lower('ABC'), upper('abc') from dual; => Abc,abc,ABC`
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
- **增加/减去月份[add_months]：**  
	`SELECT to_char(add_months(to_date('201801','yyyymm'),2),'yyyymm') from dual; => 201803`
	`SELECT to_char(add_months(to_date('201801','yyyymm'),-2),'yyyymm') from dual; => 201711`
- **返回日期最后一天[last_day]：**  
	`SELECT last_day(sysdate) from dual;  => 当前月最后一天`
- **返回下一个星期的日期[NEXT_DAY]：**  
	`SELECT next_day(sysdate,'星期五') from dual; => 下一个星期五`
- **转换为字符串[to_char]：**  
	`SELECT to_char(sysdate,'yyyymmdd'),to_char((sysdate)+1,'yyyymmdd') from dual;  => 20180102,20180103`
- **转换为日期格式[to_date]：**  
	`SELECT to_date('20171226','yyyy-mm-dd') from dual; => 2017-12-26`
- **将字符转换为数字[to_number]：**  
	`SELECT to_number('2018') year from dual; => 2018`
- **排序[order by]：**  
	`SELECT age,height FROM tableName ORDER BY age,height DESC; => age升序height降序`
- **分组[group by]，分组条件[having]：**  
	`SELECT deptno,avg(sal) FROM tableNmae GROUP BY deptno HAVING avg(sal)>1000;  => 查询平均工资大于1000的部门编号和平均工资`
----------------------------------------
- **返回指定字符的ASCII值[ascii]，返回指定数字对应字符[chr]：**  
	`SELECT ascii('A'), ascii('a'), chr(66) from dual; =>65,97,B`
- **返回指定字符的位置[instr（被搜索字符串、希望搜索的字符串、搜索开始位置、字符出现位置）]：**  
	`SELECT instr('abcdefgd','d',1,2) from dual; =>8 (第二个 d 的位置)`
----------------------------------------
- **decode:**  
	`SELECT decode(5,1,'!',2,'#',3,'*','other') from dual; => other`
- **case when:**  
	`SELECT CASE WHEN sex = '1' THEN '男' WHEN sex = '2' THEN '女' ELSE '其他' END from tableName;`
- **nvl:**  
	`SELECT nvl(a,b) from dual; => a不为null返回a,a为null返回b`
- **连接:**  
	`SELECT concat('a','b') from dual; => ab  只能连两个`
	`SELECT 'a' || 'b' || 'c' from dual; => abc  可以连多个`

[返回目录](#目录)

# mysql使用
## 连接mysql

[返回目录](#目录)
## 数据定义语言DDL
操作数据库和表
### 库操作

[返回目录](#目录)
### 表操作

[返回目录](#目录)
## 数据操作语言DML
insert、update、delete、select表数据操作
### 插入

[返回目录](#目录)
### 修改

[返回目录](#目录)
### 删除

[返回目录](#目录)
### 查询

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
