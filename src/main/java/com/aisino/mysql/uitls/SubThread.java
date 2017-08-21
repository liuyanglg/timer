package com.aisino.mysql.uitls;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class SubThread extends Thread {
    private int threadID;
    private String querySql;
    private String insertSql;
    private int offset;
    private int pageSize;
    private int taskSize;
    private String[] keys;

    public SubThread() {
    }

    public SubThread(String querySql, String insertSql, int offset, int pageSize, int taskSize, String[] keys) {
        this.querySql = querySql;
        this.insertSql = insertSql;
        this.offset = offset;
        this.pageSize = pageSize;
        this.taskSize = taskSize;
        this.keys = keys;
    }

    public int getThreadID() {
        return threadID;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

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
        System.out.println("Thread"+threadID+" starts...");
        for (int i = 0; i < taskSize / pageSize; i++) {
            offset += i * pageSize;
            Connection conCenter = ConnectionFactory.getConnectionCenter();
            List<Map<String, Object>> queryList = JdbcUtils.queryPage(querySql, conCenter, offset, pageSize);
            Connection conCmp = ConnectionFactory.getConnectionCmp();
            JdbcUtils.insertBatch(insertSql, conCmp, queryList,keys);
        }
        System.out.println("Thread"+threadID+" finish...");
    }
}
