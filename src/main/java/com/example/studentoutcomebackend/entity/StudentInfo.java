package com.example.studentoutcomebackend.entity;

import com.example.studentoutcomebackend.entity.Competition.Competition;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.CompetitionMapper;
import com.example.studentoutcomebackend.mapper.StudentInfoMapper;
import com.example.studentoutcomebackend.service.NoticeService;
import com.example.studentoutcomebackend.service.PermissionService;
import com.example.studentoutcomebackend.utils.SM3Util;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class StudentInfo {
    @Autowired
    private StudentInfoMapper studentInfoMapper;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private NoticeService noticeService;

    private static StudentInfo me;

    @PostConstruct
    private void init() {
        me = this;
        me.studentInfoMapper = this.studentInfoMapper;
        me.permissionService = this.permissionService;
        me.noticeService = this.noticeService;
    }

    private int user_id;

    private String stu_id;

    private String stu_name;

    private String user_password;

    private String grade;

    private int main_group_id;

    public void changePassword(String oldPassword, String newPassword){
        // 先匹配原来的密码
        String hashPwdOld = SM3Util.hashPassword(oldPassword, this);
        String hashPwdNew = SM3Util.hashPassword(newPassword, this);

        if (hashPwdOld.equals(hashPwdNew))
            throw new BusinessException(602, "新密码不能与旧密码相同");
        else if (newPassword.length() < 8)
            throw new BusinessException(603, "新密码太短，至少为8位");

        int a = studentInfoMapper.updateUserPassword(user_id, hashPwdOld, hashPwdNew);
        if (a <= 0)
            throw new BusinessException(604, "旧密码错误");
    }

    /**
     * 检测输入的密码是否和用户的密码匹配，不匹配就抛异常
     * @param password
     */
    public void checkPassword(String password){
        String hashPwd = SM3Util.hashPassword(password, this);
        if (!getUser_password().equals(hashPwd))
            throw new BusinessException(601, "账号或密码错误");
    }

    public int getMenuGroupId(){
        return main_group_id;
    }

    public void resetPassword(){
        String hashPwd = SM3Util.hashPassword(stu_id, this);
        me.studentInfoMapper.setPassword(user_id, hashPwd);
    }

    public void edit(String newStuId, String newStuName, String newGrade){
        me.studentInfoMapper.editStudent(user_id, newStuId, newStuName, newGrade);
        this.stu_name = newStuName;
        this.stu_id = newStuId;
        this.grade = newGrade;
    }

    public boolean hasUnreadNotification(){
        return me.noticeService.checkPersonalNotice(user_id);
    }
}
