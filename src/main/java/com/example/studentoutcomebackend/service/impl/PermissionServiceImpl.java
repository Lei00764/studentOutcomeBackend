package com.example.studentoutcomebackend.service.impl;

import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.PermissionMapper;
import com.example.studentoutcomebackend.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean checkPermission(int userId, String permissionName) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("permissionName", permissionName);
        params.put("result", null); // Initializing the output parameter

        permissionMapper.checkPermission(params);
        return (Integer) params.get("result") == 1;
    }

    @Override
    public boolean checkPermission(String permissionName) {
        HttpSession session = request.getSession(true);
        StudentInfo completeStudent = (StudentInfo)session.getAttribute("stuInfo");
        if(completeStudent==null){
            // 没登陆，使用默认权限
            return checkPermission(-1, permissionName);
        }else{
            return checkPermission(completeStudent.getUser_id(), permissionName);
        }
    }

    @Override
    public void throwIfDontHave(int userId, String permissionName, String hint) {
        boolean ans = checkPermission(userId, permissionName);
        if(!ans){
            if(hint == null){
                hint = "请求被拒绝：无" + permissionName + "权限！";
            }
            throw new BusinessException(403, hint);
        }
    }

    @Override
    public void throwIfDontHave(String permissionName, String hint) {
        boolean ans = checkPermission(permissionName);
        if(!ans){
            if(hint == null){
                hint = "请求被拒绝：无" + permissionName + "权限！";
            }
            throw new BusinessException(403, hint);
        }
    }
}
