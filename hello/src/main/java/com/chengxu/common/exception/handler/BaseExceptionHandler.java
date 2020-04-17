package com.chengxu.common.exception.handler;


import com.chengxu.common.bean.ResponseBean;
import com.chengxu.common.exception.model.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author sunguangtao 2018-10-13
 */
public class BaseExceptionHandler {
    //private ResponseBean exceptionEntity = new ResponseBean<>();
    /**
     * 异常返回bean
     */
    private ResponseBean errorResponseBean = new ResponseBean<>();

    ResponseEntity<ResponseBean> wrapper(ServiceException ex, String requestId) {
        errorResponseBean.setCode(ex.getCode());
        errorResponseBean.setMessage(ex.getMessage());
        errorResponseBean.setRequestId(requestId);
        return new ResponseEntity<>(errorResponseBean, ex.getHttpStatus());
    }

    ResponseEntity<ResponseBean> wrapper(HttpStatus httpStatus) {
        errorResponseBean.setMessage(httpStatus.name());
        errorResponseBean.setCode(httpStatus.toString());
        return new ResponseEntity<>(errorResponseBean, httpStatus);
    }

    /**
     * see org.apache.commons.lang3.exception.ExceptionUtils;
     *
     * @param throwable exception
     * @return stack trace
     * @author sunguangtao 2018-10-30
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

}
