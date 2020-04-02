package com.chengxu.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;


import java.io.IOException;

/**
 * Json工具类封装
 */
@Slf4j
public class JsonUtils
{
    private static ObjectMapper objectMapper = new ObjectMapper();

    static
    {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * This method deserializes the specified Json into an object of the specified class.
     *
     */
    public static <T> T fromJson(String paramJson, Class<?> parametrized, Class<?>... parameterClasses)
    {
        log.debug(paramJson);
        try
        {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
            return objectMapper.readValue(paramJson, javaType);
        }
        catch (IOException ignore)
        {
            log.error("transfer json[{}] string to object exception:{}", paramJson,
                    ExceptionUtils.getStackTrace(ignore));
        }

        return null;
    }

    /**
     * This method deserializes the specified Json into an object of the specified class.
     */
    public static <T> T fromJson(String paramJson, Class<?> pParametrized, Class<?> cParametrized,
								 Class<?> parameterClass)
    {
        log.debug(paramJson);
        JavaType cType = objectMapper.getTypeFactory().constructParametricType(cParametrized, parameterClass);
        JavaType pType = objectMapper.getTypeFactory().constructParametricType(pParametrized, cType);
        try
        {
            return objectMapper.readValue(paramJson, pType);
        }
        catch (IOException ignore)
        {
            log.error(ExceptionUtils.getStackTrace(ignore));
        }
        return null;
    }

    /**
     * This method deserializes the specified Json into an object of the specified class.
     * Note that the desired object must have a default constructor, otherwise jackson can
     * not instantiate the desired object
     *
     */
    public static <T> T fromJson(String paramJson, Class<T> toClass)
    {
        log.debug(paramJson);
        try
        {
            return objectMapper.readValue(paramJson, toClass);
        }
        catch (IOException ignore)
        {
            log.error("transfer json[{}] string to object exception:{}", paramJson,
                    ExceptionUtils.getStackTrace(ignore));
        }
        return null;
    }

    /**
     * This method serializes the specified object into its equivalent Json representation.
     *
     */
    public static <T> String toJson(T object)
    {
        try
        {
            return objectMapper.writeValueAsString(object);
        }
        catch (Exception ignore)
        {
            log.error("transfer object to json exception:", ExceptionUtils.getStackTrace(ignore));
        }

        return null;
    }
}
