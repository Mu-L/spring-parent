package com.emily.infrastructure.security.plugin;

/**
 * 解密插件
 *
 * @author :  Emily
 * @since :  2025/2/7 下午7:45
 */
public interface SimpleSecurityPlugin<R> extends BasePlugin {
    R getPlugin(R value);

    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
