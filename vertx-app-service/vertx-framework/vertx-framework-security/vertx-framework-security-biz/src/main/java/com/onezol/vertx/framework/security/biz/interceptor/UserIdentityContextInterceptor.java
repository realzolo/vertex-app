package com.onezol.vertx.framework.security.biz.interceptor;

import com.onezol.vertx.framework.security.api.context.UserIdentityContext;
import com.onezol.vertx.framework.security.api.model.LoginUserDetails;
import com.onezol.vertx.framework.security.api.model.UserIdentity;
import lombok.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import static com.onezol.vertx.framework.support.interceptor.RequestInterceptionOrder.USER_IDENTITY_CONTEXT_INTERCEPTOR_ORDER;

/**
 * 用户身份上下文拦截器</br>
 * 用于将当前登录用户信息放入上下文中
 */
@Component
@Order(USER_IDENTITY_CONTEXT_INTERCEPTOR_ORDER)
public class UserIdentityContextInterceptor implements WebRequestInterceptor {

    @Override
    public void preHandle(@NonNull WebRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof LoginUserDetails loginUserDetails) {
            UserIdentity identity = UserIdentity.builder()
                    .userId(loginUserDetails.getId())
                    .username(loginUserDetails.getUsername())
                    .nickname(loginUserDetails.getNickname())
                    .department(loginUserDetails.getDepartment())
                    .roles(loginUserDetails.getRoles())
                    .permissions(loginUserDetails.getPermissions())
                    .build();
            UserIdentityContext.set(identity);
        }
    }

    @Override
    public void postHandle(@NonNull WebRequest request, ModelMap model) {
    }

    @Override
    public void afterCompletion(@NonNull WebRequest request, Exception ex) {
        UserIdentityContext.clear();
    }

}
