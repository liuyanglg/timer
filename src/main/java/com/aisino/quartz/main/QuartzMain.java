package com.aisino.quartz.main;

import com.aisino.mysql.uitls.ReadFile;
import com.aisino.quartz.utils.QuartzManager;
import com.aisino.quartz.utils.UpdateTableJob;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class QuartzMain {
    public static Logger log = Logger.getLogger(QuartzMain.class.getName());

    private static String updateAtTime = "0 0 2 * * ?";
    private static String jobName = "updateTableJob";
    private static Map<String, Object> map = null;
    private static String path = "/config/task.properties";
    private static String log4jPath = "/config/log4j.properties";

    public static void main(String[] args) {
        initLog4j();

        String projectPath = ReadFile.getProjectRootPath();
        if (map == null) {
            map = ReadFile.readPropertiesFile(projectPath + path);
        }
        String timer = (String) map.get("timer");
        if (timer != null) {
            if (timer.trim().length() > 0) {
                updateAtTime = timer;
            }
        }
        QuartzManager.addJob(jobName, UpdateTableJob.class, updateAtTime);
    }

    /**
     * @Method : initLog4j
     * @Description : 初始化Log4j配置
     * @ReturnType : void
     * @Author : liuyang
     * @CreateDate : 2017-08-19 星期六 19:17:11
     */
    public static void initLog4j() {
        String projectPath = ReadFile.getProjectRootPath();
        String filePath = projectPath + log4jPath;
        InputStream is = null;
        Properties pro = new Properties();
        try {
            is = new FileInputStream(filePath);
            pro.load(is);
            PropertyConfigurator.configure(pro);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
