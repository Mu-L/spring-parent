package com.emily.infrastructure.logback.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志基础类
 *
 * @author Emily
 * @since 2020/11/14
 */
public class BaseLogger {
    /**
     * 系统编号
     */
    private String systemNumber;
    /**
     * 请求唯一编号
     */
    private String traceId;
    /**
     * 客户端IP
     */
    private String clientIp;
    /**
     * 服务端IP
     */
    private String serverIp;
    /**
     * 版本类型，com.emily.android
     */
    private String appType;
    /**
     * 版本号，4.1.4
     */
    private String appVersion;
    /**
     * 请求URL
     */
    private String url;
    /**
     * 状态码
     */
    private int status;
    /**
     * 描述
     */
    private String message;
    /**
     * 请求参数
     */
    private Map<String, Object> requestParams = new HashMap<>();
    /**
     * 触发时间
     */
    private String triggerTime;
    /**
     * 耗时
     */
    private long spentTime;
    /**
     * 响应结果
     */
    private Object body;

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSystemNumber() {
        return systemNumber;
    }

    public void setSystemNumber(String systemNumber) {
        this.systemNumber = systemNumber;
    }

    public long getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(long spentTime) {
        this.spentTime = spentTime;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Map<String, Object> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BaseLogger systemNumber(String systemNumber) {
        this.systemNumber = systemNumber;
        return this;
    }

    public BaseLogger traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public BaseLogger clientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public BaseLogger serverIp(String serverIp) {
        this.serverIp = serverIp;
        return this;
    }

    public BaseLogger appType(String appType) {
        this.appType = appType;
        return this;
    }

    public BaseLogger appVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public BaseLogger url(String url) {
        this.url = url;
        return this;
    }

    public BaseLogger status(int status) {
        this.status = status;
        return this;
    }

    public BaseLogger message(String message) {
        this.message = message;
        return this;
    }

    public BaseLogger requestParams(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
        return this;
    }

    public BaseLogger requestParams(String key, Object value) {
        this.requestParams.put(key, value);
        return this;
    }

    public BaseLogger triggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
        return this;
    }

    public BaseLogger spentTime(long spentTime) {
        this.spentTime = spentTime;
        return this;
    }

    public BaseLogger body(Object body) {
        this.body = body;
        return this;
    }
}
