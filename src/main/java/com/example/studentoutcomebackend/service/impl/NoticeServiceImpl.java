package com.example.studentoutcomebackend.service.impl;

import com.example.studentoutcomebackend.entity.StudentInfo;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.mapper.CompetitionMapper;
import com.example.studentoutcomebackend.mapper.NoticeMapper;
import com.example.studentoutcomebackend.service.NoticeService;
import com.example.studentoutcomebackend.service.PermissionService;
import com.example.studentoutcomebackend.service.StudentInfoService;
import com.example.studentoutcomebackend.utils.TimeUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11  16:33
 * @description:
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Resource
    NoticeMapper noticeMapper;

    @Autowired
    StudentInfoService studentInfoService;

    @Autowired
    PermissionService permissionService;

    @Override
    public Map<String, Object> getSystemNotice() {
        List<Map<String, Object>> systemNoticeList = noticeMapper.selectSystemNotice();

        Map<String, Object> result = new HashMap<>();
        result.put("noticeList", systemNoticeList);

        return result;
    }

    @Override
    public void sendSystemNotice(String content, String related_link) {
        String sendTime = TimeUtil.getCurrentTime();

        noticeMapper.createSystemNotice(content, sendTime, related_link);
    }

    @Override
    public Map<String, Object> getPersonalNotice() {
        StudentInfo studentInfo = studentInfoService.getCurrentUserInfo();
        int userId = studentInfo.getUser_id();

        List<Map<String, Object>> personalNoticeList = noticeMapper.selectPersonalNotice(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("personalNotice", personalNoticeList);

        return result;
    }

    /**
     * 根据 userId 去发布个人通知 注意，这里不是学号
     */
    @Override
    public void sendPersonalNotice(int userId, String content, String relatedLink) {
        String sendTime = TimeUtil.getCurrentTime();

        noticeMapper.createPersonalNotice(userId, content, sendTime, relatedLink);
    }

    @Override
    public void markNoticeAsRead(int noticeId) {
        Map<String, Object> noticeList = selectNoticeByNoticeId(noticeId);
        if (!isNoticeExist(noticeId)) {
            throw new BusinessException(601, "通知不存在");
        }

        noticeMapper.updateNoticeReadStatus(noticeId);
    }

    @Override
    public boolean isNoticeExist(int noticeId) {
        return noticeMapper.selectNoticeByNoticeId(noticeId) != null;
    }

    @Override
    public Map<String, Object> selectNoticeByNoticeId(int noticeId) {
        Map<String, Object> notice = noticeMapper.selectNoticeByNoticeId(noticeId);
        if (notice == null) {
            throw new BusinessException(601, "通知不存在");
        }
        return notice;
    }

}

