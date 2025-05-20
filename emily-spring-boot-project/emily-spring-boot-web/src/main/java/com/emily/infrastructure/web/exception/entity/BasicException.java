package com.emily.infrastructure.web.exception.entity;


import com.emily.infrastructure.web.response.enums.ApplicationStatus;

/**
 * 业务异常
 *
 * @author Emily
 * @since 1.0
 */
public class BasicException extends RuntimeException {
    /**
     * 状态码
     */
    private int status;
    /**
     * 异常信息
     */
    private String message;
    /**
     * 是否是错误信息，默认：true
     */
    private boolean error = true;

    public BasicException() {
    }

    public BasicException(ApplicationStatus applicationStatus) {
        super(applicationStatus.getMessage());
        this.status = applicationStatus.getStatus();
        this.message = applicationStatus.getMessage();
    }

    public BasicException(int status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public BasicException(int status, String message, boolean error) {
        this(status, message);
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
