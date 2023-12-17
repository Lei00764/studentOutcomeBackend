package com.example.studentoutcomebackend.service;

import java.util.List;
import java.util.Map;

public interface VolunteerService {

    /**
     * 根据 userId 查看学生的填报记录
     *
     * @param userId
     * @return
     */
    Map<String, Object> getVolunteerInfoByUserId(int userId);

    /**
     * 插入一条学生志愿申报信息
     */
    void insertVolunteerInfo(int userId, String volName, String volType, String participateTime, int durationDay, int durationHour, String volDetail, String imageId);

    /**
     * 删除一条学生志愿申报信息
     */
    void deleteVolunteerInfo(int volId, int userId);

    /**
     * 获取指定 userId 待审核的填写记录
     */
    Map<String, Object> getVolunteerVerification(int userId);

}
