package com.example.studentoutcomebackend.service.impl;

import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.entity.Volunteer.Volunteer;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.CompetitionMapper;
import com.example.studentoutcomebackend.mapper.VolunteerMapper;
import com.example.studentoutcomebackend.service.StudentInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.studentoutcomebackend.service.VolunteerService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    @Resource
    VolunteerMapper volunteerMapper;

    @Autowired
    StudentInfoService studentInfoService;

    @Override
    @Transactional
    public Map<String, Object> getVolunteerInfoByUserId() {
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();
        int userId = studentInfo.getUser_id();
        
        List<Map<String, Object>> volunteerInfo = volunteerMapper.selectVolunteerByUserId(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("volunteerInfo", volunteerInfo);
        return result;
    }

    @Override
    @Transactional
    public void insertVolunteerInfo(String volName, String volType, String participateTime, int durationDay, int durationHour, String volDetail, String imageId) {
        // userId 是否存在
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();
        int userId = studentInfo.getUser_id();

        if (!studentInfoService.userIdExist(userId)) {
            throw new BusinessException(601, "用户 id 不存在");
        }

        volunteerMapper.insertVolunteer(userId, volName, volType, participateTime, durationDay, durationHour, volDetail, imageId);
    }

    @Override
    @Transactional
    public void deleteVolunteerInfo(int volId) {
        Map<String, Object> volunteerInfo = volunteerMapper.selectVolunteerByVolId(volId);
        if (volunteerInfo == null) {
            throw new BusinessException(601, "当前志愿服务不存在");
        }

        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();
        int userId = studentInfo.getUser_id();

        int userInfoId = (int) volunteerInfo.get("user_id");  // 数据库中记录的实际 userId

        if (userInfoId != userId) {
            throw new BusinessException("userId 和 volId 不对应");
        }

        volunteerMapper.deleteVolunteerByVolId(volId);  // 删除信息
    }

    @Override
    @Transactional
    public Map<String, Object> getVolunteerVerification() {
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();
        int userId = studentInfo.getUser_id();

        // userId 是否存在
        if (!studentInfoService.userIdExist(userId)) {
            throw new BusinessException(601, "用户 id 不存在");
        }

        List<Map<String, Object>> volunteersVerification = volunteerMapper.selectVolunteersVerification(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("volunteersVerification", volunteersVerification);
        return result;
    }

}
