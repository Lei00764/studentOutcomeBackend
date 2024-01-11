package com.example.studentoutcomebackend.entity;

import lombok.Getter;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11  14:07
 * @description:
 */
@Getter
public class Ticket {

    private int ticket_id;

    private String title;

    private String ticket_type;  // 工单类型

    private String content;  // 第一条内容

    private String create_time;

    private int user_id;

    private String status;  // 工单状态：'OPEN', 'IN_PROGRESS', 'CLOSED'

}
