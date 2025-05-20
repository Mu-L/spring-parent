package com.emily.infrastructure.logback.configuration.classic;

import ch.qos.logback.classic.Logger;
import com.emily.infrastructure.logback.common.CommonKeys;
import com.emily.infrastructure.logback.configuration.type.LogbackType;

/**
 * 日志实现抽象类
 *
 * @author Emily
 * @since : 2021/12/17
 */
public abstract class AbstractLogback {
    /**
     * 判定是否支持指定的日志类型
     *
     * @param logbackType 日志类型
     * @return true-支持，false-不支持
     */
    public boolean supports(LogbackType logbackType) {
        return false;
    }

    /**
     * 获取Logger对象
     *
     * @param commonKeys appender属性名
     * @return 返回Logger日志对象
     */
    public abstract Logger getLogger(CommonKeys commonKeys);
}
