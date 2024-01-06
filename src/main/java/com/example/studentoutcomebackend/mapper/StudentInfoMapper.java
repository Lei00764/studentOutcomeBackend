package com.example.studentoutcomebackend.mapper;

import com.example.studentoutcomebackend.entity.StudentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.*;

@Mapper
public interface StudentInfoMapper {

    /**
     * 根据账号密码查询指定用户
     *
     * @param stuId
     * @param userPassword
     * @return
     */
    @Select("SELECT * FROM STUDENT_INFO WHERE stu_id=#{stuId} AND user_password=#{userPassword}")
    StudentInfo selectByStuIdAndPassword(String stuId, String userPassword);

    /**
     * 用户更新密码
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @Update({
            "UPDATE STUDENT_INFO",
            "SET user_password = #{newPassword}",
            "WHERE user_id = #{userId} and user_password = #{oldPassword}"
    })
    int updateUserPassword(int userId, String oldPassword, String newPassword);

    @Select("SELECT * FROM STUDENT_INFO WHERE user_id=#{userId}")
    StudentInfo selectUserByUserId(int userId);

}
