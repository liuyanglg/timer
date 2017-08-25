package com.aisino.mysql.constants;

/**
 * @Package : com.aisino.mysql.constants
 * @Class : JdbcProperties
 * @Description : jdbc连接属性
 * @Author : liuyang
 * @CreateDate : 2017-08-18 星期五 22:16:38
 * @Version : V1.0.0
 * @Copyright : 2017 liuyang Inc. All rights reserved.
 */
public class JdbcProperties {
//    /*数据源1*/
//    public final static String CENTER_DRIVER = "com.mysql.jdbc.Driver";
//    public final static String CENTER_URL = "jdbc:mysql://127.0.0.1:3306/usercenter?useUnicode=true&characterEncoding=utf8&autoReconnect=true";
//    public final static String CENTER_USERNAME = "root";
//    public final static String CENTER_PASSWORD = "root";
//
//    /*数据源2*/
//    public final static String CMP_DRIVER = "com.mysql.jdbc.Driver";
//    public final static String CMP_URL = "jdbc:mysql://127.0.0.1:3306/dataserver?useUnicode=true&characterEncoding=utf8&autoReconnect=true";
//    public final static String CMP_USERNAME = "root";
//    public final static String CMP_PASSWORD = "root";

    /*数据源1*/
    public final static String CMP_DRIVER = "com.mysql.jdbc.Driver";
    public final static String CMP_URL = "jdbc:mysql://192.168.210.128:3306/dataserver?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull";
    public final static String CMP_USERNAME = "DataS_write";
    public final static String CMP_PASSWORD = "Aisino&Dat7";

    /*数据源2*/
    public final static String CENTER_DRIVER = "com.mysql.jdbc.Driver";
    public final static String CENTER_URL = "jdbc:mysql://192.168.210.18:3306/usercenter?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull";
    public final static String CENTER_USERNAME = "test";
    public final static String CENTER_PASSWORD = "kaifabuzhunlian!!!";

}
