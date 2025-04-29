package com.onezol.vertex.framework.security.api.model;

import com.onezol.vertex.framework.common.model.DataPairRecord;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserIdentity {

    private Long userId;

    private String username;

    private String nickname;

    private DataPairRecord department;

    private List<DataPairRecord> roles;

    private List<String> permissions;

}
