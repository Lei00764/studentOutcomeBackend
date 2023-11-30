package com.example.studentoutcomebackend.mapper;

import com.example.studentoutcomebackend.entity.StudentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.*;

@Mapper
public interface StudentInfoMapper {

    /**
     * 根据账号密码查询用户
     */
    @Select("SELECT * FROM STUDENT_INFO WHERE stu_id=#{stu_id} AND user_password=#{user_password}")
    StudentInfo selectByStuIdAndPassword(@Param("stu_id") String stu_id, @Param("user_password") String user_password);

}
