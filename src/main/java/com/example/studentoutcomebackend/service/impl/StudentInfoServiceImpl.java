package com.example.studentoutcomebackend.service.impl;

import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.StudentInfoMapper;
import com.example.studentoutcomebackend.service.StudentInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// @Service 作用：标注业务层组件，表示这是一个业务层组件，是Spring框架中的注解
@Service
public class StudentInfoServiceImpl implements StudentInfoService {

    @Resource
    private StudentInfoMapper studentInfoMapper;

    @Override
    @Transactional
    public void login(String stu_id, String user_password) {
        System.out.println("stu_id: " + stu_id);
        StudentInfo studentInfo = this.studentInfoMapper.selectByStuIdAndPassword(stu_id, user_password);
        if (studentInfo == null) {
            throw new BusinessException("账号或密码错误");
        }
    }

}
