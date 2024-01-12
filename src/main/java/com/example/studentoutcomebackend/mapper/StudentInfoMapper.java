package com.example.studentoutcomebackend.mapper;

import com.example.studentoutcomebackend.entity.StudentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentInfoMapper {

    /**
     * 根据账号查询指定用户
     *
     * @param stuId
     * @return
     */
    @Select("SELECT * FROM STUDENT_INFO WHERE stu_id=#{stuId}")
    StudentInfo selectByStuId(String stuId);

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

    @Select("{call SelectStudent(" +
            "#{keyword,jdbcType=VARCHAR,mode=IN}," +
            "#{fieldName,jdbcType=VARCHAR,mode=IN}," +
            "#{precise,jdbcType=INTEGER,mode=IN}," +
            "#{pageNo,jdbcType=VARCHAR,mode=IN}," +
            "#{totalCount,jdbcType=INTEGER,mode=OUT})}")
    @Options(statementType = StatementType.CALLABLE)
    List<StudentInfo> searchStudent(Map<String, Object> params);
}
