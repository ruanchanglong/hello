package com.chengxu.common.aop;


import com.chengxu.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 业务日志打印
 */
@Slf4j
@Aspect
@Component
public class HttpRequestAspect {
    public static long startTime;

    public static long costTime;

    /*@PointCut注解表示表示横切点，哪些方法需要被横切*/
    /*切点表达式*/
    @Pointcut("execution(* com.chengxu..*.controller..*.*(..))")
    public void print() {
    }

    /*@Before注解表示在具体的方法之前执行*/
    @Before("print()")
    public void before(JoinPoint joinPoint) {
        log.info("before...jump into AOP...begin...");
        startTime = System.currentTimeMillis();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String requestURI = request.getRequestURI();
        String remoteIpAddress = getIpAddressByRequest(request);
        String requestMethod = request.getMethod();
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        // 拼接出所有的入参
        Map<String, Object> params = generateParamMap(joinPoint);
        String requestParams = JsonUtils.toJson(params);
        log.info(
                "Request url is:{},client IP is:{},Request method is:{},Executing Class is:{},Executing function is:{},Request params is:{}",
                requestURI, remoteIpAddress, requestMethod, declaringTypeName, methodName, requestParams);
        log.info("before...jump into AOP...end...");
    }

    /*@After注解表示在方法执行之后执行*/
    @After("print()")
    public void after() {
        costTime = System.currentTimeMillis() - startTime;
        log.info("calculate endTime....execute after method");
    }

    /*@AfterReturning注解用于获取方法的返回值*/
    @AfterReturning(pointcut = "print()", returning = "object")
    public void getAfterReturn(Object object) {

        log.info("total time consuming:{}ms", costTime);
        if (null != object) {
            log.info("afterReturning={}", object.toString());
        }
        log.info("afterReturning is null");
    }

    /**
     * 从request中获取请求的IP地址
     *
     * @param request request
     * @return return the client ip
     */
    private String getIpAddressByRequest(HttpServletRequest request) {
        List<String> headerKeys = new LinkedList<String>();

            //这个大括号 就是 构造代码块 会在构造函数前 调用
        headerKeys.add("X-Forwarded-For");
        headerKeys.add("Proxy-Client-IP");
        headerKeys.add("WL-Proxy-Client-IP");
        headerKeys.add("HTTP_CLIENT_IP");
        headerKeys.add("HTTP_X_FORWARDED_FOR");

        String ip = null;
        //循环获取IP,直至获取到为止.
        for (String key : headerKeys) {
            ip = request.getHeader(key);
            if (!isEmptyOrUnKown(ip)) {
                break;
            }
        }
        if (isEmptyOrUnKown(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * validate the ip is empty or unkown
     *
     * @param ip request ip
     * @return return boolean  true:is empty or unkown;false：no
     */
    private boolean isEmptyOrUnKown(String ip) {
        return StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
    }

    private Map<String, Object> generateParamMap(JoinPoint joinPoint) {
        Map<String, Object> paramMap = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            Object arg = args[i];
            paramMap.put(parameterName, arg);
        }
        return paramMap;
    }
}
