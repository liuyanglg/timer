package com.aisino.mysql.constants;

/**
 * @Package : com.aisino.mysql.constants
 * @Class : SqlQuery
 * @Description : 拼装的SQL查询语句
 * @Author : liuyang
 * @CreateDate : 2017-08-18 星期五 22:17:14
 * @Version : V1.0.0
 * @Copyright : 2017 liuyang Inc. All rights reserved.
 */
public class SqlQuery {
    public final static int PAGE_SIZE = 50;//分页大小

    /*   #1   usercenter.`ucenter_user_service`表每天都有新增数据，需要将这些新增的数据与正式库和审核库数据建立关联关系*/
    /*   #1.1   查询 ucenter_user_service前一天新增的数据，即税号(c_taxnum)和服务单位ID(c_serviceid)*/
    public final static String SQL_QUERY_TB_US = "SELECT\n" +
            "  T_US.`c_taxnum` ,\n" +
            "  T_US.`c_serviceid` \n" +
            "  FROM\n" +
            "  usercenter.`ucenter_user_service` T_US\n" +
            "  WHERE\n" +
            "  T_US.`c_serviceid` IS NOT NULL\n" +
            "  AND T_US.`c_serviceid` != ''\n" +
            "  AND T_US.`c_taxnum` IS NOT NULL\n" +
            "  AND T_US.`c_taxnum` != ''\n" +
            "  AND T_US.`dt_adddate` >= DATE_SUB(now(), INTERVAL 1 DAY);";


    /*   #1.2  更新关系表tb_code_serviceid*/
    /*  #1.2.1
         #创建维护表(dataserver.`tb_cmp_card` )中六位代码(code)和(usercenter.`ucenter_user_service`)中服务单位ID(serviceid)关系表*/
    public final static String SQL_CREATE_TB_RM = "CREATE TABLE\n" +
            "IF NOT EXISTS dataserver.`tb_code_serviceid` (\n" +
            "  `code`      VARCHAR(16),\n" +
            "  `serviceid` VARCHAR(30),\n" +
            "  KEY `code_index` (`code`) USING BTREE\n" +
            ");";

    /*   #1.2.2
          # 以税号为内连接的连接条件
          # 与表usercenter.`ucenter_user_service`建立关联关系*/
    public final static String SQL_INSERT_TB_RM = "   INSERT INTO dataserver.`tb_code_serviceid` (`code`, `serviceid`) (\n" +
            "      SELECT\n" +
            "        T_M.`code`,\n" +
            "        ?\n" +
            "      FROM\n" +
            "        dataserver.`tb_cmp_card` T_M\n" +
            "      WHERE\n" +
            "        T_M.`taxid` = ?\n" +
            "        AND T_M.`taxid` IS NOT NULL\n" +
            "        AND T_M.`taxid` != ''\n" +
            "    );";

    /*   #1.3   更新关系表tb_code_taxid_serviceid*/
    /*   #1.3.1
          # 创建审核表(dataserver.`tb_cmp_card_audit` )中六位代码(code)、税号(taxid)和(usercenter.`ucenter_user_service`)中服务单位ID(serviceid)关系表*/
    public final static String SQL_CREATE_TB_RA = "CREATE TABLE\n" +
            "IF NOT EXISTS dataserver.`tb_code_taxid_serviceid` (\n" +
            "  `code`      VARCHAR(16),\n" +
            "  `taxid`     VARCHAR(30),\n" +
            "  `serviceid` VARCHAR(30),\n" +
            "  KEY `serviceid_index` (`serviceid`) USING BTREE\n" +
            ");";

