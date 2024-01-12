package com.example.studentoutcomebackend.service;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/12  17:16
 * @description:
 */
@Service
public interface SocietyService {

    void insertVolunteerInfo(String socialName, String participateTime, int durationDay, int durationHour, String socialDetail, String imageId);

}

