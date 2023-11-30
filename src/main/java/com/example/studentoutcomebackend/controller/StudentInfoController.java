package com.example.studentoutcomebackend.controller;

import com.example.studentoutcomebackend.controller.base.BaseController;
import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.service.StudentInfoService;
import com.example.studentoutcomebackend.entity.vo.ResponseVO;
import com.example.studentoutcomebackend.service.impl.StudentInfoServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/StudentInfo")
public class StudentInfoController extends BaseController {

    @Autowired
    private StudentInfoService studentInfoService;

    @RequestMapping("/login")
    public ResponseVO login(@RequestBody Map<String, Object> requestMap) {
        String username = (String) requestMap.get("username");
        String password = (String) requestMap.get("password");


        if (username == null || password == null) {
            throw new BusinessException("请求参数错误");  // 抛异常
        }

        // TODO（若有）校验验证码

        // 校验账号密码
        studentInfoService.login(username, password);
        return getSuccessResponseVO();
    }

    @RequestMapping(value="/info", method = RequestMethod.GET)
    public ResponseVO getCurrentUserInfo(){
        StudentInfo studentInfo =  studentInfoService.getCurrentUserInfo();
        Map<String, Object> resObj = new HashMap<>();
        resObj.put("stu_id", studentInfo.getStu_id());
        resObj.put("stu_name", studentInfo.getStu_name());
        resObj.put("user_id", studentInfo.getUser_id());
        resObj.put("grade", studentInfo.getGrade());
        // 先放在这，有需求再改~
        resObj.put("avatar_url", "/webstatic/defaultAvatar.png");
        return getSuccessResponseVO(resObj);
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public ResponseVO userLogout(){
        studentInfoService.logout();
        return getSuccessResponseVO();
    }

    @RequestMapping(value="/updatePasswordStudent", method = RequestMethod.POST)
    public ResponseVO updateUserPassword(@RequestBody Map<String, Object> requestMap){
        String newPassword = (String) requestMap.get("new_password");
        String oldPassword = (String) requestMap.get("old_password");

        if (newPassword == null || oldPassword == null) {
            throw new BusinessException("请求参数错误");  // 抛异常
        }

        studentInfoService.updateUserPassword(oldPassword, newPassword);
        return getSuccessResponseVO();
    }
}