    /*   #1.3.2
          # 以税号为内连接的连接条件
          # 与表usercenter.`ucenter_user_service`建立关联关系，分3种情况：*/
    /*   #1.3.2.1
          # 审核表code不为空,taixd为空*/
    public final static String SQL_INSERT_TB_RA_CASE1 = "INSERT INTO dataserver.`tb_code_taxid_serviceid` (`code`, `taxid`, `serviceid`) (\n" +
            "  SELECT DISTINCT\n" +
            "    T_A.`code`,\n" +
            "    T_A.`taxid`,\n" +
            "    ?\n" +
            "  FROM\n" +
            "    dataserver.`tb_cmp_card_audit` T_A\n" +
            "    JOIN dataserver.`tb_cmp_card` T_M ON T_A.`code` = T_M.`code`\n" +
            "  WHERE\n" +
            "    T_M.`taxid` = ?\n" +
            "    AND T_A.`code` IS NOT NULL\n" +
            "    AND T_A.`code` != ''\n" +
            "    AND T_A.`taxid` IS  NULL\n" +
            "    AND T_M.`taxid` IS NOT NULL\n" +
            ");";

    /*   #1.3.2.2
          # 审核表code不为空,taixd不为空*/
    public final static String SQL_INSERT_TB_RA_CASE2 = "INSERT INTO dataserver.`tb_code_taxid_serviceid` (`code`, `taxid`, `serviceid`) (\n" +
            "  SELECT DISTINCT\n" +
            "    T_A.`code`,\n" +
            "    T_A.`taxid`,\n" +
            "    ?\n" +
            "  FROM\n" +
            "    dataserver.`tb_cmp_card_audit` T_A\n" +
            "    JOIN dataserver.`tb_cmp_card` T_M ON T_A.`code` = T_M.`code`\n" +
            "  WHERE\n" +
            "    T_M.`taxid` = ?\n" +
            "    AND T_A.`code` IS NOT NULL\n" +
            "    AND T_A.`code` != ''\n" +
            "    AND T_A.`taxid` IS NOT NULL\n" +
            "    AND T_A.`taxid` != ''\n" +
            "    AND T_M.`taxid` IS NOT NULL\n" +
            "    AND T_M.`taxid` != ''\n" +
            ");";

    /*   #1.3.2.3
          # 审核表code不为空,taixd不为空*/
    public final static String SQL_INSERT_TB_RA_CASE3 = "INSERT INTO dataserver.`tb_code_taxid_serviceid` (`code`, `taxid`, `serviceid`) (\n" +
            "  SELECT DISTINCT\n" +
            "    T_A.`code`,\n" +
            "    T_A.`taxid`,\n" +
            "    ?\n" +
            "  FROM\n" +
            "    dataserver.`tb_cmp_card_audit` T_A\n" +
            "  WHERE\n" +
            "    T_A.`taxid` = ?\n" +
            "    AND T_A.`code` IS NULL\n" +
            "    AND T_A.`taxid` IS NOT NULL\n" +
            "    AND T_A.`taxid` != ''\n" +
            ");";

    /*   #2   正式库和审核库每天都有新增数据，需要将这些新增的数据与usercenter.`ucenter_user_service`所有数据建立关联关系*/
    /*   #2.1   数据过多，所以分页查询 ucenter_user_service所有数据，即税号(c_taxnum)和服务单位ID(c_serviceid)*/
    public final static String SQL_QUERY_TB_US_PAGE = "SELECT \n" +
            "  T_US.`c_taxnum` ,\n" +
            "  T_US.`c_serviceid` \n" +
            "FROM\n" +
            "  usercenter.`ucenter_user_service` T_US\n" +
            "WHERE\n" +
            "  T_US.`c_taxnum` IS NOT NULL\n" +
            "  AND T_US.`c_taxnum` != ''\n" +
            "  AND T_US.`c_serviceid` IS NOT NULL\n" +
            "  AND T_US.`c_serviceid` != ''\n" +
            "LIMIT ?, ?;";

    /*   #2.1   查询usercenter.`ucenter_user_service`数据数量*/
    public final static String SQL_QUERY_TB_US_COUNT = "SELECT count(*) AS size\n" +
            "FROM usercenter.`ucenter_user_service` T_US;";

