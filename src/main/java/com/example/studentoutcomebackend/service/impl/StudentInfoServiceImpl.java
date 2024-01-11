package com.example.studentoutcomebackend.service.impl;

import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.StudentInfoMapper;
import com.example.studentoutcomebackend.service.PermissionService;
import com.example.studentoutcomebackend.service.StudentInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// @Service 作用：标注业务层组件，表示这是一个业务层组件，是Spring框架中的注解
@Service
public class StudentInfoServiceImpl implements StudentInfoService {

    @Resource
    private StudentInfoMapper studentInfoMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PermissionService permissionService;

    @Override
    @Transactional
    public void login(String userName, String password) {
        // SM3 计算完再去和数据库匹配

        StudentInfo studentInfo = this.studentInfoMapper.selectByStuId(userName);

        if (studentInfo == null) {
            throw new BusinessException(601, "账号或密码错误");
        }
        studentInfo.checkPassword(password);
        // 看下账号是否有登录权限
        permissionService.throwIfDontHave(studentInfo.getUser_id(), "user.login", "账号不被允许登录");

        // 保存下登录状态~
        HttpSession session = request.getSession(true);
        session.setAttribute("stuInfo", studentInfo);
    }

    /**
     * 推荐！获取当前用户信息（主要是userId），没登陆就抛异常
     *
     * @return 当前登录的用户信息（只有userId是最可信的）
     */
    @Override
    @Transactional
    public StudentInfo getCurrentUserInfo() {
        HttpSession session = request.getSession(true);
        StudentInfo completeStudent = (StudentInfo) session.getAttribute("stuInfo");
        if (completeStudent == null) {
            // 没登陆，不行，直接抛异常
            throw new BusinessException(600, "未登录");
        }
        return completeStudent;
    }

    @Override
    @Transactional
    public void logout() {
        HttpSession session = request.getSession(true);
        session.removeAttribute("stuInfo");
    }

    @Override
    @Transactional
    public void changeUserPassword(String oldPassword, String newPassword) {
        StudentInfo studentInfo = getCurrentUserInfo();

        permissionService.throwIfDontHave("user.changePassword", null);  // 检验用户是否有修改密码的权限

        studentInfo.changePassword(oldPassword, newPassword);


    }

    @Override
    @Transactional
    public boolean userIdExist(int userId) {
        StudentInfo studentInfo = this.studentInfoMapper.selectUserByUserId(userId);
        return studentInfo != null;
    }

}
