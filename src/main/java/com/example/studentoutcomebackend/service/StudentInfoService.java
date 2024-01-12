package com.example.studentoutcomebackend.service;


import com.example.studentoutcomebackend.entity.StudentInfo;

import java.util.Map;

public interface StudentInfoService {

    void login(String userName, String password);

    StudentInfo getCurrentUserInfo();

    void logout();

    void changeUserPassword(String oldPassword, String newPassword);

    boolean userIdExist(int userId);

    Map<String, Object> getStudentInfo(int userId);

    Map<String, Object> selectStudentByCriteria(String keyword, String fieldName, boolean isPrecise, int pageNo);

    StudentInfo selectUserByUserId(int userId);
}
