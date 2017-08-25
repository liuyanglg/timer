package com.aisino.mysql.main;

import com.aisino.mysql.bean.JdbcConfig;
import com.aisino.mysql.constants.JdbcProperties;
import com.aisino.mysql.uitls.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.aisino.mysql.constants.SqlQuery.*;

public class ExecMain {
    public static volatile int threadCounter = 0;
    public static volatile int totalInsertCounter = 0;
    private static Connection centerConnection = null;
    private static Connection cmpConnection = null;

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "开始");//打印开始标记
        init();
        int pageSize = 50;
        int batchThreadNum = 5;
        threadCounter += 1 * batchThreadNum;
        updateAddUserServiceForA(pageSize, batchThreadNum);
        updateAddAudit(pageSize, batchThreadNum);
//        updateAddUserServiceForM(pageSize, batchThreadNum);
//        updateAddMaintain(pageSize, batchThreadNum);
        destroy();
    }


    public static void init() {
        JdbcConfig cmpConfig = new JdbcConfig();
        cmpConfig.setUrl(JdbcProperties.CMP_URL);
        cmpConfig.setUsername(JdbcProperties.CMP_USERNAME);
        cmpConfig.setPassword(JdbcProperties.CMP_PASSWORD);
        cmpConfig.setMaxWorkerThreads(8);
        JdbcConfig centerConfig = new JdbcConfig();
        centerConfig.setUrl(JdbcProperties.CENTER_URL);
        centerConfig.setUsername(JdbcProperties.CENTER_USERNAME);
        centerConfig.setPassword(JdbcProperties.CENTER_PASSWORD);
        centerConfig.setMaxWorkerThreads(8);

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
        while (threadCounter > 0) {
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            DBUtil.closeDataSource();
            System.out.println("关闭线程池");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void countDown(String threadName) {
        threadCounter--;
        System.out.println(threadName + "结束" + "，剩余线程数：" + threadCounter);
    }

    public static void countDown(String threadName,int insertSize) {
        threadCounter--;
        totalInsertCounter += insertSize;
        System.out.println("【"+threadName + "】结束，插入关系表" +insertSize+ "条数据，剩余线程数：" + threadCounter);
    }
    public static void updateAddUserServiceForA(int pageSize, int threadNum) {
        int totalSize = 0;
        try {
            centerConnection = DBUtil.getConnection("center");
            totalSize = JdbcUtils.count(SQL_QUERY_NEW_TB_US_COUNT, centerConnection);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (centerConnection != null) {
                try {
                    centerConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] keys = {"c_serviceid", "c_taxnum", "c_taxnum"};
        int taskSize = 0;
        int remain = 0;
        if (totalSize % (pageSize * threadNum) == 0) {
            taskSize = totalSize / threadNum;
        } else {
            remain = totalSize % (pageSize * threadNum);
            taskSize = (totalSize - remain) / threadNum;
        }

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
    }

    public static void updateAddUserServiceForM(int pageSize, int threadNum) {

        int totalSize = 0;
        try {
            centerConnection = DBUtil.getConnection("center");
            totalSize = JdbcUtils.count(SQL_QUERY_NEW_TB_US_COUNT, centerConnection);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (centerConnection != null) {
                try {
                    centerConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] keys = {"c_serviceid", "c_taxnum"};
        int taskSize = 0;
        int remain = 0;
        if (totalSize % (pageSize * threadNum) == 0) {
            taskSize = totalSize / threadNum;
        } else {
            remain = totalSize % (pageSize * threadNum);
            taskSize = (totalSize - remain) / threadNum;
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
    }

    public static void updateAddAudit(int pageSize, int threadNum) {

        int totalSize = 0;
        try {
            cmpConnection = DBUtil.getConnection("cmp");
            totalSize = JdbcUtils.count(SQL_QUERY_NEW_TB_A_COUNT, cmpConnection);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cmpConnection != null) {
                    cmpConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String[][] keys = {{"code", "taxid", "taxidM"}, {"code", "taxid", "serviceid"}};
        int taskSize = 0;
        int remain = 0;
        if (totalSize % (pageSize * threadNum) == 0) {
            taskSize = totalSize / threadNum;
        } else {
            remain = totalSize % (pageSize * threadNum);
            taskSize = (totalSize - remain) / threadNum;
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
    }


    public static void updateAddMaintain(int pageSize, int threadNum) {

        int totalSize = 0;
        try {
            cmpConnection = DBUtil.getConnection("cmp");
            totalSize = JdbcUtils.count(SQL_QUERY_NEW_TB_M_COUNT, cmpConnection);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cmpConnection != null) {
                    cmpConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String[][] keys = {{"code", "taxid"}, {"code", "serviceid"}};
        int taskSize = 0;
        int remain = 0;
        if (totalSize % (pageSize * threadNum) == 0) {
            taskSize = totalSize / threadNum;
        } else {
            remain = totalSize % (pageSize * threadNum);
            taskSize = (totalSize - remain) / threadNum;
        }

//        ThreadCounter threadCounter = new ThreadCounter(threadNum);
        for (int i = 0; i < threadNum; i++) {
            SubThread2 subThread = new SubThread2();
            String[] querySqlArray = {SQL_QUERY_NEW_TB_M, SQL_QUERY_OLD_TB_US_FOR_M};
            subThread.setQuerySql(querySqlArray);
            subThread.setInsertSql(SQL_INSERT_NEW_TB_RM);
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
    }




}
