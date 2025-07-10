# Mybatis高级查询

## 一对一映射`association`
- 实体类
```java
@Data
public class SysUser {
    // 其他属性
    
    /**
     * 用户角色
     */
    private SysRole role;

}
```
- 在`UserMapper.xml`中创建resultMap：
```xml
<resultMap id = "userRoleMap" extends = "userMap"
           type = "mybatis.simple.model.SysUser">
    <association property = "role" columnPrefix = "role_"
                javaType = "mybatis.simple.model.SysRole">
        <id property = "id" column = "id" />
        <result property = "roleName" column = "role_name" />
        <result property = "enabled" column = "enabled" />
        <result property = "createBy" column = "create_by" />
        <result property = "createTime" column = "create_time" jdbcType = "TIMESTAMP" />
    </association>
</resultMap>
```

## association标签的嵌套查询
1. 首先在`RoleMapper.xml`中添加如下方法：
```xml
<select id = "selectRoleById" resultMap = "roleMap">
    select *
    from sys_role
    where id = #{id}
</select>
```
2. 在`UserMapper.xml`中创建resultMap：
```xml
<resultMap id = "userRoleMapSelect" extends = "userMap"
           type = "mybatis.simple.model.SysUser">
    <association property = "role"
                column = "{id = role_id}"
                select = "mybatis.simple.mapper.RoleMapper.selectRoleById"
    />
</resultMap>
```
3. 查询语句：
```xml
<select id = "selectUserAndRoleByIdSelect" resultMap = "userRoleMapSelect">
    select
        u.id,
        u.user_name,
        u.user_password,
        u.user_email,
        u.user_info,
        u.head_img,
        u.create_time,
        ur.role_id
    from sys_user u
    inner join sys_user_role ur on ur.user_id = u.id
    where u.id = #{id}
</select>
```


## 一对多映射`collection`
- 实体类
```java
@Data
public class SysUser {
    // 其他属性
    
    /**
     *用户角色集合
     */
    private List<SysRole> roleList;

}
```
- 在`UserMapper.xml`中创建resultMap：
```xml
<resultMap id = "userRoleListMap" extends = "userMap"
           type = "mybatis.simple.model.SysUser">
    <id property = "id" column = "id" />
    <result property = "userName" column = "user_name " />
    <result property = "userPassword" column = "user_password" />
    <result property = "userEmail" column = "user_email" />
    <result property = "userInfo" column = "user_info" />
    <result property = "headImg" column = "head_img" jdbcType = "BLOB" />
    <result property = "createTime" column = "create_time " jdbcType = "TIMESTAMP" />
  <!--在关联映射时，一对一单关联是JavaBean对象，可以使用javaType；而一对多集合类型需要用ofType-->
    <collection property = "roleList" columnPrefix = "role_"
                ofType = "mybatis.simple.model.SysRole">
        <id property = "id" column = "id" />
        <result property = "roleName" column = "role_name" />
        <result property = "enabled" column = "enabled" />
        <result property = "createBy" column = "create_by" />
        <result property = "createTime" column = "create_time" jdbcType = "TIMESTAMP" />
    </collection>
</resultMap>
```

## collection集合的嵌套查询
1. 首先在`PrivilegeMapper.xml`中添加如下方法：
```xml
<select id = "selectPrivilegeByRoleId" resultMap = "privilegeMap">
    select p.*
    from sys_privilege p
    inner join sys_role_privilege rp on rp.privilege_id = p.id
    where role_id = #{roleId}
</select>
```
2. 在`RoleMapper.xml`中配置映射和对应的查询方法：
```xml
<resultMap id = "rolePrivilegeListMapSelect" extends = "roleMap"
           type = "mybatis.simple.model.SysRole">
    <collection property = "privilegeList"
                fetchType = "lazy"
                column = "{roleId = id}"
                select = "mybatis.simple.mapper.PrivilegeMapper.selectPrivilegeByRoleId"
    />
</resultMap>
```
3. 查询语句：
```xml
<select id = "selectRoleByUserId" resultMap = "rolePrivilegeListMapSelect">
    select
        r.id,
        r.role_name,
        r.enabled,
        r.create_by,
        r.create_time
    from sys_role r
    inner join sys_user_role ur on ur.role_id = r.id
    where ur.user_id = #{userId}
</select>
```

