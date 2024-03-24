package com.example.studentoutcomebackend.utils;

import java.io.File;

/**
 * @author asahi
 * @version 1.0
 * @project sebok-course-project
 * @description 提供文件存放位置
 * @date 2024/3/24 20:49:09
 */

public class FilePathLocator {
    String TEMP_FOLDER_PATH = "./.temp";
    String COMPETITION_NAME = "/competition.xls";

    public FilePathLocator() {
        File directory = new File(TEMP_FOLDER_PATH);

        // 检查文件夹是否存在，如果不存在，则创建
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
        }
    }

    public String getCompetitionLocation() {
        return TEMP_FOLDER_PATH + COMPETITION_NAME;
    }
}