    /*   #1.2  更新关系表tb_code_serviceid*/
    /*   #1.2.2
          # 以税号为内连接的连接条件
          # 与表usercenter.`ucenter_user_service`建立关联关系*/
    public final static String SQL_INSERT_YESTERDAY_TB_RM = "   INSERT INTO dataserver.`tb_code_serviceid` (`code`, `serviceid`) (\n" +
            "      SELECT\n" +
            "        T_M.`code`,\n" +
            "        ?\n" +
            "      FROM\n" +
            "        dataserver.`tb_cmp_card` T_M\n" +
            "      WHERE\n" +
            "        T_M.`taxid` = ?\n" +
            "        AND T_M.`taxid` IS NOT NULL\n" +
            "        AND T_M.`taxid` != ''\n" +
            "        AND T_M.`createtime` >= DATE_SUB(now(), INTERVAL 1 DAY)\n" +
            "    );";

     /*   #2.3   更新关系表tb_code_taxid_serviceid*/
    /*   #2.3.1
          # 以税号为内连接的连接条件
          # 与表usercenter.`ucenter_user_service`建立关联关系，分3种情况：*/
    /*   #2.3.1.1
          # 审核表code不为空,taixd为空*/
    public final static String SQL_INSERT_YESTERDAY_TB_RA_CASE1 = "INSERT INTO dataserver.`tb_code_taxid_serviceid` (`code`, `taxid`, `serviceid`) (\n" +
            "  SELECT DISTINCT\n" +
            "    T_A.`code`,\n" +
            "    T_A.`taxid`,\n" +
            "    ?\n" +
            "  FROM\n" +
            "    dataserver.`tb_cmp_card_audit` T_A\n" +
            "    JOIN dataserver.`tb_cmp_card` T_M ON T_A.`code` = T_M.`code`\n" +
            "  WHERE\n" +
            "    T_M.`taxid` = ?\n" +
            "    AND T_A.`code` IS NOT NULL\n" +
            "    AND T_A.`code` != ''\n" +
            "    AND T_A.`taxid` IS  NULL\n" +
            "    AND T_M.`taxid` IS NOT NULL\n" +
            "    AND T_A.`createtime` >= DATE_SUB(now(), INTERVAL 1 DAY)\n" +
            ");";

    /*   #2.3.1.2
      # 审核表code不为空,taixd不为空*/
    public final static String SQL_INSERT_YESTERDAY_TB_RA_CASE2 = "INSERT INTO dataserver.`tb_code_taxid_serviceid` (`code`, `taxid`, `serviceid`) (\n" +
            "  SELECT DISTINCT\n" +
            "    T_A.`code`,\n" +
            "    T_A.`taxid`,\n" +
            "    ?\n" +
            "  FROM\n" +
            "    dataserver.`tb_cmp_card_audit` T_A\n" +
            "    JOIN dataserver.`tb_cmp_card` T_M ON T_A.`code` = T_M.`code`\n" +
            "  WHERE\n" +
            "    T_M.`taxid` = ?\n" +
            "    AND T_A.`code` IS NOT NULL\n" +
            "    AND T_A.`code` != ''\n" +
            "    AND T_A.`taxid` IS NOT NULL\n" +
            "    AND T_A.`taxid` != ''\n" +
            "    AND T_M.`taxid` IS NOT NULL\n" +
            "    AND T_M.`taxid` != ''\n" +
            "    AND T_A.`createtime` >= DATE_SUB(now(), INTERVAL 1 DAY)\n" +
            ");";

    /*   #2.3.1.3
          # 审核表code不为空,taixd不为空*/
    public final static String SQL_INSERT_YESTERDAY_TB_RA_CASE3 = "INSERT INTO dataserver.`tb_code_taxid_serviceid` (`code`, `taxid`, `serviceid`) (\n" +
            "  SELECT DISTINCT\n" +
            "    T_A.`code`,\n" +
            "    T_A.`taxid`,\n" +
            "    ?\n" +
            "  FROM\n" +
            "    dataserver.`tb_cmp_card_audit` T_A\n" +
            "  WHERE\n" +
            "    T_A.`taxid` = ?\n" +
            "    AND T_A.`code` IS NULL\n" +
            "    AND T_A.`taxid` IS NOT NULL\n" +
            "    AND T_A.`taxid` != ''\n" +
            "    AND T_A.`createtime` >= DATE_SUB(now(), INTERVAL 1 DAY)\n" +
            ");";

