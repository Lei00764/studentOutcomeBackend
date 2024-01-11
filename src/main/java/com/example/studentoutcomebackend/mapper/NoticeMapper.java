package com.example.studentoutcomebackend.mapper;

import com.example.studentoutcomebackend.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11  16:37
 * @description:
 */
@Mapper
public interface NoticeMapper {

    /**
     * 获取系统通知
     */
    @Select("select * from NOTICE where type = 'system'")
    List<Map<String, Object>> selectSystemNotice();

    /**
     * 发布系统通知
     */
    @Select("insert into NOTICE (type, content, send_time, is_read, related_link) values ('system', '系统通知', #{sendTime}, false, #{relatedLink})")
    void createSystemNotice(String content, String sendTime, String relatedLink);


    /**
     * 获取个人通知，指定 userId
     */
    @Select("select * from NOTICE where type = 'personal' and user_id = #{userId}")
    List<Map<String, Object>> selectPersonalNotice(int userId);

    /**
     * 发布个人通知
     */
    @Select("insert into NOTICE (type, content, send_time, is_read, related_link, user_id) values ('personal', #{content}, #{sendTime}, false, #{relatedLink}, #{userId})")
    void createPersonalNotice(int userId, String content, String sendTime, String relatedLink);

    /**
     * 将通知设置为已读
     */
    @Select("update NOTICE set is_read = true where notice_id = #{noticeId}")
    void updateNoticeReadStatus(int noticeId);

    /**
     * 查询指定 noticeId 通知是否存在
     */
    @Select("select * from NOTICE where notice_id = #{noticeId}")
    Map<String, Object> selectNoticeByNoticeId(int noticeId);
    
}
