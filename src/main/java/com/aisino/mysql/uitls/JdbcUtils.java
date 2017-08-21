package com.aisino.mysql.uitls;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aisino.mysql.constants.SqlQuery.SQL_QUERY_TB_US_COUNT;

/**
 * @Package : com.aisino.mysql.uitls
 * @Class : JdbcUtils
 * @Description :
 * @Author : liuyang
 * @CreateDate : 2017-08-18 星期五 22:00:54
 * @Version : V1.0.0
 * @Copyright : 2017 liuyang Inc. All rights reserved.
 */
public class JdbcUtils {
    public static Logger log = Logger.getLogger(JdbcUtils.class.getName());


    /**
     * @Method : getConnection
     * @Description : 获取jdbc数据库连接
     * @param driver : 
     * @param url : 
     * @param username : 
     * @param password : 
     * @Return : java.sql.Connection
     * @Author : liuyang
     * @CreateDate : 2017-08-18 星期五 22:01:11
     */
    public static Connection getConnection(String driver, String url, String username, String password) {
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error("数据库连接异常："+url);
        }
        return null;
    }

    /**
     * @Method : execSql
     * @Description : 执行普通Sql语句
     * @param connection :
     * @param sql :
     * @Return : void
     * @Author : liuyang
     * @CreateDate : 2017-08-18 星期五 22:03:21
     */
    public static void execSql(String sql, Connection connection) {
        Statement stat = null;
        if (connection != null) {
            try {
                stat = connection.createStatement();
                stat.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (stat != null) {
                        stat.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @Method : queryBySql
     * @Description : 执行SQL并返回执行结果集
     * @param sql :
     * @param connection :
     * @Return : java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Author : liuyang
     * @CreateDate : 2017-08-18 星期五 22:06:43
     */
    public static List<Map<String, Object>> queryBySql(String sql, Connection connection) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        PreparedStatement ps = null;
        ResultSetMetaData rsmd = null;
        ResultSet rs = null;
        int columns;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            columns = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columns; i++) {
                    map.put(rsmd.getColumnLabel(i + 1), getValueByType(rs, rsmd.getColumnType(i + 1), rsmd.getColumnLabel(i + 1)));
                    System.out.println(rsmd.getColumnLabel(i+1));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * @Method : queryBySql
     * @Description : 执行SQL并返回执行结果集
     * @param sql :
     * @param connection :
     * @param params :
     * @param keys :
     * @Return : java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Author : liuyang
     * @CreateDate : 2017-08-19 星期六 11:56:40
     */
    public static List<Map<String, Object>> queryBySql(String sql, Connection connection, List<Map<String, Object>> params, String[] keys) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        PreparedStatement ps = null;
        ResultSetMetaData rsmd = null;
        ResultSet rs = null;
        int columns;
        try {
            ps = connection.prepareStatement(sql);
            for (Map<String, Object> param : params) {
                for (int i = 0; i < keys.length; i++) {
                    Object value = param.get(keys[i]);
                    ps.setObject(i + 1, value);
                }
            }
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            columns = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columns; i++) {
                    map.put(rsmd.getColumnName(i + 1), getValueByType(rs, rsmd.getColumnType(i + 1), rsmd.getColumnName(i + 1)));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static List<Map<String, Object>> queryPage(String sql, Connection connection, int offset,int pageSize) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        PreparedStatement ps = null;
        ResultSetMetaData rsmd = null;
        ResultSet rs = null;
        int columns;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            columns = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columns; i++) {
                    map.put(rsmd.getColumnLabel(i + 1), getValueByType(rs, rsmd.getColumnType(i + 1), rsmd.getColumnLabel(i + 1)));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * @Method : insertBatch
     * @Description : 批量插入数据，此函数不具有一般通用性
     * @param sql :
     * @param connection :
     * @param params :
     * @Return : void
     * @Author : liuyang
     * @CreateDate : 2017-08-18 星期五 22:09:19
     */
    public static void insertBatch(String sql, Connection connection, List<Map<String, Object>> params, String[] keys) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            for (Map<String, Object> param : params) {
                for (int i = 0; i < keys.length; i++) {
                    String value = (String) param.get(keys[i]);
                    ps.setString(i + 1, value);
                }
                ps.addBatch();
            }

            ps.executeBatch();//批量更新
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @Method : getQueryCount
     * @Description : 获取查询数量
     * @Param sql :
     * @Param connection :
     * @ReturnType : int
     * @Author : liuyang
     * @CreateDate : 2017-08-19 星期六 12:28:58
     */
    public static int getQueryCount(String sql, Connection connection) {
        List<Map<String, Object>> list = JdbcUtils.queryBySql(SQL_QUERY_TB_US_COUNT, connection);
        Object obj = list.get(0).get("size");
        int size = 0;
        try {
            size = Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return size;
    }

    public static int count(String sql, Connection connection) {
        int size=0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }


    /**
     * @Method : getValueByType
     * @Description : 将执行的结果集进行类型转化选择
     * @param rs :
     * @param type :
     * @param name :
     * @Return : java.lang.Object
     * @Author : liuyang
     * @CreateDate : 2017-08-18 星期五 22:04:54
     */
    private static Object getValueByType(ResultSet rs, int type, String name) throws SQLException {
        switch (type) {
            case Types.NUMERIC:
                return rs.getLong(name);
            case Types.VARCHAR:
                return rs.getString(name);
            case Types.DATE:
                return rs.getDate(name);
            case Types.TIMESTAMP:
                return rs.getTimestamp(name).toString().substring(0, rs.getTimestamp(name).toString().length() - 2);
            case Types.INTEGER:
                return rs.getInt(name);
            case Types.DOUBLE:
                return rs.getDouble(name);
            case Types.FLOAT:
                return rs.getFloat(name);
            case Types.BIGINT:
                return rs.getLong(name);
            default:
                return rs.getObject(name);
        }
    }
}
