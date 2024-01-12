package com.example.studentoutcomebackend.controller;

import com.example.studentoutcomebackend.controller.base.BaseController;
import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.service.PermissionService;
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
@RequestMapping("api/studentInfo")
public class StudentInfoController extends BaseController {

    @Autowired
    private StudentInfoService studentInfoService;
    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseVO login(@RequestBody Map<String, Object> requestMap) {
        try {
            String username = (String) requestMap.get("username");
            String password = (String) requestMap.get("password");

            // 校验账号密码
            studentInfoService.login(username, password);
            return getSuccessResponseVO();
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseVO getCurrentUserInfo() {
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();
        Map<String, Object> resObj = new HashMap<>();
        resObj.put("stu_id", studentInfo.getStu_id());
        resObj.put("stu_name", studentInfo.getStu_name());
        resObj.put("user_id", studentInfo.getUser_id());
        resObj.put("grade", studentInfo.getGrade());
        // 先放在这，有需求再改~
        resObj.put("avatar_url", "/webstatic/defaultAvatar.png");
        resObj.put("group_id", studentInfo.getMenuGroupId());
        return getSuccessResponseVO(resObj);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseVO userLogout() {
        studentInfoService.logout();
        return getSuccessResponseVO();
    }

    @RequestMapping(value = "/changePasswordStudent", method = RequestMethod.POST)
    public ResponseVO updateUserPassword(@RequestBody Map<String, Object> requestMap) {
        try {
            String newPassword = (String) requestMap.get("new_password");
            String oldPassword = (String) requestMap.get("old_password");

            studentInfoService.changeUserPassword(oldPassword, newPassword);
            return getSuccessResponseVO();
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 获取指定 userId 学生信息
     */
    @RequestMapping(value = "/getStudentInfo", method = RequestMethod.POST)
    public ResponseVO getStudentInfo(@RequestBody Map<String, Object> requestMap) {
        try {
            int userId = (int) requestMap.get("user_id");
            Map<String, Object> resObj = studentInfoService.getStudentInfo(userId);
            return getSuccessResponseVO(resObj);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    @RequestMapping(value = "/select", method = RequestMethod.POST)
    public ResponseVO selectStudent(@RequestBody Map<String, Object> requestMap) {
        permissionService.throwIfDontHave("teacher.editStudent.queryStudent", null);
        try {
            String fieldName = (String) requestMap.get("field");
            String keyword = (String) requestMap.get("keyword");
            boolean isPrecise = (Boolean) requestMap.get("precise");
            int pageNo = (int) requestMap.get("page");
            Map<String, Object> resMap = new HashMap<>();
            Map<String, Object> students = studentInfoService.selectStudentByCriteria(keyword, fieldName, isPrecise, pageNo);
            resMap.put("count", students.get("totalCount"));
            resMap.put("students", students.get("students"));

            return getSuccessResponseVO(resMap);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }
}
