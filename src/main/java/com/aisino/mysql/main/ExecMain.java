package com.aisino.mysql.main;

import com.aisino.mysql.uitls.DBUtil;
import com.aisino.mysql.uitls.JdbcUtils;
import com.aisino.mysql.uitls.SubThread1;
import com.aisino.mysql.uitls.ThreadCounter;

import java.sql.Connection;
import java.sql.SQLException;

import static com.aisino.mysql.constants.SqlQuery.*;

public class ExecMain {
//    public static  volatile int threadCounter=0;
    private static Connection centerConnection = null;
    private static Connection cmpConnection = null;
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "开始");//打印开始标记

        int totalSize = 0;
        try {
            centerConnection = DBUtil.getConnection("center");
            totalSize = JdbcUtils.count(SQL_QUERY_NEW_TB_US_COUNT, centerConnection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        ThreadCounter threadCounter = new ThreadCounter(threadNum);
        for (int i = 0; i < threadNum; i++) {
            SubThread1 subThread = new SubThread1(threadCounter);
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
    }


    public static void finish(String threadName) {
        System.out.println(threadName + "运行结束！");
    }

    public static int getTaskSize(int totalSize,int pageSize){
        int taskSize=0;

        return taskSize;
    }
}
