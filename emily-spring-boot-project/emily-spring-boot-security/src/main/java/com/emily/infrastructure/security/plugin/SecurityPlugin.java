package com.emily.infrastructure.security.plugin;

/**
 * 解密插件
 *
 * @author :  Emily
 * @since :  2025/2/7 下午7:45
 */
public interface SecurityPlugin<Q, R> {
    R getPlugin(Q entity, R value);
}