    /*以下暂时没有用到*/
    public final static String SQL_QUERY_TB_M = "SELECT\n" +
            "  T_M.`code`,\n" +
            "  T_M.`taxid`\n" +
            "FROM\n" +
            "  dataserver.`tb_cmp_card` T_M\n" +
            "WHERE\n" +
            "  T_M.`code` IS NOT NULL\n" +
            "  AND T_M.`code` != ''\n" +
            "  AND T_M.`taxid` IS NOT NULL\n" +
            "  AND T_M.`taxid` != ''\n" +
            "  AND T_M.`createtime` >= DATE_SUB(now(), INTERVAL 1 DAY);";

    public final static String SQL_QUERY_TB_A_CASE1 = "SELECT\n" +
            "  T_A.`code`  AS `codeA`,\n" +
            "  T_A.`taxid` AS `taxidA`,\n" +
            "  T_M.`taxid` AS `taxidM`\n" +
            "FROM\n" +
            "  dataserver.`tb_cmp_card_audit` T_A\n" +
            "  JOIN dataserver.`tb_cmp_card` T_M ON T_A.`code` = T_M.`code`\n" +
            "WHERE\n" +
            "  T_A.`code` IS NOT NULL\n" +
            "  AND T_A.`code` != ''\n" +
            "  AND T_A.`taxid` IS NULL\n" +
            "  AND T_M.`taxid` IS NOT NULL\n" +
            "  AND T_A.`createtime` >= DATE_SUB(now(), INTERVAL 1 DAY);";

    public final static String SQL_QUERY_TB_A_CASE2 = "SELECT\n" +
            "  T_A.`code`  AS `codeA`,\n" +
            "  T_A.`taxid` AS `taxidA`,\n" +
            "  T_M.`taxid` AS `taxidM`\n" +
            "FROM\n" +
            "  dataserver.`tb_cmp_card_audit` T_A\n" +
            "  JOIN dataserver.`tb_cmp_card` T_M ON T_A.`code` = T_M.`code`\n" +
            "WHERE\n" +
            "  T_A.`code` IS NOT NULL\n" +
            "  AND T_A.`code` != ''\n" +
            "  AND T_A.`taxid` IS NOT NULL\n" +
            "  AND T_A.`taxid` != ''\n" +
            "  AND T_M.`taxid` IS NOT NULL\n" +
            "  AND T_M.`taxid` != ''\n" +
            "  AND T_A.`createtime` >= DATE_SUB(now(), INTERVAL 1 DAY);";

    public final static String SQL_QUERY_TB_A_CASE3 = "SELECT\n" +
            "  T_A.`code`  AS `codeA`,\n" +
            "  T_A.`taxid` AS `taxidA`\n" +
            "FROM\n" +
            "  dataserver.`tb_cmp_card_audit` T_A\n" +
            "WHERE\n" +
            "  T_A.`code` IS NULL\n" +
            "  AND T_A.`taxid` IS NOT NULL\n" +
            "  AND T_A.`taxid` != ''\n" +
            "  AND T_A.`createtime` >= DATE_SUB(now(), INTERVAL 1 DAY);";

    public final static String SQL_QUERY_TB_US_CASE2 = "SELECT DISTINCT\n" +
            "  ?                  AS `code`,\n" +
            "  T_US.`c_taxnum`    AS `taxid`,\n" +
            "  T_US.`c_serviceid` AS `serviceid`\n" +
            "FROM\n" +
            "  usercenter.`ucenter_user_service` T_US\n" +
            "WHERE\n" +
            "  T_US.`c_taxnum` = ?\n" +
            "  AND T_US.`c_taxnum` IS NOT NULL\n" +
            "  AND T_US.`c_taxnum` != ''\n" +
            "  AND T_US.`c_serviceid` IS NOT NULL\n" +
            "  AND T_US.`c_serviceid` != ''";

}
