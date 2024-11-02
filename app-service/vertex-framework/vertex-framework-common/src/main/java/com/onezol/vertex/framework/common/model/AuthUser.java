package com.onezol.vertex.framework.common.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class AuthUser {

    private Long userId;

    private String username;

    private String nickname;

    private List<LabelValue<String, String>> roles;

    private List<String> permissions;

}
