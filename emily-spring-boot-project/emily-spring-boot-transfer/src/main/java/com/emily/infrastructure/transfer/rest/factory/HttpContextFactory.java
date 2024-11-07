package com.emily.infrastructure.transfer.rest.factory;

import com.emily.infrastructure.transfer.rest.context.RestTemplateContextHolder;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.Objects;
import java.util.function.BiFunction;


/**
 * 自定义HttpContext HTTP进程执行状态，它是一种可用于将属性名称映射到属性值的结构
 *
 * @author Emily
 * @since 4.1.3
 */

public class HttpContextFactory implements BiFunction<HttpMethod, URI, HttpContext> {
    @Override
    public HttpContext apply(HttpMethod httpMethod, URI uri) {
        RequestConfig requestConfig = RestTemplateContextHolder.current();
        if (Objects.nonNull(requestConfig)) {
            HttpContext context = HttpClientContext.create();
            context.setAttribute(HttpClientContext.REQUEST_CONFIG, requestConfig);
            return context;
        }
        return null;
    }
}

