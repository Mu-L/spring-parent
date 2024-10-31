package com.emily.infrastructure.web.exception;

import com.emily.infrastructure.language.convert.I18nCache;
import com.emily.infrastructure.logback.factory.LoggerFactory;
import com.emily.infrastructure.web.exception.handler.DefaultGlobalExceptionHandler;
import com.emily.infrastructure.web.exception.handler.GlobalExceptionCustomizer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;


/**
 * 异常捕获自动化配置类
 *
 * @author Emily
 * @since 2020/10/28
 */
@AutoConfiguration
@EnableConfigurationProperties(GlobalExceptionProperties.class)
@ConditionalOnProperty(prefix = GlobalExceptionProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class GlobalExceptionAutoConfiguration implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionAutoConfiguration.class);

    /**
     * 初始化异常多语言
     */
    @PostConstruct
    public void init() {
        I18nCache.bindEn("网络异常，请稍后再试", "Network exception, please try again later");
        I18nCache.bindEn("方法不允许", "Method Not Allowed");
        I18nCache.bindEn("非法参数", "Illegal parameter");
        I18nCache.bindEn("非法数据", "invalid data");
        I18nCache.bindEn("非法访问", "Illegal access");
        I18nCache.bindEn("非法代理", "Illegal agency");
    }

    /**
     * 异常抛出拦截bean初始化
     *
     * @return 全局异常捕获切面对象
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(GlobalExceptionCustomizer.class)
    public DefaultGlobalExceptionHandler defaultGlobalExceptionHandler() {
        return new DefaultGlobalExceptionHandler();
    }


    @Override
    public void destroy() throws Exception {
        logger.info("<== 【销毁--自动化配置】---全局异常组件【GlobalExceptionAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("==> 【初始化--自动化配置】---全局异常组件【GlobalExceptionAutoConfiguration】");
    }
}
