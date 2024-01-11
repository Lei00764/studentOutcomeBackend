package com.example.studentoutcomebackend.controller;

import com.example.studentoutcomebackend.controller.base.BaseController;
import com.example.studentoutcomebackend.entity.vo.ResponseVO;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.service.CompetitionService;
import com.example.studentoutcomebackend.service.NoticeService;
import com.example.studentoutcomebackend.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11  15:42
 * @description:
 */
@RestController
@RequestMapping("api/notice")
public class NoticeController extends BaseController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取系统通知
     *
     * @return
     */
    @RequestMapping("/getSystemNotice")
    public ResponseVO getSystemNotice() {
        try {
            return getSuccessResponseVO(noticeService.getSystemNotice());
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 发送系统通知
     */
    @RequestMapping("/sendSystemNotice")
    public ResponseVO sendSystemNotice(@RequestBody Map<String, Object> requestMap) {
        permissionService.throwIfDontHave("notice.edit", "你没有发布通知的权限！");

        try {
            String content = (String) requestMap.get("content");
            String relatedLink = (String) requestMap.get("related_link");

            noticeService.sendSystemNotice(content, relatedLink);

            return getSuccessResponseVO(null);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 获取个人通知
     *
     * @return
     */
    @RequestMapping("/getPersonalNotice")
    public ResponseVO getPersonalNotice() {
        try {
            return getSuccessResponseVO(noticeService.getPersonalNotice());
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 发送个人通知
     */
    @RequestMapping("/sendPersonalNotice")
    public ResponseVO sendPersonalNotice(@RequestBody Map<String, Object> requestMap) {
        permissionService.throwIfDontHave("notice.edit", "你没有发布通知的权限！");

        try {
            int userId = (int) requestMap.get("user_id");  // 发给谁
            String content = (String) requestMap.get("content");
            String relatedLink = (String) requestMap.get("related_link");

            noticeService.sendPersonalNotice(userId, content, relatedLink);

            return getSuccessResponseVO(null);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    @RequestMapping("/markAsRead")
    public ResponseVO markNoticeAsRead(@RequestBody Map<String, Object> requestMap) {
        try {
            int noticeId = (int) requestMap.get("notice_id");

            noticeService.markNoticeAsRead(noticeId);
            return getSuccessResponseVO(null);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "处理请求时发生错误");
        }
    }

}
