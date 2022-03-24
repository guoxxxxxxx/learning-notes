# SSM 学习笔记



**SSM = springMVC + spring + mybatis**



## 一、Mybatis

**基于ORM的半自动轻量级框架**

```
ORM：Object Relational Mapping (对象关系映射)
O: 对象模型-实体对象
R: 关系型数据库的结构模型-数据表
M: 映射-将实体对象与数据库表建立映射关系

半自动与全自动的主要区别是：是否需要手动编写sql
半自动:mybatis
全自动:hibernate

轻量级：程序在启动期间所需要消耗的资源少
```



**开发流程**

```
1. 创建数据库及user表
2. 创建maven工程，导入依赖（MySQL驱动、mybatis、junit）
3. 编写User实体类
4. 编写UserMapper.xml映射配置文件（ORM思想）
5. 编写SqlMapConfig.xml核心配置文件
数据库环境配置
映射关系配置的引入(引入映射配置文件的路径)
6. 编写测试代码
// 1.加载核心配置文件
// 2.获取sqlSessionFactory工厂对象
// 3.获取sqlSession会话对象
// 4.执行sql
// 5.打印结果
// 6.释放资源
```



#### **mybatis XML 配置头信息**

```xml
<!--Mapper-->

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
```

---

```xml
<!--config-->

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
```



**引入配置映射文件**

```
<!--引入映射配置文件-->
    <mappers>
        <mapper resource="mapper/UserMapper.xml"></mapper>
    </mappers>
```



**代理开发模式注意事项**

- Mapper.xml映射文件中的namespace与mapper接口的全限定名相同 
- Mapper接口方法名和Mapper.xml映射文件中定义的每个statement的id相同 
- Mapper接口方法的输入参数类型和mapper.xml映射文件中定义的每个sql的parameterType的类型相同
-  Mapper接口方法的输出参数类型和mapper.xml映射文件中定义的每个sql的resultType的类型相同





#### **#{}与#${}的区别**

**\#{} :表示一个占位符号**

- 通过 #{} 可以实现preparedStatement向占位符中设置值，自动进行java类型和jdbc类型转换，# {}可以有效防止sql注入。
- \#{} 可以接收简单类型值或pojo属性值。
- 如果parameterType传输单个简单类型值， #{} 括号中名称随便写。



**${} :表示拼接sql串**

- 通过 ${} 可以将parameterType 传入的内容拼接在sql中且不进行jdbc类型转换，会出现sql注入 问题。

- ${} 可以接收简单类型值或pojo属性值。

- 如果parameterType传输单个简单类型值， ${} 括号中只能是value。

  

### Mybatis  案例

#### jdbc.properties 

```xml
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql:///mybatis_db?characterEncoding=utf-8
jdbc.username=root
jdbc.password=123456
```



#### UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.lagou.mapper.UserMapper">
    <!--根据id查询用户-->
    <select id="findUserById" parameterType="int" resultType="com.lagou.domain.User">
        select * from user where id = #{id}
    </select>
</mapper>
```



#### sqlMapconfig.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>

    <!--加载properties文件-->
    <properties resource="jdbc.properties"></properties>

    <!--environments:运行环境-->
    <environments default="development">
        <environment id="development">
            <!--当前的事务管理器是JDBC-->
            <transactionManager type="JDBC"></transactionManager>
            <!--数据源信息 POOLED: 使用mybatis的连接池-->
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>

</configuration>
```

```xml
	<!--设置别名-->
    <typeAliases>
        <!--方式一: 给单个实体起别名-->
        <typeAlias type="com.lagou.domain.User" alias="user"></typeAlias>
        <!--方式二: 批量起别名 别名就是类名 且 不区分大小写-->
        <package name="com.lagou.domain"/>
    </typeAliases>
```

``` xml
    <!--引入配置文件-->
    <mappers>
        <!--<mapper resource="com/lagou/mapper/UserMapper.xml"></mapper>-->

        <!--使用该方式接口文件和映射文件需要同包同名-->
        <!--<mapper class="com.lagou.mapper.UserMapper"></mapper>-->

        <!--批量加载映射-->
        <package name="com.lagou.mapper"/>
    </mappers>
```

#### 自定义匹配(resultMap)

