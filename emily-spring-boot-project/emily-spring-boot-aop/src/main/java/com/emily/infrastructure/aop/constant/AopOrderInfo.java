package com.emily.infrastructure.aop.constant;

/**
 * 定义优先级顺序
 *
 * @author Emily
 * @since 2020/3/23
 */
public class AopOrderInfo {
    /**
     * API请求切面
     */
    public static final int REQUEST = 400;
    /**
     * API请求拦截器
     */
    public static final int REQUEST_INTERCEPTOR = 410;
    /**
     * api脱敏请求切面
     */
    public static final int DESENSITIZE = 500;
    /**
     * api返回值脱敏
     */
    public static final int DESENSITIZE_INTERCEPTOR = 510;
    /**
     * 多语言翻译拦截器
     */
    public static final int I18N = 500;
    /**
     * 链路日志追踪
     */
    public static final int TRACING = 600;
    /**
     * feign正常日志
     */
    public static final int FEIGN = 800;
    /**
     * Mybatis日志漆面
     */
    public static final int MYBATIS = 850;
    /**
     * MYBATIS拦截器
     */
    public static final int MYBATIS_INTERCEPTOR = 852;
    /**
     * 数据源切面
     */
    public static final int DATASOURCE = 900;
    /**
     * 数据库AOP切面拦截器
     */
    public static final int DATASOURCE_INTERCEPTOR = 910;
    /**
     * RestTemplate请求超时设置拦截器
     */
    public static final int HTTP_CLIENT = 1000;
    /**
     * RestTemplate请求拦截器优先级
     */
    public static final int HTTP_CLIENT_INTERCEPTOR = 1100;
    /**
     * Feign日志拦截器优先级顺序
     */
    public static final int FEIGN_INTERCEPTOR = 1100;
    /**
     * 限流切面拦截器
     */
    public static final int RATE_LIMITER = 1200;

}
