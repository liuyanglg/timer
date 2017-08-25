package com.aisino.mysql.uitls;

import com.aisino.mysql.bean.JdbcConfig;
import com.aisino.mysql.constants.JdbcProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aisino.mysql.constants.JdbcProperties.*;
import static com.aisino.mysql.constants.SqlQuery.*;
import static com.aisino.mysql.main.ExecUpdateTable.getQueryFromTusList;
import static com.aisino.mysql.main.ExecUpdateTable.queryByPage;

public class JdbcUtilsTest {
    public static final String CENTER = "center";
    public static final String CMP = "cmp";

    @Before
    public void init() {
        JdbcConfig cmpConfig = new JdbcConfig();
        cmpConfig.setUrl(JdbcProperties.CMP_URL);
        cmpConfig.setUsername(JdbcProperties.CMP_USERNAME);
        cmpConfig.setPassword(JdbcProperties.CMP_PASSWORD);
        cmpConfig.setMaxWorkerThreads(30);
        JdbcConfig centerConfig = new JdbcConfig();
        centerConfig.setUrl(JdbcProperties.CENTER_URL);
        centerConfig.setUsername(JdbcProperties.CENTER_USERNAME);
        centerConfig.setPassword(JdbcProperties.CENTER_PASSWORD);
        centerConfig.setMaxWorkerThreads(30);

        Map<String, JdbcConfig> datasourceMap = new HashMap<String, JdbcConfig>();
        datasourceMap.put("cmp", cmpConfig);
        datasourceMap.put("center", centerConfig);

        try {
            DBUtil.initDB(datasourceMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void destroy() {
        try {
            DBUtil.closeDataSource();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    Connection conCmp = null;
    Connection conCenter = null;


    @Test
    public void testCount() throws Exception {
        Connection conCenter = JdbcUtils.getConnection(CENTER_DRIVER, CENTER_URL, CENTER_USERNAME, CENTER_PASSWORD);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String[] keys3 = {"offset"};
        int size = JdbcUtils.getQueryCount(SQL_QUERY_TB_US_COUNT, conCenter);
        int pageIndex = 0;
        for (pageIndex = 0; pageIndex < size / PAGE_SIZE; pageIndex++) {
            List<Map<String, Object>> page = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            int offset = pageIndex * PAGE_SIZE;
            map.put("offset", offset);
            page.add(map);
            conCenter = JdbcUtils.getConnection(CENTER_DRIVER, CENTER_URL, CENTER_USERNAME, CENTER_PASSWORD);
            list = JdbcUtils.queryBySql(SQL_QUERY_TB_US_PAGE, conCenter, page, keys3);
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
        conCenter = ConnectionFactory.getConnectionCenter();
        int size = JdbcUtils.getQueryCount(SQL_QUERY_TB_US_COUNT, conCenter);
        int pages = 0;
        if (size % PAGE_SIZE == 0) {
            pages = size / PAGE_SIZE;
        } else {
            pages = size / PAGE_SIZE + 1;
        }
        for (int index = 0; index < pages; index++) {
            int offset = index * PAGE_SIZE;
            conCenter = ConnectionFactory.getConnectionCenter();
            List<Map<String, Object>> list = queryByPage(SQL_QUERY_TB_US_PAGE, conCenter, offset, PAGE_SIZE);

            String[] keys = {"c_serviceid", "c_taxnum"};
            conCmp = ConnectionFactory.getConnectionCmp();
            JdbcUtils.insertBatch(SQL_INSERT_YESTERDAY_TB_RM, conCmp, list, keys);
            conCmp = ConnectionFactory.getConnectionCmp();
            JdbcUtils.insertBatch(SQL_INSERT_YESTERDAY_TB_RA_CASE1, conCmp, list, keys);
            conCmp = ConnectionFactory.getConnectionCmp();
            JdbcUtils.insertBatch(SQL_INSERT_YESTERDAY_TB_RA_CASE2, conCmp, list, keys);
            conCmp = ConnectionFactory.getConnectionCmp();
            JdbcUtils.insertBatch(SQL_INSERT_YESTERDAY_TB_RA_CASE3, conCmp, list, keys);
        }
    }




    @Test
    public void test03() throws Exception {
        System.out.println(Thread.currentThread().getName() + "开始");//打印开始标记

        conCenter = DBUtil.getConnection("center");
        int totalSize = JdbcUtils.count(SQL_QUERY_NEW_TB_US_COUNT, conCenter);
        String[] keys = {"c_serviceid", "c_taxnum"};
        int pageSize = 50;
        int taskSize = 100;
        int remain = 0;
        if (taskSize > 0) {
            remain = totalSize % taskSize;
        }
        int threadNum = 0;
        if (taskSize > 0) {
            threadNum = totalSize / taskSize;
        }
//        ThreadCounter threadCounter = new ThreadCounter(threadNum);
        for (int i = 0; i < threadNum; i++) {
            SubThread1 subThread = new SubThread1();
            subThread.setQuerySql(SQL_QUERY_NEW_TB_US);
            subThread.setInsertSql(SQL_INSERT_TB_RM);
            if (i < threadNum - 1) {
                subThread.setTaskSize(taskSize);
            } else {
                subThread.setTaskSize(taskSize + remain);
            }
            subThread.setOffset(i * taskSize);
            subThread.setPageSize(pageSize);
            subThread.setKeys(keys);
            subThread.start();
        }
//        while (true) {//等待所有子线程执行完
//            if (!threadCounter.hasNext()) break;
//            Thread.sleep(20 * 1000);
//        }
        System.out.println(Thread.currentThread().getName() + "结束.");//打印结束标记
    }

    @Test
    public void test04() throws Exception {
        System.out.println(Thread.currentThread().getName() + "开始");//打印开始标记

        conCenter = DBUtil.getConnection("center");
        int totalSize = JdbcUtils.count(SQL_QUERY_NEW_TB_US_COUNT, conCenter);
        String[] keys = {"c_serviceid", "c_taxnum", "c_taxnum"};
        int pageSize = 50;
        int taskSize = 100;
        int remain = 0;
        if (taskSize > 0) {
            remain = totalSize % taskSize;
        }
        int threadNum = 0;
        if (taskSize > 0) {
            threadNum = totalSize / taskSize;
        }
//        ThreadCounter threadCounter = new ThreadCounter(threadNum);
        for (int i = 0; i < threadNum; i++) {
            SubThread1 subThread = new SubThread1();
            subThread.setQuerySql(SQL_QUERY_NEW_TB_US);
            subThread.setInsertSql(SQL_INSERT_TB_RA);
            if (i < threadNum - 1) {
                subThread.setTaskSize(taskSize);
            } else {
                subThread.setTaskSize(taskSize + remain);
            }
            subThread.setOffset(i * taskSize);
            subThread.setPageSize(pageSize);
            subThread.setKeys(keys);
            subThread.start();
        }
//        while (true) {//等待所有子线程执行完
//            if (!threadCounter.hasNext()) break;
//            Thread.sleep(20 * 1000);
//        }
        System.out.println(Thread.currentThread().getName() + "结束.");//打印结束标记
    }

    @Test
    public void test05() throws Exception {
        System.out.println(Thread.currentThread().getName() + "开始");//打印开始标记

        conCenter = DBUtil.getConnection("center");
        int totalSize = JdbcUtils.count(SQL_QUERY_NEW_TB_A_COUNT, conCenter);
        String[][] keys = {{"code", "taxid", "taxidM"},{"code", "taxid", "serviceid"}};
        int pageSize = 50;
        int taskSize = 100;
        int remain = 0;
        if (taskSize > 0) {
            remain = totalSize % taskSize;
        }
        int threadNum = 0;
        if (taskSize > 0) {
            threadNum = totalSize / taskSize;
        }
//        ThreadCounter threadCounter = new ThreadCounter(threadNum);
        for (int i = 0; i < threadNum; i++) {
            SubThread2 subThread = new SubThread2();
            String[] querySqlArray = {SQL_QUERY_NEW_TB_A, SQL_QUERY_OLD_TB_US_FOR_A};
            subThread.setQuerySql(querySqlArray);
            subThread.setInsertSql(SQL_INSERT_NEW_TB_RA);
            if (i < threadNum - 1) {
                subThread.setTaskSize(taskSize);
            } else {
                subThread.setTaskSize(taskSize + remain);
            }
            subThread.setOffset(i * taskSize);
            subThread.setPageSize(pageSize);
            subThread.setKeys(keys);
            subThread.start();
        }
//        while (true) {//等待所有子线程执行完
//            if (!threadCounter.hasNext()) break;
//            Thread.sleep(20 * 1000);
//        }
        System.out.println(Thread.currentThread().getName() + "结束.");//打印结束标记
    }

    public static void finish(String threadName) {
        System.out.println(threadName + "运行结束！");
    }
}