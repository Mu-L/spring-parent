package com.emily.infrastructure.transfer.rest.interceptor.client;

import com.emily.infrastructure.aop.constant.AopOrderInfo;
import com.emily.infrastructure.common.PrintExceptionUtils;
import com.emily.infrastructure.common.constant.HeaderInfo;
import com.emily.infrastructure.date.DateComputeUtils;
import com.emily.infrastructure.date.DateConvertUtils;
import com.emily.infrastructure.date.DatePatternInfo;
import com.emily.infrastructure.logback.entity.BaseLogger;
import com.emily.infrastructure.logger.utils.PrintLoggerUtils;
import com.emily.infrastructure.tracing.holder.LocalContextHolder;
import com.emily.infrastructure.transfer.rest.factory.HttpRequestFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * RestTemplate拦截器
 *
 * @author Emily
 * @since 2020/08/17
 */
public class DefaultRestTemplateInterceptor implements RestTemplateCustomizer {

    /**
     * RestTemplate拦截方法
     *
     * @param request   请求对象
     * @param body      请求体
     * @param execution 请求方法执行对象
     * @return 响应对象
     * @throws IOException 抛出异常
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        //设置事务标识
        request.getHeaders().set(HeaderInfo.TRACE_ID, LocalContextHolder.current().getTraceId());
        //创建拦截日志信息
        BaseLogger.Builder builder = BaseLogger.newBuilder()
                //系统编号
                .withSystemNumber(LocalContextHolder.current().getSystemNumber())
                //生成事物流水号
                .withTraceId(LocalContextHolder.current().getTraceId())
                //请求URL
                .withUrl(request.getURI().toString())
                //请求参数
                .withRequestParams(HttpRequestFactory.getArgs(request.getHeaders(), body));
        //开始计时
        Instant start = Instant.now();
        try {
            //调用接口
            ClientHttpResponse response = execution.execute(request, body);
            //响应数据
            Object responseBody = HttpRequestFactory.getResponseBody(StreamUtils.copyToByteArray(response.getBody()));
            //响应结果
            builder.withBody(responseBody);

            return response;
        } catch (IOException ex) {
            //响应结果
            builder.withBody(PrintExceptionUtils.printErrorInfo(ex));
            throw ex;
        } finally {
            //客户端IP
            builder.withClientIp(LocalContextHolder.current().getClientIp())
                    //服务端IP
                    .withServerIp(LocalContextHolder.current().getServerIp())
                    //版本类型
                    .withAppType(LocalContextHolder.current().getAppType())
                    //版本号
                    .withAppVersion(LocalContextHolder.current().getAppVersion())
                    //耗时
                    .withSpentTime(DateComputeUtils.minusMillis(Instant.now(), start))
                    //响应时间
                    .withTriggerTime(DateConvertUtils.format(LocalDateTime.now(), DatePatternInfo.YYYY_MM_DD_HH_MM_SS_SSS));
            //异步线程池记录日志
            PrintLoggerUtils.printThirdParty(builder.build());
            //非servlet上下文移除数据
            LocalContextHolder.unbind();
        }

    }

    @Override
    public int getOrder() {
        return AopOrderInfo.HTTP_CLIENT_INTERCEPTOR;
    }
}
