package com.onezol.vertex.framework.security.api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthUser {

    private Long userId;

    private String username;

    private String nickname;

    private SimpleDepartment department;

    private List<SimpleRole> roles;

    private List<String> permissions;

}
