package com.example.studentoutcomebackend.mapper;

import com.example.studentoutcomebackend.entity.Volunteer.Volunteer;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface VolunteerMapper {

    /**
     * 根据 user_id 查看学生填写的志愿申报信息
     */
    @Select("SELECT * FROM VOLUNTEER WHERE user_id = #{userId}")
    List<Map<String, Object>> selectVolunteerByUserId(int userId);

    /**
     * 插入学生填写的志愿申报信息
     */
    @Insert("INSERT INTO VOLUNTEER (user_id, vol_name, vol_type, participate_time, " +
            "duration_day, duration_hour, vol_detail, image_id, verify_status) " +
            "VALUES (#{userId}, #{volName}, #{volType}, #{participateTime}, " +
            "#{durationDay}, #{durationHour}, #{volDetail}, #{imageId}, 0)")
    void insertVolunteer(int userId, String volName, String volType, String participateTime, int durationDay, int durationHour, String volDetail, String imageId);

    /**
     * 根据 vol_id 查询学生填写的志愿申报信息
     */
    @Select("SELECT * FROM VOLUNTEER WHERE id = #{volId}")
    Map<String, Object> selectVolunteerByVolId(int volId);

    /**
     * 根据 vol_id 删除学生填写的志愿申报信息
     */
    @Delete("DELETE FROM VOLUNTEER WHERE id = #{volId}")
    int deleteVolunteerByVolId(int volId);

    /**
     * 获取指定 user_id 用户的所有需要被审核的数据
     */
    @Select("SELECT * FROM VOLUNTEER WHERE user_id = #{userId} AND verify_status = 0")
    List<Map<String, Object>> selectVolunteersVerification(int userId);

    /**
     * 更新指定 vol_id 的志愿申报信息
     */
    @Update("UPDATE VOLUNTEER SET vol_name = #{volName}, vol_type = #{volType}, participate_time = #{participateTime}, " +
            "duration_day = #{durationDay}, duration_hour = #{durationHour}, vol_detail = #{volDetail}, image_id = #{imageId} " +
            "WHERE id = #{volId}")
    void updateVolunteerByVolId(int volId, String volName, String volType, String participateTime, int durationDay, int durationHour, String volDetail, String imageId);

}
