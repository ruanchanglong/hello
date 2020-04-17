package com.chengxu.common.exception.service;

import com.chengxu.common.exception.dao.ErrorCodeMapper;
import com.chengxu.common.exception.model.ErrorCodeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * service layer for error code
 *
 * @author sunguangtao 2018-10-22
 */
@Service
@Slf4j
public class ErrorCodeService
{
    private final ErrorCodeMapper errorCodeMapper;

    @Autowired
    public ErrorCodeService(ErrorCodeMapper errorCodeMapper)
    {
        this.errorCodeMapper = errorCodeMapper;
    }

    @Cacheable(value = "errorCodeCache", key = "#projectCode+#moduleCode+#errorCode")
    public ErrorCodeEntity getErrorCodeEntity(String projectCode, String moduleCode, String errorCode)
    {
        log.debug("Cache miss.");
        return errorCodeMapper.getErrorCodeEntity(projectCode, moduleCode, errorCode);
    }
}
