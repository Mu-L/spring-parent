package com.emily.infrastructure.logback.configuration.context;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import com.emily.infrastructure.logback.LogbackProperties;
import com.emily.infrastructure.logback.common.CommonKeys;
import com.emily.infrastructure.logback.configuration.classic.AbstractLogback;
import com.emily.infrastructure.logback.configuration.classic.LogbackGroup;
import com.emily.infrastructure.logback.configuration.classic.LogbackModule;
import com.emily.infrastructure.logback.configuration.classic.LogbackRoot;
import com.emily.infrastructure.logback.configuration.encoder.LogbackPatternLayoutEncoder;
import com.emily.infrastructure.logback.configuration.filter.LogbackFilter;
import com.emily.infrastructure.logback.configuration.policy.AbstractRollingPolicy;
import com.emily.infrastructure.logback.configuration.policy.LogbackFixedWindowRollingPolicy;
import com.emily.infrastructure.logback.configuration.policy.LogbackSizeAndTimeBasedRollingPolicy;
import com.emily.infrastructure.logback.configuration.policy.LogbackTimeBasedRollingPolicy;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 默认容器工厂类
 *
 * @author :  Emily
 * @since :  2024/1/1 9:47 AM
 */
public class LogbackBeanFactory {
    private static final List<AbstractRollingPolicy> POLICIES = new ArrayList<>(3);
    private static final List<AbstractLogback> LOGGERS = new ArrayList<>(3);
    private static final List<LogbackPatternLayoutEncoder> ENCODERS = new ArrayList<>(1);
    private static final List<LogbackFilter> FILTERS = new ArrayList<>(1);

    public static void registerBean(LoggerContext lc, LogbackProperties properties) {
        POLICIES.add(new LogbackSizeAndTimeBasedRollingPolicy(lc, properties));
        POLICIES.add(new LogbackTimeBasedRollingPolicy(lc, properties));
        POLICIES.add(new LogbackFixedWindowRollingPolicy(lc, properties));

        LOGGERS.add(new LogbackGroup(lc, properties));
        LOGGERS.add(new LogbackModule(lc, properties));
        LOGGERS.add(new LogbackRoot(lc, properties));

        ENCODERS.add(new LogbackPatternLayoutEncoder(lc));

        FILTERS.add(new LogbackFilter(lc));
    }

    /**
     * 获取RollingPolicy对象
     *
     * @param appender      文件输出对象
     * @param loggerPath    文件路径
     * @param rollingPolicy 滚动策略对象
     * @return 滚动策略对象
     */
    public static RollingPolicy getRollingPolicy(RollingFileAppender<ILoggingEvent> appender, String loggerPath, LogbackProperties.RollingPolicy rollingPolicy) {
        Optional<AbstractRollingPolicy> policy = POLICIES.stream().filter(l -> l.support(rollingPolicy.getType())).findFirst();
        if (policy.isPresent()) {
            return policy.get().getRollingPolicy(appender, loggerPath);
        }
        throw new IllegalArgumentException("not support rolling policy type: " + rollingPolicy + " , please check your configuration");
    }

    /**
     * 构建Logger对象
     * 日志级别以及优先级排序: OFF > ERROR > WARN > INFO > DEBUG > TRACE >ALL
     *
     * @param commonKeys 属性配置上下文传递类
     */
    public static Logger getLogger(CommonKeys commonKeys) {
        Optional<AbstractLogback> logback = LOGGERS.stream().filter(l -> l.supports(commonKeys.getLogbackType())).findFirst();
        if (logback.isPresent()) {
            return logback.get().getLogger(commonKeys);
        }
        throw new IllegalArgumentException("非法参数");
    }

    /**
     * 获取Encoder对象
     *
     * @param pattern 输出样式
     * @return 编码器对象
     */
    public static PatternLayoutEncoder getEncoder(String pattern) {
        return ENCODERS.get(0).getEncoder(pattern);
    }

    /**
     * 获取Filter对象
     *
     * @return
     */
    public static LogbackFilter getFilter() {
        return FILTERS.get(0);
    }
}
