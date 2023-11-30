package com.example.studentoutcomebackend.controller.base;

import com.example.studentoutcomebackend.entity.enums.ResponseCodeEnum;
import com.example.studentoutcomebackend.entity.vo.ResponseVO;
import com.example.studentoutcomebackend.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

// import javax.servlet.http.HttpServletRequest;  // Java17 不支持
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandlerController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);

    @ExceptionHandler(value = Exception.class)
    Object handleException(Exception e, HttpServletRequest request) {
        logger.error("请求错误，请求地址 {}，错误信息：", request.getRequestURL(), e);
        ResponseVO response = new ResponseVO();
        // 404
        if (e instanceof NoHandlerFoundException) {
            response.setCode(ResponseCodeEnum.CODE_404.getCode());
            response.setMsg(ResponseCodeEnum.CODE_404.getMsg());
        } else if (e instanceof BusinessException) {
            // 业务错误
            BusinessException biz = (BusinessException) e;
            response.setCode(biz.getCode());
            if (biz.getCode() == null) {
                response.setCode(ResponseCodeEnum.CODE_600.getCode());
            }
            response.setMsg(biz.getMessage());
        } else if (e instanceof BindException) {
            // 参数类型错误
            response.setCode(ResponseCodeEnum.CODE_600.getCode());
            response.setMsg(ResponseCodeEnum.CODE_600.getMsg());
        } else if (e instanceof DuplicateKeyException) {
            // 主键冲突
            response.setCode(ResponseCodeEnum.CODE_601.getCode());
            response.setMsg(ResponseCodeEnum.CODE_601.getMsg());
        } else if (e instanceof org.springframework.web.servlet.resource.NoResourceFoundException) {
            response.setCode(550);
            response.setMsg("接口未实现");
        } else {
            response.setCode(ResponseCodeEnum.CODE_500.getCode());
            response.setMsg(ResponseCodeEnum.CODE_500.getMsg());
        }
        return response;
    }

}
