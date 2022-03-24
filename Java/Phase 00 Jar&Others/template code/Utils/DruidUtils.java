package com.guox.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DruidUtils {
    // 数据源(可以从该对象获取数据库连接 更加高效)
    public static DataSource dataSource;

    static {
        try {
            // 创建属性集对象(用于读取配置文件)
            Properties properties = new Properties();
            // 加载配置文件
            InputStream is = DruidUtils.class.getClassLoader().getResourceAsStream("druid.properties");
            // 使用properties对象的load方法从字节流中读取配置信息
            properties.load(is);
            // 通过工厂类获取连接池对象
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     * @return Connection
     */
    public static Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Druid连接池对象
     * @return DataSource
     */
    public static DataSource getDataSource(){
        return dataSource;
    }

    /**
     * 释放资源
     * @param connection 连接对象
     */
    public static void close(Connection connection){
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection connection, Statement statement){
        if (statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        close(connection);
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet){
        if (resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        close(connection, statement);
    }
}
