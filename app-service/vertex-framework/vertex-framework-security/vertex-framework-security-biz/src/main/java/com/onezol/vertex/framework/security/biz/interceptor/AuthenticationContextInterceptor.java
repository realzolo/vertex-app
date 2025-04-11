package com.onezol.vertex.framework.security.biz.interceptor;

import com.onezol.vertex.framework.security.api.context.AuthenticationContext;
import com.onezol.vertex.framework.security.api.model.dto.AuthUser;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.pojo.LoginUser;
import lombok.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import static com.onezol.vertex.framework.support.interceptor.RequestInterceptionOrder.AUTHENTICATION_CONTEXT_INTERCEPTOR_ORDER;

/**
 * 认证上下文拦截器<br>
 * 用于将当前登录用户信息放入上下文中
 */
@Component
@Order(AUTHENTICATION_CONTEXT_INTERCEPTOR_ORDER)
public class AuthenticationContextInterceptor implements WebRequestInterceptor {

    @Override
    public void preHandle(@NonNull WebRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof LoginUser loginUser) {
            User user = BeanUtils.toBean(loginUser.getDetails(), User.class);

            AuthUser model = AuthUser.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .roles(user.getRoles())
                    .permissions(user.getPermissions())
                    .build();
            AuthenticationContext.set(model);
        }
    }

    @Override
    public void postHandle(@NonNull WebRequest request, ModelMap model) {
    }

    @Override
    public void afterCompletion(@NonNull WebRequest request, Exception ex) {
        AuthenticationContext.clear();
    }

}
