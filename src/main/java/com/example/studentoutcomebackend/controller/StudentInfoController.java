package com.example.studentoutcomebackend.controller;

import com.example.studentoutcomebackend.controller.base.BaseController;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.service.StudentInfoService;
import com.example.studentoutcomebackend.entity.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/studentInfo")
public class StudentInfoController extends BaseController {

    @Autowired
    private StudentInfoService studentInfoService;

    @RequestMapping("/login")
    public ResponseVO login(@RequestBody Map<String, Object> requestMap) {
        String stu_id = (String) requestMap.get("stu_id");
        String user_password = (String) requestMap.get("user_password");


        if (stu_id == null || user_password == null) {
            throw new BusinessException("请求参数错误");  // 抛异常
        }

        // （若有）校验验证码

        // 校验账号密码
        studentInfoService.login(stu_id, user_password);
        return getSuccessResponseVO(null);
    }

}
