package com.onezol.vertex.framework.security.api.model.vo;

import com.onezol.vertex.framework.common.model.vo.VO;
import com.onezol.vertex.framework.security.api.model.dto.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthenticationVO implements VO {

    private User user;

    private UserAuthenticationJWT jwt;

    @Data
    @Builder
    public static class UserAuthenticationJWT {

        private String token;

        private Long expire;

    }

}
