package com.onezol.vertex.framework.security.biz.handler;

import com.onezol.vertex.framework.common.util.CodecUtils;
import com.onezol.vertex.framework.common.util.JsonUtils;
import com.onezol.vertex.framework.common.util.ServletUtils;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.security.api.service.LoginUserService;
import com.onezol.vertex.framework.support.support.JWTHelper;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import static com.onezol.vertex.framework.common.constant.GenericConstants.AUTHORIZATION_HEADER;

/**
 * 注销成功处理类
 */
@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {

    private final LoginUserService loginUserService;

    public UserLogoutSuccessHandler(@Lazy LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 从请求头中获取token
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(AUTHORIZATION_HEADER)) {
            String token = authorizationHeader.substring(AUTHORIZATION_HEADER.length());
            String subject = JWTHelper.getSubjectFromToken(token);
            assert subject != null;
            Long userId = Long.valueOf(CodecUtils.decodeBase64(subject));

            // 清除Redis缓存
            loginUserService.removeLoginUser(userId);
        }
        // 清除上下文
        SecurityContextHolder.clearContext();

        // 注销成功, 返回成功信息
        String json = JsonUtils.toJsonString(ResponseHelper.buildSuccessfulResponse("注销成功"));
        ServletUtils.writeJSON(response, json);
    }

}