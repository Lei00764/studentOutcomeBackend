package com.example.studentoutcomebackend.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11  16:28
 * @description:
 */
@Service
public interface NoticeService {

    /**
     * 获取系统通知
     */
    Map<String, Object> getSystemNotice();

    /**
     * 发送系统通知
     */
    void sendSystemNotice(String content, String relatedLink);

    /**
     * 获取个人通知
     */
    Map<String, Object> getPersonalNotice(int userId);

    /**
     * 发送个人通知
     */
    void sendPersonalNotice(int userId, String content, String relatedLink);

    /**
     * 查询指定 noticeId 通知是否存在
     */
    boolean isNoticeExist(int noticeId);

    /**
     * 指定 noticeId 将通知设置为已读
     *
     * @param noticeId
     */
    void markNoticeAsRead(int noticeId);

    Map<String, Object> selectNoticeByNoticeId(int noticeId);

    /**
     * 清空系统消息
     */
    void clearPersonalNotice(int userId);

    /**
     * 获得某个用户有无未读的个人信息
     * @return
     */
    boolean checkPersonalNotice(int userId);
}
