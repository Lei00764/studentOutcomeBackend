package com.example.studentoutcomebackend.service;

import com.example.studentoutcomebackend.entity.Ticket;
import com.example.studentoutcomebackend.entity.TicketContent;

import java.util.Map;

/**
 * @program: studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11 14:41
 * @description: TicketService 接口定义
 */
public interface TicketService {

    /**
     * 创建一个新的工单
     *
     * @param title      工单标题
     * @param ticketType 工单类型
     * @param content    工单内容
     * @return 创建的 Ticket 对象
     */
    void createTicket(String title, String ticketType, String content);

    /**
     * 检查 ticketId 是否存在
     */
    Boolean isTicketIdExist(int ticketId);

    /**
     * 回复工单
     *
     * @param ticketId 工单ID
     * @param content  回复内容
     * @return 创建的 TicketContent 对象
     */
    void replyTicket(int ticketId, String content);

    /**
     * 关闭工单
     *
     * @param ticketId 工单ID
     * @return 更新后的 Ticket 对象
     */
    void closeTicket(int ticketId);

    /**
     * 获取工单列表
     */
    Map<String, Object> getTicketList();

    /**
     * 获取工单详情
     */
    Map<String, Object> getTicketInfo(int ticketId);

    /**
     * 获取工单回复列表
     */
    Map<String, Object> getTicketContentList(int ticketId);

    /**
     * 管理员获取工单列表
     */
    Map<String, Object> getAllTicketList();

}
