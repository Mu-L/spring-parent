package com.emily.cloud.test.fegin;


import com.emily.infrastructure.sensitive.SensitiveType;
import com.emily.infrastructure.sensitive.annotation.JsonSimField;

/**
 * @author Emily
 * @since Created in 2022/10/27 3:29 下午
 */
public class Custom {
    @JsonSimField
    private String username;
    @JsonSimField(SensitiveType.EMAIL)
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