``` xml
	<!--id: 标签的唯一标识-->
    <!--type: 封装后的实体类型-->
    <resultMap id="userResultMap" type="com.lagou.domain.User">
        <!--手动配置映射关系-->
        <!--id: 用来配置主键 property: 类中字段名称     column: 数据库中字段名称-->
        <id property="id" column="id"></id>
        <!--result: 对表中普通字段的封装-->
        <result property="username" column="username"></result>
        <result property="birthday" column="birthday"></result>
        <result property="sex" column="sex"></result>
        <result property="address" column="address"></result>
        
        
         <!--
            association: 在进行一对一关联查询配置时, 使用association标签进行关联
                property: 要封装实体的属性名
                javaType: 要封装实体的属性类型
         -->
        <association property="user" javaType="com.lagou.domain.User">
            <id property="id" column="uid"></id>
            <result property="username" column="username"></result>
            <result property="birthday" column="birthday"></result>
            <result property="sex" column="sex"></result>
            <result property="address" column="address"></result>
        </association>
        
        
        <!--
            collection: 一对多使用collection标签进行关联
        -->
        <collection property="orderList" ofType="com.lagou.domain.Orders">
            <id property="id" column="oid"></id>
            <result property="ordertime" column="ordertime"></result>
            <result property="total" column="total"></result>
            <result property="uid" column="uid"></result>
        </collection>
        
    </resultMap>


	<!--
        一对一嵌套查询
    -->
    <resultMap id="findAllWithUser2Map" type="com.lagou.domain.Orders">
        <id property="id" column="id"></id>
        <result property="ordertime" column="ordertime"></result>
        <result property="total" column="total"></result>
        <result property="uid" column="uid"></result>

        <!--
            select: 写要进行二次查询的package名称+方法名称
            column: 要传递的参数
        -->
        <association property="user" javaType="com.lagou.domain.User" 	select="com.lagou.mapper.UserMapper.findUserById" column="uid">

        </association>
    </resultMap>
```



#### 动态if 查询

```xml
<!--
        动态sql的if标签 : 多条件查询
    -->
    <select id="findByIdAndUsernameIf" parameterType="com.lagou.domain.User" resultType="com.lagou.domain.User">
        select * from user
        <!-- where标签相当于where 1=1, 如果下述标签都不满足 则不会进行拼接 -->
            <where>
                <!-- test 里面写的就是表达式 -->
                <if test="id != null">
                    and id = #{id}
                </if>
                <if test="username != null">
                    and username = #{username}
                </if>
            </where>
    </select>
```



#### 返回主键

```xml
<!--添加用户 获取返主键 方式一-->
    <!--
        useGeneratedKeys: 返回主键
        keyProperty: 把返回主键的值, 封装到实体的那个属性上
        注意: 只适用于主键自增的数据库, mysql和sqlserver, oracle不可以
    -->
    <insert id="saveUser1" parameterType="com.lagou.domain.User" useGeneratedKeys="true" keyProperty="id">
        insert into user(username, birthday, sex, address) values (#{username}, #{birthday}, #{sex}, #{address})
    </insert>


    <!--
        selectKey: 使用范围更广, 支持所有类型的数据库
        order="AFTER": 设置在sql语句执行前(后), 执行此语句
        keyColumn: 指定主键所对应的列名
        keyProperty: 把返回主键的值, 封装到实体的那个属性上
        resultType="int": 指定主键类型
    -->
    <insert id="saveUser2" parameterType="com.lagou.domain.User">
        <selectKey order="AFTER" keyColumn="id" keyProperty="id" resultType="int">
            SELECT LAST_INSERT_ID()
        </selectKey>
        insert into user(username, birthday, sex, address) values (#{username}, #{birthday}, #{sex}, #{address})
    </insert>
```



#### 模糊查询

```xml
<!-- 模糊查询 方式一 -->
    <select id="findByUsername" resultType="com.lagou.domain.User" parameterType="string">

        <!--
            ${} 使用该占位符时，不会自动添加单引号‘’ 是sql原样拼接
            当parameterType是基本数据类型的时候或者String的时候, ${}里面的值只能写value
        -->
        <!--select * from user where username like '${value}'-->
        <!-- #{} 使用该占位符时，会自动添加单引号‘’ -->
        select * from user where username like #{string}
    </select>
```



#### 多条件查询(动态查询)

```java
/*
       多条件查询方式一
    */
   public List<User> findByIdAndUsername1(int id, String username);

   /*
       多条件查询方式二
    */
   public List<User> findByIdAndUsername2(@Param("id") int id, @Param("username") String username);

   /*
   多条件查询方式三 推荐该方式
*/
   public List<User> findByIdAndUsername3(User user);
```

