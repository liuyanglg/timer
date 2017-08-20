package com.aisino.mysql.uitls;

import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aisino.mysql.constants.JdbcProperties.*;
import static com.aisino.mysql.constants.SqlQuery.*;
import static com.aisino.mysql.main.ExecUpdateTable.getQueryFromTusList;
import static com.aisino.mysql.main.ExecUpdateTable.queryByPage;

public class JdbcUtilsTest {
    Connection conCmp = null;
    Connection conCenter = null;


    @Test
    public  void testCount() throws Exception{
        Connection conCenter = JdbcUtils.getConnection(CENTER_DRIVER, CENTER_URL, CENTER_USERNAME, CENTER_PASSWORD);
        List<Map<String, Object>> list = new ArrayList<>();

        String[] keys3 = {"offset"};
        int size = JdbcUtils.getQueryCount(SQL_QUERY_TB_US_COUNT, conCenter);
        int pageIndex=0;
        for(pageIndex=0;pageIndex<size/PAGE_SIZE;pageIndex++){
            List<Map<String, Object>> page = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            int offset = pageIndex * PAGE_SIZE;
            map.put("offset", offset);
            page.add(map);
            conCenter = JdbcUtils.getConnection(CENTER_DRIVER, CENTER_URL, CENTER_USERNAME, CENTER_PASSWORD);
            list = JdbcUtils.queryBySql(SQL_QUERY_TB_US_PAGE,conCenter,page,keys3);
        }
        System.out.println(size);
    }

    @Test
    public void test3() throws Exception {
        List<Map<String, Object>> tusList = getQueryFromTusList();
        String[] keys1 = {"c_serviceid", "c_taxnum"};
        conCmp = ConnectionFactory.getConnectionCmp();
        JdbcUtils.insertBatch(SQL_INSERT_TB_RM, conCmp, tusList, keys1);

        conCmp = ConnectionFactory.getConnectionCmp();
        JdbcUtils.insertBatch(SQL_INSERT_TB_RA_CASE1, conCmp, tusList, keys1);

        conCmp = ConnectionFactory.getConnectionCmp();
        JdbcUtils.insertBatch(SQL_INSERT_TB_RA_CASE2, conCmp, tusList, keys1);

        conCmp = ConnectionFactory.getConnectionCmp();
        JdbcUtils.insertBatch(SQL_INSERT_TB_RA_CASE3, conCmp, tusList, keys1);
    }

    @Test
    public void test4() throws Exception {
        conCenter= ConnectionFactory.getConnectionCenter();
        int size = JdbcUtils.getQueryCount(SQL_QUERY_TB_US_COUNT, conCenter);
        int pages = 0;
        if(size%PAGE_SIZE==0){
            pages = size / PAGE_SIZE;
        }else {
            pages = size / PAGE_SIZE+1 ;
        }
        for (int index = 0; index <pages;index++){
            int offset = index * PAGE_SIZE;
            conCenter= ConnectionFactory.getConnectionCenter();
            List<Map<String, Object>> list = queryByPage(SQL_QUERY_TB_US_PAGE, conCenter, offset,PAGE_SIZE);

            String []keys= {"c_serviceid", "c_taxnum"};
            conCmp = ConnectionFactory.getConnectionCmp();
            JdbcUtils.insertBatch(SQL_INSERT_YESTERDAY_TB_RM,conCmp,list,keys);
            conCmp = ConnectionFactory.getConnectionCmp();
            JdbcUtils.insertBatch(SQL_INSERT_YESTERDAY_TB_RA_CASE1,conCmp,list,keys);
            conCmp = ConnectionFactory.getConnectionCmp();
            JdbcUtils.insertBatch(SQL_INSERT_YESTERDAY_TB_RA_CASE2,conCmp,list,keys);
            conCmp = ConnectionFactory.getConnectionCmp();
            JdbcUtils.insertBatch(SQL_INSERT_YESTERDAY_TB_RA_CASE3,conCmp,list,keys);
        }
    }


}