## MyBatis的N+1问题

collection 的两种实现方式：
- `嵌套查询 (Nested Queries / select)`： 在主查询执行完毕后，MyBatis 会为结果集中每一条主记录，根据 collection 标签中指定的 select 属性值（另一个映射语句的 ID），去执行一次子查询来加载关联的集合数据。这就是著名的 N+1 查询问题。
- `嵌套结果 (Nested Results / resultMap)`： 通过编写一个包含连接（JOIN）的 SQL 语句，一次性将主记录及其关联集合的所有数据查询出来。MyBatis 的结果映射(resultMap)会负责将这一大块结果集拆解、组装成主对象包含子对象集合的结构。

在`嵌套查询(select模式)` 方式下，主查询跟子查询会使用**同一个SqlSession**完成查询，不会为每个子查询关闭然后重新打开一个新的 SqlSession 或创建新的数据库连接。

但sql查询语句本身确实被循环执行了，从而导致的 N+1(1次主查询 + N次子查询) 查询问题影响性能。

解决N+1问题：
- **使用嵌套结果 (resultMap 方式)：(推荐)**
   1. 编写一个 SQL 语句，使用 JOIN (如 LEFT JOIN) 将主表（订单）和子表（商品项）连接起来。
   2. 在 resultMap 中定义主记录的映射，并在其中使用 <collection ... resultMap="itemResultMap"> 或者直接在 <collection> 内定义子对象的列映射。MyBatis 会遍历合并后的结果集，自动根据主键将子记录分组到对应的主记录的集合属性中。
   - 优点： 只需要 1 次数据库查询。
   - 缺点： SQL 可能更复杂；结果集可能包含重复的主记录数据（JOIN 的特性），但 MyBatis 能正确处理；如果关联数据量巨大，单次查询结果集本身可能很大。
- **使用懒加载 (Lazy Loading)：**
   1. 在 collection 的 select 模式下，配置 fetchType = "lazy"。
   2. 这样，子查询不会在主查询执行后立即触发。只有在应用程序第一次实际访问主对象的集合属性（如 order.getItems()）时，MyBatis 才会在那个时刻执行子查询。
   - 优点： 避免了加载不需要的数据，特别是当主记录很多但可能不需要立即查看所有关联数据时。可以缓解（但不能根除）N+1 问题的影响，因为它推迟了子查询的执行，并且可能只在需要时才加载部分主记录的关联数据。
   - 缺点： 如果最终确实需要访问所有（或很多）主记录的关联数据，N+1 问题依然存在，只是发生的时间点延迟了。可能导致“懒加载异常”（如果访问时原始的 SqlSession 已经关闭了）。
- **批量加载 (Batch Loading)：**
   1. MyBatis 3.x 及以上支持全局或局部配置 aggressiveLazyLoading 和 lazyLoadTriggerMethods，但更强大的解决方案是使用 @Options(fetchSize=...) 或特定的执行器（如 BatchExecutor）来尝试将多个延迟加载的子查询合并成一个批量查询。这需要更复杂的配置和对 MyBatis 执行器的理解。
   - 优点： 可以将多个（比如 M 个）延迟加载的子查询合并成较少的（比如 K 个， K << M）批量查询，显著减少数据库交互次数。
   - 缺点： 配置相对复杂；不是所有场景都能完美合并。

**示例**:
```java
// 实体类结构
class A {
    Long id;
    String name;
    List<B> bList; // 1:N
}

class B {
    Long id;
    String name;
    List<C> cList; // 1:N
}

class C {
    Long id;
    String name;
    List<D> dList; // 1:N
}

class D {
    Long id;
    String name;
}
```

使用嵌套结果映射（resultMap）查询多层嵌套( A -> B (1:N), B -> C (1:N), C -> D (1:N) )结果:

