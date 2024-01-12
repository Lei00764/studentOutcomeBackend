package com.example.studentoutcomebackend.entity.Society;

import lombok.Getter;

@Getter
public class Society {

    private int id;

    private String social_name;  // 志愿活动名称

    private int user_id;

    private String social_type;  // 志愿活动类型

    private String participate_time;

    private int duration_day;

    private int duration_hour;

    private String social_detail;

    private String image_id;

    private int verify_status;

}
