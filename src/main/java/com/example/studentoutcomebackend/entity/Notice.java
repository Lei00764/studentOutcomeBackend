package com.example.studentoutcomebackend.entity;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11  15:40
 * @description:
 */
public class Notice {

    private int notice_id;

    private String type;  // 个人通知 or 系统通知

    private String content;

    private String send_time;

    private Boolean is_read;

    private String related_link;

    private int user_id;

}
