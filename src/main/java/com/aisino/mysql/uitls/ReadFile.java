package com.aisino.mysql.uitls;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReadFile {
    public static Logger log = Logger.getLogger(ReadFile.class.getName());

    /**
     * @Method : readPropertiesFile
     * @Description : 读取*.properties格式属性文件
     * @Param filePath :
     * @ReturnType : java.util.Map<java.lang.String,java.lang.Object>
     * @Author : liuyang
     * @CreateDate : 2017-08-19 星期六 19:17:52
     */
    public static Map<String, Object> readPropertiesFile(String filePath) {
        Map<String, Object> propertiesMap = new HashMap<>();
        InputStream is = null;
        Properties pro = new Properties();
        try {
            is = new FileInputStream(filePath);
            pro.load(is);
            Enumeration keys = pro.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                propertiesMap.put(key, pro.getProperty(key));
                if (!key.contains("password")) {
//                    System.out.println(key + "：" + pro.getProperty(key));
                    log.info(key + "：" + pro.getProperty(key));
                } else {
//                    System.out.println(key + "：" + "*******");
                    log.info(key + "：" + "*******");
                }
            }
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
                log.error(e.getMessage());
                log.error("属性配置文件读取错误");
            }
        }
        return propertiesMap;
    }

    public static String getProjectRootPath() {
        File file = new File("");
        String projectPath = "";
        try {
            projectPath = file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectPath;
    }

}
