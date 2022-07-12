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
    <collection property = "roleList" columnPrefix = "role_"
                javaType = "mybatis.simple.model.SysRole">
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

