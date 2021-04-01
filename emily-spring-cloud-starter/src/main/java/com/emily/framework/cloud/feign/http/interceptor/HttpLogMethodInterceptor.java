package com.emily.framework.cloud.feign.http.interceptor;

import com.emily.framework.cloud.feign.http.common.FeignLogUtils;
import com.emily.framework.context.apilog.po.AsyncLogAop;
import com.emily.framework.context.apilog.service.AsyncLogAopService;
import feign.RequestTemplate;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Description: 在接口到达具体的目标即控制器方法之前获取方法的调用权限，可以在接口方法之前或者之后做Advice(增强)处理
 * @Version: 1.0
 */
public class HttpLogMethodInterceptor implements MethodInterceptor {

    private AsyncLogAopService asyncLogAopService;
    private RequestTemplate requestTemplate;

    public HttpLogMethodInterceptor(AsyncLogAopService asyncLogAopService) {
        this.asyncLogAopService = asyncLogAopService;
    }

    /**
     * 拦截接口日志
     *
     * @param invocation 接口方法切面连接点
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //调用真实的action方法
        Object result = invocation.proceed();
        //封装异步日志信息
        AsyncLogAop asyncLog = FeignLogUtils.getAsyncLogAop();
        //响应结果
        asyncLog.setResponseBody(result);

        //异步记录接口响应信息
        asyncLogAopService.traceResponse(asyncLog);

        return result;

    }

}
