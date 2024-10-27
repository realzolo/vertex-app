package com.onezol.vertex.framework.common.model.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AuthUserModel {

    private Long userId;

    private String userCode;

    private Long agencyCode;

    private String username;

    private String nickname;

    private Set<String> roles;

    private Set<String> permissions;

}
