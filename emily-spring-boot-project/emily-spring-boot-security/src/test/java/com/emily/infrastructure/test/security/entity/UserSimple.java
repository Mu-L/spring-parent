package com.emily.infrastructure.test.security.entity;

import com.emily.infrastructure.security.annotation.SecurityModel;
import com.emily.infrastructure.security.annotation.SecurityProperty;
import com.emily.infrastructure.test.security.plugin.UserSimpleEncryptionPlugin;

/**
 * @author :  Emily
 * @since :  2025/2/8 下午4:32
 */
@SecurityModel
public class UserSimple {
    @SecurityProperty(value = UserSimpleEncryptionPlugin.class)
    private String username;
    @SecurityProperty(value = UserSimpleEncryptionPlugin.class)
    private String password;
    private int age;
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
