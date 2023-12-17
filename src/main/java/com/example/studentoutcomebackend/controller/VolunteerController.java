package com.example.studentoutcomebackend.controller;

import com.example.studentoutcomebackend.controller.base.BaseController;
import com.example.studentoutcomebackend.entity.vo.ResponseVO;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/volunteer")
public class VolunteerController extends BaseController {

    @Autowired
    private VolunteerService volunteerService;

    /**
     * 学生查看填写记录
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("/selectVolunteerInfo")
    public ResponseVO selectVolunteerInfo(@RequestBody Map<String, Object> requestMap) {
        try {
            int userId = (int) requestMap.get("user_id");
            Map<String, Object> volunteerInfo = volunteerService.getVolunteerInfoByUserId(userId);

            return getSuccessResponseVO(volunteerInfo);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 学生填写志愿者信息
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("/insertVolunteerInfo")
    public ResponseVO insertVolunteerInfo(@RequestBody Map<String, Object> requestMap) {
        try {
            int userId = (int) requestMap.get("user_id");
            String volName = (String) requestMap.get("vol_name");
            String volType = (String) requestMap.get("vol_type");
            String participateTime = (String) requestMap.get("participate_time");
            int durationDay = (int) requestMap.get("duration_day");
            int durationHour = (int) requestMap.get("duration_hour");
            String volDetail = (String) requestMap.get("vol_detail");
            String imageId = requestMap.containsKey("image_id") ? (String) requestMap.get("image_id") : null;

            volunteerService.insertVolunteerInfo(userId, volName, volType, participateTime, durationDay, durationHour, volDetail, imageId);

            return getSuccessResponseVO(null);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 学生删除志愿者信息
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("/deleteVolunteerInfo")
    public ResponseVO deleteVolunteerInfo(@RequestBody Map<String, Object> requestMap) {
        try {
            int userId = (int) requestMap.get("user_id");
            int volId = (int) requestMap.get("vol_id");
            volunteerService.deleteVolunteerInfo(volId, userId);

            return getSuccessResponseVO(null);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 获取审核页面信息
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("/getVolunteerVerification")
    public ResponseVO getVolunteerVerification(@RequestBody Map<String, Object> requestMap) {
        try {
            int userId = (int) requestMap.get("user_id");
            Map<String, Object> result = volunteerService.getVolunteerVerification(userId);

            return getSuccessResponseVO(result);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

}
