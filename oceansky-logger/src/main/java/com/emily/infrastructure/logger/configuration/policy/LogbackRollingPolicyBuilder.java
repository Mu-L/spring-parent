package com.emily.infrastructure.logger.configuration.policy;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import com.emily.infrastructure.logger.common.StrUtils;
import com.emily.infrastructure.logger.configuration.property.LoggerProperties;
import com.emily.infrastructure.logger.configuration.type.RollingPolicyType;

/**
 * logback归档策略
 *
 * @author Emily
 * @since : 2022/01/10
 */
public class LogbackRollingPolicyBuilder {
    private final Context context;
    private final LoggerProperties.RollingPolicy rollingPolicy;

    private LogbackRollingPolicyBuilder(Context context, LoggerProperties.RollingPolicy rollingPolicy) {
        this.context = context;
        this.rollingPolicy = rollingPolicy;
    }

    /**
     * 获取指定归档文件策略类型的归档策略
     *
     * @param appender   归档文件appender
     * @param loggerPath 日志文件路径
     * @return 策略
     */
    public RollingPolicy build(RollingFileAppender<ILoggingEvent> appender, String loggerPath) {
        if (RollingPolicyType.SIZE_AND_TIME_BASED.equals(rollingPolicy.getType())) {
            return getSizeAndTimeBasedRollingPolicy(appender, loggerPath);
        } else {
            return getTimeBasedRollingPolicy(appender, loggerPath);
        }
    }

    /**
     * 获取基于时间的文件归档策略
     *
     * @param appender   归档文件appender
     * @param loggerPath 日志文件路径
     * @return 基于时间的滚动策略
     */
    RollingPolicy getTimeBasedRollingPolicy(RollingFileAppender<ILoggingEvent> appender, String loggerPath) {
        //文件归档大小和时间设置
        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
        //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        policy.setContext(context);
        /**
         * 归档文件名格式设置
         * 将文件名及路径字符串编译为字符串
         http://www.logback.cn/04%E7%AC%AC%E5%9B%9B%E7%AB%A0Appenders.html
         /info/foo.%d 每天归档
         /info/%d{yyyy/MM}/foo.txt 每个月开始的时候归档
         /info/foo.%d{yyyy-ww}.log 每个周的第一天开始归档
         /info/foo%d{yyyy-MM-dd_HH}.log 每小时归档
         /info/foo%d{yyyy-MM-dd_HH-mm}.log 每分钟归档
         /info/info.%d 每天轮转
         */
        String fp = StrUtils.substVars(context, loggerPath, "%d{yyyy-MM-dd}.log");
        //设置文件名模式，支持对文件进行压缩ZIP、GZ
        policy.setFileNamePattern(StrUtils.join(fp, rollingPolicy.getCompressionMode().getSuffix()));
        //设置要保留的最大存档文件数
        policy.setMaxHistory(rollingPolicy.getMaxHistory());
        //控制所有归档文件总大小 KB、MB、GB，默认:0
        policy.setTotalSizeCap(FileSize.valueOf(rollingPolicy.getTotalSizeCap()));
        //是否在应用程序启动时删除存档，默认：false
        policy.setCleanHistoryOnStart(rollingPolicy.isCleanHistoryOnStart());
        //设置父节点是appender
        policy.setParent(appender);
        //添加内部状态
        policy.addInfo("Build TimeBasedRollingPolicy Success");
        policy.start();
        return policy;
    }

    /**
     * 获取基于时间和大小的日志文件归档策略
     *
     * @param appender   归档文件appender
     * @param loggerPath 日志文件路径
     * @return 基于时间和大小的策略
     */
    RollingPolicy getSizeAndTimeBasedRollingPolicy(RollingFileAppender<ILoggingEvent> appender, String loggerPath) {
        //文件归档大小和时间设置
        SizeAndTimeBasedRollingPolicy<ILoggingEvent> policy = new SizeAndTimeBasedRollingPolicy<>();
        //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        policy.setContext(context);
        /**
         * 归档文件名格式设置
         * 将文件名及路径字符串编译为字符串
         http://www.logback.cn/04%E7%AC%AC%E5%9B%9B%E7%AB%A0Appenders.html
         /info/foo.%d 每天归档
         /info/%d{yyyy/MM}/foo.txt 每个月开始的时候归档
         /info/foo.%d{yyyy-ww}.log 每个周的第一天开始归档
         /info/foo%d{yyyy-MM-dd_HH}.log 每小时归档
         /info/foo%d{yyyy-MM-dd_HH-mm}.log 每分钟归档
         /info/info.%d 每天轮转
         */
        String fp = StrUtils.substVars(context, loggerPath, ".%d{yyyy-MM-dd}.%i.log");
        //设置文件名模式，支持对文件进行压缩ZIP、GZ
        policy.setFileNamePattern(StrUtils.join(fp, rollingPolicy.getCompressionMode().getSuffix()));
        //最大日志文件大小 KB,MB,GB
        policy.setMaxFileSize(FileSize.valueOf(rollingPolicy.getMaxFileSize()));
        //设置要保留的最大存档文件数
        policy.setMaxHistory(rollingPolicy.getMaxHistory());
        //文件总大小限制 KB,MB,G
        policy.setTotalSizeCap(FileSize.valueOf(rollingPolicy.getTotalSizeCap()));
        //是否在应用程序启动时删除存档，默认：false
        policy.setCleanHistoryOnStart(rollingPolicy.isCleanHistoryOnStart());
        //设置父节点是appender
        policy.setParent(appender);
        //添加内部状态
        policy.addInfo("Build SizeAndTimeBasedRollingPolicy Policy Success");
        policy.start();
        return policy;
    }

    public static LogbackRollingPolicyBuilder create(Context context, LoggerProperties.RollingPolicy rollingPolicy) {
        return new LogbackRollingPolicyBuilder(context, rollingPolicy);
    }
}