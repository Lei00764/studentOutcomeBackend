package com.example.studentoutcomebackend.controller.base;

import com.example.studentoutcomebackend.entity.enums.ResponseCodeEnum;
import com.example.studentoutcomebackend.entity.vo.ResponseVO;
import com.example.studentoutcomebackend.exception.BusinessException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;

public class BaseController {

    protected ResponseVO getSuccessResponseVO(Object t) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setMsg(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }

    protected ResponseEntity<Resource> getBinaryResponse(Resource resource, String fileName) {
        return getBinaryResponse(resource, fileName, 200);
    }

    protected ResponseEntity<Resource> getBinaryResponse(Resource resource, String fileName, int code) {
        var ans =  ResponseEntity.ok().header("Code", String.valueOf(code));
        if(resource == null){
            return ans.body(null);
        }

        return ans.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName).body(resource);

    }

    protected <T> ResponseVO getSuccessResponseVO() {
        return getSuccessResponseVO(null);
    }

    protected <T> ResponseVO getBusinessErrorResponseVO(BusinessException e, T t) {
        ResponseVO vo = new ResponseVO();
        if (e.getCode() == null) {
            vo.setCode(ResponseCodeEnum.CODE_600.getCode());
        } else {
            vo.setCode(e.getCode());
        }
        vo.setMsg(e.getMessage());
        vo.setData(t);
        return vo;
    }

    protected <T> ResponseVO getServerErrorResponseVO(T t) {
        ResponseVO vo = new ResponseVO();
        vo.setCode(ResponseCodeEnum.CODE_500.getCode());
        vo.setMsg(ResponseCodeEnum.CODE_500.getMsg());
        vo.setData(t);
        return vo;
    }
}
