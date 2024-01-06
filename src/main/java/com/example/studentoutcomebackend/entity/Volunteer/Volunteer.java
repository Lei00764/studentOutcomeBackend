package com.example.studentoutcomebackend.entity.Volunteer;

import lombok.Getter;

@Getter
public class Volunteer {

    private int id;

    private String vol_name;  // 志愿活动名称

    private int user_id;

    private String vol_type;  // 志愿活动类型

    private String participate_time;

    private int duration_day;

    private int duration_hour;

    private String vol_detail;

    private String image_id;

    private int verify_status;

}
