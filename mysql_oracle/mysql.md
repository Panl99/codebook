
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
    - [explain](#explain)
    - [索引](#索引)
        - [添加索引](#添加索引)
        - [删除索引](#删除索引)
    - [使用pt-query-digest分析慢查询](#使用pt-query-digest分析慢查询)
    - [优化数据类型](#优化数据类型)

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
- 游标对对少量数据可以(千百条)，大量数据(万级以上)避免使用游标，速度慢或可能报错，可以使用临时表。
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
- 使用临时表代替游标
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
## explain
- 使用explain计划
```mysql
explain select dept_name from dept_tmp 
join employees on dept_emp.emp_no=employees.emp_no
join departments on departments.dept_no=dept_emp.dept_no
where employees.first_name='Aamer';
```
- 使用explain json，以json格式显示
```mysql
explain format=json select dept_name from dept_tmp 
join employees on dept_emp.emp_no=employees.emp_no
join departments on departments.dept_no=dept_emp.dept_no
where employees.first_name='Aamer';
```
- 使用explain连接正在进行的会话，需要指定connection ID。
    - 获取connection ID：`select CONNECTION_ID();`
    - 连接：`explain format=json for CONNECTION 778;`

[返回目录](#目录)

## 索引
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

## 使用pt-query-digest分析慢查询
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
    - 如果不允许存储空值，则应将列声明为NOT NULL。
    - 如果字符串长度是固定的，则应存储为`char`而不是`varchar`，因为varchar需要1-2个字节存储字符串长度。
    - 如果值是固定的，则应该使用`ENUM`而不是`varchar`，只需要1-2个字节即可。
    - 优先选择使用整数类型，而非字符串类型。
    - 尝试使用前缀索引。
    - 尝试利用InnoDB压缩。

[返回目录](#目录)
