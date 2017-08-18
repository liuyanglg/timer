package com.aisino.mysql.constants;

public class SqlQuery {
    public final static String SQL_CREATE_TB_RM = "CREATE TABLE\n" +
            "IF NOT EXISTS dataserver.`tb_code_serviceid` (\n" +
            "  `code`      VARCHAR(16),\n" +
            "  `serviceid` VARCHAR(30),\n" +
            "  KEY `code_index` (`code`) USING BTREE,\n" +
            ");";

    public final static String SQL_CREATE_TB_RA = "CREATE TABLE\n" +
            "IF NOT EXISTS dataserver.`tb_code_taxid_serviceid` (\n" +
            "  `code`      VARCHAR(16),\n" +
            "  `taxid`     VARCHAR(30),\n" +
            "  `serviceid` VARCHAR(30),\n" +
            "  KEY `serviceid_index` (`serviceid`) USING BTREE\n" +
            ");";

    public final static String SQL_QUERY_TB_US = "SELECT\n" +
            "  T_US.`c_taxnum`,\n" +
            "  T_US.`c_serviceid`\n" +
            "  FROM\n" +
            "  usercenter.`ucenter_user_service` T_US\n" +
            "  WHERE\n" +
            "  T_US.`c_serviceid` IS NOT NULL\n" +
            "  AND T_US.`c_serviceid` != ''\n" +
            "  AND T_US.`c_taxnum` IS NOT NULL\n" +
            "  AND T_US.`c_taxnum` != ''\n" +
            "  AND T_US.`dt_adddate` >= DATE_SUB(now(), INTERVAL 1 DAY);";
}
