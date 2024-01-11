package com.example.studentoutcomebackend.entity;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11  14:15
 * @description:
 */
public class TicketContent {

    private int ticket_id;

    private int sender_id;

    private String sender_type;  // 发送者类型：管理员 or 用户

    private String content;  // 每一条内容

    private String send_time;

}
