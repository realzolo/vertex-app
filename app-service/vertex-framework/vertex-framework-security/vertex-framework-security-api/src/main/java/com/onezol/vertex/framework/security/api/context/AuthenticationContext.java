package com.onezol.vertex.framework.security.api.context;

import com.onezol.vertex.framework.security.api.model.UserIdentity;

public class AuthenticationContext {

    private static final ThreadLocal<UserIdentity> AUTHENTICATION_THREAD_LOCAL = new InheritableThreadLocal<>();

    private AuthenticationContext() {
    }

    public static void set(UserIdentity userIdentity) {
        AUTHENTICATION_THREAD_LOCAL.set(userIdentity);
    }

    public static UserIdentity get() {
        return AUTHENTICATION_THREAD_LOCAL.get();
    }

    public static void clear() {
        AUTHENTICATION_THREAD_LOCAL.remove();
    }

}