```xml
<!--多条件查询 方式一-->
<select id="findByIdAndUsername1" resultMap="userResultMap">
    <!--select * from user where id = #{arg0} and username = #{arg1}*/-->
    select * from user where id = #{param1} and username = #{param2}
</select>

<!--多条件查询 方式二-->
<select id="findByIdAndUsername2" resultMap="userResultMap">
    select * from user where id = #{id} and username = #{username}
</select>

<!--多条件查询 方式三-->
<select id="findByIdAndUsername3" resultMap="userResultMap" parameterType="com.lagou.domain.User">
    select * from user where id = #{id} and username = #{username}
</select>
```



#### 动态更新

```xml
<!--
    动态sql的set标签: 动态更新
-->
<update id="updateIf" parameterType="com.lagou.domain.User">
    update user
    <!-- set标签: 在更新的时候会自动添加set关键字, 还会去掉最后一个标签的都好 -->
    <set>
        <if test="username != null">
            username = #{username},
        </if>
        <if test="birthday != null">
            birthday = #{birthday},
        </if>
        <if test="sex != null">
            sex = #{sex},
        </if>
        <if test="address != null">
            address = #{address},
        </if>
    </set>
        where id = #{id}
</update>
```



#### 动态查询(foreach查询)

``` xml
	<!--
        动态sql中的foreach标签: 多值查询
    -->
    <select id="findByList" parameterType="list" resultType="com.lagou.domain.User">
        select * from user
        <where>
            <!--
                collection: 代表要遍历的集合元素, 通常写collection或list
                open: 代表语句的开始部分
                close: 代表语句的结束部分
                item: 代表遍历集合中的每个元素生成的变量名
                separator: 分隔符
             -->
            <foreach collection="collection" open="id in (" close=")" item="id" separator=",">
                #{id}
            </foreach>
        </where>
    </select>
```



#### SQL代码的复用

``` xml
	<!--定义所需要服用的代码-->
	<sql id="selectUser">
        select * from user
    </sql>


    <!--查询所有用户-->
    <!--resultMap: 手动配置实体属性与表中字段的映射关系, 完成手动封装-->
    <select id="findAllResultMap" resultMap="userResultMap">
        <include refid="selectUser"></include>
    </select>
```



#### Mybatis知识小结

``` xml
<select>：查询
<insert>：插入
<update>：修改
<delete>：删除
<selectKey>：返回主键
<where>：where条件
<if>：if判断
<foreach>：for循环
<set>：set设置
<sql>：sql片段抽取
```



#### MyBatis核心配置文件常用标签

1、properties标签：该标签可以加载外部的properties文件 

2、typeAliases标签：设置类型别名 

3、environments标签：数据源环境配置标签 

4、plugins标签：配置MyBatis的插件



#### Mabatis多表配置方式

``` 
* 多对一（一对一）配置：使用<resultMap>+<association>做配置

* 一对多配置：使用<resultMap>+<collection>做配置

* 多对多配置：使用<resultMap>+<collection>做配置

* 多对多的配置跟一对多很相似，难度在于SQL语句的编写。
```



#### 实现延迟加载

``` xml
<!--在调用当前对象的equals、clone、hashCode、toString方法时也会触发关联对象的查询-->
<!--在config文件中配置setting-->
<!--局部加载的优先级更高 建议一对一的情况使用立即加载, 一对多情况时使用延迟加载-->
<settings>
    <!--所有方法都会延迟加载局部延迟加载-->
    <setting name="lazyLoadTriggerMethods" value="toString()"/>
    <!--全局加载-->
    <setting name="lazyLoadTriggerMethods" value="true"/>
</settings>


<!--
	fetchType:
		lazy: 延迟加载策略
		easy: 立即加载策略
-->
<collection property="ordersList" select="com.lagou.mapper.OrderMapper.findAllOrderWithUserId" column="id" fetchType="lazy">
```



#### 缓存技术

**一级缓存**

- 当SQL语句和参数完全一样时, 使用同一个sqlSession调用同一个方法, 往往只执行一次SQL

- sqlSession级别的缓存，默认是开启的。
- 一级缓存是SqlSession范围的缓存，执行SqlSession的C（增加）U（更新）D（删除）操作，或者调 用clearCache()、commit()、close()方法，都会清空缓存。

``` 
在select标签里面加入flushcache=true
每次查询时都会删除缓存
```

**二级缓存**(鸡肋,基本不用)

- 二级缓存是namspace级别（跨sqlSession）的缓存，是默认不开启的
- 二级缓存的开启需要进行配置，实现二级缓存的时候，MyBatis要求返回的POJO必须是可序列化的。



