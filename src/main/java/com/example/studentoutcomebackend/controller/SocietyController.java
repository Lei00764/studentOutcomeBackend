package com.example.studentoutcomebackend.controller;

import com.example.studentoutcomebackend.controller.base.BaseController;
import com.example.studentoutcomebackend.entity.Society.Society;
import com.example.studentoutcomebackend.entity.vo.ResponseVO;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.service.SocietyService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/12  17:08
 * @description:
 */
@RestController
@RequestMapping("api/social")
public class SocietyController extends BaseController {
    SocietyService societyService;

    @RequestMapping("/insertSocial")
    public ResponseVO insertSocial(@RequestBody Map<String, Object> requestMap) {

        try {
            String socialName = (String) requestMap.get("social_name");
            String participateTime = (String) requestMap.get("participate_time");
            int durationDay = (int) requestMap.get("duration_day");
            int durationHour = (int) requestMap.get("duration_hour");
            String socialDetail = (String) requestMap.get("social_detail");
            String imageId = requestMap.containsKey("image_id") ? (String) requestMap.get("image_id") : null;

            societyService.insertVolunteerInfo(socialName, participateTime, durationDay, durationHour, socialDetail, imageId);

            return getSuccessResponseVO(null);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }

    }
}
