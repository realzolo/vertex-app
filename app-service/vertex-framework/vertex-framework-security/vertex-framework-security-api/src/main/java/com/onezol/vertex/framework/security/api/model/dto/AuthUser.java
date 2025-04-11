package com.onezol.vertex.framework.security.api.model.dto;

import com.onezol.vertex.framework.common.model.LabelValue;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthUser {

    private Long userId;

    private String username;

    private String nickname;

    private List<LabelValue<String, String>> roles;

    private List<String> permissions;

}
