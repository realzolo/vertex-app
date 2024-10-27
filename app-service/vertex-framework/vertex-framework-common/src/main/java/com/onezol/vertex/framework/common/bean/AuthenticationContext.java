package com.onezol.vertex.framework.common.bean;

import com.onezol.vertex.framework.common.model.AuthUser;

public class AuthenticationContext {

    private static final ThreadLocal<AuthUser> AUTHENTICATION_THREAD_LOCAL = new InheritableThreadLocal<>();

    private AuthenticationContext() {
    }

    public static void set(AuthUser authUser) {
        AUTHENTICATION_THREAD_LOCAL.set(authUser);
    }

    public static AuthUser get() {
        return AUTHENTICATION_THREAD_LOCAL.get();
    }

    public static void clear() {
        AUTHENTICATION_THREAD_LOCAL.remove();
    }

}
