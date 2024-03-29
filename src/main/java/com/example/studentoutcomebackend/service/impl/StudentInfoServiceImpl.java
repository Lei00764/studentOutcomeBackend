package com.example.studentoutcomebackend.service.impl;

import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.StudentInfoMapper;
import com.example.studentoutcomebackend.service.NoticeService;
import com.example.studentoutcomebackend.service.PermissionService;
import com.example.studentoutcomebackend.service.StudentInfoService;
import com.example.studentoutcomebackend.utils.SM3Util;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// @Service 作用：标注业务层组件，表示这是一个业务层组件，是Spring框架中的注解
@Service
public class StudentInfoServiceImpl implements StudentInfoService {

    @Resource
    private StudentInfoMapper studentInfoMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private NoticeService noticeService;

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


    @Override
    @Transactional
    public Map<String, Object> getStudentInfo(int userId) {
        StudentInfo studentInfo = this.studentInfoMapper.selectUserByUserId(userId);
        if (studentInfo == null) {
            throw new BusinessException(601, "用户不存在");
        }
        Map<String, Object> resObj = new HashMap<>();

        resObj.put("StudentInfo", studentInfo);
        return resObj;
    }
    @Override
    public Map<String, Object> selectStudentByCriteria(String keyword, String field, boolean isPrecise, int pageNo) {
        if (!field.equals("") && !field.equals("user_id") && !field.equals("stu_name") && !field.equals("stu_id")) {
            throw new BusinessException(601, "参数错误");
        }

        Map<String, Object> callParams = new HashMap<>();
        callParams.put("keyword", keyword);
        callParams.put("fieldName", field);
        callParams.put("precise", isPrecise ? 1 : 0);
        callParams.put("pageNo", pageNo);
        callParams.put("totalCount", -1);

        var students = studentInfoMapper.searchStudent(callParams);

        Map<String, Object> ans = new HashMap<>();

        ArrayList<Map<String, Object>> studentJson = new ArrayList<>();
        for(StudentInfo studentInfo : students){
            HashMap<String, Object> nowStudentJson = new HashMap<>();
            nowStudentJson.put("user_id", studentInfo.getUser_id());
            nowStudentJson.put("stu_id", studentInfo.getStu_id());
            nowStudentJson.put("stu_name", studentInfo.getStu_name());
            nowStudentJson.put("grade", studentInfo.getGrade());
            studentJson.add(nowStudentJson);
        }

        ans.put("students", studentJson);
        ans.put("totalCount", callParams.get("totalCount"));
        return ans;
    }

    @Override
    public StudentInfo selectUserByUserId(int userId) {
        StudentInfo studentInfo = studentInfoMapper.selectUserByUserId(userId);
        if(studentInfo == null)
            throw new BusinessException("用户不存在");
        return studentInfo;
    }

    @Override
    public void resetPassword(int userId) {
        StudentInfo studentInfo = selectUserByUserId(userId);
        studentInfo.resetPassword();
        noticeService.sendPersonalNotice(userId, "管理员重置了你的密码，请及时修改密码！", null);
    }

    @Override
    public void editStudent(int userId, String stuId, String stuName, String grade) {
        StudentInfo studentInfo = selectUserByUserId(userId);
        studentInfo.edit(stuId, stuName, grade);
    }

    @Override
    public void createStudent(String stuId, String stuName, String grade) {
        StudentInfo studentInfo = studentInfoMapper.selectByStuId(stuId);
        if(studentInfo != null)
            throw new BusinessException(666, "学号已存在");

        if(stuId.equals("") || stuName.equals(""))
            throw new BusinessException(665, "学号或姓名不能为空");


        Map<String, Object> callParams = new HashMap<>();
        callParams.put("stuId", stuId);
        callParams.put("stuName", stuName);
        callParams.put("grade", grade);
        callParams.put("userId", -1);
        studentInfoMapper.createStudent(callParams);
        studentInfo = studentInfoMapper.selectByStuId(stuId);
        studentInfo.resetPassword();
    }

}
