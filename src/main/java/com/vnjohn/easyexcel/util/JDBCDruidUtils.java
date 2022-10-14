package com.vnjohn.easyexcel.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author vnjohn
 * @date 2022/7/7
 */
public class JDBCDruidUtils {
    private static DataSource dataSource;

    /**
     * 创建数据 Properties 集合对象加载加载配置文件
     */
    static {
        Properties pro = new Properties();
        //加载数据库连接池对象
        try {
            //获取数据库连接池对象
            pro.load(JDBCDruidUtils.class.getClassLoader().getResourceAsStream("druid.properties"));
            dataSource = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    /**
     * 关闭conn,和 statement独对象资源
     *
     * @param connection
     * @param statement
     * @MethodName: close
     * @return: void
     */
    public static void close(Connection connection, Statement statement) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭 conn , statement 和resultset三个对象资源
     *
     * @param connection
     * @param statement
     * @param resultSet
     * @MethodName: close
     * @return: void
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        close(connection, statement);
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取连接池对象
     * @return
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

}

