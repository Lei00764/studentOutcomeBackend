package com.example.studentoutcomebackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/12  17:20
 * @description:
 */
@Mapper
public interface SocietyMapper {

    /**
     * 插入学生填写的社会实践信息
     */
    @Insert("INSERT INTO SOCIETY (user_id, social_name, participate_time, " +
            "duration_day, duration_hour, social_detail, image_id, verify_status) " +
            "VALUES (#{userId}, #{socialName}, #{participateTime}, " +
            "#{durationDay}, #{durationHour}, #{socialDetail}, #{imageId}, 0)")
    void insertSociety(int userId, String socialName, String participateTime, int durationDay, int durationHour, String socialDetail, String imageId);

}
