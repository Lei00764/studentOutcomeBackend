package com.example.studentoutcomebackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11  14:49
 * @description:
 */
@Mapper
public interface TicketMapper {

    /**
     * 新建一条工单数据
     *
     * @param title
     * @param ticketType
     * @param content
     * @param create_time
     * @param user_id
     */
    @Insert("INSERT TICKET (title, ticket_type, content, create_time, user_id, status) VALUES (#{title}, #{ticketType}, #{content}, #{create_time}, #{user_id}, #{status})")
    void createTicket(String title, String ticketType, String content, String create_time, int user_id, String status);

    /**
     * 检查 ticketId 是否存在
     */
    @Select("SELECT ticket_id FROM TICKET WHERE ticket_id = #{ticketId}")
    Integer isTicketIdExist(int ticketId);

    /**
     * 回复工单
     */
    @Insert("INSERT TICKET_CONTENT (ticket_id, content, sender_type, sender_id, send_time) VALUES (#{ticketId}, #{content}, #{senderType}, #{senderId}, #{sendTime})")
    void replyTicket(int ticketId, String content, String senderType, int senderId, String sendTime);


    /**
     * 关闭 ticketId 工单，即将 status 设置为 CLOSE
     */
    @Update("UPDATE TICKET SET status = 'CLOSE' WHERE ticket_id = #{ticketId}")
    void closeTicket(int ticketId);

}
