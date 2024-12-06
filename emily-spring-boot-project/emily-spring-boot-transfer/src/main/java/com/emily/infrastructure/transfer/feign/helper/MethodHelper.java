package com.emily.infrastructure.transfer.feign.helper;

import com.emily.infrastructure.aop.utils.MethodInvocationUtils;
import com.emily.infrastructure.sensitive.DataMaskUtils;
import com.emily.infrastructure.sensitive.SensitizeUtils;
import com.emily.infrastructure.sensitive.annotation.DesensitizeProperty;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Map;

/**
 * 请求服务类
 *
 * @author Emily
 * @since 4.0.7
 */
public class MethodHelper {
    /**
     * 1. 支持参数为实体类的脱敏处理；
     * 2. 支持单个参数的脱敏处理；
     *
     * @param invocation 方法切面对象
     * @return 返回调用方法的参数及参数值
     */
    public static Map<String, Object> getMethodArgs(MethodInvocation invocation) {
        return MethodInvocationUtils.getMethodArgs(invocation, o -> true,
                (parameter, value) -> {
                    if (value instanceof String str) {
                        if (parameter.isAnnotationPresent(DesensitizeProperty.class)) {
                            return DataMaskUtils.doGetProperty(str, parameter.getAnnotation(DesensitizeProperty.class).value());
                        } else {
                            return value;
                        }
                    } else {
                        return SensitizeUtils.acquireElseGet(value);
                    }
                });
    }

}
