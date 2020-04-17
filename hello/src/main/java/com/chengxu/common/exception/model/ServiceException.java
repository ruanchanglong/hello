package com.chengxu.common.exception.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * @author sunguangtao 2018-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends RuntimeException {
    private String code;
    private String message;
    private Object[] params;
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public ServiceException(String code) {
        super();
        this.code = code;
    }

    public ServiceException(String code, String message) {
        this(code);
        this.message = message;
    }

    public ServiceException(String code, Object[] params) {
        this(code);
        this.params = params;
    }

    public ServiceException(HttpStatus httpStatus, String code) {
        this(code);
        this.httpStatus = httpStatus;
    }

    public ServiceException(HttpStatus httpStatus, String code, Object[] params) {
        this(code, params);
        this.httpStatus = httpStatus;
    }
}
