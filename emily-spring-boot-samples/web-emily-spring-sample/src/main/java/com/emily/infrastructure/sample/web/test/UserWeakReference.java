package com.emily.infrastructure.sample.web.test;

import com.emily.infrastructure.sample.web.entity.User;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * @author Emily
 * @program: spring-parent
 * 弱引用
 * @since 2021/11/13
 */
public class UserWeakReference extends WeakReference<User> {

    public UserWeakReference(User user) {
        super(user);
    }

    public UserWeakReference(User user, ReferenceQueue<? super User> q) {
        super(user, q);
    }
}
