package com.onezol.vertex.framework.security.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OnlineUser {

    private Long id;

    private Long userId;

    private String code;

    private Long orgCode;

    private String username;

    private String nickname;

    private String avatar;

    private String ip;

    private String browser;

    private String os;

    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime loginTime;

    private String onlineTime;

}
