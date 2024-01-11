package com.example.studentoutcomebackend.service.impl;

import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.TicketMapper;
import com.example.studentoutcomebackend.service.PermissionService;
import com.example.studentoutcomebackend.service.StudentInfoService;
import com.example.studentoutcomebackend.service.TicketService;
import com.example.studentoutcomebackend.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11  14:47
 * @description:
 */
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketMapper ticketMapper;

    @Autowired
    StudentInfoService studentInfoService;

    @Autowired
    PermissionService permissionService;

    @Override
    public void createTicket(String title, String ticketType, String content) {

        // 获取当前用户信息
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();

        int userId = studentInfo.getUser_id();

        String create_time = TimeUtil.getCurrentTime();

        ticketMapper.createTicket(title, ticketType, content, create_time, userId, "OPEN");

    }

    /**
     * 检查 ticketId 是否存在
     */
    @Override
    public Boolean isTicketIdExist(int ticketId) {
        return ticketMapper.isTicketIdExist(ticketId) != null;
    }

    /**
     * @param ticketId 工单ID
     * @param content  回复内容
     */
    @Override
    public void replyTicket(int ticketId, String content) {
        if (!isTicketIdExist(ticketId)) {
            throw new BusinessException(601, "工单不存在");
        }

        // 获取当前用户信息
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();

        int userId = studentInfo.getUser_id();

        // 检查权限
        boolean isAdmin = permissionService.checkPermission(ticketId, "admin.reply");

        String senderType = isAdmin ? "admin" : "student";

        String sendTime = TimeUtil.getCurrentTime();

        ticketMapper.replyTicket(ticketId, content, senderType, userId, sendTime);
    }

    @Override
    public void closeTicket(int ticketId) {
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();  // 是否登录

        if (!isTicketIdExist(ticketId)) {
            throw new BusinessException(601, "工单不存在");
        }

        ticketMapper.closeTicket(ticketId);
    }

}
