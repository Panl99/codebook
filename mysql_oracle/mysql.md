> [MySQL 8 Cookbook]()

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
    - [优化顺序](#优化顺序)
    - [存储引擎](#存储引擎)
    - [explain](#explain)
    - [索引](#索引)
        - [什么时候使用索引](#什么时候使用索引)
        - [使用索引注意](#使用索引注意)
        - [添加索引](#添加索引)
        - [删除索引](#删除索引)
    - [分析慢查询](#分析慢查询)
        - [使用pt-query-digest分析慢查询](#使用pt-query-digest分析慢查询)
    - [优化数据类型](#优化数据类型)
    - [分库分表](#分库分表)
        - [分表](#分表)
        - [分库](#分库)
        - [分库分表中间件](#分库分表中间件)
        
- [阿里MySQL规约](#阿里MySQL规约)
    - [阿里建表规约](#阿里建表规约)
    - [阿里索引规约](#阿里索引规约)
    - [阿里SQL规约](#阿里SQL规约)
    - [阿里ORM规约](#阿里ORM规约)

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
- 创建数据库：`create database company`
    - 名称包含特殊字符：```create database `my.contacts` ```
- 切换数据库：`use company`
- 列出所有库：`show databases;`
- 查看链接到了哪个库：`select database();`

[返回目录](#目录)

### 表操作
- 数据类型：
    - 数字：`TINYINT、SMALLINT、MEDIUMINT、INT、BIGINT、BIT`
    - 浮点数：`DECIMAL、FLOAT、DOUBLE`
    - 字符串：`CHAR、VARCHAR、BINARY、VARBINARY、BLOB、TEXT、ENUM、SET`
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

### 阿里建表规约
1. 【强制】表达是与否概念的字段，必须使用is_xxx的方式命名，数据类型是unsigned tinyint（ 1表示是，0表示否），此规则同样适用于odps建表。 
    - 说明：任何字段如果为非负数，必须是unsigned。
2. 【强制】表名、字段名必须使用小写字母或数字；禁止出现数字开头，禁止两个下划线中间只出现数字。
    - 数据库字段名的修改代价很大，因为无法进行预发布，所以字段名称需要慎重考虑。 
    - 正例：getter_admin，task_config，level3_name 
    - 反例：GetterAdmin，taskConfig，level_3_name
3. 【强制】表名不使用复数名词。 
    - 说明：表名应该仅仅表示表里面的实体内容，不应该表示实体数量，对应于DO类名也是单数形式，符合表达习惯。
4. 【强制】禁用保留字，如desc、range、match、delayed等，请参考MySQL官方保留字。
5. 【强制】唯一索引名为uk_字段名；普通索引名则为idx_字段名。 
    - 说明：uk_ 即 unique key；idx_ 即index的简称。
6. 【强制】小数类型为decimal，禁止使用float和double。 
    - 说明：float和double在存储的时候，存在精度损失的问题，很可能在值的比较时，得到不正确的结果。如果存储的数据范围超过decimal的范围，建议将数据拆成整数和小数分开存储。
7. 【强制】如果存储的字符串长度几乎相等，使用char定长字符串类型。
8. 【强制】varchar是可变长字符串，不预先分配存储空间，长度不要超过5000，如果存储长度大于此值，定义字段类型为text，独立出来一张表，用主键来对应，避免影响其它字段索引效率。
9. 【强制】表必备三字段：id, gmt_create, gmt_modified。 
    - 说明：其中id必为主键，类型为unsigned bigint、单表时自增、步长为1。gmt_create, gmt_modified的类型均为date_time类型。
10. 【推荐】表的命名最好是加上“业务名称_表的作用”。 
    - 正例：tiger_task / tiger_reader / mpp_config
11. 【推荐】库名与应用名称尽量一致。
12. 【推荐】如果修改字段含义或对字段表示的状态追加时，需要及时更新字段注释。
13. 【推荐】字段允许适当冗余，以提高性能，但是必须考虑数据同步的情况。冗余字段应遵循：
    - 1）不是频繁修改的字段。 
    - 2）不是varchar超长字段，更不能是text字段。 
    - 正例：商品类目名称使用频率高，字段长度短，名称基本一成不变，可在相关联的表中冗余存储类目名称，避免关联查询。
14. 【推荐】单表行数超过500万行或者单表容量超过2GB，才推荐进行分库分表。 
    - 说明：如果预计三年后的数据量根本达不到这个级别，请不要在创建表时就分库分表。
15. 【参考】合适的字符存储长度，不但节约数据库表空间、节约索引存储，更重要的是提升检索速度。 
    - 正例：人的年龄用unsigned tinyint（表示范围0-255，人的寿命不会超过255岁）；海龟就必须是smallint，但如果是太阳的年龄，就必须是int；如果是所有恒星的年龄都加起来，那么就必须使用bigint。

[返回目录](#目录)

## 数据操作语言DML
- `insert、update、delete、select`表数据操作

### 插入
```mysql
insert ignore into `company`.`customers`(first_name,last_name,country)
values
{'mike','Christensen','USA'},
{'Andy','Hollands','Australia'},
{'Ravi','Vedantam','India'},
{'Rajiv','Perera','Sri Lanka'};
```
- `ignore`：如果该行已经存在，并给出了`ignore`子句，则新数据将被忽略。
- 处理重复项：
    - `replace`：行存在则删除行并插入新行，行不存在则replace<=>insert。
        - `replace into customers values {1,'mike','Christensen','Australia'};`
    - 使用`on duplicate key update`：行已存在，并且主键重复，则更新已有行。
        - `insert into customers values {1,'mike','Christensen','India'} on duplicate key update country=country+values(country);`

- **全部插入**
    ```mysql
    insert into `t_dept_manager`(emp_no,emp_name,emp_status) (select emp_no,emp_name,'on' from t_departments)
    ```

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

|符号|说明|
|---|---|
| * | 0次或多次重复 |
| + | 1次或多次重复 |
| ? | 可选字符 |
| . | 任何字符 |
| \. | 区间 |
| ^ | 以...开始 |
| $ | 以...结束 |
| [abc] | 只有a、b、c |
| [^abc] | 不包含a、b、c |
| [a-z] | 字符a-z |
| [0-9] | 数字0-9 |
| \d | 任何数字 |
| \D | 任何非数字字符 |
| \s | 任何空格 |
| \S | 任何非空白字符 |
| \w | 任何字母或数字 |
| \W | 任何非字母和数字 |
| {m} | m次重复 |
| {m,n} | m-n次重复 |
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

### 阿里SQL规约
1. 【强制】不要使用count(列名)或count(常量)来替代count(*)。
    - count(*)就是SQL92定义的标准统计行数的语法，跟数据库无关，跟NULL和非NULL无关。 
    - 说明：count(*)会统计值为NULL的行，而count(列名)不会统计此列为NULL值的行。
2. 【强制】count(distinct col) 计算该列除NULL之外的不重复数量。
    - 注意 count(distinct col1, col2) 如果其中一列全为NULL，那么即使另一列有不同的值，也返回为0。
3. 【强制】当某一列的值全是NULL时，count(col)的返回结果为0，但sum(col)的返回结果为NULL，因此使用sum()时需注意NPE问题。 
    - 正例：可以使用如下方式来避免sum的NPE问题：`SELECT IF(ISNULL(SUM(g)),0,SUM(g)) FROM table;`
4. 【强制】使用ISNULL()来判断是否为NULL值。
    - 注意：NULL与任何值的直接比较都为NULL。 
    - 说明： 
        - 1） NULL<>NULL的返回结果是NULL，而不是false。
        - 2） NULL=NULL的返回结果是NULL，而不是true。 
        - 3） NULL<>1的返回结果是NULL，而不是true。
5. 【强制】 在代码中写分页查询逻辑时，若count为0应直接返回，避免执行后面的分页语句。
6. 【强制】不得使用外键与级联，一切外键概念必须在应用层解决。
    - 说明：（概念解释）学生表中的student_id是主键，那么成绩表中的student_id则为外键。如果更新学生表中的student_id，同时触发成绩表中的student_id更新，则为级联更新。外键与级联更新适用于单机低并发，不适合分布式、高并发集群；级联更新是强阻塞，存在数据库更新风暴的风险；外键影响数据库的插入速度。
7. 【强制】禁止使用存储过程，存储过程难以调试和扩展，更没有移植性。
8. 【强制】数据订正时，删除和修改记录时，要先select，避免出现误删除，确认无误才能执行更新语句。
9. 【推荐】in操作能避免则避免，若实在避免不了，需要仔细评估in后边的集合元素数量，控制在1000个之内。
10. 【参考】如果有全球化需要，所有的字符存储与表示，均以utf-8编码，那么字符计数方法注意： 
    - 说明： 
        - SELECT LENGTH("轻松工作")； 返回为12 
        - SELECT CHARACTER_LENGTH("轻松工作")； 返回为4 
        - 如果要使用表情，那么使用utfmb4来进行存储，注意它与utf-8编码的区别。
11. 【参考】 TRUNCATE TABLE 比 DELETE 速度快，且使用的系统和事务日志资源少，但TRUNCATE无事务且不触发trigger，有可能造成事故，故不建议在开发代码中使用此语句。 
    - 说明：TRUNCATE TABLE 在功能上与不带 WHERE 子句的 DELETE 语句相同。

[返回目录](#目录)

## 存储过程
- 示例：添加新员工：
    ```mysql
    drop procedure if exists create_employee;
    delimiter $$
    create procedure create_employee(OUT new_emp_no INT, IN first_name varchar(20), IN last_name varchar(20), IN gender enum('M','F'), IN birth_date date, IN emp_dept_name varchar(40), IN title varchar(50))
    BEGIN 
        declare emp_dept_no char(4);
        declare salary int default 60000;
    
        select max(emp_no) into new_emp_no from employees;
        set new_emp_no = new_emp_no + 1;
        insert into employees values(new_emp_no, birth_date,first_name,last_name,gender,CURDATE());
        
        select emp_dept_name;
        select dept_no into emp_dept_no from departments where dept_name = emp_dept_name;
        select emp_dept_no;
    
        insert into dept_emp values(new_emp_no,emp_dept_no,CURDATE(),'9999-01-01');
        insert into titles values(new_emp_no,title,CURDATE(),'9999-01-01');
    
        if title = 'Staff' then
            set salary = 100000;
        elseif title = 'Senior Staff' then
            set salary = 120000;
        end if;
    
        insert into salaries values(new_emp_no, salary,CURDATE(),'9999-01-01');
    END $$
    delimiter ;
    
    -- 调用
    call create_employee(@new_emp_no,'John','Smith','M','1984-06-19','Research','Staff');
    -- 查询存储在@new_emp_no变量中的emp_no值
    select @new_emp_no;
    ```

[返回目录](#目录)

## 函数
- 类似存储过程，区别：函数应该有返回值，并且可以在select中调用函数。通常创建函数是为了简化复杂的计算。
- 示例：根据客户收入水平给出信用卡额度：
    ```mysql
    drop function if exists get_sal_level;
    delimiter $$
    create function get_sal_level(emp int) returns varchar(10)
    deterministic 
    BEGIN 
        declare sal_level varchar(10);
        declare avg_sal float;
        
        select avg(salary) into avg_sal from salaries where emp_no=emp;
        
        if avg_sal < 50000 then
            set sal_level = 'BRONZE';
        elseif (avg_sal >= 50000 and avg_sal < 70000) then
            set sal_level = 'SILVER';
        elseif (avg_sal >= 70000 and avg_sal < 90000) then
            set sal_level = 'GOLD';
        elseif (avg_sal >= 90000) then
            set sal_level = 'PLANTINUM';
        else 
            set sal_level = 'NOT FOUND';
        end if;
        
        return (sal_level);
    END $$
    delimiter ;
    
    -- 调用
    select get_sal_level(10002);
    ```

[返回目录](#目录)

## 游标
- 游标对少量数据可以(千百条)，大量数据(万级以上)避免使用游标，速度慢或可能报错，可以使用临时表替代。
- 示例：遍历表数据：
    ```mysql
    drop procedure if exists get_info;
    delimiter $$
    create procedure get_info(IN start_time INT, IN end_time INT)
    BEGIN 
        declare t_id int default '';
        declare t_name varchar(64) default ''; 
        declare status int default 0;
  
        declare cur cursor for select id,name from t_result_info where hire_date between start_time and end_time;
        declare continue handler for sqlstate '02000' set status = 1;
          
        open cur;
        label:LOOP 
          fetch cur into t_id,t_name;
          if status = 1 then
              leave label;
          end if;
          
        end LOOP;
        close cur;
    END $$
    delimiter ;
    
    -- 调用
    call get_info(1984,1988);
    ```
- 使用**临时表**代替游标
    ```mysql
    drop procedure if exists get_info;
    delimiter $$
    create procedure get_info(IN start_time INT, IN end_time INT)
    BEGIN 
        declare t_id varchar(16) default '';
        declare t_name varchar(64) default ''; 
        declare count int default 0;
        declare i int default 1;
          
        drop table if exists t_result_tmp;
        CREATE TEMPORARY TABLE IF NOT EXISTS t_result_tmp (
          `tmp_id` int NOT NULL,
          `tmp_name` varchar(64) NOT NULL,
          PRIMARY KEY (`tmp_id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
        
        set @sqlstmt = concat('insert into t_result_tmp(`tmp_id`,`tmp_name`) (select id,name from t_result_info where hire_date between ',start_time ,' and ', end_time, ');');
        prepare stmt from @sqlstmt;
        execute stmt;
        deallocate prepare stmt;
  
        select min(`tmp_id`) into i from t_result_tmp;
        select max(`tmp_id`) into count from t_result_tmp;
  
        while i <= count do
          select tmp_id,tmp_name into t_id,t_name from t_result_tmp where tmp_id = i;
          set i = i + 1;
        end while;
    END $$
    delimiter ;
    
    -- 调用
    call get_info(1984,1988);
    ```

[返回目录](#目录)

## 触发器
- 用于在触发事件之前或者之后处理某些内容。
- 触发时间可以是：`before`、`after`
- 触发事件可以是：`insert（包含：insert、replace、load data）`、`delete（包含：delete、replace）`、`update`
- 从5.7版本开始，一个表可以有多个触发器。但要使用follows或precedes指定先行的触发器。
- 创建一个触发器：将薪水插入salaries表之前对其进行四舍五入。NEW指的是插入的新值。
    ```mysql
    drop trigger if exists salary_round;
    delimiter $$
    create trigger salary_round before insert on salaries
    for each row 
    begin 
        set NEW.salary = round(NEW.salary);
    end $$
    delimiter ;
    ```

[返回目录](#目录)

## 视图
- 视图是一个基于SQL语句的结果集的虚拟表。
- 可以使用视图来限制用户对特定行的访问。
- 示例：只提供表salaries的emp_no列和salary列，且from_date在2002-01-01之后的数据访问权限：
    ```mysql
    create ALGORITHM = UNDEFINED 
    DEFINER = `root`@`localhost`
    sql security definer view salary_view
    as
    select emp_no,salary from salaries where from_date > '2002-01-01';
    
    -- 查询视图
    select emp_no,avg(salary) as avg from salary_view group by emp_no order by avg desc limit 5;
    ```

[返回目录](#目录)

## 事件
- MySQL使用事件调度线程来执行所有预定事件。低于8.0.3版本默认未启用。
- 启用事件调度线程：`SET GLOBAL event_scheduler = ON;`
- 查看事件：`SHOW EVENTS\G`
- 查看事件定义：`SHOW CREATE EVENT purge_salary_audit\G`
- 禁用事件：`ALTER EVENT purge_salary_audit DISABLE;`
- 启用事件：`ALTER EVENT purge_salary_audit ENABLE;`
- 创建一个事件：每日运行，删除一个月前的薪水审计记录。
    ```mysql
    drop event if exists purge_salary_audit;
    delimiter $$
    create event if not exists purge_salary_audit
    on schedule 
        every 1 week 
        starts current_date
        do begin 
            delete from salary_audit where date_modified < date_add(curdate(), interval -7 day );
        end $$
    delimiter ;
    ```

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
## 优化顺序
1. **优化sql 和 建索引**
2. **添加缓存：redis**
3. **做主从复制或主主复制，读写分离**
    - 可以在应用层做，效率高，也可以用三方工具，第三方工具推荐360的atlas,其它的要么效率不高，要么没人维护。
4. **先试试mysql自带分区表**
    - 对应用是透明的，无需更改代码,但是sql语句是需要针对分区表做优化的，sql条件中要带上分区条件的列，从而使查询定位到少量的分区上，否则就会扫描全部分区
5. **先做垂直拆分**
    - 其实就是根据你模块的耦合度，将一个大的系统分为多个小的系统，也就是分布式系统；
6. **水平切分**
    - 针对数据量大的表
    - 这一步最麻烦，最能考验技术水平，要选择一个合理的sharding key,为了有好的查询效率，表结构也要改动，做一定的冗余，应用也要改，sql中尽量带sharding key，将数据定位到限定的表上去查，而不是扫描全部的表；
7. 不同引擎的优化
    - myisam 读的效果好，写的效率差，这和它数据存储格式、索引的指针、锁的策略有关的，
        - 它的数据是顺序存储的（innodb数据存储方式是聚簇索引）
        - 它的索引btree上的节点是一个指向数据物理位置的指针，所以查找起来很快，（innodb索引节点存的则是数据的主键，所以需要根据主键二次查找）；
        - myisam锁是表锁，只有读读之间是并发的，写写之间和读写之间（读和插入之间是可以并发的，去设置concurrent_insert参数，定期执行表优化操作，更新操作就没有办法了）是串行的，所以写起来慢，并且默认的写优先级比读优先级高，高到写操作来了后，可以马上插入到读操作前面去，如果批量写，会导致读请求饿死，所以要设置读写优先级或设置多少写操作后执行读操作的策略;
        - myisam不要使用查询时间太长的sql，如果策略使用不当，也会导致写饿死，所以尽量去拆分查询效率低的sql。
    - innodb一般都是行锁，这个一般指的是sql用到索引的时候，行锁是加在索引上的，不是加在数据记录上的，如果sql没有用到索引，仍然会锁定表。
        - mysql的读写之间是可以并发的，普通的select是不需要锁的，当查询的记录遇到锁时，用的是一致性的非锁定快照读，也就是根据数据库隔离级别策略，会去读被锁定行的快照，其它更新或加锁读语句用的是当前读，读取原始行；
        - 因为普通读与写不冲突，所以innodb不会出现读写饿死的情况，又因为在使用索引的时候用的是行锁，锁的粒度小，竞争相同锁的情况就少，就增加了并发处理，所以并发读写的效率还是很优秀的，问题在于索引查询后的根据主键的二次查找导致效率低；
        - 所以innodb为了避免二次查找可以使用索引覆盖技术，无法使用索引覆盖的，再延伸一下就是基于索引覆盖实现延迟关联；

> [https://www.zhihu.com/question/19719997](https://www.zhihu.com/question/19719997)  
> [MySQL千万级大表优化解决方案](https://www.cnblogs.com/yliucnblogs/p/10096530.html)

[返回目录](#目录)

## 存储引擎
### InnoDB
- 优点：
    - 支持事务、回滚、崩溃修复能力、多版本并发控制、事务安全的操作。
    - 底层存储结构使用B+树：
        - B+树的每个节点都对应InnoDB的一个Page，Page大小是固定的，一般被设为16KB。
        - 非叶子结点只存储索引，叶子结点存储完整的数据（索引+数据）。
- InnoDB适用场景：
    - 经常有数据更新的表，适合处理多重并发更新请求。
    - 支持事务。
    - 支持灾难恢复（通过bin-log日志等）。
    - 支持外键约束，只有InnoDB支持外键。
    - 支持自动增加列属性auto_increment。

### MyISAM
- 缺点：
    - 不支持事务、行级锁和外键，因此在写操作（插入、更新）时需要锁定整个表，效率较低。
- 优点：
    - 查询速度快，且占用的内存和存储资源较少。
        - 它是假设数据被组织成固定长度的记录，并且是按顺序存储的。在查找数据时，MyIASM直接查找文件的OFFSET，定位比InnoDB要快（InnoDB寻址时要先映射到块，再映射到行）。

[返回目录](#目录)

## explain
- 关注type、rows、filtered、Extra。
```
1. id //select查询的序列号，包含一组数字，表示查询中执行select子句或操作表的顺序
2. select_type //查询类型
3. table //正在访问哪个表
4. partitions //匹配的分区
5. type //访问的类型
6. possible_keys //显示可能应用在这张表中的索引，一个或多个，但不一定实际使用到
7. key //实际使用到的索引，如果为NULL，则没有使用索引
8. key_len //表示索引中使用的字节数，可通过该列计算查询中使用的索引的长度
9. ref //显示索引的哪一列被使用了，如果可能的话，是一个常数，哪些列或常量被用于查找索引列上的值
10. rows //根据表统计信息及索引选用情况，大致估算出找到所需的记录所需读取的行数
11. filtered //查询的表行占表的百分比
12. Extra //包含不适合在其它列中显示但十分重要的额外信息 
```

[explain](https://segmentfault.com/a/1190000021458117?utm_source=tag-newest)

### 使用explain查看执行计划
- 以json格式显示
```mysql
explain format=json select dept_name from dept_tmp 
join employees on dept_emp.emp_no=employees.emp_no
join departments on departments.dept_no=dept_emp.dept_no
where employees.first_name='Aamer';
```
- 使用explain连接正在进行的会话，需要指定connection ID。
    - 获取connection ID：`select CONNECTION_ID();`
    - 连接：`explain format=json for CONNECTION 778;`

### Type
- 自上到下，效率由低到高。

|type|说明|
|---|---|
| ALL | 全表扫描 |
| index | 索引全扫描 |
| range | 索引范围扫描，常用语<,<=,>=,between,in等操作 |
| ref | 指的是使用普通的索引（normal index），使用非唯一索引扫描或唯一索引前缀扫描，返回单条记录，常出现在关联查询中 |
| eq_ref | 类似ref，区别在于使用的是唯一索引，使用主键的关联查询 |
| consts/system | 单表中最多只有一个匹配行，系统会把匹配行中的其他列作为常数处理，在优化阶段即可读取到数据，如主键或唯一索引查询 |
| null | MySQL不访问任何表或索引，直接返回结果 |

### Extra
|extra|说明|
|---|---|
| Using filesort | MySQL需要额外的一次传递，以找出如何按排序顺序检索行。通过根据联接类型浏览所有行并为所有匹配WHERE子句的行保存排序关键字和行的指针来完成排序。然后关键字被排序，并按排序顺序检索行。 |
| Using temporary | 使用了临时表保存中间结果，性能特别差，需要重点优化 |
| Using index | 表示相应的 select 操作中使用了覆盖索引（Coveing Index）,避免访问了表的数据行，效率不错！如果同时出现 using where，意味着无法直接通过索引查找来查询到符合条件的数据。 |
| Using index condition | MySQL5.6之后新增的ICP，using index condtion就是使用了ICP（索引下推），在存储引擎层进行数据过滤，而不是在服务层过滤，利用索引现有的数据减少回表的数据。 |

[返回目录](#目录)

## 索引
### 什么时候使用索引 
- 经常出现在`group by`、`order by`、`distinc`关键字后面的字段 
- 经常与其他表进行连接的表，在连接字段上应该建立索引 
- 经常出现在 Where 子句中的字段 
- 经常出现用作查询选择的字段

### 使用索引注意
- B-Tree的最左前缀匹配特性，包含多个索引字段时，查询条件要携带最左字段，否则无法使用索引
- 在索引上，避免使用NOT、!=、<>、!<、!>、NOT EXISTS、NOT IN、NOT LIKE等
### 阿里索引规约
1. 【强制】业务上具有唯一特性的字段，即使是组合字段，也必须建成唯一索引。（唯一索引影响的insert速度损耗可以忽略，但提升了查询速度、杜绝脏数据产生）
2. 【强制】超过三个表禁止join。需要join的字段，数据类型保持绝对一致；多表关联查询时，保证被关联的字段需要有索引。
3. 【强制】在varchar字段上建立索引时，必须指定索引长度，没必要对全字段建立索引，根据实际文本区分度决定索引长度。（区分度计算：`count(distinct left(列名, 索引长度))/count(*)`）
4. 【强制】页面搜索严禁左模糊或者全模糊，如果需要请走搜索引擎来解决。(索引文件具有B-Tree的最左前缀匹配特性，如果左边的值未确定，那么无法使用此索引。)
5. 【推荐】如果有order by的场景，请注意利用索引的有序性。order by 最后的字段是组合索引的一部分，并且放在索引组合顺序的最后，避免出现file_sort的情况，影响查询性能。 
    - 正例：where a=? and b=? order by c; 索引：a_b_c 
    - 反例：索引中有范围查找，那么索引有序性无法利用，如：WHERE a>10 ORDER BY b; 索引a_b无法排序。
6. 【推荐】利用覆盖索引来进行查询操作，来避免回表操作。
    - 如果一本书需要知道第11章是什么标题，会翻开第11章对应的那一页吗？目录浏览一下就好，这个目录就是起到覆盖索引的作用。
7. 【推荐】利用延迟关联或者子查询优化超多分页场景。
    - 正例：先快速定位需要获取的id段，然后再关联：`SELECT a.* FROM 表1 a, (select id from 表1 where 条件 LIMIT 100000,20 ) b where a.id=b.id`
8. 【推荐】SQL性能优化的目标：至少要达到 range 级别，要求是ref级别，如果可以是consts最好。
9. 【推荐】建组合索引的时候，区分度最高的在最左边。
    - 说明：存在非等号和等号混合判断条件时，在建索引时，请把等号条件的列前置。如：`where a>? and b=?` 那么即使a的区分度更高，也必须把b放在索引的最前列。
10. 【参考】创建索引时避免有如下极端误解： 
    - 1）误认为一个查询就需要建一个索引。
    - 2）误认为索引会消耗空间、严重拖慢更新和新增速度。 
    - 3）误认为唯一索引一律需要在应用层通过“先查后插”方式解决。

### 添加索引
- 给last_name添加索引：`alter table employees add index index_last_name (last_name);`
- **唯一索引**：`alter table employees add unique index unique_index_name (last_name,first_name);`
- **前缀索引**：列的前部分非整列的索引：`alter table employees add index index_last_name (last_name(10));`
- 删除索引：`alter table employees drop index last_name;`
- 生成列的索引：
    - 对于封装在函数中的列不能使用索引。
        - 如果给hire_date添加索引：`alter table employees add index index_hire_date (hire_date);`
        - hire_date上的索引可用于在where子句中带有hire_date的查询。
        - 相反，如果将hire_date放入函数中，MySQL就必须扫描整个表：`explain select count(*) from employees where YEAR(hire_date)>=2000;`
        - 所以，避免将索引列放入函数中。
    - 如果无法避免使用函数，那就创建一个虚拟列，并在虚拟列上添加索引。
        - 创建虚拟列：`alter table employees add hire_date_year YEAR as (YEAR(hire_date)) VIRTUAL, add index index_hire_date_year (hire_date_year);`
        - 现在，查询无须使用YEAR()函数，可以直接在where子句中使用hire_date_year：`explain select count(*) from employees where hire_date_year>=2000;`
        - 此时即使使用YEAR(hire_date)，优化器也会考虑该索引hire_date_year。
- 不可见索引：若想删除索引，可以不立即删除，可以先将其标记为不可见。
    - 将last_name上的索引标记不可见：`alter table employees alter index last_name INVISIBLE;`
    - 将last_name上的索引标记可见：`alter table employees alter index last_name VISIBLE;`
- 降序索引：MySQL 8.0 引入

### 删除索引
- 重复索引
    - 查看表结构：`show create table employees`
- 冗余索引

[返回目录](#目录)

## 分析慢查询

### 通过慢查日志等定位那些执行效率较低的SQL语句

### show profile分析
- 了解SQL执行的线程的状态及消耗的时间。
```mysql
show profiles ;
show profile for query #{id};
```
- show profile默认关闭，开启：`set profiling = 1;`

### 使用pt-query-digest分析慢查询
- pt-query-digest是Percona工具包的一部分，用于对查询进行分析。
- 慢查询日志：
    - 查看、设置`long_query_time`：
        - `SELECT @@GLOBAL.LONG_QUERY_TIME;`
        - `SET @@GLOBAL.LONG_QUERY_TIME=1;`
    - 查看、设置慢查询文件：
        - `SELECT @@GLOBAL.slow_query_log_file;`
        - `SET @@GLOBAL.slow_query_log_file='/var/log/mysql/mysql_slow.log';`
        - `FLUSH LOGS`
    - 查看、启用慢查询日志：
        - `SELECT @@GLOBAL.slow_query_log;`
        - `SET @@GLOBAL.slow_query_log=1;`
    - 验证查询是否被记录：
        - `SELECT SLEEP(2);`
        - `sudo less /var/log/mysql/mysql_slow.log;`
- 分析慢查询日志：`sudo pt-query-digest /var/lib/mysql/ubuntu-slow.log > query_digest`
- 分析通用查询日志：`sudo pt-query-digest --type genlog /var/lib/mysql/db1.log > general_query_digest`
- 进程列表
- 二进制日志
- TCP转储

[返回目录](#目录)

## 优化数据类型
- 优化表，使表在磁盘占用空间减小，好处：
    - 向磁盘写入或读取的数据越少，查询就越快。
    - 在处理查询时，磁盘上的内容会被加载到主内存中。所以，表越小，占主存空间越小。
    - 被索引占用的空间越小。
- 优化：
    - 如果存储员工编号，可能最大值为500000，则最好的数据类型为`MEDIUMINT UNSIGNED`(3个字节)。如果存为`INT`(4个字节)，每行就会浪费1个字节。
    - 如果存储员工名字，可能最大值为20字节，则最好声明为`varchar(20)`。如果是`char(20)`，但只有几个员工名字为20字符，其他的不到10字符，就会浪费10字符的空间。
    - 声明`varchar()`时，应考虑长度。
        - 如果`varchar()`长度超过**255**时，则需要2个字节来存储长度。
    - 避免存储空值，应将列声明为NOT NULL。
    - 如果字符串长度是固定的，则应存储为`char`而不是`varchar`，因为varchar需要1-2个字节存储字符串长度。
    - 如果值是固定的，则应该使用`ENUM`而不是`varchar`，只需要1-2个字节即可。
    - 优先选择使用整数类型，而非字符串类型。
    - 尝试使用前缀索引。
    - 尝试利用InnoDB压缩。

[返回目录](#目录)

## 分库分表

### 分表
- 单表数据量过大时，就得分表了。
- 单表几百万数据的时候，性能就会变差。
- 如何分：
    - 比如：按照用户id分表，一个用户的数据放到一个表中，对一个用户的操作只要操作那张表。这样可以控制每张表的数据量在可控范围（比如单表200万以内）。

### 分库
- 用户过多，数据量、请求量过大，就需要分库。
- 一般，单库最多支撑并发量 2000 QPS（最好在1000左右）。


- 水平拆分：把一个表的数据分到多个库多个表中
    - 数据存放均匀，多个库可以抗住更高的并发，多个库的存储容量扩容。
- 垂直拆分：把一个有多个字段的表拆成多个表 或者库
    - 每个库表结构不一样，包含部分字段，可以将高频字段 和低频字段分开存放。
    

### 分库分表中间件
- [mycat](https://github.com/MyCATApache/Mycat-Server?spm=a2c6h.12873639.0.0.5f355986X8wcNE)
- [sharding-jdbc](https://github.com/apache/incubator-shardingsphere?spm=a2c6h.12873639.0.0.5f355986X8wcNE)

[返回目录](#目录)


# 阿里MySQL规约
## 阿里ORM规约
1. 【强制】在表查询中，一律不要使用 * 作为查询的字段列表，需要哪些字段必须明确写明。 
    - 说明：
        - 1）增加查询分析器解析成本。
        - 2）增减字段容易与resultMap配置不一致。
2. 【强制】POJO类的boolean属性不能加is，而数据库字段必须加is_，要求在resultMap中进行字段与属性之间的映射。 
    - 说明：参见定义POJO类以及数据库字段定义规定，在sql.xml增加映射，是必须的。
3. 【强制】不要用resultClass当返回参数，即使所有类属性名与数据库字段一一对应，也需要定义；反过来，每一个表也必然有一个与之对应。 
    - 说明：配置映射关系，使字段与DO类解耦，方便维护。
4. 【强制】xml配置中参数注意使用：#{}，#param# 不要使用${} 此种方式容易出现SQL注入。
5. 【强制】iBATIS自带的queryForList(String statementName,int start,int size)不推荐使用。 
    - 说明：其实现方式是在数据库取到statementName对应的SQL语句的所有记录，再通过subList取start,size的子集合，线上因为这个原因曾经出现过OOM。
    - 正例：在sqlmap.xml中引入 #start#, #size#
        ```java
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("size", size);
        ```
6. 【强制】不允许直接拿 HashMap 与Hashtable 作为查询结果集的输出。
7. 【强制】更新数据表记录时，必须同时更新记录对应的gmt_modified字段值为当前时间。
8. 【推荐】不要写一个大而全的数据更新接口，传入为POJO类，不管是不是自己的目标更新字段，都进行update table set c1=value1,c2=value2,c3=value3; 这是不对的。执行SQL时，尽量不要更新无改动的字段，一是易出错；二是效率低；三是binlog增加存储。
9. 【参考】@Transactional事务不要滥用。事务会影响数据库的QPS，另外使用事务的地方需要考虑各方面的回滚方案，包括缓存回滚、搜索引擎回滚、消息补偿、统计修正等。
10. 【参考】<isEqual>中的compareValue是与属性值对比的常量，一般是数字，表示相等时带上此条件；
    - <isNotEmpty>表示不为空且不为null时执行；
    - <isNotNull>表示不为null值时执行。

[返回目录](#目录)

