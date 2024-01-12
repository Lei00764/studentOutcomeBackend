package com.example.studentoutcomebackend.service.impl;

import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.SocietyMapper;
import com.example.studentoutcomebackend.mapper.VolunteerMapper;
import com.example.studentoutcomebackend.service.SocietyService;
import com.example.studentoutcomebackend.service.StudentInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/12  17:17
 * @description:
 */
@Service
public class SocietyServiceImpl implements SocietyService {

    @Resource
    SocietyMapper societyMapper;

    @Autowired
    StudentInfoService studentInfoService;


    @Override
    public void insertVolunteerInfo(String socialName, String participateTime, int durationDay, int durationHour, String socialDetail, String imageId) {
        // userId 是否存在
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();
        int userId = studentInfo.getUser_id();

        if (!studentInfoService.userIdExist(userId)) {
            throw new BusinessException(601, "用户 id 不存在");
        }

        societyMapper.insertSociety(userId, socialName, participateTime, durationDay, durationHour, socialDetail, imageId);
    }
}
