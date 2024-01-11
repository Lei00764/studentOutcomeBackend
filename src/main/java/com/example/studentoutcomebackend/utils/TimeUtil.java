package com.example.studentoutcomebackend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11  15:01
 * @description:
 */
public class TimeUtil {

    /**
     * 获取当前时间，格式为 LocalDate Time
     */
    public static String getCurrentTime() {
        Date date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return formatter.format(date);
    }
    
}
