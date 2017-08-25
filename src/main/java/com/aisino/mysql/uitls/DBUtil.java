package com.aisino.mysql.uitls;

import com.aisino.mysql.bean.JdbcConfig;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class DBUtil {

    private static Map<String, BasicDataSource> dataSource = new HashMap<>();


    public static void initDB(Map<String, JdbcConfig> config) throws Exception {

        Set<String> keys = config.keySet();
        for (String key : keys) {
            JdbcConfig mysqlConfig = config.get(key);
            Properties p = new Properties();
            p.setProperty("driverClassName", "com.mysql.jdbc.Driver");
            p.setProperty("url", mysqlConfig.getUrl());
            p.setProperty("username", mysqlConfig.getUsername());
            p.setProperty("password", mysqlConfig.getPassword());
            p.setProperty("maxTotal", Integer.toString(mysqlConfig.getMaxWorkerThreads() * 2 + 1));
            p.setProperty("maxIdle", Integer.toString(mysqlConfig.getMaxWorkerThreads() + 1));
            p.setProperty("maxWait", "1000");
            p.setProperty("removeAbandoned", "false");
            p.setProperty("removeAbandonedTimeout", "120");
            p.setProperty("testOnBorrow", "true");
            p.setProperty("logAbandoned", "true");

            dataSource.put(key, (BasicDataSource) BasicDataSourceFactory.createDataSource(p));
        }

    }

    /**
     * 从连接池获得对象
     * @param name 数据库配置的名称，例如 server.default.url 其中default即为配置的name
     * @return 数据库连接实例 {@link #releaseConnection(Connection)}
     * @throws SQLException
     */
    public static Connection getConnection(String name) throws SQLException {
        BasicDataSource source = dataSource.get(name);

        return source != null ? source.getConnection() : null;
    }

    /**
     * 释放链接，并归入连接池
     * @param connection    {@link #getConnection(String)}
     * @throws SQLException
     */
    public static void releaseConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * 数据集转换为map
     * @param rs    数据集
     * @return map实例
     * @throws SQLException
     */
    public static Map<String, String> praseResultSet(ResultSet rs)
            throws SQLException {
        Map<String, String> hm = new HashMap<String, String>();
        ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();

        int count = rsmd.getColumnCount();
        for (int i = 1; i <= count; i++) {
            String key = rsmd.getColumnLabel(i);
            Object value = rs.getObject(i);
            hm.put(key, value != null ? rs.getString(i) : "");

        }
        return hm;
    }

    /**
     * 关闭连接池
     * @throws SQLException
     */
    public static void closeDataSource() throws SQLException {
        Set<String> keys = dataSource.keySet();
        for (String key : keys) {
            dataSource.get(key).close();
        }
    }

}

