package com.onezol.vertx.framework.security.biz.strategy;

import com.onezol.vertx.framework.security.api.enumeration.LoginType;
import com.onezol.vertx.framework.security.biz.strategy.core.EmailAuthenticationStrategy;
import com.onezol.vertx.framework.security.biz.strategy.core.UsernamePasswordAuthenticationStrategy;

import java.util.EnumMap;

public class LoginStrategyFactory {

    private static final EnumMap<LoginType, Class<? extends LoginStrategy>> loginStrategyMap = new EnumMap<>(LoginType.class);

    static {
        loginStrategyMap.put(LoginType.UP, UsernamePasswordAuthenticationStrategy.class);
        loginStrategyMap.put(LoginType.EMAIL, EmailAuthenticationStrategy.class);
    }

    public static Class<? extends LoginStrategy> getStrategy(LoginType loginType) {
        return loginStrategyMap.get(loginType);
    }

}
