package com.onezol.vertex.framework.security.api.model.dto;

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
