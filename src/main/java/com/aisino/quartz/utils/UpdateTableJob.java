package com.aisino.quartz.utils;

import com.aisino.mysql.constants.SqlQuery;
import com.aisino.mysql.uitls.ConnectionFactory;
import com.aisino.mysql.uitls.JdbcUtils;
import com.aisino.mysql.uitls.ReadFile;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aisino.mysql.constants.SqlQuery.*;

public class UpdateTableJob implements Job {
    public static Logger log = Logger.getLogger(UpdateTableJob.class.getName());

    private Connection conCenter = null;
    private Connection conCmp = null;
    private List<Map<String, Object>> queryFromTusList = null;
    private Map<String, Object> map = null;
    private String path = "/config/task.properties";
    private int pageSize = 20000;

    /**
     * @Method : execute
     * @Description : 更新关系表任务
     * @aram context :
     * @Return : void
     * @Author : liuyang
     * @CreateDate : 2017-08-19 星期六 03:27:08
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        initParams();
        /*更新用户中心每天新增的数据*/
        Long timeStart1 = System.currentTimeMillis();
        conCmp = ConnectionFactory.getConnectionCmp();
        log.info("开始更新数据库数据......");

        JdbcUtils.execSql(SQL_CREATE_TB_RM, conCmp);

        conCmp = ConnectionFactory.getConnectionCmp();
        JdbcUtils.execSql(SQL_CREATE_TB_RA, conCmp);

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

        Long timeEnd1 = System.currentTimeMillis();
        log.info("更新用户中心新增数据，耗时为：" + getTimeString(timeEnd1 - timeStart1));
        /*更新正式库、审核库中每天新增的数据*/
        Long timeStart2 = System.currentTimeMillis();
        conCenter = ConnectionFactory.getConnectionCenter();
        int size = JdbcUtils.getQueryCount(SQL_QUERY_TB_US_COUNT, conCenter);
        int pages = 0;
        if (size % pageSize == 0) {
            pages = size / pageSize;
        } else {
            pages = size / pageSize + 1;
        }
        for (int index = 0; index < pages; index++) {
            int offset = index * pageSize;
            conCenter = ConnectionFactory.getConnectionCenter();
            List<Map<String, Object>> list = queryByPage(SQL_QUERY_TB_US_PAGE, conCenter, offset, pageSize);

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
        Long timeEnd2 = System.currentTimeMillis();
        log.info("更新正式库和审核库新增数据，耗时为：" + getTimeString(timeEnd2 - timeStart2));
        log.info("数据库数据更新完成。");

    }

    public void initParams() {
        String projectPath = ReadFile.getProjectRootPath();
        if (map == null) {
            map = ReadFile.readPropertiesFile(projectPath + path);
        }
        Object object = map.get("pageSize");
        try {
            if (object != null) {
                pageSize = Integer.parseInt(object.toString());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Method : getTimeString
     * @Description : 将毫秒转换为小时
     * @Param useTime :
     * @ReturnType : java.lang.String
     * @Author : liuyang
     * @CreateDate : 2017-08-19 星期六 19:52:18
     */
    public  String getTimeString(Long useTime) {
        StringBuffer timeBuffer = new StringBuffer();
        long ms = useTime % 1000;
        long sec = (useTime / (1000)) % (60);
        long min = (useTime / (1000 * 60)) % (60);
        long hour = useTime / (1000 * 60 * 60);
        if (hour > 0) {
            timeBuffer.append(hour + "时");
        }
        if (min > 0) {
            timeBuffer.append(min + "分");
        }
        if (sec > 0) {
            timeBuffer.append(sec + "秒");
        }
        timeBuffer.append(ms + "毫秒");
        return timeBuffer.toString();
    }

    /**
     * @Method : getQueryFromTusList
     * @Description : 从usercenter.ucenter_user_service查询前一天的数据（taxid和code）
     * @Return : java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Author : liuyang
     * @CreateDate : 2017-08-18 星期五 23:38:42
     */
    public List<Map<String, Object>> getQueryFromTusList() {
        if (queryFromTusList == null) {
            queryFromTusList = new ArrayList<Map<String, Object>>();
        }
        Connection connection = ConnectionFactory.getConnectionCenter();
        queryFromTusList = JdbcUtils.queryBySql(SqlQuery.SQL_QUERY_NEW_TB_US, connection);
        return queryFromTusList;
    }

    /**
     * @Method : queryByPage
     * @Description : 从usercenter.ucenter_user_service分页查询所有数据（taxid和code）
     * @Param sql :
     * @Param connection :
     * @Param offset :
     * @ReturnType : java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Author : liuyang
     * @CreateDate : 2017-08-19 星期六 15:31:06
     */
    public List<Map<String, Object>> queryByPage(String sql, Connection connection, int offset, int pageSize) {
        List<Map<String, Object>> list = null;
        List<Map<String, Object>> page = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        String[] keys = {"offset", "pageSize"};
        map.put("offset", offset);
        map.put("pageSize", pageSize);
        page.add(map);
        list = JdbcUtils.queryBySql(sql, connection, page, keys);
        return list;
    }

}
