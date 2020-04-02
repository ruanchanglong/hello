package com.chengxu.common.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseBean<T> {
    public static final String SUCCESS_CODE = "200";
    public static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";


    /**
     * 返回码 0成功，其他失败
     */
    private String code;

    /**
     * 描述信息
     */
    private String message;

    private String requestId;
    /**
     * 返回结果
     */

    @JsonProperty("data")
    private T result;

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    public ResponseBean(String code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public ResponseBean(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseBean() {
        // why this is not allowed.
        //this(ApiConstants.DEFAULT_RESULT);
    }
    public ResponseBean(T result) {
        this.setResult(result);
        this.code = SUCCESS_CODE;
        this.message = DEFAULT_SUCCESS_MESSAGE;
    }

}