1. 编写包含所有层级的 SQL（使用 LEFT JOIN）
```mysql
-- A表有字段：id、name
-- B表有字段：id、name、A.id
-- C表有字段：id、name、B.id
-- D表有字段：id、name、C.id
SELECT
   a.id        AS a_id,
   a.name      AS a_name,
   b.id        AS b_id,
   b.name      AS b_name,
   c.id        AS c_id,
   c.name      AS c_name,
   d.id        AS d_id,
   d.name      AS d_name
FROM A a
        LEFT JOIN B b ON a.id = b.a_id -- 空集合会自动初始化为空列表（非 null）
        LEFT JOIN C c ON b.id = c.b_id -- 空集合会自动初始化为空列表（非 null）
        LEFT JOIN D d ON c.id = d.c_id -- 空集合会自动初始化为空列表（非 null）
WHERE a.id = 123
ORDER BY a.id, b.id, c.id  -- 确保结果集分组有序（无序数据可能会导致对象重复创建）
```
2. 配置多层嵌套的 resultMap
```xml
<resultMap id="aResultMap" type="com.example.A">
    <id property="id" column="a_id"/>
    <result property="name" column="a_name"/>
    <!-- 1:N 映射到 B -->
    <collection property="bList" ofType="com.example.B" resultMap="bResultMap"/>
</resultMap>

<resultMap id="bResultMap" type="com.example.B">
    <id property="id" column="b_id"/>
    <result property="name" column="b_name"/>
    <!-- 1:N 映射到 C -->
    <collection property="cList" ofType="com.example.C" resultMap="cResultMap"/>
</resultMap>

<resultMap id="cResultMap" type="com.example.C">
    <id property="id" column="c_id"/>
    <result property="name" column="c_name"/>
    <!-- 1:N 映射到 D -->
    <collection property="dList" ofType="com.example.D">
        <id property="id" column="d_id"/>
        <result property="name" column="d_name"/>
    </collection>
</resultMap>

<!--3. 在 Mapper 中引用 第1步的SQL语句-->
<select id="selectAWithBCD" resultMap="aResultMap">
   SELECT
       a.id        AS a_id,
       a.name      AS a_name,
       b.id        AS b_id,
       b.name      AS b_name,
       c.id        AS c_id,
       c.name      AS c_name,
       d.id        AS d_id,
       d.name      AS d_name
   FROM A a
   LEFT JOIN B b ON a.id = b.a_id
   LEFT JOIN C c ON b.id = c.b_id
   LEFT JOIN D d ON c.id = d.c_id
   WHERE a.id = 123
   ORDER BY a.id, b.id, c.id  -- 确保结果集分组有序（无序数据可能会导致对象重复创建）
</select>
```

4. 如果数据量极大（如百万级）：
   1. 分页优先：在主查询添加分页（LIMIT），只加载当前页数据
   2. 按需加载：
      ```xml
      <collection property="bList" fetchType="lazy" ... />
      ```
   3. 批量延迟加载（MyBatis 3.4.1+）：
      ```xml
      <!-- 全局配置 -->
      <setting name="aggressiveLazyLoading" value="false"/>
      <setting name="lazyLoadTriggerMethods" value=""/>
      ```


## 鉴别器`discriminator`
角色的属性enable 值为1 的时候表示状态可用， 为0 的时候表示状态不可用。当角色可
用时， 使用rolePrivilegeListMapSelect 映射，这是一个一对多的嵌套查询映射，因此
可以获取到该角色下详细的权限信息。当角色被禁用时，只能获取角色的基本信息，不能获得
角色的权限信息。
```xml
<resultMap id = "rolePrivilegeListMapChoose"
           type = "mybatis.simple.model.SysRole ">
    <discriminator column = "enabled" JavaType= "int">
        <case value = "1" resultMap= "rolePrivilegeListMapSelect " />
        <case value = "0" resultMap = "roleMap" />
    </discriminator>
</resultMap>
```

