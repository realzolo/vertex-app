package com.onezol.vertx.framework.security.api.context;

import com.onezol.vertx.framework.security.api.model.UserIdentity;

public class UserIdentityContext {

    private static final ThreadLocal<UserIdentity> THREAD_LOCAL = new InheritableThreadLocal<>();

    private UserIdentityContext() {
    }

    public static void set(UserIdentity userIdentity) {
        THREAD_LOCAL.set(userIdentity);
    }

    public static UserIdentity get() {
        return THREAD_LOCAL.get();
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

}
