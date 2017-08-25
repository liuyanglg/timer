package com.aisino.mysql.uitls;

import com.aisino.quartz.utils.TimerTaskJob;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SubThread1 extends Thread {
    //    private ThreadCounter counter;
    private String querySql;
    private String insertSql;
    private int offset;
    private int pageSize;
    private int taskSize;
    private String[] keys;

    public SubThread1() {
    }

    public SubThread1(String querySql, String insertSql, int offset, int pageSize, int taskSize, String[] keys) {
        this.querySql = querySql;
        this.insertSql = insertSql;
        this.offset = offset;
        this.pageSize = pageSize;
        this.taskSize = taskSize;
        this.keys = keys;
    }

//    public SubThread1(ThreadCounter counter) {
//        this.counter = counter;
//    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public String getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    @Override
    public void run() {
        Connection cenConnection = null;
        Connection cmpConnection = null;
        String threadName = "[" + Thread.currentThread().getName() + "] ";
        try {
            System.out.println(threadName+"start......");
            cenConnection = DBUtil.getConnection("center");
            cmpConnection = DBUtil.getConnection("cmp");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int pages = 0;

        try {
            pages = taskSize / pageSize;
            if (taskSize % pageSize != 0) {
                pages += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("分页大小为0，导致除数为0引起的异常");
        }

        for (int i = 0; i < pages; i++) {
            int innerOffset = offset + i * pageSize;
            try {
                List<Map<String, Object>> queryList = null;
                if (querySql != null && querySql.trim().length() > 0) {
                    queryList = JdbcUtils.queryPage(querySql, cenConnection, innerOffset, pageSize);
                }
                if (insertSql != null && insertSql.trim().length() > 0) {
                    JdbcUtils.insertBatch(insertSql, cmpConnection, queryList, keys);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        counter.countDown();//计时器减1
        TimerTaskJob.countDown(threadName);
    }
}
