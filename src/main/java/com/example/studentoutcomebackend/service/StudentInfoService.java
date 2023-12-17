package com.example.studentoutcomebackend.service;


import com.example.studentoutcomebackend.entity.StudentInfo;

public interface StudentInfoService {

    void login(String userName, String password);

    StudentInfo getCurrentUserInfo();

    void logout();

    void changeUserPassword(String oldPassword, String newPassword);

    boolean userIdExist(int userId);

}