**配置核心配置文件**

``` xml
<settings>
    <!--
    因为cacheEnabled的取值默认就为true，所以这一步可以省略不配置。
    为true代表开启二级缓存；为false代表不开启二级缓存。
    -->
    <setting name="cacheEnabled" value="true"/>
</settings>

```



### MyBatis注解开发

#### 1. 实现一对一查询

``` java
    /**
     * 查询所有订单, 同时查询订单所属的用户的信息
     */
    @Select("select * from orders")
    // 代替的就是ResultMap标签
    @Results({
        /*
        	property: 定义的实体类中的属性名
        	column: 数据库中的字段名
        	id = true : 表示该关键字为主键
        	javaType: 表示该对象的类型
        	column: 表示所要传递的参数
        	one: 一对一	其中注解中 select值等于所要调用的查询方法的package.methodName
        	fetchType: 是否实现延迟加载 EAGER 否	LAZY 是 DEFAULT 看全局开关
        */
            @Result(property = "id", column = "id", id = true),
            @Result(property = "ordertime", column = "ordertime"),
            @Result(property = "total", column = "total"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "user", javaType = User.class, column = "uid", one = @One(select = "com.lagou.mapper.UserMapper.findUserById", fetchType = FetchType.EAGER))
    })
    public List<Orders> findAllWithUser();
```



#### 2. 实现一对多查询

``` java
	/**
     * 查询一个用户，与此同时查询出该用户具有的订单
     */
    @Select("select * from user")
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "username", column = "username"),
            @Result(property = "birthday", column = "birthday"),
            @Result(property = "sex", column = "sex"),
            @Result(property = "address", column = "address"),
            @Result(property = "ordersList", javaType = List.class, column = "id", many = @Many(select = "com.lagou.mapper.OrderMapper.findOrdersListById"))
    })
    public List<User> findUserWithOrders();
```



#### 3. 实现多对多查询

```java
	/**
     * 查询所有用户，同时查询出该用户的所有角色
     */
    @Select("select * from user")
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "username", column = "username"),
            @Result(property = "birthday", column = "birthday"),
            @Result(property = "sex", column = "sex"),
            @Result(property = "address", column = "address"),
            @Result(property = "roleList", javaType = List.class, column = "id", many = @Many(select = "com.lagou.mapper.RoleMapper.findRoleByUid"))
    })
    public List<User> findUserWithRole();



	/**
     * 根据userid查询其所对应的职位
     */
    @Select("SELECT * FROM sys_user_role ur INNER JOIN sys_role r ON ur.userid = #{uid} AND r.id = ur.roleid")
    public List<Role> findRoleByUid(int uid);
```



#### 4. 使用注解开启二级缓存

``` java
// 开启二级缓存
@CacheNamespace
```



#### 5. 使用注解开发和使用xml开发的优缺点

* 注解开发和xml配置相比，从开发效率来说，注解编写更简单，效率更高。
* 从可维护性来说，注解如果要修改，必须修改源码，会导致维护成本增加。xml维护性更强
* 建议: 如果是单表查询的话建议使用注解来进行开发，如果是多表查询，建议使用xml来进行开发。







## 二、Spring IOC

### 1. Spring的两大核心

- IOC：控制反转(Inverse Of Control)
- AOP: 面前切面编程(Aspect Oriented Programming)



### 2. Spring 的核心配置信息（applicationContext.xml)

- **配置头**

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd">
</beans>
```

 ``` xml
 <?xml version="1.0" encoding="UTF-8"?>
 <beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="
         http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/context
         http://www.springframework.org/schema/context/spring-context.xsd">
 </beans>
 ```

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd ">
</beans>
```







- **方式一、无参构造方法实例化**

``` xml
  <!--
          在Spring配置文件中配置UserDaoImpl
          id : 唯一标识
          class: 类全路径
          scope: 对象的作用范围
              - singleton 单例 是默认方式, 只创建一次 当加载Spring核心文件时,对象就被创建了
              - prototype 多例 每次通过反射机制调用getBean方法时都会创建一个新的对象
          init-method: 初始化方法(用的很少,了解一下)
          destroy-method: 销毁方法(用的很少,了解一下)
      -->
      <bean id="userDao" class="com.lagou.dao.impl.UserDaoImpl" scope="singleton" init-method="init" destroy-method="destroy"></bean>
  
```

  

- **方式二、工厂静态方法实例化**

