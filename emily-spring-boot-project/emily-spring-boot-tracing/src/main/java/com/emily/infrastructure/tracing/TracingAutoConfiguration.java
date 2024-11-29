package com.emily.infrastructure.tracing;

import com.emily.infrastructure.tracing.helper.SystemNumberHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;

/**
 * 全链路追踪上下文自动化配置
 *
 * @author Emily
 * @since 2021/11/27
 */
@AutoConfiguration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(TracingProperties.class)
@ConditionalOnProperty(prefix = TracingProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class TracingAutoConfiguration implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(TracingAutoConfiguration.class);

    public TracingAutoConfiguration(TracingProperties properties) {
        SystemNumberHelper.setSystemNumber(properties.getSystemNumber());
    }

    @Override
    public void destroy() {
        logger.info("<== 【销毁--自动化配置】----全链路日志追踪组件【TracingAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("==> 【初始化--自动化配置】----全链路日志追踪组件【TracingAutoConfiguration】");
    }
}
