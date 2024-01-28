package com.onezol.vertex.framework.common.bean;

import com.onezol.vertex.framework.common.model.pojo.AuthUserModel;
import lombok.experimental.UtilityClass;

public class AuthenticationContext {

    private static final ThreadLocal<AuthUserModel> AUTHENTICATION_THREAD_LOCAL = new ThreadLocal<>();

    private AuthenticationContext() {
    }

    public static void set(AuthUserModel authUserModel) {
        AUTHENTICATION_THREAD_LOCAL.set(authUserModel);
    }

    public static AuthUserModel get() {
        return AUTHENTICATION_THREAD_LOCAL.get();
    }

    public static void clear() {
        AUTHENTICATION_THREAD_LOCAL.remove();
    }

}