``` xml
<!--工厂静态方法实例化-->
    <bean id="userDao" class="com.lagou.factory.StaticFactoryBean" factory-method="createUserDao"></bean>
```



- **方式三、普通工厂方法实例化**

``` xml
      <!--普通工厂方法实例化-->
      <bean id="dynamicFactoryBean" class="com.lagou.factory.DynamicFactoryBean"></bean>
      <bean id="userDao" factory-bean="dynamicFactoryBean" factory-method="createUserDao"></bean>
```

  

### 3. 依赖注入

- **方式一、有参注入**

``` xml
    <!--
        配置UserService
        constructor-arg: 声明这是一个有参构造
        index="0": 指的是值要赋给第一个参数
        type: 所要赋值参数的数据类型
        ref: 需要注入的值
        name: 所要注入的参数名称
    -->
    <bean id="userService" class="com.lagou.service.impl.UserServiceImpl">
        <!--<constructor-arg index="0" type="com.lagou.dao.UserDao" ref="userDao"></constructor-arg>-->
        <constructor-arg name="userDao" ref="userDao"></constructor-arg>
    </bean>
```

- **方式二、set注入(更加常用)**

``` xml
	<bean id="userService" class="com.lagou.service.impl.UserServiceImpl">
        <!--
            set方法实现依赖注入
            ref: 引用类型对象
            value: 数字类型
        -->
        <property name="userDao" ref="userDao"></property>
    </bean>
```

- **方式二-2，p  注入 **



### 4. 数据类型注入

``` xml
	<!--
        在Spring配置文件中配置UserDaoImpl
        id : 唯一标识
        class: 类全路径
        scope: 对象的作用范围
            - singleton 单例 是默认方式, 只创建一次 当加载Spring核心文件时,对象就被创建了
            - prototype 多例 每次通过反射机制调用getBean方法时都会创建一个新的对象
        init-method: 初始化方法(用的很少,了解一下)
        destroy-method: 销毁方法(用的很少,了解一下)
    -->
    <bean id="userDao" class="com.lagou.dao.impl.UserDaoImpl" scope="singleton">
        <!--
            完成依赖注入
            ref: 用于引用数据类型的注入
            value: 用于普通数据类型的注入
        -->
        <property name="username" value="子慕"></property>
        <property name="age" value="18"></property>

        <!--进行集合类型的数据注入-->
        <property name="list">
            <list>
                <value>aaa</value>
                <ref bean="user"></ref>
            </list>
        </property>

        <!--进行set集合的配置-->
        <property name="set">
            <set>
                <value>bbb</value>
                <ref bean="user"></ref>
            </set>
        </property>

        <!--进行array集合的配置-->
        <property name="array">
            <array>
                <value>ccc</value>
                <ref bean="user"></ref>
            </array>
        </property>

        <!--进行map集合的配置-->
        <property name="map">
            <map>
                <entry key="k1" value-ref="user"></entry>
                <entry key-ref="user" value="user"></entry>
            </map>
        </property>
    </bean>
```



### 5. 配置文件模块化

``` xml
<import resource="applicationContext-xxx.xml"/>
```

**注意**

- 同一个xml中不能出现相同名称的bean,如果出现会报错
  - 多个xml如果出现相同名称的bean，不会报错，但是后加载的会覆盖前加载的bean



### 6. Spring注解开发



| 注解            | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| @Component      | 使用在类上用于实例化Bean                                     |
| @Controller     | 使用在web层上用于实例化Bean                                  |
| @Service        | 使用在Service层上用于实例化Bean                              |
| @Repository     | 使用在Dao层上用于实例化Bean                                  |
| @Autowired      | 使用在字段上用于根据类型注入依赖                             |
| @Qualifier      | 结合@Autowired一起使用,根据名称进行依赖注入                  |
| @Resource       | 相当于@Autowired+@Qualifier, 按照名称进行注入(基本上不用)    |
| @Value          | 注入普通属性                                                 |
| @Scope          | 标注Bean标签的作用范围                                       |
| @postConstruct  | Bean的初始化方法类似与init-method                            |
| @PreDestroy     | Bean的销毁方法类似于destroy-method                           |
|                 |                                                              |
| @configuration  | 用于指定当前类是一个Spring配置类, 当创建容器时会从该类上加载注解 |
| @Bean           | 使用在方法上, 标注将该方法的返回值存储到Spring容器中         |
| @PropertySource | 用于加载properties文件中的配置                               |
| @ComponentScan  | 用于指定Spring在初始化容器时要扫描的包                       |
| @Import         | 用于导入其他配置                                             |