## 调用存储过程
```xml
<select id="selectUserById" statementType="CALLABLE" useCache= "false">
    {call select_user_by_id(
        #{id, mode=IN} ,
        #{userName, mode=OUT, jdbcType=VARCHAR} ,
        #{userPassword, mode=OUT, jdbcType=VARCHAR} ,
        #{userEmail, mode=OUT, jdbcType=VARCHAR},
        #{userInfo, mode=OUT, jdbcType=VARCHAR} ,
        #{headImg, mode=OUT, jdbcType=BLOB, javaType=byte[]} ,
        #{createTime, mode=OUT, jdbcType=TIMESTAMP}
    )}
</select>
```
- 返回结果集：
```xml
<select id="selectUserPage" statementType="CALLABLE" useCache="false"
        resultMap="userMap">
    {call select_user_page(
        #{userName, mode=IN},
        #{offset, mode=IN},
        #{limit, mode=IN} ,
        #{total, mode=OUT, jdbcType=BIGINT}
    )}
</select>
```
- insert、delete
```xml
<insert id="insertUserAndRoles" statementType= "CALLABLE">
    {call insert_user_and_roles(
        #{user.id, mode=OUT, jdbcType =BIGINT} ,
        #{user.userName, mode=IN} ,
        #{user . userPassword, mode=IN} ,
        #{user.userEmail, mode=IN} ,
        #{user.userInfo, mode=IN} ,
        #{user.headImg, mode=IN, jdbcType=BLOB},
        #{user.createTime, mode=OUT, jdbcType=TIMESTAMP},
        #{roleIds, mode=IN}
    )}
</insert>
```
```xml
<delete id="deleteUserById" statementType="CALLABLE">
    {call delete_user_by_id( #{id, mode=IN} ) }
</delete>
```


# MyBatis缓存配置
一般MyBatis缓存指`二级缓存`，
`一级缓存(本地缓存)`默认会启用，并且不能控制。

## 一级缓存
MyBatis 的一级缓存存在于`SqlSession` 的生命周期中，在同一个SqlSession 中查询时，MyBatis 会把执行的方法和参数通过算法生成缓存的键值，将键值和查询结果存入一个Map对象中。
如果同一个SqlSession 中执行的方法和参数完全一致，那么通过算法会生成相同的键值，当Map 缓存对象中己经存在该键值时，则会返回缓存中的对象。

如果不想使用一级缓存，可以添加`flushCache="true"`，会在查询数据前清空当前的一级缓存。
- **但是该配置会清空SqlSession 中所有缓存对象，反复获取只读数据的情况下会增加查询数据库的次数，所以要避免这么使用！**
```xml
<select id="selectById" flushCache="true" resultMap="userMap" >
    select * from sys_user where id = #{id)
</select>
```

任何的`INSERT`、`UPDATE`、`DELETE` 操作都会清空一级缓存。

## 二级缓存

可以理解二级缓存存在于`SqlSessionFactory` 的生命周期中。  
当存在多个SqlSessionFactory 时，它们的缓存都是绑定在各自对象上的，缓存数据在一般情况下是不相通的。只有在使用如Redis这样的缓存数据库时，才可以共享缓存。


### 二级缓存配置

[mybatis-config.xml](mybatis-config.xml)

MyBatis 的二级缓存是和命名空间绑定的，即二级缓存需要配置在Mapper.xml 映射文件中，或者配置在Mapper.java接口中。
- 在Mapper.xml映射文件中：命名空间就是XML 根节点mapper 的namespace 属性。
- 在Mapper.java 接口中：命名空间就是接口的全限定名称。

**`Mapper.xml`中配置二级缓存**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
                        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="mybatis.simple.mapper.RoleMapper">
    <!--1.只需添加<cache/>元素即可-->
    <cache />
