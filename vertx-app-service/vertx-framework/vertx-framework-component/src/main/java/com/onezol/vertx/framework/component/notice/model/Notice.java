package com.onezol.vertx.framework.component.notice.model;

import com.onezol.vertx.framework.common.model.DataPairRecord;
import com.onezol.vertx.framework.common.skeleton.model.BaseDTO;
import com.onezol.vertx.framework.security.api.model.dto.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Notice extends BaseDTO {

    @Schema(name = "标题")
    private String title;

    @Schema(name = "内容")
    private String content;

    @Schema(name = "类型")
    private Integer type;

    @Schema(name = "生效时间")
    private LocalDateTime effectiveTime;

    @Schema(name = "终止时间")
    private LocalDateTime terminateTime;

    @Schema(name = "通知范围")
    private Integer noticeScope;

    @Schema(name = "通知用户")
    private List<User> noticeUsers;

    @Schema(name = "状态")
    private Integer status;

    @Schema(name = "发布人")
    private DataPairRecord publisher;

}