#### 1) 添加注解扫描(重要！核心)

``` xml
    <!--注解扫描-->
    <context:component-scan base-package="com.lagou"/>
```

#### 2) Bean实例化

- 传统方法

``` xml
<bean id="userDao" class="com.lagou.dao.impl.UserDaoImpl"></bean>
```

- 使用注解

``` xml
```

#### 3) 注解配置文件

``` java
package com.lagou.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.lagou")
@Import(dataSourceConfig.class)
public class SpringConfig {




    @Bean("queryRunner")
    public QueryRunner getQueryRunner(@Autowired DruidDataSource dataSource){
        return new QueryRunner(dataSource);
    }
}

```

``` java
package com.lagou.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:jdbc.properties")
public class dataSourceConfig {

    @Value("${jdbc.driver}")
    private String driver;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Bean("dataSource")
    public DruidDataSource getDruidDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        return druidDataSource;
    }

}

```



- 通过注解获取service层对象

``` java
AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);

AccountService accountService = (AccountService) nnotationConfigApplicationContext.getBean("accountService");
```



## 三、Spring AOP

### 1、 JDK动态代理

### 2、 Cglib动态代理

###  3、AOP的xml开发方式

``` xml
    <!--AOP配置-->
    <aop:config>
        <!--抽取切点表达式-->
        <aop:pointcut id="myPointCut" expression="execution(* com.lagou.service.impl.AccountServiceImpl.*(..))"/>
        <!-- 配置切面: 切入点 + 通知 -->
        <aop:aspect ref="myAdvice">
            <!--配置目标类的transfer方法执行时, 使用myadvice类中的before方法做个前置增强 -->
            <aop:before method="before" pointcut-ref="myPointCut"/>
            <aop:after-returning method="after" pointcut-ref="myPointCut"/>
        </aop:aspect>
    </aop:config>
```

- 访问修饰符可以省略

- 返回值类型、包名、类名、方法名可以使用星号 * 代替，代表任意 
- 包名与类名之间一个点 . 代表当前包下的类，两个点 .. 表示当前包及其子包下的类 
- 参数列表可以使用两个点 .. 表示任意个数，任意类型的参数列表

**例如**

``` 
execution(public void com.lagou.service.impl.AccountServiceImpl.transfer())
execution(* com.lagou.service.impl.AccountServiceImpl.*(..)) ------ 常用这种方法
execution(* com.lagou.service.impl.*.*(..))
execution(* com.lagou.service..*.*(..))
```



### 4、 AOP的注解开发

``` java
package com.lagou.service.impl;

import com.lagou.service.AccountService;
import org.springframework.stereotype.Service;


@Service
public class AccountServiceImpl implements AccountService {

    @Override
    public void transfer() {
        System.out.println("转账方法执行了");
    }
}
```



``` java
package com.lagou.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 通知类
 */
@Component
@Aspect     // 升级为切面类: 配置切点和通知的关系
public class MyAdvice {

    @Pointcut("execution(* com.lagou.service.impl.AccountServiceImpl.*(..))")
    public void myPoint(){}

    @Before("MyAdvice.myPoint()")
    public void before(){
        System.out.println("前置通知执行了!");
    }

    @AfterReturning("MyAdvice.myPoint()")
    public void afterReturning(){
        System.out.println("后置方法执行了!");
    }

    @AfterThrowing("MyAdvice.myPoint()")
    public void afterThrowing(){
        System.out.println("异常通知执行了!");
    }

    @After("MyAdvice.myPoint()")
    public void after(){
        System.out.println("最终通知执行了!");
    }

    @Around("MyAdvice.myPoint()")
    public Object around(ProceedingJoinPoint pjp){
        System.out.println("before");
        try {
            pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("after");
        System.out.println("afterReturning");
        System.out.println("error");
        return null;
    }

}
```

``` xml
动态代理方法实现方法增强
<!--AOP配置-->
    <aop:config>
        <!--切点表达式-->
        <aop:pointcut id="myPointCut" expression="execution(* com.lagou.service.impl.AccountServiceImpl.*(..))"/>
        <!--切面配置-->
        <aop:aspect ref="transactionManager">
            <aop:before method="beginTransaction" pointcut-ref="myPointCut"/>
            <aop:after-returning method="commit" pointcut-ref="myPointCut"/>
            <aop:after-throwing method="rollback" pointcut-ref="myPointCut"/>
            <aop:after method="release" pointcut-ref="myPointCut"/>
        </aop:aspect>
    </aop:config>
```

