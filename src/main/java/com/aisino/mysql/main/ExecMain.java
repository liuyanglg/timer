package com.aisino.mysql.main;

import com.aisino.mysql.bean.JdbcConfig;
import com.aisino.mysql.constants.JdbcProperties;
import com.aisino.mysql.uitls.DBUtil;
import com.aisino.mysql.uitls.JdbcUtils;
import com.aisino.mysql.uitls.SubThread2;
import com.aisino.mysql.uitls.ThreadCounter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.aisino.mysql.constants.SqlQuery.*;

public class ExecMain {
    //    public static  volatile int threadCounter=0;
    private static Connection centerConnection = null;
    private static Connection cmpConnection = null;

    public static void main(String[] args) {
        init();
        System.out.println(Thread.currentThread().getName() + "开始");//打印开始标记
        int threadNum = 5;
        int totalSize = 0;
        try {
            cmpConnection = DBUtil.getConnection("cmp");
            totalSize = JdbcUtils.count(SQL_QUERY_NEW_TB_A_COUNT, cmpConnection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[][] keys = {{"code", "taxid", "taxidM"},{"code", "taxid", "serviceid"}};
        int pageSize = 501;
        int taskSize = 0;
        int remain = 0;
        if (totalSize % (pageSize * threadNum) == 0) {
            taskSize = totalSize / threadNum;
        } else {
            remain = totalSize % (pageSize * threadNum);
            taskSize = (totalSize - remain) / threadNum;
        }
        if (taskSize == 0) {
            threadNum = 1;
        }
        ThreadCounter threadCounter = new ThreadCounter(threadNum);
        for (int i = 0; i < threadNum; i++) {
            SubThread2 subThread = new SubThread2(threadCounter);
            String[] querySqlArray = {SQL_QUERY_NEW_TB_A, SQL_QUERY_OLD_TB_US};
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
        while (true) {//等待所有子线程执行完
            if (!threadCounter.hasNext()) break;
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + "结束.");//打印结束标记
        try {
            DBUtil.closeDataSource();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        destroy();
    }


    public static void finish(String threadName) {
        System.out.println(threadName + "运行结束！");
    }

    public static void init() {
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

    public static void destroy() {
        try {
            DBUtil.closeDataSource();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
