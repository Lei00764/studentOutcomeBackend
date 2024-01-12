package com.example.studentoutcomebackend.controller;

import com.example.studentoutcomebackend.controller.base.BaseController;
import com.example.studentoutcomebackend.entity.vo.ResponseVO;
import com.example.studentoutcomebackend.exception.BusinessException;
import com.example.studentoutcomebackend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program:studentOutcomeBackend
 * @author: Xiang Lei
 * @email: xiang.lei.se@foxmail.com
 * @time: 2024/1/11  14:19
 * @description:
 */
@RestController
@RequestMapping("api/ticket")
public class TicketController extends BaseController {

    @Autowired
    private TicketService ticketService;

    /**
     * 创建工单
     *
     * @return
     */
    @RequestMapping("/createTicket")
    public ResponseVO createTicket(@RequestBody Map<String, Object> requestMap) {
        try {
            String ticketType = (String) requestMap.get("ticket_type");
            String title = (String) requestMap.get("title");
            String content = (String) requestMap.get("content");
            ticketService.createTicket(title, ticketType, content);
            return getSuccessResponseVO(null);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 回复工单
     *
     * @return
     */
    @RequestMapping("/replyTicket")
    public ResponseVO replyTicket(@RequestBody Map<String, Object> requestMap) {
        try {
            int ticketId = (int) requestMap.get("ticket_id");
            String content = (String) requestMap.get("content");

            ticketService.replyTicket(ticketId, content);

            return getSuccessResponseVO(null);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 关闭工单
     */
    @RequestMapping("/closeTicket")
    public ResponseVO closeTicket(@RequestBody Map<String, Object> requestMap) {
        try {
            int ticketId = (int) requestMap.get("ticket_id");

            ticketService.closeTicket(ticketId);

            return getSuccessResponseVO(null);
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 获取工单列表
     */
    @RequestMapping("/getTicketList")
    public ResponseVO getTicketList() {
        try {
            return getSuccessResponseVO(ticketService.getTicketList());
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }


    /**
     * 获取指定工单的详情
     */
    @RequestMapping("/getTicketInfo")
    public ResponseVO getTicketInfo(@RequestBody Map<String, Object> requestMap) {
        try {
            int ticketId = (int) requestMap.get("ticket_id");

            return getSuccessResponseVO(ticketService.getTicketInfo(ticketId));
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 获取指定工单的回复列表
     */
    @RequestMapping("/getTicketContentList")
    public ResponseVO getTicketContentList(@RequestBody Map<String, Object> requestMap) {
        try {
            int ticketId = (int) requestMap.get("ticket_id");

            return getSuccessResponseVO(ticketService.getTicketContentList(ticketId));
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

    /**
     * 管理员获取所有工单
     */
    @RequestMapping(value = "/getAllTicketList", method = RequestMethod.POST)
    public ResponseVO getAllTicketList() {
        try {
            return getSuccessResponseVO(ticketService.getAllTicketList());
        } catch (ClassCastException e) {
            throw new BusinessException(601, "请求参数错误");
        }
    }

}
