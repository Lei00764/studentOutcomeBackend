package com.example.studentoutcomebackend.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormatTools {

    private LocalDate parseDateStringToLocalDate(String dateString) {
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 解析日期字符串
        return LocalDate.parse(dateString, formatter);
    }
    
}
