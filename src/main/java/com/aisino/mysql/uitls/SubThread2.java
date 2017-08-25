package com.aisino.mysql.uitls;

import com.aisino.quartz.utils.TimerTaskJob;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubThread2 extends Thread {
//    private ThreadCounter counter;
    private String[] querySql;
    private String insertSql;
    private int offset;
    private int pageSize;
    private int taskSize;
    private String[][] keys;

//    public SubThread2(ThreadCounter counter) {
//        this.counter = counter;
//    }


    public SubThread2() {
    }

    public SubThread2(String[] querySql, String insertSql, int offset, int pageSize, int taskSize, String[][] keys) {
        this.querySql = querySql;
        this.insertSql = insertSql;
        this.offset = offset;
        this.pageSize = pageSize;
        this.taskSize = taskSize;
        this.keys = keys;
    }

    public SubThread2(Runnable target, String[] querySql, String insertSql, int offset, int pageSize, int taskSize, String[][] keys) {
        super(target);
        this.querySql = querySql;
        this.insertSql = insertSql;
        this.offset = offset;
        this.pageSize = pageSize;
        this.taskSize = taskSize;
        this.keys = keys;
    }

    public SubThread2(ThreadGroup group, Runnable target, String[] querySql, String insertSql, int offset, int pageSize, int taskSize, String[][] keys) {
        super(group, target);
        this.querySql = querySql;
        this.insertSql = insertSql;
        this.offset = offset;
        this.pageSize = pageSize;
        this.taskSize = taskSize;
        this.keys = keys;
    }

    public SubThread2(String name, String[] querySql, String insertSql, int offset, int pageSize, int taskSize, String[][] keys) {
        super(name);
        this.querySql = querySql;
        this.insertSql = insertSql;
        this.offset = offset;
        this.pageSize = pageSize;
        this.taskSize = taskSize;
        this.keys = keys;
    }

    public SubThread2(ThreadGroup group, String name, String[] querySql, String insertSql, int offset, int pageSize, int taskSize, String[][] keys) {
        super(group, name);
        this.querySql = querySql;
        this.insertSql = insertSql;
        this.offset = offset;
        this.pageSize = pageSize;
        this.taskSize = taskSize;
        this.keys = keys;
    }

    public SubThread2(Runnable target, String name, String[] querySql, String insertSql, int offset, int pageSize, int taskSize, String[][] keys) {
        super(target, name);
        this.querySql = querySql;
        this.insertSql = insertSql;
        this.offset = offset;
        this.pageSize = pageSize;
        this.taskSize = taskSize;
        this.keys = keys;
    }

    public SubThread2(ThreadGroup group, Runnable target, String name, String[] querySql, String insertSql, int offset, int pageSize, int taskSize, String[][] keys) {
        super(group, target, name);
        this.querySql = querySql;
        this.insertSql = insertSql;
        this.offset = offset;
        this.pageSize = pageSize;
        this.taskSize = taskSize;
        this.keys = keys;
    }

    public SubThread2(ThreadGroup group, Runnable target, String name, long stackSize, String[] querySql, String insertSql, int offset, int pageSize, int taskSize, String[][] keys) {
        super(group, target, name, stackSize);
        this.querySql = querySql;
        this.insertSql = insertSql;
        this.offset = offset;
        this.pageSize = pageSize;
        this.taskSize = taskSize;
        this.keys = keys;
    }

    public String[] getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String[] querySql) {
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

    public String[][] getKeys() {
        return keys;
    }

    public void setKeys(String[][] keys) {
        this.keys = keys;
    }

    @Override
    public void run() {

        Connection cenConnection = null;
        Connection cmpConnection = null;
        String threadName = "[" + Thread.currentThread().getName() + "] ";
        try {
            System.out.println(threadName+"start......");
            cmpConnection = DBUtil.getConnection("cmp");
            cenConnection = DBUtil.getConnection("center");
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
                List<Map<String, Object>> queryList1 = null;
                if (querySql[0] != null && querySql[0].trim().length() > 0) {
                    long t = System.currentTimeMillis();
                    queryList1 = JdbcUtils.queryPage(querySql[0], cmpConnection, innerOffset, pageSize);
                }
//                System.out.println(this.getName()+"queryList1:" + queryList1.size());

                List<Map<String, Object>> queryList2 = new ArrayList<Map<String, Object>>();
                for(Map<String, Object>map:queryList1) {
                    int len = keys[0].length;
                    if(len==3) {
                        if (map.get("code") == null) {
                            keys[0][len - 1] = "taxid";
                        } else {
                            keys[0][len - 1] = "taxidM";
                        }
                    }
                    List<Map<String, Object>>singleList = JdbcUtils.queryObject(querySql[1], cenConnection, map, keys[0]);
                    if(singleList.size()>0){
                        queryList2.addAll(singleList);
                    }

                }
//                System.out.println(this.getName()+"queryList2:" + queryList2.size());
                if (insertSql != null && insertSql.trim().length() > 0) {
                    JdbcUtils.insertBatch(insertSql, cmpConnection, queryList2, keys[1]);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        counter.countDown();//计时器减1
        TimerTaskJob.countDown(threadName);
    }
}
