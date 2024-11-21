package com.emily.infrastructure.web.request.interceptor;

import com.emily.infrastructure.common.ObjectUtils;
import com.emily.infrastructure.common.PrintExceptionUtils;
import com.emily.infrastructure.common.constant.AopOrderInfo;
import com.emily.infrastructure.common.constant.CharacterInfo;
import com.emily.infrastructure.date.DateComputeUtils;
import com.emily.infrastructure.date.DateConvertUtils;
import com.emily.infrastructure.date.DatePatternInfo;
import com.emily.infrastructure.json.JsonUtils;
import com.emily.infrastructure.logback.entity.BaseLogger;
import com.emily.infrastructure.logger.utils.PrintLoggerUtils;
import com.emily.infrastructure.sensitive.SensitiveUtils;
import com.emily.infrastructure.tracing.holder.LocalContextHolder;
import com.emily.infrastructure.tracing.holder.ServletStage;
import com.emily.infrastructure.web.exception.entity.BasicException;
import com.emily.infrastructure.web.exception.type.AppStatusType;
import com.emily.infrastructure.web.filter.helper.ServletHelper;
import com.emily.infrastructure.web.response.entity.BaseResponse;
import com.otter.infrastructure.servlet.RequestUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 在接口到达具体的目标即控制器方法之前获取方法的调用权限，可以在接口方法之前或者之后做Advice(增强)处理
 * 1. 在进入拦截器后首先设置单签上下文标识是控制器阶段，在此后发生的异常都会在拦截器中记录；
 * 2. 控制器参数校验异常不会进入此拦截器，具体的参数异常日志在全局异常AOP切面中记录；
 *
 * @author Emily
 * @since 1.0
 */
public class DefaultRequestMethodInterceptor implements RequestCustomizer {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRequestMethodInterceptor.class);

    /**
     * 拦截接口日志
     *
     * @param invocation 接口方法切面连接点
     */
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        //设置当前阶段标识，标记后如果发生异常，全局异常处理控制器不会记录日志
        LocalContextHolder.current().setServletStage(ServletStage.BEFORE_CONTROLLER);
        //获取请求参数
        Map<String, Object> paramsMap = ServletHelper.getApiArgs(invocation);
        //封装异步日志信息
        BaseLogger baseLogger = new BaseLogger();
        try {
            if (LOG.isDebugEnabled()) {
                LOG.warn("接口日志记录拦截器：START============>>{}", RequestUtils.getRequest().getRequestURL());
            }
            //调用真实的action方法
            Object response = invocation.proceed();
            // 返回值类型为ResponseEntity时，特殊处理
            return handleResponse(response, baseLogger);
        } catch (Exception ex) {
            //响应码
            baseLogger.status((ex instanceof BasicException) ? ((BasicException) ex).getStatus() : AppStatusType.EXCEPTION.getStatus())
                    //响应描述
                    .message((ex instanceof BasicException) ? ex.getMessage() : AppStatusType.EXCEPTION.getMessage())
                    //异常响应体
                    .body(PrintExceptionUtils.printErrorInfo(ex));
            throw ex;
        } finally {
            baseLogger.systemNumber(LocalContextHolder.current().getSystemNumber())
                    //事务唯一编号
                    .traceId(LocalContextHolder.current().getTraceId())
                    //请求参数
                    .requestParams(paramsMap)
                    //时间
                    .triggerTime(DateConvertUtils.format(LocalDateTime.now(), DatePatternInfo.YYYY_MM_DD_HH_MM_SS_SSS))
                    //客户端IP
                    .clientIp(LocalContextHolder.current().getClientIp())
                    //服务端IP
                    .serverIp(LocalContextHolder.current().getServerIp())
                    //请求URL
                    .url(StringUtils.substringBefore(String.valueOf(RequestUtils.getRequest().getRequestURL()), CharacterInfo.ASK_SIGN_EN))
                    //版本类型
                    .appType(LocalContextHolder.current().getAppType())
                    //版本号
                    .appVersion(LocalContextHolder.current().getAppVersion())
                    //耗时
                    .spentTime(DateComputeUtils.minusMillis(Instant.now(), LocalContextHolder.current().getStartTime()));
            //API耗时--用于返回值耗时字段设置
            LocalContextHolder.current().setSpentTime(baseLogger.getSpentTime());
            //异步记录接口响应信息
            PrintLoggerUtils.printRequest(baseLogger);
            if (LOG.isDebugEnabled()) {
                LOG.warn("接口日志记录拦截器：END<<============{}", RequestUtils.getRequest().getRequestURL());
            }
        }

    }

    /**
     * 对返回是ResponseEntity类型异常类型特殊处理，如：404 Not Fund接口处理
     *
     * @param response   接口返回值
     * @param baseLogger 日志信息封装器
     * @return 接口返回值
     */
    private Object handleResponse(Object response, BaseLogger baseLogger) {
        if (ObjectUtils.isEmpty(response)) {
            return response;
        }
        if (response instanceof ResponseEntity<?> entity) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                baseLogger.body(SensitiveUtils.acquireElseGet(entity.getBody(), BaseResponse.class));
                return entity;
            }
            Map<?, ?> dataMap = JsonUtils.toJavaBean(JsonUtils.toJSONString(entity.getBody()), Map.class);
            baseLogger.url(dataMap.get("path").toString())
                    .status(entity.getStatusCode().value())
                    .message(dataMap.get("error").toString());
            BaseResponse<Object> baseResponse = new BaseResponse<>()
                    .status(entity.getStatusCode().value())
                    .message(dataMap.get("error").toString());
            baseLogger.body(baseResponse);
            return new ResponseEntity<>(baseResponse, entity.getHeaders(), entity.getStatusCode());
        }
        // 设置响应体
        baseLogger.body(SensitiveUtils.acquireElseGet(response, BaseResponse.class));
        return response;
    }

    @Override
    public int getOrder() {
        return AopOrderInfo.REQUEST_INTERCEPTOR;
    }
}
