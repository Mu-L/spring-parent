package com.emily.infrastructure.transfer.feign;


import com.emily.infrastructure.aop.advisor.AnnotationPointcutAdvisor;
import com.emily.infrastructure.aop.constant.AopOrderInfo;
import com.emily.infrastructure.transfer.feign.interceptor.DefaultFeignMethodInterceptor;
import com.emily.infrastructure.transfer.feign.interceptor.FeignCustomizer;
import com.emily.infrastructure.transfer.feign.interceptor.FeignRequestInterceptor;
import com.emily.infrastructure.transfer.feign.interceptor.FeignResponseInterceptor;
import com.emily.infrastructure.transfer.feign.loadbalancer.FeignLoggerLoadBalancerLifecycle;
import com.emily.infrastructure.transfer.feign.logger.FeignLogger;
import feign.Logger;
import jakarta.annotation.Nonnull;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerDefaultMappingsProviderAutoConfiguration;
import org.springframework.cloud.commons.config.CommonsConfigAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.function.Supplier;

/**
 * 控制器切点配置
 *
 * @author Emily
 * @since 1.0
 */
@AutoConfiguration
@EnableConfigurationProperties(FeignProperties.class)
@ConditionalOnProperty(prefix = FeignProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class FeignAutoConfiguration implements BeanFactoryPostProcessor, InitializingBean, DisposableBean {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FeignAutoConfiguration.class);

    /**
     * 定义接口拦截器切点
     *
     * @param customizers 扩展点对象
     * @return 切面对象
     * @since 1.0
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor feignAdvisor(ObjectProvider<FeignCustomizer> customizers) {
        Assert.isTrue(customizers.orderedStream().findFirst().isPresent(), "Feign拦截器必须存在");
        //限定类|方法级别的切点
        Pointcut pointcut = new AnnotationMatchingPointcut(FeignClient.class, RequestMapping.class, true);
        //切面增强类
        AnnotationPointcutAdvisor advisor = new AnnotationPointcutAdvisor(customizers.orderedStream().findFirst().get(), pointcut);
        //设置增强拦截器执行顺序
        advisor.setOrder(AopOrderInfo.FEIGN);
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DefaultFeignMethodInterceptor defaultFeignMethodInterceptor(ApplicationContext context) {
        return new DefaultFeignMethodInterceptor(context);
    }

    /**
     * Feign 请求日志拦截
     *
     * @return 请求拦截器
     */
    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        Supplier<FeignRequestInterceptor> supplier = FeignRequestInterceptor::new;
        return supplier.get();
    }

    /**
     * 请求响应拦截器
     */
    @Bean
    public FeignResponseInterceptor feignResponseInterceptor() {
        return new FeignResponseInterceptor();
    }

    /**
     * Feign 声明周期管理，主要获取真实URL
     *
     * @return 负载均衡执行前后执行的对象
     */
    @Bean
    public FeignLoggerLoadBalancerLifecycle feignLogLoadBalancerLifecycle() {
        Supplier<FeignLoggerLoadBalancerLifecycle> supplier = FeignLoggerLoadBalancerLifecycle::new;
        return supplier.get();
    }

    /**
     * 自定义日志系统代理feign日志系统
     *
     * @return 日志对象
     */
    @Bean
    public Logger logger() {
        Supplier<Logger> supplier = FeignLogger::new;
        return supplier.get();
    }

    @Override
    public void postProcessBeanFactory(@Nonnull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (beanFactory.containsBeanDefinition(CommonsConfigAutoConfiguration.class.getName())) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(CommonsConfigAutoConfiguration.class.getName());
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
        if (beanFactory.containsBeanDefinition(LoadBalancerDefaultMappingsProviderAutoConfiguration.class.getName())) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(LoadBalancerDefaultMappingsProviderAutoConfiguration.class.getName());
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
        if (beanFactory.containsBeanDefinition("loadBalancerClientsDefaultsMappingsProvider")) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition("loadBalancerClientsDefaultsMappingsProvider");
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
        if (beanFactory.containsBeanDefinition("defaultsBindHandlerAdvisor")) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition("defaultsBindHandlerAdvisor");
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
    }

    @Override
    public void destroy() throws Exception {
        logger.info("<== 【销毁--自动化配置】----Feign拦截器组件【FeignAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("==> 【初始化--自动化配置】----Feign拦截器组件【FeignAutoConfiguration】");
    }
}
