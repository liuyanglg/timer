package com.aisino.mysql.uitls;

import org.junit.Test;

import java.io.File;
import java.util.Map;

public class ReadFileTest {
    @Test
    public void readPropertiesFile() throws Exception {
        File file = new File("");
        String projectPath=ReadFileTest.class.getResource("").getPath();
        projectPath = file.getCanonicalPath();
        String path =projectPath+ "/config/jdbc.properties";
        Map<String, Object> propertiesMap = ReadFile.readPropertiesFile(path);
        int i=0;
    }

}