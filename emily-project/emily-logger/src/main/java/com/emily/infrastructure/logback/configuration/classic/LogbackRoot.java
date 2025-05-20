package com.emily.infrastructure.logback.configuration.classic;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.emily.infrastructure.logback.LogbackProperties;
import com.emily.infrastructure.logback.common.CommonKeys;
import com.emily.infrastructure.logback.configuration.appender.AbstractAppender;
import com.emily.infrastructure.logback.configuration.appender.LogbackAsyncAppender;
import com.emily.infrastructure.logback.configuration.appender.LogbackConsoleAppender;
import com.emily.infrastructure.logback.configuration.appender.LogbackRollingFileAppender;
import com.emily.infrastructure.logback.configuration.type.LogbackType;

/**
 * 日志组件抽象类
 *
 * @author Emily
 * @since : 2021/07/08
 */
public class LogbackRoot extends AbstractLogback {
    private final LoggerContext lc;
    private final LogbackProperties properties;

    public LogbackRoot(LoggerContext lc, LogbackProperties properties) {
        this.lc = lc;
        this.properties = properties;
    }

    @Override
    public boolean supports(LogbackType logbackType) {
        return LogbackType.ROOT == logbackType;
    }

    /**
     * 构建RootLogger对象，需在配置类中主动调用进行初始化
     * 日志级别以及优先级排序: OFF &gt; ERROR &gt; WARN &gt; INFO &gt; DEBUG &gt; TRACE &gt;ALL
     */
    @Override
    public Logger getLogger(CommonKeys commonKeys) {
        // 获取logger对象
        Logger logger = lc.getLogger(commonKeys.getLoggerName());
        //设置是否向上级打印信息
        logger.setAdditive(false);
        // 设置日志级别
        logger.setLevel(Level.toLevel(properties.getRoot().getLevel().toString()));
        // appender对象
        AbstractAppender appender = LogbackRollingFileAppender.create(lc, properties, commonKeys);
        // 是否开启异步日志
        if (properties.getAppender().getAsync().isEnabled()) {
            //异步appender
            LogbackAsyncAppender asyncAppender = LogbackAsyncAppender.create(lc, properties);
            if (logger.getLevel().levelInt <= Level.ERROR_INT) {
                logger.addAppender(asyncAppender.getAppender(appender.build(Level.ERROR)));
            }
            if (logger.getLevel().levelInt <= Level.WARN_INT) {
                logger.addAppender(asyncAppender.getAppender(appender.build(Level.WARN)));
            }
            if (logger.getLevel().levelInt <= Level.INFO_INT) {
                logger.addAppender(asyncAppender.getAppender(appender.build(Level.INFO)));
            }
            if (logger.getLevel().levelInt <= Level.DEBUG_INT) {
                logger.addAppender(asyncAppender.getAppender(appender.build(Level.DEBUG)));
            }
            if (logger.getLevel().levelInt <= Level.TRACE_INT) {
                logger.addAppender(asyncAppender.getAppender(appender.build(Level.TRACE)));
            }
        } else {
            if (logger.getLevel().levelInt <= Level.ERROR_INT) {
                logger.addAppender(appender.build(Level.ERROR));
            }
            if (logger.getLevel().levelInt <= Level.WARN_INT) {
                logger.addAppender(appender.build(Level.WARN));
            }
            if (logger.getLevel().levelInt <= Level.INFO_INT) {
                logger.addAppender(appender.build(Level.INFO));
            }
            if (logger.getLevel().levelInt <= Level.DEBUG_INT) {
                logger.addAppender(appender.build(Level.DEBUG));
            }
            if (logger.getLevel().levelInt <= Level.TRACE_INT) {
                logger.addAppender(appender.build(Level.TRACE));
            }
        }
        if (properties.getRoot().isConsole()) {
            //移除console控制台appender
            logger.detachAppender(LogbackConsoleAppender.CONSOLE);
            //基于springboot默认初始化的appender name默认大写
            logger.detachAppender(LogbackConsoleAppender.CONSOLE.toUpperCase());
            // 添加控制台appender
            logger.addAppender(LogbackConsoleAppender.create(lc, properties).build(logger.getLevel()));
        } else {
            //移除console控制台appender
            logger.detachAppender(LogbackConsoleAppender.CONSOLE);
            //基于springboot默认初始化appender name默认大写
            logger.detachAppender(LogbackConsoleAppender.CONSOLE.toUpperCase());
        }
        return logger;
    }
}
