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