</mapper>
```
默认的二级缓存会有如下效果：
- 映射语句文件中的所有SELECT 语句将会被缓存。
- 映射语句文件中的所有时INSERT、UPDATE、DELETE 语句会刷新缓存。
- 缓存会使用LeastRecentlyUsed（LRU，最近最少使用的）算法来收回。
- 根据时间表（如no Flush Interval，没有刷新间隔），缓存不会以任何时间顺序来刷新。
- 缓存会存储集合或对象（无论查询方法返回什么类型的值）的1024个引用。
- 缓存会被视为read/write（可读/可写）的，意味着对象检索不是共享的，而且可以安全地被调用者修改，而不干扰其他调用者或线程所做的潜在修改。

👇

这些属性都可以通过缓存元素属性来进行修改：
```xml
    <cache
        eviction="FIFO" <!-- 创建一个FIFO先进先出缓存，默认LRU最近最少使用，还有SOFT软引用、WEAK弱引用 -->
        flushInterval="60000" <!-- 每隔60秒刷新一次。默认没有刷新间隔，缓存仅在调用语句时刷新。 -->
        size="512" <!-- 存储集合 或对象 的512个引用。默认1024个，要记住缓存的对象数目和运行环境的可用内存资源数目。 -->
        readOnly="true" <!-- 返回的对象是只读的，默认false，
                             只读的缓存会给所有调用者返回缓存对象的相同实例，因此这些对象不能被修改，这提供了很重要的性能优势。
                             可读写的缓存会通过序列化返回缓存对象的拷贝，这种方式会慢一些，但是安全。--> 
    />
```

### 使用二级缓存
对RoleMapper 配置二级缓存后，当调用RoleMapper 所有的select 查询方法时，二级缓存就己经开始起作用了。
- 配置可读写的缓存，MyBatis使用SerializedCache（org.apache.ibatis.cache.decorators.SerializedCache）序列化缓存来实现可读写缓存类，并通过序列化和反序列化来保证通过缓存获取数据时，得到的是一个新的实例。
    - 要求缓存类对象必须实现Serializable (java.io.Serializable）接口。
- 配置为只读缓存，MyBatis就会使用Map 来存储缓存值，这种情况下，从缓存中获取的对象就是同一个实例。

### 集成Redis缓存
1. 引入`mybatis-redis`依赖
2. 配置Redis服务
3. 修改RoleMapper.xml 中的缓存配置
```xml
<mapper namespace="mybatis.simple.mapper.RoleMapper">
    <cache type="org.mybatis.caches.redis.RedisCache"/>
</mapper>
```

**好处：可以使分布式系统应用连接到同一个缓存服务器，实现分布式应用间缓存共享。**

### 脏数据的产生和避免
二级缓存可以提升效率，减轻数据库服务器压力，但使用不当则会很容易产生脏数据。

**背景**：MyBatis 的二级缓存是和命名空间绑定的，所以通常情况下每一个Mapper 映射文件都拥有自己的二级缓存，不同Mapper 的二级缓存互不影响。

**脏数据的产生**：在关联多表查询时会将该查询放到某个命名空间下的映射文件中，这样一个多表的查询就会缓存在该命名空间的二级缓存中。
但涉及这些表的增、删、改操作通常不在一个映射文件中，它们的命名空间不同，因此当有数据变化时，多表查询的缓存未必会被清空，这种情况下就会产生脏数据。

**避免脏数据**：需要用到**参照缓存**，当某几个表可以作为一个业务整体时，通常是让几个会关联的ER 表同时使用同一个二级缓存，这样就能解决脏数据问题。
```xml
<mapper namespace="mybatis.simple.mapper.UserMapper">
    <cache-ref namespace="mybatis.simple.mapper.RoleMapper"/>
