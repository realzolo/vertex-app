package com.onezol.vertx.framework.security.api.model;

import com.onezol.vertx.framework.security.api.model.dto.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthIdentity {

    private User user;

    private Ticket jwt;

    @Data
    @Builder
    public static class Ticket {

        private String token;

        private Long expire;

    }

}
