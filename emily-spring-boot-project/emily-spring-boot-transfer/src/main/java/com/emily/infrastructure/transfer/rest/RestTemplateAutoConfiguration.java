package com.emily.infrastructure.transfer.rest;

import com.emily.infrastructure.aop.advisor.AnnotationPointcutAdvisor;
import com.emily.infrastructure.aop.constant.AopOrderInfo;
import com.emily.infrastructure.logback.factory.LoggerFactory;
import com.emily.infrastructure.transfer.rest.annotation.TargetHttpTimeout;
import com.emily.infrastructure.transfer.rest.factory.HttpContextFactory;
import com.emily.infrastructure.transfer.rest.handler.CustomResponseErrorHandler;
import com.emily.infrastructure.transfer.rest.interceptor.client.DefaultRestTemplateInterceptor;
import com.emily.infrastructure.transfer.rest.interceptor.client.RestTemplateCustomizer;
import com.emily.infrastructure.transfer.rest.interceptor.timeout.DefaultRestTemplateTimeoutMethodInterceptor;
import com.emily.infrastructure.transfer.rest.interceptor.timeout.RestTemplateTimeoutCustomizer;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 将RestTemplate加入容器
 *
 * @author Emily
 * @see <a href="https://spring.io/blog/2023/06/07/securing-spring-boot-applications-with-ssl">官方文档案例</a>
 * @see <a href="https://github.com/scottfrederick/spring-boot-ssl-demo/tree/main">案例代码</a>
 * @see <a href="https://www.baeldung.com/spring-boot-security-ssl-bundles">证书生成</a>
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.ssl">SSL官方文档</a>
 * @since 5.0.2
 */
@AutoConfiguration
@ConditionalOnClass(RestTemplate.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(RestTemplateProperties.class)
@ConditionalOnProperty(prefix = RestTemplateProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class RestTemplateAutoConfiguration implements BeanFactoryPostProcessor, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateAutoConfiguration.class);

    @Primary
    @Bean("restTemplate")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RestTemplate restTemplate(ObjectProvider<RestTemplateCustomizer> customizers, ClientHttpRequestFactory clientHttpRequestFactory) {
        Assert.isTrue(customizers.orderedStream().findFirst().isPresent(), "拦截器不可以为空");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(clientHttpRequestFactory));
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        restTemplate.setInterceptors(List.of(customizers.orderedStream().findFirst().get()));
        return restTemplate;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean
    public DefaultRestTemplateInterceptor defaultRestTemplateInterceptor(ApplicationContext context) {
        return new DefaultRestTemplateInterceptor(context);
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ClientHttpRequestFactory clientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        //设置HTTP进程执行状态工厂类（设置单个请求超时时间）
        factory.setHttpContextFactory(new HttpContextFactory());
        return factory;
    }

    @Bean("httpClient")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public HttpClient httpClient(RestTemplateProperties properties) {
        //构造函数内明确支持http、https
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        //指定连接池中的最大连接数，默认：25
        connectionManager.setMaxTotal(25);
        //指定每个HTTP路由的最大连接数（所有指向同一主机的请求可以使用相同的连接路由），默认：5
        connectionManager.setDefaultMaxPerRoute(5);

        RequestConfig requestConfig = RequestConfig.custom()
                // 响应超时时间
                .setResponseTimeout(properties.getReadTimeOut().getSeconds(), TimeUnit.SECONDS)
                //从连接池中获取连接的超时时间
                .setConnectionRequestTimeout(properties.getConnectTimeOut().getSeconds(), TimeUnit.SECONDS)
                .build();

        return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }

    /**
     * RestTemplate请求超时切面（单个请求）
     *
     * @return 切面对象
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor httpTimeoutPointCutAdvice(ObjectProvider<RestTemplateTimeoutCustomizer> customizers) {
        Assert.isTrue(customizers.orderedStream().findFirst().isPresent(), "拦截器不可为空");
        //限定方法级别的切点
        Pointcut mpc = new AnnotationMatchingPointcut(null, TargetHttpTimeout.class, false);
        //组合切面(并集)，即只要有一个切点的条件符合，则就拦截
        Pointcut pointcut = new ComposablePointcut(mpc);
        //切面增强类
        AnnotationPointcutAdvisor advisor = new AnnotationPointcutAdvisor(customizers.orderedStream().findFirst().get(), pointcut);
        //切面优先级顺序
        advisor.setOrder(AopOrderInfo.REST);
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean
    public RestTemplateTimeoutCustomizer httpTimeoutCustomizer() {
        return new DefaultRestTemplateTimeoutMethodInterceptor();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanNames = beanFactory.getBeanNamesForType(RestTemplateProperties.class);
        if (beanNames.length > 0 && beanFactory.containsBeanDefinition(beanNames[0])) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanNames[0]);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
    }

    @Override
    public void destroy() {
        logger.info("<== 【销毁--自动化配置】----RestTemplate组件【RestTemplateAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("==> 【初始化--自动化配置】----RestTemplate组件【RestTemplateAutoConfiguration】");
    }
}


