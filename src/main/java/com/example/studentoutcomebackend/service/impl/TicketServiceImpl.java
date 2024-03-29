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

import java.util.List;
import java.util.Map;

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

        String senderType;
        int groupId = studentInfo.getMenuGroupId();
        if (groupId == 2) {
            senderType = "student";
        } else {
            senderType = "admin";
        }

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

    @Override
    public Map<String, Object> getTicketList() {
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();
        int userId = studentInfo.getUser_id();

        List<Map<String, Object>> tickets = ticketMapper.selectTicketByUserId(userId);

        Map<String, Object> result = new java.util.HashMap<>();

        result.put("tickets", tickets);

        return result;
    }

    @Override
    public Map<String, Object> getTicketInfo(int ticketId) {
        if (!isTicketIdExist(ticketId)) {
            throw new BusinessException(601, "工单不存在");
        }

        Map<String, Object> result = new java.util.HashMap<>();

        result.put("ticket", ticketMapper.selectTicketByTicketId(ticketId));

        return result;
    }

    @Override
    public Map<String, Object> getTicketContentList(int ticketId) {
        if (!isTicketIdExist(ticketId)) {
            throw new BusinessException(601, "工单不存在");
        }

        Map<String, Object> result = new java.util.HashMap<>();

        result.put("ticketContents", ticketMapper.selectTicketContentByTicketId(ticketId));

        return result;
    }

    /**
     * 获取工单列表
     */
    @Override
    public Map<String, Object> getAllTicketList() {
        List<Map<String, Object>> tickets = ticketMapper.selectAllTicket();

        Map<String, Object> result = new java.util.HashMap<>();

        result.put("tickets", tickets);

        return result;
    }
}
