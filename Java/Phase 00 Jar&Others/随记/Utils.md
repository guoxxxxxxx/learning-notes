# Utils

## 1. ConnectionUtils

``` java
package com.lagou.utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 连接工具类: 从数据源中获取一个链接, 并将获取到的链接与线程进行绑定
 */
@Component
public class ConnectionUtils {

    @Autowired
    private DruidDataSource dataSource;

    // 线程内部的存储类, 可以在指定的线程内存储数据 key:threadLocal(当前线程) value:connect
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    /**
     *  获取当前线程绑定的链接: 如果获取到的链接为空, 那么就要从数据源中获取链接, 并且放到ThreadLocal中(绑定到当前线程)
     */
    public Connection getThreadConnection(){
        // 先从ThreadLocal上获取链接
        Connection connection = threadLocal.get();
        // 判断当前线程中是否有connection
        if (connection == null){
            //从数据源中获取一个链接 并保存到threadLocal中
            try {
                connection = dataSource.getConnection();
                threadLocal.set(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * 接触当前线程的链接绑定
     */
    public void removeThreadLocal(){
        threadLocal.remove();
    }
}

```



## 2. TransactionManager

``` java
package com.lagou.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务管理器工具类， 包含: 开启事务、提交事务、回滚事务、释放资源
 */
@Component
public class TransactionManager {

    @Autowired
    private ConnectionUtils connectionUtils;

    /**
     * 开启事务
     */
    public void beginTransaction(){
        // 获取connection对象
        Connection connection = connectionUtils.getThreadConnection();
        try {
            // 开启手动事务
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 提交事务
     */
    public void commit(){
        Connection connection = connectionUtils.getThreadConnection();
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回滚事务
     */
    public void rollback(){
        Connection connection = connectionUtils.getThreadConnection();
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    public void release(){
        // 将手动事务改回自动事务
        Connection connection = connectionUtils.getThreadConnection();
        try {
            connection.setAutoCommit(true);
            // 将链接归还到连接池
            connectionUtils.getThreadConnection().close();
            // 解除事务绑定
            connectionUtils.removeThreadLocal();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