</mapper>
```
虽然这样可以解决脏数据的问题，但是并不是所有的关联查询都可以这么解决，如果有几十个表甚至所有表都以不同的关联关系存在于各自的映射文件中时，使用参照缓存显然没有意义。

### 二级缓存适用场景
- 以查询为主的应用中，只有尽可能少的增、删、改操作。
- 绝大多数以单表操作存在时，由于很少存在互相关联的情况，因此不会出现脏数据。
- 可以按业务划分对表进行分组时， 如关联的表比较少，可以通过参照缓存进行配置。
- 脏读对系统没有影响。

# Mybatis源码



# Mybatis自定义数据返回类型

## 结果返回List<Map<String, String>>

**示例1：在MyBatis中将MySQL返回的数据（例如：161:1;164:1;166:1;315:0）转换为一个List<Map<String, String>>格式。**

分析步骤：
1. 编写一个MyBatis的自定义类型处理器（TypeHandler），用来将数据库中的字符串转换为List<Map<String, String>>。
2. 处理这个字符串，将它拆分成key:value的形式，并且将它们转换为Map<String, String>。

实现步骤：
1. 编写TypeHandler
```java
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringToListMapTypeHandler extends BaseTypeHandler<List<Map<String, String>>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Map<String, String>> parameter, JdbcType jdbcType) throws SQLException {
        // 将 List<Map<String, String>> 转为数据库中的字符串格式，进行存储
        StringBuilder sb = new StringBuilder();
        for (Map<String, String> map : parameter) {
            map.forEach((key, value) -> sb.append(key).append(":").append(value).append(";"));
        }
        ps.setString(i, sb.toString());
    }

    @Override
    public List<Map<String, String>> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseResult(rs.getString(columnName));
    }

    @Override
    public List<Map<String, String>> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseResult(rs.getString(columnIndex));
    }

    @Override
    public List<Map<String, String>> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseResult(cs.getString(columnIndex));
    }

    private List<Map<String, String>> parseResult(String result) {
        List<Map<String, String>> list = new ArrayList<>();
        if (result != null && !result.isEmpty()) {
            String[] items = result.split(";");
            Map<String, String> map = new HashMap<>();
            for (String item : items) {
                String[] keyValue = item.split(":");
                if (keyValue.length == 2) {
                    map.put(keyValue[0], keyValue[1]);
                } else if (keyValue.length == 1) {
                    map.put(keyValue[0], "");
                }
            }
            list.add(map);
        }
        return list;
    }
}

```

2. 使用TypeHandler  
   **全局配置（注意：会影响到全局的sql）：**
   - 使用 @MapperScan 和 SqlSessionFactory 注册 TypeHandler 
    ```java
    import org.apache.ibatis.session.SqlSessionFactory;
    import org.apache.ibatis.type.TypeHandlerRegistry;
    import org.springframework.context.annotation.Bean;
    import org.mybatis.spring.SqlSessionFactoryBean;
    import org.springframework.context.annotation.Configuration;
    import xyz.jingxun.common.core.handler.StringToListMapTypeHandler;
    
    import javax.sql.DataSource;
    
    @Configuration
    public class MyBatisConfig {
    
        @Bean
        public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);
    
            SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
    
            // 注册自定义的 TypeHandler
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            typeHandlerRegistry.register(StringToListMapTypeHandler.class);
    
            return sqlSessionFactory;
        }
    }
    ```
   - 通过 application.yml 配置自定义TypeHandler所在的包，启动的时候会扫描到Mybatis中。
    ```yaml
    mybatis:
        type-handlers-package: com.example.handler
    ```
   
   **局部配置（查询语句级别配置，不需要全局注册TypeHandler）：**
   - 通过 @Mapper 注解注册 TypeHandler
    ```java
    import org.apache.ibatis.annotations.Result;
    import org.apache.ibatis.annotations.Results;
    import org.apache.ibatis.annotations.Select;
    import org.apache.ibatis.type.TypeHandler;
    
    public interface YourMapper {
    
        @Select("SELECT id, value_data FROM your_table WHERE some_condition")
        @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "valueData", column = "value_data", typeHandler = StringToListMapTypeHandler.class)
        })
        List<YourEntity> getYourEntities();
    }
    ```
   - 在 XML 映射文件中局部应用 TypeHandler
   ```xml
   <resultMap id="ResultMap" type="YourEntity">
       <result column="id" property="id"/>
       <result column="value_data" property="valueData" typeHandler="com.example.handler.StringToListMapTypeHandler"/>
   </resultMap>
   ```
   - @TypeHandler 注解 适用于MyBatis Plus
   ```java
   public class YourEntity {
       private Long id;
   
       @TypeHandler(StringToListMapTypeHandler.class)  // 仅此字段使用 TypeHandler
       private Map<String, String> valueData;
   }
   ```
   
