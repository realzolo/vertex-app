package com.onezol.vertex.framework.security.biz.strategy;

import com.onezol.vertex.framework.security.api.model.AuthIdentity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface LoginStrategy {
    
    AuthIdentity login(Object... params) throws AuthenticationException;

    Authentication createAuthentication(Object... params);

}