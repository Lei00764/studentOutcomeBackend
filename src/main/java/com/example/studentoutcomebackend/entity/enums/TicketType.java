package com.example.studentoutcomebackend.entity.enums;

/**
 * @program: studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11 14:12
 * @description: 枚举类，定义不同的订单类型
 */
public enum TicketType {

    // 订单类型
    COMPETITION_RELATED("竞赛相关"),
    PATENT_RELATED("专利相关"),
    PAPER_RELATED("论文相关"),
    VOLUNTEER_ACTIVITY_RELATED("志愿活动相关"),
    SOCIAL_ACTIVITY_RELATED("社会活动相关"),
    OTHER("其他");

    private final String description;

    TicketType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
