package com.emily.infrastructure.logback.configuration.context;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.util.StatusListenerConfigHelper;
import ch.qos.logback.core.util.StatusPrinter;
import com.emily.infrastructure.logback.LogbackProperties;

/**
 * -------------------------------------------
 * debug和logback.debug可以在VM Options中配置，示例如下:
 * -Dlogback.debug=true
 * -Ddebug=true
 * -------------------------------------------
 * <p>
 * logback日志全局configuration配置属性解析
 *
 * @author Emily
 * @since :  Created in 2023/7/15 5:32 PM
 */
public class ConfigurationAction extends ContextAwareBase {
    static final String DEBUG_SYSTEM_PROPERTY_KEY = "logback.debug";
    static final String INTERNAL_DEBUG_ATTR = "debug";
    static final String SCAN_ATTR = "scan";
    static final String SCAN_PERIOD_ATTR = "scanPeriod";
    static final String PACKAGING_DATA_ATTR = "packagingData";
    private final LogbackProperties properties;

    public ConfigurationAction(LoggerContext lc, LogbackProperties properties) {
        this.context = lc;
        this.properties = properties;
    }

    /**
     * 1.控制是否报告logback内部状态信息
     * 2.控制是否开启debug模式
     */
    public void start() {
        if (Boolean.getBoolean(DEBUG_SYSTEM_PROPERTY_KEY) || Boolean.getBoolean(INTERNAL_DEBUG_ATTR) || properties.isDebug()) {
            //是否报告logback内部状态信息
            StatusPrinter.print(context);
            //开启内部debug模式
            StatusListenerConfigHelper.addOnConsoleListenerInstance(context, new OnConsoleStatusListener());
        } else {
            addInfo(INTERNAL_DEBUG_ATTR + " attribute not set");
        }
        if (properties.isPackagingData()) {
            ((LoggerContext) context).setPackagingDataEnabled(true);
        } else {
            ((LoggerContext) context).setPackagingDataEnabled(false);
        }
    }

}
