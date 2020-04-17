package com.chengxu.common.exception.handler;


import com.chengxu.common.bean.ResponseBean;
import com.chengxu.common.exception.model.ErrorCodeEntity;
import com.chengxu.common.exception.model.ServiceException;
import com.chengxu.common.exception.service.ErrorCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Global handler of exception
 * <p>
 * Error code must compliance with the specification defined at
 * 'http://10.10.7.5:8090/pages/viewpage.action?pageId=7776127'
 *
 * @author sunguangtao 2018-10-13
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler extends BaseExceptionHandler {
    private HttpStatus defaultHttpStatus = HttpStatus.BAD_REQUEST;
    private final ErrorCodeService errorCodeService;

    @Autowired
    public GlobalExceptionHandler(ErrorCodeService errorCodeService) {
        this.errorCodeService = errorCodeService;
    }

    /**
     * The standard error code is in the form of  'xxx.xxxxxx',
     * such as '201.002001'.
     *
     * @param exception business exception
     * @return response of exception
     */
    @ExceptionHandler(Exception.class)
    ResponseEntity<ResponseBean> handleServiceException(HttpServletRequest req, Exception exception) {
        String requestId = null;
        if (exception instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) exception;
            if (StringUtils.isEmpty(serviceException.getMessage())) {
                //获取请求requestId //todo  未生成请求id 暂固定
                requestId = req.getAttribute("RequestConstants.REQUEST_ID_KEY").toString();

                String code = serviceException.getCode();
                String projectKey = code.substring(0, 3);
                String moduleKey = code.substring(4, 7);
                String errorKey = code.substring(7, 10);

                // if the error code does not comply with the specification mentioned above, or
                // an database exception occurred when translating the error code, there will
                // also throws an exception.
                String defaultMessage = defaultHttpStatus.getReasonPhrase();
                serviceException.setMessage(defaultMessage);
                try {
                    ErrorCodeEntity errorCodeEntity = errorCodeService.getErrorCodeEntity(projectKey, moduleKey, errorKey);
                    if (Objects.isNull(errorCodeEntity)) {
                        return wrapper(serviceException,requestId);
                    }
                    if (Objects.isNull(serviceException.getParams())) {
                        serviceException.setMessage(errorCodeEntity.getErrorMessage());
                    } else {
                        serviceException.setMessage(String.format(errorCodeEntity.getErrorMessage(), serviceException.getParams()));
                    }
                } catch (Exception ignore) {
                    log.error("fail in parsing error code, cause {}", getStackTrace(ignore));
                }
            }
            return wrapper(serviceException,requestId);
        }
        return wrapper(HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
