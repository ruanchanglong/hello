package com.chengxu.common.exception.conf;


import com.chengxu.common.exception.handler.BaseExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * aspect for print exception stacktrace
 *
 * @author sunguangtao 2018-10-30
 */
@Slf4j
@Aspect
@Component
public class ExceptionAspect {
    @Pointcut("execution(* com.chengxu.common.exception.handler..*.*(..))")
    public void executePackage() {
    }

    @Before("executePackage()")
    public void beforeAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (Objects.nonNull(args) && args[0] instanceof Throwable) {
            log.error(BaseExceptionHandler.getStackTrace((Throwable) args[0]));
        }
    }

}
