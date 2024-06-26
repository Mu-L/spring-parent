package com.emily.infrastructure.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置文件
 *
 * @author Emily
 * @since 2020/05/14
 */
@ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
public class DataSourceProperties {
    /**
     * 前缀
     */
    public static final String PREFIX = "spring.emily.datasource";
    /**
     * 是否开启数据源组件, 默认：true
     */
    private boolean enabled = true;
    /**
     * 默认数据源配置
     */
    private String defaultConfig;
    /**
     * 是否拦截超类或者接口中的方法，默认：true
     */
    private boolean checkInherited = true;
    /**
     * 是否对默认数据源执行宽松回退，即：当目标数据源找不到时回退到默认数据源，默认：true
     */
    private boolean lenientFallback = true;
    /**
     * Druid数据库连接池多数据源配置
     */
    private Map<String, DruidDataSource> druid;
    /**
     * Hikari数据库连接池多数据源配置
     */
    private Map<String, HikariDataSource> hikari;
    /**
     * JNDI数据源
     */
    private Map<String, String> jndi;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public boolean isCheckInherited() {
        return checkInherited;
    }

    public void setCheckInherited(boolean checkInherited) {
        this.checkInherited = checkInherited;
    }

    public boolean isLenientFallback() {
        return lenientFallback;
    }

    public void setLenientFallback(boolean lenientFallback) {
        this.lenientFallback = lenientFallback;
    }

    public Map<String, DruidDataSource> getDruid() {
        return druid;
    }

    public void setDruid(Map<String, DruidDataSource> druid) {
        this.druid = druid;
    }

    public Map<String, HikariDataSource> getHikari() {
        return hikari;
    }

    public void setHikari(Map<String, HikariDataSource> hikari) {
        this.hikari = hikari;
    }

    public Map<String, String> getJndi() {
        return jndi;
    }

    public void setJndi(Map<String, String> jndi) {
        this.jndi = jndi;
    }

    /**
     * 获取合并后的目标数据源配置
     *
     * @return 数据源映射
     */
    public Map<Object, Object> getTargetDataSources() {
        Map<Object, Object> dsMap = new HashMap<>(5);
        if (!CollectionUtils.isEmpty(this.druid)) {
            dsMap.putAll(this.druid);
        }
        if (!CollectionUtils.isEmpty(this.hikari)) {
            dsMap.putAll(this.hikari);
        }
        if (!CollectionUtils.isEmpty(this.jndi)) {
            dsMap.putAll(this.jndi);
        }
        return Collections.unmodifiableMap(dsMap);
    }

    /**
     * 获取默认数据源
     *
     * @return 默认数据源
     */
    public Object getDefaultDataSource() {
        return getTargetDataSources().get(this.defaultConfig);
    }
}
