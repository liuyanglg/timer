package com.aisino.mysql.uitls;

import com.aisino.global.utils.CryptUtils;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class ConnectionFactory {
    public static Logger log = Logger.getLogger(ConnectionFactory.class.getName());
    private static String path = "/config/jdbc.properties";
    private static Connection conCmp = null;
    private static Connection conCenter = null;
    private static Map<String, Object> map = null;

    /**
     * @Method : getConnectionCmp
     * @Description : 连接数据源Cmp
     * @ReturnType : java.sql.Connection
     * @Author : liuyang
     * @CreateDate : 2017-08-19 星期六 16:36:08
     */
    public static Connection getConnectionCmp() {
        String projectPath = ReadFile.getProjectRootPath();
        if (map == null) {
            InputStream inputStream= CryptUtils.decryptFile(projectPath + path);
            map = ReadFile.readPropertiesFile(inputStream);
        }
        try {
            if (conCmp == null) {
                conCmp = JdbcUtils.getConnection((String) map.get("cmp.driver"), (String) map.get("cmp.url"), (String) map.get("cmp.username"), (String) map.get("cmp.password"));
//                conCmp = JdbcUtils.getConnection(CMP_DRIVER, CMP_URL, CMP_USERNAME, CMP_PASSWORD);

            } else if (conCmp.isClosed()) {
                conCmp = JdbcUtils.getConnection((String) map.get("cmp.driver"), (String) map.get("cmp.url"), (String) map.get("cmp.username"), (String) map.get("cmp.password"));
//                conCmp = JdbcUtils.getConnection(CMP_DRIVER, CMP_URL, CMP_USERNAME, CMP_PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error("数据库连接异常："+(String) map.get("cmp.url"));
        }
        return conCmp;
    }

    /**
     * @Method : getConnectionCenter
     * @Description : 连接数据源Center
     * @ReturnType : java.sql.Connection
     * @Author : liuyang
     * @CreateDate : 2017-08-19 星期六 16:36:45
     */
    public static Connection getConnectionCenter() {
        String projectPath = ReadFile.getProjectRootPath();
        if (map == null) {
            map = ReadFile.readPropertiesFile(projectPath + path);
        }
        try {
            
            if (conCenter == null) {
                conCenter = JdbcUtils.getConnection((String) map.get("center.driver"), (String) map.get("center.url"), (String) map.get("center.username"), (String) map.get("center.password"));
//                conCenter = JdbcUtils.getConnection(CENTER_DRIVER, CENTER_URL, CENTER_USERNAME, CENTER_PASSWORD);
            } else if (conCenter.isClosed()) {
                conCenter = JdbcUtils.getConnection((String) map.get("center.driver"), (String) map.get("center.url"), (String) map.get("center.username"), (String) map.get("center.password"));
//                conCenter = JdbcUtils.getConnection(CENTER_DRIVER, CENTER_URL, CENTER_USERNAME, CENTER_PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error("数据库连接异常："+(String) map.get("center.url"));
        }
        return conCenter;
    }
}
